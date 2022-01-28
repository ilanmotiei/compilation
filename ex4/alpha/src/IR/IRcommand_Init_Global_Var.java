package IR;

import MIPS.MIPSGenerator;
import TEMP.TEMP;
import java.util.*;

public class IRcommand_Init_Global_Var extends IRcommand
{
    public String var_name;

    public IRcommand_Init_Global_Var(String var_name)
    {
        this.var_name = var_name;
    }

    public void MIPSme()
    {
        MIPSGenerator.getInstance().init_global_var(var_name);
    }

    // get the temps whom values are used when applying the command
    public LinkedList<TEMP> getUsedTemps()
    {
        return null;
    }

    // get the temps whom values are changed after applying the command
    // (which are the temps whom previous values are no more used)
    public LinkedList<TEMP> getChangedTemps()
    {
        return null;
    }
}
