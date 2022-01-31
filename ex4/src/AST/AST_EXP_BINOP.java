package AST;

import IR.*;
import SYMBOL_TABLE.*;
import TYPES.*;
import TEMP.*;
import java.lang.Math;

public class AST_EXP_BINOP extends AST_EXP
{
	int OP;
	public AST_EXP left;
	public AST_EXP right;
	public int line;

	// codegen metadata

	boolean is_between_strings = false;
	
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
	public AST_EXP_BINOP(AST_EXP left, AST_EXP right, int OP, int line)
	{
		SerialNumber = AST_Node_Serial_Number.getFresh(); // SET A UNIQUE SERIAL NUMBER

		// PRINT THE CORRESPONDING DERIVATION RULE
		System.out.print("====================== exp -> exp BINOP exp\n");

		// COPY INPUT DATA NENBERS ... 
		this.left = left;
		this.right = right;
		this.OP = OP;
		this.line = line;
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

	public BOX SemantMe() throws Exception{

		BOX left_box = this.left.SemantMe();
		BOX right_box = this.right.SemantMe();

		TYPE left_type = left_box.type;
		TYPE right_type = right_box.type;

		if (OP == 0)
		{
			// OP == "+"
			if ((left_type.is_int() == false) && (right_type.is_int() == false))
			{
				if ((left_type.is_string() == false) && (right_type.is_string() == false))
				{
					// THE BINARY OPERATION "+" CANNOT BE PERFORMED ON THE TWO SIDES : THROW EXCEPTION :
					String cls_name = this.getClass().getName();
					throw new Exception("SEMANTIC ERROR : " + this.line + " : " + cls_name);
				}

				this.is_between_strings = true;
			}

			// ELSE :

			return new BOX(left_type);
		}

		// ELSE :

		if (OP == 6)
		{
			// OP == "="

			if (! (left_type.semantically_equals(right_type) || right_type.semantically_equals(left_type)))
			{
				// TYPES CANNOT BE TESTED FOR EQUALITY : THROW EXCEPTION :
				
				String cls_name = this.getClass().getName();
				throw new Exception("SEMANTIC ERROR : " + this.line + " : " + cls_name);
			}

			if (left_type.is_void() || right_type.is_void())
			{
				// CAN'T COMPARE VOID TYPES OBJECTS

				String cls_name = this.getClass().getName();
				throw new Exception("SEMANTIC ERROR : " + this.line + " : " + cls_name);
			
			}

			if (left_type.is_string() && right_type.is_string())
			{
				this.is_between_strings = true;
			}

			return new BOX(TYPE_INT.getInstance(), null, false);
		}

		// ELSE : OP is one of ["-", "*", "/", "<", ">"]

		if ( (! left_type.is_int()) || (! right_type.is_int()) )
		{
			// ONE OF THE BINARY OPERATION SIDES TYPE IS NOT AN INTEGER : THROW EXCEPTION :
			
			String cls_name = this.getClass().getName();
			throw new Exception("SEMANTIC ERROR : " + this.line + " : " + cls_name);
		}

		// ELSE : THE OPERATION IS DONE BETWEEN 2 INTS

		if (OP == 3)
		{
			// OPERATION IS "/"

			if (right_box.is_zero)
			{
				// A DIVISION BY 0 WAS BEEN TRIES : THROW EXCEPTION :

				String cls_name = this.getClass().getName();
				throw new Exception("SEMANTIC ERROR : " + this.line + " : " + cls_name);
			}
		}

		// ELSE : OP is one of ["-", "*", "<", ">"]
		// EXPERSSION IS VALID AND ITS TYPE IS "TYPE_INT"

		return new BOX(TYPE_INT.getInstance(), null, false);
	}

	public TEMP IRme()
	{
		TEMP t1 = null;
		TEMP t2 = null;
		TEMP dst = TEMP_FACTORY.getInstance().getFreshTEMP();
				
		if (left  != null) t1 = left.IRme();
		if (right != null) t2 = right.IRme();


		int max = ((int) Math.pow(2, 15)) - 1;
		int min = -((int) Math.pow(2, 15)) + 1;

		if (OP == 0) {
			if (this.is_between_strings) {
				IR.
						getInstance().
						Add_IRcommand(new IRcommand_Binop_Add_Strings(dst, t1, t2));
			} else {
				// The addition is between integers.
				IR.
						getInstance().
						Add_IRcommand(new IRcommand_Binop_Add_Integers(dst, t1, t2));

				IR.getInstance().Add_IRcommand(new IRcommand_Bind(dst, min, max));
			}
		}

		if (OP == 1)
		{
			IR.
			getInstance().
			Add_IRcommand(new IRcommand_Binop_Sub_Integers(dst,t1,t2));

			IR.getInstance().Add_IRcommand(new IRcommand_Bind(dst, min, max));
		}
		if (OP == 2)
		{
			IR.
			getInstance().
			Add_IRcommand(new IRcommand_Binop_Mul_Integers(dst,t1,t2));

			IR.getInstance().Add_IRcommand(new IRcommand_Bind(dst, min, max));
		}
		if (OP == 3)
		{
			IR.
			getInstance().
			Add_IRcommand(new IRcommand_Binop_Divide_Integers(dst,t1,t2));

			IR.getInstance().Add_IRcommand(new IRcommand_Bind(dst, min, max));
		}
		if (OP == 4)
		{
			IR.
			getInstance().
			Add_IRcommand(new IRcommand_Binop_LT_Integers(dst,t1,t2));
		}
		if (OP == 5)
		{
			// GT
			IR.
			getInstance().
			Add_IRcommand(new IRcommand_Binop_LT_Integers(dst,t2,t1));
		}
		if (OP == 6)
		{
			// EQ : 
			if (this.is_between_strings)
			{
				// This is an equality between strings
				IR.
				getInstance().
				Add_IRcommand(new IRcommand_Binop_EQ_Strings(dst,t1,t2));
			}
			else
			{
				// This is an equality between integers/objects.
				// We only check if their temporals contain the same value
				IR.
				getInstance().
				Add_IRcommand(new IRcommand_Binop_EQ_Integers(dst, t1, t2));
			}
		}

		return dst;
	}
}
