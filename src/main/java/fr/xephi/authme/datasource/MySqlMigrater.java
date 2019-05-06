//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package fr.xephi.authme.datasource;

import fr.xephi.authme.ConsoleLogger;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

final class MySqlMigrater {
  private MySqlMigrater() {
  }

  static void migrateLastIpColumn(Statement st, DatabaseMetaData metaData, String tableName, Columns col) throws SQLException {
    boolean isNotNullWithoutDefault = SqlDataSourceUtils.isNotNullColumn(metaData, tableName, col.LAST_IP) && SqlDataSourceUtils.getColumnDefaultValue(metaData, tableName, col.LAST_IP) == null;
    if (isNotNullWithoutDefault) {
      String sql = String.format("ALTER TABLE %s MODIFY %s VARCHAR(40) CHARACTER SET ascii COLLATE ascii_bin", tableName, col.LAST_IP);
      st.execute(sql);
      ConsoleLogger.info("Changed last login column to allow NULL values. Please verify the registration feature if you are hooking into a forum.");
    }

  }

  static void migrateLastLoginColumn(Statement st, DatabaseMetaData metaData, String tableName, Columns col) throws SQLException {
    ResultSet rs = metaData.getColumns((String)null, (String)null, tableName, col.LAST_LOGIN);
    Throwable var6 = null;

    int columnType;
    label110: {
      try {
        if (rs.next()) {
          columnType = rs.getInt("DATA_TYPE");
          break label110;
        }

        ConsoleLogger.warning("Could not get LAST_LOGIN meta data. This should never happen!");
      } catch (Throwable var16) {
        var6 = var16;
        throw var16;
      } finally {
        if (rs != null) {
          if (var6 != null) {
            try {
              rs.close();
            } catch (Throwable var15) {
              var6.addSuppressed(var15);
            }
          } else {
            rs.close();
          }
        }

      }

      return;
    }

    if (columnType == 93) {
      migrateLastLoginColumnFromTimestamp(st, tableName, col);
    } else if (columnType == 4) {
      migrateLastLoginColumnFromInt(st, tableName, col);
    }

  }

  private static void migrateLastLoginColumnFromTimestamp(Statement st, String tableName, Columns col) throws SQLException {
    ConsoleLogger.info("Migrating lastlogin column from timestamp to bigint");
    String lastLoginOld = col.LAST_LOGIN + "_old";
    String sql = String.format("ALTER TABLE %s CHANGE COLUMN %s %s BIGINT", tableName, col.LAST_LOGIN, lastLoginOld);
    st.execute(sql);
    sql = String.format("ALTER TABLE %s ADD COLUMN %s BIGINT NOT NULL DEFAULT 0 AFTER %s", tableName, col.LAST_LOGIN, col.LAST_IP);
    st.execute(sql);
    sql = String.format("UPDATE %s SET %s = UNIX_TIMESTAMP(%s) * 1000", tableName, col.LAST_LOGIN, lastLoginOld);
    st.execute(sql);
    sql = String.format("ALTER TABLE %s DROP COLUMN %s", tableName, lastLoginOld);
    st.execute(sql);
    ConsoleLogger.info("Finished migration of lastlogin (timestamp to bigint)");
  }

  private static void migrateLastLoginColumnFromInt(Statement st, String tableName, Columns col) throws SQLException {
    ConsoleLogger.info("Migrating lastlogin column from int to bigint");
    String sql = String.format("ALTER TABLE %s MODIFY %s BIGINT;", tableName, col.LAST_LOGIN);
    st.execute(sql);
    int rangeStart = 1262304000;
    int rangeEnd = 1514678400;
    sql = String.format("UPDATE %s SET %s = %s * 1000 WHERE %s > %d AND %s < %d;", tableName, col.LAST_LOGIN, col.LAST_LOGIN, col.LAST_LOGIN, rangeStart, col.LAST_LOGIN, rangeEnd);
    int changedRows = st.executeUpdate(sql);
    ConsoleLogger.warning("You may have entries with invalid timestamps. Please check your data before purging. " + changedRows + " rows were migrated from seconds to milliseconds.");
  }

  static void addRegistrationDateColumn(Statement st, String tableName, Columns col) throws SQLException {
    st.executeUpdate("ALTER TABLE " + tableName + " ADD COLUMN " + col.REGISTRATION_DATE + " BIGINT NOT NULL DEFAULT 0;");
    long currentTimestamp = System.currentTimeMillis();
    int updatedRows = st.executeUpdate(String.format("UPDATE %s SET %s = %d;", tableName, col.REGISTRATION_DATE, currentTimestamp));
    ConsoleLogger.info("Created column '" + col.REGISTRATION_DATE + "' and set the current timestamp, " + currentTimestamp + ", to all " + updatedRows + " rows");
  }
}
