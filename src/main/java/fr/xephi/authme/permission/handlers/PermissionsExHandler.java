package fr.xephi.authme.permission.handlers;

import fr.xephi.authme.permission.PermissionNode;
import fr.xephi.authme.permission.PermissionsSystemType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.bukkit.OfflinePlayer;
import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class PermissionsExHandler
  implements PermissionHandler
{
  private PermissionManager permissionManager;
  
  public PermissionsExHandler()
    throws PermissionHandlerException
  {
    this.permissionManager = PermissionsEx.getPermissionManager();
    if (this.permissionManager == null) {
      throw new PermissionHandlerException("Could not get manager of PermissionsEx");
    }
  }
  
  public boolean addToGroup(OfflinePlayer player, String group)
  {
    if (!PermissionsEx.getPermissionManager().getGroupNames().contains(group)) {
      return false;
    }
    PermissionUser user = PermissionsEx.getUser(player.getName());
    user.addGroup(group);
    return true;
  }
  
  public boolean hasGroupSupport()
  {
    return true;
  }
  
  public boolean hasPermissionOffline(String name, PermissionNode node)
  {
    PermissionUser user = this.permissionManager.getUser(name);
    return user.has(node.getNode());
  }
  
  public boolean isInGroup(OfflinePlayer player, String group)
  {
    PermissionUser user = this.permissionManager.getUser(player.getName());
    return user.inGroup(group);
  }
  
  public boolean removeFromGroup(OfflinePlayer player, String group)
  {
    PermissionUser user = this.permissionManager.getUser(player.getName());
    user.removeGroup(group);
    return true;
  }
  
  public boolean setGroup(OfflinePlayer player, String group)
  {
    List<String> groups = new ArrayList();
    groups.add(group);
    
    PermissionUser user = this.permissionManager.getUser(player.getName());
    user.setParentsIdentifier(groups);
    return true;
  }
  
  public List<String> getGroups(OfflinePlayer player)
  {
    PermissionUser user = this.permissionManager.getUser(player.getName());
    return user.getParentIdentifiers(null);
  }
  
  public PermissionsSystemType getPermissionSystem()
  {
    return PermissionsSystemType.PERMISSIONS_EX;
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\permission\handlers\PermissionsExHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */