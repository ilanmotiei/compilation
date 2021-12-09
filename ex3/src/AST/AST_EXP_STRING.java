package AST;

import TYPES.*;

public class AST_EXP_STRING extends AST_EXP {
	public String s;
	public int line;

	/* Class Constructor */
	public AST_EXP_STRING(String s, int line)
	{
		// SET A UNIQUE SERIAL NUMBER 
		SerialNumber = AST_Node_Serial_Number.getFresh();

		// PRINT CORRESPONDING DERIVATION RULE
		System.out.format("====================== exp -> STRING(%s)\n", s);

		// COPY INPUT DATA NENBERS
		this.s = s;
		this.line = line;
	}

	
	// The printing message for an INT EXP AST node
	public void PrintMe()
	{
		// AST NODE TYPE = AST INT EXP
		System.out.format("AST NODE STRING(%s)\n", s);

		// Print to AST GRAPHIZ DOT file
		
        AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("STRING(%s)", " ' '" + s.substring(1, s.length()-1)) + " ' ");
			// can't use double quotes in a label of Graphviz 
	}

	public BOX SemantMe() throws Exception{
		return new BOX(TYPE_STRING.getInstance(), true);
	}
}
