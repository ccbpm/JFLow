package BP.NetPlatformImpl;

public final class Sys_PubClass
{
	public static String getRequestParas()
	{
		String urlExt = "";
		String rawUrl = System.Web.HttpContext.Current.Request.RawUrl;
		rawUrl = "&" + rawUrl.substring(rawUrl.indexOf('?') + 1);
		String[] paras = rawUrl.split("[&]", -1);
		for (String para : paras)
		{
			if (para == null || para.equals("") || para.contains("=") == false)
			{
				continue;
			}
			urlExt += "&" + para;
		}
		return urlExt;
	}
}