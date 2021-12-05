package AST;

import TYPES.*;

public abstract class AST_VAR extends AST_Node
{
    public abstract TYPE SemantMe() throws Exception;

    public abstract TYPE SemantMe(TYPE_CLASS cls) throws Exception;
}
