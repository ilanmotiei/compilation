package TYPES;

public class TYPE_CLASS_FIELD extends TYPE
{
	public TYPE type; // The type of the FIELD
	public String name; // The name of the FIELD

	public Object initial_value = null;
	// Optional. May contain fields initial value if known at compilation time
	
	public TYPE_CLASS_FIELD(TYPE type, String name, Object init_val)
	{
		this.type = type;
		this.name = name;
		this.initial_value = init_val;

		this.type_name = "class_field";
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
