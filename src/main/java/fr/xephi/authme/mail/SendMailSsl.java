package fr.xephi.authme.mail;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.output.LogLevel;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.settings.properties.EmailSettings;
import fr.xephi.authme.settings.properties.PluginSettings;
import fr.xephi.authme.util.StringUtils;
import java.security.Security;
import java.util.Properties;
import javax.activation.CommandMap;
import javax.activation.MailcapCommandMap;
import javax.mail.Session;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

public class SendMailSsl
{
  @Inject
  private Settings settings;
  
  public boolean hasAllInformation()
  {
    return (!((String)this.settings.getProperty(EmailSettings.MAIL_ACCOUNT)).isEmpty()) && 
      (!((String)this.settings.getProperty(EmailSettings.MAIL_PASSWORD)).isEmpty());
  }
  
  public HtmlEmail initializeMail(String emailAddress)
    throws EmailException
  {
    String senderMail = StringUtils.isEmpty((String)this.settings.getProperty(EmailSettings.MAIL_ADDRESS)) ? (String)this.settings.getProperty(EmailSettings.MAIL_ACCOUNT) : (String)this.settings.getProperty(EmailSettings.MAIL_ADDRESS);
    
    String senderName = StringUtils.isEmpty((String)this.settings.getProperty(EmailSettings.MAIL_SENDER_NAME)) ? senderMail : (String)this.settings.getProperty(EmailSettings.MAIL_SENDER_NAME);
    String mailPassword = (String)this.settings.getProperty(EmailSettings.MAIL_PASSWORD);
    int port = ((Integer)this.settings.getProperty(EmailSettings.SMTP_PORT)).intValue();
    
    HtmlEmail email = new HtmlEmail();
    email.setCharset("utf-8");
    email.setSmtpPort(port);
    email.setHostName((String)this.settings.getProperty(EmailSettings.SMTP_HOST));
    email.addTo(emailAddress);
    email.setFrom(senderMail, senderName);
    email.setSubject((String)this.settings.getProperty(EmailSettings.RECOVERY_MAIL_SUBJECT));
    email.setAuthentication((String)this.settings.getProperty(EmailSettings.MAIL_ACCOUNT), mailPassword);
    if (((LogLevel)this.settings.getProperty(PluginSettings.LOG_LEVEL)).includes(LogLevel.DEBUG)) {
      email.setDebug(true);
    }
    setPropertiesForPort(email, port);
    return email;
  }
  
  public boolean sendEmail(String content, HtmlEmail email)
  {
    Thread.currentThread().setContextClassLoader(SendMailSsl.class.getClassLoader());
    
    MailcapCommandMap mc = (MailcapCommandMap)CommandMap.getDefaultCommandMap();
    mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
    mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
    mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
    mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
    mc.addMailcap("message/rfc822;; x-java-content- handler=com.sun.mail.handlers.message_rfc822");
    try
    {
      email.setHtmlMsg(content);
      email.setTextMsg(content);
    }
    catch (EmailException e)
    {
      ConsoleLogger.logException("Your email.html config contains an error and cannot be sent:", e);
      return false;
    }
    try
    {
      email.send();
      return true;
    }
    catch (EmailException e)
    {
      ConsoleLogger.logException("Failed to send a mail to " + email.getToAddresses() + ":", e);
    }
    return false;
  }
  
  private void setPropertiesForPort(HtmlEmail email, int port)
    throws EmailException
  {
    switch (port)
    {
    case 587: 
      String oAuth2Token = (String)this.settings.getProperty(EmailSettings.OAUTH2_TOKEN);
      if (!oAuth2Token.isEmpty())
      {
        if (Security.getProvider("Google OAuth2 Provider") == null) {
          Security.addProvider(new OAuth2Provider());
        }
        Properties mailProperties = email.getMailSession().getProperties();
        mailProperties.setProperty("mail.smtp.ssl.enable", "true");
        mailProperties.setProperty("mail.smtp.auth.mechanisms", "XOAUTH2");
        mailProperties.setProperty("mail.smtp.sasl.enable", "true");
        mailProperties.setProperty("mail.smtp.sasl.mechanisms", "XOAUTH2");
        mailProperties.setProperty("mail.smtp.auth.login.disable", "true");
        mailProperties.setProperty("mail.smtp.auth.plain.disable", "true");
        mailProperties.setProperty("mail.smpt.sasl.mechanisms.oauth2.oauthToken", oAuth2Token);
        email.setMailSession(Session.getInstance(mailProperties));
      }
      else
      {
        email.setStartTLSEnabled(true);
        email.setStartTLSRequired(true);
      }
      break;
    case 25: 
      if (((Boolean)this.settings.getProperty(EmailSettings.PORT25_USE_TLS)).booleanValue())
      {
        email.setStartTLSEnabled(true);
        email.setSSLCheckServerIdentity(true);
      }
      break;
    case 465: 
      email.setSslSmtpPort(Integer.toString(port));
      email.setSSLOnConnect(true);
      break;
    default: 
      email.setStartTLSEnabled(true);
      email.setSSLOnConnect(true);
      email.setSSLCheckServerIdentity(true);
    }
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\mail\SendMailSsl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */