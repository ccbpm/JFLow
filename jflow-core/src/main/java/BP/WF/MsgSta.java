package BP.WF;


/** 
 消息状态
 
*/
public enum MsgSta
{
	/** 
	 未开始
	 
	*/
	UnRun,
	/** 
	 成功
	 
	*/
	RunOK,
	/** 
	 失败
	 
	*/
	RunError,
	/** 
	 禁止发送
	 
	*/
	Disable;

	public int getValue()
	{
		return this.ordinal();
	}

	public static MsgSta forValue(int value)
	{
		return values()[value];
	}
}