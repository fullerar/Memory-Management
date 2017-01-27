
public class TakenBlocks
{
 
  private int refNum;
  private int startPoint;
  private int endPoint;
  
  public TakenBlocks(int refNum, int startPoint, int endPoint)
  {
    this.refNum = refNum;
    this.startPoint = startPoint;
    this.endPoint = endPoint;
  }

  public static void main(String[] args)
  {
    // TODO Auto-generated method stub

  }

  public int getStartPoint()
  {
    return startPoint;
  }

  public void setStartPoint(int startPoint)
  {
    this.startPoint = startPoint;
  }

  public int getEndPoint()
  {
    return endPoint;
  }

  public void setEndPoint(int endPoint)
  {
    this.endPoint = endPoint;
  }

  public int getRefNum()
  {
    return refNum;
  }

  public void setRefNum(int refNum)
  {
    this.refNum = refNum;
  }

}
