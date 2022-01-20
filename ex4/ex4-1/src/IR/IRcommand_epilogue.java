package IR;

import MIPS.MIPSGenerator;
import TEMP.*;
import TYPES.*;

public class IRcommand_epilogue extends IRcommand {
	
	TYPE_FUNCTION func_data;
	
	public IRcommand_epilogue(TYPE_FUNCTION func_data)
	{
		this.func_data = func_data;
	}
	

	@Override
	public void MIPSme() {
		
		MIPSGenerator.getInstance().epilogue(func_data);

	}

}