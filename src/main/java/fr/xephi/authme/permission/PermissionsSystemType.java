//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package fr.xephi.authme.permission;

public enum PermissionsSystemType {
  LUCK_PERMS("LuckPerms", "LuckPerms"),
  PERMISSIONS_EX("PermissionsEx", "PermissionsEx"),
  B_PERMISSIONS("bPermissions", "bPermissions"),
  Z_PERMISSIONS("zPermissions", "zPermissions"),
  VAULT("Vault", "Vault");

  private String displayName;
  private String pluginName;

  private PermissionsSystemType(String displayName, String pluginName) {
    this.displayName = displayName;
    this.pluginName = pluginName;
  }

  public String getDisplayName() {
    return this.displayName;
  }

  public String getPluginName() {
    return this.pluginName;
  }

  public String toString() {
    return this.getDisplayName();
  }

  public static boolean isPermissionSystem(String name) {
    PermissionsSystemType[] var1 = values();
    int var2 = var1.length;

    for(int var3 = 0; var3 < var2; ++var3) {
      PermissionsSystemType permissionsSystemType = var1[var3];
      if (permissionsSystemType.pluginName.equals(name)) {
        return true;
      }
    }

    return false;
  }
}
