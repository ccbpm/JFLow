package BP.WF.Entity;

/**
 * 显示格式
 */
public enum FrmWorkShowModel
{
	/**
	 * 表格
	 */
	Table,
	/**
	 * 自由显示
	 */
	Free,
	/**
	 * 签章模式
	 */
	Sign;
	public int getValue()
	{
		return this.ordinal();
	}
	
	public static FrmWorkShowModel forValue(int value)
	{
		return values()[value];
	}
}