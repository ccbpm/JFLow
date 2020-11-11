package bp.unittesting.returncase;

import bp.wf.*;
import bp.en.*;
import bp.da.*;
import bp.web.*;
import bp.unittesting.*;
import bp.unittesting.*;

public class HeLiuNodeReturn extends TestBase
{
	/** 
	 测试合流节点退回
	*/
	public HeLiuNodeReturn()
	{
		this.Title = "测试合流节点向子线程退回";
		this.DescIt = "以demo中的 FlowNo=005 月销售总结(同表单分合流), 为测试案例.";
		this.editState = EditState.Passed;
	}


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
	 说明 ：以demo中的 FlowNo=005 月销售总结(同表单分合流), 为测试案例。
	 涉及到了: 创建，发送，撤销，方向条件、退回等功能。
	 * @throws Exception 
	*/
	@Override
	public void Do() throws Exception
	{
		//BP.WF.ClearDB cd = new ClearDB();
		//cd.Do();

		// 给全局变量赋值.
		fk_flow = "005";
		userNo = "zhanghaicheng";
		fl = new Flow(fk_flow);

		// 让发起人登录.
		bp.wf.Dev2Interface.Port_Login(userNo);

		//创建空白工作, 发起开始节点.
		workID = bp.wf.Dev2Interface.Node_CreateBlankWork(fk_flow);

		//执行发送，并获取发送对象.
		SendReturnObjs objs = bp.wf.Dev2Interface.Node_SendWork(fk_flow, workID);


			///发送子线程 zhangyifan
		// 合流点发送后，子线程点执行发送.
		bp.wf.Dev2Interface.Port_Login("zhangyifan");

		//获得子线程ID.
		long threadWorkID1 = this.GetThreadID(workID);

		// 执行发送, 让他发送到合流节点上去.
		bp.wf.Dev2Interface.Node_SendWork(fk_flow, threadWorkID1);

			/// 发送子线程 zhangyifan


			///发送子线程 zhoushengyu
		// 合流点发送后，子线程点执行发送.
		bp.wf.Dev2Interface.Port_Login("zhoushengyu");

		//获得子线程ID.
		long threadWorkID2 = this.GetThreadID(workID);

		// 执行发送, 让他发送到合流节点上去.
		bp.wf.Dev2Interface.Node_SendWork(fk_flow, threadWorkID2);

			/// 发送子线程 zhoushengyu

		// 让发起人登录.
		bp.wf.Dev2Interface.Port_Login(userNo);

		// 执行退回子线程1.
		bp.wf.Dev2Interface.Node_ReturnWork(fl.getNo(), threadWorkID1, workID, 599, 502, "test msg1", false);


			///检查子线程数据是否正确？
		gwf = new GenerWorkFlow(threadWorkID1);
		if (gwf.getFK_Node() != 502)
		{
			throw new RuntimeException("@子线程的退回节点与预期不符合，现在是:" + gwf.getFK_Node());
		}

		if (gwf.getFID() != workID)
		{
			throw new RuntimeException("@子线程的退回节点后 WF_GenerWorkFlow 上的 FID 丢失，现在是:" + gwf.getFID());
		}

		gwl = new GenerWorkerList(threadWorkID1, 502, "zhoushengyu");
		if (gwl.getIsPass() == true)
		{
			throw new RuntimeException("@子线程不应该是已通过的状态.");
		}

			/// 检查子线程数据是否正确？

		// 执行退回子线程2.
		bp.wf.Dev2Interface.Node_ReturnWork(fl.getNo(), threadWorkID2, workID, 599, 502, "test msg2", false);


			///检查子线程数据是否正确？
		gwf = new GenerWorkFlow(threadWorkID2);
		if (gwf.getFK_Node() != 502)
		{
			throw new RuntimeException("@子线程的退回节点与预期不符合，现在是:" + gwf.getFK_Node());
		}

		if (gwf.getFID() != workID)
		{
			throw new RuntimeException("@子线程的退回节点后 WF_GenerWorkFlow 上的 FID 丢失，现在是:" + gwf.getFID());
		}

		gwl = new GenerWorkerList(threadWorkID1, 502, "zhangyifan");
		if (gwl.getIsPass() == true)
		{
			throw new RuntimeException("@子线程不应该是已通过的状态.");
		}

			/// 检查子线程数据是否正确？
	}

	private long GetThreadID(long workID) throws Exception
	{
		//获取它的待办, 从而获取子线程id.
		DataTable dt = Dev2Interface.DB_GenerEmpWorksOfDataTable(WebUser.getNo(), WFState.Runing, fk_flow);
		for (DataRow dr : dt.Rows)
		{
			long fid = Long.parseLong(dr.getValue("FID").toString());
			if (fid != workID)
			{
				continue;
			}

			return Long.parseLong(dr.getValue("WorkID").toString());
		}
		throw new RuntimeException("@没有找到.");
	}
}