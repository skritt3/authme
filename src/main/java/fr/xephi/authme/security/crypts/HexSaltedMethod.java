package fr.xephi.authme.security.crypts;

import fr.xephi.authme.security.crypts.description.HasSalt;
import fr.xephi.authme.security.crypts.description.Recommendation;
import fr.xephi.authme.security.crypts.description.SaltType;
import fr.xephi.authme.security.crypts.description.Usage;
import fr.xephi.authme.util.RandomStringUtils;

@Recommendation(Usage.ACCEPTABLE)
@HasSalt(SaltType.TEXT)
public abstract class HexSaltedMethod
  implements EncryptionMethod
{
  public abstract int getSaltLength();
  
  public abstract String computeHash(String paramString1, String paramString2, String paramString3);
  
  public HashedPassword computeHash(String password, String name)
  {
    String salt = generateSalt();
    return new HashedPassword(computeHash(password, salt, null));
  }
  
  public abstract boolean comparePassword(String paramString1, HashedPassword paramHashedPassword, String paramString2);
  
  public String generateSalt()
  {
    return RandomStringUtils.generateHex(getSaltLength());
  }
  
  public boolean hasSeparateSalt()
  {
    return false;
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\security\crypts\HexSaltedMethod.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */