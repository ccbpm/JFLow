package BP.Web;

import BP.En.*;
import BP.DA.*;
import BP.Port.*;
import BP.Pub.*;
import BP.Sys.*;
import java.util.*;
import java.time.*;

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
	*/
	public static void SignInOfGener(String guestNo, String guestName, String lang, boolean isRememberMe)
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
	*/
	public static void SignInOfGener(String guestNo, String guestName, String deptNo, String deptName, String lang, boolean isRememberMe)
	{
		//2019-07-25 zyt改造
		if (HttpContextHelper.getCurrent() == null)
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
		GuestUser.setDeptNo(deptNo);
		GuestUser.setDeptName(deptName);

		//记录内部客户信息.
		BP.Port.Emp em = new Emp();
		em.setNo("Guest");
		if (em.RetrieveFromDBSources() == 0)
		{
			em.setName("客人");
			em.Insert();
		}
		WebUser.setNo(em.getNo());
		WebUser.setName(em.getName());
		WebUser.setFK_Dept(em.getFK_Dept());
		WebUser.setFK_DeptName(em.getFK_DeptText());
		WebUser.setSysLang(lang);
		if (BP.Sys.SystemConfig.getIsBSsystem())
		{
			// Guest  信息.
			/*HttpCookie cookie = new HttpCookie("CCSGuest");
			//cookie.Expires = DateTime.Now.AddMonths(10);
			cookie.Expires = DateTime.Now.AddDays(2);
			cookieValues.Add("GuestNo", guestNo);
			cookieValues.Add("GuestName", HttpUtility.UrlEncode(guestName));
			cookieValues.Add("DeptNo", deptNo);
			cookieValues.Add("DeptName", HttpUtility.UrlEncode(deptName));

			//System.Web.HttpContext.Current.Response.AppendCookie(cookie); //加入到会话。
			HttpContextHelper.ResponseCookieAdd(cookie);
			*/

			HashMap<String, String> cookieValues = new HashMap<String, String>();
			// HttpCookie cookie = new HttpCookie("CCS");
			//cookie.Expires = DateTime.Now.AddDays(2);

			// Guest  信息.
			cookieValues.put("GuestNo", guestNo);
			cookieValues.put("GuestName", HttpUtility.UrlEncode(guestName));

			cookieValues.put("DeptNo", deptNo);
			cookieValues.put("DeptName", HttpUtility.UrlEncode(deptName));

			cookieValues.put("No", "Guest");
			cookieValues.put("Name", HttpUtility.UrlEncode(em.getName()));

			if (isRememberMe)
			{
				cookieValues.put("IsRememberMe", "1");
			}
			else
			{
				cookieValues.put("IsRememberMe", "0");
			}

			cookieValues.put("FK_Dept", em.getFK_Dept());
			cookieValues.put("FK_DeptName", HttpUtility.UrlEncode(em.getFK_DeptText()));

			cookieValues.put("Token", HttpContextHelper.getCurrentSessionID());
			cookieValues.put("SID", HttpContextHelper.getCurrentSessionID());

			cookieValues.put("Lang", lang);
			cookieValues.put("Style", "0");
			cookieValues.put("Auth", ""); //授权人.
			HttpContextHelper.ResponseCookieAdd(cookieValues, LocalDateTime.now().plusDays(2), "CCS");
		}
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 静态方法
	/** 
	 通过key,取出session.
	 
	 @param key key
	 @param isNullAsVal 如果是Null, 返回的值.
	 @return 
	*/
	public static String GetSessionByKey(String key, String isNullAsVal)
	{
		if (getIsBSMode())
		{
			String str = HttpContextHelper.SessionGetString(key);
			if (DataType.IsNullOrEmpty(str))
			{
				str = isNullAsVal;
			}
			return str;
		}
		else
		{
			if (BP.Port.Current.Session.get(key) == null || BP.Port.Current.Session.get(key).toString().equals(""))
			{
				BP.Port.Current.Session.put(key, isNullAsVal);
				return isNullAsVal;
			}
			else
			{
				return (String)BP.Port.Current.Session.get(key);
			}
		}
	}
	/* 2019-7-25 张磊注释，net core中需要知道object的具体类型才行（不能被序列化的对象，无法放入session中）
	public static object GetObjByKey(string key)
	{
	    if (IsBSMode)
	    {
	        return System.Web.HttpContext.Current.Session[key];
	    }
	    else
	    {
	        return BP.Port.Current.Session[key];
	    }
	}*/
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

	/** 
	 是不是b/s 工作模式。
	*/
	protected static boolean getIsBSMode()
	{
			//2019-07-25 zyt改造
		if (HttpContextHelper.getCurrent() == null)
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	public static Object GetSessionByKey(String key, Object defaultObjVal)
	{
		if (getIsBSMode())
		{
			Object obj = HttpContextHelper.SessionGet(key);
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
			if (BP.Port.Current.Session.get(key) == null)
			{
				return defaultObjVal;
			}
			else
			{
				return BP.Port.Current.Session.get(key);
			}
		}
	}

	/** 
	 设置session
	 
	 @param key 键
	 @param val 值
	*/
	public static void SetSessionByKey(String key, Object val)
	{
		if (val == null)
		{
			return;
		}
		if (getIsBSMode())
		{
			HttpContextHelper.SessionSet(key, val);
		}
		else
		{
			BP.Port.Current.SetSession(key, val);
		}
	}
	/** 
	 退回
	*/
	public static void Exit()
	{
		if (getIsBSMode() == false)
		{
			try
			{
				String token = WebUser.getToken();

				HttpContextHelper.ResponseCookieDelete(new String[] {"No", "Name", "Pass", "IsRememberMe", "Auth", "AuthName"}, "CCS");

				BP.Port.Current.Session.clear();

				/* 2019-07-25 张磊 注释掉，CCSGuest 不再使用
				// Guest  信息.
				cookie = new HttpCookie("CCSGuest");
				cookie.Expires = DateTime.Now.AddDays(2);
				cookie.Values.Add("GuestNo", string.Empty);
				cookie.Values.Add("GuestName", string.Empty);
				cookie.Values.Add("DeptNo", string.Empty);
				cookie.Values.Add("DeptName", string.Empty);
				System.Web.HttpContext.Current.Response.AppendCookie(cookie); //加入到会话。
				*/
			}
			catch (java.lang.Exception e)
			{
			}
		}
		else
		{
			try
			{
				BP.Port.Current.Session.clear();

				HttpContextHelper.ResponseCookieDelete(new String[] {"No", "Name", "Pass", "IsRememberMe", "Auth", "AuthName"}, "CCS");

				HttpContextHelper.SessionClear();

				/* 2019-07-25 张磊 注释掉，CCSGuest 不再使用 
				// Guest  信息.
				cookie = new HttpCookie("CCSGuest");
				cookie.Expires = DateTime.Now.AddDays(2);
				cookie.Values.Add("GuestNo", string.Empty);
				cookie.Values.Add("GuestName", string.Empty);
				cookie.Values.Add("DeptNo", string.Empty);
				cookie.Values.Add("DeptName", string.Empty);
				System.Web.HttpContext.Current.Response.AppendCookie(cookie); //加入到会话。
				*/
			}
			catch (java.lang.Exception e2)
			{
			}
		}
	}
	public static String GetValFromCookie(String valKey, String isNullAsVal, boolean isChinese)
	{
		if (getIsBSMode() == false)
		{
			return BP.Port.Current.GetSessionStr(valKey, isNullAsVal);
		}

		try
		{
			String val = HttpContextHelper.RequestCookieGet(valKey, "CCS");
			/*
			if (isChinese)
			    val = HttpUtility.UrlDecode(hc[valKey]);
			else
			    val = hc.Values[valKey];
			*/
			if (DataType.IsNullOrEmpty(val))
			{
				return isNullAsVal;
			}
			return val;
		}
		catch (java.lang.Exception e)
		{
			return isNullAsVal;
		}
		throw new RuntimeException("@err-001 登录信息丢失。");
	}
	/** 
	 编号
	*/
	public static String getNo()
	{
			//return GetValFromCookie("GuestNo", null, false);
		String no = null; // GetSessionByKey("No", null);
		if (no == null || no.equals(""))
		{
			if (getIsBSMode() == false)
			{
				return "admin";
			}

			GuestUser.setNo(HttpContextHelper.RequestCookieGet("No", "CCS"));
			GuestUser.setName(HttpContextHelper.RequestCookieGet("Name", "CCS"));

			if (DataType.IsNullOrEmpty(GuestUser.getNo()))
			{
				throw new RuntimeException("@err-002 Guest 登录信息丢失。");
			}

			return GuestUser.getNo();
		}
		return no;
	}
	public static void setNo(String value)
	{
		SetSessionByKey("GuestNo", value);
	}
	/** 
	 名称
	*/
	public static String getName()
	{
		String val = GetValFromCookie("GuestName", null, true);
		if (val == null)
		{
			throw new RuntimeException("@err-001 GuestName 登录信息丢失。");
		}
		return val;
	}
	public static void setName(String value)
	{
		SetSessionByKey("GuestName", value);
	}
	/** 
	 部门名称
	*/
	public static String getDeptNo()
	{
		String val = GetValFromCookie("DeptNo", null, true);
		if (val == null)
		{
			throw new RuntimeException("@err-003 DeptNo 登录信息丢失。");
		}
		return val;
	}
	public static void setDeptNo(String value)
	{
		SetSessionByKey("DeptNo", value);
	}
	/** 
	 部门名称
	*/
	public static String getDeptName()
	{
		String val = GetValFromCookie("DeptName", null, true);
		if (val == null)
		{
			throw new RuntimeException("@err-002 DeptName 登录信息丢失。");
		}
		return val;
	}
	public static void setDeptName(String value)
	{
		SetSessionByKey("DeptName", value);
	}
	/** 
	 风格
	*/
	public static String getStyle()
	{
		return GetSessionByKey("Style", "0");
	}
	public static void setStyle(String value)
	{
		SetSessionByKey("Style", value);
	}
}