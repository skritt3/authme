package fr.xephi.authme.datasource.converter;

import de.luricos.bukkit.xAuth.database.DatabaseController;
import de.luricos.bukkit.xAuth.database.DatabaseTables;
import de.luricos.bukkit.xAuth.utils.xAuthLog;
import de.luricos.bukkit.xAuth.xAuth;
import fr.xephi.authme.data.auth.PlayerAuth;
import fr.xephi.authme.data.auth.PlayerAuth.Builder;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.initialization.DataFolder;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.util.FileUtils;
import fr.xephi.authme.util.Utils;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;

public class XAuthConverter
  implements Converter
{
  @Inject
  @DataFolder
  private File dataFolder;
  @Inject
  private DataSource database;
  @Inject
  private PluginManager pluginManager;
  
  public void execute(CommandSender sender)
  {
    try
    {
      Class.forName("de.luricos.bukkit.xAuth.xAuth");
      convert(sender);
    }
    catch (ClassNotFoundException ce)
    {
      sender.sendMessage("xAuth has not been found, please put xAuth.jar in your plugin folder and restart!");
    }
  }
  
  private void convert(CommandSender sender)
  {
    if (this.pluginManager.getPlugin("xAuth") == null)
    {
      sender.sendMessage("[AuthMe] xAuth plugin not found");
      return;
    }
    File xAuthDb = new File(this.dataFolder.getParent(), FileUtils.makePath(new String[] { "xAuth", "xAuth.h2.db" }));
    if (!xAuthDb.exists()) {
      sender.sendMessage("[AuthMe] xAuth H2 database not found, checking for MySQL or SQLite data...");
    }
    List<Integer> players = getXAuthPlayers();
    if (Utils.isCollectionEmpty(players))
    {
      sender.sendMessage("[AuthMe] Error while importing xAuthPlayers: did not find any players");
      return;
    }
    sender.sendMessage("[AuthMe] Starting import...");
    for (Iterator localIterator = players.iterator(); localIterator.hasNext();)
    {
      int id = ((Integer)localIterator.next()).intValue();
      String pl = getIdPlayer(id);
      String psw = getPassword(id);
      if ((psw != null) && (!psw.isEmpty()) && (pl != null))
      {
        PlayerAuth auth = PlayerAuth.builder().name(pl.toLowerCase()).realName(pl).password(psw, null).build();
        this.database.saveAuth(auth);
      }
    }
    sender.sendMessage("[AuthMe] Successfully converted from xAuth database");
  }
  
  private String getIdPlayer(int id)
  {
    String realPass = "";
    Connection conn = xAuth.getPlugin().getDatabaseController().getConnection();
    PreparedStatement ps = null;
    ResultSet rs = null;
    try
    {
      String sql = String.format("SELECT `playername` FROM `%s` WHERE `id` = ?", new Object[] {
        xAuth.getPlugin().getDatabaseController().getTable(DatabaseTables.ACCOUNT) });
      ps = conn.prepareStatement(sql);
      ps.setInt(1, id);
      rs = ps.executeQuery();
      if (!rs.next()) {
        return null;
      }
      realPass = rs.getString("playername").toLowerCase();
    }
    catch (SQLException e)
    {
      String str1;
      xAuthLog.severe("Failed to retrieve name for account: " + id, e);
      return null;
    }
    finally
    {
      xAuth.getPlugin().getDatabaseController().close(conn, ps, rs);
    }
    return realPass;
  }
  
  private List<Integer> getXAuthPlayers()
  {
    List<Integer> xP = new ArrayList();
    Connection conn = xAuth.getPlugin().getDatabaseController().getConnection();
    PreparedStatement ps = null;
    ResultSet rs = null;
    try
    {
      String sql = String.format("SELECT * FROM `%s`", new Object[] {
        xAuth.getPlugin().getDatabaseController().getTable(DatabaseTables.ACCOUNT) });
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      while (rs.next()) {
        xP.add(Integer.valueOf(rs.getInt("id")));
      }
    }
    catch (SQLException e)
    {
      xAuthLog.severe("Cannot import xAuthPlayers", e);
      return new ArrayList();
    }
    finally
    {
      xAuth.getPlugin().getDatabaseController().close(conn, ps, rs);
    }
    return xP;
  }
  
  private String getPassword(int accountId)
  {
    String realPass = "";
    Connection conn = xAuth.getPlugin().getDatabaseController().getConnection();
    PreparedStatement ps = null;
    ResultSet rs = null;
    try
    {
      String sql = String.format("SELECT `password`, `pwtype` FROM `%s` WHERE `id` = ?", new Object[] {
        xAuth.getPlugin().getDatabaseController().getTable(DatabaseTables.ACCOUNT) });
      ps = conn.prepareStatement(sql);
      ps.setInt(1, accountId);
      rs = ps.executeQuery();
      if (!rs.next()) {
        return null;
      }
      realPass = rs.getString("password");
    }
    catch (SQLException e)
    {
      String str1;
      xAuthLog.severe("Failed to retrieve password hash for account: " + accountId, e);
      return null;
    }
    finally
    {
      xAuth.getPlugin().getDatabaseController().close(conn, ps, rs);
    }
    return realPass;
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\datasource\converter\XAuthConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */