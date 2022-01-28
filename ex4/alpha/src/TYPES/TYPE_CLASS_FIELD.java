package TYPES;

public class TYPE_CLASS_FIELD extends TYPE
{
	public TYPE type; // The type of the FIELD
	public String name; // The name of the FIELD

	public Object initial_value = null; // optional - an initial value for the field

	public TYPE_CLASS_FIELD(TYPE type, String name, Object initial_value)
	{
		this.type = type;
		this.name = name;
		this.initial_value = initial_value;

		this.type_name = "class_field";
	}

	public TYPE_CLASS_FIELD(TYPE type, String name)
	{
		this.type = type;
		this.name = name;

		this.type_name = "class_field";
	}

	public boolean hasInitialValue()
	{
		return (initial_value != null);
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
