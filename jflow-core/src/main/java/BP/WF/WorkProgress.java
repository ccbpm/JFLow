package BP.WF;


public enum WorkProgress
{
	/** 
	 正常运行
	 
	*/
	Runing,
	/** 
	 预警
	 
	*/
	Alert,
	/** 
	 逾期
	 
	*/
	Timeout;

	public int getValue()
	{
		return this.ordinal();
	}

	public static WorkProgress forValue(int value)
	{
		return values()[value];
	}
}