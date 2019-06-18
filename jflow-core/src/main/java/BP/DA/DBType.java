package BP.DA;

/**
 * 数据库类型
 */
public enum DBType
{
	/**
	 * sqlserver
	 */
	MSSQL,
	/**
	 * oracle
	 */
	Oracle,
	/**
	 * Access
	 */
	Access,
	/**
	 * Sybase
	 */
	Sybase,
	/**
	 * DB2
	 */
	DB2,
	/**
	 * MySQL
	 */
	MySQL,
	/**
	 * Informix
	 */
	Informix, 
	
	/**
	 * WebServices
	 */
	WebServices,
	
	
	PostgreSQL;
	
	public int getValue()
	{
		return this.ordinal();
	}
	
	public static DBType forValue(int value)
	{
		return values()[value];
	}
}