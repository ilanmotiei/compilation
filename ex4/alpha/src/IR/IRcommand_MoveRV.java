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

// MOVES THE RETURN VALUE TO THE DESTINATION REGISTER
public class IRcommand_MoveRV extends IRcommand
{
	TEMP dst;
	
	public IRcommand_Load(TEMP dst)
	{
		this.dst = dst;
	}
	
	public void MIPSme()
	{
		MIPSGenerator.getInstance().move_rv(dst);
	}
}