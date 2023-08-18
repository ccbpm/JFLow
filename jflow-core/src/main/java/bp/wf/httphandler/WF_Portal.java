package bp.wf.httphandler;

import bp.da.*;
import bp.en.Entity;
import bp.sys.*;
import bp.web.*;
import bp.ccfast.ccmenu.*;
import bp.wf.Glo;
import bp.wf.port.admin2group.*;
import bp.tools.*;
import bp.ccfast.portal.*;
import bp.difference.*;
import bp.wf.xml.*;
import bp.port.*;
import bp.wf.port.*;
import bp.wf.template.*;
import bp.*;
import bp.wf.*;

import javax.servlet.http.Cookie;
import java.util.*;
import java.io.*;
import java.time.*;

/** 
 页面功能实体
*/
public class WF_Portal extends bp.difference.handler.DirectoryPageBase
{
	public final String getPageID()
	{
		String pageID = this.GetRequestVal("PageID");
		if (DataType.IsNullOrEmpty(pageID) == true)
		{
			pageID = "Home";
		}

		return pageID;
	}
	/** 
	 初始化
	 
	 @return 
	*/
	public final String Home_Init() throws Exception {
		WindowTemplates ens = new WindowTemplates();
		ens.Retrieve(WindowTemplateAttr.PageID, this.getPageID(), "Idx");
		if (ens.size() == 0 && this.getPageID().equals("Home") == true)
		{
			ens.InitHomePageData(); //初始化数据.
			ens.Retrieve(WindowTemplateAttr.PageID, this.getPageID(), "Idx");
		}

		//初始化数据.
		ens.InitDocs();

		DataTable dt = ens.ToDataTableField("dt");
		dt.TableName = "WindowTemplates";

		return Json.ToJson(dt);
	}


	public final String Home_DoMove()
	{
		String[] mypks = this.getMyPK().split("[,]", -1);
		for (int i = 0; i < mypks.length; i++)
		{
			String str = mypks[i];
			if (str == null || Objects.equals(str, ""))
			{
				continue;
			}

			String sql = "UPDATE GPM_WindowTemplate SET Idx=" + i + " WHERE No='" + str + "' AND PageID='" + this.getPageID() + "' ";
			DBAccess.RunSQL(sql);
		}
		return "移动成功..";
	}
	/** 
	 构造函数
	*/
	public WF_Portal()
	{
	}
	public final String CheckEncryptEnable()
	{
		if (SystemConfig.getIsEnablePasswordEncryption() == true)
		{
			return "1";
		}
		return "0";
	}
	/** 
	 系统信息
	 
	 @return 
	*/
	public final String Login_InitInfo()
	{
		Hashtable ht = new Hashtable();
		ht.put("SysNo", SystemConfig.getSysNo());
		ht.put("SysName", SystemConfig.getSysName());
		ht.put("OSModel", SystemConfig.getCCBPMRunModel().getValue());

		// 0=内网模式, 1=运营模式.
		ht.put("SaaSModel", SystemConfig.GetValByKey("SaaSModel", "0"));

		return Json.ToJson(ht);
	}
	/** 
	 初始化登录界面.
	 
	 @return 
	*/
	public final String Login_Init() throws Exception {
		/*DTS.GenerSKeyWords gsw = new DTS.GenerSKeyWords();
		gsw.Do();*/
		//判断是否已经安装数据库，是否需要更新
		if (CheckIsDBInstall() == true)
		{
			return "url@/WF/Admin/DBInstall.htm";
		}


			///#region 如果是saas模式.
		if (SystemConfig.getCCBPMRunModel() == CCBPMRunModel.SAAS)
		{
			if (DataType.IsNullOrEmpty(this.GetRequestVal("OrgNo")) == true)
			{
				return "url@/Portal/SaaS/SelectOneOrg.htm";
			}
			else
			{
				return "url@/Portal/SaaS/Login.htm?OrgNo=" + this.getOrgNo();
			}
		}

			///#endregion 如果是saas模式.


		String doType = GetRequestVal("LoginType");
		if (DataType.IsNullOrEmpty(doType) == false && doType.equals("Out") == true)
		{
			//清空cookie
			WebUser.Exit();
			return "成功退出.";
		}

		//是否需要自动登录。 这里都把cookeis的数据获取来了.
		String userNo = this.GetRequestVal("UserNo");
		String sid = this.GetRequestVal("Token");

		if (DataType.IsNullOrEmpty(sid) == false && DataType.IsNullOrEmpty(userNo) == false)
		{
			//调用登录方法.
			Dev2Interface.Port_Login(this.getUserNo(), this.getSID());
			return "url@Apps.htm?UserNo=" + this.getUserNo() + "&Token=" + getSID();

		}

		Hashtable ht = new Hashtable();
		ht.put("SysName", SystemConfig.getSysName());
		ht.put("SysNo", SystemConfig.getSysNo());
		ht.put("ServiceTel", SystemConfig.getServiceTel());
		ht.put("CustomerName", SystemConfig.getCustomerName());
		if (WebUser.getNoOfRel() == null)
		{
			ht.put("UserNo", "");
			ht.put("UserName", "");
		}
		else
		{
			ht.put("UserNo", WebUser.getNo());

			String name = WebUser.getName();

			if (DataType.IsNullOrEmpty(name) == true)
			{
				ht.put("UserName", WebUser.getNo());
			}
			else
			{
				ht.put("UserName", name);
			}
		}

		return Json.ToJsonEntityModel(ht);
	}
	public final String Login_VerifyState() throws Exception {
		Cookie cookie = ContextHolderUtils.getCookie(this.getClass().getName() + "_Login_Error");
		if(cookie == null)
			return "无需验证";
		if (DataType.IsNullOrEmpty(cookie.getValue()) == false)
		{
			return "err@" + Login_VerifyCode();
		}

		return "无需验证";
	}

	public final String Login_VerifyCode() throws Exception {
		String userNo = this.GetRequestVal("TB_No");

		return Verify.DrawImage(5, this.toString(), "Login_Error", "VerifyCode", userNo);
	}
	private boolean IsCheckCode = true;

	// 记录失败次数
	private static Hashtable failRecord = new Hashtable();
	// 记录锁定用户
	private static Hashtable lockTable = new Hashtable();

	private long getTimeStamp()
	{
		long ts = new Date().getTime() - new Date("1970-01-00 00:00:00").getTime();
		return ts;
	}

	// 判断用户是否被锁定
	private boolean isBeenLock(String userNo)
	{
		// 如果包含此用户，判断是否到锁定结束时间。
		if (lockTable.containsKey(userNo))
		{
			// 如果正被锁定
			if (this.getTimeStamp() < (Long)lockTable.get(userNo))
			{
				return true;
			}
			else
			{
				// 超时解锁用户
				lockTable.remove(userNo);
				failRecord.remove(userNo);
				return false;
			}
		}
		return false;
	}

	private void handleLoginFail(String userNo)
	{
		// 没有记录则新增
		if (!failRecord.containsKey(userNo))
		{
			int failCount = 1;
			failRecord.put(userNo, failCount);

		}
		else
		{
			try
			{
				int failCount = (Integer)failRecord.get(userNo);
				failCount++;
				failRecord.put(userNo, failCount);
				if (failCount >= 3 && !lockTable.containsKey(userNo))
				{
					lockTable.put(userNo, this.getTimeStamp() + Long.parseLong(SystemConfig.getUserLockTimeSeconds()));
				}
			}
			catch (java.lang.Exception e)
			{
				failRecord.put(userNo, 1);
			}
		}
	}

