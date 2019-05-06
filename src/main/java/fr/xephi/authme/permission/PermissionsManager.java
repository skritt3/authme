package fr.xephi.authme.permission;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.initialization.Reloadable;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.listener.JoiningPlayer;
import fr.xephi.authme.permission.handlers.BPermissionsHandler;
import fr.xephi.authme.permission.handlers.LuckPermsHandler;
import fr.xephi.authme.permission.handlers.PermissionHandler;
import fr.xephi.authme.permission.handlers.PermissionHandlerException;
import fr.xephi.authme.permission.handlers.PermissionsExHandler;
import fr.xephi.authme.permission.handlers.VaultHandler;
import fr.xephi.authme.permission.handlers.ZPermissionsHandler;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.settings.properties.PluginSettings;
import fr.xephi.authme.util.StringUtils;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;
import java.util.function.BiFunction;
import javax.annotation.PostConstruct;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public class PermissionsManager
  implements Reloadable
{
  private final Server server;
  private final PluginManager pluginManager;
  private Settings settings;
  private PermissionHandler handler = null;
  
  @Inject
  public PermissionsManager(Server server, PluginManager pluginManager, Settings settings)
  {
    this.server = server;
    this.pluginManager = pluginManager;
    this.settings = settings;
  }
  
  public boolean isEnabled()
  {
    return this.handler != null;
  }
  
  @PostConstruct
  private void setup()
  {
    if (((Boolean)this.settings.getProperty(PluginSettings.FORCE_VAULT_HOOK)).booleanValue()) {
      try
      {
        PermissionHandler handler = createPermissionHandler(PermissionsSystemType.VAULT);
        if (handler != null)
        {
          this.handler = handler;
          ConsoleLogger.info("Hooked into " + PermissionsSystemType.VAULT.getDisplayName() + "!");
          return;
        }
      }
      catch (PermissionHandlerException e)
      {
        ConsoleLogger.logException("Failed to create Vault hook (forced):", e);
      }
    }
    for (PermissionsSystemType type : PermissionsSystemType.values()) {
      try
      {
        PermissionHandler handler = createPermissionHandler(type);
        if (handler != null)
        {
          this.handler = handler;
          ConsoleLogger.info("Hooked into " + type.getDisplayName() + "!");
          return;
        }
      }
      catch (Exception ex)
      {
        ConsoleLogger.logException("Error while hooking into " + type.getDisplayName(), ex);
      }
    }
    ConsoleLogger.info("No supported permissions system found! Permissions are disabled!");
  }
  
  private PermissionHandler createPermissionHandler(PermissionsSystemType type)
    throws PermissionHandlerException
  {
    Plugin plugin = this.pluginManager.getPlugin(type.getPluginName());
    if (plugin == null) {
      return null;
    }
    if (!plugin.isEnabled())
    {
      ConsoleLogger.info("Not hooking into " + type.getDisplayName() + " because it's disabled!");
      return null;
    }
    switch (type)
    {
    case LUCK_PERMS: 
      return new LuckPermsHandler();
    case PERMISSIONS_EX: 
      return new PermissionsExHandler();
    case Z_PERMISSIONS: 
      return new ZPermissionsHandler();
    case VAULT: 
      return new VaultHandler(this.server);
    case B_PERMISSIONS: 
      return new BPermissionsHandler();
    }
    throw new IllegalStateException("Unhandled permission type '" + type + "'");
  }
  
  private void unhook()
  {
    this.handler = null;
    
    ConsoleLogger.info("Unhooked from Permissions!");
  }
  
  public void reload()
  {
    unhook();
    
    setup();
  }
  
  public void onPluginEnable(String pluginName)
  {
    if (PermissionsSystemType.isPermissionSystem(pluginName))
    {
      ConsoleLogger.info(pluginName + " plugin enabled, dynamically updating permissions hooks!");
      setup();
    }
  }
  
  public void onPluginDisable(String pluginName)
  {
    if (PermissionsSystemType.isPermissionSystem(pluginName))
    {
      ConsoleLogger.info(pluginName + " plugin disabled, updating hooks!");
      setup();
    }
  }
  
  public PermissionsSystemType getPermissionSystem()
  {
    return isEnabled() ? this.handler.getPermissionSystem() : null;
  }
  
  public boolean hasPermission(CommandSender sender, PermissionNode permissionNode)
  {
    if (permissionNode == null) {
      return true;
    }
    if ((!(sender instanceof Player)) || (!isEnabled())) {
      return permissionNode.getDefaultPermission().evaluate(sender);
    }
    Player player = (Player)sender;
    return player.hasPermission(permissionNode.getNode());
  }
  
  public boolean hasPermission(JoiningPlayer joiningPlayer, PermissionNode permissionNode)
  {
    return ((Boolean)joiningPlayer.getPermissionLookupFunction().apply(this, permissionNode)).booleanValue();
  }
  
  public boolean hasPermissionOffline(OfflinePlayer player, PermissionNode permissionNode)
  {
    if (permissionNode == null) {
      return true;
    }
    if (!isEnabled()) {
      return permissionNode.getDefaultPermission().evaluate(player);
    }
    return this.handler.hasPermissionOffline(player.getName(), permissionNode);
  }
  
  public boolean hasPermissionOffline(String name, PermissionNode permissionNode)
  {
    if (permissionNode == null) {
      return true;
    }
    if (!isEnabled()) {
      return permissionNode.getDefaultPermission().evaluate(null);
    }
    return this.handler.hasPermissionOffline(name, permissionNode);
  }
  
  public boolean hasGroupSupport()
  {
    return (isEnabled()) && (this.handler.hasGroupSupport());
  }
  
  public Collection<String> getGroups(OfflinePlayer player)
  {
    return isEnabled() ? this.handler.getGroups(player) : Collections.emptyList();
  }
  
  public String getPrimaryGroup(OfflinePlayer player)
  {
    return isEnabled() ? this.handler.getPrimaryGroup(player) : null;
  }
  
  public boolean isInGroup(OfflinePlayer player, String groupName)
  {
    return (isEnabled()) && (this.handler.isInGroup(player, groupName));
  }
  
  public boolean addGroup(OfflinePlayer player, String groupName)
  {
    if ((!isEnabled()) || (StringUtils.isEmpty(groupName))) {
      return false;
    }
    return this.handler.addToGroup(player, groupName);
  }
  
  public boolean addGroups(OfflinePlayer player, Collection<String> groupNames)
  {
    if (!isEnabled()) {
      return false;
    }
    boolean result = false;
    for (String groupName : groupNames) {
      if (!groupName.isEmpty()) {
        result |= this.handler.addToGroup(player, groupName);
      }
    }
    return result;
  }
  
  public boolean removeGroup(OfflinePlayer player, String groupName)
  {
    return (isEnabled()) && (this.handler.removeFromGroup(player, groupName));
  }
  
  public boolean removeGroups(OfflinePlayer player, Collection<String> groupNames)
  {
    if (!isEnabled()) {
      return false;
    }
    boolean result = false;
    for (String groupName : groupNames) {
      if (!groupName.isEmpty()) {
        result |= this.handler.removeFromGroup(player, groupName);
      }
    }
    return result;
  }
  
  public boolean setGroup(OfflinePlayer player, String groupName)
  {
    return (isEnabled()) && (this.handler.setGroup(player, groupName));
  }
  
  public boolean removeAllGroups(OfflinePlayer player)
  {
    if (!isEnabled()) {
      return false;
    }
    Collection<String> groupNames = getGroups(player);
    
    return removeGroups(player, groupNames);
  }
  
  public void loadUserData(UUID uuid)
  {
    if (!isEnabled()) {
      return;
    }
    this.handler.loadUserData(uuid);
  }
  
  public void loadUserData(String name)
  {
    if (!isEnabled()) {
      return;
    }
    this.handler.loadUserData(name);
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\permission\PermissionsManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */