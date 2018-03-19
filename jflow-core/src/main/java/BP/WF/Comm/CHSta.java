package BP.WF.Comm;

public enum CHSta
{
	/**
	 * 及时完成
	 */
	JiShi,
	/**
	 * 按期完成
	 */
	AnQi,
	/**
	 * 预期完成
	 */
	YuQi,
	/**
	 * 超期完成
	 */
	ChaoQi;
	
	public int getValue()
	{
		return this.ordinal();
	}
	
	public static CHSta forValue(int value)
	{
		return values()[value];
	}
}