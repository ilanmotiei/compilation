package AST;

import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.TYPE_CLASS;
import TYPES.TYPE_FUNCTION;
import TYPES.TYPE_LIST;
import jdk.nashorn.internal.codegen.types.Type;

public class AST_FUNCDEC extends AST_Node {
    
    public AST_TYPE type;
	public String name;
	public AST_TYPE_LIST typeList;
    public AST_STMT_LIST body;

	// Class Constructor
	public AST_FUNCDEC(AST_TYPE type, String name, AST_TYPE_LIST typeList, AST_STMT_LIST body)
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

	public TYPE SemantMe()
	{

		/*-------- EXTRACTING FUNCTION'S META DATA --------*/

		SYMBOL_TABLE.getInstance().beginScope();
		TYPE_LIST args_types = this.typeList.SemantMe(); // Adds the new arg names and their types to the new scope recursively
		TYPE returnType = this.type.SemantMe(); // Returns the type of the function's return value
		SYMBOL_TABLE.getInstance().endScope();

		/*-------- DEFINING THE FUNCTION'S TYPE AND ADDING IT TO THE SCOPE --------*/

		TYPE_CLASS func_class = SYMBOL_TABLE.getInstance().find_curr_scope_class();
		TYPE_FUNCTION func_type = new TYPE_FUNCTION(returnType, this.name, args_types, func_class);
		SYMBOL_TABLE.getInstance().enter(this.name, func_type);

		/*-------- CHECKING IF FUNCTION SHADOWS AN ANOTHER FUNCTION ILLEGALY --------*/

		this.check_shadows(func_dec);

		/*-------- GETTING HERE MEANS THE FUNCTION DIDN'T SHADOW AN ANOTHER METHOD; CHECK THE FUNCTION'S BODY --------*/
		
		SYMBOL_TABLE.getInstance().beginScope();
		this.typeList.SemantMe(); // Adds the new arg names and their types to the new scope recursively
		this.body.SemantMe(); // Makes semantic checks for the body (checks also the validity of the types of the return stmts in it)
		SYMBOL_TABLE.getInstance().endScope();

		return func_type;
	}

	// Checks if the function illegaly shadows another function. throws an exception if does.

	public void check_shadows(TYPE_FUNCTION thisfunc_dec)
	{
		TYPE_LIST all_decs = SYMBOL_TABLE.getInstance().find_all(this.name);

		for (Type dec : all_decs)
		{
			if (dec.getClass() != TYPE_FUNCTION.class)
			{
				// SHADOWS A VARIABLE OR A CLASS NAME : THROW EXCEPTION : TODO
			}

			TYPE_FUNCTION func_dec = (TYPE_FUNCTION) dec; 

			if (func_dec.returnType != thisfunc_dec.returnType)
			{
				// ABSOLUTELY SHADOWS : THROW EXCEPTION : TODO
			}

			if (thisfunc_dec.cls == null)
			{
				// OUR METHOD IS NOT IN ANY CLASS THEREFORE SHADOWS AN ANOTHER FUNCTION NAME : THROW EXCEPTION : TODO 
			}

			if (curr_scope_class.is_ancestor(thisfunc_dec.cls) == false)
			{
				// WE AREN'T OVERRIDING AN INHERITED METHOD, BUT SHADOWING A METHOD : THROW EXCEPTION : TODO
			}

			// ELSE : WE ARE TRYING TO OVERRIDE AN INHERITED METHOD

			if (func_dec.params.equals(thisfunc_dec.params) == false)
			{
				// WE ARE SHADOWING A FUNCTION WITH THE SAME NAME AND RETURN_TYPE IN SOME PARENT METHOD : THROW EXCEPTION : TODO
			}

			// ELSE : WE ARE LEGALLY OVERRIDING AN INHERITED METHOD : CONTINUE TO CHECK THE NEXT DECLERATION
		}

		// ALL THE DECLERATIONS CHECKED ARE FINE, SO THIS FUNCTION'S DECLERATION IS LEGAL;
	}
}
