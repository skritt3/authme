package fr.xephi.authme.util.lazytags;

import java.util.function.Function;
import java.util.function.Supplier;

public final class TagBuilder
{
  public static <A> Tag<A> createTag(String name, Function<A, String> replacementFunction)
  {
    return new DependentTag(name, replacementFunction);
  }
  
  public static <A> Tag<A> createTag(String name, Supplier<String> replacementFunction)
  {
    return new SimpleTag(name, replacementFunction);
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authm\\util\lazytags\TagBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */