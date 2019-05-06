package fr.xephi.authme.datasource.mysqlextensions;

import fr.xephi.authme.data.auth.PlayerAuth;
import fr.xephi.authme.datasource.Columns;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.settings.properties.HooksSettings;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.OptionalInt;

class WordpressExtension
  extends MySqlExtension
{
  private final String wordpressPrefix;
  
  WordpressExtension(Settings settings, Columns col)
  {
    super(settings, col);
    this.wordpressPrefix = ((String)settings.getProperty(HooksSettings.WORDPRESS_TABLE_PREFIX));
  }
  
  public void saveAuth(PlayerAuth auth, Connection con)
    throws SQLException
  {
    OptionalInt authId = retrieveIdFromTable(auth.getNickname(), con);
    if (authId.isPresent()) {
      saveSpecifics(auth, authId.getAsInt(), con);
    }
  }
  
  private void saveSpecifics(PlayerAuth auth, int id, Connection con)
    throws SQLException
  {
    String sql = "INSERT INTO " + this.wordpressPrefix + "usermeta (user_id, meta_key, meta_value) VALUES (?,?,?)";
    PreparedStatement pst = con.prepareStatement(sql);Throwable localThrowable3 = null;
    try
    {
      pst.setInt(1, id);
      pst.setString(2, "first_name");
      pst.setString(3, "");
      pst.addBatch();
      
      pst.setInt(1, id);
      pst.setString(2, "last_name");
      pst.setString(3, "");
      pst.addBatch();
      
      pst.setInt(1, id);
      pst.setString(2, "nickname");
      pst.setString(3, auth.getNickname());
      pst.addBatch();
      
      pst.setInt(1, id);
      pst.setString(2, "description");
      pst.setString(3, "");
      pst.addBatch();
      
      pst.setInt(1, id);
      pst.setString(2, "rich_editing");
      pst.setString(3, "true");
      pst.addBatch();
      
      pst.setInt(1, id);
      pst.setString(2, "comment_shortcuts");
      pst.setString(3, "false");
      pst.addBatch();
      
      pst.setInt(1, id);
      pst.setString(2, "admin_color");
      pst.setString(3, "fresh");
      pst.addBatch();
      
      pst.setInt(1, id);
      pst.setString(2, "use_ssl");
      pst.setString(3, "0");
      pst.addBatch();
      
      pst.setInt(1, id);
      pst.setString(2, "show_admin_bar_front");
      pst.setString(3, "true");
      pst.addBatch();
      
      pst.setInt(1, id);
      pst.setString(2, this.wordpressPrefix + "capabilities");
      pst.setString(3, "a:1:{s:10:\"subscriber\";b:1;}");
      pst.addBatch();
      
      pst.setInt(1, id);
      pst.setString(2, this.wordpressPrefix + "user_level");
      pst.setString(3, "0");
      pst.addBatch();
      
      pst.setInt(1, id);
      pst.setString(2, "default_password_nag");
      pst.setString(3, "");
      pst.addBatch();
      
      pst.executeBatch();
      pst.clearBatch();
    }
    catch (Throwable localThrowable1)
    {
      localThrowable3 = localThrowable1;throw localThrowable1;
    }
    finally
    {
      if (pst != null) {
        if (localThrowable3 != null) {
          try
          {
            pst.close();
          }
          catch (Throwable localThrowable2)
          {
            localThrowable3.addSuppressed(localThrowable2);
          }
        } else {
          pst.close();
        }
      }
    }
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\datasource\mysqlextensions\WordpressExtension.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */