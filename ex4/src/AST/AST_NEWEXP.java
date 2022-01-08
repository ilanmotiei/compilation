package AST;

import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.*;

public class AST_NEWEXP extends AST_Node {
	public AST_TYPE type;
	public AST_EXP exp;
	public int line;

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

			return this.type.SemantMe();
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

			return new BOX(this.type.SemantMe().type, null, false, true);
		}
	}
    
}
