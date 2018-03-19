package BP.WF.Template;

/** 
 数据同步方案
 
*/
public enum FlowDTSTime
{
	/** 
	 所有的节点发送后
	 
	*/
	AllNodeSend,
	/** 
	 指定的节点发送后
	 
	*/
	SpecNodeSend,
	/** 
	 当流程结束时
	 
	*/
	WhenFlowOver;

	public int getValue()
	{
		return this.ordinal();
	}

	public static FlowDTSTime forValue(int value)
	{
		return values()[value];
	}
}