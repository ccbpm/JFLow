package bp.unittesting.attrflow;

import bp.wf.*;
import bp.da.*;
import bp.unittesting.*;

/** 
 为开始节点生成工作
*/
public class CreateStartWork extends TestBase
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
	 为开始节点生成工作
	*/
	public CreateStartWork()
	{
		this.Title = "为开始节点生成工作";
		this.DescIt = "生成一个工作.";
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

		//在执行一次创建.
		long workID2 = bp.wf.Dev2Interface.Node_CreateBlankWork(fk_flow);

		if (workID == workID2)
		{
			throw new RuntimeException("应该两次生成的WorkID不相同， 但是现在相同.");
		}

		// 看看有没有当前人员的待办工作》
		sql = "SELECT COUNT(*) FROM WF_EmpWorks WHERE WorkID=" + workID;
		if (DBAccess.RunSQLReturnValInt(sql) == 0)
		{
			throw new RuntimeException("@没有找到它的待办工作." + sql);
		}

		// 检查空白是否有待办，如果有则是错误。
		sql = "SELECT COUNT(*) FROM WF_EmpWorks WHERE WorkID=" + workID2;
		if (DBAccess.RunSQLReturnValInt(sql) >= 1)
		{
			throw new RuntimeException("@没有找到它的待办工作." + sql);
		}

		//删除测试数据.
		bp.wf.Dev2Interface.Flow_DoDeleteFlowByReal(workID, false);

	  // BP.WF.Dev2Interface.Flow_DoDeleteFlowByReal(fl.getNo(), workID2, false);
	}
}