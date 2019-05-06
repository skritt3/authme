package fr.xephi.authme.security.crypts;

import fr.xephi.authme.security.HashUtils;
import fr.xephi.authme.security.crypts.description.Recommendation;
import fr.xephi.authme.security.crypts.description.Usage;

@Deprecated
@Recommendation(Usage.DEPRECATED)
public class DoubleMd5
  extends UnsaltedMethod
{
  public String computeHash(String password)
  {
    return HashUtils.md5(HashUtils.md5(password));
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\security\crypts\DoubleMd5.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */