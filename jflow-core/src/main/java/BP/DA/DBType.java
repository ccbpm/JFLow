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
	 * PostgreSQL
	 */
	PostgreSQL,
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
	WebServices;
	 
	
	public int getValue()
	{
		return this.ordinal();
	}
	
	public static DBType forValue(int value)
	{
		return values()[value];
	}
}