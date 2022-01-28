package AST;

import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.*;
import TEMP.*;

public class AST_EXP_VAR extends AST_EXP
{
	public AST_VAR var;
	public int line;

	// Class Constructor
	public AST_EXP_VAR(AST_VAR var, int line)
	{
		
		// SET A UNIQUE SERIAL NUMBER
		SerialNumber = AST_Node_Serial_Number.getFresh();

		// PRINT CORRESPONDING DERIVATION RULE 
		System.out.print("====================== exp -> var\n");

		// COPY INPUT DATA NENBERS
		this.var = var;
		this.line = line;
	}
	
	
	// The default message for an exp var AST node
	public void PrintMe()
	{
		
		// AST NODE TYPE = EXP VAR AST NODE
		System.out.print("AST NODE EXP VAR\n");

		// RECURSIVELY PRINT var
		if (var != null) var.PrintMe();
		
		
		// Print to AST GRAPHIZ DOT file
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"EXP\nVAR");

		// PRINT Edges to AST GRAPHVIZ DOT file
		if (var != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);
	}

	public BOX SemantMe() throws Exception{
		return this.var.SemantMe();
	}

	public TEMP IRme()
	{
		return this.var.IRme();
	}
}
