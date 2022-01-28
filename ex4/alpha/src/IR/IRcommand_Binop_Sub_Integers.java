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

public class IRcommand_Binop_Sub_Integers extends IRcommand
{
    public TEMP t1;
    public TEMP t2;
    public TEMP dst;

    public IRcommand_Binop_Sub_Integers(TEMP dst,TEMP t1,TEMP t2)
    {
        this.dst = dst;
        this.t1 = t1;
        this.t2 = t2;
    }

    public void MIPSme()
    {
        MIPSGenerator.getInstance().sub(dst,t1,t2);
    }

    // get the temps whome values are used when applying the command
    public LinkedList<TEMP> getUsedTemps()
    {
        LinkedList<TEMP> res = new LinkedList<TEMP>();
        res.add(t1);
        res.add(t2);

        return res;
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