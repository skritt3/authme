package fr.xephi.authme.initialization;

import fr.xephi.authme.data.auth.PlayerAuth;
import fr.xephi.authme.data.auth.PlayerAuth.Builder;
import fr.xephi.authme.data.auth.PlayerCache;
import fr.xephi.authme.data.limbo.LimboService;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.service.BukkitService;
import fr.xephi.authme.service.ValidationService;
import fr.xephi.authme.service.bungeecord.BungeeSender;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.settings.SpawnLoader;
import fr.xephi.authme.settings.properties.RestrictionSettings;
import fr.xephi.authme.util.PlayerUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class OnShutdownPlayerSaver
{
  @Inject
  private BukkitService bukkitService;
  @Inject
  private Settings settings;
  @Inject
  private ValidationService validationService;
  @Inject
  private DataSource dataSource;
  @Inject
  private SpawnLoader spawnLoader;
  @Inject
  private PlayerCache playerCache;
  @Inject
  private LimboService limboService;
  @Inject
  private BungeeSender bungeeSender;
  
  public void saveAllPlayers()
  {
    for (Player player : this.bukkitService.getOnlinePlayers()) {
      savePlayer(player);
    }
  }
  
  private void savePlayer(Player player)
  {
    String name = player.getName().toLowerCase();
    if ((PlayerUtils.isNpc(player)) || (this.validationService.isUnrestricted(name))) {
      return;
    }
    if (this.limboService.hasLimboPlayer(name)) {
      this.limboService.restoreData(player);
    } else {
      saveLoggedinPlayer(player);
    }
    this.playerCache.removePlayer(name);
  }
  
  private void saveLoggedinPlayer(Player player)
  {
    if (((Boolean)this.settings.getProperty(RestrictionSettings.SAVE_QUIT_LOCATION)).booleanValue())
    {
      Location loc = this.spawnLoader.getPlayerLocationOrSpawn(player);
      
      PlayerAuth auth = PlayerAuth.builder().name(player.getName().toLowerCase()).realName(player.getName()).location(loc).build();
      this.dataSource.updateQuitLoc(auth);
      this.bungeeSender.sendAuthMeBungeecordMessage("refresh.quitloc", player.getName());
    }
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\initialization\OnShutdownPlayerSaver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */