package fr.xephi.authme.events;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class UnregisterByPlayerEvent
  extends AbstractUnregisterEvent
{
  private static final HandlerList handlers = new HandlerList();
  
  public UnregisterByPlayerEvent(Player player, boolean isAsync)
  {
    super(player, isAsync);
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


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\events\UnregisterByPlayerEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */