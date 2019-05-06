package fr.xephi.authme.listener;

import fr.xephi.authme.libs.javax.inject.Inject;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockListener
  implements Listener
{
  @Inject
  private ListenerService listenerService;
  
  @EventHandler(ignoreCancelled=true)
  public void onBlockPlace(BlockPlaceEvent event)
  {
    if (this.listenerService.shouldCancelEvent(event.getPlayer())) {
      event.setCancelled(true);
    }
  }
  
  @EventHandler(ignoreCancelled=true)
  public void onBlockBreak(BlockBreakEvent event)
  {
    if (this.listenerService.shouldCancelEvent(event.getPlayer())) {
      event.setCancelled(true);
    }
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\listener\BlockListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */