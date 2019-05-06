package fr.xephi.authme.listener;

import fr.xephi.authme.data.auth.PlayerCache;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.initialization.SettingsDependent;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.service.ValidationService;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.settings.properties.RegistrationSettings;
import fr.xephi.authme.util.PlayerUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.player.PlayerEvent;

class ListenerService
  implements SettingsDependent
{
  private final DataSource dataSource;
  private final PlayerCache playerCache;
  private final ValidationService validationService;
  private boolean isRegistrationForced;
  
  @Inject
  ListenerService(Settings settings, DataSource dataSource, PlayerCache playerCache, ValidationService validationService)
  {
    this.dataSource = dataSource;
    this.playerCache = playerCache;
    this.validationService = validationService;
    reload(settings);
  }
  
  public boolean shouldCancelEvent(EntityEvent event)
  {
    Entity entity = event.getEntity();
    return shouldCancelEvent(entity);
  }
  
  public boolean shouldCancelEvent(Entity entity)
  {
    if ((entity == null) || (!(entity instanceof Player))) {
      return false;
    }
    Player player = (Player)entity;
    return shouldCancelEvent(player);
  }
  
  public boolean shouldCancelEvent(PlayerEvent event)
  {
    Player player = event.getPlayer();
    return shouldCancelEvent(player);
  }
  
  public boolean shouldCancelEvent(Player player)
  {
    return (player != null) && (!checkAuth(player.getName())) && (!PlayerUtils.isNpc(player));
  }
  
  public void reload(Settings settings)
  {
    this.isRegistrationForced = ((Boolean)settings.getProperty(RegistrationSettings.FORCE)).booleanValue();
  }
  
  private boolean checkAuth(String name)
  {
    if ((this.validationService.isUnrestricted(name)) || (this.playerCache.isAuthenticated(name))) {
      return true;
    }
    if ((!this.isRegistrationForced) && (!this.dataSource.isAuthAvailable(name))) {
      return true;
    }
    return false;
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\listener\ListenerService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */