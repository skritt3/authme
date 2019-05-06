package fr.xephi.authme.data;

import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.datasource.DataSourceResult;
import fr.xephi.authme.initialization.HasCleanup;
import fr.xephi.authme.initialization.SettingsDependent;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.mail.EmailService;
import fr.xephi.authme.permission.PermissionsManager;
import fr.xephi.authme.permission.PlayerPermission;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.settings.properties.SecuritySettings;
import fr.xephi.authme.util.RandomStringUtils;
import fr.xephi.authme.util.Utils;
import fr.xephi.authme.util.expiring.ExpiringMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.bukkit.entity.Player;

public class VerificationCodeManager
  implements SettingsDependent, HasCleanup
{
  private final EmailService emailService;
  private final DataSource dataSource;
  private final PermissionsManager permissionsManager;
  private final ExpiringMap<String, String> verificationCodes;
  private final Set<String> verifiedPlayers;
  private boolean canSendMail;
  
  @Inject
  VerificationCodeManager(Settings settings, DataSource dataSource, EmailService emailService, PermissionsManager permissionsManager)
  {
    this.emailService = emailService;
    this.dataSource = dataSource;
    this.permissionsManager = permissionsManager;
    this.verifiedPlayers = new HashSet();
    long countTimeout = ((Integer)settings.getProperty(SecuritySettings.VERIFICATION_CODE_EXPIRATION_MINUTES)).intValue();
    this.verificationCodes = new ExpiringMap(countTimeout, TimeUnit.MINUTES);
    reload(settings);
  }
  
  public boolean canSendMail()
  {
    return this.canSendMail;
  }
  
  public boolean isVerificationRequired(Player player)
  {
    String name = player.getName();
    return (this.canSendMail) && 
      (!isPlayerVerified(name)) && 
      (this.permissionsManager.hasPermission(player, PlayerPermission.VERIFICATION_CODE)) && 
      (hasEmail(name));
  }
  
  public boolean isCodeRequired(String name)
  {
    return (this.canSendMail) && (hasCode(name)) && (!isPlayerVerified(name));
  }
  
  private boolean isPlayerVerified(String name)
  {
    return this.verifiedPlayers.contains(name.toLowerCase());
  }
  
  public boolean hasCode(String name)
  {
    return this.verificationCodes.get(name.toLowerCase()) != null;
  }
  
  public boolean hasEmail(String name)
  {
    boolean result = false;
    DataSourceResult<String> emailResult = this.dataSource.getEmail(name);
    if (emailResult.playerExists())
    {
      String email = (String)emailResult.getValue();
      if (!Utils.isEmailEmpty(email)) {
        result = true;
      }
    }
    return result;
  }
  
  public void codeExistOrGenerateNew(String name)
  {
    if (!hasCode(name)) {
      generateCode(name);
    }
  }
  
  private void generateCode(String name)
  {
    DataSourceResult<String> emailResult = this.dataSource.getEmail(name);
    if (emailResult.playerExists())
    {
      String email = (String)emailResult.getValue();
      if (!Utils.isEmailEmpty(email))
      {
        String code = RandomStringUtils.generateNum(6);
        this.verificationCodes.put(name.toLowerCase(), code);
        this.emailService.sendVerificationMail(name, email, code);
      }
    }
  }
  
  public boolean checkCode(String name, String code)
  {
    boolean correct = false;
    if (code.equals(this.verificationCodes.get(name.toLowerCase())))
    {
      verify(name);
      correct = true;
    }
    return correct;
  }
  
  public void verify(String name)
  {
    this.verifiedPlayers.add(name.toLowerCase());
  }
  
  public void unverify(String name)
  {
    this.verifiedPlayers.remove(name.toLowerCase());
  }
  
  public void reload(Settings settings)
  {
    this.canSendMail = this.emailService.hasAllInformation();
    long countTimeout = ((Integer)settings.getProperty(SecuritySettings.VERIFICATION_CODE_EXPIRATION_MINUTES)).intValue();
    this.verificationCodes.setExpiration(countTimeout, TimeUnit.MINUTES);
  }
  
  public void performCleanup()
  {
    this.verificationCodes.removeExpiredEntries();
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\data\VerificationCodeManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */