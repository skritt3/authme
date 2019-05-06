package fr.xephi.authme.settings.commandconfig;

public class Command
{
  private String command;
  private Executor executor = Executor.PLAYER;
  
  public Command() {}
  
  public Command(String command, Executor executor)
  {
    this.command = command;
    this.executor = executor;
  }
  
  public String getCommand()
  {
    return this.command;
  }
  
  public void setCommand(String command)
  {
    this.command = command;
  }
  
  public Executor getExecutor()
  {
    return this.executor;
  }
  
  public void setExecutor(Executor executor)
  {
    this.executor = executor;
  }
  
  public String toString()
  {
    return this.command + " (" + this.executor + ")";
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\settings\commandconfig\Command.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */