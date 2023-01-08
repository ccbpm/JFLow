package bp.wf;

import bp.*;

/** 
 所有子流程结束，父流程处理规则
*/
public enum AllSubFlowOverRole
{
	/** 
	 无
	*/
	None,
	/** 
	 发送父流程到下一个节点
	*/
	SendParentFlowToNextStep,
	/** 
	 结束父流程
	*/
	OverParentFlow,
	/**
	 * 父流程运行到指定的节点
	 */
	SendParentFlowToSpecifiedNode;

	public static final int SIZE = java.lang.Integer.SIZE;

	public int getValue() {
		return this.ordinal();
	}

	public static AllSubFlowOverRole forValue(int value) 
	{return values()[value];
	}
}