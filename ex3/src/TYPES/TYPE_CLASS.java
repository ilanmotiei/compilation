package TYPES;

public class TYPE_CLASS extends TYPE
{
	/*********************************************************************/
	/* If this class does not extend a father class this should be null  */
	/*********************************************************************/
	public TYPE_CLASS father;

	/**************************************************/
	/* Gather up all data members in one place        */
	/* Note that data members coming from the AST are */
	/* packed together with the class methods         */
	/**************************************************/
	public TYPE_LIST data_members;
	
	/****************/
	/* CTROR(S) ... */
	/****************/
	public TYPE_CLASS(TYPE_CLASS father, String name, TYPE_LIST data_members)
	{
		this.name = name;
		this.father = father;
		this.data_members = data_members;
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
	/* 	    	Finds a method of the class with the given name      */
	/*****************************************************************/

	public TYPE_FUNCTION find_Method(String method_name)
	{
		for (TYPE dec : this.data_members)
		{
			if (dec.getClass() == TYPE_FUNCTION.class)
			{
				if (((TYPE_FUNCTION) dec).name == method_name)
				{
					return (TYPE_FUNCTION) dec;
				}
			}
		}

		// didn't found a method with the given name

		return null;
	}

	
	/*************/
	/* isClass() */
	/*************/
	public boolean isClass() { return true; }

}
