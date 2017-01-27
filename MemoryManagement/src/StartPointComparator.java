import java.util.Comparator;

/**
 * 
 * @author Andrew Fuller
 * 
 */
public class StartPointComparator implements Comparator<Holes>
{

  /**
   * 
   * @param args
   *          -
   */
  public static void main(String[] args)
  {
    // TODO Auto-generated method stub
  }

  /**
   * @param x
   *          -
   * @param y
   *          -
   * @return -
   */
  public int compare(Holes x, Holes y)
  {
    return x.getStartPoint() < y.getStartPoint() ? -1
        : x.getStartPoint() == y.getStartPoint() ? 0 : 1;
  }


}
