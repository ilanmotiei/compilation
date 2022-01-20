package IR;

import TYPES.*;
import MIPS.MIPSGenerator;;

public class IRcommand_Funcdef extends IRcommand{
	
	TYPE_CLASS cls = null; // optional - defined only if it's a class function
	TYPE_FUNCTION func_data;
	
	public IRcommand_Funcdef(TYPE_FUNCTION func_data)
	{
		this.func_data = func_data;
	}
	
	public IRcommand_Funcdef(TYPE_CLASS cls, TYPE_FUNCTION func_data)
	{
		this.cls = cls;
		this.func_data = func_data;
	}
	
	@Override
	public void MIPSme() {
		
		if (this.cls != null)
		{
			MIPSGenerator.getInstance().label(cls.name + func_data.name);
		}
		else {
			MIPSGenerator.getInstance().label(func_data.name);
		}
	}
	
}
