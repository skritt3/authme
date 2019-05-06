package fr.xephi.authme.command.executable.authme.debug;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.datasource.DataSourceResult;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.mail.SendMailSsl;
import fr.xephi.authme.permission.DebugSectionPermissions;
import fr.xephi.authme.permission.PermissionNode;
import fr.xephi.authme.util.StringUtils;
import fr.xephi.authme.util.Utils;
import java.util.List;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;

class TestEmailSender
  implements DebugSection
{
  @Inject
  private DataSource dataSource;
  @Inject
  private SendMailSsl sendMailSsl;
  @Inject
  private Server server;
  
  public String getName()
  {
    return "mail";
  }
  
  public String getDescription()
  {
    return "Sends out a test email";
  }
  
  public void execute(CommandSender sender, List<String> arguments)
  {
    sender.sendMessage(ChatColor.BLUE + "AuthMe test email sender");
    if (!this.sendMailSsl.hasAllInformation())
    {
      sender.sendMessage(ChatColor.RED + "You haven't set all required configurations in config.yml for sending emails. Please check your config.yml");
      
      return;
    }
    String email = getEmail(sender, arguments);
    if (email != null)
    {
      boolean sendMail = sendTestEmail(email);
      if (sendMail) {
        sender.sendMessage("Test email sent to " + email + " with success");
      } else {
        sender.sendMessage(ChatColor.RED + "Failed to send test mail to " + email + "; please check your logs");
      }
    }
  }
  
  public PermissionNode getRequiredPermission()
  {
    return DebugSectionPermissions.TEST_EMAIL;
  }
  
  private String getEmail(CommandSender sender, List<String> arguments)
  {
    if (arguments.isEmpty())
    {
      DataSourceResult<String> emailResult = this.dataSource.getEmail(sender.getName());
      if (!emailResult.playerExists())
      {
        sender.sendMessage(ChatColor.RED + "Please provide an email address, e.g. /authme debug mail test@example.com");
        
        return null;
      }
      String email = (String)emailResult.getValue();
      if (Utils.isEmailEmpty(email))
      {
        sender.sendMessage(ChatColor.RED + "No email set for your account! Please use /authme debug mail <email>");
        
        return null;
      }
      return email;
    }
    String email = (String)arguments.get(0);
    if (StringUtils.isInsideString('@', email)) {
      return email;
    }
    sender.sendMessage(ChatColor.RED + "Invalid email! Usage: /authme debug mail test@example.com");
    return null;
  }
  
  private boolean sendTestEmail(String email)
  {
    try
    {
      HtmlEmail htmlEmail = this.sendMailSsl.initializeMail(email);
    }
    catch (EmailException e)
    {
      HtmlEmail htmlEmail;
      ConsoleLogger.logException("Failed to create email for sample email:", e);
      return false;
    }
    HtmlEmail htmlEmail=null;
    htmlEmail.setSubject("AuthMe test email");
    
    String message = "Hello there!<br />This is a sample email sent to you from a Minecraft server (" + this.server.getName() + ") via /authme debug mail. If you're seeing this, sending emails should be fine.";
    return this.sendMailSsl.sendEmail(message, htmlEmail);
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\command\executable\authme\debug\TestEmailSender.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */