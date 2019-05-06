package fr.xephi.authme.settings.properties;

import fr.xephi.authme.libs.jalu.configme.configurationdata.ConfigurationData;
import fr.xephi.authme.libs.jalu.configme.configurationdata.ConfigurationDataBuilder;

public final class AuthMeSettingsRetriever
{
  public static ConfigurationData buildConfigurationData()
  {
    return ConfigurationDataBuilder.collectData(new Class[] { DatabaseSettings.class, PluginSettings.class, RestrictionSettings.class, EmailSettings.class, HooksSettings.class, ProtectionSettings.class, PurgeSettings.class, SecuritySettings.class, RegistrationSettings.class, LimboSettings.class, BackupSettings.class, ConverterSettings.class });
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\settings\properties\AuthMeSettingsRetriever.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */