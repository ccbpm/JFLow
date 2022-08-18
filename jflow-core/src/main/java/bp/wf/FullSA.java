package bp.wf;

import bp.da.*;
import bp.wf.template.*;
import bp.web.*;
import bp.port.*;
import java.util.Date;

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
	 
	 param CurrWorkNode 当前的节点
	 param nd
	 param toND
	*/
	public FullSA(WorkNode currWorkNode) throws Exception {
		//如果当前不需要计算未来处理人.
		if (currWorkNode.getHisFlow().isFullSA() == false && currWorkNode.IsSkip == false)
		{
			return;
		}

		//如果到达最后一个节点，就不处理了。
		if (currWorkNode.getHisNode().isEndNode())
		{
			return;
		}

		//初始化一些变量.
		this.HisCurrWorkNode = currWorkNode;
		Node currND = currWorkNode.getHisNode();
		long workid = currWorkNode.getHisWork().getOID();

		//查询出来所有的节点.
		Nodes nds = currWorkNode.getHisFlow().getHisNodes();

		// 开始节点需要特殊处理》
		/* 如果启用了要计算未来的处理人 */
		SelectAccper sa = new SelectAccper();

		Node toNode = currWorkNode.JumpToNode;
		//首先要清除以前的计算，重新计算。
		if (toNode != null)
		{
			DBAccess.RunSQL("Delete From WF_SelectAccper Where WorkID=" + workid + " AND FK_Node !=" + toNode.getNodeID());
		}
		else
		{
			DBAccess.RunSQL("Delete From WF_SelectAccper Where WorkID=" + workid);
		}

		//求出已经路过的节点.
		DataTable dt = DBAccess.RunSQLReturnTable("SELECT FK_Node FROM WF_GenerWorkerList WHERE WorkID=" + bp.difference.SystemConfig.getAppCenterDBVarStr() + "WorkID", "WorkID", workid);
		String passedNodeIDs = "";
		for (DataRow item : dt.Rows)
		{
			passedNodeIDs += item.get(0).toString() + ",";
		}

		//遍历当前的节点。
		for (Node item : nds.ToJavaList())
		{
			if (item.isStartNode() == true)
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
				// string sql = "SELECT No, Name FROM Port_Emp WHERE No IN (SELECT A.FK_Emp FROM " + BP.WF.Glo.EmpStation + " A, WF_NodeStation B WHERE A.FK_Station=B.FK_Station AND B.FK_Node=" + item.NodeID + ")";

				String sql = "SELECT DISTINCT a." + bp.sys.base.Glo.getUserNo() + ", a.Name FROM Port_Emp A, Port_DeptEmpStation B, WF_NodeStation C "; // WHERE No IN (SELECT A.FK_Emp FROM " + BP.WF.Glo.EmpStation + " A, WF_NodeStation B WHERE A.FK_Station=B.FK_Station AND B.FK_Node=" + item.NodeID + ")";
				sql += " WHERE A." + bp.sys.base.Glo.getUserNoWhitOutAS() + "=B.FK_Emp AND B.FK_Station=C.FK_Station AND C.FK_Node=" + item.getNodeID();

				dt = DBAccess.RunSQLReturnTable(sql);
				if (dt.Rows.size() == 0)
				{
					continue;
				}
				for (DataRow dr : dt.Rows)
				{
					String no = dr.getValue(0).toString();
					String name = dr.getValue(1).toString();
					sa = new SelectAccper();
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
				}
				continue;
			}

			//按照绑定的部门计算
			if (item.getHisDeliveryWay() == DeliveryWay.ByDept)
			{
				String dbStr = bp.difference.SystemConfig.getAppCenterDBVarStr();
				Paras ps = new Paras();
				ps.Add("FK_Node", item.getNodeID());
				ps.Add("WorkID", currWorkNode.getHisWork().getOID());
				ps.SQL = "SELECT DISTINCT FK_Emp,IDX FROM WF_SelectAccper WHERE FK_Node=" + dbStr + "FK_Node AND WorkID=" + dbStr + "WorkID AND AccType=0 ORDER BY IDX";
				dt = DBAccess.RunSQLReturnTable(ps);
				continue;
			}


				///#region 仅按组织计算
			if (item.getHisDeliveryWay() == DeliveryWay.ByTeamOnly)
			{
				String sql = "SELECT DISTINCT c." + bp.sys.base.Glo.getUserNo() + ",c.Name FROM Port_TeamEmp A, WF_NodeTeam B, Port_Emp C WHERE A.FK_Emp=C." + bp.sys.base.Glo.getUserNoWhitOutAS() + " AND A.FK_Team=B.FK_Team AND B.FK_Node=" + bp.difference.SystemConfig.getAppCenterDBVarStr() + "FK_Node ORDER BY C." + bp.sys.base.Glo.getUserNoWhitOutAS();
				Paras ps = new Paras();
				ps.Add("FK_Node", item.getNodeID());
				ps.SQL = sql;
				dt = DBAccess.RunSQLReturnTable(ps);
				if (dt.Rows.size() == 0)
				{
					throw new RuntimeException("err@节点绑定的仅按照 用户组 计算，没有找到人员:" + item.getName() + " SQL=" + ps.getSQLNoPara());
				}
				for (DataRow dr : dt.Rows)
				{
					String no = dr.getValue(0).toString();
					String name = dr.getValue(1).toString();
					sa = new SelectAccper();
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
				}
			}

				///#endregion


				///#region 本组织计算
			if (item.getHisDeliveryWay() == DeliveryWay.ByTeamOrgOnly)
			{
				String sql = "SELECT DISTINCT c." + bp.sys.base.Glo.getUserNo() + ",c.Name FROM Port_TeamEmp A, WF_NodeTeam B, Port_Emp C WHERE A.FK_Emp=C." + bp.sys.base.Glo.getUserNoWhitOutAS() + " AND A.FK_Team=B.FK_Team AND B.FK_Node=" + bp.difference.SystemConfig.getAppCenterDBVarStr() + "FK_Node AND C.OrgNo=" + bp.difference.SystemConfig.getAppCenterDBVarStr() + "OrgNo ORDER BY C." + bp.sys.base.Glo.getUserNoWhitOutAS();
				Paras ps = new Paras();
				ps.Add("FK_Node", item.getNodeID());
				ps.Add("OrgNo", WebUser.getOrgNo(), false);

				ps.SQL = sql;
				dt = DBAccess.RunSQLReturnTable(ps);
				if (dt.Rows.size() == 0)
				{
					throw new RuntimeException("err@节点绑定的仅按照 用户组 ByTeamOrgOnly，没有找到人员:" + item.getName() + " SQL=" + ps.getSQLNoPara());
				}
				for (DataRow dr : dt.Rows)
				{
					String no = dr.getValue(0).toString();
					String name = dr.getValue(1).toString();
					sa = new SelectAccper();
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
				}
			}

				///#endregion


				///#region 本组织计算
			if (item.getHisDeliveryWay() == DeliveryWay.ByTeamDeptOnly)
			{
				String sql = "SELECT DISTINCT A.FK_Emp AS No,c.Name FROM Port_TeamEmp A, WF_NodeTeam B, Port_Emp C WHERE A.FK_Emp=C.No AND A.FK_Team=B.FK_Team AND B.FK_Node=" + bp.difference.SystemConfig.getAppCenterDBVarStr() + "FK_Node AND C.FK_Dept=" + bp.difference.SystemConfig.getAppCenterDBVarStr() + "FK_Dept ORDER BY A.FK_Emp";
				Paras ps = new Paras();
				ps.Add("FK_Node", item.getNodeID());
				ps.Add("FK_Dept", WebUser.getFK_Dept(), false);

				ps.SQL = sql;
				dt = DBAccess.RunSQLReturnTable(ps);
				if (dt.Rows.size() == 0)
				{
					throw new RuntimeException("err@节点绑定的仅按照 用户组 ByTeamDeptOnly，没有找到人员:" + item.getName() + " SQL=" + ps.getSQLNoPara());
				}
				for (DataRow dr : dt.Rows)
				{
					String no = dr.getValue(0).toString();
					String name = dr.getValue(1).toString();
					sa = new SelectAccper();
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
				}
			}

				///#endregion


				///#region 2019-09-25 byzhoupeng, 仅按岗位计算
			if (item.getHisDeliveryWay() == DeliveryWay.ByStationOnly)
			{
			   String sql = "SELECT DISTINCT c." + bp.sys.base.Glo.getUserNo() + ",c.Name FROM Port_DeptEmpStation A, WF_NodeStation B, Port_Emp C WHERE A.FK_Emp=C." + bp.sys.base.Glo.getUserNoWhitOutAS() + " AND A.FK_Station=B.FK_Station AND B.FK_Node=" + bp.difference.SystemConfig.getAppCenterDBVarStr() + "FK_Node ORDER BY C." + bp.sys.base.Glo.getUserNoWhitOutAS();
				Paras ps = new Paras();
				ps.Add("FK_Node", item.getNodeID());
				ps.SQL = sql;
				dt = DBAccess.RunSQLReturnTable(ps);
				if (dt.Rows.size() == 0)
				{
					throw new RuntimeException("err@节点绑定的仅按照岗位计算，没有找到人员:" + item.getName() + " SQL=" + ps.getSQLNoPara());
				}
				for (DataRow dr : dt.Rows)
				{
					String no = dr.getValue(0).toString();
					String name = dr.getValue(1).toString();
					sa = new SelectAccper();
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
				}

			}

				///#endregion

			//处理与指定节点相同的人员.
			if (item.getHisDeliveryWay() == DeliveryWay.BySpecNodeEmp && String.valueOf(currND.getNodeID()).equals(item.getDeliveryParas()))
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
				nes.Retrieve(NodeEmpAttr.FK_Node, item.getNodeID(), null);
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

			//按照节点的 岗位与部门的交集计算.

				///#region 按部门与岗位的交集计算.
			if (item.getHisDeliveryWay() == DeliveryWay.ByDeptAndStation)
			{
				String dbStr = bp.difference.SystemConfig.getAppCenterDBVarStr();
				String sql = "";

				//added by liuxc,2015.6.30.
				//区别集成与BPM模式

					sql = "SELECT DISTINCT pdes.FK_Emp AS No" + " FROM   Port_DeptEmpStation pdes" + "        INNER JOIN WF_NodeDept wnd" + "             ON  wnd.FK_Dept = pdes.FK_Dept" + "             AND wnd.FK_Node = " + item.getNodeID() + "        INNER JOIN WF_NodeStation wns" + "             ON  wns.FK_Station = pdes.FK_Station" + "             AND wnd.FK_Node =" + item.getNodeID() + " ORDER BY" + "        pdes.FK_Emp";

					dt = DBAccess.RunSQLReturnTable(sql);


				for (DataRow dr : dt.Rows)
				{
					Emp emp = new Emp(dr.getValue(0).toString());
					sa.setFK_Emp(emp.getUserID());
					sa.setFK_Node(item.getNodeID());
					sa.setDeptName(emp.getFK_DeptText());

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

				///#endregion 按部门与岗位的交集计算.
		}

		//预制当前节点到达节点的数据。
		Nodes toNDs = currND.getHisToNodes();
		for (Node item : toNDs.ToJavaList())
		{
			if (item.getHisDeliveryWay() == DeliveryWay.ByStation || item.getHisDeliveryWay() == DeliveryWay.FindSpecDeptEmpsInStationlist)
			{
				/*如果按照岗位访问*/

					///#region 最后判断 - 按照岗位来执行。
				String dbStr = bp.difference.SystemConfig.getAppCenterDBVarStr();
				String sql = "";
				Paras ps = new Paras();
				/* 如果执行节点 与 接受节点岗位集合不一致 */
				/* 没有查询到的情况下, 先按照本部门计算。*/

				switch (bp.difference.SystemConfig.getAppCenterDBType( ))
				{
					case MySQL:
					case MSSQL:
						sql = "select DISTINCT x." + bp.sys.base.Glo.getUserNo() + " from Port_Emp x inner join (select FK_Emp from Port_DeptEmpStation a inner join WF_NodeStation b ";
						sql += " on a.FK_Station=b.FK_Station where FK_Node=" + dbStr + "FK_Node) as y on x." + bp.sys.base.Glo.getUserNoWhitOutAS() + "=y.FK_Emp inner join Port_DeptEmp z on";
						sql += " x." + bp.sys.base.Glo.getUserNoWhitOutAS() + "=z.FK_Emp where z.FK_Dept =" + dbStr + "FK_Dept order by x." + bp.sys.base.Glo.getUserNoWhitOutAS();
						break;
					default:
						sql = "SELECT DISTINCT " + bp.sys.base.Glo.getUserNo() + " FROM Port_Emp WHERE " + bp.sys.base.Glo.getUserNoWhitOutAS() + " IN " + "(SELECT  FK_Emp  FROM Port_DeptEmpStation WHERE FK_Station IN (SELECT FK_Station FROM WF_NodeStation WHERE FK_Node=" + dbStr + "FK_Node) )" + " AND  " + bp.sys.base.Glo.getUserNoWhitOutAS() + " IN " + "(SELECT  FK_Emp  FROM Port_DeptEmp WHERE FK_Dept =" + dbStr + "FK_Dept)";
						sql += " ORDER BY  " + bp.sys.base.Glo.getUserNoWhitOutAS();
						break;
				}

				ps = new Paras();
				ps.SQL = sql;
				ps.Add("FK_Node", item.getNodeID());
				ps.Add("FK_Dept", WebUser.getFK_Dept(), false);

				dt = DBAccess.RunSQLReturnTable(ps);
				for (DataRow dr : dt.Rows)
				{
					Emp emp = new Emp(dr.getValue(0).toString());
					sa.setFK_Emp(emp.getUserID());
					sa.setFK_Node(item.getNodeID());
					sa.setDeptName(emp.getFK_DeptText());

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

					///#endregion  按照岗位来执行。
			}
		}
	}
	/** 
	 计算两个时间点.
	 
	 param sa
	 param nd
	*/
	private void InitDT(SelectAccper sa, Node nd) throws Exception {
		//计算上一个时间的发送点.
		if (this.LastTimeDot == null)
		{
			Paras ps = new Paras();
			ps.SQL = "SELECT SDT FROM WF_GenerWorkerlist WHERE WorkID=" + ps.getDBStr() + "WorkID AND FK_Node=" + ps.getDBStr() + "FK_Node";
			ps.Add("WorkID", this.HisCurrWorkNode.getWorkID());
			ps.Add("FK_Node", nd.getNodeID());
			DataTable dt = DBAccess.RunSQLReturnTable(ps);

			for (DataRow dr : dt.Rows)
			{
				this.LastTimeDot = dr.getValue(0).toString();
				break;
			}
		}

		//上一个节点的发送时间点或者 到期的时间点，就是当前节点的接受任务的时间。
		sa.setPlanADT(this.LastTimeDot);

		//计算当前节点的应该完成日期。
		Date dtOfShould = Glo.AddDayHoursSpan(this.LastTimeDot, nd.getTimeLimit(), nd.getTimeLimitHH(), nd.getTimeLimitMM(), nd.getTWay());
		sa.setPlanSDT(DataType.dateToStr(dtOfShould,DataType.getSysDatetimeFormatCN()));

		//给最后的时间点复制.
		this.LastTimeDot = sa.getPlanSDT();
	}
	/** 
	 当前节点应该完成的日期.
	*/
	private String LastTimeDot = null;
}