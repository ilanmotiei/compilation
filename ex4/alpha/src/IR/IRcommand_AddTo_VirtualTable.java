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
import java.util.*;

public class IRcommand_AddTo_VirtualTable extends IRcommand
{
    TYPE_CLASS cls;
    String method_name;
	
	public IRcommand_AddTo_VirtualTable(TYPE_CLASS cls, String method_name)
    {
        this.cls = cls;
        this.method_name = method_name;
	}
	
	public void MIPSme()
	{
		MIPSGenerator.getInstance().add_to_vtable(cls, method_name);
	}

	// get the temps whome values are used when applying the command
	public LinkedList<TEMP> getUsedTemps()
	{
		return null;
	}

	// get the temps whome values are changed after applying the command
	// (which are the temps whome previous values are no more used)
	public LinkedList<TEMP> getChangedTemps()
	{
		return null;
	}
}
