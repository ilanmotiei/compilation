package TYPES;

public class TYPE_ARRAY extends TYPE
{
    public TYPE elems_type;
    public String name; // The array name

    public TYPE_ARRAY(String name, TYPE elems_type)
    {
        this.name = name;
        this.elems_type = elems_type;

        this.type_name = "array";
    }

    /*************/
	/* isArray() */
	/*************/
	public boolean isArray(){ return true; }
}
