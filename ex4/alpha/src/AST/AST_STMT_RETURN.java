package AST;

import IR.*;
import SYMBOL_TABLE.*;
import TYPES.*;
import TEMP.*;

public class AST_STMT_RETURN extends AST_STMT {
	public AST_EXP exp;
	public int line;

	// metadata for code generation :
	public String func_name;

	//  Class Constructor
	public AST_STMT_RETURN(AST_EXP exp, int line)
	{
		// SET A UNIQUE SERIAL NUMBER
		SerialNumber = AST_Node_Serial_Number.getFresh();

		// PRINT CORRESPONDING DERIVATION RULE
		System.out.print("====================== stmt -> RETURN [exp] SEMICOLON\n");

		// COPY INPUT DATA NENBERS
		this.exp = exp;
		this.line = line;
	}
	
	// The printing message for an assign statement AST node
	public void PrintMe()
	{
		// AST NODE TYPE = AST ASSIGNMENT STATEMENT
		System.out.print("AST NODE RETURN STMT\n");

		
		// RECURSIVELY PRINT VAR + EXP
		if (exp != null) exp.PrintMe();

		// PRINT Node to AST GRAPHVIZ DOT file
		if(exp == null){
            AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"RETURN\n RETURN;\n");    
        }
        else{
            AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"RETURN\n RETURN exp;\n");
        }
		
		// PRINT Edges to AST GRAPHVIZ DOT file
		if(exp != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,exp.SerialNumber);
	}

	public void SemantMe() throws Exception{

		TYPE_FUNCTION func = SYMBOL_TABLE.getInstance().find_curr_scope_function();

		if (func == null)
		{
			// THE 'RETURN' STMT SHOWS OUTSIDE A FUNCTION : THROW EXCEPTION :
			String cls_name = this.getClass().getName();
			throw new Exception("SEMANTIC ERROR : " + this.line + " : " + cls_name);
		}

		// ELSE

		if (exp != null)
		{
			TYPE exp_type = this.exp.SemantMe().type;

			if (func.returnType.semantically_equals(exp_type) == false)
			{
				// THIS RETURN'S VALUE TYPE IS INVALID : THROW EXCEPTION :
				String cls_name = this.getClass().getName();
				throw new Exception("SEMANTIC ERROR : " + this.line + " : " + cls_name);
			}

			if (exp_type.is_void())
			{
				// RETURN VALUE TYPE SHOULDN'T BE VOID : THROW EXCEPTION :
				String cls_name = this.getClass().getName();
				throw new Exception("SEMANTIC ERROR : " + this.line + " : " + cls_name);
			}
		}
		else
		{
			// exp == null, and this return is empty;

			if ( ! func.returnType.is_void())
			{
				// THIS RETURN SHOULD HAVE RETURNED A VALUE, BUT IS EMPTY : THROW EXCEPTION :
				String cls_name = this.getClass().getName();
				throw new Exception("SEMANTIC ERROR : " + this.line + " : " + cls_name);
			}
		}

		this.func_name = func.name;

		// VALID;
	}

	public void IRme()
	{
		TEMP rv = this.exp.IRme();

		IR.getInstance().Add_IRcommand(new IRcommand_Return(rv, func_name));
	}
}
