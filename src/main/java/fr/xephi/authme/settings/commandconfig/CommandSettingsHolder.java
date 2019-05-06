package fr.xephi.authme.settings.commandconfig;

import fr.xephi.authme.libs.jalu.configme.SectionComments;
import fr.xephi.authme.libs.jalu.configme.SettingsHolder;
import fr.xephi.authme.libs.jalu.configme.properties.BeanProperty;
import fr.xephi.authme.libs.jalu.configme.properties.Property;
import java.util.HashMap;
import java.util.Map;

public final class CommandSettingsHolder
  implements SettingsHolder
{
  public static final Property<CommandConfig> COMMANDS = new BeanProperty(CommandConfig.class, "", new CommandConfig());
  
  @SectionComments
  public static Map<String, String[]> sectionComments()
  {
    String[] comments = { "This configuration file allows you to execute commands on various events.", "Supported placeholders in commands:", "  %p is replaced with the player name.", "  %nick is replaced with the player's nick name", "  %ip is replaced with the player's IP address", "  %country is replaced with the player's country", "", "For example, if you want to send a welcome message to a player who just registered:", "onRegister:", "  welcome:", "    command: 'msg %p Welcome to the server!'", "    executor: CONSOLE", "", "This will make the console execute the msg command to the player.", "Each command under an event has a name you can choose freely (e.g. 'welcome' as above),", "after which a mandatory 'command' field defines the command to run,", "and 'executor' defines who will run the command (either PLAYER or CONSOLE). Longer example:", "onLogin:", "  welcome:", "    command: 'msg %p Welcome back!'", "    executor: PLAYER", "  broadcast:", "    command: 'broadcast %p has joined, welcome back!'", "    executor: CONSOLE", "", "Supported command events: onLogin, onSessionLogin, onJoin, onLogout, onRegister, onUnregister" };
    
    Map<String, String[]> commentMap = new HashMap();
    commentMap.put("", comments);
    commentMap.put("onUnregister", new String[] { "Commands to run whenever a player is unregistered (by himself, or by an admin)" });
    
    commentMap.put("onLogout", new String[] { "These commands are called whenever a logged in player uses /logout or quits.", "The commands are not run if a player that was not logged in quits the server.", "Note: if your server crashes, these commands won't be run, so don't rely on them to undo", "'onLogin' commands that would be dangerous for non-logged in players to have!" });
    
    return commentMap;
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\settings\commandconfig\CommandSettingsHolder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */