package AST;

import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.*;
import IR.*;
import TEMP.*;

public class AST_FUNCDEC extends AST_Node {
    
    public AST_TYPE type;
	public String name;
	public AST_TYPE_LIST typeList;
	public AST_STMT_LIST body;
	public int line;
	
	private TYPE_FUNCTION func_data;
	private TYPE_CLASS cls = null;  // optional - defined only if function is a class method
	

	// Class Constructor
	public AST_FUNCDEC(AST_TYPE type, String name, AST_TYPE_LIST typeList, AST_STMT_LIST body, int line)
	{
		// SET A UNIQUE SERIAL NUMBER
		SerialNumber = AST_Node_Serial_Number.getFresh();

		// PRINT CORRESPONDING DERIVATION RULE
		System.out.format("====================== funcDec -> type ID(%s) LPAREN typeList RPAREN LBRACE stmtList RBRACE \n", name);

		// COPY INPUT DATA NENBERS
		this.type = type;
		this.name = name;
		this.typeList = typeList;
		this.body = body;
		this.line = line;
	}

	// The printing message for a statement list AST node
	public void PrintMe()
	{
		// AST NODE TYPE = AST STATEMENT LIST
		System.out.print("AST NODE FUNCDEC \n");

		// RECURSIVELY PRINT HEAD + TAIL
		if (type != null) type.PrintMe();
		if (typeList != null) typeList.PrintMe();
		if (body != null) body.PrintMe();


		// PRINT to AST GRAPHVIZ DOT file
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("FUNCDEC\ntype ID(%s)(arg1,...argn) { body }\n", name));
		
		// PRINT Edges to AST GRAPHVIZ DOT file
		if (type != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,type.SerialNumber);
		if (typeList != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,typeList.SerialNumber);
		if (body != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,body.SerialNumber);
	}

	public BOX SemantMe() throws Exception
	{
		/*-------- CHECKING IF THE FUNCTION IS NESTED ---------*/

		if (SYMBOL_TABLE.getInstance().find_curr_scope_function() != null)
		{
			// IT IS NESTED (WE PROHIBIT THIS) : THROW EXCEPTION :
			String cls_name = this.getClass().getName();
			throw new Exception("SEMANTIC ERROR : " + this.line + " : " + cls_name);
		}
		
		/*-------- EXTRACTING FUNCTION'S META DATA --------*/

		SYMBOL_TABLE.getInstance().beginScope(null);
		
		TYPE_LIST args_types = null;
		if (this.typeList != null)
		{
			args_types = this.typeList.SemantMe(); 
			// Adds the new arg names and their types to the new scope recursively
			// Checks also for shadowing at the global scope

			for (TYPE t : args_types)
			{
				if (t.is_void())
				{
					// FUNCTION'S PARAMATER TYPE CANNOT BE VOID : THROW EXCEPTION :
					
					String cls_name = this.getClass().getName();
					throw new Exception("SEMANTIC ERROR : " + this.line + " : " + cls_name);
				}
			}
		}
		
		TYPE returnType = this.type.SemantMe().type; // Returns the type of the function's return value

		SYMBOL_TABLE.getInstance().endScope();

		/* ------------------ DEFINING THE FUNCTION'S TYPE --------------------- */

		TYPE_FUNCTION func_type = new TYPE_FUNCTION(returnType, this.name, args_types);

		SYMBOL_TABLE.getInstance().enter(this.name, func_type, false, false);
		
		// Adding it to the current scope (so functions from the same scope can access it)
		
		/* ------- CHECKING IF FUNCTION SHADOWS AN ANOTHER FUNCTION ILLEGALY ------- */

		// args_types.check_shadows(func_type);

		/*-------- GETTING HERE MEANS THE FUNCTION DIDN'T SHADOW AN ANOTHER METHOD; CHECK THE FUNCTION'S BODY --------*/
		
		SYMBOL_TABLE.getInstance().beginScope(func_type); // -------------------------
		
		// Add the function also to the scope of its body (for supporting recursion)
		SYMBOL_TABLE.getInstance().enter(this.name, func_type, false, false);
		
		// Adds the new arg names and their types to the new scope recursively
		if (this.typeList != null)
		{
			this.typeList.SemantMe();
		}
		
		// Makes semantic checks for the body (checks also the validity of the types of the return stmts in it)
		
		this.body.SemantMe();
		
		SYMBOL_TABLE.getInstance().endScope(); // ------------------------------------
		
		this.func_data = func_type;

		return new BOX(func_type, func_type.name);
	}
	
	public void IRMe()
	{
		// THE DECLERATION WAS CALLED INSIDE THE GLOBAL SCOPE (I.E. IT'S NOT A CLASS METHOD)
		
		IR.getInstance().Add_IRcommand(new IRcommand_Funcdef(func_data));
		
		if (body != null) body.IRMe();
	}

	public void IRMe(TYPE_CLASS cls)
	{
		// THE DECLERATION WERE CALLED INSIDE OF A CLASS
		
		IR.getInstance().Add_IRcommand(new IRcommand_Funcdef(cls, func_data));
		
		IR.getInstance().Add_IRcommand(new IRcommand_proluge(func_data));
		
		if (body != null) body.IRMe();
		
		IR.getInstance().Add_IRcommand(new IRcommand_epilogue(func_data));
	}

}
