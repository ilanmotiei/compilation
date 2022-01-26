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
}