package TYPES;

import java.util.Iterator;

public class TYPE_LIST implements Iterable<TYPE>
{
	/****************/
	/* DATA MEMBERS */
	/****************/
	public TYPE head;
	public TYPE_LIST tail;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public TYPE_LIST(TYPE head, TYPE_LIST tail)
	{
		this.head = head;
		this.tail = tail;
	}

	public Iterator<TYPE> iterator(){
		return new TYPE_LIST_ITER();
	}

	class TYPE_LIST_ITER implements Iterator<TYPE>{
		private TYPE curr = head;
		private TYPE_LIST curr_list_tail = tail;
		private boolean end = false;

		public boolean hasNext(){
			return (!end);
		}

		public TYPE next(){
			TYPE curr_ptr = curr;

			if (curr_list_tail == null){
				end = true;
			}else{
				curr = curr_list_tail.head;
				curr_list_tail = curr_list_tail.tail;
			}

			return curr_ptr;
		}
	}

	/* -------------- CHECKS IF THE TWO GIVEN LISTS ARE SYNTACTICALLY (EXACTLY) EQUAL -------------- */
	public boolean equals(TYPE_LIST other){
		if (this.head != other.head)
		{
			return false;
		}

		return this.tail.equals(other.tail);
	}

	/*
	-----------------------------------------------------------------------------------------------------------------------
								CHECKS IF THE TWO GIVEN LISTS ARE SEMANTICALLY EQUAL : 
	i.e. each type in other is a son of the corresponding item in the current list, or is the same as the corrsponding item
	-----------------------------------------------------------------------------------------------------------------------
	*/
	public boolean semantically_equals(TYPE_LIST other){
		if (!this.head.semantically_equals(other.head))
		{
			return false;
		}

		return this.tail.semantically_equals(other.tail);
	}
}
