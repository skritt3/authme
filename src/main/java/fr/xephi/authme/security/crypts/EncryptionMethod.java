package fr.xephi.authme.security.crypts;

public abstract interface EncryptionMethod
{
  public abstract HashedPassword computeHash(String paramString1, String paramString2);
  
  public abstract String computeHash(String paramString1, String paramString2, String paramString3);
  
  public abstract boolean comparePassword(String paramString1, HashedPassword paramHashedPassword, String paramString2);
  
  public abstract String generateSalt();
  
  public abstract boolean hasSeparateSalt();
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\security\crypts\EncryptionMethod.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */