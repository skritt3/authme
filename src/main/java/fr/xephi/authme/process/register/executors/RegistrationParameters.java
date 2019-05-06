package fr.xephi.authme.process.register.executors;

import org.bukkit.entity.Player;

public abstract class RegistrationParameters
{
  private final Player player;
  
  public RegistrationParameters(Player player)
  {
    this.player = player;
  }
  
  public Player getPlayer()
  {
    return this.player;
  }
  
  public String getPlayerName()
  {
    return this.player.getName();
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\process\register\executors\RegistrationParameters.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */