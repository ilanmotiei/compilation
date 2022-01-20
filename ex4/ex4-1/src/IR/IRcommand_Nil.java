package IR;
import TEMP.*;
import MIPS.*;

public class IRcommand_Nil extends IRcommand{

	TEMP dst;

    // <dst> = Nil
	
	public IRcommand_Nil(TEMP dst)
	{
		this.dst = dst;
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		// allocating 4 bytes for the null pointer
		
		TEMP size_1 = TEMP_FACTORY.getInstance().getFreshTEMP();
		
		MIPSGenerator.getInstance().li(size_1, 1);
		
		MIPSGenerator.getInstance().malloc(dst, size_1, 4);
		
		TEMP zero = TEMP_FACTORY.getInstance().getFreshTEMP();
		
		// filling it with 0's
		
		MIPSGenerator.getInstance().li(zero, 0);
		
		MIPSGenerator.getInstance().sw(zero, dst, 0);
	}
}
