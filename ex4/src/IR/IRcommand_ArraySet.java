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

public class IRcommand_ArraySet extends IRcommand
{
    TEMP arr;  // The register that contains the array address
    TEMP idx;
    TEMP value;

    // arr[idx] = value;
	
	public IRcommand_ArraySet(TEMP arr, TEMP idx, TEMP value)
	{
		this.arr = arr;
        this.idx = idx;
        this.value = value;
	}
	
	public void MIPSme()
	{
		MIPSGenerator.getInstance().array_set(arr, idx, value);
	}

	// get the temps whome values are used when applying the command
	public LinkedList<TEMP> getUsedTemps()
	{
		LinkedList<TEMP> res = new LinkedList<>();
		res.add(arr);
		res.add(idx);
		res.add(value);
		
		return res;
	}

	// get the temps whome values are changed after applying the command
	// (which are the temps whome previous values are no more used)
	public LinkedList<TEMP> getChangedTemps()
	{
		return null;
	}
}
