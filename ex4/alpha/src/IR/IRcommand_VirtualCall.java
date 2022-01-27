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

import java.util.LinkedList;

import MIPS.*;

public class IRcommand_VirtualCall extends IRcommand
{
	TEMP src;
	TEMP_LIST args;
	TYPE_CLASS cls;
	String func_name;

	// <src>.func_name(args);
	
	public IRcommand_VirtualCall(TEMP src, TEMP_LIST args, TYPE_CLASS cls, String func_name)
	{
		this.src = src;
		this.args = args;
		this.cls = cls;
		this.func_name = func_name;
	}
	
	public void MIPSme()
	{
		MIPSGenerator.getInstance().virtual_call(src, args, cls, func_name);
	}

	// get the temps whome values are used when applying the command
	public LinkedList<TEMP> getUsedTemps()
	{
		LinkedList<TEMP> res = new LinkedList<>();

		res.add(src);

		for (TEMP t : args)
		{
			res.add(t);
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