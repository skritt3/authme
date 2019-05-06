package fr.xephi.authme.command.executable.register;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.command.PlayerCommand;
import fr.xephi.authme.libs.jalu.configme.properties.Property;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.mail.EmailService;
import fr.xephi.authme.message.MessageKey;
import fr.xephi.authme.process.Management;
import fr.xephi.authme.process.register.RegisterSecondaryArgument;
import fr.xephi.authme.process.register.RegistrationType;
import fr.xephi.authme.process.register.executors.EmailRegisterParams;
import fr.xephi.authme.process.register.executors.PasswordRegisterParams;
import fr.xephi.authme.process.register.executors.RegistrationMethod;
import fr.xephi.authme.process.register.executors.TwoFactorRegisterParams;
import fr.xephi.authme.security.HashAlgorithm;
import fr.xephi.authme.service.CommonService;
import fr.xephi.authme.service.ValidationService;
import fr.xephi.authme.settings.properties.EmailSettings;
import fr.xephi.authme.settings.properties.RegistrationSettings;
import fr.xephi.authme.settings.properties.SecuritySettings;
import java.util.List;
import org.bukkit.entity.Player;

public class RegisterCommand
  extends PlayerCommand
{
  @Inject
  private Management management;
  @Inject
  private CommonService commonService;
  @Inject
  private EmailService emailService;
  @Inject
  private ValidationService validationService;
  
  public void runCommand(Player player, List<String> arguments)
  {
    if (this.commonService.getProperty(SecuritySettings.PASSWORD_HASH) == HashAlgorithm.TWO_FACTOR)
    {
      this.management.performRegister(RegistrationMethod.TWO_FACTOR_REGISTRATION, 
        TwoFactorRegisterParams.of(player));
      return;
    }
    if (arguments.size() < 1)
    {
      this.commonService.send(player, MessageKey.USAGE_REGISTER);
      return;
    }
    RegistrationType registrationType = (RegistrationType)this.commonService.getProperty(RegistrationSettings.REGISTRATION_TYPE);
    if (registrationType == RegistrationType.PASSWORD) {
      handlePasswordRegistration(player, arguments);
    } else if (registrationType == RegistrationType.EMAIL) {
      handleEmailRegistration(player, arguments);
    } else {
      throw new IllegalStateException("Unknown registration type '" + registrationType + "'");
    }
  }
  
  protected String getAlternativeCommand()
  {
    return "/authme register <playername> <password>";
  }
  
  public MessageKey getArgumentsMismatchMessage()
  {
    return MessageKey.USAGE_LOGIN;
  }
  
  private void handlePasswordRegistration(Player player, List<String> arguments)
  {
    if (isSecondArgValidForPasswordRegistration(player, arguments))
    {
      String password = (String)arguments.get(0);
      String email = getEmailIfAvailable(arguments);
      
      this.management.performRegister(RegistrationMethod.PASSWORD_REGISTRATION, 
        PasswordRegisterParams.of(player, password, email));
    }
  }
  
  private String getEmailIfAvailable(List<String> arguments)
  {
    if (arguments.size() >= 2)
    {
      RegisterSecondaryArgument secondArgType = (RegisterSecondaryArgument)this.commonService.getProperty(RegistrationSettings.REGISTER_SECOND_ARGUMENT);
      if ((secondArgType == RegisterSecondaryArgument.EMAIL_MANDATORY) || (secondArgType == RegisterSecondaryArgument.EMAIL_OPTIONAL)) {
        return (String)arguments.get(1);
      }
    }
    return null;
  }
  
  private boolean isSecondArgValidForPasswordRegistration(Player player, List<String> arguments)
  {
    RegisterSecondaryArgument secondArgType = (RegisterSecondaryArgument)this.commonService.getProperty(RegistrationSettings.REGISTER_SECOND_ARGUMENT);
    if ((secondArgType == RegisterSecondaryArgument.NONE) || ((secondArgType == RegisterSecondaryArgument.EMAIL_OPTIONAL) && (arguments.size() < 2))) {
      return true;
    }
    if (arguments.size() < 2)
    {
      this.commonService.send(player, MessageKey.USAGE_REGISTER);
      return false;
    }
    if (secondArgType == RegisterSecondaryArgument.CONFIRMATION)
    {
      if (((String)arguments.get(0)).equals(arguments.get(1))) {
        return true;
      }
      this.commonService.send(player, MessageKey.PASSWORD_MATCH_ERROR);
      return false;
    }
    if ((secondArgType == RegisterSecondaryArgument.EMAIL_MANDATORY) || (secondArgType == RegisterSecondaryArgument.EMAIL_OPTIONAL))
    {
      if (this.validationService.validateEmail((String)arguments.get(1))) {
        return true;
      }
      this.commonService.send(player, MessageKey.INVALID_EMAIL);
      return false;
    }
    throw new IllegalStateException("Unknown secondary argument type '" + secondArgType + "'");
  }
  
  private void handleEmailRegistration(Player player, List<String> arguments)
  {
    if (!this.emailService.hasAllInformation())
    {
      this.commonService.send(player, MessageKey.INCOMPLETE_EMAIL_SETTINGS);
      ConsoleLogger.warning("Cannot register player '" + player.getName() + "': no email or password is set to send emails from. Please adjust your config at " + EmailSettings.MAIL_ACCOUNT
        .getPath());
      return;
    }
    String email = (String)arguments.get(0);
    if (!this.validationService.validateEmail(email)) {
      this.commonService.send(player, MessageKey.INVALID_EMAIL);
    } else if (isSecondArgValidForEmailRegistration(player, arguments)) {
      this.management.performRegister(RegistrationMethod.EMAIL_REGISTRATION, 
        EmailRegisterParams.of(player, email));
    }
  }
  
  private boolean isSecondArgValidForEmailRegistration(Player player, List<String> arguments)
  {
    RegisterSecondaryArgument secondArgType = (RegisterSecondaryArgument)this.commonService.getProperty(RegistrationSettings.REGISTER_SECOND_ARGUMENT);
    if ((secondArgType == RegisterSecondaryArgument.NONE) || ((secondArgType == RegisterSecondaryArgument.EMAIL_OPTIONAL) && (arguments.size() < 2))) {
      return true;
    }
    if (arguments.size() < 2)
    {
      this.commonService.send(player, MessageKey.USAGE_REGISTER);
      return false;
    }
    if ((secondArgType == RegisterSecondaryArgument.EMAIL_OPTIONAL) || (secondArgType == RegisterSecondaryArgument.EMAIL_MANDATORY) || (secondArgType == RegisterSecondaryArgument.CONFIRMATION))
    {
      if (((String)arguments.get(0)).equals(arguments.get(1))) {
        return true;
      }
      this.commonService.send(player, MessageKey.USAGE_REGISTER);
      return false;
    }
    throw new IllegalStateException("Unknown secondary argument type '" + secondArgType + "'");
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\command\executable\register\RegisterCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */