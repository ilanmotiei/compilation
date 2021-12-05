package TYPES;

public abstract class TYPE
{
	/******************************/
	/*  Every type has a name ... */
	/******************************/
	public String type_name;

	/*************/
	/* isClass() */
	/*************/
	public boolean isClass(){ return false;}

	/*************/
	/* isArray() */
	/*************/
	public boolean isArray(){ return false;}

	/****************************************************************/
	/* Checks if the given class is an ancestor class of this class */
	/****************************************************************/
	public boolean is_ancestor(TYPE_CLASS cls) { return false; }

	/****************************************************************/
	/* 	Checks if the given types are semantically equal 
		( i.e. an object from current class can be replaced anywhere by an object from the other class ) */
	/****************************************************************/

	public boolean is_int()
	{
		return this.type_name.equals("int");
	}

	public boolean is_nil()
	{
		return this.type_name.equals("nil");
	}

	public boolean is_string()
	{
		return this.type_name.equals("string");
	}

	public boolean is_void()
	{
		return this.type_name.equals("void");
	}

	public boolean is_function()
	{
		return this.type_name.equals("function");
	}

	public boolean is_class()
	{
		return this.type_name.equals("class");
	}

	public boolean is_array()
	{
		return this.type_name.equals("array");
	}

	public boolean semantically_equals(TYPE other)
	{

		/* --------- CHECK : NIL --> TYPE_ARRAY ---------- */

		if ((other == TYPE_NIL.getInstance()) && (this.getClass() == TYPE_CLASS.class))
		{
			// NIL IS ALWAYS ACCEPTABLE WHEN A CLASS OBJECT IS ACCEPTABLE;

			return true;
		}

		/* --------- CHECK : NIL --> TYPE_CLASS ---------- */

		if ((other.type_name.equals("nil")) && (this.type_name.equals("array")))
		{
			// NIL IS ALWAYS ACCEPTABLE WHEN AN ARRAY OBJECT IS ACCEPTABLE;
			
			return true;
		}

		/* --------- CHECK : | TYPE_CLASS (SON) --> TYPE_CLASS (FATHER) | OR | TYPE(THIS) == OTHER(TYPE) | ---------- */

		if (this != other)
		{
			if (( ! this.type_name.equals("class")) || ( ! other.type_name.equals("class")))
			{
				return false;
			}

			// else : both types are TYPE_CLASS : check if the other class is the son of the current class;

			if (!((TYPE_CLASS) this).is_son((TYPE_CLASS) other))
			{
				return false;
			}
		}

		// else: the two types are semantically or syntactically valid

		return true;
	}
}
