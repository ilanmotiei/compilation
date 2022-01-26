package IR;

import MIPS.MIPSGenerator;
import TEMP.*;
import TYPES.*;

public class IRcommand_proluge extends IRcommand {
	
	TYPE_FUNCTION func_data;
	
	public IRcommand_proluge(TYPE_FUNCTION func_data)
	{
		this.func_data = func_data;
	}
	

	@Override
	public void MIPSme() {
		
		MIPSGenerator.getInstance().proluge(func_data);

	}

}
