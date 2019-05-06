package fr.xephi.authme.command;

public class CommandArgumentDescription
{
  private final String name;
  private final String description;
  private final boolean isOptional;
  
  public CommandArgumentDescription(String name, String description, boolean isOptional)
  {
    this.name = name;
    this.description = description;
    this.isOptional = isOptional;
  }
  
  public String getName()
  {
    return this.name;
  }
  
  public String getDescription()
  {
    return this.description;
  }
  
  public boolean isOptional()
  {
    return this.isOptional;
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\command\CommandArgumentDescription.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */