package fr.xephi.authme.command;

import fr.xephi.authme.command.executable.HelpCommand;
import fr.xephi.authme.command.executable.authme.AccountsCommand;
import fr.xephi.authme.command.executable.authme.AuthMeCommand;
import fr.xephi.authme.command.executable.authme.BackupCommand;
import fr.xephi.authme.command.executable.authme.ChangePasswordAdminCommand;
import fr.xephi.authme.command.executable.authme.ConverterCommand;
import fr.xephi.authme.command.executable.authme.FirstSpawnCommand;
import fr.xephi.authme.command.executable.authme.ForceLoginCommand;
import fr.xephi.authme.command.executable.authme.GetEmailCommand;
import fr.xephi.authme.command.executable.authme.GetIpCommand;
import fr.xephi.authme.command.executable.authme.LastLoginCommand;
import fr.xephi.authme.command.executable.authme.MessagesCommand;
import fr.xephi.authme.command.executable.authme.PurgeBannedPlayersCommand;
import fr.xephi.authme.command.executable.authme.PurgeCommand;
import fr.xephi.authme.command.executable.authme.PurgeLastPositionCommand;
import fr.xephi.authme.command.executable.authme.PurgePlayerCommand;
import fr.xephi.authme.command.executable.authme.RegisterAdminCommand;
import fr.xephi.authme.command.executable.authme.ReloadCommand;
import fr.xephi.authme.command.executable.authme.SetEmailCommand;
import fr.xephi.authme.command.executable.authme.SetFirstSpawnCommand;
import fr.xephi.authme.command.executable.authme.SetSpawnCommand;
import fr.xephi.authme.command.executable.authme.SpawnCommand;
import fr.xephi.authme.command.executable.authme.SwitchAntiBotCommand;
import fr.xephi.authme.command.executable.authme.UnregisterAdminCommand;
import fr.xephi.authme.command.executable.authme.VersionCommand;
import fr.xephi.authme.command.executable.authme.debug.DebugCommand;
import fr.xephi.authme.command.executable.captcha.CaptchaCommand;
import fr.xephi.authme.command.executable.changepassword.ChangePasswordCommand;
import fr.xephi.authme.command.executable.email.AddEmailCommand;
import fr.xephi.authme.command.executable.email.ChangeEmailCommand;
import fr.xephi.authme.command.executable.email.EmailBaseCommand;
import fr.xephi.authme.command.executable.email.ProcessCodeCommand;
import fr.xephi.authme.command.executable.email.RecoverEmailCommand;
import fr.xephi.authme.command.executable.email.SetPasswordCommand;
import fr.xephi.authme.command.executable.email.ShowEmailCommand;
import fr.xephi.authme.command.executable.login.LoginCommand;
import fr.xephi.authme.command.executable.logout.LogoutCommand;
import fr.xephi.authme.command.executable.register.RegisterCommand;
import fr.xephi.authme.command.executable.unregister.UnregisterCommand;
import fr.xephi.authme.command.executable.verification.VerificationCommand;
import fr.xephi.authme.libs.google.common.collect.ImmutableList;
import fr.xephi.authme.permission.AdminPermission;
import fr.xephi.authme.permission.DebugSectionPermissions;
import fr.xephi.authme.permission.PlayerPermission;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class CommandInitializer
{
  private List<CommandDescription> commands;
  
  public CommandInitializer()
  {
    buildCommands();
  }
  
  public List<CommandDescription> getCommands()
  {
    return this.commands;
  }
  
  private void buildCommands()
  {
    CommandDescription authMeBase = buildAuthMeBaseCommand();
    CommandDescription emailBase = buildEmailBaseCommand();
    
    CommandDescription loginBase = CommandDescription.builder().parent(null).labels(new String[] { "login", "l", "log" }).description("Login command").detailedDescription("Command to log in using AuthMeReloaded.").withArgument("password", "Login password", false).permission(PlayerPermission.LOGIN).executableCommand(LoginCommand.class).register();
    
    CommandDescription logoutBase = CommandDescription.builder().parent(null).labels(new String[] { "logout" }).description("Logout command").detailedDescription("Command to logout using AuthMeReloaded.").permission(PlayerPermission.LOGOUT).executableCommand(LogoutCommand.class).register();
    
    CommandDescription registerBase = CommandDescription.builder().parent(null).labels(new String[] { "register", "reg" }).description("Register an account").detailedDescription("Command to register using AuthMeReloaded.").withArgument("password", "Password", true).withArgument("verifyPassword", "Verify password", true).permission(PlayerPermission.REGISTER).executableCommand(RegisterCommand.class).register();
    
    CommandDescription unregisterBase = CommandDescription.builder().parent(null).labels(new String[] { "unregister", "unreg" }).description("Unregister an account").detailedDescription("Command to unregister using AuthMeReloaded.").withArgument("password", "Password", false).permission(PlayerPermission.UNREGISTER).executableCommand(UnregisterCommand.class).register();
    
    CommandDescription changePasswordBase = CommandDescription.builder().parent(null).labels(new String[] { "changepassword", "changepass", "cp" }).description("Change password of an account").detailedDescription("Command to change your password using AuthMeReloaded.").withArgument("oldPassword", "Old password", false).withArgument("newPassword", "New password", false).permission(PlayerPermission.CHANGE_PASSWORD).executableCommand(ChangePasswordCommand.class).register();
    
    CommandDescription captchaBase = CommandDescription.builder().parent(null).labels(new String[] { "captcha" }).description("Captcha command").detailedDescription("Captcha command for AuthMeReloaded.").withArgument("captcha", "The Captcha", false).permission(PlayerPermission.CAPTCHA).executableCommand(CaptchaCommand.class).register();
    
    CommandDescription verificationBase = CommandDescription.builder().parent(null).labels(new String[] { "verification" }).description("Verification command").detailedDescription("Command to complete the verification process for AuthMeReloaded.").withArgument("code", "The code", false).permission(PlayerPermission.VERIFICATION_CODE).executableCommand(VerificationCommand.class).register();
    
    List<CommandDescription> baseCommands = ImmutableList.of(authMeBase, emailBase, loginBase, logoutBase, registerBase, unregisterBase, changePasswordBase, captchaBase, verificationBase);
    
    setHelpOnAllBases(baseCommands);
    this.commands = baseCommands;
  }
  
  private CommandDescription buildAuthMeBaseCommand()
  {
    CommandDescription authmeBase = CommandDescription.builder().labels(new String[] { "authme" }).description("AuthMe op commands").detailedDescription("The main AuthMeReloaded command. The root for all admin commands.").executableCommand(AuthMeCommand.class).register();
    
    CommandDescription.builder()
      .parent(authmeBase)
      .labels(new String[] { "register", "reg", "r" })
      .description("Register a player")
      .detailedDescription("Register the specified player with the specified password.")
      .withArgument("player", "Player name", false)
      .withArgument("password", "Password", false)
      .permission(AdminPermission.REGISTER)
      .executableCommand(RegisterAdminCommand.class)
      .register();
    
    CommandDescription.builder()
      .parent(authmeBase)
      .labels(new String[] { "unregister", "unreg", "unr" })
      .description("Unregister a player")
      .detailedDescription("Unregister the specified player.")
      .withArgument("player", "Player name", false)
      .permission(AdminPermission.UNREGISTER)
      .executableCommand(UnregisterAdminCommand.class)
      .register();
    
    CommandDescription.builder()
      .parent(authmeBase)
      .labels(new String[] { "forcelogin", "login" })
      .description("Enforce login player")
      .detailedDescription("Enforce the specified player to login.")
      .withArgument("player", "Online player name", true)
      .permission(AdminPermission.FORCE_LOGIN)
      .executableCommand(ForceLoginCommand.class)
      .register();
    
    CommandDescription.builder()
      .parent(authmeBase)
      .labels(new String[] { "password", "changepassword", "changepass", "cp" })
      .description("Change a player's password")
      .detailedDescription("Change the password of a player.")
      .withArgument("player", "Player name", false)
      .withArgument("pwd", "New password", false)
      .permission(AdminPermission.CHANGE_PASSWORD)
      .executableCommand(ChangePasswordAdminCommand.class)
      .register();
    
    CommandDescription.builder()
      .parent(authmeBase)
      .labels(new String[] { "lastlogin", "ll" })
      .description("Player's last login")
      .detailedDescription("View the date of the specified players last login.")
      .withArgument("player", "Player name", true)
      .permission(AdminPermission.LAST_LOGIN)
      .executableCommand(LastLoginCommand.class)
      .register();
    
    CommandDescription.builder()
      .parent(authmeBase)
      .labels(new String[] { "accounts", "account" })
      .description("Display player accounts")
      .detailedDescription("Display all accounts of a player by his player name or IP.")
      .withArgument("player", "Player name or IP", true)
      .permission(AdminPermission.ACCOUNTS)
      .executableCommand(AccountsCommand.class)
      .register();
    
    CommandDescription.builder()
      .parent(authmeBase)
      .labels(new String[] { "email", "mail", "getemail", "getmail" })
      .description("Display player's email")
      .detailedDescription("Display the email address of the specified player if set.")
      .withArgument("player", "Player name", true)
      .permission(AdminPermission.GET_EMAIL)
      .executableCommand(GetEmailCommand.class)
      .register();
    
    CommandDescription.builder()
      .parent(authmeBase)
      .labels(new String[] { "setemail", "setmail", "chgemail", "chgmail" })
      .description("Change player's email")
      .detailedDescription("Change the email address of the specified player.")
      .withArgument("player", "Player name", false)
      .withArgument("email", "Player email", false)
      .permission(AdminPermission.CHANGE_EMAIL)
      .executableCommand(SetEmailCommand.class)
      .register();
    
    CommandDescription.builder()
      .parent(authmeBase)
      .labels(new String[] { "getip", "ip" })
      .description("Get player's IP")
      .detailedDescription("Get the IP address of the specified online player.")
      .withArgument("player", "Player name", false)
      .permission(AdminPermission.GET_IP)
      .executableCommand(GetIpCommand.class)
      .register();
    
    CommandDescription.builder()
      .parent(authmeBase)
      .labels(new String[] { "spawn", "home" })
      .description("Teleport to spawn")
      .detailedDescription("Teleport to the spawn.")
      .permission(AdminPermission.SPAWN)
      .executableCommand(SpawnCommand.class)
      .register();
    
    CommandDescription.builder()
      .parent(authmeBase)
      .labels(new String[] { "setspawn", "chgspawn" })
      .description("Change the spawn")
      .detailedDescription("Change the player's spawn to your current position.")
      .permission(AdminPermission.SET_SPAWN)
      .executableCommand(SetSpawnCommand.class)
      .register();
    
    CommandDescription.builder()
      .parent(authmeBase)
      .labels(new String[] { "firstspawn", "firsthome" })
      .description("Teleport to first spawn")
      .detailedDescription("Teleport to the first spawn.")
      .permission(AdminPermission.FIRST_SPAWN)
      .executableCommand(FirstSpawnCommand.class)
      .register();
    
    CommandDescription.builder()
      .parent(authmeBase)
      .labels(new String[] { "setfirstspawn", "chgfirstspawn" })
      .description("Change the first spawn")
      .detailedDescription("Change the first player's spawn to your current position.")
      .permission(AdminPermission.SET_FIRST_SPAWN)
      .executableCommand(SetFirstSpawnCommand.class)
      .register();
    
    CommandDescription.builder()
      .parent(authmeBase)
      .labels(new String[] { "purge", "delete" })
      .description("Purge old data")
      .detailedDescription("Purge old AuthMeReloaded data longer than the specified number of days ago.")
      .withArgument("days", "Number of days", false)
      .permission(AdminPermission.PURGE)
      .executableCommand(PurgeCommand.class)
      .register();
    
    CommandDescription.builder()
      .parent(authmeBase)
      .labels(new String[] { "purgeplayer" })
      .description("Purges the data of one player")
      .detailedDescription("Purges data of the given player.")
      .withArgument("player", "The player to purge", false)
      .withArgument("options", "'force' to run without checking if player is registered", true)
      .permission(AdminPermission.PURGE_PLAYER)
      .executableCommand(PurgePlayerCommand.class)
      .register();
    
    CommandDescription.builder()
      .parent(authmeBase)
      .labels(new String[] { "backup" })
      .description("Perform a backup")
      .detailedDescription("Creates a backup of the registered users.")
      .permission(AdminPermission.BACKUP)
      .executableCommand(BackupCommand.class)
      .register();
    
    CommandDescription.builder()
      .parent(authmeBase)
      .labels(new String[] { "resetpos", "purgelastposition", "purgelastpos", "resetposition", "resetlastposition", "resetlastpos" })
      
      .description("Purge player's last position")
      .detailedDescription("Purge the last know position of the specified player or all of them.")
      .withArgument("player/*", "Player name or * for all players", false)
      .permission(AdminPermission.PURGE_LAST_POSITION)
      .executableCommand(PurgeLastPositionCommand.class)
      .register();
    
    CommandDescription.builder()
      .parent(authmeBase)
      .labels(new String[] { "purgebannedplayers", "purgebannedplayer", "deletebannedplayers", "deletebannedplayer" })
      .description("Purge banned players data")
      .detailedDescription("Purge all AuthMeReloaded data for banned players.")
      .permission(AdminPermission.PURGE_BANNED_PLAYERS)
      .executableCommand(PurgeBannedPlayersCommand.class)
      .register();
    
    CommandDescription.builder()
      .parent(authmeBase)
      .labels(new String[] { "switchantibot", "toggleantibot", "antibot" })
      .description("Switch AntiBot mode")
      .detailedDescription("Switch or toggle the AntiBot mode to the specified state.")
      .withArgument("mode", "ON / OFF", true)
      .permission(AdminPermission.SWITCH_ANTIBOT)
      .executableCommand(SwitchAntiBotCommand.class)
      .register();
    
    CommandDescription.builder()
      .parent(authmeBase)
      .labels(new String[] { "reload", "rld" })
      .description("Reload plugin")
      .detailedDescription("Reload the AuthMeReloaded plugin.")
      .permission(AdminPermission.RELOAD)
      .executableCommand(ReloadCommand.class)
      .register();
    
    CommandDescription.builder()
      .parent(authmeBase)
      .labels(new String[] { "version", "ver", "v", "about", "info" })
      .description("Version info")
      .detailedDescription("Show detailed information about the installed AuthMeReloaded version, the developers, contributors, and license.")
      
      .executableCommand(VersionCommand.class)
      .register();
    
    CommandDescription.builder()
      .parent(authmeBase)
      .labels(new String[] { "converter", "convert", "conv" })
      .description("Converter command")
      .detailedDescription("Converter command for AuthMeReloaded.")
      .withArgument("job", "Conversion job: xauth / crazylogin / rakamak / royalauth / vauth / sqliteToSql / mysqlToSqlite / loginsecurity", true)
      
      .permission(AdminPermission.CONVERTER)
      .executableCommand(ConverterCommand.class)
      .register();
    
    CommandDescription.builder()
      .parent(authmeBase)
      .labels(new String[] { "messages", "msg" })
      .description("Add missing messages")
      .detailedDescription("Adds missing messages to the current messages file.")
      .withArgument("help", "Add 'help' to update the help messages file", true)
      .permission(AdminPermission.UPDATE_MESSAGES)
      .executableCommand(MessagesCommand.class)
      .register();
    
    CommandDescription.builder()
      .parent(authmeBase)
      .labels(new String[] { "debug", "dbg" })
      .description("Debug features")
      .detailedDescription("Allows various operations for debugging.")
      .withArgument("child", "The child to execute", true)
      .withArgument("arg", "argument (depends on debug section)", true)
      .withArgument("arg", "argument (depends on debug section)", true)
      .permission(DebugSectionPermissions.DEBUG_COMMAND)
      .executableCommand(DebugCommand.class)
      .register();
    
    return authmeBase;
  }
  
  private CommandDescription buildEmailBaseCommand()
  {
    CommandDescription emailBase = CommandDescription.builder().parent(null).labels(new String[] { "email" }).description("Add email or recover password").detailedDescription("The AuthMeReloaded email command base.").executableCommand(EmailBaseCommand.class).register();
    
    CommandDescription.builder()
      .parent(emailBase)
      .labels(new String[] { "show", "myemail" })
      .description("Show Email")
      .detailedDescription("Show your current email address.")
      .permission(PlayerPermission.SEE_EMAIL)
      .executableCommand(ShowEmailCommand.class)
      .register();
    
    CommandDescription.builder()
      .parent(emailBase)
      .labels(new String[] { "add", "addemail", "addmail" })
      .description("Add Email")
      .detailedDescription("Add a new email address to your account.")
      .withArgument("email", "Email address", false)
      .withArgument("verifyEmail", "Email address verification", false)
      .permission(PlayerPermission.ADD_EMAIL)
      .executableCommand(AddEmailCommand.class)
      .register();
    
    CommandDescription.builder()
      .parent(emailBase)
      .labels(new String[] { "change", "changeemail", "changemail" })
      .description("Change Email")
      .detailedDescription("Change an email address of your account.")
      .withArgument("oldEmail", "Old email address", false)
      .withArgument("newEmail", "New email address", false)
      .permission(PlayerPermission.CHANGE_EMAIL)
      .executableCommand(ChangeEmailCommand.class)
      .register();
    
    CommandDescription.builder()
      .parent(emailBase)
      .labels(new String[] { "recover", "recovery", "recoveremail", "recovermail" })
      .description("Recover password using email")
      .detailedDescription("Recover your account using an Email address by sending a mail containing a new password.")
      
      .withArgument("email", "Email address", false)
      .permission(PlayerPermission.RECOVER_EMAIL)
      .executableCommand(RecoverEmailCommand.class)
      .register();
    
    CommandDescription.builder()
      .parent(emailBase)
      .labels(new String[] { "code" })
      .description("Submit code to recover password")
      .detailedDescription("Recover your account by submitting a code delivered to your email.")
      .withArgument("code", "Recovery code", false)
      .permission(PlayerPermission.RECOVER_EMAIL)
      .executableCommand(ProcessCodeCommand.class)
      .register();
    
    CommandDescription.builder()
      .parent(emailBase)
      .labels(new String[] { "setpassword" })
      .description("Set new password after recovery")
      .detailedDescription("Set a new password after successfully recovering your account.")
      .withArgument("password", "New password", false)
      .permission(PlayerPermission.RECOVER_EMAIL)
      .executableCommand(SetPasswordCommand.class)
      .register();
    
    return emailBase;
  }
  
  private void setHelpOnAllBases(Collection<CommandDescription> commands)
  {
    List<String> helpCommandLabels = Arrays.asList(new String[] { "help", "hlp", "h", "sos", "?" });
    for (CommandDescription base : commands) {
      CommandDescription.builder().parent(base).labels(helpCommandLabels).description("View help").detailedDescription("View detailed help for /" + (String)base.getLabels().get(0) + " commands.").withArgument("query", "The command or query to view help for.", true).executableCommand(HelpCommand.class).register();
    }
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\command\CommandInitializer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */