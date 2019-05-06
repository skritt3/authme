//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package fr.xephi.authme.service;

import com.earth2me.essentials.Essentials;
import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MVWorldManager;
import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.libs.jalu.injector.annotations.NoFieldScan;
import fr.xephi.authme.libs.javax.inject.Inject;
import java.io.File;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

@NoFieldScan
public class PluginHookService {
  private final PluginManager pluginManager;
  private Essentials essentials;
  private MultiverseCore multiverse;

  @Inject
  public PluginHookService(PluginManager pluginManager) {
    this.pluginManager = pluginManager;
    this.tryHookToEssentials();
    this.tryHookToMultiverse();
  }

  public void setEssentialsSocialSpyStatus(Player player, boolean socialSpyStatus) {
    if (this.essentials != null) {
      this.essentials.getUser(player).setSocialSpyEnabled(socialSpyStatus);
    }

  }

  public File getEssentialsDataFolder() {
    return this.essentials != null ? this.essentials.getDataFolder() : null;
  }

  public Location getMultiverseSpawn(World world) {
    if (this.multiverse != null) {
      MVWorldManager manager = this.multiverse.getMVWorldManager();
      if (manager.isMVWorld(world)) {
        return manager.getMVWorld(world).getSpawnLocation();
      }
    }

    return null;
  }

  public boolean isEssentialsAvailable() {
    return this.essentials != null;
  }

  public boolean isMultiverseAvailable() {
    return this.multiverse != null;
  }

  public void tryHookToEssentials() {
    try {
      this.essentials = (Essentials)getPlugin(this.pluginManager, "Essentials", Essentials.class);
    } catch (NoClassDefFoundError | Exception var2) {
      this.essentials = null;
    }

  }

  public void tryHookToMultiverse() {
    try {
      this.multiverse = (MultiverseCore)getPlugin(this.pluginManager, "Multiverse-Core", MultiverseCore.class);
    } catch (NoClassDefFoundError | Exception var2) {
      this.multiverse = null;
    }

  }

  public void unhookEssentials() {
    this.essentials = null;
  }

  public void unhookMultiverse() {
    this.multiverse = null;
  }

  private static <T extends Plugin> T getPlugin(PluginManager pluginManager, String name, Class<T> clazz) throws Exception, NoClassDefFoundError {
    if (pluginManager.isPluginEnabled(name)) {
      T plugin = clazz.cast(pluginManager.getPlugin(name));
      ConsoleLogger.info("Hooked successfully into " + name);
      return plugin;
    } else {
      return null;
    }
  }
}
