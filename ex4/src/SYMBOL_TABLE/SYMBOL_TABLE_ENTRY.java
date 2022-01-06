/***********/
/* PACKAGE */
/***********/
package SYMBOL_TABLE;

/*******************/
/* PROJECT IMPORTS */
/*******************/
import TYPES.*;

/**********************/
/* SYMBOL TABLE ENTRY */
/**********************/
public class SYMBOL_TABLE_ENTRY
{
	public static int localVarOffset;
	public static int argOffset;

	int index;
	public String name;
	public TYPE type;

	/*********************************************/
	/* prevtop and next symbol table entries ... */
	/*********************************************/
	public SYMBOL_TABLE_ENTRY prevtop;
	public SYMBOL_TABLE_ENTRY next;

	/****************************************************/
	/* The prevtop_index is just for debug purposes ... */
	/****************************************************/
	public int prevtop_index;

	public int offset;
	public boolean isArg;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public SYMBOL_TABLE_ENTRY(
		String name,
		TYPE type,
		int index,
		SYMBOL_TABLE_ENTRY next, // meta-data for the symbol table
		SYMBOL_TABLE_ENTRY prevtop, // the entry defined before this entry at the symbol table
		int prevtop_index,
		boolean isArg,
		boolean isLocalVar)
	{
		this.index = index;
		this.name = name;
		this.type = type;
		this.next = next;
		this.prevtop = prevtop;
		this.prevtop_index = prevtop_index;
		this.isArg = isArg;
		this.offset = 0; // default invalid offset

		if(type.is_function())
		{
			// initialize the relative variables for this function frame
			localVarOffset = -4;
			argOffset = 8;
		}
		else if(isArg)
		{
			this.offset = argOffset;
			argOffset += 4;
		}
		else if(isLocalVar)
		{
			this.offset = localVarOffset;
			localVarOffset -= 4;
		}
	}

	public boolean isScopeBoundary()
	{
		return this.name.equals("SCOPE-BOUNDARY");
	}

	public boolean isClassBoundary()
	{
		if ( ! this.isScopeBoundary())
		{
			return false;
		}
		
		return ((TYPE_FOR_SCOPE_BOUNDARIES) this.type).isClassWrapper();
	}

	public boolean isFunctionBoundary()
	{
		if ( ! this.isScopeBoundary())
		{
			return false;
		}
		
		return ((TYPE_FOR_SCOPE_BOUNDARIES) this.type).isFunctionWrapper();
	}

	public TYPE getScopeWrapper()
	{
		return ((TYPE_FOR_SCOPE_BOUNDARIES) this.type).getScopeWrapper();
	}

}
