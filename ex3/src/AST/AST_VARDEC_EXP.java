package AST;

import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.*;

public class AST_VARDEC_EXP extends AST_VARDEC {
    // DATA MEMBERS
	public AST_TYPE type;
	public String name;
	public AST_EXP exp;

	// Class Constructor
	public AST_VARDEC_EXP(AST_TYPE type, String name, AST_EXP exp)
	{
		// SET A UNIQUE SERIAL NUMBER
		SerialNumber = AST_Node_Serial_Number.getFresh();

		// PRINT CORRESPONDING DERIVATION RULE
		System.out.format("====================== varDec -> type ID(%s) [ASSIGN exp] SEMICOLON\n", name);

		// COPY INPUT DATA NENBERS
		this.type = type;
		this.name = name;
		this.exp = exp;
	}


	// The printing message for a statement list AST node
	public void PrintMe()
	{
		// AST NODE TYPE = AST STATEMENT LIST
		System.out.print("AST NODE EXP VARDEC\n");

		// RECURSIVELY PRINT HEAD + TAIL
		if (type != null) type.PrintMe();
		if (exp != null) exp.PrintMe();

		// PRINT to AST GRAPHVIZ DOT file
        if(exp != null) {
            AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("EXP ASSIGN\ntype ID(%s) := right\n", name));
        }
        else{
		    AST_GRAPHVIZ.getInstance().logNode(
		    	SerialNumber,
			    String.format("EXP ASSIGN\ntype ID(%s)\n", name));;
        }
		// PRINT Edges to AST GRAPHVIZ DOT file
		if (type != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,type.SerialNumber);
		if (exp != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,exp.SerialNumber);
	}

	public TYPE SemantMe() throws Exception
	{
		return SemantMe(null);	
	}

	public TYPE SemantMe(TYPE_CLASS cls) throws Exception
	{

		if (SYMBOL_TABLE.getInstance().find(this.name) != null)
		{
			// A VARIABLE WITH THE SAME NAME WAS ALLREADY DECLARED : THROW ERROR :
			throw new Exception("SEMANTIC ERROR");
		}

		// ELSE : 

		TYPE var_type = this.type.SemantMe();
		TYPE exp_type = this.exp.SemantMe();

		if (var_type.semantically_equals(exp_type) == false)
		{
			// the newExp cannot be assigned to the variable : THROW EXCEPTION :
			throw new Exception("SEMANTIC ERROR");
		}

		if (cls != null){
			// CHECK THAT THE EXPRESSION'S VALUE IS CONSTANT :
			
			/*
			if ( ! exp_type.is_const)
			{
				throw new Exception("SEMANTIC ERROR");
			}
			*/

			// CHECK THAT THE VARIABLE DOES NOT SHADOW AN ANOTHER CLASS FIELD :

			if (cls.get_field(this.name) != null)
			{
				throw new Exception("SEMANTIC ERROR");
			}
		}

		SYMBOL_TABLE.getInstance().enter(this.name, var_type);

		if (cls != null)
		{
			// MAKE A CLASS FIELD OBJECT OUT OF THIS DECLERATION (WRAP IT WITH A NAME)
			var_type = new TYPE_CLASS_FIELD(var_type, this.name);
		}

		return var_type;
	}
}