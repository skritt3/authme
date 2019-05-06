package fr.xephi.authme.datasource;

import fr.xephi.authme.ConsoleLogger;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class SqlDataSourceUtils
{
  public static void logSqlException(SQLException e)
  {
    ConsoleLogger.logException("Error during SQL operation:", e);
  }
  
  public static Long getNullableLong(ResultSet rs, String columnName)
    throws SQLException
  {
    long longValue = rs.getLong(columnName);
    return rs.wasNull() ? null : Long.valueOf(longValue);
  }
  
  public static boolean isNotNullColumn(DatabaseMetaData metaData, String tableName, String columnName)
    throws SQLException
  {
    ResultSet rs = metaData.getColumns(null, null, tableName, columnName);Throwable localThrowable4 = null;
    try
    {
      if (!rs.next()) {
        throw new IllegalStateException("Did not find meta data for column '" + columnName + "' while checking for not-null constraint");
      }
      int nullableCode = rs.getInt("NULLABLE");
      if (nullableCode == 0) {
        return true;
      }
      if (nullableCode == 2) {
        ConsoleLogger.warning("Unknown nullable status for column '" + columnName + "'");
      }
    }
    catch (Throwable localThrowable2)
    {
      localThrowable4 = localThrowable2;throw localThrowable2;
    }
    finally
    {
      if (rs != null) {
        if (localThrowable4 != null) {
          try
          {
            rs.close();
          }
          catch (Throwable localThrowable3)
          {
            localThrowable4.addSuppressed(localThrowable3);
          }
        } else {
          rs.close();
        }
      }
    }
    return false;
  }
  
  public static Object getColumnDefaultValue(DatabaseMetaData metaData, String tableName, String columnName)
    throws SQLException
  {
    ResultSet rs = metaData.getColumns(null, null, tableName, columnName);Throwable localThrowable3 = null;
    try
    {
      if (!rs.next()) {
        throw new IllegalStateException("Did not find meta data for column '" + columnName + "' while checking its default value");
      }
      return rs.getObject("COLUMN_DEF");
    }
    catch (Throwable localThrowable4)
    {
      localThrowable3 = localThrowable4;throw localThrowable4;
    }
    finally
    {
      if (rs != null) {
        if (localThrowable3 != null) {
          try
          {
            rs.close();
          }
          catch (Throwable localThrowable2)
          {
            localThrowable3.addSuppressed(localThrowable2);
          }
        } else {
          rs.close();
        }
      }
    }
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\datasource\SqlDataSourceUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */