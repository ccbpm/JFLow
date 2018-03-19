package BP.DTS;

/**
 * 运行类型
 */
public enum RunType
{
	/**
	 * 中间层方法
	 */
	Method,
	/**
	 * SQL文本
	 */
	SQL,
	/**
	 * 存储过程
	 */
	SP,
	/**
	 * 数据调度
	 */
	DataIO;
	
	public int getValue()
	{
		return this.ordinal();
	}
	
	public static RunType forValue(int value)
	{
		return values()[value];
	}
}