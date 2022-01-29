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

public class IRcommand_Load extends IRcommand
{
	TEMP dst;
	String var_name;
	TYPE_CLASS cls;  // the class we are at when this loading was called. may be null

	boolean isLocalVar;
	boolean isArg;
	boolean isClassField;
	int offset;
	
	public IRcommand_Load(TEMP dst,
						String var_name, 
						TYPE_CLASS cls,
						boolean isLocalVar, 
						boolean isArg, 
						boolean isClassField,
						int offset)
	{
		this.dst = dst;
		this.var_name = var_name;
		this.cls = cls; 
		this.isLocalVar = isLocalVar;
		this.isArg = isArg;
		this.isClassField = isClassField;
		this.offset = offset;
	}
	
	public void MIPSme()
	{
		MIPSGenerator.getInstance().load(dst, 
										var_name, 
										cls,
										isLocalVar, 
										isArg, 
										isClassField, 
										offset);
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
		LinkedList<TEMP> res = new LinkedList<TEMP>();
		res.add(dst);

		return res;
	}
}
