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
import TYPES.TYPE_FUNCTION;
import MIPS.*;
import java.util.*;

public class IRcommand_Return extends IRcommand
{
    TEMP rv;
    String func_fullname;

    // return <rv>;

    public IRcommand_Return(TEMP return_value,
                            String func_fullname)
	{
        this.rv = return_value;
        this.func_fullname = func_fullname;
	}
	
	public void MIPSme()
	{
        if (this.rv != null)
        {
            MIPSGenerator.getInstance().set_rv(rv);
        }

        MIPSGenerator.getInstance().jump("epilogue_" + func_fullname);
    }

    // get the temps whome values are used when applying the command
    public LinkedList<TEMP> getUsedTemps()
    {
        LinkedList<TEMP> res = new LinkedList<TEMP>();

        if (this.rv != null)
        {
            res.add(rv);
        }

        return res;
    }

	// get the temps whome values are changed after applying the command
	// (which are the temps whome previous values are no more used)
    public LinkedList<TEMP> getChangedTemps()
    {
        return null;
    }
}