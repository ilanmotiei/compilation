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

public class IRcommand_Return extends IRcommand
{
    TEMP rv;
    String func_fullname;
    
    // return rv;

	public IRcommand_Store(TEMP return_value, String func_fullname)
	{
        this.rv = return_value;
        this.func_fullname = func_fullname;
	}
	
	public void MIPSme()
	{
        MIPSGenerator.getInstance().set_rv(rv);

        MIPSGenerator.getInstance().jump("epilogue_" + func_fullname);
    }
}