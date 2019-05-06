package fr.xephi.authme.security.crypts;

import fr.xephi.authme.security.HashUtils;
import fr.xephi.authme.security.MessageDigestAlgorithm;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class CrazyCrypt1
  extends UsernameSaltMethod
{
  private static final char[] CRYPTCHARS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
  
  private static String byteArrayToHexString(byte... args)
  {
    char[] chars = new char[args.length * 2];
    for (int i = 0; i < args.length; i++)
    {
      chars[(i * 2)] = CRYPTCHARS[(args[i] >> 4 & 0xF)];
      chars[(i * 2 + 1)] = CRYPTCHARS[(args[i] & 0xF)];
    }
    return new String(chars);
  }
  
  public HashedPassword computeHash(String password, String name)
  {
    String text = "ÜÄaeut//&/=I " + password + "7421€547" + name + "__+IÄIH§%NK " + password;
    MessageDigest md = HashUtils.getDigest(MessageDigestAlgorithm.SHA512);
    md.update(text.getBytes(StandardCharsets.UTF_8), 0, text.length());
    return new HashedPassword(byteArrayToHexString(md.digest()));
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\security\crypts\CrazyCrypt1.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */