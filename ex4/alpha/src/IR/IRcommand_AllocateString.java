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

public class IRcommand_AllocateString extends IRcommand
{
    TEMP dst;
	String str;
	
	public IRcommand_AllocateString(TEMP dst, String str)
	{
        this.dst = dst;
        this.str = str;
	}

	public void MIPSme()
	{
		MIPSGenerator.getInstance().allocate_string(dst, str);
	}

	// get the temps whome values are used when applying the command
	public LinkedList<TEMP> getUsedTemps()
	{
		return null;
	}

	// get the temps whome values are changed after applying the command
	// (which are the temps whome previous values are no more used)
	public LinkedList<TEMP> getChangedTemps()
	{
		LinkedList<TEMP> res = new LinkedList<>();
		res.add(dst);

		return res;
	}
}