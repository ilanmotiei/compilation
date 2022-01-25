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

public class IRcommandList
{
	public IRcommand head;
	public IRcommandList tail;

	IRcommandList(IRcommand head, IRcommandList tail)
	{
		this.head = head;
		this.tail = tail;
	}

	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		// MIPSme for the current head
		if (head != null) head.MIPSme();
		// recursive MIPSme for all the rest list
		if (tail != null) tail.MIPSme();
	}
}
