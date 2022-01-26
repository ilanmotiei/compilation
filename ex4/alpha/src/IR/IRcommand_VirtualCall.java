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

public class IRcommand_VirtualCall extends IRcommand
{
	TEMP src;
	TEMP_LIST args;
	TYPE_CLASS cls;
	String func_name;

	// <src>.func_name(args);
	
	public IRcommand_VirtualCall(TEMP src, TEMP_LIST args, TYPE_CLASS cls, String func_name)
	{
		this.src = src;
		this.args = args;
		this.cls = cls;
		this.func_name = func_name;
	}
	
	public void MIPSme()
	{
		MIPSGenerator.getInstance().virtual_call(src, args, cls, func_name);
	}
}