package AST;

import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.*;

public class AST_STMT_ASSIGN_NEWEXP extends AST_STMT {
    /********************/
	/*  var := new exp  */
	/********************/
	public AST_VAR var;
	public AST_NEWEXP newExp;


	//  Class Constructor
	public AST_STMT_ASSIGN_NEWEXP(AST_VAR var,AST_NEWEXP newExp)
	{
		// SET A UNIQUE SERIAL NUMBER
		SerialNumber = AST_Node_Serial_Number.getFresh();

		// PRINT CORRESPONDING DERIVATION RULE
		System.out.print("====================== stmt -> var ASSIGN newExp SEMICOLON\n");

		// COPY INPUT DATA NENBERS
		this.var = var;
		this.newExp = newExp;
	}

	
	// The printing message for an assign statement AST node
	public void PrintMe()
	{
		// AST NODE TYPE = AST ASSIGNMENT STATEMENT
		System.out.print("AST NODE ASSIGN NEWEXP STMT\n");

		
		// RECURSIVELY PRINT VAR + EXP
		if (var != null) var.PrintMe();
		if (newExp != null) newExp.PrintMe();

		// PRINT Node to AST GRAPHVIZ DOT file
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"ASSIGN NEWEXP\nleft := new right\n");
		
		// PRINT Edges to AST GRAPHVIZ DOT file
		if (var != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);
		if (newExp != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,newExp.SerialNumber);
	}

	public void SemantMe() throws Exception{

		if (!(this.var.SemantMe().semantically_equals(this.newExp.SemantMe())))
		{
			// TYPES OF RHS TO ON OBJECT OF THE TYPE OF THE LHS : THROW EXCEPTION
			throw new Exception("SEMANTIC ERROR");
		}

		// else - the assignment statement is valid.
	}
    
}
