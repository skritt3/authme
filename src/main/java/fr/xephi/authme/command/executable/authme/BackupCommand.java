package fr.xephi.authme.command.executable.authme;

import fr.xephi.authme.command.ExecutableCommand;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.service.BackupService;
import fr.xephi.authme.service.BackupService.BackupCause;
import java.util.List;
import org.bukkit.command.CommandSender;

public class BackupCommand
  implements ExecutableCommand
{
  @Inject
  private BackupService backupService;
  
  public void executeCommand(CommandSender sender, List<String> arguments)
  {
    this.backupService.doBackup(BackupService.BackupCause.COMMAND, sender);
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\command\executable\authme\BackupCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */