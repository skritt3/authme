package fr.xephi.authme.listener;

import fr.xephi.authme.libs.javax.inject.Inject;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEditBookEvent;

public class PlayerListener16
  implements Listener
{
  @Inject
  private ListenerService listenerService;
  
  @EventHandler(ignoreCancelled=true, priority=EventPriority.LOWEST)
  public void onPlayerEditBook(PlayerEditBookEvent event)
  {
    if (this.listenerService.shouldCancelEvent(event)) {
      event.setCancelled(true);
    }
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\listener\PlayerListener16.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */