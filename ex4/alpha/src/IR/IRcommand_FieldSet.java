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


public class IRcommand_FieldSet extends IRcommand
{
    TEMP obj;  // The register that contains the object address
    TYPE_CLASS cls;
    String field_name;
    TEMP value;

    // src.field_name = value;
	
	public IRcommand_FieldSet(TEMP obj, TYPE_CLASS cls, String field_name, TEMP value)
	{
        this.obj = obj;
        this.cls = cls;
        this.field_name = field_name;
        this.value = value;
	}
	
	public void MIPSme()
	{
		MIPSGenerator.getInstance().field_set(obj, cls, field_name, value);
	}
}