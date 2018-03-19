package BP.En;

/**
 * 相关功能类型
 */
public enum RefMethodType
{
	/**
	 * 功能
	 */
	Func,
	/**
	 * 模态窗口打开
	 */
	LinkModel,
	/**
	 * 新窗口打开
	 */
	LinkeWinOpen,
	/**
	 * 右侧窗口打开
	 */
	RightFrameOpen;
	
	public int getValue()
	{
		return this.ordinal();
	}
	
	public static RefMethodType forValue(int value)
	{
		return values()[value];
	}
}