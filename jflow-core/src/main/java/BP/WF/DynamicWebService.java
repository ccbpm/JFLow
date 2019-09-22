package BP.WF;

import BP.WF.NetPlatformImpl.*;

/** 
 调用webservices.
*/
public class DynamicWebService
{
	/** 
	 调用webservices.
	*/
	private DynamicWebService()
	{

	}
	/** 
	 动态调用web服务
	 
	 @param url 链接串
	 @param methodname 方法名
	 @param args 参数
	 @return 
	*/
	public static Object InvokeWebService(String url, String methodName, Object[] args)
	{
		return DynamicWebService.InvokeWebService(url, null, methodName, args);
	}
	private static CookieContainer container = new CookieContainer();
	/** 
	 动态调用web服务
	 
	 @param url
	 @param classname
	 @param methodname
	 @param args
	 @return 
	*/
	public static Object InvokeWebService(String url, String className, String methodName, Object[] args)
	{
		return WF_DynamicWebService.InvokeWebService(url, className, methodName, args);
	}
}