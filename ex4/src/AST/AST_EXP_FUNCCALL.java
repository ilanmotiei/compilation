package AST;

import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.*;

public class AST_EXP_FUNCCALL extends AST_EXP {
    public AST_VAR var;
	String func_name;
	public AST_EXP_LIST expList;
	public int line;

	// Class Constructor
	public AST_EXP_FUNCCALL(AST_VAR var, String func_name, AST_EXP_LIST expList, int line)
	{
		
		// SET A UNIQUE SERIAL NUMBER
		SerialNumber = AST_Node_Serial_Number.getFresh();

		// PRINT CORRESPONDING DERIVATION RULE 
		System.out.format("====================== exp -> [var DOT] ID(%s) LPAREN expList RPAREN\n", func_name);

		// COPY INPUT DATA NENBERS
		this.var = var;
		this.func_name = func_name;
		this.expList = expList;
		this.line = line;
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
			String.format("EXP\n[var.]ID(%s) (exps)", func_name));

		// PRINT Edges to AST GRAPHVIZ DOT file
		if (var != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);
		if (expList != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,expList.SerialNumber);
	}

//	public TEMP IRme()
//	{
//		TEMP t=null;
//
//		if (params != null) { t = params.head.IRme(); }
//
//		IR.getInstance().Add_IRcommand(new IRcommand_PrintInt(t));
//
//		return null;
//	}

	public BOX SemantMe() throws Exception
	{
		/* ------------- CHEKING THE VALIDITY OF THE EXP LIST -------------- */
		
		TYPE_LIST args_types = null;

		if (this.expList != null)
		{
			args_types = this.expList.SemantMe();
		}
		
		TYPE func_dec;
		
		if (this.var == null)
		{
			// THE CALL IS A GENERIC FUNCTION CALL

			func_dec = SYMBOL_TABLE.getInstance().find(this.func_name);
		}
		else
		{
			// WE'RE CALLING FOR A CLASS METHOD ON AN INSTANCE OF IT

			TYPE var_type = this.var.SemantMe().type;

			if (! (var_type.is_class()))
			{
				// VARIABLE IS NOT A CLASS OBJECT : THROW EXCEPTION :
				String cls_name = this.getClass().getName();
				throw new Exception("SEMANTIC ERROR : " + this.line + " : " + cls_name);
			}

			func_dec = SYMBOL_TABLE.getInstance().find_by_hierarchy((TYPE_CLASS) var_type, this.func_name);
		}

		if (func_dec == null)
		{
			// THE FUNCTION WAS NOT DEFINED YET, OR NOT DEFINED FOR THE CLASS OF THE INSTANCE WHICH CALLED IT : THROW EXCEPTION

			String cls_name = this.getClass().getName();
			throw new Exception("SEMANTIC ERROR : " + this.line + " : " + cls_name);
		}

		if ( ! (func_dec.is_function()))
		{
			// WE CALLED A VERIABLE/CLASS AS A METHOD : THROW EXCEPTION :
			String cls_name = this.getClass().getName();
			throw new Exception("SEMANTIC ERROR : " + this.line + " : " + cls_name);
		}

		// ELSE : 

		if (((TYPE_FUNCTION) func_dec).AcceptableArgs(args_types) == false)
		{
			// GIVEN ARGUMENTS AREN'T ACCEPTABLE; THROW EXCEPTION :
			String cls_name = this.getClass().getName();
			throw new Exception("SEMANTIC ERROR : " + this.line + " : " + cls_name);
		}

		// VALID;

		TYPE_FUNCTION func = (TYPE_FUNCTION) func_dec;

		return new BOX(func.returnType, func.name);
	}
    
}