	public final String Login_Submit() throws Exception {
		try
		{
			String gotoSystem = this.GetRequestVal("DDL_System");
			if (DataType.IsNullOrEmpty(gotoSystem) == true)
			{
				gotoSystem = "";
			}

			//@hongyan. 是不是中间件.
			String val = this.GetRequestVal("IsZZJ");
			if (DataType.IsNullOrEmpty(val) == true)
			{
				val = "0";
			}
			if (val.equals("1") == true)
			{
				gotoSystem = "CCFlow";
			}

			String userNo = this.GetRequestVal("TB_No");
			if (this.isBeenLock(userNo))
			{
				return "err@账号已被锁定";
			}
			if (userNo == null)
			{
				userNo = this.GetRequestVal("TB_UserNo");
			}

			userNo = userNo.trim();


				///#region 先校验用户名也密码.

			String pass = this.GetRequestVal("TB_PW");
			if (pass == null)
			{
				pass = this.GetRequestVal("TB_Pass");
			}

			pass = pass.trim();
			Emp emp = new Emp();
			emp.setUserID(userNo);
			//是否存在用户
			boolean isExist = emp.RetrieveFromDBSources() == 0 ? false : true;
			if (isExist == false && DBAccess.IsExitsTableCol("Port_Emp", "NikeName") == true)
			{
				/*如果包含昵称列,就检查昵称是否存在.*/
				Paras ps = new Paras();
				ps.SQL = "SELECT No FROM Port_Emp WHERE NikeName=" + SystemConfig.getAppCenterDBVarStr() + "NikeName";
				ps.Add("NikeName", userNo, false);
				String no = DBAccess.RunSQLReturnStringIsNull(ps, null);
				if (DataType.IsNullOrEmpty(no) == false)
				{
					emp.setNo(no);
					if (emp.RetrieveFromDBSources() != 0)
					{
						isExist = true;
					}
				}
			}
			if (isExist == false && DBAccess.IsExitsTableCol("Port_Emp", "Tel") == true)
			{
				/*如果包含Name列,就检查Name是否存在.*/
				Paras ps = new Paras();
				ps.SQL = "SELECT No FROM Port_Emp WHERE Tel=" + SystemConfig.getAppCenterDBVarStr() + "Tel";
				ps.Add("Tel", userNo, false);
				String no = DBAccess.RunSQLReturnStringIsNull(ps, null);
				if (DataType.IsNullOrEmpty(no) == false)
				{
					emp.setNo(no);
					if (emp.RetrieveFromDBSources() != 0)
					{
						isExist = true;
					}
				}
			}
			if (isExist == false && DBAccess.IsExitsTableCol("Port_Emp", "Email") == true)
			{
				/*如果包含Name列,就检查Name是否存在.*/
				Paras ps = new Paras();
				ps.SQL = "SELECT No FROM Port_Emp WHERE Email=" + SystemConfig.getAppCenterDBVarStr() + "Email";
				ps.Add("Email", userNo, false);
				String no = DBAccess.RunSQLReturnStringIsNull(ps, null);
				if (DataType.IsNullOrEmpty(no) == false)
				{
					emp.setNo(no);
					if (emp.RetrieveFromDBSources() != 0)
					{
						isExist = true;
					}
				}
			}
			if (isExist == false)
			{
				this.handleLoginFail(userNo);
				return "err@用户名不存在.";
			}


				///#region 校验验证码.
			//WFEmp wfEmp = new WFEmp();
			//wfEmp.setNo(emp.getUserID());
			//if (wfEmp.RetrieveFromDBSources() == 0)
			//{
			//    wfEmp.setName(emp.getName();
			//    wfEmp.setDeptNo(emp.getDeptNo();
			//    wfEmp.Insert();
			//}
			//String code = wfEmp.GetParaString("VerifyCode");

			//if (DataType.IsNullOrEmpty(code) == false)
			//{
			//    String strMd5 = this.GetRequestVal("VerifyCode");
			//    if (DataType.IsNullOrEmpty(strMd5)==true)
			//        strMd5 = "";
			//    else
			//        strMd5 = Convert.ToBase64String(MD5.Create().ComputeHash(Encoding.UTF8.GetBytes(strMd5))).replace("+", "%2B");

			//    if (code.equals(strMd5) == false)
			//        return "err@验证码错误.";

			//    //清空验证信息
			//    wfEmp.SetPara("VerifyCode", "");
			//    wfEmp.Update();

			//    //var ccsCks = HttpContext.Current.Request.Cookies["CCS");
			//    //if (ccsCks != null)
			//    //{
			//    //    ccsCks.Expires = DateTime.Today.AddDays(-1);
			//    //    HttpContextHelper.Response.Cookies.Add(ccsCks);
			//    //    HttpContextHelper.Request.Cookies.Remove("CCS");
			//    //}

			//}


				///#endregion 校验验证码.

			if (emp.CheckPass(pass) == false)
			{
				this.handleLoginFail(userNo);
				return "err@用户名或者密码错误.";
			}



				///#endregion 先校验用户名也密码.

			if (DataType.IsNullOrEmpty(userNo) == false && userNo.equals("admin"))
			{
				try
				{
					// 执行升级
					Glo.UpdataCCFlowVer();
				}
				catch (RuntimeException ex)
				{
					Glo.UpdataCCFlowVer();
					String msg = "err@升级失败(ccbpm有自动修复功能,您可以刷新一下系统会自动创建字段,刷新多次扔解决不了问题,请反馈给我们)";
					msg += "@系统信息:" + ex.getMessage();
					return msg;
				}
			}
			String token = "";
			if (bp.wf.Glo.getCCBPMRunModel() == CCBPMRunModel.Single)
			{
				Dev2Interface.Port_Login(emp.getUserID());
				//调用登录方法.
				if (DBAccess.IsExitsTableCol("Port_Emp", "EmpSta") == true)
				{
					String sql = "SELECT EmpSta FROM Port_Emp WHERE No='" + emp.getNo() + "'";
					if (DBAccess.RunSQLReturnValInt(sql, 0) == 1)
					{
						return "err@该用户已经被禁用.";
					}
				}
				token = Dev2Interface.Port_GenerToken("PC");
				//HttpContextHelper.RedisUtils.SetInHash(SystemConfig.RedisCacheKey("WebUser"), "WebUser_" + token, token);

				if (gotoSystem.equals("CCFlow") == true)
				{
					return "url@/WF/AppClassic/Home.htm?Token=" + token + "&UserNo=" + emp.getUserID();
				}
				else
				{
					return "url@Default.htm?Token=" + token + "&UserNo=" + emp.getUserID();
				}
			}

			//设置他的组织，信息.
			WebUser.setNo(emp.getUserID()); //登录帐号.
			WebUser.setDeptNo(emp.getDeptNo());
			WebUser.setDeptName(emp.getDeptText());

			//执行登录.
			Dev2Interface.Port_Login(emp.getUserID(), emp.getOrgNo());
			token = Dev2Interface.Port_GenerToken("PC");
			//HttpContextHelper.RedisUtils.Set("WebUser_" + token, token);

			return "url@Default.htm?Token=" + token + "&UserNo=" + emp.getUserID() + "&OrgNo=" + emp.getOrgNo();
			//return "url@SelectOneOrg.htm?Token=" + token + "&UserNo=" + emp.getUserID() + "&OrgNo=" + emp.getOrgNo();
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
	}
	/** 
	 登录.
	 
	 @return 
	*/
	public final String Login_SubmitBak() throws Exception {
		try
		{
			String userNo = this.GetRequestVal("TB_No");
			if (userNo == null)
			{
				userNo = this.GetRequestVal("TB_UserNo");
			}

			userNo = userNo.trim();

			//if (IsCheckCode == true)
			//{
			//    String verifyCode = this.GetRequestVal("VerifyCode");
			//    String atParaStr = DBAccess.RunSQLReturnString("select AtPara from wf_emp where no='" + userNo + "'");

			//    AtPara atPara = new AtPara(atParaStr);

			//    String checkVerifyCode = atPara.GetValStrByKey(this.ToString() + "_VerifyCode");// HttpUtility.UrlDecode(HttpContextHelper.RequestCookieGet(this.ToString() + "_VerifyCode", "CCS"));
			//    String strMd5 = string.IsNullOrEmpty(verifyCode) ? "" : Convert.ToBase64String(MD5.Create().ComputeHash(Encoding.UTF8.GetBytes(verifyCode)));

			//    //String login_Error = atPara.GetValStrByKey(this.ToString() + "_Login_Error"); //HttpContextHelper.RequestCookieGet(this.ToString() + "_Login_Error", "CCS");

			//    //if (string.IsNullOrEmpty(login_Error) == true && string.IsNullOrEmpty(verifyCode) == false)
			//    //    return "err@错误的验证状态.";

			//    if (string.IsNullOrEmpty(checkVerifyCode) == false && checkVerifyCode != strMd5)
			//        return "err@验证码错误.";

			//    var ccsCks = HttpContext.Current.Request.Cookies["CCS");
			//    if (ccsCks != null)
			//    {
			//        ccsCks.Expires = DateTime.Today.AddDays(-1);
			//        HttpContextHelper.Response.Cookies.Add(ccsCks);
			//        HttpContextHelper.Request.Cookies.Remove("CCS");
			//    }
			//}



			String pass = this.GetRequestVal("TB_PW");
			if (pass == null)
			{
				pass = this.GetRequestVal("TB_Pass");
			}

			pass = pass.trim();
			//pass = HttpUtility.UrlDecode(pass,Encoding.UTF8);

			Emp emp = new Emp();
			emp.setUserID(userNo);
			if (emp.RetrieveFromDBSources() == 0)
			{
				if (DBAccess.IsExitsTableCol("Port_Emp", "NikeName") == true)
				{
					/*如果包含昵称列,就检查昵称是否存在.*/
					Paras ps = new Paras();
					ps.SQL = "SELECT No FROM Port_Emp WHERE NikeName=" + SystemConfig.getAppCenterDBVarStr() + "NikeName";
					ps.Add("NikeName", userNo, false);
					String no = DBAccess.RunSQLReturnStringIsNull(ps, null);
					if (no == null)
					{
						return "err@用户名或者密码错误.";
						//HttpContextHelper.AddCookie("CCS", this.ToString() + "_Login_Error", this.ToString() + "_Login_Error");
					}

					emp.setNo(no);
					int i = emp.RetrieveFromDBSources();
					if (i == 0)
					{
						//HttpContextHelper.AddCookie("CCS", this.ToString() + "_Login_Error", this.ToString() + "_Login_Error");
						return "err@用户名或者密码错误.";
					}
				}

				if (DBAccess.IsExitsTableCol("Port_Emp", "Tel") == true)
				{
					/*如果包含Name列,就检查Name是否存在.*/
					Paras ps = new Paras();
					ps.SQL = "SELECT No FROM Port_Emp WHERE Tel=" + SystemConfig.getAppCenterDBVarStr() + "Tel";
					ps.Add("Tel", userNo, false);
					String no = DBAccess.RunSQLReturnStringIsNull(ps, null);
					if (no == null)
					{
						//HttpContextHelper.AddCookie("CCS", this.ToString() + "_Login_Error", this.ToString() + "_Login_Error");
						return "err@用户名或者密码错误.";
					}

					emp.setNo(no);
					int i = emp.RetrieveFromDBSources();
					if (i == 0)
					{
						//HttpContextHelper.AddCookie("CCS", this.ToString() + "_Login_Error", this.ToString() + "_Login_Error");
						return "err@用户名或者密码错误.";
					}
				}
				else
				{
					//HttpContextHelper.AddCookie("CCS", this.ToString() + "_Login_Error", this.ToString() + "_Login_Error");
					return "err@用户名或者密码错误.";
				}
			}

			if (emp.CheckPass(pass) == false)
			{
				//HttpContextHelper.AddCookie("CCS", this.ToString() + "_Login_Error", this.ToString() + "_Login_Error");
				return "err@用户名或者密码错误.";
			}

			//清空登录错误的信息
			String str = DBAccess.RunSQLReturnString("select AtPara from wf_emp where no='" + userNo + "'");

			AtPara ap = new AtPara(str);
			ap.SetVal(this.toString() + "_VerifyCode", "");
			//ap.SetVal(this.ToString() + "_Login_Error", "");
			DBAccess.RunSQL("update wf_emp set atPara='" + ap.GenerAtParaStrs() + "' where no='" + userNo + "'");



			if (DataType.IsNullOrEmpty(userNo) == false && userNo.equals("admin"))
			{
				try
				{
					// 执行升级
					Glo.UpdataCCFlowVer();
				}
				catch (RuntimeException ex)
				{
					Glo.UpdataCCFlowVer();
					String msg = "err@升级失败(ccbpm有自动修复功能,您可以刷新一下系统会自动创建字段,刷新多次扔解决不了问题,请反馈给我们)";
					msg += "@系统信息:" + ex.getMessage();
					return msg;
				}
			}

			if (bp.wf.Glo.getCCBPMRunModel() == CCBPMRunModel.Single)
			{
				Dev2Interface.Port_Login(emp.getUserID());
				//调用登录方法.
				if (DBAccess.IsExitsTableCol("Port_Emp", "EmpSta") == true)
				{
					String sql = "SELECT EmpSta FROM Port_Emp WHERE No='" + emp.getNo() + "'";
					if (DBAccess.RunSQLReturnValInt(sql, 1) == 1)
					{
						return "err@该用户已经被禁用.";
					}
				}
				return "url@Default.htm?Token=" + Dev2Interface.Port_GenerToken("PC") + "&UserNo=" + emp.getUserID();
			}

			//获得当前管理员管理的组织数量.
			OrgAdminers adminers = null;

			//查询他管理多少组织.
			adminers = new OrgAdminers();
			adminers.Retrieve(OrgAdminerAttr.FK_Emp, emp.getUserID(), null);
			if (adminers.size() == 0)
			{
				Orgs orgs = new Orgs();
				int i = orgs.Retrieve("Adminer", this.GetRequestVal("TB_No"), null);
				if (i == 0)
				{
					//调用登录方法.
					Dev2Interface.Port_Login(emp.getUserID(), emp.getOrgNo());
					return "url@Default.htm?Token=" + Dev2Interface.Port_GenerToken("PC") + "&UserNo=" + emp.getUserID() + "&OrgNo=" + emp.getOrgNo();
				}

				for (Org org : orgs.ToJavaList())
				{
					OrgAdminer oa = new OrgAdminer();
					oa.setEmpNo(WebUser.getNo());
					oa.setOrgNo(org.getNo());
					oa.Save();
				}
				adminers.Retrieve(OrgAdminerAttr.FK_Emp, emp.getUserID(), null);
			}

			//设置他的组织，信息.
			WebUser.setNo(emp.getUserID()); //登录帐号.
			WebUser.setDeptNo(emp.getDeptNo());
			WebUser.setDeptName(emp.getDeptText());

			//执行登录.
			Dev2Interface.Port_Login(emp.getUserID(), emp.getOrgNo());

			String token = Dev2Interface.Port_GenerToken("PC");

			//判断是否是多个组织的情况.
			if (adminers.size() == 1)
			{
				return "url@Default.htm?Token=" + token + "&UserNo=" + emp.getUserID() + "&OrgNo=" + emp.getOrgNo();
			}

			//return "url@Default.htm?Token=" + token + "&UserNo=" + emp.getUserID() + "&OrgNo=" + emp.getOrgNo();


			return "url@SelectOneOrg.htm?Token=" + token + "&UserNo=" + emp.getUserID() + "&OrgNo=" + emp.getOrgNo();
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
	}
	public final String Login_SubmitVue3() throws Exception {
		this.IsCheckCode = false;
		return Login_Submit();
	}
	private boolean CheckIsDBInstall()
	{
		//检查数据库连接.
		try
		{
			DBAccess.TestIsConnection();
		}
		catch (RuntimeException ex)
		{
			throw new RuntimeException("err@异常信息:" + ex.getMessage());
		}

		//检查是否缺少Port_Emp 表，如果没有就是没有安装.
		if (DBAccess.IsExitsObject("Port_Emp") == false && DBAccess.IsExitsObject("WF_Flow") == false)
		{
			return true;
		}

		//如果没有流程表，就执行安装.
		if (DBAccess.IsExitsObject("WF_Flow") == false)
		{
			return true;
		}
		return false;
	}


		///#region Frm.htm 表单.
	/** 
	 表单树.
	 
	 @return 
	*/
	public final String Frms_InitSort() throws Exception {

		//获得数量.
		String sqlWhere = "";
		String sql = "";
		//集团模式且一个部门下维护一套角色体系
		if (SystemConfig.getCCBPMRunModel() == CCBPMRunModel.GroupInc)
		{
			//如果当前管理员登录的部门是主部门
			Paras ps = new Paras();
			Emp emp = new Emp(WebUser.getNo());
			DataTable dt = null;
			if (!emp.getDeptNo().equals(WebUser.getDeptNo()))
			{
				sql = "SELECT No,Name,ParentNo From Sys_FormTree WHERE No='" + WebUser.getDeptNo() + "' Order By Idx";
				dt = DBAccess.RunSQLReturnTable(sql);
				if (dt.Rows.size() == 0)
				{
					//根据这个部门编号生成一个流程类别
					SysFormTree formTree = new SysFormTree();
					formTree.setNo(WebUser.getDeptNo());
					formTree.setParentNo(WebUser.getOrgNo());
					formTree.setName(WebUser.getDeptName());
					formTree.setOrgNo(WebUser.getOrgNo());
					formTree.DirectInsert();
				}
			}
			sql = "SELECT No,Name,ParentNo From Sys_FormTree WHERE OrgNo='" + WebUser.getOrgNo() + "'  Order By Idx ";
			//if (WebUser.getNo().equals("admin") == true)
			//   sql = "SELECT No,Name,ParentNo From Sys_FormTree Order By Idx";
			ps.SQL = sql;
			dt = DBAccess.RunSQLReturnTable(ps);
			return Json.ToJson(dt);
		}
		if (SystemConfig.getCCBPMRunModel() != CCBPMRunModel.Single)
		{
			sqlWhere = "   OrgNo='" + WebUser.getOrgNo() + "' AND No!='" + WebUser.getOrgNo() + "'";
			if (SystemConfig.getCCBPMRunModel() == CCBPMRunModel.GroupInc && SystemConfig.getGroupStationModel() == 2)
			{
				bp.wf.port.admingroup.Org org = new bp.wf.port.admingroup.Org(WebUser.getOrgNo());
				if (!WebUser.getNo().equals(org.getAdminer()))
				{
					sqlWhere += " AND No IN(SELECT FrmTreeNo From Port_OrgAdminerFrmTree Where OrgNo='" + WebUser.getOrgNo() + "' AND FK_Emp='" + WebUser.getNo() + "')";
				}
			}
		}
		else
		{
			sqlWhere = "   No!='100' ";
		}


		//求内容.
		sql = "SELECT No as \"No\",Name as \"Name\" FROM Sys_FormTree WHERE  " + sqlWhere + " ORDER BY Idx ";
		if (SystemConfig.getCCBPMRunModel() == CCBPMRunModel.Single)
		{
			GloVar gloVar = new GloVar();
			gloVar.setNo(WebUser.getDeptNo() + "_" + WebUser.getNo() + "_Adminer");
			if (gloVar.RetrieveFromDBSources() != 0)
			{
				sql = "SELECT No as \"No\",Name as \"Name\" FROM Sys_FormTree WHERE  No='" + WebUser.getDeptNo() + "' OR ParentNo='" + WebUser.getDeptNo() + "' ORDER BY Idx ";

			}
		}
		DataTable dtSort = DBAccess.RunSQLReturnTable(sql);
		if (SystemConfig.getAppCenterDBFieldCaseModel() != FieldCaseModel.None)
		{
			dtSort.Columns.get(0).ColumnName = "No";
			dtSort.Columns.get(1).ColumnName = "Name";
			//dtSort.Columns.get(2).ColumnName = "WFSta2";
			//dtSort.Columns.get(3).ColumnName = "WFSta3";
			//dtSort.Columns.get(4).ColumnName = "WFSta5";
		}
		return Json.ToJson(dtSort);
	}
	/** 
	 表单
	 
	 @return 
	*/
	public final String Frms_Init() throws Exception {
		//获得流程实例的数量.
		String sqlWhere = "";
		String sql = "";
		if (SystemConfig.getCCBPMRunModel() != CCBPMRunModel.Single)
		{
			sqlWhere = " AND OrgNo='" + WebUser.getOrgNo() + "'";
		}


		//求流程内容.
		sql = "SELECT No as \"No\",Name as \"Name\",FrmType,FK_FormTree,PTable,DBSrc,Icon,EntityType,Ver FROM Sys_MapData WHERE 1=1 " + sqlWhere + " ORDER BY Idx ";
		DataTable dtFlow = null;
		try
		{
			dtFlow = DBAccess.RunSQLReturnTable(sql);
		}
		catch (RuntimeException ex)
		{
			MapData md = new MapData();
			md.CheckPhysicsTable();
			dtFlow = DBAccess.RunSQLReturnTable(sql);
		}

		if (SystemConfig.getAppCenterDBFieldCaseModel() != FieldCaseModel.None)
		{
			dtFlow.Columns.get(0).ColumnName = "No";
			dtFlow.Columns.get(1).ColumnName = "Name";
			dtFlow.Columns.get(2).ColumnName = "FrmType";
			dtFlow.Columns.get(3).ColumnName = "FK_FormTree";
			dtFlow.Columns.get(4).ColumnName = "PTable";
			dtFlow.Columns.get(5).ColumnName = "DBSrc";
			dtFlow.Columns.get(6).ColumnName = "Icon";
			dtFlow.Columns.get(7).ColumnName = "EntityType";
			dtFlow.Columns.get(8).ColumnName = "Ver";
			//dtFlow.Columns.get(2).ColumnName = "WorkModel";
			//dtFlow.Columns.get(3).ColumnName = "AtPara";
			//dtFlow.Columns.get(4).ColumnName = "FK_FlowSort";
			//dtFlow.Columns.get(5).ColumnName = "WFSta2";
			//dtFlow.Columns.get(6).ColumnName = "WFSta3";
			//dtFlow.Columns.get(7).ColumnName = "WFSta5";
		}
		return Json.ToJson(dtFlow);
	}
	/** 
	 流程移动.
	 
	 @return 
	*/
	public final String Frms_Move()
	{
		String sortNo = this.GetRequestVal("SortNo");
		String[] flowNos = this.GetRequestVal("EnNos").split("[,]", -1);
		for (int i = 0; i < flowNos.length; i++)
		{
			String flowNo = flowNos[i];

			String sql = "UPDATE Sys_MapData SET FK_FormTree ='" + sortNo + "',Idx=" + i + " WHERE No='" + flowNo + "'";
			DBAccess.RunSQL(sql);
		}
		return "表单顺序移动成功..";
	}
	public final String Frms_MoveSort() throws Exception {
		String[] ens = this.GetRequestVal("SortNos").split("[,]", -1);

		SysFormTree ft = new SysFormTree();

		String table = ft.getEnMap().getPhysicsTable();

		for (int i = 0; i < ens.length; i++)
		{
			String en = ens[i];

			String sql = "UPDATE " + table + " SET Idx=" + i + " WHERE No='" + en + "'";
			DBAccess.RunSQL(sql);
		}
		return "目录移动成功..";
	}


		///#endregion Frm.htm 表单.


		///#region 流程树.
	/** 
	 初始化
	 
	 @return 
	*/
	public final String FlowTree_InitSort()
	{
		//   if (SystemConfig.getCCBPMRunModel()==)
		return "";
	}

		///#endregion 流程树.



		///#region Flows.htm 流程.
	public final String Flows_Init()
	{
		//获得流程实例的数量.
		String sqlWhere = "";
		String sql = "";
		if (SystemConfig.getCCBPMRunModel() != CCBPMRunModel.Single)
		{
			sqlWhere = " AND OrgNo='" + WebUser.getOrgNo() + "'";
		}

		//求流程数量.
		sql = "SELECT FK_Flow,WFState, COUNT(*) AS Num FROM WF_GenerWorkFlow WHERE 1=1 " + sqlWhere + " GROUP BY FK_Flow, WFState ";
		DataTable dt = DBAccess.RunSQLReturnTable(sql);

		//求流程内容.
		sql = "SELECT No as \"No\",Name as \"Name\",WorkModel, FK_FlowSort, 0 as WFSta2, 0 as WFSta3, 0 as WFSta5, Ver FROM WF_Flow WHERE 1=1 " + sqlWhere + " ORDER BY Idx ";
		DataTable dtFlow = DBAccess.RunSQLReturnTable(sql);
		if (SystemConfig.getAppCenterDBFieldCaseModel() != FieldCaseModel.None)
		{
			dtFlow.Columns.get(0).ColumnName = "No";
			dtFlow.Columns.get(1).ColumnName = "Name";
			dtFlow.Columns.get(2).ColumnName = "WorkModel";
			//dtFlow.Columns.get(3).ColumnName = "AtPara";
			dtFlow.Columns.get(3).ColumnName = "FK_FlowSort";
			dtFlow.Columns.get(4).ColumnName = "WFSta2";
			dtFlow.Columns.get(5).ColumnName = "WFSta3";
			dtFlow.Columns.get(6).ColumnName = "WFSta5";
			dtFlow.Columns.get(7).ColumnName = "Ver";
		}

		// 给状态赋值.
		for (DataRow dr : dtFlow.Rows)
		{
			String flowNo = dr.getValue(0) instanceof String ? (String)dr.getValue(0) : null;
			for (DataRow mydr : dt.Rows)
			{
				String fk_flow = mydr.getValue(0).toString();
				if (fk_flow.equals(flowNo) == false)
				{
					continue;
				}

				int wfstate = Integer.parseInt(mydr.getValue(1).toString());
				int Num = Integer.parseInt(mydr.getValue(2).toString());
				if (wfstate == 2)
				{
					dr.setValue("WFSta2", Num);
				}
				if (wfstate == 3)
				{
					dr.setValue("WFSta3", Num);
				}
				if (wfstate == 5)
				{
					dr.setValue("WFSta5", Num);
				}
				break;
			}
		}
		return Json.ToJson(dtFlow);
	}
	/** 
	 流程移动.
	 
	 @return 
	*/
	public final String Flows_Move()
	{
		String sourceSortNo = this.GetRequestVal("SourceSortNo");
		String sourceFlowNos = this.GetRequestVal("SourceFlowNos");
		String toSortNo = this.GetRequestVal("ToSortNo");
		String toFlowNos = this.GetRequestVal("ToFlowNos");
		String[] flowNos = sourceFlowNos.split("[,]", -1);
		for (int i = 0; i < flowNos.length; i++)
		{
			String flowNo = flowNos[i];

			String sql = "UPDATE WF_Flow SET FK_FlowSort ='" + sourceSortNo + "',Idx=" + i + " WHERE No='" + flowNo + "'";
			DBAccess.RunSQL(sql);
		}
		//如果是在同一个流程类别中拖动流程顺序
		if (sourceSortNo.equals(toSortNo) == true)
		{
			return "流程顺序移动成功..";
		}
		flowNos = toFlowNos.split("[,]", -1);
		for (int i = 0; i < flowNos.length; i++)
		{
			String flowNo = flowNos[i];

			String sql = "UPDATE WF_Flow SET FK_FlowSort ='" + toSortNo + "',Idx=" + i + " WHERE No='" + flowNo + "'";
			DBAccess.RunSQL(sql);
		}
		return "流程顺序移动成功..";
	}
	public final String Flows_MoveSort()
	{
		String[] ens = this.GetRequestVal("SortNos").split("[,]", -1);
		for (int i = 0; i < ens.length; i++)
		{
			String en = ens[i];

			String sql = "UPDATE WF_FlowSort SET Idx=" + i + " WHERE No='" + en + "'";
			DBAccess.RunSQL(sql);
		}
		return "目录移动成功..";
	}

		///#endregion 流程.


		///#region 消息.

	/** 
	 消息初始化
	 
	 @return 
	*/
	public final String Message_Init() throws Exception {
		//获得消息分组.
		String sql = "SELECT MsgType, Count(*) as Num FROM Sys_SMS WHERE SendTo='" + WebUser.getNo() + "'  GROUP BY MsgType";
		DataTable groups = DBAccess.RunSQLReturnTable(sql);
		groups.TableName = "Groups";
		groups.Columns.Add("TypeName");
		//foreach (DataRow dr in groups.Rows)
		//{
		//    dr["TypeName"] = dr["MsgType");
		//}

		//获得消息.
		sql = "SELECT MyPK, EmailTitle,EmailDoc,EmailSta, RDT,Sender, AtPara,MsgType,IsRead FROM Sys_SMS WHERE SendTo='" + WebUser.getNo() + "' ORDER BY IsRead ";
		DataTable infos = DBAccess.RunSQLReturnTable(sql);
		infos.TableName = "Messages";

		DataSet ds = new DataSet();
		ds.Tables.add(groups);
		ds.Tables.add(infos);

		//返回信息.
		return Json.ToJson(ds);
	}

		///#endregion 消息.

		///#region 通知公告.

	/** 
	 消息初始化
	 
	 @return 
	*/
	public final String Info_Init() throws Exception {

		//获得消息.
		String sql = "SELECT No, Name,Docs,InfoPRI, InfoSta,RecName, RelerName,RelDeptName,RDT FROM OA_Info WHERE InfoSta=0 ORDER BY RDT ";
		DataTable infos = DBAccess.RunSQLReturnTable(sql);
		infos.TableName = "Infos";

		DataSet ds = new DataSet();
		ds.Tables.add(infos);


		//返回信息.
		return Json.ToJson(ds);
	}

		///#endregion 通知公告



		///#region   加载菜单 .

	/** 
	 获得菜单:权限.
	 
	 @return 
	*/
	public final String Default_InitExt() throws Exception {
		String pkval = WebUser.getNo() + "_Menus";
		if (SystemConfig.getCCBPMRunModel() == CCBPMRunModel.SAAS)
		{
			pkval += "_" + WebUser.getOrgNo();
		}

		//String docs = DBAccess.GetBigTextFromDB("Sys_UserRegedit", "MyPK", pkval, "BigDocs");
		//if (DataType.IsNullOrEmpty(docs) == false)
		//    return docs;

		//系统表.
		MySystems systems = new MySystems();
		systems.RetrieveAll();
		MySystems systemsCopy = new MySystems();

		//模块.
		Modules modules = new Modules();
		modules.RetrieveAll();
		Modules modulesCopy = new Modules();

		//菜单.
		Menus menus = new Menus();
		menus.RetrieveAll();
		Menus menusCopy = new Menus();

		//权限中心.
		PowerCenters pcs = new PowerCenters();
		pcs.RetrieveAll();

		String mydepts = "" + WebUser.getDeptNo() + ","; //我的部门.
		String mystas = ""; //我的角色.

		DataTable mydeptsDT = DBAccess.RunSQLReturnTable("SELECT FK_Dept,FK_Station FROM Port_DeptEmpStation WHERE FK_Emp='" + WebUser.getUserID() + "'");
		for (DataRow dr : mydeptsDT.Rows)
		{
			mydepts += dr.getValue(0).toString() + ",";
			mystas += dr.getValue(1).toString() + ",";
		}


			///#region 1.0 首先解决系统权限问题.
		//首先解决系统的权限.
		String ids = "";
		for (MySystem item : systems.ToJavaList())
		{
			//如果被禁用了.
			if (item.getItIsEnable() == false)
			{
				continue;
			}

			//找到关于系统的控制权限集合.
			bp.en.Entities tempVar = pcs.GetEntitiesByKey(PowerCenterAttr.CtrlPKVal, item.getNo());
			PowerCenters mypcs = tempVar instanceof PowerCenters ? (PowerCenters)tempVar : null;
			//如果没有权限控制的描述，就默认有权限.
			if (mypcs == null)
			{
				systemsCopy.AddEntity(item);
				continue;
			}

			//控制遍历权限.
			for (PowerCenter pc : mypcs.ToJavaList())
			{
				if (pc.getCtrlModel().equals("Anyone") == true)
				{
					systemsCopy.AddEntity(item);
					break;
				}
				if (pc.getCtrlModel().equals("Adminer") == true && WebUser.getNo().equals("admin") == true)
				{
					systemsCopy.AddEntity(item);
					break;
				}

				if (pc.getCtrlModel().equals("AdminerAndAdmin2") == true && WebUser.getIsAdmin() == true)
				{
					systemsCopy.AddEntity(item);
					break;
				}
				ids = "," + pc.getIDs() + ",";
				if (pc.getCtrlModel().equals("Emps") == true && ids.contains("," + WebUser.getNo() + ",") == true)
				{
					systemsCopy.AddEntity(item);
					break;
				}

				//是否包含部门？
				if (pc.getCtrlModel().equals("Depts") == true && DataType.IsHaveIt(pc.getIDs(), mydepts) == true)
				{
					systemsCopy.AddEntity(item);
					break;
				}

				//是否包含角色？
				if (pc.getCtrlModel().equals("Stations") == true && DataType.IsHaveIt(pc.getIDs(), mystas) == true)
				{
					systemsCopy.AddEntity(item);
					break;
				}

				//SQL？
				if (pc.getCtrlModel().equals("SQL") == true)
				{
					String sql = bp.wf.Glo.DealExp(pc.getIDs(), null, "");
					if (DBAccess.RunSQLReturnValFloat(sql) > 0)
					{
						systemsCopy.AddEntity(item);
					}
					break;
				}
			}
		}

			///#endregion 首先解决系统权限问题.


			///#region 2.0 根据求出的系统集合处理权限， 求出模块权限..
		for (MySystem item : systemsCopy.ToJavaList())
		{
			for (Module module_Keyword : modules.ToJavaList())
			{
				//如果被禁用了.
				if (module_Keyword.getItIsEnable() == false)
				{
					continue;
				}


				if (module_Keyword.getSystemNo().equals(item.getNo()) == false)
				{
					continue;
				}

				//找到关于系统的控制权限集合.
				bp.en.Entities tempVar2 = pcs.GetEntitiesByKey(PowerCenterAttr.CtrlPKVal, module_Keyword.getNo());
				PowerCenters mypcs = tempVar2 instanceof PowerCenters ? (PowerCenters)tempVar2 : null;
				//如果没有权限控制的描述，就默认有权限.
				if (mypcs == null)
				{
					modulesCopy.AddEntity(module_Keyword);
					continue;
				}

				//控制遍历权限.
				for (PowerCenter pc : mypcs.ToJavaList())
				{
					if (pc.getCtrlModel().equals("Anyone") == true)
					{
						modulesCopy.AddEntity(module_Keyword);
						break;
					}
					if (pc.getCtrlModel().equals("Adminer") == true && WebUser.getNo().equals("admin") == true)
					{
						modulesCopy.AddEntity(module_Keyword);
						break;
					}

					if (pc.getCtrlModel().equals("AdminerAndAdmin2") == true && WebUser.getIsAdmin() == true)
					{
						modulesCopy.AddEntity(module_Keyword);
						break;
					}

					ids = "," + pc.getIDs() + ",";
					if (pc.getCtrlModel().equals("Emps") == true && ids.contains("," + WebUser.getNo() + ",") == true)
					{
						modulesCopy.AddEntity(module_Keyword);
						break;
					}

					//是否包含部门？
					if (pc.getCtrlModel().equals("Depts") == true && this.IsHaveIt(pc.getIDs(), mydepts) == true)
					{
						modulesCopy.AddEntity(module_Keyword);
						break;
					}

					//是否包含角色？
					if (pc.getCtrlModel().equals("Stations") == true && this.IsHaveIt(pc.getIDs(), mystas) == true)
					{
						modulesCopy.AddEntity(module_Keyword);
						break;
					}
					if (pc.getCtrlModel().equals("SQL") == true)
					{
						String sql = bp.wf.Glo.DealExp(pc.getIDs(), null, "");
						if (DBAccess.RunSQLReturnValFloat(sql) > 0)
						{
							modulesCopy.AddEntity(module_Keyword);
						}
						break;
					}
				}
			}
		}

			///#endregion 2.0 根据求出的系统集合处理权限,求出模块权限.


			///#region 3.0 根据求出的模块集合处理权限， 求出菜单权限..
		for (Module item : modulesCopy.ToJavaList())
		{
			for (Menu menu : menus.ToJavaList())
			{

				//如果被禁用了.
				if (menu.getItIsEnable() == false)
				{
					continue;
				}

				if (menu.getModuleNo().equals(item.getNo()) == false)
				{
					continue;
				}

				//找到关于系统的控制权限集合.
				bp.en.Entities tempVar3 = pcs.GetEntitiesByKey(PowerCenterAttr.CtrlPKVal, menu.getNo());
				PowerCenters mypcs = tempVar3 instanceof PowerCenters ? (PowerCenters)tempVar3 : null;
				//如果没有权限控制的描述，就默认有权限.
				if (mypcs == null)
				{
					menusCopy.AddEntity(menu);
					continue;
				}

				//控制遍历权限.
				for (PowerCenter pc : mypcs.ToJavaList())
				{
					if (pc.getCtrlModel().equals("Anyone") == true)
					{
						menusCopy.AddEntity(menu);
						break;
					}
					if (pc.getCtrlModel().equals("Adminer") == true && WebUser.getNo().equals("admin") == true)
					{
						menusCopy.AddEntity(menu);
						break;
					}

					if (pc.getCtrlModel().equals("AdminerAndAdmin2") == true && WebUser.getIsAdmin() == true)
					{
						menusCopy.AddEntity(menu);
						break;
					}

					ids = "," + pc.getIDs() + ",";
					if (pc.getCtrlModel().equals("Emps") == true && ids.contains("," + WebUser.getNo() + ",") == true)
					{
						menusCopy.AddEntity(menu);
						break;
					}

					//是否包含部门？
					if (pc.getCtrlModel().equals("Depts") == true && this.IsHaveIt(pc.getIDs(), mydepts) == true)
					{
						menusCopy.AddEntity(menu);
						break;
					}

					//是否包含角色？
					if (pc.getCtrlModel().equals("Stations") == true && this.IsHaveIt(pc.getIDs(), mystas) == true)
					{
						menusCopy.AddEntity(menu);
						break;
					}

					//按照SQL语句
					if (pc.getCtrlModel().equals("SQL") == true)
					{
						String sql = pc.getIDs();
						if (DataType.IsNullOrEmpty(sql) == true)
						{
							menusCopy.AddEntity(menu);
							break;
						}
						sql = bp.wf.Glo.DealExp(sql, null);
						if (DBAccess.RunSQLReturnValInt(sql, 0) > 0)
						{
							menusCopy.AddEntity(menu);
							break;
						}
					}
				}
			}
		}

			///#endregion 2.0 根据求出的系统集合处理权限,求出模块权限.


			///#region 组装数据.
		DataSet ds = new DataSet();
		DataTable dtSystem = systemsCopy.ToDataTableField("System");
		dtSystem.Columns.Add("IsOpen");

		//给第1个系统，第1个模块设置打开状态.
		DataTable dtModule = modulesCopy.ToDataTableField("Module");
		dtModule.Columns.Add("IsOpen");
		if (dtSystem.Rows.size() > 0)
		{
			dtSystem.Rows.get(0).setValue("IsOpen","true");
			String systemNo = dtSystem.Rows.get(0).getValue("No").toString();
			for (DataRow dr : dtModule.Rows)
			{
				if (dr.getValue("SystemNo").toString().equals(systemNo) == false)
				{
					continue;
				}

				dr.setValue("IsOpen", "true");
				break;
			}
		}

		ds.Tables.add(dtSystem);
		ds.Tables.add(dtModule);

		DataTable dtMenu = menusCopy.ToDataTableField("Menu");
		dtMenu.Columns.get("UrlExt").ColumnName = "Url";
		ds.Tables.add(dtMenu);

			///#endregion 组装数据.


		String json = Json.ToJson(ds);
		DBAccess.SaveBigTextToDB(json, "Sys_UserRegedit", "MyPK", pkval, "BigDocs");

		try
		{
			DBAccess.RunSQL("UPDATE Sys_UserRegedit SET FK_Emp='" + WebUser.getNo() + "', CfgKey='Menus',OrgNo='" + WebUser.getOrgNo() + "' WHERE MyPK='" + pkval + "'");
		}
		catch (RuntimeException ex)
		{
			UserRegedit ur = new UserRegedit();
			ur.CheckPhysicsTable();
		}

		return json;

	}
	/** 
	 比较两个字符串是否有交集
	 
	 @param ids1
	 @param ids2
	 @return 
	*/
	public final boolean IsHaveIt(String ids1, String ids2)
	{
		if (DataType.IsNullOrEmpty(ids1) == true)
		{
			return false;
		}
		if (DataType.IsNullOrEmpty(ids2) == true)
		{
			return false;
		}

		String[] str1s = ids1.split("[,]", -1);
		String[] str2s = ids2.split("[,]", -1);

		for (String str1 : str1s)
		{
			if (Objects.equals(str1, "") || str1 == null)
			{
				continue;
			}

			for (String str2 : str2s)
			{
				if (Objects.equals(str2, "") || str2 == null)
				{
					continue;
				}

				if (str2.equals(str1) == true)
				{
					return true;
				}
			}
		}
		return false;
	}
	public final String Default_LogOut() throws Exception {

		String orgNo = WebUser.getOrgNo();
		WebUser.Exit();

		if (SystemConfig.getCCBPMRunModel() == CCBPMRunModel.SAAS)
		{
			return "/Portal/SaaS/Login.htm?OrgNo=" + orgNo;
		}

		return "./Login.htm?DoType=Logout&SystemNo=CCFast";
	}
	/** 
	 返回构造的JSON.
	 
	 @return 
	*/
	public final String Default_Init() throws Exception {
		//如果是admin. 
		if (WebUser.getIsAdmin() == true && this.getItIsMobile() == false)
		{
		}
		else
		{
			return Default_InitExt();
		}

		DataSet myds = new DataSet();


			///#region 构造数据容器.
		//系统表.
		MySystems systems = new MySystems();
		systems.RetrieveAll();
		DataTable dtSys = systems.ToDataTableField("System");
		dtSys.Columns.Add("IsOpen");


		//模块.
		Modules modules = new Modules();
		modules.RetrieveAll();
		DataTable dtModule = modules.ToDataTableField("Module");
		dtModule.Columns.Add("IsOpen");

		//菜单.
		Menus menus = new Menus();
		menus.RetrieveAll();
		DataTable dtMenu = menus.ToDataTableField("Menu");
		dtMenu.Columns.get("UrlExt").ColumnName = "Url";

			///#endregion 构造数据容器.


			///#region 把数据加入里面去.
		myds.Tables.add(dtSys);
		myds.Tables.add(dtModule);
		myds.Tables.add(dtMenu);

			///#endregion 把数据加入里面去.


			///#region 如果是admin.
		if (WebUser.getIsAdmin() == true && this.getItIsMobile() == false && SystemConfig.getCCBPMRunModel() != CCBPMRunModel.SAAS)
		{

				///#region 增加默认的系统.
			DataRow dr = dtSys.NewRow();
			dr.setValue("No", "Flows");
			dr.setValue("Name", "流程设计");
			dr.setValue("Icon", "");
			dtSys.Rows.add(dr);

			dr = dtSys.NewRow();
			dr.setValue("No", "Frms");
			dr.setValue("Name", "表单设计");
			dr.setValue("Icon", "");
			dtSys.Rows.add(dr);

			dr = dtSys.NewRow();
			dr.setValue("No", "System");
			dr.setValue("Name", "系统管理");
			dr.setValue("Icon", "");
			dtSys.Rows.add(dr);

				///#endregion 增加默认的系统.

			String sqlWhere = "";
			if (SystemConfig.getCCBPMRunModel() != CCBPMRunModel.Single)
			{
				sqlWhere = " AND OrgNo='" + WebUser.getOrgNo() + "'";
			}

			DataSet dsAdminMenus = new DataSet();
			AdminMenus mymenus = new AdminMenus();
			dsAdminMenus.readXml(mymenus.getFile());


			//增加模块.
			DataTable dtGroup = dsAdminMenus.GetTableByName("Group");
			for (DataRow dtRow : dtGroup.Rows)
			{
				DataRow drModel = dtModule.NewRow();
				drModel.setValue("No", dtRow.get("No"));
				drModel.setValue("Name", dtRow.get("Name"));
				drModel.setValue("SystemNo", "System");
				drModel.setValue("Icon", dtRow.get("Icon"));
				dtModule.Rows.add(drModel);
			}

			//增加菜单.
			DataTable dtItem = dsAdminMenus.GetTableByName("Item");
			for (DataRow dtRow : dtItem.Rows)
			{
				DataRow drMenu = dtMenu.NewRow();
				drMenu.setValue("No", dtRow.get("No"));
				drMenu.setValue("Name", dtRow.get("Name"));
				drMenu.setValue("ModuleNo", dtRow.get("GroupNo"));
				drMenu.setValue("Url", dtRow.get("Url"));
				drMenu.setValue("Icon", dtRow.get("Icon"));
				drMenu.setValue("SystemNo", "System");
				dtMenu.Rows.add(drMenu);
			}
		}

			///#endregion 如果是admin.

		//   myds.WriteXml("c:/11.xml");


			///#region 让第一个系统的第1个模块的默认打开的. @hongyan.
		if (myds.GetTableByName("System").Rows.size() != 0)
		{
			//让第一个打开.
			myds.GetTableByName("System").Rows.get(0).setValue("IsOpen" , "true");
			String systemNo = myds.GetTableByName("System").Rows.get(0).getValue("No").toString();
			for (DataRow dr : myds.GetTableByName("Module").Rows)
			{
				if (dr.getValue("SystemNo").toString().equals(systemNo) == false)
				{
					continue;
				}

				dr.setValue("IsOpen", "true");
				break;
			}
		}
		else
		{
		}

			///#endregion 让第一个系统的第1个模块的第一个菜单打开.


		return Json.ToJson(myds);
	}

		///#endregion   加载菜单.


	/** 
	 生成页面
	 
	 @return 
	*/
	public final String LoginGenerQRCodeMobile_Init()
	{
		String url = SystemConfig.getHostURL() + "/FastMobilePortal/Login.htm";
		return url;
	}
	///#region 按照流程类别批量导出流程模板
	/** 
	 批量导出流程模板
	 
	 @return 
	*/
	public final String Flow_BatchExpFlowTemplate() throws Exception {
		String flowSort = this.GetRequestVal("FK_Sort");
		String flowSortName = this.GetRequestVal("FlowSortName");
		if (DataType.IsNullOrEmpty("flowSort") == true)
		{
			return "err@流程的类别不能为空";
		}
		//根据流程类别获取改类别下的所有流程
		Flows flows = new Flows(flowSort);
		//在临时文件中指定一个目录
		String path = SystemConfig.getPathOfTemp() + flowSortName + "/";
		File file = new File(path);
		if (file.exists() == true)
		{
			bp.tools.FileAccess.deletesFile(file);
		}
		else
		{
			(new File(path)).mkdirs();
		}
		for (Flow flow : flows.ToJavaList())
		{
			flow.DoExpFlowXmlTemplete(path);
		}
		//生成压缩包文件
		String zipFile = SystemConfig.getPathOfTemp() + flowSortName + ".zip";
		try
		{
			while ((new File(zipFile)).isFile() == true)
			{
				(new File(zipFile)).delete();
			}
			//执行压缩.
			ZipCompress fz = new ZipCompress(zipFile, path);
			fz.zip();
			//删除临时文件夹
			bp.tools.FileAccess.deletesFile(file);
		}
		catch (RuntimeException ex)
		{
			return "err@执行压缩出现错误:" + ex.getMessage() + ",路径tempPath:" + path + ",zipFile=" + zipFile;
		}
		if(SystemConfig.isJarRun()==true){
			return "url@DataUser/Temp/" + flowSortName + ".zip";
		}
		return "url@DataUser/Temp/" + flowSortName + ".zip";
	}

		///#endregion 按照流程类别批量导出流程模板


		///#region 按照表单类别批量导出表单模板
	/** 
	 批量导出表单模板
	 
	 @return 
	*/
	public final String Form_BatchExpFrmTemplate() throws Exception {
		String frmTree = this.GetRequestVal("FK_FrmTree");
		String frmTreeName = this.GetRequestVal("FrmTreeName");
		if (DataType.IsNullOrEmpty("frmTree") == true)
			return "err@表单的类别不能为空";
		//根据流程类别获取改类别下的所有流程
		MapDatas mds = new MapDatas();
		mds.Retrieve(MapDataAttr.FK_FormTree, frmTree, MapDataAttr.Idx);
		//在临时文件中指定一个目录
		String path = SystemConfig.getPathOfTemp() + frmTreeName + "/";
		File file = new File(path);
		if (file.exists() == true)
			bp.tools.FileAccess.deletesFile(file);

		file.mkdir();
		for (MapData md : mds.ToJavaList())
		{
			DataSet ds = bp.sys.CCFormAPI.GenerHisDataSet_AllEleInfo(md.getNo());

			String xmlFile = path + md.getName() + ".xml";
			ds.WriteXml(xmlFile,XmlWriteMode.IgnoreSchema,ds);
		}
		//生成压缩包文件
		String zipFile = SystemConfig.getPathOfTemp() + frmTreeName + ".zip";
		File zipFileFile = new File(zipFile);
		try {
			while (zipFileFile.exists() == true) {
				zipFileFile.delete();
			}
			// 执行压缩.
			ZipCompress fz = new ZipCompress(zipFile, path);
			fz.zip();
			// 删除临时文件夹
			bp.tools.FileAccess.deletesFile(file);
		} catch (Exception ex) {
			return "err@执行压缩出现错误:" + ex.getMessage() + ",路径tempPath:" + path + ",zipFile=" + zipFile;
		}
		if(SystemConfig.isJarRun()==true){
			return "url@DataUser/Temp/" + frmTreeName + ".zip";
		}
		return "url@DataUser/Temp/" + frmTreeName + ".zip";
	}

		///#endregion 按照表单类别批量导出表单模板
}
