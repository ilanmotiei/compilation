package AST;

public class AST_STMT_WHILE extends AST_STMT
{
	public AST_EXP cond;
	public AST_STMT_LIST body;

	// Class Constructor
	public AST_STMT_WHILE(AST_EXP cond,AST_STMT_LIST body)
	{
		this.cond = cond;
		this.body = body;
	}
}