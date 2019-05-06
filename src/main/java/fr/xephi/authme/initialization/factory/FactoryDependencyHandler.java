package fr.xephi.authme.initialization.factory;

import fr.xephi.authme.libs.jalu.injector.Injector;
import fr.xephi.authme.libs.jalu.injector.context.ResolvedInstantiationContext;
import fr.xephi.authme.libs.jalu.injector.handlers.dependency.DependencyHandler;
import fr.xephi.authme.libs.jalu.injector.handlers.instantiation.DependencyDescription;
import fr.xephi.authme.libs.jalu.injector.utils.ReflectionUtils;

public class FactoryDependencyHandler
  implements DependencyHandler
{
  public Object resolveValue(ResolvedInstantiationContext<?> context, DependencyDescription dependencyDescription)
  {
    if (dependencyDescription.getType() == Factory.class)
    {
      Class<?> genericType = ReflectionUtils.getGenericType(dependencyDescription.getGenericType());
      if (genericType == null) {
        throw new IllegalStateException("Factory fields must have concrete generic type. Cannot get generic type for field in '" + context.getMappedClass() + "'");
      }
      return new FactoryImpl(genericType, context.getInjector());
    }
    return null;
  }
  
  private static final class FactoryImpl<P>
    implements Factory<P>
  {
    private final Injector injector;
    private final Class<P> parentClass;
    
    FactoryImpl(Class<P> parentClass, Injector injector)
    {
      this.parentClass = parentClass;
      this.injector = injector;
    }
    
    public <C extends P> C newInstance(Class<C> clazz)
    {
      if (this.parentClass.isAssignableFrom(clazz)) {
        return (C)this.injector.newInstance(clazz);
      }
      throw new IllegalArgumentException(clazz + " not child of " + this.parentClass);
    }
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\initialization\factory\FactoryDependencyHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */