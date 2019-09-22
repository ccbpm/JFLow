package BP.DA;

/** 
 DBUrl 的摘要说明。
*/
public class DBUrl
{
	/** 
	 连接
	*/
	public DBUrl()
	{
	}
	/** 
	 连接
	 
	 @param type 连接type
	*/
	public DBUrl(DBUrlType type)
	{
		this.setDBUrlType(type);
	}
	/** 
	 创建数据库连接
	 
	 @param type 连接type
	*/
	public DBUrl(String dbSrc)
	{
		//数据库类型.
		this.setDBUrlType(DA.DBUrlType.DBSrc);

		//数据库连接.
		this.setHisDBSrc(new BP.Sys.SFDBSrc(dbSrc));
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 其他数据源.
	private BP.Sys.SFDBSrc _HisDBSrc = null;
	public final BP.Sys.SFDBSrc getHisDBSrc()
	{
		return _HisDBSrc;
	}
	public final void setHisDBSrc(BP.Sys.SFDBSrc value)
	{
		_HisDBSrc = value;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 其他数据源.

	/** 
	 默认值
	*/
	public DBUrlType _DBUrlType = DBUrlType.AppCenterDSN;
	/** 
	 要连接的到的库。
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
			case PostgreSQL:
				return ":";
			case MySQL:
			case MSSQL:
				return "@";
			case Informix:
				return "?";
			default:
				return "@";
		}
	}
	/** 
	 数据库类型
	*/
	public final DBType getDBType()
	{
		switch (this.getDBUrlType())
		{
			case AppCenterDSN:
				return DBAccess.getAppCenterDBType();
			case DBAccessOfMSSQL1:
			case DBAccessOfMSSQL2:
				return DBType.MSSQL;
			case DBAccessOfOLE:
				return DBType.Access;
			case DBAccessOfOracle1:
			case DBAccessOfOracle2:
				return DBType.Oracle;
			case DBSrc:
				return this.getHisDBSrc().getHisDBType();
			default:
				throw new RuntimeException("不明确的连接");
		}
	}
}