package fr.xephi.authme.process.quit;

import fr.xephi.authme.AuthMe;
import fr.xephi.authme.data.VerificationCodeManager;
import fr.xephi.authme.data.auth.PlayerAuth;
import fr.xephi.authme.data.auth.PlayerAuth.Builder;
import fr.xephi.authme.data.auth.PlayerCache;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.process.AsynchronousProcess;
import fr.xephi.authme.process.SyncProcessManager;
import fr.xephi.authme.service.CommonService;
import fr.xephi.authme.service.SessionService;
import fr.xephi.authme.service.ValidationService;
import fr.xephi.authme.service.bungeecord.BungeeSender;
import fr.xephi.authme.settings.SpawnLoader;
import fr.xephi.authme.settings.properties.PluginSettings;
import fr.xephi.authme.settings.properties.RestrictionSettings;
import fr.xephi.authme.util.PlayerUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class AsynchronousQuit
  implements AsynchronousProcess
{
  @Inject
  private AuthMe plugin;
  @Inject
  private DataSource database;
  @Inject
  private CommonService service;
  @Inject
  private PlayerCache playerCache;
  @Inject
  private SyncProcessManager syncProcessManager;
  @Inject
  private SpawnLoader spawnLoader;
  @Inject
  private ValidationService validationService;
  @Inject
  private VerificationCodeManager codeManager;
  @Inject
  private SessionService sessionService;
  @Inject
  private BungeeSender bungeeSender;
  
  public void processQuit(Player player)
  {
    if ((player == null) || (this.validationService.isUnrestricted(player.getName()))) {
      return;
    }
    String name = player.getName().toLowerCase();
    boolean wasLoggedIn = this.playerCache.isAuthenticated(name);
    if (wasLoggedIn)
    {
      if (((Boolean)this.service.getProperty(RestrictionSettings.SAVE_QUIT_LOCATION)).booleanValue())
      {
        Location loc = this.spawnLoader.getPlayerLocationOrSpawn(player);
        
        PlayerAuth auth = PlayerAuth.builder().name(name).location(loc).realName(player.getName()).build();
        this.database.updateQuitLoc(auth);
      }
      String ip = PlayerUtils.getPlayerIp(player);
      
      PlayerAuth auth = PlayerAuth.builder().name(name).realName(player.getName()).lastIp(ip).lastLogin(Long.valueOf(System.currentTimeMillis())).build();
      this.database.updateSession(auth);
      this.bungeeSender.sendAuthMeBungeecordMessage("refresh.quitloc", name);
    }
    this.playerCache.removePlayer(name);
    this.codeManager.unverify(name);
    if (wasLoggedIn)
    {
      this.database.setUnlogged(name);
      if (!((Boolean)this.service.getProperty(PluginSettings.SESSIONS_ENABLED)).booleanValue()) {
        this.sessionService.revokeSession(name);
      }
    }
    if (this.plugin.isEnabled()) {
      this.syncProcessManager.processSyncPlayerQuit(player, wasLoggedIn);
    }
    this.database.invalidateCache(name);
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\process\quit\AsynchronousQuit.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */