/***********/
/* PACKAGE */
/***********/
package MIPS;

/*******************/
/* GENERAL IMPORTS */
/*******************/
import java.io.PrintWriter;
import java.util.*;

/*******************/
/* PROJECT IMPORTS */
/*******************/
import TEMP.*;
import TYPES.*;

public class MIPSGenerator
{
	private int WORD_SIZE=4;
	/***********************/
	/* The file writer ... */
	/***********************/
	private PrintWriter fileWriter;

	/***********************/
	/* The file writer ... */
	/***********************/
	public void finalizeFile()
	{
		exit();
		fileWriter.close();
	}

	public void print_int(TEMP t)
	{
		// mips instructions for syscall printInt
		int idx=t.getSerialNumber();
		// fileWriter.format("\taddi $a0,Temp_%d,0\n",idx);
		fileWriter.format("\tmove $a0,Temp_%d\n",idx);
		fileWriter.format("\tli $v0,1\n");
		fileWriter.format("\tsyscall\n");
		// mips instructions for print a space
		fileWriter.format("\tli $a0,32\n");
		fileWriter.format("\tli $v0,11\n");
		fileWriter.format("\tsyscall\n");
	}

	public void print_string(TEMP t)
	{
		// mips instructions for syscall printString
		int idx=t.getSerialNumber();
		// fileWriter.format("\taddi $a0,Temp_%d,0\n",idx);
		fileWriter.format("\tla $a0,Temp_%d\n",idx);
		fileWriter.format("\tli $v0,4\n");
		fileWriter.format("\tsyscall\n");
		// mips instruction for print a space
		fileWriter.format("\tli $a0,32\n");
		fileWriter.format("\tli $v0,11\n");
		fileWriter.format("\tsyscall\n");
	}

	public void exit()
	{
		// final mips instructions to exit the program
		fileWriter.format("\tli $v0,10\n");
		fileWriter.format("\tsyscall\n");
	}

	//public TEMP addressLocalVar(int serialLocalVarNum)
	//{
	//	TEMP t  = TEMP_FACTORY.getInstance().getFreshTEMP();
	//	int idx = t.getSerialNumber();
	//
	//	fileWriter.format("\taddi Temp_%d,$fp,%d\n",idx,-serialLocalVarNum*WORD_SIZE);
	//	
	//	return t;
	//}
	
	// -------------------------------------------- FOR FUNCTIONS --------------------------------------------------

	public void prologue(TYPE_FUNCTION func_data)
	{
		// mips instructions for the start of each function
		fileWriter.format("\tsubu $sp,$sp,4\n");
		fileWriter.format("\tsw $ra,0($sp)\n");
		fileWriter.format("\tsubu $sp,$sp,4\n");
		fileWriter.format("\tsw $fp,0($sp)\n");
		fileWriter.format("\tmove $fp,$sp\n");
		// take num_local_vars from the AST annotations
		int dec_offset = 8 + 4 * func_data.num_local_vars;
		fileWriter.format("\tsubu $sp,$sp,%d\n", dec_offset);
	}
	
	public void epilogue(TYPE_FUNCTION func_data) 
	{
		// mips instructions for the end of each function
		fileWriter.format("\tmove $sp,$fp\n");
		fileWriter.format("\tlw $fp,0($sp)\n");
		fileWriter.format("\tlw $ra,4($sp)\n");
		// fileWriter.format("\taddu $sp,$sp,%d\n", 4 * func_data.params.length()); wrong becuase we just skip fp and ret old values
		// the skip over the arguments with be in the caller
		fileWriter.format("\taddu $sp,$sp,%d\n", 8);
		fileWriter.format("\tjr $ra");
	}

	public void rtn()
	{
		// mips instruction for the return from function
		fileWriter.format("\tjr $ra\n");
	}
	
	public void store_return_value(TEMP t)
	{
		int idx = t.getSerialNumber();
		fileWriter.format("\tmove Temp_%d,$v0\n", idx);
	}


