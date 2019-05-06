package fr.xephi.authme.command.executable.authme;

import fr.xephi.authme.AuthMe;
import fr.xephi.authme.command.ExecutableCommand;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class AuthMeCommand
  implements ExecutableCommand
{
  public void executeCommand(CommandSender sender, List<String> arguments)
  {
    sender.sendMessage(ChatColor.GREEN + "This server is running " + AuthMe.getPluginName() + " v" + 
      AuthMe.getPluginVersion() + " b" + AuthMe.getPluginBuildNumber() + "! " + ChatColor.RED + "<3");
    sender.sendMessage(ChatColor.YELLOW + "Use the command " + ChatColor.GOLD + "/authme help" + ChatColor.YELLOW + " to view help.");
    
    sender.sendMessage(ChatColor.YELLOW + "Use the command " + ChatColor.GOLD + "/authme about" + ChatColor.YELLOW + " to view about.");
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\command\executable\authme\AuthMeCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */