package TYPES;

public class TYPE_CLASS_FIELD extends TYPE
{
	public TYPE type; // The type of the FIELD
	public String name; // The name of the FIELD
	
	public TYPE_CLASS_FIELD(TYPE type, String name)
	{
		this.type = type;
		this.name = name;
	}
	
	// Is this a variable FIELD
	public boolean is_var()
	{
		return ( type.is_int() || type.is_string() || type.is_class() || type.is_array() );
	}

	// Is this a method
	public boolean is_function()
	{
		return this.type.is_function();
	}

}
