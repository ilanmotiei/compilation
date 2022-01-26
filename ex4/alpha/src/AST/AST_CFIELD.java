package AST;

import TYPES.*;

public abstract class AST_CFIELD extends AST_Node {
	
	public void PrintMe()
	{
		System.out.print("UNKNOWN AST STATEMENT NODE");
	}

	public abstract void SemantMe(TYPE_CLASS cls) throws Exception;
	
	public abstract void IRme();
}
