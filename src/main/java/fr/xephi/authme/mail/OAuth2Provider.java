package fr.xephi.authme.mail;

import java.security.Provider;

public class OAuth2Provider
  extends Provider
{
  private static final long serialVersionUID = 1L;
  
  public OAuth2Provider()
  {
    super("Google OAuth2 Provider", 1.0D, "Provides the XOAUTH2 SASL Mechanism");
    
    put("SaslClientFactory.XOAUTH2", "fr.xephi.authme.mail.OAuth2SaslClientFactory");
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\mail\OAuth2Provider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */