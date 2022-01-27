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
import TYPE.*;

public class IRcommand_NewArray extends IRcommand
{
	TEMP dst;
    TEMP arr_size;
    TYPE elems_type;
    
    // defines a new array of size arr_size with the specified element's type
	public IRcommand_NewArray(TEMP dst, TEMP arr_size, TYPE elems_type)
	{
		this.dst = dst;
        this.arr_size = arr_size;
        this.elems_type = elems_type;
	}
	
	public void MIPSme()
	{
        MIPSGenerator.getInstance().allocate_array(dst, arr_size);
	}

	// get the temps whome values are used when applying the command
	public LinkedList<TEMP> getUsedTemps()
	{
		LinkedList<TEMP> res = new LinkedList<>();
		res.add(arr_size);

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