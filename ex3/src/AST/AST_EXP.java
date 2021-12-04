package AST;

import TYPES.TYPE;

public abstract class AST_EXP extends AST_Node
{	
	public abstract TYPE SemantMe() throws Exception;
}