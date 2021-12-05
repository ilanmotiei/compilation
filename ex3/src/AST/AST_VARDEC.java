package AST;
import TYPES.*;

public abstract class AST_VARDEC extends AST_Node {
    public void PrintMe()
	{
		System.out.print("UNKNOWN AST VARDEC NODE");
	}
	
	public abstract TYPE SemantMe() throws Exception;

	public abstract TYPE SemantMe(TYPE_CLASS cls) throws Exception;
}
