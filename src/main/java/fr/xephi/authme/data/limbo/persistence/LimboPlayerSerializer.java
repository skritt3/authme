package fr.xephi.authme.data.limbo.persistence;

import fr.xephi.authme.data.limbo.LimboPlayer;
import fr.xephi.authme.libs.google.gson.Gson;
import fr.xephi.authme.libs.google.gson.JsonElement;
import fr.xephi.authme.libs.google.gson.JsonObject;
import fr.xephi.authme.libs.google.gson.JsonSerializationContext;
import fr.xephi.authme.libs.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import org.bukkit.Location;
import org.bukkit.World;

class LimboPlayerSerializer
  implements JsonSerializer<LimboPlayer>
{
  static final String LOCATION = "location";
  static final String LOC_WORLD = "world";
  static final String LOC_X = "x";
  static final String LOC_Y = "y";
  static final String LOC_Z = "z";
  static final String LOC_YAW = "yaw";
  static final String LOC_PITCH = "pitch";
  static final String GROUPS = "groups";
  static final String IS_OP = "operator";
  static final String CAN_FLY = "can-fly";
  static final String WALK_SPEED = "walk-speed";
  static final String FLY_SPEED = "fly-speed";
  private static final Gson GSON = new Gson();
  
  public JsonElement serialize(LimboPlayer limboPlayer, Type type, JsonSerializationContext context)
  {
    Location loc = limboPlayer.getLocation();
    JsonObject locationObject = new JsonObject();
    locationObject.addProperty("world", loc.getWorld().getName());
    locationObject.addProperty("x", Double.valueOf(loc.getX()));
    locationObject.addProperty("y", Double.valueOf(loc.getY()));
    locationObject.addProperty("z", Double.valueOf(loc.getZ()));
    locationObject.addProperty("yaw", Float.valueOf(loc.getYaw()));
    locationObject.addProperty("pitch", Float.valueOf(loc.getPitch()));
    
    JsonObject obj = new JsonObject();
    obj.add("location", locationObject);
    obj.add("groups", GSON.toJsonTree(limboPlayer.getGroups()).getAsJsonArray());
    
    obj.addProperty("operator", Boolean.valueOf(limboPlayer.isOperator()));
    obj.addProperty("can-fly", Boolean.valueOf(limboPlayer.isCanFly()));
    obj.addProperty("walk-speed", Float.valueOf(limboPlayer.getWalkSpeed()));
    obj.addProperty("fly-speed", Float.valueOf(limboPlayer.getFlySpeed()));
    return obj;
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\data\limbo\persistence\LimboPlayerSerializer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */