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
	public void MIPSme()
	{
		MIPSGenerator.getInstance().add_prologue();
	}
}
