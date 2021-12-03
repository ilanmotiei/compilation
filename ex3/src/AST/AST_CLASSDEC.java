package AST;

import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.TYPE_CLASS;

public class AST_CLASSDEC extends AST_Node {
    public String name1;
	public String name2;
	public AST_CFIELD_LIST cFieldList;
	
	// Class Constructor
	public AST_CLASSDEC(String name1, String name2, AST_CFIELD_LIST cFieldList)
	{
		// SET A UNIQUE SERIAL NUMBER
		SerialNumber = AST_Node_Serial_Number.getFresh();

		// PRINT CORRESPONDING DERIVATION RULE
		if(name2 == null)
			System.out.format("====================== classDec -> CLASS ID(%s) LBRACE cFieldList RBRACE \n", name1);
		else
			System.out.format("====================== classDec -> CLASS ID(%s) EXTENDS ID(%s) LBRACE cFieldList RBRACE \n",name1, name2);


		// COPY INPUT DATA NENBERS
		this.name1 = name1;
		this.name2 = name2;
		this.cFieldList = cFieldList;
	}


	// The printing message for a statement list AST node
	public void PrintMe()
	{
		// AST NODE TYPE = AST STATEMENT LIST
		System.out.print("AST NODE CLASSDEC \n");

		// RECURSIVELY PRINT HEAD + TAIL
		if (cFieldList != null) cFieldList.PrintMe();


		// PRINT to AST GRAPHVIZ DOT file
		if(name2 == null){
			AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("CLASSDEC\nCLASS ID(%s) {cFields} \n", name1));
		}
		else{
			AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("CLASSDEC\nCLASS ID(%s) EXTENDS ID(%s) {cFields} \n", name1, name2));
		}
		
		// PRINT Edges to AST GRAPHVIZ DOT file
		if (cFieldList != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,cFieldList.SerialNumber);
	}

	public TYPE SemantMe()
	{

		if (SYMBOL_TABLE.getInstance().find_curr_scope_class() != null)
		{
			// CLASS ISN'T DEFINED AT THE GLOBAL SCOPE : THROW EXCEPTION : TODO
		}

		SYMBOL_TABLE.getInstance().beginScope();
		TYPE_LIST fields_types = this.cFieldList.SemantMe();
		SYMBOL_TABLE.getInstance().endScope();

		TYPE cls_type = new TYPE_CLASS(this.name2, this.name1, fields);

		SYMBOL_TABLE.getInstance().enter(this.name1, cls_type);

		return cls_type;
	}
    
}
