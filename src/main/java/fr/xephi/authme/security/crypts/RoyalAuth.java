package fr.xephi.authme.security.crypts;

import fr.xephi.authme.security.HashUtils;
import fr.xephi.authme.security.MessageDigestAlgorithm;
import java.security.MessageDigest;

public class RoyalAuth
  extends UnsaltedMethod
{
  public String computeHash(String password)
  {
    MessageDigest algorithm = HashUtils.getDigest(MessageDigestAlgorithm.SHA512);
    for (int i = 0; i < 25; i++) {
      password = HashUtils.hash(password, algorithm);
    }
    return password;
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\security\crypts\RoyalAuth.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */