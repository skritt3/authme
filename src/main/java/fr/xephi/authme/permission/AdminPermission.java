package fr.xephi.authme.permission;

public enum AdminPermission
  implements PermissionNode
{
  REGISTER("authme.admin.register"),  UNREGISTER("authme.admin.unregister"),  FORCE_LOGIN("authme.admin.forcelogin"),  CHANGE_PASSWORD("authme.admin.changepassword"),  LAST_LOGIN("authme.admin.lastlogin"),  ACCOUNTS("authme.admin.accounts"),  GET_EMAIL("authme.admin.getemail"),  CHANGE_EMAIL("authme.admin.changemail"),  GET_IP("authme.admin.getip"),  SPAWN("authme.admin.spawn"),  SET_SPAWN("authme.admin.setspawn"),  FIRST_SPAWN("authme.admin.firstspawn"),  SET_FIRST_SPAWN("authme.admin.setfirstspawn"),  PURGE("authme.admin.purge"),  PURGE_LAST_POSITION("authme.admin.purgelastpos"),  PURGE_BANNED_PLAYERS("authme.admin.purgebannedplayers"),  PURGE_PLAYER("authme.admin.purgeplayer"),  SWITCH_ANTIBOT("authme.admin.switchantibot"),  CONVERTER("authme.admin.converter"),  RELOAD("authme.admin.reload"),  ANTIBOT_MESSAGES("authme.admin.antibotmessages"),  UPDATE_MESSAGES("authme.admin.updatemessages"),  SEE_OTHER_ACCOUNTS("authme.admin.seeotheraccounts"),  BACKUP("authme.admin.backup");
  
  private String node;
  
  private AdminPermission(String node)
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


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\permission\AdminPermission.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */