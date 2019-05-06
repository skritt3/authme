//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package fr.xephi.authme.datasource;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.data.auth.PlayerAuth;
import fr.xephi.authme.data.auth.PlayerAuth.Builder;
import fr.xephi.authme.security.crypts.HashedPassword;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/** @deprecated */
@Deprecated
public class FlatFile implements DataSource {
  private final File source;

  public FlatFile(File source) throws IOException {
    this.source = source;
    if (!source.exists() && !source.createNewFile()) {
      throw new IOException("Could not create file '" + source.getPath() + "'");
    }
  }

  public void reload() {
    throw new UnsupportedOperationException("Flatfile no longer supported");
  }

  @Override
  public void invalidateCache(String playerName) {

  }

  @Override
  public void refreshCache(String playerName) {

  }

  public synchronized boolean isAuthAvailable(String user) {
    try {
      BufferedReader br = new BufferedReader(new FileReader(this.source));
      Throwable var3 = null;

      while(true) {
        boolean var6;
        try {
          String line;
          if ((line = br.readLine()) == null) {
            return false;
          }

          String[] args = line.split(":");
          if (args.length <= 1 || !args[0].equalsIgnoreCase(user)) {
            continue;
          }

          var6 = true;
        } catch (Throwable var17) {
          var3 = var17;
          throw var17;
        } finally {
          if (br != null) {
            if (var3 != null) {
              try {
                br.close();
              } catch (Throwable var16) {
                var3.addSuppressed(var16);
              }
            } else {
              br.close();
            }
          }

        }

        return var6;
      }
    } catch (IOException var19) {
      ConsoleLogger.warning(var19.getMessage());
      return false;
    }
  }

  public HashedPassword getPassword(String user) {
    PlayerAuth auth = this.getAuth(user);
    return auth != null ? auth.getPassword() : null;
  }

  public synchronized boolean saveAuth(PlayerAuth auth) {
    if (this.isAuthAvailable(auth.getNickname())) {
      return false;
    } else {
      try {
        BufferedWriter bw = new BufferedWriter(new FileWriter(this.source, true));
        Throwable var3 = null;

        try {
          bw.write(auth.getNickname() + ":" + auth.getPassword().getHash() + ":" + auth.getLastIp() + ":" + auth.getLastLogin() + ":" + auth.getQuitLocX() + ":" + auth.getQuitLocY() + ":" + auth.getQuitLocZ() + ":" + auth.getWorld() + ":" + auth.getEmail() + "\n");
        } catch (Throwable var13) {
          var3 = var13;
          throw var13;
        } finally {
          if (bw != null) {
            if (var3 != null) {
              try {
                bw.close();
              } catch (Throwable var12) {
                var3.addSuppressed(var12);
              }
            } else {
              bw.close();
            }
          }

        }

        return true;
      } catch (IOException var15) {
        ConsoleLogger.warning(var15.getMessage());
        return false;
      }
    }
  }

  public synchronized boolean updatePassword(PlayerAuth auth) {
    return this.updatePassword(auth.getNickname(), auth.getPassword());
  }

  public boolean updatePassword(String user, HashedPassword password) {
    user = user.toLowerCase();
    if (!this.isAuthAvailable(user)) {
      return false;
    } else {
      PlayerAuth newAuth = null;

      try {
        BufferedReader br = new BufferedReader(new FileReader(this.source));
        Throwable var5 = null;

        try {
          String line;
          try {
            while((line = br.readLine()) != null) {
              String[] args = line.split(":");
              if (args[0].equals(user)) {
                newAuth = buildAuthFromArray(args);
                if (newAuth != null) {
                  newAuth.setPassword(password);
                }
                break;
              }
            }
          } catch (Throwable var16) {
            var5 = var16;
            throw var16;
          }
        } finally {
          if (br != null) {
            if (var5 != null) {
              try {
                br.close();
              } catch (Throwable var15) {
                var5.addSuppressed(var15);
              }
            } else {
              br.close();
            }
          }

        }
      } catch (IOException var18) {
        ConsoleLogger.warning(var18.getMessage());
        return false;
      }

      if (newAuth != null) {
        this.removeAuth(user);
        this.saveAuth(newAuth);
      }

      return true;
    }
  }

  public boolean updateSession(PlayerAuth auth) {
    if (!this.isAuthAvailable(auth.getNickname())) {
      return false;
    } else {
      PlayerAuth newAuth = null;

      try {
        BufferedReader br = new BufferedReader(new FileReader(this.source));
        Throwable var4 = null;

        try {
          String line;
          try {
            while((line = br.readLine()) != null) {
              String[] args = line.split(":");
              if (args[0].equalsIgnoreCase(auth.getNickname())) {
                newAuth = buildAuthFromArray(args);
                if (newAuth != null) {
                  newAuth.setLastLogin(auth.getLastLogin());
                  newAuth.setLastIp(auth.getLastIp());
                }
                break;
              }
            }
          } catch (Throwable var15) {
            var4 = var15;
            throw var15;
          }
        } finally {
          if (br != null) {
            if (var4 != null) {
              try {
                br.close();
              } catch (Throwable var14) {
                var4.addSuppressed(var14);
              }
            } else {
              br.close();
            }
          }

        }
      } catch (IOException var17) {
        ConsoleLogger.warning(var17.getMessage());
        return false;
      }

      if (newAuth != null) {
        this.removeAuth(auth.getNickname());
        this.saveAuth(newAuth);
      }

      return true;
    }
  }

  public boolean updateQuitLoc(PlayerAuth auth) {
    if (!this.isAuthAvailable(auth.getNickname())) {
      return false;
    } else {
      PlayerAuth newAuth = null;

      try {
        BufferedReader br = new BufferedReader(new FileReader(this.source));
        Throwable var4 = null;

        try {
          String line;
          try {
            while((line = br.readLine()) != null) {
              String[] args = line.split(":");
              if (args[0].equalsIgnoreCase(auth.getNickname())) {
                newAuth = buildAuthFromArray(args);
                if (newAuth != null) {
                  newAuth.setQuitLocX(auth.getQuitLocX());
                  newAuth.setQuitLocY(auth.getQuitLocY());
                  newAuth.setQuitLocZ(auth.getQuitLocZ());
                  newAuth.setWorld(auth.getWorld());
                  newAuth.setEmail(auth.getEmail());
                }
                break;
              }
            }
          } catch (Throwable var15) {
            var4 = var15;
            throw var15;
          }
        } finally {
          if (br != null) {
            if (var4 != null) {
              try {
                br.close();
              } catch (Throwable var14) {
                var4.addSuppressed(var14);
              }
            } else {
              br.close();
            }
          }

        }
      } catch (IOException var17) {
        ConsoleLogger.warning(var17.getMessage());
        return false;
      }

      if (newAuth != null) {
        this.removeAuth(auth.getNickname());
        this.saveAuth(newAuth);
      }

      return true;
    }
  }

  public Set<String> getRecordsToPurge(long until) {
    throw new UnsupportedOperationException("Flat file no longer supported");
  }

  public void purgeRecords(Collection<String> toPurge) {
    throw new UnsupportedOperationException("Flat file no longer supported");
  }

  public synchronized boolean removeAuth(String user) {
    if (!this.isAuthAvailable(user)) {
      return false;
    } else {
      ArrayList lines = new ArrayList();

      try {
        BufferedReader br = new BufferedReader(new FileReader(this.source));
        Throwable var4 = null;

        try {
          String line;
          while((line = br.readLine()) != null) {
            String[] args = line.split(":");
            if (args.length > 1 && !args[0].equals(user)) {
              lines.add(line);
            }
          }

          BufferedWriter bw = new BufferedWriter(new FileWriter(this.source));
          Throwable var7 = null;

          try {
            Iterator var8 = lines.iterator();

            while(var8.hasNext()) {
              String l = (String)var8.next();
              bw.write(l + "\n");
            }
          } catch (Throwable var33) {
            var7 = var33;
            throw var33;
          } finally {
            if (bw != null) {
              if (var7 != null) {
                try {
                  bw.close();
                } catch (Throwable var32) {
                  var7.addSuppressed(var32);
                }
              } else {
                bw.close();
              }
            }

          }
        } catch (Throwable var35) {
          var4 = var35;
          throw var35;
        } finally {
          if (br != null) {
            if (var4 != null) {
              try {
                br.close();
              } catch (Throwable var31) {
                var4.addSuppressed(var31);
              }
            } else {
              br.close();
            }
          }

        }

        return true;
      } catch (IOException var37) {
        ConsoleLogger.warning(var37.getMessage());
        return false;
      }
    }
  }

  public synchronized PlayerAuth getAuth(String user) {
    try {
      BufferedReader br = new BufferedReader(new FileReader(this.source));
      Throwable var3 = null;

      try {
        String line;
        try {
          while((line = br.readLine()) != null) {
            String[] args = line.split(":");
            if (args[0].equalsIgnoreCase(user)) {
              PlayerAuth var6 = buildAuthFromArray(args);
              return var6;
            }
          }
        } catch (Throwable var17) {
          var3 = var17;
          throw var17;
        }
      } finally {
        if (br != null) {
          if (var3 != null) {
            try {
              br.close();
            } catch (Throwable var16) {
              var3.addSuppressed(var16);
            }
          } else {
            br.close();
          }
        }

      }

      return null;
    } catch (IOException var19) {
      ConsoleLogger.warning(var19.getMessage());
      return null;
    }
  }

  public void closeConnection() {
  }

  public boolean updateEmail(PlayerAuth auth) {
    if (!this.isAuthAvailable(auth.getNickname())) {
      return false;
    } else {
      PlayerAuth newAuth = null;

      try {
        BufferedReader br = new BufferedReader(new FileReader(this.source));
        Throwable var4 = null;

        try {
          String line;
          try {
            while((line = br.readLine()) != null) {
              String[] args = line.split(":");
              if (args[0].equals(auth.getNickname())) {
                newAuth = buildAuthFromArray(args);
                if (newAuth != null) {
                  newAuth.setEmail(auth.getEmail());
                }
                break;
              }
            }
          } catch (Throwable var15) {
            var4 = var15;
            throw var15;
          }
        } finally {
          if (br != null) {
            if (var4 != null) {
              try {
                br.close();
              } catch (Throwable var14) {
                var4.addSuppressed(var14);
              }
            } else {
              br.close();
            }
          }

        }
      } catch (IOException var17) {
        ConsoleLogger.warning(var17.getMessage());
        return false;
      }

      if (newAuth != null) {
        this.removeAuth(auth.getNickname());
        this.saveAuth(newAuth);
      }

      return true;
    }
  }

  public List<String> getAllAuthsByIp(String ip) {
    ArrayList countIp = new ArrayList();

    try {
      BufferedReader br = new BufferedReader(new FileReader(this.source));
      Throwable var4 = null;

      ArrayList var19;
      try {
        String line;
        while((line = br.readLine()) != null) {
          String[] args = line.split(":");
          if (args.length > 3 && args[2].equals(ip)) {
            countIp.add(args[0]);
          }
        }

        var19 = countIp;
      } catch (Throwable var16) {
        var4 = var16;
        throw var16;
      } finally {
        if (br != null) {
          if (var4 != null) {
            try {
              br.close();
            } catch (Throwable var15) {
              var4.addSuppressed(var15);
            }
          } else {
            br.close();
          }
        }

      }

      return var19;
    } catch (IOException var18) {
      ConsoleLogger.warning(var18.getMessage());
      return new ArrayList();
    }
  }

  public int countAuthsByEmail(String email) {
    int countEmail = 0;

    try {
      BufferedReader br = new BufferedReader(new FileReader(this.source));
      Throwable var4 = null;

      int var19;
      try {
        String line;
        while((line = br.readLine()) != null) {
          String[] args = line.split(":");
          if (args.length > 8 && args[8].equals(email)) {
            ++countEmail;
          }
        }

        var19 = countEmail;
      } catch (Throwable var16) {
        var4 = var16;
        throw var16;
      } finally {
        if (br != null) {
          if (var4 != null) {
            try {
              br.close();
            } catch (Throwable var15) {
              var4.addSuppressed(var15);
            }
          } else {
            br.close();
          }
        }

      }

      return var19;
    } catch (IOException var18) {
      ConsoleLogger.warning(var18.getMessage());
      return 0;
    }
  }

  public DataSourceType getType() {
    return DataSourceType.FILE;
  }

  public boolean isLogged(String user) {
    throw new UnsupportedOperationException("Flat file no longer supported");
  }

  public void setLogged(String user) {
  }

  public void setUnlogged(String user) {
  }

  public boolean hasSession(String user) {
    throw new UnsupportedOperationException("Flat file no longer supported");
  }

  public void grantSession(String user) {
  }

  public void revokeSession(String user) {
  }

  public void purgeLogged() {
  }

  public int getAccountsRegistered() {
    int result = 0;

    try {
      BufferedReader br = new BufferedReader(new FileReader(this.source));
      Throwable var3 = null;

      try {
        while(br.readLine() != null) {
          ++result;
        }
      } catch (Throwable var13) {
        var3 = var13;
        throw var13;
      } finally {
        if (br != null) {
          if (var3 != null) {
            try {
              br.close();
            } catch (Throwable var12) {
              var3.addSuppressed(var12);
            }
          } else {
            br.close();
          }
        }

      }

      return result;
    } catch (Exception var15) {
      ConsoleLogger.warning(var15.getMessage());
      return result;
    }
  }

  public boolean updateRealName(String user, String realName) {
    throw new UnsupportedOperationException("Flat file no longer supported");
  }

  public DataSourceResult<String> getEmail(String user) {
    throw new UnsupportedOperationException("Flat file no longer supported");
  }

  public List<PlayerAuth> getAllAuths() {
    ArrayList auths = new ArrayList();

    try {
      BufferedReader br = new BufferedReader(new FileReader(this.source));
      Throwable var3 = null;

      try {
        String line;
        try {
          while((line = br.readLine()) != null) {
            String[] args = line.split(":");
            PlayerAuth auth = buildAuthFromArray(args);
            if (auth != null) {
              auths.add(auth);
            }
          }
        } catch (Throwable var15) {
          var3 = var15;
          throw var15;
        }
      } finally {
        if (br != null) {
          if (var3 != null) {
            try {
              br.close();
            } catch (Throwable var14) {
              var3.addSuppressed(var14);
            }
          } else {
            br.close();
          }
        }

      }
    } catch (IOException var17) {
      ConsoleLogger.logException("Error while getting auths from flatfile:", var17);
    }

    return auths;
  }

  public List<String> getLoggedPlayersWithEmptyMail() {
    throw new UnsupportedOperationException("Flat file no longer supported");
  }

  private static PlayerAuth buildAuthFromArray(String[] args) {
    if (args.length >= 2 && args.length <= 9 && args.length != 5 && args.length != 6) {
      Builder builder = PlayerAuth.builder().name(args[0]).realName(args[0]).password(args[1], (String)null);
      if (args.length >= 3) {
        builder.lastIp(args[2]);
      }

      if (args.length >= 4) {
        builder.lastLogin(parseNullableLong(args[3]));
      }

      if (args.length >= 7) {
        builder.locX(Double.parseDouble(args[4])).locY(Double.parseDouble(args[5])).locZ(Double.parseDouble(args[6]));
      }

      if (args.length >= 8) {
        builder.locWorld(args[7]);
      }

      if (args.length >= 9) {
        builder.email(args[8]);
      }

      return builder.build();
    } else {
      return null;
    }
  }

  private static Long parseNullableLong(String str) {
    return "null".equals(str) ? null : Long.parseLong(str);
  }
}
