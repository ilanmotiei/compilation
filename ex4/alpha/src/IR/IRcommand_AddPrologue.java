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

public class IRcommand_AddPrologue extends IRcommand
{
    //  the maximum offset local variables defined inside the function
    int function_max_local_var_offset;
    
    public IRcommand_AddPrologue(int function_max_local_var_offset)
    {
        this.function_max_local_var_offset = function_max_local_var_offset;
    }

	public void MIPSme()
	{
		MIPSGenerator.getInstance().add_prologue(function_max_local_var_offset);
	}
}
