package AST;

import IR.*;
import SYMBOL_TABLE.*;
import TYPES.*;
import TEMP.*;

public class AST_VAR_SIMPLE extends AST_VAR
{
	// simple variable name
	public String name;
	public int line;

	// metadata for code generation
	TYPE type = null;
	TYPE_CLASS curr_scope_class = null;

	// Class Constructor
	public AST_VAR_SIMPLE(String name, int line)
	{
		// SET A UNIQUE SERIAL NUMBER
		SerialNumber = AST_Node_Serial_Number.getFresh();
	
		// PRINT CORRESPONDING DERIVATION RULE
		System.out.format("====================== var -> ID( %s )\n",name);

		// COPY INPUT DATA NENBERS
		this.name = name;
		this.line = line;
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

	public BOX SemantMe() throws Exception
	{
		TYPE var_type = SYMBOL_TABLE.getInstance().find(this.name);

		if (var_type == null)
		{
			// VAR WASN'T FOUND : THROW EXCEPTION :
			String cls_name = this.getClass().getName();
			throw new Exception("SEMANTIC ERROR : " + this.line + " : " + cls_name);
		}

		// ELSE : 

		// Setting meta data for code generation :

		SYMBOL_TABLE_ENTRY entry = SYMBOL_TABLE.getInstance().find_entry(this.name);
		this.setCodeGenMetaData(entry);

		this.type = var_type;

		// in case it is a class field:
		this.curr_scope_class = SYMBOL_TABLE.getInstance().find_curr_scope_class();

		// Returning information to the caller :

		return new BOX(var_type);
	}

	public BOX SemantMe(TYPE_CLASS cls) throws Exception
	{
		// We won't apply this method. It is only for compilation reasons.

		return this.SemantMe();
	}

	public void setCodeGenMetaData(SYMBOL_TABLE_ENTRY entry)
	{
		this.offset = entry.offset;
		this.isArg = entry.isArg;
		this.isLocalVar = entry.isLocalVar;
		this.isClassField = entry.isClassField;
	}

	public TEMP IRme()
	{
		TEMP var_val_tmp = TEMP_FACTORY.getInstance().getFreshTEMP();

		IR.getInstance().Add_IRcommand(new IRcommand_Load(var_val_tmp, 
														this.name,
														this.curr_scope_class,
														this.isLocalVar,
														this.isArg,
														this.isClassField,
														this.offset));
		
		return var_val_tmp;
	}

	public void set(TEMP value, TYPE value_type)
	{
		IR.getInstance().Add_IRcommand(new IRcommand_Store(this.name,
														   this.curr_scope_class,
														   value,
														   this.isLocalVar,
														   this.isArg,
														   this.isClassField,
														   this.offset));
	}
}