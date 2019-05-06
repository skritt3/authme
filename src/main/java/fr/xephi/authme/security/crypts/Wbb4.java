package fr.xephi.authme.security.crypts;

import fr.xephi.authme.security.crypts.description.Recommendation;
import fr.xephi.authme.security.crypts.description.Usage;

@Recommendation(Usage.RECOMMENDED)
public class Wbb4
  extends HexSaltedMethod
{
  public String computeHash(String password, String salt, String name)
  {
    return BCryptService.hashpw(BCryptService.hashpw(password, salt), salt);
  }
  
  public boolean comparePassword(String password, HashedPassword hashedPassword, String playerName)
  {
    if (hashedPassword.getHash().length() != 60) {
      return false;
    }
    String salt = hashedPassword.getHash().substring(0, 29);
    return computeHash(password, salt, null).equals(hashedPassword.getHash());
  }
  
  public String generateSalt()
  {
    return BCryptService.gensalt(8);
  }
  
  public int getSaltLength()
  {
    return 8;
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\security\crypts\Wbb4.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */