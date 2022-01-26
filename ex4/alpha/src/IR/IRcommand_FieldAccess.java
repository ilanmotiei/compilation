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
import TYPES.*;

public class IRcommand_FieldAccess extends IRcommand
{
	TEMP dst;
	TEMP obj;  // the object from which the field is acceseed
	TYPE_CLASS cls;  // the class type of the object
	String FieldName;

	// <dst> = <obj>.field_name
	
	public IRcommand_FieldAccess(TEMP dst, TEMP obj, TYPE_CLASS cls, String FieldName)
	{
		this.dst = dst;
		this.obj = obj;
		this.cls = cls;
		this.FieldName = FieldName;
	}
	
	public void MIPSme()
	{
		MIPSGenerator.getInstance().field_access(dst, obj, cls, FieldName);
	}
}
