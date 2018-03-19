package BP.WF;


/** 
 时间段
 
*/
public enum TSpan
{
	/** 
	 本周
	 
	*/
	ThisWeek,
	/** 
	 上周
	 
	*/
	NextWeek,
	/** 
	 两周以前
	 
	*/
	TowWeekAgo,
	/** 
	 更早
	 
	*/
	More;

	public int getValue()
	{
		return this.ordinal();
	}

	public static TSpan forValue(int value)
	{
		return values()[value];
	}
}