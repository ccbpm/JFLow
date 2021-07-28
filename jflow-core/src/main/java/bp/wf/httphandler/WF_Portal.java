package bp.wf.httphandler;


import java.util.Hashtable;

import bp.da.DBAccess;
import bp.da.DBType;
import bp.da.DataRow;
import bp.da.DataSet;
import bp.da.DataTable;
import bp.da.DataType;
import bp.da.FieldCaseModel;
import bp.da.Paras;
import bp.difference.SystemConfig;
import bp.difference.handler.WebContralBase;
import bp.gpm.home.WindowTemplates;
import bp.gpm.menu2020.Menus;
import bp.gpm.menu2020.Modules;
import bp.gpm.menu2020.MySystems;
import bp.port.Emp;
import bp.sys.CCBPMRunModel;
import bp.sys.FrmTree;
import bp.sys.MapData;
import bp.tools.StringHelper;
import bp.web.WebUser;
import bp.wf.Flow;
import bp.wf.Glo;
//import bp.gpm.menu2020.*;
import bp.wf.port.admin2.Org;
import bp.wf.port.admin2.OrgAdminer;
import bp.wf.port.admin2.OrgAdminerAttr;
import bp.wf.port.admin2.OrgAdminers;
import bp.wf.port.admin2.Orgs;
import bp.wf.template.FlowSort;

/** 
 页面功能实体
*/
public class WF_Portal extends WebContralBase
{

	
	/** 
	 构造函数
	*/
	public WF_Portal()
	{
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

		return bp.tools.Json.ToJson(ht);
	}
	/** 
	 初始化登录界面.
	 
	 @return 
	 * @throws Exception 
	*/
	public final String Login_Init() throws Exception
	{
		//判断是否已经安装数据库，是否需要更新
		if (CheckIsDBInstall() == true)
		{
			return "url@../Admin/DBInstall.htm";
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
	 /** 
	 登录.
	 
	 @return 
	 * @throws Exception 
*/
	public final String Login_Submit() throws Exception
	{
		try
		{
			String userNo = this.GetRequestVal("TB_No");
			if (userNo == null)
			{
				userNo = this.GetRequestVal("TB_UserNo");
			}

			String pass = this.GetRequestVal("TB_PW");
			if (pass == null)
			{
				pass = this.GetRequestVal("TB_Pass");
			}

			if (DataType.IsNullOrEmpty(userNo) == false && userNo.equals("admin"))
			{

				try
				{
					// 执行升级
					bp.wf.Glo.UpdataCCFlowVer();
				}
				catch (RuntimeException ex)
				{
					bp.wf.Glo.UpdataCCFlowVer();
					String msg = "err@升级失败(ccbpm有自动修复功能,您可以刷新一下系统会自动创建字段,刷新多次扔解决不了问题,请反馈给我们)";
					msg += "@系统信息:" + ex.getMessage();
					return msg;
				}
			}
			bp.port.Emp emp = new Emp();
			emp.setUserID(userNo);
			if (emp.RetrieveFromDBSources() == 0)
			{
				if (DBAccess.IsExitsTableCol("Port_Emp", "NikeName") == true)
				{
					/*如果包含昵称列,就检查昵称是否存在.*/
					Paras ps = new Paras();
					ps.SQL = "SELECT No FROM Port_Emp WHERE NikeName=" + SystemConfig.getAppCenterDBVarStr() + "NikeName";
					ps.Add("NikeName", userNo);
					String no = DBAccess.RunSQLReturnStringIsNull(ps, null);
					if (no == null)
					{
						return "err@用户名或者密码错误.";
					}

					emp.setNo(no);
					int i = emp.RetrieveFromDBSources();
					if (i == 0)
					{
						return "err@用户名或者密码错误.";
					}
				}
				else if (DBAccess.IsExitsTableCol("Port_Emp", "Tel") == true)
				{
					/*如果包含Name列,就检查Name是否存在.*/
					Paras ps = new Paras();
					ps.SQL = "SELECT No FROM Port_Emp WHERE Tel=" + SystemConfig.getAppCenterDBVarStr() + "Tel";
					ps.Add("Tel", userNo);
					String no = DBAccess.RunSQLReturnStringIsNull(ps, null);
					if (no == null)
					{
						return "err@用户名或者密码错误.";
					}

					emp.setNo(no);
					int i = emp.RetrieveFromDBSources();
					if (i == 0)
					{
						return "err@用户名或者密码错误.";
					}
				}
				else
				{
					return "err@用户名或者密码错误.";
				}
			}

			if (emp.CheckPass(pass) == false)
			{
				return "err@用户名或者密码错误.";
			}

			if (Glo.getCCBPMRunModel() == CCBPMRunModel.Single)
			{
				//调用登录方法.
				bp.wf.Dev2Interface.Port_Login(emp.getUserID());

				if (DBAccess.IsView("Port_Emp") == false)
				{
					String sid = DBAccess.GenerGUID();
					DBAccess.RunSQL("UPDATE Port_Emp SET SID='" + sid + "' WHERE No='" + emp.getUserID() + "'");
					WebUser.setSID(sid);
					emp.setSID(sid);
				}

				return "url@Default.htm?SID=" + emp.getSID() + "&UserNo=" + emp.getUserID();
			}

			//获得当前管理员管理的组织数量.
			OrgAdminers adminers = null;

			//查询他管理多少组织.
			adminers = new OrgAdminers();
			adminers.Retrieve(OrgAdminerAttr.FK_Emp, emp.getUserID());
			if (adminers.size() == 0)
			{
				bp.wf.port.admin2.Orgs orgs = new Orgs();
				int i = orgs.Retrieve("Adminer", this.GetRequestVal("TB_No"));
				if (i == 0)
				{
					//调用登录方法.
					bp.wf.Dev2Interface.Port_Login(emp.getUserID(), null, emp.getOrgNo());
					return "url@Default.htm?SID=" + emp.getSID() + "&UserNo=" + emp.getUserID() + "&OrgNo=" + emp.getOrgNo();
				}


				for (Org org : orgs.ToJavaList())
				{
					OrgAdminer oa = new OrgAdminer();
					oa.setFK_Emp(WebUser.getNo());
					oa.setOrgNo(org.getNo());
					oa.Save();
				}
				adminers.Retrieve(OrgAdminerAttr.FK_Emp, emp.getUserID());
			}


			//设置他的组织，信息.
			WebUser.setNo(emp.getUserID()); //登录帐号.
			WebUser.setFK_Dept(emp.getFK_Dept());
			WebUser.setFK_DeptName(emp.getFK_DeptText());


			//执行登录.
			bp.wf.Dev2Interface.Port_Login(emp.getUserID(), null, emp.getOrgNo());

			//设置SID.
			WebUser.setSID(DBAccess.GenerGUID()); //设置SID.
			emp.setSID(WebUser.getSID()); //设置SID.
			bp.wf.Dev2Interface.Port_SetSID(emp.getUserID(), WebUser.getSID());

			//执行更新到用户表信息.
			// WebUser.UpdateSIDAndOrgNoSQL();

			//判断是否是多个组织的情况.
			if (adminers.size() == 1)
			{
				return "url@Default.htm?SID=" + emp.getSID() + "&UserNo=" + emp.getUserID() + "&OrgNo=" + emp.getOrgNo();
			}

			return "url@SelectOneOrg.htm?SID=" + emp.getSID() + "&UserNo=" + emp.getUserID() + "&OrgNo=" + emp.getOrgNo();


		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
	}

	/// <summary>
    /// 初始化
    /// </summary>
    /// <returns></returns>
    public String Home_Init() throws Exception
    {
        WindowTemplates ens = new WindowTemplates();
        ens.RetrieveAll();

        DataTable dt = ens.ToDataTableField();
        dt.TableName = "WindowTemplates";
        return bp.tools.Json.ToJson(dt);

        //return "移动成功..";
    }
    public String Home_DoMove()
    {
    	String[] mypks = this.getMyPK().split(",");
        for (int i = 0; i < mypks.length; i++)
        {
        	String str = mypks[i];
            if (str == null || str == "")
                continue;

            String sql = "UPDATE GPM_WindowTemplate SET Idx=" + i + " WHERE No='" + str + "' ";
            DBAccess.RunSQL(sql);
        }
        return "移动成功..";
    }
    
	private boolean CheckIsDBInstall() throws Exception
    {
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
	 ///#region   加载菜单 .
		/** 
		 返回构造的JSON.
		 
		 @return 
		 * @throws Exception 
		*/
		public final String Default_Init() throws Exception
		{
			//如果是admin.

			DataSet myds = new DataSet();

			///#region 构造数据容器.
			//系统表.
			MySystems systems = new MySystems();
			systems.RetrieveAll();
			DataTable dtSys = systems.ToDataTableField("System");

			//模块.
			Modules modules = new Modules();
			modules.RetrieveAll();
			DataTable dtModule = modules.ToDataTableField("Module");

			//菜单.
			Menus menus = new Menus();
			menus.RetrieveAll();
			DataTable dtMenu = menus.ToDataTableField("Menu");
			dtMenu.Columns.get("UrlExt").ColumnName = "Url";
			///#endregion 构造数据容器.

			///#region 如果是admin.
			if (WebUser.getIsAdmin() == true && this.getIsMobile() == false)
			{
				///#region 增加默认的系统.
				DataRow dr = dtSys.NewRow();
				dr.setValue("No", "Flows");
				//  dr["Name"] = "流程设计";
				dr.setValue("Icon", "");
				dtSys.Rows.add(dr);

				dr = dtSys.NewRow();
				dr.setValue("No", "Frms");
				//    dr["Name"] = "表单设计";
				dr.setValue("Icon", "");
				dtSys.Rows.add(dr);

				dr = dtSys.NewRow();
				dr.setValue("No", "System");
				//  dr["Name"] = "系统管理";
				dr.setValue("Icon", "");
				dtSys.Rows.add(dr);
				///#endregion 增加默认的系统.

				String sqlWhere = "";
				if (SystemConfig.getCCBPMRunModel() != CCBPMRunModel.Single)
				{
					sqlWhere = " AND OrgNo='" + WebUser.getOrgNo() + "'";
				}

				///#region 流程树.
				String sql = "SELECT No,Name,ParentNo FROM WF_FlowSort WHERE 1=1 " + sqlWhere + " ORDER BY Idx,No ";
				DataTable dtFlowSorts = DBAccess.RunSQLReturnTable(sql);
				dtFlowSorts.Columns.get(0).setColumnName("No");
				dtFlowSorts.Columns.get(1).setColumnName("Name");
				dtFlowSorts.Columns.get(2).setColumnName("ParentNo");
				dtFlowSorts.TableName = "FlowTree";

				//没有数据就预制数据.
				if (dtFlowSorts.Rows.size() == 0)
				{
					if (SystemConfig.getCCBPMRunModel() == CCBPMRunModel.Single)
					{
						FlowSort fs = new FlowSort();
						fs.setParentNo("0");
						fs.setName("流程根目录");
						fs.setNo("100");
						fs.Insert();

						fs.setNo("101");
						fs.setName("业务流程目录1");
						fs.setParentNo("100");
						fs.setIdx(0);
						fs.Insert();

						fs.setNo("102");
						fs.setName("业务流程目录2");
						fs.setParentNo("100");
						fs.setIdx(2);
						fs.Insert();
					}
					else
					{
						FlowSort fs = new FlowSort();
						fs.setParentNo("100");
						fs.setName("流程根目录");
						fs.setNo(WebUser.getOrgNo());
						fs.setOrgNo(WebUser.getOrgNo());
						fs.Insert();

						fs.setNo("101");
						fs.setName("业务流程目录1");
						fs.setParentNo(WebUser.getOrgNo());
						fs.setIdx(0);
						fs.setOrgNo(WebUser.getOrgNo());
						fs.Insert();

						fs.setNo("102");
						fs.setName("业务流程目录2");
						fs.setParentNo(WebUser.getOrgNo());
						fs.setIdx(2);
						fs.setOrgNo(WebUser.getOrgNo());
						fs.Insert();
					}
				}

				DataTable dtFlows = null;
				try
				{
					sql = "SELECT No,Name,FK_FlowSort AS TreeNo, WorkModel, '' as Icon, '' as Url FROM WF_Flow WHERE 1=1  " + sqlWhere + " AND  FK_FlowSort IS NOT NULL ORDER BY FK_FlowSort,Idx  ";
					dtFlows = DBAccess.RunSQLReturnTable(sql);
				}
				catch (RuntimeException ex)
				{
					Flow fl = new Flow();
					fl.CheckPhysicsTable();

					sql = "SELECT No,Name,FK_FlowSort AS TreeNo, WorkModel, '' as Icon, '' as Url FROM WF_Flow WHERE 1=1  " + sqlWhere + " AND  FK_FlowSort IS NOT NULL ORDER BY FK_FlowSort,Idx  ";
					dtFlows = DBAccess.RunSQLReturnTable(sql);
				}
				dtFlows.Columns.get(0).setColumnName("No");
                dtFlows.Columns.get(1).setColumnName("Name");
                dtFlows.Columns.get(2).setColumnName("TreeNo");
                dtFlows.Columns.get(3).setColumnName("WorkModel");
                dtFlows.Columns.get(4).setColumnName("Icon");
                dtFlows.Columns.get(5).setColumnName("Url");
				dtFlows.TableName = "Flows";
				///#endregion 流程树.

				///#region 表单树.
				sql = "SELECT No,Name, ParentNo, '' as Icon FROM Sys_FormTree WHERE 1=1 " + sqlWhere + "  ORDER BY Idx,No ";
				DataTable dtFrmSorts = DBAccess.RunSQLReturnTable(sql);
				dtFrmSorts.Columns.get(0).setColumnName("No");
                dtFrmSorts.Columns.get(1).setColumnName("Name");
                dtFrmSorts.Columns.get(2).setColumnName("ParentNo");
                dtFrmSorts.Columns.get(3).setColumnName("Icon");
				dtFrmSorts.TableName = "FrmTree";

				//没有数据就预制数据.
				if (dtFrmSorts.Rows.size() == 0)
				{
					if (SystemConfig.getCCBPMRunModel() == CCBPMRunModel.Single)
					{
						FrmTree fs = new FrmTree();
						fs.setParentNo("0");
						fs.setName("根目录");
						fs.setNo("100");
						fs.Insert();

						fs.setNo("101");
						fs.setName("业务表单目录1");
						fs.setParentNo ("100");
						fs.setIdx(0);
						fs.Insert();

						fs.setNo("102");
						fs.setName("业务表单目录2");
						fs.setParentNo("100");
						fs.setIdx(2);
						fs.Insert();
					}
					else
					{
						FrmTree fs = new FrmTree();
						fs.setParentNo("100");
						fs.setName("根目录");
						fs.setNo( WebUser.getOrgNo());
						fs.setOrgNo(WebUser.getOrgNo());
						fs.Insert();

						fs.setNo("101");
						fs.setName("业务表单目录1");
						fs.setParentNo(WebUser.getOrgNo());
						fs.setIdx(0);
						fs.setOrgNo(WebUser.getOrgNo());
						fs.Insert();

						fs.setNo("102");
						fs.setName("业务表单目录2");
						fs.setParentNo(WebUser.getOrgNo());
						fs.setIdx(2);
						fs.setOrgNo(WebUser.getOrgNo());
						fs.Insert();
					}
				}

				sql = "SELECT No,Name,FK_FormTree AS TreeNo,  '' as Icon, FrmType FROM Sys_MapData WHERE 1=1 " + sqlWhere + " AND (FK_FormTree!='' AND FK_FormTree IS NOT NULL)   ORDER BY FK_FormTree,Idx  ";
				DataTable dtFrms = DBAccess.RunSQLReturnTable(sql);
				dtFrms.Columns.get(0).setColumnName("No");
                dtFrms.Columns.get(1).setColumnName("Name");
                dtFrms.Columns.get(2).setColumnName("TreeNo");
                dtFrms.Columns.get(3).setColumnName("Icon");
                dtFrms.Columns.get(4).setColumnName("FrmType");
				dtFrms.TableName = "Frms";
				///#endregion 表单树.

				//加入菜单信息.
				myds.Tables.add(dtFlowSorts);
				myds.Tables.add(dtFlows);
				myds.Tables.add(dtFrmSorts);
				myds.Tables.add(dtFrms);

				// BP.WF.XML.AdminMenus ens = new XML.AdminMenus();
				DataSet dsAdminMenus = new DataSet();

				//模版
				String file = SystemConfig.getPathOfWebApp() + "DataUser\\XML\\AdminMenu2021.xml";

				//获得文件.
				dsAdminMenus.readXml(file);
				//增加模块.
				DataTable dtGroup = dsAdminMenus.GetTableByName("Group");
				for (DataRow dtRow : dtGroup.Rows)
				{
					DataRow drModel = dtModule.NewRow();
					drModel.setValue("No", dtRow.getValue("No"));
					drModel.setValue("Name", dtRow.getValue("Name"));
					drModel.setValue("SystemNo", "System");
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

			myds.Tables.add(dtSys);
			myds.Tables.add(dtModule);
			myds.Tables.add(dtMenu);
			//   myds.WriteXml("c:\\11.xml");

			return bp.tools.Json.ToJson(myds);
		}
		///#endregion   加载菜单.
		///#region Flows.htm 流程.
		/** 
		 初始化类别.
		 
		 @return 
		 * @throws Exception 
		*/
		public final String Flows_InitSort() throws Exception
		{

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


			String sql = "SELECT  FK_FlowSort, WFState, COUNT(*) AS Num FROM WF_GenerWorkFlow WHERE " + sqlWhere + " GROUP BY FK_FlowSort, WFState ";
			DataTable dt = DBAccess.RunSQLReturnTable(sql);

			//求内容.
			if (SystemConfig.getCCBPMRunModel() != CCBPMRunModel.Single)
			{
				sqlWhere = "   OrgNo='" + WebUser.getOrgNo() + "' AND No!='" + WebUser.getOrgNo() + "'";
			}
			else
			{
				sqlWhere = "  ParentNo!='0' ";
			}
			sql = "SELECT No,Name, 0 as WFSta2, 0 as WFSta3, 0 as WFSta5 FROM WF_FlowSort WHERE  " + sqlWhere + " ORDER BY Idx ";
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
				String flowNo = (String)dr.getValue(0);
				for (DataRow mydr : dt.Rows)
				{
					String fk_flow = (String)mydr.getValue(0);
					if (fk_flow.equals(flowNo) == false)
					{
						continue;
					}
					int wfstate = 0;
				    if(mydr.get(1) != null){
				         wfstate = Integer.parseInt(mydr.get(1).toString());
				     }
				     
				     int Num =0;
				     if(mydr.get(2)!=null) {
				         Num = Integer.parseInt(mydr.get(2).toString());
				     }
					if (wfstate == 2)
					{
						dr.set("WFSta2", Num);
					}
					if (wfstate == 3)
					{
						dr.set("WFSta3", Num);
					}
					if (wfstate == 5)
					{
						dr.set("WFSta5", Num);
					}
					break;
				}
			}
			return bp.tools.Json.ToJson(dtSort);
		}
		public final String Flows_Init() throws Exception
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
			sql = "SELECT No,Name,WorkModel, FK_FlowSort, 0 as WFSta2, 0 as WFSta3, 0 as WFSta5 FROM WF_Flow WHERE 1=1 " + sqlWhere + " ORDER BY Idx ";
			DataTable dtFlow = DBAccess.RunSQLReturnTable(sql);
			if (SystemConfig.AppCenterDBFieldCaseModel() != FieldCaseModel.None)
			{
				dtFlow.Columns.get(0).setColumnName("No");
				dtFlow.Columns.get(1).setColumnName("Name");
				dtFlow.Columns.get(2).setColumnName("WorkModel");
				//dtFlow.Columns.get(3).setColumnName("AtPara");
				dtFlow.Columns.get(3).setColumnName("FK_FlowSort");
				dtFlow.Columns.get(4).setColumnName("WFSta2");
				dtFlow.Columns.get(5).setColumnName("WFSta3");
				dtFlow.Columns.get(6).setColumnName("WFSta5");
			}

			// 给状态赋值.
			for (DataRow dr : dtFlow.Rows)
			{
				String flowNo = dr.getValue(0) instanceof String ? (String)dr.get(0) : null;
				for (DataRow mydr : dt.Rows)
				{
					String fk_flow = mydr.getValue(0).toString();
					if (fk_flow.equals(flowNo) == false)
					{
						continue;
					}

					int wfstate = Integer.parseInt(mydr.get(1).toString());
					int Num = Integer.parseInt(mydr.get(2).toString());
					if (wfstate == 2)
					{
						dr.set("WFSta2", Num);
					}
					if (wfstate == 3)
					{
						dr.set("WFSta3", Num);
					}
					if (wfstate == 5)
					{
						dr.set("WFSta5", Num);
					}
					break;
				}
			}
			return bp.tools.Json.ToJson(dtFlow);
		}
		 /** 
		 流程移动.
		 
		 @return 
		  */
		public final String Flows_Move()
		{
			String sortNo = this.GetRequestVal("SortNo");
			String[] flowNos = this.GetRequestVal("EnNos").split("[,]", -1);
			for (int i = 0; i < flowNos.length; i++)
			{
				String flowNo = flowNos[i];

				String sql = "UPDATE WF_Flow SET FK_FlowSort ='" + sortNo + "',Idx=" + i + " WHERE No='" + flowNo + "'";
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
		 //region Frm.htm 表单.
	        /// <summary>
	        /// 表单树.
	        /// </summary>
	        /// <returns></returns>
	        public String Frms_InitSort() throws Exception
	        {
	            //获得数量.
	        	String sqlWhere = "";
	        	String sql = "";
	            if (SystemConfig.getCCBPMRunModel() != CCBPMRunModel.Single)
	                sqlWhere = "   OrgNo='" + WebUser.getOrgNo() + "' AND No!='" + WebUser.getOrgNo() + "'";
	            else
	                sqlWhere = "   ParentNo!='0' ";


	            //求内容.
	            sql = "SELECT No,Name FROM Sys_FormTree WHERE  " + sqlWhere + " ORDER BY Idx ";
	            DataTable dtSort = DBAccess.RunSQLReturnTable(sql);
	            if (SystemConfig.AppCenterDBFieldCaseModel() != FieldCaseModel.None)
	            {
	                dtSort.Columns.get(0).setColumnName("No");
	                dtSort.Columns.get(1).setColumnName("Name");
	            }
	            return bp.tools.Json.ToJson(dtSort);
	        }
	        /// <summary>
	        /// 表单
	        /// </summary>
	        /// <returns></returns>
	        public String Frms_Init() throws Exception
	        {
	            //获得流程实例的数量.
	        	String sqlWhere = "";
	        	String sql = "";
	            if (SystemConfig.getCCBPMRunModel() != CCBPMRunModel.Single)
	                sqlWhere = " AND OrgNo='" + WebUser.getOrgNo() + "'";

	            //求流程内容.
	            sql = "SELECT No,Name,FrmType,FK_FormTree,PTable,DBSrc,Icon,EntityType FROM Sys_MapData WHERE 1=1 " + sqlWhere + " ORDER BY Idx ";
	            DataTable dtFlow = null;
	            try
	            {
	                dtFlow = DBAccess.RunSQLReturnTable(sql);
	            }
	            catch (Exception ex)
	            {
	                MapData md = new MapData();
	                md.CheckPhysicsTable();
    				if (DBAccess.IsExitsTableCol("Sys_MapData", "Icon") == false)
    				{
    					/*如果没有此列，就自动创建此列.*/
    					String sqlA = "ALTER TABLE Sys_MapData ADD  Icon varchar(500) ";
    					DBAccess.RunSQL(sqlA);
    				}
	    			
	                dtFlow = DBAccess.RunSQLReturnTable(sql);
	            }

                dtFlow.Columns.get(0).setColumnName("No");
                dtFlow.Columns.get(1).setColumnName("Name");
                dtFlow.Columns.get(2).setColumnName("FrmType");
                dtFlow.Columns.get(3).setColumnName("FK_FormTree");
                dtFlow.Columns.get(4).setColumnName("PTable");
                dtFlow.Columns.get(5).setColumnName("DBSrc");
                dtFlow.Columns.get(6).setColumnName("Icon");
                dtFlow.Columns.get(7).setColumnName("EntityType");
	            
	            return bp.tools.Json.ToJson(dtFlow);
	        }

}
