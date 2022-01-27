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

	private int function_id = 0;

	public void AllocateRegisters()
	{
		LinkedList<IRcommand> curr_function_cmds;
		boolean in_function = false;

		for (IRcommand cmd : cmd_list)
		{
			if (cmd instanceof IRcommand_AddPrologue)
			{
				// we're starting to parse a function :
				curr_function_cmds = new LinkedList<>();
				in_function = true;
			}
			
			if (cmd instanceof IRcommand_AddEpilogue)
			{
				// we've finished to parse a function :
				in_function = false;
				function_id ++;

				// do liveness analysis and allocate the machine registers :
				
			}

			if (in_function)
			{
				curr_function_cmds.add(cmd);
				cmd.function_id = function_id;
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
