package TYPES;

public class TYPE_CLASS_FIELD extends TYPE
{
	public TYPE type; // The type of the FIELD
	public String name; // The name of the FIELD
	public TYPE_CLASS initialClass; // The first class that this field is declared at

	public Object initial_value = null; // optional - an initial value for the field

	public TYPE_CLASS_FIELD(TYPE type, String name, Object initial_value, TYPE_CLASS initialClass)
	{
		this.type = type;
		this.name = name;
		this.initial_value = initial_value;
		this.initialClass = initialClass;

		this.type_name = "class_field";
	}

	public TYPE_CLASS_FIELD(TYPE type, String name, TYPE_CLASS initialClass)
	{
		this.type = type;
		this.name = name;
		this.initialClass = initialClass;

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

	// The first class at the hierarchy that this field is declared at
	public TYPE_CLASS getInitialClass()
	{
		return initialClass;
	}

}
