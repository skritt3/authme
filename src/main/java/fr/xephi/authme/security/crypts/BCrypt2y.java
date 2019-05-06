package fr.xephi.authme.security.crypts;

import fr.xephi.authme.security.crypts.description.Recommendation;
import fr.xephi.authme.security.crypts.description.Usage;

@Recommendation(Usage.RECOMMENDED)
public class BCrypt2y
  extends HexSaltedMethod
{
  public String computeHash(String password, String salt, String name)
  {
    if (salt.length() == 22) {
      salt = "$2y$10$" + salt;
    }
    return BCryptService.hashpw(password, salt);
  }
  
  public boolean comparePassword(String password, HashedPassword encrypted, String unusedName)
  {
    String hash = encrypted.getHash();
    if (hash.length() != 60) {
      return false;
    }
    String salt = hash.substring(0, 29);
    return hash.equals(computeHash(password, salt, null));
  }
  
  public int getSaltLength()
  {
    return 22;
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\security\crypts\BCrypt2y.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */