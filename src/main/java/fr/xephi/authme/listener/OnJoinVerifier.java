package fr.xephi.authme.listener;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.data.auth.PlayerAuth;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.initialization.Reloadable;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.message.MessageKey;
import fr.xephi.authme.message.Messages;
import fr.xephi.authme.permission.PermissionsManager;
import fr.xephi.authme.permission.PlayerStatePermission;
import fr.xephi.authme.service.AntiBotService;
import fr.xephi.authme.service.BukkitService;
import fr.xephi.authme.service.ValidationService;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.settings.properties.ProtectionSettings;
import fr.xephi.authme.settings.properties.RegistrationSettings;
import fr.xephi.authme.settings.properties.RestrictionSettings;
import fr.xephi.authme.util.StringUtils;
import fr.xephi.authme.util.Utils;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

public class OnJoinVerifier
  implements Reloadable
{
  @Inject
  private Settings settings;
  @Inject
  private DataSource dataSource;
  @Inject
  private Messages messages;
  @Inject
  private PermissionsManager permissionsManager;
  @Inject
  private AntiBotService antiBotService;
  @Inject
  private ValidationService validationService;
  @Inject
  private BukkitService bukkitService;
  @Inject
  private Server server;
  private Pattern nicknamePattern;
  
  @PostConstruct
  public void reload()
  {
    String nickRegEx = (String)this.settings.getProperty(RestrictionSettings.ALLOWED_NICKNAME_CHARACTERS);
    this.nicknamePattern = Utils.safePatternCompile(nickRegEx);
  }
  
  public void checkAntibot(JoiningPlayer joiningPlayer, boolean isAuthAvailable)
    throws FailedVerificationException
  {
    if ((isAuthAvailable) || (this.permissionsManager.hasPermission(joiningPlayer, PlayerStatePermission.BYPASS_ANTIBOT))) {
      return;
    }
    if (this.antiBotService.shouldKick())
    {
      this.antiBotService.addPlayerKick(joiningPlayer.getName());
      throw new FailedVerificationException(MessageKey.KICK_ANTIBOT, new String[0]);
    }
  }
  
  public void checkKickNonRegistered(boolean isAuthAvailable)
    throws FailedVerificationException
  {
    if ((!isAuthAvailable) && (((Boolean)this.settings.getProperty(RestrictionSettings.KICK_NON_REGISTERED)).booleanValue())) {
      throw new FailedVerificationException(MessageKey.MUST_REGISTER_MESSAGE, new String[0]);
    }
  }
  
  public void checkIsValidName(String name)
    throws FailedVerificationException
  {
    if ((name.length() > ((Integer)this.settings.getProperty(RestrictionSettings.MAX_NICKNAME_LENGTH)).intValue()) || 
      (name.length() < ((Integer)this.settings.getProperty(RestrictionSettings.MIN_NICKNAME_LENGTH)).intValue())) {
      throw new FailedVerificationException(MessageKey.INVALID_NAME_LENGTH, new String[0]);
    }
    if (!this.nicknamePattern.matcher(name).matches()) {
      throw new FailedVerificationException(MessageKey.INVALID_NAME_CHARACTERS, new String[] { this.nicknamePattern.pattern() });
    }
  }
  
  public boolean refusePlayerForFullServer(PlayerLoginEvent event)
  {
    Player player = event.getPlayer();
    if (event.getResult() != PlayerLoginEvent.Result.KICK_FULL) {
      return false;
    }
    if (!this.permissionsManager.hasPermission(player, PlayerStatePermission.IS_VIP))
    {
      event.setKickMessage(this.messages.retrieveSingle(MessageKey.KICK_FULL_SERVER, new String[0]));
      return true;
    }
    Collection<? extends Player> onlinePlayers = this.bukkitService.getOnlinePlayers();
    if (onlinePlayers.size() < this.server.getMaxPlayers())
    {
      event.allow();
      return false;
    }
    Player nonVipPlayer = generateKickPlayer(onlinePlayers);
    if (nonVipPlayer != null)
    {
      nonVipPlayer.kickPlayer(this.messages.retrieveSingle(MessageKey.KICK_FOR_VIP, new String[0]));
      event.allow();
      return false;
    }
    ConsoleLogger.info("VIP player " + player.getName() + " tried to join, but the server was full");
    event.setKickMessage(this.messages.retrieveSingle(MessageKey.KICK_FULL_SERVER, new String[0]));
    return true;
  }
  
  public void checkNameCasing(String connectingName, PlayerAuth auth)
    throws FailedVerificationException
  {
    if ((auth != null) && (((Boolean)this.settings.getProperty(RegistrationSettings.PREVENT_OTHER_CASE)).booleanValue()))
    {
      String realName = auth.getRealName();
      if ((StringUtils.isEmpty(realName)) || ("Player".equals(realName))) {
        this.dataSource.updateRealName(connectingName.toLowerCase(), connectingName);
      } else if (!realName.equals(connectingName)) {
        throw new FailedVerificationException(MessageKey.INVALID_NAME_CASE, new String[] { realName, connectingName });
      }
    }
  }
  
  public void checkPlayerCountry(JoiningPlayer joiningPlayer, String address, boolean isAuthAvailable)
    throws FailedVerificationException
  {
    if (((!isAuthAvailable) || (((Boolean)this.settings.getProperty(ProtectionSettings.ENABLE_PROTECTION_REGISTERED)).booleanValue())) && 
      (((Boolean)this.settings.getProperty(ProtectionSettings.ENABLE_PROTECTION)).booleanValue()) && 
      (!this.permissionsManager.hasPermission(joiningPlayer, PlayerStatePermission.BYPASS_COUNTRY_CHECK)) && 
      (!this.validationService.isCountryAdmitted(address))) {
      throw new FailedVerificationException(MessageKey.COUNTRY_BANNED_ERROR, new String[0]);
    }
  }
  
  public void checkSingleSession(String name)
    throws FailedVerificationException
  {
    if (!((Boolean)this.settings.getProperty(RestrictionSettings.FORCE_SINGLE_SESSION)).booleanValue()) {
      return;
    }
    Player onlinePlayer = this.bukkitService.getPlayerExact(name);
    if (onlinePlayer != null) {
      throw new FailedVerificationException(MessageKey.USERNAME_ALREADY_ONLINE_ERROR, new String[0]);
    }
  }
  
  private Player generateKickPlayer(Collection<? extends Player> onlinePlayers)
  {
    for (Player player : onlinePlayers) {
      if (!this.permissionsManager.hasPermission(player, PlayerStatePermission.IS_VIP)) {
        return player;
      }
    }
    return null;
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\listener\OnJoinVerifier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */