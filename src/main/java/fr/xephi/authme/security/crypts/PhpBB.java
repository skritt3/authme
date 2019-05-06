package fr.xephi.authme.security.crypts;

import fr.xephi.authme.security.HashUtils;
import fr.xephi.authme.security.MessageDigestAlgorithm;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

public class PhpBB
  implements EncryptionMethod
{
  private final BCrypt2y bCrypt2y = new BCrypt2y();
  
  public HashedPassword computeHash(String password, String name)
  {
    String salt = generateSalt();
    return new HashedPassword(BCryptService.hashpw(password, salt));
  }
  
  public String computeHash(String password, String salt, String name)
  {
    return this.bCrypt2y.computeHash(password, salt, name);
  }
  
  public boolean comparePassword(String password, HashedPassword hashedPassword, String name)
  {
    String hash = hashedPassword.getHash();
    if (HashUtils.isValidBcryptHash(hash)) {
      return this.bCrypt2y.comparePassword(password, hashedPassword, name);
    }
    if (hash.length() == 34) {
      return PhpassSaltedMd5.phpbb_check_hash(password, hash);
    }
    return PhpassSaltedMd5.md5(password).equals(hash);
  }
  
  public String generateSalt()
  {
    return BCryptService.gensalt(10);
  }
  
  public boolean hasSeparateSalt()
  {
    return false;
  }
  
  private static final class PhpassSaltedMd5
  {
    private static final String itoa64 = "./0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    
    private static String md5(String data)
    {
      try
      {
        byte[] bytes = data.getBytes("ISO-8859-1");
        MessageDigest md5er = HashUtils.getDigest(MessageDigestAlgorithm.MD5);
        byte[] hash = md5er.digest(bytes);
        return bytes2hex(hash);
      }
      catch (UnsupportedEncodingException e)
      {
        throw new UnsupportedOperationException(e);
      }
    }
    
    private static int hexToInt(char ch)
    {
      if ((ch >= '0') && (ch <= '9')) {
        return ch - '0';
      }
      ch = Character.toUpperCase(ch);
      if ((ch >= 'A') && (ch <= 'F')) {
        return ch - 'A' + 10;
      }
      throw new IllegalArgumentException("Not a hex character: " + ch);
    }
    
    private static String bytes2hex(byte[] bytes)
    {
      StringBuilder r = new StringBuilder(32);
      for (byte b : bytes)
      {
        String x = Integer.toHexString(b & 0xFF);
        if (x.length() < 2) {
          r.append('0');
        }
        r.append(x);
      }
      return r.toString();
    }
    
    private static String pack(String hex)
    {
      StringBuilder buf = new StringBuilder();
      for (int i = 0; i < hex.length(); i += 2)
      {
        char c1 = hex.charAt(i);
        char c2 = hex.charAt(i + 1);
        char packed = (char)(hexToInt(c1) * 16 + hexToInt(c2));
        buf.append(packed);
      }
      return buf.toString();
    }
    
    private static String _hash_encode64(String input, int count)
    {
      StringBuilder output = new StringBuilder();
      int i = 0;
      do
      {
        int value = input.charAt(i++);
        output.append("./0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".charAt(value & 0x3F));
        if (i < count) {
          value |= input.charAt(i) << '\b';
        }
        output.append("./0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".charAt(value >> 6 & 0x3F));
        if (i++ >= count) {
          break;
        }
        if (i < count) {
          value |= input.charAt(i) << '\020';
        }
        output.append("./0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".charAt(value >> 12 & 0x3F));
        if (i++ >= count) {
          break;
        }
        output.append("./0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".charAt(value >> 18 & 0x3F));
      } while (i < count);
      return output.toString();
    }
    
    private static String _hash_crypt_private(String password, String setting)
    {
      String output = "*";
      if (!setting.substring(0, 3).equals("$H$")) {
        return output;
      }
      int count_log2 = "./0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".indexOf(setting.charAt(3));
      if ((count_log2 < 7) || (count_log2 > 30)) {
        return output;
      }
      int count = 1 << count_log2;
      String salt = setting.substring(4, 12);
      if (salt.length() != 8) {
        return output;
      }
      String m1 = md5(salt + password);
      String hash = pack(m1);
      do
      {
        hash = pack(md5(hash + password));
        count--;
      } while (count > 0);
      output = setting.substring(0, 12);
      output = output + _hash_encode64(hash, 16);
      return output;
    }
    
    private static boolean phpbb_check_hash(String password, String hash)
    {
      return _hash_crypt_private(password, hash).equals(hash);
    }
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\security\crypts\PhpBB.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */