package fr.xephi.authme.security;

public enum MessageDigestAlgorithm
{
  MD5("MD5"),  SHA1("SHA-1"),  SHA256("SHA-256"),  SHA512("SHA-512");
  
  private final String key;
  
  private MessageDigestAlgorithm(String key)
  {
    this.key = key;
  }
  
  public String getKey()
  {
    return this.key;
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\security\MessageDigestAlgorithm.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */