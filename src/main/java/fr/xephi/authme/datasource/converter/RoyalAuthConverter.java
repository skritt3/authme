package fr.xephi.authme.datasource.converter;

import fr.xephi.authme.AuthMe;
import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.data.auth.PlayerAuth;
import fr.xephi.authme.data.auth.PlayerAuth.Builder;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.util.FileUtils;
import java.io.File;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class RoyalAuthConverter
  implements Converter
{
  private static final String LAST_LOGIN_PATH = "timestamps.quit";
  private static final String PASSWORD_PATH = "login.password";
  private final AuthMe plugin;
  private final DataSource dataSource;
  
  @Inject
  RoyalAuthConverter(AuthMe plugin, DataSource dataSource)
  {
    this.plugin = plugin;
    this.dataSource = dataSource;
  }
  
  public void execute(CommandSender sender)
  {
    for (OfflinePlayer player : this.plugin.getServer().getOfflinePlayers()) {
      try
      {
        String name = player.getName().toLowerCase();
        File file = new File(FileUtils.makePath(new String[] { ".", "plugins", "RoyalAuth", "userdata", name + ".yml" }));
        if ((this.dataSource.isAuthAvailable(name)) || (file.exists()))
        {
          FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);
          
          PlayerAuth auth = PlayerAuth.builder().name(name).password(configuration.getString("login.password"), null).lastLogin(Long.valueOf(configuration.getLong("timestamps.quit"))).realName(player.getName()).build();
          
          this.dataSource.saveAuth(auth);
          this.dataSource.updateSession(auth);
        }
      }
      catch (Exception e)
      {
        ConsoleLogger.logException("Error while trying to import " + player.getName() + " RoyalAuth data", e);
      }
    }
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\datasource\converter\RoyalAuthConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */