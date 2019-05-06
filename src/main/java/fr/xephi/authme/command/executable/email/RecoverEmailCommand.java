package fr.xephi.authme.command.executable.email;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.command.PlayerCommand;
import fr.xephi.authme.data.auth.PlayerCache;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.datasource.DataSourceResult;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.mail.EmailService;
import fr.xephi.authme.message.MessageKey;
import fr.xephi.authme.service.CommonService;
import fr.xephi.authme.service.PasswordRecoveryService;
import fr.xephi.authme.service.RecoveryCodeService;
import fr.xephi.authme.util.Utils;
import java.util.List;
import org.bukkit.entity.Player;

public class RecoverEmailCommand
  extends PlayerCommand
{
  @Inject
  private CommonService commonService;
  @Inject
  private DataSource dataSource;
  @Inject
  private PlayerCache playerCache;
  @Inject
  private EmailService emailService;
  @Inject
  private PasswordRecoveryService recoveryService;
  @Inject
  private RecoveryCodeService recoveryCodeService;
  
  protected void runCommand(Player player, List<String> arguments)
  {
    String playerMail = (String)arguments.get(0);
    String playerName = player.getName();
    if (!this.emailService.hasAllInformation())
    {
      ConsoleLogger.warning("Mail API is not set");
      this.commonService.send(player, MessageKey.INCOMPLETE_EMAIL_SETTINGS);
      return;
    }
    if (this.playerCache.isAuthenticated(playerName))
    {
      this.commonService.send(player, MessageKey.ALREADY_LOGGED_IN_ERROR);
      return;
    }
    DataSourceResult<String> emailResult = this.dataSource.getEmail(playerName);
    if (!emailResult.playerExists())
    {
      this.commonService.send(player, MessageKey.USAGE_REGISTER);
      return;
    }
    String email = (String)emailResult.getValue();
    if ((Utils.isEmailEmpty(email)) || (!email.equalsIgnoreCase(playerMail)))
    {
      this.commonService.send(player, MessageKey.INVALID_EMAIL);
      return;
    }
    if (this.recoveryCodeService.isRecoveryCodeNeeded()) {
      this.recoveryService.createAndSendRecoveryCode(player, email);
    } else {
      this.recoveryService.generateAndSendNewPassword(player, email);
    }
  }
  
  public MessageKey getArgumentsMismatchMessage()
  {
    return MessageKey.USAGE_RECOVER_EMAIL;
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\command\executable\email\RecoverEmailCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */