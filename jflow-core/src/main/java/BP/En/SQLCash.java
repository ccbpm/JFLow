package BP.En;

/**
 * 缓存SQL
 */
public class SQLCash {

	//把row 
	private Row _Row = null;
	public Row getRow() throws Exception {
		
		if (_Row == null) {
			this._Row = new Row();
			this._Row.LoadAttrs(this.en.getEnMap().getAttrs());		 
		}
		
		Row row  =(Row) this._Row.clone();
		return row;		 
	}
	
	public Row CreateNewRow()
	{
		Row row  =(Row) this._Row.clone();
		return row;
	}

	public String EnName = null;

	private String Insert = null;

	public String getInsert() throws Exception {
		if (Insert == null)			
			this.Insert = SqlBuilder.InsertForPara(en);
		return this.Insert;
	}

	private String Update = null;

	public String getUpdate() throws Exception {
		if (Update == null)
			this.Update = SqlBuilder.UpdateForPara(en, null);
		return this.Update;
	}

	private String Delete = null;

	public String getDelete() throws Exception {
		if (Delete == null)
			this.Delete = SqlBuilder.DeleteForPara(en);
		return this.Delete;
	}

	private String Select = null;

	public String getSelect() throws Exception {
		if (Select == null)
			this.Select = SqlBuilder.RetrieveForPara(en);
		return this.Select;
	}

	private Entity en = null;

	public SQLCash() {

	}

	public SQLCash(Entity enObj) throws Exception {
		this.EnName = enObj.toString();
		this.en = enObj;

	}

	/**
	 * 获取指定的key, 返回更新的语句。
	 * 
	 * @param keys
	 * @return
	 * @throws Exception
	 */
	public final String GetUpdateSQL(Entity en, String[] keys) throws Exception {
		if (keys == null) {
			return this.getUpdate();
		}

		String mykey = "";
		for (String k : keys) {
			mykey += k;
		}

		Object tempVar = this.getUpdateSQLs().get(mykey);
		String sql = (String) ((tempVar instanceof String) ? tempVar : null);
		if (sql == null) {
			getUpdateSQLs().put(mykey, SqlBuilder.UpdateForPara(en, keys));
		}

		Object tempVar2 = getUpdateSQLs().get(mykey);
		sql = (String) ((tempVar2 instanceof String) ? tempVar2 : null);

		if (sql == null) {
			throw new RuntimeException("@error");
		}

		return sql;
	}

	// UpdateSQLs
	private java.util.Hashtable _UpdateSQLs;

	public final java.util.Hashtable getUpdateSQLs() {
		if (_UpdateSQLs == null) {
			_UpdateSQLs = new java.util.Hashtable();
		}
		return _UpdateSQLs;
	}
}