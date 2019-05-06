package fr.xephi.authme.process.login;

import fr.xephi.authme.data.auth.PlayerAuth;
import fr.xephi.authme.data.limbo.LimboPlayer;
import fr.xephi.authme.data.limbo.LimboService;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.events.LoginEvent;
import fr.xephi.authme.events.RestoreInventoryEvent;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.process.SynchronousProcess;
import fr.xephi.authme.service.BukkitService;
import fr.xephi.authme.service.CommonService;
import fr.xephi.authme.service.JoinMessageService;
import fr.xephi.authme.service.TeleportationService;
import fr.xephi.authme.service.bungeecord.BungeeSender;
import fr.xephi.authme.settings.WelcomeMessageConfiguration;
import fr.xephi.authme.settings.commandconfig.CommandManager;
import fr.xephi.authme.settings.properties.RegistrationSettings;
import fr.xephi.authme.settings.properties.RestrictionSettings;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class ProcessSyncPlayerLogin
  implements SynchronousProcess
{
  @Inject
  private BungeeSender bungeeSender;
  @Inject
  private LimboService limboService;
  @Inject
  private BukkitService bukkitService;
  @Inject
  private TeleportationService teleportationService;
  @Inject
  private DataSource dataSource;
  @Inject
  private CommandManager commandManager;
  @Inject
  private CommonService commonService;
  @Inject
  private WelcomeMessageConfiguration welcomeMessageConfiguration;
  @Inject
  private JoinMessageService joinMessageService;
  
  private void restoreInventory(Player player)
  {
    RestoreInventoryEvent event = new RestoreInventoryEvent(player);
    this.bukkitService.callEvent(event);
    if (!event.isCancelled()) {
      player.updateInventory();
    }
  }
  
  public void processPlayerLogin(Player player)
  {
    String name = player.getName().toLowerCase();
    LimboPlayer limbo = this.limboService.getLimboPlayer(name);
    if (limbo != null) {
      this.limboService.restoreData(player);
    }
    if (((Boolean)this.commonService.getProperty(RestrictionSettings.PROTECT_INVENTORY_BEFORE_LOGIN)).booleanValue()) {
      restoreInventory(player);
    }
    PlayerAuth auth = this.dataSource.getAuth(name);
    this.teleportationService.teleportOnLogin(player, auth, limbo);
    
    this.joinMessageService.sendMessage(name);
    if (((Boolean)this.commonService.getProperty(RegistrationSettings.APPLY_BLIND_EFFECT)).booleanValue()) {
      player.removePotionEffect(PotionEffectType.BLINDNESS);
    }
    this.bukkitService.callEvent(new LoginEvent(player));
    player.saveData();
    
    this.welcomeMessageConfiguration.sendWelcomeMessage(player);
    
    this.commandManager.runCommandsOnLogin(player);
    
    this.bungeeSender.connectPlayerOnLogin(player);
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\process\login\ProcessSyncPlayerLogin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */