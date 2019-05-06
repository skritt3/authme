package fr.xephi.authme.settings;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.initialization.DataFolder;
import fr.xephi.authme.libs.google.common.base.MoreObjects;
import fr.xephi.authme.libs.jalu.configme.migration.PlainMigrationService;
import fr.xephi.authme.libs.jalu.configme.properties.Property;
import fr.xephi.authme.libs.jalu.configme.properties.PropertyInitializer;
import fr.xephi.authme.libs.jalu.configme.properties.StringListProperty;
import fr.xephi.authme.libs.jalu.configme.resource.PropertyResource;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.output.LogLevel;
import fr.xephi.authme.process.register.RegisterSecondaryArgument;
import fr.xephi.authme.process.register.RegistrationType;
import fr.xephi.authme.security.HashAlgorithm;
import fr.xephi.authme.settings.properties.PluginSettings;
import fr.xephi.authme.settings.properties.RegistrationSettings;
import fr.xephi.authme.settings.properties.RestrictionSettings;
import fr.xephi.authme.settings.properties.SecuritySettings;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class SettingsMigrationService
  extends PlainMigrationService
{
  private final File pluginFolder;
  private List<String> onLoginCommands = Collections.emptyList();
  private List<String> onLoginConsoleCommands = Collections.emptyList();
  private List<String> onRegisterCommands = Collections.emptyList();
  private List<String> onRegisterConsoleCommands = Collections.emptyList();
  
  @Inject
  SettingsMigrationService(@DataFolder File pluginFolder)
  {
    this.pluginFolder = pluginFolder;
  }
  
  protected boolean performMigrations(PropertyResource resource, List<Property<?>> properties)
  {
    boolean changes = false;
    if ("[a-zA-Z0-9_?]*".equals(resource.getString(RestrictionSettings.ALLOWED_NICKNAME_CHARACTERS.getPath())))
    {
      resource.setValue(RestrictionSettings.ALLOWED_NICKNAME_CHARACTERS.getPath(), "[a-zA-Z0-9_]*");
      changes = true;
    }
    gatherOldCommandSettings(resource);
    
    return 
    
      ((changes | performMailTextToFileMigration(resource) | migrateJoinLeaveMessages(resource) | migrateForceSpawnSettings(resource) | changeBooleanSettingToLogLevelProperty(resource) | hasOldHelpHeaderProperty(resource) | hasSupportOldPasswordProperty(resource) | convertToRegistrationType(resource) | mergeAndMovePermissionGroupSettings(resource) | moveDeprecatedHashAlgorithmIntoLegacySection(resource))) || 
      (hasDeprecatedProperties(resource));
  }
  
  private static boolean hasDeprecatedProperties(PropertyResource resource)
  {
    String[] deprecatedProperties = { "Converter.Rakamak.newPasswordHash", "Hooks.chestshop", "Hooks.legacyChestshop", "Hooks.notifications", "Passpartu", "Performances", "settings.restrictions.enablePasswordVerifier", "Xenoforo.predefinedSalt", "VeryGames", "settings.restrictions.allowAllCommandsIfRegistrationIsOptional", "DataSource.mySQLWebsite", "Hooks.customAttributes", "Security.stop.kickPlayersBeforeStopping", "settings.restrictions.keepCollisionsDisabled", "settings.forceCommands", "settings.forceCommandsAsConsole", "settings.forceRegisterCommands", "settings.forceRegisterCommandsAsConsole", "settings.sessions.sessionExpireOnIpChange" };
    for (String deprecatedPath : deprecatedProperties) {
      if (resource.contains(deprecatedPath)) {
        return true;
      }
    }
    return false;
  }
  
  private void gatherOldCommandSettings(PropertyResource resource)
  {
    this.onLoginCommands = getStringList(resource, "settings.forceCommands");
    this.onLoginConsoleCommands = getStringList(resource, "settings.forceCommandsAsConsole");
    this.onRegisterCommands = getStringList(resource, "settings.forceRegisterCommands");
    this.onRegisterConsoleCommands = getStringList(resource, "settings.forceRegisterCommandsAsConsole");
  }
  
  private List<String> getStringList(PropertyResource resource, String path)
  {
    return (List)new StringListProperty(path, new String[0]).getValue(resource);
  }
  
  public List<String> getOnLoginCommands()
  {
    return this.onLoginCommands;
  }
  
  public List<String> getOnLoginConsoleCommands()
  {
    return this.onLoginConsoleCommands;
  }
  
  public List<String> getOnRegisterCommands()
  {
    return this.onRegisterCommands;
  }
  
  public List<String> getOnRegisterConsoleCommands()
  {
    return this.onRegisterConsoleCommands;
  }
  
  private boolean performMailTextToFileMigration(PropertyResource resource)
  {
    String oldSettingPath = "Email.mailText";
    String oldMailText = resource.getString("Email.mailText");
    if (oldMailText == null) {
      return false;
    }
    File emailFile = new File(this.pluginFolder, "email.html");
    
    String mailText = oldMailText.replace("<playername>", "<playername />").replace("%playername%", "<playername />").replace("<servername>", "<servername />").replace("%servername%", "<servername />").replace("<generatedpass>", "<generatedpass />").replace("%generatedpass%", "<generatedpass />").replace("<image>", "<image />").replace("%image%", "<image />");
    if (!emailFile.exists()) {
      try
      {
        FileWriter fw = new FileWriter(emailFile);Throwable localThrowable3 = null;
        try
        {
          fw.write(mailText);
        }
        catch (Throwable localThrowable1)
        {
          localThrowable3 = localThrowable1;throw localThrowable1;
        }
        finally
        {
          if (fw != null) {
            if (localThrowable3 != null) {
              try
              {
                fw.close();
              }
              catch (Throwable localThrowable2)
              {
                localThrowable3.addSuppressed(localThrowable2);
              }
            } else {
              fw.close();
            }
          }
        }
      }
      catch (IOException e)
      {
        ConsoleLogger.logException("Could not create email.html configuration file:", e);
      }
    }
    return true;
  }
  
  private static boolean migrateJoinLeaveMessages(PropertyResource resource)
  {
    Property<Boolean> oldDelayJoinProperty = PropertyInitializer.newProperty("settings.delayJoinLeaveMessages", false);
    boolean hasMigrated = moveProperty(oldDelayJoinProperty, RegistrationSettings.DELAY_JOIN_MESSAGE, resource);
    if (hasMigrated) {
      ConsoleLogger.info(String.format("Note that we now also have the settings %s and %s", new Object[] {RegistrationSettings.REMOVE_JOIN_MESSAGE
        .getPath(), RegistrationSettings.REMOVE_LEAVE_MESSAGE.getPath() }));
    }
    return hasMigrated;
  }
  
  private static boolean migrateForceSpawnSettings(PropertyResource resource)
  {
    Property<Boolean> oldForceLocEnabled = PropertyInitializer.newProperty("settings.restrictions.ForceSpawnLocOnJoinEnabled", false);
    
    Property<List<String>> oldForceWorlds = PropertyInitializer.newListProperty("settings.restrictions.ForceSpawnOnTheseWorlds", new String[] { "world", "world_nether", "world_the_ed" });
    
    return moveProperty(oldForceLocEnabled, RestrictionSettings.FORCE_SPAWN_LOCATION_AFTER_LOGIN, resource) | 
      moveProperty(oldForceWorlds, RestrictionSettings.FORCE_SPAWN_ON_WORLDS, resource);
  }
  
  private static boolean changeBooleanSettingToLogLevelProperty(PropertyResource resource)
  {
    String oldPath = "Security.console.noConsoleSpam";
    Property<LogLevel> newProperty = PluginSettings.LOG_LEVEL;
    if ((!newProperty.isPresent(resource)) && (resource.contains("Security.console.noConsoleSpam")))
    {
      ConsoleLogger.info("Moving 'Security.console.noConsoleSpam' to '" + newProperty.getPath() + "'");
      boolean oldValue = ((Boolean)MoreObjects.firstNonNull(resource.getBoolean("Security.console.noConsoleSpam"), Boolean.valueOf(false))).booleanValue();
      LogLevel level = oldValue ? LogLevel.INFO : LogLevel.FINE;
      resource.setValue(newProperty.getPath(), level.name());
      return true;
    }
    return false;
  }
  
  private static boolean hasOldHelpHeaderProperty(PropertyResource resource)
  {
    if (resource.contains("settings.helpHeader"))
    {
      ConsoleLogger.warning("Help header setting is now in messages/help_xx.yml, please check the file to set it again");
      
      return true;
    }
    return false;
  }
  
  private static boolean hasSupportOldPasswordProperty(PropertyResource resource)
  {
    String path = "settings.security.supportOldPasswordHash";
    if (resource.contains(path))
    {
      ConsoleLogger.warning(
        "Property '" + path + "' is no longer supported. Use '" + SecuritySettings.LEGACY_HASHES.getPath() + "' instead.");
      return true;
    }
    return false;
  }
  
  private static boolean convertToRegistrationType(PropertyResource resource)
  {
    String oldEmailRegisterPath = "settings.registration.enableEmailRegistrationSystem";
    if ((RegistrationSettings.REGISTRATION_TYPE.isPresent(resource)) || (!resource.contains(oldEmailRegisterPath))) {
      return false;
    }
    boolean useEmail = ((Boolean)PropertyInitializer.newProperty(oldEmailRegisterPath, false).getValue(resource)).booleanValue();
    RegistrationType registrationType = useEmail ? RegistrationType.EMAIL : RegistrationType.PASSWORD;
    
    String useConfirmationPath = useEmail ? "settings.registration.doubleEmailCheck" : "settings.restrictions.enablePasswordConfirmation";
    
    boolean hasConfirmation = ((Boolean)PropertyInitializer.newProperty(useConfirmationPath, false).getValue(resource)).booleanValue();
    RegisterSecondaryArgument secondaryArgument = hasConfirmation ? RegisterSecondaryArgument.CONFIRMATION : RegisterSecondaryArgument.NONE;
    
    ConsoleLogger.warning("Merging old registration settings into '" + RegistrationSettings.REGISTRATION_TYPE
      .getPath() + "'");
    resource.setValue(RegistrationSettings.REGISTRATION_TYPE.getPath(), registrationType);
    resource.setValue(RegistrationSettings.REGISTER_SECOND_ARGUMENT.getPath(), secondaryArgument);
    return true;
  }
  
  private static boolean mergeAndMovePermissionGroupSettings(PropertyResource resource)
  {
    Property<String> oldUnloggedInGroup = PropertyInitializer.newProperty("settings.security.unLoggedinGroup", "");
    Property<String> oldRegisteredGroup = PropertyInitializer.newProperty("GroupOptions.RegisteredPlayerGroup", "");
    boolean performedChanges;
    if (!((String)oldUnloggedInGroup.getValue(resource)).isEmpty()) {
      performedChanges = moveProperty(oldUnloggedInGroup, PluginSettings.REGISTERED_GROUP, resource);
    } else {
      performedChanges = moveProperty(oldRegisteredGroup, PluginSettings.REGISTERED_GROUP, resource);
    }
    performedChanges |= moveProperty(PropertyInitializer.newProperty("GroupOptions.UnregisteredPlayerGroup", ""), PluginSettings.UNREGISTERED_GROUP, resource);
    
    performedChanges |= moveProperty(PropertyInitializer.newProperty("permission.EnablePermissionCheck", false), PluginSettings.ENABLE_PERMISSION_CHECK, resource);
    
    return performedChanges;
  }
  
  private static boolean moveDeprecatedHashAlgorithmIntoLegacySection(PropertyResource resource)
  {
    HashAlgorithm currentHash = (HashAlgorithm)SecuritySettings.PASSWORD_HASH.getValue(resource);
    if ((currentHash != HashAlgorithm.CUSTOM) && (currentHash != HashAlgorithm.PLAINTEXT))
    {
      Class<?> encryptionClass = currentHash.getClazz();
      if (encryptionClass.isAnnotationPresent(Deprecated.class))
      {
        resource.setValue(SecuritySettings.PASSWORD_HASH.getPath(), HashAlgorithm.SHA256);
        Set<HashAlgorithm> legacyHashes = (Set)SecuritySettings.LEGACY_HASHES.getValue(resource);
        legacyHashes.add(currentHash);
        resource.setValue(SecuritySettings.LEGACY_HASHES.getPath(), legacyHashes);
        ConsoleLogger.warning("The hash algorithm '" + currentHash + "' is no longer supported for active use. New hashes will be in SHA256.");
        
        return true;
      }
    }
    return false;
  }
  
  private static <T> boolean moveProperty(Property<T> oldProperty, Property<T> newProperty, PropertyResource resource)
  {
    if (resource.contains(oldProperty.getPath()))
    {
      if (resource.contains(newProperty.getPath()))
      {
        ConsoleLogger.info("Detected deprecated property " + oldProperty.getPath());
      }
      else
      {
        ConsoleLogger.info("Renaming " + oldProperty.getPath() + " to " + newProperty.getPath());
        resource.setValue(newProperty.getPath(), oldProperty.getValue(resource));
      }
      return true;
    }
    return false;
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\settings\SettingsMigrationService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */