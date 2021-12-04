package AST;

import TYPES.*;

public class AST_STMT_VARDEC extends AST_STMT {
    public AST_VARDEC varDec;

	//  Class Constructor
	public AST_STMT_VARDEC(AST_VARDEC varDec)
	{
		// SET A UNIQUE SERIAL NUMBER
		SerialNumber = AST_Node_Serial_Number.getFresh();

		// PRINT CORRESPONDING DERIVATION RULE
		System.out.print("====================== stmt -> varDec\n");

		// COPY INPUT DATA NENBERS
		this.varDec = varDec;
	}

	
	// The printing message for an assign statement AST node
	public void PrintMe()
	{
		// AST NODE TYPE = AST ASSIGNMENT STATEMENT
		System.out.print("AST NODE VARDEC STMT\n");

		
		// RECURSIVELY PRINT VAR + EXP
		if (varDec != null) varDec.PrintMe();

		// PRINT Node to AST GRAPHVIZ DOT file
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"VARDEC\n var declaration\n");
		
		// PRINT Edges to AST GRAPHVIZ DOT file
		if (varDec != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,varDec.SerialNumber);
	}

	public void SemantMe() throws Exception{
		this.varDec.SemantMe();
	}
}
