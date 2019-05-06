package fr.xephi.authme.command.executable.authme;

import fr.xephi.authme.command.ExecutableCommand;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.service.BukkitService;
import fr.xephi.authme.task.purge.PurgeService;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

public class PurgeBannedPlayersCommand
  implements ExecutableCommand
{
  @Inject
  private PurgeService purgeService;
  @Inject
  private BukkitService bukkitService;
  
  public void executeCommand(CommandSender sender, List<String> arguments)
  {
    Set<OfflinePlayer> bannedPlayers = this.bukkitService.getBannedPlayers();
    Set<String> namedBanned = new HashSet(bannedPlayers.size());
    for (OfflinePlayer offlinePlayer : bannedPlayers) {
      namedBanned.add(offlinePlayer.getName().toLowerCase());
    }
    this.purgeService.purgePlayers(sender, namedBanned, (OfflinePlayer[])bannedPlayers.toArray(new OfflinePlayer[bannedPlayers.size()]));
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\command\executable\authme\PurgeBannedPlayersCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */