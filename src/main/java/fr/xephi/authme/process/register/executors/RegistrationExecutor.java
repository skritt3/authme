package fr.xephi.authme.process.register.executors;

import fr.xephi.authme.data.auth.PlayerAuth;

public abstract interface RegistrationExecutor<P extends RegistrationParameters>
{
  public abstract boolean isRegistrationAdmitted(P paramP);
  
  public abstract PlayerAuth buildPlayerAuth(P paramP);
  
  public abstract void executePostPersistAction(P paramP);
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\process\register\executors\RegistrationExecutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */