package AST;

import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.*;

public class AST_FUNCDEC extends AST_Node {
    
    public AST_TYPE type;
	public String name;
	public AST_TYPE_LIST typeList;
	public AST_STMT_LIST body;
	public int line;

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
		}
		
		TYPE returnType = this.type.SemantMe().type; // Returns the type of the function's return value

		SYMBOL_TABLE.getInstance().endScope();

		/* ------------------ DEFINING THE FUNCTION'S TYPE --------------------- */

		TYPE_FUNCTION func_type = new TYPE_FUNCTION(returnType, this.name, args_types);

		SYMBOL_TABLE.getInstance().enter(this.name, func_type); 
		// Adding it to the current scope (so functions from the same scope can access it)
		
		/*-------- CHECKING IF FUNCTION SHADOWS AN ANOTHER FUNCTION ILLEGALY --------*/

		// args_types.check_shadows(func_type);

		/*-------- GETTING HERE MEANS THE FUNCTION DIDN'T SHADOW AN ANOTHER METHOD; CHECK THE FUNCTION'S BODY --------*/
		
		SYMBOL_TABLE.getInstance().beginScope(func_type); // -------------------------
		
		// Add the function also to the scope of its body (for supporting recursion)
		SYMBOL_TABLE.getInstance().enter(this.name, func_type);
		
		// Adds the new arg names and their types to the new scope recursively
		if (this.typeList != null)
		{
			this.typeList.SemantMe();
		}
		
		// Makes semantic checks for the body (checks also the validity of the types of the return stmts in it)
		this.body.SemantMe();
		
		SYMBOL_TABLE.getInstance().endScope(); // ------------------------------------

		return new BOX(func_type, func_type.name);
	}

	// Checks if the function illegaly shadows another function. throws an exception if does.


	/*
	public void check_shadows(TYPE_FUNCTION thisfunc_dec)
	{
		TYPE_LIST all_decs = SYMBOL_TABLE.getInstance().find_all(this.name);

		for (TYPE dec : all_decs)
		{
			if ( ! dec.is_class())
			{
				// SHADOWS A VARIABLE OR A CLASS NAME : THROW EXCEPTION :
				String cls_name = this.getClass().getName();
				throw new Exception("SEMANTIC ERROR : " + this.line + " : " + cls_name);
			}

			TYPE_FUNCTION func_dec = (TYPE_FUNCTION) dec; 

			if (func_dec.returnType != thisfunc_dec.returnType)
			{
				// ABSOLUTELY SHADOWS : THROW EXCEPTION :
				String cls_name = this.getClass().getName();
				throw new Exception("SEMANTIC ERROR : " + this.line + " : " + cls_name);
			}

			if (thisfunc_dec.cls == null)
			{
				// OUR METHOD IS NOT IN ANY CLASS THEREFORE SHADOWS AN ANOTHER FUNCTION NAME : THROW EXCEPTION :
				String cls_name = this.getClass().getName();
				throw new Exception("SEMANTIC ERROR : " + this.line + " : " + cls_name);
			}

			if (thisfunc_dec.cls.is_ancestor(func_dec.cls) == false)
			{
				// WE AREN'T OVERRIDING AN INHERITED METHOD, BUT SHADOWING A METHOD : THROW EXCEPTION :
				String cls_name = this.getClass().getName();
				throw new Exception("SEMANTIC ERROR : " + this.line + " : " + cls_name);
			}

			// ELSE : WE ARE TRYING TO OVERRIDE AN INHERITED METHOD

			if (func_dec.params.semantically_equals(thisfunc_dec.params) == false)
			{
				// WE ARE SHADOWING A FUNCTION WITH THE SAME NAME AND RETURN_TYPE IN SOME PARENT METHOD : THROW EXCEPTION :
				String cls_name = this.getClass().getName();
				throw new Exception("SEMANTIC ERROR : " + this.line + " : " + cls_name);
			}

			// ELSE : WE ARE LEGALLY OVERRIDING AN INHERITED METHOD : CONTINUE TO CHECK THE NEXT DECLERATION
		}

		// ALL THE DECLERATIONS CHECKED ARE FINE, SO THIS FUNCTION'S DECLERATION IS LEGAL;
	}

	*/
}
