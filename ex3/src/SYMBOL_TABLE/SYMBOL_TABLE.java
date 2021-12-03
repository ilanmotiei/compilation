/***********/
/* PACKAGE */
/***********/
package SYMBOL_TABLE;

/*******************/
/* GENERAL IMPORTS */
/*******************/
import java.io.PrintWriter;

/*******************/
/* PROJECT IMPORTS */
/*******************/
import TYPES.*;


public class SYMBOL_TABLE
{
	private int hashArraySize = 13;
	
	/**********************************************/
	/* The actual symbol table data structure ... */
	/**********************************************/
	private SYMBOL_TABLE_ENTRY[] table = new SYMBOL_TABLE_ENTRY[hashArraySize];
	private SYMBOL_TABLE_ENTRY top = null; // The last entry which was inserted to the symbol table
	private int top_index = 0;
	
	/**************************************************************/
	/* A very primitive hash function for exposition purposes ... */
	/**************************************************************/
	private int hash(String s)
	{
		if (s.charAt(0) == 'l') {return 1;}
		if (s.charAt(0) == 'm') {return 1;}
		if (s.charAt(0) == 'r') {return 3;}
		if (s.charAt(0) == 'i') {return 6;}
		if (s.charAt(0) == 'd') {return 6;}
		if (s.charAt(0) == 'k') {return 6;}
		if (s.charAt(0) == 'f') {return 6;}
		if (s.charAt(0) == 'S') {return 6;}
		return 12;
	}

	/****************************************************************************/
	/* Enter a variable, function, class type or array type to the symbol table */
	/****************************************************************************/
	public void enter(String name, TYPE t)
	{
		/* [1] Compute the hash value for this new entry */

		int hashValue = hash(name);

		/* [2] Extract what will eventually be the next entry in the hashed position */
		/* NOTE: this entry can very well be null, but the behaviour is identical */

		SYMBOL_TABLE_ENTRY next = table[hashValue];
	
		/* [3] Prepare a new symbol table entry with name, type, next and prevtop */
		SYMBOL_TABLE_ENTRY e = new SYMBOL_TABLE_ENTRY(name, t, hashValue, next, top, top_index++);

		/* [4] Update the top of the symbol table ... */
		top = e;
		
		/* [5] Enter the new entry to the table */
		table[hashValue] = e;
		
		/* [6] Print Symbol Table */
		PrintMe();
	}

	/***********************************************/
	/* Find the inner-most scope element with name */
	/***********************************************/
	public TYPE find(String name)
	{
		SYMBOL_TABLE_ENTRY e;
				
		for (e = table[hash(name)]; e != null; e = e.next)
		{
			if (name.equals(e.name))
			{
				return e.type;
			}
		}

		// didn't found that name
		
		return null;
	}

	/*************************************************/
	/* Finds all of the elements with the given name */
	/*************************************************/
	public TYPE_LIST find_all(String name){
		return find_all_rec(name, this.table[hash(name)]);
	}

	public TYPE_LIST find_all_rec(String name, SYMBOL_TABLE_ENTRY curr)
	{
		for (SYMBOL_TABLE_ENTRY e = curr; e != null; e = e.next)
		{
			if (name.equals(e.name))
			{
				return new TYPE_LIST(e.type, find_all_rec(name, e.next));
			}
		}

		return null;
	}

	/***************************************************************************/
	/* begine scope = Enter the <SCOPE-BOUNDARY> element to the data structure */
	/***************************************************************************/
	public void beginScope()
	{
		/************************************************************************/
		/* Though <SCOPE-BOUNDARY> entries are present inside the symbol table, */
		/* they are not really types. In order to be able to debug print them,  */
		/* a special TYPE_FOR_SCOPE_BOUNDARIES was developed for them. This     */
		/* class only contain their type name which is the bottom sign: _|_     */
		/************************************************************************/
		enter(
			"SCOPE-BOUNDARY",
			new TYPE_FOR_SCOPE_BOUNDARIES("NONE"));

		/*********************************************/
		/* Print the symbol table after every change */
		/*********************************************/
		PrintMe();
	}

	/********************************************************************************/
	/* end scope = Keep popping elements out of the data structure,                 */
	/* from most recent element entered, until a <NEW-SCOPE> element is encountered */
	/********************************************************************************/
	public void endScope()
	{
		/**************************************************************************/
		/* Pop elements from the symbol table stack until a SCOPE-BOUNDARY is hit */		
		/**************************************************************************/
		while (top.name != "SCOPE-BOUNDARY")
		{
			table[top.index] = top.next;
			top_index = top_index-1;
			top = top.prevtop;
		}
		/**************************************/
		/* Pop the SCOPE-BOUNDARY sign itself */		
		/**************************************/
		table[top.index] = top.next;
		top_index = top_index-1;
		top = top.prevtop;

		/*********************************************/
		/* Print the symbol table after every change */		
		/*********************************************/
		PrintMe();
	}


	/****************************************************************************************/
	/* Finds the inner-most class we are at, and if we are not in any class - returns null; */
	/****************************************************************************************/
	public TYPE_CLASS find_curr_scope_class(){
		
		SYMBOL_TABLE_ENTRY top_entry = top;

		while (top_entry.type.getClass() != TYPE_CLASS){
			top_entry = top_entry.prevtop;
		}

		// if top_entry == null that means no we are at no class' scope

		if (top_entry != null){
			return top_entry.type;
		}

		// else

		return null;
	}

	/*******************************************************************************************/
	/* Finds the inner-most function we are at, and if we are not in any class - returns null; */
	/*******************************************************************************************/
	public TYPE_CLASS find_curr_scope_function(){
		
		SYMBOL_TABLE_ENTRY top_entry = top;

		while (top_entry.type.getClass() != TYPE_FUNCTION){
			top_entry = top_entry.prevtop;
		}

		// if top_entry == null that means no we are at no FUNCTION's scope

		if (top_entry != null){
			return top_entry.type;
		}

		// else

		return null;
	}

	/*****************************************************************************************************/
	/* Finds the given name at the global scope and returns it if found. If did not found - returns null */
	/*****************************************************************************************************/

	public TYPE find_at_global_scope(String name)
	{
		SYMBOL_TABLE_ENTRY curr_scope_top = this.top;

		SYMBOL_TABLE_ENTRY curr_top = this.top;

		while (curr_top != null)
		{
			if (curr_top.name == "SCOPE-BOUNDARY")
			{
				curr_scope_top = curr_top;
			}

			curr_top = curr_top.prevtop;
		}

		SYMBOL_TABLE_ENTRY global_scope_top = curr_scope_top;

		while (global_scope_top != null)
		{
			if (global_scope_top.name == name)
			{
				return global_scope_top.type;
			}
		}

		return null;
	}


	/*******************************************************************************/
	/* SEARCHES THE NAME BEGGINING IN THE GIVEN CLASS, MOVING TO ITS SUPER CLASS   */
	/* IF DIDN'T FOUND THERE, AND MOVES TO THE GLOBAL SCOPE WHEN DIDN'T FOUND IN   */
	/* ANY SUPER CLASS OF THE GIVEN CLASS.										   */
	/*******************************************************************************/

	public TYPE find_by_hierarchy(TYPE_CLASS cls, String name)
	{
		if (cls == null) { return null; }

		for (TYPE founded = find_at_class(cls, name);
			(founded == null) && (cls != null); 
			cls=cls.father, founded=find_at_class(cls, name));

		if (founded == null)
		{
			// DIDN'T FOUND IN ANY SUPER CLASS OF THE GIVEN CLASS; SEARCHING IT AT THE GLOBAL CLASS;

			return find_at_global_scope(name);
		}
		else
		{
			return founded;
		}
	}

	
	/*****************************************************************************************************/
	/* Finds the given name at the given class and returns it if found. If did not found - returns null  */
	/*****************************************************************************************************/

	public TYPE find_at_class(TYPE_CLASS cls, String name)
	{

		for (TYPE dec : cls.data_members)
		{
			if (dec.name == name)
			{
				return dec;
			}
		}

		return null;
	}

	/*****************************************************************************************************/
	/* 						 Tells us if wer'e currently at the global scope or not 					*/
	/*****************************************************************************************************/

	public TYPE at_global_scope()
	{
		SYMBOL_TABLE_ENTRY curr_top = this.top;

		while (curr_top != null)
		{
			if (curr_top.name == "SCOPE_BOUNDARY")
			{
				// WER'E NOT AT THE GLOBAL SCOPE
				return false;
			}
			curr_top = curr_top.prevtop;
		}

		// WE'RE AT THE GLOBAL SCOPE

		return true;
	}
	
	public static int n=0;
	
	public void PrintMe()
	{
		int i=0;
		int j=0;
		String dirname="./output/";
		String filename=String.format("SYMBOL_TABLE_%d_IN_GRAPHVIZ_DOT_FORMAT.txt",n++);

		try
		{
			/*******************************************/
			/* [1] Open Graphviz text file for writing */
			/*******************************************/
			PrintWriter fileWriter = new PrintWriter(dirname+filename);

			/*********************************/
			/* [2] Write Graphviz dot prolog */
			/*********************************/
			fileWriter.print("digraph structs {\n");
			fileWriter.print("rankdir = LR\n");
			fileWriter.print("node [shape=record];\n");

			/*******************************/
			/* [3] Write Hash Table Itself */
			/*******************************/
			fileWriter.print("hashTable [label=\"");
			for (i=0;i<hashArraySize-1;i++) { fileWriter.format("<f%d>\n%d\n|",i,i); }
			fileWriter.format("<f%d>\n%d\n\"];\n",hashArraySize-1,hashArraySize-1);
		
			/****************************************************************************/
			/* [4] Loop over hash table array and print all linked lists per array cell */
			/****************************************************************************/
			for (i=0;i<hashArraySize;i++)
			{
				if (table[i] != null)
				{
					/*****************************************************/
					/* [4a] Print hash table array[i] -> entry(i,0) edge */
					/*****************************************************/
					fileWriter.format("hashTable:f%d -> node_%d_0:f0;\n",i,i);
				}
				j=0;
				for (SYMBOL_TABLE_ENTRY it=table[i];it!=null;it=it.next)
				{
					/*******************************/
					/* [4b] Print entry(i,it) node */
					/*******************************/
					fileWriter.format("node_%d_%d ",i,j);
					fileWriter.format("[label=\"<f0>%s|<f1>%s|<f2>prevtop=%d|<f3>next\"];\n",
						it.name,
						it.type.name,
						it.prevtop_index);

					if (it.next != null)
					{
						/***************************************************/
						/* [4c] Print entry(i,it) -> entry(i,it.next) edge */
						/***************************************************/
						fileWriter.format(
							"node_%d_%d -> node_%d_%d [style=invis,weight=10];\n",
							i,j,i,j+1);
						fileWriter.format(
							"node_%d_%d:f3 -> node_%d_%d:f0;\n",
							i,j,i,j+1);
					}
					j++;
				}
			}
			fileWriter.print("}\n");
			fileWriter.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}		
	}
	
	/**************************************/
	/* USUAL SINGLETON IMPLEMENTATION ... */
	/**************************************/
	private static SYMBOL_TABLE instance = null;

	/*****************************/
	/* PREVENT INSTANTIATION ... */
	/*****************************/
	protected SYMBOL_TABLE() {}

	/******************************/
	/* GET SINGLETON INSTANCE ... */
	/******************************/
	public static SYMBOL_TABLE getInstance()
	{
		if (instance == null)
		{
			/*******************************/
			/* [0] The instance itself ... */
			/*******************************/
			instance = new SYMBOL_TABLE();

			/*****************************************/
			/* [1] Enter primitive types int, string */
			/*****************************************/
			instance.enter("int",   TYPE_INT.getInstance());
			instance.enter("string",TYPE_STRING.getInstance());

			/*************************************/
			/* [2] How should we handle void ??? */
			/*************************************/

			/***************************************/
			/* [3] Enter library function PrintInt */
			/***************************************/
			instance.enter(
				"PrintInt",
				new TYPE_FUNCTION(
					TYPE_VOID.getInstance(),
					"PrintInt",
					new TYPE_LIST(
						TYPE_INT.getInstance(),
						null)));
			
		}
		return instance;
	}
}
