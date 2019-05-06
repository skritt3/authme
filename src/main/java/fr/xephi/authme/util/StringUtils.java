package fr.xephi.authme.util;

import fr.xephi.authme.libs.ricecode.similarity.LevenshteinDistanceStrategy;
import fr.xephi.authme.libs.ricecode.similarity.StringSimilarityService;
import fr.xephi.authme.libs.ricecode.similarity.StringSimilarityServiceImpl;

public final class StringUtils
{
  public static double getDifference(String first, String second)
  {
    if ((first == null) || (second == null)) {
      return 1.0D;
    }
    StringSimilarityService service = new StringSimilarityServiceImpl(new LevenshteinDistanceStrategy());
    
    return Math.abs(service.score(first, second) - 1.0D);
  }
  
  public static boolean containsAny(String str, Iterable<String> pieces)
  {
    if (str == null) {
      return false;
    }
    for (String piece : pieces) {
      if ((piece != null) && (str.contains(piece))) {
        return true;
      }
    }
    return false;
  }
  
  public static boolean isEmpty(String str)
  {
    return (str == null) || (str.trim().isEmpty());
  }
  
  public static String formatException(Throwable th)
  {
    return "[" + th.getClass().getSimpleName() + "]: " + th.getMessage();
  }
  
  public static boolean isInsideString(char needle, String haystack)
  {
    int index = haystack.indexOf(needle);
    return (index > 0) && (index < haystack.length() - 1);
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authm\\util\StringUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */