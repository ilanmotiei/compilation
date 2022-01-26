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

public class IRcommand_ClassDec extends IRcommand
{
	TYPE_CLASS cls;
	
	public IRcommand_Call(TYPE_CLASS cls)
	{
		this.cls = cls;
	}
	
	public void MIPSme()
	{
        MIPSGenerator.getInstance().init_class_vtable(cls);
	}
}