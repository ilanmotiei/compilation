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

public class IRcommand_Call extends IRcommand
{
	TEMP_LIST args;
	String func_name;

	// func_name(args);
	
	public IRcommand_Call(TEMP_LIST args, String func_name)
	{
		this.args = args;
		this.func_name = func_name;
	}
	
	public void MIPSme()
	{
        MIPSGenerator.getInstance().push_args(args);
		MIPSGenerator.getInstance().jal(func_name);
	}
}