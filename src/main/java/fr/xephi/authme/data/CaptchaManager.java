package fr.xephi.authme.data;

import fr.xephi.authme.initialization.HasCleanup;
import fr.xephi.authme.initialization.SettingsDependent;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.settings.properties.SecuritySettings;
import fr.xephi.authme.util.RandomStringUtils;
import fr.xephi.authme.util.expiring.TimedCounter;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class CaptchaManager
  implements SettingsDependent, HasCleanup
{
  private final TimedCounter<String> playerCounts;
  private final ConcurrentHashMap<String, String> captchaCodes;
  private boolean isEnabled;
  private int threshold;
  private int captchaLength;
  
  @Inject
  CaptchaManager(Settings settings)
  {
    this.captchaCodes = new ConcurrentHashMap();
    long countTimeout = ((Integer)settings.getProperty(SecuritySettings.CAPTCHA_COUNT_MINUTES_BEFORE_RESET)).intValue();
    this.playerCounts = new TimedCounter(countTimeout, TimeUnit.MINUTES);
    reload(settings);
  }
  
  public void increaseCount(String name)
  {
    if (this.isEnabled)
    {
      String playerLower = name.toLowerCase();
      this.playerCounts.increment(playerLower);
    }
  }
  
  public boolean isCaptchaRequired(String name)
  {
    return (this.isEnabled) && (this.playerCounts.get(name.toLowerCase()).intValue() >= this.threshold);
  }
  
  public String getCaptchaCodeOrGenerateNew(String name)
  {
    String code = (String)this.captchaCodes.get(name.toLowerCase());
    return code == null ? generateCode(name) : code;
  }
  
  public String generateCode(String name)
  {
    String code = RandomStringUtils.generate(this.captchaLength);
    this.captchaCodes.put(name.toLowerCase(), code);
    return code;
  }
  
  public boolean checkCode(String name, String code)
  {
    String savedCode = (String)this.captchaCodes.get(name.toLowerCase());
    if (savedCode == null) {
      return true;
    }
    if (savedCode.equalsIgnoreCase(code))
    {
      this.captchaCodes.remove(name.toLowerCase());
      this.playerCounts.remove(name.toLowerCase());
      return true;
    }
    return false;
  }
  
  public void resetCounts(String name)
  {
    if (this.isEnabled)
    {
      this.captchaCodes.remove(name.toLowerCase());
      this.playerCounts.remove(name.toLowerCase());
    }
  }
  
  public void reload(Settings settings)
  {
    this.isEnabled = ((Boolean)settings.getProperty(SecuritySettings.USE_CAPTCHA)).booleanValue();
    this.threshold = ((Integer)settings.getProperty(SecuritySettings.MAX_LOGIN_TRIES_BEFORE_CAPTCHA)).intValue();
    this.captchaLength = ((Integer)settings.getProperty(SecuritySettings.CAPTCHA_LENGTH)).intValue();
    long countTimeout = ((Integer)settings.getProperty(SecuritySettings.CAPTCHA_COUNT_MINUTES_BEFORE_RESET)).intValue();
    this.playerCounts.setExpiration(countTimeout, TimeUnit.MINUTES);
  }
  
  public void performCleanup()
  {
    this.playerCounts.removeExpiredEntries();
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\data\CaptchaManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */