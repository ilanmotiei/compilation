package TYPES;
import java.util.*;

import sun.net.www.content.text.plain;

public class TYPE_CLASS extends TYPE
{

	/* Class Name */
	public String name;

	/*********************************************************************/
	/* If this class does not extend a father class this should be null  */
	/*********************************************************************/
	public TYPE_CLASS father;

	public Linkedlist<TYPE_CLASS> sons = null;

	/**************************************************/
	/* Gather up all data members in one place        */
	/* Note that data members coming from the AST are */
	/* packed together with the class methods         */
	/**************************************************/
	public TYPE_CLASS_FIELD_LIST data_members;
	int var_field_cnt = 0; // a counter for variable fields of the class
	
	/****************/
	/* CTROR(S) ... */
	/****************/
	public TYPE_CLASS(TYPE_CLASS father, String name, TYPE_CLASS_FIELD_LIST data_members)
	{
		this.name = name;
		this.father = father;

		if (this.father != null)
		{
			this.father.AddSon(this);
			this.var_field_cnt = father.var_field_cnt;
		}

		this.data_members = data_members;
		this.type_name = "class";
	}

	public TYPE_CLASS(TYPE_CLASS father, String name)
	{
		this.name = name;
		this.father = father;
		
		if (this.father != null)
		{
			this.father.AddSon(this);
			this.var_field_cnt = father.var_field_cnt;
		}

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
		if (newField.is_var())
		{
			var_field_cnt ++;
		}

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

	public LinkedList<String> getMethodsNames()
	{
		LinkedList<String> methods_names = new LinkedList<>();

		TYPE_CLASS curr_cls = this;

		while (curr_cls != null)
		{
			TYPE_CLASS_FIELD_LIST curr = curr_cls.data_members;

			while (curr != null)
			{
				TYPE_CLASS_FIELD f = curr.head;

				if (f.is_function()){
					if (! methods_names.contains(f.name))
					{
						// add it to the methods names in a reverse order
						methods_names.addFirst(f.name);
					}
				}

				curr = curr.tail;
			}

			curr_cls = curr_cls.father;
		}

		return methods_names;
	}

	public int getMethodNumber(String method_name)
	{
		LinkedList<String> methods_names = getMethodsNames();

		return methods_names.indexOf(method_name);
	}

	public void AddSon(TYPE_CLASS son)
	{
		if (this.sons == null)
		{
			this.sons = new LinkedList<TYPE_CLASS>();
		}

		this.sons.add(son);
	}

	// get class fields (without methods), including those inherited.
	public LinekedList<TYPE_CLASS_FIELD> getClassFields()
	{
		LinkedList<TYPE_CLASS_FIELD> class_fields = new LinkedList<>();

		TYPE_CLASS curr_cls = this;

		while (curr_cls != null)
		{
			TYPE_CLASS_FIELD_LIST curr = curr_cls.data_members;

			while (curr != null)
			{
				TYPE_CLASS_FIELD f = curr.head;

				if (f.is_var()){
					class_fields.addFirst(f);
				}

				curr = curr.tail;
			}

			curr_cls = curr_cls.father;
		}

		return class_fields;
	}

	// get class given field name's index 
	// (among all class fields - including inherited ones)
	public int getFieldIndex(String field_name)
	{
		int idx = this.var_field_cnt - 1;

		TYPE_CLASS curr_cls = this;

		while (curr_cls != null)
		{
			TYPE_CLASS_FIELD_LIST curr = curr_cls.data_members;

			while (curr != null)
			{
				TYPE_CLASS_FIELD f = curr.head;

				if (f.name.equals(field_name)){
					return idx;
				}

				curr = curr.tail;
				idx --;
			}

			curr_cls = curr_cls.father;
		}

		return -1; // for error if field name wasn't found
	}

	// For memory allocation
	public int getFieldAmount()
	{
		return this.var_field_cnt;
	}

	// Get the most bottom class at the hierachy that implements the method
	// with the given name
	public TYPE_CLASS getMethodImplementor(String method_name)
	{
		TYPE_CLASS curr_cls = this;

		while (curr_cls != null)
		{
			TYPE_CLASS_FIELD_LIST curr = curr_cls.data_members;

			while (curr != null)
			{
				TYPE_CLASS_FIELD f = curr.head;

				if (f.is_function() && f.name.equals(method_name)){
					return curr_cls;
				}

				curr = curr.tail;
			}

			curr_cls = curr_cls.father;
		}

		return null; // for error if method name wasn't found
	}

	public LinkedList<TYPE_CLASS_FIELD> getClassNonInheritedFields()
	{
		LinkedList<TYPE_CLASS_FIELD> class_non_inherited_fields = new LinkedList<>();

		for (TYPE_CLASS_FIELD f : this.data_members)
		{
			if (f.is_var())
				{ class_non_inherited_fields.addFirst(f); }
		}

		return class_non_inherited_fields;
	}

	/*************/
	/* isClass() */
	/*************/
	public boolean isClass() { return true; }

	public LinekedList<TYPE_CLASS_FIELD> getFieldsAmount()
	{

		TYPE_CLASS curr_cls = this;
		int count = 0;

		while (curr_cls != null)
		{
			TYPE_CLASS_FIELD_LIST curr = curr_cls.data_members;

			while (curr != null)
			{
				TYPE_CLASS_FIELD f = curr.head;

				if (f.is_var()){
					count++;
				}

				curr = curr.tail;
			}

			curr_cls = curr_cls.father;
		}

		return count;
	}
}
