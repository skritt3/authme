package fr.xephi.authme.command.executable.authme;

import fr.xephi.authme.command.ExecutableCommand;
import fr.xephi.authme.data.auth.PlayerAuth;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.message.MessageKey;
import fr.xephi.authme.service.BukkitService;
import fr.xephi.authme.service.CommonService;
import java.util.List;
import org.bukkit.command.CommandSender;

public class AccountsCommand
  implements ExecutableCommand
{
  @Inject
  private DataSource dataSource;
  @Inject
  private BukkitService bukkitService;
  @Inject
  private CommonService commonService;
  
  public void executeCommand(final CommandSender sender, List<String> arguments)
  {
    final String playerName = arguments.isEmpty() ? sender.getName() : (String)arguments.get(0);
    if (playerName.contains(".")) {
      this.bukkitService.runTaskAsynchronously(new Runnable()
      {
        public void run()
        {
          List<String> accountList = AccountsCommand.this.dataSource.getAllAuthsByIp(playerName);
          if (accountList.isEmpty()) {
            sender.sendMessage("[AuthMe] This IP does not exist in the database.");
          } else if (accountList.size() == 1) {
            sender.sendMessage("[AuthMe] " + playerName + " is a single account player");
          } else {
            AccountsCommand.outputAccountsList(sender, playerName, accountList);
          }
        }
      });
    } else {
      this.bukkitService.runTaskAsynchronously(new Runnable()
      {
        public void run()
        {
          PlayerAuth auth = AccountsCommand.this.dataSource.getAuth(playerName.toLowerCase());
          if (auth == null)
          {
            AccountsCommand.this.commonService.send(sender, MessageKey.UNKNOWN_USER);
            return;
          }
          if (auth.getLastIp() == null)
          {
            sender.sendMessage("No known last IP address for player");
            return;
          }
          List<String> accountList = AccountsCommand.this.dataSource.getAllAuthsByIp(auth.getLastIp());
          if (accountList.isEmpty()) {
            AccountsCommand.this.commonService.send(sender, MessageKey.UNKNOWN_USER);
          } else if (accountList.size() == 1) {
            sender.sendMessage("[AuthMe] " + playerName + " is a single account player");
          } else {
            AccountsCommand.outputAccountsList(sender, playerName, accountList);
          }
        }
      });
    }
  }
  
  private static void outputAccountsList(CommandSender sender, String playerName, List<String> accountList)
  {
    sender.sendMessage("[AuthMe] " + playerName + " has " + accountList.size() + " accounts.");
    String message = "[AuthMe] " + String.join(", ", accountList) + ".";
    sender.sendMessage(message);
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\command\executable\authme\AccountsCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */