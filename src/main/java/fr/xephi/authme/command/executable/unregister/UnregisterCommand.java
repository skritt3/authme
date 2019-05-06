package fr.xephi.authme.command.executable.unregister;

import fr.xephi.authme.command.PlayerCommand;
import fr.xephi.authme.data.VerificationCodeManager;
import fr.xephi.authme.data.auth.PlayerCache;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.message.MessageKey;
import fr.xephi.authme.process.Management;
import fr.xephi.authme.service.CommonService;
import java.util.List;
import org.bukkit.entity.Player;

public class UnregisterCommand
  extends PlayerCommand
{
  @Inject
  private Management management;
  @Inject
  private CommonService commonService;
  @Inject
  private PlayerCache playerCache;
  @Inject
  private VerificationCodeManager codeManager;
  
  public void runCommand(Player player, List<String> arguments)
  {
    String playerPass = (String)arguments.get(0);
    String playerName = player.getName();
    if (!this.playerCache.isAuthenticated(playerName))
    {
      this.commonService.send(player, MessageKey.NOT_LOGGED_IN);
      return;
    }
    if (this.codeManager.isVerificationRequired(player))
    {
      this.codeManager.codeExistOrGenerateNew(playerName);
      this.commonService.send(player, MessageKey.VERIFICATION_CODE_REQUIRED);
      return;
    }
    this.management.performUnregister(player, playerPass);
  }
  
  public MessageKey getArgumentsMismatchMessage()
  {
    return MessageKey.USAGE_UNREGISTER;
  }
  
  protected String getAlternativeCommand()
  {
    return "/authme unregister <player>";
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\command\executabl\\unregister\UnregisterCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */