package fr.xephi.authme.command.executable.authme;

import fr.xephi.authme.command.PlayerCommand;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.settings.SpawnLoader;
import java.util.List;
import org.bukkit.entity.Player;

public class FirstSpawnCommand
  extends PlayerCommand
{
  @Inject
  private SpawnLoader spawnLoader;
  
  public void runCommand(Player player, List<String> arguments)
  {
    if (this.spawnLoader.getFirstSpawn() == null) {
      player.sendMessage("[AuthMe] First spawn has failed, please try to define the first spawn");
    } else {
      player.teleport(this.spawnLoader.getFirstSpawn());
    }
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\command\executable\authme\FirstSpawnCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */