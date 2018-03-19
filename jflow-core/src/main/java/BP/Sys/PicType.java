package BP.Sys;

public enum PicType
{
	/** 
	 自动签名
	 
	*/
	Auto,
	/** 
	 手动签名
	 
	*/
	ShouDong;

	public int getValue()
	{
		return this.ordinal();
	}

	public static PicType forValue(int value)
	{
		return values()[value];
	}
}