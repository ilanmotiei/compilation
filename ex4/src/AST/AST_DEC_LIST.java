package AST;

import SYMBOL_TABLE.SYMBOL_TABLE;

public class AST_DEC_LIST extends AST_Node
{ 
    // DATA MEMBERS
	public AST_DEC head;
	public AST_DEC_LIST tail;
	public int line;

	// Class Constructor
	public AST_DEC_LIST(AST_DEC head,AST_DEC_LIST tail, int line)
	{
		// SET A UNIQUE SERIAL NUMBER
		SerialNumber = AST_Node_Serial_Number.getFresh();

		// PRINT CORRESPONDING DERIVATION RULE
		if (tail != null) System.out.print("====================== decs -> dec decs\n");
		if (tail == null) System.out.print("====================== decs -> dec      \n");

		// COPY INPUT DATA NENBERS
		this.head = head;
		this.tail = tail;
		this.line = line;
	}

	public TEMP IRme()
	{
		if (head != null) head.IRme();
		if (tail != null) tail.IRme();
		
		return null;			
	}

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

	public void SemantMe() throws Exception
	{
		this.head.SemantMe();
		
		if (this.tail != null)
		{
			this.tail.SemantMe();
		}
	}
}
