package fr.xephi.authme.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class InternetProtocolUtils
{
  private static final Pattern LOCAL_ADDRESS_PATTERN = Pattern.compile("(^127\\.)|(^(0)?10\\.)|(^172\\.(0)?1[6-9]\\.)|(^172\\.(0)?2[0-9]\\.)|(^172\\.(0)?3[0-1]\\.)|(^169\\.254\\.)|(^192\\.168\\.)");
  
  public static boolean isLocalAddress(String address)
  {
    return LOCAL_ADDRESS_PATTERN.matcher(address).find();
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authm\\util\InternetProtocolUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */