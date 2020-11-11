package bp.wf.httphandler;

import bp.da.*;
import bp.difference.SystemConfig;
import bp.difference.handler.WebContralBase;
import bp.sys.*;
import bp.web.*;
import bp.port.*;
import bp.en.*;
import bp.wf.*;
import bp.wf.Glo;
import bp.wf.template.*;

/** 
 页面功能实体
*/
public class WF_Admin_TestingContainer extends WebContralBase
{
	/** 
	 构造函数
	*/
	public WF_Admin_TestingContainer()
	{
	}

	/** 
	 左侧的树刷新
	 
	 @return 
	*/
	public final String Left_Init()
	{
		return "";
	}
	/** 
	 测试页面初始化
	 
	 @return 
	 * @throws Exception 
	*/
	public final String Default_Init() throws Exception
	{
		String userNo = this.GetRequestVal("UserNo");
		if (WebUser.getNo().equals(userNo) == false)
		{
			bp.wf.Dev2Interface.Port_Login(userNo);
		}

		long workid = bp.wf.Dev2Interface.Node_CreateBlankWork(this.getFK_Flow(), userNo);
		return String.valueOf(workid);
	}


	/** 
	 数据库信息
	 
	 @return 
	 * @throws Exception 
	*/
	public final String DBInfo_Init() throws Exception
	{
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
		if (SystemConfig.getAppCenterDBType() == DBType.Oracle)
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
		en.setPKVal(this.getWorkID());
		en.RetrieveFromDBSources();
		ds.Tables.add(en.ToDataTableField("NDRpt"));

		//转化为json ,返回出去.
		return bp.tools.Json.ToJson(ds);
	}
	/** 
	 SelectOneUser_Init
	 
	 @return 
	 * @throws Exception 
	*/
	public final String SelectOneUser_Init() throws Exception
	{

		bp.wf.GenerWorkerLists ens = new GenerWorkerLists();
		QueryObject qo = new QueryObject(ens);
		qo.AddWhere("WorkID", this.getWorkID());
		qo.addOr();
		qo.AddWhere("FID", this.getWorkID());
		qo.addOrderBy(" RDT,CDT ");
		qo.DoQuery();

		return ens.ToJson();
	}

