package fr.xephi.authme.mail;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.initialization.DataFolder;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.settings.properties.EmailSettings;
import fr.xephi.authme.settings.properties.SecuritySettings;
import fr.xephi.authme.util.FileUtils;
import java.io.File;
import java.io.IOException;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.imageio.ImageIO;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.bukkit.Server;

public class EmailService
{
  private final File dataFolder;
  private final String serverName;
  private final Settings settings;
  private final SendMailSsl sendMailSsl;
  
  @Inject
  EmailService(@DataFolder File dataFolder, Server server, Settings settings, SendMailSsl sendMailSsl)
  {
    this.dataFolder = dataFolder;
    this.serverName = server.getServerName();
    this.settings = settings;
    this.sendMailSsl = sendMailSsl;
  }
  
  public boolean hasAllInformation()
  {
    return this.sendMailSsl.hasAllInformation();
  }
  
  public boolean sendPasswordMail(String name, String mailAddress, String newPass)
  {
    if (!hasAllInformation())
    {
      ConsoleLogger.warning("Cannot perform email registration: not all email settings are complete");
      return false;
    }
    try
    {
      HtmlEmail email = this.sendMailSsl.initializeMail(mailAddress);
    }
    catch (EmailException e)
    {
      HtmlEmail email;
      ConsoleLogger.logException("Failed to create email with the given settings:", e);
      return false;
    }
    HtmlEmail email=null;
    String mailText = replaceTagsForPasswordMail(this.settings.getPasswordEmailMessage(), name, newPass);
    
    File file = null;
    if (((Boolean)this.settings.getProperty(EmailSettings.PASSWORD_AS_IMAGE)).booleanValue()) {
      try
      {
        file = generatePasswordImage(name, newPass);
        mailText = embedImageIntoEmailContent(file, email, mailText);
      }
      catch (IOException|EmailException e)
      {
        ConsoleLogger.logException("Unable to send new password as image for email " + mailAddress + ":", e);
      }
    }
    boolean couldSendEmail = this.sendMailSsl.sendEmail(mailText, email);
    FileUtils.delete(file);
    return couldSendEmail;
  }
  
  public boolean sendVerificationMail(String name, String mailAddress, String code)
  {
    if (!hasAllInformation())
    {
      ConsoleLogger.warning("Cannot send verification email: not all email settings are complete");
      return false;
    }
    try
    {
      HtmlEmail email = this.sendMailSsl.initializeMail(mailAddress);
    }
    catch (EmailException e)
    {
      HtmlEmail email;
      ConsoleLogger.logException("Failed to create verification email with the given settings:", e);
      return false;
    }
    HtmlEmail email=null;
    String mailText = replaceTagsForVerificationEmail(this.settings.getVerificationEmailMessage(), name, code, 
      ((Integer)this.settings.getProperty(SecuritySettings.VERIFICATION_CODE_EXPIRATION_MINUTES)).intValue());
    return this.sendMailSsl.sendEmail(mailText, email);
  }
  
  public boolean sendRecoveryCode(String name, String email, String code)
  {
    try
    {
      HtmlEmail htmlEmail = this.sendMailSsl.initializeMail(email);
    }
    catch (EmailException e)
    {
      HtmlEmail htmlEmail;
      ConsoleLogger.logException("Failed to create email for recovery code:", e);
      return false;
    }
    HtmlEmail htmlEmail=null;
    String message = replaceTagsForRecoveryCodeMail(this.settings.getRecoveryCodeEmailMessage(), name, code, 
      ((Integer)this.settings.getProperty(SecuritySettings.RECOVERY_CODE_HOURS_VALID)).intValue());
    return this.sendMailSsl.sendEmail(message, htmlEmail);
  }
  
  private File generatePasswordImage(String name, String newPass)
    throws IOException
  {
    ImageGenerator gen = new ImageGenerator(newPass);
    File file = new File(this.dataFolder, name + "_new_pass.jpg");
    ImageIO.write(gen.generateImage(), "jpg", file);
    return file;
  }
  
  private static String embedImageIntoEmailContent(File image, HtmlEmail email, String content)
    throws EmailException
  {
    DataSource source = new FileDataSource(image);
    String tag = email.embed(source, image.getName());
    return content.replace("<image />", "<img src=\"cid:" + tag + "\">");
  }
  
  private String replaceTagsForPasswordMail(String mailText, String name, String newPass)
  {
    return 
    
      mailText.replace("<playername />", name).replace("<servername />", this.serverName).replace("<generatedpass />", newPass);
  }
  
  private String replaceTagsForVerificationEmail(String mailText, String name, String code, int minutesValid)
  {
    return 
    
      mailText.replace("<playername />", name).replace("<servername />", this.serverName).replace("<generatedcode />", code).replace("<minutesvalid />", String.valueOf(minutesValid));
  }
  
  private String replaceTagsForRecoveryCodeMail(String mailText, String name, String code, int hoursValid)
  {
    return 
    
      mailText.replace("<playername />", name).replace("<servername />", this.serverName).replace("<recoverycode />", code).replace("<hoursvalid />", String.valueOf(hoursValid));
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\mail\EmailService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */