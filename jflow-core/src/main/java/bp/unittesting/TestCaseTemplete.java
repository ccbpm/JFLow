package bp.unittesting;

import bp.wf.*;
import bp.en.*;
import bp.da.*;
import bp.web.*;

/** 
 测试名称
*/
public class TestCaseTemplete extends TestBase
{
	/** 
	 测试名称
	*/
	public TestCaseTemplete()
	{
		this.Title = "测试名称";
		this.DescIt = "流程: 005月销售总结(同表单分合流),执行发送后的数据是否符合预期要求.";
		this.editState = EditState.UnOK;
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
	}
}