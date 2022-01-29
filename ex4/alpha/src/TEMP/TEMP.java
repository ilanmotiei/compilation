/***********/
/* PACKAGE */
/***********/
package TEMP;
import java.util.LinkedList;

/*******************/
/* GENERAL IMPORTS */
/*******************/

/*******************/
/* PROJECT IMPORTS */
/*******************/

public class TEMP
{
	private int serial=0;

	// the machine register that this temporal is being mapped to. 
	// varies from 0 to 9
	public int color = -1;

	// a boolean that tells us if the temp/node is currently the inference graph
	public boolean in_the_inference_graph = false; 

	// the temporary's neighbors at the inference graph
	public LinkedList<TEMP> neighbors;
	public LinkedList<TEMP> neighbors_copy;
	
	public TEMP(int serial)
	{
		this.serial = serial;
	}
	
	public int getSerialNumber()
	{
		if (color != -1)
		{
			return color;
		}

		// shouldn't get here; all temps should be mapped to a machine register
		// when this method is called

		return -1;
	}

	public void initialize()
	{
		color = -1;
		in_the_inference_graph = false;

		neighbors = new LinkedList<TEMP>();
		neighbors_copy = new LinkedList<TEMP>();
	}

	// Get the degree of the temporal at the inference graph
	public int getDegree()
	{
		return this.neighbors.size();
	}

	// removes this node from the inference graph by deleting the edges
	// between him and its neighbors
	public void remove_from_inference_graph()
	{
		for (TEMP neighbor : neighbors)
		{
			neighbor.neighbors.remove(this);
		}	
	}

	// called when node (temp) is gonna get a color at the coloring algorihtm
	public void return_to_inference_graph_n_color(int num_of_colors)
	{
		this.in_the_inference_graph = true; // this node backs to the graph

		LinkedList<Integer> neighbors_at_the_graph_colors = new LinkedList<>();
		
		for (TEMP ne : neighbors_copy)
		{
			if (ne.in_the_inference_graph)
			{
				neighbors_at_the_graph_colors.add(ne.color);
			}
		}

		for (int c = 0; c < num_of_colors; c ++)
		{
			if (! neighbors_at_the_graph_colors.contains(c))
			{
				// this temporary can be colored with the color c

				this.color = c;
				break;
			}
		}

		if (this.color == -1)
		{
			// we've failed to color the node;
			System.out.print("Fail at register allocation : couldn't color temporal\n");
			System.exit(1);
		}
	}
}
