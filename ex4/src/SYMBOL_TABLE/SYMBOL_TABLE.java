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
		if (s.charAt(0) == 'm') {return 2;}
		if (s.charAt(0) == 'r') {return 3;}
		if (s.charAt(0) == 'i') {return 4;}
		if (s.charAt(0) == 'd') {return 5;}
		if (s.charAt(0) == 'k') {return 6;}
		if (s.charAt(0) == 'f') {return 7;}
		if (s.charAt(0) == 'S') {return 8;}
		return 12;
	}

	/****************************************************************************/
	/* Enter a variable, function, class type or array type to the symbol table */
	/****************************************************************************/
	public void enter(String name, TYPE t, boolean isArg, boolean isLocalVar)
	{
		/* [1] Compute the hash value for this new entry */

		int hashValue = hash(name);

		/* [2] Extract what will eventually be the next entry in the hashed position */
		/* NOTE: this entry can very well be null, but the behaviour is identical */

		SYMBOL_TABLE_ENTRY next = table[hashValue];
	
		/* [3] Prepare a new symbol table entry with name, type, next and prevtop */
		SYMBOL_TABLE_ENTRY e = new SYMBOL_TABLE_ENTRY(name, t, hashValue, next, top, top_index++, isArg, isLocalVar);

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
		SYMBOL_TABLE_ENTRY e = top;

		while (e != null)
		{
			if (e.isClassBoundary())
			{
				break;
			}

			// else:

			if (e.name.equals(name))
			{
				return e.type;
			}

			e = e.prevtop;
		}

		if (e == null) { return null; }

		// else : we are inside some class

		return find_by_hierarchy((TYPE_CLASS) e.getScopeWrapper(), name);
	}

	public SYMBOL_TABLE_ENTRY find_entry(String name)
	{
		SYMBOL_TABLE_ENTRY e = top;

		while (e != null)
		{
			if (e.name.equals(name))
			{
				return e;
			}

			e = e.prevtop;
		}

		return null;
	}



	/*
	Finds the given name at the current scope. Returns null if wasn't found.
	*/
	public TYPE find_at_curr_scope(String name)
	{
		SYMBOL_TABLE_ENTRY e = top;

		while (e != null)
		{
			if (e.name.equals(name))
			{
				return e.type;
			}

			if (e.isScopeBoundary())
			{
				break;
			}

			e = e.prevtop;
		}

		if (e == null) 
		{ 
			// We were at the outermost scope and we didn't found the given name in it
			return null; 
		}

		// else : e != null,
		// thus we are inside some class/function scope and we didn't found the given name in it

		return null;
	}

	/***************************************************************************/
	/* begine scope = Enter the <SCOPE-BOUNDARY> element to the data structure */
	/***************************************************************************/
	public void beginScope(TYPE scope_wrapper)
	{
		/************************************************************************/
		/* Though <SCOPE-BOUNDARY> entries are present inside the symbol table, */
		/* they are not really types. In order to be able to debug print them,  */
		/* a special TYPE_FOR_SCOPE_BOUNDARIES was developed for them. This     */
		/* class only contain their type name which is the bottom sign: _|_     */
		/************************************************************************/
		
		enter(
			"SCOPE-BOUNDARY",
			new TYPE_FOR_SCOPE_BOUNDARIES(scope_wrapper),
				false,
				false);
		
		/*********************************************/
		/* Print the symbol table after every change */
		/*********************************************/
		PrintMe();
	}

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
			new TYPE_FOR_SCOPE_BOUNDARIES(null),
				false,
				false);
		
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
		while ( ! top.isScopeBoundary())
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
		
		SYMBOL_TABLE_ENTRY top_entry = this.top;

		while (top_entry != null)
		{
			if (top_entry.isScopeBoundary())
			{
				if (top_entry.isClassBoundary())
				{
					return (TYPE_CLASS) top_entry.getScopeWrapper();
				}
			}

			top_entry = top_entry.prevtop;
		}

		// else

		return null;
	}

	/*******************************************************************************************/
	/* Finds the inner-most function we are at, and if we are not in any class - returns null; */
	/*******************************************************************************************/
	public TYPE_FUNCTION find_curr_scope_function(){
		
		SYMBOL_TABLE_ENTRY top_entry = this.top;

		while (top_entry != null)
		{
			if (top_entry.isScopeBoundary())
			{
				if (top_entry.isFunctionBoundary())
				{
					return (TYPE_FUNCTION) top_entry.getScopeWrapper();
				}
			}

			top_entry = top_entry.prevtop;
		}

		// if we got here that means no we are at no FUNCTION's scope

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
			if (curr_top.isScopeBoundary())
			{
				curr_scope_top = curr_top;
			}

			curr_top = curr_top.prevtop;
		}

		SYMBOL_TABLE_ENTRY global_scope_top = curr_scope_top;

		while (global_scope_top != null)
		{
			if (global_scope_top.name.equals(name))
			{
				return global_scope_top.type;
			}

			global_scope_top = global_scope_top.prevtop;
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
		TYPE founded = null;

		if (cls != null)
		{	
			founded = find_at_class(cls, name);
		}

		if (founded == null)
		{
			// DIDN'T FOUND IN ANY SUPER CLASS OF THE GIVEN CLASS,
			// OR WE ARE AT THE GLOBAL SCOPE; 
			// SEARCHING IT AT THE GLOBAL SCOPE;

			SYMBOL_TABLE_ENTRY curr = this.top;

			while (curr != null)
			{
				if (curr.name.equals(name))
				{
					return curr.type;
				}

				curr = curr.prevtop;
			}

			// else : name was not found
			return null;
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
		while (cls != null)
		{
			if (cls.data_members != null)
			{
				for (TYPE_CLASS_FIELD dec : cls.data_members)
				{
					if (dec.name.equals(name))
					{
						return dec.type;
					}
				}
			}

			cls = cls.father;
		}

		return null;
	}

	/*****************************************************************************************************/
	/* 						 Tells us if wer'e currently at the global scope or not 					*/
	/*****************************************************************************************************/

	public boolean at_global_scope()
	{
		SYMBOL_TABLE_ENTRY curr_top = this.top;

		while (curr_top != null)
		{
			if (curr_top.isScopeBoundary())
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
						it.type.type_name,
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
			instance.enter("int",   TYPE_INT.getInstance(), false, false);
			instance.enter("string",TYPE_STRING.getInstance(), false, false);

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
						null)), false, false);

			instance.enter("PrintString", 
						new TYPE_FUNCTION(
							TYPE_VOID.getInstance(), 
							"PrintString", 
							new TYPE_LIST(
								TYPE_STRING.getInstance(), 
								null)),
							  false,
					       false);

			instance.enter("PrintTrace", 
						new TYPE_FUNCTION(
							TYPE_VOID.getInstance(), 
							"PrintTrace", 
							null),
							false,
					     false);
		}
		return instance;
	}
}
