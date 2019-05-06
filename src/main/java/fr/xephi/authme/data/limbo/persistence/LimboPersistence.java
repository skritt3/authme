package fr.xephi.authme.data.limbo.persistence;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.data.limbo.LimboPlayer;
import fr.xephi.authme.initialization.SettingsDependent;
import fr.xephi.authme.initialization.factory.Factory;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.settings.properties.LimboSettings;
import org.bukkit.entity.Player;

public class LimboPersistence
  implements SettingsDependent
{
  private final Factory<LimboPersistenceHandler> handlerFactory;
  private LimboPersistenceHandler handler;
  
  @Inject
  LimboPersistence(Settings settings, Factory<LimboPersistenceHandler> handlerFactory)
  {
    this.handlerFactory = handlerFactory;
    reload(settings);
  }
  
  public LimboPlayer getLimboPlayer(Player player)
  {
    try
    {
      return this.handler.getLimboPlayer(player);
    }
    catch (Exception e)
    {
      ConsoleLogger.logException("Could not get LimboPlayer for '" + player.getName() + "'", e);
    }
    return null;
  }
  
  public void saveLimboPlayer(Player player, LimboPlayer limbo)
  {
    try
    {
      this.handler.saveLimboPlayer(player, limbo);
    }
    catch (Exception e)
    {
      ConsoleLogger.logException("Could not save LimboPlayer for '" + player.getName() + "'", e);
    }
  }
  
  public void removeLimboPlayer(Player player)
  {
    try
    {
      this.handler.removeLimboPlayer(player);
    }
    catch (Exception e)
    {
      ConsoleLogger.logException("Could not remove LimboPlayer for '" + player.getName() + "'", e);
    }
  }
  
  public void reload(Settings settings)
  {
    LimboPersistenceType persistenceType = (LimboPersistenceType)settings.getProperty(LimboSettings.LIMBO_PERSISTENCE_TYPE);
    if ((this.handler != null) && (this.handler.getType() != persistenceType)) {
      ConsoleLogger.info("Limbo persistence type has changed! Note that the data is not converted.");
    }
    this.handler = ((LimboPersistenceHandler)this.handlerFactory.newInstance(persistenceType.getImplementationClass()));
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\data\limbo\persistence\LimboPersistence.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */