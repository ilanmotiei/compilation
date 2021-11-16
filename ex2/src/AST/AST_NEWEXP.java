package AST;

public class AST_NEWEXP extends AST_Node {
	public String name;
    public AST_EXP exp;

    public AST_NEWEXP(String name, AST_EXP exp) {
        // SET A UNIQUE SERIAL NUMBER
		SerialNumber = AST_Node_Serial_Number.getFresh();

		// PRINT CORRESPONDING DERIVATION RULE
		if(exp == null)
			System.out.format("====================== newExp -> NEW ID(%s)\n", name);
		else
			System.out.format("====================== newExp -> NEW ID(%s) LBRACK exp RBRACK\n", name);

		// COPY INPUT DATA NENBERS
		this.name = name;
		this.exp = exp;
    }

    public void PrintMe()
	{
		// AST NODE TYPE = AST ASSIGNMENT STATEMENT
		System.out.print("AST NODE NEWEXP\n");
        
		
		// RECURSIVELY PRINT VAR + EXP
		if (exp != null) exp.PrintMe();

		// PRINT Node to AST GRAPHVIZ DOT file
		if(exp == null){
			AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("newExp\n NEW ID(%s) \n", name));
		}
		else{
			AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("newExp\n NEW ID(%s)[exp] \n", name));
		}
		
		
		// PRINT Edges to AST GRAPHVIZ DOT file
		if(exp != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,exp.SerialNumber);
	}
    
}
