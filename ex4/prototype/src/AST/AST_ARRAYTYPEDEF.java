package AST;

import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.*;
import TEMP.*;
import IR.*;

public class AST_ARRAYTYPEDEF extends AST_Node {
    public String name;
	public AST_TYPE type;
	public int line;

	public TYPE_ARRAY arr_type_data;
	
	// Class Constructor
	public AST_ARRAYTYPEDEF(String name, AST_TYPE type, int line)
	{
		// SET A UNIQUE SERIAL NUMBER
		SerialNumber = AST_Node_Serial_Number.getFresh();

		// PRINT CORRESPONDING DERIVATION RULE
		System.out.format("====================== arrayTypedef -> ARRAY ID(%s) EQ type LBRACK RBRACK \n", name);

		// COPY INPUT DATA NENBERS
		this.type = type;
		this.name = name;
		this.line = line;
	}

	// The printing message for a statement list AST node
	public void PrintMe()
	{
		// AST NODE TYPE = AST STATEMENT LIST
		System.out.print("AST NODE ARRAYTYPEDEF \n");

		// RECURSIVELY PRINT HEAD + TAIL
		if (type != null) type.PrintMe();


		// PRINT to AST GRAPHVIZ DOT file
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("ARRAYTYPEDEF\nARRAY ID(%s) = type[] \n", name));
		
		// PRINT Edges to AST GRAPHVIZ DOT file
		if (type != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,type.SerialNumber);
	}

	public void SemantMe() throws Exception
	{

		if ( ! SYMBOL_TABLE.getInstance().at_global_scope())
		{
			// WE ARE NOT AT THE GLOBAL SCOPE : THROW EXCEPTION :

			String cls_name = this.getClass().getName();
			throw new Exception("SEMANTIC ERROR : " + this.line + " : " + cls_name);
		}

		if (SYMBOL_TABLE.getInstance().find(this.name) != null)
		{
			// AN ANOTHER OBJECT WITH THIS NAME WAS ALREADY BEEN DECLARED : THROW EXCEPTION :

			String cls_name = this.getClass().getName();
			throw new Exception("SEMANTIC ERROR : " + this.line + " : " + cls_name);
		}

		TYPE array_elems_type = this.type.SemantMe().type;
		TYPE_ARRAY arr_type = new TYPE_ARRAY(this.name, array_elems_type); // Defining the new array type, with its name;

		if (arr_type.elems_type.is_void())
		{
			// AN ARRAY OF TYPE VOID WAS DEFINED : THROW EXCEPTION

			String cls_name = this.getClass().getName();
			throw new Exception("SEMANTIC ERROR : " + this.line + " : " + cls_name);
		}

		SYMBOL_TABLE.getInstance().enter(this.name, arr_type, false, false);
	
		this.arr_type_data = arr_type;
	}    
	
	// FOR DEFINING AN ARRAY WE DONT NEED AN INTERMEDIATE REPRESENTSATION. WE WORK WITH THE META DATA WE'VE COLLECTED ON IT AT THE SEMANTIC PARSING.
	
	public void IRMe() {
		
		TYPES.getInstance().Add_Type(arr_type_data);
		
	}
}
