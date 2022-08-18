package bp.sys;

import bp.*;

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
	 PGSQL
	*/
	PostgreSQL(5),
	/** 
	 人大金仓
	*/
	KingBaseR3(6),
	KingBaseR6(7),
	/** 
	 WebService数据源
	*/
	WebServices(100),
	/** 
	 海尔的Dubbo服务.
	*/
	Dubbo(50),
	/// <summary>
	/// 优炫
	/// </summary>
	UX(9);

	public static final int SIZE = java.lang.Integer.SIZE;

	private int intValue;
	private static java.util.HashMap<Integer, DBSrcType> mappings;
	private static java.util.HashMap<Integer, DBSrcType> getMappings()  {
		if (mappings == null)
		{
			synchronized (DBSrcType.class)
			{
				if (mappings == null)
				{
					mappings = new java.util.HashMap<Integer, DBSrcType>();
				}
			}
		}
		return mappings;
	}

	private DBSrcType(int value)
	{intValue = value;
		getMappings().put(value, this);
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