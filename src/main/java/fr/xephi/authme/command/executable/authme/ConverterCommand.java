//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package fr.xephi.authme.command.executable.authme;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.command.ExecutableCommand;
import fr.xephi.authme.datasource.converter.Converter;
import fr.xephi.authme.datasource.converter.CrazyLoginConverter;
import fr.xephi.authme.datasource.converter.LoginSecurityConverter;
import fr.xephi.authme.datasource.converter.MySqlToSqlite;
import fr.xephi.authme.datasource.converter.RakamakConverter;
import fr.xephi.authme.datasource.converter.RoyalAuthConverter;
import fr.xephi.authme.datasource.converter.SqliteToSql;
import fr.xephi.authme.datasource.converter.VAuthConverter;
import fr.xephi.authme.datasource.converter.XAuthConverter;
import fr.xephi.authme.initialization.factory.Factory;
import fr.xephi.authme.libs.google.common.annotations.VisibleForTesting;
import fr.xephi.authme.libs.google.common.collect.ImmutableSortedMap;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.message.MessageKey;
import fr.xephi.authme.service.BukkitService;
import fr.xephi.authme.service.CommonService;
import java.util.List;
import java.util.Map;
import org.bukkit.command.CommandSender;

public class ConverterCommand implements ExecutableCommand {
  @VisibleForTesting
  static final Map<String, Class<? extends Converter>> CONVERTERS = getConverters();
  @Inject
  private CommonService commonService;
  @Inject
  private BukkitService bukkitService;
  @Inject
  private Factory<Converter> converterFactory;

  public ConverterCommand() {
  }

  public void executeCommand(final CommandSender sender, List<String> arguments) {
    Class<? extends Converter> converterClass = getConverterClassFromArgs(arguments);
    if (converterClass == null) {
      sender.sendMessage("Converters: " + String.join(", ", CONVERTERS.keySet()));
    } else {
      final Converter converter = (Converter)this.converterFactory.newInstance(converterClass);
      this.bukkitService.runTaskAsynchronously(new Runnable() {
        public void run() {
          try {
            converter.execute(sender);
          } catch (Exception var2) {
            ConverterCommand.this.commonService.send(sender, MessageKey.ERROR);
            ConsoleLogger.logException("Error during conversion:", var2);
          }

        }
      });
      sender.sendMessage("[AuthMe] Successfully started " + (String)arguments.get(0));
    }
  }

  private static Class<? extends Converter> getConverterClassFromArgs(List<String> arguments) {
    return arguments.isEmpty() ? null : (Class)CONVERTERS.get(((String)arguments.get(0)).toLowerCase());
  }

  private static Map<String, Class<? extends Converter>> getConverters() {
    return (Map)ImmutableSortedMap.naturalOrder().put("xauth", XAuthConverter.class).put("crazylogin", CrazyLoginConverter.class).put("rakamak", RakamakConverter.class).put("royalauth", RoyalAuthConverter.class).put("vauth", VAuthConverter.class).put("sqlitetosql", SqliteToSql.class).put("mysqltosqlite", MySqlToSqlite.class).put("loginsecurity", LoginSecurityConverter.class).build();
  }
}
