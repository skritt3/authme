package fr.xephi.authme.security.crypts;

import fr.xephi.authme.security.HashUtils;
import fr.xephi.authme.security.crypts.description.Recommendation;
import fr.xephi.authme.security.crypts.description.Usage;

@Recommendation(Usage.RECOMMENDED)
public class Sha256
  extends HexSaltedMethod
{
  public String computeHash(String password, String salt, String name)
  {
    return "$SHA$" + salt + "$" + HashUtils.sha256(new StringBuilder().append(HashUtils.sha256(password)).append(salt).toString());
  }
  
  public boolean comparePassword(String password, HashedPassword hashedPassword, String playerName)
  {
    String hash = hashedPassword.getHash();
    String[] line = hash.split("\\$");
    return (line.length == 4) && (hash.equals(computeHash(password, line[2], "")));
  }
  
  public int getSaltLength()
  {
    return 16;
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\security\crypts\Sha256.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */