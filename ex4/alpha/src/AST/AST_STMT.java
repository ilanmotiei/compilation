package AST;

import IR.*;
import SYMBOL_TABLE.*;
import TYPES.*;
import TEMP.*;

public abstract class AST_STMT extends AST_Node
{
	// The default message for an unknown AST statement node
	public void PrintMe()
	{
		System.out.print("UNKNOWN AST STATEMENT NODE");
	}

	public abstract void SemantMe() throws Exception;

	public abstract void IRme();
}
