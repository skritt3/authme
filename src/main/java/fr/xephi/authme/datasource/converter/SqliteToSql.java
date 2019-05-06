package fr.xephi.authme.datasource.converter;

import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.datasource.DataSourceType;
import fr.xephi.authme.datasource.SQLite;
import fr.xephi.authme.initialization.DataFolder;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.settings.Settings;
import java.io.File;
import java.sql.SQLException;

public class SqliteToSql
  extends AbstractDataSourceConverter<SQLite>
{
  private final Settings settings;
  private final File dataFolder;
  
  @Inject
  SqliteToSql(Settings settings, DataSource dataSource, @DataFolder File dataFolder)
  {
    super(dataSource, DataSourceType.MYSQL);
    this.settings = settings;
    this.dataFolder = dataFolder;
  }
  
  protected SQLite getSource()
    throws SQLException
  {
    return new SQLite(this.settings, this.dataFolder);
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\datasource\converter\SqliteToSql.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */