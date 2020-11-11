package bp.unittesting.nodeattr;

import bp.wf.*;
import bp.en.*;
import bp.da.*;
import bp.web.*;
import bp.unittesting.*;
import bp.unittesting.*;
import java.util.*;

/** 
 测试开发者参数做为转向条件
*/
public class TurnByDevPara extends TestBase
{
	/** 
	 测试开发者参数做为转向条件
	*/
	public TurnByDevPara()
	{
		this.Title = "测试开发者参数做为转向条件";
		this.DescIt = "以测试用例的-029流程做为开发者参数.";
		this.editState = EditState.Passed;
	}
	/** 
	 执行
	 * @throws Exception 
	*/
	@Override
	public void Do() throws Exception
	{
		String fk_flow = "029";
		Flow fl = new Flow(fk_flow);
		fl.DoCheck();


			///  zhoupeng 登录.
		bp.wf.Dev2Interface.Port_Login("zhoupeng");
		//创建空白工作, 发起开始节点.
		long workid = bp.wf.Dev2Interface.Node_CreateBlankWork(fk_flow);
		//加入开发者参数,表单里没有TurnTo字段.
		Hashtable ht = new Hashtable();
		ht.put("Turn", "A");

		//执行发送，并获取发送对象,.
		SendReturnObjs objs = bp.wf.Dev2Interface.Node_SendWork(fk_flow, workid, ht);
		if (objs.getVarToNodeID() != 2999)
		{
			throw new RuntimeException("@应该转向A。");
		}

		//删除测试数据.
		bp.wf.Dev2Interface.Flow_DoDeleteFlowByReal(objs.getVarWorkID(), false);

			///


			///  zhoupeng 登录.
		bp.wf.Dev2Interface.Port_Login("zhoupeng");
		//创建空白工作, 发起开始节点.
		workid = bp.wf.Dev2Interface.Node_CreateBlankWork(fk_flow);
		//加入开发者参数,表单里没有TurnTo字段.
		ht = new Hashtable();
		ht.put("Turn", "B");

		//执行发送，并获取发送对象,.
		objs = bp.wf.Dev2Interface.Node_SendWork(fk_flow, workid, ht);
		if (objs.getVarToNodeID() != 2902)
		{
			throw new RuntimeException("@应该转向B。");
		}

		//删除测试数据.
		bp.wf.Dev2Interface.Flow_DoDeleteFlowByReal(objs.getVarWorkID(), false);

			///
	}
}