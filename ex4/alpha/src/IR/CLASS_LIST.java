package IR;

import TYPES.*;

import java.util.Iterator;

public class CLASS_LIST implements Iterable<TYPE_CLASS>
{
	public TYPE_CLASS head;
	public CLASS_LIST tail;
	
	public CLASS_LIST(TYPE_CLASS head, CLASS_LIST tail)
	{
		this.head = head;
		this.tail = tail;
	}	

	public Iterator<TYPE_CLASS> iterator(){
		return new CLASS_LIST_ITER();
	}

	public class CLASS_LIST_ITER implements Iterator<TYPE_CLASS>
	{	
		private TYPE_CLASS curr = head;
		private CLASS_LIST_ITER curr_list_tail = tail;
		private boolean end = (head == null);

		public boolean hasNext(){
			return (!end);
		}

		public TYPE_CLASS next(){
			TYPE_CLASS curr_ptr = curr;

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
	public TYPE_CLASS_LIST getLast()
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

	/* Appends the element 'e' to the end of this list of class declerations */
	public void Append(TYPE_CLASS e)
	{
		CLASS_LIST curr = this.tail;
		CLASS_LIST prev = this;

		while (curr != null)
		{
			prev = curr;
			curr = curr.tail;
		}

		CLASS_LIST last = prev;

		last.tail = new CLASS_LIST(e, null);
	}
}
