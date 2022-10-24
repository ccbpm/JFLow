package bp.sys;


/** 
 结束流程 窗口
*/
public enum IsOpenEndFlow
{
	/** 
	 默认不打开.
	*/
	Close,
	/** 
	 打开
	*/
	Open;

	public static final int SIZE = java.lang.Integer.SIZE;

	public int getValue()
	{
		return this.ordinal();
	}

	public static IsOpenEndFlow forValue(int value) 
	{
		return values()[value];
	}
}