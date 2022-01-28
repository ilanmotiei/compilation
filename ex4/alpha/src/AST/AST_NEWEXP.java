package AST;

import SYMBOL_TABLE.SYMBOL_TABLE;
import TEMP.*;
import TYPES.*;
import IR.*;

public class AST_NEWEXP extends AST_Node {
	public AST_TYPE type;
	public AST_EXP exp;
	public int line;

	// extracted expression type (inferred at the semantic analysis)
	public TYPE _type_;

    public AST_NEWEXP(AST_TYPE type, AST_EXP exp, int line) {
        // SET A UNIQUE SERIAL NUMBER
		SerialNumber = AST_Node_Serial_Number.getFresh();

		// PRINT CORRESPONDING DERIVATION RULE
		if(exp == null)
			System.out.print("====================== newExp -> NEW type\n");
		else
			System.out.print("====================== newExp -> NEW type LBRACK exp RBRACK\n");

		// COPY INPUT DATA NENBERS
		this.type = type;
		this.exp = exp;
		this.line = line;
    }

    public void PrintMe()
	{
		// AST NODE TYPE = AST ASSIGNMENT STATEMENT
		System.out.print("AST NODE NEWEXP\n");
        
		
		// RECURSIVELY PRINT VAR + EXP
		if (type != null) type.PrintMe();
		if (exp != null) exp.PrintMe();

		// PRINT Node to AST GRAPHVIZ DOT file
		if(exp == null){
			AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"newExp\n NEW type \n");
		}
		else{
			AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"newExp\n NEW type[exp] \n");
		}
		
		
		// PRINT Edges to AST GRAPHVIZ DOT file
		if(type != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,type.SerialNumber);
		if(exp != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,exp.SerialNumber);
	}

	public BOX SemantMe() throws Exception{
		if (exp == null)
		{
			// THE FORM IS : NEW ${type}

			BOX type_box = this.type.SemantMe();

			this._type_ = type_box.type;

			return type_box;
		}
		else
		{
			// THE FORM IS : NEW ${type} [${exp}]

			BOX exp_box = this.exp.SemantMe();

			if ( ! exp_box.type.is_int()) 
			{
				// EXP'S SIZE IS NOT AN INTEGER : THROW EXCEPTION :
				String cls_name = this.getClass().getName();
				throw new Exception("SEMANTIC ERROR : " + this.line + " : " + cls_name);
			}

			if (exp_box.is_negative || exp_box.is_zero) 
			{
				// EXP'S VALUE IS <= 0, WHICH IS ILLEGAL : THROW EXCEPTION :
				String cls_name = this.getClass().getName();
				throw new Exception("SEMANTIC ERROR : " + this.line + " : " + cls_name);
			}

			// ELSE :

			this._type_ = this.type.SemantMe().type;

			return new BOX(this._type_, null, false, true);
		}
	}

	public TEMP IRme()
	{
		// The result that this method will return
		TEMP dst = TEMP_FACTORY.getInstance().getFreshTEMP();

		if (exp != null)
		{
			// We are defining an ARRAY : 

			TEMP arr_size = this.exp.IRme();

			IR.
			getInstance().
			Add_IRcommand(new IRcommand_NewArray(dst,
												 arr_size, 
												 this._type_));

			// Initializing the array :

			TEMP zero = TEMP_FACTORY.getInstance().getFreshTEMP();
			IR.getInstance().Add_IRcommand(new IRcommand_LoadImmediate(zero, 0));

			TEMP one = TEMP_FACTORY.getInstance().getFreshTEMP();
			IR.getInstance().Add_IRcommand(new IRcommand_LoadImmediate(one, 1));

			TEMP idx = TEMP_FACTORY.getInstance().getFreshTEMP();
			IR.getInstance().Add_IRcommand(new IRcommand_LoadImmediate(idx, 0));

			TEMP subtraction = TEMP_FACTORY.getInstance().getFreshTEMP();

			String start_label = IRcommand.getFreshLabel("start_label");
			String end_label = IRcommand.getFreshLabel("end_label");

			IR.getInstance().Add_IRcommand(new IRcommand_Label(start_label));

			IR.getInstance().Add_IRcommand(new IRcommand_Binop_Sub_Integers(subtraction, arr_size, idx));
			IR.getInstance().Add_IRcommand(new IRcommand_Jump_If_Eq_To_Zero(subtraction, end_label));
			IR.getInstance().Add_IRcommand(new IRcommand_ArraySet(dst, idx, zero));
			IR.getInstance().Add_IRcommand(new IRcommand_Binop_Add_Integers(idx, idx, one));
			IR.getInstance().Add_IRcommand(new IRcommand_Jump_Label(start_label));

			IR.getInstance().Add_IRcommand(new IRcommand_Label(end_label));
		}
		else
		{
			// we're defining a class object

			IR.
			getInstance().
			Add_IRcommand(new IRcommand_NewClass(dst,
												 (TYPE_CLASS) this._type_));

		}

		return dst;
	}
}
