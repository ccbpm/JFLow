package bp.wf.template;

import bp.da.*;
import bp.tools.HttpClientUtil;
import bp.web.*;
import bp.port.*;
import bp.sys.*;
import bp.wf.*;
import bp.wf.Glo;

import java.util.*;

/** 
 找人规则
*/
public class FindWorker
{

		///#region 变量
	public WorkNode town = null;
	public WorkNode currWn = null;
	public Flow fl = null;
	private String dbStr = bp.difference.SystemConfig.getAppCenterDBVarStr();
	public Paras ps = null;
	private String JumpToEmp = null;
	private int JumpToNode = 0;
	private long WorkID = 0;

		///#endregion 变量

	public FindWorker() throws Exception {

	}
	public final DataTable FindByWorkFlowModel() throws Exception {
		this.town = town;


		Node toNode = town.getHisNode();

		DataTable dt = new DataTable();
		dt.Columns.Add("No", String.class);
		String sql;
		String FK_Emp;

		// 如果执行了两次发送，那前一次的轨迹就需要被删除,这里是为了避免错误。

		ps = new Paras();
		ps.Add("WorkID", town.getHisWork().getOID());
		ps.Add("FK_Node", town.getHisNode().getNodeID());
		ps.SQL = "DELETE FROM WF_GenerWorkerlist WHERE WorkID=" + dbStr + "WorkID AND FK_Node =" + dbStr + "FK_Node";
		DBAccess.RunSQL(ps);

		// 如果指定特定的人员处理。
		if (DataType.IsNullOrEmpty(JumpToEmp) == false)
		{
			String[] myEmpStrs = JumpToEmp.split("[,]", -1);
			for (String emp : myEmpStrs)
			{
				if (DataType.IsNullOrEmpty(emp))
				{
					continue;
				}
				DataRow dr = dt.NewRow();
				dr.setValue(0, emp);
				dt.Rows.add(dr);
			}
			return dt;
		}

		// 按上一节点发送人处理。
		if (town.getHisNode().getHisDeliveryWay() == DeliveryWay.ByPreviousNodeEmp)
		{
			DataRow dr = dt.NewRow();
			dr.setValue(0, WebUser.getNo());
			dt.Rows.add(dr);
			return dt;
		}

		//首先判断是否配置了获取下一步接受人员的sql.
		if (town.getHisNode().getHisDeliveryWay() == DeliveryWay.BySQL || town.getHisNode().getHisDeliveryWay() == DeliveryWay.BySQLTemplate || town.getHisNode().getHisDeliveryWay() == DeliveryWay.BySQLAsSubThreadEmpsAndData)
		{

			if (town.getHisNode().getHisDeliveryWay() == DeliveryWay.BySQLTemplate)
			{
				SQLTemplate st = new SQLTemplate(town.getHisNode().getDeliveryParas());
				sql = st.getDocs();
			}
			else
			{
				if (town.getHisNode().getDeliveryParas().length() < 4)
				{
					throw new RuntimeException("@您设置的当前节点按照SQL，决定下一步的接受人员，但是你没有设置SQL.");
				}
				sql = town.getHisNode().getDeliveryParas();
				sql = sql.toString();
			}


			//特殊的变量.
			sql = sql.replace("@FK_Node", String.valueOf(this.town.getHisNode().getNodeID()));
			sql = sql.replace("@NodeID", String.valueOf(this.town.getHisNode().getNodeID()));

			sql = sql.replace("@WorkID", String.valueOf(this.currWn.getHisWork().getOID()));
			sql = sql.replace("@FID", String.valueOf(this.currWn.getHisWork().getFID()));


			if (this.town.getHisNode().getFormType() == NodeFormType.RefOneFrmTree)
			{
				GEEntity en = new GEEntity(this.town.getHisNode().getNodeFrmID(), this.currWn.getHisWork().getOID());
				sql = Glo.DealExp(sql, en, null);
			}
			else
			{
				sql = Glo.DealExp(sql, this.currWn.rptGe, null);
			}


			if (sql.contains("@GuestUser.No"))
			{
				sql = sql.replace("@GuestUser.No", GuestUser.getNo());
			}

			if (sql.contains("@GuestUser.Name"))
			{
				sql = sql.replace("@GuestUser.Name", GuestUser.getName());
			}

			dt = DBAccess.RunSQLReturnTable(sql);
			if (dt.Rows.size() == 0 && town.getHisNode().getHisWhenNoWorker() == false)
			{
				throw new RuntimeException("@没有找到可接受的工作人员。@技术信息：执行的SQL没有发现人员:" + sql);
			}
			return dt;
		}


			///#region 按绑定部门计算,该部门一人处理标识该工作结束(子线程)..
		if (town.getHisNode().getHisDeliveryWay() == DeliveryWay.BySetDeptAsSubthread)
		{
			if (this.town.getHisNode().getIsSubThread() == false)
			{
				throw new RuntimeException("@您设置的节点接收人方式为：按绑定部门计算,该部门一人处理标识该工作结束(子线程)，但是当前节点非子线程节点。");
			}

			sql = "SELECT " + bp.sys.base.Glo.getUserNo() + ", Name,FK_Dept AS GroupMark FROM Port_Emp WHERE FK_Dept IN (SELECT FK_Dept FROM WF_NodeDept WHERE FK_Node=" + town.getHisNode().getNodeID() + ")";
			dt = DBAccess.RunSQLReturnTable(sql);
			if (dt.Rows.size() == 0 && town.getHisNode().getHisWhenNoWorker() == false)
			{
				throw new RuntimeException("@没有找到可接受的工作人员,接受人方式为, ‘按绑定部门计算,该部门一人处理标识该工作结束(子线程)’ @技术信息：执行的SQL没有发现人员:" + sql);
			}
			return dt;
		}

			///#endregion 按绑定部门计算,该部门一人处理标识该工作结束(子线程)..


			///#region 按照明细表,作为子线程的接收人.
		if (town.getHisNode().getHisDeliveryWay() == DeliveryWay.ByDtlAsSubThreadEmps)
		{
			if (this.town.getHisNode().getIsSubThread() == false)
			{
				throw new RuntimeException("@您设置的节点接收人方式为：以分流点表单的明细表数据源确定子线程的接收人，但是当前节点非子线程节点。");
			}

			this.currWn.getHisNode().WorkID = this.WorkID; //为获取表单ID ( NodeFrmID )提供参数.
			MapDtls dtls = new MapDtls(this.currWn.getHisNode().getNodeFrmID());
			String msg = null;
			for (MapDtl dtl : dtls.ToJavaList())
			{
				try
				{
					String empFild = town.getHisNode().getDeliveryParas();
					if (DataType.IsNullOrEmpty(empFild))
					{
						empFild = " UserNo ";
					}

					ps = new Paras();
					ps.SQL = "SELECT " + empFild + ", * FROM " + dtl.getPTable() + " WHERE RefPK=" + dbStr + "OID ORDER BY OID";
					if (bp.difference.SystemConfig.getAppCenterDBType( ) == DBType.MySQL)
					{
						ps.SQL = "SELECT " + empFild + ", A.* FROM " + dtl.getPTable() + " A WHERE RefPK=" + dbStr + "OID ORDER BY OID";
					}
					ps.Add("OID", this.WorkID);
					dt = DBAccess.RunSQLReturnTable(ps);
					if (dt.Rows.size() == 0 && town.getHisNode().getHisWhenNoWorker() == false)
					{
						throw new RuntimeException("@流程设计错误，到达的节点（" + town.getHisNode().getName() + "）在指定的节点中没有数据，无法找到子线程的工作人员。");
					}
					return dt;
				}
				catch (RuntimeException ex)
				{
					msg += ex.getMessage();
					//if (dtls.size() == 1)
					//    throw new Exception("@估计是流程设计错误,没有在分流节点的明细表中设置");
				}
			}
			throw new RuntimeException("@没有找到分流节点的明细表作为子线程的发起的数据源，流程设计错误，请确认分流节点表单中的明细表是否有UserNo约定的系统字段。" + msg);
		}

			///#endregion 按照明细表,作为子线程的接收人.


			///#region 按节点绑定的人员处理.
		if (town.getHisNode().getHisDeliveryWay() == DeliveryWay.ByBindEmp)
		{
			ps = new Paras();
			ps.Add("FK_Node", town.getHisNode().getNodeID());
			ps.SQL = "SELECT FK_Emp FROM WF_NodeEmp WHERE FK_Node=" + dbStr + "FK_Node ORDER BY FK_Emp";
			dt = DBAccess.RunSQLReturnTable(ps);
			if (dt.Rows.size() == 0)
			{
				throw new RuntimeException("@流程设计错误:下一个节点(" + town.getHisNode().getName() + ")没有绑定工作人员 . ");
			}
			return dt;
		}

			///#endregion 按节点绑定的人员处理.

		String empNo = WebUser.getNo();
		String empDept = WebUser.getFK_Dept();


			///#region 找指定节点的人员直属领导 .
		if (town.getHisNode().getHisDeliveryWay() == DeliveryWay.ByEmpLeader)
		{
			//查找指定节点的人员， 如果没有节点，就是当前的节点.
			String para = town.getHisNode().getDeliveryParas();
			if (DataType.IsNullOrEmpty(para) == true)
			{
				para = String.valueOf(this.currWn.getHisNode().getNodeID());
			}

			//throw new Exception("err@配置错误，当前节点是找指定节点的直属领导，但是您没有设置指定的节点ID.");

			String[] strs = para.split("[,]", -1);
			for (String str : strs)
			{
				if (DataType.IsNullOrEmpty(str) == true)
				{
					continue;
				}

				ps = new Paras();
				ps.SQL = "SELECT FK_Emp FROM WF_GenerWorkerList WHERE WorkID=" + dbStr + "OID AND FK_Node=" + dbStr + "FK_Node ";
				ps.Add("OID", this.WorkID);
				ps.Add("FK_Node", Integer.parseInt(str));
				dt = DBAccess.RunSQLReturnTable(ps);
				if (dt.Rows.size() != 1)
				{
					continue;
				}

				empNo = dt.Rows.get(0).getValue(0).toString();

				//查找人员的直属leader
				sql = "";
				if (bp.difference.SystemConfig.getCCBPMRunModel() == CCBPMRunModel.Single)
				{
					sql = "SELECT Leader,FK_Dept FROM Port_Emp WHERE No='" + empNo + "'";
				}
				else
				{
					sql = "SELECT Leader,FK_Dept FROM Port_Emp WHERE No='" + WebUser.getOrgNo() + "_" + empNo + "'";
				}

				DataTable dtEmp = DBAccess.RunSQLReturnTable(sql);

				//查找他的leader, 如果没有就找部门领导.
				String leader = dtEmp.Rows.get(0).getValue(0) instanceof String ? (String)dtEmp.Rows.get(0).getValue(0) : null;
				String deptNo = dtEmp.Rows.get(0).getValue(1) instanceof String ? (String)dtEmp.Rows.get(0).getValue(1) : null;
				if (leader == null)
				{
					sql = "SELECT Leader FROM Port_Dept WHERE No='" + deptNo + "'";
					leader = DBAccess.RunSQLReturnStringIsNull(sql, null);
					if (leader == null)
					{
						throw new RuntimeException("@流程设计错误:下一个节点(" + town.getHisNode().getName() + ")设置的按照直属领导计算，没有维护(" + WebUser.getNo() + "," + WebUser.getName() + ")的直属领导 . ");
					}
				}
				dt = DBAccess.RunSQLReturnTable(sql);
				return dt;
			}
		}

			///#endregion .按照部门负责人计算


			///#region 按照部门负责人计算.
		if (town.getHisNode().getHisDeliveryWay() == DeliveryWay.ByDeptLeader)
		{
			return ByDeptLeader();
		}


			///#endregion .按照部门负责人计算


			///#region 按照部门分管领导计算.
		if (town.getHisNode().getHisDeliveryWay() == DeliveryWay.ByDeptShipLeader)
		{
			return ByDeptShipLeader();
		}


			///#endregion .按照部门负责人计算


			///#region 按照选择的人员处理。
		if (town.getHisNode().getHisDeliveryWay() == DeliveryWay.BySelected || town.getHisNode().getHisDeliveryWay() == DeliveryWay.BySelectedForPrj || town.getHisNode().getHisDeliveryWay() == DeliveryWay.ByFEE)
		{
			ps = new Paras();
			ps.Add("FK_Node", this.town.getHisNode().getNodeID());
			ps.Add("WorkID", this.currWn.getHisWork().getOID());
			ps.SQL = "SELECT FK_Emp FROM WF_SelectAccper WHERE FK_Node=" + dbStr + "FK_Node AND WorkID=" + dbStr + "WorkID AND AccType=0 ORDER BY IDX";
			dt = DBAccess.RunSQLReturnTable(ps);
			if (dt.Rows.size() == 0)
			{
				/*从上次发送设置的地方查询. */
				SelectAccpers sas = new SelectAccpers();
				int i = sas.QueryAccepterPriSetting(this.town.getHisNode().getNodeID());
				if (i == 0)
				{
					if (town.getHisNode().getHisDeliveryWay() == DeliveryWay.BySelected || town.getHisNode().getHisDeliveryWay() == DeliveryWay.BySelectedForPrj)
					{
						Node currNode = this.currWn.getHisNode();
						if (toNode.isResetAccepter() == false && toNode.getHisToNDs().contains("@" + currNode.getNodeID()) == true &&
								currNode.getHisToNDs().contains("@" + toNode.getNodeID()) == true)
						{
							sql = "SELECT EmpFrom From ND" + Integer.valueOf(toNode.getFK_Flow()) + "Track WHERE WorkID=" + this.WorkID + " AND NDFrom=" + toNode.getNodeID() + " AND NDTo=" + currNode.getNodeID() + " AND ActionType IN(0,1,6,7,11)";
							DataTable dtt = DBAccess.RunSQLReturnTable(sql);
							if (dtt.Rows.size() > 0)
							{
								for (DataRow drr : dtt.Rows)
								{
									DataRow dr = dt.NewRow();
									dr.setValue(0, drr.getValue(0).toString());

									dt.Rows.add(dr);
								}
								return dt;
							}
						}
						Selector select = new Selector(toNode.getNodeID());
						if (select.getSelectorModel() == SelectorModel.GenerUserSelecter)
						{
							throw new RuntimeException("url@./WorkOpt/AccepterOfGener.htm?FK_Flow=" + toNode.getFK_Flow() + "&FK_Node=" + this.currWn.getHisNode().getNodeID() + "&ToNode=" + toNode.getNodeID() + "&WorkID=" + this.WorkID);
						}
						else
						{
							throw new RuntimeException("url@./WorkOpt/Accepter.htm?FK_Flow=" + toNode.getFK_Flow() + "&FK_Node=" + this.currWn.getHisNode().getNodeID() + "&ToNode=" + toNode.getNodeID() + "&WorkID=" + this.WorkID);
						}
					}
					else
					{
						throw new RuntimeException("@流程设计错误，请重写FEE，然后为节点(" + town.getHisNode().getName() + ")设置接受人员，详细请参考cc流程设计手册。");
					}
				}

				//插入里面.
				for (SelectAccper item : sas.ToJavaList())
				{
					DataRow dr = dt.NewRow();
					dr.setValue(0, item.getFK_Emp());
					dt.Rows.add(dr);
				}
				return dt;
			}
			return dt;
		}

			///#endregion 按照选择的人员处理。


			///#region 按照指定节点的处理人计算。
		if (town.getHisNode().getHisDeliveryWay() == DeliveryWay.BySpecNodeEmp || town.getHisNode().getHisDeliveryWay() == DeliveryWay.ByStarter)
		{
			/* 按指定节点的人员计算 */
			String strs = town.getHisNode().getDeliveryParas();
			if (town.getHisNode().getHisDeliveryWay() == DeliveryWay.ByStarter)
			{
				long myworkid = this.currWn.getWorkID();
				if (this.currWn.getHisWork().getFID() != 0)
				{
					myworkid = this.currWn.getHisWork().getFID();
				}
				dt = DBAccess.RunSQLReturnTable("SELECT Starter as No, StarterName as Name FROM WF_GenerWorkFlow WHERE WorkID=" + myworkid);
				if (dt.Rows.size() == 1)
				{
					return dt;
				}

				/* 有可能当前节点就是第一个节点，那个时间还没有初始化数据，就返回当前人. */
				if (this.currWn.getHisNode().isStartNode())
				{
					DataRow dr = dt.NewRow();
					dr.setValue(0, WebUser.getNo());
					dt.Rows.add(dr);
					return dt;
				}

				if (dt.Rows.size() == 0 && town.getHisNode().getHisWhenNoWorker() == false)
				{
					throw new RuntimeException("@流程设计错误，到达的节点（" + town.getHisNode().getName() + "）无法找到开始节点的工作人员。");
				}
				else
				{
					return dt;
				}

			}

			// 首先从本流程里去找。
			strs = strs.replace(";", ",");
			String[] ndStrs = strs.split("[,]", -1);
			for (String nd : ndStrs)
			{
				if (DataType.IsNullOrEmpty(nd))
				{
					continue;
				}

				if (DataType.IsNumStr(nd) == false)
				{
					throw new RuntimeException("流程设计错误:您设置的节点(" + town.getHisNode().getName() + ")的接收方式为按指定的节点岗位投递，但是您没有在访问规则设置中设置节点编号。");
				}

				ps = new Paras();
				String workSQL = "";
				//获取指定节点的信息
				Node specNode = new Node(nd);
				//指定节点是子线程
				if (specNode.getIsSubThread() == true)
				{
					if (this.currWn.getHisNode().getIsSubThread() == true)
					{
						workSQL = "FID=" + this.currWn.getHisWork().getFID();
					}
					else
					{
						workSQL = "FID=" + this.WorkID;
					}
				}
				else
				{
					if (this.currWn.getHisNode().getIsSubThread() == true)
					{
						workSQL = "WorkID=" + this.currWn.getHisWork().getFID();
					}
					else
					{
						workSQL = "WorkID=" + this.WorkID;
					}

				}

				ps.SQL = "SELECT DISTINCT(FK_Emp) FROM WF_GenerWorkerList WHERE " + workSQL + " AND FK_Node=" + dbStr + "FK_Node AND IsEnable=1 ";
				ps.Add("FK_Node", Integer.parseInt(nd));

				DataTable dt_ND = DBAccess.RunSQLReturnTable(ps);
				//添加到结果表
				if (dt_ND.Rows.size() != 0)
				{
					for (DataRow row : dt_ND.Rows)
					{
						DataRow dr = dt.NewRow();
						dr.setValue(0, row.getValue(0).toString());
						dt.Rows.add(dr);
					}
					//此节点已找到数据则不向下找，继续下个节点
					continue;
				}

				//就要到轨迹表里查,因为有可能是跳过的节点.
				ps = new Paras();
				ps.SQL = "SELECT DISTINCT(" + TrackAttr.EmpFrom + ") FROM ND" + Integer.parseInt(fl.getNo()) + "Track WHERE" + " (ActionType=" + dbStr + "ActionType1 OR ActionType=" + dbStr + "ActionType2 OR ActionType=" + dbStr + "ActionType3" + "  OR ActionType=" + dbStr + "ActionType4 OR ActionType=" + dbStr + "ActionType5 OR ActionType=" + dbStr + "ActionType6)" + "   AND NDFrom=" + dbStr + "NDFrom AND " + workSQL;
				ps.Add("ActionType1", ActionType.Skip.getValue());
				ps.Add("ActionType2", ActionType.Forward.getValue());
				ps.Add("ActionType3", ActionType.ForwardFL.getValue());
				ps.Add("ActionType4", ActionType.ForwardHL.getValue());
				ps.Add("ActionType5", ActionType.SubThreadForward.getValue());
				ps.Add("ActionType6", ActionType.Start.getValue());
				ps.Add("NDFrom", Integer.parseInt(nd));

				dt_ND = DBAccess.RunSQLReturnTable(ps);
				if (dt_ND.Rows.size() != 0)
				{
					for (DataRow row : dt_ND.Rows)
					{
						DataRow dr = dt.NewRow();
						dr.setValue(0, row.getValue(0).toString());
						dt.Rows.add(dr);
					}
					continue;
				}

				//从Selector中查找
				ps = new Paras();
				ps.SQL = "SELECT DISTINCT(FK_Emp) FROM WF_SelectAccper WHERE FK_Node=" + dbStr + "FK_Node AND " + workSQL;
				ps.Add("FK_Node", Integer.parseInt(nd));


				dt_ND = DBAccess.RunSQLReturnTable(ps);
				//添加到结果表
				if (dt_ND.Rows.size() != 0)
				{
					for (DataRow row : dt_ND.Rows)
					{
						DataRow dr = dt.NewRow();
						dr.setValue(0, row.getValue(0).toString());
						dt.Rows.add(dr);
					}
					//此节点已找到数据则不向下找，继续下个节点
					continue;
				}


			}

			//本流程里没有有可能该节点是配置的父流程节点,也就是说子流程的一个节点与父流程指定的节点的工作人员一致.
			GenerWorkFlow gwf = new GenerWorkFlow(this.WorkID);
			if (gwf.getPWorkID() != 0)
			{
				for (String pnodeiD : ndStrs)
				{
					if (DataType.IsNullOrEmpty(pnodeiD))
					{
						continue;
					}

					Node nd = new Node(Integer.parseInt(pnodeiD));
					if (!nd.getFK_Flow().equals(gwf.getPFlowNo()))
					{
						continue; // 如果不是父流程的节点，就不执行.
					}

					ps = new Paras();
					ps.SQL = "SELECT FK_Emp FROM WF_GenerWorkerList WHERE WorkID=" + dbStr + "OID AND FK_Node=" + dbStr + "FK_Node AND IsPass=1 AND IsEnable=1 ";
					ps.Add("FK_Node", nd.getNodeID());
					if (this.currWn.getHisNode().getIsSubThread() == true)
					{
						ps.Add("OID", gwf.getPFID());
					}
					else
					{
						ps.Add("OID", gwf.getPWorkID());
					}

					DataTable dt_PWork = DBAccess.RunSQLReturnTable(ps);
					if (dt_PWork.Rows.size() != 0)
					{
						for (DataRow row : dt_PWork.Rows)
						{
							DataRow dr = dt.NewRow();
							dr.setValue(0, row.getValue(0).toString());
							dt.Rows.add(dr);
						}
						//此节点已找到数据则不向下找，继续下个节点
						continue;
					}

					//就要到轨迹表里查,因为有可能是跳过的节点.
					ps = new Paras();
					ps.SQL = "SELECT " + TrackAttr.EmpFrom + " FROM ND" + Integer.parseInt(fl.getNo()) + "Track WHERE (ActionType=" + dbStr + "ActionType1 OR ActionType=" + dbStr + "ActionType2 OR ActionType=" + dbStr + "ActionType3 OR ActionType=" + dbStr + "ActionType4 OR ActionType=" + dbStr + "ActionType5) AND NDFrom=" + dbStr + "NDFrom AND WorkID=" + dbStr + "WorkID";
					ps.Add("ActionType1", ActionType.Start.getValue());
					ps.Add("ActionType2", ActionType.Forward.getValue());
					ps.Add("ActionType3", ActionType.ForwardFL.getValue());
					ps.Add("ActionType4", ActionType.ForwardHL.getValue());
					ps.Add("ActionType5", ActionType.Skip.getValue());

					ps.Add("NDFrom", nd.getNodeID());

					if (this.currWn.getHisNode().getIsSubThread() == true)
					{
						ps.Add("WorkID", gwf.getPFID());
					}
					else
					{
						ps.Add("WorkID", gwf.getPWorkID());
					}

					dt_PWork = DBAccess.RunSQLReturnTable(ps);
					if (dt_PWork.Rows.size() != 0)
					{
						for (DataRow row : dt_PWork.Rows)
						{
							DataRow dr = dt.NewRow();
							dr.setValue(0, row.getValue(0).toString());
							dt.Rows.add(dr);
						}
					}
				}
			}
			//返回指定节点的处理人
			if (dt.Rows.size() != 0)
			{
				return dt;
			}

			throw new RuntimeException("@流程设计错误，到达的节点（" + town.getHisNode().getName() + "）在指定的节点(" + strs + ")中没有数据，无法找到工作的人员。 @技术信息如下: 投递方式:BySpecNodeEmp sql=" + ps.getSQLNoPara());
		}

			///#endregion 按照节点绑定的人员处理。


			///#region 按照上一个节点表单指定字段的人员处理。
		if (town.getHisNode().getHisDeliveryWay() == DeliveryWay.ByPreviousNodeFormEmpsField)
		{
			// 检查接受人员规则,是否符合设计要求.
			String specEmpFields = town.getHisNode().getDeliveryParas();
			if (DataType.IsNullOrEmpty(specEmpFields))
			{
				specEmpFields = "SysSendEmps";
			}

			if (this.currWn.rptGe.getEnMap().getAttrs().contains(specEmpFields) == false)
			{
				throw new RuntimeException("@您设置的接受人规则是按照表单指定的字段，决定下一步的接受人员，该字段{" + specEmpFields + "}已经删除或者丢失。");
			}

			//判断该字段是否启用了pop返回值？
			sql = "SELECT  Tag1 AS VAL FROM Sys_FrmEleDB WHERE RefPKVal=" + this.WorkID + " AND EleID='" + specEmpFields + "'";
			String emps = "";
			DataTable dtVals = DBAccess.RunSQLReturnTable(sql);

			//获取接受人并格式化接受人, 
			if (dtVals.Rows.size() > 0)
			{
				for (DataRow dr : dtVals.Rows)
				{
					emps += dr.getValue(0).toString() + ",";
				}
			}
			else
			{
				emps = this.currWn.rptGe.GetValStringByKey(specEmpFields);
			}


			emps = emps.replace(" ", ""); //去掉空格.

			if (emps.contains(",") && emps.contains(";"))
			{
				/*如果包含,; 例如 zhangsan,张三;lisi,李四;*/
				String[] myemps1 = emps.split("[;]", -1);
				for (String str : myemps1)
				{
					if (DataType.IsNullOrEmpty(str))
					{
						continue;
					}

					String[] ss = str.split("[,]", -1);
					DataRow dr = dt.NewRow();
					dr.setValue(0, ss[0]);
					dt.Rows.add(dr);
				}
				if (dt.Rows.size() == 0 && town.getHisNode().getHisWhenNoWorker() == false)
				{
					throw new RuntimeException("@输入的接受人员信息错误;[" + emps + "]。");
				}
				else
				{
					return dt;
				}
			}

			emps = emps.replace(";", ",");
			emps = emps.replace("；", ",");
			emps = emps.replace("，", ",");
			emps = emps.replace("、", ",");
			emps = emps.replace("@", ",");

			if (DataType.IsNullOrEmpty(emps) && town.getHisNode().getHisWhenNoWorker() == false)
			{
				throw new RuntimeException("@没有在字段[" + this.currWn.getHisWork().getEnMap().getAttrs().GetAttrByKey(specEmpFields).getDesc() + "]中指定接受人，工作无法向下发送。");
			}

			// 把它加入接受人员列表中.
			String[] myemps = emps.split("[,]", -1);
			for (String s : myemps)
			{
				if (DataType.IsNullOrEmpty(s))
				{
					continue;
				}

				//if (DBAccess.RunSQLReturnValInt("SELECT COUNT(NO) AS NUM FROM Port_Emp WHERE NO='" + s + "' or name='"+s+"'", 0) == 0)
				//    continue;

				DataRow dr = dt.NewRow();
				dr.setValue(0, s);
				dt.Rows.add(dr);
			}
			return dt;
		}
		//#endregion 按照上一个节点表单指定字段的人员处理。
		//#region按照上一个节点表单指定字段的部门处理
		if (town.getHisNode().getHisDeliveryWay() == DeliveryWay.ByPreviousNodeFormDepts){
			// 检查接受人员规则,是否符合设计要求.
			String specEmpFields = town.getHisNode().getDeliveryParas();
			if(DataType.IsNullOrEmpty(specEmpFields)==true)
				throw new RuntimeException("@您设置的接受人规则是按照表单指定的字段是部门，但是没有选择表单字段");
			if (this.currWn.rptGe.getEnMap().getAttrs().contains(specEmpFields) == false)
				throw new RuntimeException("@您设置的接受人规则是按照表单指定的部门字段，决定下一步的接受人员，该字段{" + specEmpFields + "}已经删除或者丢失。");

			//判断该字段是否启用了pop返回值？
			sql = "SELECT  Tag1 AS VAL FROM Sys_FrmEleDB WHERE RefPKVal=" + this.WorkID + " AND EleID='" + specEmpFields + "'";
			String depts = "";
			DataTable dtVals = DBAccess.RunSQLReturnTable(sql);

			//获取接受人并格式化接受人,
			if (dtVals.Rows.size() > 0)
			{
				for (DataRow dr : dtVals.Rows)
				{
					depts += dr.getValue(0).toString() + ",";
				}
			}
			else
			{
				depts = this.currWn.rptGe.GetValStringByKey(specEmpFields);
			}


			depts = depts.replace(" ", ""); //去掉空格.
			if(depts.endsWith(","))
				depts = depts.substring(0,depts.length()-1);
			if(DataType.IsNullOrEmpty(depts)==false){
				depts="'"+depts.replace(",","','")+"'";
			}
			//获取人员
			if(DataType.IsNullOrEmpty(depts) == true)
				throw new RuntimeException("@您设置的接受人规则是按照表单指定的部门字段，没有选择部门");

			sql="SELECT DISTINCT(FK_Emp) From Port_DeptEmp WHERE FK_Dept IN("+depts+")";
			DataTable dtt = DBAccess.RunSQLReturnTable(sql);
			if(dtt.Rows.size()==0)
				throw new RuntimeException("@您设置的接受人规则是按照表单指定的部门字段，填写的部门中不存在人员");
			return dtt;
		}
		//#endregion按照上一个节点表单指定字段的部门处理
		///#region  按照上一个节点表单指定字段的 【岗位】处理。
		if (town.getHisNode().getHisDeliveryWay() == DeliveryWay.ByPreviousNodeFormStationsAI
				|| town.getHisNode().getHisDeliveryWay() == DeliveryWay.ByPreviousNodeFormStationsOnly)
		{
			// 检查接受人员规则,是否符合设计要求.
			String specEmpFields = town.getHisNode().getDeliveryParas();
			if (DataType.IsNullOrEmpty(specEmpFields))
			{
				specEmpFields = "SysSendEmps";
			}

			if (this.currWn.rptGe.getEnMap().getAttrs().contains(specEmpFields) == false)
			{
				throw new RuntimeException("@您设置的接受人规则是按照表单指定的岗位字段，决定下一步的接受人员，该字段{" + specEmpFields + "}已经删除或者丢失。");
			}

			//判断该字段是否启用了pop返回值？
			sql = "SELECT  Tag1 AS VAL FROM Sys_FrmEleDB WHERE RefPKVal=" + this.WorkID + " AND EleID='" + specEmpFields + "'";
			String emps = "";
			DataTable dtVals = DBAccess.RunSQLReturnTable(sql);

			//获得岗位信息.
			String stas = "";

			//获取接受人并格式化接受人,
			if (dtVals.Rows.size() > 0)
			{
				for (DataRow dr : dtVals.Rows)
				{
					emps += dr.getValue(0).toString() + ",";
				}
			}
			else
			{
				emps = this.currWn.rptGe.GetValStringByKey(specEmpFields);
			}

			emps = emps.replace(" ", ""); //去掉空格.

			if (emps.contains(",") && emps.contains(";"))
			{
				/*如果包含,; 例如 xxx,岗位1;333,岗位2;*/
				String[] myemps1 = emps.split("[;]", -1);
				for (String str : myemps1)
				{
					if (DataType.IsNullOrEmpty(str))
					{
						continue;
					}

					String[] ss = str.split("[,]", -1);
					stas += "," + ss[0];

				}
				if (dt.Rows.size() == 0 && town.getHisNode().getHisWhenNoWorker() == false)
				{
					throw new RuntimeException("@输入的接受人员岗位信息错误;[" + emps + "]。");
				}
				else
				{
					return dt;
				}
			}else
			{
				emps = emps.replace(";", ",");
				emps = emps.replace("；", ",");
				emps = emps.replace("，", ",");
				emps = emps.replace("、", ",");
				emps = emps.replace("@", ",");

				if (DataType.IsNullOrEmpty(emps) && town.getHisNode().getHisWhenNoWorker() == false)
				{
					throw new RuntimeException("@没有在字段[" + this.currWn.getHisWork().getEnMap().getAttrs().GetAttrByKey(specEmpFields).getDesc() + "]中指定接受人，工作无法向下发送。");
				}

				// 把它加入接受人员列表中.
				String[] myemps = emps.split("[,]", -1);
				for (String s : myemps)
				{
					if (DataType.IsNullOrEmpty(s))
					{
						continue;
					}
					stas += "," + s;
				}
			}

			//根据岗位：集合获取信息.
			stas = stas.substring(1);

			//把这次的岗位s存储到临时变量,以方便用到下一个节点多人处理规则，按岗位删除时用到。
			this.currWn.getHisGenerWorkFlow().SetPara("NodeStas" + town.getHisNode().getNodeID(), stas);

			// 仅按岗位计算.以下都有要重写.
			if (toNode.getHisDeliveryWay() == DeliveryWay.ByPreviousNodeFormStationsOnly)
			{
				dt = WorkFlowBuessRole.FindWorker_GetEmpsByStations(stas);
				if (dt.Rows.size() == 0 && toNode.getHisWhenNoWorker() == false)
				{
					throw new RuntimeException("err@按照字段岗位(仅按岗位计算)找接受人错误,当前部门下没有您选择的岗位人员.");
				}

				return dt;
			}

			///#region 按岗位智能计算, 还是集合模式.
			if (toNode.getDeliveryStationReqEmpsWay() == 0)
			{
				String deptNo = WebUser.getFK_Dept();
				dt = WorkFlowBuessRole.FindWorker_GetEmpsByDeptAI(stas, deptNo);
				if (dt.Rows.size() == 0 && toNode.getHisWhenNoWorker() == false)
				{
					throw new RuntimeException("err@按照字段岗位(智能)找接受人错误,当前部门与父级部门下没有您选择的岗位人员.");
				}
				return dt;
			}
			///#endregion 按岗位智能计算, 要判断切片模式,还是集合模式.

			///#region 按岗位智能计算, 切片模式. 需要对每个岗位都要找到接受人，然后把这些接受人累加起来.
			if (toNode.getDeliveryStationReqEmpsWay() == 1 || toNode.getDeliveryStationReqEmpsWay() == 2)
			{
				String deptNo = WebUser.getFK_Dept();
				String[] temps = stas.split("[,]", -1);
				for (String str : temps)
				{
					//求一个岗位下的人员.
 					DataTable mydt1 = WorkFlowBuessRole.FindWorker_GetEmpsByDeptAI(str, deptNo);

					//如果是严谨模式.
					if (toNode.getDeliveryStationReqEmpsWay() == 1 && mydt1.Rows.size() == 0)
					{
						Station st = new Station(str);
						throw new RuntimeException("@岗位[" + st.getName() + "]下，没有找到人不能发送下去，请检查组织结构是否完整。");
					}

					//累加.
					for (DataRow dr : mydt1.Rows)
					{
						DataRow mydr = dt.NewRow();
						mydr.setValue(0,dr.getValue(0).toString());
						dt.Rows.add(mydr);
					}
				}
			}
			///#endregion 按岗位智能计算, 切片模式.
			return dt;
		}
		///#endregion 按照上一个节点表单指定字段的[岗位]人员处理.

		///#region 为省立医院增加，按照指定的部门范围内的岗位计算..
		if (town.getHisNode().getHisDeliveryWay() == DeliveryWay.FindSpecDeptEmpsInStationlist)
		{
			sql = "SELECT A.FK_Emp FROM Port_DeptEmpStation A WHERE A.FK_DEPT ='" + WebUser.getFK_Dept()+ "' AND A.FK_Station in(";
			sql += "select FK_Station from WF_NodeStation where FK_node=" + town.getHisNode().getNodeID() + ")";

			dt = DBAccess.RunSQLReturnTable(sql);

			if (dt.Rows.size() > 0)
				return dt;

			if (this.town.getHisNode().getHisWhenNoWorker() == false)
				throw new RuntimeException("@节点访问规则(" + town.getHisNode().getHisDeliveryWay().toString() + ")错误:节点(" + town.getHisNode().getNodeID() + "," + town.getHisNode().getName() + "), 按照岗位与部门的交集确定接受人的范围错误，没有找到人员:SQL=" + sql);
			else
				return dt;
		}
		///#endregion 按部门与岗位的交集计算.

		///#region 按部门与岗位的交集计算.
		if (town.getHisNode().getHisDeliveryWay() == DeliveryWay.ByDeptAndStation)
		{
			//added by liuxc,2015.6.29.

			sql = "SELECT pdes.FK_Emp AS No" + " FROM   Port_DeptEmpStation pdes" + " INNER JOIN WF_NodeDept wnd ON wnd.FK_Dept = pdes.FK_Dept" + " AND wnd.FK_Node = " + town.getHisNode().getNodeID() + " INNER JOIN WF_NodeStation wns ON  wns.FK_Station = pdes.FK_Station" + " AND wns.FK_Node =" + town.getHisNode().getNodeID() + " ORDER BY pdes.FK_Emp";


			dt = DBAccess.RunSQLReturnTable(sql);

			if (dt.Rows.size() > 0)
				return dt;
			if (this.town.getHisNode().getHisWhenNoWorker() == false)
				throw new RuntimeException("@节点访问规则(" + town.getHisNode().getHisDeliveryWay().toString() + ")错误:节点(" + town.getHisNode().getNodeID() + "," + town.getHisNode().getName() + "), 按照岗位与部门的交集确定接受人的范围错误，没有找到人员:SQL=" + sql);
			else
				return dt;
		}

			///#endregion 按部门与岗位的交集计算.


			///#region 判断节点部门里面是否设置了部门，如果设置了就按照它的部门处理。
		if (town.getHisNode().getHisDeliveryWay() == DeliveryWay.ByDept)
		{
			ps = new Paras();
			sql= "SELECT A.No,A.Name FROM Port_Emp A,Port_DeptEmp B WHERE A.No=B.FK_Emp AND B.FK_Dept IN(SELECT FK_dept FROM WF_NodeDept WHERE FK_Node =" + dbStr + "FK_Node)";
			ps.Add("FK_Node", this.town.getHisNode().getNodeID());
			ps.SQL = sql;
			dt = DBAccess.RunSQLReturnTable(ps);
			if (dt.Rows.size() == 0)
			{
				if(this.town.getHisNode().getHisWhenNoWorker() == false){
					bp.wf.template.BtnLab btnLab = new BtnLab(this.town.getHisNode().getNodeID());
					throw new Exception("err@按照 [按绑定的部门计算] 计算接收人的时候出现错误，没有找到人，请检查节点["+btnLab.getName()+"]绑定的部门下的人员.");
				}

			}
			return dt;
		}

			///#endregion 判断节点部门里面是否设置了部门，如果设置了，就按照它的部门处理。


			///#region 用户组 计算
		if (town.getHisNode().getHisDeliveryWay() == DeliveryWay.ByTeamOnly)
		{
			ps = new Paras();
			sql = "SELECT A.FK_Emp FROM Port_TeamEmp A, WF_NodeTeam B WHERE A.FK_Team=B.FK_Team AND B.FK_Node=" + dbStr + "FK_Node ORDER BY A.FK_Emp";
			ps.Add("FK_Node", town.getHisNode().getNodeID());
			ps.SQL = sql;
			dt = DBAccess.RunSQLReturnTable(ps);
			if (dt.Rows.size() > 0)
				return dt;

			if (this.town.getHisNode().getHisWhenNoWorker() == false)
				throw new RuntimeException("@节点访问规则错误:节点(" + town.getHisNode().getNodeID() + "," + town.getHisNode().getName() + "), 仅按用户组计算，没有找到人员:SQL=" + ps.getSQLNoPara());
			else
				return dt; //可能处理跳转,在没有处理人的情况下.

		}
		if (town.getHisNode().getHisDeliveryWay() == DeliveryWay.ByTeamOrgOnly)
		{
			sql = "SELECT DISTINCT A.FK_Emp FROM Port_TeamEmp A, WF_NodeTeam B, Port_Emp C WHERE A.FK_Emp=C." + bp.sys.base.Glo.getUserNoWhitOutAS() + " AND A.FK_Team=B.FK_Team AND B.FK_Node=" + dbStr + "FK_Node AND C.OrgNo=" + dbStr + "OrgNo  ORDER BY A.FK_Emp";
			ps = new Paras();
			ps.Add("FK_Node", town.getHisNode().getNodeID());
			ps.Add("OrgNo", WebUser.getOrgNo(), false);

			ps.SQL = sql;
			dt = DBAccess.RunSQLReturnTable(ps);
			if (dt.Rows.size() > 0)
				return dt;

			if (this.town.getHisNode().getHisWhenNoWorker() == false)
				throw new RuntimeException("@节点访问规则错误:节点(" + town.getHisNode().getNodeID() + "," + town.getHisNode().getName() + "), 仅按用户组计算，没有找到人员:SQL=" + ps.getSQLNoPara());

			return dt; //可能处理跳转,在没有处理人的情况下.
		}

		if (town.getHisNode().getHisDeliveryWay() == DeliveryWay.ByTeamDeptOnly)
		{
			sql = "SELECT DISTINCT A.FK_Emp FROM Port_TeamEmp A, WF_NodeTeam B, Port_Emp C WHERE A.FK_Emp=C." + bp.sys.base.Glo.getUserNoWhitOutAS() + " AND A.FK_Team=B.FK_Team AND B.FK_Node=" + dbStr + "FK_Node AND C.FK_Dept=" + dbStr + "FK_Dept  ORDER BY A.FK_Emp";
			ps = new Paras();
			ps.Add("FK_Node", town.getHisNode().getNodeID());
			ps.Add("FK_Dept", WebUser.getFK_Dept(), false);

			ps.SQL = sql;
			dt = DBAccess.RunSQLReturnTable(ps);
			if (dt.Rows.size() > 0)
				return dt;

			if (this.town.getHisNode().getHisWhenNoWorker() == false)
				throw new RuntimeException("@节点访问规则错误 ByTeamDeptOnly :节点(" + town.getHisNode().getNodeID() + "," + town.getHisNode().getName() + "), 仅按用户组计算，没有找到人员:SQL=" + ps.getSQLNoPara());

			return dt; //可能处理跳转,在没有处理人的情况下.
		}

			///#endregion


			///#region 仅按岗位计算
		if (town.getHisNode().getHisDeliveryWay() == DeliveryWay.ByStationOnly)
		{
			ps = new Paras();
			if (bp.difference.SystemConfig.getCCBPMRunModel() == CCBPMRunModel.SAAS)
			{
				//2020-4-25 按照岗位倒序排序 修改原因队列模式时，下级岗位处理后发给上级岗位， 岗位越高数值越小
				sql = "SELECT A.FK_Emp FROM Port_DeptEmpStation A, WF_NodeStation B WHERE A.FK_Station=B.FK_Station AND A.OrgNo=" + dbStr + "OrgNo AND B.FK_Node=" + dbStr + "FK_Node ORDER BY A.FK_Station desc";
				ps.Add("OrgNo", WebUser.getOrgNo(), false);
				ps.Add("FK_Node", town.getHisNode().getNodeID());
				ps.SQL = sql;
				dt = DBAccess.RunSQLReturnTable(ps);
			}
			else
			{
				//2020-4-25 按照岗位倒序排序 修改原因队列模式时，下级岗位处理后发给上级岗位， 岗位越高数值越小
				sql = "SELECT A.FK_Emp FROM Port_DeptEmpStation A, WF_NodeStation B WHERE A.FK_Station=B.FK_Station AND B.FK_Node=" + dbStr + "FK_Node ORDER BY A.FK_Station desc";
				ps.Add("FK_Node", town.getHisNode().getNodeID());
				ps.SQL = sql;
				dt = DBAccess.RunSQLReturnTable(ps);
			}
			if (dt.Rows.size() > 0)
				return dt;

			if (this.town.getHisNode().getHisWhenNoWorker() == false)
				throw new RuntimeException("@节点访问规则错误:流程[" + town.getHisNode().getFlowName() + "]节点[" + town.getHisNode().getNodeID() + "," + town.getHisNode().getName() + "], 仅按岗位计算，没有找到人员。");

			return dt; //可能处理跳转,在没有处理人的情况下.
		}

			///#endregion


			///#region 按配置的人员路由表计算
		if (town.getHisNode().getHisDeliveryWay() == DeliveryWay.ByFromEmpToEmp)
		{
			String[] fromto = town.getHisNode().getDeliveryParas().split("[@]", -1);

			String defUser = "";

			for (String str : fromto)
			{
				String[] kv = str.split("[,]", -1);

				if (kv[0].equals("Defalut") == true)
				{
					defUser = kv[1];
					continue;
				}

				if (WebUser.getNo().equals(kv[0]))
				{
					String empTo = kv[1];
					//bp.port.Emp emp = new bp.port.Emp(empTo);
					DataRow dr = dt.NewRow();
					dr.setValue(0, empTo);
					//  dr[1] = emp.Name;
					dt.Rows.add(dr);
					return dt;
				}
			}

			if (DataType.IsNullOrEmpty(defUser) == false)
			{
				String empTo = defUser;
				DataRow dr = dt.NewRow();
				dr.setValue(0, empTo);
				dt.Rows.add(dr);
				return dt;
			}

			throw new RuntimeException("@接收人规则是按照人员路由表设置的，但是系统管理员没有为您配置路由,当前节点;" + town.getHisNode().getName());
		}

			///#endregion


			///#region 按岗位计算(以部门集合为纬度).
		if (town.getHisNode().getHisDeliveryWay() == DeliveryWay.ByStationAndEmpDept)
		{
			/* 考虑当前操作人员的部门, 如果本部门没有这个岗位就不向上寻找. */

			if (bp.difference.SystemConfig.getCCBPMRunModel() == CCBPMRunModel.SAAS)
			{
				ps = new Paras();
				ps.SQL = "SELECT UserID as No,Name FROM Port_Emp WHERE UserID=" + dbStr + "FK_Emp AND OrgNo=" + dbStr + "OrgNo ";
				ps.Add("FK_Emp", WebUser.getNo(), false);
				ps.Add("OrgNo", WebUser.getOrgNo(), false);

				dt = DBAccess.RunSQLReturnTable(ps);
			}
			else
			{
				ps = new Paras();
				ps.SQL = "SELECT No,Name FROM Port_Emp WHERE No=" + dbStr + "FK_Emp ";
				ps.Add("FK_Emp", WebUser.getNo(), false);
				dt = DBAccess.RunSQLReturnTable(ps);

			}

			if (dt.Rows.size() > 0)
			{
				return dt;
			}
			else
			{
				if (this.town.getHisNode().getHisWhenNoWorker() == false)
				{
					throw new RuntimeException("@节点访问规则(" + town.getHisNode().getHisDeliveryWay().toString() + ")错误:节点(" + town.getHisNode().getNodeID() + "," + town.getHisNode().getName() + "), 按岗位计算(以部门集合为纬度)。技术信息,执行的SQL=" + ps.getSQLNoPara());
				}
				else
				{
					return dt; //可能处理跳转,在没有处理人的情况下.
				}
			}
		}

			///#endregion


			///#region 按照自定义的URL来计算
		if (town.getHisNode().getHisDeliveryWay() == DeliveryWay.BySelfUrl)
		{
			ps = new Paras();
			ps.Add("FK_Node", this.town.getHisNode().getNodeID());
			ps.Add("WorkID", this.currWn.getHisWork().getOID());
			ps.SQL = "SELECT FK_Emp FROM WF_SelectAccper WHERE FK_Node=" + dbStr + "FK_Node AND WorkID=" + dbStr + "WorkID AND AccType=0 ORDER BY IDX";
			dt = DBAccess.RunSQLReturnTable(ps);
			if (dt.Rows.size() == 0)
			{
				/*从上次发送设置的地方查询. */
				SelectAccpers sas = new SelectAccpers();
				int i = sas.QueryAccepterPriSetting(this.town.getHisNode().getNodeID());
				if (i == 0)
				{
					GenerWorkFlow gwf = new GenerWorkFlow(this.WorkID);
					if (DataType.IsNullOrEmpty(toNode.getDeliveryParas()) == true)
					{
						throw new RuntimeException("节点" + toNode.getNodeID() + "_" + toNode.getName() + "设置的接收人规则是自定义的URL,现在未获取到设置的信息");
					}
					else
					{
						throw new RuntimeException("BySelfUrl@" + toNode.getDeliveryParas() + "?FK_Flow=" + toNode.getFK_Flow() + "&FK_Node=" + this.currWn.getHisNode().getNodeID() + "&ToNode=" + toNode.getNodeID() + "&WorkID=" + this.WorkID + "&PWorkID=" + gwf.getPWorkID() + "&FID=" + gwf.getFID());
					}
				}

				//插入里面.
				for (SelectAccper item : sas.ToJavaList())
				{
					DataRow dr = dt.NewRow();
					dr.setValue(0, item.getFK_Emp());
					dt.Rows.add(dr);
				}
				return dt;
			}
			return dt;
		}

			///#endregion 按照自定义的URL来计算


			///#region 按照设置的WebAPI接口获取的数据计算
		if (town.getHisNode().getHisDeliveryWay() == DeliveryWay.ByAPIUrl)
		{
			//返回值
			String postData = "";
			//用户输入的webAPI地址
			String apiUrl = town.getHisNode().getDeliveryParas();
			if (apiUrl.contains("@WebApiHost")) //可以替换配置文件中配置的webapi地址
			{
				apiUrl = apiUrl.replace("@WebApiHost", (CharSequence) bp.difference.SystemConfig.getAppSettings().get("WebApiHost"));
			}
			//如果有参数
			if (apiUrl.contains("?"))
			{
				//api接口地址
				String apiHost = apiUrl.split("[?]", -1)[0];
				//api参数
				String apiParams = apiUrl.split("[?]", -1)[1];
				//参数替换
				apiParams = Glo.DealExp(apiParams, town.getHisWork());
				//执行POST
				postData = HttpClientUtil.doPost(apiHost, apiParams,null);

				if (postData.equals("[]") || postData.equals("") || postData == null)
				{
					throw new RuntimeException("节点" + town.getHisNode().getNodeID() + "_" + town.getHisNode().getName() + "设置的WebAPI接口返回的数据出错，请检查接口返回值。");
				}

				dt = bp.tools.Json.ToDataTable(postData);
				return dt;
			}
			else
			{ //如果没有参数，执行GET
				postData = HttpClientUtil.doGet(apiUrl);
				if (postData.equals("[]") || postData.equals("") || postData == null)
				{
					throw new RuntimeException("节点" + town.getHisNode().getNodeID() + "_" + town.getHisNode().getName() + "设置的WebAPI接口返回的数据出错，请检查接口返回值。");
				}

				dt = bp.tools.Json.ToDataTable(postData);
				return dt;
			}
		}

			///#endregion 按照设置的WebAPI接口获取的数据计算


			///#region 按照组织模式人员选择器
		if (town.getHisNode().getHisDeliveryWay() == DeliveryWay.BySelectedEmpsOrgModel)
		{
			ps = new Paras();
			ps.Add("FK_Node", this.town.getHisNode().getNodeID());
			ps.Add("WorkID", this.currWn.getHisWork().getOID());
			ps.SQL = "SELECT FK_Emp FROM WF_SelectAccper WHERE FK_Node=" + dbStr + "FK_Node AND WorkID=" + dbStr + "WorkID AND AccType=0 ORDER BY IDX";
			dt = DBAccess.RunSQLReturnTable(ps);
			if (dt.Rows.size() == 0)
			{
				/*从上次发送设置的地方查询. */
				SelectAccpers sas = new SelectAccpers();
				int i = sas.QueryAccepterPriSetting(this.town.getHisNode().getNodeID());
				if (i == 0)
				{
					throw new RuntimeException("url@./WorkOpt/AccepterOfOrg.htm?FK_Flow=" + toNode.getFK_Flow() + "&FK_Node=" + this.currWn.getHisNode().getNodeID() + "&ToNode=" + toNode.getNodeID() + "&WorkID=" + this.WorkID);
				}

				//插入里面.
				for (SelectAccper item : sas.ToJavaList())
				{
					DataRow dr = dt.NewRow();
					dr.setValue(0, item.getFK_Emp());
					dt.Rows.add(dr);
				}
				return dt;
			}
			return dt;
		}
		//#region 选择其他组织的联络员
		if (town.getHisNode().getHisDeliveryWay() == DeliveryWay.BySelectEmpByOfficer)
		{
			ps = new Paras();
			ps.Add("FK_Node", this.town.getHisNode().getNodeID());
			ps.Add("WorkID", this.currWn.getHisWork().getOID());
			ps.SQL = "SELECT FK_Emp FROM WF_SelectAccper WHERE FK_Node=" + dbStr + "FK_Node AND WorkID=" + dbStr + "WorkID AND AccType=0 ORDER BY IDX";
			dt = DBAccess.RunSQLReturnTable(ps);
			if (dt.Rows.size() == 0)
			{
				/**从上次发送设置的地方查询. */
				SelectAccpers sas = new SelectAccpers();
				int i = sas.QueryAccepterPriSetting(this.town.getHisNode().getNodeID());
				if (i == 0)
				{
					throw new RuntimeException("url@./WorkOpt/AccepterOfOfficer.htm?FK_Flow=" + toNode.getFK_Flow() + "&FK_Node=" + this.currWn.getHisNode().getNodeID() + "&ToNode=" + toNode.getNodeID() + "&WorkID=" + this.WorkID);
				}

				//插入里面.
				for (SelectAccper item : sas.ToJavaList())
				{
					DataRow dr = dt.NewRow();
					dr.setValue(0, item.getFK_Emp());
					dt.Rows.add(dr);
				}
				return dt;
			}
			return dt;
		}
        //#endregion 选择其他组织的联络员

			///#endregion 按照自定义的URL来计算


			///#region 发送人的上级部门的负责人: 2022.2.20 benjing. by zhoupeng
		if (town.getHisNode().getHisDeliveryWay() == DeliveryWay.BySenderParentDeptLeader)
		{
			String deptNo = WebUser.getDeptParentNo();
			sql = "SELECT A.No,A.Name FROM Port_Emp A, Port_Dept B WHERE A.No=B.Leader AND B.No='" + deptNo + "'";
			dt = DBAccess.RunSQLReturnTable(sql);
			String leaderNo = null;
			if (dt.Rows.size() == 1)
			{
				leaderNo = dt.Rows.get(0).getValue(0) instanceof String ? (String)dt.Rows.get(0).getValue(0) : null;
				//如果领导是当前操作员，就让其找上一级的部门领导。
				if (leaderNo != null && WebUser.getNo().equals(leaderNo) == true)
				{
					leaderNo = null;
				}
			}

			if (dt.Rows.size() == 0 || DataType.IsNullOrEmpty(leaderNo) == true)
			{
				//如果没有找到,就到父节点去找.
				Dept pDept = new Dept(deptNo);
				sql = "SELECT A.No,A.Name FROM Port_Emp A, Port_Dept B WHERE A.No=B.Leader AND B.No='" + pDept.getNo() + "'";
				dt = DBAccess.RunSQLReturnTable(sql);
				return dt;
				// throw new Exception("err@按照 [发送人的上级部门的负责人] 计算接收人的时候出现错误，您没有维护部门[" + pDept.Name + "]的部门负责人.");
			}
			return dt;
		}

			///#endregion 发送人的上级部门的负责人 2022.2.20 benjing.


			///#region 发送人上级部门指定的岗位 2022.2.20 beijing. by zhoupeng
		if (town.getHisNode().getHisDeliveryWay() == DeliveryWay.BySenderParentDeptStations)
		{
			//当前人员身份 sf
			Hashtable sf = GetEmpDeptBySFModel();
			empDept = sf.get("DeptNo").toString();
			empNo = sf.get("EmpNo").toString();

			bp.port.Dept dept = new bp.port.Dept(empDept);
			String deptNo = dept.getParentNo();

			sql = "SELECT A.FK_Emp,FK_Dept FROM Port_DeptEmpStation A, WF_NodeStation B WHERE A.FK_Station=B.FK_Station AND B.FK_Node=" + town.getHisNode().getNodeID() + " AND A.FK_Dept='" + deptNo + "'";
			dt = DBAccess.RunSQLReturnTable(sql);
			/*if (dt.Rows.size() == 0)
			{
				Dept pDept = new Dept(deptNo);
				throw new RuntimeException("err@按照 [发送人上级部门指定的岗位] 计算接收人的时候出现错误，没有找到人，请检查节点绑定的岗位以及该部门【" + pDept.getName() + "】下的人员设置的岗位.");
			}*/
			if (dt.Rows.size() > 0)
				return dt;
			else
			{
				if (this.town.getHisNode().getHisWhenNoWorker() == false) {
					bp.port.Dept pDept = new bp.port.Dept(deptNo);
					throw new Exception("err@按照 [发送人上级部门指定的岗位] 计算接收人的时候出现错误，没有找到人，请检查节点绑定的岗位以及该部门【" + pDept.getName() + "】下的人员设置的岗位.");
				}
				else
					return dt;
			}
			
		}

			///#endregion 发送人的上级部门的负责人 2022.2.20 beijing.


			///#region 最后判断 - 按照岗位来执行。
		//从历史数据中获取接收人 2022-03-25这块代码注释，需要查询本部门的岗位
		//if (this.currWn.HisNode.IsStartNode == false)
		//{
		//    ps = new Paras();

		//    如果当前的节点不是开始节点， 从轨迹里面查询。
		//    sql = "SELECT DISTINCT FK_Emp  FROM Port_DeptEmpStation WHERE FK_Station IN "
		//       + "(SELECT FK_Station FROM WF_NodeStation WHERE FK_Node=" + town.HisNode.NodeID + ") "
		//       + "AND FK_Emp IN (SELECT FK_Emp FROM WF_GenerWorkerlist WHERE WorkID=" + dbStr + "WorkID AND FK_Node IN (" + DataType.PraseAtToInSql(town.HisNode.GroupStaNDs, true) + ") )";

		//    sql += " ORDER BY FK_Emp ";

		//    ps.SQL = sql;
		//    ps.Add("WorkID", this.WorkID);
		//    dt = DBAccess.RunSQLReturnTable(ps);
		//    如果能够找到.
		//    if (dt.Rows.size() >= 1)
		//        return dt;
		//}



		/* 如果执行节点 与 接受节点岗位集合一致 */
		String currGroupStaNDs = this.currWn.getHisNode().getGroupStaNDs();
		String toNodeTeamStaNDs = town.getHisNode().getGroupStaNDs();

		if (DataType.IsNullOrEmpty(currGroupStaNDs) == false && currGroupStaNDs.equals(toNodeTeamStaNDs) == true)
		{
			/* 说明，就把当前人员做为下一个节点处理人。*/
			DataRow dr = dt.NewRow();
			if (dt.Columns.size() == 0)
			{
				dt.Columns.Add("No");
			}

			dr.setValue(0, WebUser.getNo());
			dt.Rows.add(dr);
			return dt;
		}

		//获取当前人员信息的
		Hashtable ht = GetEmpDeptBySFModel();
		empDept = ht.get("DeptNo").toString();
		empNo = ht.get("EmpNo").toString();

		/* 如果执行节点 与 接受节点岗位集合不一致 */
		if ((DataType.IsNullOrEmpty(toNodeTeamStaNDs) == true && DataType.IsNullOrEmpty(currGroupStaNDs) == true) || currGroupStaNDs.equals(toNodeTeamStaNDs) == false)
		{
			/* 没有查询到的情况下, 先按照本部门计算。*/


			sql = "SELECT FK_Emp as No FROM Port_DeptEmpStation A, WF_NodeStation B         WHERE A.FK_Station=B.FK_Station AND B.FK_Node=" + dbStr + "FK_Node AND A.FK_Dept=" + dbStr + "FK_Dept";
			ps = new Paras();
			ps.SQL = sql;
			ps.Add("FK_Node", town.getHisNode().getNodeID());
			ps.Add("FK_Dept", empDept, false);

			dt = DBAccess.RunSQLReturnTable(ps);
			if (dt.Rows.size() == 0)
			{
				NodeStations nextStations = town.getHisNode().getNodeStations();
				if (nextStations.size() == 0)
				{
					throw new RuntimeException("@节点没有岗位:" + town.getHisNode().getNodeID() + "  " + town.getHisNode().getName());
				}
			}
			else
			{
				boolean isInit = false;
				for (DataRow dr : dt.Rows)
				{
					if (dr.getValue(0).toString().equals(WebUser.getNo()))
					{
						/* 如果岗位分组不一样，并且结果集合里还有当前的人员，就说明了出现了当前操作员，拥有本节点上的岗位也拥有下一个节点的工作岗位
						 导致：节点的分组不同，传递到同一个人身上。 */
						isInit = true;
					}
				}


///#warning edit by peng, 用来确定不同岗位集合的传递包含同一个人的处理方式。

				//  if (isInit == false || isInit == true)
				return dt;
			}
		}

		/*这里去掉了向下级别寻找的算法. */


		/* 没有查询到的情况下, 按照最大匹配数 提高一个级别计算，递归算法未完成。
		 * 因为:以上已经做的岗位的判断，就没有必要在判断其它类型的节点处理了。
		 * */

		Object tempVar = empDept;
		String nowDeptID = tempVar instanceof String ? (String)tempVar : null;

		//第1步:直线父级寻找.
		while (true)
		{
			Dept myDept = new Dept(nowDeptID);
			nowDeptID = myDept.getParentNo();
			if (nowDeptID.equals("-1") || nowDeptID.toString().equals("0"))
			{
				break; //一直找到了最高级仍然没有发现，就跳出来循环从当前操作员人部门向下找。
				//throw new RuntimeException("@按岗位计算没有找到(" + town.getHisNode().getName() + ")接受人.");
			}

			//检查指定的父部门下面是否有该人员.
			DataTable mydtTemp = this.Func_GenerWorkerList_SpecDept(nowDeptID, empNo);
			if (mydtTemp.Rows.size() != 0)
			{
				return mydtTemp;
			}

			continue;
		}

		//第2步：父级的平级.如果是0查找，1不查找父级的平级
		int StationFindWay = town.getHisNode().GetParaInt("StationFindWay");
		if(StationFindWay == 0)
		{
			Object tempVar2 = empDept;
			nowDeptID = tempVar2 instanceof String ? (String)tempVar2 : null;
			while (true)
			{
				Dept myDept = new Dept(nowDeptID);
				nowDeptID = myDept.getParentNo();
				if (nowDeptID.equals("-1") || nowDeptID.toString().equals("0"))
				{
					break; //一直找到了最高级仍然没有发现，就跳出来循环从当前操作员人部门向下找。
					//throw new RuntimeException("@按岗位计算没有找到(" + town.getHisNode().getName() + ")接受人.");
				}

				//该部门下的所有子部门是否有人员.
				DataTable mydtTemp = Func_GenerWorkerList_SpecDept_SameLevel(nowDeptID, empNo);
				if (mydtTemp.Rows.size() != 0)
				{
					return mydtTemp;
				}
				continue;
			}
		}


		/*如果向上找没有找到，就考虑从本级部门上向下找。只找一级下级的平级 */
		Object tempVar3 = empDept;
		nowDeptID = tempVar3 instanceof String ? (String)tempVar3 : null;

		//递归出来子部门下有该岗位的人员
		DataTable mydt = Func_GenerWorkerList_SpecDept_SameLevel(nowDeptID, empNo);

		if ((mydt == null || mydt.Rows.size() == 0) && this.town.getHisNode().getHisWhenNoWorker() == false)
		{
			//如果递归没有找到人,就全局搜索岗位.
			sql = "SELECT A.FK_Emp FROM  Port_DeptEmpStation A, WF_NodeStation B WHERE A.FK_Station=B.FK_Station AND B.FK_Node=" + dbStr + "FK_Node ORDER BY A.FK_Emp";
			ps = new Paras();
			ps.Add("FK_Node", town.getHisNode().getNodeID());
			ps.SQL = sql;
			dt = DBAccess.RunSQLReturnTable(ps);

			if (dt.Rows.size() > 0)
			{
				return dt;
			}
			if (this.town.getHisNode().getHisWhenNoWorker() == false)
			{
				throw new RuntimeException("@按岗位智能计算没有找到(" + town.getHisNode().getName() + ")接受人 @当前工作人员:" + WebUser.getNo() + ",名称:" + WebUser.getName() + " , 部门编号:" + WebUser.getFK_Dept()+ " 部门名称：" + WebUser.getFK_DeptName());
			}

			if (dt.Rows.size() == 0)
			{
				mydt = new DataTable();
				mydt.Columns.Add(new DataColumn("No", String.class));
				mydt.Columns.Add(new DataColumn("Name", String.class));
			}
		}

		return mydt;

			///#endregion  按照岗位来执行。
	}

	private Hashtable GetEmpDeptBySFModel() throws Exception {
		Node nd = town.getHisNode();
		Hashtable ht = new Hashtable();
		//身份模式.
		int sfModel = nd.GetParaInt("ShenFenModel", 0);

		//身份参数.
		String sfVal = nd.GetParaString("ShenFenVal");

		//按照当前节点的身份计算
		if (sfModel == 0)
		{
			ht.put("EmpNo", WebUser.getNo());
			ht.put("DeptNo", WebUser.getFK_Dept());
			return ht;
		}

		//按照指定节点的身份计算.
		if (sfModel == 1)
		{
			if (DataType.IsNullOrEmpty(sfVal))
			{
				sfVal = String.valueOf(currWn.getHisNode().getNodeID());
			}
			Paras ps = new Paras();
			ps.SQL = "SELECT FK_Emp,FK_Dept FROM WF_GenerWorkerList WHERE (WorkID=" + dbStr + "OID OR WorkID="+dbStr+"FID) AND FK_Node=" + dbStr + "FK_Node Order By RDT DESC";
			ps.Add("OID", this.WorkID);
			ps.Add("FID", currWn.getHisWork().getFID());
			ps.Add("FK_Node", Integer.parseInt(sfVal));

			DataTable dt = DBAccess.RunSQLReturnTable(ps);
			if (dt.Rows.size() == 0)
			{
				throw new RuntimeException("err@不符合常理，没有找到数据");
			}
			ht.put("EmpNo", dt.Rows.get(0).getValue(0).toString());
			ht.put("DeptNo", dt.Rows.get(0).getValue(1).toString());
		}

		//按照 字段的值的人员编号作为身份计算.
		if (sfModel == 2)
		{
			//获得字段的值.
			String empNo = "";
			if (currWn.getHisNode().getHisFormType() == NodeFormType.RefOneFrmTree)
			{
				empNo = currWn.getHisWork().GetValStrByKey(sfVal);
			}
			else
			{
				empNo = currWn.rptGe.GetValStrByKey(sfVal);
			}
			Emp emp = new Emp();
			emp.setUserID(empNo);
			if (emp.RetrieveFromDBSources() == 0)
			{
				throw new RuntimeException("err@根据字段值:" + sfVal + "在Port_Emp中没有找到人员信息");
			}
			ht.put("EmpNo", emp.getNo());
			ht.put("DeptNo", emp.getFK_Dept());
		}
		return ht;
	}
	/** 
	 找部门的领导
	 
	 @return 
	*/
	private DataTable ByDeptLeader() throws Exception {

		Node nd = town.getHisNode();

		//身份模式.
		int sfModel = nd.GetParaInt("ShenFenModel", 0);

		//身份参数.
		String sfVal = nd.GetParaString("ShenFenVal");

		//按照当前节点的身份计算.
		if (sfModel == 0)
		{
			return ByDeptLeader_Nodes(String.valueOf(currWn.getHisNode().getNodeID()));
		}

		//按照指定节点的身份计算.
		if (sfModel == 1)
		{
			return ByDeptLeader_Nodes(sfVal);
		}

		//按照 字段的值的人员编号作为身份计算.
		if (sfModel == 2)
		{
			//获得字段的值.
			String empNo = "";
			if (currWn.getHisNode().getHisFormType() == NodeFormType.RefOneFrmTree)
			{
				empNo = currWn.getHisWork().GetValStrByKey(sfVal);
			}
			else
			{
				empNo = currWn.rptGe.GetValStrByKey(sfVal);
			}
			Emp emp = new Emp();
			emp.setUserID(empNo);
			if (emp.RetrieveFromDBSources() == 0)
			{
				throw new RuntimeException("err@根据字段值:" + sfVal + "在Port_Emp中没有找到人员信息");
			}
			return ByDeptLeader_Fields(emp.getNo(), emp.getFK_Dept());
		}

		throw new RuntimeException("err@没有判断的身份模式.");
	}
	/** 
	 找部门的分管领导
	 
	 @return 
	*/
	private DataTable ByDeptShipLeader() throws Exception {

		Node nd = town.getHisNode();

		//身份模式.
		int sfModel = nd.GetParaInt("ShenFenModel", 0);

		//身份参数.
		String sfVal = nd.GetParaString("ShenFenVal");

		//按照当前节点的身份计算
		if (sfModel == 0)
		{
			return ByDeptShipLeader_Nodes(String.valueOf(currWn.getHisNode().getNodeID()));
		}

		//按照指定节点的身份计算.
		if (sfModel == 1)
		{
			return ByDeptShipLeader_Nodes(sfVal);
		}

		//按照 字段的值的人员编号作为身份计算.
		if (sfModel == 2)
		{
			//获得字段的值.
			String empNo = "";
			if (currWn.getHisNode().getHisFormType() == NodeFormType.RefOneFrmTree)
			{
				empNo = currWn.getHisWork().GetValStrByKey(sfVal);
			}
			else
			{
				empNo = currWn.rptGe.GetValStrByKey(sfVal);
			}
			Emp emp = new Emp();
			emp.setUserID(empNo);
			if (emp.RetrieveFromDBSources() == 0)
			{
				throw new RuntimeException("err@根据字段值:" + sfVal + "在Port_Emp中没有找到人员信息");
			}
			return ByDeptShipLeader_Fields(emp.getNo(), emp.getFK_Dept());
		}

		throw new RuntimeException("err@没有判断的身份模式.");
	}
	private DataTable ByDeptLeader_Nodes(String nodes) throws Exception {
		DataTable dt = null;
		//查找指定节点的人员， 如果没有节点，就是当前的节点.
		if (DataType.IsNullOrEmpty(nodes) == true)
		{
			nodes = String.valueOf(this.currWn.getHisNode().getNodeID());
		}

		Paras ps = new Paras();
		ps.SQL = "SELECT FK_Emp,FK_Dept FROM WF_GenerWorkerList WHERE WorkID=" + dbStr + "OID AND FK_Node=" + dbStr + "FK_Node Order By RDT DESC";
		ps.Add("OID", this.WorkID);
		ps.Add("FK_Node", Integer.parseInt(nodes));

		dt = DBAccess.RunSQLReturnTable(ps);
		if (dt.Rows.size() == 0)
		{
			throw new RuntimeException("err@不符合常理，没有找到数据");
		}
		String empNo = dt.Rows.get(0).getValue(0).toString();
		String deptNo = dt.Rows.get(0).getValue(1).toString();
		return ByDeptLeader_Fields(empNo, deptNo);
	}
	private DataTable ByDeptShipLeader_Nodes(String nodes) throws Exception {
		DataTable dt = null;
		//查找指定节点的人员， 如果没有节点，就是当前的节点.
		if (DataType.IsNullOrEmpty(nodes) == true)
		{
			nodes = String.valueOf(this.currWn.getHisNode().getNodeID());
		}

		Paras ps = new Paras();
		ps.SQL = "SELECT FK_Emp,FK_Dept FROM WF_GenerWorkerList WHERE WorkID=" + dbStr + "OID AND FK_Node=" + dbStr + "FK_Node Order By RDT DESC";
		ps.Add("OID", this.WorkID);
		ps.Add("FK_Node", Integer.parseInt(nodes));

		dt = DBAccess.RunSQLReturnTable(ps);
		if (dt.Rows.size() == 0)
		{
			throw new RuntimeException("err@不符合常理，没有找到数据");
		}
		String empNo = dt.Rows.get(0).getValue(0).toString();
		String deptNo = dt.Rows.get(0).getValue(1).toString();
		return ByDeptShipLeader_Fields(empNo, deptNo);
	}
	private DataTable ByDeptLeader_Fields(String empNo, String empDept) throws Exception {
		String sql = "SELECT Leader FROM Port_Dept WHERE No='" + empDept + "'";
		String myEmpNo = DBAccess.RunSQLReturnStringIsNull(sql, null);

		if (DataType.IsNullOrEmpty(myEmpNo) == true)
		{
			//如果部门的负责人为空，则查找Port_Emp中的Learder信息
			if (bp.difference.SystemConfig.getCCBPMRunModel() == CCBPMRunModel.SAAS)
			{
				sql = "SELECT Leader FROM Port_Emp WHERE UserID='" + empNo + "' AND OrgNo='" + WebUser.getOrgNo() + "'";
			}
			else
			{
				sql = "SELECT Leader FROM Port_Emp WHERE No='" + empNo + "'";
			}

			myEmpNo = DBAccess.RunSQLReturnStringIsNull(sql, null);
			if (DataType.IsNullOrEmpty(myEmpNo) == true)
			{
				Dept mydept = new Dept(empDept);
				throw new RuntimeException("@流程设计错误:下一个节点(" + town.getHisNode().getName() + ")设置的按照部门负责人计算，当前您的部门(" + mydept.getNo() + "," + mydept.getName() + ")没有维护负责人 . ");
			}
		}

		//如果有这个人,并且是当前人员，说明他本身就是经理或者部门负责人.
		if (myEmpNo.equals(empNo) == true)
		{
			sql = "SELECT Leader FROM Port_Dept WHERE No=(SELECT PARENTNO FROM PORT_DEPT WHERE NO='" + empDept + "')";
			myEmpNo = DBAccess.RunSQLReturnStringIsNull(sql, null);
			if (DataType.IsNullOrEmpty(myEmpNo) == true)
			{
				Dept mydept = new Dept(empDept);
				throw new RuntimeException("@流程设计错误:下一个节点(" + town.getHisNode().getName() + ")设置的按照部门负责人计算，当前您的部门(" + mydept.getName() + ")上级没有维护负责人 . ");
			}
		}
		return DBAccess.RunSQLReturnTable(sql);
	}
	private DataTable ByDeptShipLeader_Fields(String empNo, String empDept) throws Exception {
		Dept mydept = new Dept(empDept);
		Paras ps = new Paras();
		ps.Add("No", empDept, false);
		ps.SQL = "SELECT ShipLeader FROM Port_Dept WHERE No='" + empDept + "'";

		DataTable dt = DBAccess.RunSQLReturnTable(ps);
		if (dt.Rows.size() != 0 && dt.Rows.get(0).getValue(0) != null && DataType.IsNullOrEmpty(dt.Rows.get(0).getValue(0).toString()) == true)
		{
			//如果部门的负责人为空，则查找Port_Emp中的Learder信息
			ps.clear();
			if (bp.difference.SystemConfig.getCCBPMRunModel() == CCBPMRunModel.SAAS)
			{
				ps.SQL = "SELECT ShipLeader FROM Port_Emp WHERE UserID='" + empNo + "' AND OrgNo='" + WebUser.getOrgNo() + "'";
			}
			else
			{
				ps.SQL = "SELECT ShipLeader FROM Port_Emp WHERE No='" + empNo + "'";
			}

			dt = DBAccess.RunSQLReturnTable(ps);
			if (dt.Rows.size() != 0 && dt.Rows.get(0).getValue(0) != null && DataType.IsNullOrEmpty(dt.Rows.get(0).getValue(0).toString()) == true)
			{
				throw new RuntimeException("@流程设计错误:下一个节点(" + town.getHisNode().getName() + ")设置的按照部门负责人计算，当前您的部门(" + mydept.getNo() + "," + mydept.getName() + ")没有维护负责人 . ");
			}
		}

		//如果有这个人,并且是当前人员，说明他本身就是经理或者部门负责人.
		if (dt.Rows.get(0).getValue(0).toString().equals(empNo) == true)
		{
			ps.SQL = "SELECT ShipLeader FROM Port_Dept WHERE No=(SELECT PARENTNO FROM PORT_DEPT WHERE NO='" + empDept + "')";
			dt = DBAccess.RunSQLReturnTable(ps);
			if (dt.Rows.size() == 0)
			{
				throw new RuntimeException("@流程设计错误:下一个节点(" + town.getHisNode().getName() + ")设置的按照部门负责人计算，当前您的部门(" + mydept.getName() + ")上级没有维护负责人 . ");
			}
		}
		return dt;
	}
	/** 
	 获得指定部门下是否有该岗位的人员.
	 
	 param deptNo 部门编号
	 param empNo 人员编号
	 @return 
	*/
	public final DataTable Func_GenerWorkerList_SpecDept(String deptNo, String empNo)
	{
		String sql;

		Paras ps = new Paras();
		if (this.town.getHisNode().isExpSender() == true)
		{
			/* 不允许包含当前处理人. */
			sql = "SELECT FK_Emp as No FROM Port_DeptEmpStation A, WF_NodeStation B WHERE A.FK_Station=B.FK_Station AND B.FK_Node=" + dbStr + "FK_Node AND A.FK_Dept=" + dbStr + "FK_Dept AND A.FK_Emp!=" + dbStr + "FK_Emp";

			ps.SQL = sql;
			ps.Add("FK_Node", town.getHisNode().getNodeID());
			ps.Add("FK_Dept", deptNo, false);
			ps.Add("FK_Emp", empNo, false);
		}
		else
		{
			sql = "SELECT FK_Emp as No FROM Port_DeptEmpStation A, WF_NodeStation B WHERE A.FK_Station=B.FK_Station AND B.FK_Node=" + dbStr + "FK_Node AND A.FK_Dept=" + dbStr + "FK_Dept";

			ps.SQL = sql;
			ps.Add("FK_Node", town.getHisNode().getNodeID());
			ps.Add("FK_Dept", deptNo, false);
		}

		DataTable dt = DBAccess.RunSQLReturnTable(ps);
		return dt;
	}
	/** 
	 获得本部门的人员
	 
	 param deptNo
	 param emp1
	 @return 
	*/
	public final DataTable Func_GenerWorkerList_SpecDept_SameLevel(String deptNo, String empNo)
	{
		String sql;

		Paras ps = new Paras();
		if (this.town.getHisNode().isExpSender() == true)
		{
			/* 不允许包含当前处理人. */
			sql = "SELECT FK_Emp as No FROM Port_DeptEmpStation A, WF_NodeStation B, Port_Dept C WHERE A.FK_Dept=C.No AND A.FK_Station=B.FK_Station AND B.FK_Node=" + dbStr + "FK_Node AND C.ParentNo=" + dbStr + "FK_Dept AND A.FK_Emp!=" + dbStr + "FK_Emp";

			ps.SQL = sql;
			ps.Add("FK_Node", town.getHisNode().getNodeID());
			ps.Add("FK_Dept", deptNo, false);
			ps.Add("FK_Emp", empNo, false);
		}
		else
		{
			sql = "SELECT FK_Emp as No FROM Port_DeptEmpStation A, WF_NodeStation B, Port_Dept C  WHERE A.FK_Dept=C.No AND A.FK_Station=B.FK_Station AND B.FK_Node=" + dbStr + "FK_Node AND C.ParentNo=" + dbStr + "FK_Dept";
			ps.SQL = sql;
			ps.Add("FK_Node", town.getHisNode().getNodeID());
			ps.Add("FK_Dept", deptNo, false);
		}

		DataTable dt = DBAccess.RunSQLReturnTable(ps);
		return dt;
	}
	/** 
	 执行找人
	 
	 @return 
	*/
	public final DataTable DoIt(Flow fl, WorkNode currWn, WorkNode toWn) throws Exception {
		// 给变量赋值.
		this.fl = fl;
		this.currWn = currWn;
		this.town = toWn;
		this.WorkID = currWn.getWorkID();

		if (this.town.getHisNode().isGuestNode())
		{
			/*到达的节点是客户参与的节点. add by zhoupeng 2016.5.11*/
			DataTable mydt = new DataTable();
			mydt.Columns.Add("No", String.class);
			mydt.Columns.Add("Name", String.class);

			DataRow dr = mydt.NewRow();
			dr.setValue("No", "Guest");
			dr.setValue("Name", "外部用户");
			mydt.Rows.add(dr);
			return mydt;
		}

		//如果到达的节点是按照workflow的模式。
		if (toWn.getHisNode().getHisDeliveryWay() != DeliveryWay.ByCCFlowBPM)
		{
			DataTable re_dt = this.FindByWorkFlowModel();
			if (re_dt.Rows.size() == 1)
			{
				return re_dt; //如果只有一个人，就直接返回，就不处理了。
			}


				///#region 根据配置追加接收人 by dgq 2015.5.18

			String paras = this.town.getHisNode().getDeliveryParas();
			if (paras.contains("@Spec"))
			{
				//如果返回null ,则创建表
				if (re_dt == null)
				{
					re_dt = new DataTable();
					re_dt.Columns.Add("No", String.class);
				}

				//获取配置规则
				String[] reWays = this.town.getHisNode().getDeliveryParas().split("[@]", -1);
				for (String reWay : reWays)
				{
					if (DataType.IsNullOrEmpty(reWay))
					{
						continue;
					}
					String[] specItems = reWay.split("[=]", -1);
					//配置规则错误
					if (specItems.length != 2)
					{
						continue;
					}
					//规则名称，SpecStations、SpecEmps
					String specName = specItems[0];
					//规则内容
					String specContent = specItems[1];
					switch (specName)
					{
						case "SpecStations": //按岗位
							String[] stations = specContent.split("[,]", -1);
							for (String station : stations)
							{
								if (DataType.IsNullOrEmpty(station))
								{
									continue;
								}

								//获取岗位下的人员
								String sql = "";
								if (bp.difference.SystemConfig.getCCBPMRunModel() == CCBPMRunModel.Single)
								{
									sql = "SELECT FK_Emp FROM Port_DeptEmpStation WHERE FK_Station='" + station + "'";
								}
								else
								{
									sql = "SELECT FK_Emp FROM Port_DeptEmpStation WHERE FK_Station='" + station + "' AND OrgNo='" + WebUser.getOrgNo() + "'";
								}

								DataTable dt_Emps = DBAccess.RunSQLReturnTable(sql);
								for (DataRow empRow : dt_Emps.Rows)
								{
									//排除为空编号
									if (empRow.getValue(0) == null || DataType.IsNullOrEmpty(empRow.getValue(0).toString()))
									{
										continue;
									}

									DataRow dr = re_dt.NewRow();
									dr.setValue(0, empRow.getValue(0));
									re_dt.Rows.add(dr);
								}
							}
							break;
						case "SpecEmps": //按人员编号
							String[] myEmpStrs = specContent.split("[,]", -1);
							for (String emp : myEmpStrs)
							{
								//排除为空编号
								if (DataType.IsNullOrEmpty(emp))
								{
									continue;
								}

								DataRow dr = re_dt.NewRow();
								dr.setValue(0, emp);
								re_dt.Rows.add(dr);
							}
							break;
					}
				}
			}

				///#endregion

			//本节点接收人不允许包含上一步发送人 。
			if (this.town.getHisNode().isExpSender() == true && re_dt.Rows.size() >= 2)
			{
				/*
				 * 排除了接受人分组的情况, 因为如果有了分组，就破坏了分组的结构了.
				 * 
				 */
				//复制表结构
				DataTable dt =re_dt.clone();
				for (DataRow row : re_dt.Rows)
				{
					//排除当前登录人
					if (row.getValue(0).toString().equals(WebUser.getNo()))
					{
						continue;
					}

					DataRow dr = dt.NewRow();
					dr.setValue(0, row.getValue(0));

					if (row.table.Columns.size() == 2)
						dr.setValue(1, row.getValue(1));

					dt.Rows.add(dr);
				}
				return dt;
			}
			return re_dt;
		}

		// 规则集合.
		FindWorkerRoles ens = new FindWorkerRoles(town.getHisNode().getNodeID());
		for (FindWorkerRole en : ens.ToJavaList())
		{
			en.fl = this.fl;
			en.town = toWn;
			en.currWn = currWn;
			en.HisNode = currWn.getHisNode();
			en.WorkID = this.WorkID;

			DataTable dt = en.GenerWorkerOfDataTable();
			if (dt == null || dt.Rows.size() == 0)
			{
				continue;
			}

			//本节点接收人不允许包含上一步发送人
			if (this.town.getHisNode().isExpSender() == true)
			{
				DataTable re_dt = dt.clone();
				for (DataRow row : dt.Rows)
				{
					if (row.getValue(0).toString().equals(WebUser.getNo()))
					{
						continue;
					}
					DataRow dr = re_dt.NewRow();
					dr.setValue(0, row.getValue(0));
					re_dt.Rows.add(dr);
				}
				return re_dt;
			}
			return dt;
		}

		//没有找到人的情况，就返回空.
		return null;
	}


}