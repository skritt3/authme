package fr.xephi.authme.output;

import java.util.logging.Filter;
import java.util.logging.LogRecord;

public class ConsoleFilter
  implements Filter
{
  public boolean isLoggable(LogRecord record)
  {
    if ((record == null) || (record.getMessage() == null)) {
      return true;
    }
    if (LogFilterHelper.isSensitiveAuthMeCommand(record.getMessage()))
    {
      String playerName = record.getMessage().split(" ")[0];
      record.setMessage(playerName + " issued an AuthMe command");
    }
    return true;
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\output\ConsoleFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */