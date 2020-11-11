package bp.web;
import bp.da.*;
import bp.difference.ContextHolderUtils;
import bp.difference.SystemConfig;
import bp.port.*;

/** 
 客户的用户信息。
*/
public class GuestUser
{
	/** 
	 通用的登陆
	 
	 @param guestNo
	 @param guestName
	 @param lang
	 @param isRememberMe
	 * @throws Exception 
	*/

	public static void SignInOfGener(String guestNo, String guestName, String lang) throws Exception
	{
		SignInOfGener(guestNo, guestName, lang, true);
	}

	public static void SignInOfGener(String guestNo, String guestName) throws Exception
	{
		SignInOfGener(guestNo, guestName, "CH", true);
	}


	public static void SignInOfGener(String guestNo, String guestName, String lang, boolean isRememberMe) throws Exception
	{
		SignInOfGener(guestNo, guestName, "deptNo", "deptName", lang, isRememberMe);
	}
	/** 
	 通用的登陆
	 
	 @param guestNo 客户编号
	 @param guestName 客户名称
	 @param deptNo 部门编号
	 @param deptName 部门名称
	 @param lang 语言
	 @param isRememberMe 是否记忆我
	 * @throws Exception 
	*/
	public static void SignInOfGener(String guestNo, String guestName, String deptNo, String deptName, String lang, boolean isRememberMe) throws Exception
	{
		if (ContextHolderUtils.getInstance() == null)
		{
			SystemConfig.setIsBSsystem(false);
		}
		else
		{
			SystemConfig.setIsBSsystem(true);
		}

		//记录客人信息.
		GuestUser.setNo(guestNo);
		GuestUser.setName(guestName);

		//记录内部客户信息.
		bp.port.Emp em = new Emp();
		em.setNo("Guest");
		if (em.RetrieveFromDBSources() == 0)
		{
			em.setName("客人");
			em.Insert();
		}
		WebUser.SignInOfGener(em);
		return;
	}


		///静态方法
	/** 
	 通过key,取出session.
	 
	 @param key key
	 @param isNullAsVal 如果是Null, 返回的值.
	 @return 
	 * @throws Exception 
	*/
	public static String GetSessionByKey(String key, String isNullAsVal) throws Exception
	{
		if (getIsBSMode())
		{
			String str = (String) ContextHolderUtils.getSession().getAttribute(key);
			if (DataType.IsNullOrEmpty(str))
			{
				str = isNullAsVal;
			}
			return str;
		}
		else
		{
			if (bp.pub.Current.Session.get(key) == null || bp.pub.Current.Session.get(key).toString().equals(""))
			{
				bp.pub.Current.Session.put(key, isNullAsVal);
				return isNullAsVal;
			}
			else
			{
				return (String)bp.pub.Current.Session.get(key);
			}
		}
	}

	/** 
	 是不是b/s 工作模式。
	 * @throws Exception 
	*/
	protected static boolean getIsBSMode() throws Exception
	{
			//2019-07-25 zyt改造
		if (ContextHolderUtils.getInstance() == null)
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	public static Object GetSessionByKey(String key, Object defaultObjVal) throws Exception
	{
		if (getIsBSMode())
		{
			Object obj = ContextHolderUtils.getSession().getAttribute(key);
			if (obj == null)
			{
				return defaultObjVal;
			}
			else
			{
				return obj;
			}
		}
		else
		{
			if (bp.pub.Current.Session.get(key) == null)
			{
				return defaultObjVal;
			}
			else
			{
				return bp.pub.Current.Session.get(key);
			}
		}
	}

	/** 
	 设置session
	 
	 @param key 键
	 @param val 值
	 * @throws Exception 
	*/
	public static void SetSessionByKey(String key, Object val) throws Exception
	{
		if (val == null)
		{
			return;
		}
		if (getIsBSMode())
		{
			ContextHolderUtils.getSession().setAttribute(key, val);
		}
		else
		{
			bp.pub.Current.SetSession(key, val);
		}
	}
	/** 
	 退回
	*/
	public static void Exit()
	{
		
		try {
			// 清理Session
			WebUser.setNo(null);
			WebUser.setName(null);
			WebUser.setFK_Dept(null);
			WebUser.setFK_DeptName(null);
			WebUser.setSID(null);
			WebUser.setAuth(null);
			WebUser.setSysLang(null);
			if (SystemConfig.getIsBSsystem()) {
				
				ContextHolderUtils.addCookie("No",  null);
				ContextHolderUtils.addCookie("Name", null);
				ContextHolderUtils.addCookie("IsRememberMe", null);
				ContextHolderUtils.addCookie("FK_Dept", null);
				ContextHolderUtils.addCookie("FK_DeptName",  null);
				ContextHolderUtils.addCookie("Token",null);
				ContextHolderUtils.addCookie("SID",  null);
				ContextHolderUtils.addCookie("Lang", null);
				ContextHolderUtils.addCookie("Auth", null);
			}
		} catch (java.lang.Exception e2) {}
		
		
	}
	/** 
	 编号
	 * @throws Exception 
	*/
	public static String getNo() throws Exception
	{
		return WebUser.GetValFromCookie("GuestNo", null, true);
	}
	public static void setNo(String value) throws Exception
	{
		WebUser.SetSessionByKey("GuestNo", value.trim()); //@祝梦娟.
	}
	/** 
	 名称
	*/
	public static String getName()throws Exception
	{
		String val = WebUser.GetValFromCookie("GuestName", null, true);
		if (val == null)
		{
			throw new RuntimeException("@err-001 GuestName 登录信息丢失。");
		}
		return val;
	}
	public static void setName(String value) throws Exception
	{
		WebUser.SetSessionByKey("GuestName", value);
	}
}