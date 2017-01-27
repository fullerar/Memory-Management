import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;


public class Driver
{
  final static int MAX = 1024;

  public static void main(String[] args) throws FileNotFoundException
  {
    int refNum = 0;
    int operation = 0;
    int argument = 0;

    Scanner input = new Scanner(System.in);
    // to read from file

    List<MemoryAllocations> ffList = new ArrayList<MemoryAllocations>();
    // creates a list for ff processes

    List<MemoryAllocations> bfList = new ArrayList<MemoryAllocations>();
    // creates a list for bf processes

    List<MemoryAllocations> wfList = new ArrayList<MemoryAllocations>();
    // creates a list for wf processes

    while (input.hasNext()) // read in processes and add to Memory Allocation objects
    {
      refNum = input.nextInt();
      operation = input.nextInt();
      argument = input.nextInt();

      MemoryAllocations newMem = new MemoryAllocations(refNum, operation, argument);

      ffList.add(newMem);
      bfList.add(newMem);
      wfList.add(newMem);
    }
    input.close();

    System.out.println("FirstFit Results:");
    System.out.println(ffDriver(ffList));       //runs the driver for FirstFit
    System.out.println("\nBestFit Results:");
    System.out.println(bfDriver(bfList));       //runs the driver for BestFit
    System.out.println("\nWorstFit Results:");
    System.out.println(wfDriver(wfList));       //runs the driver for WorstFit
  }

  
  
  /**
   * 
   * @param list - list of Memory Allocations to either allocate or deallocate
   * @return - string saying success or where the allocation failed
   */
  public static String ffDriver(List<MemoryAllocations> list)
  {
    boolean allFit = true;
    String result = "";

    StartPointComparator comparator = new StartPointComparator(); 
    //to keep the list of holes in order by sorting them based on start point

    //the initial hole
    Holes hole = new Holes(0, 1023);
    List<Holes> holeList = new ArrayList<Holes>();
    holeList.add(hole);

    List<TakenBlocks> takenList = new ArrayList<TakenBlocks>();

    for(MemoryAllocations x : list)
    {
      Collections.sort(holeList, comparator);
      holeList = condense(holeList);
      Collections.sort(holeList, comparator);
      holeList = condense(holeList);
      Collections.sort(holeList, comparator);

      /* Uncomment below for debugging purposes */
      //      int count = 0;
      //      for(Holes y : holeList)
      //      {
      //        System.out.println("Hole found at " + count + " " + y.getStartPoint() + "-" + y.getEndPoint());
      //        count++;
      //      }
      //      System.out.println("==================");

      //to allocate, search holes and place in the bytes to be allocated, fails if there is not an open hole
      //creates new hole from old hole - incoming bytes
      if(x.getOperation() == 1)
      {
        if(holeCheck(holeList, x.getArgument()))
        {
          for(Holes z : holeList)
          {
            if(((z.getEndPoint() - z.getStartPoint()) + 1) >= x.getArgument())
            {
              int newHoleStart = z.getStartPoint() + x.getArgument();
              int newHoleEnd = z.getEndPoint();

              TakenBlocks newTaken = new TakenBlocks(x.getRefNum(), z.getStartPoint(), z.getStartPoint() + x.getArgument());
              takenList.add(newTaken);

              if(newHoleStart < newHoleEnd)
              {
                Holes newHole = new Holes(newHoleStart, newHoleEnd);
                holeList.add(newHole);
              }

              holeList.remove(z);

              break;
            }
          }
        }
        else
        {
          allFit = false;
          result = String.format("Process %d failed to allocate %d bytes.\nExternal Fragmentation: %d", x.getRefNum(), x.getArgument(), fragCount(holeList));
          break;
        }
      }
      //to deallocate
      //creates new hole
      else if(x.getOperation() == 2)
      {
        for(TakenBlocks a : takenList)
        {
          boolean enter = a.getRefNum() == x.getArgument();
          if(enter)
          {
            int newHoleStart = a.getStartPoint();
            int newHoleEnd = a.getEndPoint() - 1;
            Holes newHole = new Holes(newHoleStart, newHoleEnd);
            holeList.add(newHole);
            takenList.remove(a);
            break;
          }
        }

      }
    }
    if(allFit)
    {
      result = String.format("Success.");
    }
    return result;
  }
  
  
  

  /* Driver method for Best Fit */
  public static String bfDriver(List<MemoryAllocations> list)
  {    
    boolean allFit = true;
    String result = "";

    StartPointComparator comparator = new StartPointComparator();
    //to keep the list of holes in order by sorting them based on start point

    Holes hole = new Holes(0, 1023);
    List<Holes> holeList = new ArrayList<Holes>();
    holeList.add(hole);

    List<TakenBlocks> takenList = new ArrayList<TakenBlocks>();

    ArrayList<Integer> bytes = new ArrayList<Integer>();
    for(int i = 0; i < 1024; i++)
    {
      bytes.add(i, 0);
    }

    for(MemoryAllocations x : list)
    {
      Collections.sort(holeList, comparator);
      holeList = condense(holeList);
      Collections.sort(holeList, comparator);
      holeList = condense(holeList);
      Collections.sort(holeList, comparator);

      /* Uncomment below for debugging purposes */
//      int count = 0;
//      System.out.println();
//      for(Holes y : holeList)
//      {
//        System.out.println("Hole found at " + count + " " + y.getStartPoint() + "-" + y.getEndPoint());
//        count++;
//      }
//      System.out.println("==================");


      if(x.getOperation() == 1)
      {
        if(holeCheck(holeList, x.getArgument()))
        {
          Holes bestFitHole = findBestFitHole(holeList, x.getArgument());
          //              System.out.println(bestFitHole.getStartPoint() + " " + bestFitHole.getEndPoint());
          int newHoleStart = bestFitHole.getStartPoint() + x.getArgument();
          int newHoleEnd = bestFitHole.getEndPoint();

          TakenBlocks newTaken = new TakenBlocks(x.getRefNum(), bestFitHole.getStartPoint(), bestFitHole.getStartPoint() + x.getArgument());
          takenList.add(newTaken);

          if(newHoleStart < newHoleEnd)
          {
            Holes newHole = new Holes(newHoleStart, newHoleEnd);
            holeList.add(newHole);
            //                System.out.println("HERE");
          }

          for(Holes z : holeList)
          {
            if(z.getStartPoint() == bestFitHole.getStartPoint() && z.getEndPoint() == bestFitHole.getEndPoint())
            {
              holeList.remove(z);
              break;
            }
          }
        }
        else
        {
          allFit = false;
          result = String.format("Process %d failed to allocate %d bytes.\nExternal Fragmentation: %d", x.getRefNum(), x.getArgument(), fragCount(holeList));
          break;
        }
      }
      else if(x.getOperation() == 2)
      {
        for(TakenBlocks a : takenList)
        {
          boolean enter = a.getRefNum() == x.getArgument();
          if(enter)
          {
            int newHoleStart = a.getStartPoint();
            int newHoleEnd = a.getEndPoint() - 1;
            Holes newHole = new Holes(newHoleStart, newHoleEnd);
            holeList.add(newHole);
            takenList.remove(a);
            break;
          }
        }

      }
    }
    if(allFit)
    {
      result = String.format("Success.");
    }
    return result;
  }



  /**
   * 
   * @param list - list of Memory Allocations to allocate or deallocate
   * @return - result of success or failure if allocation is not possible
   */
  public static String wfDriver(List<MemoryAllocations> list)
  {
    boolean allFit = true;
    String result = "";

    StartPointComparator comparator = new StartPointComparator();
    //to keep the list of holes in order by sorting them based on start point

    //intial hole
    Holes hole = new Holes(0, 1023);
    List<Holes> holeList = new ArrayList<Holes>();
    holeList.add(hole);

    List<TakenBlocks> takenList = new ArrayList<TakenBlocks>();


    ArrayList<Integer> bytes = new ArrayList<Integer>();
    for(int i = 0; i < 1024; i++)
    {
      bytes.add(i, 0);
    }

    for(MemoryAllocations x : list)
    {
      Collections.sort(holeList, comparator);
      holeList = condense(holeList);
      Collections.sort(holeList, comparator);
      holeList = condense(holeList);
      Collections.sort(holeList, comparator);

      /* Uncomment below for debugging pursposes */
      //          int count = 0;
      //          System.out.println();
      //          for(Holes y : holeList)
      //          {
      //            System.out.println("Hole found at " + count + " " + y.getStartPoint() + "-" + y.getEndPoint());
      //            count++;
      //          }
      //          System.out.println("==================");


      if(x.getOperation() == 1)
      {
        if(holeCheck(holeList, x.getArgument()))
        {
          Holes bestFitHole = findWorstFitHole(holeList, x.getArgument());
          //              System.out.println(bestFitHole.getStartPoint() + " " + bestFitHole.getEndPoint());
          int newHoleStart = bestFitHole.getStartPoint() + x.getArgument();
          int newHoleEnd = bestFitHole.getEndPoint();

          TakenBlocks newTaken = new TakenBlocks(x.getRefNum(), bestFitHole.getStartPoint(), bestFitHole.getStartPoint() + x.getArgument());
          takenList.add(newTaken);

          if(newHoleStart < newHoleEnd)
          {
            Holes newHole = new Holes(newHoleStart, newHoleEnd);
            holeList.add(newHole);
            //                System.out.println("HERE");
          }

          for(Holes z : holeList)
          {
            if(z.getStartPoint() == bestFitHole.getStartPoint() && z.getEndPoint() == bestFitHole.getEndPoint())
            {
              holeList.remove(z);
              break;
            }
          }
        }
        else
        {
          allFit = false;
          result = String.format("Process %d failed to allocate %d bytes.\nExternal Fragmentation: %d", x.getRefNum(), x.getArgument(), fragCount(holeList));
          break;
        }
      }
      else if(x.getOperation() == 2)
      {
        for(TakenBlocks a : takenList)
        {
          boolean enter = a.getRefNum() == x.getArgument();
          if(enter)
          {
            int newHoleStart = a.getStartPoint();
            int newHoleEnd = a.getEndPoint() - 1;
            Holes newHole = new Holes(newHoleStart, newHoleEnd);
            holeList.add(newHole);
            takenList.remove(a);
            break;
          }
        }

      }
    }
    if(allFit)
    {
      result = String.format("Success.");
    }
    return result;
  }


