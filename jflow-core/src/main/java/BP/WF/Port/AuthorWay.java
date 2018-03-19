package BP.WF.Port;


/** 
 授权方式
*/
public enum AuthorWay
{
	/** 
	 不授权
	*/
	None,
	/** 
	 全部授权
	*/
	All,
	/** 
	 指定流程授权
	*/
	SpecFlows;

	public int getValue()
	{
		return this.ordinal();
	}

	public static AuthorWay forValue(int value)
	{
		return values()[value];
	}
}