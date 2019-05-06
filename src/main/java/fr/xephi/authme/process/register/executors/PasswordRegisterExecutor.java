package fr.xephi.authme.process.register.executors;

import fr.xephi.authme.data.auth.PlayerAuth;

class PasswordRegisterExecutor
  extends AbstractPasswordRegisterExecutor<PasswordRegisterParams>
{
  public PlayerAuth createPlayerAuthObject(PasswordRegisterParams params)
  {
    return PlayerAuthBuilderHelper.createPlayerAuth(params.getPlayer(), params.getHashedPassword(), params.getEmail());
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\process\register\executors\PasswordRegisterExecutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */