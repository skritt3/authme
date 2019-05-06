package fr.xephi.authme.service;

import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.util.StringUtils;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class JoinMessageService
{
  private BukkitService bukkitService;
  private Map<String, String> joinMessages;
  
  @Inject
  JoinMessageService(BukkitService bukkitService)
  {
    this.bukkitService = bukkitService;
    this.joinMessages = new ConcurrentHashMap();
  }
  
  public void putMessage(String playerName, String string)
  {
    this.joinMessages.put(playerName, string);
  }
  
  public void sendMessage(String playerName)
  {
    String joinMessage = (String)this.joinMessages.remove(playerName);
    if (!StringUtils.isEmpty(joinMessage)) {
      this.bukkitService.broadcastMessage(joinMessage);
    }
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\service\JoinMessageService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */