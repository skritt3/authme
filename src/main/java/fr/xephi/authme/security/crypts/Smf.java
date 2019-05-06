package fr.xephi.authme.security.crypts;

import fr.xephi.authme.security.HashUtils;
import fr.xephi.authme.security.crypts.description.HasSalt;
import fr.xephi.authme.security.crypts.description.Recommendation;
import fr.xephi.authme.security.crypts.description.SaltType;
import fr.xephi.authme.security.crypts.description.Usage;
import fr.xephi.authme.util.RandomStringUtils;

@Recommendation(Usage.DO_NOT_USE)
@HasSalt(SaltType.USERNAME)
public class Smf
  implements EncryptionMethod
{
  public HashedPassword computeHash(String password, String name)
  {
    return new HashedPassword(computeHash(password, null, name), generateSalt());
  }
  
  public String computeHash(String password, String salt, String name)
  {
    return HashUtils.sha1(name.toLowerCase() + password);
  }
  
  public boolean comparePassword(String password, HashedPassword hashedPassword, String name)
  {
    return computeHash(password, null, name).equals(hashedPassword.getHash());
  }
  
  public String generateSalt()
  {
    return RandomStringUtils.generate(4);
  }
  
  public boolean hasSeparateSalt()
  {
    return true;
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\security\crypts\Smf.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */