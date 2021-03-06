package AST;

import IR.*;
import SYMBOL_TABLE.*;
import TYPES.*;
import TEMP.*;

public abstract class AST_VARDEC extends AST_Node {

	// metadata for code generation
	public int offset;
	public boolean isArg;
	public boolean isLocalVar;
	public boolean isClassField;

    public void PrintMe()
	{
		System.out.print("UNKNOWN AST VARDEC NODE");
	}
	
	public abstract BOX SemantMe() throws Exception;

	public abstract BOX SemantMe(TYPE_CLASS cls) throws Exception;

	public abstract void IRme();
}
