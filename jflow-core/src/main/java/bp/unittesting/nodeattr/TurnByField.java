package bp.unittesting.nodeattr;

import bp.wf.*;
import bp.en.*;
import bp.da.*;
import bp.web.*;
import bp.unittesting.*;
import bp.unittesting.*;
import java.util.*;

public class TurnByField extends TestBase
{
	/** 
	 测试岗位方向条件
	*/
	public TurnByField()
	{
		this.Title = "测试岗位方向条件与表单字段的方向条件";
		this.DescIt = "以 002请假流程(按岗位控制走向)为测试对象.";
		this.editState = EditState.Passed;
	}
	/** 
	 说明 ：此测试针对于演示环境中的 002 流程编写的单元测试代码。
	 涉及到了: 创建，发送，撤销，方向条件、退回等功能。
	 * @throws Exception 
	*/
	@Override
	public void Do() throws Exception
	{
		String fk_flow = "002";
		Flow fl = new Flow(fk_flow);


			///  zhoutianjiao 登录. 基层路线
		bp.wf.Dev2Interface.Port_Login("zhoutianjiao");

		//创建空白工作, 发起开始节点.
		long workid = bp.wf.Dev2Interface.Node_CreateBlankWork(fk_flow);

		//执行发送，并获取发送对象,.
		SendReturnObjs objs = bp.wf.Dev2Interface.Node_SendWork(fk_flow, workid);
		if (objs.getVarToNodeID() != 299)
		{
			throw new RuntimeException("@按照岗位做方向条件错误，基层人员没有发送到[部门经理审批]。");
		}

			///


			///测试表单字段的方向条件.
		if (!objs.getVarAcceptersID().equals("qifenglin"))
		{
			throw new RuntimeException("@没有让他的部门经理审批不是所期望的值.");
		}

		//按照他的部门经理登录.
		bp.wf.Dev2Interface.Port_Login(objs.getVarAcceptersID());

		//建立表单参数.
		Hashtable ht = new Hashtable();
		ht.put("QingJiaTianShu", 8);
		objs = bp.wf.Dev2Interface.Node_SendWork(fk_flow, workid, ht);
		if (objs.getVarToNodeID() != 204)
		{
			throw new RuntimeException("@按表单字段控制方向错误，小于10天的应该让人办资源部门审批。");
		}

		// 撤销发送.
		bp.wf.Dev2Interface.Flow_DoUnSend(fl.getNo(), workid);

		// 按照请假15天发送.
		ht = new Hashtable();
		ht.put("QingJiaTianShu", 15);
		objs = bp.wf.Dev2Interface.Node_SendWork(fk_flow, workid, ht);
		if (objs.getVarToNodeID() != 206)
		{
			throw new RuntimeException("@按表单字段控制方向错误，大于等于10天的应该让[总经理审批]");
		}

		//删除测试数据.
		bp.wf.Dev2Interface.Flow_DoDeleteFlowByReal(objs.getVarWorkID(), false);

			///


			///让中层 qifenglin登录.
		bp.wf.Dev2Interface.Port_Login("qifenglin");
		workid = bp.wf.Dev2Interface.Node_CreateBlankWork(fk_flow);

		objs = bp.wf.Dev2Interface.Node_SendWork(fk_flow, workid);
		if (objs.getVarToNodeID() != 202)
		{
			throw new RuntimeException("@按照岗位做方向条件错误，中层人员没有发送到[总经理审批]节点。");
		}
		// 删除测试数据.
		bp.wf.Dev2Interface.Flow_DoDeleteFlowByReal(objs.getVarWorkID(), false);

			/// 让中层 qifenglin登录.


			///让高层 zhoupeng 登录.
		bp.wf.Dev2Interface.Port_Login("zhoupeng");
		workid = bp.wf.Dev2Interface.Node_CreateBlankWork(fk_flow);

		objs = bp.wf.Dev2Interface.Node_SendWork(fk_flow, workid);
		if (objs.getVarToNodeID() != 203)
		{
			throw new RuntimeException("@按照岗位做方向条件错误，高层人员没有发送到[人力资源审批]节点。");
		}
		//delete it.
		bp.wf.Dev2Interface.Flow_DoDeleteFlowByReal(objs.getVarWorkID(), false);

			///
	}
}