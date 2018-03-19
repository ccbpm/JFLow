package BP.WF;

public enum HuiQianTaskSta
{
	/** 
	 无
	 
	*/
	None,
	/** 
	 会签中
	 
	*/
	HuiQianing,
	/** 
	 会签完成
	 
	*/
	HuiQianOver;

	public int getValue()
	{
		return this.ordinal();
	}

	public static HuiQianTaskSta forValue(int value)
	{
		return values()[value];
	}
}
