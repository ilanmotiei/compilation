package TYPES;


/*******************/
/* PROJECT IMPORTS */
/*******************/

public class TYPES
{
	private TYPE_LIST type_list = null;
	
	public void Add_Type(TYPE new_type) 
	{
		if (type_list == null) 
		{
			type_list = new TYPE_LIST(new_type, null);
		}
		else 
		{
			TYPE_LIST curr = type_list;
			TYPE_LIST prev = null;
			
			while (curr != null)
			{
				prev = curr;
				curr = curr.tail;
			}
			prev.tail = new TYPE_LIST(new_type, null);
		}
	}

	/**************************************/
	/* USUAL SINGLETON IMPLEMENTATION ... */
	/**************************************/
	private static TYPES instance = null;

	/*****************************/
	/* PREVENT INSTANTIATION ... */
	/*****************************/
	protected TYPES() {}

	/******************************/
	/* GET SINGLETON INSTANCE ... */
	/******************************/
	public static TYPES getInstance()
	{
		if (instance == null)
		{
			/*******************************/
			/* [0] The instance itself ... */
			/*******************************/
			instance = new TYPES();
		}
		return instance;
	}
}