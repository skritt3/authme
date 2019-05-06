package fr.xephi.authme.listener.protocollib;

import fr.xephi.authme.AuthMe;
import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.data.auth.PlayerCache;
import fr.xephi.authme.initialization.SettingsDependent;
import fr.xephi.authme.libs.jalu.injector.annotations.NoFieldScan;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.service.BukkitService;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.settings.properties.RestrictionSettings;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

@NoFieldScan
public class ProtocolLibService
  implements SettingsDependent
{
  private InventoryPacketAdapter inventoryPacketAdapter;
  private TabCompletePacketAdapter tabCompletePacketAdapter;
  private boolean protectInvBeforeLogin;
  private boolean denyTabCompleteBeforeLogin;
  private boolean isEnabled;
  private AuthMe plugin;
  private BukkitService bukkitService;
  private PlayerCache playerCache;
  
  @Inject
  ProtocolLibService(AuthMe plugin, Settings settings, BukkitService bukkitService, PlayerCache playerCache)
  {
    this.plugin = plugin;
    this.bukkitService = bukkitService;
    this.playerCache = playerCache;
    reload(settings);
  }
  
  public void setup()
  {
    if (!this.plugin.getServer().getPluginManager().isPluginEnabled("ProtocolLib"))
    {
      if (this.protectInvBeforeLogin) {
        ConsoleLogger.warning("WARNING! The protectInventory feature requires ProtocolLib! Disabling it...");
      }
      if (this.denyTabCompleteBeforeLogin) {
        ConsoleLogger.warning("WARNING! The denyTabComplete feature requires ProtocolLib! Disabling it...");
      }
      this.isEnabled = false;
      return;
    }
    if ((this.protectInvBeforeLogin) && (this.inventoryPacketAdapter == null))
    {
      this.inventoryPacketAdapter = new InventoryPacketAdapter(this.plugin, this.playerCache);
      this.inventoryPacketAdapter.register();
    }
    else if (this.inventoryPacketAdapter != null)
    {
      this.inventoryPacketAdapter.unregister();
      this.inventoryPacketAdapter = null;
    }
    if ((this.denyTabCompleteBeforeLogin) && (this.tabCompletePacketAdapter == null))
    {
      this.tabCompletePacketAdapter = new TabCompletePacketAdapter(this.plugin, this.playerCache);
      this.tabCompletePacketAdapter.register();
    }
    else if (this.tabCompletePacketAdapter != null)
    {
      this.tabCompletePacketAdapter.unregister();
      this.tabCompletePacketAdapter = null;
    }
    this.isEnabled = true;
  }
  
  public void disable()
  {
    this.isEnabled = false;
    if (this.inventoryPacketAdapter != null)
    {
      this.inventoryPacketAdapter.unregister();
      this.inventoryPacketAdapter = null;
    }
    if (this.tabCompletePacketAdapter != null)
    {
      this.tabCompletePacketAdapter.unregister();
      this.tabCompletePacketAdapter = null;
    }
  }
  
  public void sendBlankInventoryPacket(Player player)
  {
    if ((this.isEnabled) && (this.inventoryPacketAdapter != null)) {
      this.inventoryPacketAdapter.sendBlankInventoryPacket(player);
    }
  }
  
  public void reload(Settings settings)
  {
    boolean oldProtectInventory = this.protectInvBeforeLogin;
    
    this.protectInvBeforeLogin = ((Boolean)settings.getProperty(RestrictionSettings.PROTECT_INVENTORY_BEFORE_LOGIN)).booleanValue();
    this.denyTabCompleteBeforeLogin = ((Boolean)settings.getProperty(RestrictionSettings.DENY_TABCOMPLETE_BEFORE_LOGIN)).booleanValue();
    if ((oldProtectInventory) && (!this.protectInvBeforeLogin))
    {
      this.inventoryPacketAdapter.unregister();
      for (Player onlinePlayer : this.bukkitService.getOnlinePlayers()) {
        if (!this.playerCache.isAuthenticated(onlinePlayer.getName())) {
          onlinePlayer.updateInventory();
        }
      }
    }
    setup();
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\listener\protocollib\ProtocolLibService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */