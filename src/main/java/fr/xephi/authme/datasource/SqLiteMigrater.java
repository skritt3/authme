package fr.xephi.authme.datasource;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.settings.properties.DatabaseSettings;
import fr.xephi.authme.util.FileUtils;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

class SqLiteMigrater
{
  private final File dataFolder;
  private final String databaseName;
  private final String tableName;
  private final Columns col;
  
  SqLiteMigrater(Settings settings, File dataFolder)
  {
    this.dataFolder = dataFolder;
    this.databaseName = ((String)settings.getProperty(DatabaseSettings.MYSQL_DATABASE));
    this.tableName = ((String)settings.getProperty(DatabaseSettings.MYSQL_TABLE));
    this.col = new Columns(settings);
  }
  
  static boolean isMigrationRequired(DatabaseMetaData metaData, String tableName, Columns col)
    throws SQLException
  {
    return (SqlDataSourceUtils.isNotNullColumn(metaData, tableName, col.LAST_IP)) && 
      (SqlDataSourceUtils.getColumnDefaultValue(metaData, tableName, col.LAST_IP) == null);
  }
  
  void performMigration(SQLite sqLite)
    throws SQLException
  {
    ConsoleLogger.warning("YOUR SQLITE DATABASE NEEDS MIGRATING! DO NOT TURN OFF YOUR SERVER");
    
    String backupName = createBackup();
    ConsoleLogger.info("Made a backup of your database at 'backups/" + backupName + "'");
    
    recreateDatabaseWithNewDefinitions(sqLite);
    ConsoleLogger.info("SQLite database migrated successfully");
  }
  
  private String createBackup()
  {
    File sqLite = new File(this.dataFolder, this.databaseName + ".db");
    File backupDirectory = new File(this.dataFolder, "backups");
    FileUtils.createDirectory(backupDirectory);
    
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm");
    String backupName = "backup-" + this.databaseName + dateFormat.format(new Date()) + ".db";
    File backup = new File(backupDirectory, backupName);
    try
    {
      Files.copy(sqLite.toPath(), backup.toPath(), new CopyOption[0]);
      return backupName;
    }
    catch (IOException e)
    {
      throw new IllegalStateException("Failed to create SQLite backup before migration", e);
    }
  }
  
  private void recreateDatabaseWithNewDefinitions(SQLite sqLite)
    throws SQLException
  {
    Connection connection = getConnection(sqLite);
    String tempTable = "tmp_" + this.tableName;
    Statement st = connection.createStatement();Throwable localThrowable6 = null;
    try
    {
      st.execute("ALTER TABLE " + this.tableName + " RENAME TO " + tempTable + ";");
    }
    catch (Throwable localThrowable1)
    {
      localThrowable6 = localThrowable1;throw localThrowable1;
    }
    finally
    {
      if (st != null) {
        if (localThrowable6 != null) {
          try
          {
            st.close();
          }
          catch (Throwable localThrowable2)
          {
            localThrowable6.addSuppressed(localThrowable2);
          }
        } else {
          st.close();
        }
      }
    }
    sqLite.reload();
    connection = getConnection(sqLite);
    
    st = connection.createStatement();localThrowable6 = null;
    try
    {
      String copySql = "INSERT INTO $table ($id, $name, $realName, $password, $lastIp, $lastLogin, $regIp, $regDate, $locX, $locY, $locZ, $locWorld, $locPitch, $locYaw, $email, $isLogged)SELECT $id, $name, $realName, $password, CASE WHEN $lastIp = '127.0.0.1' OR $lastIp = '' THEN NULL else $lastIp END, $lastLogin, $regIp, $regDate, $locX, $locY, $locZ, $locWorld, $locPitch, $locYaw, CASE WHEN $email = 'your@email.com' THEN NULL ELSE $email END, $isLogged FROM " + tempTable + ";";
      
      int insertedEntries = st.executeUpdate(replaceColumnVariables(copySql));
      ConsoleLogger.info("Copied over " + insertedEntries + " from the old table to the new one");
      
      st.execute("DROP TABLE " + tempTable + ";");
    }
    catch (Throwable localThrowable4)
    {
      localThrowable6 = localThrowable4;throw localThrowable4;
    }
    finally
    {
      if (st != null) {
        if (localThrowable6 != null) {
          try
          {
            st.close();
          }
          catch (Throwable localThrowable5)
          {
            localThrowable6.addSuppressed(localThrowable5);
          }
        } else {
          st.close();
        }
      }
    }
  }
  
  private String replaceColumnVariables(String sql)
  {
    String replacedSql = sql.replace("$table", this.tableName).replace("$id", this.col.ID).replace("$name", this.col.NAME).replace("$realName", this.col.REAL_NAME).replace("$password", this.col.PASSWORD).replace("$lastIp", this.col.LAST_IP).replace("$lastLogin", this.col.LAST_LOGIN).replace("$regIp", this.col.REGISTRATION_IP).replace("$regDate", this.col.REGISTRATION_DATE).replace("$locX", this.col.LASTLOC_X).replace("$locY", this.col.LASTLOC_Y).replace("$locZ", this.col.LASTLOC_Z).replace("$locWorld", this.col.LASTLOC_WORLD).replace("$locPitch", this.col.LASTLOC_PITCH).replace("$locYaw", this.col.LASTLOC_YAW).replace("$email", this.col.EMAIL).replace("$isLogged", this.col.IS_LOGGED);
    if (replacedSql.contains("$")) {
      throw new IllegalStateException("SQL still statement still has '$' in it - was a tag not replaced? Replacement result: " + replacedSql);
    }
    return replacedSql;
  }
  
  private static Connection getConnection(SQLite sqLite)
  {
    try
    {
      Field connectionField = SQLite.class.getDeclaredField("con");
      connectionField.setAccessible(true);
      return (Connection)connectionField.get(sqLite);
    }
    catch (NoSuchFieldException|IllegalAccessException e)
    {
      throw new IllegalStateException("Failed to get the connection from SQLite", e);
    }
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\datasource\SqLiteMigrater.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */