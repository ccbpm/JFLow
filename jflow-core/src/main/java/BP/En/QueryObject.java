package BP.En;

import BP.DA.*;
import BP.En.*;
import BP.Pub.*;
import BP.Sys.*;
import java.math.*;

/** 
 QueryObject 的摘要说明。
*/
public class QueryObject
{
	private Entity _en = null;
	private Entities _ens = null;
	private String _sql = "";
	private Entity getEn()
	{
		if (this._en == null)
		{
			return this.getEns().getGetNewEntity();
		}
		else
		{
			return this._en;
		}
	}
	private void setEn(Entity value)
	{
		this._en = value;
	}
	private Entities getEns()
	{
		return this._ens;
	}
	private void setEns(Entities value)
	{
		this._ens = value;
	}
	/** 
	 处理Order by , group by . 
	*/
	private String _groupBy = "";
	/** 
	 要得到的查询sql 。
	*/
	public final String getSQL()
	{
		String sql = "";
		String selecSQL = SqlBuilder.SelectSQL(this.getEn(), this.getTop());
		if (this._sql == null || this._sql.length() == 0)
		{
			sql = selecSQL + this._groupBy + this._orderBy;
		}
		else
		{
			if (selecSQL.contains(" WHERE "))
			{
				sql = selecSQL + "  AND ( " + this._sql + " ) " + _groupBy + this._orderBy;
			}
			else
			{
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
	public final void setSQL(String value)
	{
		this._sql = this._sql + " " + value;
	}
	public final String getSQLWithOutPara()
	{
		String sql = this.getSQL();
		for (Para en : this.getMyParas())
		{
			sql = sql.replace(SystemConfig.getAppCenterDBVarStr() + en.ParaName, "'" + en.val.toString() + "'");
		}
		return sql;
	}
	public final void AddWhere(String str)
	{
		this._sql = this._sql + " " + str;
	}
	/** 
	 修改于2009 -05-12 
	*/
	private int _Top = -1;
	public final int getTop()
	{
		return _Top;
	}
	public final void setTop(int value)
	{
		this._Top = value;
	}
	private Paras _Paras = null;
	public final Paras getMyParas()
	{
		if (_Paras == null)
		{
			_Paras = new Paras();
		}
		return _Paras;
	}
	public final void setMyParas(Paras value)
	{
		_Paras = value;
	}
	private Paras _ParasR = null;
	public final Paras getMyParasR()
	{
		if (_ParasR == null)
		{
			_ParasR = new Paras();
		}
		return _ParasR;
	}
	public final void AddPara(String key, Object v)
	{
		key = "P" + key;
		this.getMyParas().Add(key, v);
	}
	public QueryObject()
	{
	}
	/** DictBase
	*/
	public QueryObject(Entity en)
	{
		this.getMyParas().Clear();
		this._en = en;

		this.HisDBType = this._en.getEnMap().getEnDBUrl().getDBType();
		this.HisDBUrlType = this._en.getEnMap().getEnDBUrl().getDBUrlType();
	}
	public QueryObject(Entities ens)
	{
		this.getMyParas().Clear();
		ens.Clear();
		this._ens = ens;

		Entity en = this._ens.getGetNewEntity();

		this.HisDBType = en.getEnMap().getEnDBUrl().getDBType();
		this.HisDBUrlType = en.getEnMap().getEnDBUrl().getDBUrlType();
	}
	public BP.DA.DBType HisDBType = DBType.MSSQL;
	public BP.DA.DBUrlType HisDBUrlType = DBUrlType.AppCenterDSN;

	public final String getHisVarStr()
	{
		switch (this.HisDBType)
		{
			case MSSQL:
			case Access:
			case MySQL:
				return "@";
			case Informix:
				return "?";
			default:
				return ":";
		}
	}
	/** 
	 增加函数查寻．
	 
	 @param attr 属性
	 @param exp 表达格式 大于，等于，小于
	 @param len 长度
	*/
	public final void AddWhereLen(String attr, String exp, int len, BP.DA.DBType dbtype)
	{
		this.setSQL("( " + BP.Sys.SystemConfig.getAppCenterDBLengthStr() + "( " + attr2Field(attr) + " ) " + exp + " '" + String.valueOf(len) + "')");
	}
	/** 
	 增加查询条件，条件用 IN 表示．sql必须是一个列的集合．
	 
	 @param attr 属性
	 @param sql 此sql,必须是有一个列的集合．
	*/
	public final void AddWhereInSQL(String attr, String sql)
	{
		this.AddWhere(attr, " IN ", "( " + sql + " )");
	}
	/** 
	 增加查询条件，条件用 IN 表示．sql必须是一个列的集合．
	 
	 @param attr 属性
	 @param sql 此sql,必须是有一个列的集合．
	*/
	public final void AddWhereInSQL(String attr, String sql, String orderBy)
	{
		this.AddWhere(attr, " IN ", "( " + sql + " )");
		this.addOrderBy(orderBy);
	}
	/** 
	 增加查询条件，条件用 IN 表示．sql必须是一个列的集合．
	 
	 @param attr 属性
	 @param sql 此sql,必须是有一个列的集合．
	*/
	public final void AddWhereNotInSQL(String attr, String sql)
	{
		this.AddWhere(attr, " NOT IN ", " ( " + sql + " ) ");
	}
	public final void AddWhereNotIn(String attr, String val)
	{
		this.AddWhere(attr, " NOT IN ", " ( " + val + " ) ");
	}
	/** 
	 增加条件, DataTable 第一列的值．
	 
	 @param attr 属性
	 @param dt 第一列是要组合的values
	*/
	public final void AddWhereIn(String attr, DataTable dt)
	{
		String strs = "";
		for (DataRow dr : dt.Rows)
		{
			strs += dr.get(0).toString() + ",";
		}
		strs = tangible.StringHelper.substring(strs, strs.length() - 1, 0);
		this.AddWhereIn(attr, strs);
	}
	/** 
	 增加条件,vals 必须是sql可以识别的字串．
	 
	 @param attr 属性
	 @param vals 用 , 分开的．
	*/
	public final void AddWhereIn(String attr, String vals)
	{
		this.AddWhere(attr, " IN ", vals);
	}
	/** 
	 
	 
	 @param attr
	 @param exp
	 @param val
	*/
	public final void AddWhere(String attr, String exp, Object val)
	{
		AddWhere(attr, exp, val, null);
	}
	/** 
	 增加条件
	 
	 @param attr 属性
	 @param exp 操作符号（根据不同的数据库）
	 @param val 值
	 @param paraName 参数名称，可以为null, 如果查询中有多个参数中有相同属性名的需要，分别给他们起一个参数名。
	*/
	public final void AddWhere(String attr, String exp, Object val, String paraName)
	{
		if (val == null)
		{
			val = "";
		}

		String valStr = String.valueOf(val);

		if (valStr.equals("all"))
		{
			this.setSQL("( 1=1 )");
			return;
		}

		if (exp.toLowerCase().contains("in"))
		{
			this.setSQL("( " + attr2Field(attr) + " " + exp + "  " + valStr + " )");
			return;
		}

		if (exp.toLowerCase().contains("like"))
		{
			if (attr.equals("FK_Dept"))
			{
				valStr = valStr.replace("'", "");
				valStr = valStr.replace("%", "");

				switch (this.HisDBType)
				{
					case Oracle:
						this.setSQL("( " + attr2Field(attr) + " " + exp + " '%'||" + this.getHisVarStr() + "FK_Dept||'%' )");
						this.getMyParas().Add("FK_Dept", valStr);
						break;
					default:
						//this.SQL = "( " + attr2Field(attr) + " " + exp + "  '" + this.HisVarStr + "FK_Dept%' )";
						this.setSQL("( " + attr2Field(attr) + " " + exp + "  '" + valStr + "%' )");
						//this.MyParas.Add("FK_Dept", val);
						break;
				}
			}
			else
			{
				if (valStr.contains(":") || valStr.contains("@"))
				{
					this.setSQL("( " + attr2Field(attr) + " " + exp + "  " + valStr + " )");
				}
				else
				{
					if (valStr.contains("'") == false)
					{
						this.setSQL("( " + attr2Field(attr) + " " + exp + "  '" + valStr + "' )");
					}
					else
					{
						this.setSQL("( " + attr2Field(attr) + " " + exp + "  " + valStr + " )");
					}
				}
			}
			return;
		}
		if (this.getHisVarStr().equals("?"))
		{
			this.setSQL("( " + attr2Field(attr) + " " + exp + "?)");
			this.getMyParas().Add(attr, val);
		}
		else
		{
			if (paraName == null)
			{
				this.setSQL("( " + attr2Field(attr) + " " + exp + this.getHisVarStr() + attr + ")");
				this.getMyParas().Add(attr, val);
			}
			else
			{
				this.setSQL("( " + attr2Field(attr) + " " + exp + this.getHisVarStr() + paraName + ")");
				this.getMyParas().Add(paraName, val);
			}
		}
	}
	public final void AddWhereDept(String val)
	{
		String attr = "FK_Dept";
		String exp = "=";

		if (val.contains("'") == false)
		{
			this.setSQL("( " + attr2Field(attr) + " " + exp + "  '" + val + "' )");
		}
		else
		{
			this.setSQL("( " + attr2Field(attr) + " " + exp + "  " + val + " )");
		}
	}
	/** 
	 是空的
	 
	 @param attr
	*/
	public final void AddWhereIsNull(String attr)
	{
		this.setSQL("( " + attr2Field(attr) + "  IS NULL OR  " + attr2Field(attr) + "='' )");
	}
	public final void AddWhereField(String attr, String exp, String val)
	{
		if (val.toString().equals("all"))
		{
			this.setSQL("( 1=1 )");
			return;
		}

		if (exp.toLowerCase().contains("in"))
		{
			this.setSQL("( " + attr + " " + exp + "  " + val + " )");
			return;
		}

		this.setSQL("( " + attr + " " + exp + " :" + attr + " )");
		this.getMyParas().Add(attr, val);
	}
	public final void AddWhereField(String attr, String exp, int val)
	{
		if (String.valueOf(val).equals("all"))
		{
			this.setSQL("( 1=1 )");
			return;
		}

		if (exp.toLowerCase().contains("in"))
		{
			this.setSQL("( " + attr + " " + exp + "  " + val + " )");
			return;
		}

		if (attr.equals("RowNum"))
		{
			this.setSQL("( " + attr + " " + exp + "  " + val + " )");
			return;
		}

		if (this.getHisVarStr().equals("?"))
		{
			this.setSQL("( " + attr + " " + exp + "?)");
		}
		else
		{
			this.setSQL("( " + attr + " " + exp + "  " + this.getHisVarStr() + attr + " )");
		}

		this.getMyParas().Add(attr, val);
	}
	/** 
	 增加条件
	 
	 @param attr 属性
	 @param exp 操作符号（根据不同的数据库）
	 @param val 值
	*/
	public final void AddWhere(String attr, String exp, int val)
	{
		if (attr.equals("RowNum"))
		{
			this.setSQL("( " + attr2Field(attr) + " " + exp + " " + val + ")");
		}
		else
		{
			if (this.getHisVarStr().equals("?"))
			{
				this.setSQL("( " + attr2Field(attr) + " " + exp + "?)");
			}
			else
			{
				this.setSQL("( " + attr2Field(attr) + " " + exp + this.getHisVarStr() + attr + ")");
			}

			this.getMyParas().Add(attr, val);
		}
	}
	public final void AddHD()
	{
		this.setSQL("(  1=1 ) ");
	}
	/** 
	 非恒等。
	*/
	public final void AddHD_Not()
	{
		this.setSQL("(  1=2 ) ");
	}
	/** 
	 增加条件
	 
	 @param attr 属性
	 @param exp 操作符号（根据不同的数据库）
	 @param val 值
	*/
	public final void AddWhere(String attr, String exp, float val)
	{
		this.getMyParas().Add(attr, val);
		if (this.getHisVarStr().equals("?"))
		{
			this.setSQL("( " + attr2Field(attr) + " " + exp + "?)");
		}
		else
		{
			this.setSQL("( " + attr2Field(attr) + " " + exp + " " + this.getHisVarStr() + attr + ")");
		}
	}
	/** 
	 增加条件(默认的是= )
	 
	 @param attr 属性
	 @param val 值
	*/
	public final void AddWhere(String attr, String val)
	{
		this.AddWhere(attr, "=", val);
	}
	/** 
	 增加条件(默认的是= )
	 
	 @param attr 属性
	 @param val 值
	*/
	public final void AddWhere(String attr, int val)
	{
		this.AddWhere(attr, "=", val, null);
	}
	/** 
	 增加条件
	 
	 @param attr 属性
	 @param val 值 true/false
	*/
	public final void AddWhere(String attr, boolean val)
	{
		if (val)
		{
			this.AddWhere(attr, "=", 1);
		}
		else
		{
			this.AddWhere(attr, "=", 0);
		}
	}
	public final void AddWhere(String attr, long val)
	{
		this.AddWhere(attr, "=", val, null);
	}
	public final void AddWhere(String attr, float val)
	{
		this.AddWhere(attr, "=", val);
	}
	public final void AddWhere(String attr, Object val)
	{
		if (val == null)
		{
			throw new RuntimeException("Attr=" + attr + ", 值是空 is null");
		}

		if (val.getClass() == Integer.class || val.getClass() == Long.class)
		{
			this.AddWhere(attr, "=", val);
			return;
		}
		this.AddWhere(attr, "=", String.valueOf(val));
	}

	public final void addLeftBracket()
	{
		this.setSQL(" ( ");
	}

	public final void addRightBracket()
	{
		this.setSQL(" ) ");
	}

	public final void addAnd()
	{
		this.setSQL(" AND ");
	}

	public final void addOr()
	{
		this.setSQL(" OR ");
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 关于endsql
	public final void addGroupBy(String attr)
	{
		this._groupBy = " GROUP BY  " + attr2Field(attr);
	}

	public final void addGroupBy(String attr1, String attr2)
	{
		this._groupBy = " GROUP BY  " + attr2Field(attr1) + " , " + attr2Field(attr2);
	}

	private String _orderBy = "";
	public final void addOrderBy(String attr)
	{
		if (this._orderBy.indexOf("ORDER BY") != -1)
		{
			this._orderBy += " , " + attr2Field(attr);
		}
		else
		{
			this._orderBy = " ORDER BY " + attr2Field(attr);
		}
	}

	/** 
	 
	 
	 @param attr
	*/
	public final void addOrderByRandom()
	{
		if (this._orderBy.indexOf("ORDER BY") != -1)
		{
			this._orderBy = " , NEWID()";
		}
		else
		{
			this._orderBy = " ORDER BY NEWID()";
		}
	}
	/** 
	 addOrderByDesc
	 
	 @param attr
	 @param desc
	*/
	public final void addOrderByDesc(String attr)
	{
		if (this._orderBy.indexOf("ORDER BY") != -1)
		{
			this._orderBy += " , " + attr2Field(attr) + " DESC ";
		}
		else
		{
			this._orderBy = " ORDER BY " + attr2Field(attr) + " DESC ";
		}

	}
	public final void addOrderByDesc(String attr1, String attr2)
	{
		this._orderBy = " ORDER BY  " + attr2Field(attr1) + " DESC ," + attr2Field(attr2) + " DESC";
	}
	public final void addOrderBy(String attr1, String attr2)
	{
		this._orderBy = " ORDER BY  " + attr2Field(attr1) + "," + attr2Field(attr2);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

	public final void addHaving()
	{
	}
	/** 清除查询条件
	*/
	public final void clear()
	{
		this._sql = "";
		this._groupBy = "";
		//this._orderBy = "";
		this.getMyParas().Clear();
	}
	private Map _HisMap;
	public final Map getHisMap()
	{
		if (_HisMap == null)
		{
			_HisMap = this.getEn().getEnMap();
		}
		return _HisMap;
	}
	public final void setHisMap(Map value)
	{
		_HisMap = value;
	}
	/** 
	 增加字段.
	 
	 @param attr
	 @return 
	*/
	private String attr2Field(String attrKey)
	{
		Attr attr = this.getHisMap().GetAttrByKey(attrKey);
		if (attr.getIsRefAttr() == true)
		{
			//  Entity en = attr.HisFKEn;
			if (this.HisDBType == DBType.Oracle)
			{
				return "T" + attr.getKey().replace("Text", "") + ".Name";
			}
			else
			{
				Entity en = attr.getHisFKEn();
				return en.getEnMap().getPhysicsTable() + "_" + attr.getKey().replace("Text", "") + ".Name";
			}

		}

		return this.getHisMap().getPhysicsTable() + "." + attr.getField();
		// return this.HisMap.PhysicsTable + "."+attr;
	}
	public final DataTable DoGroupReturnTable(Entity en, Attrs attrsOfGroupKey, Attr attrGroup, GroupWay gw, OrderWay ow)
	{
		switch (en.getEnMap().getEnDBUrl().getDBType())
		{
			case Oracle:
				return DoGroupReturnTableOracle(en, attrsOfGroupKey, attrGroup, gw, ow);
			default:
				return DoGroupReturnTableSqlServer(en, attrsOfGroupKey, attrGroup, gw, ow);
		}
	}
	public final DataTable DoGroupReturnTableOracle(Entity en, Attrs attrsOfGroupKey, Attr attrGroup, GroupWay gw, OrderWay ow)
	{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region  生成要查询的语句
		String fields = "";
		String str = "";
		for (Attr attr : attrsOfGroupKey)
		{
			if (attr.getField() == null)
			{
				continue;
			}

			str = "," + attr.getField();
			fields += str;
		}

		if (attrGroup.getKey().equals("MyNum"))
		{
			switch (gw)
			{
				case BySum:
					fields += ", COUNT(*) AS MyNum";
					break;
				case ByAvg:
					fields += ", AVG(" + attrGroup.getField() + ") AS MyNum";
					break;
				default:
					throw new RuntimeException("no such case:");
			}
		}
		else
		{
			switch (gw)
			{
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
		for (Attr attr : attrsOfGroupKey)
		{
			if (attr.getField() == null)
			{
				continue;
			}

			str = "," + attr.getField();
			by += str;
		}
		by = by.substring(1);
		//string sql 
		String sql = "SELECT " + fields.substring(1) + " FROM " + this.getEn().getEnMap().getPhysicsTable() + " WHERE " + this._sql + " Group BY " + by;
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region
		Map map = new Map();
		map.setPhysicsTable("@VT@");
		map.setAttrs(attrsOfGroupKey);
		map.getAttrs().Add(attrGroup);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion .

		String sql1 = SqlBuilder.SelectSQLOfOra(en.toString(), map) + " " + SqlBuilder.GenerFormWhereOfOra(en, map);

		sql1 = sql1.replace("@TopNum", "");
		sql1 = sql1.replace("FROM @VT@", "FROM (" + sql + ") VT");
		sql1 = sql1.replace("@VT@", "VT");
		sql1 = sql1.replace("TOP", "");

		if (ow == OrderWay.OrderByUp)
		{
			sql1 += " ORDER BY " + attrGroup.getKey() + " DESC ";
		}
		else
		{
			sql1 += " ORDER BY " + attrGroup.getKey();
		}

		return DBAccess.RunSQLReturnTable(sql1, this.getMyParas());
	}

	public final DataTable DoGroupReturnTableSqlServer(Entity en, Attrs attrsOfGroupKey, Attr attrGroup, GroupWay gw, OrderWay ow)
	{

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region  生成要查询的语句
		String fields = "";
		String str = "";
		for (Attr attr : attrsOfGroupKey)
		{
			if (attr.getField() == null)
			{
				continue;
			}
			str = "," + attr.getField();
			fields += str;
		}

		if (attrGroup.getKey().equals("MyNum"))
		{
			switch (gw)
			{
				case BySum:
					fields += ", COUNT(*) AS MyNum";
					break;
				case ByAvg:
					fields += ", AVG(*)   AS MyNum";
					break;
				default:
					throw new RuntimeException("no such case:");
			}
		}
		else
		{
			switch (gw)
			{
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
		for (Attr attr : attrsOfGroupKey)
		{
			if (attr.getField() == null)
			{
				continue;
			}

			str = "," + attr.getField();
			by += str;
		}
		by = by.substring(1);
		//string sql 
		String sql = "SELECT " + fields.substring(1) + " FROM " + this.getEn().getEnMap().getPhysicsTable() + " WHERE " + this._sql + " Group BY " + by;
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region
		Map map = new Map();
		map.setPhysicsTable("@VT@");
		map.setAttrs(attrsOfGroupKey);
		map.getAttrs().Add(attrGroup);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion .
		//string sql1=SqlBuilder.SelectSQLOfMS( map )+" "+SqlBuilder.GenerFormWhereOfMS( en,map) + "   AND ( " + this._sql+" ) "+_endSql;

		String sql1 = SqlBuilder.SelectSQLOfMS(map) + " " + SqlBuilder.GenerFormWhereOfMS(en, map);

		sql1 = sql1.replace("@TopNum", "");

		sql1 = sql1.replace("FROM @VT@", "FROM (" + sql + ") VT");

		sql1 = sql1.replace("@VT@", "VT");
		sql1 = sql1.replace("TOP", "");
		if (ow == OrderWay.OrderByUp)
		{
			sql1 += " ORDER BY " + attrGroup.getKey() + " DESC ";
		}
		else
		{
			sql1 += " ORDER BY " + attrGroup.getKey();
		}
		return DBAccess.RunSQLReturnTable(sql1, this.getMyParas());
	}
	/** 
	 分组查询，返回datatable.
	 
	 @param attrsOfGroupKey
	 @param groupValField
	 @param gw
	 @return 
	*/
	public final DataTable DoGroupReturnTable1(Entity en, Attrs attrsOfGroupKey, Attr attrGroup, GroupWay gw, OrderWay ow)
	{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region  生成要查询的语句
		String fields = "";
		String str = "";
		for (Attr attr : attrsOfGroupKey)
		{
			if (attr.getField() == null)
			{
				continue;
			}
			str = "," + attr.getField();
			fields += str;
		}

		if (attrGroup.getKey().equals("MyNum"))
		{
			switch (gw)
			{
				case BySum:
					fields += ", COUNT(*) AS MyNum";
					break;
				case ByAvg:
					fields += ", AVG(*)   AS MyNum";
					break;
				default:
					throw new RuntimeException("no such case:");
			}
		}
		else
		{
			switch (gw)
			{
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
		for (Attr attr : attrsOfGroupKey)
		{
			if (attr.getField() == null)
			{
				continue;
			}

			str = "," + attr.getField();
			by += str;
		}
		by = by.substring(1);
		//string sql 
		String sql = "SELECT " + fields.substring(1) + " FROM " + this.getEn().getEnMap().getPhysicsTable() + " WHERE " + this._sql + " Group BY " + by;
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region
		Map map = new Map();
		map.setPhysicsTable("@VT@");
		map.setAttrs(attrsOfGroupKey);
		map.getAttrs().Add(attrGroup);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion .

		//string sql1=SqlBuilder.SelectSQLOfMS( map )+" "+SqlBuilder.GenerFormWhereOfMS( en,map) + "   AND ( " + this._sql+" ) "+_endSql;

		String sql1 = SqlBuilder.SelectSQLOfMS(map) + " " + SqlBuilder.GenerFormWhereOfMS(en, map);

		sql1 = sql1.replace("@TopNum", "");
		sql1 = sql1.replace("FROM @VT@", "FROM (" + sql + ") VT");
		sql1 = sql1.replace("@VT@", "VT");
		sql1 = sql1.replace("TOP", "");
		if (ow == OrderWay.OrderByUp)
		{
			sql1 += " ORDER BY " + attrGroup.getKey() + " DESC ";
		}
		else
		{
			sql1 += " ORDER BY " + attrGroup.getKey();
		}
		return DBAccess.RunSQLReturnTable(sql1);
	}
	public String[] FullAttrs = null;
	/** 
	 执行查询
	 
	 @return 
	*/
	public final int DoQuery()
	{
		try
		{
			if (this._en == null)
			{
				return this.doEntitiesQuery();
			}
			else
			{
				return this.doEntityQuery();
			}
		}
		catch (RuntimeException ex)
		{
			if (this._en == null)
			{
				this._ens.getGetNewEntity().CheckPhysicsTable();
			}
			else
			{
				this._en.CheckPhysicsTable();
			}
			throw ex;
		}
	}
	public final int DoQueryBak20111203()
	{
		try
		{
			if (this._en == null)
			{
				return this.doEntitiesQuery();
			}
			else
			{
				return this.doEntityQuery();
			}
		}
		catch (RuntimeException ex)
		{
			try
			{
				if (this._en == null)
				{
					this.getEns().getGetNewEntity().CheckPhysicsTable();
				}
				else
				{
					this._en.CheckPhysicsTable();
				}
			}
			catch (java.lang.Exception e)
			{
			}
			throw ex;
		}
	}
	public final String DealString(String sql)
	{
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		String strs = "";
		for (DataRow dr : dt.Rows)
		{
			strs += ",'" + dr.get(0).toString() + "'";
		}
		return strs.substring(1);
	}
	public final String GenerPKsByTableWithPara(String pk, String sql, int from, int to)
	{
		//Log.DefaultLogWriteLineWarning(" ***************************** From= " + from + "  T0" + to);
		DataTable dt = DBAccess.RunSQLReturnTable(sql, this.getMyParas());
		String pks = "";
		int i = 0;
		int paraI = 0;

		String dbStr = SystemConfig.getAppCenterDBVarStr();
		for (DataRow dr : dt.Rows)
		{
			i++;
			if (i > from)
			{
				paraI++;
				//pks += "'" + dr[0].ToString() + "'";
				if (dbStr.equals("?"))
				{
					pks += "?,";
				}
				else
				{
					pks += SystemConfig.getAppCenterDBVarStr() + "R" + paraI + ",";
				}

				if (pk.equals("OID") || pk.equals("WorkID") || pk.equals("NodeID"))
				{
				this.getMyParasR().Add("R" + paraI, Integer.parseInt(dr.get(0).toString()));
				}
				else
				{
					this.getMyParasR().Add("R" + paraI, dr.get(0).toString());
				}


				if (i >= to)
				{
					return pks.substring(0, pks.length() - 1);
				}
			}
		}
		if (pks.equals(""))
		{
			return null;
			//return " '1'  ";
			return "  ";
		}
		return pks.substring(0, pks.length() - 1);
	}
	public final String GenerPKsByTable(String sql, int from, int to)
	{
		//Log.DefaultLogWriteLineWarning(" ***************************** From= " + from + "  T0" + to);
		DataTable dt = DBAccess.RunSQLReturnTable(sql, this.getMyParas());
		String pks = "";
		int i = 0;
		for (DataRow dr : dt.Rows)
		{
			i++;
			if (i > from)
			{
				if (i >= to)
				{
					pks += "'" + dr.get(0).toString() + "'";
					return pks;
				}
				else
				{
					pks += "'" + dr.get(0).toString() + "',";
				}
			}
		}
		if (pks.equals(""))
		{
			return "  '11111111' ";
		}
		return pks.substring(0, pks.length() - 1);
	}
	/** 
	 删除当前查询的排序字段，然后可以再次增加其他的排序字段
	 <p>added by liuxc,2015.3.18,为解决默认增加的是主键字段排序，但此排序字段未提供删除方法的问题</p>
	*/
	public final void ClearOrderBy()
	{
		this._orderBy = "";
	}
	/** 
	 
	 
	 @param pk
	 @param pageSize
	 @param pageIdx
	 @return 
	*/
	public final int DoQuery(String pk, int pageSize, int pageIdx)
	{
		if (pk.equals("OID") || pk.equals("WorkID"))
		{
			return DoQuery(pk, pageSize, pageIdx, pk, true);
		}
		else
		{
			return DoQuery(pk, pageSize, pageIdx, pk, false);
		}
	}
	/** 
	 分页查询方法
	 
	 @param pk 主键
	 @param pageSize 页面大小
	 @param pageIdx 第x页
	 @param orderby 排序
	 @param orderway 排序方式: 两种情况 Down UP 
	 @return 查询结果
	*/
	public final int DoQuery(String pk, int pageSize, int pageIdx, String orderBy, String orderWay)
	{
		if (orderWay.toLowerCase().trim().equals("up") || orderWay.toLowerCase().trim().equals("asc"))
		{
			return DoQuery(pk, pageSize, pageIdx, orderBy, false);
		}
		else
		{
			return DoQuery(pk, pageSize, pageIdx, orderBy, true);
		}
	}
	/** 
	 分页查询方法
	 
	 @param pk 主键
	 @param pageSize 页面大小
	 @param pageIdx 第x页
	 @param orderby 排序
	 @return 查询结果
	*/
	public final int DoQuery(String pk, int pageSize, int pageIdx, boolean isDesc)
	{
		return DoQuery(pk, pageSize, pageIdx, pk, isDesc);
	}
	/** 
	 分页查询方法
	 
	 @param pk 主键
	 @param pageSize 页面大小
	 @param pageIdx 第x页
	 @param orderby 排序
	 @param orderway 排序方式: 两种情况 desc 或者 为 null. 
	 @return 查询结果
	*/
	public final int DoQuery(String pk, int pageSize, int pageIdx, String orderBy, boolean isDesc)
	{
		int pageNum = 0;

		//如果没有加入排序字段，使用主键
		if (DataType.IsNullOrEmpty(this._orderBy))
		{
			String isDescStr = "";
			if (isDesc)
			{
				isDescStr = " DESC ";
			}

			if (DataType.IsNullOrEmpty(orderBy))
			{
				orderBy = pk;
			}

			this._orderBy = attr2Field(orderBy) + isDescStr;
		}

		if (this._orderBy.contains("ORDER BY") == false)
		{
			_orderBy = " ORDER BY " + this._orderBy;
		}

		try
		{
			if (this._en == null)
			{
				int recordConut = 0;
				recordConut = this.GetCount(); // 获取 它的数量。

				if (recordConut == 0)
				{
					this._ens.Clear();
					return 0;
				}

				// xx!5555 提出的错误.
				if (pageSize == 0)
				{
					pageSize = 12;
				}

				BigDecimal pageCountD = BigDecimal.Parse(String.valueOf(recordConut)).divide(BigDecimal.Parse(String.valueOf(pageSize))); // 页面个数。
				String[] strs = pageCountD.toString("0.0000").split("[.]", -1);
				if (Integer.parseInt(strs[1]) > 0)
				{
					pageNum = Integer.parseInt(strs[0]) + 1;
				}
				else
				{
					pageNum = Integer.parseInt(strs[0]);
				}

				int myleftCount = recordConut - (pageNum * pageSize);

				pageNum++;
				int top = pageSize * (pageIdx - 1);

				String sql = "";
				Entity en = this._ens.getGetNewEntity();
				Map map = en.getEnMap();
				int toIdx = 0;
				String pks = "";
				switch (map.getEnDBUrl().getDBType())
				{
					case Oracle:
						toIdx = top + pageSize;
						if (this._sql.equals("") || this._sql == null)
						{
							if (top == 0)
							{
								sql = "SELECT * FROM ( SELECT  " + pk + " FROM " + map.getPhysicsTable() + " " + this._orderBy + "   ) WHERE ROWNUM <=" + pageSize;
							}
							else
							{
								sql = "SELECT * FROM ( SELECT  " + pk + " FROM " + map.getPhysicsTable() + " " + this._orderBy + ") ";
							}
						}
						else
						{
							String mysql = this.getSQL();
							mysql = mysql.substring(mysql.indexOf("FROM "));

							if (top == 0)
							{
								sql = "SELECT * FROM ( SELECT " + map.getPhysicsTable() + "." + pk + " " + mysql + " )  WHERE ROWNUM <=" + pageSize;
							}
							else
							{
								sql = "SELECT * FROM ( SELECT " + map.getPhysicsTable() + "." + pk + " " + mysql + " ) ";
							}
							//sql = "SELECT * FROM ( SELECT  " + pk + " FROM " + map.PhysicsTable + " WHERE " + this._sql + " " + this._orderBy + "   ) ";
						}

						sql = sql.replace("AND ( ( 1=1 ) )", " ");

						pks = this.GenerPKsByTableWithPara(pk, sql, top, toIdx);
						this.clear();
						this.setMyParas(this.getMyParasR());
						if (pks != null)
						{
							this.AddWhereIn(pk, "(" + pks + ")");
						}
						else
						{
							this.AddHD();
						}

						this.setTop(pageSize);
						return this.doEntitiesQuery();
					case Informix:
						toIdx = top + pageSize;
						if (this._sql.equals("") || this._sql == null)
						{
							if (top == 0)
							{
								sql = " SELECT first " + pageSize + "  " + this.getEn().getPKField() + " FROM " + map.getPhysicsTable() + " " + this._orderBy;
							}
							else
							{
								sql = " SELECT  " + this.getEn().getPKField() + " FROM " + map.getPhysicsTable() + " " + this._orderBy;
							}
						}
						else
						{
							String mysql = this.getSQL();
							mysql = mysql.substring(mysql.indexOf("FROM "));
							if (top == 0)
							{
								sql = "SELECT first " + pageSize + " " + this.getEn().getPKField() + "  " + mysql;
							}
							else
							{
								sql = "SELECT  " + this.getEn().getPKField() + " " + mysql;
							}
						}

						sql = sql.replace("AND ( ( 1=1 ) )", " ");

						pks = this.GenerPKsByTableWithPara(pk, sql, top, toIdx);
						this.clear();
						this.setMyParas(this.getMyParasR());

						if (pks == null)
						{
							this.AddHD_Not();
						}
						else
						{
							this.AddWhereIn(pk, "(" + pks + ")");
						}

						this.setTop(pageSize);
						return this.doEntitiesQuery();
					case MySQL:
						toIdx = top + pageSize;
						if (this._sql.equals("") || this._sql == null)
						{
							if (top == 0)
							{
								sql = " SELECT  " + this.getEn().getPKField() + " FROM " + map.getPhysicsTable() + " " + this._orderBy + " LIMIT " + pageSize;
							}
							else
							{
								sql = " SELECT  " + this.getEn().getPKField() + " FROM " + map.getPhysicsTable() + " " + this._orderBy;
							}
						}
						else
						{
							String mysql = this.getSQL();
							mysql = mysql.substring(mysql.indexOf("FROM "));

							if (top == 0)
							{
								sql = "SELECT " + map.getPhysicsTable() + "." + this.getEn().getPKField() + " " + mysql + " LIMIT " + pageSize;
							}
							else
							{
								sql = "SELECT " + map.getPhysicsTable() + "." + this.getEn().getPKField() + " " + mysql;
							}
						}

						sql = sql.replace("AND ( ( 1=1 ) )", " ");

						pks = this.GenerPKsByTableWithPara(pk, sql, top, toIdx);
						this.clear();
						this.setMyParas(this.getMyParasR());

						if (pks == null)
						{
							this.AddHD_Not();
						}
						else
						{
							this.AddWhereIn(pk, "(" + pks + ")");
						}

						this.setTop(pageSize);
						return this.doEntitiesQuery();
					case PostgreSQL:
						toIdx = top + pageSize;
						if (this._sql.equals("") || this._sql == null)
						{
							if (top == 0)
							{
								sql = " SELECT  " + this.getEn().getPKField() + " FROM " + map.getPhysicsTable() + " " + this._orderBy + " LIMIT " + pageSize;
							}
							else
							{
								sql = " SELECT  " + this.getEn().getPKField() + " FROM " + map.getPhysicsTable() + " " + this._orderBy;
							}
						}
						else
						{
							String mysql = this.getSQL();
							mysql = mysql.substring(mysql.indexOf("FROM "));

							if (top == 0)
							{
								sql = "SELECT " + map.getPhysicsTable() + "." + this.getEn().getPKField() + " " + mysql + " LIMIT " + pageSize;
							}
							else
							{
								sql = "SELECT " + map.getPhysicsTable() + "." + this.getEn().getPKField() + " " + mysql;
							}
						}

						sql = sql.replace("AND ( ( 1=1 ) )", " ");

						pks = this.GenerPKsByTableWithPara(pk, sql, top, toIdx);
						this.clear();
						this.setMyParas(this.getMyParasR());

						if (pks == null)
						{
							this.AddHD_Not();
						}
						else
						{
							this.AddWhereIn(pk, "(" + pks + ")");
						}

						this.setTop(pageSize);
						return this.doEntitiesQuery();
					case MSSQL:
					default:
						toIdx = top + pageSize;
						if (this._sql.equals("") || this._sql == null)
						{
							//此处去掉原有的第1页时用top pagesize的写法，会导致第1页数据查询出来的不准确，统一都用下面的写法，edited by liuxc,2017-8-30
							//此处查询数据，除第1页外，有可能会造排序不正确，但每一页的数据是准确的，限于原有写法，没法改动此处逻辑解决这个问题
							sql = " SELECT  [" + this.getEn().getPKField() + "] FROM " + map.getPhysicsTable() + " " + this._orderBy;
						}
						else
						{
							String mysql = this.getSQL();
							mysql = mysql.substring(mysql.indexOf("FROM "));
							sql = "SELECT " + map.getPhysicsTable() + "." + this.getEn().getPKField() + " as  [" + this.getEn().getPKField() + "]  " + mysql;
						}

						sql = sql.replace("AND ( ( 1=1 ) )", " ");

						pks = this.GenerPKsByTableWithPara(pk, sql, top, toIdx);
						this.clear();
						this.setMyParas(this.getMyParasR());

						if (pks == null)
						{
							this.AddHD_Not();
						}
						else
						{
							this.AddWhereIn(pk, "(" + pks + ")");
						}

						this.setTop(pageSize);
						return this.doEntitiesQuery();
				}
			}
			else
			{
				return this.doEntityQuery();
			}
		}
		catch (RuntimeException ex)
		{
			try
			{
				if (this._en == null)
				{
					this.getEns().getGetNewEntity().CheckPhysicsTable();
				}
				else
				{
					this._en.CheckPhysicsTable();
				}
			}
			catch (java.lang.Exception e)
			{
			}
			throw ex;
		}
	}
	/** 
	 按照
	 
	 @return 
	*/
	public final DataTable DoQueryToTable()
	{
		try
		{
			String sql = this.getSQL();
			sql = sql.replace("WHERE (1=1) AND ( AND ( ( ( 1=1 ) ) AND ( ( 1=1 ) ) ) )", "");

			return DBAccess.RunSQLReturnTable(sql, this.getMyParas());
		}
		catch (RuntimeException ex)
		{
			if (this._en == null)
			{
				this.getEns().getGetNewEntity().CheckPhysicsTable();
			}
			else
			{
				this._en.CheckPhysicsTable();
			}
			throw ex;
		}
	}
	/** 
	 得到返回的数量
	 
	 @return 得到返回的数量
	*/
	public final int GetCount()
	{
		String sql = this.getSQL();
		//sql="SELECT COUNT(*) "+sql.Substring(sql.IndexOf("FROM") ) ;
		String ptable = this.getEn().getEnMap().getPhysicsTable();
		String pk = this.getEn().getPKField();

		switch (this.getEn().getEnMap().getEnDBUrl().getDBType())
		{
			case Oracle:
				if (this._sql.equals("") || this._sql == null)
				{
					sql = "SELECT COUNT(" + ptable + "." + pk + ") as C FROM " + ptable;
				}
				else
				{
					sql = "SELECT COUNT(" + ptable + "." + pk + ") as C " + sql.substring(sql.indexOf("FROM "));
				}
				break;
			default:
				if (this._sql.equals("") || this._sql == null)
				{
					sql = "SELECT COUNT(" + ptable + "." + pk + ") as C FROM " + ptable;
				}
				else
				{
					sql = sql.substring(sql.indexOf("FROM "));
					if (sql.indexOf("ORDER BY") >= 0)
					{
						sql = sql.substring(0, sql.indexOf("ORDER BY") - 1);
					}
					sql = "SELECT COUNT(" + ptable + "." + pk + ") as C " + sql;
				}
				//sql="SELECT COUNT(*) as C "+this._endSql  +sql.Substring(  sql.IndexOf("FROM ") ) ;
				//sql="SELECT COUNT(*) as C FROM "+ this._ens.GetNewEntity.EnMap.PhysicsTable+ "  " +sql.Substring(sql.IndexOf("WHERE") ) ;
				//int i = sql.IndexOf("ORDER BY") ;
				//if (i!=-1)
				//	sql=sql.Substring(0,i);
				break;
		}
		try
		{
			int i = DBAccess.RunSQLReturnValInt(sql, this.getMyParas());
			if (this.getTop() == -1)
			{
				return i;
			}

			if (this.getTop() >= i)
			{
				return i;
			}
			else
			{
				return this.getTop();
			}
		}
		catch (RuntimeException ex)
		{
			//   if (SystemConfig.IsDebug)
			this.getEn().CheckPhysicsTable();
			throw ex;
		}
	}

	public final DataTable DoGroupQueryToTable(String selectSQl, String groupBy, String orderBy)
	{
		String sql = this.getSQL();
		String ptable = this.getEn().getEnMap().getPhysicsTable();
		String pk = this.getEn().getPKField();

		switch (this.getEn().getEnMap().getEnDBUrl().getDBType())
		{
			case Oracle:
				if (this._sql.equals("") || this._sql == null)
				{
					sql = selectSQl + " FROM " + ptable + "WHERE " + groupBy + orderBy;
				}
				else
				{
					sql = selectSQl + sql.substring(sql.indexOf(" FROM ")) + groupBy + orderBy;
				}
				break;
			default:
				if (this._sql.equals("") || this._sql == null)
				{
					sql = selectSQl + " FROM " + ptable + "WHERE " + groupBy + orderBy;
				}
				else
				{
					sql = sql.substring(sql.indexOf(" FROM "));
					if (sql.indexOf("ORDER BY") >= 0)
					{
						sql = sql.substring(0, sql.indexOf("ORDER BY") - 1);
					}
					sql = selectSQl + sql + groupBy + orderBy;
				}


				//sql="SELECT COUNT(*) as C "+this._endSql  +sql.Substring(  sql.IndexOf("FROM ") ) ;
				//sql="SELECT COUNT(*) as C FROM "+ this._ens.GetNewEntity.EnMap.PhysicsTable+ "  " +sql.Substring(sql.IndexOf("WHERE") ) ;
				//int i = sql.IndexOf("ORDER BY") ;
				//if (i!=-1)
				//	sql=sql.Substring(0,i);
				break;
		}
		return DBAccess.RunSQLReturnTable(sql, this.getMyParas());

	}
	/** 
	 最大的数量
	 
	 @param topNum 最大的数量
	 @return 要查询的信息
	*/
	public final DataTable DoQueryToTable(int topNum)
	{
		this.setTop(topNum);
		return DBAccess.RunSQLReturnTable(this.getSQL(), this.getMyParas());
	}

	private int doEntityQuery()
	{
		return EntityDBAccess.Retrieve(this.getEn(), this.getSQL(), this.getMyParas());
	}
	private int doEntitiesQuery()
	{
		switch (this.HisDBType)
		{
			case Oracle:
				if (this.getTop() != -1)
				{
					this.addAnd();
					this.AddWhereField("RowNum", "<=", this.getTop());
				}
				break;
			case MSSQL:
			case MySQL:
			default:
				break;
		}
		return EntityDBAccess.Retrieve(this.getEns(), this.getSQL(), this.getMyParas(), this.FullAttrs);
	}
	/** 
	 根据data初始化entiies.
	 
	 @param ens 实体s
	 @param dt 数据表
	 @param fullAttrs 要填充的树形
	 @return 初始化后的ens
	*/
	public static Entities InitEntitiesByDataTable(Entities ens, DataTable dt, String[] fullAttrs)
	{
		if (fullAttrs == null)
		{
			Map enMap = ens.getGetNewEntity().getEnMap();
			Attrs attrs = enMap.getAttrs();
			try
			{
				for (DataRow dr : dt.Rows)
				{
					Entity en = ens.getGetNewEntity();
					for (Attr attr : attrs)
					{
						//if (attr.IsRefAttr)
						//    continue;
						if (dt.Columns.Contains(attr.getKey()) == false)
						{
							continue;
						}
						en.getRow().SetValByKey(attr.getKey(), dr.get(attr.getKey()));
					}
					ens.AddEntity(en);
				}
			}
			catch (RuntimeException ex)
			{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
///#warning 不应该出现的错误. 2011-12-03 add
				String cols = "";
				for (DataColumn dc : dt.Columns)
				{
					cols += " , " + dc.ColumnName;
				}
				throw new RuntimeException("Columns=" + cols + "@Ens=" + ens.toString() + " @异常信息:" + ex.getMessage());
			}
		}
		else
		{

			for (DataRow dr : dt.Rows)
			{
				Entity en = ens.getGetNewEntity();
				for (String str : fullAttrs)
				{
					en.getRow().SetValByKey(str, dr.get(str));
				}
				ens.AddEntity(en);
			}
		}
		return ens;
	}
}