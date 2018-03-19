package BP.WF;


/** 
 任务状态
 
*/
public enum TaskSta
{
	/** 
	 无
	 
	*/
	None,
	/** 
	 共享
	 
	*/
	Sharing,
	/** 
	 已经取走
	 
	*/
	Takeback;

	public int getValue()
	{
		return this.ordinal();
	}

	public static TaskSta forValue(int value)
	{
		return values()[value];
	}
}