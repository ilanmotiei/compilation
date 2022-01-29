package IR;

import MIPS.MIPSGenerator;
import TEMP.TEMP;

import java.util.LinkedList;

public class IRcommand_Bind extends IRcommand{

    TEMP t;
    int minimum;
    int maximum;

    public IRcommand_Bind(TEMP t, int minimum, int maximum)
    {
        this.t = t;
        this.minimum = minimum;
        this.maximum = maximum;
    }

    public void MIPSme()
    {
        MIPSGenerator.getInstance().bind(t,minimum,maximum);
    }

    // get the temps whome values are used when applying the command
    public LinkedList<TEMP> getUsedTemps()
    {
        LinkedList<TEMP> res = new LinkedList<TEMP>();
        res.add(t);

        return res;
    }

    // get the temps whome values are changed after applying the command
    // (which are the temps whome previous values are no more used)
    public LinkedList<TEMP> getChangedTemps()
    {
        LinkedList<TEMP> res = new LinkedList<TEMP>();
        res.add(t);

        return res;
    }
}
