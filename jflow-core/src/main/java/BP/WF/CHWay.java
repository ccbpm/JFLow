package BP.WF;

/** 
 考核规则
 
*/
public enum CHWay
{
	/** 
	 不考核
	 
	*/
	None,
	/** 
	 按照时效考核
	 
	*/
	ByTime,
	/** 
	 按照工作量考核
	 
	*/
	ByWorkNum;

	public int getValue()
	{
		return this.ordinal();
	}

	public static CHWay forValue(int value)
	{
		return values()[value];
	}
}