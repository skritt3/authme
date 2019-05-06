package fr.xephi.authme.events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class FirstSpawnTeleportEvent
  extends AbstractTeleportEvent
{
  private static final HandlerList handlers = new HandlerList();
  
  public FirstSpawnTeleportEvent(Player player, Location to)
  {
    super(false, player, to);
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


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\events\FirstSpawnTeleportEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */