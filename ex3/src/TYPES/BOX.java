
package TYPES;

/* THIS TYPE PACKS THE RETURNED VALUES OF THE "SemantMe()" METHOD OF THE AST NODES */

public class BOX
{
    public TYPE type;
    public String name = null;
    public boolean is_const = false;

    public BOX(TYPE type, String name)
    {
        this.type = type;
        this.name = name;
    }

    public BOX(TYPE type, String name, boolean is_const)
    {
        this.type = type;
        this.name = name;
        this.is_const = is_const;
    }

    public BOX(TYPE type)
    {
        this.type = type;
    }
}