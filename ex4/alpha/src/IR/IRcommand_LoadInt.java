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
}
