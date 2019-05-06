package fr.xephi.authme.output;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.filter.AbstractFilter;
import org.apache.logging.log4j.message.Message;

public class Log4JFilter
  extends AbstractFilter
{
  private static final long serialVersionUID = -5594073755007974254L;
  
  private static Filter.Result validateMessage(Message message)
  {
    if (message == null) {
      return Filter.Result.NEUTRAL;
    }
    return validateMessage(message.getFormattedMessage());
  }
  
  private static Filter.Result validateMessage(String message)
  {
    return LogFilterHelper.isSensitiveAuthMeCommand(message) ? Filter.Result.DENY : Filter.Result.NEUTRAL;
  }
  
  public Filter.Result filter(LogEvent event)
  {
    Message candidate = null;
    if (event != null) {
      candidate = event.getMessage();
    }
    return validateMessage(candidate);
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, Message msg, Throwable t)
  {
    return validateMessage(msg);
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object... params)
  {
    return validateMessage(msg);
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, Object msg, Throwable t)
  {
    String candidate = null;
    if (msg != null) {
      candidate = msg.toString();
    }
    return validateMessage(candidate);
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\output\Log4JFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */