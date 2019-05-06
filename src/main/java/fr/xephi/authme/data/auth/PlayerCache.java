package fr.xephi.authme.data.auth;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerCache
{
  private final Map<String, PlayerAuth> cache = new ConcurrentHashMap();
  
  public void updatePlayer(PlayerAuth auth)
  {
    this.cache.put(auth.getNickname().toLowerCase(), auth);
  }
  
  public void removePlayer(String user)
  {
    this.cache.remove(user.toLowerCase());
  }
  
  public boolean isAuthenticated(String user)
  {
    return this.cache.containsKey(user.toLowerCase());
  }
  
  public PlayerAuth getAuth(String user)
  {
    return (PlayerAuth)this.cache.get(user.toLowerCase());
  }
  
  public int getLogged()
  {
    return this.cache.size();
  }
  
  public Map<String, PlayerAuth> getCache()
  {
    return this.cache;
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\data\auth\PlayerCache.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */