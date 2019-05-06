//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package fr.xephi.authme.command.executable.captcha;

import fr.xephi.authme.command.PlayerCommand;
import fr.xephi.authme.data.CaptchaManager;
import fr.xephi.authme.data.auth.PlayerCache;
import fr.xephi.authme.data.limbo.LimboService;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.message.MessageKey;
import fr.xephi.authme.service.CommonService;
import java.util.List;
import org.bukkit.entity.Player;

public class CaptchaCommand extends PlayerCommand {
  @Inject
  private PlayerCache playerCache;
  @Inject
  private CaptchaManager captchaManager;
  @Inject
  private CommonService commonService;
  @Inject
  private LimboService limboService;

  public CaptchaCommand() {
  }

  public void runCommand(Player player, List<String> arguments) {
    String playerName = player.getName().toLowerCase();
    if (this.playerCache.isAuthenticated(playerName)) {
      this.commonService.send(player, MessageKey.ALREADY_LOGGED_IN_ERROR);
    } else if (!this.captchaManager.isCaptchaRequired(playerName)) {
      this.commonService.send(player, MessageKey.USAGE_LOGIN);
    } else {
      this.checkCaptcha(player, (String)arguments.get(0));
    }

  }

  private void checkCaptcha(Player player, String captchaCode) {
    boolean isCorrectCode = this.captchaManager.checkCode(player.getName(), captchaCode);
    if (isCorrectCode) {
      this.commonService.send(player, MessageKey.CAPTCHA_SUCCESS);
      this.commonService.send(player, MessageKey.LOGIN_MESSAGE);
      this.limboService.unmuteMessageTask(player);
    } else {
      String newCode = this.captchaManager.generateCode(player.getName());
      this.commonService.send(player, MessageKey.CAPTCHA_WRONG_ERROR, new String[]{newCode});
    }

  }
}
