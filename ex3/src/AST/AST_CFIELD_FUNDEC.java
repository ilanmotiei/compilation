package AST;

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

	public TYPE SemantMe(){
		return this.funcDec.SemantMe();
	}
}
