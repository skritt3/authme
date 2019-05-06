package fr.xephi.authme.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class RestoreSessionEvent
  extends CustomEvent
  implements Cancellable
{
  private static final HandlerList handlers = new HandlerList();
  private final Player player;
  private boolean isCancelled;
  
  public RestoreSessionEvent(Player player, boolean isAsync)
  {
    super(isAsync);
    this.player = player;
  }
  
  public boolean isCancelled()
  {
    return this.isCancelled;
  }
  
  public void setCancelled(boolean isCancelled)
  {
    this.isCancelled = isCancelled;
  }
  
  public Player getPlayer()
  {
    return this.player;
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


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\events\RestoreSessionEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */