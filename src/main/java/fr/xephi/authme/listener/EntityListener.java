package fr.xephi.authme.listener;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.libs.javax.inject.Inject;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;

public class EntityListener
  implements Listener
{
  private final ListenerService listenerService;
  private Method getShooter;
  private boolean shooterIsLivingEntity;
  
  @Inject
  EntityListener(ListenerService listenerService)
  {
    this.listenerService = listenerService;
    try
    {
      this.getShooter = Projectile.class.getDeclaredMethod("getShooter", new Class[0]);
      this.shooterIsLivingEntity = (this.getShooter.getReturnType() == LivingEntity.class);
    }
    catch (NoSuchMethodException|SecurityException e)
    {
      ConsoleLogger.logException("Cannot load getShooter() method on Projectile class", e);
    }
  }
  
  @EventHandler(ignoreCancelled=true, priority=EventPriority.LOWEST)
  public void onDamage(EntityDamageEvent event)
  {
    if (this.listenerService.shouldCancelEvent(event))
    {
      event.getEntity().setFireTicks(0);
      event.setDamage(0.0D);
      event.setCancelled(true);
    }
  }
  
  @EventHandler(ignoreCancelled=true, priority=EventPriority.LOWEST)
  public void onAttack(EntityDamageByEntityEvent event)
  {
    if (this.listenerService.shouldCancelEvent(event.getDamager())) {
      event.setCancelled(true);
    }
  }
  
  @EventHandler(ignoreCancelled=true, priority=EventPriority.LOWEST)
  public void onEntityTarget(EntityTargetEvent event)
  {
    if (this.listenerService.shouldCancelEvent(event))
    {
      event.setTarget(null);
      event.setCancelled(true);
    }
  }
  
  @EventHandler(ignoreCancelled=true, priority=EventPriority.LOWEST)
  public void onFoodLevelChange(FoodLevelChangeEvent event)
  {
    if (this.listenerService.shouldCancelEvent(event)) {
      event.setCancelled(true);
    }
  }
  
  @EventHandler(ignoreCancelled=true, priority=EventPriority.LOWEST)
  public void entityRegainHealthEvent(EntityRegainHealthEvent event)
  {
    if (this.listenerService.shouldCancelEvent(event))
    {
      event.setAmount(0.0D);
      event.setCancelled(true);
    }
  }
  
  @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGHEST)
  public void onEntityInteract(EntityInteractEvent event)
  {
    if (this.listenerService.shouldCancelEvent(event)) {
      event.setCancelled(true);
    }
  }
  
  @EventHandler(ignoreCancelled=true, priority=EventPriority.LOWEST)
  public void onLowestEntityInteract(EntityInteractEvent event)
  {
    if (this.listenerService.shouldCancelEvent(event)) {
      event.setCancelled(true);
    }
  }
  
  @EventHandler(ignoreCancelled=true, priority=EventPriority.LOWEST)
  public void onProjectileLaunch(ProjectileLaunchEvent event)
  {
    if (event.getEntity() == null) {
      return;
    }
    Projectile projectile = event.getEntity();
    
    Object shooterRaw = null;
    if (this.shooterIsLivingEntity) {
      try
      {
        if (this.getShooter == null) {
          this.getShooter = Projectile.class.getMethod("getShooter", new Class[0]);
        }
        shooterRaw = this.getShooter.invoke(projectile, new Object[0]);
      }
      catch (NoSuchMethodException|InvocationTargetException|IllegalAccessException e)
      {
        ConsoleLogger.logException("Error getting shooter", e);
      }
    } else {
      shooterRaw = projectile.getShooter();
    }
    if (((shooterRaw instanceof Player)) && (this.listenerService.shouldCancelEvent((Player)shooterRaw))) {
      event.setCancelled(true);
    }
  }
  
  @EventHandler(ignoreCancelled=true, priority=EventPriority.NORMAL)
  public void onShoot(EntityShootBowEvent event)
  {
    if (this.listenerService.shouldCancelEvent(event)) {
      event.setCancelled(true);
    }
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\listener\EntityListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */