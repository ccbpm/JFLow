package BP.Port;

import java.util.*;

public class Current
{
	static
	{
		Session = new Hashtable();
	}
	public static Hashtable Session;
	public static void SetSession(Object key, Object Value)
	{
		if (Session.containsKey(key))
		{
			Session.remove(key);
		}
		Session.put(key,Value);
	}
	public static String GetSessionStr(Object key, String isNullAsValue)
	{
		Object val = Session.get(key);
		if (val == null)
		{
			return isNullAsValue;
		}
		return val instanceof String ? (String)val : null;
		//if (Session.ContainsKey(key))
		//{
		//    Session.Remove(key);
		//}
		//Session.Add(key, Value);
	}
}