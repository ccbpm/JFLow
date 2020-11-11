package bp.web;

import bp.en.*;
import bp.da.*;
import bp.difference.ContextHolderUtils;
import bp.difference.SystemConfig;
import bp.port.*;
import bp.sys.*;
import bp.pub.Current;
import javax.servlet.http.Cookie;

import org.apache.poi.util.StringUtil;

import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * User 的摘要说明。
 */
public class WebUser {
	/**
	 * 密码解密
	 * 
	 * @param pass
	 *            用户输入密码
	 * @return 解密后的密码
	 */
	public static String ParsePass(String pass) {
		if (pass.equals("")) {
			return "";
		}

		String str = "";
		char[] mychars = pass.toCharArray();
		int i = 0;
		for (char c : mychars) {
			i++;

			// step 1
			long A = (long) c * 2;

			// step 2
			long B = A - i * i;

			// step 3
			long C = 0;
			if (B > 196) {
				C = 196;
			} else {
				C = B;
			}

			str = str + String.valueOf((char) C);
		}
		return str;
	}

	/**
	 * 更改一个人当前登录的主要部门 再一个人有多个部门的情况下有效.
	 * 
	 * @param empNo
	 *            人员编号
	 * @param fk_dept
	 *            当前所在的部门.
	 */
	public static void ChangeMainDept(String empNo, String fk_dept) {
		// 这里要考虑集成的模式下，更新会出现是.

		String sql = SystemConfig.GetValByKey("UpdataMainDeptSQL", "");
		if (sql.equals("")) {
			/* 如果没有配置, 就取默认的配置. */
			sql = "UPDATE Port_Emp SET FK_Dept=@FK_Dept WHERE No=@No";
		}

		sql = sql.replace("@FK_Dept", "'" + fk_dept + "'");
		sql = sql.replace("@No", "'" + empNo + "'");

		try {
			if (sql.contains("UPDATE Port_Emp SET FK_Dept=") == true) {
				if (DBAccess.IsView("Port_Emp", SystemConfig.getAppCenterDBType()) == true) {
					return;
				}
			}
			DBAccess.RunSQL(sql);
		} catch (RuntimeException ex) {
			throw new RuntimeException("@执行更改当前操作员的主部门的时候错误,请检查SQL配置:" + ex.getMessage());
		}
	}

	/**
	 * 通用的登陆
	 * 
	 * @param em
	 *            人员
	 * @param lang
	 *            语言
	 * @param auth
	 *            授权人
	 * @param isRememberMe
	 *            是否记录cookies
	 * @param IsRecSID
	 *            是否记录SID
	 * @throws Exception
	 */

	public static void SignInOfGener(Emp em, String lang, boolean isRememberMe, boolean IsRecSID, String authNo)
			throws Exception {
		SignInOfGener(em, lang, isRememberMe, IsRecSID, authNo, null);
	}

	public static void SignInOfGener(Emp em, String lang, boolean isRememberMe, boolean IsRecSID) throws Exception {
		SignInOfGener(em, lang, isRememberMe, IsRecSID, null, null);
	}

	public static void SignInOfGener(Emp em, String lang, boolean isRememberMe) throws Exception {
		SignInOfGener(em, lang, isRememberMe, false, null, null);
	}

	public static void SignInOfGener(Emp em, String lang) throws Exception {
		SignInOfGener(em, lang, false, false, null, null);
	}

	public static void SignInOfGener(Emp em) throws Exception {
		SignInOfGener(em, "CH", false, false, null, null);
	}

	public static void SignInOfGener(Emp em, String lang, boolean isRememberMe, boolean IsRecSID, String authNo,
			String authName) throws Exception {
		if (ContextHolderUtils.getInstance() == null) {
			SystemConfig.setIsBSsystem(false);
		} else {
			SystemConfig.setIsBSsystem(true);
		}

		if (SystemConfig.getIsBSsystem()) {
			bp.sys.Glo.WriteUserLog("SignIn", em.getNo(), "登录");
		}

		WebUser.setNo(em.getNo());
		WebUser.setName(em.getName());

		// 增加他的orgNo
		if (SystemConfig.getCCBPMRunModel() != CCBPMRunModel.Single) {
			WebUser.setOrgNo(
					DBAccess.RunSQLReturnString("SELECT OrgNo FROM Port_Emp WHERE No='" + WebUser.getNo() + "'"));
		}

		if (DataType.IsNullOrEmpty(authNo) == false) {
			WebUser.setAuth(authNo); // 被授权人，实际工作的执行者.
			WebUser.setAuthName(authName);
		} else {
			WebUser.setAuth("");
			WebUser.setAuthName("");
		}

		/// 解决部门的问题.
		if (DataType.IsNullOrEmpty(em.getFK_Dept()) == true) {
			String sql = "";

			sql = "SELECT FK_Dept FROM Port_DeptEmp WHERE FK_Emp='" + em.getNo() + "'";

			String deptNo = DBAccess.RunSQLReturnString(sql);
			if (DataType.IsNullOrEmpty(deptNo) == true) {
				sql = "SELECT FK_Dept FROM Port_Emp WHERE No='" + em.getNo() + "'";
				deptNo = DBAccess.RunSQLReturnString(sql);
				if (DataType.IsNullOrEmpty(deptNo) == true) {
					throw new RuntimeException("@登录人员(" + em.getNo() + "," + em.getName() + ")没有维护部门...");
				}
			} else {
				// 调用接口更改所在的部门.
				WebUser.ChangeMainDept(em.getNo(), deptNo);
			}
		}

		bp.port.Dept dept = new Dept();
		dept.setNo(em.getFK_Dept());
		if (dept.RetrieveFromDBSources() == 0) {
			throw new RuntimeException(
					"@登录人员(" + em.getNo() + "," + em.getName() + ")没有维护部门,或者部门编号{" + em.getFK_Dept() + "}不存在.");
		}

		/// 解决部门的问题.

		WebUser.setFK_Dept(em.getFK_Dept());
		WebUser.setFK_DeptName(em.getFK_DeptText());

		WebUser.setSysLang(lang);
		if (SystemConfig.getIsBSsystem()) {

			// cookie操作，为适应不同平台，统一使用HttpContextHelper
			ContextHolderUtils.addCookie("No", em.getNo());
			//ContextHolderUtils.addCookie("Name", URLEncoder.encode(em.getName(), "UTF-8"));

			if (isRememberMe) {
				ContextHolderUtils.addCookie("IsRememberMe", "1");
			} else {
				ContextHolderUtils.addCookie("IsRememberMe", "0");
			}

			ContextHolderUtils.addCookie("FK_Dept", em.getFK_Dept());
			//ContextHolderUtils.addCookie("FK_DeptName", URLEncoder.encode(em.getFK_DeptText(), "UTF-8"));

			if (ContextHolderUtils.getInstance() != null) {
				ContextHolderUtils.addCookie("Token", ContextHolderUtils.getSession().getId());
				ContextHolderUtils.addCookie("SID", ContextHolderUtils.getSession().getId());
			}
			ContextHolderUtils.addCookie("Tel", em.getTel());
			ContextHolderUtils.addCookie("Lang", lang);
			if (authNo == null) {
				authNo = "";
			}
			ContextHolderUtils.addCookie("Auth", authNo); // 授权人.

			if (authName == null) {
				authName = "";
			}
			//ContextHolderUtils.addCookie("AuthName", authName); // 授权人名称..

		}
	}

	/// 静态方法
	/**
	 * 通过key,取出session.
	 * 
	 * @param key
	 *            key
	 * @param isNullAsVal
	 *            如果是Null, 返回的值.
	 * @return
	 * @throws Exception
	 */
	public static String GetSessionByKey(String key, String isNullAsVal) throws Exception {
		// 2019-07-25 zyt改造
		if (getIsBSMode() && ContextHolderUtils.getInstance() != null
				&& ContextHolderUtils.getInstance().getSession() != null) {
			String str = (String) ContextHolderUtils.getSession().getAttribute(key);
			if (DataType.IsNullOrEmpty(str)) {
				str = isNullAsVal;
			}
			return str;
		} else {
			if (bp.pub.Current.Session.get(key) == null || bp.pub.Current.Session.get(key).toString().equals("")) {
				bp.pub.Current.Session.put(key, isNullAsVal);
				return isNullAsVal;
			} else {
				return (String) bp.pub.Current.Session.get(key);
			}
		}
	}

	///

	/**
	 * 是不是b/s 工作模式。
	 * 
	 * @throws Exception
	 */
	protected static boolean getIsBSMode() throws Exception {
		if (ContextHolderUtils.getInstance() == null) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 设置session
	 * 
	 * @param key
	 *            键
	 * @param val
	 *            值
	 * @throws Exception
	 */
	public static void SetSessionByKey(String key, String val) throws Exception {

		if (val == null) {
			return;
		}
		if (getIsBSMode() && null != ContextHolderUtils.getRequest() && null != ContextHolderUtils.getSession()) {
			ContextHolderUtils.getSession().setAttribute(key, val);
		} else {
			Current.SetSession(key, val);
		}

	}

	/**
	 * 退回
	 */
	public static void Exit() {
		try {
			// 清理Session
			WebUser.setNo("");
			WebUser.setName("");
			WebUser.setFK_Dept("");
			WebUser.setFK_DeptName("");
			WebUser.setSID("");
			WebUser.setAuth("");
			WebUser.setSysLang("");
			if (SystemConfig.getIsBSsystem()) {

				ContextHolderUtils.addCookie("No", "");
				ContextHolderUtils.addCookie("Name", "");
				ContextHolderUtils.addCookie("IsRememberMe", "");
				ContextHolderUtils.addCookie("FK_Dept", "");
				ContextHolderUtils.addCookie("FK_DeptName", "");
				ContextHolderUtils.addCookie("Token", "");
				ContextHolderUtils.addCookie("SID", "");
				ContextHolderUtils.addCookie("Lang", "");
				ContextHolderUtils.addCookie("Auth", "");
			}
		} catch (java.lang.Exception e2) {
		}
	}

	/**
	 * 授权人
	 * 
	 * @throws Exception
	 */
	public static String getAuth() throws Exception {
		String val = GetValFromCookie("Auth", null, false);
		if (DataType.IsNullOrEmpty(val)==true) {
			val = GetSessionByKey("Auth", null);
		}
		return val;
	}

	public static void setAuth(String value) throws Exception {
		if (DataType.IsNullOrEmpty(value) == true) {
			SetSessionByKey("Auth", "");
		} else {
			SetSessionByKey("Auth", value);
		}
	}

	/**
	 * 部门名称
	 */
	public static String getFK_DeptName() {
		try {
			String val = GetValFromCookie("FK_DeptName", null, true);
			return val;
		} catch (java.lang.Exception e) {
			return "无";
		}
	}

	public static void setFK_DeptName(String value) throws Exception {
		SetSessionByKey("FK_DeptName", value);
	}

	/**
	 * 部门全称
	 * 
	 * @throws Exception
	 */
	public static String getFK_DeptNameOfFull() throws Exception {
		String val = GetValFromCookie("FK_DeptNameOfFull", null, true);
		if (DataType.IsNullOrEmpty(val)) {
			try {
				
				val = DBAccess.RunSQLReturnStringIsNull(
						"SELECT NameOfPath FROM Port_Dept WHERE No='" + WebUser.getFK_Dept() + "'", null);

				if (DataType.IsNullOrEmpty(val))
					val = WebUser.getFK_DeptName();

				WebUser.setFK_DeptNameOfFull(val);

				return val;
			} catch (java.lang.Exception e) {
				val = WebUser.getFK_DeptName();
			}

			// 给它赋值.
			setFK_DeptNameOfFull(val);
		}
		return val;
	}

	public static void setFK_DeptNameOfFull(String value) throws Exception {
		SetSessionByKey("FK_DeptNameOfFull", value);
	}

	/**
	 * 令牌
	 * 
	 * @throws Exception
	 */
	public static String getToken() throws Exception {
		return GetSessionByKey("token", "null");
	}

	public static void setToken(String value) throws Exception {
		SetSessionByKey("token", value);
	}

	/**
	 * 语言
	 */
	public static String getSysLang() {
		return "CH";

	}

	public static void setSysLang(String value) throws Exception {
		SetSessionByKey("Lang", value);
	}

	/**
	 * 当前登录人员的部门
	 * 
	 * @throws Exception
	 */
	public static String getFK_Dept() throws Exception {
		String val = GetValFromCookie("FK_Dept", null, false);
		if (val == null) {
			if (WebUser.getNo() == null) {
				throw new RuntimeException("@登录信息丢失，请你确认是否启用了cookie? ");
			}

			String sql = "SELECT FK_Dept FROM Port_Emp WHERE No='" + WebUser.getNo() + "'";
			String dept = DBAccess.RunSQLReturnStringIsNull(sql, null);
			if (dept == null) {
				sql = "SELECT FK_Dept FROM Port_Emp WHERE No='" + WebUser.getNo() + "'";
				dept = DBAccess.RunSQLReturnStringIsNull(sql, null);
			}

			if (dept == null) {
				throw new RuntimeException("@err-003 FK_Dept，当前登录人员(" + WebUser.getNo() + ")，没有设置部门。");
			}

			SetSessionByKey("FK_Dept", dept);
			return dept;
		}
		return val;
	}

	public static void setFK_Dept(String value) throws Exception {
		SetSessionByKey("FK_Dept", value);
	}

	/**
	 * 所在的集团编号
	 * 
	 * @throws Exception
	 */
	public static String getGroupNo111() throws Exception {
		String val = GetValFromCookie("GroupNo", null, false);
		if (val == null) {
			if (!SystemConfig.getCustomerNo().equals("Bank")) {
				return "0";
			}

			if (WebUser.getNo() == null) {
				throw new RuntimeException("@登录信息丢失，请你确认是否启用了cookie? ");
			}

			String sql = "SELECT GroupNo FROM Port_Dept WHERE No='" + WebUser.getFK_Dept() + "'";
			String groupNo = DBAccess.RunSQLReturnStringIsNull(sql, null);

			if (groupNo == null) {
				throw new RuntimeException("@err-003 FK_Dept，当前登录人员(" + WebUser.getNo() + ")，没有设置部门。");
			}

			SetSessionByKey("GroupNo", groupNo);
			return groupNo;
		}
		return val;
	}

	public static void setGroupNo111(String value) throws Exception {
		SetSessionByKey("GroupNo", value);
	}

	/**
	 * 当前登录人员的父节点编号
	 * 
	 * @throws Exception
	 */
	public static String getDeptParentNo() throws Exception {
		String val = GetValFromCookie("DeptParentNo", null, false);
		if (val == null) {
			if (WebUser.getFK_Dept() == null) {
				throw new RuntimeException("@err-001 DeptParentNo, FK_Dept 登录信息丢失。");
			}

			bp.port.Dept dept = new bp.port.Dept(WebUser.getFK_Dept());
			WebUser.setDeptParentNo(dept.getParentNo());
			return dept.getParentNo();
		}
		return val;
	}

	public static void setDeptParentNo(String value) throws Exception {
		SetSessionByKey("DeptParentNo", value);
	}

	/**
	 * 检查权限控制
	 * 
	 * @param sid
	 * @return
	 * @throws Exception
	 */
	public static boolean CheckSID(String userNo, String sid) throws Exception {

		Paras paras = new Paras();
		paras.SQL = "SELECT SID FROM Port_Emp WHERE No=" + SystemConfig.getAppCenterDBVarStr() + "No";
		paras.Add("No", userNo);
		String mysid = DBAccess.RunSQLReturnStringIsNull(paras, null);
		return sid.equals(mysid);
	}

	public static String getNoOfRel() throws Exception {
		String val = GetSessionByKey("No", null);
		if (val == null) {
			return GetValFromCookie("No", null, true);
		}
		return val;
	}

	public static String GetValFromCookie(String valKey, String isNullAsVal, boolean isChinese) throws Exception {
		if (!getIsBSMode()) {
			return Current.GetSessionStr(valKey, isNullAsVal);
		}

		try {
			// 先从session里面取.
			Object value = ContextHolderUtils.getSession().getAttribute(valKey);
			String v = value == null ? "" : String.valueOf(value);
			if (DataType.IsNullOrEmpty(v) == false) {
				if (isChinese) {
					v = URLDecoder.decode(v, "UTF-8");
				}
				return v;
			}
		} catch (java.lang.Exception e) {

		}

		try {
			String val = null;
			Cookie cookie = ContextHolderUtils.getCookie(valKey);
			if (cookie != null) {
				if (isChinese) {
					val = URLDecoder.decode(cookie.getValue(), "UTF-8");
				} else {
					val = cookie.getValue();
				}
			}

			if (DataType.IsNullOrEmpty(val) == true) {
				return isNullAsVal;
			}
			return val;
		} catch (java.lang.Exception e2) {
			e2.printStackTrace();
			return isNullAsVal;
		}
	}

	/**
	 * 设置信息.
	 * 
	 * @param keyVals
	 * @throws Exception
	 */
	public static void SetValToCookie(String keyVals) throws Exception {
		if (SystemConfig.getIsBSsystem() == false) {
			return;
		}
		AtPara ap = new AtPara(keyVals);
		for (String key : ap.getHisHT().keySet()) {
			ContextHolderUtils.addCookie(key, ap.GetValStrByKey(key));
		}

	}

	/**
	 * 是否是操作员？
	 * 
	 * @throws Exception
	 */
	public static boolean getIsAdmin() throws Exception {
		if (WebUser.getNo().equals("admin") == true) {
			return true;
		}

		if (SystemConfig.getCCBPMRunModel() == CCBPMRunModel.Single) {
			return false; // 单机版.
		}

		// SAAS版本. 集团版 @hongyan 需要翻译.
		if (SystemConfig.getCCBPMRunModel() != CCBPMRunModel.Single) {
			String sql = "SELECT FK_Emp FROM Port_OrgAdminer WHERE FK_Emp='" + WebUser.getNo() + "' AND OrgNo='"
					+ WebUser.getOrgNo() + "'";
			if (DBAccess.RunSQLReturnTable(sql).Rows.size() == 0) {
				return false;
			}
			return true;
		}
		return false;
	}

	/**
	 * 编号
	 * 
	 * @throws Exception
	 */
	public static String getNo() throws Exception {
		return GetValFromCookie("No", null, true);
	}

	public static void setNo(String value) throws Exception {
		
		SetSessionByKey("No", value.trim()); // @祝梦娟.
	}

	/**
	 * 名称
	 * 
	 * @throws Exception
	 */
	public static String getName() throws Exception {
		String no = WebUser.getNo();
		if (no == null) {
			throw new RuntimeException("@err-002 no 登录信息丢失。");
		}
		String val = GetValFromCookie("Name", no, true);
		if (val == null) {
			throw new RuntimeException("@err-002 Name 登录信息丢失。");
		}

		return val;
	}

	public static void setName(String value) throws Exception {
		SetSessionByKey("Name", value);
	}

	/**
	 * 运行设备
	 * 
	 * @throws Exception
	 */
	public static String getSheBei() throws Exception {
		String no = WebUser.getNo();
		String val = GetValFromCookie("SheBei", no, true);
		if (val == null) {
			return "PC";
		}
		return val;
	}

	public static void setSheBei(String value) throws Exception {
		SetSessionByKey("SheBei", value);
	}

	/**
	 * 更新当前管理员的组织SID信息.
	 * 
	 * @throws Exception
	 */
	public static void UpdateSIDAndOrgNoSQL() throws Exception {
		if (DBAccess.IsView("Port_Emp") == false) {
			String sql = "UPDATE Port_Emp SET SID='" + WebUser.getSID() + "',OrgNo='" + WebUser.getOrgNo()
					+ "', FK_Dept='" + WebUser.getFK_Dept() + "' WHERE No='" + WebUser.getNo() + "'";
			DBAccess.RunSQL(sql);
		} else {
			String sql = Glo.getUpdateSIDAndOrgNoSQL();
			if (DataType.IsNullOrEmpty(sql) == true) {
				throw new RuntimeException("err@系统管理员缺少全局配置变量 UpdateSIDAndOrgNoSQL ");
			}

			sql = sql.replace("@FK_Dept", WebUser.getFK_Dept());
			sql = sql.replace("@OrgNo", WebUser.getOrgNo());
			sql = sql.replace("@SID", WebUser.getSID());
			sql = sql.replace("@No", WebUser.getNo());
			DBAccess.RunSQL(sql);
		}
	}

	/**
	 * 所在的组织
	 * 
	 * @throws Exception
	 */
	public static String getOrgNo() throws Exception {
		if (SystemConfig.getCCBPMRunModel() == CCBPMRunModel.Single) {
			return "";
		}

		String val = GetValFromCookie("OrgNo", null, true);
		if (val == null) {
			if (WebUser.getNo() == null) {
				return "";
			}
			String no = DBAccess.RunSQLReturnString("SELECT OrgNo FROM Port_Emp WHERE No='" + WebUser.getNo() + "'");
			SetSessionByKey("OrgNo", no);
			return no;
		}
		return val;
	}

	public static void setOrgNo(String value) throws Exception {
		SetSessionByKey("OrgNo", value);
	}

	public static String getOrgName() throws Exception {
		if (SystemConfig.getCCBPMRunModel() == CCBPMRunModel.Single) {
			return "";
		}

		String val = GetValFromCookie("OrgName", null, true);
		if (val == null) {
			if (WebUser.getNo() == null) {
				throw new RuntimeException("@err-006 OrgName 登录信息丢失，或者在 CCBPMRunModel=0 的模式下不能读取该节点.");
			}

			val = DBAccess.RunSQLReturnString("SELECT Name FROM Port_Org WHERE No='" + WebUser.getOrgNo() + "'");
			SetSessionByKey("OrgName", val);

		}
		if (val == null) {
			val = "";
		}
		return val;
	}

	public static void setOrgName(String value) throws Exception {
		SetSessionByKey("OrgName", value);
	}

	/**
	 * 手机号
	 * 
	 * @throws Exception
	 */
	public static String getTel() throws Exception {
		String val = GetValFromCookie("Tel", null, false);
		if (val == null) {
			if (WebUser.getNo() == null) {
				throw new RuntimeException("@登录信息丢失，请你确认是否启用了cookie? ");
			}

			String sql = "SELECT Tel FROM Port_Emp WHERE No='" + WebUser.getNo() + "'";
			String tel = DBAccess.RunSQLReturnStringIsNull(sql, null);

			SetSessionByKey("Tel", tel);
			return tel;
		}
		return val;
	}

	public static void setTel(String value) throws Exception {
		SetSessionByKey("Tel", value);
	}

	/**
	 * 域
	 * 
	 * @throws Exception
	 */
	public static String getDomain() throws Exception {
		String val = GetValFromCookie("Domain", null, true);
		if (val == null) {
			throw new RuntimeException("@err-003 Domain 登录信息丢失。");
		}
		return val;
	}

	public static void setDomain(String value) throws Exception {
		SetSessionByKey("Domain", value);
	}

	public static Stations getHisStations() throws Exception {
		Stations sts = new Stations();
		QueryObject qo = new QueryObject(sts);
		qo.AddWhereInSQL("No", "SELECT FK_Station FROM Port_DeptEmpStation WHERE FK_Emp='" + WebUser.getNo() + "'");
		qo.DoQuery();

		return sts;
	}

	/**
	 * SID
	 * 
	 * @throws Exception
	 */
	public static String getSID() throws Exception {
		String val = GetValFromCookie("SID", null, true);
		if (val == null) {
			return "";
		}
		return val;
	}

	public static void setSID(String value) throws Exception {
		SetSessionByKey("SID", value);
	}

	/**
	 * 设置SID
	 * 
	 * @param sid
	 * @throws Exception
	 */
	public static void SetSID(String sid) throws Exception {
		// 判断是否视图，如果为视图则不进行修改
		if (DBAccess.IsView("Port_Emp", SystemConfig.getAppCenterDBType()) == false) {
			Paras ps = new Paras();
			ps.SQL = "UPDATE Port_Emp SET SID=" + SystemConfig.getAppCenterDBVarStr() + "SID WHERE No="
					+ SystemConfig.getAppCenterDBVarStr() + "No";
			ps.Add("SID", sid);
			ps.Add("No", WebUser.getNo());
			DBAccess.RunSQL(ps);
		}
		WebUser.setSID(sid);
	}

	/**
	 * 是否是授权状态
	 * 
	 * @throws Exception
	 */
	public static boolean getIsAuthorize() throws Exception {
		if (getAuth() == null || getAuth().equals("")) {
			return false;
		}
		return true;
	}

	/**
	 * 使用授权人ID
	 * 
	 * @throws Exception
	 */
	public static String getAuthName() throws Exception {
		String val = GetValFromCookie("AuthName", null, false);
		if (val == null) {
			val = GetSessionByKey("AuthName", null);
		}
		return val;
	}

	public static void setAuthName(String value) throws Exception {
		if (DataType.IsNullOrEmpty(value) == true) {
			SetSessionByKey("AuthName", "");
		} else {
			SetSessionByKey("AuthName", value);
		}
	}

	public static String getTheame() throws Exception {
		String val = GetValFromCookie("Theame", null, false);
		if (val == null) {
			val = GetSessionByKey("Theame", null);
		}
		return val;
	}

	public static void setTheame(String value) throws Exception {
		if (value.equals("")) {
			SetSessionByKey("Theame", "");
		} else {
			SetSessionByKey("Theame", value);
		}
	}

}