package BP.WF;

/** 
 流程应用类型
 
*/
public enum FlowAppType
{
	/** 
	 普通的
	 
	*/
	Normal,
	/** 
	 工程类
	 
	*/
	PRJ,
	/** 
	 公文流程
	 
	*/
	DocFlow;

	public int getValue()
	{
		return this.ordinal();
	}

	public static FlowAppType forValue(int value)
	{
		return values()[value];
	}
}