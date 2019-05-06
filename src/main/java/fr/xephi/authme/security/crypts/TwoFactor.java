package fr.xephi.authme.security.crypts;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.libs.google.common.escape.Escaper;
import fr.xephi.authme.libs.google.common.io.BaseEncoding;
import fr.xephi.authme.libs.google.common.net.UrlEscapers;
import fr.xephi.authme.libs.google.common.primitives.Ints;
import fr.xephi.authme.security.crypts.description.HasSalt;
import fr.xephi.authme.security.crypts.description.Recommendation;
import fr.xephi.authme.security.crypts.description.SaltType;
import fr.xephi.authme.security.crypts.description.Usage;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

@Recommendation(Usage.DOES_NOT_WORK)
@HasSalt(SaltType.NONE)
public class TwoFactor
  extends UnsaltedMethod
{
  private static final int SCRET_BYTE = 10;
  private static final int SCRATCH_CODES = 5;
  private static final int BYTES_PER_SCRATCH_CODE = 4;
  private static final int TIME_PRECISION = 3;
  private static final String CRYPTO_ALGO = "HmacSHA1";
  
  public static String getQrBarcodeUrl(String user, String host, String secret)
  {
    String format = "https://www.google.com/chart?chs=130x130&chld=M%%7C0&cht=qr&chl=otpauth://totp/%s@%s%%3Fsecret%%3D%s";
    
    Escaper urlEscaper = UrlEscapers.urlFragmentEscaper();
    return String.format(format, new Object[] { urlEscaper.escape(user), urlEscaper.escape(host), secret });
  }
  
  public String computeHash(String password)
  {
    byte[] buffer = new byte[30];
    
    new SecureRandom().nextBytes(buffer);
    
    byte[] secretKey = Arrays.copyOf(buffer, 10);
    return BaseEncoding.base32().encode(secretKey);
  }
  
  public boolean comparePassword(String password, HashedPassword hashedPassword, String name)
  {
    try
    {
      return checkPassword(hashedPassword.getHash(), password);
    }
    catch (Exception e)
    {
      ConsoleLogger.logException("Failed to verify two auth code:", e);
    }
    return false;
  }
  
  private boolean checkPassword(String secretKey, String userInput)
    throws NoSuchAlgorithmException, InvalidKeyException
  {
    Integer code = Ints.tryParse(userInput);
    if (code == null) {
      return false;
    }
    long currentTime = Calendar.getInstance().getTimeInMillis() / TimeUnit.SECONDS.toMillis(30L);
    return checkCode(secretKey, code.intValue(), currentTime);
  }
  
  private boolean checkCode(String secret, long code, long t)
    throws NoSuchAlgorithmException, InvalidKeyException
  {
    byte[] decodedKey = BaseEncoding.base32().decode(secret);
    
    int window = 3;
    for (int i = -window; i <= window; i++)
    {
      long hash = verifyCode(decodedKey, t + i);
      if (hash == code) {
        return true;
      }
    }
    return false;
  }
  
  private int verifyCode(byte[] key, long t)
    throws NoSuchAlgorithmException, InvalidKeyException
  {
    byte[] data = new byte[8];
    long value = t;
    for (int i = 8; i-- > 0; value >>>= 8) {
      data[i] = ((byte)(int)value);
    }
    SecretKeySpec signKey = new SecretKeySpec(key, "HmacSHA1");
    Mac mac = Mac.getInstance("HmacSHA1");
    mac.init(signKey);
    byte[] hash = mac.doFinal(data);
    
    int offset = hash[19] & 0xF;
    
    long truncatedHash = 0L;
    for (int i = 0; i < 4; i++)
    {
      truncatedHash <<= 8;
      
      truncatedHash |= hash[(offset + i)] & 0xFF;
    }
    truncatedHash &= 0x7FFFFFFF;
    truncatedHash %= 1000000L;
    
    return (int)truncatedHash;
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\security\crypts\TwoFactor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */