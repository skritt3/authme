package fr.xephi.authme.security.crypts;

import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.security.HashUtils;
import fr.xephi.authme.security.crypts.description.HasSalt;
import fr.xephi.authme.security.crypts.description.Recommendation;
import fr.xephi.authme.security.crypts.description.SaltType;
import fr.xephi.authme.security.crypts.description.Usage;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.settings.properties.SecuritySettings;
import fr.xephi.authme.util.RandomStringUtils;

@Recommendation(Usage.ACCEPTABLE)
@HasSalt(SaltType.TEXT)
public class Salted2Md5
  extends SeparateSaltMethod
{
  private final int saltLength;
  
  @Inject
  public Salted2Md5(Settings settings)
  {
    this.saltLength = ((Integer)settings.getProperty(SecuritySettings.DOUBLE_MD5_SALT_LENGTH)).intValue();
  }
  
  public String computeHash(String password, String salt, String name)
  {
    return HashUtils.md5(HashUtils.md5(password) + salt);
  }
  
  public String generateSalt()
  {
    return RandomStringUtils.generateHex(this.saltLength);
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\security\crypts\Salted2Md5.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */