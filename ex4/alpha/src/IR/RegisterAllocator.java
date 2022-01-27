/***********/
/* PACKAGE */
/***********/
package RegisterAllocation;
/*******************/
/* GENERAL IMPORTS */
/*******************/

import java.util.LinkedList;

/*******************/
/* PROJECT IMPORTS */
/*******************/
import IR.*;

public class RegisterAllocator
{
    LinkedList<IRcommand> cmd_list = null;

    public RegisterAllocator()
    {
        cmd_list = new LinkedList<>();
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

            String searched_label_name
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

    
}
