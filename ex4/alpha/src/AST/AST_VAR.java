package AST;

import IR.*;
import SYMBOL_TABLE.*;
import TYPES.*;
import TEMP.*;

public abstract class AST_VAR extends AST_Node
{
    // metadata for code generation
	public int offset;
	public boolean isArg;
    public boolean isLocalVar;
    public boolean isClassField;

    public abstract BOX SemantMe() throws Exception;

    public abstract BOX SemantMe(TYPE_CLASS cls) throws Exception;

    public abstract TEMP IRme();

    // sets the variable value to that stored at the given temporary
    public abstract void set(TEMP value, TYPE value_type);
}
