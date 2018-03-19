package BP.WF;

/** 
 子线程类型
 
*/
public enum SubThreadType
{
	/** 
	 同表单
	 
	*/
	SameSheet,
	/** 
	 异表单
	 
	*/
	UnSameSheet;

	public int getValue()
	{
		return this.ordinal();
	}

	public static SubThreadType forValue(int value)
	{
		return values()[value];
	}
}