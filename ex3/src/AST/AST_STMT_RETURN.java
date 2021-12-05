package AST;

import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.*;

public class AST_STMT_RETURN extends AST_STMT {
    public AST_EXP exp;

	//  Class Constructor
	public AST_STMT_RETURN(AST_EXP exp)
	{
		// SET A UNIQUE SERIAL NUMBER
		SerialNumber = AST_Node_Serial_Number.getFresh();

		// PRINT CORRESPONDING DERIVATION RULE
		System.out.print("====================== stmt -> RETURN [exp] SEMICOLON\n");

		// COPY INPUT DATA NENBERS
		this.exp = exp;
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
			throw new Exception("SEMANTIC ERROR");
		}

		// ELSE

		if (exp != null)
		{
			if (func.returnType.semantically_equals(this.exp.SemantMe()) == false)
			{
				// THIS RETURN'S VALUE TYPE IS INVALID : THROW EXCEPTION :
				throw new Exception("SEMANTIC ERROR");
			}
		}
		else
		{
			// exp == null, and this return is empty;

			if ( ! func.returnType.is_void())
			{
				// THIS RETURN SHOULD HAVE RETURNED A VALUE, BUT IS EMPTY : THROW EXCEPTION :
				throw new Exception("SEMANTIC ERROR");
			}
		}

		// VALID;
	}
}
