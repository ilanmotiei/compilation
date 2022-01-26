package IR;

import TEMP.TEMP;
import TEMP.TEMP_LIST;
import TYPES.TYPE_FUNCTION;
import TYPES.*;

public class IRcommand_Virtualcall extends IRcommand{
	
	TEMP return_value_reg = null; // optional
	
	TEMP obj;
	TYPE obj_type;
	String func_name;
	TEMP_LIST args;
		
	
	// VirtualCall <obj> <func_name> <args>
	
	public IRcommand_Virtualcall(TEMP obj, TYPE obj_type, String func_name, TEMP_LIST args)
	{
		this.obj = obj;
		this.obj_type = obj_type;
		this.func_name = func_name;
		this.args = args;
	}
	
	
	// <return_value_reg> = VirtualCall <obj> <func_name> <args>
	public IRcommand_Virtualcall(TEMP return_value_reg, TEMP obj, TYPE obj_type, String func_name, TEMP_LIST args)
	{
		this.return_value_reg = return_value_reg;
		
		this.obj = obj;
		this.obj_type = obj_type;
		this.func_name = func_name;
		this.args = args;	
	}
	
	
}
