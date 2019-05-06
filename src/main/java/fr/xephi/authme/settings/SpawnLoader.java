package fr.xephi.authme.settings;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.initialization.DataFolder;
import fr.xephi.authme.initialization.Reloadable;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.service.PluginHookService;
import fr.xephi.authme.settings.properties.HooksSettings;
import fr.xephi.authme.settings.properties.RestrictionSettings;
import fr.xephi.authme.util.FileUtils;
import fr.xephi.authme.util.StringUtils;
import java.io.File;
import java.io.IOException;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class SpawnLoader
  implements Reloadable
{
  private final File authMeConfigurationFile;
  private final Settings settings;
  private final PluginHookService pluginHookService;
  private FileConfiguration authMeConfiguration;
  private String[] spawnPriority;
  private Location essentialsSpawn;
  
  @Inject
  SpawnLoader(@DataFolder File pluginFolder, Settings settings, PluginHookService pluginHookService)
  {
    File spawnFile = new File(pluginFolder, "spawn.yml");
    FileUtils.copyFileFromResource(spawnFile, "spawn.yml");
    this.authMeConfigurationFile = spawnFile;
    this.settings = settings;
    this.pluginHookService = pluginHookService;
    reload();
  }
  
  public void reload()
  {
    this.spawnPriority = ((String)this.settings.getProperty(RestrictionSettings.SPAWN_PRIORITY)).split(",");
    this.authMeConfiguration = YamlConfiguration.loadConfiguration(this.authMeConfigurationFile);
    loadEssentialsSpawn();
  }
  
  public Location getSpawn()
  {
    return getLocationFromConfiguration(this.authMeConfiguration, "spawn");
  }
  
  public boolean setSpawn(Location location)
  {
    return setLocation("spawn", location);
  }
  
  public Location getFirstSpawn()
  {
    return getLocationFromConfiguration(this.authMeConfiguration, "firstspawn");
  }
  
  public boolean setFirstSpawn(Location location)
  {
    return setLocation("firstspawn", location);
  }
  
  public void loadEssentialsSpawn()
  {
    File essentialsFolder = this.pluginHookService.getEssentialsDataFolder();
    if (essentialsFolder == null) {
      return;
    }
    File essentialsSpawnFile = new File(essentialsFolder, "spawn.yml");
    if (essentialsSpawnFile.exists())
    {
      this.essentialsSpawn = getLocationFromConfiguration(
        YamlConfiguration.loadConfiguration(essentialsSpawnFile), "spawns.default");
    }
    else
    {
      this.essentialsSpawn = null;
      ConsoleLogger.info("Essentials spawn file not found: '" + essentialsSpawnFile.getAbsolutePath() + "'");
    }
  }
  
  public void unloadEssentialsSpawn()
  {
    this.essentialsSpawn = null;
  }
  
  public Location getSpawnLocation(Player player)
  {
    if ((player == null) || (player.getWorld() == null)) {
      return null;
    }
    World world = player.getWorld();
    Location spawnLoc = null;
    for (String priority : this.spawnPriority)
    {
      switch (priority.toLowerCase().trim())
      {
      case "default": 
        if (world.getSpawnLocation() != null) {
          spawnLoc = world.getSpawnLocation();
        }
        break;
      case "multiverse": 
        if (((Boolean)this.settings.getProperty(HooksSettings.MULTIVERSE)).booleanValue()) {
          spawnLoc = this.pluginHookService.getMultiverseSpawn(world);
        }
        break;
      case "essentials": 
        spawnLoc = this.essentialsSpawn;
        break;
      case "authme": 
        spawnLoc = getSpawn();
        break;
      }
      if (spawnLoc != null) {
        return spawnLoc;
      }
    }
    return world.getSpawnLocation();
  }
  
  private boolean setLocation(String prefix, Location location)
  {
    if ((location != null) && (location.getWorld() != null))
    {
      this.authMeConfiguration.set(prefix + ".world", location.getWorld().getName());
      this.authMeConfiguration.set(prefix + ".x", Double.valueOf(location.getX()));
      this.authMeConfiguration.set(prefix + ".y", Double.valueOf(location.getY()));
      this.authMeConfiguration.set(prefix + ".z", Double.valueOf(location.getZ()));
      this.authMeConfiguration.set(prefix + ".yaw", Float.valueOf(location.getYaw()));
      this.authMeConfiguration.set(prefix + ".pitch", Float.valueOf(location.getPitch()));
      return saveAuthMeConfig();
    }
    return false;
  }
  
  private boolean saveAuthMeConfig()
  {
    try
    {
      this.authMeConfiguration.save(this.authMeConfigurationFile);
      return true;
    }
    catch (IOException e)
    {
      ConsoleLogger.logException("Could not save spawn config (" + this.authMeConfigurationFile + ")", e);
    }
    return false;
  }
  
  public Location getPlayerLocationOrSpawn(Player player)
  {
    if ((player.isOnline()) && (player.isDead())) {
      return getSpawnLocation(player);
    }
    return player.getLocation();
  }
  
  private static Location getLocationFromConfiguration(FileConfiguration configuration, String pathPrefix)
  {
    if (containsAllSpawnFields(configuration, pathPrefix))
    {
      String prefix = pathPrefix + ".";
      String worldName = configuration.getString(prefix + "world");
      World world = Bukkit.getWorld(worldName);
      if ((!StringUtils.isEmpty(worldName)) && (world != null)) {
        return new Location(world, configuration.getDouble(prefix + "x"), configuration
          .getDouble(prefix + "y"), configuration.getDouble(prefix + "z"), 
          getFloat(configuration, prefix + "yaw"), getFloat(configuration, prefix + "pitch"));
      }
    }
    return null;
  }
  
  private static boolean containsAllSpawnFields(FileConfiguration configuration, String pathPrefix)
  {
    String[] fields = { "world", "x", "y", "z", "yaw", "pitch" };
    for (String field : fields) {
      if (!configuration.contains(pathPrefix + "." + field)) {
        return false;
      }
    }
    return true;
  }
  
  private static float getFloat(FileConfiguration configuration, String path)
  {
    Object value = configuration.get(path);
    
    return (value instanceof Number) ? ((Number)value).floatValue() : 0.0F;
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\settings\SpawnLoader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */