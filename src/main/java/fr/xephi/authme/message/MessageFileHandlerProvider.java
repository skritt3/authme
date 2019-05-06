package fr.xephi.authme.message;

import fr.xephi.authme.initialization.DataFolder;
import fr.xephi.authme.libs.google.common.annotations.VisibleForTesting;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.settings.properties.PluginSettings;
import fr.xephi.authme.util.FileUtils;
import java.io.File;
import java.util.function.Function;

public class MessageFileHandlerProvider
{
  private static final String DEFAULT_LANGUAGE = "en";
  @Inject
  @DataFolder
  private File dataFolder;
  @Inject
  private Settings settings;
  
  public MessageFileHandler initializeHandler(Function<String, String> pathBuilder)
  {
    return initializeHandler(pathBuilder, null);
  }
  
  public MessageFileHandler initializeHandler(Function<String, String> pathBuilder, String updateCommand)
  {
    String language = (String)this.settings.getProperty(PluginSettings.MESSAGES_LANGUAGE);
    return new MessageFileHandler(
      initializeFile(language, pathBuilder), 
      (String)pathBuilder.apply("en"), updateCommand);
  }
  
  @VisibleForTesting
  File initializeFile(String language, Function<String, String> pathBuilder)
  {
    String filePath = (String)pathBuilder.apply(language);
    File file = new File(this.dataFolder, filePath);
    if ((FileUtils.getResourceFromJar(filePath) != null) && (FileUtils.copyFileFromResource(file, filePath))) {
      return file;
    }
    String defaultFilePath = (String)pathBuilder.apply("en");
    if (FileUtils.copyFileFromResource(file, defaultFilePath)) {
      return file;
    }
    return null;
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\message\MessageFileHandlerProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */