package fr.xephi.authme.data.limbo.persistence;

import fr.xephi.authme.data.limbo.LimboPlayer;
import org.bukkit.entity.Player;

class NoOpPersistenceHandler
  implements LimboPersistenceHandler
{
  public LimboPlayer getLimboPlayer(Player player)
  {
    return null;
  }
  
  public void saveLimboPlayer(Player player, LimboPlayer limbo) {}
  
  public void removeLimboPlayer(Player player) {}
  
  public LimboPersistenceType getType()
  {
    return LimboPersistenceType.DISABLED;
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\data\limbo\persistence\NoOpPersistenceHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */