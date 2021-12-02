package AST;

public class AST_FUNCDEC extends AST_Node {
    
    public AST_TYPE type;
	public String name;
	public AST_TYPE_LIST typeList;
    public AST_STMT_LIST stmtList;

	// Class Constructor
	public AST_FUNCDEC(AST_TYPE type, String name, AST_TYPE_LIST typeList, AST_STMT_LIST stmtList)
	{
		// SET A UNIQUE SERIAL NUMBER
		SerialNumber = AST_Node_Serial_Number.getFresh();

		// PRINT CORRESPONDING DERIVATION RULE
		System.out.format("====================== funcDec -> type ID(%s) LPAREN typeList RPAREN LBRACE stmtList RBRACE \n", name);

		// COPY INPUT DATA NENBERS
		this.type = type;
		this.name = name;
		this.typeList = typeList;
        this.stmtList = stmtList;
	}


	// The printing message for a statement list AST node
	public void PrintMe()
	{
		// AST NODE TYPE = AST STATEMENT LIST
		System.out.print("AST NODE FUNCDEC \n");

		// RECURSIVELY PRINT HEAD + TAIL
		if (type != null) type.PrintMe();
		if (typeList != null) typeList.PrintMe();
		if (stmtList != null) stmtList.PrintMe();


		// PRINT to AST GRAPHVIZ DOT file
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("FUNCDEC\ntype ID(%s)(arg1,...argn) { body }\n", name));
		
		// PRINT Edges to AST GRAPHVIZ DOT file
		if (type != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,type.SerialNumber);
		if (typeList != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,typeList.SerialNumber);
		if (stmtList != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,stmtList.SerialNumber);
	}
}
