package fr.xephi.authme.events;

import fr.xephi.authme.security.crypts.EncryptionMethod;
import org.bukkit.event.HandlerList;

public class PasswordEncryptionEvent
  extends CustomEvent
{
  private static final HandlerList handlers = new HandlerList();
  private EncryptionMethod method;
  
  public PasswordEncryptionEvent(EncryptionMethod method)
  {
    super(false);
    this.method = method;
  }
  
  public static HandlerList getHandlerList()
  {
    return handlers;
  }
  
  public HandlerList getHandlers()
  {
    return handlers;
  }
  
  public EncryptionMethod getMethod()
  {
    return this.method;
  }
  
  public void setMethod(EncryptionMethod method)
  {
    this.method = method;
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\events\PasswordEncryptionEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */