package AST;

import TYPES.*;

public class AST_VAR_SUBSCRIPT extends AST_VAR
{
	public AST_VAR var;
	public AST_EXP subscript;
	public int line;
	
	// Class Constructor
	public AST_VAR_SUBSCRIPT(AST_VAR var,AST_EXP subscript, int line)
	{
		// SET A UNIQUE SERIAL NUMBER
		SerialNumber = AST_Node_Serial_Number.getFresh();

		// PRINT CORRESPONDING DERIVATION RULE
		System.out.print("====================== var -> var [ exp ]\n");

		// COPY INPUT DATA NENBERS
		this.var = var;
		this.subscript = subscript;
		this.line = line;
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

	public BOX SemantMe() throws Exception
	{
		TYPE var_type = this.var.SemantMe().type;
		
		if ( ! var_type.is_array())
		{
			// ONLY ARRAYS ARE SUBSCRIPTABLE : THROW EXCEPTION :
			String cls_name = this.getClass().getName();
			throw new Exception("SEMANTIC ERROR : " + this.line + " : " + cls_name);
		}

		TYPE subscript_exp = this.subscript.SemantMe().type;

		if ( ! subscript_exp.is_int())
		{
			// SUBSCRIPT IS NOT INTEGER : THROW EXCEPTION :
			String cls_name = this.getClass().getName();
			throw new Exception("SEMANTIC ERROR : " + this.line + " : " + cls_name);
		}

		return new BOX(((TYPE_ARRAY) var_type).elems_type);
	}

	public BOX SemantMe(TYPE_CLASS cls) throws Exception
	{
		// We won't apply this method. It is only for compilation reasons.
		return this.SemantMe();
	}
}
