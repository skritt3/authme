package fr.xephi.authme.command.help;

public enum HelpMessage
{
  HEADER("header"),  OPTIONAL("optional"),  HAS_PERMISSION("hasPermission"),  NO_PERMISSION("noPermission"),  DEFAULT("default"),  RESULT("result");
  
  private static final String PREFIX = "common.";
  private final String key;
  
  private HelpMessage(String key)
  {
    this.key = ("common." + key);
  }
  
  public String getKey()
  {
    return this.key;
  }
  
  public String getEntryKey()
  {
    return this.key.substring("common.".length());
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\command\help\HelpMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */