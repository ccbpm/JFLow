package BP.En;

/**
 * 逻辑类型
 */
public enum FieldTypeS
{
	/**
	 * 普通类型
	 */
	Normal,
	/**
	 * 枚举类型
	 */
	Enum,
	/**
	 * 外键
	 */
	FK,
	/** 
	 功能页面
	 
	*/
	WinOpen;
	public int getValue()
	{
		return this.ordinal();
	}
	
	public static FieldTypeS forValue(int value)
	{
		return values()[value];
	}
}