package fr.xephi.authme.settings.properties;

import fr.xephi.authme.libs.google.common.collect.ImmutableMap;
import fr.xephi.authme.libs.jalu.configme.Comment;
import fr.xephi.authme.libs.jalu.configme.SectionComments;
import fr.xephi.authme.libs.jalu.configme.SettingsHolder;
import fr.xephi.authme.libs.jalu.configme.properties.Property;
import fr.xephi.authme.libs.jalu.configme.properties.PropertyInitializer;
import java.util.Map;

public final class ConverterSettings
  implements SettingsHolder
{
  @Comment({"Rakamak file name"})
  public static final Property<String> RAKAMAK_FILE_NAME = PropertyInitializer.newProperty("Converter.Rakamak.fileName", "users.rak");
  @Comment({"Rakamak use IP?"})
  public static final Property<Boolean> RAKAMAK_USE_IP = PropertyInitializer.newProperty("Converter.Rakamak.useIP", false);
  @Comment({"Rakamak IP file name"})
  public static final Property<String> RAKAMAK_IP_FILE_NAME = PropertyInitializer.newProperty("Converter.Rakamak.ipFileName", "UsersIp.rak");
  @Comment({"CrazyLogin database file name"})
  public static final Property<String> CRAZYLOGIN_FILE_NAME = PropertyInitializer.newProperty("Converter.CrazyLogin.fileName", "accounts.db");
  @Comment({"LoginSecurity: convert from SQLite; if false we use MySQL"})
  public static final Property<Boolean> LOGINSECURITY_USE_SQLITE = PropertyInitializer.newProperty("Converter.loginSecurity.useSqlite", true);
  @Comment({"LoginSecurity MySQL: database host"})
  public static final Property<String> LOGINSECURITY_MYSQL_HOST = PropertyInitializer.newProperty("Converter.loginSecurity.mySql.host", "");
  @Comment({"LoginSecurity MySQL: database name"})
  public static final Property<String> LOGINSECURITY_MYSQL_DATABASE = PropertyInitializer.newProperty("Converter.loginSecurity.mySql.database", "");
  @Comment({"LoginSecurity MySQL: database user"})
  public static final Property<String> LOGINSECURITY_MYSQL_USER = PropertyInitializer.newProperty("Converter.loginSecurity.mySql.user", "");
  @Comment({"LoginSecurity MySQL: password for database user"})
  public static final Property<String> LOGINSECURITY_MYSQL_PASSWORD = PropertyInitializer.newProperty("Converter.loginSecurity.mySql.password", "");
  
  @SectionComments
  public static Map<String, String[]> buildSectionComments()
  {
    return ImmutableMap.of("Converter", new String[] { "Converter settings: see https://github.com/AuthMe/AuthMeReloaded/wiki/Converters" });
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\settings\properties\ConverterSettings.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */