package fr.xephi.authme.command.executable.authme;

import fr.xephi.authme.command.ExecutableCommand;
import fr.xephi.authme.data.auth.PlayerAuth;
import fr.xephi.authme.data.auth.PlayerCache;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.message.MessageKey;
import fr.xephi.authme.service.BukkitService;
import fr.xephi.authme.service.CommonService;
import fr.xephi.authme.service.ValidationService;
import java.util.List;
import org.bukkit.command.CommandSender;

public class SetEmailCommand
  implements ExecutableCommand
{
  @Inject
  private DataSource dataSource;
  @Inject
  private CommonService commonService;
  @Inject
  private PlayerCache playerCache;
  @Inject
  private BukkitService bukkitService;
  @Inject
  private ValidationService validationService;
  
  public void executeCommand(final CommandSender sender, List<String> arguments)
  {
    final String playerName = (String)arguments.get(0);
    final String playerEmail = (String)arguments.get(1);
    if (!this.validationService.validateEmail(playerEmail))
    {
      this.commonService.send(sender, MessageKey.INVALID_EMAIL);
      return;
    }
    this.bukkitService.runTaskOptionallyAsync(new Runnable()
    {
      public void run()
      {
        PlayerAuth auth = SetEmailCommand.this.dataSource.getAuth(playerName);
        if (auth == null)
        {
          SetEmailCommand.this.commonService.send(sender, MessageKey.UNKNOWN_USER);
          return;
        }
        if (!SetEmailCommand.this.validationService.isEmailFreeForRegistration(playerEmail, sender))
        {
          SetEmailCommand.this.commonService.send(sender, MessageKey.EMAIL_ALREADY_USED_ERROR);
          return;
        }
        auth.setEmail(playerEmail);
        if (!SetEmailCommand.this.dataSource.updateEmail(auth))
        {
          SetEmailCommand.this.commonService.send(sender, MessageKey.ERROR);
          return;
        }
        if (SetEmailCommand.this.playerCache.getAuth(playerName) != null) {
          SetEmailCommand.this.playerCache.updatePlayer(auth);
        }
        SetEmailCommand.this.commonService.send(sender, MessageKey.EMAIL_CHANGED_SUCCESS);
      }
    });
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\command\executable\authme\SetEmailCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */