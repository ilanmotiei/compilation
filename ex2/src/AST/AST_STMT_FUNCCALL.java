package AST;

public class AST_STMT_FUNCCALL extends AST_STMT{
    public AST_VAR var;
    public String name;
    public AST_EXP_LIST expList;

	//  Class Constructor
	public AST_STMT_FUNCCALL(AST_VAR var, String name, AST_EXP_LIST expList)
	{
		// SET A UNIQUE SERIAL NUMBER
		SerialNumber = AST_Node_Serial_Number.getFresh();

		// PRINT CORRESPONDING DERIVATION RULE
		System.out.format("====================== stmt -> [var DOT] ID(%s) LPAREN expList RPAREN SEMICOLON\n", name);

		// COPY INPUT DATA NENBERS
		this.var = var;
		this.name = name;
		this.expList = expList;
	}

	
	// The printing message for an assign statement AST node
	public void PrintMe()
	{
		// AST NODE TYPE = AST ASSIGNMENT STATEMENT
		System.out.print("AST NODE ID STMT\n");

		
		// RECURSIVELY PRINT VAR + EXP
		if (var != null) var.PrintMe();
		if (expList != null) expList.PrintMe();

		// PRINT Node to AST GRAPHVIZ DOT file
		if(var == null){
            if(expList == null){
                AST_GRAPHVIZ.getInstance().logNode(
			    SerialNumber,
			    String.format("ID stmt\n ID(%s)();\n", name));
            }
			else{
				AST_GRAPHVIZ.getInstance().logNode(
			    SerialNumber,
			    String.format("ID stmt\n ID(%s)(expList);\n", name));
			}
                
        }
        else{
            if(expList == null){
                AST_GRAPHVIZ.getInstance().logNode(
			    SerialNumber,
			    String.format("ID stmt\n var.ID(%s)();\n", name));
            }
			else{
				AST_GRAPHVIZ.getInstance().logNode(
			    SerialNumber,
			    String.format("ID stmt\n var.ID(%s)(expList);\n", name));
			}
        }
		
		// PRINT Edges to AST GRAPHVIZ DOT file
		if(var != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);
		if(expList != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,expList.SerialNumber);
	}
}
