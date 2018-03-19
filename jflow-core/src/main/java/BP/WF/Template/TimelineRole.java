package BP.WF.Template;

/** 
 流程考核类型
 
*/
public enum TimelineRole
{
	/** 
	 按节点
	 
	*/
	ByNodeSet,
	/** 
	 按流程
	 
	*/
	ByFlow;

	public int getValue()
	{
		return this.ordinal();
	}

	public static TimelineRole forValue(int value)
	{
		return values()[value];
	}
}