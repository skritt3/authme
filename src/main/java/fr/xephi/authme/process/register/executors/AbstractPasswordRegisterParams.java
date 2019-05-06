package fr.xephi.authme.process.register.executors;

import fr.xephi.authme.security.crypts.HashedPassword;
import org.bukkit.entity.Player;

public abstract class AbstractPasswordRegisterParams
  extends RegistrationParameters
{
  private final String password;
  private HashedPassword hashedPassword;
  
  public AbstractPasswordRegisterParams(Player player, String password)
  {
    super(player);
    this.password = password;
  }
  
  public AbstractPasswordRegisterParams(Player player)
  {
    this(player, null);
  }
  
  public String getPassword()
  {
    return this.password;
  }
  
  void setHashedPassword(HashedPassword hashedPassword)
  {
    this.hashedPassword = hashedPassword;
  }
  
  HashedPassword getHashedPassword()
  {
    return this.hashedPassword;
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\process\register\executors\AbstractPasswordRegisterParams.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */