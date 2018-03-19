package BP.Port;

public class Current
{
	static
	{
		Session = new java.util.Hashtable();
	}
	public static java.util.Hashtable Session;
	
	public static void SetSession(Object key, Object Value)
	{
		if (Session.containsKey(key))
		{
			Session.remove(key);
		}
		if (Value != null){
			Session.put(key, Value);
		}
	}
	
	public static String GetSessionStr(Object key, String isNullAsValue)
	{
		Object val = Session.get(key);
		if (val == null)
		{
			return isNullAsValue;
		}
		return (String) ((val instanceof String) ? val : null);
		// if (Session.ContainsKey(key))
		// {
		// Session.remove(key);
		// }
		// Session.Add(key, Value);
	}
}