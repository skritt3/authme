package fr.xephi.authme.command.executable.authme;

import fr.xephi.authme.command.ExecutableCommand;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.process.Management;
import fr.xephi.authme.service.CommonService;
import fr.xephi.authme.service.ValidationService;
import fr.xephi.authme.service.ValidationService.ValidationResult;
import java.util.List;
import org.bukkit.command.CommandSender;

public class ChangePasswordAdminCommand
  implements ExecutableCommand
{
  @Inject
  private ValidationService validationService;
  @Inject
  private CommonService commonService;
  @Inject
  private Management management;
  
  public void executeCommand(CommandSender sender, List<String> arguments)
  {
    String playerName = (String)arguments.get(0);
    String playerPass = (String)arguments.get(1);
    
    ValidationService.ValidationResult validationResult = this.validationService.validatePassword(playerPass, playerName);
    if (validationResult.hasError()) {
      this.commonService.send(sender, validationResult.getMessageKey(), validationResult.getArgs());
    } else {
      this.management.performPasswordChangeAsAdmin(sender, playerName, playerPass);
    }
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\command\executable\authme\ChangePasswordAdminCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */