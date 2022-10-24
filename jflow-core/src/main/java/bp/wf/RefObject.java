package bp.wf;

public final class RefObject<T>
{
	public T argvalue;
	
	public RefObject(T refarg)
	{
		argvalue = (T) refarg;
	}
	
}