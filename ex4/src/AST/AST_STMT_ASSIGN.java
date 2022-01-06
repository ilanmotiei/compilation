package AST;

import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.*;

public class AST_STMT_ASSIGN extends AST_STMT
{
	/***************/
	/*  var := exp */
	/***************/
	public AST_VAR var;
	public AST_EXP exp;
	public int line;


	//  Class Constructor
	public AST_STMT_ASSIGN(AST_VAR var,AST_EXP exp, int line)
	{
		// SET A UNIQUE SERIAL NUMBER
		SerialNumber = AST_Node_Serial_Number.getFresh();

		// PRINT CORRESPONDING DERIVATION RULE
		System.out.print("====================== stmt -> var ASSIGN exp SEMICOLON\n");

		// COPY INPUT DATA NENBERS
		this.var = var;
		this.exp = exp;
		this.line = line;
	}

	
	// The printing message for an assign statement AST node
	public void PrintMe()
	{
		// AST NODE TYPE = AST ASSIGNMENT STATEMENT
		System.out.print("AST NODE ASSIGN STMT\n");

		
		// RECURSIVELY PRINT VAR + EXP
		if (var != null) var.PrintMe();
		if (exp != null) exp.PrintMe();

		// PRINT Node to AST GRAPHVIZ DOT file
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"ASSIGN\nleft := right\n");
		
		// PRINT Edges to AST GRAPHVIZ DOT file
		if (var != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);
		if (exp != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,exp.SerialNumber);
	}

//	public TEMP IRme()
//	{
//		TEMP src = exp.IRme();
//		IR.
//		getInstance().
//		Add_IRcommand(new IRcommand_Store(((AST_EXP_VAR_SIMPLE) var).name,src));
//
//		return null;
//	}

	public void SemantMe() throws Exception
	{
		if (!(this.var.SemantMe().type.semantically_equals(this.exp.SemantMe().type)))
		{
			// TYPES OF LHS AND RHS ARE INEQUAL : THROW AN EXCEPTION

			String cls_name = this.getClass().getName();
			throw new Exception("SEMANTIC ERROR : " + this.line + " : " + cls_name);
		}

		// else - the assignment statement is valid.
	}
}
