package IR;
import TEMP.*;
import MIPS.*;

public class IRcommand_Array_Access extends IRcommand{

	TEMP dst;
	
	TEMP arr;
	TEMP index;
	
	// <dst> = array_access <arr> <index>
	
	public IRcommand_Array_Access(TEMP dst, TEMP arr, TEMP index) {
		this.dst = dst;
		this.arr = arr;
		this.index = index;
	}
	
	public void MIPSme() {
		MIPS.MIPSGenerator.getInstance().array_access(dst, arr, index);
	}
}
