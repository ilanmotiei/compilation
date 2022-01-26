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

public class IRcommand_Binop_Divide_Integers extends IRcommand
{
	public TEMP t1;
	public TEMP t2;
	public TEMP dst;
	
	public IRcommand_Binop_Divide_Integers(TEMP dst,TEMP t1,TEMP t2)
	{
		this.dst = dst;
		this.t1 = t1;
		this.t2 = t2;
	}
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
        // A RUNNING TIME CHECK FOR A DIVISION BY ZERO : 
        MIPSGenerator.getInstance().beqz(t2, "division_by_zero_abort");

        // PERFORMING THE DIVISION : 
		MIPSGenerator.getInstance().divide(dst,t1,t2);
	}
}