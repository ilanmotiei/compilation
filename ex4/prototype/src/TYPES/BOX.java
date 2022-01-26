
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

    public Object initial_value = null;
    // optional - may contain the value if known at compilation time

    public BOX(TYPE type, String name, boolean is_const, boolean is_array, Object init_value)
    {
        this.type = type;
        this.name = name;
        this.is_const = is_const;
        this.is_array = is_array;
        this.initial_value = init_value;
    }

    public BOX(TYPE type, String name, boolean is_const, boolean is_array)
    {
        this.type = type;
        this.name = name;
        this.is_const = is_const;
        this.is_array = is_array;
    }

    public BOX(TYPE type, String name, boolean is_const, Object init_value)
    {
        this.type = type;
        this.name = name;
        this.is_const = is_const;

        this.initial_value = init_value;
    }

    public BOX(TYPE type, String name)
    {
        this.type = type;
        this.name = name;
    }

    public BOX(TYPE type, boolean is_const, Object initial_value)
    {
        this.type = type;
        this.is_const = is_const;

        this.initial_value = initial_value;
    }

    public BOX(TYPE type)
    {
        this.type = type;
    }
}