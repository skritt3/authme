package fr.xephi.authme.security.crypts;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.security.HashUtils;
import fr.xephi.authme.security.crypts.description.HasSalt;
import fr.xephi.authme.security.crypts.description.Recommendation;
import fr.xephi.authme.security.crypts.description.SaltType;
import fr.xephi.authme.security.crypts.description.Usage;
import fr.xephi.authme.util.RandomStringUtils;
import fr.xephi.authme.util.StringUtils;

@Recommendation(Usage.DOES_NOT_WORK)
@HasSalt(value=SaltType.TEXT, length=22)
public class Ipb4
  implements EncryptionMethod
{
  public String computeHash(String password, String salt, String name)
  {
    return BCryptService.hashpw(password, "$2a$13$" + salt);
  }
  
  public HashedPassword computeHash(String password, String name)
  {
    String salt = generateSalt();
    return new HashedPassword(computeHash(password, salt, name), salt);
  }
  
  public boolean comparePassword(String password, HashedPassword hash, String name)
  {
    try
    {
      return (HashUtils.isValidBcryptHash(hash.getHash())) && (BCryptService.checkpw(password, hash.getHash()));
    }
    catch (IllegalArgumentException e)
    {
      ConsoleLogger.warning("Bcrypt checkpw() returned " + StringUtils.formatException(e));
    }
    return false;
  }
  
  public String generateSalt()
  {
    return RandomStringUtils.generateLowerUpper(22);
  }
  
  public boolean hasSeparateSalt()
  {
    return true;
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\security\crypts\Ipb4.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */