package BP.En;

/**
 * 自动填充方式
 */
public enum AutoFullWay
{
	/**
	 * 不设置
	 */
	Way0,
	/**
	 * 方式1
	 */
	Way1_JS,
	/**
	 * sql 方式。
	 */
	Way2_SQL,
	/**
	 * 外键
	 */
	Way3_FK,
	/**
	 * 明细
	 */
	Way4_Dtl,
	/**
	 * 脚本
	 */
	Way5_JS;
	
	public int getValue()
	{
		return this.ordinal();
	}
	
	public static AutoFullWay forValue(int value)
	{
		return values()[value];
	}
}