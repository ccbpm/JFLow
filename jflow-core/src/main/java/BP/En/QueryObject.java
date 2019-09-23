package BP.En;


import BP.DA.DBAccess;
import BP.DA.DBType;
import BP.DA.DBUrlType;
import BP.DA.DataColumn;
import BP.DA.DataRow;
import BP.DA.DataTable;
import BP.DA.GroupWay;
import BP.DA.OrderWay;
import BP.DA.Para;
import BP.DA.Paras;
import BP.Sys.SystemConfig;
import BP.Tools.StringHelper;

/**
 * QueryObject 的摘要说明。
 */
public class QueryObject {
	private Entity _en = null;
	private Entities _ens = null;
	private String _sql = "";

	private Entity getEn() {
		if (this._en == null) {
			return this.getEns().getNewEntity();
		} else {
			return this._en;
		}
	}

	private Entities getEns() {
		return this._ens;
	}


	/**
	 * 处理Order by , group by .
	 */
	private String _groupBy = "";

	/**
	 * 要得到的查询sql 。
	 * 
	 * @throws Exception
	 */
	public final String getSQL() throws Exception {

		String sql = "";
		String selecSQL = SqlBuilder.SelectSQL(this.getEn(), this.getTop());

		if (this._sql == null || this._sql.length() == 0) {
			sql = selecSQL + this._groupBy + this._orderBy;
		} else {
			if (selecSQL.contains(" WHERE ")) {
				sql = selecSQL + "  AND ( " + this._sql + " ) " + _groupBy + this._orderBy;
			} else {
				sql = selecSQL + " WHERE   ( " + this._sql + " ) " + _groupBy + this._orderBy;
			}
		}

		sql = sql.replace("  ", " ");
		sql = sql.replace("  ", " ");

		sql = sql.replace("WHERE AND", "WHERE");
		sql = sql.replace("WHERE  AND", "WHERE");

		sql = sql.replace("WHERE ORDER", "ORDER");
		return sql;
	}

	public final void setSQL(String value) {
		if (value.indexOf("(") == -1) {
			this.IsEndAndOR = false;
		} else {
			this.IsEndAndOR = true;
		}

		this._sql = this._sql + " " + value;
	}

	public final String getSQLWithOutPara() throws Exception {
		String sql = this.getSQL();
		for (Para en : this.getMyParas()) {
			sql = sql.replace(SystemConfig.getAppCenterDBVarStr() + en.ParaName, "'" + en.val.toString() + "'");
		}
		return sql;
	}

	public final void AddWhere(String str) {
		this._sql = this._sql + " " + str;
	}

	/**
	 * 修改于2009 -05-12
	 */
	private int _Top = -1;

	public final int getTop() {
		return _Top;
	}

	public final void setTop(int value) {
		this._Top = value;
	}

	private Paras _Paras = null;

	public final Paras getMyParas() {
		if (_Paras == null) {
			_Paras = new Paras();
		}
		return _Paras;
	}

	public final void setMyParas(Paras value) {
		_Paras = value;
	}

	private Paras _ParasR = null;

	public final Paras getMyParasR() {
		if (_ParasR == null) {
			_ParasR = new Paras();
		}
		return _ParasR;
	}

	public final void AddPara(String key, Object v) {
		key = "P" + key;
		this.getMyParas().Add(key, v);
	}

	public QueryObject() {
	}

	/**
	 * DictBase
	 * @throws Exception 
	 */
	public QueryObject(Entity en) throws Exception {
		this.getMyParas().clear();
		this._en = en;
		this.HisDBType = this._en.getEnMap().getEnDBUrl().getDBType();
		this.HisDBUrlType = this._en.getEnMap().getEnDBUrl().getDBUrlType();
	}

	public QueryObject(Entities ens) throws Exception {
		this.getMyParas().clear();
		ens.clear();
		this._ens = ens;
		Entity en = this._ens.getNewEntity();
		this.HisDBType = SystemConfig.getAppCenterDBType();
		this.HisDBUrlType = en.getEnMap().getEnDBUrl().getDBUrlType();
	}

	public BP.DA.DBType HisDBType = DBType.MSSQL;
	public BP.DA.DBUrlType HisDBUrlType = DBUrlType.AppCenterDSN;

	public final String getHisVarStr() {
		switch (this.HisDBType) {
		case MSSQL:
		case Access:
			return ":";
		case MySQL:
			return ":";
		case Informix:
			return "?";
		default:
			return ":";
		}
	}

	/**
	 * 增加函数查寻．
	 * 
	 * @param attr
	 *            属性
	 * @param exp
	 *            表达格式 大于，等于，小于
	 * @param len
	 *            长度
	 * @throws Exception 
	 */
	public final void AddWhereLen(String attr, String exp, int len, BP.DA.DBType dbtype) throws Exception {
		this.setSQL("( " + BP.Sys.SystemConfig.getAppCenterDBLengthStr() + "( " + attr2Field(attr) + " ) " + exp + " '"
				+ (new Integer(len)).toString() + "')");
	}

	/**
	 * 增加查询条件，条件用 IN 表示．sql必须是一个列的集合．
	 * 
	 * @param attr
	 *            属性
	 * @param sql
	 *            此sql,必须是有一个列的集合．
	 * @throws Exception 
	 */
	public final void AddWhereInSQL(String attr, String sql) throws Exception {
		this.AddWhere(attr, " IN ", "( " + sql + " )");
	}

	/**
	 * 增加查询条件，条件用 EXISTS 表示．sql查询的字段必须有与attr同名的字段, 否则EXISTS不成立, 报错
	 * 
	 * @param attr
	 *            属性
	 * @param sql
	 *            此sql,必须是有一个列的集合．
	 * @throws Exception 
	 */
	public final void AddWhereExistsSQL(String attr, String sql) throws Exception {
		this.AddWhere(attr, " EXISTS ", "( " + sql + " )");
	}

	/**
	 * 增加查询条件，条件用 IN 表示．sql必须是一个列的集合．
	 * 
	 * @param attr
	 *            属性
	 * @param sql
	 *            此sql,必须是有一个列的集合．
	 * @throws Exception 
	 */
	public final void AddWhereInSQL(String attr, String sql, String orderBy) throws Exception {
		this.AddWhere(attr, " IN ", "( " + sql + " )");
		this.addOrderBy(orderBy);
	}

	/**
	 * 增加查询条件，条件用 IN 表示．sql必须是一个列的集合．
	 * 
	 * @param attr
	 *            属性
	 * @param sql
	 *            此sql,必须是有一个列的集合．
	 * @throws Exception 
	 */
	public final void AddWhereNotInSQL(String attr, String sql) throws Exception {
		this.AddWhere(attr, " NOT IN ", " ( " + sql + " ) ");
	}

	public final void AddWhereNotIn(String attr, String val) throws Exception {
		this.AddWhere(attr, " NOT IN ", " ( " + val + " ) ");
	}

	/**
	 * 增加条件, DataTable 第一列的值．
	 * 
	 * @param attr
	 *            属性
	 * @param dt
	 *            第一列是要组合的values
	 * @throws Exception 
	 */
	public final void AddWhereIn(String attr, DataTable dt) throws Exception {
		String strs = "";
		for (DataRow dr : dt.Rows) {
			strs += dr.getValue(0).toString() + ",";
			/*
			 * warning strs += dr.getValue(0).toString() + ",";
			 */
		}
		strs = strs.substring(strs.length() - 1, strs.length() - 1 + 0);
		this.AddWhereIn(attr, strs);
	}

	/**
	 * 增加条件,vals 必须是sql可以识别的字串．
	 * 
	 * @param attr
	 *            属性
	 * @param vals
	 *            用 , 分开的．
	 * @throws Exception 
	 */
	public final void AddWhereIn(String attr, String vals) throws Exception {
		this.AddWhere(attr, " IN ", vals);
	}

	/**
	 * @param attr
	 * @param exp
	 * @param val
	 * @throws Exception 
	 */
	public final void AddWhere(String attr, String exp, String val) throws Exception {
		AddWhere(attr, exp, val, null);
	}

	/**
	 * 增加条件
	 * 
	 * @param attr
	 *            属性
	 * @param exp
	 *            操作符号（根据不同的数据库）
	 * @param val
	 *            、 * 值
	 * @param paraName
	 *            参数名称，可以为null, 如果查询中有多个参数中有相同属性名的需要，分别给他们起一个参数名。
	 * @throws Exception 
	 */
	public final void AddWhere(String attr, String exp, String val, String paraName) throws Exception {

		if (val == null)
			val = "";

		if (val.equals("all")) {
			this.setSQL("( 1=1 )");
			return;
		}

		if (exp.toLowerCase().contains("in")) {
			this.setSQL("( " + attr2Field(attr) + " " + exp + "  " + val + " )");

			// 暂时还原代码，测试发现有问题 by 于庆海
			/*
			 * // in效率低下, 改为exists StringBuilder sql = new StringBuilder();
			 * sql.append(" EXISTS ( "); sql.append("	SELECT 1 FROM ( ");
			 * sql.append( val); sql.append("	) JFLOW_ ");
			 * sql.append("	WHERE ");
			 * sql.append("		JFLOW_.").append(attr).append("=").append(
			 * attr2Field(attr)); sql.append(" ) ");
			 * 
			 * this.setSQL("( " + sql.toString() + " )");
			 */
			return;
		}

		if (exp.toLowerCase().contains(" exists")) {
			StringBuilder sql = new StringBuilder();
			sql.append(" EXISTS ( ");
			sql.append("	SELECT 1 FROM ( ");
			sql.append(val);
			sql.append("	) JFLOW_ ");
			sql.append("	WHERE ");
			sql.append("		JFLOW_.").append(attr).append("=").append(attr2Field(attr));
			sql.append(" ) ");

			this.setSQL("( " + sql.toString() + " )");
			return;
		}

		if (exp.toLowerCase().contains("like")) {
			if (attr.equals("FK_Dept")) {
				val = val.replace("'", "");
				val = val.replace("%", "");

				switch (this.HisDBType) {
				case Oracle:
					this.setSQL(
							"( " + attr2Field(attr) + " " + exp + " '%'||" + this.getHisVarStr() + "FK_Dept||'%' )");
					this.getMyParas().Add("FK_Dept", val);
					break;
				default:
					this.setSQL("( " + attr2Field(attr) + " " + exp + "  '" + val + "%' )");
					break;
				}
			} else {
				if (val.indexOf(":")==0 || val.indexOf("@")==0) {
					this.setSQL("( " + attr2Field(attr) + " " + exp + "  " + val + " )");
				} else {
					if (!val.contains("'")) {
						this.setSQL("( " + attr2Field(attr) + " " + exp + "  '" + val + "' )");
					} else {
						this.setSQL("( " + attr2Field(attr) + " " + exp + "  " + val + " )");
					}
				}
			}
			return;
		}
		if (this.getHisVarStr().equals("?")) {
			this.setSQL("( " + attr2Field(attr) + " " + exp + "?)");
			this.getMyParas().Add(attr, val);
		} else {
			if (paraName == null) {
				this.setSQL("( " + attr2Field(attr) + " " + exp + this.getHisVarStr() + attr + ")");
				this.getMyParas().Add(attr, val);
			} else {
				this.setSQL("( " + attr2Field(attr) + " " + exp + this.getHisVarStr() + paraName + ")");
				this.getMyParas().Add(paraName, val);
			}
		}
	}

	public final void AddWhereDept(String val) throws Exception {
		String attr = "FK_Dept";
		String exp = "=";

		if (!val.contains("'")) {
			this.setSQL("( " + attr2Field(attr) + " " + exp + "  '" + val + "' )");
		} else {
			this.setSQL("( " + attr2Field(attr) + " " + exp + "  " + val + " )");
		}
	}

	/**
	 * 是空的 @ param attr
	 * @throws Exception 
	 */
	public final void AddWhereIsNull(String attr) throws Exception {
		this.setSQL("( " + attr2Field(attr) + "  IS NULL OR  " + attr2Field(attr) + "='' )");
	}

	public final void AddWhereField(String attr, String exp, String val) {
		if (val.toString().equals("all")) {
			this.setSQL("( 1=1 )");
			return;
		}

		if (exp.toLowerCase().contains(" in")) {
			this.setSQL("( " + attr + " " + exp + "  " + val + " )");
			return;
		}

		this.setSQL("( " + attr + " " + exp + " :" + attr + " )");
		this.getMyParas().Add(attr, val);
	}

	public final void AddWhereField(String attr, String exp, int val) {
		if ((new Integer(val)).toString().equals("all")) {
			this.setSQL("( 1=1 )");
			return;
		}

		if (exp.toLowerCase().contains(" in")) {
			this.setSQL("( " + attr + " " + exp + "  " + val + " )");
			return;
		}

		if (attr.equals("RowNum")) {
			this.setSQL("( " + attr + " " + exp + "  " + val + " )");
			return;
		}

		if (this.getHisVarStr().equals("?")) {
			this.setSQL("( " + attr + " " + exp + "?)");
		} else {
			this.setSQL("( " + attr + " " + exp + "  " + this.getHisVarStr() + attr + " )");
		}

		this.getMyParas().Add(attr, val);
	}

	/**
	 * 增加条件
	 * 
	 * @param attr
	 *            属性
	 * @param exp
	 *            操作符号（根据不同的数据库）
	 * @param val
	 *            值
	 * @throws Exception 
	 */
	public final void AddWhere(String attr, String exp, int val) throws Exception {
		if (attr.equals("RowNum")) {
			this.setSQL("( " + attr2Field(attr) + " " + exp + " " + val + ")");
		} else {
			if (this.getHisVarStr().equals("?")) {
				this.setSQL("( " + attr2Field(attr) + " " + exp + "?)");
			} else {
				this.setSQL("( " + attr2Field(attr) + " " + exp + this.getHisVarStr() + attr + ")");
			}

			this.getMyParas().Add(attr, val);
		}
	}

	public final void AddHD() {
		this.setSQL("(  1=1 ) ");
	}

	/**
	 * 非恒等。
	 */
	public final void AddHD_Not() {
		this.setSQL("(  1=2 ) ");
	}

	/**
	 * 增加条件
	 * 
	 * @param attr
	 *            属性
	 * @param exp
	 *            操作符号（根据不同的数据库）
	 * @param val
	 *            值
	 * @throws Exception 
	 */
	public final void AddWhere(String attr, String exp, float val) throws Exception {
		this.getMyParas().Add(attr, val);
		if (this.getHisVarStr().equals("?")) {
			this.setSQL("( " + attr2Field(attr) + " " + exp + "?)");
		} else {
			this.setSQL("( " + attr2Field(attr) + " " + exp + " " + this.getHisVarStr() + attr + ")");
		}
	}

	/**
	 * 增加条件(默认的是= )
	 * 
	 * @param attr
	 *            属性
	 * @param val
	 *            值
	 * @throws Exception 
	 */
	public final void AddWhere(String attr, String val) throws Exception {
		this.AddWhere(attr, "=", val);
	}

	public final void AddWhere(String attr, int val) throws Exception {
		this.AddWhere(attr, "=", val);
	}

	/**
	 * 增加条件
	 * 
	 * @param attr
	 *            属性
	 * @param val
	 *            值 true/false
	 * @throws Exception 
	 */
	public final void AddWhere(String attr, boolean val) throws Exception {
		if (val) {
			this.AddWhere(attr, "=", 1);
		} else {
			this.AddWhere(attr, "=", 0);
		}
	}

	public final void AddWhere(String attr, long val) throws Exception {
		this.AddWhere(attr, (new Long(val)).toString());
	}

	public final void AddWhere(String attr, float val) throws Exception {
		this.AddWhere(attr, "=", val);
	}

	@SuppressWarnings("rawtypes")
	public final void AddWhere(String attr, Object val) throws Exception {

		if (val instanceof Enum) {
			this.AddWhere(attr, ((Enum) val).ordinal());
			return;
		}

		if (val == null) {
			throw new RuntimeException("Attr=" + attr + ", val is null");
		}

		if (val.getClass() == Integer.class || val.getClass() == Integer.class) {
			// int i = int.Parse(val.ToString()) ;
			this.AddWhere(attr, "=", ((Integer) val).intValue());
			return;
		}
		this.AddWhere(attr, "=", val.toString());
	}

	public final void addLeftBracket() {
		this.setSQL(" ( ");
	}

	public final void addRightBracket() {
		this.setSQL(" ) ");
		this.IsEndAndOR = true;
	}

	public final void addAnd() {
		this.setSQL(" AND ");
	}

	public final void addOr() {
		this.setSQL(" OR ");
	}

	// 关于endsql
	public final void addGroupBy(String attr) throws Exception {
		this._groupBy = " GROUP BY  " + attr2Field(attr);
	}

	public final void addGroupBy(String attr1, String attr2) throws Exception {
		this._groupBy = " GROUP BY  " + attr2Field(attr1) + " , " + attr2Field(attr2);
	}

	private String _orderBy = "";

	public final void addOrderBy(String attr) throws Exception {
		if (this._orderBy.indexOf("ORDER BY") != -1) {
			this._orderBy = " , " + attr2Field(attr);
		} else {
			this._orderBy = " ORDER BY " + attr2Field(attr);
		}
	}

	/**
	 * @param attr
	 */
	public final void addOrderByRandom() {
		if (this._orderBy.indexOf("ORDER BY") != -1) {
			this._orderBy = " , NEWID()";
		} else {
			this._orderBy = " ORDER BY NEWID()";
		}
	}

	/**
	 * addOrderByDesc
	 * 
	 * @param attr
	 * @param desc
	 * @throws Exception 
	 */
	public final void addOrderByDesc(String attr) throws Exception {
		this._orderBy = " ORDER BY " + attr2Field(attr) + " DESC ";
	}

	public final void addOrderByDesc(String attr1, String attr2) throws Exception {
		this._orderBy = " ORDER BY  " + attr2Field(attr1) + " DESC ," + attr2Field(attr2) + " DESC";
	}

	public final void addOrderBy(String attr1, String attr2) throws Exception {
		this._orderBy = " ORDER BY  " + attr2Field(attr1) + "," + attr2Field(attr2);
	}

	public final void addHaving() {
	}

	/**
	 * 清除查询条件
	 */
	public final void clear() {
		this._sql = "";
		this._groupBy = "";
		// this._orderBy = "";
		this.getMyParas().clear();
	}

	private Map _HisMap;

	public final Map getHisMap() throws Exception {
		if (_HisMap == null) {
			_HisMap = this.getEn().getEnMap();
		}
		return _HisMap;
	}

	public final void setHisMap(Map value) {
		_HisMap = value;
	}

	private String attr2Field(String attrKey) throws Exception {
		// @yln翻译
		Attr attr = this.getHisMap().GetAttrByKey(attrKey);
		if (attr.getIsRefAttr() == true) {
			if (this.HisDBType == DBType.Oracle) {
				return "T" + attr.getKey().replace("Text", "") + ".Name";
			} else {
				Entity en = attr.getHisFKEn();
				return en.getEnMap().getPhysicsTable() + "_" + attr.getKey().replace("Text", "") + ".Name";
			}
		}
		return this.getHisMap().getPhysicsTable() + "." + attr.getField();
	}

	public final DataTable DoGroupReturnTable(Entity en, Attrs attrsOfGroupKey, Attr attrGroup, GroupWay gw,
			OrderWay ow) throws Exception {
		switch (en.getEnMap().getEnDBUrl().getDBType()) {
		case Oracle:
			return DoGroupReturnTableOracle(en, attrsOfGroupKey, attrGroup, gw, ow);
		default:
			return DoGroupReturnTableSqlServer(en, attrsOfGroupKey, attrGroup, gw, ow);
		}
	}

	public final DataTable DoGroupReturnTableOracle(Entity en, Attrs attrsOfGroupKey, Attr attrGroup, GroupWay gw,
			OrderWay ow) throws Exception {
		// 生成要查询的语句
		String fields = "";
		String str = "";
		for (Attr attr : attrsOfGroupKey) {
			if (attr.getField() == null) {
				continue;
			}

			str = "," + attr.getField();
			fields += str;
		}

		if (attrGroup.getKey().equals("MyNum")) {
			switch (gw) {
			case BySum:
				fields += ", COUNT(*) AS MyNum";
				break;
			case ByAvg:
				fields += ", AVG(" + attrGroup.getField() + ") AS MyNum";
				break;
			default:
				throw new RuntimeException("no such case:");
			}
		} else {
			switch (gw) {
			case BySum:
				fields += ",SUM(" + attrGroup.getField() + ") AS " + attrGroup.getKey();
				break;
			case ByAvg:
				fields += ",AVG(" + attrGroup.getField() + ") AS " + attrGroup.getKey();
				break;
			default:
				throw new RuntimeException("no such case:");
			}
		}

		String by = "";
		for (Attr attr : attrsOfGroupKey) {
			if (attr.getField() == null) {
				continue;
			}

			str = "," + attr.getField();
			by += str;
		}
		by = by.substring(1);
		// string sql
		String sql = "SELECT " + fields.substring(1) + " FROM " + this.getEn().getEnMap().getPhysicsTable() + " WHERE "
				+ this._sql + " Group BY " + by;

		Map map = new Map();
		map.setPhysicsTable("@VT@");
		map.setAttrs(attrsOfGroupKey);
		map.getAttrs().Add(attrGroup);

		String sql1 = SqlBuilder.SelectSQLOfOra(en.toString(), map) + " " + SqlBuilder.GenerFormWhereOfOra(en, map);

		sql1 = sql1.replace("@TopNum", "");
		sql1 = sql1.replace("FROM @VT@", "FROM (" + sql + ") VT");
		sql1 = sql1.replace("@VT@", "VT");
		sql1 = sql1.replace("TOP", "");

		if (ow == OrderWay.OrderByUp) {
			sql1 += " ORDER BY " + attrGroup.getKey() + " DESC ";
		} else {
			sql1 += " ORDER BY " + attrGroup.getKey();
		}

		return DBAccess.RunSQLReturnTable(sql1, this.getMyParas());
	}

	public final DataTable DoGroupReturnTableSqlServer(Entity en, Attrs attrsOfGroupKey, Attr attrGroup, GroupWay gw,
			OrderWay ow) throws Exception {

		// 生成要查询的语句
		String fields = "";
		String str = "";
		for (Attr attr : attrsOfGroupKey) {
			if (attr.getField() == null) {
				continue;
			}
			str = "," + attr.getField();
			fields += str;
		}

		if (attrGroup.getKey().equals("MyNum")) {
			switch (gw) {
			case BySum:
				fields += ", COUNT(*) AS MyNum";
				break;
			case ByAvg:
				fields += ", AVG(*)   AS MyNum";
				break;
			default:
				throw new RuntimeException("no such case:");
			}
		} else {
			switch (gw) {
			case BySum:
				fields += ",SUM(" + attrGroup.getField() + ") AS " + attrGroup.getKey();
				break;
			case ByAvg:
				fields += ",AVG(" + attrGroup.getField() + ") AS " + attrGroup.getKey();
				break;
			default:
				throw new RuntimeException("no such case:");
			}
		}

		String by = "";
		for (Attr attr : attrsOfGroupKey) {
			if (attr.getField() == null) {
				continue;
			}

			str = "," + attr.getField();
			by += str;
		}
		by = by.substring(1);
		// string sql
		String sql = "SELECT " + fields.substring(1) + " FROM " + this.getEn().getEnMap().getPhysicsTable() + " WHERE "
				+ this._sql + " Group BY " + by;

		Map map = new Map();
		map.setPhysicsTable("@VT@");
		map.setAttrs(attrsOfGroupKey);
		map.getAttrs().Add(attrGroup);
		// string sql1=SqlBuilder.SelectSQLOfMS( map
		// )+" "+SqlBuilder.GenerFormWhereOfMS( en,map) + " AND ( " +
		// this._sql+" ) "+_endSql;

		String sql1 = SqlBuilder.SelectSQLOfMS(map) + " " + SqlBuilder.GenerFormWhereOfMS(en, map);

		sql1 = sql1.replace("@TopNum", "");

		sql1 = sql1.replace("FROM @VT@", "FROM (" + sql + ") VT");

		sql1 = sql1.replace("@VT@", "VT");
		sql1 = sql1.replace("TOP", "");
		if (ow == OrderWay.OrderByUp) {
			sql1 += " ORDER BY " + attrGroup.getKey() + " DESC ";
		} else {
			sql1 += " ORDER BY " + attrGroup.getKey();
		}
		return DBAccess.RunSQLReturnTable(sql1, this.getMyParas());
	}

	/**
	 * 在尾部上是否执行了 AddAnd()方法。
	 */
	public boolean IsEndAndOR = false;
	public String[] FullAttrs = null;

	/**
	 * 执行查询
	 * 
	 * @return
	 * @throws Exception
	 */
	public final int DoQuery() throws Exception {
		try {
			if (this._en == null) {
				return this.doEntitiesQuery();
			} else {
				return this.doEntityQuery();
			}

		} catch (RuntimeException ex) {
			if (this._en == null) {
				try {
					this._ens.getNewEntity().CheckPhysicsTable();
					return this.doEntitiesQuery();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				try {
					this._en.CheckPhysicsTable();
					return this.doEntityQuery();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			throw ex;
		}
	}

	public final String DealString(String sql) {
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		String strs = "";
		for (DataRow dr : dt.Rows) {
			strs += ",'" + dr.getValue(0).toString() + "'";
			/*
			 * warning strs += ",'" + dr.getValue(0).toString() + "'";
			 */
		}
		return strs.substring(1);
	}

	public final String GenerPKsByTableWithPara(String pk, String sql, int from, int to) {
		// Log.DefaultLogWriteLineWarning(" ***************************** From=
		// "
		// + from + " T0" + to);
		DataTable dt = DBAccess.RunSQLReturnTable(sql, this.getMyParas());
		String pks = "";
		int i = 0;
		int paraI = 0;

		String dbStr = SystemConfig.getAppCenterDBVarStr();
		for (DataRow dr : dt.Rows) {
			i++;
			if (i > from) {
				paraI++;
				// pks += "'" + dr.getValue(0).ToString() + "'";
				if (dbStr.equals("?")) {
					pks += "?,";
				} else {
					pks += SystemConfig.getAppCenterDBVarStr() + "R" + paraI + ",";
				}

				if (pk.equals("OID") || pk.equals("WorkID") || pk.equals("NodeID"))
					this.getMyParasR().Add("R" + paraI, Integer.parseInt( dr.getValue(0).toString()));
				else
					this.getMyParasR().Add("R" + paraI, dr.getValue(0).toString());

//				this.getMyParasR().Add("R" + paraI, dr.getValue(0).toString());
				/*
				 * warning this.getMyParasR().Add("R" + paraI,
				 * dr.getValue(0).toString());
				 */
				if (i >= to) {
					return pks.substring(0, pks.length() - 1);
				}
			}
		}
		if (pks.equals("")) {
			/*
			 * warning return null;
			 */
			// return " '1' ";
			return null;
		}
		return pks.substring(0, pks.length() - 1);
	}

	public final String GenerPKsByTable(String sql, int from, int to) {
		// Log.DefaultLogWriteLineWarning(" ***************************** From=
		// "
		// + from + " T0" + to);
		DataTable dt = DBAccess.RunSQLReturnTable(sql, this.getMyParas());
		String pks = "";
		int i = 0;
		for (DataRow dr : dt.Rows) {
			i++;
			if (i > from) {
				if (i >= to) {
					pks += "'" + dr.getValue(0).toString() + "'";
					/*
					 * warning pks += "'" + dr.getValue(0).toString() + "'";
					 */
					return pks;
				} else {
					pks += "'" + dr.getValue(0).toString() + "',";
					/*
					 * warning pks += "'" + dr.getValue(0).toString() + "',";
					 */
				}
			}
		}
		if (pks.equals("")) {
			return "  '11111111' ";
		}
		return pks.substring(0, pks.length() - 1);
	}

	/**
	 * @param pk
	 * @param pageSize
	 * @param pageIdx
	 * @return
	 * @throws Exception
	 */
	public final int DoQuery(String pk, int pageSize, int pageIdx) throws Exception {
		if (pk.equals("OID") || pk.equals("WorkID")) {
			return DoQuery(pk, pageSize, pageIdx, pk, true);
		} else {
			return DoQuery(pk, pageSize, pageIdx, pk, false);
		}
	}

	/**
	 * 分页查询方法
	 * 
	 * @param pk
	 *            主键
	 * @param pageSize
	 *            页面大小
	 * @param pageIdx
	 *            第x页
	 * @param orderby
	 *            排序
	 * @param orderway
	 *            排序方式: 两种情况 Down UP
	 * @return 查询结果
	 * @throws Exception
	 */
	public final int DoQuery(String pk, int pageSize, int pageIdx, String orderBy, String orderWay) throws Exception {
		if (orderWay.toLowerCase().trim().equals("up")) {
			return DoQuery(pk, pageSize, pageIdx, orderBy, false);
		} else {
			return DoQuery(pk, pageSize, pageIdx, orderBy, true);
		}
	}

	/**
	 * 分页查询方法
	 * 
	 * @param pk
	 *            主键
	 * @param pageSize
	 *            页面大小
	 * @param pageIdx
	 *            第x页
	 * @param orderby
	 *            排序
	 * @return 查询结果
	 * @throws Exception
	 */
	public final int DoQuery(String pk, int pageSize, int pageIdx, boolean isDesc) throws Exception {
		return DoQuery(pk, pageSize, pageIdx, pk, isDesc);
	}

	/**
	 * 分页查询方法
	 * 
	 * @param pk
	 *            主键
	 * @param pageSize
	 *            页面大小
	 * @param pageIdx
	 *            第x页
	 * @param orderby
	 *            排序
	 * @param orderway
	 *            排序方式: 两种情况 desc 或者 为 null.
	 * @return 查询结果
	 * @throws Exception
	 */
	public final int DoQuery(String pk, int pageSize, int pageIdx, String orderBy, boolean isDesc) throws Exception {
		int pageNum = 0;

		// 如果没有加入排序字段，使用主键
		if (StringHelper.isNullOrEmpty(this._orderBy)) {
			String isDescStr = "";
			if (isDesc) {
				isDescStr = " DESC ";
			}

			if (StringHelper.isNullOrEmpty(orderBy)) {
				orderBy = pk;
			}

			this._orderBy = attr2Field(orderBy) + isDescStr;
		}

		if (!this._orderBy.contains("ORDER BY")) {
			_orderBy = " ORDER BY " + this._orderBy;
		}

		try {
			if (this._en == null) {
				int recordConut = 0;
				recordConut = this.GetCount(); // 获取 它的数量。

				if (recordConut == 0) {
					this._ens.clear();
					return 0;
				}

				// xx!5555 提出的错误.
				if (pageSize == 0) {
					pageSize = 12;
				}

				pageNum = Math.round(recordConut / pageSize); // 页面个数。

				pageNum++;
				int top = pageSize * (pageIdx - 1);

				String sql = "";
				Entity en = this._ens.getNewEntity();
				Map map = en.getEnMap();
				int toIdx = 0;
				String pks = "";
				switch (map.getEnDBUrl().getDBType()) {
				case Oracle:
					toIdx = top + pageSize;
					if (this._sql.equals("") || this._sql == null) {
						if (top == 0) {
							sql = "SELECT * FROM ( SELECT  " + pk + " FROM " + map.getPhysicsTable() + " "
									+ this._orderBy + "   ) WHERE ROWNUM <=" + pageSize;
						} else {
							sql = "SELECT * FROM ( SELECT  " + pk + " FROM " + map.getPhysicsTable() + " "
									+ this._orderBy + ") ";
						}
					} else {
						String mySql = this.getSQL();
						mySql = mySql.substring(mySql.indexOf("FROM"));

						if (top == 0) {
							sql = "SELECT * FROM ( SELECT  " + map.getPhysicsTable() + "." + pk + " " + mySql
									+ " ) WHERE ROWNUM <=" + pageSize;

						} else {
							sql = "SELECT * FROM ( SELECT  " + map.getPhysicsTable() + "." + pk + " " + mySql + " )";

						}
					}

					sql = sql.replace("AND ( ( 1=1 ) )", " ");

					pks = this.GenerPKsByTableWithPara(pk, sql, top, toIdx);
					this.clear();
					this.setMyParas(this.getMyParasR());
					if (pks != null) {
						this.AddWhereIn(pk, "(" + pks + ")");
					} else {
						this.AddHD();
					}

					this.setTop(pageSize);
					return this.doEntitiesQuery();
				case Informix:
					toIdx = top + pageSize;
					if (this._sql.equals("") || this._sql == null) {
						if (top == 0) {
							sql = " SELECT first  " + pageSize + "  " + this.getEn().getPK() + " FROM "
									+ map.getPhysicsTable() + " " + this._orderBy;
						} else {
							sql = " SELECT  " + this.getEn().getPK() + " FROM " + map.getPhysicsTable() + " "
									+ this._orderBy;
						}
					} else {
						String mySql = this.getSQL();
						mySql = mySql.substring(mySql.indexOf("FROM"));

						if (top == 0) {
							sql = "SELECT first " + pageSize + " " + this.getEn().getPK() + " " + mySql;

						} else {
							sql = "SELECT  " + this.getEn().getPK() + " " + mySql;

						}
					}

					sql = sql.replace("AND ( ( 1=1 ) )", " ");

					pks = this.GenerPKsByTableWithPara(pk, sql, top, toIdx);
					this.clear();
					this.setMyParas(this.getMyParasR());

					if (pks == null) {
						this.AddHD_Not();
					} else {
						this.AddWhereIn(pk, "(" + pks + ")");
					}

					this.setTop(pageSize);
					return this.doEntitiesQuery();
				case MySQL:
					toIdx = top + pageSize;
					if (this._sql.equals("") || this._sql == null) {
						if (top == 0) {
							sql = " SELECT  " + this.getEn().getPK() + " FROM " + map.getPhysicsTable() + " "
									+ this._orderBy + " LIMIT " + pageSize;
						} else {
							sql = " SELECT  " + this.getEn().getPK() + " FROM " + map.getPhysicsTable() + " "
									+ this._orderBy;
						}
					} else {
						String mySql = this.getSQL();
						mySql = mySql.substring(mySql.indexOf("FROM"));

						if (top == 0) {
							sql = "SELECT  " + map.getPhysicsTable() + "." + this.getEn().getPK() + " " + mySql
									+ " LIMIT " + pageSize;

						} else {
							sql = "SELECT  " + map.getPhysicsTable() + "." + this.getEn().getPK() + " " + mySql;
						}
					}

					sql = sql.replace("AND ( ( 1=1 ) )", " ");

					pks = this.GenerPKsByTableWithPara(pk, sql, top, toIdx);
					this.clear();
					this.setMyParas(this.getMyParasR());

					if (pks == null) {
						this.AddHD_Not();
					} else {
						this.AddWhereIn(pk, "(" + pks + ")");
					}

					this.setTop(pageSize);
					return this.doEntitiesQuery();
				case MSSQL:
				default:
					toIdx = top + pageSize;

					if (this._sql.equals("") || this._sql == null) {
						sql = " SELECT  [" + this.getEn().getPK() + "] FROM " + map.getPhysicsTable() + " "
								+ this._orderBy;
					} else {
						String mySql = this.getSQL();
						mySql = mySql.substring(mySql.indexOf("FROM"));

						sql = "SELECT " + map.getPhysicsTable() + "." + this.getEn().getPK() + " as  ["
								+ this.getEn().getPK() + "]  " + mySql;
					}

					sql = sql.replace("AND ( ( 1=1 ) )", " ");

					pks = this.GenerPKsByTableWithPara(pk, sql, top, toIdx);
					this.clear();
					this.setMyParas(this.getMyParasR());

					if (pks == null) {
						this.AddHD_Not();
					} else {
						this.AddWhereIn(pk, "(" + pks + ")");
					}

					this.setTop(pageSize);
					return this.doEntitiesQuery();
				}
			} else {
				return this.doEntityQuery();
			}
		} catch (RuntimeException ex) {
			try {
				if (this._en == null) {
					this.getEns().getNewEntity().CheckPhysicsTable();
				} else {
					this._en.CheckPhysicsTable();
				}
			} catch (java.lang.Exception e) {
			}
			throw ex;
		}
	}

	/**
	 * @return
	 * @throws Exception
	 */
	public final DataTable DoQueryToTable() throws Exception {
		try {
			String sql = this.getSQL();
			sql = sql.replace("WHERE (1=1) AND ( AND ( ( ( 1=1 ) ) AND ( ( 1=1 ) ) ) )", "");

			return DBAccess.RunSQLReturnTable(sql, this.getMyParas());

		} catch (RuntimeException ex) {
			if (this._en == null) {
				try {
					this.getEns().getNewEntity().CheckPhysicsTable();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				try {
					this._en.CheckPhysicsTable();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			throw ex;
		}
	}

	/**
	 * 得到返回的数量
	 * 
	 * @return 得到返回的数量
	 * @throws Exception
	 */
	public final int GetCount() throws Exception {
		String sql = this.getSQL();
		// sql="SELECT COUNT(*) "+sql.substing(sql.IndexOf("FROM") ) ;
		String ptable = this.getEn().getEnMap().getPhysicsTable();
		String pk = this.getEn().getPK();

		switch (this.getEn().getEnMap().getEnDBUrl().getDBType()) {
		case Oracle:
			if (this._sql.equals("") || this._sql == null) {
				sql = "SELECT COUNT(" + ptable + "." + pk + ") as C FROM " + ptable;
			} else {
				sql = "SELECT COUNT(" + ptable + "." + pk + ") as C " + sql.substring(sql.indexOf("FROM "));
			}
			break;
		default:
			if (this._sql.equals("") || this._sql == null) {
				sql = "SELECT COUNT(" + ptable + "." + pk + ") as C FROM " + ptable;
			} else {
				sql = sql.substring(sql.indexOf("FROM "));
				if (sql.indexOf("ORDER BY") >= 0)
					sql = sql.substring(0, sql.indexOf("ORDER BY") - 1);
				sql = "SELECT COUNT(" + ptable + "." + pk + ") as C " + sql;
			}

			break;
		}
		try {
			int i = DBAccess.RunSQLReturnValInt(sql, this.getMyParas());
			if (this.getTop() == -1) {
				return i;
			}

			if (this.getTop() >= i) {
				return i;
			} else {
				return this.getTop();
			}
		} catch (RuntimeException ex) {
			if (SystemConfig.getIsDebug()) {
				try {
					this.getEn().CheckPhysicsTable();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			throw ex;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	public DataTable DoGroupQueryToTable(String selectSQl, String groupBy, String orderBy) throws Exception {
		String sql = this.getSQL();
		String ptable = this.getEn().getEnMap().getPhysicsTable();

		switch (this.getEn().getEnMap().getEnDBUrl().getDBType()) {
		case Oracle:
			if (this._sql == "" || this._sql == null)
				sql = selectSQl + " FROM " + ptable + "WHERE " + groupBy + orderBy;
			else
				sql = selectSQl + sql.substring(sql.indexOf(" FROM ")) + groupBy + orderBy;
			break;
		default:
			if (this._sql == "" || this._sql == null)
				sql = selectSQl + " FROM " + ptable + "WHERE " + groupBy + orderBy;
			else {
				sql = sql.substring(sql.indexOf(" FROM "));
				if (sql.indexOf("ORDER BY") >= 0)
					sql = sql.substring(0, sql.indexOf("ORDER BY") - 1);
				sql = selectSQl + sql + groupBy + orderBy;
			}

			break;
		}
		return DBAccess.RunSQLReturnTable(sql, this.getMyParas());

	}

	/**
	 * 最大的数量
	 * 
	 * @param topNum
	 *            最大的数量
	 * @return 要查询的信息
	 * @throws Exception
	 */
	public final DataTable DoQueryToTable(int topNum) throws Exception {

		return DBAccess.RunSQLReturnTable(this.getSQL(), this.getMyParas());

	}

	private int doEntityQuery() throws Exception {
		return DBAccess.RunSQLReturnResultSet(this.getSQL(), this.getMyParas(), this.getEn(),
				this.getEn().getEnMap().getAttrs());
		// return EntityDBAccess.Retrieve(this.getEn(), , );
	}

	private int doEntitiesQuery() throws Exception {

		if (SystemConfig.getAppCenterDBType() == DBType.Oracle && this.getTop() != -1) {

			this.addAnd();
			this.AddWhereField("RowNum", "<=", this.getTop());
		}

		return DBAccess.RunSQLReturnResultSet(this.getSQL(), this.getMyParas(), this.getEns(),
				this.getEn().getEnMap().getAttrs());
	}

	/**
	 * 根据data初始化entiies.
	 * 
	 * @param ens
	 *            实体s
	 * @param dt
	 *            数据表
	 * @param fullAttrs
	 *            要填充的树形
	 * @return 初始化后的ens
	 * @throws Exception 
	 */
	public static Entities InitEntitiesByDataTable(Entities ens, DataTable dt, String[] fullAttrs) throws Exception {
		Boolean isUpper = false;
		if (SystemConfig.getAppCenterDBType() == DBType.Oracle)
			isUpper = true;

		if (fullAttrs == null) {
			Map enMap = ens.getNewEntity().getEnMap();
			Attrs attrs = enMap.getAttrs();
			try {

				for (DataRow dr : dt.Rows) {
					Entity en = ens.getNewEntity();
					for (Attr attr : attrs) {
						if (isUpper == true)
							en.SetValByKey(attr.getKey(), dr.getValue(attr.getKey().toUpperCase()));
						else
							en.SetValByKey(attr.getKey(), dr.getValue(attr.getKey()));
					}
					ens.AddEntity(en);
				}
			} catch (RuntimeException ex) {
				// warning 不应该出现的错误. 2011-12-03 add
				String cols = "";
				for (DataColumn dc : dt.Columns) {
					cols += " , " + dc.ColumnName;
				}
				throw new RuntimeException("Columns=" + cols + "@Ens=" + ens.toString() + " @异常信息:" + ex.getMessage());
			}

			return ens;
		}

		for (DataRow dr : dt.Rows) {
			Entity en = ens.getNewEntity();
			for (String str : fullAttrs) {
				if (isUpper == true)
					en.SetValByKey(str, dr.getValue(str.toUpperCase()));
				else
					en.SetValByKey(str, dr.getValue(str));

			}
			ens.AddEntity(en);
		}

		return ens;
	}

}