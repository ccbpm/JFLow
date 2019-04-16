package BP.Web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.Cookie;
import javax.sound.sampled.Port;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import BP.DA.DBAccess;
import BP.DA.Paras;
import BP.En.QueryObject;
import BP.Port.Current;
import BP.Port.Emp;
import BP.Port.Stations;
import BP.Sys.OSDBSrc;
import BP.Sys.OSModel;
import BP.Sys.SystemConfig;
import BP.Tools.ContextHolderUtils;
import BP.Tools.StringHelper;
import BP.WF.Dev2Interface;
import BP.WF.DotNetToJavaStringHelper;

/**
 * User 的摘要说明。
 */
public class WebUser {

	/**
	 * 登录
	 * 
	 * @param em
	 * @throws Exception 
	 */
	public static void SignInOfGener(Emp em) throws Exception {
		SignInOfGener(em, "CH", null, true, false);
	}

	/**
	 * 登录
	 * 
	 * @param em
	 * @param isRememberMe
	 * @throws Exception 
	 */
	public static void SignInOfGener(Emp em, boolean isRememberMe) throws Exception {
		SignInOfGener(em, "CH", null, isRememberMe, false);
	}

	/**
	 * 登录
	 * 
	 * @param em
	 * @param auth
	 * @throws Exception 
	 */
	public static void SignInOfGenerAuth(Emp em, String auth) throws Exception {
		SignInOfGener(em, "CH", auth, true, false);
	}

	/**
	 * 登录
	 * 
	 * @param em
	 * @param lang
	 * @throws Exception 
	 */
	public static void SignInOfGenerLang(Emp em, String lang, boolean isRememberMe) throws Exception {
		SignInOfGener(em, lang, null, isRememberMe, false);
	}

	/**
	 * 登录
	 * 
	 * @param em
	 * @param lang
	 * @throws Exception 
	 */
	public static void SignInOfGenerLang(Emp em, String lang) throws Exception {
		SignInOfGener(em, lang, null, true, false);
	}

	public static void SignInOfGener(Emp em, String lang) throws Exception {
		SignInOfGener(em, lang, em.getNo(), true, false);
	}

	/**
	 * 登录
	 * 
	 * @param em 登录人
	 * @param lang 语言
	 * @param auth 被授权登录人
	 * @param isRememberMe 是否记忆我
	 * @throws Exception 
	 */
	public static void SignInOfGener(Emp em, String lang, String auth, boolean isRememberMe) throws Exception {
		SignInOfGener(em, lang, auth, isRememberMe, false);
	}

	/**
	 * 通用的登录
	 * 
	 * @param em 人员
	 * @param lang 语言
	 * @param auth 授权人
	 * @param isRememberMe 是否记录cookies
	 * @param IsRecSID 是否记录SID
	 * @throws Exception 
	 */
	public static String SignInOfGener(Emp em, String lang, String auth, boolean isRememberMe, boolean IsRecSID) throws Exception {
		if (SystemConfig.getIsBSsystem()) {
			BP.Sys.Glo.WriteUserLog("SignIn", em.getNo(), "登录");
		}
		if (auth == null) {
			auth = "";
		}
		String sid = null;
		try {
			WebUser.setNo(em.getNo());
			WebUser.setName(em.getName());
			WebUser.setFK_Dept(em.getFK_Dept());
			WebUser.setFK_DeptName(em.getFK_DeptText());
			
			try {
				sid = ContextHolderUtils.getSession().getId();
			} catch (Exception e) {
				sid = DBAccess.GenerOID()+"";
			}
			
			if (IsRecSID) {
				WebUser.setSID(sid);
				Dev2Interface.Port_SetSID(em.getNo(), sid);
			}
			
			WebUser.setAuth(auth);
			WebUser.setUserWorkDev(UserWorkDev.PC);
			WebUser.setSysLang(lang);
			
			if (SystemConfig.getIsBSsystem()) {
				try {
					int expiry = 60 * 60 * 24 * 2;
					ContextHolderUtils.addCookie("No", expiry, em.getNo());
					ContextHolderUtils.addCookie("Name", expiry, URLEncoder.encode(em.getName(), "UTF-8"));
					ContextHolderUtils.addCookie("IsRememberMe", expiry, isRememberMe ? "1" : "0");
					ContextHolderUtils.addCookie("FK_Dept", expiry, em.getFK_Dept());
					ContextHolderUtils.addCookie("FK_DeptName", expiry, URLEncoder.encode(em.getFK_DeptText(), "UTF-8"));
					if (ContextHolderUtils.getSession() != null) {
						ContextHolderUtils.addCookie("Token", expiry, sid);
						ContextHolderUtils.addCookie("SID", expiry, sid);
					}
					ContextHolderUtils.addCookie("Lang", expiry, lang);
//					String isEnableStyle = SystemConfig.getAppSettings().get("IsEnableStyle").toString();
//					if (isEnableStyle.equals("1")) {
//						try {
//							String sql = "SELECT Style FROM WF_Emp WHERE No='" + em.getNo() + "' ";
//							int val = DBAccess.RunSQLReturnValInt(sql, 0);
//							/*
//							 * warning cookie.Values.Add("Style", (new
//							 * Integer(val)).toString());
//							 */
//							ContextHolderUtils.addCookie("Style", expiry, String.valueOf(val));
//							WebUser.setStyle(String.valueOf(val));
//						} catch (java.lang.Exception e) {}
//					}
					/*
					 * warning cookie.Values.Add("Auth", auth); // 授权人.
					 * BP.Glo.getHttpContextCurrent().Response.AppendCookie(cookie);
					 */
					ContextHolderUtils.addCookie("Auth", expiry, auth);
				} catch (Exception e) {
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sid;
	}
	/** 
	 通用的登陆
	 
	 @param em 人员
	 @param lang 语言
	 @param auth 授权人
	 @param isRememberMe 是否记录cookies
	 @param IsRecSID 是否记录SID
	 * @throws Exception 
	*/
	public static void SignInOfGener(Emp em, String lang, boolean isRememberMe, boolean IsRecSID, String authNo, String authName) throws Exception
	{
		if (SystemConfig.getIsBSsystem())
		{
			BP.Sys.Glo.WriteUserLog("SignIn", em.getNo(), "登录");
		}

		WebUser.setNo(em.getNo());
		WebUser.setName(em.getName());
		if (DotNetToJavaStringHelper.isNullOrEmpty(authNo) == false)
		{
			WebUser.setAuth(authNo);//被授权人，实际工作的执行者.
			WebUser.setAuthName(authName);
		}
		else
		{
			WebUser.setAuth(null);
			WebUser.setAuthName(null);;
		}


		//登录模式？
		BP.Web.WebUser.setUserWorkDev(UserWorkDev.PC);

		///#region 解决部门的问题.
		if (BP.Sys.SystemConfig.getOSDBSrc() == OSDBSrc.Database)
		{
			if (DotNetToJavaStringHelper.isNullOrEmpty(em.getFK_Dept()) == true)
			{
				String sql = "";
				if (BP.Sys.SystemConfig.getOSModel()== OSModel.OneOne)
				{
					sql = "SELECT FK_Dept FROM Port_Emp WHERE No='" + em.getNo() + "'";
				}
				else
				{
					sql = "SELECT FK_Dept FROM Port_DeptEmp WHERE FK_Emp='" + em.getNo() + "'";
				}

				String deptNo = BP.DA.DBAccess.RunSQLReturnString(sql);
				if (DotNetToJavaStringHelper.isNullOrEmpty(deptNo) == true)
				{
					throw new RuntimeException("@登录人员(" + em.getNo() + "," + em.getName() + ")没有维护部门...");
				}
				else
				{
					//调用接口更改所在的部门.
					WebUser.ChangeMainDept(em.getNo(), deptNo);
				}
			}

			BP.Port.Dept dept = new BP.Port.Dept();
			dept.setNo(em.getFK_Dept());
			if (dept.RetrieveFromDBSources() == 0)
			{
				throw new RuntimeException("@登录人员(" + em.getNo() + "," + em.getName() + ")没有维护部门,或者部门编号{"+em.getFK_Dept()+"}不存在.");
			}
		}

		if (BP.Sys.SystemConfig.getOSDBSrc() == OSDBSrc.WebServices)
		{
			/*Object ws = DataType.GetPortalInterfaceSoapClientInstance();
			DataTable dt = ws.GetEmpHisDepts(em.getNo());
			String strs = BP.DA.DBAccess.GenerWhereInPKsString(dt);
			Paras ps = new Paras();
			ps.SQL = "UPDATE WF_Emp SET Depts=" + SystemConfig.getAppCenterDBVarStr() + "Depts WHERE No=" + SystemConfig.getAppCenterDBVarStr() + "No";
			ps.Add("Depts", strs);
			ps.Add("No", em.getNo());
			BP.DA.DBAccess.RunSQL(ps);
			WebUser.setHisDeptsStr(strs);

			dt = ws.GetEmpHisStations(em.getNo());
			strs = BP.DA.DBAccess.GenerWhereInPKsString(dt);
			ps = new Paras();
			ps.SQL = "UPDATE WF_Emp SET Stas=" + SystemConfig.getAppCenterDBVarStr() + "Stas WHERE No=" + SystemConfig.getAppCenterDBVarStr() + "No";
			ps.Add("Stas", strs);
			ps.Add("No", em.getNo());
			BP.DA.DBAccess.RunSQL(ps);
			WebUser.setHisStationsStr(strs);*/
		}
		///#endregion 解决部门的问题.

		WebUser.setFK_Dept(em.getFK_Dept());
		WebUser.setFK_DeptName(em.getFK_DeptText());
		WebUser.setHisDeptsStr(null);
		WebUser.setHisStationsStr(null);
		if (IsRecSID)
		{
			//判断是否视图，如果为视图则不进行修改 @于庆海 需要翻译
			if (BP.DA.DBAccess.IsView("Port_Emp") == false){
			//如果记录sid
			SimpleDateFormat formatter = new SimpleDateFormat ("MMddHHmmss");
			String sid1 = formatter.format(new Date());
			DBAccess.RunSQL("UPDATE Port_Emp SET SID='" + sid1 + "' WHERE No='" + WebUser.getNo() + "'");
			WebUser.setSID(sid1);
		}
		}

		WebUser.setSysLang(lang);
		if (BP.Sys.SystemConfig.getIsBSsystem())
		{
			//System.Web.HttpContext.Current.Response.Cookies.Clear();

			//HttpCookie hc = BP.Sys.Glo.getRequest().getCookies("CCS");
			Cookie hc = ContextHolderUtils.getCookie("CCS");
			if (hc != null)
			{
				ContextHolderUtils.deleteCookie("CCS");
			}

			int expiry = 60 * 60 * 24 * 2;
			ContextHolderUtils.addCookie("No", expiry, em.getNo());
			ContextHolderUtils.addCookie("Name", expiry, URLEncoder.encode(em.getName(), "UTF-8"));


			if (isRememberMe)
			{
				ContextHolderUtils.addCookie("IsRememberMe", expiry, "1");
			}
			else
			{
				ContextHolderUtils.addCookie("IsRememberMe", expiry, "0");
			}

			ContextHolderUtils.addCookie("FK_Dept", expiry, em.getFK_Dept());
			ContextHolderUtils.addCookie("FK_DeptName", expiry, URLEncoder.encode(em.getFK_DeptText(), "UTF-8"));

			if (ContextHolderUtils.getSession() != null)
			{
				ContextHolderUtils.addCookie("Token", expiry, getNoOfSessionID());
				ContextHolderUtils.addCookie("SID", expiry, getNoOfSessionID());
			}

			ContextHolderUtils.addCookie("Lang", expiry, lang);
			if (authNo == null)
			{
				authNo = "";
			}
			
			ContextHolderUtils.addCookie("Auth", expiry, authNo);//授权人.

			if (authName == null)
			{
				authName = "";
			}
			ContextHolderUtils.addCookie("AuthName", expiry, URLEncoder.encode(authName, "UTF-8"));//授权人名称
		}
	}

	private static void ChangeMainDept(String no, String deptNo) {
		//这里要考虑集成的模式下，更新会出现是.

		String sql = BP.Sys.SystemConfig.GetValByKey("UpdataMainDeptSQL", "");
		if (sql.equals(""))
		{
			//如果没有配置, 就取默认的配置.
			sql = "UPDATE Port_Emp SET FK_Dept=@FK_Dept WHERE No=@No";
		}

		sql = sql.replace("@FK_Dept","'"+deptNo+"'");
		sql = sql.replace("@No", "'"+no+"'");

		try
		{
			BP.DA.DBAccess.RunSQL(sql);
		}
		catch (RuntimeException ex)
		{
			throw new RuntimeException("@执行更改当前操作员的主部门的时候错误,请检查SQL配置:" + ex.getMessage());
		}
	}

	/**
	 * 退出
	 */
	public static void Exit() {
		try {
//			String token = WebUser.getToken();
//			ContextHolderUtils.clearCookie();// 不能全部都清除，应只清理本系统数据。
//			ContextHolderUtils.getSession().invalidate();// 不能全部都清除，应只清理本系统数据。
			// 清理Session
			WebUser.setNo(null);
			WebUser.setName(null);
			WebUser.setFK_Dept(null);
			WebUser.setFK_DeptName(null);
			WebUser.setSID(null);
			WebUser.setAuth(null);
			//WebUser.setUserWorkDev(null);
			WebUser.setSysLang(null);
			if (SystemConfig.getIsBSsystem()) {
				int expiry = 60 * 60 * 24 * 2;
				ContextHolderUtils.addCookie("No", expiry, null);
				ContextHolderUtils.addCookie("Name", expiry, null);
				ContextHolderUtils.addCookie("IsRememberMe", expiry, null);
				ContextHolderUtils.addCookie("FK_Dept", expiry, null);
				ContextHolderUtils.addCookie("FK_DeptName", expiry, null);
				ContextHolderUtils.addCookie("Token", expiry, null);
				ContextHolderUtils.addCookie("SID", expiry, null);
				ContextHolderUtils.addCookie("Lang", expiry, null);
				ContextHolderUtils.addCookie("Auth", expiry, null);
			}
//			WebUser.setToken(token);
		} catch (java.lang.Exception e2) {}
	}

	/**
	 * 是不是b/s 工作模式。
	 */
	private static boolean getIsBSMode() {
		
		if (ContextHolderUtils.getRequest() == null) {
			return false;
		} else {
			return true;
		}
	}
	
//	public static Object GetObjByKey(String key) {
//		if (getIsBSMode()) {
//			/*
//			 * warning return BP.Glo.getHttpContextCurrent().Session[key];
//			 */
//			return ContextHolderUtils.getSession().getAttribute(key);
//		} else {
//			return Current.Session.get(key);
//		}
//	}

	// 静态方法
	/**
	 * 通过key,取出session.
	 * 
	 * @param key key
	 * @param isNullAsVal 如果是Null, 返回的值.
	 * @return
	 */
	public static String GetSessionByKey(String key, String isNullAsVal) {
//		return GetSessionByKey(key, isNullAsVal, false);
//	}
//
//	/**
//	 * 通过key,取出session.
//	 * 
//	 * @param key key
//	 * @param isNullAsVal 如果是Null, 返回的值.
//	 * @return
//	 */
//	public static String GetSessionByKey(String key, String isNullAsVal, boolean isChinese) {
//		try {
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
//		} catch (UnsupportedEncodingException e) {
//			return isNullAsVal;
//		}
	}

	public static Object GetSessionByKey(String key, Object defaultObjVal) {
		if (getIsBSMode() && null != ContextHolderUtils.getRequest() && null != ContextHolderUtils.getSession()) {
			Object value = ContextHolderUtils.getSession().getAttribute(key);
			if (null == value) {
				return defaultObjVal;
			} else {
				return value;
			}
		} else {
			if (Current.Session.get(key) == null) {
				return defaultObjVal;
			} else {
				return Current.Session.get(key);
			}
		}
	}

	/**
	 * 设置session
	 * 
	 * @param key 键
	 * @param val 值
	 */
	public static void SetSessionByKey(String key, Object val) {
		if (getIsBSMode() && null != ContextHolderUtils.getRequest() && null != ContextHolderUtils.getSession()) {
			ContextHolderUtils.getSession().setAttribute(key, val);
		} else {
			Current.SetSession(key, val);
		}
	}

	public static String GetValFromCookie(String valKey, String isNullAsVal, boolean isChinese) {
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

 

	public static String getSysLang() {
		return "CH";
	}

	public static void setSysLang(String value) {
		SetSessionByKey("Lang", value);
	}

	/**
	 * FK_Dept
	 * @throws Exception 
	 */
	public static String getFK_Dept() 
	{
		
		String val = GetValFromCookie("FK_Dept", null, false);
		if (val == null) {
			throw new RuntimeException("@err-003 FK_Dept 登录信息丢失，请确认当前操作员的部门信息是否完整，检查表:Port_Emp 字段 FK_Dept。");
		}
		return val;
	}

	public static void setFK_Dept(String value) {
		SetSessionByKey("FK_Dept", value);
	}

	/**
	 * 当前登录人员的父节点编号
	 * @throws Exception 
	 */
	public static String getDeptParentNo() throws Exception {
		String val = GetValFromCookie("DeptParentNo", null, false);
		if (val == null) {
			
			if (WebUser.getFK_Dept()==null)			
			throw new RuntimeException("@err-001 DeptParentNo, FK_Dept 登录信息丢失。");
			
			   BP.Port.Dept dept = new BP.Port.Dept( WebUser.getFK_Dept());
               BP.Web.WebUser.setDeptParentNo(  dept.getParentNo());
               return val;
		}
		return val;
	}

	public static void setDeptParentNo(String value) {
		SetSessionByKey("DeptParentNo", value);
	}

	/**
	 * 检查权限控制
	 * @param sid
	 * @return
	 */
	public static boolean CheckSID(String UserNo, String SID) {
//		String mysid = null;
//		try {
//			mysid = DBAccess.RunSQLReturnStringIsNull(
//					"SELECT SID FROM Port_Emp WHERE No='" + UserNo + "'", null);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		if (SID != null && SID.equals(mysid)) {
//			return true;
//		} else {
//			return false;
//		}
		return true;
	}

	/**
	 * sessionID
	 */
	public static String getNoOfSessionID() {
		String s = GetSessionByKey("No", null);
		if (s == null) {
			/*
			 * warning return BP.Glo.getHttpContextCurrent().Session.SessionID;
			 */
			return ContextHolderUtils.getSession().getId();
		}
		return s;
	}
	
	/**
	 *  是否是操作员？
	 * @throws Exception 
	 */
    public static boolean getIsAdmin() throws Exception
    {
            if ("admin".equals(BP.Web.WebUser.getNo()))
                return true;
            try
            {
                String sql = "SELECT * FROM WF_Emp WHERE UserType=1 AND No='"+WebUser.getNo()+"'";
                if (DBAccess.RunSQLReturnTable(sql).Rows.size() == 1)
                    return true;
                return false;
            }
            catch (Exception E)
            {
                return false;
            }
      }
    

	public static String getNoOfRel() {
		return GetSessionByKey("No", null);
	}

	/**
	 * 编号
	 * @throws Exception 
	 */
	public static String getNo() throws Exception {
		// 如果设置了第三方的SessionKey名称，则进行根据第三方系统用户Key进行登录。
		String userNoSessionKey = ContextHolderUtils.getInstance().getUserNoSessionKey();
		if (StringUtils.isNotBlank(userNoSessionKey)){
			String userNo = ObjectUtils.toString(ContextHolderUtils.getSession().getAttribute(userNoSessionKey));
			// 如果第三方的用户id不存在，则代表已退出，同步退出jflow。
			if (StringUtils.isNotBlank(userNo)){
				// 如果是平台管理员账号，则转换为jflow的管理员账号
				if (BP.WF.Glo.getIsAdmin(userNo)){
					userNo = "admin";
				}
				// 获取当前是否已登录，如果已登录并且与第三方id一样，则直接返回，否则重新登录
				String val = GetSessionByKey("No", "");
				if (val != null && userNo.equals(val)){
					return val;
				}else{
					// 登录WF平台，这里需要保存SID方便第三方系统获取。
					BP.Port.Emp emp = new BP.Port.Emp(userNo);
					WebUser.SignInOfGener(emp, "CH", null, true, true);
				}
			}else{
				WebUser.Exit();
			}
		}
		//		String val = GetValFromCookie("No", null, true);
		String val = GetSessionByKey("No", "");
		if (val == null) {
			//val = "admin";
			throw new RuntimeException("@err-002 No 登录信息丢失。");
		}
		return val;
	}

	public static void setNo(String value) {
		SetSessionByKey("No", value.trim());
	}

	/**
	 * 名称
	 */
	public static String getName() {
		//		String val = GetValFromCookie("Name", null, true);
		String val = GetSessionByKey("Name", "");
		if (val == null) {
			throw new RuntimeException("@err-002 Name 登录信息丢失。");
		}
		return val;
	}

	public static void setName(String value) {
		SetSessionByKey("Name", value);
	}

	/**
	 * 域
	 */
	public static String getDomain() {
		String val = GetSessionByKey("Domain", "");
		if (val == null) {
			throw new RuntimeException("@err-003 Domain 登录信息丢失。");
		}
		return val;
	}

	public static void setDomain(String value) {
		SetSessionByKey("Domain", value);
	}

	/**
	 * 令牌
	 */
	public static String getToken() {

		return GetSessionByKey("token", "null");
	}

	public static void setToken(String value) {
		SetSessionByKey("token", value);
	}

	/**
	 * 授权人
	 */
	public static String getAuth() {
		String val = GetValFromCookie("Auth", null, false);
		if (val == null) {
			val = GetSessionByKey("Auth", null);
		}
		return val;
	}

	public static void setAuth(String value) {
		if (value == null || value.equals("")) {
			SetSessionByKey("Auth", null);
		} else {
			SetSessionByKey("Auth", value);
		}
	}

	// 使用授权人ID
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
		if (null == value || "null".equals(value) || value.equals(""))
		{
		SetSessionByKey("AuthName", null);
		}
		else
		{
		SetSessionByKey("AuthName", value);
		}
	}

	public static String getFK_DeptName() {
		try {
			//String val = GetValFromCookie("FK_DeptName", null, true);
			String val = GetSessionByKey("FK_DeptName", "");
			return val;
		} catch (java.lang.Exception e) {
			return "无";
		}
	}

	public static void setFK_DeptName(String value) {
		SetSessionByKey("FK_DeptName", value);
	}

	/**
	 * 部门全称
	 */
	public static String getFK_DeptNameOfFull() {
		String val = GetValFromCookie("FK_DeptNameOfFull", null, true);
		if (StringHelper.isNullOrEmpty(val)) {
			try {
				val = DBAccess.RunSQLReturnStringIsNull("SELECT NameOfPath FROM Port_Dept WHERE No='" + WebUser.getFK_Dept() + "'", "");
				return val;
			} catch (java.lang.Exception e) {
				val = WebUser.getFK_DeptName();
			}

			// 给它赋值.
			setFK_DeptNameOfFull(val);
		}
		return val;
	}

	public static void setFK_DeptNameOfFull(String value) {
		SetSessionByKey("FK_DeptNameOfFull", value);
	}

	public static String getStyle() {
		return GetSessionByKey("Style", "0");
	}

	public static void setStyle(String value) {
		SetSessionByKey("Style", value);
	}

	/**
	 * 当前工作人员实体
	 * @throws Exception 
	 */
	public static Emp getHisEmp() throws Exception {
		return new Emp(WebUser.getNo());
	}

	/**
	 * SID
	 */
	public static String getSID() {
		String val = GetValFromCookie("SID", null, true);
		if (val == null) {
			return "";
		}
		return val;
	}

	public static void setSID(String value) {
		SetSessionByKey("SID", value);
	}
	/** 
	 设置SID
	 
	 @param sid
	 * @throws Exception 
*/
	public static void SetSID(String sid) throws Exception
	{
		//判断是否视图，如果为视图则不进行修改 @于庆海 需要翻译
		if (BP.DA.DBAccess.IsView("Port_Emp") == false)
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
	 * 是否是授权状态
	 */
	public static boolean getIsAuthorize() {
		if (getAuth() == null || getAuth().equals("")) {
			return false;
		}
		return true;
	}

	/**
	 * 使用授权人ID
	 */
	public static String getAuthorizerEmpID() {
		return GetSessionByKey("AuthorizerEmpID", null);

	}

	public static void setAuthorizerEmpID(String value) {
		SetSessionByKey("AuthorizerEmpID", value);
	}

	/**
	 * 用户工作方式.
	 */
	public static UserWorkDev getUserWorkDev() {
		if (BP.Sys.SystemConfig.getIsBSsystem() == false) {
			return getUserWorkDev().PC;
		}

		int s = (Integer) GetSessionByKey("UserWorkDev", 0);
		BP.Web.UserWorkDev wd = UserWorkDev.forValue(s);
		return wd;
	}

	public static void setUserWorkDev(UserWorkDev value) {
		SetSessionByKey("UserWorkDev", value.getValue());
	}

	public static boolean getIsWap() {
		if (!SystemConfig.getIsBSsystem())
			return false;
		int s = Integer.parseInt(GetSessionByKey("IsWap", 9).toString());
		if (9 == s) {
			String userAgent = ContextHolderUtils.getRequest().getHeader("USER-AGENT").toLowerCase();
			boolean b = userAgent.contains("wap");
			setIsWap(b);
			if (b) {
				SetSessionByKey("IsWap", 1);
			} else {
				SetSessionByKey("IsWap", 0);
			}
			return b;
		}
		if (s == 1)
			return true;
		else
			return false;
	}

	public static void setIsWap(boolean value) {
		if (value) {
			SetSessionByKey("IsWap", 1);
		} else {
			SetSessionByKey("IsWap", 0);
		}
	}

	// 部门权限
	 

	// 部门权限

	// public static Stations getHisStations() {
	// EmpStations sts = new EmpStations();
	// return sts.GetHisStations(WebUser.getNo());
	// }
	public static Stations getHisStations() throws Exception {
		Object obj = null;
		obj = GetSessionByKey("HisSts", obj);
		if (obj == null) {
			Stations sts = new Stations();
			QueryObject qo = new QueryObject(sts);
			qo.AddWhereInSQL("No", "SELECT FK_Station FROM Port_EmpStation WHERE FK_Emp='" + WebUser.getNo() + "'");
			qo.DoQuery();
			SetSessionByKey("HisSts", sts);
			return sts;
		}
		return (Stations) ((obj instanceof Stations) ? obj : null);
	}

	/**
	 * 岗位s
	 * @throws Exception 
	 */
	public static String getHisStationsStr() throws Exception {
		String val = GetValFromCookie("HisStationsStr", null, true);
		if (val == null) {
			Object tempVar = BP.DA.DBAccess.RunSQLReturnVal("SELECT Stas FROM WF_Emp WHERE No='" + WebUser.getNo() + "'");
			val = (String) ((tempVar instanceof String) ? tempVar : null);

			if (val == null) {
				val = "";
			}
			SetSessionByKey("HisStationsStr", val);
		}
		return val;
	}

	public static void setHisStationsStr(String value) {
		SetSessionByKey("HisStationsStr", value);

	}

	/**
	 * 部门s
	 * @throws Exception 
	 */
	public static String getHisDeptsStr() throws Exception {
		String val = GetValFromCookie("HisDeptsStr", "", true);
		if (val == null) {
			Object tempVar = BP.DA.DBAccess.RunSQLReturnVal("SELECT Depts FROM WF_Emp WHERE No='" + WebUser.getNo() + "'");
			val = (String) ((tempVar instanceof String) ? tempVar : null);
			if (val == null) {
				val = "";
			}
			SetSessionByKey("HisDeptsStr", val);
		}
		return val;
	}

	public static void setHisDeptsStr(String value) {
		SetSessionByKey("HisDeptsStr", value);
	}
	
	public static void SignInOfGener2017(String userNo, String userName, String deptNo, String deptName, String authNo) throws UnsupportedEncodingException
	{
		SignInOfGener2017(userNo, userName, deptNo, deptName, authNo, null);
	}

	public static void SignInOfGener2017(String userNo, String userName, String deptNo, String deptName) throws UnsupportedEncodingException
	{
		SignInOfGener2017(userNo, userName, deptNo, deptName, null, null);
	}

	public static void SignInOfGener2017(String userNo, String userName, String deptNo) throws UnsupportedEncodingException
	{
		SignInOfGener2017(userNo, userName, deptNo, null, null, null);
	}

	public static void SignInOfGener2017(String userNo, String userName) throws UnsupportedEncodingException
	{
		SignInOfGener2017(userNo, userName, null, null, null, null);
	}

	public static void SignInOfGener2017(String userNo) throws UnsupportedEncodingException
	{
		SignInOfGener2017(userNo, null, null, null, null, null);
	}

	/** 
	 * 用户登录
	 * @param userNo 用户编号
	 * @param userName 用户名称
	 * @param deptNo 部门编号
	 * @param deptName 名称
	 * @param authNo 授权人编号
	 * @param authName 名称
	 * @throws UnsupportedEncodingException 
	 */
	public static void SignInOfGener2017(String userNo, String userName, String deptNo, String deptName, String authNo, String authName) throws UnsupportedEncodingException
	{
		if (ContextHolderUtils.getRequest() == null)
		{
			SystemConfig.setIsBSsystem(false);
		}
		else
		{
			SystemConfig.setIsBSsystem(true);
		}


		WebUser.setNo(userNo);
		WebUser.setName(userName);
		if (DotNetToJavaStringHelper.isNullOrEmpty(authNo) == false)
		{
			WebUser.setAuth(authNo); //被授权人，实际工作的执行者.
			WebUser.setAuthName(authName);
		}
		else
		{
			WebUser.setAuth(null); 
			WebUser.setAuthName(null); 
		}


		//登录模式？
		BP.Web.WebUser.setUserWorkDev(UserWorkDev.PC);


		///#region 解决部门的问题.
		if (BP.Sys.SystemConfig.getOSDBSrc() == OSDBSrc.Database)
		{
			if (StringHelper.isNullOrEmpty(deptNo) == true)
			{
				String sql = "";
				if (BP.Sys.SystemConfig.getOSModel() == OSModel.OneOne)
				{
					sql = "SELECT FK_Dept FROM Port_EmpDept WHERE FK_Emp='" + userNo + "'";
				}
				else
				{
					sql = "SELECT FK_Dept FROM Port_DeptEmp WHERE FK_Emp='" + userNo + "'";
				}

				deptNo = BP.DA.DBAccess.RunSQLReturnString(sql);
				if (StringHelper.isNullOrEmpty(deptNo) == true)
				{
					throw new RuntimeException("@登录人员(" + userNo + "," + userName + ")没有维护部门...");
				}
				else
				{
					//调用接口更改所在的部门.
					// WebUser.ChangeMainDept(em.No, deptNo);
				}
			}
		}



		///#endregion 解决部门的问题.

		WebUser.setFK_Dept(deptNo);
		WebUser.setFK_DeptName(deptName); 
		WebUser.setHisDeptsStr(null);
		WebUser.setHisStationsStr(null);

		if (BP.Sys.SystemConfig.getIsBSsystem())
		{
			//System.Web.HttpContext.Current.Response.Cookies.Clear();
			//HttpCookie hc = Glo.getRequest.Cookies["CCS"];
			Cookie hc = ContextHolderUtils.getCookie("CCS");
			if (hc != null)
			{
				hc = new Cookie("CCS",null);
			}

			int expiry = 60 * 60 * 24 * 2;
			ContextHolderUtils.addCookie("No", expiry, userNo);
			ContextHolderUtils.addCookie("Name", expiry, URLEncoder.encode(userName, "UTF-8"));

			ContextHolderUtils.addCookie("FK_Dept", expiry, deptNo);
			ContextHolderUtils.addCookie("FK_DeptName", expiry, URLEncoder.encode(deptName, "UTF-8"));
			if (ContextHolderUtils.getSession() != null)
			{
				ContextHolderUtils.addCookie("Token", expiry, getNoOfSessionID());
				ContextHolderUtils.addCookie("SID", expiry, getNoOfSessionID());
			}

			if (authNo == null)
			{
				authNo = "";
			}

			ContextHolderUtils.addCookie("Auth", expiry, authNo);//授权人.
			if (authName == null)
			{
				authName = "";
			}
			ContextHolderUtils.addCookie("AuthName", expiry, authName);//授权人名称..
		}
	}
}
