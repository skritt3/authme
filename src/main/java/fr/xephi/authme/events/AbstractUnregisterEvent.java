package fr.xephi.authme.events;

import org.bukkit.entity.Player;

public abstract class AbstractUnregisterEvent
  extends CustomEvent
{
  private final Player player;
  
  public AbstractUnregisterEvent(Player player, boolean isAsync)
  {
    super(isAsync);
    this.player = player;
  }
  
  public Player getPlayer()
  {
    return this.player;
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\events\AbstractUnregisterEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */