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

    // obj.field_name = value;
	
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
    
    // get the temps whome values are used when applying the command
    public LinkedList<TEMP> getUsedTemps()
    {
        LinkedList<TEMP> res = new LinkedList<TEMP>();
        res.add(obj);
        res.add(value);

        return res;
    }

	// get the temps whome values are changed after applying the command
	// (which are the temps whome previous values are no more used)
    public LinkedList<TEMP> getChangedTemps()
    {
        return null;
    }
}