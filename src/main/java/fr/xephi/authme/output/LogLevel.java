package fr.xephi.authme.output;

public enum LogLevel
{
  INFO(3),  FINE(2),  DEBUG(1);
  
  private int value;
  
  private LogLevel(int value)
  {
    this.value = value;
  }
  
  public boolean includes(LogLevel level)
  {
    return this.value <= level.value;
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\output\LogLevel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */