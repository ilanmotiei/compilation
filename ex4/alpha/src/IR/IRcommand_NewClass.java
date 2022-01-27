
package IR;

/*******************/
/* GENERAL IMPORTS */
/*******************/

/*******************/
/* PROJECT IMPORTS */
/*******************/
import TEMP.*;
import MIPS.*;
import TYPE.*;

public class IRcommand_NewClass extends IRcommand
{
	TEMP dst;
    TYPE_CLASS type;
    
    // defines a new class element of
	public IRcommand_NewClass(TEMP dst, TYPE type)
	{
		this.dst = dst;
        this.type = type;
	}
	
	public void MIPSme()
	{
        MIPSGenerator.getInstance().allocate_class_obj(type, dst);
	}
}