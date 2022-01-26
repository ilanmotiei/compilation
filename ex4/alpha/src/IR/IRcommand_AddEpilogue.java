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

public class IRcommand_AddEpilogue extends IRcommand
{
	public void MIPSme()
	{
		MIPSGenerator.getInstance().add_epilogue();
	}
}
