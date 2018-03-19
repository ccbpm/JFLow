package BP.Sys;

/** 
 Pop返回值类型
 
*/
public enum PopValFormat
{
	/** 
	 编号
	 
	*/
	OnlyNo,
	/** 
	 名称
	 
	*/
	OnlyName,
	/** 
	 编号与名称
	 
	*/
	NoName;

	public int getValue()
	{
		return this.ordinal();
	}

	public static PopValFormat forValue(int value)
	{
		return values()[value];
	}
}