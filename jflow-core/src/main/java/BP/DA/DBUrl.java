package BP.DA;

/**
 * DBUrl 的摘要说明。
 */
public class DBUrl
{
	/**
	 * 连接
	 */
	public DBUrl()
	{
	}
	
	/**
	 * 连接
	 * 
	 * @param type
	 *            连接type
	 */
	public DBUrl(DBUrlType type)
	{
		this.setDBUrlType(type);
	}
	
	/**
	 * 默认值
	 */
	public DBUrlType _DBUrlType = DBUrlType.AppCenterDSN;
	
	/**
	 * 要连接的到的库。
	 */
	public final DBUrlType getDBUrlType()
	{
		return _DBUrlType;
	}
	
	public final void setDBUrlType(DBUrlType value)
	{
		_DBUrlType = value;
	}
	
	public final String getDBVarStr()
	{
		switch (this.getDBType())
		{
			case Oracle:
			case DM:
				return ":";
			case MySQL:
				return ":";
			case MSSQL:
				return ":";
			case Informix:
				return "?";
			default:
				return "@";
		}
	}
	
	/**
	 * 数据库类型
	 */
	public final DBType getDBType()
	{
		switch (this.getDBUrlType())
		{
			case AppCenterDSN:
				return DBAccess.getAppCenterDBType();		  
			case DBAccessOfOLE:
				return DBType.Access;
			case DBAccessOfOracle1:		 
				return DBType.Oracle;
			default:
				throw new RuntimeException("不明确的连接");
		}
	}
}