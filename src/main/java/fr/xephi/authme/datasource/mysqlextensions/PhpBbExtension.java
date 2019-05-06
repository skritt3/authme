//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package fr.xephi.authme.datasource.mysqlextensions;

import fr.xephi.authme.data.auth.PlayerAuth;
import fr.xephi.authme.datasource.Columns;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.settings.properties.HooksSettings;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.OptionalInt;

class PhpBbExtension extends MySqlExtension {
  private final String phpBbPrefix;
  private final int phpBbGroup;

  PhpBbExtension(Settings settings, Columns col) {
    super(settings, col);
    this.phpBbPrefix = (String)settings.getProperty(HooksSettings.PHPBB_TABLE_PREFIX);
    this.phpBbGroup = (Integer)settings.getProperty(HooksSettings.PHPBB_ACTIVATED_GROUP_ID);
  }

  public void saveAuth(PlayerAuth auth, Connection con) throws SQLException {
    OptionalInt authId = this.retrieveIdFromTable(auth.getNickname(), con);
    if (authId.isPresent()) {
      this.updateSpecificsOnSave(authId.getAsInt(), auth.getNickname(), con);
    }

  }

  private void updateSpecificsOnSave(int id, String name, Connection con) throws SQLException {
    String sql = "INSERT INTO " + this.phpBbPrefix + "user_group (group_id, user_id, group_leader, user_pending) VALUES (?,?,?,?);";
    PreparedStatement pst = con.prepareStatement(sql);
    Throwable var6 = null;

    try {
      pst.setInt(1, this.phpBbGroup);
      pst.setInt(2, id);
      pst.setInt(3, 0);
      pst.setInt(4, 0);
      pst.executeUpdate();
    } catch (Throwable var160) {
      var6 = var160;
      throw var160;
    } finally {
      if (pst != null) {
        if (var6 != null) {
          try {
            pst.close();
          } catch (Throwable var153) {
            var6.addSuppressed(var153);
          }
        } else {
          pst.close();
        }
      }

    }

    sql = "UPDATE " + this.tableName + " SET " + this.tableName + ".username_clean=? WHERE " + this.col.NAME + "=?;";
    pst = con.prepareStatement(sql);
    var6 = null;

    try {
      pst.setString(1, name);
      pst.setString(2, name);
      pst.executeUpdate();
    } catch (Throwable var159) {
      var6 = var159;
      throw var159;
    } finally {
      if (pst != null) {
        if (var6 != null) {
          try {
            pst.close();
          } catch (Throwable var149) {
            var6.addSuppressed(var149);
          }
        } else {
          pst.close();
        }
      }

    }

    sql = "UPDATE " + this.tableName + " SET " + this.tableName + ".group_id=? WHERE " + this.col.NAME + "=?;";
    pst = con.prepareStatement(sql);
    var6 = null;

    try {
      pst.setInt(1, this.phpBbGroup);
      pst.setString(2, name);
      pst.executeUpdate();
    } catch (Throwable var158) {
      var6 = var158;
      throw var158;
    } finally {
      if (pst != null) {
        if (var6 != null) {
          try {
            pst.close();
          } catch (Throwable var150) {
            var6.addSuppressed(var150);
          }
        } else {
          pst.close();
        }
      }

    }

    long time = System.currentTimeMillis() / 1000L;
    sql = "UPDATE " + this.tableName + " SET " + this.tableName + ".user_regdate=? WHERE " + this.col.NAME + "=?;";
    pst = con.prepareStatement(sql);
    Throwable var8 = null;

    try {
      pst.setLong(1, time);
      pst.setString(2, name);
      pst.executeUpdate();
    } catch (Throwable var157) {
      var8 = var157;
      throw var157;
    } finally {
      if (pst != null) {
        if (var8 != null) {
          try {
            pst.close();
          } catch (Throwable var152) {
            var8.addSuppressed(var152);
          }
        } else {
          pst.close();
        }
      }

    }

    sql = "UPDATE " + this.tableName + " SET " + this.tableName + ".user_lastvisit=? WHERE " + this.col.NAME + "=?;";
    pst = con.prepareStatement(sql);
    var8 = null;

    try {
      pst.setLong(1, time);
      pst.setString(2, name);
      pst.executeUpdate();
    } catch (Throwable var156) {
      var8 = var156;
      throw var156;
    } finally {
      if (pst != null) {
        if (var8 != null) {
          try {
            pst.close();
          } catch (Throwable var151) {
            var8.addSuppressed(var151);
          }
        } else {
          pst.close();
        }
      }

    }

    sql = "UPDATE " + this.phpBbPrefix + "config SET config_value = config_value + 1 WHERE config_name = 'num_users';";
    pst = con.prepareStatement(sql);
    var8 = null;

    try {
      pst.executeUpdate();
    } catch (Throwable var155) {
      var8 = var155;
      throw var155;
    } finally {
      if (pst != null) {
        if (var8 != null) {
          try {
            pst.close();
          } catch (Throwable var154) {
            var8.addSuppressed(var154);
          }
        } else {
          pst.close();
        }
      }

    }

  }
}
