package fr.xephi.authme.datasource.mysqlextensions;

import fr.xephi.authme.data.auth.PlayerAuth;
import fr.xephi.authme.datasource.Columns;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.settings.properties.HooksSettings;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

class Ipb4Extension
  extends MySqlExtension
{
  private final String ipbPrefix;
  private final int ipbGroup;
  
  Ipb4Extension(Settings settings, Columns col)
  {
    super(settings, col);
    this.ipbPrefix = ((String)settings.getProperty(HooksSettings.IPB_TABLE_PREFIX));
    this.ipbGroup = ((Integer)settings.getProperty(HooksSettings.IPB_ACTIVATED_GROUP_ID)).intValue();
  }
  
  public void saveAuth(PlayerAuth auth, Connection con)
    throws SQLException
  {
    String sql = "UPDATE " + this.ipbPrefix + this.tableName + " SET " + this.tableName + ".member_group_id=? WHERE " + this.col.NAME + "=?;";
    
    PreparedStatement pst2 = con.prepareStatement(sql);Throwable localThrowable9 = null;
    try
    {
      pst2.setInt(1, this.ipbGroup);
      pst2.setString(2, auth.getNickname());
      pst2.executeUpdate();
    }
    catch (Throwable localThrowable1)
    {
      localThrowable9 = localThrowable1;throw localThrowable1;
    }
    finally
    {
      if (pst2 != null) {
        if (localThrowable9 != null) {
          try
          {
            pst2.close();
          }
          catch (Throwable localThrowable2)
          {
            localThrowable9.addSuppressed(localThrowable2);
          }
        } else {
          pst2.close();
        }
      }
    }
    long time = System.currentTimeMillis() / 1000L;
    
    sql = "UPDATE " + this.ipbPrefix + this.tableName + " SET " + this.tableName + ".joined=? WHERE " + this.col.NAME + "=?;";
    
    pst2 = con.prepareStatement(sql);Throwable localThrowable10 = null;
    try
    {
      pst2.setLong(1, time);
      pst2.setString(2, auth.getNickname());
      pst2.executeUpdate();
    }
    catch (Throwable localThrowable4)
    {
      localThrowable10 = localThrowable4;throw localThrowable4;
    }
    finally
    {
      if (pst2 != null) {
        if (localThrowable10 != null) {
          try
          {
            pst2.close();
          }
          catch (Throwable localThrowable5)
          {
            localThrowable10.addSuppressed(localThrowable5);
          }
        } else {
          pst2.close();
        }
      }
    }
    sql = "UPDATE " + this.ipbPrefix + this.tableName + " SET " + this.tableName + ".last_visit=? WHERE " + this.col.NAME + "=?;";
    
    pst2 = con.prepareStatement(sql);localThrowable10 = null;
    try
    {
      pst2.setLong(1, time);
      pst2.setString(2, auth.getNickname());
      pst2.executeUpdate();
    }
    catch (Throwable localThrowable7)
    {
      localThrowable10 = localThrowable7;throw localThrowable7;
    }
    finally
    {
      if (pst2 != null) {
        if (localThrowable10 != null) {
          try
          {
            pst2.close();
          }
          catch (Throwable localThrowable8)
          {
            localThrowable10.addSuppressed(localThrowable8);
          }
        } else {
          pst2.close();
        }
      }
    }
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\datasource\mysqlextensions\Ipb4Extension.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */