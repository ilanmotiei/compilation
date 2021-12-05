package AST;

import TYPES.*;

public class AST_VAR_SUBSCRIPT extends AST_VAR
{
	public AST_VAR var;
	public AST_EXP subscript;
	
	// Class Constructor
	public AST_VAR_SUBSCRIPT(AST_VAR var,AST_EXP subscript)
	{
		// SET A UNIQUE SERIAL NUMBER
		SerialNumber = AST_Node_Serial_Number.getFresh();

		// PRINT CORRESPONDING DERIVATION RULE
		System.out.print("====================== var -> var [ exp ]\n");

		// COPY INPUT DATA NENBERS
		this.var = var;
		this.subscript = subscript;
	}

	// The printing message for a subscript var AST node
	public void PrintMe()
	{
		// AST NODE TYPE = AST SUBSCRIPT VAR
		System.out.print("AST NODE SUBSCRIPT VAR\n");

		// RECURSIVELY PRINT VAR + SUBSRIPT
		if (var != null) var.PrintMe();
		if (subscript != null) subscript.PrintMe();
		
		// PRINT Node to AST GRAPHVIZ DOT file
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"SUBSCRIPT\nVAR\n...[...]");
		
		// PRINT Edges to AST GRAPHVIZ DOT file
		if (var       != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);
		if (subscript != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,subscript.SerialNumber);
	}

	public TYPE SemantMe() throws Exception
	{
		TYPE var_type = this.var.SemantMe();

		if ( ! var_type.is_array())
		{
			// ONLY ARRAYS ARE SUBSCRIPTABLE : THROW EXCEPTION :
			throw new Exception("SEMANTIC ERROR");
		}

		TYPE subscript_exp = this.subscript.SemantMe();

		if ( ! subscript_exp.is_int())
		{
			// SUBSCRIPT IS NOT INTEGER : THROW EXCEPTION :
			throw new Exception("SEMANTIC ERROR");
		}

		return ((TYPE_ARRAY) var_type).elems_type;
	}

	public TYPE SemantMe(TYPE_CLASS cls) throws Exception
	{
		// We won't apply this method. It is only for compilation reasons.
		return this.SemantMe();
	}
}