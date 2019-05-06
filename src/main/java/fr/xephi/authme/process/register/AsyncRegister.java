package fr.xephi.authme.process.register;

import fr.xephi.authme.data.auth.PlayerAuth;
import fr.xephi.authme.data.auth.PlayerCache;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.initialization.factory.SingletonStore;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.message.MessageKey;
import fr.xephi.authme.permission.PermissionsManager;
import fr.xephi.authme.permission.PlayerStatePermission;
import fr.xephi.authme.process.AsynchronousProcess;
import fr.xephi.authme.process.register.executors.RegistrationExecutor;
import fr.xephi.authme.process.register.executors.RegistrationMethod;
import fr.xephi.authme.process.register.executors.RegistrationParameters;
import fr.xephi.authme.service.CommonService;
import fr.xephi.authme.service.bungeecord.BungeeSender;
import fr.xephi.authme.settings.properties.RegistrationSettings;
import fr.xephi.authme.settings.properties.RestrictionSettings;
import fr.xephi.authme.util.PlayerUtils;
import java.util.List;
import org.bukkit.entity.Player;

public class AsyncRegister
  implements AsynchronousProcess
{
  @Inject
  private DataSource database;
  @Inject
  private PlayerCache playerCache;
  @Inject
  private CommonService service;
  @Inject
  private PermissionsManager permissionsManager;
  @Inject
  private SingletonStore<RegistrationExecutor> registrationExecutorFactory;
  @Inject
  private BungeeSender bungeeSender;
  
  public <P extends RegistrationParameters> void register(RegistrationMethod<P> variant, P parameters)
  {
    if (preRegisterCheck(parameters.getPlayer()))
    {
      RegistrationExecutor<P> executor = (RegistrationExecutor)this.registrationExecutorFactory.getSingleton(variant.getExecutorClass());
      if (executor.isRegistrationAdmitted(parameters)) {
        executeRegistration(parameters, executor);
      }
    }
  }
  
  private boolean preRegisterCheck(Player player)
  {
    String name = player.getName().toLowerCase();
    if (this.playerCache.isAuthenticated(name))
    {
      this.service.send(player, MessageKey.ALREADY_LOGGED_IN_ERROR);
      return false;
    }
    if (!((Boolean)this.service.getProperty(RegistrationSettings.IS_ENABLED)).booleanValue())
    {
      this.service.send(player, MessageKey.REGISTRATION_DISABLED);
      return false;
    }
    if (this.database.isAuthAvailable(name))
    {
      this.service.send(player, MessageKey.NAME_ALREADY_REGISTERED);
      return false;
    }
    return isPlayerIpAllowedToRegister(player);
  }
  
  private <P extends RegistrationParameters> void executeRegistration(P parameters, RegistrationExecutor<P> executor)
  {
    PlayerAuth auth = executor.buildPlayerAuth(parameters);
    if (this.database.saveAuth(auth))
    {
      executor.executePostPersistAction(parameters);
      this.bungeeSender.sendAuthMeBungeecordMessage("register", parameters.getPlayerName());
    }
    else
    {
      this.service.send(parameters.getPlayer(), MessageKey.ERROR);
    }
  }
  
  private boolean isPlayerIpAllowedToRegister(Player player)
  {
    int maxRegPerIp = ((Integer)this.service.getProperty(RestrictionSettings.MAX_REGISTRATION_PER_IP)).intValue();
    String ip = PlayerUtils.getPlayerIp(player);
    if ((maxRegPerIp > 0) && 
      (!"127.0.0.1".equalsIgnoreCase(ip)) && 
      (!"localhost".equalsIgnoreCase(ip)) && 
      (!this.permissionsManager.hasPermission(player, PlayerStatePermission.ALLOW_MULTIPLE_ACCOUNTS)))
    {
      List<String> otherAccounts = this.database.getAllAuthsByIp(ip);
      if (otherAccounts.size() >= maxRegPerIp)
      {
        this.service.send(player, MessageKey.MAX_REGISTER_EXCEEDED, new String[] { Integer.toString(maxRegPerIp), 
          Integer.toString(otherAccounts.size()), String.join(", ", otherAccounts) });
        return false;
      }
    }
    return true;
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\process\register\AsyncRegister.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */