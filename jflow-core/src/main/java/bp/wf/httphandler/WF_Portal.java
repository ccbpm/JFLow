package bp.wf.httphandler;

import bp.da.*;
import bp.difference.handler.WebContralBase;
import bp.en.EntityTree;
import bp.port.Emp;
import bp.sys.*;
import bp.tools.StringHelper;
import bp.web.*;
import bp.ccfast.ccmenu.*;
import bp.wf.Glo;
import bp.wf.port.admin2group.*;
import bp.tools.*;
import bp.ccfast.portal.*;
import bp.difference.*;
import bp.wf.template.FlowSort;
import bp.wf.xml.*;
import bp.wf.*;
import org.apache.tomcat.jni.Directory;
import org.springframework.boot.system.ApplicationHome;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import java.net.URLDecoder;
import java.util.*;
import java.io.*;
import java.util.concurrent.ConcurrentHashMap;
import bp.tools.AesEncryptUtil;
/** 
 页面功能实体
*/
public class WF_Portal extends WebContralBase
{
	public final String getPageID()  {
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
	public final String Home_DoMove() throws Exception {
		String[] mypks = this.getMyPK().split("[,]", -1);
		for (int i = 0; i < mypks.length; i++)
		{
			String str = mypks[i];
			if (str == null || str.equals(""))
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
	public WF_Portal() throws Exception {
	}
	/** 
	 系统信息
	 
	 @return 
	*/
	public final String Login_InitInfo() throws Exception {
		Hashtable ht = new Hashtable();
		ht.put("SysNo", SystemConfig.getSysNo());
		ht.put("SysName", SystemConfig.getSysName());

		return Json.ToJson(ht);
	}
	/** 
	 初始化登录界面.
	 
	 @return 
	*/
	public final String Login_Init() throws Exception {
		//判断是否已经安装数据库，是否需要更新
		if (CheckIsDBInstall() == true)
		{
			return "url@../../WF/Admin/DBInstall.htm";
		}

		String doType = GetRequestVal("LoginType");
		if (DataType.IsNullOrEmpty(doType) == false && doType.equals("Out") == true)
		{
			//清空cookie
			WebUser.Exit();
		}

		//是否需要自动登录。 这里都把cookeis的数据获取来了.
		String userNo = this.GetRequestVal("UserNo");
		String sid = this.GetRequestVal("SID");

		if (StringHelper.isNullOrEmpty(sid) == false && StringHelper.isNullOrEmpty(userNo) == false)
		{
			//调用登录方法.
			bp.wf.Dev2Interface.Port_Login(this.getUserNo(), this.getSID());
			return "url@Apps.htm?UserNo=" + this.getUserNo() + "&SID=" + sid;

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

		return bp.tools.Json.ToJsonEntityModel(ht);
	}
	public final String Login_VerifyState() throws Exception {
		Cookie cookie = ContextHolderUtils.getCookie(this.getClass().getName() + "_Login_Error");
		if(cookie ==null)
			return "无需验证";
		if (DataType.IsNullOrEmpty(cookie.getValue())==false)
		{
			return "err@" + Login_VerifyCode();
		}

		return "无需验证";
	}

	public final String Login_VerifyCode() throws Exception {
		return bp.tools.Verify.DrawImage(5, this.getClass().getName() + "_VerifyCode");
	}

	public final String CheckEncryptEnable() {
		return bp.difference.SystemConfig.getIsEnablePasswordEncryption() ? "1":"0";
	}
	/**
	 登录.
	 
	 @return 
	*/
	public final String Login_Submit() throws Exception {
		try
		{

			String verifyCode = this.GetRequestVal("VerifyCode");
			Cookie verifyCookie = ContextHolderUtils.getCookie(this.getClass().getName() + "_VerifyCode");
			String checkVerifyCode = "";
			if(verifyCookie==null)
				checkVerifyCode ="";
			else
				checkVerifyCode = URLDecoder.decode(verifyCookie.getValue());
			String strMd5 = DataType.IsNullOrEmpty(verifyCode) ? "" :bp.tools.Rand.GetMd5Str(verifyCode);

			String login_Error = ContextHolderUtils.getCookie(this.getClass().getName() + "_Login_Error")==null?"":ContextHolderUtils.getCookie(this.getClass().getName() + "_Login_Error").getValue();


//			if (DataType.IsNullOrEmpty(login_Error) == true && DataType.IsNullOrEmpty(verifyCode) == false)
//				return "err@错误的验证状态.";

			if (DataType.IsNullOrEmpty(login_Error) == false && checkVerifyCode.equals(strMd5)==false)
				return "err@验证码错误.";

			ContextHolderUtils.clearCookie();
			ContextHolderUtils.addCookie(this.getClass().getName() + "_VerifyCode", "");
			ContextHolderUtils.addCookie(this.getClass().getName() + "_Login_Error", "");

			String userNo = this.GetRequestVal("TB_No");
			if (userNo == null)
			{
				userNo = this.GetRequestVal("TB_UserNo");
			}

			userNo = userNo.trim();

			String pass = this.GetRequestVal("TB_PW");
			if (bp.difference.SystemConfig.getIsEnablePasswordEncryption() == true) {
				pass = Encodes.decodeBase64String(pass);
			}

			if (pass == null)
			{
				pass = this.GetRequestVal("TB_Pass");
			}

			//pass = AesEncryptUtil.desEncrypt(pass).trim();
			pass = pass.trim();
//			if (DataType.IsNullOrEmpty(userNo) == false && userNo.equals("admin"))
//			{
//
//				try
//				{
//					// 执行升级
//					bp.wf.Glo.UpdataCCFlowVer();
//				}
//				catch (RuntimeException ex)
//				{
//					bp.wf.Glo.UpdataCCFlowVer();
//					String msg = "err@升级失败(ccbpm有自动修复功能,您可以刷新一下系统会自动创建字段,刷新多次扔解决不了问题,请反馈给我们)";
//					msg += "@系统信息:" + ex.getMessage();
//					return msg;
//				}
//			}

			bp.port.Emp emp = new bp.port.Emp();
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

			if (bp.wf.Glo.getCCBPMRunModel() == CCBPMRunModel.Single)
			{
				//调用登录方法.
				String token = Dev2Interface.Port_GenerToken(emp.getNo(),"PC");
				Dev2Interface.Port_Login(emp.getUserID());
				if (DBAccess.IsExitsTableCol("Port_Emp", "EmpSta") == true)
				{
					String sql = "SELECT EmpSta FROM Port_Emp WHERE No='" + emp.getNo() + "'";
					if (DBAccess.RunSQLReturnValInt(sql, 1) == 1)
					{
						return "err@该用户已经被禁用.";
					}
				}
				return "url@Default.htm?Token=" + token + "&UserNo=" + emp.getUserID();
			}

			//获得当前管理员管理的组织数量.

			//查询他管理多少组织.
			OrgAdminers adminers = new OrgAdminers();
			adminers.Retrieve(OrgAdminerAttr.FK_Emp, emp.getUserID(), null);
			if (adminers.size() == 0)
			{
				//调用登录方法， 普通用户了.
				String token = Dev2Interface.Port_GenerToken(emp.getNo(),"PC");
				Dev2Interface.Port_Login(emp.getUserID(), emp.getOrgNo());
				return "url@Default.htm?Token=" + token+ "&UserNo=" + emp.getUserID() + "&OrgNo=" + emp.getOrgNo();
			}



			ClearOldSession();
			//设置SID.
			String token = bp.wf.Dev2Interface.Port_GenerToken(emp.getNo(),"PC");

			//设置他的组织，信息.
			WebUser.setNo(emp.getUserID()); //登录帐号.
			WebUser.setFK_Dept(emp.getFK_Dept());
			WebUser.setFK_DeptName(emp.getFK_DeptText());

			//执行登录.
			bp.wf.Dev2Interface.Port_Login(emp.getUserID(), null, emp.getOrgNo());


			//判断是否是多个组织的情况.
			if (adminers.size() == 1)
				return "url@Default.htm?Token=" + token + "&UserNo=" + emp.getUserID() + "&OrgNo=" + emp.getOrgNo();
			return "url@SelectOneOrg.htm?Token=" + token + "&UserNo=" + emp.getUserID() + "&OrgNo=" + emp.getOrgNo();
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
	}
	private boolean CheckIsDBInstall() throws Exception {
		//检查数据库连接.
		try
		{
			DBAccess.TestIsConnection();
		}
		catch (Exception ex)
		{
			throw new Exception("err@异常信息:" + ex.getMessage());
		}

		//检查是否缺少Port_Emp 表，如果没有就是没有安装.
		if (DBAccess.IsExitsObject("Port_Emp") == false && DBAccess.IsExitsObject("WF_Flow") == false)
			return true;

		//如果没有流程表，就执行安装.
		if (DBAccess.IsExitsObject("WF_Flow") == false)
			return true;
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
		if (SystemConfig.getCCBPMRunModel() != CCBPMRunModel.Single)
		{
			sqlWhere = "   OrgNo='" + WebUser.getOrgNo() + "' AND No!='" + WebUser.getOrgNo() + "'";
		}
		else
		{
			sqlWhere = "   No!='100' ";
		}


		//求内容.
		sql = "SELECT No as \"No\",Name as \"Name\" FROM Sys_FormTree WHERE  " + sqlWhere + " ORDER BY Idx ";
		DataTable dtSort = DBAccess.RunSQLReturnTable(sql);
		if (SystemConfig.AppCenterDBFieldCaseModel() != FieldCaseModel.None)
		{
			dtSort.Columns.get(0).setColumnName("No");
			dtSort.Columns.get(1).setColumnName("Name");
			//dtSort.Columns.get(2).setColumnName("WFSta2";
			//dtSort.Columns.get(3).setColumnName("WFSta3";
			//dtSort.Columns.get(4).setColumnName("WFSta5";
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
		sql = "SELECT No as \"No\",Name as \"Name\",FrmType,FK_FormTree,PTable,DBSrc,Icon,EntityType FROM Sys_MapData WHERE 1=1 " + sqlWhere + " ORDER BY Idx ";
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

		if (SystemConfig.AppCenterDBFieldCaseModel() != FieldCaseModel.None)
		{
			dtFlow.Columns.get(0).setColumnName("No");
			dtFlow.Columns.get(1).setColumnName("Name");
			dtFlow.Columns.get(2).setColumnName("FrmType");
			dtFlow.Columns.get(3).setColumnName("FK_FormTree");
			dtFlow.Columns.get(4).setColumnName("PTable");
			dtFlow.Columns.get(5).setColumnName("DBSrc");
			dtFlow.Columns.get(6).setColumnName("Icon");
			dtFlow.Columns.get(7).setColumnName("EntityType");

			//dtFlow.Columns.get(2).setColumnName("WorkModel";
			//dtFlow.Columns.get(3).setColumnName("AtPara";
			//dtFlow.Columns.get(4).setColumnName("FK_FlowSort";
			//dtFlow.Columns.get(5).setColumnName("WFSta2";
			//dtFlow.Columns.get(6).setColumnName("WFSta3";
			//dtFlow.Columns.get(7).setColumnName("WFSta5";
		}
		return Json.ToJson(dtFlow);
	}
	/** 
	 流程移动.
	 
	 @return 
	*/
	public final String Frms_Move() throws Exception {
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

		FrmTree ft = new FrmTree();

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
		private void ClearOldSession(){
			HttpSession session = ContextHolderUtils.getSession();

			// 用来存储原sessionde的值
			ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();

			Enumeration enumeration = session.getAttributeNames();
			// 遍历enumeration
			while (enumeration.hasMoreElements()) {
				// 获取session的属性名称
				String name = enumeration.nextElement().toString();
				// 根据键值取session中的值
				concurrentHashMap.put(name,session.getAttribute(name));
			}

			// 获取之前旧的session
			HttpSession oldSession = ContextHolderUtils.getRequest().getSession(false);
			if (oldSession != null) {
				//废除掉登陆前的session
				oldSession.invalidate();
			}
			ContextHolderUtils.getRequest().getSession(true);
			// 获取新session
			session =  ContextHolderUtils.getRequest().getSession();
			// 将原先老session的值存入
			java.util.Iterator<java.util.Map.Entry<String, String>> it = concurrentHashMap.entrySet().iterator();
			while (it.hasNext()) {
				java.util.Map.Entry<String, String> entry = it.next();
				session.setAttribute(entry.getKey(), entry.getValue());
			}
		}

		///#region Flows.htm 流程.
	/** 
	 初始化类别.
	 
	 @return 
	*/
	public final String Flows_InitSort() throws Exception {
		String sql = "";
		String dbStr = SystemConfig.getAppCenterDBVarStr();
		DataTable dt = null;
		//集团模式且一个部门下维护一套岗位体系
		if (SystemConfig.getCCBPMRunModel() == CCBPMRunModel.GroupInc)
		{
			//如果当前管理员登录的部门是主部门
			Paras ps = new Paras();

			sql = "SELECT No,Name,ParentNo From WF_FlowSort WHERE No='" + WebUser.getFK_Dept() + "' or parentNo='" + WebUser.getFK_Dept()+"'" ;
			dt = DBAccess.RunSQLReturnTable(sql);
			if (dt.Rows.size() == 1)
			{
				//根据这个部门编号生成一个流程类别
				bp.wf.template.FlowSort fs = new bp.wf.template.FlowSort();
				fs.setNo(WebUser.getOrgNo());

				if(fs.RetrieveFromDBSources()==0) {
					bp.port.Dept dept = new bp.port.Dept(WebUser.getOrgNo());
					fs.setParentNo(dept.getParentNo());

					bp.wf.port.admingroup.Org org = new bp.wf.port.admingroup.Org(WebUser.getOrgNo());
					fs.setName(org.getName()); // WebUser.FK_DeptName;
					fs.setOrgNo(WebUser.getOrgNo());
					fs.DirectInsert();
				}
				EntityTree subFS1 = fs.DoCreateSubNode("办公类");
				subFS1.SetValByKey("OrgNo", fs.getNo());
				subFS1.Update();

				EntityTree subFS2 = fs.DoCreateSubNode("财务类");
				subFS2.SetValByKey("OrgNo", fs.getNo());
				subFS2.Update();

			}

		}

		//求数量.
		String sqlWhere = "";
		if (SystemConfig.getCCBPMRunModel() != CCBPMRunModel.Single)
		{
			sqlWhere = "   OrgNo='" + WebUser.getOrgNo() + "' AND WFState>0 ";
		}
		else
		{
			sqlWhere = " WFState>0 ";
		}


		sql = "SELECT  FK_FlowSort, WFState, COUNT(*) AS Num FROM WF_GenerWorkFlow WHERE " + sqlWhere + " GROUP BY FK_FlowSort, WFState ";
		dt = DBAccess.RunSQLReturnTable(sql);
		//求内容. 
		if (SystemConfig.getCCBPMRunModel() != CCBPMRunModel.Single)
		{
			sqlWhere = "   OrgNo='" + WebUser.getOrgNo() + "' AND No!='" + WebUser.getOrgNo() + "'";
		}
		else
		{
			sqlWhere = "   ParentNo!='0' ";
		}
		sql = "SELECT No as \"No\",Name as \"Name\", 0 as WFSta2, 0 as WFSta3, 0 as WFSta5 FROM WF_FlowSort WHERE  " + sqlWhere + " ORDER BY Idx ";
		DataTable dtSort = DBAccess.RunSQLReturnTable(sql);
		if (SystemConfig.AppCenterDBFieldCaseModel() != FieldCaseModel.None)
		{
			dtSort.Columns.get(0).setColumnName("No");
			dtSort.Columns.get(1).setColumnName("Name");
			dtSort.Columns.get(2).setColumnName("WFSta2");
			dtSort.Columns.get(3).setColumnName("WFSta3");
			dtSort.Columns.get(4).setColumnName("WFSta5");
		}

		// 给状态赋值.
		for (DataRow dr : dtSort.Rows)
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
		return Json.ToJson(dtSort);
	}
	public final String Flows_Init() throws Exception {
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
		sql = "SELECT No as \"No\",Name as \"Name\",WorkModel, FK_FlowSort, 0 as WFSta2, 0 as WFSta3, 0 as WFSta5 FROM WF_Flow WHERE 1=1 " + sqlWhere + " ORDER BY Idx ";
		DataTable dtFlow = DBAccess.RunSQLReturnTable(sql);
		if (SystemConfig.AppCenterDBFieldCaseModel() != FieldCaseModel.None)
		{
			dtFlow.Columns.get(0).setColumnName("No");
			dtFlow.Columns.get(1).setColumnName("Name");
			dtFlow.Columns.get(2).setColumnName("WorkModel");
			//dtFlow.Columns.get(3).setColumnName("AtPara";
			dtFlow.Columns.get(3).setColumnName("FK_FlowSort");
			dtFlow.Columns.get(4).setColumnName("WFSta2");
			dtFlow.Columns.get(5).setColumnName("WFSta3");
			dtFlow.Columns.get(6).setColumnName("WFSta5");
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
	public final String Flows_Move() throws Exception {
		String sourceSortNo = this.GetRequestVal("SourceSortNo");
		String sourceFlowNos[] = this.GetRequestVal("SourceFlowNos").split("[,]", -1);
		String toSortNo = this.GetRequestVal("ToSortNo");
		String toFlowNos[] = this.GetRequestVal("ToFlowNos").split("[,]", -1);
		String flowNos[] = sourceFlowNos;
		for (int i = 0; i < flowNos.length; i++)
		{
			String flowNo = flowNos[i];

			String sql = "UPDATE WF_Flow SET FK_FlowSort ='" + sourceSortNo + "',Idx=" + i + " WHERE No='" + flowNo + "'";
			DBAccess.RunSQL(sql);
		}
		//如果是在同一个流程类别中拖动流程顺序
		if (sourceSortNo.equals(toSortNo) == true)
			return "流程顺序移动成功..";
		flowNos = toFlowNos;
		for (int i = 0; i < flowNos.length; i++)
		{
			String flowNo = flowNos[i];

			String sql = "UPDATE WF_Flow SET FK_FlowSort ='" + toSortNo + "',Idx=" + i + " WHERE No='" + flowNo + "'";
			DBAccess.RunSQL(sql);
		}
		return "流程顺序移动成功..";
	}
	public final String Flows_MoveSort() throws Exception {
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
		//    dr["TypeName"] = dr["MsgType"];
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

		//string docs = DBAccess.GetBigTextFromDB("Sys_UserRegedit", "MyPK", pkval, "BigDocs");
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

		String mydepts = "" + WebUser.getFK_Dept()+ ","; //我的部门.
		String mystas = ""; //我的岗位.

		DataTable mydeptsDT = DBAccess.RunSQLReturnTable("SELECT FK_Dept,FK_Station FROM Port_DeptEmpStation WHERE FK_Emp='" + WebUser.getNo() + "'");
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
			if (item.isEnable() == false)
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

				//是否包含岗位？
				if (pc.getCtrlModel().equals("Stations") == true && DataType.IsHaveIt(pc.getIDs(), mystas) == true)
				{
					systemsCopy.AddEntity(item);
					break;
				}

				//SQL？
				if (pc.getCtrlModel().equals("SQL") == true)
				{
					String sql = Glo.DealExp(pc.getIDs(), null, "");
					if (DBAccess.RunSQLReturnValFloat(sql) > 0)
					{
						systemsCopy.AddEntity(item);
					}
					break;
				}
				//用户组？
				String myteams = DBAccess.RunSQLReturnString("select FK_Team from Port_TeamEmp where FK_Emp='"+WebUser.getNo()+"'");
				if (pc.getCtrlModel().equals("Teams") == true&& DataType.IsHaveIt(pc.getIDs(), myteams) == true)
				{
					systemsCopy.AddEntity(item);
					break;
				}
			}
		}

			///#endregion 首先解决系统权限问题.


			///#region 2.0 根据求出的系统集合处理权限， 求出模块权限..
		for (MySystem item : systemsCopy.ToJavaList())
		{
			for (Module module : modules.ToJavaList())
			{
				//如果被禁用了.
				if (module.isEnable() == false)
				{
					continue;
				}


				if (module.getSystemNo().equals(item.getNo()) == false)
				{
					continue;
				}

				//找到关于系统的控制权限集合.
				bp.en.Entities tempVar2 = pcs.GetEntitiesByKey(PowerCenterAttr.CtrlPKVal, module.getNo());
				PowerCenters mypcs = tempVar2 instanceof PowerCenters ? (PowerCenters)tempVar2 : null;
				//如果没有权限控制的描述，就默认有权限.
				if (mypcs == null)
				{
					modulesCopy.AddEntity(module);
					continue;
				}

				//控制遍历权限.
				for (PowerCenter pc : mypcs.ToJavaList())
				{
					if (pc.getCtrlModel().equals("Anyone") == true)
					{
						modulesCopy.AddEntity(module);
						break;
					}
					if (pc.getCtrlModel().equals("Adminer") == true && WebUser.getNo().equals("admin") == true)
					{
						modulesCopy.AddEntity(module);
						break;
					}

					if (pc.getCtrlModel().equals("AdminerAndAdmin2") == true && WebUser.getIsAdmin() == true)
					{
						modulesCopy.AddEntity(module);
						break;
					}

					ids = "," + pc.getIDs() + ",";
					if (pc.getCtrlModel().equals("Emps") == true && ids.contains("," + WebUser.getNo() + ",") == true)
					{
						modulesCopy.AddEntity(module);
						break;
					}

					//是否包含部门？
					if (pc.getCtrlModel().equals("Depts") == true && this.IsHaveIt(pc.getIDs(), mydepts) == true)
					{
						modulesCopy.AddEntity(module);
						break;
					}

					//是否包含岗位？
					if (pc.getCtrlModel().equals("Stations") == true && this.IsHaveIt(pc.getIDs(), mystas) == true)
					{
						modulesCopy.AddEntity(module);
						break;
					}
					if (pc.getCtrlModel().equals("SQL") == true)
					{
						String sql = bp.wf.Glo.DealExp(pc.getIDs(), null, "");
						if (DBAccess.RunSQLReturnValFloat(sql) > 0)
						{
							modulesCopy.AddEntity(module);
						}
						break;
					}
					//用户组？
					String myteams = DBAccess.RunSQLReturnString("select FK_Team from Port_TeamEmp where FK_Emp='"+WebUser.getNo()+"'");
					if (pc.getCtrlModel().equals("Teams") == true&& DataType.IsHaveIt(pc.getIDs(), myteams) == true)
					{
						modulesCopy.AddEntity(module);
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
				if (menu.isEnable() == false)
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

					//是否包含岗位？
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
					//用户组？
					String myteams = DBAccess.RunSQLReturnString("select FK_Team from Port_TeamEmp where FK_Emp='"+WebUser.getNo()+"'");
					if (pc.getCtrlModel().equals("Teams") == true&& DataType.IsHaveIt(pc.getIDs(), myteams) == true)
					{
						menusCopy.AddEntity(menu);
						break;
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
	 
	 param ids1
	 param ids2
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
			if (str1.equals("") || str1 == null)
			{
				continue;
			}

			for (String str2 : str2s)
			{
				if (str2.equals("") || str2 == null)
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
		WebUser.Exit();

		if (SystemConfig.getCCBPMRunModel() == CCBPMRunModel.SAAS)
		{
			return "http://passport.ccbpm.cn/";
		}

		return "Login.htm?DoType=Logout";
	}
	/** 
	 返回构造的JSON.
	 
	 @return 
	*/
	public final String Default_Init() throws Exception {
		//如果是admin.
		if (WebUser.getIsAdmin() == true && this.getIsMobile() == false)
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
		if (WebUser.getIsAdmin() == true && this.getIsMobile() == false)
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
				drModel.setValue("No", dtRow.getValue("No"));
				drModel.setValue("Name", dtRow.getValue("Name"));
				drModel.setValue("SystemNo", "System");
				drModel.setValue("Icon", dtRow.getValue("Icon"));
				dtModule.Rows.add(drModel);
			}

			//增加菜单.
			DataTable dtItem = dsAdminMenus.GetTableByName("Item");
			for (DataRow dtRow : dtItem.Rows)
			{
				DataRow drMenu = dtMenu.NewRow();
				drMenu.setValue("No", dtRow.getValue("No"));
				drMenu.setValue("Name", dtRow.getValue("Name"));
				drMenu.setValue("ModuleNo", dtRow.getValue("GroupNo"));
				drMenu.setValue("Url", dtRow.getValue("Url"));
				drMenu.setValue("Icon", dtRow.getValue("Icon"));
				drMenu.setValue("SystemNo", "System");
				dtMenu.Rows.add(drMenu);
			}
		}

			///#endregion 如果是admin.

		//   myds.WriteXml("c:/11.xml");


			///#region 让第一个系统的第1个模块的默认打开的.
		//让第一个打开.
		myds.GetTableByName("System").Rows.get(0).setValue("IsOpen", "true") ;
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

			///#endregion 让第一个系统的第1个模块的第一个菜单打开.


		return Json.ToJson(myds);
	}


		///#endregion   加载菜单.


	/** 
	 生成页面
	 
	 @return 
	*/
	public final String LoginGenerQRCodeMobile_Init() throws Exception {
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
		if(SystemConfig.getIsJarRun()==true){
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
		if(SystemConfig.getIsJarRun()==true){
			return "url@DataUser/Temp/" + frmTreeName + ".zip";
		}
		return "url@DataUser/Temp/" + frmTreeName + ".zip";
	}

		///#endregion 按照表单类别批量导出表单模板


}