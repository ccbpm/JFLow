package BP.WF;

/** 
 工作提醒规则
 
*/
public enum CHAlertRole
{
	/** 
	 不提醒
	 
	*/
	None,
	/** 
	 一天一次
	 
	*/
	OneDayOneTime,
	/** 
	 一天两次
	 
	*/
	OneDayTowTime;

	public int getValue()
	{
		return this.ordinal();
	}

	public static CHAlertRole forValue(int value)
	{
		return values()[value];
	}
}