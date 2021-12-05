package AST;

import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.*;

public class AST_CFIELD_LIST extends AST_Node {
    // DATA MEMBERS
	public AST_CFIELD head;
	public AST_CFIELD_LIST tail;

	// Class Constructor
	public AST_CFIELD_LIST(AST_CFIELD head, AST_CFIELD_LIST tail)
	{
		// SET A UNIQUE SERIAL NUMBER
		SerialNumber = AST_Node_Serial_Number.getFresh();

		// PRINT CORRESPONDING DERIVATION RULE
		if (tail != null) System.out.print("====================== cFields -> cField cFields\n");
		if (tail == null) System.out.print("====================== cFields -> cField      \n");

		// COPY INPUT DATA NENBERS
		this.head = head;
		this.tail = tail;
	}
	

	// The printing message for a statement list AST node
	public void PrintMe()
	{
		// AST NODE TYPE = AST STATEMENT LIST
		System.out.print("AST NODE CFIELD LIST\n");

		// RECURSIVELY PRINT HEAD + TAIL
		if (head != null) head.PrintMe();
		if (tail != null) tail.PrintMe();

		// PRINT to AST GRAPHVIZ DOT file
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"CFIELD\nLIST\n");
		
		// PRINT Edges to AST GRAPHVIZ DOT file
		if (head != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,head.SerialNumber);
		if (tail != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,tail.SerialNumber);
	}
	
	public void SemantMe(TYPE_CLASS cls) throws Exception{

		this.head.SemantMe(cls);

		if (this.tail != null)
		{
			this.tail.SemantMe(cls);
		}
	}

}
