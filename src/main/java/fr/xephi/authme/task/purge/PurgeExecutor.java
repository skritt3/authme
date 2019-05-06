package fr.xephi.authme.task.purge;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.permission.PermissionsManager;
import fr.xephi.authme.service.BukkitService;
import fr.xephi.authme.service.PluginHookService;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.settings.properties.PurgeSettings;
import fr.xephi.authme.util.FileUtils;
import fr.xephi.authme.util.PlayerUtils;
import java.io.File;
import java.util.Collection;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;

public class PurgeExecutor
{
  @Inject
  private Settings settings;
  @Inject
  private DataSource dataSource;
  @Inject
  private PermissionsManager permissionsManager;
  @Inject
  private PluginHookService pluginHookService;
  @Inject
  private BukkitService bukkitService;
  @Inject
  private Server server;
  
  public void executePurge(Collection<OfflinePlayer> players, Collection<String> names)
  {
    purgeFromAuthMe(names);
    purgeEssentials(players);
    purgeDat(players);
    purgeLimitedCreative(names);
    purgeAntiXray(names);
    purgePermissions(players);
  }
  
  synchronized void purgeAntiXray(Collection<String> cleared)
  {
    if (!((Boolean)this.settings.getProperty(PurgeSettings.REMOVE_ANTI_XRAY_FILE)).booleanValue()) {
      return;
    }
    int i = 0;
    File dataFolder = new File(FileUtils.makePath(new String[] { ".", "plugins", "AntiXRayData", "PlayerData" }));
    if ((!dataFolder.exists()) || (!dataFolder.isDirectory())) {
      return;
    }
    for (String file : dataFolder.list()) {
      if (cleared.contains(file.toLowerCase()))
      {
        File playerFile = new File(dataFolder, file);
        if ((playerFile.exists()) && (playerFile.delete())) {
          i++;
        }
      }
    }
    ConsoleLogger.info("AutoPurge: Removed " + i + " AntiXRayData Files");
  }
  
  synchronized void purgeFromAuthMe(Collection<String> names)
  {
    this.dataSource.purgeRecords(names);
    
    ConsoleLogger.info(ChatColor.GOLD + "Deleted " + names.size() + " user accounts");
  }
  
  synchronized void purgeLimitedCreative(Collection<String> cleared)
  {
    if (!((Boolean)this.settings.getProperty(PurgeSettings.REMOVE_LIMITED_CREATIVE_INVENTORIES)).booleanValue()) {
      return;
    }
    int i = 0;
    File dataFolder = new File(FileUtils.makePath(new String[] { ".", "plugins", "LimitedCreative", "inventories" }));
    if ((!dataFolder.exists()) || (!dataFolder.isDirectory())) {
      return;
    }
    for (String file : dataFolder.list())
    {
      String name = file;
      
      int idx = file.lastIndexOf("_creative.yml");
      if (idx != -1)
      {
        name = name.substring(0, idx);
      }
      else
      {
        idx = file.lastIndexOf("_adventure.yml");
        if (idx != -1)
        {
          name = name.substring(0, idx);
        }
        else
        {
          idx = file.lastIndexOf(".yml");
          if (idx != -1) {
            name = name.substring(0, idx);
          }
        }
      }
      if (!name.equals(file)) {
        if (cleared.contains(name.toLowerCase()))
        {
          File dataFile = new File(dataFolder, file);
          if ((dataFile.exists()) && (dataFile.delete())) {
            i++;
          }
        }
      }
    }
    ConsoleLogger.info("AutoPurge: Removed " + i + " LimitedCreative Survival, Creative and Adventure files");
  }
  
  synchronized void purgeDat(Collection<OfflinePlayer> cleared)
  {
    if (!((Boolean)this.settings.getProperty(PurgeSettings.REMOVE_PLAYER_DAT)).booleanValue()) {
      return;
    }
    int i = 0;
    
    File dataFolder = new File(this.server.getWorldContainer(), FileUtils.makePath(new String[] {(String)this.settings.getProperty(PurgeSettings.DEFAULT_WORLD), "players" }));
    for (OfflinePlayer offlinePlayer : cleared)
    {
      File playerFile = new File(dataFolder, PlayerUtils.getUuidOrName(offlinePlayer) + ".dat");
      if (playerFile.delete()) {
        i++;
      }
    }
    ConsoleLogger.info("AutoPurge: Removed " + i + " .dat Files");
  }
  
  synchronized void purgeEssentials(Collection<OfflinePlayer> cleared)
  {
    if (!((Boolean)this.settings.getProperty(PurgeSettings.REMOVE_ESSENTIALS_FILES)).booleanValue()) {
      return;
    }
    File essentialsDataFolder = this.pluginHookService.getEssentialsDataFolder();
    if (essentialsDataFolder == null)
    {
      ConsoleLogger.info("Cannot purge Essentials: plugin is not loaded");
      return;
    }
    File userDataFolder = new File(essentialsDataFolder, "userdata");
    if ((!userDataFolder.exists()) || (!userDataFolder.isDirectory())) {
      return;
    }
    int deletedFiles = 0;
    for (OfflinePlayer offlinePlayer : cleared)
    {
      File playerFile = new File(userDataFolder, PlayerUtils.getUuidOrName(offlinePlayer) + ".yml");
      if ((playerFile.exists()) && (playerFile.delete())) {
        deletedFiles++;
      }
    }
    ConsoleLogger.info("AutoPurge: Removed " + deletedFiles + " EssentialsFiles");
  }
  
  synchronized void purgePermissions(Collection<OfflinePlayer> cleared)
  {
    if (!((Boolean)this.settings.getProperty(PurgeSettings.REMOVE_PERMISSIONS)).booleanValue()) {
      return;
    }
    for (OfflinePlayer offlinePlayer : cleared)
    {
      try
      {
        this.permissionsManager.loadUserData(offlinePlayer.getUniqueId());
      }
      catch (NoSuchMethodError e)
      {
        this.permissionsManager.loadUserData(offlinePlayer.getName());
      }
      this.permissionsManager.removeAllGroups(offlinePlayer);
    }
    ConsoleLogger.info("AutoPurge: Removed permissions from " + cleared.size() + " player(s).");
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\task\purge\PurgeExecutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */