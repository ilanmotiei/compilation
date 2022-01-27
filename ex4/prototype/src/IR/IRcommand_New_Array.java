package IR;

import TEMP.*;
import MIPS.MIPSGenerator;
import TYPES.*;

public class IRcommand_New_Array extends IRcommand
{
    TEMP dst;
	TEMP size_reg;
    TYPE type;
    
    // <dst> = new_array <size_reg>

	public IRcommand_New_Array(TEMP dst, TEMP size_reg, TYPE type)
	{
        this.dst = dst;
        this.size_reg = size_reg;
        this.type = type;
	}
	
	public void MIPSme()
	{
		MIPSGenerator.getInstance().allocate(dst, size_reg);
	}

}