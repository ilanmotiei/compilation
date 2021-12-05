package TYPES;

/* GLOBAL FUNCTION TYPE */

public class TYPE_FUNCTION extends TYPE
{
	/***********************************/
	/* The return type of the function */
	/***********************************/
	public TYPE returnType;

	/*************************/
	/* types of input params */
	/*************************/
	public TYPE_LIST params;

	/*************************/
	/*  name of the function */
	/*************************/
	public String name;
	
	public TYPE_FUNCTION(TYPE returnType, String name, TYPE_LIST params)
	{
		this.name = name;
		this.returnType = returnType;
		this.params = params;
		this.type_name = "function";
	}

	public boolean AcceptableArgs(TYPE_LIST args_types)
	{
		return this.params.semantically_equals(args_types);
	}
}
