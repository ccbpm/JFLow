package bp.dts;

/** 
 运行类型．
*/
public enum RunTimeType
{
	/** 
	 每分钟
	*/
	Minute,
	/** 
	 每小时
	*/
	Hour,
	/** 
	 每天
	*/
	Day,
	/** 
	 每月
	*/
	Month,
	/** 
	 没有指定
	*/
	UnName;

	public static final int SIZE = java.lang.Integer.SIZE;

	public int getValue()
	{
		return this.ordinal();
	}

	public static RunTimeType forValue(int value) 
	{
		return values()[value];
	}
}