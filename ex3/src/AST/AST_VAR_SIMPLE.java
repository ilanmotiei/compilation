package AST;

import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.*;

public class AST_VAR_SIMPLE extends AST_VAR
{
	// simple variable name
	public String name;
	

	// Class Constructor

	public AST_VAR_SIMPLE(String name)
	{
		// SET A UNIQUE SERIAL NUMBER
		SerialNumber = AST_Node_Serial_Number.getFresh();
	
		// PRINT CORRESPONDING DERIVATION RULE
		System.out.format("====================== var -> ID( %s )\n",name);

		// COPY INPUT DATA NENBERS
		this.name = name;
	}


	// The printing message for a simple var AST node
	public void PrintMe()
	{
		// AST NODE TYPE = AST SIMPLE VAR
		System.out.format("AST NODE SIMPLE VAR( %s )\n",name);

		// Print to AST GRAPHIZ DOT file
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("SIMPLE\nVAR\n(%s)",name));
	}

	public TYPE SemantMe() throws Exception
	{
		TYPE_CLASS curr_scope_class = SYMBOL_TABLE.getInstance().find_curr_scope_class();

		TYPE var_type = SYMBOL_TABLE.getInstance().find_by_hierarchy(curr_scope_class, this.name);

		if (var_type == null)
		{
			// VAR WASN'T FOUND : THROW EXCEPTION :
			throw new Exception("SEMANTIC ERROR");
		}

		// ELSE

		return var_type;
	}
}
