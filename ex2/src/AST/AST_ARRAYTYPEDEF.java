package AST;

public class AST_ARRAYTYPEDEF extends AST_Node {
    public String name;
	public AST_TYPE type;
	
	// Class Constructor
	public AST_ARRAYTYPEDEF(String name, AST_TYPE type)
	{
		// SET A UNIQUE SERIAL NUMBER
		SerialNumber = AST_Node_Serial_Number.getFresh();

		// PRINT CORRESPONDING DERIVATION RULE
		System.out.format("====================== arrayTypedef -> ARRAY ID(%s) EQ type LBRACK RBRACK \n", name);

		// COPY INPUT DATA NENBERS
		this.type = type;
		this.name = name;
	}


	// The printing message for a statement list AST node
	public void PrintMe()
	{
		// AST NODE TYPE = AST STATEMENT LIST
		System.out.print("AST NODE ARRAYTYPEDEF \n");

		// RECURSIVELY PRINT HEAD + TAIL
		if (type != null) type.PrintMe();


		// PRINT to AST GRAPHVIZ DOT file
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("ARRAYTYPEDEF\nARRAY ID(%s) = type[] \n", name));
		
		// PRINT Edges to AST GRAPHVIZ DOT file
		if (type != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,type.SerialNumber);
	}
    
}
