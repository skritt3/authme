package fr.xephi.authme.security.crypts;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.libs.de.mkammerer.argon2.Argon2Factory;
import fr.xephi.authme.security.crypts.description.HasSalt;
import fr.xephi.authme.security.crypts.description.Recommendation;
import fr.xephi.authme.security.crypts.description.SaltType;
import fr.xephi.authme.security.crypts.description.Usage;

@Recommendation(Usage.RECOMMENDED)
@HasSalt(value=SaltType.TEXT, length=16)
public class Argon2
  extends UnsaltedMethod
{
  private fr.xephi.authme.libs.de.mkammerer.argon2.Argon2 argon2;
  
  public Argon2()
  {
    this.argon2 = Argon2Factory.create();
  }
  
  public static boolean isLibraryLoaded()
  {
    try
    {
      System.loadLibrary("argon2");
      return true;
    }
    catch (UnsatisfiedLinkError e)
    {
      ConsoleLogger.logException("Cannot find argon2 library: https://github.com/AuthMe/AuthMeReloaded/wiki/Argon2-as-Password-Hash", e);
    }
    return false;
  }
  
  public String computeHash(String password)
  {
    return this.argon2.hash(2, 65536, 1, password);
  }
  
  public boolean comparePassword(String password, HashedPassword hashedPassword, String name)
  {
    return this.argon2.verify(hashedPassword.getHash(), password);
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\security\crypts\Argon2.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */