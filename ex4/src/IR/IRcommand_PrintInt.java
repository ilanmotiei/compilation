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
import MIPS.*;
import java.util.*;

public class IRcommand_PrintInt extends IRcommand
{
	TEMP t;
	
	public IRcommand_PrintInt(TEMP t)
	{
		this.t = t;
	}
	
	public void MIPSme()
	{
		MIPSGenerator.getInstance().print_int(t);
	}

	// get the temps whome values are used when applying the command
	public LinkedList<TEMP> getUsedTemps()
	{
		LinkedList<TEMP> res = new LinkedList<>();
		res.add(t);

		return res;
	}

	// get the temps whome values are changed after applying the command
	// (which are the temps whome previous values are no more used)
	public LinkedList<TEMP> getChangedTemps()
	{
		return null;
	}
}
