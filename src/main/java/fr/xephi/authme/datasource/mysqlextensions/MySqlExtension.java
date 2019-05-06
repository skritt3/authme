package fr.xephi.authme.datasource.mysqlextensions;

import fr.xephi.authme.data.auth.PlayerAuth;
import fr.xephi.authme.datasource.Columns;
import fr.xephi.authme.security.crypts.HashedPassword;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.settings.properties.DatabaseSettings;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.OptionalInt;

public abstract class MySqlExtension
{
  protected final Columns col;
  protected final String tableName;
  
  MySqlExtension(Settings settings, Columns col)
  {
    this.col = col;
    this.tableName = ((String)settings.getProperty(DatabaseSettings.MYSQL_TABLE));
  }
  
  public void saveAuth(PlayerAuth auth, Connection con)
    throws SQLException
  {}
  
  public void extendAuth(PlayerAuth auth, int id, Connection con)
    throws SQLException
  {}
  
  public void changePassword(String user, HashedPassword password, Connection con)
    throws SQLException
  {}
  
  public void removeAuth(String user, Connection con)
    throws SQLException
  {}
  
  protected OptionalInt retrieveIdFromTable(String name, Connection con)
    throws SQLException
  {
    String sql = "SELECT " + this.col.ID + " FROM " + this.tableName + " WHERE " + this.col.NAME + "=?;";
    PreparedStatement pst = con.prepareStatement(sql);Throwable localThrowable8 = null;
    try
    {
      pst.setString(1, name);
      ResultSet rs = pst.executeQuery();Throwable localThrowable9 = null;
      try
      {
        if (rs.next()) {
          return OptionalInt.of(rs.getInt(this.col.ID));
        }
      }
      catch (Throwable localThrowable11)
      {
        localThrowable9 = localThrowable11;throw localThrowable11;
      }
      finally {}
    }
    catch (Throwable localThrowable6)
    {
      localThrowable8 = localThrowable6;throw localThrowable6;
    }
    finally
    {
      if (pst != null) {
        if (localThrowable8 != null) {
          try
          {
            pst.close();
          }
          catch (Throwable localThrowable7)
          {
            localThrowable8.addSuppressed(localThrowable7);
          }
        } else {
          pst.close();
        }
      }
    }
    return OptionalInt.empty();
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\datasource\mysqlextensions\MySqlExtension.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */