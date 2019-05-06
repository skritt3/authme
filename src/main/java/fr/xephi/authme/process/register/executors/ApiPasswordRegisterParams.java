package fr.xephi.authme.process.register.executors;

import org.bukkit.entity.Player;

public class ApiPasswordRegisterParams
  extends PasswordRegisterParams
{
  private final boolean loginAfterRegister;
  
  protected ApiPasswordRegisterParams(Player player, String password, boolean loginAfterRegister)
  {
    super(player, password, null);
    this.loginAfterRegister = loginAfterRegister;
  }
  
  public static ApiPasswordRegisterParams of(Player player, String password, boolean loginAfterRegister)
  {
    return new ApiPasswordRegisterParams(player, password, loginAfterRegister);
  }
  
  public boolean getLoginAfterRegister()
  {
    return this.loginAfterRegister;
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\process\register\executors\ApiPasswordRegisterParams.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */