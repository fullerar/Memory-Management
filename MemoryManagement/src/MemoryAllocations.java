
public class MemoryAllocations
{
  
  private int refNum;
  private int operation;
  private int argument;
  
  
  public MemoryAllocations(int refNum, int operation, int argument)
  {
    this.refNum = refNum;
    this.operation = operation;
    this.argument = argument;
  }

  public static void main(String[] args)
  {
    // TODO Auto-generated method stub

  }

  public int getRefNum()
  {
    return this.refNum;
  }

 public int getOperation()
  {
    return this.operation;
  }

  public int getArgument()
  {
    return this.argument;
  }


}
