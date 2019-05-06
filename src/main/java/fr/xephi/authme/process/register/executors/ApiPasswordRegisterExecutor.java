package fr.xephi.authme.process.register.executors;

import fr.xephi.authme.data.auth.PlayerAuth;

class ApiPasswordRegisterExecutor
  extends AbstractPasswordRegisterExecutor<ApiPasswordRegisterParams>
{
  protected PlayerAuth createPlayerAuthObject(ApiPasswordRegisterParams params)
  {
    return 
      PlayerAuthBuilderHelper.createPlayerAuth(params.getPlayer(), params.getHashedPassword(), null);
  }
  
  protected boolean performLoginAfterRegister(ApiPasswordRegisterParams params)
  {
    return params.getLoginAfterRegister();
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\process\register\executors\ApiPasswordRegisterExecutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */