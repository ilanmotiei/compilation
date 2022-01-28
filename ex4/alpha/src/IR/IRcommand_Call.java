/***********/
/* PACKAGE */
/***********/
package IR;

/*******************/
/* GENERAL IMPORTS */
/*******************/

/*******************/
/* PROJECT IMPORTS */
/*******************/
import TEMP.*;

import java.util.*;

import MIPS.*;

public class IRcommand_Call extends IRcommand
{
	TEMP_LIST args;
	String func_name;

	// func_name(args);
	
	public IRcommand_Call(TEMP_LIST args, String func_name)
	{
		this.args = args;
		this.func_name = func_name;
	}
	
	public void MIPSme()
	{
        MIPSGenerator.getInstance().call(args, func_name);
	}

	// get the temps whome values are used when applying the command
	public LinkedList<TEMP> getUsedTemps()
	{
		LinkedList<TEMP> res = new LinkedList<>();

		if (args != null)
		{
			for (TEMP a : args)
			{
				res.add(a);
			}
		}

		return res;
	}

	// get the temps whome values are changed after applying the command
	// (which are the temps whome previous values are no more used)
	public LinkedList<TEMP> getChangedTemps()
	{
		return null;
	}
}