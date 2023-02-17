package bp.web;

import bp.en.*;
import bp.da.*;
import bp.port.*;
import bp.sys.*;
import bp.pub.*;
import bp.difference.*;
import bp.tools.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.util.*;

/**
 * User 的摘要说明。
 */
public class WebUser {
//   public static Hashtable htWebUser=null;

    /**
     * 更改一个人当前登录的主要部门
     * 再一个人有多个部门的情况下有效.
     * <p>
     * param empNo 人员编号
     * param fk_dept 当前所在的部门.
     */
    public static void ChangeMainDept(String empNo, String fk_dept) {
        //这里要考虑集成的模式下，更新会出现是.

        String sql = SystemConfig.GetValByKey("UpdataMainDeptSQL", "");
        if (sql.equals("")) {
            /*如果没有配置, 就取默认的配置.*/
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
     * <p>
     * param em 人员
     * param lang 语言
     * //param auth 授权人
     * param isRememberMe 是否记录cookies
     * param IsRecSID 是否记录SID
     */

    public static void SignInOfGener(Emp em, String lang, boolean isRememberMe, boolean IsRecSID, String authNo) throws Exception {
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

    public static void SignInOfGener(Emp em, String lang, boolean isRememberMe, boolean IsRecSID, String authNo, String authName) throws Exception {
        if (ContextHolderUtils.getInstance() == null) {
            SystemConfig.setIsBSsystem(false);
        } else {
            SystemConfig.setIsBSsystem(true);
        }

        if (SystemConfig.getIsBSsystem()) {
            bp.sys.base.Glo.WriteUserLog(em.getNo(), "登录");
        }
        HashMap<String, String> ht = new HashMap<>();
        //存储WebUser信息
        ht.put("No", em.getNo());
        ht.put("Name", em.getName());

        // 增加他的orgNo
        if (SystemConfig.getCCBPMRunModel() != CCBPMRunModel.Single) {
            ht.put("OrgNo",
                    DBAccess.RunSQLReturnString("SELECT OrgNo FROM Port_Emp WHERE No='" + em.getNo() + "'"));
        }

        if (DataType.IsNullOrEmpty(authNo) == false) {
            ht.put("Auth", authNo); // 被授权人，实际工作的执行者.
            ht.put("AuthName", authName);
        } else {
            ht.put("Auth", ""); // 被授权人，实际工作的执行者.
            ht.put("AuthName", "");
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

        ht.put("FK_Dept", em.getFK_Dept());
        ht.put("FK_DeptName", em.getFK_DeptText());

        ht.put("SysLang", lang);
        if(SystemConfig.getRedisIsEnable()){
            String ip = getIp();
            if (!StringUtils.isBlank(ip)) {
                ContextHolderUtils.getRedisUtils().set(false, SystemConfig.getRedisCacheKey("WebUser_" + ip), ht,300);
            }
        }else{
            for (java.util.Map.Entry<String, String> next: ht.entrySet()) {
                WebUser.setItemValue(next.getKey(),next.getValue());
            }
        }

        if (SystemConfig.getIsBSsystem()) {

            // cookie操作，为适应不同平台，统一使用HttpContextHelper
            ContextHolderUtils.addCookie("No", em.getNo());
            if (isRememberMe) {
                ContextHolderUtils.addCookie("IsRememberMe", "1");
            } else {
                ContextHolderUtils.addCookie("IsRememberMe", "0");
            }

            ContextHolderUtils.addCookie("FK_Dept", em.getFK_Dept());

            ContextHolderUtils.addCookie("Tel", em.getTel());
            ContextHolderUtils.addCookie("Lang", lang);
            if (authNo == null) {
                authNo = "";
            }
            ContextHolderUtils.addCookie("Auth", authNo); // 授权人.

            if (authName == null) {
                authName = "";
            }

        }
    }


    ///#region 静态方法

    /**
     * 通过key,取出session.
     * <p>
     * param key key
     * param isNullAsVal 如果是Null, 返回的值.
     *
     * @return
     */
    public static String GetSessionByKey(String key, String isNullAsVal) {

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

    ///#endregion

    /**
     * 是不是b/s 工作模式。
     */
    protected static boolean isBSMode() {
        if (ContextHolderUtils.getInstance() == null) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 设置session
     * <p>
     * param key 键
     * param val 值
     */
    public static void SetSessionByKey(String key, String val) {
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
    public static void Exit() throws Exception {
        if(SystemConfig.getRedisIsEnable()){
            String token = getIp();
            if(DataType.IsNullOrEmpty(token)==false && WebUser.getIsAdmin()==false) {
                String guid = DBAccess.GenerGUID();
                DBAccess.RunSQL("UPDATE WF_Emp SET AtPara=REPLACE(AtPara,'@Token_PC=" + bp.web.WebUser.getToken() + "', '@Token_PC=" + guid + "') WHERE No = '" + bp.web.WebUser.getNo() + "'");
                ContextHolderUtils.getRedisUtils().del(false, SystemConfig.getRedisCacheKey("WebUser_" + token));
            }
        }
        try {
            // 清理Session
            if(SystemConfig.getRedisIsEnable()==false){
                WebUser.setNo("");
                WebUser.setName("");
                WebUser.setFK_Dept("");
                WebUser.setFK_DeptName("");
                WebUser.setSID("");
                WebUser.setAuth("");
                WebUser.setSysLang("");
            }
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
     */
    public static String getAuth() throws Exception {
        return getItemValue("Auth");
    }

    public static String getItemValue(String item) {
        if(SystemConfig.getRedisIsEnable()){
            if(DataType.IsNullOrEmpty(getIp())==true)
                return "";
            Object obj = ContextHolderUtils.getRedisUtils().get(false, SystemConfig.getRedisCacheKey("WebUser_" + getIp()));
            if (obj == null)
                return "";
            HashMap ht = obj instanceof HashMap ? (HashMap) obj : null;
            if(ht==null)
                return "";
            return ht.get(item) != null ? ht.get(item).toString() : "";
        }
        return GetValFromCookie(item, null, true);
    }

    public static void setItemValue(String item, String value) {
        if(SystemConfig.getRedisIsEnable()){
            String ip = getIp();
            if (StringUtils.isBlank(ip)) {
                return;
            }
            Object obj = ContextHolderUtils.getRedisUtils().get(false, SystemConfig.getRedisCacheKey("WebUser_" + ip));
            HashMap<String, Object> ht = null;
            if (obj == null)
                ht = new HashMap<>();
            else
                ht = obj instanceof HashMap ? (HashMap) obj : null;
            if (value == null) value = "";
            if (ht != null) {
                ht.put(item, value);
                ContextHolderUtils.getRedisUtils().set(false, SystemConfig.getRedisCacheKey("WebUser_" + ip), ht,300);
            }
            return;
        }
        SetSessionByKey(item, value.trim());
    }

    public static void setAuth(String value) throws Exception {
        setItemValue("Auth", value);
    }

    /**
     * 部门名称
     */
    public static String getFK_DeptName() {
        return getItemValue("FK_DeptName");
    }

    public static void setFK_DeptName(String value) throws Exception {
        setItemValue("FK_DeptName", value);
    }

    /**
     * 部门全称
     */
    public static String getFK_DeptNameOfFull() {
        String val = getItemValue("FK_DeptNameOfFull");
		if (DataType.IsNullOrEmpty(val))
		{
			try
			{

				Paras ps = new Paras();
				ps.SQL = "SELECT NameOfPath FROM Port_Dept WHERE No =" + ps.getDBStr() + "No";
				ps.Add("No", WebUser.getFK_Dept());
				val = DBAccess.RunSQLReturnStringIsNull(ps, null);
				if (DataType.IsNullOrEmpty(val))
					val = WebUser.getFK_DeptName();

				WebUser.setFK_DeptNameOfFull(val);
				return val;
			}
			catch (java.lang.Exception e)
			{
				val = WebUser.getFK_DeptName();
			}
		}
		return val;
    }

    public static void setFK_DeptNameOfFull(String value) throws Exception {
        setItemValue("FK_DeptNameOfFull", value);
    }

    /**
     * 令牌
     */
    public static String getToken() {
        return getIp();
        //return GetValFromCookie("Token", null, false);
    }

    public static void setToken(String value) throws Exception {
        ContextHolderUtils.addCookie("Token", value);
        setItemValue("Token", value);
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
     */
    public static String getFK_Dept() {
        String val =  getItemValue("FK_Dept");
		if (val == null)
		{
			if (WebUser.getNo() == null)
				throw new RuntimeException("@登录信息丢失，请你确认是否启用了cookie? ");

			String sql = "SELECT FK_Dept FROM Port_Emp WHERE No='" + WebUser.getNo() + "'";
			String dept = DBAccess.RunSQLReturnStringIsNull(sql, null);
			if (dept == null)
			{
				sql = "SELECT FK_Dept FROM Port_Emp WHERE No='" + WebUser.getNo() + "'";
				dept = DBAccess.RunSQLReturnStringIsNull(sql, null);
			}

			if (dept == null)
				throw new RuntimeException("@err-003 FK_Dept，当前登录人员(" + WebUser.getNo() + ")，没有设置部门。");
            setItemValue("FK_Dept", dept);
			return dept;
		}
		return val;
    }

    public static void setFK_Dept(String value){
        setItemValue("FK_Dept", value);
    }

    /**
     * 当前登录人员的父节点编号
     */
    public static String getDeptParentNo() throws Exception {
        String val = getItemValue("DeptParentNo");
        if (val == null) {
            if (bp.web.WebUser.getFK_Dept() == null) {
                throw new RuntimeException("@err-001 DeptParentNo, FK_Dept 登录信息丢失。");
            }

            Dept dept = new Dept(bp.web.WebUser.getFK_Dept());
            bp.web.WebUser.setDeptParentNo(dept.getParentNo());
            return dept.getParentNo();
        }
        return val;
    }

    public static void setDeptParentNo(String value) throws Exception {
        setItemValue("DeptParentNo", value);
    }

    public static String getNoOfRel() throws Exception {
        return getItemValue("No");
    }

    public static String GetValFromCookie(String valKey, String isNullAsVal, boolean isChinese) {
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
            if (valKey.equals("No") && DataType.IsNullOrEmpty(v) == true)
                return "";
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
                return "";
            }
            return val;
        } catch (java.lang.Exception e2) {
            e2.printStackTrace();
            return "";
        }
    }

    /**
     * 是不是b/s 工作模式。
     *
     * @throws Exception
     */
    protected static boolean getIsBSMode() {
        if (ContextHolderUtils.getInstance() == null) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 设置信息.
     * <p>
     * param keyVals
     */
    public static void SetValToCookie(String keyVals) {
        if (SystemConfig.getIsBSsystem() == false) {
            return;
        }
        AtPara ap = new AtPara(keyVals);
        for (String key : ap.getHisHT().keySet()) {
            ContextHolderUtils.addCookie(key, ap.GetValStrByKey(key));
        }
    }

    public static boolean getIsAdmin() {
        if (WebUser.getNo() != null && WebUser.getNo().equals("admin")) {
            return true;
        }

        if (SystemConfig.getCCBPMRunModel() == CCBPMRunModel.Single) {
            return false; // 单机版.
        }

        // SAAS版本. 集团版
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
     */
    public static String getNo() {
        return getItemValue("No");
    }

    public static void setNo(String value) throws Exception {
        setItemValue("No", value.trim());
    }

    /**
     * 名称
     */
    public static String getName() {
		String no = bp.web.WebUser.getNo();

		String val = getItemValue("Name");
        if(DataType.IsNullOrEmpty(val))
            val = no;
		if (val == null)
			throw new RuntimeException("@err-002 Name 登录信息丢失。");
		return val;
    }

    public static void setName(String value) throws Exception {
        setItemValue("Name", value);
    }

    /**
     * 运行设备
     */
    public static String getSheBei() throws Exception {
        return "PC";
		/*String no = bp.web.WebUser.getNo();
		String val = GetValFromCookie("SheBei", no, true);
		if (val == null)
		{
			return "PC";
		}
		return val;*/
    }

    public static void setSheBei(String value) throws Exception {
        SetSessionByKey("SheBei", value);
    }

    /**
     * 更新当前管理员的组织SID信息.
     */
    public static void UpdateSIDAndOrgNoSQL() throws Exception {
        String sql = "";
        if (DBAccess.IsView("Port_Emp") == false) {
            sql = "UPDATE Port_Emp SET OrgNo='" + WebUser.getOrgNo() + "', FK_Dept='" + WebUser.getFK_Dept() + "' WHERE No='" + WebUser.getNo() + "'";
            DBAccess.RunSQL(sql);

            sql = "UPDATE WF_Emp SET OrgNo='" + WebUser.getOrgNo() + "', FK_Dept='" + WebUser.getFK_Dept() + "' WHERE No='" + WebUser.getNo() + "'";
            DBAccess.RunSQL(sql);
            return;
        }

        //比如: UPDATE XXX SET bumenbianao='@FK_Dept', zhizhibianhao='@OrgNo',  SID='@SID'  WHERE bianhao='@No'
        sql = bp.sys.base.Glo.getUpdateSIDAndOrgNoSQL();
        if (DataType.IsNullOrEmpty(sql) == true) {
            return;
        }
        //      throw new Exception("err@系统管理员缺少全局配置变量 AppSetting UpdateSIDAndOrgNoSQL ");

        sql = sql.replace("@FK_Dept", WebUser.getFK_Dept());
        sql = sql.replace("@OrgNo", WebUser.getOrgNo());
        sql = sql.replace("@Token", WebUser.getToken());
        sql = sql.replace("@No", WebUser.getNo());
        DBAccess.RunSQL(sql);
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
     * <p>
     * param sid
     *
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
     * 所在的组织
     */
    public static String getOrgNo() {
        if (SystemConfig.getCCBPMRunModel() == CCBPMRunModel.Single) {
            return "";
        }
        String val =  getItemValue("OrgNo");
        if (DataType.IsNullOrEmpty(val)==true) {
            if (WebUser.getNo() == null) {
                throw new RuntimeException("err@登陆信息丢失，请重新登录.");
            }

            if (SystemConfig.getCCBPMRunModel() == CCBPMRunModel.SAAS) {
                String no = DBAccess.RunSQLReturnString("SELECT OrgNo FROM Port_Emp WHERE UserID='" + WebUser.getNo() + "'");
                if(DataType.IsNullOrEmpty(no)==true)
                    throw new RuntimeException("err@SAAS模式人员编号为["+WebUser.getNo()+"]的Port_Emp表中组织编号不能为空");
                setItemValue("OrgNo", no);
                return no;
            }

            if (SystemConfig.getCCBPMRunModel() == CCBPMRunModel.GroupInc) {
                String no = DBAccess.RunSQLReturnString("SELECT OrgNo FROM Port_Emp WHERE No='" + WebUser.getNo() + "'");
                if(DataType.IsNullOrEmpty(no)==true)
                    throw new RuntimeException("err@集团模式人员编号为["+WebUser.getNo()+"]的Port_Emp表中组织编号不能为空");
                setItemValue("OrgNo", no);
                return no;
            }
        }
        return val;
    }

    public static void setOrgNo(String value) {
        setItemValue("OrgNo", value);
    }

    public static String getOrgName() {
        if (SystemConfig.getCCBPMRunModel() == CCBPMRunModel.Single) {
            return "";
        }

        String val = getItemValue("OrgName");
        if (val == null) {
            if (WebUser.getNo() == null) {
                throw new RuntimeException("@err-006 OrgName 登录信息丢失，或者在 CCBPMRunModel=0 的模式下不能读取该节点.");
            }

            val = DBAccess.RunSQLReturnString("SELECT Name FROM Port_Org WHERE No='" + WebUser.getOrgNo() + "'");
            setItemValue("OrgName", val);

        }
        if (val == null) {
            val = "";
        }
        return val;
    }

    public static void setOrgName(String value) throws Exception {
        setItemValue("OrgName", value);
    }

    /**
     * 手机号
     */
    public static String getTel() throws Exception {
        String val = getItemValue("Tel");
        if (val == null) {
            if (WebUser.getNo() == null) {
                throw new RuntimeException("@登录信息丢失，请你确认是否启用了cookie? ");
            }

            String sql = "SELECT Tel FROM Port_Emp WHERE No='" + WebUser.getNo() + "'";
            String tel = DBAccess.RunSQLReturnStringIsNull(sql, null);

            setItemValue("Tel", tel);
            return tel;
        }
        return val;
    }

    public static void setTel(String value) throws Exception {
        setItemValue("Tel", value);
    }

    /**
     * 域
     */
    public static String getDomain() throws Exception {
        String val = getItemValue("Domain");
        if (val == null) {
            throw new RuntimeException("@err-003 Domain 登录信息丢失。");
        }
        return val;
    }

    public static void setDomain(String value) throws Exception {
        setItemValue("Domain", value);
    }

    public static Stations getHisStations() throws Exception {
        Stations sts = new Stations();
        QueryObject qo = new QueryObject(sts);
        qo.AddWhereInSQL("No", "SELECT FK_Station FROM Port_DeptEmpStation WHERE FK_Emp='" + WebUser.getNo() + "'");
        qo.DoQuery();

        return sts;
    }

    /**
     * 是否是授权状态
     */
    public static boolean getIsAuthorize() throws Exception {
        if (getAuth() == null || getAuth().equals("")) {
            return false;
        }
        return true;
    }

    /**
     * 使用授权人ID
     */
    public static String getAuthName() throws Exception {
        return getItemValue("AuthName");
		/*String val = GetValFromCookie("AuthName", null, false);
		if (val == null)
		{
			val = GetSessionByKey("AuthName", null);
		}
		return val;*/
    }

    public static void setAuthName(String value) throws Exception {
            setItemValue("AuthName", value);

    }

    /**
     * 获取唯一标识
     *
     * @return
     */
    private static String getIp() {
        HttpServletRequest request = ContextHolderUtils.getRequest();
        Cookie[] cookies = request.getCookies();
        String cookieValue = "";
        if (DataType.IsNullOrEmpty(cookies) == false) {
            for (Cookie cookie : cookies) {
                String name = cookie.getName();
                if (name.equals("Token")) {
                    cookieValue = cookie.getValue();
                    break;
                }
            }
        }
        if (DataType.IsNullOrEmpty(cookieValue) == true) {
            cookieValue = ContextHolderUtils.getRequest().getHeader("Token");
        }
        if (DataType.IsNullOrEmpty(cookieValue) == true) {
            Object obj = ContextHolderUtils.getRequest().getAttribute("Token");
            if(obj!=null)
                cookieValue = obj.toString();
        }
        return cookieValue;
    }
}