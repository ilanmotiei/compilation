package AST;

import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.*;

public class AST_TYPE extends AST_Node {

    int num; // tells us which derivation we have used
	String name;
	public int line;
	
	/* Class Constructor */
	public AST_TYPE(int num, String name, int line)
	{
		SerialNumber = AST_Node_Serial_Number.getFresh(); // SET A UNIQUE SERIAL NUMBER

		// PRINT THE CORRESPONDING DERIVATION RULE
		if(num == 0) System.out.print("====================== type ->  TYPE_INT \n");
		if(num == 1) System.out.print("====================== type ->  TYPE_STRING \n");
		if(num == 2) System.out.print("====================== type ->  TYPE_VOID \n");
		if(num == 3) System.out.format("====================== type ->  ID(%s) \n", name);

		// COPY INPUT DATA NENBERS ... 
		this.num = num;
		this.name = name;

		this.line = line;
	}
	
	
	/* The printing message for a binop exp AST node */
	public void PrintMe()
	{
		String type="";

		// CONVERT OP to a printable sOP
		if (num == 0) {type = "TYPE_INT";}
		if (num == 1) {type = "TYPE_STRING";}
		if (num == 2) {type = "TYPE_VOID";}
		if (num == 3) {type = "ID";}		

		// AST NODE TYPE = AST BINOP EXP
		System.out.print("AST NODE TYPE\n");

		// PRINT Node to AST GRAPHVIZ DOT file
		if(num == 3){
			AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("ID(%s)", name));
		}
		else{
			AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("%s",type));
		}
	}

	public BOX SemantMe() throws Exception
	{
		if (num == 0){
			return new BOX(TYPE_INT.getInstance(), null, true);
		}
		if (num == 1){
			return new BOX(TYPE_STRING.getInstance(), null, true);
		}
		if (num == 2){
			return new BOX(TYPE_VOID.getInstance(), null, true);
		}
		if (num == 3){
			TYPE id_cls = SYMBOL_TABLE.getInstance().find(this.name);

			if (id_cls == null) 
			{
				// CLASS NAME WASN'T FOUND AT THE SYMBOL TABLE : THROW EXCEPTION
				String cls_name = this.getClass().getName();
				throw new Exception("SEMANTIC ERROR : " + this.line + " : " + cls_name);
			}

			if ( ( ! id_cls.is_class()) && ( ! id_cls.is_array()) )
			{
				// CLASS NAME WAS FOUND AT SYMBOL TABLE, BUT NOT AS A CLASS OR AS AN ARRAY DECLERATION : THROW EXCEPTION
				String cls_name = this.getClass().getName();
				throw new Exception("SEMANTIC ERROR : " + this.line + " : " + cls_name);
			}

			// ELSE

			return new BOX(id_cls);
		}

		// Shouldn't get here
		throw new Exception("SEMANTIC TYPE ERROR");
	}
    
}