  /**
   * 
   * @param holeList - list of Holes objects to search through
   * @param incoming - the bytes to be allocated
   * @return - the hole that leaves least remaining hole space after allocation
   */
  public static Holes findBestFitHole(List<Holes> holeList, int incoming)
  {
    Holes newHole = new Holes(0,0);
    int remainder = 10000;
    boolean cont = false;
    for(Holes x : holeList)
    {
      if(x.getEndPoint() - x.getStartPoint() < remainder && ((x.getEndPoint() - x.getStartPoint()) + 1) >= incoming)
      {
        remainder = x.getEndPoint() - x.getStartPoint();
        cont = true;

        newHole.setEndPoint(x.getEndPoint());
        newHole.setStartPoint(x.getStartPoint());
      }
    }
    return newHole;
  }



  /**
   * 
   * @param holeList - list of Holes objects to search through
   * @param incoming - the bytes to be allocated
   * @return - the largest hole object for incoming to go into
   */
  public static Holes findWorstFitHole(List<Holes> holeList, int incoming)
  {
    Holes newHole = new Holes(0,0);
    int remainder = 0;
    for(Holes x : holeList)
    {
      if(x.getEndPoint() - x.getStartPoint() > remainder)
      {
        remainder = ((x.getEndPoint() - x.getStartPoint()));
        //        System.out.println("REMAINDER: " + remainder);

        newHole.setEndPoint(x.getEndPoint());
        newHole.setStartPoint(x.getStartPoint());
      }
    }
    return newHole;
  }

  /**
   * 
   * @param holeList - list of Holes objects to search through
   * @param incoming - the bytes to be allocated
   * @return - true if there is a hole slot that incoming will fit into
   */
  public static boolean holeCheck(List<Holes> holeList, int incoming)
  {
    boolean fit = false;
    for(Holes x : holeList)
    {
      if(((x.getEndPoint() - x.getStartPoint()) + 1) >= incoming)
      {
        fit = true;
        break;
      }
    }
    return fit;
  }

  /**
   * 
   * @param holeList - list of Holes objects to search through
   * @return - the number of frag bytes
   */
  public static int fragCount(List<Holes> holeList)
  {
    int result = 0;
    for(Holes x : holeList)
    {
      result += x.getEndPoint() - x.getStartPoint() + 1;
    }
    return result;
  }

  /**
   * 
   * @param holeList - list of Holes objects to search through
   * @return - true if one hole start point is the same as the previous hole end point
   */
  public static boolean condenseCheck(List<Holes> holeList)
  {
    boolean result = false;
    for(Holes x : holeList)
    {
      for(Holes y : holeList)
      {
        if(x.getEndPoint() == y.getStartPoint())
        {
          result = true;
        }
      }
    }
    return result;
  }


  /**
   * 
   * @param holeList - list of Holes objects to search through
   * @return - new list of hole objects that combines hole objects that overlap or are adjacent to each other
   */
  public static List<Holes> condense(List<Holes> holeList)
  {
    List<Holes> newList = new ArrayList<Holes>();
    newList.addAll(holeList);

    Holes prevHole = null;

    boolean removeCurrent = false;

    for(int i = 0; i < holeList.size(); i++)
    {
      Holes currentHole = holeList.get(i);

      if(prevHole == null)
      {

      }
      else if( prevHole != null && currentHole.getStartPoint() - 1 == prevHole.getEndPoint())
      {
        int newHoleStart = prevHole.getStartPoint();
        int newHoleEnd = currentHole.getEndPoint();
        Holes newHole = new Holes(newHoleStart, newHoleEnd); 
        newList.add(newHole);
        newList.remove(prevHole);
        removeCurrent = true;
      }
      else if( prevHole != null && currentHole.getStartPoint() == prevHole.getEndPoint())
      {
        int newHoleStart = prevHole.getStartPoint();
        int newHoleEnd = currentHole.getEndPoint();
        Holes newHole = new Holes(newHoleStart, newHoleEnd); 
        newList.add(newHole);
        newList.remove(prevHole);
        removeCurrent = true;
      }
      else if( prevHole != null && currentHole.getStartPoint() < prevHole.getEndPoint())
      {
        int newHoleStart = prevHole.getStartPoint();
        int newHoleEnd = currentHole.getEndPoint();
        Holes newHole = new Holes(newHoleStart, newHoleEnd); 
        newList.add(newHole);
        newList.remove(prevHole);
        removeCurrent = true;
      }

      prevHole = currentHole;

      if(removeCurrent)
      {
        newList.remove(currentHole);
        removeCurrent = false;
      }
    }
    return newList;
  }
}
