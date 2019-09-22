package BP.WF;

/** 
 子流程结束处理规则
*/
public enum SubFlowOver
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
	OverParentFlow;

	public static final int SIZE = java.lang.Integer.SIZE;

	public int getValue()
	{
		return this.ordinal();
	}

	public static SubFlowOver forValue(int value)
	{
		return values()[value];
	}
}