package fr.xephi.authme.datasource;

import fr.xephi.authme.data.auth.PlayerAuth;
import fr.xephi.authme.initialization.Reloadable;
import fr.xephi.authme.security.crypts.HashedPassword;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public abstract interface DataSource
  extends Reloadable
{
  public abstract boolean isAuthAvailable(String paramString);
  
  public abstract HashedPassword getPassword(String paramString);
  
  public abstract PlayerAuth getAuth(String paramString);
  
  public abstract boolean saveAuth(PlayerAuth paramPlayerAuth);
  
  public abstract boolean updateSession(PlayerAuth paramPlayerAuth);
  
  public abstract boolean updatePassword(PlayerAuth paramPlayerAuth);
  
  public abstract boolean updatePassword(String paramString, HashedPassword paramHashedPassword);
  
  public abstract Set<String> getRecordsToPurge(long paramLong);
  
  public abstract void purgeRecords(Collection<String> paramCollection);
  
  public abstract boolean removeAuth(String paramString);
  
  public abstract boolean updateQuitLoc(PlayerAuth paramPlayerAuth);
  
  public abstract List<String> getAllAuthsByIp(String paramString);
  
  public abstract int countAuthsByEmail(String paramString);
  
  public abstract boolean updateEmail(PlayerAuth paramPlayerAuth);
  
  public abstract void closeConnection();
  
  public abstract DataSourceType getType();
  
  public abstract boolean isLogged(String paramString);
  
  public abstract void setLogged(String paramString);
  
  public abstract void setUnlogged(String paramString);
  
  public abstract boolean hasSession(String paramString);
  
  public abstract void grantSession(String paramString);
  
  public abstract void revokeSession(String paramString);
  
  public abstract void purgeLogged();
  
  public abstract List<String> getLoggedPlayersWithEmptyMail();
  
  public abstract int getAccountsRegistered();
  
  public abstract boolean updateRealName(String paramString1, String paramString2);
  
  public abstract DataSourceResult<String> getEmail(String paramString);
  
  public abstract List<PlayerAuth> getAllAuths();
  
  public abstract void reload();
  
  public void invalidateCache(String playerName);
  
  public void refreshCache(String playerName);
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\datasource\DataSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */