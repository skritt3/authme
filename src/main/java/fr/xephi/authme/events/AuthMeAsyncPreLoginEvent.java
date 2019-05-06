package fr.xephi.authme.events;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class AuthMeAsyncPreLoginEvent
  extends CustomEvent
{
  private static final HandlerList handlers = new HandlerList();
  private final Player player;
  private boolean canLogin = true;
  
  public AuthMeAsyncPreLoginEvent(Player player, boolean isAsync)
  {
    super(isAsync);
    this.player = player;
  }
  
  public Player getPlayer()
  {
    return this.player;
  }
  
  public boolean canLogin()
  {
    return this.canLogin;
  }
  
  public void setCanLogin(boolean canLogin)
  {
    this.canLogin = canLogin;
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


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\events\AuthMeAsyncPreLoginEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */