package bp.unittesting.nodeattr;

import bp.wf.*;
import bp.en.*;
import bp.da.*;
import bp.web.*;
import bp.unittesting.*;
import java.util.*;

/** 
 测试 按URL 做为转向条件
*/
public class TurnByURL extends TestBase
{
	/** 
	 测试 按URL 做为转向条件
	*/
	public TurnByURL()
	{
		this.Title = "测试 按URL 做为转向条件";
		this.DescIt = "以测试用例的-075流程做测试用例.";
		this.editState = EditState.Passed;
	}
	/** 
	 执行
	 * @throws Exception 
	*/
	@Override
	public void Do() throws Exception
	{
		String fk_flow = "075";
		Flow fl = new Flow(fk_flow);


			///  zhoupeng 登录.
		bp.wf.Dev2Interface.Port_Login("zhoushengyu");
		//创建空白工作, 发起开始节点.
		long workid = bp.wf.Dev2Interface.Node_CreateBlankWork(fk_flow);

		//加入 按URL ,表单里没有TurnTo字段.
		Hashtable ht = new Hashtable();
		ht.put("ToNodeID", "7502");
		SendReturnObjs objs = null;

		//执行发送，并获取发送对象, 应该出来异常才对，因为传递这个参数会导致sql出错误。
		objs = bp.wf.Dev2Interface.Node_SendWork(fk_flow, workid, ht);

		if (objs.getVarToNodeID() != 7502)
		{
			throw new RuntimeException("@应该转向7502 ");
		}

		//删除测试数据.
		bp.wf.Dev2Interface.Flow_DoDeleteFlowByReal(objs.getVarWorkID(), false);

		//测试流程完成条件.
		workid = bp.wf.Dev2Interface.Node_CreateBlankWork(fk_flow);
		ht = new Hashtable();
		ht.put("ToNodeID", "");
		objs = bp.wf.Dev2Interface.Node_SendWork(fk_flow, workid, ht);
		if (objs.getIsStopFlow() == false)
		{
			throw new RuntimeException("@流程应该结束，但是未结束。");
		}

			///
	}
}