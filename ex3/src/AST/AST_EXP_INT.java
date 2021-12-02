package AST;

public class AST_EXP_INT extends AST_EXP
{
	public int value;
	public int hasMinus; // 0 if doesn't hava "-" before int, 1 if does.

	/* Class Constructor */
	public AST_EXP_INT(int value, int hasMinus)
	{
		// SET A UNIQUE SERIAL NUMBER 
		SerialNumber = AST_Node_Serial_Number.getFresh();

		// PRINT CORRESPONDING DERIVATION RULE
		System.out.format("====================== exp -> [-] INT( %d )\n", value);

		// COPY INPUT DATA NENBERS
		this.value = value;
		this.hasMinus = hasMinus;
	}

	
	// The printing message for an INT EXP AST node
	public void PrintMe()
	{
		// AST NODE TYPE = AST INT EXP
		System.out.format("AST NODE [-] INT( %d )\n",value);

		// Print to AST GRAPHIZ DOT file
		if(hasMinus == 0){
			AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("INT(%d)",value));	
		}
		else{
			AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("-INT(%d)",value));
		}
	}
}
