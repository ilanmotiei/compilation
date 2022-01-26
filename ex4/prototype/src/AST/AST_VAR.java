package AST;

import TYPES.*;
import TEMP.TEMP;

public abstract class AST_VAR extends AST_Node
{
    public abstract BOX SemantMe() throws Exception;

    public abstract BOX SemantMe(TYPE_CLASS cls) throws Exception;
    
    public abstract TEMP IRMe();
}
