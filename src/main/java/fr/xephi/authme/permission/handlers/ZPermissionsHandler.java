package fr.xephi.authme.permission.handlers;

import fr.xephi.authme.permission.PermissionNode;
import fr.xephi.authme.permission.PermissionsSystemType;
import java.util.Collection;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.ServicesManager;
import org.tyrannyofheaven.bukkit.zPermissions.ZPermissionsService;

public class ZPermissionsHandler
  implements PermissionHandler
{
  private ZPermissionsService zPermissionsService;
  
  public ZPermissionsHandler()
    throws PermissionHandlerException
  {
    ZPermissionsService zPermissionsService = (ZPermissionsService)Bukkit.getServicesManager().load(ZPermissionsService.class);
    if (zPermissionsService == null) {
      throw new PermissionHandlerException("Failed to get the ZPermissions service!");
    }
    this.zPermissionsService = zPermissionsService;
  }
  
  public boolean addToGroup(OfflinePlayer player, String group)
  {
    return Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "permissions player " + player
      .getName() + " addgroup " + group);
  }
  
  public boolean hasGroupSupport()
  {
    return true;
  }
  
  public boolean hasPermissionOffline(String name, PermissionNode node)
  {
    Map<String, Boolean> perms = this.zPermissionsService.getPlayerPermissions(null, null, name);
    return ((Boolean)perms.getOrDefault(node.getNode(), Boolean.valueOf(false))).booleanValue();
  }
  
  public boolean removeFromGroup(OfflinePlayer player, String group)
  {
    return Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "permissions player " + player
      .getName() + " removegroup " + group);
  }
  
  public boolean setGroup(OfflinePlayer player, String group)
  {
    return Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "permissions player " + player
      .getName() + " setgroup " + group);
  }
  
  public Collection<String> getGroups(OfflinePlayer player)
  {
    return this.zPermissionsService.getPlayerGroups(player.getName());
  }
  
  public String getPrimaryGroup(OfflinePlayer player)
  {
    return this.zPermissionsService.getPlayerPrimaryGroup(player.getName());
  }
  
  public PermissionsSystemType getPermissionSystem()
  {
    return PermissionsSystemType.Z_PERMISSIONS;
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\permission\handlers\ZPermissionsHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */