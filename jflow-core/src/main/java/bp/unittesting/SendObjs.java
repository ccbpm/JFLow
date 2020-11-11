package bp.unittesting;

import bp.wf.*;
import bp.en.*;
import bp.da.*;
import bp.web.*;

/** 
 发送对象
*/
public class SendObjs extends TestBase
{
	/** 
	 发送对象
	*/
	public SendObjs()
	{
		this.Title = "发送对象";
		this.DescIt = "流程: 023 执行发送,产看发送对象是否符合要求.";
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
		objs = bp.wf.Dev2Interface.Node_SendWork(fk_flow, this.workID, null, null, 0, null);



			///检查发送结果是否符合预期.
		String msgText = objs.ToMsgOfText();
		if (msgText.contains("<"))
		{
			throw new RuntimeException("text信息, 具有html标记.");
		}




			/// 检查发送结果是否符合预期.

	}
}