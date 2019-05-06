package fr.xephi.authme.mail;

import java.io.IOException;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.sasl.SaslClient;
import javax.security.sasl.SaslException;

class OAuth2SaslClient
  implements SaslClient
{
  private final String oauthToken;
  private final CallbackHandler callbackHandler;
  private boolean isComplete = false;
  
  public OAuth2SaslClient(String oauthToken, CallbackHandler callbackHandler)
  {
    this.oauthToken = oauthToken;
    this.callbackHandler = callbackHandler;
  }
  
  public String getMechanismName()
  {
    return "XOAUTH2";
  }
  
  public boolean hasInitialResponse()
  {
    return true;
  }
  
  public byte[] evaluateChallenge(byte[] challenge)
    throws SaslException
  {
    if (this.isComplete) {
      return new byte[0];
    }
    NameCallback nameCallback = new NameCallback("Enter name");
    Callback[] callbacks = { nameCallback };
    try
    {
      this.callbackHandler.handle(callbacks);
    }
    catch (UnsupportedCallbackException e)
    {
      throw new SaslException("Unsupported callback: " + e);
    }
    catch (IOException e)
    {
      throw new SaslException("Failed to execute callback: " + e);
    }
    String email = nameCallback.getName();
    
    byte[] response = String.format("user=%s\001auth=Bearer %s\001\001", new Object[] { email, this.oauthToken }).getBytes();
    this.isComplete = true;
    return response;
  }
  
  public boolean isComplete()
  {
    return this.isComplete;
  }
  
  public byte[] unwrap(byte[] incoming, int offset, int len)
    throws SaslException
  {
    throw new IllegalStateException();
  }
  
  public byte[] wrap(byte[] outgoing, int offset, int len)
    throws SaslException
  {
    throw new IllegalStateException();
  }
  
  public Object getNegotiatedProperty(String propName)
  {
    if (!isComplete()) {
      throw new IllegalStateException();
    }
    return null;
  }
  
  public void dispose()
    throws SaslException
  {}
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\mail\OAuth2SaslClient.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */