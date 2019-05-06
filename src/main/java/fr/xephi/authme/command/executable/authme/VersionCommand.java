package fr.xephi.authme.command.executable.authme;

import fr.xephi.authme.AuthMe;
import fr.xephi.authme.command.ExecutableCommand;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.service.BukkitService;
import java.util.Collection;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VersionCommand
  implements ExecutableCommand
{
  @Inject
  private BukkitService bukkitService;
  
  public void executeCommand(CommandSender sender, List<String> arguments)
  {
    sender.sendMessage(ChatColor.GOLD + "==========[ " + AuthMe.getPluginName() + " ABOUT ]==========");
    sender.sendMessage(ChatColor.GOLD + "Version: " + ChatColor.WHITE + AuthMe.getPluginName() + " v" + 
      AuthMe.getPluginVersion() + ChatColor.GRAY + " (build: " + AuthMe.getPluginBuildNumber() + ")");
    sender.sendMessage(ChatColor.GOLD + "Developers:");
    Collection<? extends Player> onlinePlayers = this.bukkitService.getOnlinePlayers();
    printDeveloper(sender, "Alexandre Vanhecke", "xephi59", "Original Author", onlinePlayers);
    printDeveloper(sender, "Lucas J.", "ljacqu", "Main Developer", onlinePlayers);
    printDeveloper(sender, "Gnat008", "gnat008", "Developer", onlinePlayers);
    printDeveloper(sender, "DNx5", "DNx5", "Developer", onlinePlayers);
    printDeveloper(sender, "games647", "games647", "Developer", onlinePlayers);
    printDeveloper(sender, "Tim Visee", "timvisee", "Developer", onlinePlayers);
    printDeveloper(sender, "Gabriele C.", "sgdc3", "Project manager, Contributor", onlinePlayers);
    sender.sendMessage(ChatColor.GOLD + "Website: " + ChatColor.WHITE + "http://dev.bukkit.org/bukkit-plugins/authme-reloaded/");
    
    sender.sendMessage(ChatColor.GOLD + "License: " + ChatColor.WHITE + "GNU GPL v3.0" + ChatColor.GRAY + ChatColor.ITALIC + " (See LICENSE file)");
    
    sender.sendMessage(ChatColor.GOLD + "Copyright: " + ChatColor.WHITE + "Copyright (c) AuthMe-Team 2017. All rights reserved.");
  }
  
  private static void printDeveloper(CommandSender sender, String name, String minecraftName, String function, Collection<? extends Player> onlinePlayers)
  {
    StringBuilder msg = new StringBuilder();
    msg.append(" ")
      .append(ChatColor.WHITE)
      .append(name);
    
    msg.append(ChatColor.GRAY).append(" // ").append(ChatColor.WHITE).append(minecraftName);
    msg.append(ChatColor.GRAY).append(ChatColor.ITALIC).append(" (").append(function).append(")");
    if (isPlayerOnline(minecraftName, onlinePlayers)) {
      msg.append(ChatColor.GREEN).append(ChatColor.ITALIC).append(" (In-Game)");
    }
    sender.sendMessage(msg.toString());
  }
  
  private static boolean isPlayerOnline(String minecraftName, Collection<? extends Player> onlinePlayers)
  {
    for (Player player : onlinePlayers) {
      if (player.getName().equalsIgnoreCase(minecraftName)) {
        return true;
      }
    }
    return false;
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\command\executable\authme\VersionCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */