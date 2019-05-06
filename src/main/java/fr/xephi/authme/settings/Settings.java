package fr.xephi.authme.settings;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.libs.google.common.io.CharSource;
import fr.xephi.authme.libs.google.common.io.Files;
import fr.xephi.authme.libs.jalu.configme.SettingsManager;
import fr.xephi.authme.libs.jalu.configme.configurationdata.ConfigurationData;
import fr.xephi.authme.libs.jalu.configme.migration.MigrationService;
import fr.xephi.authme.libs.jalu.configme.resource.PropertyResource;
import fr.xephi.authme.util.FileUtils;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Settings
  extends SettingsManager
{
  private final File pluginFolder;
  private String passwordEmailMessage;
  private String verificationEmailMessage;
  private String recoveryCodeEmailMessage;
  
  public Settings(File pluginFolder, PropertyResource resource, MigrationService migrationService, ConfigurationData configurationData)
  {
    super(resource, migrationService, configurationData);
    this.pluginFolder = pluginFolder;
    loadSettingsFromFiles();
  }
  
  public String getPasswordEmailMessage()
  {
    return this.passwordEmailMessage;
  }
  
  public String getVerificationEmailMessage()
  {
    return this.verificationEmailMessage;
  }
  
  public String getRecoveryCodeEmailMessage()
  {
    return this.recoveryCodeEmailMessage;
  }
  
  private void loadSettingsFromFiles()
  {
    this.passwordEmailMessage = readFile("email.html");
    this.verificationEmailMessage = readFile("verification_code_email.html");
    this.recoveryCodeEmailMessage = readFile("recovery_code_email.html");
  }
  
  public void reload()
  {
    super.reload();
    loadSettingsFromFiles();
  }
  
  private String readFile(String filename)
  {
    File file = new File(this.pluginFolder, filename);
    if (FileUtils.copyFileFromResource(file, filename)) {
      try
      {
        return Files.asCharSource(file, StandardCharsets.UTF_8).read();
      }
      catch (IOException e)
      {
        ConsoleLogger.logException("Failed to read file '" + filename + "':", e);
      }
    } else {
      ConsoleLogger.warning("Failed to copy file '" + filename + "' from JAR");
    }
    return "";
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\settings\Settings.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */