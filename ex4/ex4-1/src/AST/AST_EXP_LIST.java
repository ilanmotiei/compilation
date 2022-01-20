package AST;

import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.*;

import IR.*;
import TEMP.*;

public class AST_EXP_LIST extends AST_Node {
    // DATA MEMBERS
	public AST_EXP head;
	public AST_EXP_LIST tail;
	public int line;

	// Class Constructor
	public AST_EXP_LIST(AST_EXP head,AST_EXP_LIST tail, int line)
	{
		// SET A UNIQUE SERIAL NUMBER
		SerialNumber = AST_Node_Serial_Number.getFresh();

		// PRINT CORRESPONDING DERIVATION RULE
		if (tail == null) System.out.print("====================== exps ->  exp \n");
		if (tail != null) System.out.print("====================== stmts -> exp COMMA exps \n");

		// COPY INPUT DATA NENBERS
		this.head = head;
		this.tail = tail;
		this.line = line;
	}


	// The printing message for a statement list AST node
	public void PrintMe()
	{
		// AST NODE TYPE = AST STATEMENT LIST
		System.out.print("AST NODE EXP LIST\n");

		// RECURSIVELY PRINT HEAD + TAIL
		if (head != null) head.PrintMe();
		if (tail != null) tail.PrintMe();

		// PRINT to AST GRAPHVIZ DOT file
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"EXP\nLIST\n");
		
		// PRINT Edges to AST GRAPHVIZ DOT file
		if (head != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,head.SerialNumber);
		if (tail != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,tail.SerialNumber);
	}

	public TYPE_LIST SemantMe() throws Exception
	{
		return this.createTypelist();
	}

	public TYPE_LIST createTypelist() throws Exception
	{
		if (this.tail != null)
		{
			return new TYPE_LIST(this.head.SemantMe().type, this.tail.createTypelist());
		}
		else
		{
			return new TYPE_LIST(this.head.SemantMe().type, null);
		}
	}
	
	public TEMP_LIST IRMe()
	{
		return createArgslist();
	}
	
	public TEMP_LIST createArgslist()
	{
		if (tail != null)
		{
			return new TEMP_LIST(head.IRMe(), tail.createArgslist());
		}
		else
		{
			return new TEMP_LIST(head.IRMe(), null);
		}
	}
}
