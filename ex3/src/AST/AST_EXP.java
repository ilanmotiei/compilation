package AST;

import TYPES.*;

public abstract class AST_EXP extends AST_Node
{	
	public abstract BOX SemantMe() throws Exception;
}