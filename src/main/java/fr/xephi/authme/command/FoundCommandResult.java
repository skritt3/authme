package fr.xephi.authme.command;

import java.util.List;

public class FoundCommandResult
{
  private final CommandDescription commandDescription;
  private final List<String> labels;
  private final List<String> arguments;
  private final double difference;
  private final FoundResultStatus resultStatus;
  
  public FoundCommandResult(CommandDescription commandDescription, List<String> labels, List<String> arguments, double difference, FoundResultStatus resultStatus)
  {
    this.commandDescription = commandDescription;
    this.labels = labels;
    this.arguments = arguments;
    this.difference = difference;
    this.resultStatus = resultStatus;
  }
  
  public CommandDescription getCommandDescription()
  {
    return this.commandDescription;
  }
  
  public List<String> getArguments()
  {
    return this.arguments;
  }
  
  public List<String> getLabels()
  {
    return this.labels;
  }
  
  public double getDifference()
  {
    return this.difference;
  }
  
  public FoundResultStatus getResultStatus()
  {
    return this.resultStatus;
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\command\FoundCommandResult.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */