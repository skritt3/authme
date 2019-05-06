package fr.xephi.authme.events;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class LoginEvent
  extends CustomEvent
{
  private static final HandlerList handlers = new HandlerList();
  private final Player player;
  
  public LoginEvent(Player player)
  {
    this.player = player;
  }
  
  public Player getPlayer()
  {
    return this.player;
  }
  
  @Deprecated
  public boolean isLogin()
  {
    return true;
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


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\events\LoginEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */