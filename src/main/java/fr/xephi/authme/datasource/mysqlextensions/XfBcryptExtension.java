package fr.xephi.authme.datasource.mysqlextensions;

import fr.xephi.authme.data.auth.PlayerAuth;
import fr.xephi.authme.datasource.Columns;
import fr.xephi.authme.security.crypts.HashedPassword;
import fr.xephi.authme.security.crypts.XfBCrypt;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.settings.properties.HooksSettings;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.OptionalInt;

class XfBcryptExtension
  extends MySqlExtension
{
  private final String xfPrefix;
  private final int xfGroup;
  
  XfBcryptExtension(Settings settings, Columns col)
  {
    super(settings, col);
    this.xfPrefix = ((String)settings.getProperty(HooksSettings.XF_TABLE_PREFIX));
    this.xfGroup = ((Integer)settings.getProperty(HooksSettings.XF_ACTIVATED_GROUP_ID)).intValue();
  }
  
  public void saveAuth(PlayerAuth auth, Connection con)
    throws SQLException
  {
    OptionalInt authId = retrieveIdFromTable(auth.getNickname(), con);
    if (authId.isPresent()) {
      updateXenforoTablesOnSave(auth, authId.getAsInt(), con);
    }
  }
  
  private void updateXenforoTablesOnSave(PlayerAuth auth, int id, Connection con)
    throws SQLException
  {
    String sql = "INSERT INTO " + this.xfPrefix + "user_authenticate (user_id, scheme_class, data) VALUES (?,?,?)";
    PreparedStatement pst = con.prepareStatement(sql);Throwable localThrowable15 = null;
    try
    {
      pst.setInt(1, id);
      pst.setString(2, "XenForo_Authentication_Core12");
      String serializedHash = XfBCrypt.serializeHash(auth.getPassword().getHash());
      byte[] bytes = serializedHash.getBytes();
      Blob blob = con.createBlob();
      blob.setBytes(1L, bytes);
      pst.setBlob(3, blob);
      pst.executeUpdate();
    }
    catch (Throwable localThrowable1)
    {
      localThrowable15 = localThrowable1;throw localThrowable1;
    }
    finally
    {
      if (pst != null) {
        if (localThrowable15 != null) {
          try
          {
            pst.close();
          }
          catch (Throwable localThrowable2)
          {
            localThrowable15.addSuppressed(localThrowable2);
          }
        } else {
          pst.close();
        }
      }
    }
    sql = "UPDATE " + this.tableName + " SET " + this.tableName + ".user_group_id=? WHERE " + this.col.NAME + "=?;";
    pst = con.prepareStatement(sql);localThrowable15 = null;
    try
    {
      pst.setInt(1, this.xfGroup);
      pst.setString(2, auth.getNickname());
      pst.executeUpdate();
    }
    catch (Throwable localThrowable4)
    {
      localThrowable15 = localThrowable4;throw localThrowable4;
    }
    finally
    {
      if (pst != null) {
        if (localThrowable15 != null) {
          try
          {
            pst.close();
          }
          catch (Throwable localThrowable5)
          {
            localThrowable15.addSuppressed(localThrowable5);
          }
        } else {
          pst.close();
        }
      }
    }
    sql = "UPDATE " + this.tableName + " SET " + this.tableName + ".permission_combination_id=? WHERE " + this.col.NAME + "=?;";
    pst = con.prepareStatement(sql);localThrowable15 = null;
    try
    {
      pst.setInt(1, this.xfGroup);
      pst.setString(2, auth.getNickname());
      pst.executeUpdate();
    }
    catch (Throwable localThrowable7)
    {
      localThrowable15 = localThrowable7;throw localThrowable7;
    }
    finally
    {
      if (pst != null) {
        if (localThrowable15 != null) {
          try
          {
            pst.close();
          }
          catch (Throwable localThrowable8)
          {
            localThrowable15.addSuppressed(localThrowable8);
          }
        } else {
          pst.close();
        }
      }
    }
    sql = "INSERT INTO " + this.xfPrefix + "user_privacy (user_id, allow_view_profile, allow_post_profile, allow_send_personal_conversation, allow_view_identities, allow_receive_news_feed) VALUES (?,?,?,?,?,?)";
    
    pst = con.prepareStatement(sql);localThrowable15 = null;
    try
    {
      pst.setInt(1, id);
      pst.setString('2', "everyone");
      pst.setString('3', "members");
      pst.setString('4', "members");
      pst.setString('5', "everyone");
      pst.setString('6', "everyone");
      pst.executeUpdate();
    }
    catch (Throwable localThrowable10)
    {
      localThrowable15 = localThrowable10;throw localThrowable10;
    }
    finally
    {
      if (pst != null) {
        if (localThrowable15 != null) {
          try
          {
            pst.close();
          }
          catch (Throwable localThrowable11)
          {
            localThrowable15.addSuppressed(localThrowable11);
          }
        } else {
          pst.close();
        }
      }
    }
    sql = "INSERT INTO " + this.xfPrefix + "user_group_relation (user_id, user_group_id, is_primary) VALUES (?,?,?)";
    pst = con.prepareStatement(sql);localThrowable15 = null;
    try
    {
      pst.setInt(1, id);
      pst.setInt(2, this.xfGroup);
      pst.setString(3, "1");
      pst.executeUpdate();
    }
    catch (Throwable localThrowable13)
    {
      localThrowable15 = localThrowable13;throw localThrowable13;
    }
    finally
    {
      if (pst != null) {
        if (localThrowable15 != null) {
          try
          {
            pst.close();
          }
          catch (Throwable localThrowable14)
          {
            localThrowable15.addSuppressed(localThrowable14);
          }
        } else {
          pst.close();
        }
      }
    }
  }
  
  public void extendAuth(PlayerAuth auth, int id, Connection con)
    throws SQLException
  {
    PreparedStatement pst = con.prepareStatement("SELECT data FROM " + this.xfPrefix + "user_authenticate WHERE " + this.col.ID + "=?;");Throwable localThrowable6 = null;
    try
    {
      pst.setInt(1, id);
      ResultSet rs = pst.executeQuery();Throwable localThrowable7 = null;
      try
      {
        if (rs.next())
        {
          Blob blob = rs.getBlob("data");
          byte[] bytes = blob.getBytes(1L, (int)blob.length());
          auth.setPassword(new HashedPassword(XfBCrypt.getHashFromBlob(bytes)));
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
      if (pst != null) {
        if (localThrowable6 != null) {
          try
          {
            pst.close();
          }
          catch (Throwable localThrowable5)
          {
            localThrowable6.addSuppressed(localThrowable5);
          }
        } else {
          pst.close();
        }
      }
    }
  }
  
  public void changePassword(String user, HashedPassword password, Connection con)
    throws SQLException
  {
    OptionalInt authId = retrieveIdFromTable(user, con);
    if (authId.isPresent())
    {
      int id = authId.getAsInt();
      
      String sql = "UPDATE " + this.xfPrefix + "user_authenticate SET data=? WHERE " + this.col.ID + "=?;";
      PreparedStatement pst = con.prepareStatement(sql);Throwable localThrowable6 = null;
      try
      {
        String serializedHash = XfBCrypt.serializeHash(password.getHash());
        byte[] bytes = serializedHash.getBytes();
        Blob blob = con.createBlob();
        blob.setBytes(1L, bytes);
        pst.setBlob(1, blob);
        pst.setInt(2, id);
        pst.executeUpdate();
      }
      catch (Throwable localThrowable1)
      {
        localThrowable6 = localThrowable1;throw localThrowable1;
      }
      finally
      {
        if (pst != null) {
          if (localThrowable6 != null) {
            try
            {
              pst.close();
            }
            catch (Throwable localThrowable2)
            {
              localThrowable6.addSuppressed(localThrowable2);
            }
          } else {
            pst.close();
          }
        }
      }
      sql = "UPDATE " + this.xfPrefix + "user_authenticate SET scheme_class=? WHERE " + this.col.ID + "=?;";
      pst = con.prepareStatement(sql);localThrowable6 = null;
      try
      {
        pst.setString(1, "XenForo_Authentication_Core12");
        pst.setInt(2, id);
        pst.executeUpdate();
      }
      catch (Throwable localThrowable4)
      {
        localThrowable6 = localThrowable4;throw localThrowable4;
      }
      finally
      {
        if (pst != null) {
          if (localThrowable6 != null) {
            try
            {
              pst.close();
            }
            catch (Throwable localThrowable5)
            {
              localThrowable6.addSuppressed(localThrowable5);
            }
          } else {
            pst.close();
          }
        }
      }
    }
  }
  
  public void removeAuth(String user, Connection con)
    throws SQLException
  {
    OptionalInt authId = retrieveIdFromTable(user, con);
    if (authId.isPresent())
    {
      String sql = "DELETE FROM " + this.xfPrefix + "user_authenticate WHERE " + this.col.ID + "=?;";
      PreparedStatement xfDelete = con.prepareStatement(sql);Throwable localThrowable3 = null;
      try
      {
        xfDelete.setInt(1, authId.getAsInt());
        xfDelete.executeUpdate();
      }
      catch (Throwable localThrowable1)
      {
        localThrowable3 = localThrowable1;throw localThrowable1;
      }
      finally
      {
        if (xfDelete != null) {
          if (localThrowable3 != null) {
            try
            {
              xfDelete.close();
            }
            catch (Throwable localThrowable2)
            {
              localThrowable3.addSuppressed(localThrowable2);
            }
          } else {
            xfDelete.close();
          }
        }
      }
    }
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\datasource\mysqlextensions\XfBcryptExtension.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */