package fr.xephi.authme.service;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.datasource.DataSourceType;
import fr.xephi.authme.initialization.DataFolder;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.settings.properties.BackupSettings;
import fr.xephi.authme.settings.properties.DatabaseSettings;
import fr.xephi.authme.util.FileUtils;
import fr.xephi.authme.util.Utils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.bukkit.command.CommandSender;

public class BackupService
{
  private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm");
  private final File dataFolder;
  private final File backupFolder;
  private final Settings settings;
  
  @Inject
  public BackupService(@DataFolder File dataFolder, Settings settings)
  {
    this.dataFolder = dataFolder;
    this.backupFolder = new File(dataFolder, "backups");
    this.settings = settings;
  }
  
  public void doBackup(BackupCause cause)
  {
    doBackup(cause, null);
  }
  
  public void doBackup(BackupCause cause, CommandSender sender)
  {
    if (!((Boolean)this.settings.getProperty(BackupSettings.ENABLED)).booleanValue())
    {
      if ((cause == BackupCause.COMMAND) || (cause == BackupCause.OTHER)) {
        Utils.logAndSendWarning(sender, "Can't perform a backup: disabled in configuration. Cause of the backup: " + cause
          .name());
      }
      return;
    }
    if (((BackupCause.START == cause) && (!((Boolean)this.settings.getProperty(BackupSettings.ON_SERVER_START)).booleanValue())) || ((BackupCause.STOP == cause) && 
      (!((Boolean)this.settings.getProperty(BackupSettings.ON_SERVER_STOP)).booleanValue()))) {
      return;
    }
    if (doBackup()) {
      Utils.logAndSendMessage(sender, "A backup has been performed successfully. Cause of the backup: " + cause
        .name());
    } else {
      Utils.logAndSendWarning(sender, "Error while performing a backup! Cause of the backup: " + cause.name());
    }
  }
  
  private boolean doBackup()
  {
    DataSourceType dataSourceType = (DataSourceType)this.settings.getProperty(DatabaseSettings.BACKEND);
    switch (dataSourceType)
    {
    case FILE: 
      return performFileBackup("auths.db");
    case MYSQL: 
      return performMySqlBackup();
    case SQLITE: 
      String dbName = (String)this.settings.getProperty(DatabaseSettings.MYSQL_DATABASE);
      return performFileBackup(dbName + ".db");
    }
    ConsoleLogger.warning("Unknown data source type '" + dataSourceType + "' for backup");
    
    return false;
  }
  
  private boolean performMySqlBackup()
  {
    FileUtils.createDirectory(this.backupFolder);
    File sqlBackupFile = constructBackupFile("sql");
    
    String backupWindowsPath = (String)this.settings.getProperty(BackupSettings.MYSQL_WINDOWS_PATH);
    boolean isUsingWindows = useWindowsCommand(backupWindowsPath);
    
    String backupCommand = "mysqldump" + buildMysqlDumpArguments(sqlBackupFile);
    try
    {
      Process runtimeProcess = Runtime.getRuntime().exec(backupCommand);
      int processComplete = runtimeProcess.waitFor();
      if (processComplete == 0)
      {
        ConsoleLogger.info("Backup created successfully. (Using Windows = " + isUsingWindows + ")");
        return true;
      }
      ConsoleLogger.warning("Could not create the backup! (Using Windows = " + isUsingWindows + ")");
    }
    catch (IOException|InterruptedException e)
    {
      ConsoleLogger.logException("Error during backup (using Windows = " + isUsingWindows + "):", e);
    }
    return false;
  }
  
  private boolean performFileBackup(String filename)
  {
    FileUtils.createDirectory(this.backupFolder);
    File backupFile = constructBackupFile("db");
    try
    {
      copy(new File(this.dataFolder, filename), backupFile);
      return true;
    }
    catch (IOException ex)
    {
      ConsoleLogger.logException("Encountered an error during file backup:", ex);
    }
    return false;
  }
  
  private static boolean useWindowsCommand(String windowsPath)
  {
    String isWin = System.getProperty("os.name").toLowerCase();
    if (isWin.contains("win"))
    {
      if (new File(windowsPath + "\\bin\\mysqldump.exe").exists()) {
        return true;
      }
      ConsoleLogger.warning("Mysql Windows Path is incorrect. Please check it");
      return false;
    }
    return false;
  }
  
  private String buildMysqlDumpArguments(File sqlBackupFile)
  {
    String dbUsername = (String)this.settings.getProperty(DatabaseSettings.MYSQL_USERNAME);
    String dbPassword = (String)this.settings.getProperty(DatabaseSettings.MYSQL_PASSWORD);
    String dbName = (String)this.settings.getProperty(DatabaseSettings.MYSQL_DATABASE);
    String tableName = (String)this.settings.getProperty(DatabaseSettings.MYSQL_TABLE);
    
    return " -u " + dbUsername + " -p" + dbPassword + " " + dbName + " --tables " + tableName + " -r " + sqlBackupFile
      .getPath() + ".sql";
  }
  
  private File constructBackupFile(String fileExtension)
  {
    String dateString = this.dateFormat.format(new Date());
    return new File(this.backupFolder, "backup" + dateString + "." + fileExtension);
  }
  
  private static void copy(File src, File dst)
    throws IOException
  {
    InputStream in = new FileInputStream(src);Throwable localThrowable6 = null;
    try
    {
      OutputStream out = new FileOutputStream(dst);Throwable localThrowable7 = null;
      try
      {
        byte[] buf = new byte['Ѐ'];
        int len;
        while ((len = in.read(buf)) > 0) {
          out.write(buf, 0, len);
        }
      }
      catch (Throwable localThrowable1)
      {
        localThrowable7 = localThrowable1;throw localThrowable1;
      }
      finally {}
    }
    catch (Throwable localThrowable4)
    {
      localThrowable6 = localThrowable4;throw localThrowable4;
    }
    finally
    {
      if (in != null) {
        if (localThrowable6 != null) {
          try
          {
            in.close();
          }
          catch (Throwable localThrowable5)
          {
            localThrowable6.addSuppressed(localThrowable5);
          }
        } else {
          in.close();
        }
      }
    }
  }
  
  public static enum BackupCause
  {
    START,  STOP,  COMMAND,  OTHER;
    
    private BackupCause() {}
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\service\BackupService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */