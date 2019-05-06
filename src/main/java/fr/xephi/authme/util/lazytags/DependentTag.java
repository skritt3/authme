package fr.xephi.authme.util.lazytags;

import java.util.function.Function;

public class DependentTag<A>
  implements Tag<A>
{
  private final String name;
  private final Function<A, String> replacementFunction;
  
  public DependentTag(String name, Function<A, String> replacementFunction)
  {
    this.name = name;
    this.replacementFunction = replacementFunction;
  }
  
  public String getName()
  {
    return this.name;
  }
  
  public String getValue(A argument)
  {
    return (String)this.replacementFunction.apply(argument);
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authm\\util\lazytags\DependentTag.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */