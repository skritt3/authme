package fr.xephi.authme.initialization.factory;

import java.util.Collection;

public abstract interface SingletonStore<P>
{
  public abstract <C extends P> C getSingleton(Class<C> paramClass);
  
  public abstract Collection<P> retrieveAllOfType();
  
  public abstract <C extends P> Collection<C> retrieveAllOfType(Class<C> paramClass);
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\initialization\factory\SingletonStore.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */