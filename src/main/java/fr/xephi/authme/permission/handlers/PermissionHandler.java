//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package fr.xephi.authme.permission.handlers;

import fr.xephi.authme.permission.PermissionNode;
import fr.xephi.authme.permission.PermissionsSystemType;
import fr.xephi.authme.util.Utils;
import java.util.Collection;
import java.util.UUID;
import org.bukkit.OfflinePlayer;

public interface PermissionHandler {
  boolean addToGroup(OfflinePlayer var1, String var2);

  boolean hasGroupSupport();

  boolean hasPermissionOffline(String var1, PermissionNode var2);

  default boolean isInGroup(OfflinePlayer player, String group) {
    return this.getGroups(player).contains(group);
  }

  boolean removeFromGroup(OfflinePlayer var1, String var2);

  boolean setGroup(OfflinePlayer var1, String var2);

  Collection<String> getGroups(OfflinePlayer var1);

  default String getPrimaryGroup(OfflinePlayer player) {
    Collection<String> groups = this.getGroups(player);
    return Utils.isCollectionEmpty(groups) ? null : (String)groups.iterator().next();
  }

  PermissionsSystemType getPermissionSystem();

  default void loadUserData(UUID uuid) {
  }

  default void loadUserData(String name) {
  }
}
