package AST;

import IR.*;
import SYMBOL_TABLE.*;
import TYPES.*;
import TEMP.*;

public class AST_VAR_FIELD extends AST_VAR
{
	public AST_VAR var;
	public String fieldName;
	public int line;

	// metadata for code generation
	TYPE_CLASS cls; // the type of the object we're setting/accessing its field
	TYPE field_type;

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
		TYPE var_type = this.var.SemantMe().type;

		if ( ! var_type.is_class())
		{
			// VAR's TYPE IS NOT A CLASS AND THUS 'VAR' HAVE NO FIELDS : THROW EXCEPTION :
			String cls_name = this.getClass().getName();
			throw new Exception("SEMANTIC ERROR : " + this.line + " : " + cls_name);
		}

		this.cls = (TYPE_CLASS) var_type;

		TYPE field_type = SYMBOL_TABLE.getInstance().find_by_hierarchy(((TYPE_CLASS) var_type), this.fieldName);

		if (field_type == null) 
		{ 
			// IF VAR != NULL : FIELD_NAME DOES NOT EXIST IN VAR'S CLASS. IF VAR == NULL : FIELD_NAME DOES NOT EXIST AT CURRENT SCOPES PATH
			// : THROW EXCEPTION :
			String cls_name = this.getClass().getName();
			throw new Exception("SEMANTIC ERROR : " + this.line + " : " + cls_name);
		}

		this.cls = (TYPE_CLASS) var_type;
		this.field_type = field_type;

		// ELSE :

		return new BOX(field_type, fieldName);
	}

	public BOX SemantMe(TYPE_CLASS cls) throws Exception
	{
 		// For compilation reasons. won't use it.
		return null;
	}

	public TEMP IRme()
	{
		TEMP dst_tmp = TEMP_FACTORY.getInstance().getFreshTEMP();

		TEMP src_tmp = this.var.IRme();

		IR.getInstance().Add_IRcommand(new IRcommand_FieldAccess(dst_tmp, 
																 src_tmp, 
																 this.cls,
																 this.fieldName));

		return dst_tmp;
	}

	public void set(AST_EXP value_exp, TYPE value_type)
	{
		TEMP src = var.IRme();
		TEMP value = value_exp.IRme();

		IR.getInstance().Add_IRcommand(new IRcommand_FieldSet(src, this.cls, this.fieldName, value));
	}
}
