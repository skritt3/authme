package fr.xephi.authme.util;

import fr.xephi.authme.ConsoleLogger;
import java.util.Collection;
import java.util.regex.Pattern;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

public final class Utils
{
  public static final long MILLIS_PER_MINUTE = 60000L;
  public static final long MILLIS_PER_HOUR = 3600000L;
  
  public static Pattern safePatternCompile(String pattern)
  {
    try
    {
      return Pattern.compile(pattern);
    }
    catch (Exception e)
    {
      ConsoleLogger.warning("Failed to compile pattern '" + pattern + "' - defaulting to allowing everything");
    }
    return Pattern.compile(".*?");
  }
  
  public static boolean isClassLoaded(String className)
  {
    try
    {
      Class.forName(className);
      return true;
    }
    catch (ClassNotFoundException e) {}
    return false;
  }
  
  public static void logAndSendMessage(CommandSender sender, String message)
  {
    ConsoleLogger.info(message);
    if ((sender != null) && (!(sender instanceof ConsoleCommandSender))) {
      sender.sendMessage(message);
    }
  }
  
  public static void logAndSendWarning(CommandSender sender, String message)
  {
    ConsoleLogger.warning(message);
    if ((sender != null) && (!(sender instanceof ConsoleCommandSender))) {
      sender.sendMessage(ChatColor.RED + message);
    }
  }
  
  public static boolean isCollectionEmpty(Collection<?> coll)
  {
    return (coll == null) || (coll.isEmpty());
  }
  
  public static int getCoreCount()
  {
    return Runtime.getRuntime().availableProcessors();
  }
  
  public static boolean isEmailEmpty(String email)
  {
    return (StringUtils.isEmpty(email)) || ("your@email.com".equalsIgnoreCase(email));
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authm\\util\Utils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */