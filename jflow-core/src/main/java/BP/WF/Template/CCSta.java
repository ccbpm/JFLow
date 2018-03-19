package BP.WF.Template;


public enum CCSta
{
	/** 
	 未读
	 
	*/
	UnRead,
	/** 
	 已读取
	 
	*/
	Read,
	/** 
	 已经回复
	 
	*/
	CheckOver,
	/** 
	 已删除
	 
	*/
	Del;

	public int getValue()
	{
		return this.ordinal();
	}

	public static CCSta forValue(int value)
	{
		return values()[value];
	}
}