package AST;

import TYPES.TYPE_INT;
import TYPES.TYPE_STRING;

public class AST_EXP_BINOP extends AST_EXP
{
	int OP;
	public AST_EXP left;
	public AST_EXP right;

	
	/* Operators */

	enum Operator{
		PLUS,
		MINUS,
		TIMES, 
		DIVIDE,
		LT,
		GT,
		EQ
	}


	/* Class Constructor */
	public AST_EXP_BINOP(AST_EXP left, AST_EXP right, int OP)
	{
		SerialNumber = AST_Node_Serial_Number.getFresh(); // SET A UNIQUE SERIAL NUMBER

		// PRINT THE CORRESPONDING DERIVATION RULE
		System.out.print("====================== exp -> exp BINOP exp\n");

		// COPY INPUT DATA NENBERS ... 
		this.left = left;
		this.right = right;
		this.OP = OP;
	}
	
	
	/* The printing message for a binop exp AST node */
	public void PrintMe()
	{
		String sOP="";

		// CONVERT OP to a printable sOP
		if (OP == 0) {sOP = "+";}
		if (OP == 1) {sOP = "-";}
		if (OP == 2) {sOP = "*";}
		if (OP == 3) {sOP = "/";}
		if (OP == 4) {sOP = "<";}
		if (OP == 5) {sOP = ">";}
		if (OP == 6) {sOP = "=";}
		

		// AST NODE TYPE = AST BINOP EXP
		System.out.print("AST NODE BINOP EXP\n");

		
		// RECURSIVELY PRINT left + right
		if (left != null) left.PrintMe();
		if (right != null) right.PrintMe();
		

		// PRINT Node to AST GRAPHVIZ DOT file
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("BINOP(%s)",sOP));
		
	
		// PRINT Edges to AST GRAPHVIZ DOT file
		if (left  != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,left.SerialNumber);
		if (right != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,right.SerialNumber);
	}

	public TYPE SemantMe(){
		TYPE left_type = this.left.SemantMe();
		TYPE right_type = this.right.SemantMe();

		if (left_type != right_type){
			// THROW AN EXCEPTION
			// TODO
		}

		if (OP != 0)
		{
			// isn't the op "+"
			if (left_type != TYPE_INT.getInstance() || left_type != TYPE_STRING.getInstance()){
				// THROW AN EXCEPTION
				// TODO
			}
		}
		else
		{
			if (left_type != TYPE_INT.getInstance()){
				// THROW AN EXCEPTION
				// TODO
			}
		}

		// Valid
		return left_type;
	}
}
