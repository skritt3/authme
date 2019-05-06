package fr.xephi.authme.listener;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.listener.protocollib.ProtocolLibService;
import fr.xephi.authme.permission.PermissionsManager;
import fr.xephi.authme.service.PluginHookService;
import fr.xephi.authme.settings.SpawnLoader;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;

public class ServerListener
  implements Listener
{
  @Inject
  private PluginHookService pluginHookService;
  @Inject
  private SpawnLoader spawnLoader;
  @Inject
  private ProtocolLibService protocolLibService;
  @Inject
  private PermissionsManager permissionsManager;
  
  @EventHandler(priority=EventPriority.HIGHEST)
  public void onPluginDisable(PluginDisableEvent event)
  {
    if (event.getPlugin() == null) {
      return;
    }
    String pluginName = event.getPlugin().getName();
    
    this.permissionsManager.onPluginDisable(pluginName);
    if ("Essentials".equalsIgnoreCase(pluginName))
    {
      this.pluginHookService.unhookEssentials();
      ConsoleLogger.info("Essentials has been disabled: unhooking");
    }
    else if ("Multiverse-Core".equalsIgnoreCase(pluginName))
    {
      this.pluginHookService.unhookMultiverse();
      ConsoleLogger.info("Multiverse-Core has been disabled: unhooking");
    }
    else if ("EssentialsSpawn".equalsIgnoreCase(pluginName))
    {
      this.spawnLoader.unloadEssentialsSpawn();
      ConsoleLogger.info("EssentialsSpawn has been disabled: unhooking");
    }
    else if ("ProtocolLib".equalsIgnoreCase(pluginName))
    {
      this.protocolLibService.disable();
      ConsoleLogger.warning("ProtocolLib has been disabled, unhooking packet adapters!");
    }
  }
  
  @EventHandler(priority=EventPriority.HIGHEST)
  public void onPluginEnable(PluginEnableEvent event)
  {
    if (event.getPlugin() == null) {
      return;
    }
    String pluginName = event.getPlugin().getName();
    
    this.permissionsManager.onPluginEnable(pluginName);
    if ("Essentials".equalsIgnoreCase(pluginName)) {
      this.pluginHookService.tryHookToEssentials();
    } else if ("Multiverse-Core".equalsIgnoreCase(pluginName)) {
      this.pluginHookService.tryHookToMultiverse();
    } else if ("EssentialsSpawn".equalsIgnoreCase(pluginName)) {
      this.spawnLoader.loadEssentialsSpawn();
    } else if ("ProtocolLib".equalsIgnoreCase(pluginName)) {
      this.protocolLibService.setup();
    }
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\listener\ServerListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */