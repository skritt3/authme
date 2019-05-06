package fr.xephi.authme.security.crypts;

import fr.xephi.authme.security.crypts.description.HasSalt;
import fr.xephi.authme.security.crypts.description.Recommendation;
import fr.xephi.authme.security.crypts.description.SaltType;
import fr.xephi.authme.security.crypts.description.Usage;

@Recommendation(Usage.DO_NOT_USE)
@HasSalt(SaltType.USERNAME)
public abstract class UsernameSaltMethod
  implements EncryptionMethod
{
  public abstract HashedPassword computeHash(String paramString1, String paramString2);
  
  public String computeHash(String password, String salt, String name)
  {
    return computeHash(password, name).getHash();
  }
  
  public boolean comparePassword(String password, HashedPassword hashedPassword, String name)
  {
    return hashedPassword.getHash().equals(computeHash(password, name).getHash());
  }
  
  public String generateSalt()
  {
    return null;
  }
  
  public boolean hasSeparateSalt()
  {
    return false;
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\security\crypts\UsernameSaltMethod.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */