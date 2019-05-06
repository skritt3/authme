package fr.xephi.authme.events;

import org.bukkit.event.Event;

public abstract class CustomEvent
  extends Event
{
  public CustomEvent()
  {
    super(false);
  }
  
  public CustomEvent(boolean isAsync)
  {
    super(isAsync);
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\events\CustomEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */