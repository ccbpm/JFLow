package BP.Web;

import BP.En.*;
import BP.DA.*;
import BP.Port.*;
import BP.Sys.*;
import java.util.*;
import java.io.*;
import java.time.*;

/** 
 User 的摘要说明。
*/
public class WebUser
{
	/** 
	 密码解密
	 
	 @param pass 用户输入密码
	 @return 解密后的密码
	*/
	public static String ParsePass(String pass)
	{
		if (pass.equals(""))
		{
			return "";
		}

		String str = "";
		char[] mychars = pass.toCharArray();
		int i = 0;
		for (char c : mychars)
		{
			i++;

			//step 1 
			long A = (long)c * 2;

			// step 2
			long B = A - i * i;

			// step 3 
			long C = 0;
			if (B > 196)
			{
				C = 196;
			}
			else
			{
				C = B;
			}

			str = str + String.valueOf((char)C);
		}
		return str;
	}
	/** 
	 更改一个人当前登录的主要部门
	 再一个人有多个部门的情况下有效.
	 
	 @param empNo 人员编号
	 @param fk_dept 当前所在的部门.
	*/
	public static void ChangeMainDept(String empNo, String fk_dept)
	{
		//这里要考虑集成的模式下，更新会出现是.

		String sql = BP.Sys.SystemConfig.GetValByKey("UpdataMainDeptSQL", "");
		if (sql.equals(""))
		{
			/*如果没有配置, 就取默认的配置.*/
			sql = "UPDATE Port_Emp SET FK_Dept=@FK_Dept WHERE No=@No";
		}

		sql = sql.replace("@FK_Dept", "'" + fk_dept + "'");
		sql = sql.replace("@No", "'" + empNo + "'");

		try
		{
			if (sql.contains("UPDATE Port_Emp SET FK_Dept=") == true)
			{
				if (BP.DA.DBAccess.IsView("Port_Emp", SystemConfig.getAppCenterDBType()) == true)
				{
					return;
				}
			}
			BP.DA.DBAccess.RunSQL(sql);
		}
		catch (RuntimeException ex)
		{
			throw new RuntimeException("@执行更改当前操作员的主部门的时候错误,请检查SQL配置:" + ex.getMessage());
		}

	}
	/** 
	 通用的登陆
	 
	 @param em 人员
	 @param lang 语言
	 @param auth 授权人
	 @param isRememberMe 是否记录cookies
	 @param IsRecSID 是否记录SID
	*/

	public static void SignInOfGener(Emp em, String lang, boolean isRememberMe, boolean IsRecSID, String authNo)
	{
		SignInOfGener(em, lang, isRememberMe, IsRecSID, authNo, null);
	}

	public static void SignInOfGener(Emp em, String lang, boolean isRememberMe, boolean IsRecSID)
	{
		SignInOfGener(em, lang, isRememberMe, IsRecSID, null, null);
	}

	public static void SignInOfGener(Emp em, String lang, boolean isRememberMe)
	{
		SignInOfGener(em, lang, isRememberMe, false, null, null);
	}

	public static void SignInOfGener(Emp em, String lang)
	{
		SignInOfGener(em, lang, false, false, null, null);
	}

	public static void SignInOfGener(Emp em)
	{
		SignInOfGener(em, "CH", false, false, null, null);
	}

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public static void SignInOfGener(Emp em, string lang = "CH", bool isRememberMe = false, bool IsRecSID = false, string authNo = null, string authName = null)
	public static void SignInOfGener(Emp em, String lang, boolean isRememberMe, boolean IsRecSID, String authNo, String authName)
	{
		if (HttpContextHelper.getCurrent() == null)
		{
			SystemConfig.setIsBSsystem(false);
		}
		else
		{
			SystemConfig.setIsBSsystem(true);
		}

		if (SystemConfig.getIsBSsystem())
		{
			BP.Sys.Glo.WriteUserLog("SignIn", em.getNo(), "登录");
		}

		WebUser.setNo(em.getNo());
		WebUser.setName(em.getName());
		if (DataType.IsNullOrEmpty(authNo) == false)
		{
			WebUser.setAuth(authNo); //被授权人，实际工作的执行者.
			WebUser.setAuthName(authName);
		}
		else
		{
			WebUser.setAuth(null);
			WebUser.setAuthName(null);
		}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 解决部门的问题.
		if (BP.Sys.SystemConfig.getOSDBSrc() == OSDBSrc.Database)
		{
			if (DataType.IsNullOrEmpty(em.getFK_Dept()) == true)
			{
				String sql = "";

				sql = "SELECT FK_Dept FROM Port_DeptEmp WHERE FK_Emp='" + em.getNo() + "'";

				String deptNo = BP.DA.DBAccess.RunSQLReturnString(sql);
				if (DataType.IsNullOrEmpty(deptNo) == true)
				{
					sql = "SELECT FK_Dept FROM Port_Emp WHERE No='" + em.getNo() + "'";
					deptNo = BP.DA.DBAccess.RunSQLReturnString(sql);
					if (DataType.IsNullOrEmpty(deptNo) == true)
					{
						throw new RuntimeException("@登录人员(" + em.getNo() + "," + em.getName() + ")没有维护部门...");
					}
				}
				else
				{
					//调用接口更改所在的部门.
					WebUser.ChangeMainDept(em.getNo(), deptNo);
				}
			}

			BP.Port.Dept dept = new Dept();
			dept.setNo(em.getFK_Dept());
			if (dept.RetrieveFromDBSources() == 0)
			{
				throw new RuntimeException("@登录人员(" + em.getNo() + "," + em.getName() + ")没有维护部门,或者部门编号{" + em.getFK_Dept() + "}不存在.");
			}
		}

		if (BP.Sys.SystemConfig.getOSDBSrc() == OSDBSrc.WebServices)
		{
			BP.En30.ccportal.PortalInterfaceSoapClient ws = DataType.GetPortalInterfaceSoapClientInstance();
			DataTable dt = ws.GetEmpHisDepts(em.getNo());
			String strs = BP.DA.DBAccess.GenerWhereInPKsString(dt);
			Paras ps = new Paras();
			ps.SQL = "UPDATE WF_Emp SET Depts=" + SystemConfig.getAppCenterDBVarStr() + "Depts WHERE No=" + SystemConfig.getAppCenterDBVarStr() + "No";
			ps.Add("Depts", strs);
			ps.Add("No", em.getNo());
			BP.DA.DBAccess.RunSQL(ps);

			dt = ws.GetEmpHisStations(em.getNo());
			strs = BP.DA.DBAccess.GenerWhereInPKsString(dt);
			ps = new Paras();
			ps.SQL = "UPDATE WF_Emp SET Stas=" + SystemConfig.getAppCenterDBVarStr() + "Stas WHERE No=" + SystemConfig.getAppCenterDBVarStr() + "No";
			ps.Add("Stas", strs);
			ps.Add("No", em.getNo());
			BP.DA.DBAccess.RunSQL(ps);
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 解决部门的问题.

		WebUser.setFK_Dept(em.getFK_Dept());
		WebUser.setFK_DeptName(em.getFK_DeptText());
		if (IsRecSID)
		{
			//判断是否视图，如果为视图则不进行修改 
			if (BP.DA.DBAccess.IsView("Port_Emp", SystemConfig.getAppCenterDBType()) == false)
			{
				/*如果记录sid*/
				String sid1 = LocalDateTime.now().toString("MMddHHmmss");
				DBAccess.RunSQL("UPDATE Port_Emp SET SID='" + sid1 + "' WHERE No='" + WebUser.getNo() + "'");
				WebUser.setSID(sid1);
			}
		}

		WebUser.setSysLang(lang);
		if (BP.Sys.SystemConfig.getIsBSsystem())
		{
			// cookie操作，为适应不同平台，统一使用HttpContextHelper
			HashMap<String, String> cookieValues = new HashMap<String, String>();

			cookieValues.put("No", em.getNo());
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

			if (HttpContextHelper.getCurrent().Session != null)
			{
				cookieValues.put("Token", HttpContextHelper.getSessionID());
				cookieValues.put("SID", HttpContextHelper.getSessionID());
			}

			cookieValues.put("Lang", lang);
			if (authNo == null)
			{
				authNo = "";
			}
			cookieValues.put("Auth", authNo); //授权人.

			if (authName == null)
			{
				authName = "";
			}
			cookieValues.put("AuthName", authName); //授权人名称..

			HttpContextHelper.ResponseCookieAdd(cookieValues, null, "CCS");
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
		//2019-07-25 zyt改造
		if (getIsBSMode() && HttpContextHelper.getCurrent() != null && HttpContextHelper.getCurrent().Session != null)
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
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

	/** 
	 是不是b/s 工作模式。
	*/
	protected static boolean getIsBSMode()
	{
		if (HttpContextHelper.getCurrent() == null)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	/** 
	 设置session
	 
	 @param key 键
	 @param val 值
	*/
	public static void SetSessionByKey(String key, String val)
	{
		if (val == null)
		{
			return;
		}
		//2019-07-25 zyt改造
		if (getIsBSMode() == true && HttpContextHelper.getCurrent() != null && HttpContextHelper.getCurrent().Session != null)
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
			HttpContextHelper.ResponseCookieDelete(new String[] {"No", "Name", "Pass", "IsRememberMe", "Auth", "AuthName"}, "CCS");

			return;
		}

		try
		{
			BP.Port.Current.Session.clear();

			HttpContextHelper.ResponseCookieDelete(new String[] {"No", "Name", "Pass", "IsRememberMe", "Auth", "AuthName"}, "CCS");

			HttpContextHelper.SessionClear();
		}
		catch (java.lang.Exception e)
		{
		}
	}
	/** 
	 授权人
	*/
	public static String getAuth()
	{
		String val = GetValFromCookie("Auth", null, false);
		if (val == null)
		{
			val = GetSessionByKey("Auth", null);
		}
		return val;
	}
	public static void setAuth(String value)
	{
		if (value.equals(""))
		{
			SetSessionByKey("Auth", null);
		}
		else
		{
			SetSessionByKey("Auth", value);
		}
	}
	/** 
	 部门名称
	*/
	public static String getFK_DeptName()
	{
		try
		{
			String val = GetValFromCookie("FK_DeptName", null, true);
			return val;
		}
		catch (java.lang.Exception e)
		{
			return "无";
		}
	}
	public static void setFK_DeptName(String value)
	{
		SetSessionByKey("FK_DeptName", value);
	}
	/** 
	 部门全称
	*/
	public static String getFK_DeptNameOfFull()
	{
		String val = GetValFromCookie("FK_DeptNameOfFull", null, true);
		if (DataType.IsNullOrEmpty(val))
		{
			try
			{
				val = DBAccess.RunSQLReturnStringIsNull("SELECT NameOfPath FROM Port_Dept WHERE No='" + WebUser.getFK_Dept() + "'", null);
				return val;
			}
			catch (java.lang.Exception e)
			{
				val = WebUser.getFK_DeptName();
			}

				//给它赋值.
			setFK_DeptNameOfFull(val);
		}
		return val;
	}
	public static void setFK_DeptNameOfFull(String value)
	{
		SetSessionByKey("FK_DeptNameOfFull", value);
	}
	/** 
	 令牌
	*/
	public static String getToken()
	{
		return GetSessionByKey("token", "null");
	}
	public static void setToken(String value)
	{
		SetSessionByKey("token", value);
	}
	/** 
	 语言
	*/
	public static String getSysLang()
	{
		return "CH";
			/*
			string no = GetSessionByKey("Lang", null);
			if (no == null || no == "")
			{
			    if (IsBSMode)
			    {
			        // HttpCookie hc1 = BP.Sys.Glo.Request.Cookies["CCS"];
			        string lang = HttpContextHelper.RequestCookieGet("Lang", "CCS");
			        if (String.IsNullOrEmpty(lang))
			            return "CH";
			        SetSessionByKey("Lang", lang);
			    }
			    else
			    {
			        return "CH";
			    }
			    return GetSessionByKey("Lang", "CH");
			}
			else
			{
			    return no;
			}*/
	}
	public static void setSysLang(String value)
	{
		SetSessionByKey("Lang", value);
	}
	/** 
	 当前登录人员的部门
	*/
	public static String getFK_Dept()
	{
		String val = GetValFromCookie("FK_Dept", null, false);
		if (val == null)
		{
			if (WebUser.getNo() == null)
			{
				throw new RuntimeException("@登录信息丢失，请你确认是否启用了cookie? ");
			}

			String sql = "SELECT FK_Dept FROM Port_Emp WHERE No='" + WebUser.getNo() + "'";
			String dept = BP.DA.DBAccess.RunSQLReturnStringIsNull(sql, null);
			if (dept == null && SystemConfig.getOSModel() == OSModel.OneMore)
			{
				sql = "SELECT FK_Dept FROM Port_Emp WHERE No='" + WebUser.getNo() + "'";
				dept = BP.DA.DBAccess.RunSQLReturnStringIsNull(sql, null);
			}

			if (dept == null)
			{
				throw new RuntimeException("@err-003 FK_Dept，当前登录人员(" + WebUser.getNo() + ")，没有设置部门。");
			}

			SetSessionByKey("FK_Dept", dept);
			return dept;
		}
		return val;
	}
	public static void setFK_Dept(String value)
	{
		SetSessionByKey("FK_Dept", value);
	}
	/** 
	 所在的集团编号
	*/
	public static String getGroupNo111()
	{
		String val = GetValFromCookie("GroupNo", null, false);
		if (val == null)
		{
			if (!SystemConfig.getCustomerNo().equals("Bank"))
			{
				return "0";
			}

			if (WebUser.getNo() == null)
			{
				throw new RuntimeException("@登录信息丢失，请你确认是否启用了cookie? ");
			}

			String sql = "SELECT GroupNo FROM Port_Dept WHERE No='" + WebUser.getFK_Dept() + "'";
			String groupNo = BP.DA.DBAccess.RunSQLReturnStringIsNull(sql, null);

			if (groupNo == null)
			{
				throw new RuntimeException("@err-003 FK_Dept，当前登录人员(" + WebUser.getNo() + ")，没有设置部门。");
			}

			SetSessionByKey("GroupNo", groupNo);
			return groupNo;
		}
		return val;
	}
	public static void setGroupNo111(String value)
	{
		SetSessionByKey("GroupNo", value);
	}
	/** 
	 当前登录人员的父节点编号
	*/
	public static String getDeptParentNo()
	{
		String val = GetValFromCookie("DeptParentNo", null, false);
		if (val == null)
		{
			if (BP.Web.WebUser.getFK_Dept() == null)
			{
				throw new RuntimeException("@err-001 DeptParentNo, FK_Dept 登录信息丢失。");
			}

			BP.Port.Dept dept = new Port.Dept(BP.Web.WebUser.getFK_Dept());
			BP.Web.WebUser.setDeptParentNo(dept.getParentNo());
			return dept.getParentNo();
		}
		return val;
	}
	public static void setDeptParentNo(String value)
	{
		SetSessionByKey("DeptParentNo", value);
	}
	/** 
	 检查权限控制
	 
	 @param sid
	 @return 
	*/
	public static boolean CheckSID(String userNo, String sid)
	{
		if (BP.Sys.SystemConfig.getOSDBSrc() == OSDBSrc.WebServices)
		{
			return true;
		}

		Paras paras = new Paras();
		paras.SQL = "SELECT SID FROM Port_Emp WHERE No=" + SystemConfig.getAppCenterDBVarStr() + "No";
		paras.Add("No", userNo);
		String mysid = DBAccess.RunSQLReturnStringIsNull(paras, null);
		return sid.equals(mysid);
	}
	public static String getNoOfRel()
	{
		String val = GetSessionByKey("No", null);
		if (val == null)
		{
			return GetValFromCookie("No", null, true);
		}
		return val;
	}
	public static String GetValFromCookie(String valKey, String isNullAsVal, boolean isChinese)
	{


		if (getIsBSMode() == false)
		{
			return BP.Port.Current.GetSessionStr(valKey, isNullAsVal);
		}

		try
		{
			//先从session里面取.
			//string v = System.Web.HttpContext.Current.Session[valKey] as string;
			//2019-07-25 zyt改造
			String v = HttpContextHelper.<String>SessionGet(valKey);
			if (DataType.IsNullOrEmpty(v) == false)
			{
				return v;
			}
		}
		catch (java.lang.Exception e)
		{
		}


		try
		{
			String val = HttpContextHelper.RequestCookieGet(valKey, "CCS");

			if (isChinese)
			{
				val = HttpUtility.UrlDecode(val);
			}



			if (DataType.IsNullOrEmpty(val))
			{
				return isNullAsVal;
			}
			return val;
		}
		catch (java.lang.Exception e2)
		{
			return isNullAsVal;
		}
		throw new RuntimeException("@err-001 (" + valKey + ")登录信息丢失。");
	}
	/** 
	 设置信息.
	 
	 @param keyVals
	*/
	public static void SetValToCookie(String keyVals)
	{
		if (BP.Sys.SystemConfig.getIsBSsystem() == false)
		{
			return;
		}

		/* 2019-7-25 张磊 如下代码没有作用，删除
		HttpCookie hc = BP.Sys.Glo.Request.Cookies["CCS"];
		if (hc != null)
		    BP.Sys.Glo.Request.Cookies.Remove("CCS");

		HttpCookie cookie = new HttpCookie("CCS");
		cookie.Expires = DateTime.Now.AddMinutes(SystemConfig.SessionLostMinute);
		*/

		HashMap<String, String> cookieValues = new HashMap<String, String>();
		AtPara ap = new AtPara(keyVals);
		for (String key : ap.getHisHT().keySet())
		{
			cookieValues.put(key, ap.GetValStrByKey(key));
		}

		HttpContextHelper.ResponseCookieAdd(cookieValues, LocalDateTime.now().plusMinutes(SystemConfig.getSessionLostMinute()), "CCS");
	}

	/** 
	 是否是操作员？
	*/
	public static boolean getIsAdmin()
	{
		if (BP.Web.WebUser.getNo().equals("admin"))
		{
			return true;
		}
		try
		{
			String sql = "SELECT No FROM WF_Emp WHERE UserType=1 AND No='" + WebUser.getNo() + "'";
			if (DBAccess.RunSQLReturnTable(sql).Rows.size() == 1)
			{
				return true;
			}
			return false;
		}
		catch (java.lang.Exception e)
		{
			return false;
		}
	}
	/** 
	 编号
	*/
	public static String getNo()
	{
		return GetValFromCookie("No", null, true);
	}
	public static void setNo(String value)
	{
		SetSessionByKey("No", value.trim()); //@祝梦娟.
	}
	/** 
	 名称
	*/
	public static String getName()
	{
		String no = BP.Web.WebUser.getNo();

		String val = GetValFromCookie("Name", no, true);
		if (val == null)
		{
			throw new RuntimeException("@err-002 Name 登录信息丢失。");
		}

		return val;
	}
	public static void setName(String value)
	{
		SetSessionByKey("Name", value);
	}
	/** 
	 域
	*/
	public static String getDomain()
	{
		String val = GetValFromCookie("Domain", null, true);
		if (val == null)
		{
			throw new RuntimeException("@err-003 Domain 登录信息丢失。");
		}
		return val;
	}
	public static void setDomain(String value)
	{
		SetSessionByKey("Domain", value);
	}
	public static Stations getHisStations()
	{
		Stations sts = new Stations();
		QueryObject qo = new QueryObject(sts);
		qo.AddWhereInSQL("No", "SELECT FK_Station FROM Port_DeptEmpStation WHERE FK_Emp='" + WebUser.getNo() + "'");
		qo.DoQuery();

		return sts;
	}
	/** 
	 SID
	*/
	public static String getSID()
	{
		String val = GetValFromCookie("SID", null, true);
		if (val == null)
		{
			return "";
		}
		return val;
	}
	public static void setSID(String value)
	{
		SetSessionByKey("SID", value);
	}
	/** 
	 设置SID
	 
	 @param sid
	*/
	public static void SetSID(String sid)
	{
		//判断是否视图，如果为视图则不进行修改
		if (BP.DA.DBAccess.IsView("Port_Emp", SystemConfig.getAppCenterDBType()) == false)
		{
			Paras ps = new Paras();
			ps.SQL = "UPDATE Port_Emp SET SID=" + SystemConfig.getAppCenterDBVarStr() + "SID WHERE No=" + SystemConfig.getAppCenterDBVarStr() + "No";
			ps.Add("SID", sid);
			ps.Add("No", WebUser.getNo());
			BP.DA.DBAccess.RunSQL(ps);
		}
		WebUser.setSID(sid);
	}
	/** 
	 是否是授权状态
	*/
	public static boolean getIsAuthorize()
	{
		if (getAuth() == null || getAuth().equals(""))
		{
			return false;
		}
		return true;
	}
	/** 
	 使用授权人ID
	*/
	public static String getAuthName()
	{
		String val = GetValFromCookie("AuthName", null, false);
		if (val == null)
		{
			val = GetSessionByKey("AuthName", null);
		}
		return val;
	}
	public static void setAuthName(String value)
	{
		if (value.equals(""))
		{
			SetSessionByKey("AuthName", null);
		}
		else
		{
			SetSessionByKey("AuthName", value);
		}
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 当前人员操作方法.
	public static void DeleteTempFileOfMy()
	{
		String usr = HttpContextHelper.RequestCookieGet("No", "CCS"); //hc.Values["No"];
		String[] strs = System.IO.Directory.GetFileSystemEntries(SystemConfig.getPathOfTemp());
		for (String str : strs)
		{
			if (str.indexOf(usr) == -1)
			{
				continue;
			}

			try
			{
				(new File(str)).delete();
			}
			catch (java.lang.Exception e)
			{
			}
		}
	}
	public static void DeleteTempFileOfAll()
	{
		String[] strs = System.IO.Directory.GetFileSystemEntries(SystemConfig.getPathOfTemp());
		for (String str : strs)
		{
			try
			{
				(new File(str)).delete();
			}
			catch (java.lang.Exception e)
			{
			}
		}
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion
}