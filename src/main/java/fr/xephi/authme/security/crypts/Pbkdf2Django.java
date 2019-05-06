package fr.xephi.authme.security.crypts;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.libs.de.rtner.security.auth.spi.PBKDF2Engine;
import fr.xephi.authme.libs.de.rtner.security.auth.spi.PBKDF2Parameters;
import fr.xephi.authme.security.crypts.description.AsciiRestricted;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

@AsciiRestricted
public class Pbkdf2Django
  extends HexSaltedMethod
{
  private static final int DEFAULT_ITERATIONS = 24000;
  
  public String computeHash(String password, String salt, String name)
  {
    String result = "pbkdf2_sha256$24000$" + salt + "$";
    PBKDF2Parameters params = new PBKDF2Parameters("HmacSHA256", "ASCII", salt.getBytes(), 24000);
    PBKDF2Engine engine = new PBKDF2Engine(params);
    
    return result + Base64.getEncoder().encodeToString(engine.deriveKey(password, 32));
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
      ConsoleLogger.logException("Could not read number of rounds for Pbkdf2Django:", e);
      return false;
    }
    String salt = line[2];
    byte[] derivedKey = Base64.getDecoder().decode(line[3]);
    PBKDF2Parameters params = new PBKDF2Parameters("HmacSHA256", "ASCII", salt.getBytes(), iterations, derivedKey);
    PBKDF2Engine engine = new PBKDF2Engine(params);
    return engine.verifyKey(password);
  }
  
  public int getSaltLength()
  {
    return 12;
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\security\crypts\Pbkdf2Django.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */