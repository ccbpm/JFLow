package bp.unittesting.attrflow;

import bp.wf.*;
import bp.en.*;
import bp.da.*;
import bp.web.*;
import bp.unittesting.*;
import bp.unittesting.*;

/** 
 生成标题
*/
public class GenerTitle extends TestBase
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
	 生成标题
	*/
	public GenerTitle()
	{
		this.Title = "标题生成规则";
		this.DescIt = "流程:023最简单的3节点(轨迹模式).";
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
		//创建空白工作, 在标题为空的情况下.
		workID = bp.wf.Dev2Interface.Node_CreateBlankWork(fk_flow);


			///检查标题是否符合预期.
		String title = DBAccess.RunSQLReturnString("SELECT Title FROM " + fl.getPTable() + " WHERE OID=" + workID);
		if (!title.equals("TitleTest"))
		{
			throw new RuntimeException("@没有按指定的数据(TitleTest)生成标题, 在流程报表里，现在是:" + title);
		}

			///

		//执行发送.
		objs = bp.wf.Dev2Interface.Node_SendWork(fk_flow, workID);


			///检查标题是否符合预期.
		title = DBAccess.RunSQLReturnString("SELECT  Title FROM " + fl.getPTable() + " where OID=" + workID);
		if (!title.equals("TitleTest"))
		{
			throw new RuntimeException("@发送后:没有按指定的数据生成标题, 在流程报表里." + title);
		}

			///

		//删除测试数据.
		bp.wf.Dev2Interface.Flow_DoDeleteFlowByReal(workID, false);


		//创建空白工作, 让ccflow根据规则自动生成标题.
		workID = bp.wf.Dev2Interface.Node_CreateBlankWork(fk_flow);


			///检查标题是否符合预期.
		//title = DBAccess.RunSQLReturnString("SELECT Title FROM WF_GenerWorkFlow where WorkID=" + workID);
		//if (DataType.IsNullOrEmpty(title))
		//    throw new Exception("@标题没有生成 在 WF_GenerWorkFlow中没有找到.");

		title = DBAccess.RunSQLReturnString("SELECT Title FROM " + fl.getPTable() + " where OID=" + workID);
		if (DataType.IsNullOrEmpty(title))
		{
			throw new RuntimeException("@标题没有生成， 在 PTable中没有找到.");
		}

			///

		//执行发送.
		objs = bp.wf.Dev2Interface.Node_SendWork(fk_flow, workID);


			///检查标题是否符合预期.
		title = DBAccess.RunSQLReturnString("SELECT Title FROM WF_GenerWorkFlow where WorkID=" + workID);
		if (DataType.IsNullOrEmpty(title))
		{
			throw new RuntimeException("@标题没有生成 在 WF_GenerWorkFlow中没有找到.");
		}

		title = DBAccess.RunSQLReturnString("SELECT Title FROM " + fl.getPTable() + " WHERE OID=" + workID);
		if (DataType.IsNullOrEmpty(title))
		{
			throw new RuntimeException("@标题没有生成， 在 PTable中没有找到.");
		}

			///

		//删除测试数据.
		bp.wf.Dev2Interface.Flow_DoDeleteFlowByReal(workID, false);
	}
}