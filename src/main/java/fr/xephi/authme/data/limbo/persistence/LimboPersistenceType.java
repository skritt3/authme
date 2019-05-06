package fr.xephi.authme.data.limbo.persistence;

public enum LimboPersistenceType
{
  INDIVIDUAL_FILES(IndividualFilesPersistenceHandler.class),  DISTRIBUTED_FILES(DistributedFilesPersistenceHandler.class),  DISABLED(NoOpPersistenceHandler.class);
  
  private final Class<? extends LimboPersistenceHandler> implementationClass;
  
  private LimboPersistenceType(Class<? extends LimboPersistenceHandler> implementationClass)
  {
    this.implementationClass = implementationClass;
  }
  
  public Class<? extends LimboPersistenceHandler> getImplementationClass()
  {
    return this.implementationClass;
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\data\limbo\persistence\LimboPersistenceType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */