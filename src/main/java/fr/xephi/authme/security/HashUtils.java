package fr.xephi.authme.security;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class HashUtils
{
  public static String sha1(String message)
  {
    return hash(message, MessageDigestAlgorithm.SHA1);
  }
  
  public static String sha256(String message)
  {
    return hash(message, MessageDigestAlgorithm.SHA256);
  }
  
  public static String sha512(String message)
  {
    return hash(message, MessageDigestAlgorithm.SHA512);
  }
  
  public static String md5(String message)
  {
    return hash(message, MessageDigestAlgorithm.MD5);
  }
  
  public static MessageDigest getDigest(MessageDigestAlgorithm algorithm)
  {
    try
    {
      return MessageDigest.getInstance(algorithm.getKey());
    }
    catch (NoSuchAlgorithmException e)
    {
      throw new UnsupportedOperationException("Your system seems not to support the hash algorithm '" + algorithm.getKey() + "'");
    }
  }
  
  public static boolean isValidBcryptHash(String hash)
  {
    return (hash.length() > 3) && (hash.substring(0, 2).equals("$2"));
  }
  
  public static String hash(String message, MessageDigest algorithm)
  {
    algorithm.reset();
    algorithm.update(message.getBytes());
    byte[] digest = algorithm.digest();
    return String.format("%0" + (digest.length << 1) + "x", new Object[] { new BigInteger(1, digest) });
  }
  
  private static String hash(String message, MessageDigestAlgorithm algorithm)
  {
    return hash(message, getDigest(algorithm));
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\security\HashUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */