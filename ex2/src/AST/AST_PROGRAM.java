package AST;

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
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,decList.SerialNumber);
			
	}
}
