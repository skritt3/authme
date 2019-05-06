package fr.xephi.authme.security.crypts;

import fr.xephi.authme.security.crypts.description.HasSalt;
import fr.xephi.authme.security.crypts.description.Recommendation;
import fr.xephi.authme.security.crypts.description.SaltType;
import fr.xephi.authme.security.crypts.description.Usage;

@Recommendation(Usage.DO_NOT_USE)
@HasSalt(SaltType.NONE)
public abstract class UnsaltedMethod
  implements EncryptionMethod
{
  public abstract String computeHash(String paramString);
  
  public HashedPassword computeHash(String password, String name)
  {
    return new HashedPassword(computeHash(password));
  }
  
  public String computeHash(String password, String salt, String name)
  {
    return computeHash(password);
  }
  
  public boolean comparePassword(String password, HashedPassword hashedPassword, String name)
  {
    return hashedPassword.getHash().equals(computeHash(password));
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


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\security\crypts\UnsaltedMethod.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */