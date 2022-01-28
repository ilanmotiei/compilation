package AST;

import IR.*;
import SYMBOL_TABLE.*;
import TYPES.*;
import TEMP.*;

public class AST_CFIELD_FUNDEC extends AST_CFIELD 
{
	public AST_FUNCDEC funcDec;
	public int line;

	// metadata for code generation :
	public TYPE_CLASS _cls_;

	//  Class Constructor
	public AST_CFIELD_FUNDEC(AST_FUNCDEC funcDec, int line)
	{
		// SET A UNIQUE SERIAL NUMBER
		SerialNumber = AST_Node_Serial_Number.getFresh();

		// PRINT CORRESPONDING DERIVATION RULE
		System.out.print("====================== cField -> funcDec \n");

		// COPY INPUT DATA NENBERS
		this.funcDec = funcDec;
		this.line = line;
	}

	// The printing message for an assign statement AST node
	public void PrintMe()
	{
		// AST NODE TYPE = AST ASSIGNMENT STATEMENT
		System.out.print("AST NODE CFIELD FUNCDEC\n");  

		
		// RECURSIVELY PRINT VAR + EXP
		if (funcDec != null) funcDec.PrintMe();

		// PRINT Node to AST GRAPHVIZ DOT file
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"CFIELD\n funcDec;\n");
		
		// PRINT Edges to AST GRAPHVIZ DOT file
		if(funcDec != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,funcDec.SerialNumber);
	}

	public void SemantMe(TYPE_CLASS cls) throws Exception{

		TYPE_FUNCTION func = (TYPE_FUNCTION) this.funcDec.SemantMe().type;

		if (cls.function_shadows(func))
		{
			// METHOD SHADOWS AN ANOTHER FUNCTION AT THE CLASS : THROW EXCEPTION
			String cls_name = this.getClass().getName();
			throw new Exception("SEMANTIC ERROR : " + this.line + " : " + cls_name);
		}

		func.cls = cls; // setting function's class

		cls.appendField(new TYPE_CLASS_FIELD(func, func.name));

		this._cls_ = cls;
	}

	public void IRme()
	{
		this.funcDec.IRme(_cls_);
	}
}
