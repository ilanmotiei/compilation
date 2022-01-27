/***********/
/* PACKAGE */
/***********/
package IR;

/*******************/
/* GENERAL IMPORTS */
/*******************/
import java.util.*;

/*******************/
/* PROJECT IMPORTS */
/*******************/

import TYPES.*;
import MIPS.*;
import RegisterAllocation.RegisterAllocator;

public class IR
{
	private LinkedList<IRcommand> cmd_list = null;

	public void Add_IRcommand(IRcommand cmd)
	{
		if (cmd_list == null)
		{
			cmd_list = new LinkedList<>();
		}

		cmd_list.add(cmd);
	}

	public void AllocateRegisters()
	{
		RegisterAllocator allocator = new RegisterAllocator();
		boolean in_function = false;

		for (IRcommand cmd : cmd_list)
		{
			if (cmd instanceof IRcommand_AddPrologue)
			{
				// we're starting to parse a function :
				allocator = new RegisterAllocator();
				in_function = true;
			}
			
			if (cmd instanceof IRcommand_AddEpilogue)
			{
				// we've finished to parse a function :
				in_function = false;

				// allocate the machine registers :
				allocator.AllocateRegisters();
			}

			if (in_function)
			{
				RegisterAllocator.add_cmd(cmd);
			}
		}
	}

	public void MIPSme()
	{
		for (IRcommand cmd : cmd_list)
		{
			cmd.MIPSme();
		}
	}

	/**************************************/
	/* USUAL SINGLETON IMPLEMENTATION ... */
	/**************************************/
	private static IR instance = null;

	/*****************************/
	/* PREVENT INSTANTIATION ... */
	/*****************************/
	protected IR() {}

	/******************************/
	/* GET SINGLETON INSTANCE ... */
	/******************************/
	public static IR getInstance()
	{
		if (instance == null)
		{
			/*******************************/
			/* [0] The instance itself ... */
			/*******************************/
			instance = new IR();
		}
		return instance;
	}
}
