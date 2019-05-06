package fr.xephi.authme.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class RestoreInventoryEvent
  extends CustomEvent
  implements Cancellable
{
  private static final HandlerList handlers = new HandlerList();
  private final Player player;
  private boolean isCancelled;
  
  public RestoreInventoryEvent(Player player)
  {
    this.player = player;
  }
  
  public Player getPlayer()
  {
    return this.player;
  }
  
  public boolean isCancelled()
  {
    return this.isCancelled;
  }
  
  public void setCancelled(boolean isCancelled)
  {
    this.isCancelled = isCancelled;
  }
  
  public static HandlerList getHandlerList()
  {
    return handlers;
  }
  
  public HandlerList getHandlers()
  {
    return handlers;
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\events\RestoreInventoryEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */