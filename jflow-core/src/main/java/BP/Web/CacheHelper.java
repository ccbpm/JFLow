package BP.Web;

public final class CacheHelper
{
	private static MemoryCache mc = MemoryCache.Default;

	public static boolean Contains(String key)
	{
		return mc.Contains(key, null);
	}

	public static <T> T Get(String key)
	{
		return (T)mc.Get(key, null);
	}

	public static <T> void Add(String key, T v)
	{
		CacheItemPolicy p = new CacheItemPolicy();
		mc.Add(key, (Object)v, p, null);
	}

	public static void Remove(String key)
	{
		mc.Remove(key, null);
	}
}