	// previous put_args_in_stack
	//	public void put_args_in_stack(TEMP_LIST args)
	//	{
	//			for(int idx: reverseSerialNumbers)
	//		{
	//			fileWriter.format("\tsubu $sp,$sp,4\n");
	//			fileWriter.format("\tsw Temp_$d,0($sp)\n", idx);
	//		}
	//	}

	public void put_args_in_stack(TEMP_LIST args)
	{
		// Creating linked list and reverse serial numbers of the args
		LinkedList<Integer> reverseSerialNumbers = new LinkedList<Integer>();
		for (TEMP arg : args)
		{
			int idx = arg.getSerialNumber();
			reverseSerialNumbers.add(idx);
		}
		Collections.reverse(reverseSerialNumbers);

		// stack in call f 3,4
		//
		// 		|   sp  |
		//		|  loc2 |
		//		|  loc1 |
		//		|   fp  |
		//		|  ret  |
		//  	|   3   |
		//  	|___4___|
		// when the stack grows up

		for(int idx: reverseSerialNumbers)
		{
			fileWriter.format("\tsubu $sp,$sp,4\n");
			fileWriter.format("\tsw Temp_$d,0($sp)\n", idx);
		}
	}


	public void array_access(TEMP dst, TEMP arr, TEMP index)
	{
		// mips instruction for the access to array
	}


	public void func_call()
	{

	}
	
	public void jal(String label)
	{
		fileWriter.format("\tjal %s\n", label);
	}
	
	public void restore_stack_pointer(int args_num)
	{
		// mult 4 because 32bit CPU means the $sp jumps in mult of 4
		int inc_offset = 4 * args_num;
		fileWriter.format("\taddu $sp,$sp,%d\n", 4 * inc_offset);
	}

	
	// -----------------------------------------------------------------------------------------------------------
	
	public void malloc(TEMP dst, TEMP size, int type_bytes_size)
	{	
		TEMP factor = TEMP_FACTORY.getInstance().getFreshTEMP();
		TEMP size_bytes = TEMP_FACTORY.getInstance().getFreshTEMP();
		
		fileWriter.format("\tli Temp_%d,%d\n", factor.getSerialNumber(), type_bytes_size);
		fileWriter.format("\tmul Temp_%d,Temp_%d,Temp_%d\n", size_bytes.getSerialNumber(), factor.getSerialNumber(), size.getSerialNumber());
		fileWriter.format("\tmove $a0,Temp_%d\n", size_bytes.getSerialNumber());
		fileWriter.format("\tli $v0,9\n");
		fileWriter.format("\tsyscall\n");
	}
	
	public void allocate(String var_name)
	{
		fileWriter.format(".data\n");
		fileWriter.format("\tglobal_%s: .word 721\n",var_name);
	}

	public void load(TEMP dst,String var_name)
	{
		int idxdst=dst.getSerialNumber();
		fileWriter.format("\tlw Temp_%d,global_%s\n",idxdst,var_name);
	}

	public void store(String var_name,TEMP src)
	{
		int idxsrc=src.getSerialNumber();
		fileWriter.format("\tsw Temp_%d,global_%s\n",idxsrc,var_name);		
	}

	public void li(TEMP t,int value)
	{
		int idx=t.getSerialNumber();
		fileWriter.format("\tli Temp_%d,%d\n",idx,value);
	}

	public void add(TEMP dst,TEMP oprnd1,TEMP oprnd2)
	{
		// mips instructions for the addition operation
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();
		int dstidx=dst.getSerialNumber();

		fileWriter.format("\tadd Temp_%d,Temp_%d,Temp_%d\n",dstidx,i1,i2);
	}

	public void sub(TEMP dst,TEMP oprnd1,TEMP oprnd2)
	{
		// mips instructions for the subtraction operation
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();
		int dstidx=dst.getSerialNumber();

		fileWriter.format("\tsub Temp_%d,Temp_%d,Temp_%d\n",dstidx,i1,i2);
	}


	public void mul(TEMP dst,TEMP oprnd1,TEMP oprnd2)
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();
		int dstidx=dst.getSerialNumber();

		fileWriter.format("\tmul Temp_%d,Temp_%d,Temp_%d\n",dstidx,i1,i2);
	}
	
	public void divide(TEMP dst,TEMP oprnd1,TEMP oprnd2)
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();
		int dstidx=dst.getSerialNumber();

		fileWriter.format("\tdiv Temp_%d,Temp_%d,Temp_%d\n",dstidx,i1,i2);
	}

	public void label(String inlabel)
	{
		if (inlabel.equals("main"))
		{
			fileWriter.format(".text\n");
			fileWriter.format("%s:\n",inlabel);
		}
		else
		{
			fileWriter.format("%s:\n",inlabel);
		}
	}	

	public void jump(String inlabel)
	{
		fileWriter.format("\tj %s\n",inlabel);
	}	

	public void blt(TEMP oprnd1,TEMP oprnd2,String label)
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();
		
		fileWriter.format("\tblt Temp_%d,Temp_%d,%s\n",i1,i2,label);				
	}

	public void bge(TEMP oprnd1,TEMP oprnd2,String label)
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();
		
		fileWriter.format("\tbge Temp_%d,Temp_%d,%s\n",i1,i2,label);				
	}

	public void bne(TEMP oprnd1,TEMP oprnd2,String label)
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();
		
		fileWriter.format("\tbne Temp_%d,Temp_%d,%s\n",i1,i2,label);				
	}

	public void beq(TEMP oprnd1,TEMP oprnd2,String label)
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();
		
		fileWriter.format("\tbeq Temp_%d,Temp_%d,%s\n",i1,i2,label);				
	}
	
	public void beqz(TEMP oprnd1,String label)
	{
		int i1 =oprnd1.getSerialNumber();
				
		fileWriter.format("\tbeq Temp_%d,$zero,%s\n",i1,label);				
	}

	// for local variables and paramteres user offset from the AST annotations
	public void lw(TEMP dst, TEMP src, int offset)
	{
		fileWriter.format("\tlw Temp_%d,%d(Temp_%d)\n", dst.getSerialNumber(), offset, src.getSerialNumber());
	}

	// for local variables and paramteres user offset from the AST annotations
	public void sw(TEMP src, TEMP dst, int offset)
	{
		fileWriter.format("\tsw Temp_%d,%d(Temp_%d)\n", src.getSerialNumber(), offset, dst.getSerialNumber());
	}
	
	/**************************************/
	/* USUAL SINGLETON IMPLEMENTATION ... */
	/**************************************/
	private static MIPSGenerator instance = null;

	/*****************************/
	/* PREVENT INSTANTIATION ... */
	/*****************************/
	protected MIPSGenerator() {}

	/******************************/
	/* GET SINGLETON INSTANCE ... */
	/******************************/
	public static MIPSGenerator getInstance()
	{
		if (instance == null)
		{
			/*******************************/
			/* [0] The instance itself ... */
			/*******************************/
			instance = new MIPSGenerator();

			try
			{
				/*********************************************************************************/
				/* [1] Open the MIPS text file and write data section with error message strings */
				/*********************************************************************************/
				String dirname="./output/";
				String filename=String.format("MIPS.txt");

				/***************************************/
				/* [2] Open MIPS text file for writing */
				/***************************************/
				instance.fileWriter = new PrintWriter(dirname+filename);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

			/*****************************************************/
			/* [3] Print data section with error message strings */
			/*****************************************************/
			instance.fileWriter.print(".data\n");
			instance.fileWriter.print("string_access_violation: .asciiz \"Access Violation\"\n");
			instance.fileWriter.print("string_illegal_div_by_0: .asciiz \"Illegal Division By Zero\"\n");
			instance.fileWriter.print("string_invalid_ptr_dref: .asciiz \"Invalid Pointer Dereference\"\n");
		}
		return instance;
	}
}
