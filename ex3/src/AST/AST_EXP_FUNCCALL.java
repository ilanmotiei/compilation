package AST;

import SYMBOL_TABLE.SYMBOL_TABLE;

public class AST_EXP_FUNCCALL extends AST_EXP {
    public AST_VAR var;
	String name;
    public AST_EXP_LIST expList;

	// Class Constructor
	public AST_EXP_FUNCCALL(AST_VAR var, String name, AST_EXP_LIST expList)
	{
		
		// SET A UNIQUE SERIAL NUMBER
		SerialNumber = AST_Node_Serial_Number.getFresh();

		// PRINT CORRESPONDING DERIVATION RULE 
		System.out.format("====================== exp -> [var DOT] ID(%s) LPAREN expList RPAREN\n", name);

		// COPY INPUT DATA NENBERS
		this.var = var;
		this.name = name;
		this.expList = expList;
	}
	
	
	// The default message for an exp var AST node
	public void PrintMe()
	{
		
		// AST NODE TYPE = EXP VAR AST NODE
		System.out.print("AST NODE EXP VARDOTID\n");

		// RECURSIVELY PRINT var
		if (var != null) var.PrintMe();
		if (expList != null) expList.PrintMe();
		
		
		// Print to AST GRAPHIZ DOT file
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("EXP\n[var.]ID(%s) (exps)", name));

		// PRINT Edges to AST GRAPHVIZ DOT file
		if (var != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);
		if (expList != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,expList.SerialNumber);
	}

	public TYPE SemantMe(){

		/* ------------- CHEKING THE VALIDITY OF THE EXP LIST -------------- */

		TYPE_LIST args_types = this.expList.SemantMe();
		
		if (this.var == null)
		{
			// THE CALL IS A GENERIC FUNCTION CALL

			TYPE_CLASS curr_scope_class = SYMBOL_TABLE.getInstance().find_curr_scope_class();
			TYPE func_dec = SYMBOL_TABLE.getInstance().find_by_hierarchy(curr_scope_class, this.func_name);
		}
		else
		{
			// WE'RE CALLING FOR A CLASS METHOD ON AN INSTANCE OF IT

			TYPE var_type = this.var.SemantMe();

			if (var_type.getClass() == TYPE_CLASS_VAR_DEC.class)
			{
				// VARIABLE IS NOT A CLASS OBJECT : THROW EXCEPTION : TODO
			}

			TYPE_CLASS var_class = (TYPE_CLASS_VAR_DEC (var_type)).cls;

			TYPE func_dec = var_class.find_by_hierarchy(var_class, this.func_name);
		}

		if (func_dec.getClass() != TYPE_FUNCTION.class)
		{
			// WE CALLED A VERIABLE/CLASS AS A METHOD : THROW EXCEPTION : TODO
		}

		if (func_dec == null)
		{
			// THE FUNCTION WAS NOT DEFINED YET, OR NOT DEFINED FOR THE CLASS OF THE INSTANCE WHICH CALLED IT : THROW EXCEPTION

			// TODO
		}

		// ELSE : 

		if (((TYPE_FUNCTION) func_dec).params.semantically_equals(args_types) == false)
		{
			// GIVEN ARGUMENTS AREN'T ACCEPTABLE; THROW EXCEPTION : TODO
		}

		// VALID;

		return ((TYPE_FUNCTION) func_dec).returnType;
	}
    
}
