package BP.Sys;

/**
 * 组织结构类型
 */
public enum OSModel
{
	/**
	 * 一个人一个部门模式.
	 */
	OneOne,
	/**
	 * 一个人多个部门模式
	 */
	OneMore;
	
	public int getValue()
	{
		return this.ordinal();
	}
	
	public static OSModel forValue(int value)
	{
		return values()[value];
	}
}