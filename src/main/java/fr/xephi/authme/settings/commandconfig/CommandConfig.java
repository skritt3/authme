package fr.xephi.authme.settings.commandconfig;

import java.util.LinkedHashMap;
import java.util.Map;

public class CommandConfig
{
  private Map<String, Command> onJoin = new LinkedHashMap();
  private Map<String, Command> onLogin = new LinkedHashMap();
  private Map<String, Command> onSessionLogin = new LinkedHashMap();
  private Map<String, Command> onRegister = new LinkedHashMap();
  private Map<String, Command> onUnregister = new LinkedHashMap();
  private Map<String, Command> onLogout = new LinkedHashMap();
  
  public Map<String, Command> getOnJoin()
  {
    return this.onJoin;
  }
  
  public void setOnJoin(Map<String, Command> onJoin)
  {
    this.onJoin = onJoin;
  }
  
  public Map<String, Command> getOnLogin()
  {
    return this.onLogin;
  }
  
  public void setOnLogin(Map<String, Command> onLogin)
  {
    this.onLogin = onLogin;
  }
  
  public Map<String, Command> getOnSessionLogin()
  {
    return this.onSessionLogin;
  }
  
  public void setOnSessionLogin(Map<String, Command> onSessionLogin)
  {
    this.onSessionLogin = onSessionLogin;
  }
  
  public Map<String, Command> getOnRegister()
  {
    return this.onRegister;
  }
  
  public void setOnRegister(Map<String, Command> onRegister)
  {
    this.onRegister = onRegister;
  }
  
  public Map<String, Command> getOnUnregister()
  {
    return this.onUnregister;
  }
  
  public void setOnUnregister(Map<String, Command> onUnregister)
  {
    this.onUnregister = onUnregister;
  }
  
  public Map<String, Command> getOnLogout()
  {
    return this.onLogout;
  }
  
  public void setOnLogout(Map<String, Command> onLogout)
  {
    this.onLogout = onLogout;
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\settings\commandconfig\CommandConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */