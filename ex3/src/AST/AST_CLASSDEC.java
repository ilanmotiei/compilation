package AST;

import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.*;

public class AST_CLASSDEC extends AST_Node {
    public String class_name;
	public String father_name;
	public AST_CFIELD_LIST cFieldList;
	
	// Class Constructor
	public AST_CLASSDEC(String class_name, String father_name, AST_CFIELD_LIST cFieldList)
	{
		// SET A UNIQUE SERIAL NUMBER
		SerialNumber = AST_Node_Serial_Number.getFresh();

		// PRINT CORRESPONDING DERIVATION RULE
		if(father_name == null)
			System.out.format("====================== classDec -> CLASS ID(%s) LBRACE cFieldList RBRACE \n", class_name);
		else
			System.out.format("====================== classDec -> CLASS ID(%s) EXTENDS ID(%s) LBRACE cFieldList RBRACE \n",class_name, father_name);


		// COPY INPUT DATA NENBERS
		this.class_name = class_name;
		this.father_name = father_name;
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
		if(father_name == null){
			AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("CLASSDEC\nCLASS ID(%s) {cFields} \n", class_name));
		}
		else{
			AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("CLASSDEC\nCLASS ID(%s) EXTENDS ID(%s) {cFields} \n", class_name, father_name));
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

		if (SYMBOL_TABLE.getInstance().find(this.class_name) != null)
		{
			// AN ANOTHER OBJECT WITH THIS NAME WAS ALREADY BEEN DECLARED : THROW EXCEPTION :
			throw new Exception("SEMANTIC ERROR");
		}

		TYPE parent_cls = null;

		if (this.father_name != null)
		{
			parent_cls = SYMBOL_TABLE.getInstance().find(this.father_name);

			if (parent_cls == null)
			{
				// MENTIONED "PARENT" CLASS DOES NOT EXIST : THROW EXCEPTION
				throw new Exception("SEMANTIC ERROR");
			}

			if  ( ! parent_cls.is_class())
			{
				// MENTIONED "PARENT" IS NOT A CLASS : THROW EXCEPTION
				throw new Exception("SEMANTIC ERROR");
			}
		}

		TYPE_CLASS cls_dec = new TYPE_CLASS((TYPE_CLASS) parent_cls, this.class_name);

		SYMBOL_TABLE.getInstance().beginScope();
		this.cFieldList.SemantMe(cls_dec); // Adds methods to the class
		SYMBOL_TABLE.getInstance().endScope();

		SYMBOL_TABLE.getInstance().enter(this.class_name, cls_dec);
	}
    
}
