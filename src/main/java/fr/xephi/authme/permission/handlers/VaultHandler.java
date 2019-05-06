//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package fr.xephi.authme.permission.handlers;

import fr.xephi.authme.permission.PermissionNode;
import fr.xephi.authme.permission.PermissionsSystemType;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import net.milkbowl.vault.permission.Permission;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultHandler implements PermissionHandler {
  private Permission vaultProvider;

  public VaultHandler(Server server) throws PermissionHandlerException {
    this.vaultProvider = getVaultPermission(server);
  }

  private static Permission getVaultPermission(Server server) throws PermissionHandlerException {
    RegisteredServiceProvider<Permission> permissionProvider = server.getServicesManager().getRegistration(Permission.class);
    if (permissionProvider == null) {
      throw new PermissionHandlerException("Could not load permissions provider service");
    } else {
      Permission vaultPerms = (Permission)permissionProvider.getProvider();
      if (vaultPerms == null) {
        throw new PermissionHandlerException("Could not load Vault permissions provider");
      } else {
        return vaultPerms;
      }
    }
  }

  public boolean addToGroup(OfflinePlayer player, String group) {
    return this.vaultProvider.playerAddGroup((String)null, player, group);
  }

  public boolean hasGroupSupport() {
    return this.vaultProvider.hasGroupSupport();
  }

  public boolean hasPermissionOffline(String name, PermissionNode node) {
    return this.vaultProvider.has((String)null, name, node.getNode());
  }

  public boolean isInGroup(OfflinePlayer player, String group) {
    return this.vaultProvider.playerInGroup((String)null, player, group);
  }

  public boolean removeFromGroup(OfflinePlayer player, String group) {
    return this.vaultProvider.playerRemoveGroup((String)null, player, group);
  }

  public boolean setGroup(OfflinePlayer player, String group) {
    Iterator var3 = this.getGroups(player).iterator();

    while(var3.hasNext()) {
      String groupName = (String)var3.next();
      this.removeFromGroup(player, groupName);
    }

    return this.vaultProvider.playerAddGroup((String)null, player, group);
  }

  public List<String> getGroups(OfflinePlayer player) {
    return Arrays.asList(this.vaultProvider.getPlayerGroups((String)null, player));
  }

  public String getPrimaryGroup(OfflinePlayer player) {
    return this.vaultProvider.getPrimaryGroup((String)null, player);
  }

  public PermissionsSystemType getPermissionSystem() {
    return PermissionsSystemType.VAULT;
  }

  @Override
  public void loadUserData(UUID uuid) {

  }

  @Override
  public void loadUserData(String name) {

  }
}
