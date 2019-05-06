package fr.xephi.authme.process.register.executors;

import fr.xephi.authme.data.auth.PlayerAuth;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.mail.EmailService;
import fr.xephi.authme.message.MessageKey;
import fr.xephi.authme.permission.PermissionsManager;
import fr.xephi.authme.permission.PlayerStatePermission;
import fr.xephi.authme.process.SyncProcessManager;
import fr.xephi.authme.security.PasswordSecurity;
import fr.xephi.authme.security.crypts.HashedPassword;
import fr.xephi.authme.service.CommonService;
import fr.xephi.authme.settings.properties.EmailSettings;
import fr.xephi.authme.util.RandomStringUtils;
import org.bukkit.entity.Player;

class EmailRegisterExecutor
  implements RegistrationExecutor<EmailRegisterParams>
{
  @Inject
  private PermissionsManager permissionsManager;
  @Inject
  private DataSource dataSource;
  @Inject
  private CommonService commonService;
  @Inject
  private EmailService emailService;
  @Inject
  private SyncProcessManager syncProcessManager;
  @Inject
  private PasswordSecurity passwordSecurity;
  
  public boolean isRegistrationAdmitted(EmailRegisterParams params)
  {
    int maxRegPerEmail = ((Integer)this.commonService.getProperty(EmailSettings.MAX_REG_PER_EMAIL)).intValue();
    if ((maxRegPerEmail > 0) && (!this.permissionsManager.hasPermission(params.getPlayer(), PlayerStatePermission.ALLOW_MULTIPLE_ACCOUNTS)))
    {
      int otherAccounts = this.dataSource.countAuthsByEmail(params.getEmail());
      if (otherAccounts >= maxRegPerEmail)
      {
        this.commonService.send(params.getPlayer(), MessageKey.MAX_REGISTER_EXCEEDED, new String[] {
          Integer.toString(maxRegPerEmail), Integer.toString(otherAccounts), "@" });
        return false;
      }
    }
    return true;
  }
  
  public PlayerAuth buildPlayerAuth(EmailRegisterParams params)
  {
    String password = RandomStringUtils.generate(((Integer)this.commonService.getProperty(EmailSettings.RECOVERY_PASSWORD_LENGTH)).intValue());
    HashedPassword hashedPassword = this.passwordSecurity.computeHash(password, params.getPlayer().getName());
    params.setPassword(password);
    return PlayerAuthBuilderHelper.createPlayerAuth(params.getPlayer(), hashedPassword, params.getEmail());
  }
  
  public void executePostPersistAction(EmailRegisterParams params)
  {
    Player player = params.getPlayer();
    boolean couldSendMail = this.emailService.sendPasswordMail(player
      .getName(), params.getEmail(), params.getPassword());
    if (couldSendMail) {
      this.syncProcessManager.processSyncEmailRegister(player);
    } else {
      this.commonService.send(player, MessageKey.EMAIL_SEND_FAILURE);
    }
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\process\register\executors\EmailRegisterExecutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */