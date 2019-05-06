package fr.xephi.authme.service;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.initialization.HasCleanup;
import fr.xephi.authme.initialization.Reloadable;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.mail.EmailService;
import fr.xephi.authme.message.MessageKey;
import fr.xephi.authme.message.Messages;
import fr.xephi.authme.security.PasswordSecurity;
import fr.xephi.authme.security.crypts.HashedPassword;
import fr.xephi.authme.settings.properties.EmailSettings;
import fr.xephi.authme.settings.properties.SecuritySettings;
import fr.xephi.authme.util.PlayerUtils;
import fr.xephi.authme.util.RandomStringUtils;
import fr.xephi.authme.util.expiring.Duration;
import fr.xephi.authme.util.expiring.ExpiringMap;
import fr.xephi.authme.util.expiring.ExpiringSet;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import org.bukkit.entity.Player;

public class PasswordRecoveryService
  implements Reloadable, HasCleanup
{
  @Inject
  private CommonService commonService;
  @Inject
  private DataSource dataSource;
  @Inject
  private EmailService emailService;
  @Inject
  private PasswordSecurity passwordSecurity;
  @Inject
  private RecoveryCodeService recoveryCodeService;
  @Inject
  private Messages messages;
  private ExpiringSet<String> emailCooldown;
  private ExpiringMap<String, String> successfulRecovers;
  
  @PostConstruct
  private void initEmailCooldownSet()
  {
    this.emailCooldown = new ExpiringSet(((Integer)this.commonService.getProperty(SecuritySettings.EMAIL_RECOVERY_COOLDOWN_SECONDS)).intValue(), TimeUnit.SECONDS);
    
    this.successfulRecovers = new ExpiringMap(((Integer)this.commonService.getProperty(SecuritySettings.PASSWORD_CHANGE_TIMEOUT)).intValue(), TimeUnit.MINUTES);
  }
  
  public void createAndSendRecoveryCode(Player player, String email)
  {
    if (!checkEmailCooldown(player)) {
      return;
    }
    String recoveryCode = this.recoveryCodeService.generateCode(player.getName());
    boolean couldSendMail = this.emailService.sendRecoveryCode(player.getName(), email, recoveryCode);
    if (couldSendMail)
    {
      this.commonService.send(player, MessageKey.RECOVERY_CODE_SENT);
      this.emailCooldown.add(player.getName().toLowerCase());
    }
    else
    {
      this.commonService.send(player, MessageKey.EMAIL_SEND_FAILURE);
    }
  }
  
  public void generateAndSendNewPassword(Player player, String email)
  {
    if (!checkEmailCooldown(player)) {
      return;
    }
    String name = player.getName();
    String thePass = RandomStringUtils.generate(((Integer)this.commonService.getProperty(EmailSettings.RECOVERY_PASSWORD_LENGTH)).intValue());
    HashedPassword hashNew = this.passwordSecurity.computeHash(thePass, name);
    
    ConsoleLogger.info("Generating new password for '" + name + "'");
    
    this.dataSource.updatePassword(name, hashNew);
    boolean couldSendMail = this.emailService.sendPasswordMail(name, email, thePass);
    if (couldSendMail)
    {
      this.commonService.send(player, MessageKey.RECOVERY_EMAIL_SENT_MESSAGE);
      this.emailCooldown.add(player.getName().toLowerCase());
    }
    else
    {
      this.commonService.send(player, MessageKey.EMAIL_SEND_FAILURE);
    }
  }
  
  public void addSuccessfulRecovery(Player player)
  {
    String name = player.getName();
    String address = PlayerUtils.getPlayerIp(player);
    
    this.successfulRecovers.put(name, address);
    this.commonService.send(player, MessageKey.RECOVERY_CHANGE_PASSWORD);
  }
  
  private boolean checkEmailCooldown(Player player)
  {
    Duration waitDuration = this.emailCooldown.getExpiration(player.getName().toLowerCase());
    if (waitDuration.getDuration() > 0L)
    {
      String durationText = this.messages.formatDuration(waitDuration);
      this.messages.send(player, MessageKey.EMAIL_COOLDOWN_ERROR, new String[] { durationText });
      return false;
    }
    return true;
  }
  
  public boolean canChangePassword(Player player)
  {
    String name = player.getName();
    String playerAddress = PlayerUtils.getPlayerIp(player);
    String storedAddress = (String)this.successfulRecovers.get(name);
    if ((storedAddress == null) || (!playerAddress.equals(storedAddress)))
    {
      this.messages.send(player, MessageKey.CHANGE_PASSWORD_EXPIRED);
      return false;
    }
    return true;
  }
  
  public void reload()
  {
    this.emailCooldown.setExpiration(
      ((Integer)this.commonService.getProperty(SecuritySettings.EMAIL_RECOVERY_COOLDOWN_SECONDS)).intValue(), TimeUnit.SECONDS);
    this.successfulRecovers.setExpiration(
      ((Integer)this.commonService.getProperty(SecuritySettings.PASSWORD_CHANGE_TIMEOUT)).intValue(), TimeUnit.MINUTES);
  }
  
  public void performCleanup()
  {
    this.emailCooldown.removeExpiredEntries();
    this.successfulRecovers.removeExpiredEntries();
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\service\PasswordRecoveryService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */