package fr.xephi.authme.service;

import fr.xephi.authme.libs.jalu.configme.properties.Property;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.message.MessageKey;
import fr.xephi.authme.message.Messages;
import fr.xephi.authme.permission.PermissionNode;
import fr.xephi.authme.permission.PermissionsManager;
import fr.xephi.authme.settings.Settings;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommonService
{
  @Inject
  private Settings settings;
  @Inject
  private Messages messages;
  @Inject
  private PermissionsManager permissionsManager;
  
  public <T> T getProperty(Property<T> property)
  {
    return (T)this.settings.getProperty(property);
  }
  
  public void send(CommandSender sender, MessageKey key)
  {
    this.messages.send(sender, key);
  }
  
  public void send(CommandSender sender, MessageKey key, String... replacements)
  {
    this.messages.send(sender, key, replacements);
  }
  
  public String retrieveSingleMessage(MessageKey key)
  {
    return this.messages.retrieveSingle(key, new String[0]);
  }
  
  public boolean hasPermission(Player player, PermissionNode node)
  {
    return this.permissionsManager.hasPermission(player, node);
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\service\CommonService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */