package BP.GPM.AD;

import BP.*;
import BP.GPM.*;

public class Glo
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 公共变量.
	public static String getADBasePath()
	{
		return Sys.SystemConfig.AppSettings["ADBasePath"];
	}
	public static String getADUser()
	{
		return Sys.SystemConfig.AppSettings["ADUser"];
	}
	public static String getADPassword()
	{
		return Sys.SystemConfig.AppSettings["ADPassword"];
	}
	public static String getADPath()
	{
		return Sys.SystemConfig.AppSettings["ADPath"];
	}
	/** 
	 跟目录(主域)
	*/
	public static DirectoryEntry getDirectoryEntryBasePath()
	{
		DirectoryEntry domain = new DirectoryEntry();

		domain.Path = Glo.getADBasePath();
		domain.Username = Glo.getADUser();
		domain.Password = Glo.getADPassword();

			//domain.AuthenticationType = AuthenticationTypes.ReadonlyServer;

			// domain.RefreshCache();
		return domain;

	}
	/** 
	 
	*/
	public static DirectoryEntry getDirectoryEntryAppRoot()
	{
		DirectorySearcher search = new DirectorySearcher(Glo.getDirectoryEntryBasePath()); //查询组织单位.
		search.Filter = "(OU=" + Glo.getADPath() + ")";
		search.SearchScope = SearchScope.Subtree;

		SearchResult result = search.FindOne();
		if (result == null)
		{
			throw new RuntimeException("err@您配置的:ADAppRoot无效,正确的配置方法比如:chichengsoft 没有找到该节点." + Glo.getADPath());
		}
		DirectoryEntry de = result.GetDirectoryEntry();
		search.Dispose();

		return de;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 公共变量.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 相关方法.

	public static String GetPropertyValue(DirectoryEntry de, String propertyName)
	{
		if (de.Properties.Contains(propertyName))
		{
			return de.Properties[propertyName][0].toString();
		}
		else
		{
			return "";
		}
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 登录校验相关.
	public static native int LogonUser11(String lpszUsername, String lpszDomain, String lpszPassword, int dwLogonType, int dwLogonProvider, tangible.RefObject<IntPtr> phToken);
	static
	{
		System.loadLibrary("advapi32.DLL");
	}


	private static native int LogonUser(String lpszUserName, String lpszDomain, String lpszPassword, int dwLogonType, int dwLogonProvider, tangible.RefObject<IntPtr> phToken);
	static
	{
		System.loadLibrary("advapi32.dll");
	}
	private static native int DuplicateToken(IntPtr hToken, int impersonationLevel, tangible.RefObject<IntPtr> hNewToken);
	private static final int LOGON32_LOGON_INTERACTIVE = 2;
	private static final int LOGON32_PROVIDER_DEFAULT = 0;
	private static WindowsImpersonationContext impersonationContext = null;
	/** 
	 执行登录
	 
	 @param domain
	 @param userNo
	 @param pass
	*/
	public static boolean CheckLogin(String domain, String userNo, String pass)
	{

		DirectoryEntry entry = new DirectoryEntry(BP.GPM.AD.Glo.getADBasePath(), userNo, pass);
		DirectorySearcher search = new DirectorySearcher(entry); //创建DirectoryEntry对象的搜索对象
		search.Filter = "(SAMAccountName=" + userNo + ")"; //过滤条件为登录帐号＝user
		SearchResult result = search.FindOne(); //查找第一个
		if (null == result) //没找到
		{
			return false;
		}
		return true;

	}
		///#endregion 登录校验相关.

}