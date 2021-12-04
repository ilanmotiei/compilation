package TYPES;

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

	/*********************************************************************************/
	/* The type of the class the function is defined at. null if defined at no class */
	/*********************************************************************************/
	public TYPE_CLASS cls;
	
	/****************/
	/* CTROR(S) ... */
	/****************/
	public TYPE_FUNCTION(TYPE returnType, String name, TYPE_LIST params)
	{
		this.name = name;
		this.returnType = returnType;
		this.params = params;
	}

	public TYPE_FUNCTION(TYPE returnType, String name, TYPE_LIST params, TYPE_CLASS cls)
	{
		this.name = name;
		this.returnType = returnType;
		this.params = params;
		this.cls = cls;
	}
}
