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
	public LinkedList<TEMP> IN = new LinkedList<>();  

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
	public void UpdateINList()
	{
		JoinIncomingLists();
		ApplyUpdatingFunction();
	}

	// applies the updating function rule of the IR command
	public void ApplyUpdatingFunction()
	{
		// the temps that their values were used at the command
		LinkedList<TEMP> new_living_temps = getUsedTemps();

		// the temps whom values were changed at the command
		LinkedList<TEMP> nonliving_temps = getChangedTemps();  

		if (new_living_temps != null)
		{
			addLivingTemps(new_living_temps);
		}
		if (nonliving_temps != null)
		{
			removeNonLivingTemps(nonliving_temps);
		}
	}

	public void addLivingTemps(LinkedList<TEMP> tmp_list)
	{
		for (TEMP t : tmp_list)
		{
			if (! IN.contains(t))
			{
				IN.add(t);
			}
		}
	}

	public void removeNonLivingTemps(LinkedList<TEMP> tmp_list)
	{
		for (TEMP t : tmp_list)
		{
			if (IN.contains(t))
			{
				IN.remove(t);
			}
		}
	}

	// updates the IN list of this by joining all of the IN lists of the IR commands
	// that this command jumps to
	public void JoinIncomingLists()
	{
		for (IRcommand cmd : this.jumps_to)
		{
			for (TEMP tmp : cmd.IN)
			{
				if (!cmd.IN.contains(tmp))
				{
					cmd.IN.add(tmp);
				}
			}
		}
	}

	// get the temps whome values are used when applying the command
	public abstract LinkedList<TEMP> getUsedTemps();

	// get the temps whome values are changed after applying the command
	// (which are the temps whome previous values are no more used)
	public abstract LinkedList<TEMP> getChangedTemps();

	// get all the temporals involved at the command
	public LinkedList<TEMP> getCMDTemps()
	{
		LinkedList<TEMP> used_temps = getUsedTemps();
		LinkedList<TEMP> changed_temps = getChangedTemps();

		if (used_temps == null)
		{
			return changed_temps;
		}

		// else:

		if (changed_temps == null)
		{
			return used_temps;
		}

		// else:

		LinkedList<TEMP> res = new LinkedList<TEMP>();

		for (TEMP t : used_temps)
		{
			res.add(t);
		}

		for (TEMP t : changed_temps)
		{
			if (! res.contains(t))
			{
				res.add(t);
			}
		}

		return res;
	}
}

