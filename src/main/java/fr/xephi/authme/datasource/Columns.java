package fr.xephi.authme.datasource;

import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.settings.properties.DatabaseSettings;

public final class Columns
{
  public final String NAME;
  public final String REAL_NAME;
  public final String PASSWORD;
  public final String SALT;
  public final String LAST_IP;
  public final String LAST_LOGIN;
  public final String GROUP;
  public final String LASTLOC_X;
  public final String LASTLOC_Y;
  public final String LASTLOC_Z;
  public final String LASTLOC_WORLD;
  public final String LASTLOC_YAW;
  public final String LASTLOC_PITCH;
  public final String EMAIL;
  public final String ID;
  public final String IS_LOGGED;
  public final String HAS_SESSION;
  public final String REGISTRATION_DATE;
  public final String REGISTRATION_IP;
  
  public Columns(Settings settings)
  {
    this.NAME = ((String)settings.getProperty(DatabaseSettings.MYSQL_COL_NAME));
    this.REAL_NAME = ((String)settings.getProperty(DatabaseSettings.MYSQL_COL_REALNAME));
    this.PASSWORD = ((String)settings.getProperty(DatabaseSettings.MYSQL_COL_PASSWORD));
    this.SALT = ((String)settings.getProperty(DatabaseSettings.MYSQL_COL_SALT));
    this.LAST_IP = ((String)settings.getProperty(DatabaseSettings.MYSQL_COL_LAST_IP));
    this.LAST_LOGIN = ((String)settings.getProperty(DatabaseSettings.MYSQL_COL_LASTLOGIN));
    this.GROUP = ((String)settings.getProperty(DatabaseSettings.MYSQL_COL_GROUP));
    this.LASTLOC_X = ((String)settings.getProperty(DatabaseSettings.MYSQL_COL_LASTLOC_X));
    this.LASTLOC_Y = ((String)settings.getProperty(DatabaseSettings.MYSQL_COL_LASTLOC_Y));
    this.LASTLOC_Z = ((String)settings.getProperty(DatabaseSettings.MYSQL_COL_LASTLOC_Z));
    this.LASTLOC_WORLD = ((String)settings.getProperty(DatabaseSettings.MYSQL_COL_LASTLOC_WORLD));
    this.LASTLOC_YAW = ((String)settings.getProperty(DatabaseSettings.MYSQL_COL_LASTLOC_YAW));
    this.LASTLOC_PITCH = ((String)settings.getProperty(DatabaseSettings.MYSQL_COL_LASTLOC_PITCH));
    this.EMAIL = ((String)settings.getProperty(DatabaseSettings.MYSQL_COL_EMAIL));
    this.ID = ((String)settings.getProperty(DatabaseSettings.MYSQL_COL_ID));
    this.IS_LOGGED = ((String)settings.getProperty(DatabaseSettings.MYSQL_COL_ISLOGGED));
    this.HAS_SESSION = ((String)settings.getProperty(DatabaseSettings.MYSQL_COL_HASSESSION));
    this.REGISTRATION_DATE = ((String)settings.getProperty(DatabaseSettings.MYSQL_COL_REGISTER_DATE));
    this.REGISTRATION_IP = ((String)settings.getProperty(DatabaseSettings.MYSQL_COL_REGISTER_IP));
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\datasource\Columns.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */