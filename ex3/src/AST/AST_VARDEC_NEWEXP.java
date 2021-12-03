package AST;

import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.TYPE;
import sun.net.www.content.text.plain;

public class AST_VARDEC_NEWEXP extends AST_VARDEC {
    // DATA MEMBERS
	public AST_TYPE type;
	public String name; 
	public AST_NEWEXP newExp;

	// Class Constructor
	public AST_VARDEC_NEWEXP(AST_TYPE type, String name, AST_NEWEXP newExp)
	{
		// SET A UNIQUE SERIAL NUMBER
		SerialNumber = AST_Node_Serial_Number.getFresh();

		// PRINT CORRESPONDING DERIVATION RULE
		System.out.format("====================== varDec -> type ID(%s) ASSIGN newExp SEMICOLON\n", name);

		// COPY INPUT DATA NENBERS
		this.type = type;
		this.name = name;
		this.newExp = newExp;
	}


	// The printing message for a statement list AST node
	public void PrintMe()
	{
		// AST NODE TYPE = AST STATEMENT LIST
		System.out.print("AST NODE NEWEXP VARDEC\n");

		// RECURSIVELY PRINT HEAD + TAIL
		if (type != null) type.PrintMe();
		if (newExp != null) newExp.PrintMe();

		// PRINT to AST GRAPHVIZ DOT file
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("NEWEXP ASSIGN\ntype ID(%s) := right\n", name));
		
		// PRINT Edges to AST GRAPHVIZ DOT file
		if (type != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,type.SerialNumber);
		if (newExp != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,newExp.SerialNumber);
	}


	public void SemantMe()
	{

		if (SYMBOL_TABLE.getInstance().find(this.name) != null)
		{
			// A VARIABLE WITH THE SAME NAME WAS ALLREADY DECLARED : THROW ERROR : TODO
		}

		// ELSE : 

		TYPE var_type = this.type.SemantMe();

		if (var_type.semantically_equals(this.newExp.SemantMe()) == false)
		{
			// the newExp cannot be assigned to the variable
		}

		SYMBOL_TABLE.getInstance().enter(this.name, var_type);
	}
}
