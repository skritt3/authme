package fr.xephi.authme.command.executable.authme.debug;

import fr.xephi.authme.data.auth.PlayerAuth;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.permission.DebugSectionPermissions;
import fr.xephi.authme.permission.PermissionNode;
import fr.xephi.authme.security.crypts.HashedPassword;
import fr.xephi.authme.util.StringUtils;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

class PlayerAuthViewer
  implements DebugSection
{
  @Inject
  private DataSource dataSource;
  
  public String getName()
  {
    return "db";
  }
  
  public String getDescription()
  {
    return "View player's data in the database";
  }
  
  public void execute(CommandSender sender, List<String> arguments)
  {
    if (arguments.isEmpty())
    {
      sender.sendMessage(ChatColor.BLUE + "AuthMe database viewer");
      sender.sendMessage("Enter player name to view his data in the database.");
      sender.sendMessage("Example: /authme debug db Bobby");
      return;
    }
    PlayerAuth auth = this.dataSource.getAuth((String)arguments.get(0));
    if (auth == null)
    {
      sender.sendMessage(ChatColor.BLUE + "AuthMe database viewer");
      sender.sendMessage("No record exists for '" + (String)arguments.get(0) + "'");
    }
    else
    {
      displayAuthToSender(auth, sender);
    }
  }
  
  public PermissionNode getRequiredPermission()
  {
    return DebugSectionPermissions.PLAYER_AUTH_VIEWER;
  }
  
  private void displayAuthToSender(PlayerAuth auth, CommandSender sender)
  {
    sender.sendMessage(ChatColor.BLUE + "[AuthMe] Player " + auth.getNickname() + " / " + auth.getRealName());
    sender.sendMessage("Email: " + auth.getEmail() + ". IP: " + auth.getLastIp() + ". Group: " + auth.getGroupId());
    sender.sendMessage("Quit location: " + 
      DebugSectionUtils.formatLocation(auth.getQuitLocX(), auth.getQuitLocY(), auth.getQuitLocZ(), auth.getWorld()));
    sender.sendMessage("Last login: " + formatDate(auth.getLastLogin()));
    sender.sendMessage("Registration: " + formatDate(Long.valueOf(auth.getRegistrationDate())) + " with IP " + auth
      .getRegistrationIp());
    
    HashedPassword hashedPass = auth.getPassword();
    sender.sendMessage("Hash / salt (partial): '" + safeSubstring(hashedPass.getHash(), 6) + "' / '" + 
      safeSubstring(hashedPass.getSalt(), 4) + "'");
  }
  
  private static String safeSubstring(String str, int length)
  {
    if (StringUtils.isEmpty(str)) {
      return "";
    }
    if (str.length() < length) {
      return str.substring(0, str.length() / 2) + "...";
    }
    return str.substring(0, length) + "...";
  }
  
  private static String formatDate(Long timestamp)
  {
    if (timestamp == null) {
      return "Not available (null)";
    }
    if (timestamp.longValue() == 0L) {
      return "Not available (0)";
    }
    LocalDateTime date = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp.longValue()), ZoneId.systemDefault());
    return DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(date);
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\command\executable\authme\debug\PlayerAuthViewer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */