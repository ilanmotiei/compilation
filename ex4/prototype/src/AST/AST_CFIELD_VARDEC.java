package AST;

import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.*;
import IR.*;
import TEMP.*;

public class AST_CFIELD_VARDEC extends AST_CFIELD {
	public AST_VARDEC varDec;
	public int line;

	//  Class Constructor
	public AST_CFIELD_VARDEC(AST_VARDEC varDec, int line)
	{
		// SET A UNIQUE SERIAL NUMBER
		SerialNumber = AST_Node_Serial_Number.getFresh();

		// PRINT CORRESPONDING DERIVATION RULE
		System.out.print("====================== cField -> varDec \n");

		// COPY INPUT DATA NENBERS
		this.varDec = varDec;
		this.line = line;
	}

	
	// The printing message for an assign statement AST node
	public void PrintMe()
	{
		// AST NODE TYPE = AST ASSIGNMENT STATEMENT
		System.out.print("AST NODE CFIELD VARDEC\n");

		
		// RECURSIVELY PRINT VAR + EXP
		if (varDec != null) varDec.PrintMe();

		// PRINT Node to AST GRAPHVIZ DOT file
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"CFIELD\n varDec;\n");
		
		// PRINT Edges to AST GRAPHVIZ DOT file
		if(varDec != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,varDec.SerialNumber);
	}

	public void SemantMe(TYPE_CLASS cls) throws Exception
	{
		BOX var_box = this.varDec.SemantMe(cls);
		TYPE_CLASS_FIELD var = (TYPE_CLASS_FIELD) var_box.type;
		// The above call checks also if no shadowing occured, and throws an error if it does

		/*
		if ( ! var_box.is_const)
		{
			// CANNO'T ASSIGN NON-CONSTANT VARIABLE FOR A CLASS FIELD : THROW EXCEPTION
			String cls_name = this.getClass().getName();
			throw new Exception("SEMANTIC ERROR : " + this.line + " : " + cls_name);
		}
		*/

		cls.appendField(new TYPE_CLASS_FIELD(var.type, var.name, var_box.initial_value));
	}	

	// for compilation reasons only;
	public void IRMe()
	{
		// do nothing. field was added to class at the semantic analysis.
	}

	// for compilation reasons only;
	public void IRMe(TYPE_CLASS cls)
	{
		// do nothing. field was added to class at the semantic analysis.
	}

}
