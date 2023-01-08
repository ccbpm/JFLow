package bp.wf.httphandler;

import bp.da.*;
import bp.difference.SystemConfig;
import bp.sys.*;
import bp.web.*;
import bp.port.*;
import bp.en.*;
import bp.wf.Glo;
import bp.wf.template.*;
import bp.*;
import bp.wf.*;

import java.util.Hashtable;

/** 
 页面功能实体
*/
public class WF_Admin_TestingContainer extends bp.difference.handler.WebContralBase
{
	/** 
	 构造函数
	*/
	public WF_Admin_TestingContainer() throws Exception {
	}

	/** 
	 测试页面初始化
	 
	 @return 
	*/
	public final String Default_Init() throws Exception {
		String testerNo = this.GetRequestVal("TesterNo");
		if (WebUser.getNo().equals(testerNo) == false)
		{
			if (bp.difference.SystemConfig.getCCBPMRunModel() == CCBPMRunModel.Single)
			{
				Dev2Interface.Port_Login(testerNo);
			}
			else
			{
				Dev2Interface.Port_Login(testerNo, WebUser.getOrgNo());
			}
		}

		long workid = bp.wf.Dev2Interface.Node_CreateBlankWork(this.getFK_Flow(), testerNo);
		return String.valueOf(workid);
	}
	/** 
	 数据库信息
	 
	 @return 
	*/
	public final String DBInfo_Init() throws Exception {
		//数据容器，用于盛放数据，并返回json.
		DataSet ds = new DataSet();

		//流程引擎控制表.
		GenerWorkFlow gwf = new GenerWorkFlow(this.getWorkID());
		ds.Tables.add(gwf.ToDataTableField("WF_GenerWorkFlow"));

		//流程引擎人员列表.
		GenerWorkerLists gwls = new GenerWorkerLists(this.getWorkID());
		gwls.Retrieve(GenerWorkerListAttr.WorkID, this.getWorkID(), GenerWorkerListAttr.RDT);
		ds.Tables.add(gwls.ToDataTableField("WF_GenerWorkerList"));


		//获得Track数据.
		String table = "ND" + Integer.parseInt(this.getFK_Flow()) + "Track";
		String sql = "SELECT * FROM " + table + " WHERE WorkID=" + this.getWorkID();
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Track";
		//把列大写转化为小写.
		if (SystemConfig.getAppCenterDBType() == DBType.Oracle
				|| SystemConfig.getAppCenterDBType() == DBType.KingBaseR3
				|| SystemConfig.getAppCenterDBType() == DBType.KingBaseR6)
		{
			Track tk = new Track();
			for (Attr attr : tk.getEnMap().getAttrs())
			{
				if (dt.Columns.contains(attr.getKey().toUpperCase()) == true)
				{
					dt.Columns.get(attr.getKey().toUpperCase()).setColumnName( attr.getKey());

				}
			}
		}
		ds.Tables.add(dt);

		//获得NDRpt表
		String rpt = "ND" + Integer.parseInt(this.getFK_Flow()) + "Rpt";
		GEEntity en = new GEEntity(rpt);
		en.setPKVal ( this.getWorkID());
		en.RetrieveFromDBSources();
		ds.Tables.add(en.ToDataTableField("NDRpt"));

		//转化为json ,返回出去.
		return bp.tools.Json.ToJson(ds);
	}
	/** 
	 SelectOneUser_Init
	 
	 @return 
	*/
	public final String SelectOneUser_Init() throws Exception {
		//Default_LetAdminerLogin();

		GenerWorkerLists ens = new GenerWorkerLists();
		QueryObject qo = new QueryObject(ens);
		qo.AddWhere("WorkID", this.getWorkID());
		qo.addOr();
		qo.AddWhere("FID", this.getWorkID());
		qo.addOrderBy(" FK_Node,RDT,CDT ");
		qo.DoQuery();

		return ens.ToJson("dt");
	}

	/** 
	 让adminer登录.
	 
	 @return 
	*/
	public final String Default_LetAdminerLogin() throws Exception {
		try
		{
			String token = this.GetRequestVal("Token");
			String userNo = Dev2Interface.Port_LoginByToken(token);
			//Dev2Interface.Port_GenerToken(userNo);
			WF_Comm comm = new WF_Comm();
			return comm.WebUser_Init();
		}
		catch (RuntimeException ex)
		{
			//@ 多人用同一个账号登录，就需要加上如下代码.
			if (DataType.IsNullOrEmpty(this.getUserNo()) == false)
			{
				Dev2Interface.Port_GenerToken(this.getUserNo());
				Dev2Interface.Port_Login(this.getUserNo());
				WF_Comm comm = new WF_Comm();
				return comm.WebUser_Init();
			}
			return ex.getMessage();
		}
	}
	/** 
	 切换用户
	 
	 @return 
	*/
	public final String SelectOneUser_ChangUser() throws Exception {

		if (bp.difference.SystemConfig.getCCBPMRunModel() == CCBPMRunModel.Single)
		{
			String adminer = this.GetRequestVal("Adminer");
			String SID = this.GetRequestVal("Token");
			try
			{
				String token = Dev2Interface.Port_GenerToken(this.getFK_Emp(),"PC");
				bp.wf.Dev2Interface.Port_Login(this.getFK_Emp());
				return token;
			}
			catch (Exception ex)
			{
				return "err@" + ex.getMessage();
			}
		}

		try
		{
			String token = Dev2Interface.Port_GenerToken(this.getFK_Emp(),"PC");
			bp.wf.Dev2Interface.Port_Login(this.getFK_Emp(), this.getOrgNo());
			return token;
		}
		catch (Exception ex)
		{
			return "err@" + ex.getMessage();
		}


	}


		///#region TestFlow2020_Init
	/** 
	 发起.
	 
	 @return 
	*/
	public final String TestFlow2020_StartIt() throws Exception {
		//此SID是管理员的SID.

		String testerNo = this.GetRequestVal("TesterNo");
		FlowExt fl = new FlowExt(this.getFK_Flow());
		fl.setTester(testerNo);
		fl.Update();

		//选择的人员登录
		String token = bp.wf.Dev2Interface.Port_GenerToken(testerNo,"PC");
		bp.wf.Dev2Interface.Port_Login(testerNo);


		//组织url发起该流程.
		String url = "Default.html?RunModel=1&FK_Flow=" + this.getFK_Flow() + "&TesterNo=" + testerNo;
		url += "&OrgNo=" + WebUser.getOrgNo();
		url += "&UserNo=" + WebUser.getNo();
		url += "&Token=" + token;
		return url;
	}
	/** 
	 初始化
	 
	 @return 
	*/
	public final String TestFlow2020_Init() throws Exception {
		//清除缓存.
		bp.difference.SystemConfig.DoClearCash();

		if (WebUser.getIsAdmin() == false)
		{
			return "err@您不是管理员，无法执行该操作.";
		}

		FlowExt fl = new FlowExt(this.getFK_Flow());


			///#region 检查.
		int nodeid = Integer.parseInt(this.getFK_Flow() + "01");
		DataTable dt = null;
		String sql = "";
		Node nd = new Node(nodeid);
		/* if (nd.IsGuestNode)
		 {
		     *//*如果是 guest 节点，就让其跳转到 guest登录界面，让其发起流程。*//*
			 //这个地址需要配置.
			 return "url@/SDKFlowDemo/GuestApp/Login.htm?FK_Flow=" + this.getFK_Flow();
	}

			///#endregion 测试人员.

			///#region 从配置里获取-测试人员.
//C# TO JAVA CONVERTER TODO TASK: The following line could not be converted:
		 */ if (fl.getTester().length() > 2)
		 {
			// 构造人员表.
			DataTable dtEmps = new DataTable();
			dtEmps.Columns.Add("No");
			dtEmps.Columns.Add("Name");
			dtEmps.Columns.Add("FK_DeptText");
			dtEmps.Columns.Add("DeptFullName");


			String[] strs = fl.getTester().split("[,]", -1);
			for (String str : strs)
			{
				if (DataType.IsNullOrEmpty(str) == true)
				{
					continue;
				}

				Emp emp = new Emp();
				emp.SetValByKey("No", str);
				int i = emp.RetrieveFromDBSources();
				if (i == 0)
				{
					continue;
				}

				DataRow dr = dtEmps.NewRow();
				dr.setValue("No", emp.getUserID());
				dr.setValue("Name", emp.getName());
				dr.setValue("FK_DeptText", emp.getFK_DeptText());

				//dr["DeptFullName"] = ;

				dtEmps.Rows.add(dr);
			}

			if (dtEmps.Rows.size() >= 1)
			{
				return bp.tools.Json.ToJson(dtEmps);
			}
		 }

			///#endregion 测试人员.

		//fl.DoCheck();
		try
		{

				///#region 从设置里获取-测试人员.
			switch (nd.getHisDeliveryWay())
			{
				case ByStation:
				case ByStationOnly:
					if (Glo.getCCBPMRunModel() == CCBPMRunModel.Single)
					{
						sql = "SELECT Port_Emp.No  FROM Port_Emp LEFT JOIN Port_Dept ON  Port_Emp.FK_Dept=Port_Dept.No  join Port_DeptEmpStation ON (fk_emp=Port_Emp.No) join WF_NodeStation on (WF_NodeStation.fk_station=Port_DeptEmpStation.fk_station) WHERE (1=1) AND  FK_Node=" + nd.getNodeID();
					}
					else
					{
						// 查询当前组织下所有的该岗位的人员. 
						sql = "SELECT a." + bp.sys.base.Glo.getUserNo() + " as No FROM Port_Emp A, Port_DeptEmpStation B, WF_NodeStation C ";
						sql += " WHERE A.OrgNo='" + WebUser.getOrgNo() + "' AND C.FK_Node=" + nd.getNodeID();
						sql += " AND A.No=B.FK_Emp AND B.FK_Station=C.FK_Station ";
					}
					break;
				case ByTeamOrgOnly: //按照组织智能计算。
				case ByTeamDeptOnly: //按照组织智能计算。
					sql = "SELECT A." + bp.sys.base.Glo.getUserNo() + ",A.Name FROM Port_Emp A, WF_NodeTeam B, Port_TeamEmp C ";
					sql += " WHERE A." + bp.sys.base.Glo.getUserNo() + "=C.FK_Emp AND B.FK_Team=C.FK_Team AND B.FK_Node=" + nd.getNodeID() + " AND A.OrgNo='" + WebUser.getOrgNo() + "'";
					break;
				case ByTeamOnly: //仅按用户组计算.

					sql = "SELECT A." + bp.sys.base.Glo.getUserNo() + ",A.Name FROM Port_Emp A, WF_NodeTeam B, Port_TeamEmp C ";
					sql += " WHERE A.No=C.FK_Emp AND B.FK_Team=C.FK_Team AND B.FK_Node=" + nd.getNodeID();
					break;
				case ByDept:
					sql = "SELECT " + bp.sys.base.Glo.getUserNo() + ",Name FROM Port_Emp A, WF_NodeDept B WHERE A.FK_Dept=B.FK_Dept AND B.FK_Node=" + nodeid;
					break;
				case ByBindEmp:
					if (bp.difference.SystemConfig.getCCBPMRunModel() == CCBPMRunModel.SAAS)
					{
						sql = "SELECT " + bp.sys.base.Glo.getUserNo() + ",Name FROM Port_Emp WHERE " + bp.sys.base.Glo.getUserNo() + " IN (SELECT FK_Emp from WF_NodeEmp where FK_Node='" + nodeid + "') AND OrgNo='" + WebUser.getOrgNo() + "'";
					}
					else
					{
						sql = "SELECT " + bp.sys.base.Glo.getUserNo() + ",Name FROM Port_Emp WHERE " + bp.sys.base.Glo.getUserNo() + " in (SELECT FK_Emp from WF_NodeEmp where FK_Node='" + nodeid + "') ";
					}

					break;
				case ByDeptAndStation:

					sql = "SELECT pdes.FK_Emp AS No" + " FROM   Port_DeptEmpStation pdes" + "        INNER JOIN WF_NodeDept wnd" + "             ON  wnd.FK_Dept = pdes.FK_Dept" + "             AND wnd.FK_Node = " + nodeid + "        INNER JOIN WF_NodeStation wns" + "             ON  wns.FK_Station = pdes.FK_Station" + "             AND wnd.FK_Node =" + nodeid + " ORDER BY" + "        pdes.FK_Emp";

					break;
				case BySelected: //所有的人员多可以启动, 2016年11月开始约定此规则.

					if (Glo.getCCBPMRunModel() == CCBPMRunModel.Single)
					{
						sql = "SELECT A.No, A.Name, B.Name as FK_DeptText FROM  Port_Emp A, Port_Dept B WHERE A.FK_Dept=B.No";
						//sql = "SELECT c.No, c.Name, B.Name as FK_DeptText FROM Port_DeptEmp A, Port_Dept B, Port_Emp C WHERE A.FK_Dept=B.No AND A.FK_Emp=C.No";
					}
					else
					{
						sql = "SELECT c." + bp.sys.base.Glo.getUserNo() + ", c.Name, B.Name as FK_DeptText FROM Port_DeptEmp A, Port_Dept B, Port_Emp C WHERE A.FK_Dept=B.No  AND A.FK_Emp=C." + bp.sys.base.Glo.getUserNoWhitOutAS() + " ";
						sql += " AND A.OrgNo='" + WebUser.getOrgNo() + "' ";
						sql += " AND B.OrgNo='" + WebUser.getOrgNo() + "' ";
						sql += " AND C.OrgNo='" + WebUser.getOrgNo() + "' ";
					}

					break;
				case BySelectedOrgs: //按照设置的组织计算: 20202年3月开始约定此规则.

					if (Glo.getCCBPMRunModel() == CCBPMRunModel.Single)
					{
						throw new RuntimeException("err@非集团版本，不能设置启用此模式.");
					}

					sql = " SELECT A." + bp.sys.base.Glo.getUserNo() + ",A.Name,C.Name as FK_DeptText FROM Port_Emp A, WF_FlowOrg B, port_dept C ";
					sql += " WHERE A.OrgNo = B.OrgNo AND B.FlowNo = '" + this.getFK_Flow() + "' AND A.FK_Dept = c.No ";

					break;
				case BySQL:
					if (DataType.IsNullOrEmpty(nd.getDeliveryParas()))
					{
						return "err@您设置的按SQL访问开始节点，但是您没有设置sql.";
					}
					break;
				default:
					break;
			}

			dt = DBAccess.RunSQLReturnTable(sql);
			if (dt.Rows.size() == 0)
			{
				return "err@您按照:[" + nd.getHisDeliveryWay() + "]的方式设置的开始节点的访问规则，但是开始节点没有人员.";
			}

			if (dt.Rows.size() > 500)
			{
				return "err@可以发起开始节点的人员太多，会导致系统崩溃变慢，请<a href='javascript:SetTester()' >设置测试发起人</a>。";
			}

			// 构造人员表.
			DataTable dtMyEmps = new DataTable();
			dtMyEmps.Columns.Add("No");
			dtMyEmps.Columns.Add("Name");
			dtMyEmps.Columns.Add("FK_DeptText");

			//处理发起人数据.
			String emps = "";
			for (DataRow dr : dt.Rows)
			{
				String myemp = dr.getValue(0).toString();
				if (emps.contains("," + myemp + ",") == true)
				{
					continue;
				}

				emps += "," + myemp + ",";

				//查询数据。
				Emp emp = new Emp();

				if (bp.difference.SystemConfig.getCCBPMRunModel() == CCBPMRunModel.SAAS)
				{
					emp.setNo(this.getOrgNo() + "_" + myemp);
					emp.RetrieveFromDBSources();
				}
				else
				{
					emp.setNo(myemp);
					emp.RetrieveFromDBSources();
				}

				//if (emp.RetrieveFromDBSources())

				DataRow drNew = dtMyEmps.NewRow();

				drNew.setValue("No", emp.getUserID());
				drNew.setValue("Name", emp.getName());
				drNew.setValue("FK_DeptText", emp.getFK_DeptText());

				dtMyEmps.Rows.add(drNew);
			}


			if (bp.difference.SystemConfig.AppCenterDBFieldCaseModel() == FieldCaseModel.UpperCase)
			{
				dtMyEmps.Columns.get("NO").ColumnName = "No";
				dtMyEmps.Columns.get("NAME").ColumnName = "Name";
				dtMyEmps.Columns.get("FK_DEPTTEXT").ColumnName = "FK_DeptText";
			}

			if (bp.difference.SystemConfig.AppCenterDBFieldCaseModel() == FieldCaseModel.Lowercase)
			{
				dtMyEmps.Columns.get("no").ColumnName = "No";
				dtMyEmps.Columns.get("name").ColumnName = "Name";
				dtMyEmps.Columns.get("fk_depttext").ColumnName = "FK_DeptText";
			}



			//返回数据源.
			return bp.tools.Json.ToJson(dtMyEmps);

				///#endregion 从设置里获取-测试人员.

		}
		catch (RuntimeException ex)
		{
			return "err@您没有正确的设置开始节点的访问规则，这样导致没有可启动的人员，方法：TestFlow2020_Init。系统错误提示:" + ex.getMessage();
		}
}

		///#endregion

}