package AST;

import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.*;

public class AST_STMT_ASSIGN_NEWEXP extends AST_STMT {
    /********************/
	/*  var := new exp  */
	/********************/
	public AST_VAR var;
	public AST_NEWEXP newExp;
	public int line;

	//  Class Constructor
	public AST_STMT_ASSIGN_NEWEXP(AST_VAR var,AST_NEWEXP newExp, int line)
	{
		// SET A UNIQUE SERIAL NUMBER
		SerialNumber = AST_Node_Serial_Number.getFresh();

		// PRINT CORRESPONDING DERIVATION RULE
		System.out.print("====================== stmt -> var ASSIGN newExp SEMICOLON\n");

		// COPY INPUT DATA NENBERS
		this.var = var;
		this.newExp = newExp;
		this.line = line;
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

		BOX var_box = this.var.SemantMe();
		BOX exp_box = this.newExp.SemantMe();

		if (var_box.type.is_array())
		{
			if ( ! exp_box.is_array)
			{
				// TRYED TO ASSIGN NON-ARRAY OBJECT TYPE TO AN ARRAY-TYPE VARIBALE
				String cls_name = this.getClass().getName();
				throw new Exception("SEMANTIC ERROR : " + this.line + " : " + cls_name);
			}
			else
			{
				if ( ! ((TYPE_ARRAY) var_box.type).elems_type.semantically_equals(exp_box.type))
				{
					// TRYED TO ASSIGN ARRAY FROM A NON-ACCEPTABLE TYPE TO THIS ARRAY (VAR)
					String cls_name = this.getClass().getName();
					throw new Exception("SEMANTIC ERROR : " + this.line + " : " + cls_name);
				}
			}
		}
		else
		{
			if (!(var_box.type.semantically_equals(exp_box.type)))
			{
				// TYPES OF RHS TO ON OBJECT OF THE TYPE OF THE LHS : THROW EXCEPTION
				String cls_name = this.getClass().getName();
				throw new Exception("SEMANTIC ERROR : " + this.line + " : " + cls_name);
			}
		}

		// else - the assignment statement is valid.
	}

	public void IRme()
	{
		TEMP exp_tmp = this.newExp.IRme();

		this.var.set(exp_tmp);  // assigns the variable the value stored at exp_tmp
	}
}
