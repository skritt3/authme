//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package fr.xephi.authme.datasource;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.data.auth.PlayerAuth;
import fr.xephi.authme.libs.google.common.annotations.VisibleForTesting;
import fr.xephi.authme.security.crypts.HashedPassword;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.settings.properties.DatabaseSettings;
import fr.xephi.authme.util.StringUtils;
import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class SQLite implements DataSource {
  private final Settings settings;
  private final File dataFolder;
  private final String database;
  private final String tableName;
  private final Columns col;
  private Connection con;

  public SQLite(Settings settings, File dataFolder) throws SQLException {
    this.settings = settings;
    this.dataFolder = dataFolder;
    this.database = (String)settings.getProperty(DatabaseSettings.MYSQL_DATABASE);
    this.tableName = (String)settings.getProperty(DatabaseSettings.MYSQL_TABLE);
    this.col = new Columns(settings);

    try {
      this.connect();
      this.setup();
      this.migrateIfNeeded();
    } catch (Exception var4) {
      ConsoleLogger.logException("Error during SQLite initialization:", var4);
      throw var4;
    }
  }

  @VisibleForTesting
  SQLite(Settings settings, File dataFolder, Connection connection) {
    this.settings = settings;
    this.dataFolder = dataFolder;
    this.database = (String)settings.getProperty(DatabaseSettings.MYSQL_DATABASE);
    this.tableName = (String)settings.getProperty(DatabaseSettings.MYSQL_TABLE);
    this.col = new Columns(settings);
    this.con = connection;
  }

  protected void connect() throws SQLException {
    try {
      Class.forName("org.sqlite.JDBC");
    } catch (ClassNotFoundException var2) {
      throw new IllegalStateException("Failed to load SQLite JDBC class", var2);
    }

    ConsoleLogger.debug("SQLite driver loaded");
    this.con = DriverManager.getConnection("jdbc:sqlite:plugins/AuthMe/" + this.database + ".db");
  }

  @VisibleForTesting
  protected void setup() throws SQLException {
    Statement st = this.con.createStatement();
    Throwable var2 = null;

    try {
      st.executeUpdate("CREATE TABLE IF NOT EXISTS " + this.tableName + " (" + this.col.ID + " INTEGER AUTO_INCREMENT, " + this.col.NAME + " VARCHAR(255) NOT NULL UNIQUE, CONSTRAINT table_const_prim PRIMARY KEY (" + this.col.ID + "));");
      DatabaseMetaData md = this.con.getMetaData();
      if (this.isColumnMissing(md, this.col.REAL_NAME)) {
        st.executeUpdate("ALTER TABLE " + this.tableName + " ADD COLUMN " + this.col.REAL_NAME + " VARCHAR(255) NOT NULL DEFAULT 'Player';");
      }

      if (this.isColumnMissing(md, this.col.PASSWORD)) {
        st.executeUpdate("ALTER TABLE " + this.tableName + " ADD COLUMN " + this.col.PASSWORD + " VARCHAR(255) NOT NULL DEFAULT '';");
      }

      if (!this.col.SALT.isEmpty() && this.isColumnMissing(md, this.col.SALT)) {
        st.executeUpdate("ALTER TABLE " + this.tableName + " ADD COLUMN " + this.col.SALT + " VARCHAR(255);");
      }

      if (this.isColumnMissing(md, this.col.LAST_IP)) {
        st.executeUpdate("ALTER TABLE " + this.tableName + " ADD COLUMN " + this.col.LAST_IP + " VARCHAR(40);");
      }

      if (this.isColumnMissing(md, this.col.LAST_LOGIN)) {
        st.executeUpdate("ALTER TABLE " + this.tableName + " ADD COLUMN " + this.col.LAST_LOGIN + " TIMESTAMP;");
      }

      if (this.isColumnMissing(md, this.col.REGISTRATION_IP)) {
        st.executeUpdate("ALTER TABLE " + this.tableName + " ADD COLUMN " + this.col.REGISTRATION_IP + " VARCHAR(40);");
      }

      if (this.isColumnMissing(md, this.col.REGISTRATION_DATE)) {
        this.addRegistrationDateColumn(st);
      }

      if (this.isColumnMissing(md, this.col.LASTLOC_X)) {
        st.executeUpdate("ALTER TABLE " + this.tableName + " ADD COLUMN " + this.col.LASTLOC_X + " DOUBLE NOT NULL DEFAULT '0.0';");
        st.executeUpdate("ALTER TABLE " + this.tableName + " ADD COLUMN " + this.col.LASTLOC_Y + " DOUBLE NOT NULL DEFAULT '0.0';");
        st.executeUpdate("ALTER TABLE " + this.tableName + " ADD COLUMN " + this.col.LASTLOC_Z + " DOUBLE NOT NULL DEFAULT '0.0';");
      }

      if (this.isColumnMissing(md, this.col.LASTLOC_WORLD)) {
        st.executeUpdate("ALTER TABLE " + this.tableName + " ADD COLUMN " + this.col.LASTLOC_WORLD + " VARCHAR(255) NOT NULL DEFAULT 'world';");
      }

      if (this.isColumnMissing(md, this.col.LASTLOC_YAW)) {
        st.executeUpdate("ALTER TABLE " + this.tableName + " ADD COLUMN " + this.col.LASTLOC_YAW + " FLOAT;");
      }

      if (this.isColumnMissing(md, this.col.LASTLOC_PITCH)) {
        st.executeUpdate("ALTER TABLE " + this.tableName + " ADD COLUMN " + this.col.LASTLOC_PITCH + " FLOAT;");
      }

      if (this.isColumnMissing(md, this.col.EMAIL)) {
        st.executeUpdate("ALTER TABLE " + this.tableName + " ADD COLUMN " + this.col.EMAIL + " VARCHAR(255);");
      }

      if (this.isColumnMissing(md, this.col.IS_LOGGED)) {
        st.executeUpdate("ALTER TABLE " + this.tableName + " ADD COLUMN " + this.col.IS_LOGGED + " INT NOT NULL DEFAULT '0';");
      }

      if (this.isColumnMissing(md, this.col.HAS_SESSION)) {
        st.executeUpdate("ALTER TABLE " + this.tableName + " ADD COLUMN " + this.col.HAS_SESSION + " INT NOT NULL DEFAULT '0';");
      }
    } catch (Throwable var11) {
      var2 = var11;
      throw var11;
    } finally {
      if (st != null) {
        if (var2 != null) {
          try {
            st.close();
          } catch (Throwable var10) {
            var2.addSuppressed(var10);
          }
        } else {
          st.close();
        }
      }

    }

    ConsoleLogger.info("SQLite Setup finished");
  }

  protected void migrateIfNeeded() throws SQLException {
    DatabaseMetaData metaData = this.con.getMetaData();
    if (SqLiteMigrater.isMigrationRequired(metaData, this.tableName, this.col)) {
      (new SqLiteMigrater(this.settings, this.dataFolder)).performMigration(this);
      this.connect();
    }

  }

  private boolean isColumnMissing(DatabaseMetaData metaData, String columnName) throws SQLException {
    ResultSet rs = metaData.getColumns((String)null, (String)null, this.tableName, columnName);
    Throwable var4 = null;

    boolean var5;
    try {
      var5 = !rs.next();
    } catch (Throwable var14) {
      var4 = var14;
      throw var14;
    } finally {
      if (rs != null) {
        if (var4 != null) {
          try {
            rs.close();
          } catch (Throwable var13) {
            var4.addSuppressed(var13);
          }
        } else {
          rs.close();
        }
      }

    }

    return var5;
  }

  public void reload() {
    close(this.con);

    try {
      this.connect();
      this.setup();
      this.migrateIfNeeded();
    } catch (SQLException var2) {
      ConsoleLogger.logException("Error while reloading SQLite:", var2);
    }

  }

  @Override
  public void invalidateCache(String playerName) {

  }

  @Override
  public void refreshCache(String playerName) {

  }

  public boolean isAuthAvailable(String user) {
    String sql = "SELECT 1 FROM " + this.tableName + " WHERE LOWER(" + this.col.NAME + ")=LOWER(?);";

    try {
      PreparedStatement pst = this.con.prepareStatement(sql);
      Throwable var4 = null;

      Boolean var7;
      try {
        pst.setString(1, user);
        ResultSet rs = pst.executeQuery();
        Throwable var6 = null;

        try {
          var7 = rs.next();
        } catch (Throwable var32) {
          throw var32;
        } finally {
          if (rs != null) {
            if (var6 != null) {
              try {
                rs.close();
              } catch (Throwable var31) {
                var6.addSuppressed(var31);
              }
            } else {
              rs.close();
            }
          }

        }
      } catch (Throwable var34) {
        var4 = var34;
        throw var34;
      } finally {
        if (pst != null) {
          if (var4 != null) {
            try {
              pst.close();
            } catch (Throwable var30) {
              var4.addSuppressed(var30);
            }
          } else {
            pst.close();
          }
        }

      }

      return (boolean)var7;
    } catch (SQLException var36) {
      ConsoleLogger.warning(var36.getMessage());
      return false;
    }
  }

  public HashedPassword getPassword(String user) {
    boolean useSalt = !this.col.SALT.isEmpty();
    String sql = "SELECT " + this.col.PASSWORD + (useSalt ? ", " + this.col.SALT : "") + " FROM " + this.tableName + " WHERE " + this.col.NAME + "=?";

    try {
      PreparedStatement pst = this.con.prepareStatement(sql);
      Throwable var5 = null;

      Object var8;
      try {
        pst.setString(1, user);
        ResultSet rs = pst.executeQuery();
        Throwable var7 = null;

        try {
          if (!rs.next()) {
            return null;
          }

          var8 = new HashedPassword(rs.getString(this.col.PASSWORD), useSalt ? rs.getString(this.col.SALT) : null);
        } catch (Throwable var36) {
          var8 = var36;
          var7 = var36;
          throw var36;
        } finally {
          if (rs != null) {
            if (var7 != null) {
              try {
                rs.close();
              } catch (Throwable var35) {
                var7.addSuppressed(var35);
              }
            } else {
              rs.close();
            }
          }

        }
      } catch (Throwable var38) {
        var5 = var38;
        throw var38;
      } finally {
        if (pst != null) {
          if (var5 != null) {
            try {
              pst.close();
            } catch (Throwable var34) {
              var5.addSuppressed(var34);
            }
          } else {
            pst.close();
          }
        }

      }

      return (HashedPassword)var8;
    } catch (SQLException var40) {
      SqlDataSourceUtils.logSqlException(var40);
      return null;
    }
  }

  public PlayerAuth getAuth(String user) {
    String sql = "SELECT * FROM " + this.tableName + " WHERE LOWER(" + this.col.NAME + ")=LOWER(?);";

    try {
      PreparedStatement pst = this.con.prepareStatement(sql);
      Throwable var4 = null;

      Object var7;
      try {
        pst.setString(1, user);
        ResultSet rs = pst.executeQuery();
        Throwable var6 = null;

        try {
          if (!rs.next()) {
            return null;
          }

          var7 = this.buildAuthFromResultSet(rs);
        } catch (Throwable var35) {
          var7 = var35;
          var6 = var35;
          throw var35;
        } finally {
          if (rs != null) {
            if (var6 != null) {
              try {
                rs.close();
              } catch (Throwable var34) {
                var6.addSuppressed(var34);
              }
            } else {
              rs.close();
            }
          }

        }
      } catch (Throwable var37) {
        var4 = var37;
        throw var37;
      } finally {
        if (pst != null) {
          if (var4 != null) {
            try {
              pst.close();
            } catch (Throwable var33) {
              var4.addSuppressed(var33);
            }
          } else {
            pst.close();
          }
        }

      }

      return (PlayerAuth)var7;
    } catch (SQLException var39) {
      SqlDataSourceUtils.logSqlException(var39);
      return null;
    }
  }

  public boolean saveAuth(PlayerAuth auth) {
    PreparedStatement pst = null;

    try {
      HashedPassword password = auth.getPassword();
      if (this.col.SALT.isEmpty()) {
        if (!StringUtils.isEmpty(auth.getPassword().getSalt())) {
          ConsoleLogger.warning("Warning! Detected hashed password with separate salt but the salt column is not set in the config!");
        }

        pst = this.con.prepareStatement("INSERT INTO " + this.tableName + "(" + this.col.NAME + "," + this.col.PASSWORD + "," + this.col.REAL_NAME + "," + this.col.EMAIL + "," + this.col.REGISTRATION_DATE + "," + this.col.REGISTRATION_IP + ") VALUES (?,?,?,?,?,?);");
        pst.setString(1, auth.getNickname());
        pst.setString(2, password.getHash());
        pst.setString(3, auth.getRealName());
        pst.setString(4, auth.getEmail());
        pst.setLong(5, auth.getRegistrationDate());
        pst.setString(6, auth.getRegistrationIp());
        pst.executeUpdate();
      } else {
        pst = this.con.prepareStatement("INSERT INTO " + this.tableName + "(" + this.col.NAME + "," + this.col.PASSWORD + "," + this.col.REAL_NAME + "," + this.col.EMAIL + "," + this.col.REGISTRATION_DATE + "," + this.col.REGISTRATION_IP + "," + this.col.SALT + ") VALUES (?,?,?,?,?,?,?);");
        pst.setString(1, auth.getNickname());
        pst.setString(2, password.getHash());
        pst.setString(3, auth.getRealName());
        pst.setString(4, auth.getEmail());
        pst.setLong(5, auth.getRegistrationDate());
        pst.setString(6, auth.getRegistrationIp());
        pst.setString(7, password.getSalt());
        pst.executeUpdate();
      }
    } catch (SQLException var7) {
      SqlDataSourceUtils.logSqlException(var7);
    } finally {
      close((Statement)pst);
    }

    return true;
  }

  public boolean updatePassword(PlayerAuth auth) {
    return this.updatePassword(auth.getNickname(), auth.getPassword());
  }

  public boolean updatePassword(String user, HashedPassword password) {
    user = user.toLowerCase();
    boolean useSalt = !this.col.SALT.isEmpty();
    String sql = "UPDATE " + this.tableName + " SET " + this.col.PASSWORD + " = ?" + (useSalt ? ", " + this.col.SALT + " = ?" : "") + " WHERE " + this.col.NAME + " = ?";

    try {
      PreparedStatement pst = this.con.prepareStatement(sql);
      Throwable var6 = null;

      boolean var7;
      try {
        pst.setString(1, password.getHash());
        if (useSalt) {
          pst.setString(2, password.getSalt());
          pst.setString(3, user);
        } else {
          pst.setString(2, user);
        }

        pst.executeUpdate();
        var7 = true;
      } catch (Throwable var17) {
        var6 = var17;
        throw var17;
      } finally {
        if (pst != null) {
          if (var6 != null) {
            try {
              pst.close();
            } catch (Throwable var16) {
              var6.addSuppressed(var16);
            }
          } else {
            pst.close();
          }
        }

      }

      return var7;
    } catch (SQLException var19) {
      SqlDataSourceUtils.logSqlException(var19);
      return false;
    }
  }

  public boolean updateSession(PlayerAuth auth) {
    String sql = "UPDATE " + this.tableName + " SET " + this.col.LAST_IP + "=?, " + this.col.LAST_LOGIN + "=?, " + this.col.REAL_NAME + "=? WHERE " + this.col.NAME + "=?;";

    try {
      PreparedStatement pst = this.con.prepareStatement(sql);
      Throwable var4 = null;

      boolean var5;
      try {
        pst.setString(1, auth.getLastIp());
        pst.setObject(2, auth.getLastLogin());
        pst.setString(3, auth.getRealName());
        pst.setString(4, auth.getNickname());
        pst.executeUpdate();
        var5 = true;
      } catch (Throwable var15) {
        var4 = var15;
        throw var15;
      } finally {
        if (pst != null) {
          if (var4 != null) {
            try {
              pst.close();
            } catch (Throwable var14) {
              var4.addSuppressed(var14);
            }
          } else {
            pst.close();
          }
        }

      }

      return var5;
    } catch (SQLException var17) {
      SqlDataSourceUtils.logSqlException(var17);
      return false;
    }
  }

  public Set<String> getRecordsToPurge(long until) {
    Set<String> list = new HashSet();
    String select = "SELECT " + this.col.NAME + " FROM " + this.tableName + " WHERE MAX( COALESCE(" + this.col.LAST_LOGIN + ", 0), COALESCE(" + this.col.REGISTRATION_DATE + ", 0)) < ?;";

    try {
      PreparedStatement selectPst = this.con.prepareStatement(select);
      Throwable var6 = null;

      try {
        selectPst.setLong(1, until);
        ResultSet rs = selectPst.executeQuery();
        Throwable var8 = null;

        try {
          while(rs.next()) {
            list.add(rs.getString(this.col.NAME));
          }
        } catch (Throwable var33) {
          var8 = var33;
          throw var33;
        } finally {
          if (rs != null) {
            if (var8 != null) {
              try {
                rs.close();
              } catch (Throwable var32) {
                var8.addSuppressed(var32);
              }
            } else {
              rs.close();
            }
          }

        }
      } catch (Throwable var35) {
        var6 = var35;
        throw var35;
      } finally {
        if (selectPst != null) {
          if (var6 != null) {
            try {
              selectPst.close();
            } catch (Throwable var31) {
              var6.addSuppressed(var31);
            }
          } else {
            selectPst.close();
          }
        }

      }
    } catch (SQLException var37) {
      SqlDataSourceUtils.logSqlException(var37);
    }

    return list;
  }

  public void purgeRecords(Collection<String> toPurge) {
    String delete = "DELETE FROM " + this.tableName + " WHERE " + this.col.NAME + "=?;";

    try {
      PreparedStatement deletePst = this.con.prepareStatement(delete);
      Throwable var4 = null;

      try {
        Iterator var5 = toPurge.iterator();

        while(var5.hasNext()) {
          String name = (String)var5.next();
          deletePst.setString(1, name.toLowerCase());
          deletePst.executeUpdate();
        }
      } catch (Throwable var15) {
        var4 = var15;
        throw var15;
      } finally {
        if (deletePst != null) {
          if (var4 != null) {
            try {
              deletePst.close();
            } catch (Throwable var14) {
              var4.addSuppressed(var14);
            }
          } else {
            deletePst.close();
          }
        }

      }
    } catch (SQLException var17) {
      SqlDataSourceUtils.logSqlException(var17);
    }

  }

  public boolean removeAuth(String user) {
    String sql = "DELETE FROM " + this.tableName + " WHERE " + this.col.NAME + "=?;";

    try {
      PreparedStatement pst = this.con.prepareStatement(sql);
      Throwable var4 = null;

      boolean var5;
      try {
        pst.setString(1, user.toLowerCase());
        pst.executeUpdate();
        var5 = true;
      } catch (Throwable var15) {
        var4 = var15;
        throw var15;
      } finally {
        if (pst != null) {
          if (var4 != null) {
            try {
              pst.close();
            } catch (Throwable var14) {
              var4.addSuppressed(var14);
            }
          } else {
            pst.close();
          }
        }

      }

      return var5;
    } catch (SQLException var17) {
      SqlDataSourceUtils.logSqlException(var17);
      return false;
    }
  }

  public boolean updateQuitLoc(PlayerAuth auth) {
    String sql = "UPDATE " + this.tableName + " SET " + this.col.LASTLOC_X + "=?, " + this.col.LASTLOC_Y + "=?, " + this.col.LASTLOC_Z + "=?, " + this.col.LASTLOC_WORLD + "=?, " + this.col.LASTLOC_YAW + "=?, " + this.col.LASTLOC_PITCH + "=? WHERE " + this.col.NAME + "=?;";

    try {
      PreparedStatement pst = this.con.prepareStatement(sql);
      Throwable var4 = null;

      boolean var5;
      try {
        pst.setDouble(1, auth.getQuitLocX());
        pst.setDouble(2, auth.getQuitLocY());
        pst.setDouble(3, auth.getQuitLocZ());
        pst.setString(4, auth.getWorld());
        pst.setFloat(5, auth.getYaw());
        pst.setFloat(6, auth.getPitch());
        pst.setString(7, auth.getNickname());
        pst.executeUpdate();
        var5 = true;
      } catch (Throwable var15) {
        var4 = var15;
        throw var15;
      } finally {
        if (pst != null) {
          if (var4 != null) {
            try {
              pst.close();
            } catch (Throwable var14) {
              var4.addSuppressed(var14);
            }
          } else {
            pst.close();
          }
        }

      }

      return var5;
    } catch (SQLException var17) {
      SqlDataSourceUtils.logSqlException(var17);
      return false;
    }
  }

  public boolean updateEmail(PlayerAuth auth) {
    String sql = "UPDATE " + this.tableName + " SET " + this.col.EMAIL + "=? WHERE " + this.col.NAME + "=?;";

    try {
      PreparedStatement pst = this.con.prepareStatement(sql);
      Throwable var4 = null;

      boolean var5;
      try {
        pst.setString(1, auth.getEmail());
        pst.setString(2, auth.getNickname());
        pst.executeUpdate();
        var5 = true;
      } catch (Throwable var15) {
        var4 = var15;
        throw var15;
      } finally {
        if (pst != null) {
          if (var4 != null) {
            try {
              pst.close();
            } catch (Throwable var14) {
              var4.addSuppressed(var14);
            }
          } else {
            pst.close();
          }
        }

      }

      return var5;
    } catch (SQLException var17) {
      SqlDataSourceUtils.logSqlException(var17);
      return false;
    }
  }

  public void closeConnection() {
    try {
      if (this.con != null && !this.con.isClosed()) {
        this.con.close();
      }
    } catch (SQLException var2) {
      SqlDataSourceUtils.logSqlException(var2);
    }

  }

  public List<String> getAllAuthsByIp(String ip) {
    List<String> countIp = new ArrayList();
    String sql = "SELECT " + this.col.NAME + " FROM " + this.tableName + " WHERE " + this.col.LAST_IP + "=?;";

    try {
      PreparedStatement pst = this.con.prepareStatement(sql);
      Throwable var5 = null;

      Object var8;
      try {
        pst.setString(1, ip);
        ResultSet rs = pst.executeQuery();
        Throwable var7 = null;

        try {
          while(rs.next()) {
            countIp.add(rs.getString(this.col.NAME));
          }

          var8 = countIp;
        } catch (Throwable var33) {
          var8 = var33;
          var7 = var33;
          throw var33;
        } finally {
          if (rs != null) {
            if (var7 != null) {
              try {
                rs.close();
              } catch (Throwable var32) {
                var7.addSuppressed(var32);
              }
            } else {
              rs.close();
            }
          }

        }
      } catch (Throwable var35) {
        var5 = var35;
        throw var35;
      } finally {
        if (pst != null) {
          if (var5 != null) {
            try {
              pst.close();
            } catch (Throwable var31) {
              var5.addSuppressed(var31);
            }
          } else {
            pst.close();
          }
        }

      }

      return (List)var8;
    } catch (SQLException var37) {
      SqlDataSourceUtils.logSqlException(var37);
      return new ArrayList();
    }
  }

  public int countAuthsByEmail(String email) {
    String sql = "SELECT COUNT(1) FROM " + this.tableName + " WHERE " + this.col.EMAIL + " = ? COLLATE NOCASE;";

    try {
      PreparedStatement pst = this.con.prepareStatement(sql);
      Throwable var4 = null;

      int var7;
      try {
        pst.setString(1, email);
        ResultSet rs = pst.executeQuery();
        Throwable var6 = null;

        try {
          if (!rs.next()) {
            return 0;
          }

          var7 = rs.getInt(1);
        } catch (Throwable var35) {
          throw var35;
        } finally {
          if (rs != null) {
            if (var6 != null) {
              try {
                rs.close();
              } catch (Throwable var34) {
                var6.addSuppressed(var34);
              }
            } else {
              rs.close();
            }
          }

        }
      } catch (Throwable var37) {
        var4 = var37;
        throw var37;
      } finally {
        if (pst != null) {
          if (var4 != null) {
            try {
              pst.close();
            } catch (Throwable var33) {
              var4.addSuppressed(var33);
            }
          } else {
            pst.close();
          }
        }

      }

      return (int)var7;
    } catch (SQLException var39) {
      SqlDataSourceUtils.logSqlException(var39);
      return 0;
    }
  }

  public DataSourceType getType() {
    return DataSourceType.SQLITE;
  }

  public boolean isLogged(String user) {
    String sql = "SELECT " + this.col.IS_LOGGED + " FROM " + this.tableName + " WHERE LOWER(" + this.col.NAME + ")=?;";

    try {
      PreparedStatement pst = this.con.prepareStatement(sql);
      Throwable var4 = null;

      Boolean var7;
      try {
        pst.setString(1, user);
        ResultSet rs = pst.executeQuery();
        Throwable var6 = null;

        try {
          if (!rs.next()) {
            return false;
          }

          var7 = rs.getInt(this.col.IS_LOGGED) == 1;
        } catch (Throwable var35) {
          throw var35;
        } finally {
          if (rs != null) {
            if (var6 != null) {
              try {
                rs.close();
              } catch (Throwable var34) {
                var6.addSuppressed(var34);
              }
            } else {
              rs.close();
            }
          }

        }
      } catch (Throwable var37) {
        var4 = var37;
        throw var37;
      } finally {
        if (pst != null) {
          if (var4 != null) {
            try {
              pst.close();
            } catch (Throwable var33) {
              var4.addSuppressed(var33);
            }
          } else {
            pst.close();
          }
        }

      }

      return (boolean)var7;
    } catch (SQLException var39) {
      SqlDataSourceUtils.logSqlException(var39);
      return false;
    }
  }

  public void setLogged(String user) {
    String sql = "UPDATE " + this.tableName + " SET " + this.col.IS_LOGGED + "=? WHERE LOWER(" + this.col.NAME + ")=?;";

    try {
      PreparedStatement pst = this.con.prepareStatement(sql);
      Throwable var4 = null;

      try {
        pst.setInt(1, 1);
        pst.setString(2, user);
        pst.executeUpdate();
      } catch (Throwable var14) {
        var4 = var14;
        throw var14;
      } finally {
        if (pst != null) {
          if (var4 != null) {
            try {
              pst.close();
            } catch (Throwable var13) {
              var4.addSuppressed(var13);
            }
          } else {
            pst.close();
          }
        }

      }
    } catch (SQLException var16) {
      SqlDataSourceUtils.logSqlException(var16);
    }

  }

  public void setUnlogged(String user) {
    String sql = "UPDATE " + this.tableName + " SET " + this.col.IS_LOGGED + "=? WHERE LOWER(" + this.col.NAME + ")=?;";

    try {
      PreparedStatement pst = this.con.prepareStatement(sql);
      Throwable var4 = null;

      try {
        pst.setInt(1, 0);
        pst.setString(2, user);
        pst.executeUpdate();
      } catch (Throwable var14) {
        var4 = var14;
        throw var14;
      } finally {
        if (pst != null) {
          if (var4 != null) {
            try {
              pst.close();
            } catch (Throwable var13) {
              var4.addSuppressed(var13);
            }
          } else {
            pst.close();
          }
        }

      }
    } catch (SQLException var16) {
      SqlDataSourceUtils.logSqlException(var16);
    }

  }

  public boolean hasSession(String user) {
    String sql = "SELECT " + this.col.HAS_SESSION + " FROM " + this.tableName + " WHERE LOWER(" + this.col.NAME + ")=?;";

    try {
      PreparedStatement pst = this.con.prepareStatement(sql);
      Throwable var4 = null;

      Boolean var7;
      try {
        pst.setString(1, user.toLowerCase());
        ResultSet rs = pst.executeQuery();
        Throwable var6 = null;

        try {
          if (!rs.next()) {
            return false;
          }

          var7 = rs.getInt(this.col.HAS_SESSION) == 1;
        } catch (Throwable var35) {
          throw var35;
        } finally {
          if (rs != null) {
            if (var6 != null) {
              try {
                rs.close();
              } catch (Throwable var34) {
                var6.addSuppressed(var34);
              }
            } else {
              rs.close();
            }
          }

        }
      } catch (Throwable var37) {
        var4 = var37;
        throw var37;
      } finally {
        if (pst != null) {
          if (var4 != null) {
            try {
              pst.close();
            } catch (Throwable var33) {
              var4.addSuppressed(var33);
            }
          } else {
            pst.close();
          }
        }

      }

      return (boolean)var7;
    } catch (SQLException var39) {
      SqlDataSourceUtils.logSqlException(var39);
      return false;
    }
  }

  public void grantSession(String user) {
    String sql = "UPDATE " + this.tableName + " SET " + this.col.HAS_SESSION + "=? WHERE LOWER(" + this.col.NAME + ")=?;";

    try {
      PreparedStatement pst = this.con.prepareStatement(sql);
      Throwable var4 = null;

      try {
        pst.setInt(1, 1);
        pst.setString(2, user.toLowerCase());
        pst.executeUpdate();
      } catch (Throwable var14) {
        var4 = var14;
        throw var14;
      } finally {
        if (pst != null) {
          if (var4 != null) {
            try {
              pst.close();
            } catch (Throwable var13) {
              var4.addSuppressed(var13);
            }
          } else {
            pst.close();
          }
        }

      }
    } catch (SQLException var16) {
      SqlDataSourceUtils.logSqlException(var16);
    }

  }

  public void revokeSession(String user) {
    String sql = "UPDATE " + this.tableName + " SET " + this.col.HAS_SESSION + "=? WHERE LOWER(" + this.col.NAME + ")=?;";

    try {
      PreparedStatement pst = this.con.prepareStatement(sql);
      Throwable var4 = null;

      try {
        pst.setInt(1, 0);
        pst.setString(2, user.toLowerCase());
        pst.executeUpdate();
      } catch (Throwable var14) {
        var4 = var14;
        throw var14;
      } finally {
        if (pst != null) {
          if (var4 != null) {
            try {
              pst.close();
            } catch (Throwable var13) {
              var4.addSuppressed(var13);
            }
          } else {
            pst.close();
          }
        }

      }
    } catch (SQLException var16) {
      SqlDataSourceUtils.logSqlException(var16);
    }

  }

  public void purgeLogged() {
    String sql = "UPDATE " + this.tableName + " SET " + this.col.IS_LOGGED + "=? WHERE " + this.col.IS_LOGGED + "=?;";

    try {
      PreparedStatement pst = this.con.prepareStatement(sql);
      Throwable var3 = null;

      try {
        pst.setInt(1, 0);
        pst.setInt(2, 1);
        pst.executeUpdate();
      } catch (Throwable var13) {
        var3 = var13;
        throw var13;
      } finally {
        if (pst != null) {
          if (var3 != null) {
            try {
              pst.close();
            } catch (Throwable var12) {
              var3.addSuppressed(var12);
            }
          } else {
            pst.close();
          }
        }

      }
    } catch (SQLException var15) {
      SqlDataSourceUtils.logSqlException(var15);
    }

  }

  public int getAccountsRegistered() {
    String sql = "SELECT COUNT(*) FROM " + this.tableName + ";";

    try {
      PreparedStatement pst = this.con.prepareStatement(sql);
      Throwable var3 = null;

      int var6;
      try {
        ResultSet rs = pst.executeQuery();
        Throwable var5 = null;

        try {
          if (!rs.next()) {
            return 0;
          }

          var6 = rs.getInt(1);
        } catch (Throwable var34) {
          throw var34;
        } finally {
          if (rs != null) {
            if (var5 != null) {
              try {
                rs.close();
              } catch (Throwable var33) {
                var5.addSuppressed(var33);
              }
            } else {
              rs.close();
            }
          }

        }
      } catch (Throwable var36) {
        var3 = var36;
        throw var36;
      } finally {
        if (pst != null) {
          if (var3 != null) {
            try {
              pst.close();
            } catch (Throwable var32) {
              var3.addSuppressed(var32);
            }
          } else {
            pst.close();
          }
        }

      }

      return (int)var6;
    } catch (SQLException var38) {
      SqlDataSourceUtils.logSqlException(var38);
      return 0;
    }
  }

  public boolean updateRealName(String user, String realName) {
    String sql = "UPDATE " + this.tableName + " SET " + this.col.REAL_NAME + "=? WHERE " + this.col.NAME + "=?;";

    try {
      PreparedStatement pst = this.con.prepareStatement(sql);
      Throwable var5 = null;

      boolean var6;
      try {
        pst.setString(1, realName);
        pst.setString(2, user);
        pst.executeUpdate();
        var6 = true;
      } catch (Throwable var16) {
        var5 = var16;
        throw var16;
      } finally {
        if (pst != null) {
          if (var5 != null) {
            try {
              pst.close();
            } catch (Throwable var15) {
              var5.addSuppressed(var15);
            }
          } else {
            pst.close();
          }
        }

      }

      return var6;
    } catch (SQLException var18) {
      SqlDataSourceUtils.logSqlException(var18);
      return false;
    }
  }

  public DataSourceResult<String> getEmail(String user) {
    String sql = "SELECT " + this.col.EMAIL + " FROM " + this.tableName + " WHERE " + this.col.NAME + "=?;";

    try {
      PreparedStatement pst = this.con.prepareStatement(sql);
      Throwable var4 = null;

      Object var7;
      try {
        pst.setString(1, user);
        ResultSet rs = pst.executeQuery();
        Throwable var6 = null;

        try {
          if (!rs.next()) {
            return DataSourceResult.unknownPlayer();
          }

          var7 = DataSourceResult.of(rs.getString(1));
        } catch (Throwable var35) {
          var7 = var35;
          var6 = var35;
          throw var35;
        } finally {
          if (rs != null) {
            if (var6 != null) {
              try {
                rs.close();
              } catch (Throwable var34) {
                var6.addSuppressed(var34);
              }
            } else {
              rs.close();
            }
          }

        }
      } catch (Throwable var37) {
        var4 = var37;
        throw var37;
      } finally {
        if (pst != null) {
          if (var4 != null) {
            try {
              pst.close();
            } catch (Throwable var33) {
              var4.addSuppressed(var33);
            }
          } else {
            pst.close();
          }
        }

      }

      return (DataSourceResult)var7;
    } catch (SQLException var39) {
      SqlDataSourceUtils.logSqlException(var39);
      return DataSourceResult.unknownPlayer();
    }
  }

  public List<PlayerAuth> getAllAuths() {
    List<PlayerAuth> auths = new ArrayList();
    String sql = "SELECT * FROM " + this.tableName + ";";

    try {
      PreparedStatement pst = this.con.prepareStatement(sql);
      Throwable var4 = null;

      try {
        ResultSet rs = pst.executeQuery();
        Throwable var6 = null;

        try {
          while(rs.next()) {
            PlayerAuth auth = this.buildAuthFromResultSet(rs);
            auths.add(auth);
          }
        } catch (Throwable var31) {
          var6 = var31;
          throw var31;
        } finally {
          if (rs != null) {
            if (var6 != null) {
              try {
                rs.close();
              } catch (Throwable var30) {
                var6.addSuppressed(var30);
              }
            } else {
              rs.close();
            }
          }

        }
      } catch (Throwable var33) {
        var4 = var33;
        throw var33;
      } finally {
        if (pst != null) {
          if (var4 != null) {
            try {
              pst.close();
            } catch (Throwable var29) {
              var4.addSuppressed(var29);
            }
          } else {
            pst.close();
          }
        }

      }
    } catch (SQLException var35) {
      SqlDataSourceUtils.logSqlException(var35);
    }

    return auths;
  }

  public List<String> getLoggedPlayersWithEmptyMail() {
    List<String> players = new ArrayList();
    String sql = "SELECT " + this.col.REAL_NAME + " FROM " + this.tableName + " WHERE " + this.col.IS_LOGGED + " = 1 AND (" + this.col.EMAIL + " = 'your@email.com' OR " + this.col.EMAIL + " IS NULL);";

    try {
      Statement st = this.con.createStatement();
      Throwable var4 = null;

      try {
        ResultSet rs = st.executeQuery(sql);
        Throwable var6 = null;

        try {
          while(rs.next()) {
            players.add(rs.getString(1));
          }
        } catch (Throwable var31) {
          var6 = var31;
          throw var31;
        } finally {
          if (rs != null) {
            if (var6 != null) {
              try {
                rs.close();
              } catch (Throwable var30) {
                var6.addSuppressed(var30);
              }
            } else {
              rs.close();
            }
          }

        }
      } catch (Throwable var33) {
        var4 = var33;
        throw var33;
      } finally {
        if (st != null) {
          if (var4 != null) {
            try {
              st.close();
            } catch (Throwable var29) {
              var4.addSuppressed(var29);
            }
          } else {
            st.close();
          }
        }

      }
    } catch (SQLException var35) {
      SqlDataSourceUtils.logSqlException(var35);
    }

    return players;
  }

  private PlayerAuth buildAuthFromResultSet(ResultSet row) throws SQLException {
    String salt = !this.col.SALT.isEmpty() ? row.getString(this.col.SALT) : null;
    return PlayerAuth.builder().name(row.getString(this.col.NAME)).email(row.getString(this.col.EMAIL)).realName(row.getString(this.col.REAL_NAME)).password(row.getString(this.col.PASSWORD), salt).lastLogin(SqlDataSourceUtils.getNullableLong(row, this.col.LAST_LOGIN)).lastIp(row.getString(this.col.LAST_IP)).registrationDate(row.getLong(this.col.REGISTRATION_DATE)).registrationIp(row.getString(this.col.REGISTRATION_IP)).locX(row.getDouble(this.col.LASTLOC_X)).locY(row.getDouble(this.col.LASTLOC_Y)).locZ(row.getDouble(this.col.LASTLOC_Z)).locWorld(row.getString(this.col.LASTLOC_WORLD)).locYaw(row.getFloat(this.col.LASTLOC_YAW)).locPitch(row.getFloat(this.col.LASTLOC_PITCH)).build();
  }

  private void addRegistrationDateColumn(Statement st) throws SQLException {
    st.executeUpdate("ALTER TABLE " + this.tableName + " ADD COLUMN " + this.col.REGISTRATION_DATE + " TIMESTAMP NOT NULL DEFAULT '0';");
    long currentTimestamp = System.currentTimeMillis();
    int updatedRows = st.executeUpdate(String.format("UPDATE %s SET %s = %d;", this.tableName, this.col.REGISTRATION_DATE, currentTimestamp));
    ConsoleLogger.info("Created column '" + this.col.REGISTRATION_DATE + "' and set the current timestamp, " + currentTimestamp + ", to all " + updatedRows + " rows");
  }

  private static void close(Statement st) {
    if (st != null) {
      try {
        st.close();
      } catch (SQLException var2) {
        SqlDataSourceUtils.logSqlException(var2);
      }
    }

  }

  private static void close(Connection con) {
    if (con != null) {
      try {
        con.close();
      } catch (SQLException var2) {
        SqlDataSourceUtils.logSqlException(var2);
      }
    }

  }
}
