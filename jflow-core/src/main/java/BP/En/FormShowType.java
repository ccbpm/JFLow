package BP.En;

/**
 * 显示方式
 */
public enum FormShowType
{
	/**
	 * 未设置
	 */
	NotSet,
	/**
	 * 傻瓜表单
	 */
	FixForm,
	/**
	 * 自由表单
	 */
	FreeForm;
	
	public int getValue()
	{
		return this.ordinal();
	}
	
	public static FormShowType forValue(int value)
	{
		return values()[value];
	}
}