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
        
	}
}