package fr.xephi.authme.security.crypts;

public abstract class SeparateSaltMethod
  implements EncryptionMethod
{
  public abstract String computeHash(String paramString1, String paramString2, String paramString3);
  
  public HashedPassword computeHash(String password, String name)
  {
    String salt = generateSalt();
    return new HashedPassword(computeHash(password, salt, name), salt);
  }
  
  public abstract String generateSalt();
  
  public boolean comparePassword(String password, HashedPassword hashedPassword, String name)
  {
    return hashedPassword.getHash().equals(computeHash(password, hashedPassword.getSalt(), null));
  }
  
  public boolean hasSeparateSalt()
  {
    return true;
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\security\crypts\SeparateSaltMethod.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */