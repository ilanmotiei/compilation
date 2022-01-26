/***********/
/* PACKAGE */
/***********/
package IR;

/*******************/
/* GENERAL IMPORTS */
/*******************/

/*******************/
/* PROJECT IMPORTS */
/*******************/
import TEMP.*;
import MIPS.*;

public class IRcommand_ArrayAccess extends IRcommand
{
	TEMP dst;
    TEMP src;  // The register that contains the array address
    TEMP idx;
	
	public IRcommand_ArrayAccess(TEMP dst, TEMP src, TEMP idx)
	{
		this.dst = dst;
        this.src = src;
        this.idx = idx;
	}
	
	public void MIPSme()
	{
		MIPSGenerator.getInstance().array_access(dst, src, idx);
	}
}
