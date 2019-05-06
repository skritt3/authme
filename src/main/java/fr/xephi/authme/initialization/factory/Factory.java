package fr.xephi.authme.initialization.factory;

public abstract interface Factory<P>
{
  public abstract <C extends P> C newInstance(Class<C> paramClass);
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\initialization\factory\Factory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */