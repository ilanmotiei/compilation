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

public class IRcommand_LoadImmediate extends IRcommand
{
	TEMP dst;
    int value;

    // dst = value
    
	public IRcommand_LoadImmediate(TEMP dst, int value)
	{
        this.dst = dst;
        this.value = value;
	}
	
	public void MIPSme()
	{
		MIPSGenerator.getInstance().load_immediate(dst, value);
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
