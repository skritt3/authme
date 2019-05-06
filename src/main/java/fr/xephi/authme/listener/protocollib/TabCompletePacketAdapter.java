package fr.xephi.authme.listener.protocollib;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.PacketType.Play.Client;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.FieldAccessException;
import fr.xephi.authme.AuthMe;
import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.data.auth.PlayerCache;
import org.bukkit.entity.Player;

class TabCompletePacketAdapter
  extends PacketAdapter
{
  private final PlayerCache playerCache;
  
  TabCompletePacketAdapter(AuthMe plugin, PlayerCache playerCache)
  {
    super(plugin, ListenerPriority.NORMAL, new PacketType[] { PacketType.Play.Client.TAB_COMPLETE });
    this.playerCache = playerCache;
  }
  
  public void onPacketReceiving(PacketEvent event)
  {
    if (event.getPacketType() == PacketType.Play.Client.TAB_COMPLETE) {
      try
      {
        if (!this.playerCache.isAuthenticated(event.getPlayer().getName())) {
          event.setCancelled(true);
        }
      }
      catch (FieldAccessException e)
      {
        ConsoleLogger.logException("Couldn't access field:", e);
      }
    }
  }
  
  public void register()
  {
    ProtocolLibrary.getProtocolManager().addPacketListener(this);
  }
  
  public void unregister()
  {
    ProtocolLibrary.getProtocolManager().removePacketListener(this);
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\listener\protocollib\TabCompletePacketAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */