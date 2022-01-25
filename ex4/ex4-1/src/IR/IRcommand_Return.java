package IR;

import MIPS.MIPSGenerator;
import TEMP.TEMP;

public class IRcommand_Return extends IRcommand{

	private TEMP return_reg;
	
	public IRcommand_Return(TEMP return_reg)
	{
		this.return_reg = return_reg;
	}
	
	public void MIPSme() {
		MIPSGenerator.getInstance().rtn();
	}
}
