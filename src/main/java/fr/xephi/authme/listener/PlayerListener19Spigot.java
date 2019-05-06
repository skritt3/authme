package fr.xephi.authme.listener;

import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.service.TeleportationService;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

public class PlayerListener19Spigot
  implements Listener
{
  @Inject
  private TeleportationService teleportationService;
  private static boolean isPlayerSpawnLocationEventCalled = false;
  
  public static boolean isPlayerSpawnLocationEventCalled()
  {
    return isPlayerSpawnLocationEventCalled;
  }
  
  @EventHandler(priority=EventPriority.HIGH)
  public void onPlayerSpawn(PlayerSpawnLocationEvent event)
  {
    isPlayerSpawnLocationEventCalled = true;
    Player player = event.getPlayer();
    
    Location customSpawnLocation = this.teleportationService.prepareOnJoinSpawnLocation(player);
    if (customSpawnLocation != null) {
      event.setSpawnLocation(customSpawnLocation);
    }
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\listener\PlayerListener19Spigot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */