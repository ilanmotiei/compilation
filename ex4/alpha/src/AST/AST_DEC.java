package AST;

import IR.*;
import SYMBOL_TABLE.*;
import TYPES.*;
import TEMP.*;

public class AST_DEC extends AST_Node {
    public AST_Node son;
	public int i; // Indicates which from  varDec | funcDec | classDec | arrayTypedef
	public int line;
	
    public AST_DEC(AST_Node son, int i, int line) {
        // SET A UNIQUE SERIAL NUMBER
		SerialNumber = AST_Node_Serial_Number.getFresh();

		// PRINT CORRESPONDING DERIVATION RULE
		if(i==0) System.out.print("====================== dec -> varDec\n");
		if(i==1) System.out.print("====================== dec -> funcDec\n");
		if(i==2) System.out.print("====================== dec -> classDec\n");
		if(i==3) System.out.print("====================== dec -> arrayTypdef\n");

		// COPY INPUT DATA NENBERS
		this.son = son;
		this.i = i;
		this.line = line;
    }

    public void PrintMe()
	{
		// AST NODE TYPE = AST ASSIGNMENT STATEMENT
		System.out.print("AST NODE dec\n");

		
		// RECURSIVELY PRINT VAR + EXP
		if (son != null) son.PrintMe();

		// PRINT Node to AST GRAPHVIZ DOT file
		if(i==0){
			AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"varDec\n");
		}
		if(i==1){
			AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"funcDec\n");
		}
		if(i==2){
			AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"classDec\n");
		}
		if(i==3){
			AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"arrayTypedef\n");
		}

		// PRINT Edges to AST GRAPHVIZ DOT file
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,son.SerialNumber);
	}

	public void SemantMe() throws Exception{

		switch(i){
			case 0:
				((AST_VARDEC) son).SemantMe();
				break;
			case 1:
				((AST_FUNCDEC) son).SemantMe();
				break;
			case 2:
				((AST_CLASSDEC) son).SemantMe();
				break;
			case 3:
				((AST_ARRAYTYPEDEF) son).SemantMe();
		}		
	}

	public void IRme()
	{
		switch(i){
			case 0:
				((AST_VARDEC) son).IRme();
				break;
			case 1:
				((AST_FUNCDEC) son).IRme();
				break;
			case 2:
				((AST_CLASSDEC) son).IRme();
				break;
			case 3:
				// we don't need to do anything when an array is defined. 
				// we only have to care when an array is allocated.
				// ((AST_ARRAYTYPEDEF) son).IRme();
		}	
	}
}
