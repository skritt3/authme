package fr.xephi.authme.security.crypts;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.security.HashUtils;
import fr.xephi.authme.security.crypts.description.HasSalt;
import fr.xephi.authme.security.crypts.description.Recommendation;
import fr.xephi.authme.security.crypts.description.SaltType;
import fr.xephi.authme.security.crypts.description.Usage;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.settings.properties.HooksSettings;
import fr.xephi.authme.util.StringUtils;

@Recommendation(Usage.RECOMMENDED)
@HasSalt(SaltType.TEXT)
public class BCrypt
  implements EncryptionMethod
{
  private final int bCryptLog2Rounds;
  
  @Inject
  public BCrypt(Settings settings)
  {
    this.bCryptLog2Rounds = ((Integer)settings.getProperty(HooksSettings.BCRYPT_LOG2_ROUND)).intValue();
  }
  
  public String computeHash(String password, String salt, String name)
  {
    return BCryptService.hashpw(password, salt);
  }
  
  public HashedPassword computeHash(String password, String name)
  {
    String salt = generateSalt();
    return new HashedPassword(BCryptService.hashpw(password, salt), null);
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
    return BCryptService.gensalt(this.bCryptLog2Rounds);
  }
  
  public boolean hasSeparateSalt()
  {
    return false;
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\security\crypts\BCrypt.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */