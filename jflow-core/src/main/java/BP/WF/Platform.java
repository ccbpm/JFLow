package BP.WF;

/** 
 运行平台
 
*/
public enum Platform
{
	/** 
	 CCFlow .net平台.
	 
	*/
	CCFlow,
	/** 
	 JFlow java 平台.
	 
	*/
	JFlow;

	public int getValue()
	{
		return this.ordinal();
	}

	public static Platform forValue(int value)
	{
		return values()[value];
	}
}