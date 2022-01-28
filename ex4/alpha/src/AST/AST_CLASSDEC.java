package AST;

import IR.*;
import SYMBOL_TABLE.*;
import TYPES.*;
import TEMP.*;

public class AST_CLASSDEC extends AST_Node {
    public String class_name;
	public String father_name;
	public AST_CFIELD_LIST cFieldList;
	public int line;

	// results from semantic analysis for codegeneration:
	public TYPE_CLASS cls;
	
	// Class Constructor
	public AST_CLASSDEC(String class_name, String father_name, AST_CFIELD_LIST cFieldList, int line)
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
		this.line = line;
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
			String cls_name = this.getClass().getName();
			throw new Exception("SEMANTIC ERROR : " + this.line + " : " + cls_name);
		}

		if (SYMBOL_TABLE.getInstance().find(this.class_name) != null)
		{
			// AN ANOTHER OBJECT WITH THIS NAME WAS ALREADY BEEN DECLARED : THROW EXCEPTION :
			String cls_name = this.getClass().getName();
			throw new Exception("SEMANTIC ERROR : " + this.line + " : " + cls_name);
		}

		TYPE parent_cls = null;

		if (this.father_name != null)
		{
			parent_cls = SYMBOL_TABLE.getInstance().find(this.father_name);

			if (parent_cls == null)
			{
				// MENTIONED "PARENT" CLASS DOES NOT EXIST : THROW EXCEPTION
				String cls_name = this.getClass().getName();
				throw new Exception("SEMANTIC ERROR : " + this.line + " : " + cls_name);
			}

			if  ( ! parent_cls.is_class())
			{
				// MENTIONED "PARENT" IS NOT A CLASS : THROW EXCEPTION
				String cls_name = this.getClass().getName();
				throw new Exception("SEMANTIC ERROR : " + this.line + " : " + cls_name);
			}
		}

		TYPE_CLASS cls_dec = new TYPE_CLASS((TYPE_CLASS) parent_cls, this.class_name);
		SYMBOL_TABLE.getInstance().enter(this.class_name, cls_dec, false, false, false);

		SYMBOL_TABLE.getInstance().beginScope(cls_dec);
		SYMBOL_TABLE.getInstance().enter(this.class_name, cls_dec, false, false, false);
		this.cFieldList.SemantMe(cls_dec); // Adds methods to the class
		SYMBOL_TABLE.getInstance().endScope();

		this.cls = cls_dec;

		SYMBOL_TABLE.getInstance().enter(this.class_name, cls_dec, false, false, false);
	}

	public void IRme()
	{
		IR.
		getInstance().
		Add_IRcommand(new IRcommand_ClassDec(cls));

		// ^ : adds a class decleration. 
		// all the class fields and methods identifiers and initial values
		// were inferred at the semantic analysis.

		this.cFieldList.IRme();
	}   
}
