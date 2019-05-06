package fr.xephi.authme.process.register.executors;

import fr.xephi.authme.data.auth.PlayerAuth;
import fr.xephi.authme.data.auth.PlayerAuth.Builder;
import fr.xephi.authme.security.crypts.HashedPassword;
import fr.xephi.authme.util.PlayerUtils;
import org.bukkit.entity.Player;

final class PlayerAuthBuilderHelper
{
  static PlayerAuth createPlayerAuth(Player player, HashedPassword hashedPassword, String email)
  {
    return 
    
      PlayerAuth.builder().name(player.getName().toLowerCase()).realName(player.getName()).password(hashedPassword).email(email).registrationIp(PlayerUtils.getPlayerIp(player)).registrationDate(System.currentTimeMillis()).build();
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\process\register\executors\PlayerAuthBuilderHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */