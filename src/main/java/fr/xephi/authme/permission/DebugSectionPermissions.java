package fr.xephi.authme.permission;

public enum DebugSectionPermissions
  implements PermissionNode
{
  DEBUG_COMMAND("authme.debug.command"),  COUNTRY_LOOKUP("authme.debug.country"),  DATA_STATISTICS("authme.debug.stats"),  HAS_PERMISSION_CHECK("authme.debug.perm"),  INPUT_VALIDATOR("authme.debug.valid"),  LIMBO_PLAYER_VIEWER("authme.debug.limbo"),  PERM_GROUPS("authme.debug.group"),  PLAYER_AUTH_VIEWER("authme.debug.db"),  MYSQL_DEFAULT_CHANGER("authme.debug.mysqldef"),  SPAWN_LOCATION("authme.debug.spawn"),  TEST_EMAIL("authme.debug.mail");
  
  private final String node;
  
  private DebugSectionPermissions(String node)
  {
    this.node = node;
  }
  
  public String getNode()
  {
    return this.node;
  }
  
  public DefaultPermission getDefaultPermission()
  {
    return DefaultPermission.OP_ONLY;
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\permission\DebugSectionPermissions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */