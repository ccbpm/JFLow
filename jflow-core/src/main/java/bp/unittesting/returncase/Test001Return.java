package bp.unittesting.returncase;

import bp.wf.*;
import bp.wf.data.*;
import bp.wf.template.*;
import bp.en.*;
import bp.da.*;
import bp.web.*;
import bp.unittesting.*;
import bp.unittesting.*;
import java.util.*;

public class Test001Return extends TestBase
{
	/** 
	 测试退回
	*/
	public Test001Return()
	{
		this.Title = "财务报销流程的退回功能";
		this.DescIt = "发送的退回，与原路返回方式的退回.";
		this.editState = EditState.Passed;
	}
	/** 
	 说明 ：此测试针对于演示环境中的 001 流程编写的单元测试代码。
	 涉及到了: 创建，发送，撤销，方向条件、退回等功能。
	 * @throws Exception 
	*/
	@Override
	public void Do() throws Exception
	{
		String fk_flow = "001";
		String startUser = "zhoutianjiao";
		bp.port.Emp starterEmp = new bp.port.Emp(startUser);


		Flow fl = new Flow(fk_flow);

		//让周天娇登录, 在以后，就可以访问WebUser.getNo(), WebUser.getName() 。
		bp.wf.Dev2Interface.Port_Login(startUser);

		//创建空白工作, 发起开始节点.
		long workid = bp.wf.Dev2Interface.Node_CreateBlankWork(fk_flow);

		//执行发送，并获取发送对象,.
		SendReturnObjs objs = bp.wf.Dev2Interface.Node_SendWork(fk_flow, workid);

		//下一个工作者.
		String nextUser = objs.getVarAcceptersID();
		// 下一个发送的节点ID
		int nextNodeID = objs.getVarToNodeID();

		// 让 nextUser = qifenglin 登录.
		bp.wf.Dev2Interface.Port_Login(nextUser);

		//获取第二个节点上的退回集合.
		DataTable dt = bp.wf.Dev2Interface.DB_GenerWillReturnNodes(objs.getVarToNodeID(), workid, 0);


			///检查获取第二步退回的节点数据是否符合预期.
		if (dt.Rows.size() != 1)
		{
			throw new RuntimeException("@在第二个节点是获取退回节点集合时，不符合数据预期,应该只能获取一个退回节点，现在是:" + dt.Rows.size());
		}

		int nodeID = Integer.parseInt(dt.Rows.get(0).getValue("No").toString());
		if (nodeID != 101)
		{
			throw new RuntimeException("@在第二个节点是获取退回节点集合时，被退回的点应该是101");
		}

		String RecNo = dt.Rows.get(0).getValue("Rec").toString();
		if (!startUser.equals(RecNo))
		{
			throw new RuntimeException("@在第二个节点是获取退回节点集合时，被退回人应该是" + startUser + ",现在是" + RecNo);
		}

			/// 检查获取第二步退回的节点数据是否符合预期.

		//在第二个节点执行退回.
		bp.wf.Dev2Interface.Node_ReturnWork(fk_flow, workid, 0, objs.getVarToNodeID(), 101, "退回测试", false);


			///检查退回后的数据完整性.
		GenerWorkFlow gwf = new GenerWorkFlow(workid);
		if (gwf.getWFState() != WFState.ReturnSta)
		{
			throw new RuntimeException("@执行退回，流程状态应该是退回,现在是:" + gwf.getWFState().toString());
		}

		if (gwf.getFK_Node() != 101)
		{
			throw new RuntimeException("@执行退回，当前节点应该是101, 现在是" + String.valueOf(gwf.getFK_Node()));
		}

		sql = "SELECT WFState from nd1rpt where oid=" + workid;
		int wfstate = DBAccess.RunSQLReturnValInt(sql, -1);
		if (wfstate != WFState.ReturnSta.getValue())
		{
			throw new RuntimeException("@在第二个节点退回后rpt数据不正确，流程状态应该是退回,现在是:" + wfstate);
		}

		//检查流程报表是否符合需求。
		sql = "SELECT * FROM " + fl.getPTable() + " WHERE oid=" + workid;
		dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() != 1)
		{
			throw new RuntimeException("@流程报表数据被删除了.");
		}
		for (DataColumn dc : dt.Columns)
		{
			String val = dt.Rows.get(0).getValue(dc.ColumnName).toString();
			switch (dc.ColumnName)
			{
				case GERptAttr.Title:
					if (DataType.IsNullOrEmpty(val))
					{
						throw new RuntimeException("@退回后流程标题丢失了");
					}
					break;
				case GERptAttr.FID:
					if (!val.equals("0"))
					{
						throw new RuntimeException("@应当是0");
					}
					break;
				case GERptAttr.FK_Dept:
					if (!val.equals(starterEmp.getFK_Dept()))
					{
						throw new RuntimeException("@发起人的部门发生了变化，应当是(" + starterEmp.getFK_Dept() + "),现在是:" + val);
					}
					break;
				case GERptAttr.FK_NY:
					if (!val.equals(DataType.getCurrentYearMonth()))
					{
						throw new RuntimeException("@应当是" + DataType.getCurrentYearMonth() + ", 现在是:" + val);
					}
					break;
				case GERptAttr.FlowDaySpan:
					if (!val.equals("0"))
					{
						throw new RuntimeException("@应当是 0 , 现在是:" + val);
					}
					break;
				//case GERptAttr.FlowEmps:
				//    if (val.Contains("zhanghaicheng") == false || val.Contains("zhoupeng") == false)
				//        throw new Exception("@应当包含的人员，现在不存在, 现在是:" + val);
				//    break;
				//case GERptAttr.FlowEnder:
				//    if (val != "zhanghaicheng")
				//        throw new Exception("@应当是 zhanghaicheng , 现在是:" + val);
				//    break;
				case GERptAttr.FlowEnderRDT:
					if (val.contains(DataType.getCurrentDate()) == false)
					{
						throw new RuntimeException("@应当是 当前日期, 现在是:" + val);
					}
					break;
				case GERptAttr.FlowEndNode:
					if (!val.equals("101"))
					{
						throw new RuntimeException("@应当是 101, 现在是:" + val);
					}
					break;
				case GERptAttr.FlowStarter:
					if (!startUser.equals(val))
					{
						throw new RuntimeException("@应当是 " + startUser + ", 现在是:" + val);
					}
					break;
				case GERptAttr.FlowStartRDT:
					if (DataType.IsNullOrEmpty(val))
					{
						throw new RuntimeException("@应当不能为空,现在是:" + val);
					}
					break;
				case GERptAttr.WFState:
					if (Integer.parseInt(val) != WFState.ReturnSta.getValue())
					{
						throw new RuntimeException("@应当是  WFState.Complete 现在是" + val);
					}
					break;
				default:
					break;
			}
		}

			/// 检查退回后的数据完整性.

		//让发起人登录，并发送到部门经理审批.
		bp.wf.Dev2Interface.Port_Login(startUser);
		objs = bp.wf.Dev2Interface.Node_SendWork(fk_flow, workid);

		//让第二步骤的qifengin登录并处理它，发送到总经理审批。
		bp.wf.Dev2Interface.Port_Login(objs.getVarAcceptersID());

		Hashtable ht = new Hashtable();
		ht.put("HeJiFeiYong", 999999); //金额大于1w 就让它发送到总经理审批节点上去.
		objs = bp.wf.Dev2Interface.Node_SendWork(fk_flow, workid, ht);

		// 让zhoupeng登录, 并执行退回.
		bp.wf.Dev2Interface.Port_Login(objs.getVarAcceptersID());

		//获取第三个节点上的退回集合.
		dt = bp.wf.Dev2Interface.DB_GenerWillReturnNodes(objs.getVarToNodeID(), workid, 0);


			///检查获取第二步退回的节点数据是否符合预期.
		if (dt.Rows.size() != 2)
		{
			throw new RuntimeException("@在第三个节点是获取退回节点集合时，不符合数据预期,应该 获取2个退回节点，现在是:" + dt.Rows.size());
		}

		boolean isHave101 = false;
		boolean isHave102 = false;

		for (DataRow dr : dt.Rows)
		{
			if (dr.getValue(0).toString().equals("101"))
			{
				isHave101 = true;
			}

			if (dr.getValue(0).toString().equals("102"))
			{
				isHave102 = true;
			}
		}

		if (isHave101 == false || isHave102 == false)
		{
			 throw new RuntimeException("@获得将要可以退回的节点集合错误,缺少101，或者102节点.");
		}

		//if (dt.Rows.get(0).getValue(0].ToString() != "101")
		//    throw new Exception("@应该是101,现在是:" + dt.Rows.get(0).getValue(0].ToString());
		//if (dt.Rows[1][0].ToString() != "102")
		//    throw new Exception("@应该是102,现在是:" + dt.Rows.get(0).getValue(0].ToString());


			/// 检查获取第二步退回的节点数据是否符合预期.

		//在第3个节点执行退回.
		bp.wf.Dev2Interface.Node_ReturnWork(fk_flow, workid, 0, objs.getVarToNodeID(), 101, "总经理-退回测试", false);


			///检查总经理-退回后的数据完整性.
		gwf = new GenerWorkFlow(workid);
		if (gwf.getWFState() != WFState.ReturnSta)
		{
			throw new RuntimeException("@执行退回，流程状态应该是退回,现在是:" + gwf.getWFState().toString());
		}

		if (gwf.getFK_Node() != 101)
		{
			throw new RuntimeException("@执行退回，当前节点应该是101, 现在是" + String.valueOf(gwf.getFK_Node()));
		}

			/// 检查退回后的数据完整性.

		// 让发起人登录, 并执行发送.
		bp.wf.Dev2Interface.Port_Login(startUser);
		objs = bp.wf.Dev2Interface.Node_SendWork(fk_flow, workid);

		// 让部门经理登录登录, 并执行发送.
		bp.wf.Dev2Interface.Port_Login(objs.getVarAcceptersID());
		ht = new Hashtable();
		ht.put("HeJiFeiYong", 999999);
		objs = bp.wf.Dev2Interface.Node_SendWork(fk_flow, workid, ht, null, 103, "zhoupeng");

		//让总经理登录.
		bp.wf.Dev2Interface.Port_Login("zhoupeng");

		//执行退回并原路返回.
		bp.wf.Dev2Interface.Node_ReturnWork(fk_flow, workid, 0, objs.getVarToNodeID(), 101, "总经理-退回并原路返回-测试", true);

		//让发起人登录, 此人在发起时，应该直接发送给第三个节点的退回人,就是zhoupeng才正确.
		bp.wf.Dev2Interface.Port_Login(startUser);
		SendReturnObjs objsNew = bp.wf.Dev2Interface.Node_SendWork(fk_flow, workid);


			///开始检查数据是否完整.
		if (!objsNew.getVarAcceptersID().equals("zhoupeng"))
		{
			throw new RuntimeException("@退回并原路返回错误，应该发送给 zhoupeng，但是目前发送到了:" + objsNew.getVarAcceptersID());
		}

		if (objsNew.getVarToNodeID() != 103)
		{
			throw new RuntimeException("@退回并原路返回错误，应该发送给103，但是目前发送到了:" + objsNew.getVarToNodeID());
		}

			///
	}
}