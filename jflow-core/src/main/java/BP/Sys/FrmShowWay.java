package BP.Sys;


/** 
 呈现方式
 
*/
public enum FrmShowWay
{
	/** 
	 隐藏
	 
	*/
	Hidden,
	/** 
	 自动大小
	 
	*/
	FrmAutoSize,
	/** 
	 指定大小
	 
	*/
	FrmSpecSize,
	/** 
	 新连接
	 
	*/
	WinOpen;

	public int getValue()
	{
		return this.ordinal();
	}

	public static FrmShowWay forValue(int value)
	{
		return values()[value];
	}
}