package fr.xephi.authme.permission.handlers;

import de.bananaco.bpermissions.api.ApiLayer;
import de.bananaco.bpermissions.api.CalculableType;
import fr.xephi.authme.permission.PermissionNode;
import fr.xephi.authme.permission.PermissionsSystemType;
import java.util.Arrays;
import java.util.List;
import org.bukkit.OfflinePlayer;

public class BPermissionsHandler
  implements PermissionHandler
{
  public boolean addToGroup(OfflinePlayer player, String group)
  {
    ApiLayer.addGroup(null, CalculableType.USER, player.getName(), group);
    return true;
  }
  
  public boolean hasGroupSupport()
  {
    return true;
  }
  
  public boolean hasPermissionOffline(String name, PermissionNode node)
  {
    return ApiLayer.hasPermission(null, CalculableType.USER, name, node.getNode());
  }
  
  public boolean isInGroup(OfflinePlayer player, String group)
  {
    return ApiLayer.hasGroup(null, CalculableType.USER, player.getName(), group);
  }
  
  public boolean removeFromGroup(OfflinePlayer player, String group)
  {
    ApiLayer.removeGroup(null, CalculableType.USER, player.getName(), group);
    return true;
  }
  
  public boolean setGroup(OfflinePlayer player, String group)
  {
    ApiLayer.setGroup(null, CalculableType.USER, player.getName(), group);
    return true;
  }
  
  public List<String> getGroups(OfflinePlayer player)
  {
    return Arrays.asList(ApiLayer.getGroups(null, CalculableType.USER, player.getName()));
  }
  
  public PermissionsSystemType getPermissionSystem()
  {
    return PermissionsSystemType.B_PERMISSIONS;
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\permission\handlers\BPermissionsHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */