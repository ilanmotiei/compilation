package IR;
import TEMP.*;
import TYPES.TYPE;
import MIPS.MIPSGenerator;

public class IRcommand_New_Class extends IRcommand{
	
	 TEMP dst;
	 TYPE type;
	    
	 // FORMAT <dst> = new_class <type>

	public IRcommand_New_Class(TEMP dst, TYPE type)
	{
	    this.dst = dst;
	    this.type = type;
	}

	// get size_in_bytes of class for AST annotations
	public void MIPSme()
	{
		MIPSGenerator.getInstance().malloc(dst, 1, type.size_in_bytes);
	}
}
