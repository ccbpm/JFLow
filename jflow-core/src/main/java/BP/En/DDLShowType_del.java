package BP.En;

/**
 * DDLShowType
 */
public enum DDLShowType_del
{
	/**
	 * None
	 */
	None(0),
	/**
	 * Gender
	 */
	Gender(1),
	/**
	 * Boolean
	 */
	Boolean(2),
	/**
	 * Year
	 */
	Year(3),
	/**
	 * Month
	 */
	Month(4),
	/**
	 * Day
	 */
	Day(5),
	/**
	 * hh
	 */
	hh(6),
	/**
	 * mm
	 */
	mm(7),
	/**
	 * 季度
	 */
	Quarter(8),
	/**
	 * Week
	 */
	Week(9),
	/**
	 * 系统枚举类型 SelfBindKey="系统枚举Key"
	 */
	SysEnum(10),
	/**
	 * Self
	 */
	Self(11),
	/**
	 * 实体集合
	 */
	Ens(12),
	/**
	 * 与Table 相关联
	 */
	BindTable(13);
	
	private int intValue;
	private static java.util.HashMap<Integer, DDLShowType_del> mappings;
	
	private synchronized static java.util.HashMap<Integer, DDLShowType_del> getMappings()
	{
		if (mappings == null)
		{
			mappings = new java.util.HashMap<Integer, DDLShowType_del>();
		}
		return mappings;
	}
	
	private DDLShowType_del(int value)
	{
		intValue = value;
		DDLShowType_del.getMappings().put(value, this);
	}
	
	public int getValue()
	{
		return intValue;
	}
	
	public static DDLShowType_del forValue(int value)
	{
		return getMappings().get(value);
	}
}