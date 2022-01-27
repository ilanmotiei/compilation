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
    
    // get the temps whome values are used when applying the command
    public LinkedList<TEMP> getUsedTemps()
    {
        return null;
    }

	// get the temps whome values are changed after applying the command
	// (which are the temps whome previous values are no more used)
    public LinkedList<TEMP> getChangedTemps()
    {
        return null;
    }
}
