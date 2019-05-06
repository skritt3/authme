package fr.xephi.authme.datasource.converter;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.data.auth.PlayerAuth;
import fr.xephi.authme.data.auth.PlayerAuth.Builder;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.initialization.DataFolder;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.settings.properties.ConverterSettings;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import org.bukkit.command.CommandSender;

public class CrazyLoginConverter
  implements Converter
{
  private final DataSource database;
  private final Settings settings;
  private final File dataFolder;
  
  @Inject
  CrazyLoginConverter(@DataFolder File dataFolder, DataSource dataSource, Settings settings)
  {
    this.dataFolder = dataFolder;
    this.database = dataSource;
    this.settings = settings;
  }
  
  public void execute(CommandSender sender)
  {
    String fileName = (String)this.settings.getProperty(ConverterSettings.CRAZYLOGIN_FILE_NAME);
    File source = new File(this.dataFolder, fileName);
    if (!source.exists())
    {
      sender.sendMessage("CrazyLogin file not found, please put " + fileName + " in AuthMe folder!");
      return;
    }
    try
    {
      BufferedReader users = new BufferedReader(new FileReader(source));Throwable localThrowable3 = null;
      try
      {
        String line;
        while ((line = users.readLine()) != null) {
          if (line.contains("|")) {
            migrateAccount(line);
          }
        }
        ConsoleLogger.info("CrazyLogin database has been imported correctly");
      }
      catch (Throwable localThrowable1)
      {
        localThrowable3 = localThrowable1;throw localThrowable1;
      }
      finally
      {
        if (users != null) {
          if (localThrowable3 != null) {
            try
            {
              users.close();
            }
            catch (Throwable localThrowable2)
            {
              localThrowable3.addSuppressed(localThrowable2);
            }
          } else {
            users.close();
          }
        }
      }
    }
    catch (IOException ex)
    {
      String line;
      ConsoleLogger.warning("Can't open the crazylogin database file! Does it exist?");
      ConsoleLogger.logException("Encountered", ex);
    }
  }
  
  private void migrateAccount(String line)
  {
    String[] args = line.split("\\|");
    if ((args.length < 2) || ("name".equalsIgnoreCase(args[0]))) {
      return;
    }
    String playerName = args[0];
    String password = args[1];
    if (password != null)
    {
      PlayerAuth auth = PlayerAuth.builder().name(playerName.toLowerCase()).realName(playerName).password(password, null).build();
      this.database.saveAuth(auth);
    }
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\datasource\converter\CrazyLoginConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */