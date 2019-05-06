package fr.xephi.authme;

import fr.xephi.authme.api.NewAPI;
import fr.xephi.authme.api.v3.AuthMeApi;
import fr.xephi.authme.command.CommandHandler;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.initialization.DataFolder;
import fr.xephi.authme.initialization.DataSourceProvider;
import fr.xephi.authme.initialization.OnShutdownPlayerSaver;
import fr.xephi.authme.initialization.OnStartupTasks;
import fr.xephi.authme.initialization.SettingsProvider;
import fr.xephi.authme.initialization.TaskCloser;
import fr.xephi.authme.initialization.factory.FactoryDependencyHandler;
import fr.xephi.authme.initialization.factory.SingletonStoreDependencyHandler;
import fr.xephi.authme.libs.google.common.annotations.VisibleForTesting;
import fr.xephi.authme.libs.jalu.injector.Injector;
import fr.xephi.authme.libs.jalu.injector.InjectorBuilder;
import fr.xephi.authme.libs.jalu.injector.handlers.Handler;
import fr.xephi.authme.listener.BlockListener;
import fr.xephi.authme.listener.EntityListener;
import fr.xephi.authme.listener.PlayerListener;
import fr.xephi.authme.listener.PlayerListener111;
import fr.xephi.authme.listener.PlayerListener16;
import fr.xephi.authme.listener.PlayerListener18;
import fr.xephi.authme.listener.PlayerListener19;
import fr.xephi.authme.listener.PlayerListener19Spigot;
import fr.xephi.authme.listener.ServerListener;
import fr.xephi.authme.security.crypts.Sha256;
import fr.xephi.authme.service.BackupService;
import fr.xephi.authme.service.BackupService.BackupCause;
import fr.xephi.authme.service.BukkitService;
import fr.xephi.authme.service.MigrationService;
import fr.xephi.authme.service.bungeecord.BungeeReceiver;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.settings.SettingsWarner;
import fr.xephi.authme.settings.properties.SecuritySettings;
import fr.xephi.authme.task.CleanupTask;
import fr.xephi.authme.task.purge.PurgeService;
import fr.xephi.authme.util.Utils;
import java.io.File;
import java.util.Collection;
import java.util.logging.Logger;
import org.apache.commons.lang.SystemUtils;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.bukkit.scheduler.BukkitScheduler;

public class AuthMe
  extends JavaPlugin
{
  private static final String PLUGIN_NAME = "AuthMeReloaded";
  private static final String LOG_FILENAME = "authme.log";
  private static final int CLEANUP_INTERVAL = 6000;
  private static String pluginVersion = "N/D";
  private static String pluginBuildNumber = "Unknown";
  private CommandHandler commandHandler;
  private Settings settings;
  private DataSource database;
  private BukkitService bukkitService;
  private Injector injector;
  private BackupService backupService;
  
  public AuthMe() {}
  
  @VisibleForTesting
  protected AuthMe(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file)
  {
    super(loader, description, dataFolder, file);
  }
  
  public static String getPluginName()
  {
    return "AuthMeReloaded";
  }
  
  public static String getPluginVersion()
  {
    return pluginVersion;
  }
  
  public static String getPluginBuildNumber()
  {
    return pluginBuildNumber;
  }
  
  @Deprecated
  public static NewAPI getApi()
  {
    return NewAPI.getInstance();
  }
  
  public void onEnable()
  {
    loadPluginInfo(getDescription().getVersion());
    try
    {
      initialize();
    }
    catch (Throwable th)
    {
      ConsoleLogger.logException("Aborting initialization of AuthMe:", th);
      stopOrUnload();
      return;
    }
    ((SettingsWarner)this.injector.getSingleton(SettingsWarner.class)).logWarningsForMisconfigurations();
    
    this.backupService.doBackup(BackupService.BackupCause.START);
    
    OnStartupTasks.sendMetrics(this, this.settings);
    
    ConsoleLogger.info("Development builds are available on our jenkins, thanks to f14stelt.");
    ConsoleLogger.info("Do you want a good game server? Look at our sponsor GameHosting.it leader in Italy as Game Server Provider!");
    
    ConsoleLogger.info("AuthMe " + getPluginVersion() + " build n." + getPluginBuildNumber() + " correctly enabled!");
    
    PurgeService purgeService = (PurgeService)this.injector.getSingleton(PurgeService.class);
    purgeService.runAutoPurge();
    
    CleanupTask cleanupTask = (CleanupTask)this.injector.getSingleton(CleanupTask.class);
    cleanupTask.runTaskTimerAsynchronously(this, 6000L, 6000L);
  }
  
  private static void loadPluginInfo(String versionRaw)
  {
    int index = versionRaw.lastIndexOf("-");
    if (index != -1)
    {
      pluginVersion = versionRaw.substring(0, index);
      pluginBuildNumber = versionRaw.substring(index + 1);
      if (pluginBuildNumber.startsWith("b")) {
        pluginBuildNumber = pluginBuildNumber.substring(1);
      }
    }
  }
  
  private void initialize()
  {
    ConsoleLogger.setLogger(getLogger());
    ConsoleLogger.setLogFile(new File(getDataFolder(), "authme.log"));
    if (!SystemUtils.isJavaVersionAtLeast(1.8F)) {
      throw new IllegalStateException("You need Java 1.8 or above to run this plugin!");
    }
    getDataFolder().mkdir();
    
    this.injector = new InjectorBuilder().addHandlers(new Handler[] { new FactoryDependencyHandler(), new SingletonStoreDependencyHandler() }).addDefaultHandlers("fr.xephi.authme").create();
    this.injector.register(AuthMe.class, this);
    this.injector.register(Server.class, getServer());
    this.injector.register(PluginManager.class, getServer().getPluginManager());
    this.injector.register(BukkitScheduler.class, getServer().getScheduler());
    this.injector.provide(DataFolder.class, getDataFolder());
    this.injector.registerProvider(Settings.class, SettingsProvider.class);
    this.injector.registerProvider(DataSource.class, DataSourceProvider.class);
    
    this.settings = ((Settings)this.injector.getSingleton(Settings.class));
    ConsoleLogger.setLoggingOptions(this.settings);
    OnStartupTasks.setupConsoleFilter(this.settings, getLogger());
    
    instantiateServices(this.injector);
    
    MigrationService.changePlainTextToSha256(this.settings, this.database, new Sha256());
    if (this.bukkitService.getOnlinePlayers().isEmpty()) {
      this.database.purgeLogged();
    }
    registerEventListeners(this.injector);
    
    OnStartupTasks onStartupTasks = (OnStartupTasks)this.injector.newInstance(OnStartupTasks.class);
    onStartupTasks.scheduleRecallEmailTask();
  }
  
  void instantiateServices(Injector injector)
  {
    this.database = ((DataSource)injector.getSingleton(DataSource.class));
    this.bukkitService = ((BukkitService)injector.getSingleton(BukkitService.class));
    this.commandHandler = ((CommandHandler)injector.getSingleton(CommandHandler.class));
    this.backupService = ((BackupService)injector.getSingleton(BackupService.class));
    
    injector.getSingleton(BungeeReceiver.class);
    
    injector.getSingleton(AuthMeApi.class);
    injector.getSingleton(NewAPI.class);
  }
  
  void registerEventListeners(Injector injector)
  {
    PluginManager pluginManager = getServer().getPluginManager();
    
    pluginManager.registerEvents((Listener)injector.getSingleton(PlayerListener.class), this);
    pluginManager.registerEvents((Listener)injector.getSingleton(BlockListener.class), this);
    pluginManager.registerEvents((Listener)injector.getSingleton(EntityListener.class), this);
    pluginManager.registerEvents((Listener)injector.getSingleton(ServerListener.class), this);
    if (Utils.isClassLoaded("org.bukkit.event.player.PlayerEditBookEvent")) {
      pluginManager.registerEvents((Listener)injector.getSingleton(PlayerListener16.class), this);
    }
    if (Utils.isClassLoaded("org.bukkit.event.player.PlayerInteractAtEntityEvent")) {
      pluginManager.registerEvents((Listener)injector.getSingleton(PlayerListener18.class), this);
    }
    if (Utils.isClassLoaded("org.bukkit.event.player.PlayerSwapHandItemsEvent")) {
      pluginManager.registerEvents((Listener)injector.getSingleton(PlayerListener19.class), this);
    }
    if (Utils.isClassLoaded("org.spigotmc.event.player.PlayerSpawnLocationEvent")) {
      pluginManager.registerEvents((Listener)injector.getSingleton(PlayerListener19Spigot.class), this);
    }
    if (Utils.isClassLoaded("org.bukkit.event.entity.EntityAirChangeEvent")) {
      pluginManager.registerEvents((Listener)injector.getSingleton(PlayerListener111.class), this);
    }
  }
  
  public void stopOrUnload()
  {
    if ((this.settings == null) || (((Boolean)this.settings.getProperty(SecuritySettings.STOP_SERVER_ON_PROBLEM)).booleanValue()))
    {
      ConsoleLogger.warning("THE SERVER IS GOING TO SHUT DOWN AS DEFINED IN THE CONFIGURATION!");
      setEnabled(false);
      getServer().shutdown();
    }
    else
    {
      setEnabled(false);
    }
  }
  
  public void onDisable()
  {
    OnShutdownPlayerSaver onShutdownPlayerSaver = this.injector == null ? null : (OnShutdownPlayerSaver)this.injector.createIfHasDependencies(OnShutdownPlayerSaver.class);
    if (onShutdownPlayerSaver != null) {
      onShutdownPlayerSaver.saveAllPlayers();
    }
    if (this.backupService != null) {
      this.backupService.doBackup(BackupService.BackupCause.STOP);
    }
    new TaskCloser(this, this.database).run();
    
    ConsoleLogger.info("AuthMe " + getDescription().getVersion() + " disabled!");
    ConsoleLogger.close();
  }
  
  public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
  {
    if (this.commandHandler == null)
    {
      getLogger().severe("AuthMe command handler is not available");
      return false;
    }
    return this.commandHandler.processCommand(sender, commandLabel, args);
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\AuthMe.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */