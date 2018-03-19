package BP.WF.Template;

/** 
 数据同步方案
 
*/
public enum FlowDTSWay
{
	/** 
	 不同步
	 
	*/
	None,
	/** 
	 同步
	 
	*/
	Syn;

	public int getValue()
	{
		return this.ordinal();
	}

	public static FlowDTSWay forValue(int value)
	{
		return values()[value];
	}
}