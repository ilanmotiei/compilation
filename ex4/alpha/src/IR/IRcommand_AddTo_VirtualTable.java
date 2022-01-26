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

public class IRcommand_AddTo_VirtualTable extends IRcommand
{
    TYPE_CLASS cls;
    String method_name;
	
	public IRcommand_AddTo_VirtualTable(TYPE_CLASS cls, String method_name)
    {
        this.cls = cls;
        this.method_name = name;
	}
	
	public void MIPSme()
	{
		MIPSGenerator.getInstance().add_to_vt(cls, cls, method_name);
	}
}
