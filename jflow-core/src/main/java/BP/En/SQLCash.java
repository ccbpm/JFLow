package BP.En;

/**
 * 缓存SQL
 */
public class SQLCash
{
	public String EnName = null;
	public String Insert = null;
	public String Update = null;
	public String Delete = null;
	public String Select = null;
	
	public SQLCash()
	{
		
	}
	
	public SQLCash(Entity en)
	{
		this.EnName = en.toString();
		this.Insert = SqlBuilder.InsertForPara(en);
		this.Update = SqlBuilder.UpdateForPara(en, null);
		this.Delete = SqlBuilder.DeleteForPara(en);
		this.Select = SqlBuilder.RetrieveForPara(en);
		
		// switch (en.EnMap.EnDBUrl.DBType)
		// {
		// case BP.DA.DBType.Access:
		// break;
		// default:
		
		// break;
		// }
	}
	
	/**
	 * 获取指定的key, 返回更新的语句。
	 * 
	 * @param keys
	 * @return
	 */
	public final String GetUpdateSQL(Entity en, String[] keys)
	{
		if (keys == null)
		{
			return this.Update;
		}
		
		String mykey = "";
		for (String k : keys)
		{
			mykey += k;
		}
		
		/*
		 * warning java.util.Hashtable tempVar =
		 * this.getUpdateSQLs().get(mykey);
		 */
		Object tempVar = this.getUpdateSQLs().get(mykey);
		String sql = (String) ((tempVar instanceof String) ? tempVar : null);
		if (sql == null)
		{
			getUpdateSQLs().put(mykey, SqlBuilder.UpdateForPara(en, keys));
		}
		
		/*
		 * warning java.util.Hashtable tempVar2 = getUpdateSQLs().get(mykey);
		 */
		Object tempVar2 = getUpdateSQLs().get(mykey);
		sql = (String) ((tempVar2 instanceof String) ? tempVar2 : null);
		
		if (sql == null)
		{
			throw new RuntimeException("@error");
		}
		
		return sql;
	}
	
	// UpdateSQLs
	private java.util.Hashtable _UpdateSQLs;
	
	public final java.util.Hashtable getUpdateSQLs()
	{
		if (_UpdateSQLs == null)
		{
			_UpdateSQLs = new java.util.Hashtable();
		}
		return _UpdateSQLs;
	}
}