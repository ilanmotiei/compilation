package AST;
import TYPES.*;

public abstract class AST_VARDEC extends AST_Node {
    public void PrintMe()
	{
		System.out.print("UNKNOWN AST VARDEC NODE");
	}

//	public TEMP IRme()
//	{
//		IR.getInstance().Add_IRcommand(new IRcommand_Allocate(name));
//
//		if (initialValue != null)
//		{
//			IR.getInstance().Add_IRcommand(new IRcommand_Store(name,initialValue.IRme()));
//		}
//		return null;
//	}
	
	public abstract BOX SemantMe() throws Exception;

	public abstract BOX SemantMe(TYPE_CLASS cls) throws Exception;
}
