package AST;

import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.*;

public class AST_STMT_FUNCCALL extends AST_STMT{
    public AST_VAR var;
    public String func_name;
    public AST_EXP_LIST expList;

	//  Class Constructor
	public AST_STMT_FUNCCALL(AST_VAR var, String func_name, AST_EXP_LIST expList)
	{
		// SET A UNIQUE SERIAL NUMBER
		SerialNumber = AST_Node_Serial_Number.getFresh();

		// PRINT CORRESPONDING DERIVATION RULE
		System.out.format("====================== stmt -> [var DOT] ID(%s) LPAREN expList RPAREN SEMICOLON\n", func_name);

		// COPY INPUT DATA NENBERS
		this.var = var;
		this.func_name = func_name;
		this.expList = expList;
	}

	
	// The printing message for an assign statement AST node
	public void PrintMe()
	{
		// AST NODE TYPE = AST ASSIGNMENT STATEMENT
		System.out.print("AST NODE ID STMT\n");

		
		// RECURSIVELY PRINT VAR + EXP
		if (var != null) var.PrintMe();
		if (expList != null) expList.PrintMe();

		// PRINT Node to AST GRAPHVIZ DOT file
		if(var == null){
            if(expList == null){
                AST_GRAPHVIZ.getInstance().logNode(
			    SerialNumber,
			    String.format("ID stmt\n ID(%s)();\n", func_name));
            }
			else{
				AST_GRAPHVIZ.getInstance().logNode(
			    SerialNumber,
			    String.format("ID stmt\n ID(%s)(expList);\n", func_name));
			}
                
        }
        else{
            if(expList == null){
                AST_GRAPHVIZ.getInstance().logNode(
			    SerialNumber,
			    String.format("ID stmt\n var.ID(%s)();\n", func_name));
            }
			else{
				AST_GRAPHVIZ.getInstance().logNode(
			    SerialNumber,
			    String.format("ID stmt\n var.ID(%s)(expList);\n", func_name));
			}
        }
		
		// PRINT Edges to AST GRAPHVIZ DOT file
		if(var != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);
		if(expList != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,expList.SerialNumber);
	}

	public void SemantMe() throws Exception{

		/* ------------- CHEKING THE VALIDITY OF THE EXP LIST -------------- */

		TYPE_LIST args_types = this.expList.SemantMe();
		TYPE func_dec;
		
		if (this.var == null)
		{
			// THE CALL IS A GENERIC FUNCTION CALL

			func_dec = SYMBOL_TABLE.getInstance().find(this.func_name);
		}
		else
		{
			// WE'RE CALLING FOR A CLASS METHOD ON AN INSTANCE OF IT

			TYPE var_type = this.var.SemantMe();

			if ( ! var_type.is_class())
			{
				// VARIABLE IS NOT A CLASS OBJECT : THROW EXCEPTION :
				throw new Exception("SEMANTIC ERROR");
			}

			func_dec = SYMBOL_TABLE.getInstance().find_by_hierarchy((TYPE_CLASS) var_type, this.func_name);
		}

		if (func_dec == null)
		{
			// THE FUNCTION WAS NOT DEFINED YET, OR NOT DEFINED FOR THE CLASS OF THE INSTANCE WHICH CALLED IT : THROW EXCEPTION

			throw new Exception("SEMANTIC ERROR");
		}

		// ELSE : 

		if ( ! func_dec.is_function())
		{
			// WE CALLED A VERIABLE/CLASS AS IT WAS A METHOD : THROW EXCEPTION :
			throw new Exception("SEMANTIC ERROR");
		}

		// ELSE : 

		if (((TYPE_FUNCTION) func_dec).AcceptableArgs(args_types) == false)
		{
			// THE GIVEN ARGUMENTS AREN'T ACCEPTABLE; THROW EXCEPTION :
			throw new Exception("SEMANTIC ERROR");
		}

		// ELSE : 
	
		// CALL IS VALID;
	}
}
