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

public class IRcommand_Binop_Add_Strings extends IRcommand
{
	public TEMP dst;
	public TEMP src1;
	public TEMP src2;

	public IRcommand_Binop_Add_Strings(TEMP dst,TEMP src1,TEMP src2)
	{
		this.dst = dst;
		this.src1 = src1;
		this.src2 = src2;
	}
	
	public void MIPSme()
	{
		MIPSGenerator.getInstance().str_add(dst, src1, src2);
	}

	// get the temps whom values are used when applying the command
	public LinkedList<TEMP> getUsedTemps()
	{
		LinkedList<TEMP> res = new LinkedList<TEMP>();
		res.add(src1);
		res.add(src2);

		return res;
	}

	// get the temps whom values are changed after applying the command
	// (which are the temps whome previous values are no more used)
	public LinkedList<TEMP> getChangedTemps()
	{
		LinkedList<TEMP> res = new LinkedList<TEMP>();
		res.add(dst);

		return res;
	}
}