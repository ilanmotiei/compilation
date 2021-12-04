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

	public void SemantMe() throws Exception
	{
		SemantMe(false);	
	}

	public void SemantMe(boolean const_exp) throws Exception
	{

		if (SYMBOL_TABLE.getInstance().find(this.name) != null)
		{
			// A VARIABLE WITH THE SAME NAME WAS ALLREADY DECLARED : THROW ERROR :
			throw new Exception("SEMANTIC ERROR");
		}

		// ELSE : 

		TYPE var_type = this.type.SemantMe();

		if (var_type.semantically_equals(this.exp.SemantMe()) == false)
		{
			// the exp cannot be assigned to the variable : THROW EXCEPTION :
			throw new Exception("SEMANTIC ERROR");
		}

		if (const_exp == true){
			// CHECK ALSO THAT THE EXPRESSION'S VALUE IS CONSTANT :
			throw new Exception("SEMANTIC ERROR");
		}

		SYMBOL_TABLE.getInstance().enter(this.name, var_type);
	}
}
