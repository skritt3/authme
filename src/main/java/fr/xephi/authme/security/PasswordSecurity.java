package fr.xephi.authme.security;

import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.events.PasswordEncryptionEvent;
import fr.xephi.authme.initialization.Reloadable;
import fr.xephi.authme.initialization.factory.Factory;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.security.crypts.EncryptionMethod;
import fr.xephi.authme.security.crypts.HashedPassword;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.settings.properties.SecuritySettings;
import java.util.Collection;
import javax.annotation.PostConstruct;
import org.bukkit.plugin.PluginManager;

public class PasswordSecurity
  implements Reloadable
{
  @Inject
  private Settings settings;
  @Inject
  private DataSource dataSource;
  @Inject
  private PluginManager pluginManager;
  @Inject
  private Factory<EncryptionMethod> encryptionMethodFactory;
  private EncryptionMethod encryptionMethod;
  private Collection<HashAlgorithm> legacyAlgorithms;
  
  @PostConstruct
  public void reload()
  {
    HashAlgorithm algorithm = (HashAlgorithm)this.settings.getProperty(SecuritySettings.PASSWORD_HASH);
    this.encryptionMethod = initializeEncryptionMethodWithEvent(algorithm);
    this.legacyAlgorithms = ((Collection)this.settings.getProperty(SecuritySettings.LEGACY_HASHES));
  }
  
  public HashedPassword computeHash(String password, String playerName)
  {
    String playerLowerCase = playerName.toLowerCase();
    return this.encryptionMethod.computeHash(password, playerLowerCase);
  }
  
  public boolean comparePassword(String password, String playerName)
  {
    HashedPassword auth = this.dataSource.getPassword(playerName);
    return (auth != null) && (comparePassword(password, auth, playerName));
  }
  
  public boolean comparePassword(String password, HashedPassword hashedPassword, String playerName)
  {
    String playerLowerCase = playerName.toLowerCase();
    return (methodMatches(this.encryptionMethod, password, hashedPassword, playerLowerCase)) || 
      (compareWithLegacyHashes(password, hashedPassword, playerLowerCase));
  }
  
  private boolean compareWithLegacyHashes(String password, HashedPassword hashedPassword, String playerName)
  {
    for (HashAlgorithm algorithm : this.legacyAlgorithms)
    {
      EncryptionMethod method = initializeEncryptionMethod(algorithm);
      if (methodMatches(method, password, hashedPassword, playerName))
      {
        hashAndSavePasswordWithNewAlgorithm(password, playerName);
        return true;
      }
    }
    return false;
  }
  
  private static boolean methodMatches(EncryptionMethod method, String password, HashedPassword hashedPassword, String playerName)
  {
    return (method != null) && ((!method.hasSeparateSalt()) || (hashedPassword.getSalt() != null)) && 
      (method.comparePassword(password, hashedPassword, playerName));
  }
  
  private EncryptionMethod initializeEncryptionMethodWithEvent(HashAlgorithm algorithm)
  {
    EncryptionMethod method = initializeEncryptionMethod(algorithm);
    PasswordEncryptionEvent event = new PasswordEncryptionEvent(method);
    this.pluginManager.callEvent(event);
    return event.getMethod();
  }
  
  private EncryptionMethod initializeEncryptionMethod(HashAlgorithm algorithm)
  {
    if ((HashAlgorithm.CUSTOM.equals(algorithm)) || (HashAlgorithm.PLAINTEXT.equals(algorithm))) {
      return null;
    }
    return (EncryptionMethod)this.encryptionMethodFactory.newInstance(algorithm.getClazz());
  }
  
  private void hashAndSavePasswordWithNewAlgorithm(String password, String playerName)
  {
    HashedPassword hashedPassword = this.encryptionMethod.computeHash(password, playerName);
    this.dataSource.updatePassword(playerName, hashedPassword);
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\security\PasswordSecurity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */