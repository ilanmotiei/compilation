package AST;

import TYPES.TYPE_INT;

//this class was given in the skeleton of ex2, and we add few functionalities
public class AST_STMT_IF extends AST_STMT
{
	public AST_EXP cond;
	public AST_STMT_LIST body;

	//  Class Constructor
	public AST_STMT_IF(AST_EXP cond,AST_STMT_LIST body)
	{
		SerialNumber = AST_Node_Serial_Number.getFresh();

		// PRINT CORRESPONDING DERIVATION RULE
		System.out.print("====================== stmt -> IF LPAREN exp RPAREN LBRACE stmts RBRACE \n");

		this.cond = cond;
		this.body = body;
	}

	public void PrintMe()
	{
		// AST NODE TYPE = AST FIELD VAR
		System.out.print("AST NODE IF STMT \n");

		// RECURSIVELY PRINT VAR, then FIELD NAME
		if (cond != null) cond.PrintMe();
		if (body != null) body.PrintMe();

		// PRINT Node to AST GRAPHVIZ DOT file
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"IF\n if(cond){body} \n");
		
		// PRINT Edges to AST GRAPHVIZ DOT file
		if (cond != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,cond.SerialNumber);
		if (body != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,body.SerialNumber);
	}

	public TYPE SemantMe(){
		TYPE cond_type = this.cond.SemantMe();

		if (cond_type != TYPE_INT.getInstance()){
			throw Exception("Type of condition is not integral.");
		}
	}
}