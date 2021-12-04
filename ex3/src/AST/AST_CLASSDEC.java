package AST;

import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.*;

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

	public void SemantMe() throws Exception
	{

		if (SYMBOL_TABLE.getInstance().at_global_scope() == false)
		{
			// WE ARE NOT AT THE GLOBAL SCOPE : THROW EXCEPTION :
			throw new Exception("SEMANTIC ERROR");
		}

		if (SYMBOL_TABLE.getInstance().find(this.name1) != null)
		{
			// AN ANOTHER OBJECT WITH THIS NAME WAS ALREADY BEEN DECLARED : THROW EXCEPTION :
			throw new Exception("SEMANTIC ERROR");
		}

		TYPE parent_cls = null;

		if (this.name2 != null)
		{
			parent_cls = SYMBOL_TABLE.getInstance().find(this.name2);

			if (parent_cls == null)
			{
				// MENTIONED "PARENT" CLASS DOES NOT EXIST : THROW EXCEPTION
				throw new Exception("SEMANTIC ERROR");
			}

			if  (parent_cls.getClass() != TYPE_CLASS.class)
			{
				// MENTIONED "PARENT" IS NOT A CLASS : THROW EXCEPTION
				throw new Exception("SEMANTIC ERROR");
			}
		}

		SYMBOL_TABLE.getInstance().beginScope();
		TYPE_LIST fields_types = this.cFieldList.SemantMe();
		SYMBOL_TABLE.getInstance().endScope();

		TYPE_CLASS curr_class = new TYPE_CLASS((TYPE_CLASS) parent_cls, this.name1, fields_types);

		SYMBOL_TABLE.getInstance().enter(this.name1, curr_class);

	}
    
}
