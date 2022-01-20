package IR;
import TEMP.*;

public class IRcommand_LoadString extends IRcommand{

	private TEMP reg;
	private String str;
	
	public IRcommand_LoadString(TEMP reg, String str)
	{
		this.reg = reg;
		this.str = str;
	}
}
