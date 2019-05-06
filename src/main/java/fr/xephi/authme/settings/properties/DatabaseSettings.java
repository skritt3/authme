package fr.xephi.authme.settings.properties;

import fr.xephi.authme.datasource.DataSourceType;
import fr.xephi.authme.libs.jalu.configme.Comment;
import fr.xephi.authme.libs.jalu.configme.SettingsHolder;
import fr.xephi.authme.libs.jalu.configme.properties.Property;
import fr.xephi.authme.libs.jalu.configme.properties.PropertyInitializer;

public final class DatabaseSettings
  implements SettingsHolder
{
  @Comment({"What type of database do you want to use?", "Valid values: SQLITE, MYSQL"})
  public static final Property<DataSourceType> BACKEND = PropertyInitializer.newProperty(DataSourceType.class, "DataSource.backend", DataSourceType.SQLITE);
  @Comment({"Enable the database caching system, should be disabled on bungeecord environments", "or when a website integration is being used."})
  public static final Property<Boolean> USE_CACHING = PropertyInitializer.newProperty("DataSource.caching", true);
  @Comment({"Database host address"})
  public static final Property<String> MYSQL_HOST = PropertyInitializer.newProperty("DataSource.mySQLHost", "127.0.0.1");
  @Comment({"Database port"})
  public static final Property<String> MYSQL_PORT = PropertyInitializer.newProperty("DataSource.mySQLPort", "3306");
  @Comment({"Connect to MySQL database over SSL"})
  public static final Property<Boolean> MYSQL_USE_SSL = PropertyInitializer.newProperty("DataSource.mySQLUseSSL", true);
  @Comment({"Username to connect to the MySQL database"})
  public static final Property<String> MYSQL_USERNAME = PropertyInitializer.newProperty("DataSource.mySQLUsername", "authme");
  @Comment({"Password to connect to the MySQL database"})
  public static final Property<String> MYSQL_PASSWORD = PropertyInitializer.newProperty("DataSource.mySQLPassword", "12345");
  @Comment({"Database Name, use with converters or as SQLITE database name"})
  public static final Property<String> MYSQL_DATABASE = PropertyInitializer.newProperty("DataSource.mySQLDatabase", "authme");
  @Comment({"Table of the database"})
  public static final Property<String> MYSQL_TABLE = PropertyInitializer.newProperty("DataSource.mySQLTablename", "authme");
  @Comment({"Column of IDs to sort data"})
  public static final Property<String> MYSQL_COL_ID = PropertyInitializer.newProperty("DataSource.mySQLColumnId", "id");
  @Comment({"Column for storing or checking players nickname"})
  public static final Property<String> MYSQL_COL_NAME = PropertyInitializer.newProperty("DataSource.mySQLColumnName", "username");
  @Comment({"Column for storing or checking players RealName"})
  public static final Property<String> MYSQL_COL_REALNAME = PropertyInitializer.newProperty("DataSource.mySQLRealName", "realname");
  @Comment({"Column for storing players passwords"})
  public static final Property<String> MYSQL_COL_PASSWORD = PropertyInitializer.newProperty("DataSource.mySQLColumnPassword", "password");
  @Comment({"Column for storing players passwords salts"})
  public static final Property<String> MYSQL_COL_SALT = PropertyInitializer.newProperty("ExternalBoardOptions.mySQLColumnSalt", "");
  @Comment({"Column for storing players emails"})
  public static final Property<String> MYSQL_COL_EMAIL = PropertyInitializer.newProperty("DataSource.mySQLColumnEmail", "email");
  @Comment({"Column for storing if a player is logged in or not"})
  public static final Property<String> MYSQL_COL_ISLOGGED = PropertyInitializer.newProperty("DataSource.mySQLColumnLogged", "isLogged");
  @Comment({"Column for storing if a player has a valid session or not"})
  public static final Property<String> MYSQL_COL_HASSESSION = PropertyInitializer.newProperty("DataSource.mySQLColumnHasSession", "hasSession");
  @Comment({"Column for storing the player's last IP"})
  public static final Property<String> MYSQL_COL_LAST_IP = PropertyInitializer.newProperty("DataSource.mySQLColumnIp", "ip");
  @Comment({"Column for storing players lastlogins"})
  public static final Property<String> MYSQL_COL_LASTLOGIN = PropertyInitializer.newProperty("DataSource.mySQLColumnLastLogin", "lastlogin");
  @Comment({"Column storing the registration date"})
  public static final Property<String> MYSQL_COL_REGISTER_DATE = PropertyInitializer.newProperty("DataSource.mySQLColumnRegisterDate", "regdate");
  @Comment({"Column for storing the IP address at the time of registration"})
  public static final Property<String> MYSQL_COL_REGISTER_IP = PropertyInitializer.newProperty("DataSource.mySQLColumnRegisterIp", "regip");
  @Comment({"Column for storing player LastLocation - X"})
  public static final Property<String> MYSQL_COL_LASTLOC_X = PropertyInitializer.newProperty("DataSource.mySQLlastlocX", "x");
  @Comment({"Column for storing player LastLocation - Y"})
  public static final Property<String> MYSQL_COL_LASTLOC_Y = PropertyInitializer.newProperty("DataSource.mySQLlastlocY", "y");
  @Comment({"Column for storing player LastLocation - Z"})
  public static final Property<String> MYSQL_COL_LASTLOC_Z = PropertyInitializer.newProperty("DataSource.mySQLlastlocZ", "z");
  @Comment({"Column for storing player LastLocation - World Name"})
  public static final Property<String> MYSQL_COL_LASTLOC_WORLD = PropertyInitializer.newProperty("DataSource.mySQLlastlocWorld", "world");
  @Comment({"Column for storing player LastLocation - Yaw"})
  public static final Property<String> MYSQL_COL_LASTLOC_YAW = PropertyInitializer.newProperty("DataSource.mySQLlastlocYaw", "yaw");
  @Comment({"Column for storing player LastLocation - Pitch"})
  public static final Property<String> MYSQL_COL_LASTLOC_PITCH = PropertyInitializer.newProperty("DataSource.mySQLlastlocPitch", "pitch");
  @Comment({"Column for storing players groups"})
  public static final Property<String> MYSQL_COL_GROUP = PropertyInitializer.newProperty("ExternalBoardOptions.mySQLColumnGroup", "");
  @Comment({"Overrides the size of the DB Connection Pool, -1 = Auto"})
  public static final Property<Integer> MYSQL_POOL_SIZE = PropertyInitializer.newProperty("DataSource.poolSize", -1);
  @Comment({"The maximum lifetime of a connection in the pool, default = 1800 seconds", "You should set this at least 30 seconds less than mysql server wait_timeout"})
  public static final Property<Integer> MYSQL_CONNECTION_MAX_LIFETIME = PropertyInitializer.newProperty("DataSource.maxLifetime", 1800);
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\settings\properties\DatabaseSettings.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */