package BP.WF.Template;

import BP.WF.*;

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

	public static final int SIZE = java.lang.Integer.SIZE;

	public int getValue()
	{
		return this.ordinal();
	}

	public static FlowDTSTime forValue(int value)
	{
		return values()[value];
	}
}