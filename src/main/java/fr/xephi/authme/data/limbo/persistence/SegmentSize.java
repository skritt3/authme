package fr.xephi.authme.data.limbo.persistence;

public enum SegmentSize
{
  ONE(1, 1),  FOUR(4, 1),  EIGHT(8, 1),  SIXTEEN(16, 1),  THIRTY_TWO(2, 5),  SIXTY_FOUR(4, 3),  ONE_TWENTY(2, 7),  TWO_FIFTY(16, 2);
  
  private final int distribution;
  private final int length;
  
  private SegmentSize(int distribution, int length)
  {
    this.distribution = distribution;
    this.length = length;
  }
  
  public int getDistribution()
  {
    return this.distribution;
  }
  
  public int getLength()
  {
    return this.length;
  }
  
  public int getTotalSegments()
  {
    return (int)Math.pow(this.distribution, this.length);
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\data\limbo\persistence\SegmentSize.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */