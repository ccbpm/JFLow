package BP.Web;

import BP.En.*;
import BP.DA.*;
import BP.Difference.ContextHolderUtils;
import BP.Difference.SystemConfig;
import BP.Port.*;
import BP.Pub.*;
import BP.Sys.*;
import BP.Tools.StringHelper;

import java.util.*;

import javax.servlet.http.Cookie;

import java.net.URLDecoder;
import java.net.URLEncoder;
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
	 * @throws Exception 
	*/
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
		//2019-07-25 zyt改造
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
		if (SystemConfig.getIsBSsystem())
		{
			int expiry = 60 * 60 * 24 * 2;
			ContextHolderUtils.addCookie("GuestNo", expiry, guestNo);
			ContextHolderUtils.addCookie("GuestName", expiry, URLEncoder.encode(guestName, "UTF-8"));
			
			ContextHolderUtils.addCookie("DeptNo", expiry, deptNo);
			ContextHolderUtils.addCookie("DeptName", expiry, URLEncoder.encode(deptName, "UTF-8"));
			
			ContextHolderUtils.addCookie("No", expiry, "Guest");
			ContextHolderUtils.addCookie("Name", expiry, URLEncoder.encode(em.getName(), "UTF-8"));
			
			

			if (isRememberMe)
			{
				ContextHolderUtils.addCookie("IsRememberMe",expiry, "1");
			}
			else
			{
				ContextHolderUtils.addCookie("IsRememberMe",expiry, "0");
			}

			ContextHolderUtils.addCookie("FK_Dept",expiry, em.getFK_Dept());
			ContextHolderUtils.addCookie("FK_DeptName",expiry,URLEncoder.encode(em.getFK_DeptText(), "UTF-8"));

			ContextHolderUtils.addCookie("Token",expiry, getNoOfSessionID());
			ContextHolderUtils.addCookie("SID",expiry, getNoOfSessionID());

			ContextHolderUtils.addCookie("Lang",expiry, lang);
			ContextHolderUtils.addCookie("Style",expiry, "0");
			ContextHolderUtils.addCookie("Auth",expiry, ""); //授权人.
		}
	}
	
	public static String getNoOfSessionID() {
		String s = GetSessionByKey("No", null);
		if (s == null) {

			return ContextHolderUtils.getSession().getId();
		}
		return s;
	}

		///#region 静态方法
	/** 
	 通过key,取出session.
	 
	 @param key key
	 @param isNullAsVal 如果是Null, 返回的值.
	 @return 
	*/
	public static String GetSessionByKey(String key, String isNullAsVal)
	{
		if (getIsBSMode() && null != ContextHolderUtils.getRequest() && null != ContextHolderUtils.getSession()) {
			Object value = ContextHolderUtils.getSession().getAttribute(key);
			String str = value == null ? "" : String.valueOf(value);
			if (StringHelper.isNullOrEmpty(str)) {
				str = isNullAsVal;
			}
			return str;
		} else {
			if ((Current.Session.get(key) == null || Current.Session.get(key).toString().equals("")) && isNullAsVal != null) {
				return isNullAsVal;
			} else {
				String str = (String) Current.Session.get(key);
				return str;
			}
		}
	}

	/** 
	 是不是b/s 工作模式。
	*/
	protected static boolean getIsBSMode()
	{
			//2019-07-25 zyt改造
		if (ContextHolderUtils.getRequest() == null) {
			return false;
		} else {
			return true;
		}
	}

	public static Object GetSessionByKey(String key, Object defaultObjVal)
	{
		if (getIsBSMode() && null != ContextHolderUtils.getRequest() && null != ContextHolderUtils.getSession()) {
			Object value = ContextHolderUtils.getSession().getAttribute(key);
			String str = value == null ? "" : String.valueOf(value);
			if (StringHelper.isNullOrEmpty(str)) {
				str = defaultObjVal.toString();
			}
			return str;
		} else {
			if ((Current.Session.get(key) == null || Current.Session.get(key).toString().equals("")) && defaultObjVal != null) {
				return defaultObjVal.toString();
			} else {
				String str = (String) Current.Session.get(key);
				return str;
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
		if (getIsBSMode() && null != ContextHolderUtils.getRequest() && null != ContextHolderUtils.getSession()) {
			ContextHolderUtils.getSession().setAttribute(key, val);
		} else {
			Current.SetSession(key, val);
		}
	}
	/** 
	 退回
	*/
	public static void Exit()
	{
		
	}
	public static String GetValFromCookie(String valKey, String isNullAsVal, boolean isChinese)
	{
		if (!getIsBSMode()) {
			return Current.GetSessionStr(valKey, isNullAsVal);
		}
	
		try {
			// 先从session里面取.
			Object value = ContextHolderUtils.getSession().getAttribute(valKey);
			String v = value == null ? "" : String.valueOf(value);
			if (!StringHelper.isNullOrEmpty(v)) {
				if (isChinese) {
					v = URLDecoder.decode(v, "UTF-8");
				}
				return v;
			}
		} catch (java.lang.Exception e) {}
	
		try {
			String val = null;
			Cookie cookie = ContextHolderUtils.getCookie(valKey);
			if (cookie != null){
				if (isChinese) {
					val = URLDecoder.decode(cookie.getValue(), "UTF-8");
				} else {
					val = cookie.getValue();
				}
			}
	
			if (StringHelper.isNullOrEmpty(val)) {
				return isNullAsVal;
			}
			return val;
		} catch (java.lang.Exception e2) {
			e2.printStackTrace();
			return isNullAsVal;
		}
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

			GuestUser.setNo(GetSessionByKey("No", "CCS"));
			GuestUser.setName(GetSessionByKey("Name", "CCS"));

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