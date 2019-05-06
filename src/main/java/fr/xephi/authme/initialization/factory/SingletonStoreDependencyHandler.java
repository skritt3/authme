package fr.xephi.authme.initialization.factory;

import fr.xephi.authme.libs.jalu.injector.Injector;
import fr.xephi.authme.libs.jalu.injector.context.ResolvedInstantiationContext;
import fr.xephi.authme.libs.jalu.injector.handlers.dependency.DependencyHandler;
import fr.xephi.authme.libs.jalu.injector.handlers.instantiation.DependencyDescription;
import fr.xephi.authme.libs.jalu.injector.utils.ReflectionUtils;
import java.util.Collection;

public class SingletonStoreDependencyHandler
  implements DependencyHandler
{
  public Object resolveValue(ResolvedInstantiationContext<?> context, DependencyDescription dependencyDescription)
  {
    if (dependencyDescription.getType() == SingletonStore.class)
    {
      Class<?> genericType = ReflectionUtils.getGenericType(dependencyDescription.getGenericType());
      if (genericType == null) {
        throw new IllegalStateException("Singleton store fields must have concrete generic type. Cannot get generic type for field in '" + context.getMappedClass() + "'");
      }
      return new SingletonStoreImpl(genericType, context.getInjector());
    }
    return null;
  }
  
  private static final class SingletonStoreImpl<P>
    implements SingletonStore<P>
  {
    private final Injector injector;
    private final Class<P> parentClass;
    
    SingletonStoreImpl(Class<P> parentClass, Injector injector)
    {
      this.parentClass = parentClass;
      this.injector = injector;
    }
    
    public <C extends P> C getSingleton(Class<C> clazz)
    {
      if (this.parentClass.isAssignableFrom(clazz)) {
        return (C)this.injector.getSingleton(clazz);
      }
      throw new IllegalArgumentException(clazz + " not child of " + this.parentClass);
    }
    
    public Collection<P> retrieveAllOfType()
    {
      return retrieveAllOfType(this.parentClass);
    }
    
    public <C extends P> Collection<C> retrieveAllOfType(Class<C> clazz)
    {
      if (this.parentClass.isAssignableFrom(clazz)) {
        return this.injector.retrieveAllOfType(clazz);
      }
      throw new IllegalArgumentException(clazz + " not child of " + this.parentClass);
    }
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\initialization\factory\SingletonStoreDependencyHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */