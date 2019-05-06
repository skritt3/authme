package fr.xephi.authme.mail;

import java.util.Map;
import java.util.logging.Logger;
import javax.security.auth.callback.CallbackHandler;
import javax.security.sasl.SaslClient;
import javax.security.sasl.SaslClientFactory;

public class OAuth2SaslClientFactory
  implements SaslClientFactory
{
  private static final Logger logger = Logger.getLogger(OAuth2SaslClientFactory.class.getName());
  public static final String OAUTH_TOKEN_PROP = "mail.smpt.sasl.mechanisms.oauth2.oauthToken";
  
  public SaslClient createSaslClient(String[] mechanisms, String authorizationId, String protocol, String serverName, Map<String, ?> props, CallbackHandler callbackHandler)
  {
    boolean matchedMechanism = false;
    for (int i = 0; i < mechanisms.length; i++) {
      if ("XOAUTH2".equalsIgnoreCase(mechanisms[i]))
      {
        matchedMechanism = true;
        break;
      }
    }
    if (!matchedMechanism)
    {
      logger.info("Failed to match any mechanisms");
      return null;
    }
    return new OAuth2SaslClient((String)props.get("mail.smpt.sasl.mechanisms.oauth2.oauthToken"), callbackHandler);
  }
  
  public String[] getMechanismNames(Map<String, ?> props)
  {
    return new String[] { "XOAUTH2" };
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\mail\OAuth2SaslClientFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */