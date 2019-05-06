package fr.xephi.authme.security.crypts;

import fr.xephi.authme.security.crypts.description.Recommendation;
import fr.xephi.authme.security.crypts.description.Usage;

@Recommendation(Usage.RECOMMENDED)
public class XAuth
  extends HexSaltedMethod
{
  private static String getWhirlpool(String message)
  {
    Whirlpool w = new Whirlpool();
    byte[] digest = new byte[64];
    w.NESSIEinit();
    w.NESSIEadd(message);
    w.NESSIEfinalize(digest);
    return Whirlpool.display(digest);
  }
  
  public String computeHash(String password, String salt, String name)
  {
    String hash = getWhirlpool(salt + password).toLowerCase();
    int saltPos = password.length() >= hash.length() ? hash.length() - 1 : password.length();
    return hash.substring(0, saltPos) + salt + hash.substring(saltPos);
  }
  
  public boolean comparePassword(String password, HashedPassword hashedPassword, String playerName)
  {
    String hash = hashedPassword.getHash();
    int saltPos = password.length() >= hash.length() ? hash.length() - 1 : password.length();
    if (saltPos + 12 > hash.length()) {
      return false;
    }
    String salt = hash.substring(saltPos, saltPos + 12);
    return hash.equals(computeHash(password, salt, null));
  }
  
  public int getSaltLength()
  {
    return 12;
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\security\crypts\XAuth.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */