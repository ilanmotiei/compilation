/***********/
/* PACKAGE */
/***********/
package RegisterAllocation;
/*******************/
/* GENERAL IMPORTS */
/*******************/

import java.util.*;

/*******************/
/* PROJECT IMPORTS */
/*******************/
import IR.*;
import TEMP.*;
import java.util.*;

public class RegisterAllocator
{
    LinkedList<IRcommand> cmd_list = null;

    public RegisterAllocator()
    {
        cmd_list = new LinkedList<>();
    }

    public void add_cmd(IRcommand cmd)
    {
        cmd_list.add(cmd);
    }

    public void AllocateRegisters()
    {
        BuildCFG();
        Perform_Liveness_Analysis();
        LinkedList<TEMP> all_func_temps = Build_Inference_Graph();
        ColorInferenceGraph(all_func_temps, 10);
    }

    // updates all of the 'jumps_to' lists of all the commands at the list.
    // note : if a command jumps to a command out of the function then that  
    //        jump will not affect the former's list
    public void BuildCFG()
    {
        IRcommand prev_cmd = null;

        for (IRcommand cmd : cmd_list)
        {
            if (prev_cmd != null)
            {
                prev_cmd.jumps_to.add(cmd);
            }

            // Treating jump commands : 

            String searched_label_name = null;
            boolean does_command_is_a_jump_cmd = false;

            if (cmd instanceof IRcommand_Jump_Label)
            {
                searched_label_name = ((IRcommand_Jump_Label) cmd).label_name;
                does_command_is_a_jump_cmd = true;
            }
            if (cmd instanceof IRcommand_Jump_If_Eq_To_Zero)
            {
                searched_label_name = ((IRcommand_Jump_If_Eq_To_Zero) cmd).label_name;
                does_command_is_a_jump_cmd = true;
            }

            if (does_command_is_a_jump_cmd)
            {
                IRcommand label_command = search_label(searched_label_name);

                if (label_command == null){
                    // label wasn't found at this function's command list
                }else{
                    // label was found as an IRcommand at this function 
                    cmd.jumps_to.add(label_command);
                }    
            }

            // updating the previous command to be the current one :
            prev_cmd = cmd;
        }
    }
    
    // returns the IRcommand that represents the searched label
    public IRcommand search_label(String searched_label_name)
    {
        for (IRcommand searched_cmd : cmd_list)
        {   
            if (searched_cmd instanceof IRcommand_Label)
            {
                if (((IRcommand_Label) searched_cmd).label_name.equals(searched_label_name))
                {
                    // we found the label (IRcommand) we were searching for

                    return searched_cmd;
                }
            }
        }

        return null;
    }

    public void Perform_Liveness_Analysis()
    {
        LinkedList<IRcommand> reveresed_list = new LinkedList<>();

        for (IRcommand cmd : cmd_list)
        {
            reveresed_list.addFirst(cmd);
        }

        for (int i=0; i <= 4; i++)
        {
            // perform the transform function of the commands in a reverse order
            for (IRcommand cmd : reveresed_list)
            {
                cmd.UpdateINList();
            }
        }
    }

    // Updates the neighbors of every TEMP according to the liveliness analysis
    // performed earlier and returns the list of all the TEMPs used at
    // the function defined by the given list of IRcommands
    // (The inference graph is represented as a adjacency *LIST*)
    public LinkedList<TEMP> Build_Inference_Graph()
    {
        LinkedList<TEMP> all_func_temps = new LinkedList<>();

        for (IRcommand cmd : cmd_list)
        {
            LinkedList<TEMP> cmd_temps = cmd.getCMDTemps();

            if (cmd_temps != null)
            {
                for (TEMP t : cmd_temps)
                {
                    if (! all_func_temps.contains(t)) {
                        all_func_temps.add(t);
                    }
                }
            }
        }

        for (TEMP t : all_func_temps)
        {
            t.initialize();
        }

        for (IRcommand cmd : cmd_list)
        {
            parse_command_temps(cmd);
        }

        return all_func_temps;
    }

    // initializes the neighbors of all the temps at the command
    public void parse_command_temps(IRcommand cmd)
    {
        LinkedList<TEMP> adj_temps = cmd.IN;

        for (TEMP t : adj_temps)
        {
            for (TEMP t1 : adj_temps)
            {
                if ((t1 != t) && (! t.neighbors.contains(t1)))
                {
                    t.neighbors.add(t1);
                    t.neighbors_copy.add(t1);
                }
            }
        }
    }

    public void ColorInferenceGraph(LinkedList<TEMP> adj_list, int num_colors)
    {
        // find the correct mapping and update the serialNumbers of the TEMPs

        LinkedList<TEMP> stack = new LinkedList<>();

        while (adj_list.size() != 0)
        {
            for (TEMP t : adj_list)
            {
                if (t.getDegree() < num_colors)
                {
                    // remove 't' from the graph (and all the edges he's connected to)
                    // and push it to the stack
                    
                    t.remove_from_inference_graph();
                    adj_list.remove(t);
                    stack.addFirst(t);
                    break;
                }
            }
        }

        for (TEMP top : stack)
        {
            // color it legally with respect to the coloring of the neighbors
            // of it that we've already returned to the inference graph

            top.return_to_inference_graph_n_color(num_colors);
        }
    }

    public LinkedList<IRcommand> getAllCMDs()
    {
        return this.cmd_list;
    }
}
