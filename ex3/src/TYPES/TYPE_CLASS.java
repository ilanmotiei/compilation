package TYPES;

public class TYPE_CLASS extends TYPE
{

	/* Class Name */
	public String name;

	/*********************************************************************/
	/* If this class does not extend a father class this should be null  */
	/*********************************************************************/
	public TYPE_CLASS father;

	/**************************************************/
	/* Gather up all data members in one place        */
	/* Note that data members coming from the AST are */
	/* packed together with the class methods         */
	/**************************************************/
	public TYPE_CLASS_FIELD_LIST data_members;
	
	/****************/
	/* CTROR(S) ... */
	/****************/
	public TYPE_CLASS(TYPE_CLASS father, String name, TYPE_CLASS_FIELD_LIST data_members)
	{
		this.name = name;
		this.father = father;
		this.data_members = data_members;

		this.type_name = "class";
	}

	public TYPE_CLASS(TYPE_CLASS father, String name)
	{
		this.name = name;
		this.father = father;

		this.type_name = "class";
	}

	/******************************************************************/
	/*  Checks if the given class is an ancestor class of this class  */
	/******************************************************************/
	public boolean is_ancestor(TYPE_CLASS cls)
	{
		TYPE_CLASS curr = this.father;

		while (curr != null)
		{
			if (curr == cls)
			{
				return true;
			}
			// else
			curr = curr.father;
		}

		// all parent classes are not the given one;

		return false;
	}

	/*****************************************************************/
	/* Checks if the given class is an inherited class of this class */
	/*****************************************************************/

	public boolean is_son(TYPE_CLASS cls)
	{
		return cls.is_ancestor(this); // equals for checking if this class is an ancestor of the given class
	}


	/*****************************************************************/
	/* 	    	Finds a field of the class with the given name       */
	/*****************************************************************/

	/* When the user queries ${Var}.field_name, when var is an instance of this class (i.e. type(Var)==${this_class})
	 - this method gives him that variable decleration, or returns null if didn't found it */

	public TYPE_CLASS_FIELD get_field(String field_name)
	{
		TYPE_CLASS curr_cls = this;

		while (curr_cls != null)
		{
			/*
			for (TYPE_CLASS_FIELD f : curr_cls.data_members)
			{
				if (f.name.equals(field_name)) { return f; }
			}

			curr_cls = curr_cls.father; 
			*/

			TYPE_CLASS_FIELD_LIST curr = curr_cls.data_members;
			while (curr != null)
			{
				TYPE_CLASS_FIELD f = curr.head;
				if (f.name.equals(field_name)) { return f; }
				curr = curr.tail;
			}

			curr_cls = curr_cls.father;
		}

		// Didn't found that field at ${thisVar}'s class;
		return null;
	}

	// Does the same as the above function, but returns the type of the searched field name. Returns null if wasn't found.

	public TYPE get_field_type(String field_name)
	{
		TYPE_CLASS_FIELD field = this.get_field(field_name);

		if (field != null)
		{
			return field.type;
		}

		return null;
	}


	/* Adds a new field to the class */
	public void appendField(TYPE_CLASS_FIELD newField)
	{
		if (this.data_members == null)
		{
			this.data_members = new TYPE_CLASS_FIELD_LIST(newField, null);
		}
		else
		{
			this.data_members.Append(newField);
		}
	}


	/* Checks if the given function shadows currently defined field of the class */
	public boolean function_shadows(TYPE_FUNCTION func)
	{
		if (this.data_members != null)
		{
			for (TYPE_CLASS_FIELD f : this.data_members)
			{
				if (f.name.equals(func.name))
				{
					return true;
				}
			}
		}

		TYPE_CLASS curr_cls = this.father;

		while ((curr_cls != null) && (curr_cls.data_members != null))
		{
			for (TYPE_CLASS_FIELD f : curr_cls.data_members)
			{
				if (f.name.equals(func.name))
				{
					if ( ! f.is_function())
					{
						// METHOD'S NAME SHADOWES A VARIABLE
						return true;
					}
					// else

					if ( ! ((TYPE_FUNCTION) f.type).returnType.semantically_equals(func.returnType))
					{
						return true;
					}

					// else

					if ( ! ((TYPE_FUNCTION) f.type).AcceptableArgs(func.params))
					{
						return true;
					}

					// else : we are legally overriding an inherited class method
				}
			}

			curr_cls = curr_cls.father;
		}

		return false;
	}

	/*************/
	/* isClass() */
	/*************/
	public boolean isClass() { return true; }

}
