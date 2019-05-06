package fr.xephi.authme.service.bungeecord;

import fr.xephi.authme.AuthMe;
import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.initialization.SettingsDependent;
import fr.xephi.authme.libs.google.common.io.ByteArrayDataInput;
import fr.xephi.authme.libs.google.common.io.ByteStreams;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.process.Management;
import fr.xephi.authme.service.BukkitService;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.settings.properties.HooksSettings;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class BungeeReceiver
  implements PluginMessageListener, SettingsDependent
{
  private final AuthMe plugin;
  private final BukkitService bukkitService;
  private final Management management;
  private final DataSource dataSource;
  private boolean isEnabled;
  
  @Inject
  BungeeReceiver(AuthMe plugin, BukkitService bukkitService, Management management, DataSource dataSource, Settings settings)
  {
    this.plugin = plugin;
    this.bukkitService = bukkitService;
    this.management = management;
    this.dataSource = dataSource;
    reload(settings);
  }
  
  public void reload(Settings settings)
  {
    this.isEnabled = ((Boolean)settings.getProperty(HooksSettings.BUNGEECORD)).booleanValue();
    if (this.isEnabled)
    {
      Messenger messenger = this.plugin.getServer().getMessenger();
      if (!messenger.isIncomingChannelRegistered(this.plugin, "BungeeCord")) {
        messenger.registerIncomingPluginChannel(this.plugin, "BungeeCord", this);
      }
    }
  }
  
  public void onPluginMessageReceived(String channel, Player player, byte[] data)
  {
    if (!this.isEnabled) {
      return;
    }
    ByteArrayDataInput in = ByteStreams.newDataInput(data);
    String subchannel = in.readUTF();
    if (!"AuthMe".equals(subchannel)) {
      return;
    }
    String type = in.readUTF();
    String name = in.readUTF();
    switch (type)
    {
    case "unregister": 
      this.dataSource.invalidateCache(name);
      break;
    case "refresh.password": 
    case "refresh.quitloc": 
    case "refresh.email": 
    case "refresh": 
      this.dataSource.refreshCache(name);
      break;
    case "bungeelogin": 
      handleBungeeLogin(name);
      break;
    default: 
      ConsoleLogger.debug("Received unsupported bungeecord message type! ({0})", type);
    }
  }
  
  private void handleBungeeLogin(String name)
  {
    Player player = this.bukkitService.getPlayerExact(name);
    if ((player != null) && (player.isOnline()))
    {
      this.management.forceLogin(player);
      ConsoleLogger.info("The user " + player.getName() + " has been automatically logged in, as requested by the AuthMeBungee integration.");
    }
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\service\bungeecord\BungeeReceiver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */