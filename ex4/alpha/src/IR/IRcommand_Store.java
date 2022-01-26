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
	TYPE_CLASS cls; // the class we are at when this storing was called.
	TEMP src;

	boolean isLocalVar;
	boolean isArg;
	boolean isClassField;
	int offset;
	
	public IRcommand_Store(String var_name, 
							TYPE_CLASS cls,
							TEMP src, 
							boolean isLocalVar, 
							boolean isArg, 
							boolean isClassField,
							int offset)
	{
		this.var_name = var_name;
		this.cls = cls;
		this.src = src;

		this.isLocalVar = isLocalVar;
		this.isArg = isArg;
		this.isClassField = isClassField;
		this.offset = offset;
	}
	
	public void MIPSme()
	{
		MIPSGenerator.getInstance().store(var_name, 
											cls,
											src, 
											isLocalVar, 
											isArg, 
											isClassField,
											offset);
	}
}
