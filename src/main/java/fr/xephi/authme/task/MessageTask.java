package fr.xephi.authme.task;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class MessageTask
  extends BukkitRunnable
{
  private final Player player;
  private final String[] message;
  private boolean isMuted;
  
  public MessageTask(Player player, String[] lines)
  {
    this.player = player;
    this.message = lines;
    this.isMuted = false;
  }
  
  public void setMuted(boolean isMuted)
  {
    this.isMuted = isMuted;
  }
  
  public void run()
  {
    if (!this.isMuted) {
      this.player.sendMessage(this.message);
    }
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\task\MessageTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */