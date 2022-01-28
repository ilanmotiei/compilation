package AST;

import IR.*;
import SYMBOL_TABLE.*;
import TYPES.*;
import TEMP.*;

public class AST_STMT_WHILE extends AST_STMT
{
	public AST_EXP cond;
	public AST_STMT_LIST body;
	public int line;

	// Class Constructor
	public AST_STMT_WHILE(AST_EXP cond,AST_STMT_LIST body, int line)
	{
		SerialNumber = AST_Node_Serial_Number.getFresh();

		// PRINT CORRESPONDING DERIVATION RULE
		System.out.print("====================== stmt -> WHILE LPAREN exp RPAREN LBRACE stmts RBRACE \n");

		this.cond = cond;
		this.body = body;
		this.line = line;
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

	public void SemantMe() throws Exception{
		
		if ( ! cond.SemantMe().type.is_int()){
			// CONDITION TYPE ISN'T INT : THROW ERROR :
			String cls_name = this.getClass().getName();
			throw new Exception("SEMANTIC ERROR : " + this.line + " : " + cls_name);
		}
		
		// ELSE

		SYMBOL_TABLE.getInstance().beginScope();
		this.body.SemantMe();
		SYMBOL_TABLE.getInstance().endScope();
	}

	public void IRme()
	{
		/*******************************/
		/* [1] Allocate 2 fresh labels */
		/*******************************/
		
		String label_end   = IRcommand.getFreshLabel("end");
		String label_start = IRcommand.getFreshLabel("start");

		/*********************************/
		/* [2] entry label for the while */
		/*********************************/
		
		IR.
		getInstance().
		Add_IRcommand(new IRcommand_Label(label_start));

		/********************/
		/* [3] cond.IRme(); */
		/********************/
		TEMP cond_temp = cond.IRMe();

		/******************************************/
		/* [4] Jump conditionally to the loop end */
		/******************************************/
		
		IR.
		getInstance().
		Add_IRcommand(new IRcommand_Jump_If_Eq_To_Zero(cond_temp,label_end));

		/*******************/
		/* [5] body.IRme() */
		/*******************/
		body.IRMe();

		/******************************/
		/* [6] Jump to the loop entry */
		/******************************/
		IR.
		getInstance().
		Add_IRcommand(new IRcommand_Jump_Label(label_start));

		/**********************/
		/* [7] Loop end label */
		/**********************/
		IR.
		getInstance().
		Add_IRcommand(new IRcommand_Label(label_end));		
	}
}

