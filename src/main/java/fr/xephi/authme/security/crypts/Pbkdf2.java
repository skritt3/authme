package fr.xephi.authme.security.crypts;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.libs.de.rtner.misc.BinTools;
import fr.xephi.authme.libs.de.rtner.security.auth.spi.PBKDF2Engine;
import fr.xephi.authme.libs.de.rtner.security.auth.spi.PBKDF2Parameters;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.security.crypts.description.Recommendation;
import fr.xephi.authme.security.crypts.description.Usage;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.settings.properties.SecuritySettings;

@Recommendation(Usage.RECOMMENDED)
public class Pbkdf2
  extends HexSaltedMethod
{
  private static final int DEFAULT_ROUNDS = 10000;
  private int numberOfRounds;
  
  @Inject
  Pbkdf2(Settings settings)
  {
    int configuredRounds = ((Integer)settings.getProperty(SecuritySettings.PBKDF2_NUMBER_OF_ROUNDS)).intValue();
    this.numberOfRounds = (configuredRounds > 0 ? configuredRounds : 10000);
  }
  
  public String computeHash(String password, String salt, String name)
  {
    String result = "pbkdf2_sha256$" + this.numberOfRounds + "$" + salt + "$";
    PBKDF2Parameters params = new PBKDF2Parameters("HmacSHA256", "UTF-8", salt.getBytes(), this.numberOfRounds);
    PBKDF2Engine engine = new PBKDF2Engine(params);
    
    return result + BinTools.bin2hex(engine.deriveKey(password, 64));
  }
  
  public boolean comparePassword(String password, HashedPassword hashedPassword, String unusedName)
  {
    String[] line = hashedPassword.getHash().split("\\$");
    int iterations;
    if (line.length != 4) {
      return false;
    }
    try
    {
      iterations = Integer.parseInt(line[1]);
    }
    catch (NumberFormatException e)
    {
      iterations=0;
      ConsoleLogger.logException("Cannot read number of rounds for Pbkdf2", e);
      return false;
    }
    String salt = line[2];
    byte[] derivedKey = BinTools.hex2bin(line[3]);
    PBKDF2Parameters params = new PBKDF2Parameters("HmacSHA256", "UTF-8", salt.getBytes(), iterations, derivedKey);
    PBKDF2Engine engine = new PBKDF2Engine(params);
    return engine.verifyKey(password);
  }
  
  public int getSaltLength()
  {
    return 16;
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\security\crypts\Pbkdf2.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */