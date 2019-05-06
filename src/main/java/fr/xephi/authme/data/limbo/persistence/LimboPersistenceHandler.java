package fr.xephi.authme.data.limbo.persistence;

import fr.xephi.authme.data.limbo.LimboPlayer;
import org.bukkit.entity.Player;

abstract interface LimboPersistenceHandler
{
  public abstract LimboPlayer getLimboPlayer(Player paramPlayer);
  
  public abstract void saveLimboPlayer(Player paramPlayer, LimboPlayer paramLimboPlayer);
  
  public abstract void removeLimboPlayer(Player paramPlayer);
  
  public abstract LimboPersistenceType getType();
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\data\limbo\persistence\LimboPersistenceHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */