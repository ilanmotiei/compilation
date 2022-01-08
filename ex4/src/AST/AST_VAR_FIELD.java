package AST;

import SYMBOL_TABLE.*;
import TYPES.*;

public class AST_VAR_FIELD extends AST_VAR
{
	public AST_VAR var;
	public String fieldName;
	public int line;

	// metadata for code generation
	public int offset;
	public boolean isArg;
	public boolean isLocalVar;

	// Class Constructor
	public AST_VAR_FIELD(AST_VAR var,String fieldName, int line)
	{
		// SET A UNIQUE SERIAL NUMBER
		SerialNumber = AST_Node_Serial_Number.getFresh();

		// PRINT CORRESPONDING DERIVATION RULE
		System.out.format("====================== var -> var DOT ID( %s )\n",fieldName);

		// COPY INPUT DATA NENBERS
		this.var = var;
		this.fieldName = fieldName;
		this.line = line;
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

	
	public BOX SemantMe() throws Exception
	{

		TYPE_CLASS curr_scope_class = SYMBOL_TABLE.getInstance().find_curr_scope_class();
		TYPE field_type;

		if (var == null)
		{
			field_type = SYMBOL_TABLE.getInstance().find_by_hierarchy(curr_scope_class, this.fieldName);
		}
		else
		{
			TYPE var_type = this.var.SemantMe().type;

			if ( ! var_type.is_class())
			{
				// VAR's TYPE IS NOT A CLASS AND THUS 'VAR' HAVE NO FIELDS : THROW EXCEPTION :
				String cls_name = this.getClass().getName();
				throw new Exception("SEMANTIC ERROR : " + this.line + " : " + cls_name);
			}

			field_type = SYMBOL_TABLE.getInstance().find_by_hierarchy(((TYPE_CLASS) var_type), this.fieldName);
		}

		if (field_type == null) 
		{ 
			// IF VAR != NULL : FIELD_NAME DOES NOT EXIST IN VAR'S CLASS. IF VAR == NULL : FIELD_NAME DOES NOT EXIST AT CURRENT SCOPES PATH
			// : THROW EXCEPTION :
			String cls_name = this.getClass().getName();
			throw new Exception("SEMANTIC ERROR : " + this.line + " : " + cls_name);
		}

		// ELSE
		SYMBOL_TABLE_ENTRY entry = SYMBOL_TABLE.getInstance().find_entry(this.fieldName);
		this.setCodeGenMetaData(entry);

		return new BOX(field_type, fieldName);
	}

	public void setCodeGenMetaData(SYMBOL_TABLE_ENTRY entry)
	{
		this.offset = entry.offset;
		this.isArg = entry.isArg;
		this.isLocalVar = entry.isLocalVar;
	}


	// Method is used only for compilation reasons
	public BOX SemantMe(TYPE_CLASS cls) throws Exception
	{
		return null; 
	}
}
