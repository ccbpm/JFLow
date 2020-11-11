package bp.unittesting.nodeattr;

import bp.wf.*;
import bp.en.*;
import bp.da.*;
import bp.web.*;
import bp.unittesting.*;
import bp.unittesting.*;
import java.util.*;

/** 
 测试 按SQL 做为转向条件
*/
public class TurnBySQL extends TestBase
{
	/** 
	 测试 按SQL 做为转向条件
	*/
	public TurnBySQL()
	{
		this.Title = "测试 按SQL 做为转向条件";
		this.DescIt = "以测试用例的-030流程做测试用例.";
		this.editState = EditState.Passed;
	}
	/** 
	 执行
	 * @throws Exception 
	*/
	@Override
	public void Do() throws Exception
	{
		String fk_flow = "030";
		Flow fl = new Flow(fk_flow);


			///  zhoupeng 登录.
		bp.wf.Dev2Interface.Port_Login("zhoupeng");
		//创建空白工作, 发起开始节点.
		long workid = bp.wf.Dev2Interface.Node_CreateBlankWork(fk_flow);

		//加入 按SQL ,表单里没有TurnTo字段.
		Hashtable ht = new Hashtable();
		ht.put("MyPara", "qqqq");
		SendReturnObjs objs = null;
		try
		{
			//执行发送，并获取发送对象, 应该出来异常才对，因为传递这个参数会导致sql出错误。
			objs = bp.wf.Dev2Interface.Node_SendWork(fk_flow, workid, ht);
		}
		catch (RuntimeException ex)
		{
			Log.DefaultLogWriteLineInfo("已经检测到SQL的变量已经被正确的替换了,导致此语句执行失败:" + ex.getMessage());
		}
		ht.clear();
		ht.put("MyPara", "1");
		//执行发送，并获取发送对象, 应该出来异常才对，因为传递这个参数会导致sql出错误。
		objs = bp.wf.Dev2Interface.Node_SendWork(fk_flow, workid, ht);
		if (objs.getVarToNodeID() != 3002)
		{
			throw new RuntimeException("@应该转向B。");
		}

		//删除测试数据.
		bp.wf.Dev2Interface.Flow_DoDeleteFlowByReal(objs.getVarWorkID(), false);

			///
	}
}