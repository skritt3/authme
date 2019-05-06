package fr.xephi.authme.command;

import fr.xephi.authme.libs.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;

public final class CommandUtils
{
  public static int getMinNumberOfArguments(CommandDescription command)
  {
    int mandatoryArguments = 0;
    for (CommandArgumentDescription argument : command.getArguments()) {
      if (!argument.isOptional()) {
        mandatoryArguments++;
      }
    }
    return mandatoryArguments;
  }
  
  public static int getMaxNumberOfArguments(CommandDescription command)
  {
    return command.getArguments().size();
  }
  
  public static List<CommandDescription> constructParentList(CommandDescription command)
  {
    List<CommandDescription> commands = new ArrayList();
    CommandDescription currentCommand = command;
    while (currentCommand != null)
    {
      commands.add(currentCommand);
      currentCommand = currentCommand.getParent();
    }
    return Lists.reverse(commands);
  }
  
  public static String constructCommandPath(CommandDescription command)
  {
    StringBuilder sb = new StringBuilder();
    String prefix = "/";
    for (CommandDescription ancestor : constructParentList(command))
    {
      sb.append(prefix).append((String)ancestor.getLabels().get(0));
      prefix = " ";
    }
    return sb.toString();
  }
  
  public static String buildSyntax(CommandDescription command, List<String> correctLabels)
  {
    String commandSyntax = ChatColor.WHITE + "/" + (String)correctLabels.get(0) + ChatColor.YELLOW;
    for (int i = 1; i < correctLabels.size(); i++) {
      commandSyntax = commandSyntax + " " + (String)correctLabels.get(i);
    }
    for (CommandArgumentDescription argument : command.getArguments()) {
      commandSyntax = commandSyntax + " " + formatArgument(argument);
    }
    return commandSyntax;
  }
  
  public static String formatArgument(CommandArgumentDescription argument)
  {
    if (argument.isOptional()) {
      return "[" + argument.getName() + "]";
    }
    return "<" + argument.getName() + ">";
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\command\CommandUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */