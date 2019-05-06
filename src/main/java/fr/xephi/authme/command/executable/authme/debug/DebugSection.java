package fr.xephi.authme.command.executable.authme.debug;

import fr.xephi.authme.permission.PermissionNode;
import java.util.List;
import org.bukkit.command.CommandSender;

abstract interface DebugSection
{
  public abstract String getName();
  
  public abstract String getDescription();
  
  public abstract void execute(CommandSender paramCommandSender, List<String> paramList);
  
  public abstract PermissionNode getRequiredPermission();
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\command\executable\authme\debug\DebugSection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */