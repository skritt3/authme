package fr.xephi.authme.settings;

import fr.xephi.authme.AuthMe;
import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.security.HashAlgorithm;
import fr.xephi.authme.security.crypts.Argon2;
import fr.xephi.authme.settings.properties.EmailSettings;
import fr.xephi.authme.settings.properties.PluginSettings;
import fr.xephi.authme.settings.properties.RestrictionSettings;
import fr.xephi.authme.settings.properties.SecuritySettings;

public class SettingsWarner
{
  @Inject
  private Settings settings;
  @Inject
  private AuthMe authMe;
  
  public void logWarningsForMisconfigurations()
  {
    if (!((Boolean)this.settings.getProperty(RestrictionSettings.FORCE_SINGLE_SESSION)).booleanValue()) {
      ConsoleLogger.warning("WARNING!!! By disabling ForceSingleSession, your server protection is inadequate!");
    }
    if ((!((Boolean)this.settings.getProperty(EmailSettings.PORT25_USE_TLS)).booleanValue()) && 
      (((Integer)this.settings.getProperty(EmailSettings.SMTP_PORT)).intValue() != 25)) {
      ConsoleLogger.warning("Note: You have set Email.useTls to false but this only affects mail over port 25");
    }
    if ((((Boolean)this.settings.getProperty(PluginSettings.SESSIONS_ENABLED)).booleanValue()) && 
      (((Integer)this.settings.getProperty(PluginSettings.SESSIONS_TIMEOUT)).intValue() <= 0)) {
      ConsoleLogger.warning("Warning: Session timeout needs to be positive in order to work!");
    }
    if ((((HashAlgorithm)this.settings.getProperty(SecuritySettings.PASSWORD_HASH)).equals(HashAlgorithm.ARGON2)) && 
      (!Argon2.isLibraryLoaded()))
    {
      ConsoleLogger.warning("WARNING!!! You use Argon2 Hash Algorithm method but we can't find the Argon2 library on your system! See https://github.com/AuthMe/AuthMeReloaded/wiki/Argon2-as-Password-Hash");
      
      this.authMe.stopOrUnload();
    }
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\settings\SettingsWarner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */