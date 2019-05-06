package fr.xephi.authme.settings.properties;

import fr.xephi.authme.libs.jalu.configme.Comment;
import fr.xephi.authme.libs.jalu.configme.SettingsHolder;
import fr.xephi.authme.libs.jalu.configme.properties.Property;
import fr.xephi.authme.libs.jalu.configme.properties.PropertyInitializer;
import java.util.List;

public final class EmailSettings
  implements SettingsHolder
{
  @Comment({"Email SMTP server host"})
  public static final Property<String> SMTP_HOST = PropertyInitializer.newProperty("Email.mailSMTP", "smtp.gmail.com");
  @Comment({"Email SMTP server port"})
  public static final Property<Integer> SMTP_PORT = PropertyInitializer.newProperty("Email.mailPort", 465);
  @Comment({"Only affects port 25: enable TLS/STARTTLS?"})
  public static final Property<Boolean> PORT25_USE_TLS = PropertyInitializer.newProperty("Email.useTls", true);
  @Comment({"Email account which sends the mails"})
  public static final Property<String> MAIL_ACCOUNT = PropertyInitializer.newProperty("Email.mailAccount", "");
  @Comment({"Email account password"})
  public static final Property<String> MAIL_PASSWORD = PropertyInitializer.newProperty("Email.mailPassword", "");
  @Comment({"Email address, fill when mailAccount is not the email address of the account"})
  public static final Property<String> MAIL_ADDRESS = PropertyInitializer.newProperty("Email.mailAddress", "");
  @Comment({"Custom sender name, replacing the mailAccount name in the email"})
  public static final Property<String> MAIL_SENDER_NAME = PropertyInitializer.newProperty("Email.mailSenderName", "");
  @Comment({"Recovery password length"})
  public static final Property<Integer> RECOVERY_PASSWORD_LENGTH = PropertyInitializer.newProperty("Email.RecoveryPasswordLength", 8);
  @Comment({"Mail Subject"})
  public static final Property<String> RECOVERY_MAIL_SUBJECT = PropertyInitializer.newProperty("Email.mailSubject", "Your new AuthMe password");
  @Comment({"Like maxRegPerIP but with email"})
  public static final Property<Integer> MAX_REG_PER_EMAIL = PropertyInitializer.newProperty("Email.maxRegPerEmail", 1);
  @Comment({"Recall players to add an email?"})
  public static final Property<Boolean> RECALL_PLAYERS = PropertyInitializer.newProperty("Email.recallPlayers", false);
  @Comment({"Delay in minute for the recall scheduler"})
  public static final Property<Integer> DELAY_RECALL = PropertyInitializer.newProperty("Email.delayRecall", 5);
  @Comment({"Blacklist these domains for emails"})
  public static final Property<List<String>> DOMAIN_BLACKLIST = PropertyInitializer.newListProperty("Email.emailBlacklisted", new String[] { "10minutemail.com" });
  @Comment({"Whitelist ONLY these domains for emails"})
  public static final Property<List<String>> DOMAIN_WHITELIST = PropertyInitializer.newListProperty("Email.emailWhitelisted", new String[0]);
  @Comment({"Send the new password drawn in an image?"})
  public static final Property<Boolean> PASSWORD_AS_IMAGE = PropertyInitializer.newProperty("Email.generateImage", false);
  @Comment({"The OAuth2 token"})
  public static final Property<String> OAUTH2_TOKEN = PropertyInitializer.newProperty("Email.emailOauth2Token", "");
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\settings\properties\EmailSettings.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */