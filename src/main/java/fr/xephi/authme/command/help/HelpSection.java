package fr.xephi.authme.command.help;

public enum HelpSection
{
  COMMAND("command"),  SHORT_DESCRIPTION("description"),  DETAILED_DESCRIPTION("detailedDescription"),  ARGUMENTS("arguments"),  ALTERNATIVES("alternatives"),  PERMISSIONS("permissions"),  CHILDREN("children");
  
  private static final String PREFIX = "section.";
  private final String key;
  
  private HelpSection(String key)
  {
    this.key = ("section." + key);
  }
  
  public String getKey()
  {
    return this.key;
  }
  
  public String getEntryKey()
  {
    return this.key.substring("section.".length());
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\command\help\HelpSection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */