package fr.xephi.authme.command;

import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class PlayerCommand
  implements ExecutableCommand
{
  public void executeCommand(CommandSender sender, List<String> arguments)
  {
    if ((sender instanceof Player))
    {
      runCommand((Player)sender, arguments);
    }
    else
    {
      String alternative = getAlternativeCommand();
      if (alternative != null) {
        sender.sendMessage("Player only! Please use " + alternative + " instead.");
      } else {
        sender.sendMessage("This command is only for players.");
      }
    }
  }
  
  protected abstract void runCommand(Player paramPlayer, List<String> paramList);
  
  protected String getAlternativeCommand()
  {
    return null;
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\command\PlayerCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */