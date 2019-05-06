//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package fr.xephi.authme.datasource;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.data.auth.PlayerAuth;
import fr.xephi.authme.datasource.mysqlextensions.MySqlExtension;
import fr.xephi.authme.datasource.mysqlextensions.MySqlExtensionsFactory;
import fr.xephi.authme.libs.google.common.annotations.VisibleForTesting;
import fr.xephi.authme.libs.zaxxer.hikari.HikariDataSource;
import fr.xephi.authme.libs.zaxxer.hikari.pool.HikariPool.PoolInitializationException;
import fr.xephi.authme.security.crypts.HashedPassword;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.settings.properties.DatabaseSettings;
import fr.xephi.authme.settings.properties.HooksSettings;
import fr.xephi.authme.util.StringUtils;
import fr.xephi.authme.util.Utils;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
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

public class MySQL implements DataSource {
  private boolean useSsl;
  private String host;
  private String port;
  private String username;
  private String password;
  private String database;
  private String tableName;
  private int poolSize;
  private int maxLifetime;
  private List<String> columnOthers;
  private Columns col;
  private MySqlExtension sqlExtension;
  private HikariDataSource ds;

  public MySQL(Settings settings, MySqlExtensionsFactory extensionsFactory) throws SQLException {
    this.setParameters(settings, extensionsFactory);

    try {
      this.setConnectionArguments();
    } catch (RuntimeException var5) {
      if (var5 instanceof IllegalArgumentException) {
        ConsoleLogger.warning("Invalid database arguments! Please check your configuration!");
        ConsoleLogger.warning("If this error persists, please report it to the developer!");
      }

      if (var5 instanceof PoolInitializationException) {
        ConsoleLogger.warning("Can't initialize database connection! Please check your configuration!");
        ConsoleLogger.warning("If this error persists, please report it to the developer!");
      }

      ConsoleLogger.warning("Can't use the Hikari Connection Pool! Please, report this error to the developer!");
      throw var5;
    }

    try {
      this.checkTablesAndColumns();
    } catch (SQLException var4) {
      this.closeConnection();
      ConsoleLogger.logException("Can't initialize the MySQL database:", var4);
      ConsoleLogger.warning("Please check your database settings in the config.yml file!");
      throw var4;
    }
  }

  @VisibleForTesting
  MySQL(Settings settings, HikariDataSource hikariDataSource, MySqlExtensionsFactory extensionsFactory) {
    this.ds = hikariDataSource;
    this.setParameters(settings, extensionsFactory);
  }

  private void setParameters(Settings settings, MySqlExtensionsFactory extensionsFactory) {
    this.host = (String)settings.getProperty(DatabaseSettings.MYSQL_HOST);
    this.port = (String)settings.getProperty(DatabaseSettings.MYSQL_PORT);
    this.username = (String)settings.getProperty(DatabaseSettings.MYSQL_USERNAME);
    this.password = (String)settings.getProperty(DatabaseSettings.MYSQL_PASSWORD);
    this.database = (String)settings.getProperty(DatabaseSettings.MYSQL_DATABASE);
    this.tableName = (String)settings.getProperty(DatabaseSettings.MYSQL_TABLE);
    this.columnOthers = (List)settings.getProperty(HooksSettings.MYSQL_OTHER_USERNAME_COLS);
    this.col = new Columns(settings);
    this.sqlExtension = extensionsFactory.buildExtension(this.col);
    this.poolSize = (Integer)settings.getProperty(DatabaseSettings.MYSQL_POOL_SIZE);
    if (this.poolSize == -1) {
      this.poolSize = Utils.getCoreCount() * 3;
    }

    this.maxLifetime = (Integer)settings.getProperty(DatabaseSettings.MYSQL_CONNECTION_MAX_LIFETIME);
    this.useSsl = (Boolean)settings.getProperty(DatabaseSettings.MYSQL_USE_SSL);
  }

  private void setConnectionArguments() {
    this.ds = new HikariDataSource();
    this.ds.setPoolName("AuthMeMYSQLPool");
    this.ds.setMaximumPoolSize(this.poolSize);
    this.ds.setMaxLifetime((long)(this.maxLifetime * 1000));
    this.ds.setJdbcUrl("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database);
    this.ds.setUsername(this.username);
    this.ds.setPassword(this.password);
    this.ds.addDataSourceProperty("useSSL", String.valueOf(this.useSsl));
    this.ds.addDataSourceProperty("characterEncoding", "utf8");
    this.ds.addDataSourceProperty("encoding", "UTF-8");
    this.ds.addDataSourceProperty("useUnicode", "true");
    this.ds.addDataSourceProperty("rewriteBatchedStatements", "true");
    this.ds.addDataSourceProperty("jdbcCompliantTruncation", "false");
    this.ds.addDataSourceProperty("cachePrepStmts", "true");
    this.ds.addDataSourceProperty("prepStmtCacheSize", "275");
    this.ds.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
    ConsoleLogger.info("Connection arguments loaded, Hikari ConnectionPool ready!");
  }

  public void reload() {
    if (this.ds != null) {
      this.ds.close();
    }

    this.setConnectionArguments();
    ConsoleLogger.info("Hikari ConnectionPool arguments reloaded!");
  }

  @Override
  public void invalidateCache(String playerName) {

  }

  @Override
  public void refreshCache(String playerName) {

  }

  private Connection getConnection() throws SQLException {
    return this.ds.getConnection();
  }

  private void checkTablesAndColumns() throws SQLException {
    Connection con = this.getConnection();
    Throwable var2 = null;

    try {
      Statement st = con.createStatement();
      Throwable var4 = null;

      try {
        String sql = "CREATE TABLE IF NOT EXISTS " + this.tableName + " (" + this.col.ID + " MEDIUMINT(8) UNSIGNED AUTO_INCREMENT,PRIMARY KEY (" + this.col.ID + ")) CHARACTER SET = utf8;";
        st.executeUpdate(sql);
        DatabaseMetaData md = con.getMetaData();
        if (this.isColumnMissing(md, this.col.NAME)) {
          st.executeUpdate("ALTER TABLE " + this.tableName + " ADD COLUMN " + this.col.NAME + " VARCHAR(255) NOT NULL UNIQUE AFTER " + this.col.ID + ";");
        }

        if (this.isColumnMissing(md, this.col.REAL_NAME)) {
          st.executeUpdate("ALTER TABLE " + this.tableName + " ADD COLUMN " + this.col.REAL_NAME + " VARCHAR(255) NOT NULL AFTER " + this.col.NAME + ";");
        }

        if (this.isColumnMissing(md, this.col.PASSWORD)) {
          st.executeUpdate("ALTER TABLE " + this.tableName + " ADD COLUMN " + this.col.PASSWORD + " VARCHAR(255) CHARACTER SET ascii COLLATE ascii_bin NOT NULL;");
        }

        if (!this.col.SALT.isEmpty() && this.isColumnMissing(md, this.col.SALT)) {
          st.executeUpdate("ALTER TABLE " + this.tableName + " ADD COLUMN " + this.col.SALT + " VARCHAR(255);");
        }

        if (this.isColumnMissing(md, this.col.LAST_IP)) {
          st.executeUpdate("ALTER TABLE " + this.tableName + " ADD COLUMN " + this.col.LAST_IP + " VARCHAR(40) CHARACTER SET ascii COLLATE ascii_bin;");
        } else {
          MySqlMigrater.migrateLastIpColumn(st, md, this.tableName, this.col);
        }

        if (this.isColumnMissing(md, this.col.LAST_LOGIN)) {
          st.executeUpdate("ALTER TABLE " + this.tableName + " ADD COLUMN " + this.col.LAST_LOGIN + " BIGINT;");
        } else {
          MySqlMigrater.migrateLastLoginColumn(st, md, this.tableName, this.col);
        }

        if (this.isColumnMissing(md, this.col.REGISTRATION_DATE)) {
          MySqlMigrater.addRegistrationDateColumn(st, this.tableName, this.col);
        }

        if (this.isColumnMissing(md, this.col.REGISTRATION_IP)) {
          st.executeUpdate("ALTER TABLE " + this.tableName + " ADD COLUMN " + this.col.REGISTRATION_IP + " VARCHAR(40) CHARACTER SET ascii COLLATE ascii_bin;");
        }

        if (this.isColumnMissing(md, this.col.LASTLOC_X)) {
          st.executeUpdate("ALTER TABLE " + this.tableName + " ADD COLUMN " + this.col.LASTLOC_X + " DOUBLE NOT NULL DEFAULT '0.0' AFTER " + this.col.LAST_LOGIN + " , ADD " + this.col.LASTLOC_Y + " DOUBLE NOT NULL DEFAULT '0.0' AFTER " + this.col.LASTLOC_X + " , ADD " + this.col.LASTLOC_Z + " DOUBLE NOT NULL DEFAULT '0.0' AFTER " + this.col.LASTLOC_Y);
        } else {
          st.executeUpdate("ALTER TABLE " + this.tableName + " MODIFY " + this.col.LASTLOC_X + " DOUBLE NOT NULL DEFAULT '0.0', MODIFY " + this.col.LASTLOC_Y + " DOUBLE NOT NULL DEFAULT '0.0', MODIFY " + this.col.LASTLOC_Z + " DOUBLE NOT NULL DEFAULT '0.0';");
        }

        if (this.isColumnMissing(md, this.col.LASTLOC_WORLD)) {
          st.executeUpdate("ALTER TABLE " + this.tableName + " ADD COLUMN " + this.col.LASTLOC_WORLD + " VARCHAR(255) NOT NULL DEFAULT 'world' AFTER " + this.col.LASTLOC_Z);
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
          st.executeUpdate("ALTER TABLE " + this.tableName + " ADD COLUMN " + this.col.IS_LOGGED + " SMALLINT NOT NULL DEFAULT '0' AFTER " + this.col.EMAIL);
        }

        if (this.isColumnMissing(md, this.col.HAS_SESSION)) {
          st.executeUpdate("ALTER TABLE " + this.tableName + " ADD COLUMN " + this.col.HAS_SESSION + " SMALLINT NOT NULL DEFAULT '0' AFTER " + this.col.IS_LOGGED);
        }
      } catch (Throwable var28) {
        var4 = var28;
        throw var28;
      } finally {
        if (st != null) {
          if (var4 != null) {
            try {
              st.close();
            } catch (Throwable var27) {
              var4.addSuppressed(var27);
            }
          } else {
            st.close();
          }
        }

      }
    } catch (Throwable var30) {
      var2 = var30;
      throw var30;
    } finally {
      if (con != null) {
        if (var2 != null) {
          try {
            con.close();
          } catch (Throwable var26) {
            var2.addSuppressed(var26);
          }
        } else {
          con.close();
        }
      }

    }

    ConsoleLogger.info("MySQL setup finished");
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

  public boolean isAuthAvailable(String user) {
    String sql = "SELECT " + this.col.NAME + " FROM " + this.tableName + " WHERE " + this.col.NAME + "=?;";

    try {
      Connection con = this.getConnection();
      Throwable var4 = null;

      Boolean var9;
      try {
        PreparedStatement pst = con.prepareStatement(sql);
        Throwable var6 = null;

        try {
          pst.setString(1, user.toLowerCase());
          ResultSet rs = pst.executeQuery();
          Throwable var8 = null;

          try {
            var9 = rs.next();
          } catch (Throwable var56) {
            throw var56;
          } finally {
            if (rs != null) {
              if (var8 != null) {
                try {
                  rs.close();
                } catch (Throwable var55) {
                  var8.addSuppressed(var55);
                }
              } else {
                rs.close();
              }
            }

          }
        } catch (Throwable var58) {
          var6 = var58;
          throw var58;
        } finally {
          if (pst != null) {
            if (var6 != null) {
              try {
                pst.close();
              } catch (Throwable var54) {
                var6.addSuppressed(var54);
              }
            } else {
              pst.close();
            }
          }

        }
      } catch (Throwable var60) {
        var4 = var60;
        throw var60;
      } finally {
        if (con != null) {
          if (var4 != null) {
            try {
              con.close();
            } catch (Throwable var53) {
              var4.addSuppressed(var53);
            }
          } else {
            con.close();
          }
        }

      }

      return (boolean)var9;
    } catch (SQLException var62) {
      SqlDataSourceUtils.logSqlException(var62);
      return false;
    }
  }

  public HashedPassword getPassword(String user) {
    boolean useSalt = !this.col.SALT.isEmpty();
    String sql = "SELECT " + this.col.PASSWORD + (useSalt ? ", " + this.col.SALT : "") + " FROM " + this.tableName + " WHERE " + this.col.NAME + "=?;";

    try {
      Connection con = this.getConnection();
      Throwable var5 = null;

      Object var10;
      try {
        PreparedStatement pst = con.prepareStatement(sql);
        Throwable var7 = null;

        try {
          pst.setString(1, user.toLowerCase());
          ResultSet rs = pst.executeQuery();
          Throwable var9 = null;

          try {
            if (!rs.next()) {
              return null;
            }

            var10 = new HashedPassword(rs.getString(this.col.PASSWORD), useSalt ? rs.getString(this.col.SALT) : null);
          } catch (Throwable var63) {
            var10 = var63;
            var9 = var63;
            throw var63;
          } finally {
            if (rs != null) {
              if (var9 != null) {
                try {
                  rs.close();
                } catch (Throwable var62) {
                  var9.addSuppressed(var62);
                }
              } else {
                rs.close();
              }
            }

          }
        } catch (Throwable var65) {
          var7 = var65;
          throw var65;
        } finally {
          if (pst != null) {
            if (var7 != null) {
              try {
                pst.close();
              } catch (Throwable var61) {
                var7.addSuppressed(var61);
              }
            } else {
              pst.close();
            }
          }

        }
      } catch (Throwable var67) {
        var5 = var67;
        throw var67;
      } finally {
        if (con != null) {
          if (var5 != null) {
            try {
              con.close();
            } catch (Throwable var60) {
              var5.addSuppressed(var60);
            }
          } else {
            con.close();
          }
        }

      }

      return (HashedPassword)var10;
    } catch (SQLException var69) {
      SqlDataSourceUtils.logSqlException(var69);
      return null;
    }
  }

  public PlayerAuth getAuth(String user) {
    String sql = "SELECT * FROM " + this.tableName + " WHERE " + this.col.NAME + "=?;";

    try {
      Connection con = this.getConnection();
      Throwable var5 = null;

      PlayerAuth var11;
      try {
        PreparedStatement pst = con.prepareStatement(sql);
        Throwable var7 = null;

        try {
          pst.setString(1, user.toLowerCase());
          ResultSet rs = pst.executeQuery();
          Throwable var9 = null;

          try {
            if (!rs.next()) {
              return null;
            }

            int id = rs.getInt(this.col.ID);
            PlayerAuth auth = this.buildAuthFromResultSet(rs);
            this.sqlExtension.extendAuth(auth, id, con);
            var11 = auth;
          } catch (Throwable var64) {
            var9 = var64;
            throw var64;
          } finally {
            if (rs != null) {
              if (var9 != null) {
                try {
                  rs.close();
                } catch (Throwable var63) {
                  var9.addSuppressed(var63);
                }
              } else {
                rs.close();
              }
            }

          }
        } catch (Throwable var66) {
          var7 = var66;
          throw var66;
        } finally {
          if (pst != null) {
            if (var7 != null) {
              try {
                pst.close();
              } catch (Throwable var62) {
                var7.addSuppressed(var62);
              }
            } else {
              pst.close();
            }
          }

        }
      } catch (Throwable var68) {
        var5 = var68;
        throw var68;
      } finally {
        if (con != null) {
          if (var5 != null) {
            try {
              con.close();
            } catch (Throwable var61) {
              var5.addSuppressed(var61);
            }
          } else {
            con.close();
          }
        }

      }

      return var11;
    } catch (SQLException var70) {
      SqlDataSourceUtils.logSqlException(var70);
      return null;
    }
  }

  public boolean saveAuth(PlayerAuth auth) {
    try {
      Connection con = this.getConnection();
      Throwable var3 = null;

      boolean var64;
      try {
        boolean useSalt = !this.col.SALT.isEmpty() || !StringUtils.isEmpty(auth.getPassword().getSalt());
        boolean hasEmail = auth.getEmail() != null;
        String emailPlaceholder = hasEmail ? "?" : "DEFAULT";
        String sql = "INSERT INTO " + this.tableName + "(" + this.col.NAME + "," + this.col.PASSWORD + "," + this.col.REAL_NAME + "," + this.col.EMAIL + "," + this.col.REGISTRATION_DATE + "," + this.col.REGISTRATION_IP + (useSalt ? "," + this.col.SALT : "") + ") VALUES (?,?,?," + emailPlaceholder + ",?,?" + (useSalt ? ",?" : "") + ");";
        PreparedStatement pst = con.prepareStatement(sql);
        Throwable var9 = null;

        try {
          int index = 1;
          int var66 = index + 1;
          pst.setString(index, auth.getNickname());
          pst.setString(var66++, auth.getPassword().getHash());
          pst.setString(var66++, auth.getRealName());
          if (hasEmail) {
            pst.setString(var66++, auth.getEmail());
          }

          pst.setObject(var66++, auth.getRegistrationDate());
          pst.setString(var66++, auth.getRegistrationIp());
          if (useSalt) {
            pst.setString(var66++, auth.getPassword().getSalt());
          }

          pst.executeUpdate();
        } catch (Throwable var58) {
          var9 = var58;
          throw var58;
        } finally {
          if (pst != null) {
            if (var9 != null) {
              try {
                pst.close();
              } catch (Throwable var55) {
                var9.addSuppressed(var55);
              }
            } else {
              pst.close();
            }
          }

        }

        if (!this.columnOthers.isEmpty()) {
          Iterator var63 = this.columnOthers.iterator();

          while(var63.hasNext()) {
            String column = (String)var63.next();
            pst = con.prepareStatement("UPDATE " + this.tableName + " SET " + column + "=? WHERE " + this.col.NAME + "=?;");
            Throwable var11 = null;

            try {
              pst.setString(1, auth.getRealName());
              pst.setString(2, auth.getNickname());
              pst.executeUpdate();
            } catch (Throwable var56) {
              var11 = var56;
              throw var56;
            } finally {
              if (pst != null) {
                if (var11 != null) {
                  try {
                    pst.close();
                  } catch (Throwable var54) {
                    var11.addSuppressed(var54);
                  }
                } else {
                  pst.close();
                }
              }

            }
          }
        }

        this.sqlExtension.saveAuth(auth, con);
        var64 = true;
      } catch (Throwable var60) {
        var3 = var60;
        throw var60;
      } finally {
        if (con != null) {
          if (var3 != null) {
            try {
              con.close();
            } catch (Throwable var53) {
              var3.addSuppressed(var53);
            }
          } else {
            con.close();
          }
        }

      }

      return var64;
    } catch (SQLException var62) {
      SqlDataSourceUtils.logSqlException(var62);
      return false;
    }
  }

  public boolean updatePassword(PlayerAuth auth) {
    return this.updatePassword(auth.getNickname(), auth.getPassword());
  }

  public boolean updatePassword(String user, HashedPassword password) {
    user = user.toLowerCase();

    try {
      Connection con = this.getConnection();
      Throwable var4 = null;

      boolean var62;
      try {
        boolean useSalt = !this.col.SALT.isEmpty();
        String sql;
        PreparedStatement pst;
        Throwable var8;
        if (useSalt) {
          sql = String.format("UPDATE %s SET %s = ?, %s = ? WHERE %s = ?;", this.tableName, this.col.PASSWORD, this.col.SALT, this.col.NAME);
          pst = con.prepareStatement(sql);
          var8 = null;

          try {
            pst.setString(1, password.getHash());
            pst.setString(2, password.getSalt());
            pst.setString(3, user);
            pst.executeUpdate();
          } catch (Throwable var56) {
            var8 = var56;
            throw var56;
          } finally {
            if (pst != null) {
              if (var8 != null) {
                try {
                  pst.close();
                } catch (Throwable var54) {
                  var8.addSuppressed(var54);
                }
              } else {
                pst.close();
              }
            }

          }
        } else {
          sql = String.format("UPDATE %s SET %s = ? WHERE %s = ?;", this.tableName, this.col.PASSWORD, this.col.NAME);
          pst = con.prepareStatement(sql);
          var8 = null;

          try {
            pst.setString(1, password.getHash());
            pst.setString(2, user);
            pst.executeUpdate();
          } catch (Throwable var55) {
            var8 = var55;
            throw var55;
          } finally {
            if (pst != null) {
              if (var8 != null) {
                try {
                  pst.close();
                } catch (Throwable var53) {
                  var8.addSuppressed(var53);
                }
              } else {
                pst.close();
              }
            }

          }
        }

        this.sqlExtension.changePassword(user, password, con);
        var62 = true;
      } catch (Throwable var59) {
        var4 = var59;
        throw var59;
      } finally {
        if (con != null) {
          if (var4 != null) {
            try {
              con.close();
            } catch (Throwable var52) {
              var4.addSuppressed(var52);
            }
          } else {
            con.close();
          }
        }

      }

      return var62;
    } catch (SQLException var61) {
      SqlDataSourceUtils.logSqlException(var61);
      return false;
    }
  }

  public boolean updateSession(PlayerAuth auth) {
    String sql = "UPDATE " + this.tableName + " SET " + this.col.LAST_IP + "=?, " + this.col.LAST_LOGIN + "=?, " + this.col.REAL_NAME + "=? WHERE " + this.col.NAME + "=?;";

    try {
      Connection con = this.getConnection();
      Throwable var4 = null;

      Boolean var7;
      try {
        PreparedStatement pst = con.prepareStatement(sql);
        Throwable var6 = null;

        try {
          pst.setString(1, auth.getLastIp());
          pst.setObject(2, auth.getLastLogin());
          pst.setString(3, auth.getRealName());
          pst.setString(4, auth.getNickname());
          pst.executeUpdate();
          var7 = true;
        } catch (Throwable var32) {
          throw var32;
        } finally {
          if (pst != null) {
            if (var6 != null) {
              try {
                pst.close();
              } catch (Throwable var31) {
                var6.addSuppressed(var31);
              }
            } else {
              pst.close();
            }
          }

        }
      } catch (Throwable var34) {
        var4 = var34;
        throw var34;
      } finally {
        if (con != null) {
          if (var4 != null) {
            try {
              con.close();
            } catch (Throwable var30) {
              var4.addSuppressed(var30);
            }
          } else {
            con.close();
          }
        }

      }

      return (boolean)var7;
    } catch (SQLException var36) {
      SqlDataSourceUtils.logSqlException(var36);
      return false;
    }
  }

  public Set<String> getRecordsToPurge(long until) {
    Set<String> list = new HashSet();
    String select = "SELECT " + this.col.NAME + " FROM " + this.tableName + " WHERE GREATEST( COALESCE(" + this.col.LAST_LOGIN + ", 0), COALESCE(" + this.col.REGISTRATION_DATE + ", 0)) < ?;";

    try {
      Connection con = this.getConnection();
      Throwable var6 = null;

      try {
        PreparedStatement selectPst = con.prepareStatement(select);
        Throwable var8 = null;

        try {
          selectPst.setLong(1, until);
          ResultSet rs = selectPst.executeQuery();
          Throwable var10 = null;

          try {
            while(rs.next()) {
              list.add(rs.getString(this.col.NAME));
            }
          } catch (Throwable var57) {
            var10 = var57;
            throw var57;
          } finally {
            if (rs != null) {
              if (var10 != null) {
                try {
                  rs.close();
                } catch (Throwable var56) {
                  var10.addSuppressed(var56);
                }
              } else {
                rs.close();
              }
            }

          }
        } catch (Throwable var59) {
          var8 = var59;
          throw var59;
        } finally {
          if (selectPst != null) {
            if (var8 != null) {
              try {
                selectPst.close();
              } catch (Throwable var55) {
                var8.addSuppressed(var55);
              }
            } else {
              selectPst.close();
            }
          }

        }
      } catch (Throwable var61) {
        var6 = var61;
        throw var61;
      } finally {
        if (con != null) {
          if (var6 != null) {
            try {
              con.close();
            } catch (Throwable var54) {
              var6.addSuppressed(var54);
            }
          } else {
            con.close();
          }
        }

      }
    } catch (SQLException var63) {
      SqlDataSourceUtils.logSqlException(var63);
    }

    return list;
  }

  public boolean removeAuth(String user) {
    user = user.toLowerCase();
    String sql = "DELETE FROM " + this.tableName + " WHERE " + this.col.NAME + "=?;";

    try {
      Connection con = this.getConnection();
      Throwable var4 = null;

      Boolean var7;
      try {
        PreparedStatement pst = con.prepareStatement(sql);
        Throwable var6 = null;

        try {
          this.sqlExtension.removeAuth(user, con);
          pst.setString(1, user.toLowerCase());
          pst.executeUpdate();
          var7 = true;
        } catch (Throwable var32) {
          throw var32;
        } finally {
          if (pst != null) {
            if (var6 != null) {
              try {
                pst.close();
              } catch (Throwable var31) {
                var6.addSuppressed(var31);
              }
            } else {
              pst.close();
            }
          }

        }
      } catch (Throwable var34) {
        var4 = var34;
        throw var34;
      } finally {
        if (con != null) {
          if (var4 != null) {
            try {
              con.close();
            } catch (Throwable var30) {
              var4.addSuppressed(var30);
            }
          } else {
            con.close();
          }
        }

      }

      return (boolean)var7;
    } catch (SQLException var36) {
      SqlDataSourceUtils.logSqlException(var36);
      return false;
    }
  }

  public boolean updateQuitLoc(PlayerAuth auth) {
    String sql = "UPDATE " + this.tableName + " SET " + this.col.LASTLOC_X + " =?, " + this.col.LASTLOC_Y + "=?, " + this.col.LASTLOC_Z + "=?, " + this.col.LASTLOC_WORLD + "=?, " + this.col.LASTLOC_YAW + "=?, " + this.col.LASTLOC_PITCH + "=? WHERE " + this.col.NAME + "=?;";

    try {
      Connection con = this.getConnection();
      Throwable var4 = null;

      Boolean var7;
      try {
        PreparedStatement pst = con.prepareStatement(sql);
        Throwable var6 = null;

        try {
          pst.setDouble(1, auth.getQuitLocX());
          pst.setDouble(2, auth.getQuitLocY());
          pst.setDouble(3, auth.getQuitLocZ());
          pst.setString(4, auth.getWorld());
          pst.setFloat(5, auth.getYaw());
          pst.setFloat(6, auth.getPitch());
          pst.setString(7, auth.getNickname());
          pst.executeUpdate();
          var7 = true;
        } catch (Throwable var32) {
          throw var32;
        } finally {
          if (pst != null) {
            if (var6 != null) {
              try {
                pst.close();
              } catch (Throwable var31) {
                var6.addSuppressed(var31);
              }
            } else {
              pst.close();
            }
          }

        }
      } catch (Throwable var34) {
        var4 = var34;
        throw var34;
      } finally {
        if (con != null) {
          if (var4 != null) {
            try {
              con.close();
            } catch (Throwable var30) {
              var4.addSuppressed(var30);
            }
          } else {
            con.close();
          }
        }

      }

      return (boolean)var7;
    } catch (SQLException var36) {
      SqlDataSourceUtils.logSqlException(var36);
      return false;
    }
  }

  public boolean updateEmail(PlayerAuth auth) {
    String sql = "UPDATE " + this.tableName + " SET " + this.col.EMAIL + " =? WHERE " + this.col.NAME + "=?;";

    try {
      Connection con = this.getConnection();
      Throwable var4 = null;

      Boolean var7;
      try {
        PreparedStatement pst = con.prepareStatement(sql);
        Throwable var6 = null;

        try {
          pst.setString(1, auth.getEmail());
          pst.setString(2, auth.getNickname());
          pst.executeUpdate();
          var7 = true;
        } catch (Throwable var32) {
          throw var32;
        } finally {
          if (pst != null) {
            if (var6 != null) {
              try {
                pst.close();
              } catch (Throwable var31) {
                var6.addSuppressed(var31);
              }
            } else {
              pst.close();
            }
          }

        }
      } catch (Throwable var34) {
        var4 = var34;
        throw var34;
      } finally {
        if (con != null) {
          if (var4 != null) {
            try {
              con.close();
            } catch (Throwable var30) {
              var4.addSuppressed(var30);
            }
          } else {
            con.close();
          }
        }

      }

      return (boolean)var7;
    } catch (SQLException var36) {
      SqlDataSourceUtils.logSqlException(var36);
      return false;
    }
  }

  public void closeConnection() {
    if (this.ds != null && !this.ds.isClosed()) {
      this.ds.close();
    }

  }

  public List<String> getAllAuthsByIp(String ip) {
    List<String> result = new ArrayList();
    String sql = "SELECT " + this.col.NAME + " FROM " + this.tableName + " WHERE " + this.col.LAST_IP + "=?;";

    try {
      Connection con = this.getConnection();
      Throwable var5 = null;

      try {
        PreparedStatement pst = con.prepareStatement(sql);
        Throwable var7 = null;

        try {
          pst.setString(1, ip);
          ResultSet rs = pst.executeQuery();
          Throwable var9 = null;

          try {
            while(rs.next()) {
              result.add(rs.getString(this.col.NAME));
            }
          } catch (Throwable var56) {
            var9 = var56;
            throw var56;
          } finally {
            if (rs != null) {
              if (var9 != null) {
                try {
                  rs.close();
                } catch (Throwable var55) {
                  var9.addSuppressed(var55);
                }
              } else {
                rs.close();
              }
            }

          }
        } catch (Throwable var58) {
          var7 = var58;
          throw var58;
        } finally {
          if (pst != null) {
            if (var7 != null) {
              try {
                pst.close();
              } catch (Throwable var54) {
                var7.addSuppressed(var54);
              }
            } else {
              pst.close();
            }
          }

        }
      } catch (Throwable var60) {
        var5 = var60;
        throw var60;
      } finally {
        if (con != null) {
          if (var5 != null) {
            try {
              con.close();
            } catch (Throwable var53) {
              var5.addSuppressed(var53);
            }
          } else {
            con.close();
          }
        }

      }
    } catch (SQLException var62) {
      SqlDataSourceUtils.logSqlException(var62);
    }

    return result;
  }

  public int countAuthsByEmail(String email) {
    String sql = "SELECT COUNT(1) FROM " + this.tableName + " WHERE UPPER(" + this.col.EMAIL + ") = UPPER(?)";

    try {
      Connection con = this.getConnection();
      Throwable var4 = null;

      int var9;
      try {
        PreparedStatement pst = con.prepareStatement(sql);
        Throwable var6 = null;

        try {
          pst.setString(1, email);
          ResultSet rs = pst.executeQuery();
          Throwable var8 = null;

          try {
            if (!rs.next()) {
              return 0;
            }

            var9 = rs.getInt(1);
          } catch (Throwable var62) {
            throw var62;
          } finally {
            if (rs != null) {
              if (var8 != null) {
                try {
                  rs.close();
                } catch (Throwable var61) {
                  var8.addSuppressed(var61);
                }
              } else {
                rs.close();
              }
            }

          }
        } catch (Throwable var64) {
          var6 = var64;
          throw var64;
        } finally {
          if (pst != null) {
            if (var6 != null) {
              try {
                pst.close();
              } catch (Throwable var60) {
                var6.addSuppressed(var60);
              }
            } else {
              pst.close();
            }
          }

        }
      } catch (Throwable var66) {
        var4 = var66;
        throw var66;
      } finally {
        if (con != null) {
          if (var4 != null) {
            try {
              con.close();
            } catch (Throwable var59) {
              var4.addSuppressed(var59);
            }
          } else {
            con.close();
          }
        }

      }

      return (int)var9;
    } catch (SQLException var68) {
      SqlDataSourceUtils.logSqlException(var68);
      return 0;
    }
  }

  public void purgeRecords(Collection<String> toPurge) {
    String sql = "DELETE FROM " + this.tableName + " WHERE " + this.col.NAME + "=?;";

    try {
      Connection con = this.getConnection();
      Throwable var4 = null;

      try {
        PreparedStatement pst = con.prepareStatement(sql);
        Throwable var6 = null;

        try {
          Iterator var7 = toPurge.iterator();

          while(var7.hasNext()) {
            String name = (String)var7.next();
            pst.setString(1, name.toLowerCase());
            pst.executeUpdate();
          }
        } catch (Throwable var32) {
          var6 = var32;
          throw var32;
        } finally {
          if (pst != null) {
            if (var6 != null) {
              try {
                pst.close();
              } catch (Throwable var31) {
                var6.addSuppressed(var31);
              }
            } else {
              pst.close();
            }
          }

        }
      } catch (Throwable var34) {
        var4 = var34;
        throw var34;
      } finally {
        if (con != null) {
          if (var4 != null) {
            try {
              con.close();
            } catch (Throwable var30) {
              var4.addSuppressed(var30);
            }
          } else {
            con.close();
          }
        }

      }
    } catch (SQLException var36) {
      SqlDataSourceUtils.logSqlException(var36);
    }

  }

  public DataSourceType getType() {
    return DataSourceType.MYSQL;
  }

  public boolean isLogged(String user) {
    String sql = "SELECT " + this.col.IS_LOGGED + " FROM " + this.tableName + " WHERE " + this.col.NAME + "=?;";

    try {
      Connection con = this.getConnection();
      Throwable var4 = null;

      Boolean var9;
      try {
        PreparedStatement pst = con.prepareStatement(sql);
        Throwable var6 = null;

        try {
          pst.setString(1, user);
          ResultSet rs = pst.executeQuery();
          Throwable var8 = null;

          try {
            var9 = rs.next() && rs.getInt(this.col.IS_LOGGED) == 1;
          } catch (Throwable var56) {
            throw var56;
          } finally {
            if (rs != null) {
              if (var8 != null) {
                try {
                  rs.close();
                } catch (Throwable var55) {
                  var8.addSuppressed(var55);
                }
              } else {
                rs.close();
              }
            }

          }
        } catch (Throwable var58) {
          var6 = var58;
          throw var58;
        } finally {
          if (pst != null) {
            if (var6 != null) {
              try {
                pst.close();
              } catch (Throwable var54) {
                var6.addSuppressed(var54);
              }
            } else {
              pst.close();
            }
          }

        }
      } catch (Throwable var60) {
        var4 = var60;
        throw var60;
      } finally {
        if (con != null) {
          if (var4 != null) {
            try {
              con.close();
            } catch (Throwable var53) {
              var4.addSuppressed(var53);
            }
          } else {
            con.close();
          }
        }

      }

      return (boolean)var9;
    } catch (SQLException var62) {
      SqlDataSourceUtils.logSqlException(var62);
      return false;
    }
  }

  public void setLogged(String user) {
    String sql = "UPDATE " + this.tableName + " SET " + this.col.IS_LOGGED + "=? WHERE " + this.col.NAME + "=?;";

    try {
      Connection con = this.getConnection();
      Throwable var4 = null;

      try {
        PreparedStatement pst = con.prepareStatement(sql);
        Throwable var6 = null;

        try {
          pst.setInt(1, 1);
          pst.setString(2, user.toLowerCase());
          pst.executeUpdate();
        } catch (Throwable var31) {
          var6 = var31;
          throw var31;
        } finally {
          if (pst != null) {
            if (var6 != null) {
              try {
                pst.close();
              } catch (Throwable var30) {
                var6.addSuppressed(var30);
              }
            } else {
              pst.close();
            }
          }

        }
      } catch (Throwable var33) {
        var4 = var33;
        throw var33;
      } finally {
        if (con != null) {
          if (var4 != null) {
            try {
              con.close();
            } catch (Throwable var29) {
              var4.addSuppressed(var29);
            }
          } else {
            con.close();
          }
        }

      }
    } catch (SQLException var35) {
      SqlDataSourceUtils.logSqlException(var35);
    }

  }

  public void setUnlogged(String user) {
    String sql = "UPDATE " + this.tableName + " SET " + this.col.IS_LOGGED + "=? WHERE " + this.col.NAME + "=?;";

    try {
      Connection con = this.getConnection();
      Throwable var4 = null;

      try {
        PreparedStatement pst = con.prepareStatement(sql);
        Throwable var6 = null;

        try {
          pst.setInt(1, 0);
          pst.setString(2, user.toLowerCase());
          pst.executeUpdate();
        } catch (Throwable var31) {
          var6 = var31;
          throw var31;
        } finally {
          if (pst != null) {
            if (var6 != null) {
              try {
                pst.close();
              } catch (Throwable var30) {
                var6.addSuppressed(var30);
              }
            } else {
              pst.close();
            }
          }

        }
      } catch (Throwable var33) {
        var4 = var33;
        throw var33;
      } finally {
        if (con != null) {
          if (var4 != null) {
            try {
              con.close();
            } catch (Throwable var29) {
              var4.addSuppressed(var29);
            }
          } else {
            con.close();
          }
        }

      }
    } catch (SQLException var35) {
      SqlDataSourceUtils.logSqlException(var35);
    }

  }

  public boolean hasSession(String user) {
    String sql = "SELECT " + this.col.HAS_SESSION + " FROM " + this.tableName + " WHERE " + this.col.NAME + "=?;";

    try {
      Connection con = this.getConnection();
      Throwable var4 = null;

      Boolean var9;
      try {
        PreparedStatement pst = con.prepareStatement(sql);
        Throwable var6 = null;

        try {
          pst.setString(1, user.toLowerCase());
          ResultSet rs = pst.executeQuery();
          Throwable var8 = null;

          try {
            var9 = rs.next() && rs.getInt(this.col.HAS_SESSION) == 1;
          } catch (Throwable var56) {
            throw var56;
          } finally {
            if (rs != null) {
              if (var8 != null) {
                try {
                  rs.close();
                } catch (Throwable var55) {
                  var8.addSuppressed(var55);
                }
              } else {
                rs.close();
              }
            }

          }
        } catch (Throwable var58) {
          var6 = var58;
          throw var58;
        } finally {
          if (pst != null) {
            if (var6 != null) {
              try {
                pst.close();
              } catch (Throwable var54) {
                var6.addSuppressed(var54);
              }
            } else {
              pst.close();
            }
          }

        }
      } catch (Throwable var60) {
        var4 = var60;
        throw var60;
      } finally {
        if (con != null) {
          if (var4 != null) {
            try {
              con.close();
            } catch (Throwable var53) {
              var4.addSuppressed(var53);
            }
          } else {
            con.close();
          }
        }

      }

      return (boolean)var9;
    } catch (SQLException var62) {
      SqlDataSourceUtils.logSqlException(var62);
      return false;
    }
  }

  public void grantSession(String user) {
    String sql = "UPDATE " + this.tableName + " SET " + this.col.HAS_SESSION + "=? WHERE " + this.col.NAME + "=?;";

    try {
      Connection con = this.getConnection();
      Throwable var4 = null;

      try {
        PreparedStatement pst = con.prepareStatement(sql);
        Throwable var6 = null;

        try {
          pst.setInt(1, 1);
          pst.setString(2, user.toLowerCase());
          pst.executeUpdate();
        } catch (Throwable var31) {
          var6 = var31;
          throw var31;
        } finally {
          if (pst != null) {
            if (var6 != null) {
              try {
                pst.close();
              } catch (Throwable var30) {
                var6.addSuppressed(var30);
              }
            } else {
              pst.close();
            }
          }

        }
      } catch (Throwable var33) {
        var4 = var33;
        throw var33;
      } finally {
        if (con != null) {
          if (var4 != null) {
            try {
              con.close();
            } catch (Throwable var29) {
              var4.addSuppressed(var29);
            }
          } else {
            con.close();
          }
        }

      }
    } catch (SQLException var35) {
      SqlDataSourceUtils.logSqlException(var35);
    }

  }

  public void revokeSession(String user) {
    String sql = "UPDATE " + this.tableName + " SET " + this.col.HAS_SESSION + "=? WHERE " + this.col.NAME + "=?;";

    try {
      Connection con = this.getConnection();
      Throwable var4 = null;

      try {
        PreparedStatement pst = con.prepareStatement(sql);
        Throwable var6 = null;

        try {
          pst.setInt(1, 0);
          pst.setString(2, user.toLowerCase());
          pst.executeUpdate();
        } catch (Throwable var31) {
          var6 = var31;
          throw var31;
        } finally {
          if (pst != null) {
            if (var6 != null) {
              try {
                pst.close();
              } catch (Throwable var30) {
                var6.addSuppressed(var30);
              }
            } else {
              pst.close();
            }
          }

        }
      } catch (Throwable var33) {
        var4 = var33;
        throw var33;
      } finally {
        if (con != null) {
          if (var4 != null) {
            try {
              con.close();
            } catch (Throwable var29) {
              var4.addSuppressed(var29);
            }
          } else {
            con.close();
          }
        }

      }
    } catch (SQLException var35) {
      SqlDataSourceUtils.logSqlException(var35);
    }

  }

  public void purgeLogged() {
    String sql = "UPDATE " + this.tableName + " SET " + this.col.IS_LOGGED + "=? WHERE " + this.col.IS_LOGGED + "=?;";

    try {
      Connection con = this.getConnection();
      Throwable var3 = null;

      try {
        PreparedStatement pst = con.prepareStatement(sql);
        Throwable var5 = null;

        try {
          pst.setInt(1, 0);
          pst.setInt(2, 1);
          pst.executeUpdate();
        } catch (Throwable var30) {
          var5 = var30;
          throw var30;
        } finally {
          if (pst != null) {
            if (var5 != null) {
              try {
                pst.close();
              } catch (Throwable var29) {
                var5.addSuppressed(var29);
              }
            } else {
              pst.close();
            }
          }

        }
      } catch (Throwable var32) {
        var3 = var32;
        throw var32;
      } finally {
        if (con != null) {
          if (var3 != null) {
            try {
              con.close();
            } catch (Throwable var28) {
              var3.addSuppressed(var28);
            }
          } else {
            con.close();
          }
        }

      }
    } catch (SQLException var34) {
      SqlDataSourceUtils.logSqlException(var34);
    }

  }

  public int getAccountsRegistered() {
    int result = 0;
    String sql = "SELECT COUNT(*) FROM " + this.tableName;

    try {
      Connection con = this.getConnection();
      Throwable var4 = null;

      try {
        Statement st = con.createStatement();
        Throwable var6 = null;

        try {
          ResultSet rs = st.executeQuery(sql);
          Throwable var8 = null;

          try {
            if (rs.next()) {
              result = rs.getInt(1);
            }
          } catch (Throwable var55) {
            var8 = var55;
            throw var55;
          } finally {
            if (rs != null) {
              if (var8 != null) {
                try {
                  rs.close();
                } catch (Throwable var54) {
                  var8.addSuppressed(var54);
                }
              } else {
                rs.close();
              }
            }

          }
        } catch (Throwable var57) {
          var6 = var57;
          throw var57;
        } finally {
          if (st != null) {
            if (var6 != null) {
              try {
                st.close();
              } catch (Throwable var53) {
                var6.addSuppressed(var53);
              }
            } else {
              st.close();
            }
          }

        }
      } catch (Throwable var59) {
        var4 = var59;
        throw var59;
      } finally {
        if (con != null) {
          if (var4 != null) {
            try {
              con.close();
            } catch (Throwable var52) {
              var4.addSuppressed(var52);
            }
          } else {
            con.close();
          }
        }

      }
    } catch (SQLException var61) {
      SqlDataSourceUtils.logSqlException(var61);
    }

    return result;
  }

  public boolean updateRealName(String user, String realName) {
    String sql = "UPDATE " + this.tableName + " SET " + this.col.REAL_NAME + "=? WHERE " + this.col.NAME + "=?;";

    try {
      Connection con = this.getConnection();
      Throwable var5 = null;

      Boolean var8;
      try {
        PreparedStatement pst = con.prepareStatement(sql);
        Throwable var7 = null;

        try {
          pst.setString(1, realName);
          pst.setString(2, user);
          pst.executeUpdate();
          var8 = true;
        } catch (Throwable var33) {
          throw var33;
        } finally {
          if (pst != null) {
            if (var7 != null) {
              try {
                pst.close();
              } catch (Throwable var32) {
                var7.addSuppressed(var32);
              }
            } else {
              pst.close();
            }
          }

        }
      } catch (Throwable var35) {
        var5 = var35;
        throw var35;
      } finally {
        if (con != null) {
          if (var5 != null) {
            try {
              con.close();
            } catch (Throwable var31) {
              var5.addSuppressed(var31);
            }
          } else {
            con.close();
          }
        }

      }

      return (boolean)var8;
    } catch (SQLException var37) {
      SqlDataSourceUtils.logSqlException(var37);
      return false;
    }
  }

  public DataSourceResult<String> getEmail(String user) {
    String sql = "SELECT " + this.col.EMAIL + " FROM " + this.tableName + " WHERE " + this.col.NAME + "=?;";

    try {
      Connection con = this.getConnection();
      Throwable var4 = null;

      Object var9;
      try {
        PreparedStatement pst = con.prepareStatement(sql);
        Throwable var6 = null;

        try {
          pst.setString(1, user);
          ResultSet rs = pst.executeQuery();
          Throwable var8 = null;

          try {
            if (!rs.next()) {
              return DataSourceResult.unknownPlayer();
            }

            var9 = DataSourceResult.of(rs.getString(1));
          } catch (Throwable var62) {
            var9 = var62;
            var8 = var62;
            throw var62;
          } finally {
            if (rs != null) {
              if (var8 != null) {
                try {
                  rs.close();
                } catch (Throwable var61) {
                  var8.addSuppressed(var61);
                }
              } else {
                rs.close();
              }
            }

          }
        } catch (Throwable var64) {
          var6 = var64;
          throw var64;
        } finally {
          if (pst != null) {
            if (var6 != null) {
              try {
                pst.close();
              } catch (Throwable var60) {
                var6.addSuppressed(var60);
              }
            } else {
              pst.close();
            }
          }

        }
      } catch (Throwable var66) {
        var4 = var66;
        throw var66;
      } finally {
        if (con != null) {
          if (var4 != null) {
            try {
              con.close();
            } catch (Throwable var59) {
              var4.addSuppressed(var59);
            }
          } else {
            con.close();
          }
        }

      }

      return (DataSourceResult)var9;
    } catch (SQLException var68) {
      SqlDataSourceUtils.logSqlException(var68);
      return DataSourceResult.unknownPlayer();
    }
  }

  public List<PlayerAuth> getAllAuths() {
    ArrayList auths = new ArrayList();

    try {
      Connection con = this.getConnection();
      Throwable var3 = null;

      try {
        Statement st = con.createStatement();
        Throwable var5 = null;

        try {
          ResultSet rs = st.executeQuery("SELECT * FROM " + this.tableName);
          Throwable var7 = null;

          try {
            while(rs.next()) {
              PlayerAuth auth = this.buildAuthFromResultSet(rs);
              this.sqlExtension.extendAuth(auth, rs.getInt(this.col.ID), con);
              auths.add(auth);
            }
          } catch (Throwable var54) {
            var7 = var54;
            throw var54;
          } finally {
            if (rs != null) {
              if (var7 != null) {
                try {
                  rs.close();
                } catch (Throwable var53) {
                  var7.addSuppressed(var53);
                }
              } else {
                rs.close();
              }
            }

          }
        } catch (Throwable var56) {
          var5 = var56;
          throw var56;
        } finally {
          if (st != null) {
            if (var5 != null) {
              try {
                st.close();
              } catch (Throwable var52) {
                var5.addSuppressed(var52);
              }
            } else {
              st.close();
            }
          }

        }
      } catch (Throwable var58) {
        var3 = var58;
        throw var58;
      } finally {
        if (con != null) {
          if (var3 != null) {
            try {
              con.close();
            } catch (Throwable var51) {
              var3.addSuppressed(var51);
            }
          } else {
            con.close();
          }
        }

      }
    } catch (SQLException var60) {
      SqlDataSourceUtils.logSqlException(var60);
    }

    return auths;
  }

  public List<String> getLoggedPlayersWithEmptyMail() {
    List<String> players = new ArrayList();
    String sql = "SELECT " + this.col.REAL_NAME + " FROM " + this.tableName + " WHERE " + this.col.IS_LOGGED + " = 1 AND (" + this.col.EMAIL + " = 'your@email.com' OR " + this.col.EMAIL + " IS NULL);";

    try {
      Connection con = this.getConnection();
      Throwable var4 = null;

      try {
        Statement st = con.createStatement();
        Throwable var6 = null;

        try {
          ResultSet rs = st.executeQuery(sql);
          Throwable var8 = null;

          try {
            while(rs.next()) {
              players.add(rs.getString(1));
            }
          } catch (Throwable var55) {
            var8 = var55;
            throw var55;
          } finally {
            if (rs != null) {
              if (var8 != null) {
                try {
                  rs.close();
                } catch (Throwable var54) {
                  var8.addSuppressed(var54);
                }
              } else {
                rs.close();
              }
            }

          }
        } catch (Throwable var57) {
          var6 = var57;
          throw var57;
        } finally {
          if (st != null) {
            if (var6 != null) {
              try {
                st.close();
              } catch (Throwable var53) {
                var6.addSuppressed(var53);
              }
            } else {
              st.close();
            }
          }

        }
      } catch (Throwable var59) {
        var4 = var59;
        throw var59;
      } finally {
        if (con != null) {
          if (var4 != null) {
            try {
              con.close();
            } catch (Throwable var52) {
              var4.addSuppressed(var52);
            }
          } else {
            con.close();
          }
        }

      }
    } catch (SQLException var61) {
      SqlDataSourceUtils.logSqlException(var61);
    }

    return players;
  }

  private PlayerAuth buildAuthFromResultSet(ResultSet row) throws SQLException {
    String salt = this.col.SALT.isEmpty() ? null : row.getString(this.col.SALT);
    int group = this.col.GROUP.isEmpty() ? -1 : row.getInt(this.col.GROUP);
    return PlayerAuth.builder().name(row.getString(this.col.NAME)).realName(row.getString(this.col.REAL_NAME)).password(row.getString(this.col.PASSWORD), salt).lastLogin(SqlDataSourceUtils.getNullableLong(row, this.col.LAST_LOGIN)).lastIp(row.getString(this.col.LAST_IP)).email(row.getString(this.col.EMAIL)).registrationDate(row.getLong(this.col.REGISTRATION_DATE)).registrationIp(row.getString(this.col.REGISTRATION_IP)).groupId(group).locWorld(row.getString(this.col.LASTLOC_WORLD)).locX(row.getDouble(this.col.LASTLOC_X)).locY(row.getDouble(this.col.LASTLOC_Y)).locZ(row.getDouble(this.col.LASTLOC_Z)).locYaw(row.getFloat(this.col.LASTLOC_YAW)).locPitch(row.getFloat(this.col.LASTLOC_PITCH)).build();
  }
}
