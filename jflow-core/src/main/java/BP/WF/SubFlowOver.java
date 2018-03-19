package BP.WF;

/** 
 节点工作退回规则
 
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

	public int getValue()
	{
		return this.ordinal();
	}

	public static SubFlowOver forValue(int value)
	{
		return values()[value];
	}
}