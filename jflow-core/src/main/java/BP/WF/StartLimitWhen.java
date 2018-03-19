package BP.WF;

/** 
 装在前提示
 
*/
public enum StartLimitWhen
{
	/** 
	 表单装载后
	 
	*/
	StartFlow,
	/** 
	 发送前检查
	 
	*/
	SendWhen;

	public int getValue()
	{
		return this.ordinal();
	}

	public static StartLimitWhen forValue(int value)
	{
		return values()[value];
	}
}