package bp.unittesting;

import bp.wf.*;
import bp.en.*;
import bp.da.*;
import bp.web.*;

/** 
 子流程的回滚
*/
public class SubFlowReback extends TestCaseTemplete
{
	/** 
	 子流程的回滚
	*/
	public SubFlowReback()
	{
		this.Title = "子流程的回滚";
		this.DescIt = "以023,024流程来测试子流程的回滚";
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
	public long workid = 0;
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
	 子流程的回滚:
	 1, 把023做为父流程，024做为子流程。
	 2, 在父流程吊起子流程后，子流程完成，让其回滚，检查数据是否符合预期。
	 * @throws Exception 
	*/
	@Override
	public void Do() throws Exception
	{
		//初始化变量.
		fk_flow = "005";
		userNo = "zhanghaicheng";
		fl = new Flow(fk_flow);

		// 让 zhanghaicheng  登录.
		bp.wf.Dev2Interface.Port_Login(userNo);

		// 创建.
		this.workID=bp.wf.Dev2Interface.Node_CreateBlankWork(this.fk_flow, null, null, WebUser.getNo(), "parent flow");

		//发送到下一步骤,还是让zhoupeng 处理.
		bp.wf.Dev2Interface.Node_SendWork(this.fk_flow, this.workid, null, null, 0, "zhoupeng");

		//创建一个子流程,让 zhoushengyu 做为发起人.
		long subWorkID = bp.wf.Dev2Interface.Node_CreateStartNodeWork("024", null, null, "zhoushengyu", "sub flow", this.workid, this.fk_flow, 0);

		// 让子流程执行到结束.
		LetSubFlowRunOver(subWorkID);

		// 让 zhoupeng  登录,开始监控子流程.
		bp.wf.Dev2Interface.Port_Login("zhoupeng");

		//执行回滚到最后一个节点上.
		bp.wf.Dev2Interface.Flow_DoRebackWorkFlow("024", subWorkID, 2401, "test reback");

		// 在让子流程执行到结束.
		LetSubFlowRunOver(subWorkID);


		//重复一次，检查是否有问题？ 在执行回滚到最后一个节点上.
		bp.wf.Dev2Interface.Flow_DoRebackWorkFlow("024", subWorkID, 2401, "test reback");

		// 在让子流程执行到结束.
		LetSubFlowRunOver(subWorkID);

	}
	/** 
	 让子流程运行到结束.
	 * @throws Exception 
	*/
	public final void LetSubFlowRunOver(long subWorkID) throws Exception
	{

			///子流程第一个节点的操作人员.
		// 让 zhoushengyu 登录.
		bp.wf.Dev2Interface.Port_Login("zhoushengyu");

		//发送这个子流程,到 zhangyifan， 发送到第二个节点上去.
		bp.wf.Dev2Interface.Node_SendWork("024", subWorkID, null, null, 0, "zhangyifan");

			/// 子流程第一个节点的操作人员.



			///子流程第二个节点的操作人员.
		// 让 zhangyifan 登录.
		bp.wf.Dev2Interface.Port_Login("zhangyifan");

		//发送这个子流程,到 zhoutianjiao.
		bp.wf.Dev2Interface.Node_SendWork("024", subWorkID, null, null, 0, "zhoutianjiao");

			/// 子流程第二个节点的操作人员.



			///子流程第三个节点的操作人员,现在子流程完成.
		// 让 zhoutianjiao 登录.
		bp.wf.Dev2Interface.Port_Login("zhoutianjiao");

		//发送这个子流程,到 zhoutianjiao.
		bp.wf.Dev2Interface.Node_SendWork("024", subWorkID, null, null, 0, "zhoutianjiao");

			/// 子流程第三个节点的操作人员,现在子流程完成.
	}
}