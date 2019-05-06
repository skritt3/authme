package fr.xephi.authme.security.crypts;

import fr.xephi.authme.security.HashUtils;

public class Md5vB
  extends HexSaltedMethod
{
  public String computeHash(String password, String salt, String name)
  {
    return "$MD5vb$" + salt + "$" + HashUtils.md5(new StringBuilder().append(HashUtils.md5(password)).append(salt).toString());
  }
  
  public boolean comparePassword(String password, HashedPassword hashedPassword, String name)
  {
    String hash = hashedPassword.getHash();
    String[] line = hash.split("\\$");
    return (line.length == 4) && (hash.equals(computeHash(password, line[2], name)));
  }
  
  public int getSaltLength()
  {
    return 16;
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\security\crypts\Md5vB.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */