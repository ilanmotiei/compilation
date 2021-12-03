package AST;

import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.TYPE_ARRAY;
import TYPES.TYPE_VOID;

public class AST_ARRAYTYPEDEF extends AST_Node {
    public String name;
	public AST_TYPE type;
	
	// Class Constructor
	public AST_ARRAYTYPEDEF(String name, AST_TYPE type)
	{
		// SET A UNIQUE SERIAL NUMBER
		SerialNumber = AST_Node_Serial_Number.getFresh();

		// PRINT CORRESPONDING DERIVATION RULE
		System.out.format("====================== arrayTypedef -> ARRAY ID(%s) EQ type LBRACK RBRACK \n", name);

		// COPY INPUT DATA NENBERS
		this.type = type;
		this.name = name;
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

	public void SemantMe(){

		if (SYMBOL_TABLE.getInstance().find_curr_scope_class() != null)
		{
			// CLASS ISN'T DEFINED AT THE GLOBAL SCOPE : THROW EXCEPTION : TODO
		}

		TYPE array_elems_type = this.type.SemantMe();
		TYPE_ARRAY arr_type = new TYPE_ARRAY(this.name, array_elems_type); // Defining the new array type, with its name;

		SYMBOL_TABLE.getInstance().enter(this.name, arr_type);
	}
    
}
