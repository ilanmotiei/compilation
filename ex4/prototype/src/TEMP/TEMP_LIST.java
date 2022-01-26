package TEMP;

import java.util.Iterator;

import TYPES.TYPE;

public class TEMP_LIST implements Iterable<TEMP> {
	
	public TEMP head;
	public TEMP_LIST tail;
	
	public TEMP_LIST(TEMP head, TEMP_LIST tail) {
		this.head = head;
		this.tail = tail;
	}
	
	public Iterator<TEMP> iterator(){
		return new TEMP_LIST_ITER();
	}

	class TEMP_LIST_ITER implements Iterator<TEMP>
	{
		private TEMP curr = head;
		private TEMP_LIST curr_list_tail = tail;
		private boolean end = false;

		public boolean hasNext(){
			return (!end);
		}

		public TEMP next(){
			TEMP curr_ptr = curr;

			if (curr_list_tail == null){
				end = true;
			}else{
				curr = curr_list_tail.head;
				curr_list_tail = curr_list_tail.tail;
			}

			return curr_ptr;
		}
	}
	
	public int length()
	{
		int len = 0;
		
		for (TEMP t : this)
		{
			len ++;
		}
		
		return len;
	}

}