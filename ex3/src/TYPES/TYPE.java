package TYPES;

public abstract class TYPE
{
	/******************************/
	/*  Every type has a name ... */
	/******************************/
	public String name;

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
	public boolean semantically_equals(TYPE other)
	{

		/* --------- CHECK : NIL --> TYPE_ARRAY ---------- */

		if ((other == TYPE_NIL.getInstance()) && (this.getClass() == TYPE_CLASS.class))
		{
			// NIL IS ALWAYS ACCEPTABLE WHEN A CLASS OBJECT IS ACCEPTABLE;

			return true;
		}

		/* --------- CHECK : NIL --> TYPE_CLASS ---------- */

		if ((other == TYPE_NIL.getInstance()) && (this.getClass() == TYPE_ARRAY.class))
		{
			// NIL IS ALWAYS ACCEPTABLE WHEN AN ARRAY OBJECT IS ACCEPTABLE;
			
			return true;
		}

		/* --------- CHECK : | TYPE_CLASS (SON) --> TYPE_CLASS (FATHER) | OR | TYPE(THIS) == OTHER(TYPE) | ---------- */

		if (this != other)
		{
			if ((this.getClass() != TYPE_CLASS.class) || (other.getClass() != TYPE_CLASS.class))
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
