package AST;

public class AST_TYPE_LIST extends AST_Node {
    public AST_TYPE type;
    String name;
    public AST_TYPE_LIST typeList;

	//  Class Constructor
	public AST_TYPE_LIST(AST_TYPE type, String name, AST_TYPE_LIST typeList)
	{
		// SET A UNIQUE SERIAL NUMBER
		SerialNumber = AST_Node_Serial_Number.getFresh();

		// PRINT CORRESPONDING DERIVATION RULE
        if(typeList == null){
            System.out.format("====================== types -> type ID(%s)\n", name);
        }
        else{
            System.out.format("====================== types -> type ID(%s) COMMA types\n", name);
        }

		// COPY INPUT DATA NENBERS
		this.type = type;
        this.name = name;
        this.typeList = typeList;

	}

	
	// The printing message for an assign statement AST node
	public void PrintMe()
	{
		// AST NODE TYPE = AST ASSIGNMENT STATEMENT
		System.out.print("AST NODE TYPE LIST\n");

		
		// RECURSIVELY PRINT VAR + EXP
		if (type != null) type.PrintMe();
		if (typeList != null) typeList.PrintMe();

		// PRINT Node to AST GRAPHVIZ DOT file
        if(typeList == null){
            AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("TYPE LIST\n type ID(%s) \n", name));
        }
        else{
            AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("TYPE LIST\n type ID(%s), tpyeList \n", name));
        }
		
		// PRINT Edges to AST GRAPHVIZ DOT file
		if(type != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,type.SerialNumber);
		if(typeList != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,typeList.SerialNumber);
	}
    
}
