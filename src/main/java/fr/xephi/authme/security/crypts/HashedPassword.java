package fr.xephi.authme.security.crypts;

public class HashedPassword
{
  private final String hash;
  private final String salt;
  
  public HashedPassword(String hash, String salt)
  {
    this.hash = hash;
    this.salt = salt;
  }
  
  public HashedPassword(String hash)
  {
    this(hash, null);
  }
  
  public String getHash()
  {
    return this.hash;
  }
  
  public String getSalt()
  {
    return this.salt;
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\security\crypts\HashedPassword.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */