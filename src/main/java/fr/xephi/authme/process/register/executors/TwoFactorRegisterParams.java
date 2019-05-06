package fr.xephi.authme.process.register.executors;

import org.bukkit.entity.Player;

public class TwoFactorRegisterParams
  extends AbstractPasswordRegisterParams
{
  protected TwoFactorRegisterParams(Player player)
  {
    super(player);
  }
  
  public static TwoFactorRegisterParams of(Player player)
  {
    return new TwoFactorRegisterParams(player);
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\process\register\executors\TwoFactorRegisterParams.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */