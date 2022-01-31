package AST;

import IR.*;
import SYMBOL_TABLE.*;
import TYPES.*;
import TEMP.*;

public class AST_STMT_ASSIGN extends AST_STMT
{
	/***************/
	/*  var := exp */
	/***************/
	public AST_VAR var;
	public AST_EXP exp;
	public int line;

	// metadata for code generation
	public TYPE var_type;
	public TYPE exp_type;

	//  Class Constructor
	public AST_STMT_ASSIGN(AST_VAR var, AST_EXP exp, int line)
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

	public void SemantMe() throws Exception
	{
		this.var_type = this.var.SemantMe().type;
		this.exp_type = this.exp.SemantMe().type;

		if (!(this.var_type.semantically_equals(exp_type)))
		{
			// TYPES OF LHS AND RHS ARE INEQUAL : THROW AN EXCEPTION

			String cls_name = this.getClass().getName();
			throw new Exception("SEMANTIC ERROR : " + this.line + " : " + cls_name);
		}

		// else - the assignment statement is valid.
	}

	public void IRme()
	{
		var.set(exp, exp_type);  // assigns the variable the value stored at exp_tmp
	}
}
