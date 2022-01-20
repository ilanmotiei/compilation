package AST;

import TYPES.*;
import SYMBOL_TABLE.SYMBOL_TABLE;
import TEMP.*;
import IR.*;

public class AST_EXP_INT extends AST_EXP
{
	public int value;
	public int hasMinus; // 0 if doesn't have a "-" before the value, 1 if does.
	public int line;

	/* Class Constructor */
	public AST_EXP_INT(int value, int hasMinus, int line)
	{
		// SET A UNIQUE SERIAL NUMBER 
		SerialNumber = AST_Node_Serial_Number.getFresh();

		// PRINT CORRESPONDING DERIVATION RULE
		System.out.format("====================== exp -> [-] INT( %d )\n", value);

		// COPY INPUT DATA NENBERS
		this.value = value;
		this.hasMinus = hasMinus;
		this.line = line;
	}

	
	// The printing message for an INT EXP AST node
	public void PrintMe()
	{
		// AST NODE TYPE = AST INT EXP
		System.out.format("AST NODE [-] INT( %d )\n",value);

		// Print to AST GRAPHIZ DOT file
		if(hasMinus == 0){
			AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("INT(%d)",value));	
		}
		else{
			AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("-INT(%d)",value));
		}
	}

	public BOX SemantMe(){

		BOX rv = new BOX(TYPE_INT.getInstance(), null, true);
		
		rv.is_zero = (this.value == 0);

		rv.is_negative = (this.hasMinus == 1);

		return rv;
	}
	
	public TEMP IRMe()
	{
		TEMP t = TEMP_FACTORY.getInstance().getFreshTEMP();
		
		IR.getInstance().Add_IRcommand(new IRcommandConstInt(t, (1 - 2*hasMinus) * value));
		
		return t;
	}
}
