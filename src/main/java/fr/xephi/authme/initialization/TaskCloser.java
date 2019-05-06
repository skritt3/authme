package fr.xephi.authme.initialization;

import fr.xephi.authme.AuthMe;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.libs.google.common.annotations.VisibleForTesting;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Server;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitWorker;

public class TaskCloser
  implements Runnable
{
  private final BukkitScheduler scheduler;
  private final Logger logger;
  private final AuthMe plugin;
  private final DataSource dataSource;
  
  public TaskCloser(AuthMe plugin, DataSource dataSource)
  {
    this.scheduler = plugin.getServer().getScheduler();
    this.logger = plugin.getLogger();
    this.plugin = plugin;
    this.dataSource = dataSource;
  }
  
  public void run()
  {
    List<Integer> pendingTasks = getPendingTasks();
    this.logger.log(Level.INFO, "Waiting for {0} tasks to finish", Integer.valueOf(pendingTasks.size()));
    int progress = 0;
    
    int tries = 60;
    while (!pendingTasks.isEmpty())
    {
      if (tries <= 0)
      {
        this.logger.log(Level.INFO, "Async tasks times out after to many tries {0}", pendingTasks);
        break;
      }
      try
      {
        sleep();
      }
      catch (InterruptedException ignored)
      {
        Thread.currentThread().interrupt();
        break;
      }
      for (Iterator<Integer> iterator = pendingTasks.iterator(); iterator.hasNext();)
      {
        int taskId = ((Integer)iterator.next()).intValue();
        if (!this.scheduler.isCurrentlyRunning(taskId))
        {
          iterator.remove();
          progress++;
          this.logger.log(Level.INFO, "Progress: {0} / {1}", new Object[] { Integer.valueOf(progress), Integer.valueOf(pendingTasks.size()) });
        }
      }
      tries--;
    }
    if (this.dataSource != null) {
      this.dataSource.closeConnection();
    }
  }
  
  @VisibleForTesting
  void sleep()
    throws InterruptedException
  {
    Thread.sleep(1000L);
  }
  
  private List<Integer> getPendingTasks()
  {
    List<Integer> pendingTasks = new ArrayList();
    for (BukkitWorker pendingTask : this.scheduler.getActiveWorkers()) {
      if (pendingTask.getOwner().equals(this.plugin)) {
        if (!this.scheduler.isQueued(pendingTask.getTaskId())) {
          pendingTasks.add(Integer.valueOf(pendingTask.getTaskId()));
        }
      }
    }
    return pendingTasks;
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\initialization\TaskCloser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */