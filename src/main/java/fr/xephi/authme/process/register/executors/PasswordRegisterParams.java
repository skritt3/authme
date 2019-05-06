package fr.xephi.authme.process.register.executors;

import org.bukkit.entity.Player;

public class PasswordRegisterParams
  extends AbstractPasswordRegisterParams
{
  private final String email;
  
  protected PasswordRegisterParams(Player player, String password, String email)
  {
    super(player, password);
    this.email = email;
  }
  
  public static PasswordRegisterParams of(Player player, String password, String email)
  {
    return new PasswordRegisterParams(player, password, email);
  }
  
  public String getEmail()
  {
    return this.email;
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\process\register\executors\PasswordRegisterParams.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */