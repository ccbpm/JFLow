package bp.gpm.home;

/** 
内容类型
*/
public enum WinDocType
{
	/** 
	 Html
	*/
	Html(0),
	/** 
	 系统内置
	*/
	System(1),
	/** 
	 SQL列表
	*/
	SQLList(2),
	/** 
	 折线图
	*/
	ChatZheXian(3),
	/** 
	 柱状图
	*/
	ChatZhuZhuang(4),
	/** 
	 饼图
	*/
	ChatPie(5);

	public static final int SIZE = java.lang.Integer.SIZE;

	private int intValue;
	private static java.util.HashMap<Integer, WinDocType> mappings;
	private static java.util.HashMap<Integer, WinDocType> getMappings()
	{
		if (mappings == null)
		{
			synchronized (WinDocType.class)
			{
				if (mappings == null)
				{
					mappings = new java.util.HashMap<Integer, WinDocType>();
				}
			}
		}
		return mappings;
	}

	private WinDocType(int value)
	{
		intValue = value;
		getMappings().put(value, this);
	}

	public int getValue()
	{
		return intValue;
	}

	public static WinDocType forValue(int value)
	{
		return getMappings().get(value);
	}
}

