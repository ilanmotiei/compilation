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
import TYPES.TYPE_ARRAY;
import TYPES.TYPE_CLASS_FIELD;
import IR.*;

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
		fileWriter.print("\tli $v0,10\n");
		fileWriter.print("\tsyscall\n");
		fileWriter.close();
	}

	public void print_int(TEMP t)
	{
		int idx=t.getSerialNumber();
		// fileWriter.format("\taddi $a0,Temp_%d,0\n",idx);
		fileWriter.format("\tmove $a0,Temp_%d\n",idx);
		fileWriter.format("\tli $v0,1\n");
		fileWriter.format("\tsyscall\n");
		fileWriter.format("\tli $a0,32\n");
		fileWriter.format("\tli $v0,11\n");
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
	
	// cls is the class we were in when this loading was called. may be null;
	public void load(TEMP dst, String var_name, TYPE_CLASS cls, boolean isLocalVar, boolean isArg, boolean isClassField, int offset)
	{
		int idxdst = dst.getSerialNumber();

		if (isLocalVar)
		{
			// IT'S A LOCAL VARIABLE AND WE DON'T NEED ITS NAME - ONLY ITS OFFSET
			// FROM THE FRAME POINTER
			fileWriter.format("\tlw Temp_%d,%d($fp)\n",idxdst, offset);
		}
		else{
			if (isArg)
			{
				// IT'S A LOCAL VARIABLE AND WE DON'T NEED ITS NAME - ONLY ITS OFFSET
				// FROM THE FRAME POINTER
				fileWriter.format("\tlw Temp_%d,%d($fp)\n",idxdst, offset);
			}
			else{
				if (isClassField)
				{
					// we are uploading the value as a field of the instance
					// stored at the 8'th offset from the frame pointer

					fileWriter.format("\tlw $s0,8($sp)\n");
					int field_off = cls.getFieldIndex(var_name) * WORD_SIZE;
					fileWriter.format("\tlw Temp_%d,%d($s0)\n", idxdst, 
																field_off);
				}
				else
				{
					// IT'S A GLOBAL VARIABLE - WE NEED ITS NAME
					fileWriter.format("\tlw Temp_%d,global_%s\n",idxdst, var_name);
				}
			}
		}		
	}

	// cls is the class we were in when this storing was called. may be null;
	public void store(String var_name, TYPE_CLASS cls, TEMP src, boolean isLocalVar, boolean isArg, boolean isClassField, int offset)
	{
		// fileWriter.format("\tsw Temp_%d,global_%s\n",idxsrc,var_name);
		
		int idxsrc = src.getSerialNumber();

		if (isLocalVar)
		{
			// IT'S A LOCAL VARIABLE AND WE DON'T NEED ITS NAME - ONLY ITS OFFSET
			// FROM THE FRAME POINTER
			fileWriter.format("\tsw Temp_%d,%d($fp)\n", idxsrc, offset);
		}
		else{
			if (isArg)
			{
				// IT'S A LOCAL VARIABLE AND WE DON'T NEED ITS NAME - ONLY ITS OFFSET
				// FROM THE FRAME POINTER
				fileWriter.format("\tsw Temp_%d,%d($fp)\n", idxsrc, offset);
			}
			else{
				if (isClassField)
				{
					// we are storing the value as a field of the instance
					// stored at the 8'th offset from the frame pointer

					fileWriter.format("\tlw $s0,8($sp)\n");
					int field_off = cls.getFieldIndex(var_name) * WORD_SIZE;
					fileWriter.format("\tsw Temp_%d,%d($s0)\n", idxsrc, 
																field_off);
				}
				else
				{
					// IT'S A GLOBAL VARIABLE - WE NEED ITS NAME
					fileWriter.format("\tsw Temp_%d,global_%s\n",idxsrc, var_name);
				}
			}
		}		
	}

	public void lw(TEMP dst, TEMP src)
	{
		fileWriter.format("\tlw Temp_%d,0(Temp_%d)\n", dst.getSerialNumber(),
													   src.getSerialNumber());
	}

	public void li(TEMP t,int value)
	{
		int idx=t.getSerialNumber();
		fileWriter.format("\tli Temp_%d,%d\n",idx,value);
	}
	public void add(TEMP dst,TEMP oprnd1,TEMP oprnd2)
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();
		int dstidx=dst.getSerialNumber();

		fileWriter.format("\tadd Temp_%d,Temp_%d,Temp_%d\n",dstidx,i1,i2);
	}

	public void divide(TEMP dst,TEMP oprnd1,TEMP oprnd2)
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();
		int dstidx=dst.getSerialNumber();

		fileWriter.format("\tdiv Temp_%d,Temp_%d\n",i1,i2);
		// result is stored at the register $lo. moving it to the "dst" register
		fileWriter.format("\tmflo Temp_%d\n", dstidx);
	}

	public void mul(TEMP dst,TEMP oprnd1,TEMP oprnd2)
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();
		int dstidx=dst.getSerialNumber();

		fileWriter.format("\tmul Temp_%d,Temp_%d,Temp_%d\n",dstidx,i1,i2);
	}

	public void strings_equal(TEMP dst, TEMP oprnd1, TEMP oprnd2)
	{
		int i1 = oprnd1.getSerialNumber();
		int i2 = oprnd2.getSerialNumber();
		int dst_idx = dst.getSerialNumber();

		// moving the 'arguments' throght the registers $s0 and $s1
		fileWriter.format("\tmove $s0,Temp_%d\n", i1);
		fileWriter.format("\tmove $s1,Temp_%d\n", i2);
		jump("_strcmp_");

		// result is stored at the register $s2. moving it to the 'dst' register
		fileWriter.format("\tmove Temp_%d,$s2", dst_idx);
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

	public void jal(String inlabel)
	{
		fileWriter.format("\tjal %s\n", inlabel);
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
	public void bltz(TEMP oprnd1,String label)
	{
		int i1 =oprnd1.getSerialNumber();
				
		fileWriter.format("\tbltz Temp_%d,$zero,%s\n",i1,label);				
	}

	public void array_access(TEMP dst, TEMP arr, TEMP idx)
	{
		// RUNNING-TIME CHECKS : ----------------------------------

		// if (idx < 0) : abort
		bltz(idx, "index_out_of_range_abort"); 
		// size = arr[0] (we store the array size at the 0 offset)
		fileWriter.format("\tlw $s0,0(Temp_%d)\n", arr.getSerialNumber());
		// if (idx >= size) : abort
		fileWriter.format("\tbge Temp_%d,$s0,%s\n", idx.getSerialNumber(), "index_out_of_range_abort");

		// ACCESSING AND STORING THE RESULTS : --------------------

		fileWriter.format("\tmove $s0,Temp_%d\n", idx.getSerialNumber());
		fileWriter.format("\tadd $s0,$s0,1\n");
		fileWriter.format("\tmul $s0,$s0,4\n");
		fileWriter.format("\tadd $s0,$s0,Temp_%d\n", arr.getSerialNumber());

		fileWriter.format("\tlw Temp_%d,0($s0)\n", dst.getSerialNumber());
	}

	public void array_set(TEMP arr, TEMP idx, TEMP value)
	{
		// RUNNING-TIME CHECKS : ------------------------------------------

		// if (idx < 0) : abort
		bltz(idx, "index_out_of_range_abort"); 
		// size = arr[0] (we store the array size at the 0 offset)
		fileWriter.format("\tlw $s0,0(Temp_%d)\n", arr.getSerialNumber());
		// if (idx >= size) : abort
		fileWriter.format("\tbge Temp_%d,$s0,%s\n", idx.getSerialNumber(), "index_out_of_range_abort");

		// ACCESSING THE INDEX AND STORING THE VALUE : --------------------

		fileWriter.format("\tmove $s0,Temp_%d\n", idx.getSerialNumber());
		fileWriter.format("\tadd $s0,$s0,1\n");
		fileWriter.format("\tmul $s0,$s0,4\n");
		fileWriter.format("\tadd $s0,$s0,Temp_%d\n", arr.getSerialNumber());

		fileWriter.format("\tsw Temp_%d,0($s0)\n", value.getSerialNumber());
	}

	public void move_rv(TEMP dst)
	{
		fileWriter.format("\tmove Temp_%d,$v0\n", dst.getSerialNumber());
	}

	public void set_rv(TEMP src)
	{
		fileWriter.format("\tmove $v0,Temp_%d\n", src.getSerialNumber());
	}

	public void load_immediate(TEMP dst, int value)
	{
		fileWriter.format("\tli Temp_%d,%d\n", dst.getSerialNumber(), value);
	}

	public void add_prologue(int function_max_local_var_offset)
	{
		// STORE RETURN ADDRESS AND FRAME POINTER --------------------------

		fileWriter.format("\tsubu $sp,$sp,4\n");
		fileWriter.format("\tsw $ra,0($sp)\n");
		fileWriter.format("\tsubu $sp,$sp,4\n");
		fileWriter.format("\tsw $fp,0($sp)\n"); // store previous frame pointer

		// MOVE CURRENT STACK POINTER TO BE CURRENT FRAME POINTER ----------
		fileWriter.format("\tmove $fp,$sp\n");

		// STORE TEMPORAL REGISTERS ---------------------------------------

		for(i=0; i<=9; i++)
		{
			fileWriter.format("\tsubu $sp,$sp,4\n");
			fileWriter.format("\tsw $t%d,0($sp)\n", i);
		}

		// CHANGE CURRENT STACK POINTER IN ORDER TO LEAVE A PLACE FOR ALL
		// THE LOCAL VARIABLELS WE'LL DEFINE AT THE FUNCTION

		fileWriter.format("\tsubu $sp,$sp,%d\n", function_max_local_var_offset);

		/*

		stack's image right now : 

		|  return_add  |
		|  previous fp |  <---  fp
		|  backuped t0 |
		|     ....     |
		|  backuped t8 |
		|  backuped t9 |
		| -local var 1-|
		| -local var 2-|
		| ............ |
		|-lastlocalvar-| <--- sp

		*/
	}

	public void add_epilogue()
	{
		/*

		stack's image right now : 

		|  return_add  |
		|  previous fp |  <---  fp
		|  backuped t0 |
		|     ....     |
		|  backuped t8 |
		|  backuped t9 |
		|  local var 1 |
		|  local var 2 |
		| ............ |
		|last local var| <--- sp

		*/

		// RESTORE TEMPORAL REGISTERS :

		for(int i=0; i <= 9; i++)
		{
			fileWriter.format("\tsubu $fp,$fp,4\n");
			fileWriter.format("\tlw $t%d,0($fp)\n");
		}

		// move frame pointer to its previous location :
		fileWriter.format("\taddu $fp,$fp,40\n");

		// load the previous fp and the return address :
		fileWriter.format("\tlw $s0,0($fp)\n"); // $s0 = previous_fp
		fileWriter.format("\tlw $ra,4($fp)\n"); // $ra = return_address

		fileWriter.format("\tmove $fp,$s0\n");  // $fp = $s0 = previous_fp

		fileWriter.format("\taddiu $sp,$fp,8\n"); // $sp = $ fp + 8 = previous_sp

		fileWriter.format("\tjr $ra\n");        // jump to the return_address
	}

	// Allocates a class object instance
	public allocate(TYPE_CLASS cls, TEMP dst)
	{
		// Allocate the memory :
		fileWriter.format("\tli $v0,9\n");
		int k = cls.getFieldsAmount();
		fileWriter.format("\tli $a0,%d\n", (k+1)*4);
		fileWriter.format("\tsyscall\n");

		// Putting it at the dst register :
		fileWriter.format("\tmove Temp_%d,$v0\n", dst.getSerialNumber());

		// Storing for it its vtable :
		fileWriter.format("\tla $s0,vt_%s\n", cls.name);
		fileWriter.format("\tsw $s0,0(Temp_%d)\n", dst.getSerialNumber());

		off = 4;

		// Initializing it :
		for (TYPE_CLASS_FIELD f : cls.getClassFields())
		{
			if (f.hasInitialValue())
			{
				if (f.initial_value instanceof String)
				{
					String label_name = f.getInitialClass().name + "_const_field";

					fileWriter.format(".data\n");
					fileWriter.format("\t%s: .asciiz \"%s\"\n", 
										label_name,
										(String) f.initial_value);

					fileWriter.format(".text\n");

					fileWriter.format("\tla $s0,%s\n", label_name);
					fileWriter.format("\tsw $s0,%d(Temp_%d)\n", off, 
																dst.getSerialNumber());
				}

				if (f.initial_value instanceof Integer)
				{
					fileWriter.format("\tli $s0,%d\n", (int) f.initial_value);
					fileWriter.format("\tsw $s0,%d(Temp_%d)\n", off,
																dst.getSerialNumber());
				}
			}
			else
			{
				fileWriter.format("\tli %s0,0\n");
				fileWriter.format("\tsw $s0,%d(Temp_%d)\n", off,
															dst.getSerialNumber());
			}

			off += 4;
		}
	}

	// Allocates an array from the specified type and size and puts it at the
	// 'dst' register
	public void allocate(TYPE elems_type, int size, TEMP dst)
	{
		// Allocate the memory :
		fileWriter.format("\tli $v0,9\n");
		fileWriter.format("\tli $a0,%d\n", (size+1)*WORD_SIZE);
		fileWriter.format("\tsyscall\n");
		fileWriter.format("\tmove Temp_%d,$v0\n", dst.getSerialNumber());

		// Initialize array's size : 
		fileWriter.format("\tli $s0,%d\n", size);
		fileWriter.format("\tsw $s0,0(Temp_%d)\n", dst.getSerialNumber());
	}

	public void push_args(TEMP_LIST args)
	{
		int amount = args.length();

		fileWriter.format("\tsubu $sp,$sp,%d\n", WORD_SIZE * amount);
		
		for (TEMP t : args)
		{
			fileWriter.format("\tsw Temp_%d,0($sp)\n", t.getSerialNumber());
			fileWriter.format("\taddu $sp,$sp,4\n");
		}

		fileWriter.format("\tsubu $sp,$sp,%d\n", WORD_SIZE * amount);
	}

	// A class method call : 
	public void virtual_call(TEMP src, TEMP_LIST args, TYPE_CLASS cls, String func_name)
	{
		TEMP_LIST final_args = new TEMP_LIST(src, args);

		push_args(final_args);

		// loads the vtable address
		fileWriter.format("\tlw $s0,0($src)\n");  

		// finds the function offset at the vtable
		int off = cls.getMethodNumber(func_name) * WORD_SIZE;  

		// loads the address of the specified function
		fileWriter.format("\tlw $s1, %d($s0)\n", off);  

		fileWriter.format("\tjalr $s1\n");
	}

	// A regular function call : 
	public void call(TEMP_LIST args, String func_name)
	{
		push_args(args);

		fileWriter.format("\tsubu $sp,$sp,4\n");  
		// ^ : leaving a hole for an object instance (no object instance will be 
		// 	   really places there), for uniformity.

		fileWriter.format("\tjal %s\n", func_name);
	}

	public static int const_str_cnt = 0;

	public void allocate_string(TEMP dst, String str)
	{
		fileWriter.format(".data\n");
		fileWriter.format("\tconst_str_%d: .asciiz \"%s\"\n", const_str_cnt, String);
		fileWriter.format(".text\n");
		fileWriter.format("\tla Temp_%d,const_str_%d\n", dst.getSerialNumber(),
														 const_str_cnt);


		const_str_cnt += 1;
	}

	public void init_class_vtable(TYPE_CLASS cls)
	{
		// HEADER :
		fileWriter.format(".data\n");
		fileWriter.format("vt_%s:\n", cls.name);

		// DEFINING THE METHODS :
		LinkedList<String> methods_names_lst = cls.getMethodsNames();

		int curr_off = 0;

		for (String method_name : methods_names_lst)
		{
			fileWriter.format("\t.word 0\n");

			TYPE_CLASS method_implementor_cls = cls.getMethodImplementor(method_name);
	
			if (method_implementor_cls == null)
			{
				// method wasn't defined in any higher class at the hierarchy
				// leaving for it a place at the vtable for when it'll be defined
			}
			else
			{
				// method was allready defined at some higher class at the hierarchy
				
				// loading its previous defenition's address :
				fileWriter.format(".text\n");
				fileWriter.format("la $s0,vt_%s", method_implementor_cls.name);
				int imp_off = method_implementor_cls.getMethodNumber(method_name);
				fileWriter.format("lw $s0,%d($s0)", imp_off);

				// storing it at the current class' vtable
				fileWriter.format("la $s1,vt_%s", cls.name);
				fileWriter.format("sw $s0,%d($s1)", curr_off);

				// changing mode back to data
				fileWriter.format(".data\n");
			}			

			curr_off += 4;
		}

		fileWriter.format(".text\n");
	}

	// changes the virtual table of the current class
	public void add_to_vtable(TYPE_CLASS cls, String method_name)
	{
		/*

		cls : the class that this method is being declared/reimplemented.
		method_name : the name of the method.

		*/

		// finding the address of the vtable of our current class : 
		fileWriter.format("\tla $s0,vt_%s\n", cls.name);

		// finding the offset at the vtable for the specified method name :
		int offset = cls.getMethodNumber(method_name) * WORD_SIZE;

		// finding the address of the method :
		fileWriter.format("\tla $s1,method_name\n");

		// updating the suitable line at the vtable of the class :
		fileWriter.format("\tsw $s1,%d($s0)\n", offset);
	}

	public void field_set(TEMP obj, TYPE_CLASS cls, String field_name, TEMP value)
	{
		int field_offset = cls.getFieldIndex(field_name) * WORD_SIZE;

		fileWriter.format("\tsw Temp_%d,%d(Temp_%d)\n", value.getSerialNumber(),
														field_offset,
														obj.getSerialNumber());
	}

	public void field_access(TEMP dst, TEMP obj, TYPE_CLASS cls, String field_name)
	{
		int field_offset = cls.getFieldIndex(field_name) * WORD_SIZE;

		fileWriter.format("\tlw Temp_%d,%d(Temp_%d)\n", dst.getSerialNumber(),
														field_offset,
														obj.getSerialNumber());
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
			instance.fileWriter.print(".text\n");
		}
		return instance;
	}
}
