package fr.xephi.authme.listener.protocollib;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.PacketType.Play.Server;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import fr.xephi.authme.AuthMe;
import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.data.auth.PlayerCache;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

class InventoryPacketAdapter
  extends PacketAdapter
{
  private static final int PLAYER_INVENTORY = 0;
  private static final int CRAFTING_SIZE = 5;
  private static final int ARMOR_SIZE = 4;
  private static final int MAIN_SIZE = 27;
  private static final int HOTBAR_SIZE = 9;
  private final PlayerCache playerCache;
  
  InventoryPacketAdapter(AuthMe plugin, PlayerCache playerCache)
  {
    super(plugin, new PacketType[] { PacketType.Play.Server.SET_SLOT, PacketType.Play.Server.WINDOW_ITEMS });
    this.playerCache = playerCache;
  }
  
  public void onPacketSending(PacketEvent packetEvent)
  {
    Player player = packetEvent.getPlayer();
    PacketContainer packet = packetEvent.getPacket();
    
    byte windowId = ((Integer)packet.getIntegers().read(0)).byteValue();
    if ((windowId == 0) && (!this.playerCache.isAuthenticated(player.getName()))) {
      packetEvent.setCancelled(true);
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
  
  public void sendBlankInventoryPacket(Player player)
  {
    ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
    PacketContainer inventoryPacket = protocolManager.createPacket(PacketType.Play.Server.WINDOW_ITEMS);
    inventoryPacket.getIntegers().write(0, Integer.valueOf(0));
    int inventorySize = 45;
    
    ItemStack[] blankInventory = new ItemStack[inventorySize];
    Arrays.fill(blankInventory, new ItemStack(Material.AIR));
    
    StructureModifier<ItemStack[]> itemArrayModifier = inventoryPacket.getItemArrayModifier();
    if (itemArrayModifier.size() > 0)
    {
      itemArrayModifier.write(0, blankInventory);
    }
    else
    {
      StructureModifier<List<ItemStack>> itemListModifier = inventoryPacket.getItemListModifier();
      itemListModifier.write(0, Arrays.asList(blankInventory));
    }
    try
    {
      protocolManager.sendServerPacket(player, inventoryPacket, false);
    }
    catch (InvocationTargetException invocationExc)
    {
      ConsoleLogger.logException("Error during sending blank inventory", invocationExc);
    }
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\listener\protocollib\InventoryPacketAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */