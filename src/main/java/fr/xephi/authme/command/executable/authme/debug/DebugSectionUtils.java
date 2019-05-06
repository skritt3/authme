package fr.xephi.authme.command.executable.authme.debug;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.data.limbo.LimboService;
import fr.xephi.authme.datasource.CacheDataSource;
import fr.xephi.authme.datasource.DataSource;
import java.lang.reflect.Field;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import org.bukkit.Location;
import org.bukkit.World;

final class DebugSectionUtils
{
  private static Field limboEntriesField;
  
  static String formatLocation(Location location)
  {
    if (location == null) {
      return "null";
    }
    String worldName = location.getWorld() == null ? "null" : location.getWorld().getName();
    return formatLocation(location.getX(), location.getY(), location.getZ(), worldName);
  }
  
  static String formatLocation(double x, double y, double z, String world)
  {
    return "(" + round(x) + ", " + round(y) + ", " + round(z) + ") in '" + world + "'";
  }
  
  private static String round(double number)
  {
    DecimalFormat df = new DecimalFormat("#.##");
    df.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.US));
    df.setRoundingMode(RoundingMode.HALF_UP);
    return df.format(number);
  }
  
  private static Field getLimboPlayerEntriesField()
  {
    if (limboEntriesField == null) {
      try
      {
        Field field = LimboService.class.getDeclaredField("entries");
        field.setAccessible(true);
        limboEntriesField = field;
      }
      catch (Exception e)
      {
        ConsoleLogger.logException("Could not retrieve LimboService entries field:", e);
      }
    }
    return limboEntriesField;
  }
  
  static <U> U applyToLimboPlayersMap(LimboService limboService, Function<Map, U> function)
  {
    Field limboPlayerEntriesField = getLimboPlayerEntriesField();
    if (limboPlayerEntriesField != null) {
      try
      {
        return (U)function.apply((Map)limboEntriesField.get(limboService));
      }
      catch (Exception e)
      {
        ConsoleLogger.logException("Could not retrieve LimboService values:", e);
      }
    }
    return null;
  }
  
  static <T> T castToTypeOrNull(Object object, Class<T> clazz)
  {
    return (T)(clazz.isInstance(object) ? clazz.cast(object) : null);
  }
  
  static DataSource unwrapSourceFromCacheDataSource(DataSource dataSource)
  {
    if ((dataSource instanceof CacheDataSource)) {
      try
      {
        Field source = CacheDataSource.class.getDeclaredField("source");
        source.setAccessible(true);
        return (DataSource)source.get(dataSource);
      }
      catch (NoSuchFieldException|IllegalAccessException e)
      {
        ConsoleLogger.logException("Could not get source of CacheDataSource:", e);
        return null;
      }
    }
    return dataSource;
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\command\executable\authme\debug\DebugSectionUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */