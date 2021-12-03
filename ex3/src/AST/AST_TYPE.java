package AST;

import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.TYPE;
import TYPES.TYPE_INT;
import TYPES.TYPE_STRING;
import TYPES.TYPE_VOID;

public class AST_TYPE extends AST_Node {
    int num; //decide which derivation we used
	String name;
	
	/* Class Constructor */
	public AST_TYPE(int num, String name)
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

	public TYPE SemantMe(){
		if (num == 0){
			return TYPE_INT.getInstance();
		}
		if (num == 1){
			return TYPE_STRING.getInstance();
		}
		if (num == 2){
			return TYPE_VOID.getInstance();
		}
		if (num == 3){
			return SYMBOL_TABLE.getInstance().find(this.name);
		}

		return null; // for code's compilation
	}
    
}
