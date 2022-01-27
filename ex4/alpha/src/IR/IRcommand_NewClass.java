
package IR;

/*******************/
/* GENERAL IMPORTS */
/*******************/

/*******************/
/* PROJECT IMPORTS */
/*******************/
import TEMP.*;
import MIPS.*;
import TYPE.*;

public class IRcommand_NewClass extends IRcommand
{
	TEMP dst;
    TYPE_CLASS type;
    
    // defines a new class element of
	public IRcommand_NewClass(TEMP dst, TYPE type)
	{
		this.dst = dst;
        this.type = type;
	}
	
	public void MIPSme()
	{
        MIPSGenerator.getInstance().allocate_class_obj(type, dst);
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