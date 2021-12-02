package AST;

import SYMBOL_TABLE.SYMBOL_TABLE;
import SYMBOL_TABLE.SYMBOL_TABLE_GRAPHVIZ;

public class AST_DEC_LIST extends AST_Node{ 
    // DATA MEMBERS
	public AST_DEC head;
	public AST_DEC_LIST tail;

	// Class Constructor
	public AST_DEC_LIST(AST_DEC head,AST_DEC_LIST tail)
	{
		// SET A UNIQUE SERIAL NUMBER
		SerialNumber = AST_Node_Serial_Number.getFresh();

		// PRINT CORRESPONDING DERIVATION RULE
		if (tail != null) System.out.print("====================== decs -> dec decs\n");
		if (tail == null) System.out.print("====================== decs -> dec      \n");

		// COPY INPUT DATA NENBERS
		this.head = head;
		this.tail = tail;
	}


	// The printing message for a statement list AST node
	public void PrintMe()
	{
		// AST NODE TYPE = AST STATEMENT LIST
		System.out.print("AST NODE DEC LIST\n");

		// RECURSIVELY PRINT HEAD + TAIL
		if (head != null) head.PrintMe();
		if (tail != null) tail.PrintMe();

		// PRINT to AST GRAPHVIZ DOT file
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"DEC\nLIST\n");
		
		// PRINT Edges to AST GRAPHVIZ DOT file
		if (head != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,head.SerialNumber);
		if (tail != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,tail.SerialNumber);
	}

	public TYPE SemantMe(){
		AST_DEC_LIST dec_list = this.tail;

		for (AST_DEC dec=this.head ; dec != null ; dec=dec_list.head, dec_list=dec_list.tail){
			Type dec_type = dec.SemantMe();

			// TODO
		}

		// TODO
		SYMBOL_TABLE_GRAPHVIZ.getInstance();
	}
}
