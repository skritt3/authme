package fr.xephi.authme.service;

import fr.xephi.authme.AuthMe;
import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.initialization.SettingsDependent;
import fr.xephi.authme.libs.google.common.collect.Iterables;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.settings.properties.PluginSettings;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Set;
import java.util.function.Function;
import org.bukkit.BanEntry;
import org.bukkit.BanList;
import org.bukkit.BanList.Type;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

public class BukkitService
  implements SettingsDependent
{
  public static final int TICKS_PER_SECOND = 20;
  public static final int TICKS_PER_MINUTE = 1200;
  private final AuthMe authMe;
  private final boolean getOnlinePlayersIsCollection;
  private Method getOnlinePlayers;
  private boolean useAsyncTasks;
  
  @Inject
  BukkitService(AuthMe authMe, Settings settings)
  {
    this.authMe = authMe;
    this.getOnlinePlayersIsCollection = doesOnlinePlayersMethodReturnCollection();
    reload(settings);
  }
  
  public int scheduleSyncDelayedTask(Runnable task)
  {
    return Bukkit.getScheduler().scheduleSyncDelayedTask(this.authMe, task);
  }
  
  public int scheduleSyncDelayedTask(Runnable task, long delay)
  {
    return Bukkit.getScheduler().scheduleSyncDelayedTask(this.authMe, task, delay);
  }
  
  public void scheduleSyncTaskFromOptionallyAsyncTask(Runnable task)
  {
    if (Bukkit.isPrimaryThread()) {
      task.run();
    } else {
      scheduleSyncDelayedTask(task);
    }
  }
  
  public BukkitTask runTask(Runnable task)
  {
    return Bukkit.getScheduler().runTask(this.authMe, task);
  }
  
  public BukkitTask runTaskLater(Runnable task, long delay)
  {
    return Bukkit.getScheduler().runTaskLater(this.authMe, task, delay);
  }
  
  public void runTaskOptionallyAsync(Runnable task)
  {
    if (this.useAsyncTasks) {
      runTaskAsynchronously(task);
    } else {
      task.run();
    }
  }
  
  public BukkitTask runTaskAsynchronously(Runnable task)
  {
    return Bukkit.getScheduler().runTaskAsynchronously(this.authMe, task);
  }
  
  public BukkitTask runTaskTimerAsynchronously(Runnable task, long delay, long period)
  {
    return Bukkit.getScheduler().runTaskTimerAsynchronously(this.authMe, task, delay, period);
  }
  
  public BukkitTask runTaskTimer(BukkitRunnable task, long delay, long period)
  {
    return task.runTaskTimer(this.authMe, delay, period);
  }
  
  public int broadcastMessage(String message)
  {
    return Bukkit.broadcastMessage(message);
  }
  
  public Player getPlayerExact(String name)
  {
    return this.authMe.getServer().getPlayerExact(name);
  }
  
  public OfflinePlayer getOfflinePlayer(String name)
  {
    return this.authMe.getServer().getOfflinePlayer(name);
  }
  
  public Set<OfflinePlayer> getBannedPlayers()
  {
    return Bukkit.getBannedPlayers();
  }
  
  public OfflinePlayer[] getOfflinePlayers()
  {
    return Bukkit.getOfflinePlayers();
  }
  
  public Collection<? extends Player> getOnlinePlayers()
  {
    if (this.getOnlinePlayersIsCollection) {
      return Bukkit.getOnlinePlayers();
    }
    try
    {
      if (this.getOnlinePlayers == null) {
        this.getOnlinePlayers = Bukkit.class.getDeclaredMethod("getOnlinePlayers", new Class[0]);
      }
      Object obj = this.getOnlinePlayers.invoke(null, new Object[0]);
      if ((obj instanceof Collection)) {
        return (Collection)obj;
      }
      if ((obj instanceof Player[])) {
        return Arrays.asList((Player[])obj);
      }
      String type = obj == null ? "null" : obj.getClass().getName();
      ConsoleLogger.warning("Unknown list of online players of type " + type);
    }
    catch (NoSuchMethodException|IllegalAccessException|InvocationTargetException e)
    {
      ConsoleLogger.logException("Could not retrieve list of online players:", e);
    }
    return Collections.emptyList();
  }
  
  public void callEvent(Event event)
  {
    Bukkit.getPluginManager().callEvent(event);
  }
  
  public <E extends Event> E createAndCallEvent(Function<Boolean, E> eventSupplier)
  {
    E event = eventSupplier.apply(Boolean.valueOf(this.useAsyncTasks));
    callEvent(event);
    return event;
  }
  
  public World getWorld(String name)
  {
    return Bukkit.getWorld(name);
  }
  
  public boolean dispatchCommand(CommandSender sender, String commandLine)
  {
    return Bukkit.dispatchCommand(sender, commandLine);
  }
  
  public boolean dispatchConsoleCommand(String commandLine)
  {
    return Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commandLine);
  }
  
  public void reload(Settings settings)
  {
    this.useAsyncTasks = ((Boolean)settings.getProperty(PluginSettings.USE_ASYNC_TASKS)).booleanValue();
  }
  
  public void sendBungeeMessage(byte[] bytes)
  {
    Player player = (Player)Iterables.getFirst(getOnlinePlayers(), null);
    if (player != null) {
      player.sendPluginMessage(this.authMe, "BungeeCord", bytes);
    }
  }
  
  private static boolean doesOnlinePlayersMethodReturnCollection()
  {
    try
    {
      Method method = Bukkit.class.getDeclaredMethod("getOnlinePlayers", new Class[0]);
      return method.getReturnType() == Collection.class;
    }
    catch (NoSuchMethodException e)
    {
      ConsoleLogger.warning("Error verifying if getOnlinePlayers is a collection! Method doesn't exist");
    }
    return false;
  }
  
  public BanEntry banIp(String ip, String reason, Date expires, String source)
  {
    return Bukkit.getServer().getBanList(BanList.Type.IP).addBan(ip, reason, expires, source);
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\service\BukkitService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */