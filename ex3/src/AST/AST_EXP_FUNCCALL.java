package AST;

public class AST_EXP_FUNCCALL extends AST_EXP {
    public AST_VAR var;
	String name;
    public AST_EXP_LIST expList;

	// Class Constructor
	public AST_EXP_FUNCCALL(AST_VAR var, String name, AST_EXP_LIST expList)
	{
		
		// SET A UNIQUE SERIAL NUMBER
		SerialNumber = AST_Node_Serial_Number.getFresh();

		// PRINT CORRESPONDING DERIVATION RULE 
		System.out.format("====================== exp -> [var DOT] ID(%s) LPAREN expList RPAREN\n", name);

		// COPY INPUT DATA NENBERS
		this.var = var;
		this.name = name;
		this.expList = expList;
	}
	
	
	// The default message for an exp var AST node
	public void PrintMe()
	{
		
		// AST NODE TYPE = EXP VAR AST NODE
		System.out.print("AST NODE EXP VARDOTID\n");

		// RECURSIVELY PRINT var
		if (var != null) var.PrintMe();
		if (expList != null) expList.PrintMe();
		
		
		// Print to AST GRAPHIZ DOT file
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("EXP\n[var.]ID(%s) (exps)", name));

		// PRINT Edges to AST GRAPHVIZ DOT file
		if (var != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);
		if (expList != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,expList.SerialNumber);
	}

	public TYPE_VOID SemantMe(){

		TYPE var_type = 

	}
    
}
