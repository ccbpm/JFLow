package bp.unittesting.nodeattr;

import bp.wf.*;
import bp.en.*;
import bp.da.*;
import bp.web.*;
import bp.unittesting.*;
import bp.unittesting.*;
import java.util.*;

/** 
 发送参数
*/
public class SendPara extends TestCaseTemplete
{
	/** 
	 发送参数
	*/
	public SendPara()
	{
		this.Title = "发送参数";
		this.DescIt = "流程: 023 执行发送,产看参数是否符合要求.";
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
	 1, .
	 2, .
	 3，.
	 * @throws Exception 
	*/
	@Override
	public void Do() throws Exception
	{
		//初始化变量.
		fk_flow = "023";
		userNo = "zhanghaicheng";
		fl = new Flow(fk_flow);
		bp.wf.Dev2Interface.Port_Login(userNo);

		//创建一个工作。
		this.workID=bp.wf.Dev2Interface.Node_CreateBlankWork(fk_flow, null, null, userNo, null);

		Hashtable ht = new Hashtable();
		ht.put(bp.wf.WorkSysFieldAttr.SysSDTOfNode,"2020-12-01 08:00"); //下一个节点完成时间。
		ht.put(bp.wf.WorkSysFieldAttr.SysSDTOfFlow,"2020-12-31 08:00"); //整体流程需要完成的时间。。
		objs = bp.wf.Dev2Interface.Node_SendWork(fk_flow, this.workID, ht, null, 0, null);


			///检查发送结果是否符合预期.
		//sql = "SELECT "+GenerWorkFlowAttr.SDTOfFlow+","+GenerWorkFlowAttr.SDTOfNode+" FROM WF_GenerWorkFlow WHERE WorkID="+this.workID;
		GenerWorkFlow gwf = new GenerWorkFlow(this.workID);
		if (!gwf.getSDTOfFlow().equals("2020-12-31 08:00"))
		{
			throw new RuntimeException("@没有写入流程应完成时间，现在的应完成时间是:" + gwf.getSDTOfFlow());
		}

		if (!gwf.getSDTOfNode().equals("2020-12-01 08:00"))
		{
			throw new RuntimeException("@没有写入节点应该完成时间，现在的应完成时间是:" + gwf.getSDTOfNode());
		}

		GenerWorkerLists gwls = new GenerWorkerLists(this.workID, 2302);
		for (GenerWorkerList gwl : gwls.ToJavaList())
		{
			if (!gwl.getSDT().equals("2020-12-01 08:00"))
			{
				throw new RuntimeException("@没有写入节点应该应该完成时间.");
			}
		}

			/// 检查发送结果是否符合预期.

	}
}