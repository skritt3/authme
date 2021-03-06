package fr.xephi.authme.security.crypts;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.security.HashUtils;
import fr.xephi.authme.util.StringUtils;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XfBCrypt
  implements EncryptionMethod
{
  public static final String SCHEME_CLASS = "XenForo_Authentication_Core12";
  private static final Pattern HASH_PATTERN = Pattern.compile("\"hash\";s.*\"(.*)?\"");
  
  public String generateSalt()
  {
    return BCryptService.gensalt();
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
  
  public boolean comparePassword(String password, HashedPassword hash, String salt)
  {
    try
    {
      return (HashUtils.isValidBcryptHash(hash.getHash())) && (BCryptService.checkpw(password, hash.getHash()));
    }
    catch (IllegalArgumentException e)
    {
      ConsoleLogger.warning("XfBCrypt checkpw() returned " + StringUtils.formatException(e));
    }
    return false;
  }
  
  public boolean hasSeparateSalt()
  {
    return false;
  }
  
  public static String getHashFromBlob(byte[] blob)
  {
    String line = new String(blob);
    Matcher m = HASH_PATTERN.matcher(line);
    if (m.find()) {
      return m.group(1);
    }
    return "*";
  }
  
  public static String serializeHash(String hash)
  {
    return "a:1:{s:4:\"hash\";s:" + hash.length() + ":\"" + hash + "\";}";
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\security\crypts\XfBCrypt.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */