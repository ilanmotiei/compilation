package AST;
import TYPES.*;

public abstract class AST_VARDEC extends AST_Node {

	// metadata for code generation
	public int offset;
	public boolean isArg;
	public boolean isLocalVar;

    public void PrintMe()
	{
		System.out.print("UNKNOWN AST VARDEC NODE");
	}
	
	public abstract BOX SemantMe() throws Exception;

	public abstract BOX SemantMe(TYPE_CLASS cls) throws Exception;

	public abstract void IRme();
}
