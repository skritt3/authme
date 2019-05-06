package fr.xephi.authme.datasource.converter;

import fr.xephi.authme.data.auth.PlayerAuth;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.datasource.FlatFile;

public class ForceFlatToSqlite
  extends AbstractDataSourceConverter<FlatFile>
{
  private final FlatFile source;
  
  public ForceFlatToSqlite(FlatFile source, DataSource destination)
  {
    super(destination, destination.getType());
    this.source = source;
  }
  
  public FlatFile getSource()
  {
    return this.source;
  }
  
  protected void adaptPlayerAuth(PlayerAuth auth)
  {
    auth.setRealName("Player");
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\datasource\converter\ForceFlatToSqlite.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */