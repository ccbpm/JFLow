package BP.DA;

/**
 * 保管位置
 */
public enum Depositary
{
	/**
	 * 不保管
	 */
	None,
	/**
	 * 全体
	 */
	Application;
	
	public int getValue()
	{
		return this.ordinal();
	}
	
	public static Depositary forValue(int value)
	{
		return values()[value];
	}
}