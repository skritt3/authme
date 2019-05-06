package fr.xephi.authme.command.executable.email;

import fr.xephi.authme.command.CommandMapper;
import fr.xephi.authme.command.ExecutableCommand;
import fr.xephi.authme.command.FoundCommandResult;
import fr.xephi.authme.command.help.HelpProvider;
import fr.xephi.authme.libs.javax.inject.Inject;
import java.util.Collections;
import java.util.List;

import fr.xephi.authme.message.MessageKey;
import org.bukkit.command.CommandSender;

public class EmailBaseCommand
  implements ExecutableCommand
{
  @Inject
  private CommandMapper commandMapper;
  @Inject
  private HelpProvider helpProvider;
  
  public void executeCommand(CommandSender sender, List<String> arguments)
  {
    FoundCommandResult result = this.commandMapper.mapPartsToCommand(sender, Collections.singletonList("email"));
    this.helpProvider.outputHelp(sender, result, 64);
  }

  @Override
  public MessageKey getArgumentsMismatchMessage() {
    return null;
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\command\executable\email\EmailBaseCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */