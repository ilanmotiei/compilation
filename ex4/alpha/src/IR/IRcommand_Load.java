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

public class IRcommand_Load extends IRcommand
{
	TEMP dst;
	String var_name;

	boolean isLocalVar;
	boolean isArg;
	int offset;
	
	public IRcommand_Load(TEMP dst,String var_name, boolean isLocalVar, boolean isArg, int offset)
	{
		this.dst      = dst;
		this.var_name = var_name;
		this.isLocalVar = isLocalVar;
		this.isArg = isArg;
		this.offset = offset;
	}
	
	public void MIPSme()
	{
		MIPSGenerator.getInstance().load(dst, var_name, isLocalVar, isArg, offset);
	}
}
