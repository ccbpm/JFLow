package BP.WF;

/** 
 周末休息类型
 
*/
public enum WeekResetType
{
	/** 
	 双休
	 
	*/
	Double,
	/** 
	 单休
	 
	*/
	Single,
	/** 
	 不
	 
	*/
	None;

	public int getValue()
	{
		return this.ordinal();
	}

	public static WeekResetType forValue(int value)
	{
		return values()[value];
	}
}