/***********/
/* PACKAGE */
/***********/
package MIPS;

/*******************/
/* GENERAL IMPORTS */
/*******************/
import java.io.PrintWriter;
import java.util.*;
import IR.*;
import SYMBOL_TABLE.*;
import TYPES.*;
import TEMP.*;
import java.io.FileReader;

/*******************/
/* PROJECT IMPORTS */
/*******************/
import TEMP.*;
import TYPES.TYPE_ARRAY;
import TYPES.TYPE_CLASS;
import TYPES.TYPE_CLASS_FIELD;
import IR.*;

public class MIPSGenerator
{
	public int WORD_SIZE=4;
	/***********************/
	/* The file writer ... */
	/***********************/
	private PrintWriter fileWriter;

	/***********************/
	/* The file writer ... */
	/***********************/
	public void finalizeFile()
	{
		fileWriter.close();
	}

	public void print_int(TEMP t)
	{
		int idx=t.getSerialNumber();
		// fileWriter.format("\taddi $a0,$t%d,0\n",idx);
		fileWriter.format("\tmove $a0,$t%d\n",idx);
		fileWriter.format("\tli $v0,1\n");
		fileWriter.format("\tsyscall\n");
		fileWriter.format("\tli $a0,32\n");
		fileWriter.format("\tli $v0,11\n");
		fileWriter.format("\tsyscall\n");
	}

	public void move(TEMP dst, TEMP src)
	{
		fileWriter.format("\tmove $t%d,$t%d\n", dst.getSerialNumber(), src.getSerialNumber());
	}
	
	// cls is the class we were in when this loading was called. may be null;
	public void load(TEMP dst, String var_name, TYPE_CLASS cls, boolean isLocalVar, boolean isArg, boolean isClassField, int offset)
	{
		int idxdst = dst.getSerialNumber();

		if (isLocalVar)
		{
			// IT'S A LOCAL VARIABLE AND WE DON'T NEED ITS NAME - ONLY ITS OFFSET
			// FROM THE FRAME POINTER
			fileWriter.format("\tlw $t%d,%d($fp)\n",idxdst, offset);
		}
		else{
			if (isArg)
			{
				// IT'S A LOCAL VARIABLE AND WE DON'T NEED ITS NAME - ONLY ITS OFFSET
				// FROM THE FRAME POINTER
				fileWriter.format("\tlw $t%d,%d($fp)\n",idxdst, offset);
			}
			else{
				if (isClassField)
				{
					// we are uploading the value as a field of the instance
					// stored at the 8'th offset from the frame pointer

					fileWriter.format("\tlw $s0,8($fp)\n");
					int field_off = (cls.getFieldIndex(var_name) + 1) * WORD_SIZE;
					fileWriter.format("\tlw $t%d,%d($s0)\n", idxdst, 
																field_off);
				}
				else
				{
					// IT'S A GLOBAL VARIABLE - WE NEED ITS NAME
					fileWriter.format("\tlw $t%d,global_%s\n",idxdst, var_name);
				}
			}
		}		
	}

	// cls is the class we were in when this storing was called. may be null;
	public void store(String var_name, TYPE_CLASS cls, TEMP src, boolean isLocalVar, boolean isArg, boolean isClassField, int offset)
	{
		// fileWriter.format("\tsw $t%d,global_%s\n",idxsrc,var_name);
		
		int idxsrc = src.getSerialNumber();

		if (isLocalVar)
		{
			// IT'S A LOCAL VARIABLE AND WE DON'T NEED ITS NAME - ONLY ITS OFFSET
			// FROM THE FRAME POINTER
			fileWriter.format("\tsw $t%d,%d($fp)\n", idxsrc, offset);
		}
		else{
			if (isArg)
			{
				// IT'S A LOCAL VARIABLE AND WE DON'T NEED ITS NAME - ONLY ITS OFFSET
				// FROM THE FRAME POINTER
				fileWriter.format("\tsw $t%d,%d($fp)\n", idxsrc, offset);
			}
			else{
				if (isClassField)
				{
					// we are storing the value as a field of the instance
					// stored at the 8'th offset from the frame pointer

					fileWriter.format("\tlw $s0,8($fp)\n");
					int field_off = (cls.getFieldIndex(var_name) + 1) * WORD_SIZE;
					fileWriter.format("\tsw $t%d,%d($s0)\n", idxsrc, 
																field_off);
				}
				else
				{
					// IT'S A GLOBAL VARIABLE - WE NEED ITS NAME
					fileWriter.format("\tsw $t%d,global_%s\n",idxsrc, var_name);
				}
			}
		}		
	}

	public void lw(TEMP dst, TEMP src)
	{
		fileWriter.format("\tlw $t%d,0($t%d)\n", dst.getSerialNumber(),
													   src.getSerialNumber());
	}

	public void li(TEMP t,int value)
	{
		int idx=t.getSerialNumber();
		fileWriter.format("\tli $t%d,%d\n",idx,value);
	}
	public void add(TEMP dst,TEMP oprnd1,TEMP oprnd2)
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();
		int dstidx=dst.getSerialNumber();

		fileWriter.format("\tadd $t%d,$t%d,$t%d\n",dstidx,i1,i2);
	}

	public void addu(TEMP dst,TEMP oprnd1,int offset)
	{
		int i1 =oprnd1.getSerialNumber();
		int dstidx=dst.getSerialNumber();

		fileWriter.format("\taddu $t%d,$t%d,%d\n",dstidx,i1,offset);
	}

	public void subu(TEMP dst,TEMP oprnd1,int offset)
	{
		int i1 =oprnd1.getSerialNumber();
		int dstidx=dst.getSerialNumber();

		fileWriter.format("\tsubu $t%d,$t%d,%d\n",dstidx,i1,offset);
	}

	public void sub(TEMP dst,TEMP oprnd1,TEMP oprnd2)
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();
		int dstidx=dst.getSerialNumber();

		fileWriter.format("\tsub $t%d,$t%d,$t%d\n",dstidx,i1,i2);
	}

	public void divide(TEMP dst,TEMP oprnd1,TEMP oprnd2)
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();
		int dstidx=dst.getSerialNumber();

		beqz(oprnd2, "division_by_zero_abort");

		fileWriter.format("\tdiv $t%d,$t%d\n",i1,i2);
		// result is stored at the register $lo. moving it to the "dst" register
		fileWriter.format("\tmflo $t%d\n", dstidx);
	}

	public void mul(TEMP dst,TEMP oprnd1,TEMP oprnd2)
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();
		int dstidx=dst.getSerialNumber();

		fileWriter.format("\tmul $t%d,$t%d,$t%d\n",dstidx,i1,i2);
	}

	// binds the number at 't' between the range (minimum, maximum)
	public void bind(TEMP t, int minimum, int maximum)
	{
		fileWriter.format("\tmove $s0,$t%d\n", t.getSerialNumber());
		fileWriter.format("\tli $s1,%d\n", minimum);
		fileWriter.format("\tli $s2,%d\n", maximum);

		jal("_bind_int_");

		fileWriter.format("\tmove $t%d,$v0\n", t.getSerialNumber());
	}

	public void str_cmp(TEMP dst, TEMP oprnd1, TEMP oprnd2)
	{
		int i1 = oprnd1.getSerialNumber();
		int i2 = oprnd2.getSerialNumber();
		int dst_idx = dst.getSerialNumber();

		// moving the 'arguments' throght the registers $s0 and $s1
		fileWriter.format("\tmove $s0,$t%d\n", i1);
		fileWriter.format("\tmove $s1,$t%d\n", i2);
		jal("_strcmp_");

		// result is stored at the register $v0. moving it to the 'dst' register
		fileWriter.format("\tmove $t%d,$v0\n", dst_idx);
	}

	public void str_add(TEMP dst, TEMP oprnd1, TEMP oprnd2)
	{
		int i1 = oprnd1.getSerialNumber();
		int i2 = oprnd2.getSerialNumber();
		int dst_idx = dst.getSerialNumber();

		// moving the 'arguments' throght the registers $s0 and $s1
		fileWriter.format("\tmove $s0,$t%d\n", i1);
		fileWriter.format("\tmove $s1,$t%d\n", i2);
		jal("_stradd_");

		// result is stored at the register $v0. moving it to the 'dst' register
		fileWriter.format("\tmove $t%d,$v0\n", dst_idx);
	}

	public void str_cpy(TEMP dst, TEMP src)
	{
		int dst_idx = dst.getSerialNumber();
		int src_idx = src.getSerialNumber();

		// moving the 'arguments' throght the registers $s0 and $s1
		fileWriter.format("\tmove $s0,$t%d\n", dst_idx);
		fileWriter.format("\tmove $s1,$t%d\n", src_idx);

		// jumping into the copy section (which then jumps back here)
		jal("_strcpy_");

		// result is at the address that the 'dst' register is pointing on
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
		
		fileWriter.format("\tblt $t%d,$t%d,%s\n",i1,i2,label);				
	}
	public void bge(TEMP oprnd1,TEMP oprnd2,String label)
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();
		
		fileWriter.format("\tbge $t%d,$t%d,%s\n",i1,i2,label);				
	}
	public void bne(TEMP oprnd1,TEMP oprnd2,String label)
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();
		
		fileWriter.format("\tbne $t%d,$t%d,%s\n",i1,i2,label);				
	}
	public void beq(TEMP oprnd1,TEMP oprnd2,String label)
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();
		
		fileWriter.format("\tbeq $t%d,$t%d,%s\n",i1,i2,label);				
	}
	public void beqz(TEMP oprnd1,String label)
	{
		int i1 =oprnd1.getSerialNumber();
				
		fileWriter.format("\tbeqz $t%d,%s\n",i1,label);
	}

	public void bltz(TEMP oprnd1,String label)
	{
		int i1 =oprnd1.getSerialNumber();
				
		fileWriter.format("\tbltz $t%d,%s\n",i1,label);
	}

	public void array_access(TEMP dst, TEMP arr, TEMP idx)
	{
		// RUNNING-TIME CHECKS : ----------------------------------

		beqz(arr, "invalid_ptr_dref_abort"); // checking the array is initialized (i.e. not null)

		// if (idx < 0) : abort
		bltz(idx, "index_out_of_range_abort"); 
		// size = arr[0] (we store the array size at the 0 offset)
		fileWriter.format("\tlw $s0,0($t%d)\n", arr.getSerialNumber());
		// if (idx >= size) : abort
		fileWriter.format("\tbge $t%d,$s0,%s\n", idx.getSerialNumber(), "index_out_of_range_abort");

		// ACCESSING AND STORING THE RESULTS : --------------------

		fileWriter.format("\tmove $s0,$t%d\n", idx.getSerialNumber());
		fileWriter.format("\tadd $s0,$s0,1\n");
		fileWriter.format("\tmul $s0,$s0,4\n");
		fileWriter.format("\tadd $s0,$s0,$t%d\n", arr.getSerialNumber());

		fileWriter.format("\tlw $t%d,0($s0)\n", dst.getSerialNumber());
	}

	public void array_set(TEMP arr, TEMP idx, TEMP value)
	{
		// RUNNING-TIME CHECKS : ------------------------------------------

		beqz(arr, "invalid_ptr_dref_abort"); // checking the array is initialized (i.e. not null)

		// if (idx < 0) : abort
		bltz(idx, "index_out_of_range_abort"); 
		// size = arr[0] (we store the array size at the 0 offset)
		fileWriter.format("\tlw $s0,0($t%d)\n", arr.getSerialNumber());
		// if (idx >= size) : abort
		fileWriter.format("\tbge $t%d,$s0,%s\n", idx.getSerialNumber(), "index_out_of_range_abort");

		// ACCESSING THE INDEX AND STORING THE VALUE : --------------------

		fileWriter.format("\tmove $s0,$t%d\n", idx.getSerialNumber());
		fileWriter.format("\tadd $s0,$s0,1\n");
		fileWriter.format("\tmul $s0,$s0,4\n");
		fileWriter.format("\tadd $s0,$s0,$t%d\n", arr.getSerialNumber());

		fileWriter.format("\tsw $t%d,0($s0)\n", value.getSerialNumber());
	}

	public void move_rv(TEMP dst)
	{
		fileWriter.format("\tmove $t%d,$v0\n", dst.getSerialNumber());
	}

	public void set_rv(TEMP src)
	{
		fileWriter.format("\tmove $v0,$t%d\n", src.getSerialNumber());
	}

	public void load_immediate(TEMP dst, int value)
	{
		fileWriter.format("\tli $t%d,%d\n", dst.getSerialNumber(), value);
	}

	public void add_prologue(int function_max_local_var_offset)
	{

		// STORE RETURN ADDRESS AND FRAME POINTER --------------------------

		fileWriter.format("\tsubu $sp,$sp,4\n");
		fileWriter.format("\tsw $ra,0($sp)\n");
		fileWriter.format("\tsubu $sp,$sp,4\n");
		fileWriter.format("\tsw $fp,0($sp)\n"); // store previous frame pointer

		jal("__prologue__");

		// CHANGE CURRENT STACK POINTER IN ORDER TO LEAVE A PLACE FOR ALL
		// THE LOCAL VARIABLES WE'LL DEFINE AT THE FUNCTION

		fileWriter.format("\tsubu $sp,$sp,%d\n", -(function_max_local_var_offset + 40));

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
		jump("__epilogue__");
	}

	// Allocates a class object instance
	public void allocate_class_obj(TYPE_CLASS cls, TEMP dst)
	{
		// Allocate the memory :
		fileWriter.format("\tli $v0,9\n");
		int k = cls.getFieldsAmount();
		fileWriter.format("\tli $a0,%d\n", (k+1)*4);
		fileWriter.format("\tsyscall\n");

		// Putting it at the dst register :
		fileWriter.format("\tmove $t%d,$v0\n", dst.getSerialNumber());

		// Storing for it its vtable :
		fileWriter.format("\tla $s0,vt_%s\n", cls.name);
		fileWriter.format("\tsw $s0,0($t%d)\n", dst.getSerialNumber());

		int off = 4;

		// Initializing it :
		for (TYPE_CLASS_FIELD f : cls.getClassFields())
		{
			if ((f.hasInitialValue()) && (f.initial_value != null))
			{
				if (f.initial_value instanceof String)
				{
					init_string_field(cls, dst, f);
				}

				if (f.initial_value instanceof Integer)
				{
					fileWriter.format("\tli $s0,%d\n", (int) f.initial_value);
					fileWriter.format("\tsw $s0,%d($t%d)\n", off,
																dst.getSerialNumber());
				}
			}
			else
			{
				// it has not initial value (or it's null and will be initialized to 0)
				fileWriter.format("\tli $s0,0\n");
				fileWriter.format("\tsw $s0,%d($t%d)\n", off,
															dst.getSerialNumber());
			}

			off += 4;
		}
	}

	// an auxiliary function for the above one
	public void init_string_field(TYPE_CLASS cls, TEMP dst, TYPE_CLASS_FIELD f)
	{
		int f_offset = (cls.getFieldIndex(f.name) + 1) * WORD_SIZE;

		String label_name = f.getInitialClass().name + "_" + 
										f.name + "_const_field";
		// ^ : the name of the label that this field is stored in at the data section

		// allocate size for the new string field

		fileWriter.format("\tla $s0,%s\n", label_name);
		jal("_strsize_");
		fileWriter.format("\tmove $a0,$v0\n");
		fileWriter.format("\tadd $a0,$a0,1\n");
		fileWriter.format("\tli $v0,9\n");
		fileWriter.format("\tsyscall\n");
		fileWriter.format("\tmove $s0,$v0\n");

		fileWriter.format("\tsw $s0,%d($t%d)\n", f_offset, dst.getSerialNumber());

		// copy the content t is pointing on to the address of the field
		// of the newly allocated class object

		fileWriter.format("\tla $s1,%s\n", label_name);

		jal("_strcpy_"); // copy's t's content into the address at 'f_address'. $s0 == destination, $s1 == source
	}

	// Allocates an array from the specified type and size and puts it at the
	// 'dst' register
	public void allocate_array(TEMP dst, TEMP size)
	{
		// Find the number of bytes needed :
		fileWriter.format("\tmove $a0,$t%d\n", size.getSerialNumber());
		fileWriter.format("\taddu $a0, $a0, 1\n");
		fileWriter.format("\tmul $a0,$a0,%d\n", WORD_SIZE);

		// Allocate the memory :
		fileWriter.format("\tli $v0,9\n");
		fileWriter.format("\tsyscall\n");
		fileWriter.format("\tmove $t%d,$v0\n", dst.getSerialNumber());

		// Initialize array's size :
		fileWriter.format("\tsw $t%d,0($t%d)\n", size.getSerialNumber(),
														dst.getSerialNumber());
	}

	public void push_args(TEMP_LIST args, boolean leave_hole)
	{
		int args_amount = 0;

		if (args != null)
		{
			args_amount = args.length();
		}

		fileWriter.format("\tsubu $sp,$sp,%d\n", WORD_SIZE * args_amount);

		if (args != null)
		{
			for (TEMP t : args)
			{
				fileWriter.format("\tsw $t%d,0($sp)\n", t.getSerialNumber());
				fileWriter.format("\taddu $sp,$sp,4\n");
			}
		}

		fileWriter.format("\tsubu $sp,$sp,%d\n", WORD_SIZE * args_amount);

		if (leave_hole)
		{
			// we leave a hole for uniformity among virtual and regular function calls
			fileWriter.format("\tsubu $sp,$sp,4\n");
		}
	}

	// A class method call : 
	public void virtual_call(TEMP src, TEMP_LIST args, TYPE_CLASS cls, String func_name)
	{
		TEMP_LIST final_args = new TEMP_LIST(src, args);

		push_args(final_args, false);

		// loads the vtable address
		fileWriter.format("\tlw $s0,0($t%d)\n", src.getSerialNumber());

		// finds the function offset at the vtable
		int off = cls.getMethodNumber(func_name) * WORD_SIZE;  

		// loads the address of the specified function
		fileWriter.format("\tlw $s1, %d($s0)\n", off);  

		fileWriter.format("\tjalr $s1\n");
	}

	// A regular function call : 
	public void call(TEMP_LIST args, String func_name)
	{
		push_args(args, true);

		fileWriter.format("\tjal %s\n", func_name);
	}

	public static int const_str_cnt = 0;

	public void allocate_string(TEMP dst, String str)
	{
		fileWriter.format(".data\n");
		fileWriter.format("\tconst_str_%d: .asciiz %s\n", const_str_cnt, str);
		fileWriter.format(".text\n");
		fileWriter.format("\tla $t%d,const_str_%d\n", dst.getSerialNumber(),
														 const_str_cnt);

		const_str_cnt ++;
	}

	public void init_global_var(String var_name)
	{
		fileWriter.print(".data\n");
		fileWriter.format("\tglobal_%s: .word 0\n", var_name);  // pointer's initialized to null

		fileWriter.print(".text\n");
 	}

	public void init_class_vtable(TYPE_CLASS cls)
	{
		// HEADER :
		fileWriter.format(".data\n");
		fileWriter.format("vt_%s:\n", cls.name);

		// DEFINING THE METHODS :
		LinkedList<String> methods_names_lst = cls.getMethodsNames();

		TYPE_CLASS method_implementor_cls;

		int curr_off = 0;

		for (String method_name : methods_names_lst)
		{
			// the first (from top) class at the hierarchy that implements the method
			method_implementor_cls = cls.getMethodImplementor(method_name);

			fileWriter.format("\t.word %s\n", method_implementor_cls.name + "_" + method_name);
		}

		// initializing class' string fields :

		fileWriter.format("non_inherited_fields_initialization_%s:\n", cls.name);

		LinkedList<TYPE_CLASS_FIELD> cls_non_inherited_fields = 
													cls.getClassNonInheritedFields();
		
		for (TYPE_CLASS_FIELD f : cls_non_inherited_fields)
		{
			if ((f.hasInitialValue()) && (f.initial_value instanceof String))
			{
				String string_label_name = cls.name + "_" + f.name + "_const_field";
				fileWriter.format("%s: .asciiz %s\n", string_label_name,
															f.initial_value);
			}
		}

		// switching back to the text section : -----------------------------

		fileWriter.format(".text\n");
	}

	public void field_set(TEMP obj, TYPE_CLASS cls, String field_name, TEMP value)
	{
		fileWriter.format("\tbeqz $t%d,invalid_ptr_dref_abort\n", obj.getSerialNumber()); // null pointer check

		// ELSE :

		int field_offset = (cls.getFieldIndex(field_name) + 1) * WORD_SIZE;

		fileWriter.format("\tsw $t%d,%d($t%d)\n", value.getSerialNumber(),
														field_offset,
														obj.getSerialNumber());
	}

	public void field_access(TEMP dst, TEMP obj, TYPE_CLASS cls, String field_name)
	{
		fileWriter.format("\tbeqz $t%d,invalid_ptr_dref_abort\n", obj.getSerialNumber()); // null pointer check

		// ELSE :

		int field_offset = (cls.getFieldIndex(field_name) + 1) * WORD_SIZE;

		fileWriter.format("\tlw $t%d,%d($t%d)\n", dst.getSerialNumber(),
														field_offset,
														obj.getSerialNumber());
	}

	public void jump_ra()
	{
		fileWriter.format("\tjr $ra\n");
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

			instance.initialize();
		}

		return instance;
	}

	// initializing the abort labels and the string methods
	public void initialize()
	{
		/******************************************************/
		/* initialize data section with error message strings */
		/******************************************************/
		fileWriter.print(".data\n");
		fileWriter.print("string_access_violation: .asciiz \"Access Violation\"\n");
		fileWriter.print("string_illegal_div_by_0: .asciiz \"Division By Zero\"\n");
		fileWriter.print("string_invalid_ptr_dref: .asciiz \"Invalid Pointer Dereference\"\n");
		
		fileWriter.print(".text\n");

		init_main();

		init_prologue();
		init_epilogue();
		init_aborts();
		init_strcmp();
		init_strsize();
		init_strcpy();
		init_stradd();
		init_print_int();
		init_print_string();
		init_bind_int();
	}

	public void init_main()
	{
		label("main");
		fileWriter.print("\tjal global_initializations\n");
		fileWriter.print("\tjal user_main\n");
		
		label("exit");
		fileWriter.print("\tli $v0,10\n");
		fileWriter.print("\tsyscall\n");
	}

	public void init_aborts()
	{
		label("index_out_of_range_abort");
		fileWriter.print("\tla $a0,string_access_violation\n");
		fileWriter.print("\tli $v0,4\n");
		fileWriter.print("\tsyscall\n");
		jump("exit");

		label("division_by_zero_abort");
		fileWriter.print("\tla $a0,string_illegal_div_by_0\n");
		fileWriter.print("\tli $v0,4\n");
		fileWriter.print("\tsyscall\n");
		jump("exit");

		label("invalid_ptr_dref_abort");
		fileWriter.print("\tla $a0,string_invalid_ptr_dref\n");
		fileWriter.print("\tli $v0,4\n");
		fileWriter.print("\tsyscall\n");
		jump("exit");		
	}

	public void init_strcmp()
	{
		label("_strcmp_");
		// $s0 = oprnd1
		// $s1 = oprnd2
		// result is stored at $v0

		add_backup_save_registers();

		fileWriter.print("\tli $v0,1\n");
		label("_strcmp_loop_");
		fileWriter.print("\tlb $s2,0($s0)\n");
		fileWriter.print("\tlb $s3,0($s1)\n");
		fileWriter.print("\tbne $s2,$s3,_strcmp_noteq_\n");
		fileWriter.print("\tbeq $s2,0,_strcmp_end_\n");
		fileWriter.print("\taddu $s0,$s0,1\n");
		fileWriter.print("\taddu $s1,$s1,1\n");
		jump("_strcmp_loop_");

		label("_strcmp_noteq_");
		fileWriter.print("\tli $v0,0\n");

		label("_strcmp_end_");

		add_restore_save_registers();
		fileWriter.print("\tjr $ra\n");
	}

	public void init_strsize()
	{
		label("_strsize_");
		// $s0 = oprnd
		// result is stored at $v0

		add_backup_save_registers();

		fileWriter.print("\tli $v0,0\n");

		label("_strsize_loop_");
		fileWriter.print("\tlb $s2,0($s0)\n");
		fileWriter.print("\tbeqz $s2,_strsize_end_\n"); // we got to the '\0' char
		fileWriter.print("\taddu $s0,$s0,1\n");
		fileWriter.print("\tadd $v0,$v0,1\n"); // increment the counted size
		jump("_strsize_loop_");
	
		label("_strsize_end_");
		add_restore_save_registers();

		fileWriter.print("\tjr $ra\n");
	}

	public void init_stradd()
	{
		label("_stradd_");
		// $s0 = oprnd1
		// $s1 = oprnd2
		// result is stored at $v0

		add_backup_save_registers();

		fileWriter.format("\tmove $s5,$s0\n");
		fileWriter.format("\tmove $s6,$s1\n");

		// calculate $s0's size :
		fileWriter.format("\tmove $s0,$s0\n");
		jal("_strsize_");
		fileWriter.format("\tmove $s2, $v0\n");
		// ^ : store the result at $s2

		// calculate $s1's size :
		fileWriter.format("\tmove $s0,$s1\n");
		jal("_strsize_");
		fileWriter.format("\taddu $s2,$s2,$v0\n");
		// ^ : $s2 contains the total size of the strings

		// allocate memory for the new string
		fileWriter.print("\tmove $a0,$s2\n");
		fileWriter.print("\taddu $a0,$a0,1\n");
		fileWriter.print("\tli $v0,9\n");
		fileWriter.print("\tsyscall\n");

		fileWriter.print("\tmove $s3,$v0\n"); // move to $s3 the allocated memory address

		fileWriter.print("\tmove $s0,$s5\n");
		fileWriter.print("\tmove $s1,$s6\n");

		// copy the new strings to the newly allocated place
		label("_stradd_loop_1_");

		fileWriter.print("\tlb $s2,0($s0)\n");
		fileWriter.print("\tbeqz $s2,_stradd_loop_2_\n");
		fileWriter.print("\tsb $s2,0($s3)\n");
		fileWriter.print("\taddu $s3,$s3,1\n");
		fileWriter.print("\taddu $s0,$s0,1\n");
		jump("_stradd_loop_1_");

		label("_stradd_loop_2_");

		fileWriter.print("\tlb $s2,0($s1)\n");
		fileWriter.print("\tbeqz $s2,_stradd_end_\n");
		fileWriter.print("\tsb $s2,0($s3)\n");
		fileWriter.print("\taddu $s3,$s3,1\n");
		fileWriter.print("\taddu $s1,$s1,1\n");
		jump("_stradd_loop_2_");

		label("_stradd_end_");

		fileWriter.print("\tsb $s2,0($s3)\n");
		add_restore_save_registers();

		fileWriter.print("\tjr $ra\n");
	}

	public void init_strcpy()
	{
		label("_strcpy_");
		// $s0 = dst
		// $s1 = src
		
		add_backup_save_registers();
		
		label("_strcpy_loop_");
		fileWriter.print("\tlb $s2,0($s1)\n");
		fileWriter.print("\tbeqz $s2,_strcpy_end_\n");
		fileWriter.print("\tsb $s2,0($s0)\n");
		fileWriter.print("\taddu $s0,$s0,1\n");
		fileWriter.print("\taddu $s1,$s1,1\n");
		jump("_strcpy_loop_");
	
		label("_strcpy_end_");
		fileWriter.print("\tlb $s2,0($s0)\n");

		add_restore_save_registers();

		fileWriter.print("\tjr $ra\n");
	}

	public void init_print_int()
	{
		label("PrintInt");

		add_prologue(-40);

		// backup $a0
		fileWriter.print("\tsubu $sp,$sp,4\n");
		fileWriter.print("\tsw $a0,0($sp)\n");

		// print the int
		fileWriter.print("\tlw $a0, 12($fp)\n"); // first argument is at offset 12
		fileWriter.print("\tli $v0, 1\n");
		fileWriter.print("\tsyscall\n");

		// print " " (space)
		fileWriter.print("\tli $a0, 32\n"); // 32 == code for SPACE
		fileWriter.print("\tli $v0, 11\n"); // system call for printing a character
		fileWriter.print("\tsyscall\n");

		// restore $a0
		fileWriter.print("\tlw $a0, 0($sp)\n");
		fileWriter.print("\taddu $sp,$sp,4\n");

		// return
		add_epilogue();
	}

	public void init_print_string()
	{
		label("PrintString");

		add_prologue(-40);

		// backup $a0
		fileWriter.print("\tsubu $sp,$sp,4\n");
		fileWriter.print("\tsw $a0,0($sp)\n");

		// print
		fileWriter.print("\tlw $a0, 12($fp)\n"); // first argument is at offset 12
		fileWriter.print("\tli $v0, 4\n");
		fileWriter.print("\tsyscall\n");

		// restore $a0
		fileWriter.print("\tlw $a0, 0($sp)\n");
		fileWriter.print("\taddu $sp,$sp,4\n");

		// return
		add_epilogue();
	}

	public void init_prologue()
	{
		label("__prologue__");

		// MOVE CURRENT STACK POINTER TO BE CURRENT FRAME POINTER ----------
		fileWriter.format("\tmove $fp,$sp\n");

		// STORE TEMPORAL REGISTERS ----------------------------------------

		for(int i=0; i<=9; i++)
		{
			fileWriter.format("\tsubu $sp,$sp,4\n");
			fileWriter.format("\tsw $t%d,0($sp)\n", i);
		}

		fileWriter.print("\tjr $ra\n");
	}

	public void init_epilogue()
	{
		label("__epilogue__");

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
			fileWriter.format("\tlw $t%d,0($fp)\n", i);
		}

		// move frame pointer to its previous location :
		fileWriter.format("\taddu $fp,$fp,40\n");

		// load the previous fp and the return address :
		fileWriter.format("\tlw $s0,0($fp)\n"); // $s0 = previous_fp
		fileWriter.format("\tlw $ra,4($fp)\n"); // $ra = return_address

		fileWriter.format("\taddu $sp,$fp,8\n"); // $sp = $fp + 8

		fileWriter.format("\tmove $fp,$s0\n");  // $fp = $s0 = previous_fp

		fileWriter.format("\tjr $ra\n");        // jump to the return_address
	}

	public void init_bind_int()
	{
		// $src = $s0
		// minimum = $s1
		// maximum = $s2
		// $dst = $v0

		label("_bind_int_");

		add_backup_save_registers();

		fileWriter.format("\tble $s0,$s1,_bind_int_minimum_\n");
		fileWriter.format("\tbge $s0,$s2,_bind_int_maximum_\n");

		// else : it is exactly in the range :
		jump("_bind_int_end_");

		label("_bind_int_minimum_");
		fileWriter.format("\tmove $s0,$s1\n");
		jump("_bind_int_end_");

		label("_bind_int_maximum_");
		fileWriter.format("\tmove $s0,$s2\n");

		label("_bind_int_end_");
		fileWriter.format("\tmove $v0,$s0\n");
		add_restore_save_registers();

		fileWriter.format("\tjr $ra\n");
	}


	// adds a section of code for backuping the save registers
	public void add_backup_save_registers()
	{
		for(int i=7; i>=0; i--)
		{
			fileWriter.print("\tsubu $sp,$sp,4\n");
			fileWriter.format("\tsw $s%d,0($sp)\n", i);
		}

		fileWriter.print("\tsubu $sp,$sp,4\n");
		fileWriter.print("\tsw $ra,0($sp)\n");
	}

	// adds a section of code for restoring the save registers
	public void add_restore_save_registers()
	{
		fileWriter.print("\tlw $ra,0($sp)\n");
		fileWriter.print("\taddu $sp,$sp,4\n");

		for(int i=0; i<=7; i++)
		{
			fileWriter.format("\tlw $s%d,0($sp)\n", i);
			fileWriter.print("\taddu $sp,$sp,4\n");
		}
	}
}
