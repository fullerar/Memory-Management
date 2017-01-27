# Memory-Management

JMU Fall '16

Author: Andrew Fuller

With supervision from Dr. Brett Tjaden
https://users.cs.jmu.edu/tjadenbc/Web/

PROJECT OVERVIEW
-----------------------------

-This project will deal with memory management. It will simulate contiguous memory
allocation using the first-fit, best-fit, and worst-fit algorithms.

-The simulation ends when either the last memory operation completes successfully or
any memory operation cannot be satisfied. If all memory operations complete successfully,
the program should output the word "Success." If a memory operation cannot be satisfied
the program should output two lines:

	1. The word "Failed", the reference number for the request that failed, and the number
		of bytes for the request that failed.
	2. The amount of external fragmentation (in bytes) when the request failed.

GENERAL USAGE NOTES
-----------------------------

-This submission uses one driver class (Driver.java) to run the program.

-The Driver class reads from standard input.

-This submission also includes the following classes:

	-TakenBlocks.java to represent blocks of memory that are taken.
	
	-Holes.java to represent blocks of memory that are not taken.
	
	-MemoryAllocations.java to represent the incoming memory allocations.
	
	-StartPointComparator.java to help sort lists of Holes objects by 
		their start point.
	
-Input file required structure:

	-.txt file containing the following information

	-Each line should contain 3 individual integer numbers in the form:
		x1 x2 x3
	
	-x1: the process id number
	
	-x2: should be either a 1 for "allocate" or 2 for "deallocate"
	
	-x3: if x2=1 then x3 = the number of bytes to be allocated.
		  else if x2=2 then x3 = the process id of bytes to be deallocated.
		  
		  For example:
		  1 1 10 => would allocate 10 bytes
		  2 2 1  => would deallocate the 10 bytes allocated by process 1
		  
	-Note: The max bytes allowed is set at 1024 in the Driver class
	
-This submission compiles all classes with use of a makeFile.

-The Driver class reads from standard input.

	-To run Driver at the command line:
		1. $ make
		2. $ java Driver < input.txt
			Note: "input.txt" does not have to be named "input.txt" 
					but the test file to be used should be in the src package
