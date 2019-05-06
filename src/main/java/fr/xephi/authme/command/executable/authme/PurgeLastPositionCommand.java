package fr.xephi.authme.command.executable.authme;

import fr.xephi.authme.command.ExecutableCommand;
import fr.xephi.authme.data.auth.PlayerAuth;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.message.MessageKey;
import fr.xephi.authme.service.CommonService;
import fr.xephi.authme.service.bungeecord.BungeeSender;
import java.util.List;
import org.bukkit.command.CommandSender;

public class PurgeLastPositionCommand
  implements ExecutableCommand
{
  @Inject
  private DataSource dataSource;
  @Inject
  private CommonService commonService;
  @Inject
  private BungeeSender bungeeSender;
  
  public void executeCommand(CommandSender sender, List<String> arguments)
  {
    String playerName = arguments.isEmpty() ? sender.getName() : (String)arguments.get(0);
    if ("*".equals(playerName))
    {
      for (PlayerAuth auth : this.dataSource.getAllAuths())
      {
        resetLastPosition(auth);
        this.dataSource.updateQuitLoc(auth);
        this.bungeeSender.sendAuthMeBungeecordMessage("refresh.quitloc", playerName);
      }
      sender.sendMessage("All players last position locations are now reset");
    }
    else
    {
      PlayerAuth auth = this.dataSource.getAuth(playerName);
      if (auth == null)
      {
        this.commonService.send(sender, MessageKey.UNKNOWN_USER);
        return;
      }
      resetLastPosition(auth);
      this.dataSource.updateQuitLoc(auth);
      this.bungeeSender.sendAuthMeBungeecordMessage("refresh.quitloc", playerName);
      sender.sendMessage(playerName + "'s last position location is now reset");
    }
  }
  
  private static void resetLastPosition(PlayerAuth auth)
  {
    auth.setQuitLocX(0.0D);
    auth.setQuitLocY(0.0D);
    auth.setQuitLocZ(0.0D);
    auth.setWorld("world");
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\command\executable\authme\PurgeLastPositionCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */