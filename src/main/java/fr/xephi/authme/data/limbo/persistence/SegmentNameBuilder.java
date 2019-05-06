package fr.xephi.authme.data.limbo.persistence;

import java.util.HashMap;
import java.util.Map;

class SegmentNameBuilder
{
  private final int length;
  private final int distribution;
  private final String prefix;
  private final Map<Character, Character> charToSegmentChar;
  
  SegmentNameBuilder(SegmentSize partition)
  {
    this.length = partition.getLength();
    this.distribution = partition.getDistribution();
    this.prefix = ("seg" + partition.getTotalSegments() + "-");
    this.charToSegmentChar = buildCharMap(this.distribution);
  }
  
  String createSegmentName(String uuid)
  {
    if (this.distribution == 16) {
      return this.prefix + uuid.substring(0, this.length);
    }
    return this.prefix + buildSegmentName(uuid.substring(0, this.length).toCharArray());
  }
  
  String getPrefix()
  {
    return this.prefix;
  }
  
  private String buildSegmentName(char[] chars)
  {
    if (chars.length == 1) {
      return String.valueOf(this.charToSegmentChar.get(Character.valueOf(chars[0])));
    }
    StringBuilder sb = new StringBuilder(chars.length);
    for (char chr : chars) {
      sb.append(this.charToSegmentChar.get(Character.valueOf(chr)));
    }
    return sb.toString();
  }
  
  private static Map<Character, Character> buildCharMap(int distribution)
  {
    char[] hexChars = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
    int divisor = 16 / distribution;
    
    Map<Character, Character> charToSegmentChar = new HashMap();
    for (int i = 0; i < hexChars.length; i++)
    {
      int mappedChar = i / divisor;
      charToSegmentChar.put(Character.valueOf(hexChars[i]), Character.valueOf(hexChars[mappedChar]));
    }
    return charToSegmentChar;
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\data\limbo\persistence\SegmentNameBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */