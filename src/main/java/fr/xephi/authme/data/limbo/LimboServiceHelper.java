package fr.xephi.authme.data.limbo;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.permission.PermissionsManager;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.settings.SpawnLoader;
import fr.xephi.authme.settings.properties.LimboSettings;
import fr.xephi.authme.settings.properties.RestrictionSettings;
import fr.xephi.authme.util.Utils;
import java.util.Collection;
import java.util.Collections;
import org.bukkit.Location;
import org.bukkit.entity.Player;

class LimboServiceHelper
{
  @Inject
  private SpawnLoader spawnLoader;
  @Inject
  private PermissionsManager permissionsManager;
  @Inject
  private Settings settings;
  
  LimboPlayer createLimboPlayer(Player player, boolean isRegistered, Location location)
  {
    boolean isOperator = (isRegistered) && (player.isOp());
    boolean flyEnabled = player.getAllowFlight();
    float walkSpeed = player.getWalkSpeed();
    float flySpeed = player.getFlySpeed();
    
    Collection<String> playerGroups = this.permissionsManager.hasGroupSupport() ? this.permissionsManager.getGroups(player) : Collections.emptyList();
    ConsoleLogger.debug("Player `{0}` has groups `{1}`", player.getName(), String.join(", ", playerGroups));
    
    return new LimboPlayer(location, isOperator, playerGroups, flyEnabled, walkSpeed, flySpeed);
  }
  
  void revokeLimboStates(Player player)
  {
    player.setOp(false);
    ((AllowFlightRestoreType)this.settings.getProperty(LimboSettings.RESTORE_ALLOW_FLIGHT))
      .processPlayer(player);
    if (!((Boolean)this.settings.getProperty(RestrictionSettings.ALLOW_UNAUTHED_MOVEMENT)).booleanValue())
    {
      player.setFlySpeed(0.0F);
      player.setWalkSpeed(0.0F);
    }
  }
  
  LimboPlayer merge(LimboPlayer newLimbo, LimboPlayer oldLimbo)
  {
    if (newLimbo == null) {
      return oldLimbo;
    }
    if (oldLimbo == null) {
      return newLimbo;
    }
    boolean isOperator = (newLimbo.isOperator()) || (oldLimbo.isOperator());
    boolean canFly = (newLimbo.isCanFly()) || (oldLimbo.isCanFly());
    float flySpeed = Math.max(newLimbo.getFlySpeed(), oldLimbo.getFlySpeed());
    float walkSpeed = Math.max(newLimbo.getWalkSpeed(), oldLimbo.getWalkSpeed());
    Collection<String> groups = getLimboGroups(oldLimbo.getGroups(), newLimbo.getGroups());
    Location location = firstNotNull(oldLimbo.getLocation(), newLimbo.getLocation());
    
    return new LimboPlayer(location, isOperator, groups, canFly, walkSpeed, flySpeed);
  }
  
  private static Location firstNotNull(Location first, Location second)
  {
    return first == null ? second : first;
  }
  
  private static Collection<String> getLimboGroups(Collection<String> oldLimboGroups, Collection<String> newLimboGroups)
  {
    ConsoleLogger.debug("Limbo merge: new and old groups are `{0}` and `{1}`", newLimboGroups, oldLimboGroups);
    return Utils.isCollectionEmpty(oldLimboGroups) ? newLimboGroups : oldLimboGroups;
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\data\limbo\LimboServiceHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */