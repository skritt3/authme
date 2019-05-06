package fr.xephi.authme.datasource;

public final class DataSourceResult<T>
{
  private static final DataSourceResult UNKNOWN_PLAYER = new DataSourceResult(null);
  private final T value;
  
  private DataSourceResult(T value)
  {
    this.value = value;
  }
  
  public static <T> DataSourceResult<T> of(T value)
  {
    return new DataSourceResult(value);
  }
  
  public static <T> DataSourceResult<T> unknownPlayer()
  {
    return UNKNOWN_PLAYER;
  }
  
  public boolean playerExists()
  {
    return this != UNKNOWN_PLAYER;
  }
  
  public T getValue()
  {
    return (T)this.value;
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\datasource\DataSourceResult.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */