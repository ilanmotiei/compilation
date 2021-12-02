package AST;

public class AST_DEC extends AST_Node {
    public AST_Node son;
	public int i; // Indicates which from  varDec | funcDec | classDec | arrayTypedef

    public AST_DEC(AST_Node son, int i) {
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

	public TYPE SemantMe(){
		return son.SemantMe();
	}
}
