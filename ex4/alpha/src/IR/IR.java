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
import TEMP.*;
import RegisterAllocation.RegisterAllocator;

public class IR
{
	private LinkedList<IRcommand> cmd_list = null;
	private LinkedList<IRcommand> global_decs_list = null;

	private boolean global_mode = false;

	public void change_to_global_mode()
	{
		global_mode = true;
	}

	public void change_to_local_mode()
	{
		global_mode = false;
	}

	// adds a command to the global initializations list

	public void Add_IRcommand(IRcommand cmd)
	{
		if (cmd_list == null)
		{
			cmd_list = new LinkedList<>();
		}

		if (global_decs_list == null)
		{
			global_decs_list = new LinkedList<>();
		}

		if (global_mode)
		{
			global_decs_list.add(cmd);
		}
		else
		{
			cmd_list.add(cmd);
		}
	}

	// Moves initializations and declarations of global variables to the start
	public void Move_Global_Initializations()
	{
		// Allocate registers :

		RegisterAllocator ra = new RegisterAllocator();

		for (IRcommand cmd : global_decs_list)
		{
			ra.add_cmd(cmd);
		}

		ra.AllocateRegisters();

		// MIPS the code :

		MIPSGenerator.getInstance().label("global_initializations");

		for (IRcommand cmd : global_decs_list)
		{
			cmd.MIPSme();
		}

		MIPSGenerator.getInstance().jump_ra();
	}

	public void MIPSme()
	{
		Move_Global_Initializations();  // initializes the global information

		// MIPSing the other code
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
				allocator.add_cmd(cmd);

				// we've finished to parse a function :
				in_function = false;

				// allocate the machine registers :
				allocator.AllocateRegisters();

				LinkedList<IRcommand> all_func_cmds = allocator.getAllCMDs();

				for (IRcommand cmd1 : all_func_cmds)
				{
					cmd1.MIPSme();
				}

				continue;
			}

			if (in_function)
			{
				// we are in a function and will MIPS the command after we'll allocate suitable registers
				allocator.add_cmd(cmd);
			}
			else
			{
				// DEPRECATED : WE SHOULDN'T GET HERE ANYMORE. ALL GLOBAL INITIALIZATIONS SHOULD HAPPEN AT
				// 				THE 'global_initializations' SECTION.

				// -----------------------------------------------------------------------------------------

				// we are not in a function and will MIPS the command now, we won't use the registers
				// for many time so we can allocate them arbitrary.
				// there is no command that uses more than 10 temps and therefore the 'color' won't exceed 9

				int color = 0;

				LinkedList<TEMP> cmd_temps = cmd.getCMDTemps();

				if (cmd_temps != null)
				{
					for (TEMP t : cmd_temps)
					{
						t.color = color;
						color ++;
					}
				}

				cmd.MIPSme();
			}
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
