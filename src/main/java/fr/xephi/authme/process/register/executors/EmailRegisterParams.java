package fr.xephi.authme.process.register.executors;

import org.bukkit.entity.Player;

public class EmailRegisterParams
  extends RegistrationParameters
{
  private final String email;
  private String password;
  
  protected EmailRegisterParams(Player player, String email)
  {
    super(player);
    this.email = email;
  }
  
  public static EmailRegisterParams of(Player player, String email)
  {
    return new EmailRegisterParams(player, email);
  }
  
  public String getEmail()
  {
    return this.email;
  }
  
  void setPassword(String password)
  {
    this.password = password;
  }
  
  String getPassword()
  {
    return this.password;
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\process\register\executors\EmailRegisterParams.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */