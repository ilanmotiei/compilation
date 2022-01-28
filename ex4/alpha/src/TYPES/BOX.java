
package TYPES;

/* THIS TYPE PACKS THE RETURNED VALUES OF THE "SemantMe()" METHOD OF THE AST NODES */

public class BOX
{
    public TYPE type;
    public String name = null;
    public boolean is_const = false;
    public boolean is_zero = false;
    public boolean is_negative = false;

    public boolean is_array = false;

    // optional - will be assigned a value if known at compilation time and is contant
    public Object initial_value = null; 

    public BOX(TYPE type, String name, boolean is_const, boolean is_array)
    {
        this.type = type;
        this.name = name;
        this.is_const = is_const;
        this.is_array = is_array;
    }

    public BOX(TYPE type, String name, boolean is_const)
    {
        this.type = type;
        this.name = name;
        this.is_const = is_const;
    }

    public BOX(TYPE type, String name, Object initial_value)
    {
        this.type = type;
        this.name = name;

        this.initial_value = initial_value;
    }

    public BOX(TYPE type, String name)
    {
        this.type = type;
        this.name = name;
    }

    public BOX(TYPE type, boolean is_const)
    {
        this.type = type;
        this.is_const = is_const;
    }

    public BOX(TYPE type)
    {
        this.type = type;
    }
}