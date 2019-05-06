package fr.xephi.authme.command.executable.authme;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.command.ExecutableCommand;
import fr.xephi.authme.command.help.HelpMessagesService;
import fr.xephi.authme.initialization.DataFolder;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.message.Messages;
import fr.xephi.authme.service.HelpTranslationGenerator;
import fr.xephi.authme.service.MessageUpdater;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.settings.properties.PluginSettings;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.bukkit.command.CommandSender;

public class MessagesCommand
  implements ExecutableCommand
{
  private static final String DEFAULT_LANGUAGE = "en";
  @Inject
  private Settings settings;
  @Inject
  @DataFolder
  private File dataFolder;
  @Inject
  private Messages messages;
  @Inject
  private HelpTranslationGenerator helpTranslationGenerator;
  @Inject
  private HelpMessagesService helpMessagesService;
  
  public void executeCommand(CommandSender sender, List<String> arguments)
  {
    if ((!arguments.isEmpty()) && ("help".equalsIgnoreCase((String)arguments.get(0)))) {
      updateHelpFile(sender);
    } else {
      updateMessagesFile(sender);
    }
  }
  
  private void updateHelpFile(CommandSender sender)
  {
    try
    {
      this.helpTranslationGenerator.updateHelpFile();
      sender.sendMessage("Successfully updated the help file");
      this.helpMessagesService.reload();
    }
    catch (IOException e)
    {
      sender.sendMessage("Could not update help file: " + e.getMessage());
      ConsoleLogger.logException("Could not update help file:", e);
    }
  }
  
  private void updateMessagesFile(CommandSender sender)
  {
    String language = (String)this.settings.getProperty(PluginSettings.MESSAGES_LANGUAGE);
    try
    {
      boolean isFileUpdated = new MessageUpdater(new File(this.dataFolder, getMessagePath(language)), getMessagePath(language), getMessagePath("en")).executeCopy(sender);
      if (isFileUpdated) {
        this.messages.reload();
      }
    }
    catch (Exception e)
    {
      sender.sendMessage("Could not update messages: " + e.getMessage());
      ConsoleLogger.logException("Could not update messages:", e);
    }
  }
  
  private static String getMessagePath(String code)
  {
    return "messages/messages_" + code + ".yml";
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\command\executable\authme\MessagesCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */