package AST;

import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.*;

public class AST_VARDEC_EXP extends AST_VARDEC {
    // DATA MEMBERS
	public AST_TYPE type;
	public String name;
	public AST_EXP exp;
	public int line;

	// Class Constructor
	public AST_VARDEC_EXP(AST_TYPE type, String name, AST_EXP exp, int line)
	{
		// SET A UNIQUE SERIAL NUMBER
		SerialNumber = AST_Node_Serial_Number.getFresh();

		// PRINT CORRESPONDING DERIVATION RULE
		System.out.format("====================== varDec -> type ID(%s) [ASSIGN exp] SEMICOLON\n", name);

		// COPY INPUT DATA NENBERS
		this.type = type;
		this.name = name;
		this.exp = exp;

		this.line = line;
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

	public BOX SemantMe() throws Exception
	{
		return SemantMe(null);	
	}

	public BOX SemantMe(TYPE_CLASS cls) throws Exception
	{	
		if (cls != null)
		{
			if (SYMBOL_TABLE.getInstance().find_at_class(cls, this.name) != null)
			{
				// A VARIABLE WITH THE SAME NAME WAS ALLREADY DECLARED AT THE GIVEN CLASS : THROW ERROR :
				String cls_name = this.getClass().getName();
				throw new Exception("SEMANTIC ERROR : " + this.line + " : " + cls_name);
			}
		}

		// ELSE : 

		TYPE var_type = this.type.SemantMe().type;
		TYPE exp_type = null;

		if (this.exp != null)
		{
			BOX exp_box = this.exp.SemantMe();
			exp_type = exp_box.type;

			if (var_type.semantically_equals(exp_type) == false)
			{
				// the newExp cannot be assigned to the variable : THROW EXCEPTION :
				String cls_name = this.getClass().getName();
				throw new Exception("SEMANTIC ERROR : " + this.line + " : " + cls_name);
			}

			if (cls != null)
			{
				// CHECK THAT THE EXPRESSION'S VALUE IS CONSTANT :

				if ( ! exp_box.is_const)
				{
					String cls_name = this.getClass().getName();
					throw new Exception("SEMANTIC ERROR : " + this.line + " : " + cls_name);
				}
			}
		}

		if (cls != null)
		{
			// CHECK THAT THE VARIABLE DOES NOT SHADOW AN ANOTHER CLASS FIELD :

			if (cls.get_field(this.name) != null)
			{
				String cls_name = this.getClass().getName();
				throw new Exception("SEMANTIC ERROR : " + this.line + " : " + cls_name);
			}
		}

		SYMBOL_TABLE.getInstance().enter(this.name, var_type);

		if (cls != null)
		{
			// MAKE A CLASS FIELD OBJECT OUT OF THIS DECLERATION (WRAP IT WITH A NAME)
			var_type = new TYPE_CLASS_FIELD(var_type, this.name);
		}

		return new BOX(var_type);
	}
}
