package bp.unittesting.returncase;

import bp.wf.*;
import bp.en.*;
import bp.da.*;
import bp.web.*;
import bp.unittesting.*;
import bp.unittesting.*;

public class ReturnByDesigner extends TestBase
{
	/** 
	 测试按轨迹退回
	*/
	public ReturnByDesigner()
	{
		this.Title = "测试按轨迹退回";
		this.DescIt = "对应测试用例 031流程-按轨迹退回";
		this.editState = EditState.Passed;
	}
	/** 
	 执行
	 * @throws Exception 
	*/
	@Override
	public void Do() throws Exception
	{
		String fk_flow = "031";
		String startUser = "zhangyifan";

		Flow fl = new Flow(fk_flow);

		bp.wf.Dev2Interface.Port_Login(startUser);

		//创建空白工作, 发起开始节点.
		long workid = bp.wf.Dev2Interface.Node_CreateBlankWork(fk_flow);

		//执行发送，并获取发送对象,.
		SendReturnObjs objs = bp.wf.Dev2Interface.Node_SendWork(fk_flow, workid);

		// 让 下一个工作者登录.
		bp.wf.Dev2Interface.Port_Login(objs.getVarAcceptersID());

		//让第二个节点执行发送.
		objs = bp.wf.Dev2Interface.Node_SendWork(fk_flow, workid);
		if (!objs.getVarAcceptersID().equals("zhoupeng"))
		{
			throw new RuntimeException("@此节点的执行人员应该是zhoupeng.");
		}

		// 让 第三个工作者登录.
		bp.wf.Dev2Interface.Port_Login(objs.getVarAcceptersID());


		//获取第三个节点上的退回集合.
		DataTable dt = bp.wf.Dev2Interface.DB_GenerWillReturnNodes(objs.getVarToNodeID(), workid, 0);


			///检查获取第二步退回的节点数据是否符合预期.
		if (dt.Rows.size() != 1)
		{
			throw new RuntimeException("@在第3个节点是获取退回节点集合时，不符合数据预期,应该只能获取一个退回节点，现在是:" + dt.Rows.size());
		}

		int nodeID = Integer.parseInt(dt.Rows.get(0).getValue("No").toString());
		if (nodeID != 3101)
		{
			throw new RuntimeException("@在第3个节点是获取退回节点集合时，被退回的点应该是3101");
		}
		String RecNo = dt.Rows.get(0).getValue("Rec").toString();
		if (!startUser.equals(RecNo))
		{
			throw new RuntimeException("@在第3个节点是获取退回节点集合时，被退回人应该是" + startUser + ",现在是" + RecNo);
		}

			/// 检查获取第二步退回的节点数据是否符合预期.

		//执行退回,当前节点编号.
		bp.wf.Dev2Interface.Node_ReturnWork(fk_flow, workid, 0, objs.getVarToNodeID(), 3101, "按轨迹退回测试", false);


			///检查退回后的数据完整性.
		GenerWorkFlow gwf = new GenerWorkFlow(workid);
		if (gwf.getWFState() != WFState.ReturnSta)
		{
			throw new RuntimeException("@执行退回，流程状态应该是退回,现在是:" + gwf.getWFState().toString());
		}

		if (gwf.getFK_Node() != 3101)
		{
			throw new RuntimeException("@执行退回，当前节点应该是101, 现在是" + String.valueOf(gwf.getFK_Node()));
		}

		sql = "SELECT WFState from nd31rpt where oid=" + workid;
		int wfstate = DBAccess.RunSQLReturnValInt(sql, -1);
		if (wfstate != WFState.ReturnSta.getValue())
		{
			throw new RuntimeException("@在第3个节点退回后rpt数据不正确，流程状态应该是退回,现在是:" + wfstate);
		}

		sql = "SELECT FlowEndNode from nd31rpt where oid=" + workid;
		int FlowEndNode = DBAccess.RunSQLReturnValInt(sql, -1);
		if (FlowEndNode != 3101)
		{
			throw new RuntimeException("@在第3个节点退回后rpt数据不正确，最后的节点应该是101,现在是:" + FlowEndNode);
		}

			/// 检查退回后的数据完整性.


		//删除此测试数据.
		bp.wf.Dev2Interface.Flow_DoDeleteFlowByReal(objs.getVarWorkID(), false);

	}
}