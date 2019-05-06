package fr.xephi.authme.process.logout;

import fr.xephi.authme.data.VerificationCodeManager;
import fr.xephi.authme.data.auth.PlayerAuth;
import fr.xephi.authme.data.auth.PlayerCache;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.message.MessageKey;
import fr.xephi.authme.process.AsynchronousProcess;
import fr.xephi.authme.process.SyncProcessManager;
import fr.xephi.authme.service.CommonService;
import fr.xephi.authme.service.SessionService;
import fr.xephi.authme.service.bungeecord.BungeeSender;
import fr.xephi.authme.settings.properties.RestrictionSettings;
import org.bukkit.entity.Player;

public class AsynchronousLogout
  implements AsynchronousProcess
{
  @Inject
  private DataSource database;
  @Inject
  private CommonService service;
  @Inject
  private PlayerCache playerCache;
  @Inject
  private VerificationCodeManager codeManager;
  @Inject
  private SyncProcessManager syncProcessManager;
  @Inject
  private SessionService sessionService;
  @Inject
  private BungeeSender bungeeSender;
  
  public void logout(Player player)
  {
    String name = player.getName().toLowerCase();
    if (!this.playerCache.isAuthenticated(name))
    {
      this.service.send(player, MessageKey.NOT_LOGGED_IN);
      return;
    }
    PlayerAuth auth = this.playerCache.getAuth(name);
    this.database.updateSession(auth);
    this.bungeeSender.sendAuthMeBungeecordMessage("refresh.session", name);
    if (((Boolean)this.service.getProperty(RestrictionSettings.SAVE_QUIT_LOCATION)).booleanValue())
    {
      auth.setQuitLocation(player.getLocation());
      this.database.updateQuitLoc(auth);
      this.bungeeSender.sendAuthMeBungeecordMessage("refresh.quitloc", name);
    }
    this.playerCache.removePlayer(name);
    this.codeManager.unverify(name);
    this.database.setUnlogged(name);
    this.sessionService.revokeSession(name);
    this.bungeeSender.sendAuthMeBungeecordMessage("logout", name);
    this.syncProcessManager.processSyncPlayerLogout(player);
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\process\logout\AsynchronousLogout.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */