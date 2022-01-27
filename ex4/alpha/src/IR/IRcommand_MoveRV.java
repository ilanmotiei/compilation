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

// MOVES THE RETURN VALUE TO THE DESTINATION REGISTER
public class IRcommand_MoveRV extends IRcommand
{
	TEMP dst;
	
	public IRcommand_Load(TEMP dst)
	{
		this.dst = dst;
	}
	
	public void MIPSme()
	{
		MIPSGenerator.getInstance().move_rv(dst);
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