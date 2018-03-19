package BP.WF.Port;


public enum AlertWay
{
	/** 
	 不提示
	*/
	None,
	/** 
	 手机短信
	*/
	SMS,
	/** 
	 邮件
	*/
	Email,
	/** 
	 手机短信+邮件
	*/
	SMSAndEmail,
	/** 
	 内部消息
	*/
	AppSystemMsg;

	public int getValue()
	{
		return this.ordinal();
	}

	public static AlertWay forValue(int value)
	{
		return values()[value];
	}
}