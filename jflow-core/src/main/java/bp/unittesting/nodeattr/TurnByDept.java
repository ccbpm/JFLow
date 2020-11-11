package bp.unittesting.nodeattr;

import bp.wf.*;
import bp.en.*;
import bp.da.*;
import bp.web.*;
import bp.unittesting.*;
import bp.unittesting.*;

/** 
 按部门的方向条件转向
*/
public class TurnByDept extends TestBase
{
	/** 
	 测试岗位方向条件
	*/
	public TurnByDept()
	{
		this.Title = "测试岗位、部门、表单字段的方向条件";
		this.DescIt = "以测试用例的-028部门方向条件做为测试用例.";
		this.editState = EditState.Passed;
	}
	/** 
	 说明 ：此测试针对于演示环境中的 028 流程编写的单元测试代码。
	 涉及到了: 多种方式的方向转向功能是否可用。
	 * @throws Exception 
	*/
	@Override
	public void Do() throws Exception
	{
		String fk_flow = "028";
		Flow fl = new Flow(fk_flow);


			///  yangyilei 登录. 基层路线
		bp.wf.Dev2Interface.Port_Login("yangyilei");
		//创建空白工作, 发起开始节点.
		long workid = bp.wf.Dev2Interface.Node_CreateBlankWork(fk_flow);

		//执行发送，并获取发送对象,.
		SendReturnObjs objs = bp.wf.Dev2Interface.Node_SendWork(fk_flow, workid);
		if (objs.getVarToNodeID() != 2802)
		{
			throw new RuntimeException("@财务部门人员发起没有转入到财务部节点上去。");
		}

		//删除测试数据.
		bp.wf.Dev2Interface.Flow_DoDeleteFlowByReal(objs.getVarWorkID(), false);

			/// yangyilei


			/// qifenglin登录.
		bp.wf.Dev2Interface.Port_Login("qifenglin");
		workid = bp.wf.Dev2Interface.Node_CreateBlankWork(fk_flow);

		objs = bp.wf.Dev2Interface.Node_SendWork(fl.getNo(), workid);
		if (objs.getVarToNodeID() != 2899)
		{
			throw new RuntimeException("@研发部人员发起没有转入研发部节点上去。");
		}
		// 删除测试数据.
		bp.wf.Dev2Interface.Flow_DoDeleteFlowByReal(objs.getVarWorkID(), false);

			///  qifenglin登录.


			/// liyan登录.
		bp.wf.Dev2Interface.Port_Login("liyan");
		workid = bp.wf.Dev2Interface.Node_CreateBlankWork(fk_flow);

		objs = bp.wf.Dev2Interface.Node_SendWork(fl.getNo(), workid);
		if (objs.getVarToNodeID() != 2803)
		{
			throw new RuntimeException("@没有转入人力资源部上去。");
		}
		// 删除测试数据.
		bp.wf.Dev2Interface.Flow_DoDeleteFlowByReal(objs.getVarWorkID(), false);

			/// liyan登录.
	}
}