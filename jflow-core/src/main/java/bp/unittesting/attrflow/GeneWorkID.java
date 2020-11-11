package bp.unittesting.attrflow;

import bp.wf.*;
import bp.en.*;
import bp.da.*;
import bp.web.*;
import bp.unittesting.*;
import bp.unittesting.*;

/** 
 生成WorkID
*/
public class GeneWorkID extends TestBase
{

		///变量
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
	 生成WorkID
	*/
	public GeneWorkID()
	{
		this.Title = "检查生成WorkID是否重复";
		this.DescIt = "有几次出现生成WorkID重复的现象.";
		this.editState = EditState.Passed;
	}
	/** 
	 过程说明：
	 1，以流程 023最简单的3节点(轨迹模式)， 为测试用例。
	 2，仅仅测试发送功能，与检查发送后的数据是否完整.
	 3, 此测试有三个节点发起点、中间点、结束点，对应三个测试方法。
	 * @throws Exception 
	*/
	@Override
	public void Do() throws Exception
	{

			///定义变量.
		fk_flow = "023";
		userNo = "zhanghaicheng";
		fl = new Flow(fk_flow);

			/// 定义变量.

		//让 userNo 登录.
		bp.wf.Dev2Interface.Port_Login(userNo);

		// 创建空白工作, 在标题为空的情况下.
		workID = bp.wf.Dev2Interface.Node_CreateBlankWork(fk_flow);


			///检查数据是否符合预期.

			/// 检查数据是否符合预期

		//在执行一次创建.
		long workID2 = bp.wf.Dev2Interface.Node_CreateBlankWork(fk_flow);
		if (workID != workID2)
		{
			throw new RuntimeException("应该两次生成的WorkID相同， 但是现在不同.");
		}

		 //执行发送.
		 bp.wf.Dev2Interface.Node_SendWork(fk_flow, workID);

		 //在执行一次创建.
		 workID2 = bp.wf.Dev2Interface.Node_CreateBlankWork(fk_flow);

		   if (workID == workID2)
		   {
			   throw new RuntimeException("应该两次生成的WorkID不相同， 但是现在相同.");
		   }

		//删除测试数据.
		bp.wf.Dev2Interface.Flow_DoDeleteFlowByReal(workID, false);
	}
}