package bp.unittesting.nodeattr;

import bp.wf.*;
import bp.en.*;
import bp.da.*;
import bp.web.*;
import bp.unittesting.*;
import bp.unittesting.*;

/** 
 测试跳转规则1
*/
public class Skip1_13 extends TestBase
{
	/** 
	 测试跳转规则
	*/
	public Skip1_13()
	{
		this.Title = "测试跳转规则1-13 连续跳转2个节点。";
		this.DescIt = "流程: 以demo 流程 062:测试跳转规则，连续跳转3个节点并到最后结束。";
		this.editState = EditState.Passed;
	}


		///全局变量
	/** 
	 流程编号
	*/
	public String fk_flow = "";
	/** 
	 用户编号
	*/
	public String userNo = "";
	/** 
	 所有的流程
	*/
	public Flow fl = null;
	/** 
	 主线程ID
	*/
	public long workID = 0;
	/** 
	 发送后返回对象
	*/
	public SendReturnObjs objs = null;
	/** 
	 工作人员列表
	*/
	public GenerWorkerList gwl = null;
	/** 
	 流程注册表
	*/
	public GenerWorkFlow gwf = null;

		/// 变量

	/** 
	 测试案例说明:
	 1， 分别列举4种。
	 2， 测试找领导的两种模式
	 * @throws Exception 
	*/
	@Override
	public void Do() throws Exception
	{
		this.fk_flow = "062";
		fl = new Flow(this.fk_flow);
		String sUser = "zhoupeng";
		bp.wf.Dev2Interface.Port_Login(sUser);

		//创建.
		workID = bp.wf.Dev2Interface.Node_CreateBlankWork(fl.getNo());

		//执行发送, 应该跳转到最后一个步骤上去.
		SendReturnObjs objs = bp.wf.Dev2Interface.Node_SendWork(fl.getNo(), workID);


			///分析预期
		if (objs.getVarAcceptersID() != null)
		{
			throw new RuntimeException("@应当是null现在是" + objs.getVarAcceptersID());
		}

		//if (objs.VarToNodeID != 5701)
		//    throw new Exception("@应当是开始节点，但是运行到了:" + objs.VarToNodeID + " - " + objs.VarToNodeName);

			///
	}

}