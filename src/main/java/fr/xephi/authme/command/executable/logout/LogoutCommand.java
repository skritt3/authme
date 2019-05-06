package fr.xephi.authme.command.executable.logout;

import fr.xephi.authme.command.PlayerCommand;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.process.Management;
import java.util.List;
import org.bukkit.entity.Player;

public class LogoutCommand
  extends PlayerCommand
{
  @Inject
  private Management management;
  
  public void runCommand(Player player, List<String> arguments)
  {
    this.management.performLogout(player);
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\command\executable\logout\LogoutCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */