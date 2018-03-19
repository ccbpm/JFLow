package BP.Sys;

/** 
 表数据来源类型
 
*/
public enum SrcType
{
	/** 
	 本地的类
	 
	*/
	BPClass(0),
	/** 
	 通过ccform创建表
	 
	*/
	CreateTable(1),
	/** 
	 表或视图
	 
	*/
	TableOrView(2),
	/** 
	 SQL查询数据
	 
	*/
	SQL(3),
	/** 
	 WebServices
	 
	*/
	WebServices(4);

	private int intValue;
	private static java.util.HashMap<Integer, SrcType> mappings;
	private synchronized static java.util.HashMap<Integer, SrcType> getMappings()
	{
		if (mappings == null)
		{
			mappings = new java.util.HashMap<Integer, SrcType>();
		}
		return mappings;
	}

	private SrcType(int value)
	{
		intValue = value;
		SrcType.getMappings().put(value, this);
	}

	public int getValue()
	{
		return intValue;
	}

	public static SrcType forValue(int value)
	{
		return getMappings().get(value);
	}
}