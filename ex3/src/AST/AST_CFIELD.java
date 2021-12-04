package AST;

public abstract class AST_CFIELD extends AST_Node {
    public void PrintMe()
	{
		System.out.print("UNKNOWN AST STATEMENT NODE");
	}

	public abstract TYPE SemantMe();
}
