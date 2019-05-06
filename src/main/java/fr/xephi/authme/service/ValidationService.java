package fr.xephi.authme.service;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.initialization.Reloadable;
import fr.xephi.authme.libs.google.common.collect.HashMultimap;
import fr.xephi.authme.libs.google.common.collect.Multimap;
import fr.xephi.authme.libs.jalu.configme.properties.Property;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.message.MessageKey;
import fr.xephi.authme.permission.PermissionsManager;
import fr.xephi.authme.permission.PlayerStatePermission;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.settings.properties.EmailSettings;
import fr.xephi.authme.settings.properties.ProtectionSettings;
import fr.xephi.authme.settings.properties.RestrictionSettings;
import fr.xephi.authme.settings.properties.SecuritySettings;
import fr.xephi.authme.util.PlayerUtils;
import fr.xephi.authme.util.StringUtils;
import fr.xephi.authme.util.Utils;
import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ValidationService
  implements Reloadable
{
  @Inject
  private Settings settings;
  @Inject
  private DataSource dataSource;
  @Inject
  private PermissionsManager permissionsManager;
  @Inject
  private GeoIpService geoIpService;
  private Pattern passwordRegex;
  private Set<String> unrestrictedNames;
  private Multimap<String, String> restrictedNames;
  
  @PostConstruct
  public void reload()
  {
    this.passwordRegex = Utils.safePatternCompile((String)this.settings.getProperty(RestrictionSettings.ALLOWED_PASSWORD_REGEX));
    
    this.unrestrictedNames = new HashSet((Collection)this.settings.getProperty(RestrictionSettings.UNRESTRICTED_NAMES));
    
    this.restrictedNames = (((Boolean)this.settings.getProperty(RestrictionSettings.ENABLE_RESTRICTED_USERS)).booleanValue() ? loadNameRestrictions((List)this.settings.getProperty(RestrictionSettings.RESTRICTED_USERS)) : HashMultimap.create());
  }
  
  public ValidationResult validatePassword(String password, String username)
  {
    String passLow = password.toLowerCase();
    if (!this.passwordRegex.matcher(passLow).matches()) {
      return new ValidationResult(MessageKey.PASSWORD_CHARACTERS_ERROR, new String[] { this.passwordRegex.pattern() });
    }
    if (passLow.equalsIgnoreCase(username)) {
      return new ValidationResult(MessageKey.PASSWORD_IS_USERNAME_ERROR, new String[0]);
    }
    if ((password.length() < ((Integer)this.settings.getProperty(SecuritySettings.MIN_PASSWORD_LENGTH)).intValue()) || 
      (password.length() > ((Integer)this.settings.getProperty(SecuritySettings.MAX_PASSWORD_LENGTH)).intValue())) {
      return new ValidationResult(MessageKey.INVALID_PASSWORD_LENGTH, new String[0]);
    }
    if (((List)this.settings.getProperty(SecuritySettings.UNSAFE_PASSWORDS)).contains(passLow)) {
      return new ValidationResult(MessageKey.PASSWORD_UNSAFE_ERROR, new String[0]);
    }
    return new ValidationResult();
  }
  
  public boolean validateEmail(String email)
  {
    if ((Utils.isEmailEmpty(email)) || (!StringUtils.isInsideString('@', email))) {
      return false;
    }
    String emailDomain = email.split("@")[1];
    return validateWhitelistAndBlacklist(emailDomain, EmailSettings.DOMAIN_WHITELIST, EmailSettings.DOMAIN_BLACKLIST);
  }
  
  public boolean isEmailFreeForRegistration(String email, CommandSender sender)
  {
    return (this.permissionsManager.hasPermission(sender, PlayerStatePermission.ALLOW_MULTIPLE_ACCOUNTS)) || 
      (this.dataSource.countAuthsByEmail(email) < ((Integer)this.settings.getProperty(EmailSettings.MAX_REG_PER_EMAIL)).intValue());
  }
  
  public boolean isCountryAdmitted(String hostAddress)
  {
    if ((((List)this.settings.getProperty(ProtectionSettings.COUNTRIES_WHITELIST)).isEmpty()) && 
      (((List)this.settings.getProperty(ProtectionSettings.COUNTRIES_BLACKLIST)).isEmpty())) {
      return true;
    }
    String countryCode = this.geoIpService.getCountryCode(hostAddress);
    boolean isCountryAllowed = validateWhitelistAndBlacklist(countryCode, ProtectionSettings.COUNTRIES_WHITELIST, ProtectionSettings.COUNTRIES_BLACKLIST);
    
    ConsoleLogger.debug("Country code `{0}` for `{1}` is allowed: {2}", new Object[] { countryCode, hostAddress, Boolean.valueOf(isCountryAllowed) });
    return isCountryAllowed;
  }
  
  public boolean isUnrestricted(String name)
  {
    return this.unrestrictedNames.contains(name.toLowerCase());
  }
  
  public boolean fulfillsNameRestrictions(Player player)
  {
    Collection<String> restrictions = this.restrictedNames.get(player.getName().toLowerCase());
    if (Utils.isCollectionEmpty(restrictions)) {
      return true;
    }
    String ip = PlayerUtils.getPlayerIp(player);
    String domain = player.getAddress().getHostName();
    for (String restriction : restrictions)
    {
      if (restriction.startsWith("regex:")) {
        restriction = restriction.replace("regex:", "");
      } else {
        restriction = restriction.replaceAll("\\*", "(.*)");
      }
      if (ip.matches(restriction)) {
        return true;
      }
      if (domain.matches(restriction)) {
        return true;
      }
    }
    return false;
  }
  
  private boolean validateWhitelistAndBlacklist(String value, Property<List<String>> whitelist, Property<List<String>> blacklist)
  {
    List<String> whitelistValue = (List)this.settings.getProperty(whitelist);
    if (!Utils.isCollectionEmpty(whitelistValue)) {
      return containsIgnoreCase(whitelistValue, value);
    }
    List<String> blacklistValue = (List)this.settings.getProperty(blacklist);
    return (Utils.isCollectionEmpty(blacklistValue)) || (!containsIgnoreCase(blacklistValue, value));
  }
  
  private static boolean containsIgnoreCase(Collection<String> coll, String needle)
  {
    for (String entry : coll) {
      if (entry.equalsIgnoreCase(needle)) {
        return true;
      }
    }
    return false;
  }
  
  private Multimap<String, String> loadNameRestrictions(List<String> configuredRestrictions)
  {
    Multimap<String, String> restrictions = HashMultimap.create();
    for (String restriction : configuredRestrictions) {
      if (StringUtils.isInsideString(';', restriction))
      {
        String[] data = restriction.split(";");
        restrictions.put(data[0].toLowerCase(), data[1]);
      }
      else
      {
        ConsoleLogger.warning("Restricted user rule must have a ';' separating name from restriction, but found: '" + restriction + "'");
      }
    }
    return restrictions;
  }
  
  public static final class ValidationResult
  {
    private final MessageKey messageKey;
    private final String[] args;
    
    public ValidationResult()
    {
      this.messageKey = null;
      this.args = null;
    }
    
    public ValidationResult(MessageKey messageKey, String... args)
    {
      this.messageKey = messageKey;
      this.args = args;
    }
    
    public boolean hasError()
    {
      return this.messageKey != null;
    }
    
    public MessageKey getMessageKey()
    {
      return this.messageKey;
    }
    
    public String[] getArgs()
    {
      return this.args;
    }
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\service\ValidationService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */