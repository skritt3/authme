package fr.xephi.authme.settings.properties;

import fr.xephi.authme.libs.jalu.configme.Comment;
import fr.xephi.authme.libs.jalu.configme.SettingsHolder;
import fr.xephi.authme.libs.jalu.configme.properties.Property;
import fr.xephi.authme.libs.jalu.configme.properties.PropertyInitializer;
import java.util.List;

public final class ProtectionSettings
  implements SettingsHolder
{
  @Comment({"Enable some servers protection (country based login, antibot)"})
  public static final Property<Boolean> ENABLE_PROTECTION = PropertyInitializer.newProperty("Protection.enableProtection", false);
  @Comment({"Apply the protection also to registered usernames"})
  public static final Property<Boolean> ENABLE_PROTECTION_REGISTERED = PropertyInitializer.newProperty("Protection.enableProtectionRegistered", true);
  @Comment({"Countries allowed to join the server and register. For country codes, see", "http://dev.maxmind.com/geoip/legacy/codes/iso3166/", "PLEASE USE QUOTES!"})
  public static final Property<List<String>> COUNTRIES_WHITELIST = PropertyInitializer.newListProperty("Protection.countries", new String[] { "US", "GB" });
  @Comment({"Countries not allowed to join the server and register", "PLEASE USE QUOTES!"})
  public static final Property<List<String>> COUNTRIES_BLACKLIST = PropertyInitializer.newListProperty("Protection.countriesBlacklist", new String[] { "A1" });
  @Comment({"Do we need to enable automatic antibot system?"})
  public static final Property<Boolean> ENABLE_ANTIBOT = PropertyInitializer.newProperty("Protection.enableAntiBot", true);
  @Comment({"The interval in seconds"})
  public static final Property<Integer> ANTIBOT_INTERVAL = PropertyInitializer.newProperty("Protection.antiBotInterval", 5);
  @Comment({"Max number of players allowed to login in the interval", "before the AntiBot system is enabled automatically"})
  public static final Property<Integer> ANTIBOT_SENSIBILITY = PropertyInitializer.newProperty("Protection.antiBotSensibility", 10);
  @Comment({"Duration in minutes of the antibot automatic system"})
  public static final Property<Integer> ANTIBOT_DURATION = PropertyInitializer.newProperty("Protection.antiBotDuration", 10);
  @Comment({"Delay in seconds before the antibot activation"})
  public static final Property<Integer> ANTIBOT_DELAY = PropertyInitializer.newProperty("Protection.antiBotDelay", 60);
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\settings\properties\ProtectionSettings.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */