package AST;

import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.*;

public class AST_VAR_FIELD extends AST_VAR
{
	public AST_VAR var;
	public String fieldName;
	
	// Class Constructor
	public AST_VAR_FIELD(AST_VAR var,String fieldName)
	{
		// SET A UNIQUE SERIAL NUMBER
		SerialNumber = AST_Node_Serial_Number.getFresh();

		// PRINT CORRESPONDING DERIVATION RULE
		System.out.format("====================== var -> var DOT ID( %s )\n",fieldName);

		// COPY INPUT DATA NENBERS
		this.var = var;
		this.fieldName = fieldName;
	}


	// The printing message for a field var AST node
	public void PrintMe()
	{
		// AST NODE TYPE = AST FIELD VAR
		System.out.print("AST NODE FIELD VAR\n");

		// RECURSIVELY PRINT VAR, then FIELD NAME
		if (var != null) var.PrintMe();
		System.out.format("FIELD NAME( %s )\n",fieldName);

		// PRINT Node to AST GRAPHVIZ DOT file
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("FIELD\nVAR\n...->%s",fieldName));
		
		// PRINT Edges to AST GRAPHVIZ DOT file
		if (var != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);
	}

	
	public TYPE SemantMe() throws Exception
	{

		TYPE_CLASS curr_scope_class = SYMBOL_TABLE.getInstance().find_curr_scope_class();
		TYPE field_type;

		if (var == null)
		{
			field_type = SYMBOL_TABLE.get_instance().find_by_hierarchy(curr_scope_class, this.fieldName);
		}
		else
		{
			TYPE var_type = this.var.SemantMe();

			if (var_type.getClass() != TYPE_CLASS.class)
			{
				// VAR IS A PRIMITIVE TYPE AND HAS NO FIELDS : THROW EXCEPTION :
				throw new Exception("SEMANTIC ERROR");
			}

			field_type = SYMBOL_TABLE.getInstance().find_by_hierarchy((TYPE_CLASS) var_type, this.fieldName);
		}

		if (field_type == null) 
		{ 
			// FIELD_NAME DOES NOT EXIST IN VAR'S CLASS : THROW EXCEPTION :
			throw new Exception("SEMANTIC ERROR");
		}

		// ELSE

		return field_type;
	}
}
