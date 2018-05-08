package BP.WF;

import BP.DA.*;
import BP.WF.Template.*;
import BP.Web.*;
import BP.Port.*;
import BP.Sys.OSModel;
import BP.Tools.DateUtils;

/** 
 计算未来处理人
 
*/
public class FullSA
{
	/** 
	 工作Node.
	 
	*/
	public WorkNode HisCurrWorkNode = null;
	/** 
	 自动计算未来处理人（该方法在发送成功后执行.）
	 
	 @param CurrWorkNode 当前的节点
	 @param nd
	 @param toND
	 * @throws Exception 
	*/
	public FullSA(WorkNode currWorkNode) throws Exception
	{
		//如果当前不需要计算未来处理人.
		if (currWorkNode.getHisFlow().getIsFullSA() == false && currWorkNode.IsSkip==false)
		{
			return;
		}

		//如果到达最后一个节点，就不处理了。
		if (currWorkNode.getHisNode().getIsEndNode())
		{
			return;
		}

		//初始化一些变量.
		this.HisCurrWorkNode = currWorkNode;
		Node currND = currWorkNode.getHisNode();
		long workid = currWorkNode.getHisWork().getOID();

		//查询出来所有的节点.
		Nodes nds = new Nodes(this.HisCurrWorkNode.getHisFlow().getNo());

		// 开始节点需要特殊处理》
		// 如果启用了要计算未来的处理人 
		SelectAccper sa = new SelectAccper();

		//首先要清除以前的计算，重新计算。
		sa.Delete(SelectAccperAttr.WorkID, workid);

		//求出已经路过的节点.
		DataTable dt = DBAccess.RunSQLReturnTable("SELECT FK_Node FROM WF_GenerWorkerList WHERE WorkID="+BP.Sys.SystemConfig.getAppCenterDBVarStr()+"WorkID", "WorkID", workid);
		String passedNodeIDs = "";
		for (DataRow item : dt.Rows)
		{
			passedNodeIDs += item.getValue(0).toString() + ",";
		}

		//遍历当前的节点。
		for (Node item : nds.ToJavaList())
		{
			if (item.getIsStartNode() == true)
			{
				continue;
			}

			//如果已经包含了，就说明该节点已经经过了，就不处理了。
			if (passedNodeIDs.contains(item.getNodeID() + ",") == true)
			{
				continue;
			}

			//如果按照岗位计算（默认的第一个规则.）
			if (item.getHisDeliveryWay() == DeliveryWay.ByStation)
			{
				String sql = "SELECT No, Name FROM Port_Emp WHERE No IN (SELECT A.FK_Emp FROM " + BP.WF.Glo.getEmpStation() + " A, WF_NodeStation B WHERE A.FK_Station=B.FK_Station AND B.FK_Node=" + item.getNodeID() + ")";
				dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
				if (dt.Rows.size() != 1)
				{
					continue;
				}

				String no = dt.Rows.get(0).getValue(0).toString();
				String name = dt.Rows.get(0).getValue(1).toString();
				sa.setFK_Emp(no);
				sa.setEmpName(name);
				sa.setFK_Node(item.getNodeID());
				sa.setWorkID(workid);
				sa.setInfo("无");
				sa.setAccType(0);
				sa.ResetPK();
				if (sa.getIsExits())
				{
					continue;
				}

				//计算接受任务时间与应该完成任务时间.
				InitDT(sa, item);

				sa.Insert();
				continue;
			}

			//处理与指定节点相同的人员.
			if (item.getHisDeliveryWay() == DeliveryWay.BySpecNodeEmp && (new Integer(currND.getNodeID())).toString().equals(item.getDeliveryParas()))
			{

				sa.setFK_Emp(WebUser.getNo());
				sa.setFK_Node(item.getNodeID());
				sa.setWorkID(workid);
				sa.setInfo("无");
				sa.setAccType(0);
				sa.setEmpName(WebUser.getName());

				sa.ResetPK();
				if (sa.getIsExits())
				{
					continue;
				}

				//计算接受任务时间与应该完成任务时间.
				InitDT(sa, item);

				sa.Insert();
				continue;
			}

			//处理绑定的节点人员..
			if (item.getHisDeliveryWay() == DeliveryWay.ByBindEmp)
			{
				NodeEmps nes = new NodeEmps();
				nes.Retrieve(NodeEmpAttr.FK_Node, item.getNodeID());
				for (NodeEmp ne : nes.ToJavaList())
				{
					sa.setFK_Emp(ne.getFK_Emp());
					sa.setFK_Node(item.getNodeID());
					sa.setWorkID(workid);
					sa.setInfo("无");
					sa.setAccType(0);
					sa.setEmpName(ne.getFK_EmpT());

					sa.ResetPK();
					if (sa.getIsExits())
					{
						continue;
					}

					//计算接受任务时间与应该完成任务时间.
					InitDT(sa, item);

					sa.Insert();
				}
			}
				///#region 按部门与岗位的交集计算.
			if (item.getHisDeliveryWay() == DeliveryWay.ByDeptAndStation)
			{
				String dbStr = BP.Sys.SystemConfig.getAppCenterDBVarStr();
				String sql = "";
				//区别集成与BPM模式
				if (BP.WF.Glo.getOSModel() == BP.Sys.OSModel.OneOne)
				{
					sql = "SELECT No FROM Port_Emp WHERE No IN ";
					sql += "(SELECT No as FK_Emp FROM Port_Emp WHERE FK_Dept IN ";
					sql += "( SELECT FK_Dept FROM WF_NodeDept WHERE FK_Node=" + dbStr + "FK_Node1)";
					sql += ")";
					sql += "AND No IN ";
					sql += "(";
					sql += "SELECT FK_Emp FROM " + BP.WF.Glo.getEmpStation() + " WHERE FK_Station IN ";
					sql += "( SELECT FK_Station FROM WF_NodeStation WHERE FK_Node=" + dbStr + "FK_Node1 )";
					sql += ") ORDER BY No ";

					Paras ps = new Paras();
					ps.Add("FK_Node1", item.getNodeID());
					ps.Add("FK_Node2", item.getNodeID());
					ps.SQL = sql;
					dt = DBAccess.RunSQLReturnTable(ps);
				}
				else
				{
					sql = "SELECT pdes.FK_Emp AS No" + " FROM   Port_DeptEmpStation pdes" + "        INNER JOIN WF_NodeDept wnd" + "             ON  wnd.FK_Dept = pdes.FK_Dept" + "             AND wnd.FK_Node = " + item.getNodeID() + "        INNER JOIN WF_NodeStation wns" + "             ON  wns.FK_Station = pdes.FK_Station" + "             AND wnd.FK_Node =" + item.getNodeID() + " ORDER BY" + "        pdes.FK_Emp";

					dt = DBAccess.RunSQLReturnTable(sql);
				}

				for (DataRow dr : dt.Rows)
				{
					Emp emp = new Emp(dr.getValue(0).toString());
					sa.setFK_Emp(emp.getNo());
					sa.setFK_Node(item.getNodeID());
					sa.setWorkID(workid);
					sa.setInfo("无");
					sa.setAccType(0);
					sa.setEmpName(emp.getName());

					sa.ResetPK();
					if (sa.getIsExits())
					{
						continue;
					}

					//计算接受任务时间与应该完成任务时间.
					InitDT(sa, item);

					sa.Insert();
				}
			}
		}

		//预制当前节点到达节点的数据。
		Nodes toNDs = currND.getHisToNodes();
		for (Node item : toNDs.ToJavaList())
		{
			if (item.getHisDeliveryWay() == DeliveryWay.ByStation)
			{
				//如果按照岗位访问

					///#region 最后判断 - 按照岗位来执行。
				String dbStr = BP.Sys.SystemConfig.getAppCenterDBVarStr();
				String sql = "";
				Paras ps = new Paras();
				// 如果执行节点 与 接受节点岗位集合不一致 
				// 没有查询到的情况下, 先按照本部门计算。
				if (this.HisCurrWorkNode.getHisFlow().getFlowAppType() == FlowAppType.Normal)
				{
					switch (BP.Sys.SystemConfig.getAppCenterDBType())
					{
						case MySQL:
						case MSSQL:
							sql = "select X.No from Port_Emp x inner join (select FK_Emp from " + BP.WF.Glo.getEmpStation() + " a inner join WF_NodeStation b ";
							sql += " on a.FK_Station=b.FK_Station where FK_Node=" + dbStr + "FK_Node) as y on x.No=y.FK_Emp inner join Port_Emp z on";
							sql += " x.No=z.No where z.FK_Dept =" + dbStr + "FK_Dept order by x.No";
							break;
						default:
							sql = "SELECT No FROM Port_Emp WHERE NO IN " + "(SELECT  FK_Emp  FROM " + BP.WF.Glo.getEmpStation() + " WHERE FK_Station IN (SELECT FK_Station FROM WF_NodeStation WHERE FK_Node=" + dbStr + "FK_Node) )" + " AND  NO IN " + "(SELECT No FROM Port_Emp WHERE FK_Dept =" + dbStr + "FK_Dept)";
							sql += " ORDER BY No ";
							break;
					}

					ps = new Paras();
					ps.SQL = sql;
					ps.Add("FK_Node", item.getNodeID());
					ps.Add("FK_Dept", WebUser.getFK_Dept());
				}

				dt = DBAccess.RunSQLReturnTable(ps);
				for (DataRow dr : dt.Rows)
				{
					Emp emp = new Emp(dr.getValue(0).toString());
					sa.setFK_Emp(emp.getNo());
					sa.setFK_Node(item.getNodeID());
					sa.setWorkID(workid);
					sa.setInfo("无");
					sa.setAccType(0);
					sa.setEmpName(emp.getName());

					sa.ResetPK();
					if (sa.getIsExits())
					{
						continue;
					}

					//计算接受任务时间与应该完成任务时间.
					InitDT(sa, item);

					sa.Insert();
				}

			}
		}
	}
	/** 
	 计算两个时间点.
	 @param sa
	 @param nd
	 * @throws Exception 
	*/
	private void InitDT(SelectAccper sa, Node nd) throws Exception
	{
		//计算上一个时间的发送点.
		if (this.LastTimeDot == null)
		{
			Paras ps = new Paras();
			ps.SQL="SELECT SDT FROM WF_GenerWorkerlist WHERE WorkID="+ps.getDBStr()+"WorkID AND FK_Node="+ps.getDBStr()+"FK_Node";
			ps.Add("WorkID", this.HisCurrWorkNode.getWorkID());
			ps.Add("FK_Node", nd.getNodeID());
			DataTable dt=DBAccess.RunSQLReturnTable(ps);

			for (DataRow dr : dt.Rows)
			{
				this.LastTimeDot = dr.getValue(0).toString();
				break;
			}
		}

		//上一个节点的发送时间点或者 到期的时间点，就是当前节点的接受任务的时间。
		sa.setPlanADT(this.LastTimeDot);
		//计算当前节点的应该完成日期。
		java.util.Date dtOfShould;
		dtOfShould = Glo.AddDayHoursSpan(this.LastTimeDot, nd.getTimeLimit(), nd.getTimeLimitHH(), nd.getTimeLimitMM(),nd.getTWay());
		sa.setPlanSDT(DateUtils.format(dtOfShould,DataType.getSysDatatimeFormatCN()));
		//给最后的时间点复制.
		this.LastTimeDot = sa.getPlanSDT();

	}
	/** 
	 当前节点应该完成的日期.
	*/
	private String LastTimeDot = null;
}