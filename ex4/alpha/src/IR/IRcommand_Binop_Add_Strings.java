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

public class IRcommand_Binop_Add_Strings extends IRcommand
{
	public TEMP dst;
	public TEMP src1;
	public TEMP src2;

	public IRcommand_Binop_Add_Strings(TEMP dst,TEMP src1,TEMP src2)
	{
		this.dst = dst;
		this.src1 = src1;
		this.src2 = src2;
	}
	
	public void MIPSme()
	{
		MIPSGenerator.getInstance().str_add(dst, t1, t2);
	}
}