package fr.xephi.authme.process.email;

import fr.xephi.authme.data.auth.PlayerAuth;
import fr.xephi.authme.data.auth.PlayerCache;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.message.MessageKey;
import fr.xephi.authme.process.AsynchronousProcess;
import fr.xephi.authme.service.CommonService;
import fr.xephi.authme.service.ValidationService;
import fr.xephi.authme.service.bungeecord.BungeeSender;
import org.bukkit.entity.Player;

public class AsyncChangeEmail
  implements AsynchronousProcess
{
  @Inject
  private CommonService service;
  @Inject
  private PlayerCache playerCache;
  @Inject
  private DataSource dataSource;
  @Inject
  private ValidationService validationService;
  @Inject
  private BungeeSender bungeeSender;
  
  public void changeEmail(Player player, String oldEmail, String newEmail)
  {
    String playerName = player.getName().toLowerCase();
    if (this.playerCache.isAuthenticated(playerName))
    {
      PlayerAuth auth = this.playerCache.getAuth(playerName);
      String currentEmail = auth.getEmail();
      if (currentEmail == null) {
        this.service.send(player, MessageKey.USAGE_ADD_EMAIL);
      } else if ((newEmail == null) || (!this.validationService.validateEmail(newEmail))) {
        this.service.send(player, MessageKey.INVALID_NEW_EMAIL);
      } else if (!oldEmail.equalsIgnoreCase(currentEmail)) {
        this.service.send(player, MessageKey.INVALID_OLD_EMAIL);
      } else if (!this.validationService.isEmailFreeForRegistration(newEmail, player)) {
        this.service.send(player, MessageKey.EMAIL_ALREADY_USED_ERROR);
      } else {
        saveNewEmail(auth, player, newEmail);
      }
    }
    else
    {
      outputUnloggedMessage(player);
    }
  }
  
  private void saveNewEmail(PlayerAuth auth, Player player, String newEmail)
  {
    auth.setEmail(newEmail);
    if (this.dataSource.updateEmail(auth))
    {
      this.playerCache.updatePlayer(auth);
      this.bungeeSender.sendAuthMeBungeecordMessage("refresh.email", player.getName());
      this.service.send(player, MessageKey.EMAIL_CHANGED_SUCCESS);
    }
    else
    {
      this.service.send(player, MessageKey.ERROR);
    }
  }
  
  private void outputUnloggedMessage(Player player)
  {
    if (this.dataSource.isAuthAvailable(player.getName())) {
      this.service.send(player, MessageKey.LOGIN_MESSAGE);
    } else {
      this.service.send(player, MessageKey.REGISTER_MESSAGE);
    }
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\process\email\AsyncChangeEmail.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */