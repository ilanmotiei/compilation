/***********/
/* PACKAGE */
/***********/
package TEMP;

/*******************/
/* GENERAL IMPORTS */
/*******************/

/*******************/
/* PROJECT IMPORTS */
/*******************/

public class TEMP
{
	private int serial=0;

	// the temporary's neighbors at the inference graph
	public LinkedList<TEMP> neighbors = new LinkedList<>();
	
	public TEMP(int serial)
	{
		this.serial = serial;
	}
	
	public int getSerialNumber()
	{
		return serial;
	}

	// Get the degree of the temporal at the inference graph
	public int getDegree()
	{
		return this.neighbors.size();
	}
}
