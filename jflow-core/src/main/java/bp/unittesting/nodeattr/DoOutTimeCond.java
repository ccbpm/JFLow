package bp.unittesting.nodeattr;

import bp.unittesting.*;

public class DoOutTimeCond extends TestBase
{
	/** 
	 测试财务报销流程-超时审批功能
	*/
	public DoOutTimeCond()
	{
		this.Title = "节点考核属性-节点超时处理";
		this.DescIt = "以流程:032(节点属性-超时处理)测试用例,测试多种类型下的超时处理逻辑是否正确.";
		this.editState = EditState.UnOK;
	}
	/** 
	 说明 ：此测试针对于演示环境中的 001 流程编写的单元测试代码。
	 涉及到了: 创建，发送，撤销，方向条件、退回等功能。
	*/
	@Override
	public void Do()
	{
		throw new RuntimeException("@此功能尚未完成");


	}
}