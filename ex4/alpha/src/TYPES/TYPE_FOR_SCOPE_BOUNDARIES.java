package TYPES;

public class TYPE_FOR_SCOPE_BOUNDARIES extends TYPE
{

	private TYPE scope_wrapper; // The class/function that wraps this scope
	
	public TYPE_FOR_SCOPE_BOUNDARIES(TYPE scop_wrapper)
	{
		if (scope_wrapper != null){
			if ( ! (scope_wrapper.is_class() || scop_wrapper.is_function()))
			{
				System.out.println("Scope wrapper is not a class or a function");
			}
		}

		this.type_name = "SCOPE-BOUNDARY";
		this.scope_wrapper = scop_wrapper;
	}

	public boolean isClassWrapper()
	{
		return (scope_wrapper != null) && scope_wrapper.is_class();
	}

	public boolean isFunctionWrapper()
	{
		return (scope_wrapper != null) && scope_wrapper.is_function();
	}

	public TYPE getScopeWrapper()
	{
		return this.scope_wrapper;
	}
}
