package fr.xephi.authme.process.register;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.data.limbo.LimboService;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.message.MessageKey;
import fr.xephi.authme.process.SynchronousProcess;
import fr.xephi.authme.service.CommonService;
import fr.xephi.authme.service.bungeecord.BungeeSender;
import fr.xephi.authme.settings.commandconfig.CommandManager;
import fr.xephi.authme.settings.properties.EmailSettings;
import fr.xephi.authme.settings.properties.RegistrationSettings;
import fr.xephi.authme.util.PlayerUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class ProcessSyncPasswordRegister
  implements SynchronousProcess
{
  @Inject
  private BungeeSender bungeeSender;
  @Inject
  private CommonService service;
  @Inject
  private LimboService limboService;
  @Inject
  private CommandManager commandManager;
  
  private void requestLogin(Player player)
  {
    this.limboService.replaceTasksAfterRegistration(player);
    if ((player.isInsideVehicle()) && (player.getVehicle() != null)) {
      player.getVehicle().eject();
    }
  }
  
  public void processPasswordRegister(Player player)
  {
    this.service.send(player, MessageKey.REGISTER_SUCCESS);
    if (!((String)this.service.getProperty(EmailSettings.MAIL_ACCOUNT)).isEmpty()) {
      this.service.send(player, MessageKey.ADD_EMAIL_MESSAGE);
    }
    player.saveData();
    ConsoleLogger.fine(player.getName() + " registered " + PlayerUtils.getPlayerIp(player));
    if (((Boolean)this.service.getProperty(RegistrationSettings.FORCE_KICK_AFTER_REGISTER)).booleanValue())
    {
      player.kickPlayer(this.service.retrieveSingleMessage(MessageKey.REGISTER_SUCCESS));
      return;
    }
    this.commandManager.runCommandsOnRegister(player);
    if (((Boolean)this.service.getProperty(RegistrationSettings.FORCE_LOGIN_AFTER_REGISTER)).booleanValue())
    {
      requestLogin(player);
      return;
    }
    this.bungeeSender.connectPlayerOnLogin(player);
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\process\register\ProcessSyncPasswordRegister.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */