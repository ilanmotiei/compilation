package AST;

import TYPES.*;

public abstract class AST_Node
{
	/*******************************************/
	/* The serial number is for debug purposes */
	/* In particular, it can help in creating  */
	/* a graphviz dot format of the AST ...    */
	/*******************************************/
	public int SerialNumber;
	
	
	// The default message for an unknown AST node
	public void PrintMe()
	{
		System.out.print("AST NODE UNKNOWN\n");
	}

	
	/*
	 
	The default behavior when semantic an AST node
	public TYPE SemantMe()
	{
		return null;
	}
	*/

	
}
