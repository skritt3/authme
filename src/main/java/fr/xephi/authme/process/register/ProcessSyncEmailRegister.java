package fr.xephi.authme.process.register;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.data.limbo.LimboService;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.message.MessageKey;
import fr.xephi.authme.process.SynchronousProcess;
import fr.xephi.authme.service.CommonService;
import fr.xephi.authme.util.PlayerUtils;
import org.bukkit.entity.Player;

public class ProcessSyncEmailRegister
  implements SynchronousProcess
{
  @Inject
  private CommonService service;
  @Inject
  private LimboService limboService;
  
  public void processEmailRegister(Player player)
  {
    this.service.send(player, MessageKey.ACCOUNT_NOT_ACTIVATED);
    this.limboService.replaceTasksAfterRegistration(player);
    
    player.saveData();
    ConsoleLogger.fine(player.getName() + " registered " + PlayerUtils.getPlayerIp(player));
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\process\register\ProcessSyncEmailRegister.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */