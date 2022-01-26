package IR;

import TEMP.TEMP;
import TYPES.*;
import MIPS.MIPSGenerator;

public class IRcommand_Fieldset extends IRcommand{
	
	private TEMP reg;
	private String field_name;
	private TEMP val_reg;
	
	private TYPE_CLASS cls;
	
	// <reg>.<field_name> = <val_reg>
	
	public IRcommand_Fieldset(TEMP reg, TYPE_CLASS cls, String field_name, TEMP val_reg) 
	{
		this.reg = reg;
		this.field_name = field_name;
		this.val_reg = val_reg;
		this.cls = cls;
	}
	
	public void MIPSme()
	{
		int offset = cls.field_idx(field_name);
		
		MIPSGenerator.getInstance().sw(val_reg, reg, offset * 4);
	}
}
