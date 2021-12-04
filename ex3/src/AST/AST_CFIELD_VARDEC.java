package AST;

import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.*;

public class AST_CFIELD_VARDEC extends AST_CFIELD {
    public AST_VARDEC varDec;

	//  Class Constructor
	public AST_CFIELD_VARDEC(AST_VARDEC varDec)
	{
		// SET A UNIQUE SERIAL NUMBER
		SerialNumber = AST_Node_Serial_Number.getFresh();

		// PRINT CORRESPONDING DERIVATION RULE
		System.out.print("====================== cField -> varDec \n");

		// COPY INPUT DATA NENBERS
		this.varDec = varDec;
	}

	
	// The printing message for an assign statement AST node
	public void PrintMe()
	{
		// AST NODE TYPE = AST ASSIGNMENT STATEMENT
		System.out.print("AST NODE CFIELD VARDEC\n");

		
		// RECURSIVELY PRINT VAR + EXP
		if (varDec != null) varDec.PrintMe();

		// PRINT Node to AST GRAPHVIZ DOT file
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"CFIELD\n varDec;\n");
		
		// PRINT Edges to AST GRAPHVIZ DOT file
		if(varDec != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,varDec.SerialNumber);
	}

	public void SemantMe() throws Exception{
		this.varDec.SemantMe(true);
	}
}
