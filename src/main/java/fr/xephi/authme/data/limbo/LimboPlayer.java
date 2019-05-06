package fr.xephi.authme.data.limbo;

import fr.xephi.authme.task.MessageTask;
import java.util.Collection;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitTask;

public class LimboPlayer
{
  public static final float DEFAULT_WALK_SPEED = 0.2F;
  public static final float DEFAULT_FLY_SPEED = 0.1F;
  private final boolean canFly;
  private final boolean operator;
  private final Collection<String> groups;
  private final Location loc;
  private final float walkSpeed;
  private final float flySpeed;
  private BukkitTask timeoutTask = null;
  private MessageTask messageTask = null;
  
  public LimboPlayer(Location loc, boolean operator, Collection<String> groups, boolean fly, float walkSpeed, float flySpeed)
  {
    this.loc = loc;
    this.operator = operator;
    this.groups = groups;
    this.canFly = fly;
    this.walkSpeed = walkSpeed;
    this.flySpeed = flySpeed;
  }
  
  public Location getLocation()
  {
    return this.loc;
  }
  
  public boolean isOperator()
  {
    return this.operator;
  }
  
  public Collection<String> getGroups()
  {
    return this.groups;
  }
  
  public boolean isCanFly()
  {
    return this.canFly;
  }
  
  public float getWalkSpeed()
  {
    return this.walkSpeed;
  }
  
  public float getFlySpeed()
  {
    return this.flySpeed;
  }
  
  public BukkitTask getTimeoutTask()
  {
    return this.timeoutTask;
  }
  
  public void setTimeoutTask(BukkitTask timeoutTask)
  {
    if (this.timeoutTask != null) {
      this.timeoutTask.cancel();
    }
    this.timeoutTask = timeoutTask;
  }
  
  public MessageTask getMessageTask()
  {
    return this.messageTask;
  }
  
  public void setMessageTask(MessageTask messageTask)
  {
    if (this.messageTask != null) {
      this.messageTask.cancel();
    }
    this.messageTask = messageTask;
  }
  
  public void clearTasks()
  {
    setMessageTask(null);
    setTimeoutTask(null);
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\data\limbo\LimboPlayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */