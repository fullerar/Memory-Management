
public class Holes implements Comparable<Holes>
{
  
  private int startPoint;
  private int endPoint;
  
  public Holes(int startPoint, int endPoint)
  {
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

  public int compareTo(Holes x, Holes y)
  {
    return x.getStartPoint() > y.getStartPoint() ? -1
        : x.getStartPoint() == y.getStartPoint() ? 0 : 1;
  }

  public int compareTo(Holes o)
  {
    // TODO Auto-generated method stub
    return 0;
  }

}
