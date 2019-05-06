package fr.xephi.authme.settings.properties;

import fr.xephi.authme.libs.jalu.configme.Comment;
import fr.xephi.authme.libs.jalu.configme.SettingsHolder;
import fr.xephi.authme.libs.jalu.configme.properties.Property;
import fr.xephi.authme.libs.jalu.configme.properties.PropertyInitializer;

public final class BackupSettings
  implements SettingsHolder
{
  @Comment({"General configuration for backups: if false, no backups are possible"})
  public static final Property<Boolean> ENABLED = PropertyInitializer.newProperty("BackupSystem.ActivateBackup", false);
  @Comment({"Create backup at every start of server"})
  public static final Property<Boolean> ON_SERVER_START = PropertyInitializer.newProperty("BackupSystem.OnServerStart", false);
  @Comment({"Create backup at every stop of server"})
  public static final Property<Boolean> ON_SERVER_STOP = PropertyInitializer.newProperty("BackupSystem.OnServerStop", true);
  @Comment({"Windows only: MySQL installation path"})
  public static final Property<String> MYSQL_WINDOWS_PATH = PropertyInitializer.newProperty("BackupSystem.MysqlWindowsPath", "C:\\Program Files\\MySQL\\MySQL Server 5.1\\");
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\settings\properties\BackupSettings.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */