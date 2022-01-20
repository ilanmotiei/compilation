package AST;

import TYPES.*;
import TEMP.*;

public class AST_EXP_PARENEXP extends AST_EXP {
	public AST_EXP exp;
	public int line;

	// Class Constructor
	public AST_EXP_PARENEXP(AST_EXP exp,int line)
	{
		
		// SET A UNIQUE SERIAL NUMBER
		SerialNumber = AST_Node_Serial_Number.getFresh();

		// PRINT CORRESPONDING DERIVATION RULE 
		System.out.print("====================== exp -> LPAREN exp RPAREN \n");

		// COPY INPUT DATA NENBERS
		this.exp = exp;
		this.line = line;
	}
	
	
	// The default message for an exp var AST node
	public void PrintMe()
	{
		
		// AST NODE TYPE = EXP VAR AST NODE
		System.out.print("AST NODE PARENEXP\n");

		// RECURSIVELY PRINT var
		if (exp != null) exp.PrintMe();
		
		
		// Print to AST GRAPHIZ DOT file
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"EXP\n(EXP)");

		// PRINT Edges to AST GRAPHVIZ DOT file
		if (exp != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,exp.SerialNumber);
			
	}

	public BOX SemantMe() throws Exception
	{
		return this.exp.SemantMe();
	}
	
	public TEMP IRMe()
	{
		return exp.IRMe();
	}
    
}
