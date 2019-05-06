package fr.xephi.authme.util;

import java.security.SecureRandom;
import java.util.Random;

public final class RandomStringUtils
{
  private static final char[] CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
  private static final Random RANDOM = new SecureRandom();
  private static final int NUM_INDEX = 10;
  private static final int LOWER_ALPHANUMERIC_INDEX = 36;
  private static final int HEX_MAX_INDEX = 16;
  
  public static String generate(int length)
  {
    return generateString(length, 36);
  }
  
  public static String generateHex(int length)
  {
    return generateString(length, 16);
  }
  
  public static String generateNum(int length)
  {
    return generateString(length, 10);
  }
  
  public static String generateLowerUpper(int length)
  {
    return generateString(length, CHARS.length);
  }
  
  private static String generateString(int length, int maxIndex)
  {
    if (length < 0) {
      throw new IllegalArgumentException("Length must be positive but was " + length);
    }
    StringBuilder sb = new StringBuilder(length);
    for (int i = 0; i < length; i++) {
      sb.append(CHARS[RANDOM.nextInt(maxIndex)]);
    }
    return sb.toString();
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authm\\util\RandomStringUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */