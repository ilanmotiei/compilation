package AST;

import IR.*;
import SYMBOL_TABLE.*;
import TYPES.*;
import TEMP.*;

public abstract class AST_CFIELD extends AST_Node {
	
	public void PrintMe()
	{
		System.out.print("UNKNOWN AST STATEMENT NODE");
	}

	public abstract void SemantMe(TYPE_CLASS cls) throws Exception;
	
	public abstract void IRme(TYPE_CLASS cls);
}
