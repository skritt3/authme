package fr.xephi.authme.message;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.util.FileUtils;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class MessageFileHandler
{
  private final String filename;
  private final FileConfiguration configuration;
  private final String updateAddition;
  private final String defaultFile;
  private FileConfiguration defaultConfiguration;
  
  public MessageFileHandler(File file, String defaultFile, String updateCommand)
  {
    this.filename = file.getName();
    this.configuration = YamlConfiguration.loadConfiguration(file);
    this.defaultFile = defaultFile;
    this.updateAddition = (" (or run " + updateCommand + ")");
  }
  
  public boolean hasSection(String path)
  {
    return this.configuration.get(path) != null;
  }
  
  public String getMessage(String key)
  {
    String message = this.configuration.getString(key);
    if (message == null)
    {
      ConsoleLogger.warning("Error getting message with key '" + key + "'. Please update your config file '" + this.filename + "'" + this.updateAddition);
      
      return getDefault(key);
    }
    return message;
  }
  
  public String getMessageIfExists(String key)
  {
    return this.configuration.getString(key);
  }
  
  private String getDefault(String key)
  {
    if (this.defaultConfiguration == null)
    {
      InputStream stream = FileUtils.getResourceFromJar(this.defaultFile);
      this.defaultConfiguration = YamlConfiguration.loadConfiguration(new InputStreamReader(stream));
    }
    String message = this.defaultConfiguration.getString(key);
    return message == null ? "Error retrieving message '" + key + "'" : message;
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\message\MessageFileHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */