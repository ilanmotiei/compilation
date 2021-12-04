package AST;

import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.*;

public class AST_CFIELD_FUNDEC extends AST_CFIELD {
    public AST_FUNCDEC funcDec;

	//  Class Constructor
	public AST_CFIELD_FUNDEC(AST_FUNCDEC funcDec)
	{
		// SET A UNIQUE SERIAL NUMBER
		SerialNumber = AST_Node_Serial_Number.getFresh();

		// PRINT CORRESPONDING DERIVATION RULE
		System.out.print("====================== cField -> funcDec \n");

		// COPY INPUT DATA NENBERS
		this.funcDec = funcDec;
	}

	
	// The printing message for an assign statement AST node
	public void PrintMe()
	{
		// AST NODE TYPE = AST ASSIGNMENT STATEMENT
		System.out.print("AST NODE CFIELD FUNCDEC\n");  

		
		// RECURSIVELY PRINT VAR + EXP
		if (funcDec != null) funcDec.PrintMe();

		// PRINT Node to AST GRAPHVIZ DOT file
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"CFIELD\n funcDec;\n");
		
		// PRINT Edges to AST GRAPHVIZ DOT file
		if(funcDec != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,funcDec.SerialNumber);
	}

	public void SemantMe() throws Exception{

		if (SYMBOL_TABLE.getInstance().find_curr_scope_class() == null)
		{
			// CAN'T DEFINE A CLASS FIELD OUTSIDE ANY CLASS : THROW ERROR :

			throw new Exception("SEMANTIC ERROR");
		}

		this.funcDec.SemantMe();
	}
}
