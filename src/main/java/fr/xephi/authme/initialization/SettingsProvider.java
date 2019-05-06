package fr.xephi.authme.initialization;

import fr.xephi.authme.libs.jalu.configme.configurationdata.ConfigurationData;
import fr.xephi.authme.libs.jalu.configme.resource.PropertyResource;
import fr.xephi.authme.libs.jalu.configme.resource.YamlFileResource;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.libs.javax.inject.Provider;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.settings.SettingsMigrationService;
import fr.xephi.authme.settings.properties.AuthMeSettingsRetriever;
import fr.xephi.authme.util.FileUtils;
import java.io.File;

public class SettingsProvider
  implements Provider<Settings>
{
  @Inject
  @DataFolder
  private File dataFolder;
  @Inject
  private SettingsMigrationService migrationService;
  
  public Settings get()
  {
    File configFile = new File(this.dataFolder, "config.yml");
    if (!configFile.exists()) {
      FileUtils.create(configFile);
    }
    PropertyResource resource = new YamlFileResource(configFile);
    ConfigurationData configurationData = AuthMeSettingsRetriever.buildConfigurationData();
    return new Settings(this.dataFolder, resource, this.migrationService, configurationData);
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\initialization\SettingsProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */