package fr.xephi.authme.data.limbo.persistence;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.data.limbo.LimboPlayer;
import fr.xephi.authme.initialization.DataFolder;
import fr.xephi.authme.libs.google.common.io.CharSource;
import fr.xephi.authme.libs.google.common.io.Files;
import fr.xephi.authme.libs.google.gson.Gson;
import fr.xephi.authme.libs.google.gson.GsonBuilder;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.service.BukkitService;
import fr.xephi.authme.util.FileUtils;
import fr.xephi.authme.util.PlayerUtils;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.bukkit.entity.Player;

class IndividualFilesPersistenceHandler
  implements LimboPersistenceHandler
{
  private final Gson gson;
  private final File cacheDir;
  
  @Inject
  IndividualFilesPersistenceHandler(@DataFolder File dataFolder, BukkitService bukkitService)
  {
    this.cacheDir = new File(dataFolder, "playerdata");
    if ((!this.cacheDir.exists()) && (!this.cacheDir.isDirectory()) && (!this.cacheDir.mkdir())) {
      ConsoleLogger.warning("Failed to create playerdata directory '" + this.cacheDir + "'");
    }
    this.gson = new GsonBuilder().registerTypeAdapter(LimboPlayer.class, new LimboPlayerSerializer()).registerTypeAdapter(LimboPlayer.class, new LimboPlayerDeserializer(bukkitService)).setPrettyPrinting().create();
  }
  
  public LimboPlayer getLimboPlayer(Player player)
  {
    String id = PlayerUtils.getUuidOrName(player);
    File file = new File(this.cacheDir, id + File.separator + "data.json");
    if (!file.exists()) {
      return null;
    }
    try
    {
      String str = Files.asCharSource(file, StandardCharsets.UTF_8).read();
      return (LimboPlayer)this.gson.fromJson(str, LimboPlayer.class);
    }
    catch (IOException e)
    {
      ConsoleLogger.logException("Could not read player data on disk for '" + player.getName() + "'", e);
    }
    return null;
  }
  
  public void saveLimboPlayer(Player player, LimboPlayer limboPlayer)
  {
    String id = PlayerUtils.getUuidOrName(player);
    try
    {
      File file = new File(this.cacheDir, id + File.separator + "data.json");
      Files.createParentDirs(file);
      Files.touch(file);
      Files.write(this.gson.toJson(limboPlayer), file, StandardCharsets.UTF_8);
    }
    catch (IOException e)
    {
      ConsoleLogger.logException("Failed to write " + player.getName() + " data:", e);
    }
  }
  
  public void removeLimboPlayer(Player player)
  {
    String id = PlayerUtils.getUuidOrName(player);
    File file = new File(this.cacheDir, id);
    if (file.exists())
    {
      FileUtils.purgeDirectory(file);
      FileUtils.delete(file);
    }
  }
  
  public LimboPersistenceType getType()
  {
    return LimboPersistenceType.INDIVIDUAL_FILES;
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\data\limbo\persistence\IndividualFilesPersistenceHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */