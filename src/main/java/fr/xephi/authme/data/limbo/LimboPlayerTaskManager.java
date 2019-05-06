package fr.xephi.authme.data.limbo;

import fr.xephi.authme.data.auth.PlayerCache;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.message.MessageKey;
import fr.xephi.authme.message.Messages;
import fr.xephi.authme.service.BukkitService;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.settings.properties.RegistrationSettings;
import fr.xephi.authme.settings.properties.RestrictionSettings;
import fr.xephi.authme.task.MessageTask;
import fr.xephi.authme.task.TimeoutTask;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

class LimboPlayerTaskManager
{
  @Inject
  private Messages messages;
  @Inject
  private Settings settings;
  @Inject
  private BukkitService bukkitService;
  @Inject
  private PlayerCache playerCache;
  
  void registerMessageTask(Player player, LimboPlayer limbo, boolean isRegistered)
  {
    int interval = ((Integer)this.settings.getProperty(RegistrationSettings.MESSAGE_INTERVAL)).intValue();
    MessageKey key = getMessageKey(isRegistered);
    if (interval > 0)
    {
      MessageTask messageTask = new MessageTask(player, this.messages.retrieve(key));
      this.bukkitService.runTaskTimer(messageTask, 40L, interval * 20);
      limbo.setMessageTask(messageTask);
    }
  }
  
  void registerTimeoutTask(Player player, LimboPlayer limbo)
  {
    int timeout = ((Integer)this.settings.getProperty(RestrictionSettings.TIMEOUT)).intValue() * 20;
    if (timeout > 0)
    {
      String message = this.messages.retrieveSingle(MessageKey.LOGIN_TIMEOUT_ERROR, new String[0]);
      BukkitTask task = this.bukkitService.runTaskLater(new TimeoutTask(player, message, this.playerCache), timeout);
      limbo.setTimeoutTask(task);
    }
  }
  
  static void setMuted(MessageTask task, boolean isMuted)
  {
    if (task != null) {
      task.setMuted(isMuted);
    }
  }
  
  private static MessageKey getMessageKey(boolean isRegistered)
  {
    if (isRegistered) {
      return MessageKey.LOGIN_MESSAGE;
    }
    return MessageKey.REGISTER_MESSAGE;
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\data\limbo\LimboPlayerTaskManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */