package fr.xephi.authme.task.purge;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.permission.PermissionsManager;
import fr.xephi.authme.permission.PlayerStatePermission;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

class PurgeTask
  extends BukkitRunnable
{
  private static final int INTERVAL_CHECK = 5;
  private final PurgeService purgeService;
  private final PermissionsManager permissionsManager;
  private final UUID sender;
  private final Set<String> toPurge;
  private final OfflinePlayer[] offlinePlayers;
  private final int totalPurgeCount;
  private int currentPage = 0;
  
  PurgeTask(PurgeService service, PermissionsManager permissionsManager, CommandSender sender, Set<String> toPurge, OfflinePlayer[] offlinePlayers)
  {
    this.purgeService = service;
    this.permissionsManager = permissionsManager;
    if ((sender instanceof Player)) {
      this.sender = ((Player)sender).getUniqueId();
    } else {
      this.sender = null;
    }
    this.toPurge = toPurge;
    this.totalPurgeCount = toPurge.size();
    this.offlinePlayers = offlinePlayers;
  }
  
  public void run()
  {
    if (this.toPurge.isEmpty())
    {
      finish();
      return;
    }
    Set<OfflinePlayer> playerPortion = new HashSet(5);
    Set<String> namePortion = new HashSet(5);
    for (int i = 0; i < 5; i++)
    {
      int nextPosition = this.currentPage * 5 + i;
      if (this.offlinePlayers.length <= nextPosition) {
        break;
      }
      OfflinePlayer offlinePlayer = this.offlinePlayers[nextPosition];
      if ((offlinePlayer.getName() != null) && (this.toPurge.remove(offlinePlayer.getName().toLowerCase())) && 
        (!this.permissionsManager.hasPermissionOffline(offlinePlayer, PlayerStatePermission.BYPASS_PURGE)))
      {
        playerPortion.add(offlinePlayer);
        namePortion.add(offlinePlayer.getName());
      }
    }
    if ((!this.toPurge.isEmpty()) && (playerPortion.isEmpty()))
    {
      ConsoleLogger.info("Finished lookup of offlinePlayers. Begin looking purging player names only");
      for (String name : this.toPurge) {
        if (!this.permissionsManager.hasPermissionOffline(name, PlayerStatePermission.BYPASS_PURGE)) {
          namePortion.add(name);
        }
      }
      this.toPurge.clear();
    }
    this.currentPage += 1;
    this.purgeService.executePurge(playerPortion, namePortion);
    if (this.currentPage % 20 == 0)
    {
      int completed = this.totalPurgeCount - this.toPurge.size();
      sendMessage("[AuthMe] Purge progress " + completed + '/' + this.totalPurgeCount);
    }
  }
  
  private void finish()
  {
    cancel();
    
    sendMessage(ChatColor.GREEN + "[AuthMe] Database has been purged successfully");
    
    ConsoleLogger.info("Purge finished!");
    this.purgeService.setPurging(false);
  }
  
  private void sendMessage(String message)
  {
    if (this.sender == null)
    {
      Bukkit.getConsoleSender().sendMessage(message);
    }
    else
    {
      Player player = Bukkit.getPlayer(this.sender);
      if (player != null) {
        player.sendMessage(message);
      }
    }
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\task\purge\PurgeTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */