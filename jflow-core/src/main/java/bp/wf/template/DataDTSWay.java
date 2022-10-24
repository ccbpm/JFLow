package bp.wf.template;


/** 
 数据同步方案
*/
public enum DataDTSWay
{
	/** 
	 不同步
	*/
	None,
	/** 
	 按数据源同步
	*/
	Syn,
	/** 
	 按WebAPI方式同步
	*/
	WebAPI;

	public static final int SIZE = java.lang.Integer.SIZE;

	public int getValue()  {
		return this.ordinal();
	}

	public static DataDTSWay forValue(int value)
	{return values()[value];
	}
}