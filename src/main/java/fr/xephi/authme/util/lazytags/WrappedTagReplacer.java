package fr.xephi.authme.util.lazytags;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WrappedTagReplacer<T, A>
{
  private final Collection<T> items;
  private final BiFunction<T, String, ? extends T> itemCreator;
  private final TagReplacer<A> tagReplacer;
  
  public WrappedTagReplacer(Collection<Tag<A>> allTags, Collection<T> items, Function<? super T, String> stringGetter, BiFunction<T, String, ? extends T> itemCreator)
  {
    this.items = items;
    this.itemCreator = itemCreator;
    
    List<String> stringItems = (List)items.stream().map(stringGetter).collect(Collectors.toList());
    this.tagReplacer = TagReplacer.newReplacer(allTags, stringItems);
  }
  
  public List<T> getAdaptedItems(A argument)
  {
    List<String> adaptedStrings = this.tagReplacer.getAdaptedMessages(argument);
    List<T> adaptedItems = new LinkedList();
    
    Iterator<T> originalItemsIter = this.items.iterator();
    Iterator<String> newStringsIter = adaptedStrings.iterator();
    while ((originalItemsIter.hasNext()) && (newStringsIter.hasNext())) {
      adaptedItems.add(this.itemCreator.apply(originalItemsIter.next(), newStringsIter.next()));
    }
    return adaptedItems;
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authm\\util\lazytags\WrappedTagReplacer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */