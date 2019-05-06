//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package fr.xephi.authme.datasource.converter;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.data.auth.PlayerAuth;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.initialization.DataFolder;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.util.FileUtils;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

public class VAuthConverter implements Converter {
  private final DataSource dataSource;
  private final File vAuthPasswordsFile;

  @Inject
  VAuthConverter(@DataFolder File dataFolder, DataSource dataSource) {
    this.vAuthPasswordsFile = new File(dataFolder.getParent(), FileUtils.makePath(new String[]{"vAuth", "passwords.yml"}));
    this.dataSource = dataSource;
  }

  public void execute(CommandSender sender) {
    try {
      Scanner scanner = new Scanner(this.vAuthPasswordsFile);
      Throwable var3 = null;

      try {
        while(true) {
          PlayerAuth auth;
          while(true) {
            if (!scanner.hasNextLine()) {
              return;
            }

            String line = scanner.nextLine();
            String name = line.split(": ")[0];
            String password = line.split(": ")[1];
            if (isUuidInstance(password)) {
              String pname;
              try {
                pname = Bukkit.getOfflinePlayer(UUID.fromString(name)).getName();
              } catch (NoSuchMethodError | Exception var19) {
                pname = this.getName(UUID.fromString(name));
              }

              if (pname == null) {
                continue;
              }

              auth = PlayerAuth.builder().name(pname.toLowerCase()).realName(pname).password(password, (String)null).build();
              break;
            }

            auth = PlayerAuth.builder().name(name.toLowerCase()).realName(name).password(password, (String)null).build();
            break;
          }

          this.dataSource.saveAuth(auth);
        }
      } catch (Throwable var20) {
        var3 = var20;
        throw var20;
      } finally {
        if (scanner != null) {
          if (var3 != null) {
            try {
              scanner.close();
            } catch (Throwable var18) {
              var3.addSuppressed(var18);
            }
          } else {
            scanner.close();
          }
        }

      }
    } catch (IOException var22) {
      ConsoleLogger.logException("Error while trying to import some vAuth data", var22);
    }
  }

  private static boolean isUuidInstance(String s) {
    return s.length() > 8 && s.charAt(8) == '-';
  }

  private String getName(UUID uuid) {
    OfflinePlayer[] var2 = Bukkit.getOfflinePlayers();
    int var3 = var2.length;

    for(int var4 = 0; var4 < var3; ++var4) {
      OfflinePlayer op = var2[var4];
      if (op.getUniqueId().compareTo(uuid) == 0) {
        return op.getName();
      }
    }

    return null;
  }
}
