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

	/**********************************************************/
	/*  max amount of local variables defined in the function */
	/**********************************************************/
	public int max_local_var_offset = 0;

	/***************************************/
	/*   the class in which it's defined   */
	/***************************************/
	public TYPE_CLASS cls = null; // may be null if function is global
	
	public TYPE_FUNCTION(TYPE returnType, 
						 String name, 
						 TYPE_LIST params, 
						 TYPE_CLASS cls)
	{
		this.name = name;
		this.returnType = returnType;
		this.params = params;
		this.cls = cls;

		this.type_name = "function";
	}

	public TYPE_FUNCTION(TYPE returnType, 
						 String name, 
						 TYPE_LIST params)
	{
		this.name = name;
		this.returnType = returnType;
		this.params = params;

		this.type_name = "function";
	}

	public boolean AcceptableArgs(TYPE_LIST args_types)
	{
		if (params == null)
		{
			if (args_types == null){
				return true;
			}
			else{
				return false;
			}
		}

		if (args_types == null)
		{
			if (params == null){
				return true;
			}else{
				return false;
			}
		}

		return this.params.semantically_equals(args_types);
	}
}
