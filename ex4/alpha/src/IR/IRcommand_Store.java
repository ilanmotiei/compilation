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

public class IRcommand_Store extends IRcommand
{
	String var_name;
	TEMP src;

	boolean isLocalVar;
	boolean isArg;
	int offset;
	
	public IRcommand_Store(String var_name, TEMP src, boolean isLocalVar, boolean isArg, int offset)
	{
		this.src      = src;
		this.var_name = var_name;

		this.isLocalVar = isLocalVar;
		this.isArg = isArg;
		this.offset = offset;
	}
	
	public void MIPSme()
	{
		MIPSGenerator.getInstance().store(var_name, src, isLocalVar, isArg, offset);
	}
}
