/***********/
/* PACKAGE */
/***********/
package IR;

/*******************/
/* GENERAL IMPORTS */
/*******************/
import java.util.*;
/*******************/
/* PROJECT IMPORTS */
/*******************/
import TEMP.*;

public abstract class IRcommand
{
	/*****************/
	/* Label Factory */
	/*****************/
	protected static int label_counter=0;

	public static String getFreshLabel(String msg)
	{
		return String.format("Label_%d_%s",label_counter++,msg);
	}

	/* more fields */

	// the function that this command is defined at. -1 if it doesn't defined 
	// inside of a function.
	public int function_id = -1;

	// The 'IN' list for the command at the Liveness Analysis
	public LinkedList<Integer> IN = new LinkedList<>();  

	// All the commands that this IR command jumps to
	public LinkedList<IRcommand> jumps_to = new LinkedList<>();

	// every IRcommand has to implement the followig method
	public abstract void MIPSme();

	/*
	update the 'IN' list for the command at the Liveness Analysis by :

	A. joining all the lists of the command at 'jumps_to' (the commands that
		this command jumps to)

	B. updating the living variables list (the 'IN' list) by adding the 
	   temporaries used at the command, and by throwing from the list
	   the temporaries which their values change when the command's applied
	*/
	public abstract void updateINList();
}
