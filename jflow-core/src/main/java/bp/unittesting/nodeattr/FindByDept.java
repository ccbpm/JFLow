package bp.unittesting.nodeattr;

import bp.wf.*;
import bp.en.*;
import bp.da.*;
import bp.web.*;
import bp.unittesting.*;
import bp.unittesting.*;

/** 
 测试按部门找人员
*/
public class FindByDept extends TestBase
{
	/** 
	 测试按部门找人员
	*/
	public FindByDept()
	{
		this.Title = "测试按部门找人员";
		this.DescIt = "流程: 以demo 流程 064:找人规则(找领导) 为例测试。";
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
	 2， 测试按部门找人员的两种模式
	 * @throws Exception 
	*/
	@Override
	public void Do() throws Exception
	{
		this.fk_flow = "064";
		fl = new Flow(this.fk_flow);
		String sUser = "zhoupeng";
		bp.wf.Dev2Interface.Port_Login(sUser);

		//创建.
		workID = bp.wf.Dev2Interface.Node_CreateBlankWork(fl.getNo());

		//执行发送, 按照职务找
		SendReturnObjs objs = bp.wf.Dev2Interface.Node_SendWork(fl.getNo(), workID, null, 6499, null);


			///分析预期
		if (!objs.getVarAcceptersID().equals("zhanghaicheng"))
		{
			throw new RuntimeException("@按照职务找错误, 应当是zhanghaicheng现在是" + objs.getVarAcceptersID());
		}

			///

		//执行撤销发送,按照岗位找.
		bp.wf.Dev2Interface.Flow_DoUnSend(fl.getNo(), workID);
		objs = bp.wf.Dev2Interface.Node_SendWork(fl.getNo(), workID, null, 6402, null);


			///分析预期
		if (!objs.getVarAcceptersID().equals("zhanghaicheng"))
		{
			throw new RuntimeException("@按照职务找错误, 应当是zhanghaicheng现在是" + objs.getVarAcceptersID());
		}

			///

		//执行撤销发送,找部门所有人员.
		bp.wf.Dev2Interface.Flow_DoUnSend(fl.getNo(), workID);
		objs = bp.wf.Dev2Interface.Node_SendWork(fl.getNo(), workID, null, 6403, null);


			///分析预期
		if (!objs.getVarAcceptersID().equals("zhoupeng"))
		{
			throw new RuntimeException("@按照职务找错误, 应当是zhoupeng现在是" + objs.getVarAcceptersID());
		}

			///

	}
}