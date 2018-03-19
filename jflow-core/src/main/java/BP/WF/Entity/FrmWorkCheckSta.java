package BP.WF.Entity;

/**
 * 审核组件状态
 */
public enum FrmWorkCheckSta
{
	/**
	 * 不可用
	 */
	Disable,
	/**
	 * 可用
	 */
	Enable,
	/**
	 * 只读
	 */
	Readonly;
	
	public int getValue()
	{
		return this.ordinal();
	}
	
	public static FrmWorkCheckSta forValue(int value)
	{
		return values()[value];
	}
}