package AST;

import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.TYPE_INT;

public class AST_STMT_WHILE extends AST_STMT
{
	public AST_EXP cond;
	public AST_STMT_LIST body;

	// Class Constructor
	public AST_STMT_WHILE(AST_EXP cond,AST_STMT_LIST body)
	{
		SerialNumber = AST_Node_Serial_Number.getFresh();

		// PRINT CORRESPONDING DERIVATION RULE
		System.out.print("====================== stmt -> WHILE LPAREN exp RPAREN LBRACE stmts RBRACE \n");

		this.cond = cond;
		this.body = body;
	}

	public void PrintMe()
	{
		// AST NODE TYPE = AST FIELD VAR
		System.out.print("AST NODE WHILE STMT \n");

		// RECURSIVELY PRINT VAR, then FIELD NAME
		if (cond != null) cond.PrintMe();
		if (body != null) body.PrintMe();

		// PRINT Node to AST GRAPHVIZ DOT file
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"WHILE\n while(cond){body} \n");
		
		// PRINT Edges to AST GRAPHVIZ DOT file
		if (cond != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,cond.SerialNumber);
		if (body != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,body.SerialNumber);
	}

	public TYPE SemantMe(){
		
		if (cond.SemantMe() != TYPE_INT.getInstance()){
			// CONDITION TYPE ISN'T INT : THROW ERROR : TODO
		}
		
		// ELSE

		SYMBOL_TABLE.getInstance().beginScope();
		this.body.SemantMe();
		SYMBOL_TABLE.getInstance().endScope();
		
	}
}

