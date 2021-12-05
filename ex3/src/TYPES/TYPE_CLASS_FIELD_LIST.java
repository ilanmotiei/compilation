package TYPES;

import java.util.Iterator;

public class TYPE_CLASS_FIELD_LIST implements Iterable<TYPE_CLASS_FIELD>
{
	public TYPE_CLASS_FIELD head;
	public TYPE_CLASS_FIELD_LIST tail;
	
	public TYPE_CLASS_FIELD_LIST(TYPE_CLASS_FIELD head, TYPE_CLASS_FIELD_LIST tail)
	{
		this.head = head;
		this.tail = tail;
	}	

	public Iterator<TYPE_CLASS_FIELD> iterator(){
		return new TYPE_CLASS_FIELD_ITER();
	}

	class TYPE_CLASS_FIELD_ITER implements Iterator<TYPE_CLASS_FIELD>
	{	
		private TYPE_CLASS_FIELD curr = head;
		private TYPE_CLASS_FIELD_LIST curr_list_tail = tail;
		private boolean end = false;

		public boolean hasNext(){
			return (!end);
		}

		public TYPE_CLASS_FIELD next(){
			TYPE_CLASS_FIELD curr_ptr = curr;

			if (curr_list_tail == null){
				end = true;
			}
			else
			{
				curr = curr_list_tail.head;
				curr_list_tail = curr_list_tail.tail;
			}

			return curr_ptr;
		}
	}

	/* Returns the last element at the list */
	public TYPE_CLASS_FIELD getLast()
	{	
		TYPE_CLASS_FIELD_LIST curr = this.tail;
		TYPE_CLASS_FIELD prev = this.head;

		while (curr != null)
		{
			prev = curr.head;
			curr = curr.tail;
		}

		return prev;
	}

	/* Appends the element 'e' to the end of this list of fields */
	public void Append(TYPE_CLASS_FIELD e)
	{
		TYPE_CLASS_FIELD_LIST curr = this.tail;
		TYPE_CLASS_FIELD_LIST prev = this;

		while (curr != null)
		{
			prev = curr;
			curr = curr.tail;
		}

		TYPE_CLASS_FIELD_LIST last = prev;

		last.tail = new TYPE_CLASS_FIELD_LIST(e, null);
	}
}
