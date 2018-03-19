package BP.Sys;


/** 
 数据源类型
 
*/
public enum DBSrcType
{
	/** 
	 本机数据库
	 
	*/
	Localhost(0),
	/** 
	 SQL
	 
	*/
	SQLServer(1),
	/** 
	 Oracle
	 
	*/
	Oracle(2),
	/** 
	 MySQL
	 
	*/
	MySQL(3),
	/** 
	 Informix
	 
	*/
	Informix(4),
	/** 
	 WebService数据源
	 
	*/
	WebServices(100),
	/** 
	 海尔的double服务.
	 
	*/
	Double(200);

	private int intValue;
	private static java.util.HashMap<Integer, DBSrcType> mappings;
	private synchronized static java.util.HashMap<Integer, DBSrcType> getMappings()
	{
		if (mappings == null)
		{
			mappings = new java.util.HashMap<Integer, DBSrcType>();
		}
		return mappings;
	}

	private DBSrcType(int value)
	{
		intValue = value;
		DBSrcType.getMappings().put(value, this);
	}

	public int getValue()
	{
		return intValue;
	}

	public static DBSrcType forValue(int value)
	{
		return getMappings().get(value);
	}
}