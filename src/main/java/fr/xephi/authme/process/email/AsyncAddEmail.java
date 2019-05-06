package fr.xephi.authme.process.email;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.data.auth.PlayerAuth;
import fr.xephi.authme.data.auth.PlayerCache;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.message.MessageKey;
import fr.xephi.authme.process.AsynchronousProcess;
import fr.xephi.authme.service.CommonService;
import fr.xephi.authme.service.ValidationService;
import fr.xephi.authme.service.bungeecord.BungeeSender;
import fr.xephi.authme.util.Utils;
import org.bukkit.entity.Player;

public class AsyncAddEmail
  implements AsynchronousProcess
{
  @Inject
  private CommonService service;
  @Inject
  private DataSource dataSource;
  @Inject
  private PlayerCache playerCache;
  @Inject
  private ValidationService validationService;
  @Inject
  private BungeeSender bungeeSender;
  
  public void addEmail(Player player, String email)
  {
    String playerName = player.getName().toLowerCase();
    if (this.playerCache.isAuthenticated(playerName))
    {
      PlayerAuth auth = this.playerCache.getAuth(playerName);
      String currentEmail = auth.getEmail();
      if (!Utils.isEmailEmpty(currentEmail))
      {
        this.service.send(player, MessageKey.USAGE_CHANGE_EMAIL);
      }
      else if (!this.validationService.validateEmail(email))
      {
        this.service.send(player, MessageKey.INVALID_EMAIL);
      }
      else if (!this.validationService.isEmailFreeForRegistration(email, player))
      {
        this.service.send(player, MessageKey.EMAIL_ALREADY_USED_ERROR);
      }
      else
      {
        auth.setEmail(email);
        if (this.dataSource.updateEmail(auth))
        {
          this.playerCache.updatePlayer(auth);
          this.bungeeSender.sendAuthMeBungeecordMessage("refresh.email", playerName);
          this.service.send(player, MessageKey.EMAIL_ADDED_SUCCESS);
        }
        else
        {
          ConsoleLogger.warning("Could not save email for player '" + player + "'");
          this.service.send(player, MessageKey.ERROR);
        }
      }
    }
    else
    {
      sendUnloggedMessage(player);
    }
  }
  
  private void sendUnloggedMessage(Player player)
  {
    if (this.dataSource.isAuthAvailable(player.getName())) {
      this.service.send(player, MessageKey.LOGIN_MESSAGE);
    } else {
      this.service.send(player, MessageKey.REGISTER_MESSAGE);
    }
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\process\email\AsyncAddEmail.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */