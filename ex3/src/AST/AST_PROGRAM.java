package AST;

import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.*;

public class AST_PROGRAM extends AST_Node{
    public AST_DEC_LIST decList;

    public AST_PROGRAM(AST_DEC_LIST decList){
        // SET A UNIQUE SERIAL NUMBER
		SerialNumber = AST_Node_Serial_Number.getFresh();

		// PRINT CORRESPONDING DERIVATION RULE 
		System.out.print("====================== Program -> decList\n");

        this.decList = decList;
    }

    public void PrintMe()
	{
		
		// AST NODE TYPE = EXP VAR AST NODE
		System.out.print("AST NODE PROGRAM DECLIST\n");

		// RECURSIVELY PRINT var
		if (decList != null) decList.PrintMe();
		
		// Print to AST GRAPHIZ DOT file
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"PROGRAM\nDECLIST\n");

		// PRINT Edges to AST GRAPHVIZ DOT file
		if (decList != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,decList.SerialNumber);
			
	}

	public void SemantMe() throws Exception{

		SYMBOL_TABLE.getInstance().enter("PrintInt", 
										new TYPE_FUNCTION(TYPE_VOID.getInstance(), "PrintInt", new TYPE_LIST(TYPE_INT.getInstance(), null)));

		SYMBOL_TABLE.getInstance().enter("PrintString", 
										new TYPE_FUNCTION(TYPE_VOID.getInstance(), "PrintString", new TYPE_LIST(TYPE_STRING.getInstance(), null)));

		SYMBOL_TABLE.getInstance().enter("PrintTrace", 
										new TYPE_FUNCTION(TYPE_VOID.getInstance(), "PrintTrace", null));

		decList.SemantMe();
	}
}