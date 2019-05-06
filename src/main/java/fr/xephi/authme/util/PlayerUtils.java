package fr.xephi.authme.util;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.UUID;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public final class PlayerUtils
{
  public static String getUuidOrName(OfflinePlayer player)
  {
    try
    {
      return player.getUniqueId().toString();
    }
    catch (NoSuchMethodError ignore) {}
    return player.getName();
  }
  
  public static String getPlayerIp(Player p)
  {
    return p.getAddress().getAddress().getHostAddress();
  }
  
  public static boolean isNpc(Player player)
  {
    return player.hasMetadata("NPC");
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authm\\util\PlayerUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */