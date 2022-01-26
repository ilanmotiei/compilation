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
		MIPSGenerator.getInstance().array_set(dst, src, idx);
	}
}
