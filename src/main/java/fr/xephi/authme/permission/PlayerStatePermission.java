package fr.xephi.authme.permission;

public enum PlayerStatePermission
  implements PermissionNode
{
  BYPASS_ANTIBOT("authme.bypassantibot", DefaultPermission.OP_ONLY),  BYPASS_FORCE_SURVIVAL("authme.bypassforcesurvival", DefaultPermission.OP_ONLY),  IS_VIP("authme.vip", DefaultPermission.OP_ONLY),  ALLOW_MULTIPLE_ACCOUNTS("authme.allowmultipleaccounts", DefaultPermission.OP_ONLY),  BYPASS_PURGE("authme.bypasspurge", DefaultPermission.NOT_ALLOWED),  BYPASS_COUNTRY_CHECK("authme.bypasscountrycheck", DefaultPermission.NOT_ALLOWED);
  
  private String node;
  private DefaultPermission defaultPermission;
  
  private PlayerStatePermission(String node, DefaultPermission defaultPermission)
  {
    this.node = node;
    this.defaultPermission = defaultPermission;
  }
  
  public String getNode()
  {
    return this.node;
  }
  
  public DefaultPermission getDefaultPermission()
  {
    return this.defaultPermission;
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\permission\PlayerStatePermission.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */