package fr.xephi.authme.settings.properties;

import fr.xephi.authme.libs.jalu.configme.Comment;
import fr.xephi.authme.libs.jalu.configme.SettingsHolder;
import fr.xephi.authme.libs.jalu.configme.properties.Property;
import fr.xephi.authme.libs.jalu.configme.properties.PropertyInitializer;
import java.util.List;

public final class RestrictionSettings
  implements SettingsHolder
{
  @Comment({"Can not authenticated players chat?", "Keep in mind that this feature also blocks all commands not", "listed in the list below."})
  public static final Property<Boolean> ALLOW_CHAT = PropertyInitializer.newProperty("settings.restrictions.allowChat", false);
  @Comment({"Hide the chat log from players who are not authenticated?"})
  public static final Property<Boolean> HIDE_CHAT = PropertyInitializer.newProperty("settings.restrictions.hideChat", false);
  @Comment({"Allowed commands for unauthenticated players"})
  public static final Property<List<String>> ALLOW_COMMANDS = PropertyInitializer.newLowercaseListProperty("settings.restrictions.allowCommands", new String[] { "/login", "/register", "/l", "/reg", "/email", "/captcha" });
  @Comment({"Max number of allowed registrations per IP", "The value 0 means an unlimited number of registrations!"})
  public static final Property<Integer> MAX_REGISTRATION_PER_IP = PropertyInitializer.newProperty("settings.restrictions.maxRegPerIp", 1);
  @Comment({"Minimum allowed username length"})
  public static final Property<Integer> MIN_NICKNAME_LENGTH = PropertyInitializer.newProperty("settings.restrictions.minNicknameLength", 3);
  @Comment({"Maximum allowed username length"})
  public static final Property<Integer> MAX_NICKNAME_LENGTH = PropertyInitializer.newProperty("settings.restrictions.maxNicknameLength", 16);
  @Comment({"When this setting is enabled, online players can't be kicked out", "due to \"Logged in from another Location\"", "This setting will prevent potential security exploits."})
  public static final Property<Boolean> FORCE_SINGLE_SESSION = PropertyInitializer.newProperty("settings.restrictions.ForceSingleSession", true);
  @Comment({"If enabled, every player that spawn in one of the world listed in", "\"ForceSpawnLocOnJoin.worlds\" will be teleported to the spawnpoint after successful", "authentication. The quit location of the player will be overwritten.", "This is different from \"teleportUnAuthedToSpawn\" that teleport player", "to the spawnpoint on join."})
  public static final Property<Boolean> FORCE_SPAWN_LOCATION_AFTER_LOGIN = PropertyInitializer.newProperty("settings.restrictions.ForceSpawnLocOnJoin.enabled", false);
  @Comment({"WorldNames where we need to force the spawn location", "Case-sensitive!"})
  public static final Property<List<String>> FORCE_SPAWN_ON_WORLDS = PropertyInitializer.newListProperty("settings.restrictions.ForceSpawnLocOnJoin.worlds", new String[] { "world", "world_nether", "world_the_end" });
  @Comment({"This option will save the quit location of the players."})
  public static final Property<Boolean> SAVE_QUIT_LOCATION = PropertyInitializer.newProperty("settings.restrictions.SaveQuitLocation", false);
  @Comment({"To activate the restricted user feature you need", "to enable this option and configure the AllowedRestrictedUser field."})
  public static final Property<Boolean> ENABLE_RESTRICTED_USERS = PropertyInitializer.newProperty("settings.restrictions.AllowRestrictedUser", false);
  @Comment({"The restricted user feature will kick players listed below", "if they don't match the defined IP address. Names are case-insensitive.", "You can use * as wildcard (127.0.0.*), or regex with a \"regex:\" prefix regex:127\\.0\\.0\\..*", "Example:", "    AllowedRestrictedUser:", "    - playername;127.0.0.1", "    - playername;regex:127\\.0\\.0\\..*"})
  public static final Property<List<String>> RESTRICTED_USERS = PropertyInitializer.newLowercaseListProperty("settings.restrictions.AllowedRestrictedUser", new String[0]);
  @Comment({"Ban unknown IPs trying to log in with a restricted username?"})
  public static final Property<Boolean> BAN_UNKNOWN_IP = PropertyInitializer.newProperty("settings.restrictions.banUnsafedIP", false);
  @Comment({"Should unregistered players be kicked immediately?"})
  public static final Property<Boolean> KICK_NON_REGISTERED = PropertyInitializer.newProperty("settings.restrictions.kickNonRegistered", false);
  @Comment({"Should players be kicked on wrong password?"})
  public static final Property<Boolean> KICK_ON_WRONG_PASSWORD = PropertyInitializer.newProperty("settings.restrictions.kickOnWrongPassword", true);
  @Comment({"Should not logged in players be teleported to the spawn?", "After the authentication they will be teleported back to", "their normal position."})
  public static final Property<Boolean> TELEPORT_UNAUTHED_TO_SPAWN = PropertyInitializer.newProperty("settings.restrictions.teleportUnAuthedToSpawn", false);
  @Comment({"Can unregistered players walk around?"})
  public static final Property<Boolean> ALLOW_UNAUTHED_MOVEMENT = PropertyInitializer.newProperty("settings.restrictions.allowMovement", false);
  @Comment({"After how many seconds should players who fail to login or register", "be kicked? Set to 0 to disable."})
  public static final Property<Integer> TIMEOUT = PropertyInitializer.newProperty("settings.restrictions.timeout", 30);
  @Comment({"Regex pattern of allowed characters in the player name."})
  public static final Property<String> ALLOWED_NICKNAME_CHARACTERS = PropertyInitializer.newProperty("settings.restrictions.allowedNicknameCharacters", "[a-zA-Z0-9_]*");
  @Comment({"How far can unregistered players walk?", "Set to 0 for unlimited radius"})
  public static final Property<Integer> ALLOWED_MOVEMENT_RADIUS = PropertyInitializer.newProperty("settings.restrictions.allowedMovementRadius", 100);
  @Comment({"Should we protect the player inventory before logging in? Requires ProtocolLib."})
  public static final Property<Boolean> PROTECT_INVENTORY_BEFORE_LOGIN = PropertyInitializer.newProperty("settings.restrictions.ProtectInventoryBeforeLogIn", true);
  @Comment({"Should we deny the tabcomplete feature before logging in? Requires ProtocolLib."})
  public static final Property<Boolean> DENY_TABCOMPLETE_BEFORE_LOGIN = PropertyInitializer.newProperty("settings.restrictions.DenyTabCompleteBeforeLogin", false);
  @Comment({"Should we display all other accounts from a player when he joins?", "permission: /authme.admin.accounts"})
  public static final Property<Boolean> DISPLAY_OTHER_ACCOUNTS = PropertyInitializer.newProperty("settings.restrictions.displayOtherAccounts", true);
  @Comment({"Spawn priority; values: authme, essentials, multiverse, default"})
  public static final Property<String> SPAWN_PRIORITY = PropertyInitializer.newProperty("settings.restrictions.spawnPriority", "authme,essentials,multiverse,default");
  @Comment({"Maximum Login authorized by IP"})
  public static final Property<Integer> MAX_LOGIN_PER_IP = PropertyInitializer.newProperty("settings.restrictions.maxLoginPerIp", 0);
  @Comment({"Maximum Join authorized by IP"})
  public static final Property<Integer> MAX_JOIN_PER_IP = PropertyInitializer.newProperty("settings.restrictions.maxJoinPerIp", 0);
  @Comment({"AuthMe will NEVER teleport players if set to true!"})
  public static final Property<Boolean> NO_TELEPORT = PropertyInitializer.newProperty("settings.restrictions.noTeleport", false);
  @Comment({"Regex syntax for allowed chars in passwords. The default [!-~] allows all visible ASCII", "characters, which is what we recommend. See also http://asciitable.com", "You can test your regex with https://regex101.com"})
  public static final Property<String> ALLOWED_PASSWORD_REGEX = PropertyInitializer.newProperty("settings.restrictions.allowedPasswordCharacters", "[!-~]*");
  @Comment({"Force survival gamemode when player joins?"})
  public static final Property<Boolean> FORCE_SURVIVAL_MODE = PropertyInitializer.newProperty("settings.GameMode.ForceSurvivalMode", false);
  @Comment({"Below you can list all account names that AuthMe will ignore", "for registration or login. Configure it at your own risk!!", "This option adds compatibility with BuildCraft and some other mods.", "It is case-insensitive! Example:", "UnrestrictedName:", "- 'npcPlayer'", "- 'npcPlayer2'"})
  public static final Property<List<String>> UNRESTRICTED_NAMES = PropertyInitializer.newLowercaseListProperty("settings.unrestrictions.UnrestrictedName", new String[0]);
  @Comment({"Threshold of the other accounts command, a value less than 2 means disabled."})
  public static final Property<Integer> OTHER_ACCOUNTS_CMD_THRESHOLD = PropertyInitializer.newProperty("settings.restrictions.otherAccountsCmdThreshold", 0);
  @Comment({"Command to run when a user has more accounts than the configured threshold.", "Available variables: %playername%, %playerip%"})
  public static final Property<String> OTHER_ACCOUNTS_CMD = PropertyInitializer.newProperty("settings.restrictions.otherAccountsCmd", "say The player %playername% with ip %playerip% has multiple accounts!");
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\settings\properties\RestrictionSettings.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */