package AST;

import TYPES.*;

public abstract class AST_VAR extends AST_Node
{
    public abstract BOX SemantMe() throws Exception;

    public abstract BOX SemantMe(TYPE_CLASS cls) throws Exception;
}
