package BP.En;

/**
 * AddAllLocation
 */
public enum AddAllLocation
{
	/**
	 * 显示上方
	 */
	Top,
	/**
	 * 显示下方
	 */
	End,
	/**
	 * 显示上方和下方
	 */
	TopAndEnd,
	/**
	 * 不显示
	 */
	None,
	/**
	 * 多选
	 */
	TopAndEndWithMVal;
	
	public int getValue()
	{
		return this.ordinal();
	}
	
	public static AddAllLocation forValue(int value)
	{
		return values()[value];
	}
}