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
import java.util.*;

public class IRcommand_Jump_Label extends IRcommand
{
	public String label_name;
	
	public IRcommand_Jump_Label(String label_name)
	{
		this.label_name = label_name;
	}
	
	public void MIPSme()
	{
		MIPSGenerator.getInstance().jump(label_name);
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
