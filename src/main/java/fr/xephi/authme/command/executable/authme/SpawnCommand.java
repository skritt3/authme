package fr.xephi.authme.command.executable.authme;

import fr.xephi.authme.command.PlayerCommand;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.settings.SpawnLoader;
import java.util.List;
import org.bukkit.entity.Player;

public class SpawnCommand
  extends PlayerCommand
{
  @Inject
  private SpawnLoader spawnLoader;
  
  public void runCommand(Player player, List<String> arguments)
  {
    if (this.spawnLoader.getSpawn() == null) {
      player.sendMessage("[AuthMe] Spawn has failed, please try to define the spawn");
    } else {
      player.teleport(this.spawnLoader.getSpawn());
    }
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\command\executable\authme\SpawnCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */