package IR;

import TEMP.TEMP_LIST;
import TEMP.*;
import MIPS.MIPSGenerator;
import TYPES.TYPE_FUNCTION;

public class IRcommand_Call extends IRcommand{

	TEMP return_value_reg = null;
	
	TYPE_FUNCTION func_data;
	TEMP_LIST args;
		
	// Call <func_name> <args>
	public IRcommand_Call(TYPE_FUNCTION func_data, TEMP_LIST args)
	{
		this.func_data = func_data;
		this.args = args;
	}
	
	// <return_value_reg> = Call <func_name> <args>
	
	public IRcommand_Call(TEMP return_value_reg, TYPE_FUNCTION func_data, TEMP_LIST args)
	{
		
		this.return_value_reg = return_value_reg;
		
		this.func_data = func_data;
		this.args = args;
	}
	
	public void MIPSme()
	{
		MIPS.MIPSGenerator.getInstance().put_args_in_stack(args);
		MIPS.MIPSGenerator.getInstance().jal(func_data.name);
		MIPS.MIPSGenerator.getInstance().restore_stack_pointer(args.length());
		
		if (return_value_reg != null)
		{
			MIPS.MIPSGenerator.getInstance().store_return_value(return_value_reg);
		}	
	}
	
}
