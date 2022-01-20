package AST;

import TYPES.*;
import SYMBOL_TABLE.SYMBOL_TABLE;
import TEMP.*;
import IR.*;


public class AST_EXP_NIL extends AST_EXP {

	public int line;
    
	/* Class Constructor */
	public AST_EXP_NIL(int line)
	{
		// SET A UNIQUE SERIAL NUMBER 
		SerialNumber = AST_Node_Serial_Number.getFresh();

		// PRINT CORRESPONDING DERIVATION RULE
		System.out.format("====================== exp -> NIL\n");

		this.line = line;
	}

	
	// The printing message for an INT EXP AST node
	public void PrintMe()
	{
		// AST NODE TYPE = AST INT EXP
		System.out.format("AST NODE NIL\n");

		// Print to AST GRAPHIZ DOT file
        AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("NIL"));
	}

	public BOX SemantMe()
	{
		return new BOX(TYPE_NIL.getInstance(), null, true);
	}
	
	public TEMP IRMe() {
		
		TEMP t = TEMP_FACTORY.getInstance().getFreshTEMP();

		IR.getInstance().Add_IRcommand(new IRcommand_Nil(t));

		return t;
	}
    
}
