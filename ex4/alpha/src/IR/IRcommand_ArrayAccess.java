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

public class IRcommand_ArrayAccess extends IRcommand
{
	TEMP dst;
    TEMP src;  // The register that contains the array address
    TEMP idx;
	
	public IRcommand_ArrayAccess(TEMP dst, TEMP src, TEMP idx)
	{
		this.dst = dst;
        this.src = src;
        this.idx = idx;
	}
	
	public void MIPSme()
	{
		MIPSGenerator.getInstance().array_access(dst, src, idx);
	}

	// get the temps whome values are used when applying the command
	public LinkedList<TEMP> getUsedTemps()
	{
		LinkedList<TEMP> res = new LinkedList<TEMP>();
		res.add(src);
		res.add(idx);

		return res;
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
