package AST;

import TYPES.TYPE_INT;
import TYPES.TYPE_NIL;

public class AST_EXP_NIL extends AST_EXP {
    
	/* Class Constructor */
	public AST_EXP_NIL()
	{
		// SET A UNIQUE SERIAL NUMBER 
		SerialNumber = AST_Node_Serial_Number.getFresh();

		// PRINT CORRESPONDING DERIVATION RULE
		System.out.format("====================== exp -> NIL\n");
	}

	
	// The printing message for an INT EXP AST node
	public void PrintMe()
	{
		// AST NODE TYPE = AST INT EXP
		System.out.format("AST NODE NIL\n");

		// Print to AST GRAPHIZ DOT file
        AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("NIL"));
	}

	public TYPE_NIL SemantMe()
	{
		return TYPE_NIL.getInstance();
	}
    
}
