package fr.xephi.authme.permission;

public enum PlayerPermission
  implements PermissionNode
{
  LOGIN("authme.player.login"),  LOGOUT("authme.player.logout"),  REGISTER("authme.player.register"),  UNREGISTER("authme.player.unregister"),  CHANGE_PASSWORD("authme.player.changepassword"),  SEE_EMAIL("authme.player.email.see"),  ADD_EMAIL("authme.player.email.add"),  CHANGE_EMAIL("authme.player.email.change"),  RECOVER_EMAIL("authme.player.email.recover"),  CAPTCHA("authme.player.captcha"),  CAN_LOGIN_BE_FORCED("authme.player.canbeforced"),  SEE_OWN_ACCOUNTS("authme.player.seeownaccounts"),  VERIFICATION_CODE("authme.player.security.verificationcode");
  
  private String node;
  
  private PlayerPermission(String node)
  {
    this.node = node;
  }
  
  public String getNode()
  {
    return this.node;
  }
  
  public DefaultPermission getDefaultPermission()
  {
    return DefaultPermission.ALLOWED;
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\permission\PlayerPermission.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */