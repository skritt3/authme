package fr.xephi.authme.output;

import fr.xephi.authme.libs.google.common.annotations.VisibleForTesting;
import fr.xephi.authme.util.StringUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

final class LogFilterHelper
{
  @VisibleForTesting
  static final List<String> COMMANDS_TO_SKIP = withAndWithoutAuthMePrefix(new String[] { "/login ", "/l ", "/log ", "/register ", "/reg ", "/unregister ", "/unreg ", "/changepassword ", "/cp ", "/changepass ", "/authme register ", "/authme reg ", "/authme r ", "/authme changepassword ", "/authme password ", "/authme changepass ", "/authme cp " });
  private static final String ISSUED_COMMAND_TEXT = "issued server command:";
  
  static boolean isSensitiveAuthMeCommand(String message)
  {
    if (message == null) {
      return false;
    }
    String lowerMessage = message.toLowerCase();
    return (lowerMessage.contains("issued server command:")) && (StringUtils.containsAny(lowerMessage, COMMANDS_TO_SKIP));
  }
  
  private static List<String> withAndWithoutAuthMePrefix(String... commands)
  {
    List<String> commandList = new ArrayList(commands.length * 2);
    for (String command : commands)
    {
      commandList.add(command);
      commandList.add(command.substring(0, 1) + "authme:" + command.substring(1));
    }
    return Collections.unmodifiableList(commandList);
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\output\LogFilterHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */