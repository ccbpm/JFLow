package BP.En;

public enum OperatorSymbol
{
	/**
	 * 大于
	 */
	DaYu,
	/**
	 * 等于
	 */
	DengYu,
	/**
	 * 小于
	 */
	XiaoYu,
	/**
	 * 相似
	 */
	Like;
	
	public int getValue()
	{
		return this.ordinal();
	}
	
	public static OperatorSymbol forValue(int value)
	{
		return values()[value];
	}
}