	/** 
	 让adminer登录.
	 
	 @return 
	 * @throws Exception 
	*/
	public final String Default_LetAdminerLogin() throws Exception
	{
		String SID = this.GetRequestVal("SID");
		bp.wf.Dev2Interface.Port_LoginBySID(SID);

		return "登录成功.";
		
	}
	/** 
	 切换用户
	 
	 @return 
	 * @throws Exception 
	*/
	public final String SelectOneUser_ChangUser() throws Exception
	{

		String adminer = this.GetRequestVal("Adminer");
		String SID = this.GetRequestVal("SID");

		try
		{
			bp.wf.Dev2Interface.Port_Login(this.getFK_Emp());
			return "登录成功.";
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
	}


	/** 
	 发起.
	 
	 @return 
	 * @throws Exception 
	*/
	public final String TestFlow2020_StartIt() throws Exception
	{
		String sid = this.GetRequestVal("SID");
	
		FlowExt fl = new FlowExt(this.getFK_Flow());
		fl.setTester(this.GetRequestVal("UserNo"));
		fl.Update();

		//用户编号.
		String userNo = this.GetRequestVal("UserNo");

		//判断是否可以测试该流程？ 
		bp.port.Emp myEmp = new bp.port.Emp();
		int i = myEmp.Retrieve("SID", sid);
		
		//组织url发起该流程.
		String url = "Default.html?RunModel=1&FK_Flow=" + this.getFK_Flow() + "&SID=" + sid + "&UserNo=" + userNo;
		return url;
	}
	/** 
	 初始化
	 
	 @return 
	 * @throws Exception 
	*/
	public final String TestFlow2020_Init() throws Exception
	{
		//清除缓存.
		SystemConfig.DoClearCash();

		if (WebUser.getIsAdmin() == false)
		{
			return "err@您不是管理员，无法执行该操作.";
		}

		FlowExt fl = new FlowExt(this.getFK_Flow());

		if (1 == 2 && WebUser.getNo().equals("admin") && fl.getTester().length() <= 1)
		{
			String msg = "err@二级管理员[" + WebUser.getName() + "]您好,您尚未为该流程配置测试人员.";
			msg += "您需要在流程属性里的底部[设置流程发起测试人]的属性里，设置可以发起的测试人员,多个人员用逗号分开.";
			return msg;
		}


			///检查.
		int nodeid = Integer.parseInt(this.getFK_Flow() + "01");
		DataTable dt = null;
		String sql = "";
		bp.wf.Node nd = new bp.wf.Node(nodeid);
		/* if (nd.IsGuestNode)
		 {
		     *//*如果是 guest 节点，就让其跳转到 guest登录界面，让其发起流程。*//*
			 //这个地址需要配置.
			 return "url@/SDKFlowDemo/GuestApp/Login.htm?FK_Flow=" + this.getFK_Flow();
	}

			/// 测试人员.

			///从配置里获取-测试人员.
		 */ if (fl.getTester().length() > 2)
		 {
			// 构造人员表.
			DataTable dtEmps = new DataTable();
			dtEmps.Columns.Add("No");
			dtEmps.Columns.Add("Name");
			dtEmps.Columns.Add("FK_DeptText");

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
				dr.setValue("No", emp.getNo());
				dr.setValue("Name", emp.getName());
				dr.setValue("FK_DeptText", emp.getFK_DeptText());
				dtEmps.Rows.add(dr);
			}

			if (dtEmps.Rows.size() > 1)
			{
				return bp.tools.Json.ToJson(dtEmps);
			}
		 }

			/// 测试人员.

		//fl.DoCheck();

		try
		{

				///从设置里获取-测试人员.
			switch (nd.getHisDeliveryWay())
			{
				case ByStation:
				case ByStationOnly:
					if (Glo.getCCBPMRunModel() == CCBPMRunModel.Single)
					{
						sql = "SELECT Port_Emp.No  FROM Port_Emp LEFT JOIN Port_Dept   Port_Dept_FK_Dept ON  Port_Emp.FK_Dept=Port_Dept_FK_Dept.No  join Port_DeptEmpStation on (fk_emp=Port_Emp.getNo()) join WF_NodeStation on (WF_NodeStation.fk_station=Port_DeptEmpStation.fk_station) WHERE (1=1) AND  FK_Node=" + nd.getNodeID();
					}
					else
					{
						sql = "SELECT Port_Emp.No FROM Port_Emp WHERE OrgNo='" + WebUser.getOrgNo() + "' LEFT JOIN Port_Dept   Port_Dept_FK_Dept ON  Port_Emp.FK_Dept=Port_Dept_FK_Dept.No  join Port_DeptEmpStation on (fk_emp=Port_Emp.getNo()) join WF_NodeStation on (WF_NodeStation.fk_station=Port_DeptEmpStation.fk_station) WHERE (1=1) AND  FK_Node=" + nd.getNodeID();
					}

					// emps.RetrieveInSQL_Order("select fk_emp from Port_Empstation WHERE fk_station in (select fk_station from WF_NodeStation WHERE FK_Node=" + nodeid + " )", "FK_Dept");
					break;
				case ByTeamOrgOnly: //按照组织智能计算。
				case ByTeamDeptOnly: //按照组织智能计算。
					sql = "SELECT A.No,A.Name FROM Port_Emp A, WF_NodeTeam B, Port_TeamEmp C ";
					sql += " WHERE A.No=C.FK_Emp AND B.FK_Team=C.FK_Team AND B.FK_Node=" + nd.getNodeID() + " AND A.OrgNo='" + WebUser.getOrgNo() + "'";
					break;
				case ByTeamOnly: //仅按用户组计算. @lizhen.

					sql = "SELECT A.No,A.Name FROM Port_Emp A, WF_NodeTeam B, Port_TeamEmp C ";
					sql += " WHERE A.No=C.FK_Emp AND B.FK_Team=C.FK_Team AND B.FK_Node=" + nd.getNodeID();
					break;
				case ByDept:
					sql = "SELECT No,Name FROM Port_Emp A, WF_NodeDept B WHERE A.FK_Dept=B.FK_Dept AND B.FK_Node=" + nodeid;
					break;
				case ByBindEmp:
					sql = "SELECT No,Name from Port_Emp WHERE No in (select FK_Emp from WF_NodeEmp where FK_Node='" + nodeid + "') ";
					//emps.RetrieveInSQL("select fk_emp from wf_NodeEmp WHERE fk_node=" + int.Parse(this.FK_Flow + "01") + " ");
					break;
				case ByDeptAndStation:

					sql = "SELECT pdes.FK_Emp AS No" + " FROM   Port_DeptEmpStation pdes" + "        INNER JOIN WF_NodeDept wnd" + "             ON  wnd.FK_Dept = pdes.FK_Dept" + "             AND wnd.FK_Node = " + nodeid + "        INNER JOIN WF_NodeStation wns" + "             ON  wns.FK_Station = pdes.FK_Station" + "             AND wnd.FK_Node =" + nodeid + " ORDER BY" + "        pdes.FK_Emp";

					break;
				case BySelected: //所有的人员多可以启动, 2016年11月开始约定此规则.

					if (Glo.getCCBPMRunModel() == CCBPMRunModel.Single)
					{
						sql = "SELECT c.No, c.Name, B.Name as FK_DeptText FROM Port_DeptEmp A, Port_Dept B, Port_Emp C WHERE A.FK_Dept=B.No AND A.FK_Emp=C.No ";
					}
					else
					{
						sql = "SELECT c.No, c.Name, B.Name as FK_DeptText FROM Port_DeptEmp A, Port_Dept B, Port_Emp C WHERE A.FK_Dept=B.No AND B.OrgNo='" + WebUser.getOrgNo() + "' AND A.FK_Emp=C.No ";
					}

					break;
				case BySelectedOrgs: //按照设置的组织计算: 20202年3月开始约定此规则.

					if (Glo.getCCBPMRunModel() == CCBPMRunModel.Single)
					{
						throw new RuntimeException("err@非集团版本，不能设置启用此模式.");
					}

					sql = " SELECT A.No,A.Name,C.Name as FK_DeptText FROM Port_Emp A, WF_FlowOrg B, port_dept C ";
					sql += " WHERE A.OrgNo=B.OrgNo AND B.FlowNo = '" + this.getFK_Flow() + "' AND A.FK_Dept = c.No ";

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
				return "err@您按照:" + nd.getHisDeliveryWay() + "的方式设置的开始节点的访问规则，但是开始节点没有人员。";
			}

			if (dt.Rows.size() > 2000)
			{
				return "err@可以发起开始节点的人员太多，会导致系统崩溃变慢，您需要在流程属性里设置可以发起的测试用户.";
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

				bp.port.Emp emp = new Emp(myemp);

				DataRow drNew = dtMyEmps.NewRow();

				drNew.setValue("No", emp.getNo());
				drNew.setValue("Name", emp.getName());
				drNew.setValue("FK_DeptText", emp.getFK_DeptText());

				dtMyEmps.Rows.add(drNew);
			}

			if (SystemConfig.getAppCenterDBType() == DBType.Oracle)
			{
				dtMyEmps.Columns.get("NO").setColumnName("No");
				dtMyEmps.Columns.get("NAME").setColumnName("Name");
				dtMyEmps.Columns.get("FK_DEPTTEXT").setColumnName("FK_DeptText");
			}

			if (SystemConfig.getAppCenterDBType() == DBType.PostgreSQL)
			{
				dtMyEmps.Columns.get("no").setColumnName("No");
				dtMyEmps.Columns.get("name").setColumnName("Name");
				dtMyEmps.Columns.get("fk_depttext").setColumnName("FK_DeptText");
			}

			//返回数据源.
			return bp.tools.Json.ToJson(dtMyEmps);

				/// 从设置里获取-测试人员.

		}
		catch (RuntimeException ex)
		{
			return "err@您没有正确的设置开始节点的访问规则，这样导致没有可启动的人员 系统错误提示:" + ex.getMessage();
		}
}

		///

}