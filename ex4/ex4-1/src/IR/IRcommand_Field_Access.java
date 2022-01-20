package IR;

import TEMP.TEMP;
import MIPS.MIPSGenerator;
import TYPES.TYPE_CLASS;

public class IRcommand_Field_Access extends IRcommand{
	
	TEMP dst;
	TEMP obj;
	TYPE_CLASS cls;
	String field_name;
	
	public IRcommand_Field_Access(TEMP dst, TEMP obj, TYPE_CLASS cls, String field_name) {
		this.dst = dst;
		this.obj = obj;
		this.cls = cls;
		this.field_name = field_name;
	}
	
	public void MIPSme()
	{
		MIPSGenerator.getInstance().lw(dst, obj, 4 * cls.field_idx(field_name));
	}
}
