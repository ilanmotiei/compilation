package AST;

public abstract class AST_VARDEC extends AST_Node {
    public void PrintMe()
	{
		System.out.print("UNKNOWN AST VARDEC NODE");
	}
	
	public abstract void SemantMe() throws Exception;

	public abstract void SemantMe(boolean const_assignment) throws Exception;
}
