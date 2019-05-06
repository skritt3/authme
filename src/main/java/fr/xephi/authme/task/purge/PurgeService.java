package fr.xephi.authme.task.purge;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.permission.PermissionsManager;
import fr.xephi.authme.service.BukkitService;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.settings.properties.PurgeSettings;
import fr.xephi.authme.util.Utils;
import java.util.Calendar;
import java.util.Collection;
import java.util.Set;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

public class PurgeService
{
  @Inject
  private BukkitService bukkitService;
  @Inject
  private DataSource dataSource;
  @Inject
  private Settings settings;
  @Inject
  private PermissionsManager permissionsManager;
  @Inject
  private PurgeExecutor purgeExecutor;
  private boolean isPurging = false;
  
  public void runAutoPurge()
  {
    int daysBeforePurge = ((Integer)this.settings.getProperty(PurgeSettings.DAYS_BEFORE_REMOVE_PLAYER)).intValue();
    if (!((Boolean)this.settings.getProperty(PurgeSettings.USE_AUTO_PURGE)).booleanValue()) {
      return;
    }
    if (daysBeforePurge <= 0)
    {
      ConsoleLogger.warning("Did not run auto purge: configured days before purging must be positive");
      return;
    }
    ConsoleLogger.info("Automatically purging the database...");
    Calendar calendar = Calendar.getInstance();
    calendar.add(5, -daysBeforePurge);
    long until = calendar.getTimeInMillis();
    
    runPurge(null, until);
  }
  
  public void runPurge(CommandSender sender, long until)
  {
    Set<String> toPurge = this.dataSource.getRecordsToPurge(until);
    if (Utils.isCollectionEmpty(toPurge))
    {
      Utils.logAndSendMessage(sender, "No players to purge");
      return;
    }
    purgePlayers(sender, toPurge, this.bukkitService.getOfflinePlayers());
  }
  
  public void purgePlayers(CommandSender sender, Set<String> names, OfflinePlayer[] players)
  {
    if (this.isPurging)
    {
      Utils.logAndSendMessage(sender, "Purge is already in progress! Aborting purge request");
      return;
    }
    this.isPurging = true;
    PurgeTask purgeTask = new PurgeTask(this, this.permissionsManager, sender, names, players);
    this.bukkitService.runTaskTimer(purgeTask, 0L, 1L);
  }
  
  void setPurging(boolean purging)
  {
    this.isPurging = purging;
  }
  
  void executePurge(Collection<OfflinePlayer> players, Collection<String> names)
  {
    this.purgeExecutor.executePurge(players, names);
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\task\purge\PurgeService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */