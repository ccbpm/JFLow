package BP.En;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import BP.DA.AtPara;
import BP.DA.CashEntity;
import BP.DA.DBAccess;
import BP.DA.DBType;
import BP.DA.DBUrlType;
import BP.DA.DataColumn;
import BP.DA.DataRow;
import BP.DA.DataTable;
import BP.DA.DataType;
import BP.DA.Depositary;
import BP.DA.Log;
import BP.DA.LogType;
import BP.DA.Paras;
import BP.Sys.EnVer;
import BP.Sys.EnVerDtl;
import BP.Sys.MapAttr;
import BP.Sys.MapData;
import BP.Sys.MapDtl;
import BP.Sys.SysEnum;
import BP.Sys.SysEnumAttr;
import BP.Sys.SysEnums;
import BP.Sys.SystemConfig;
import BP.Tools.StringHelper;
import BP.WF.DotNetToJavaStringHelper;
import BP.Web.WebUser;
import BP.XML.XmlEn;

/**
 * Entity 的摘要说明。
 */
public abstract class Entity extends EnObj {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 与缓存有关的操作
	private Entities _GetNewEntities = null;

	public Entities getGetNewEntities() {
		if (_GetNewEntities == null) {
			String str = this.toString();
			java.util.ArrayList al = ClassFactory.GetObjects("BP.En.Entities");
			for (Object o : al) {
				Entities ens = (Entities) ((o instanceof Entities) ? o : null);

				if (ens == null) {
					continue;
				}
				if (ens.getGetNewEntity().toString() != null && ens.getGetNewEntity().toString().equals(str)) {
					_GetNewEntities = ens;
					return _GetNewEntities;
				}
			}
			throw new RuntimeException("@no ens" + this.toString());
		}
		return _GetNewEntities;
	}

	protected String getCashKey_Del() {
		return null;
	}

	public String getClassID() {
		return this.toString();
	}

	// 与sql操作有关
	protected SQLCash _SQLCash = null;
	public SQLCash getSQLCash() throws Exception {
		if (_SQLCash == null) {
			_SQLCash = BP.DA.Cash.GetSQL(this.toString());
			if (_SQLCash == null) {
				_SQLCash = new SQLCash(this);
				BP.DA.Cash.SetSQL(this.toString(), _SQLCash);
			}
		}
		return _SQLCash;
	}

	public void setSQLCash(SQLCash value) {
		_SQLCash = value;
	}

	// 清除缓存SQLCase.
	public void clearSQLCash() {
		BP.DA.Cash.getSQL_Cash().remove(this.toString());
	}

	/**
	 * 转化成
	 * 
	 * @return
	 */
	public final String ToStringAtParas() {
		String str = "";
		for (Attr attr : this.getEnMap().getAttrs()) {
			str += "@" + attr.getKey() + "=" + this.GetValByKey(attr.getKey());
		}
		return str;
	}

	public final DataTable ToEmptyTableField() {
		return ToEmptyTableField(null);
	}

	public final DataTable ToEmptyTableField(Entity en) {
		DataTable dt = new DataTable();

		if (en == null)
			en = this;

		dt.TableName = en.getEnMap().getPhysicsTable();

		for (Attr attr : en.getEnMap().getAttrs()) {
			switch (attr.getMyDataType()) {
			case DataType.AppString:
				dt.Columns.Add(new DataColumn(attr.getKey(), String.class, true));
				break;
			case DataType.AppInt:
				dt.Columns.Add(new DataColumn(attr.getKey(), Integer.class, true));
				break;
			case DataType.AppFloat:
				dt.Columns.Add(new DataColumn(attr.getKey(), Float.class, true));
				break;
			case DataType.AppBoolean:
				dt.Columns.Add(new DataColumn(attr.getKey(), String.class, true));
				break;
			case DataType.AppDouble:
				dt.Columns.Add(new DataColumn(attr.getKey(), Double.class, true));
				break;
			case DataType.AppMoney:
				dt.Columns.Add(new DataColumn(attr.getKey(), Double.class, true));
				break;
			case DataType.AppDate:
				dt.Columns.Add(new DataColumn(attr.getKey(), String.class, true));
				break;
			case DataType.AppDateTime:
				dt.Columns.Add(new DataColumn(attr.getKey(), String.class, true));
				break;
			default:
				throw new RuntimeException("@bulider insert sql error: 没有这个数据类型");
			}
		}
		return dt;
	}

	/**
	 * 区分大小写
	 * 
	 * @param tableName
	 * @return
	 */
	public final DataTable ToDataTableField(String tableName) {

		DataTable dt = this.ToEmptyTableField();
		dt.TableName = tableName;

		// 增加参数列.
		if (this.getRow().containsKey("AtPara") == true) {
			/* 如果包含这个字段,就说明他有参数,把参数也要弄成一个列. */
			AtPara ap = this.getatPara();
			for (String key : ap.getHisHT().keySet()) {
				if (dt.Columns.contains(key) == true)
					continue;

				dt.Columns.Add(key, System.class);
			}
		}

		DataRow dr = dt.NewRow();
		for (Attr attr : this.getEnMap().getAttrs()) {
			if (attr.getMyDataType() == DataType.AppBoolean) {
				if (this.GetValIntByKey(attr.getKey()) == 1)
					dr.setValue(attr.getKey(), "1");
				else
					dr.setValue(attr.getKey(), "0");
				continue;
			}

			/*
			 * 如果是外键 就要去掉左右空格。
			 */
			if (attr.getMyFieldType() == FieldType.FK || attr.getMyFieldType() == FieldType.PKFK) {
				dr.setValue(attr.getKey(), this.GetValByKey(attr.getKey()).toString().trim());
			} else {

				String obj = this.GetValStrByKey(attr.getKey());
				if (obj == null && attr.getIsNum())
					obj = "0";

				dr.setValue(attr.getKey(), obj);
			}
		}

		/* 如果包含这个字段 */
		if (this.getRow().containsKey("AtPara") == true) {

			AtPara ap = this.getatPara();
			for (String key : ap.getHisHT().keySet())
				dr.setValue(key, ap.getHisHT().get(key));
		}

		dt.Rows.add(dr);
		return dt;

	}

	// 关于database 操作
	public int RunSQL(String sql) {
		Paras ps = new Paras();
		ps.SQL = sql;
		return this.RunSQL(ps);
	}

	/**
	 * 在此实体是运行sql 返回结果集合
	 * 
	 * @param sql
	 *            要运行的sql
	 * @return 执行的结果
	 * @throws Exception
	 */
	public final int RunSQL(Paras ps) {
		switch (this.getEnMap().getEnDBUrl().getDBUrlType()) {
		case AppCenterDSN:
			return DBAccess.RunSQL(ps);
		// case DBAccessOfMSMSSQL:
		// return DBAccessOfMSMSSQL.RunSQL(ps.SQL);
		// case DBAccessOfOracle:
		// return DBAccessOfOracle.RunSQL(ps.SQL);
		default:
			throw new RuntimeException("@没有设置类型。");
		}
	}

	public final int RunSQL(String sql, Paras paras) {
		switch (this.getEnMap().getEnDBUrl().getDBUrlType()) {
		case AppCenterDSN:
			return DBAccess.RunSQL(sql, paras);
		// case DBAccessOfMSMSSQL:
		// return DBAccessOfMSMSSQL.RunSQL(sql);
		// case DBAccessOfOracle:
		// return DBAccessOfOracle.RunSQL(sql);
		default:
			throw new RuntimeException("@没有设置类型。");
		}
	}

	/**
	 * 在此实体是运行sql 返回结果集合
	 * 
	 * @param sql
	 *            要运行的 select sql
	 * @return 执行的查询结果
	 * @throws Exception
	 */
	public DataTable RunSQLReturnTable(String sql) {
		switch (this.getEnMap().getEnDBUrl().getDBUrlType()) {
		case AppCenterDSN:
			return DBAccess.RunSQLReturnTable(sql);
		// case DBAccessOfMSMSSQL:
		// return DBAccessOfMSMSSQL.RunSQLReturnTable(sql);
		// case DBAccessOfOracle:
		// return DBAccessOfOracle.RunSQLReturnTable(sql);
		default:
			throw new RuntimeException("@没有设置类型。");
		}
	}

	// 关于明细的操作
	public final Entities GetEnsDaOfOneVSM(AttrOfOneVSM attr) throws Exception {
		Entities ensOfMM = attr.getEnsOfMM();
		Entities ensOfM = attr.getEnsOfM();
		ensOfM.clear();

		QueryObject qo = new QueryObject(ensOfMM);
		qo.AddWhere(attr.getAttrOfOneInMM(), this.getPKVal().toString());
		qo.DoQuery();

		for (Entity en : Entities.convertEntities(ensOfMM)) {
			Entity enOfM = ensOfM.getGetNewEntity();
			enOfM.setPKVal(en.GetValStringByKey(attr.getAttrOfMInMM()));
			enOfM.Retrieve();
			ensOfM.AddEntity(enOfM);
		}
		return ensOfM;
	}

	/**
	 * 取得实体集合多对多的实体集合.
	 * 
	 * @param ensOfMMclassName
	 *            实体集合的类名称
	 * @return 数据实体
	 * @throws Exception
	 */
	public final Entities GetEnsDaOfOneVSM(String ensOfMMclassName) throws Exception {
		AttrOfOneVSM attr = this.getEnMap().GetAttrOfOneVSM(ensOfMMclassName);

		return GetEnsDaOfOneVSM(attr);
	}

	public final Entities GetEnsDaOfOneVSMFirst() throws Exception {
		AttrOfOneVSM attr = this.getEnMap().getAttrsOfOneVSM().getItem(0);
		// throw new Exception("err "+attr.Desc);
		return this.GetEnsDaOfOneVSM(attr);
	}

	// 关于明细的操作
	/**
	 * 得到他的数据实体
	 * 
	 * @param EnsName
	 *            类名称
	 * @return
	 * @throws Exception
	 */
	public final Entities GetDtlEnsDa(String EnsName) throws Exception {
		Entities ens = ClassFactory.GetEns(EnsName);
		return GetDtlEnsDa(ens);
		//
		// EnDtls eds =this.EnMap.Dtls;
		// foreach(EnDtl ed in eds)
		// {
		// if (ed.EnsName==EnsName)
		// {
		// Entities ens =ClassFactory.GetEns(EnsName) ;
		// QueryObject qo = new QueryObject(ClassFactory.GetEns(EnsName));
		// qo.AddWhere(ed.RefKey,this.PKVal.ToString());
		// qo.DoQuery();
		// return ens;
		// }
		// }
		// throw new Exception("@实体["+this.EnDesc+"],不包含"+EnsName);
		//
	}

	/**
	 * 取出他的数据实体
	 * 
	 * @param ens
	 *            集合
	 * @return 执行后的实体信息
	 * @throws Exception
	 */
	public final Entities GetDtlEnsDa(Entities ens) throws Exception {
		for (EnDtl dtl : this.getEnMap().getDtls()) {
			if (dtl.getEns().getClass() == ens.getClass()) {
				QueryObject qo = new QueryObject(dtl.getEns());
				qo.AddWhere(dtl.getRefKey(), this.getPKVal().toString());
				qo.DoQuery();
				return dtl.getEns();
			}
		}
		throw new RuntimeException(
				"@在取[" + this.getEnDesc() + "]的明细时出现错误。[" + ens.getGetNewEntity().getEnDesc() + "],不在他的集合内。");
	}

	public final Entities GetDtlEnsDa(EnDtl dtl) throws Exception {

		try {
			QueryObject qo = new QueryObject(dtl.getEns());
			qo.AddWhere(dtl.getRefKey(), this.getPKVal().toString());
			qo.DoQuery();
			return dtl.getEns();
		} catch (RuntimeException e) {
			throw new RuntimeException("@在取[" + this.getEnDesc() + "]的明细时出现错误。[" + dtl.getDesc() + "],不在他的集合内。");
		}
	}

	// /// <summary>
	// /// 返回第一个实体
	// /// </summary>
	// /// <returns>返回第一个实体,如果没有就抛出异常</returns>
	// public Entities GetDtl()
	// {
	// return this.GetDtls(0);
	// }
	// /// <summary>
	// /// 返回第一个实体
	// /// </summary>
	// /// <returns>返回第一个实体</returns>
	// public Entities GetDtl(int index)
	// {
	// try
	// {
	// return this.GetDtls(this.EnMap.Dtls[index].Ens);
	// }
	// catch( Exception ex)
	// {
	// throw new Exception("@在取得按照顺序取["+this.EnDesc+"]的明细,出现错误:"+ex.Message);
	// }
	// }
	/**
	 * 取出他的明细集合。
	 * 
	 * @return
	 * @throws Exception
	 */
	public final java.util.ArrayList GetDtlsDatasOfArrayList() throws Exception {
		java.util.ArrayList al = new java.util.ArrayList();
		for (EnDtl dtl : this.getEnMap().getDtls()) {
			al.add(this.GetDtlEnsDa(dtl.getEns()));
		}
		return al;
	}

	public final java.util.ArrayList<Entities> GetDtlsDatasOfList() throws Exception {
		java.util.ArrayList<Entities> al = new java.util.ArrayList<Entities>();
		for (EnDtl dtl : this.getEnMap().getDtls()) {
			al.add(this.GetDtlEnsDa(dtl));
		}
		return al;
	}

	// 检查一个属性值是否存在于实体集合中
	/**
	 * 检查一个属性值是否存在于实体集合中 这个方法经常用到在beforeinsert中。
	 * 
	 * @param key
	 *            要检查的key.
	 * @param val
	 *            要检查的key.对应的val
	 * @return
	 * @throws Exception
	 * @throws NumberFormatException
	 */
	protected final int ExitsValueNum(String key, String val) throws NumberFormatException, Exception {
		String field = this.getEnMap().GetFieldByKey(key);
		Paras ps = new Paras();
		ps.Add("p", val);

		String sql = "SELECT COUNT( " + key + " ) FROM " + this.getEnMap().getPhysicsTable() + " WHERE " + key + "="
				+ this.getHisDBVarStr() + "p";
		return Integer.parseInt(DBAccess.RunSQLReturnVal(sql, ps).toString());
	}

	// 于编号有关系的处理。
	/**
	 * 这个方法是为不分级字典，生成一个编号。根据制订的 属性.
	 * 
	 * @param attrKey
	 *            属性
	 * @return 产生的序号
	 * @throws Exception
	 */
	public final String GenerNewNoByKey(String attrKey) throws Exception {
		try {
			String sql = null;
			Attr attr = this.getEnMap().GetAttrByKey(attrKey);
			if (!attr.getUIIsReadonly()) {
				throw new RuntimeException("@需要自动生成编号的列(" + attr.getKey() + ")必须为只读。");
			}

			String field = this.getEnMap().GetFieldByKey(attrKey);
			switch (this.getEnMap().getEnDBUrl().getDBType()) {
			case MSSQL:
				sql = "SELECT CONVERT(INT, MAX(CAST(" + field + " as int)) )+1 AS No FROM "
						+ this.get_enMap().getPhysicsTable();
				break;
			case Oracle:
				sql = "SELECT MAX(" + field + ") +1 AS No FROM " + this.get_enMap().getPhysicsTable();
				break;
			case MySQL:
				sql = "SELECT CONVERT(MAX(CAST(" + field + " AS SIGNED INTEGER)),SIGNED) +1 AS No FROM "
						+ this.get_enMap().getPhysicsTable();
				break;
			case Informix:
				sql = "SELECT MAX(" + field + ") +1 AS No FROM " + this.get_enMap().getPhysicsTable();
				break;
			case Access:
				sql = "SELECT MAX( [" + field + "]) +1 AS  No FROM " + this.get_enMap().getPhysicsTable();
				break;
			default:
				throw new RuntimeException("error");
			}
			String str = (new Integer(DBAccess.RunSQLReturnValInt(sql, 1))).toString();
			if (str.equals("0") || str.equals("")) {
				str = "1";
			}
			return StringUtils.leftPad(str, Integer.parseInt(this.get_enMap().getCodeStruct()), "0");
			/*
			 * warning return
			 * str.PadLeft(Integer.parseInt(this.get_enMap().getCodeStruct()),
			 * '0');
			 */
		} catch (RuntimeException ex) {
			this.CheckPhysicsTable();
			throw ex;
		}
	}

	/**
	 * 按照一列产生顺序号码。
	 * 
	 * @param attrKey
	 *            要产生的列
	 * @param attrGroupKey
	 *            分组的列名
	 * @param FKVal
	 *            分组的主键
	 * @return
	 * @throws Exception
	 */
	public final String GenerNewNoByKey(int nolength, String attrKey, String attrGroupKey, String attrGroupVal)
			throws Exception {
		if (attrGroupKey == null || attrGroupVal == null) {
			throw new RuntimeException("@分组字段attrGroupKey attrGroupVal 不能为空");
		}

		Paras ps = new Paras();

		String sql = "";
		String field = this.getEnMap().GetFieldByKey(attrKey);

		switch (this.getEnMap().getEnDBUrl().getDBType()) {
		case MSSQL:
			sql = "SELECT CONVERT(bigint, MAX([" + field + "]))+1 AS Num FROM " + this.getEnMap().getPhysicsTable()
					+ " WHERE " + attrGroupKey + "='" + attrGroupVal + "'";
			break;
		case Oracle:
			ps.Add("groupKey", attrGroupKey);
			ps.Add("groupVal", attrGroupVal);
			ps.Add("f", attrKey);
			sql = "SELECT MAX( :f )+1 AS No FROM " + this.getEnMap().getPhysicsTable() + " WHERE "
					+ this.getHisDBVarStr() + "groupKey=" + this.getHisDBVarStr() + "groupVal ";
			break;
		case Informix:
			sql = "SELECT MAX( :f )+1 AS No FROM " + this.getEnMap().getPhysicsTable() + " WHERE "
					+ this.getHisDBVarStr() + "groupKey=" + this.getHisDBVarStr() + "groupVal ";
			break;
		case MySQL:
			sql = "SELECT MAX(" + field + ") +1 AS Num FROM " + this.getEnMap().getPhysicsTable() + " WHERE "
					+ attrGroupKey + "='" + attrGroupVal + "'";
			break;
		case Access:
			sql = "SELECT MAX([" + field + "]) +1 AS Num FROM " + this.getEnMap().getPhysicsTable() + " WHERE "
					+ attrGroupKey + "='" + attrGroupVal + "'";
			break;
		default:
			throw new RuntimeException("error");
		}

		DataTable dt = DBAccess.RunSQLReturnTable(sql, ps);
		String str = "1";
		if (dt.Rows.size() != 0) {
			// System.DBNull n = new DBNull();
			/*
			 * warning if (dt.Rows[0][0] instanceof DBNull)
			 */
			if (dt.Rows.get(0).getValue(0) == null) {
				str = "1";
			} else {
				str = dt.Rows.get(0).getValue(0).toString();
				/*
				 * warning str = dt.Rows[0][0].toString();
				 */
			}
		}
		return StringUtils.leftPad(str, nolength, '0');
		/*
		 * warning return str.PadLeft(nolength, '0');
		 */
	}

	public final String GenerNewNoByKey(String attrKey, String attrGroupKey, String attrGroupVal)
			throws NumberFormatException, Exception {
		return this.GenerNewNoByKey(Integer.parseInt(this.getEnMap().getCodeStruct()), attrKey, attrGroupKey,
				attrGroupVal);
	}

	/**
	 * 按照两列查生顺序号码。
	 * 
	 * @param attrKey
	 * @param attrGroupKey1
	 * @param attrGroupKey2
	 * @param attrGroupVal1
	 * @param attrGroupVal2
	 * @return
	 * @throws Exception
	 */
	public final String GenerNewNoByKey(String attrKey, String attrGroupKey1, String attrGroupKey2,
			Object attrGroupVal1, Object attrGroupVal2) throws Exception {
		String f = this.getEnMap().GetFieldByKey(attrKey);
		Paras ps = new Paras();
		// ps.Add("f", f);

		String sql = "";
		switch (this.getEnMap().getEnDBUrl().getDBType()) {
		case Oracle:
		case Informix:
			sql = "SELECT   MAX(" + f + ") +1 AS No FROM " + this.getEnMap().getPhysicsTable();
			break;
		case MSSQL:
			sql = "SELECT CONVERT(INT, MAX(" + this.getEnMap().GetFieldByKey(attrKey) + ") )+1 AS No FROM "
					+ this.getEnMap().getPhysicsTable() + " WHERE " + this.getEnMap().GetFieldByKey(attrGroupKey1)
					+ "='" + attrGroupVal1 + "' AND " + this.getEnMap().GetFieldByKey(attrGroupKey2) + "='"
					+ attrGroupVal2 + "'";
			break;
		case Access:
			sql = "SELECT CONVERT(INT, MAX(" + this.getEnMap().GetFieldByKey(attrKey) + ") )+1 AS No FROM "
					+ this.getEnMap().getPhysicsTable() + " WHERE " + this.getEnMap().GetFieldByKey(attrGroupKey1)
					+ "='" + attrGroupVal1 + "' AND " + this.getEnMap().GetFieldByKey(attrGroupKey2) + "='"
					+ attrGroupVal2 + "'";
			break;
		default:
			break;
		}

		DataTable dt = DBAccess.RunSQLReturnTable(sql, ps);
		String str = "1";
		if (dt.Rows.size() != 0) {
			str = dt.Rows.get(0).getValue(0).toString();
			/*
			 * warning str = dt.Rows[0][0].toString();
			 */
		}
		return StringUtils.leftPad(str, Integer.parseInt(this.getEnMap().getCodeStruct()), '0');
		/*
		 * warning return
		 * str.PadLeft(Integer.parseInt(this.getEnMap().getCodeStruct()), '0');
		 */
	}

	// 构造方法
	public Entity() {
	}

	// 排序操作
	protected final void DoOrderUp(String groupKeyAttr, String groupKeyVal, String idxAttr) {
		// string pkval = this.PKVal as string;
		String pkval = this.getPKVal().toString();
		String pk = this.getPK();
		String table = this.getEnMap().getPhysicsTable();

		String sql = "SELECT " + pk + "," + idxAttr + " FROM " + table + " WHERE " + groupKeyAttr + "='" + groupKeyVal
				+ "' ORDER BY " + idxAttr;
		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		int idx = 0;
		String beforeNo = "";
		String myNo = "";
		boolean isMeet = false;

		for (DataRow dr : dt.Rows) {
			idx++;
			myNo = dr.getValue(pk).toString();
			/*
			 * warning myNo = dr[pk].toString();
			 */
			if (pkval.equals(myNo)) {
				isMeet = true;
			}

			if (!isMeet) {
				beforeNo = myNo;
			}
			DBAccess.RunSQL("UPDATE " + table + " SET " + idxAttr + "=" + idx + " WHERE " + pk + "='" + myNo + "'");

		}
		DBAccess.RunSQL(
				"UPDATE " + table + " SET " + idxAttr + "=" + idxAttr + "+1 WHERE " + pk + "='" + beforeNo + "'");
		DBAccess.RunSQL("UPDATE " + table + " SET " + idxAttr + "=" + idxAttr + "-1 WHERE " + pk + "='" + pkval + "'");

	}

	protected final void DoOrderUp(String groupKeyAttr, String groupKeyVal, String gKey2, String gVal2,
			String idxAttr) {
		// string pkval = this.PKVal as string;
		String pkval = this.getPKVal().toString();
		String pk = this.getPK();
		String table = this.getEnMap().getPhysicsTable();

		String sql = "SELECT " + pk + "," + idxAttr + " FROM " + table + " WHERE (" + groupKeyAttr + "='" + groupKeyVal
				+ "' AND " + gKey2 + "='" + gVal2 + "') ORDER BY " + idxAttr;
		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		int idx = 0;
		String beforeNo = "";
		String myNo = "";
		boolean isMeet = false;

		for (DataRow dr : dt.Rows) {
			idx++;
			myNo = dr.getValue(pk).toString();
			/*
			 * warning myNo = dr[pk].toString();
			 */
			if (pkval.equals(myNo)) {
				isMeet = true;
			}

			if (!isMeet) {
				beforeNo = myNo;
			}
			DBAccess.RunSQL("UPDATE " + table + " SET " + idxAttr + "=" + idx + " WHERE " + pk + "='" + myNo
					+ "'  AND  (" + groupKeyAttr + "='" + groupKeyVal + "' AND " + gKey2 + "='" + gVal2 + "') ");
		}
		DBAccess.RunSQL("UPDATE " + table + " SET " + idxAttr + "=" + idxAttr + "+1 WHERE " + pk + "='" + beforeNo
				+ "'  AND  (" + groupKeyAttr + "='" + groupKeyVal + "' AND " + gKey2 + "='" + gVal2 + "')");
		DBAccess.RunSQL("UPDATE " + table + " SET " + idxAttr + "=" + idxAttr + "-1 WHERE " + pk + "='" + pkval
				+ "'  AND   (" + groupKeyAttr + "='" + groupKeyVal + "' AND " + gKey2 + "='" + gVal2 + "')");
	}

	protected final void DoOrderDown(String groupKeyAttr, String groupKeyVal, String idxAttr) {
		String pkval = this.getPKVal().toString();
		String pk = this.getPK();
		String table = this.getEnMap().getPhysicsTable();

		String sql = "SELECT " + pk + " ," + idxAttr + " FROM " + table + " WHERE " + groupKeyAttr + "='" + groupKeyVal
				+ "' order by " + idxAttr;
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		int idx = 0;
		String nextNo = "";
		String myNo = "";
		boolean isMeet = false;
		for (DataRow dr : dt.Rows) {
			myNo = dr.getValue(pk).toString();
			/*
			 * warning myNo = dr[pk].toString();
			 */
			if (isMeet) {
				nextNo = myNo;
				isMeet = false;
			}
			idx++;

			if (pkval.equals(myNo)) {
				isMeet = true;
			}
			DBAccess.RunSQL("UPDATE " + table + " SET " + idxAttr + "=" + idx + " WHERE " + pk + "='" + myNo + "'");
		}

		DBAccess.RunSQL(
				"UPDATE  " + table + " SET " + idxAttr + "=" + idxAttr + "-1 WHERE " + pk + "='" + nextNo + "'");
		DBAccess.RunSQL("UPDATE  " + table + " SET " + idxAttr + "=" + idxAttr + "+1 WHERE " + pk + "='" + pkval + "'");
	}

	protected final void DoOrderDown(String groupKeyAttr, String groupKeyVal, String gKeyAttr2, String gKeyVal2,
			String idxAttr) {
		String pkval = this.getPKVal().toString();
		String pk = this.getPK();
		String table = this.getEnMap().getPhysicsTable();

		String sql = "SELECT " + pk + " ," + idxAttr + " FROM " + table + " WHERE (" + groupKeyAttr + "='" + groupKeyVal
				+ "' AND " + gKeyAttr2 + "='" + gKeyVal2 + "' ) order by " + idxAttr;
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		int idx = 0;
		String nextNo = "";
		String myNo = "";
		boolean isMeet = false;
		for (DataRow dr : dt.Rows) {
			myNo = dr.getValue(pk).toString();
			/*
			 * warning myNo = dr[pk].toString();
			 */
			if (isMeet) {
				nextNo = myNo;
				isMeet = false;
			}
			idx++;

			if (pkval.equals(myNo)) {
				isMeet = true;
			}
			DBAccess.RunSQL("UPDATE " + table + " SET " + idxAttr + "=" + idx + " WHERE " + pk + "='" + myNo
					+ "' AND  (" + groupKeyAttr + "='" + groupKeyVal + "' AND " + gKeyAttr2 + "='" + gKeyVal2 + "' ) ");
		}

		DBAccess.RunSQL("UPDATE  " + table + " SET " + idxAttr + "=" + idxAttr + "-1 WHERE " + pk + "='" + nextNo
				+ "' AND (" + groupKeyAttr + "='" + groupKeyVal + "' AND " + gKeyAttr2 + "='" + gKeyVal2 + "' )");
		DBAccess.RunSQL("UPDATE  " + table + " SET " + idxAttr + "=" + idxAttr + "+1 WHERE " + pk + "='" + pkval
				+ "' AND (" + groupKeyAttr + "='" + groupKeyVal + "' AND " + gKeyAttr2 + "='" + gKeyVal2 + "' )");
	}

	// 直接操作
	/**
	 * 直接更新
	 * 
	 * @throws Exception
	 */
	public final int DirectUpdate() throws Exception {
		return EntityDBAccess.Update(this, null);
	}

	/**
	 * 直接的Insert
	 * 
	 * @throws Exception
	 */
	public int DirectInsert() throws Exception {
		try {
			switch (SystemConfig.getAppCenterDBType()) {
			case MSSQL:
				return this.RunSQL(this.getSQLCash().Insert, SqlBuilder.GenerParas(this, null));
			case Access:
				return this.RunSQL(this.getSQLCash().Insert, SqlBuilder.GenerParas(this, null));
			case MySQL:
			case Informix:
			default:
				return this.RunSQL(this.getSQLCash().Insert.replace("[", "").replace("]", ""),
						SqlBuilder.GenerParas(this, null));
			}
		} catch (RuntimeException ex) {
			this.roll();
			if (SystemConfig.getIsDebug()) {
				try {
					this.CheckPhysicsTable();
				} catch (Exception ex1) {
					throw new RuntimeException(ex.getMessage() + " == " + ex1.getMessage());
				}
			}
			throw ex;
		}

		// this.RunSQL(this.SQLCash.Insert, SqlBuilder.GenerParas(this, null));
	}

	/**
	 * 直接的Delete
	 * 
	 * @throws Exception
	 */
	public final void DirectDelete() throws Exception {
		EntityDBAccess.Delete(this);
	}

	public void DirectSave() throws Exception {
		if (this.getIsExits()) {
			this.DirectUpdate();
		} else {
			this.DirectInsert();
		}
	}

	// Retrieve
	/**
	 * 按照属性查询
	 * 
	 * @param attr
	 *            属性名称
	 * @param val
	 *            值
	 * @return 是否查询到
	 * @throws Exception
	 */
	public final boolean RetrieveByAttrAnd(String attr1, Object val1, String attr2, Object val2) throws Exception {
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(attr1, val1);
		qo.addAnd();
		qo.AddWhere(attr2, val2);

		if (qo.DoQuery() >= 1) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 按照属性查询
	 * 
	 * @param attr
	 *            属性名称
	 * @param val
	 *            值
	 * @return 是否查询到
	 * @throws Exception
	 */
	public final boolean RetrieveByAttrOr(String attr1, Object val1, String attr2, Object val2) throws Exception {
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(attr1, val1);
		qo.addOr();
		qo.AddWhere(attr2, val2);

		if (qo.DoQuery() == 1) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 按照属性查询
	 * 
	 * @param attr
	 *            属性名称
	 * @param val
	 *            值
	 * @return 是否查询到
	 * @throws Exception
	 */
	public final boolean RetrieveByAttr(String attr, Object val) throws Exception {
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(attr, val);
		if (qo.DoQuery() == 1) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 从DBSources直接查询
	 * 
	 * @return 查询的个数
	 * @throws Exception
	 */
	public int RetrieveFromDBSources() throws Exception {
		try {
			return EntityDBAccess.Retrieve(this, this.getSQLCash().Select, SqlBuilder.GenerParasPK(this));
		} catch (java.lang.Exception e) {
			try {
				this.CheckPhysicsTable();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			return EntityDBAccess.Retrieve(this, this.getSQLCash().Select, SqlBuilder.GenerParasPK(this));
		}
	}

	/**
	 * 查询
	 * 
	 * @param key
	 * @param val
	 * @return
	 * @throws Exception
	 */
	public final int Retrieve(String key, Object val) throws Exception {
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(key, val);
		return qo.DoQuery();
	}

	public final int Retrieve(String key1, Object val1, String key2, Object val2) throws Exception {
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(key1, val1);
		qo.addAnd();
		qo.AddWhere(key2, val2);
		return qo.DoQuery();
	}

	public final int Retrieve(String key1, Object val1, String key2, Object val2, String key3, Object val3)
			throws Exception {
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(key1, val1);
		qo.addAnd();
		qo.AddWhere(key2, val2);
		qo.addAnd();
		qo.AddWhere(key3, val3);
		return qo.DoQuery();
	}

	/**
	 * 按主键查询，返回查询出来的个数。 如果查询出来的是多个实体，那把第一个实体给值。
	 * 
	 * @return 查询出来的个数
	 * @throws Exception
	 */
	public int Retrieve() throws Exception {

		// 如果是没有放入缓存的实体.
		try {
			if (EntityDBAccess.Retrieve(this, this.getSQLCash().Select, SqlBuilder.GenerParasPK(this)) <= 0) {
				String msg = "";
				if (this.getPK().equals("OID")) {
					msg += "[ 主键=OID 值=" + this.GetValStrByKey("OID") + " ]";
				} else if (this.getPK().equals("No")) {
					msg += "[ 主键=No 值=" + this.GetValStrByKey("No") + " ]";
				} else if (this.getPK().equals("MyPK")) {
					msg += "[ 主键=MyPK 值=" + this.GetValStrByKey("MyPK") + " ]";
				} else if (this.getPK().equals("ID")) {
					msg += "[ 主键=ID 值=" + this.GetValStrByKey("ID") + " ]";
				} else {
					java.util.Hashtable ht = this.getPKVals();
					/*
					 * warning for (String key : ht.keySet())
					 */
					Set<String> keys = ht.keySet();
					for (String key : keys) {
						msg += "[ 主键=" + key + " 值=" + ht.get(key) + " ]";
					}
				}
				Log.DefaultLogWriteLine(LogType.Error,
						"@没有[" + this.getEnMap().getEnDesc() + "  " + this.getEnMap().getPhysicsTable() + ", 类["
								+ this.toString() + "], 物理表[" + this.getEnMap().getPhysicsTable() + "] 实例。PK = "
								+ this.GetValByKey(this.getPK()));
			}
			return 1;
		} catch (RuntimeException ex) {
			String msg = ex.getMessage() == null ? "" : ex.getMessage();
			if (msg.contains("无效") || msg.contains("field list") ) {
				try {

					this.CheckPhysicsTable();

					if (BP.DA.DBAccess.IsExits(this.getEnMap().getPhysicsTable()) == false)
						return this.Retrieve();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			throw new RuntimeException(msg + "@在Entity(" + this.toString() + ")查询期间出现错误@" + ex.getStackTrace());
		}

	}

	/**
	 * 判断是不是存在的方法.
	 * 
	 * @return
	 * @throws Exception
	 */
	public boolean getIsExits() throws Exception {
		try {
			if (this.getPKField().contains(",")) {
				Attrs attrs = this.getEnMap().getAttrs();

				// 说明多个主键
				QueryObject qo = new QueryObject(this);
				String[] pks = this.getPKField().split("[,]", -1);

				boolean isNeedAddAnd = false;
				for (String pk : pks) {
					if (StringHelper.isNullOrEmpty(pk)) {
						continue;
					}

					if (isNeedAddAnd) {
						qo.addAnd();
					} else {
						isNeedAddAnd = true;
					}

					Attr attr = attrs.GetAttrByKey(pk);
					switch (attr.getMyDataType()) {
					case DataType.AppBoolean:
					case DataType.AppInt:
						qo.AddWhere(pk, this.GetValIntByKey(attr.getKey()));
						break;
					case DataType.AppDouble:
					case DataType.AppMoney:
					case DataType.AppRate:
						qo.AddWhere(pk, this.GetValDecimalByKey(attr.getKey()));
						break;
					default:
						qo.AddWhere(pk, this.GetValStringByKey(attr.getKey()));
						break;
					}

				}

				if (qo.DoQueryToTable().Rows.size() == 0) {
					return false;
				}

				return true;
			}

			Object obj = this.getPKVal();
			if (obj == null || obj.toString().equals("")) {
				return false;
			}

			if (this.getIsOIDEntity()) {
				if (obj.toString().equals("0")) {
					return false;
				}
			}

			// 生成数据库判断语句。
			String selectSQL = "SELECT " + this.getPKField() + " FROM " + this.getEnMap().getPhysicsTable() + " WHERE ";
			switch (this.getEnMap().getEnDBUrl().getDBType()) {
			case MSSQL:
				selectSQL += SqlBuilder.GetKeyConditionOfMS(this);
				break;
			case Oracle:
				selectSQL += SqlBuilder.GetKeyConditionOfOraForPara(this);
				break;
			case Informix:
				selectSQL += SqlBuilder.GetKeyConditionOfInformixForPara(this);
				break;
			case MySQL:
				selectSQL += SqlBuilder.GetKeyConditionOfMySQL(this);
				break;
			case Access:
				selectSQL += SqlBuilder.GetKeyConditionOfOLE(this);
				break;
			default:
				throw new RuntimeException("@没有设计到。" + this.getEnMap().getEnDBUrl().getDBUrlType());
			}

			// 从数据库里面查询，判断有没有。
			switch (this.getEnMap().getEnDBUrl().getDBUrlType()) {
			case AppCenterDSN:
				return DBAccess.IsExits(selectSQL, SqlBuilder.GenerParasPK(this));
			// case DBAccessOfMSMSSQL:
			// return DBAccessOfMSMSSQL.IsExits(selectSQL);
			// case DBAccessOfOLE:
			// return DBAccessOfOLE.IsExits(selectSQL);
			// case DBAccessOfOracle:
			// return DBAccessOfOracle.IsExits(selectSQL);
			default:
				throw new RuntimeException("@没有设计到的DBUrl。" + this.getEnMap().getEnDBUrl().getDBUrlType());
			}

		} catch (RuntimeException ex) {
			try {
				this.CheckPhysicsTable();
			} catch (Exception e) {
				e.printStackTrace();
			}
			throw ex;
		}
	}

	/**
	 * 按照主键查询，查询出来的结果不赋给当前的实体。
	 * 
	 * @return 查询出来的个数
	 * @throws Exception
	 */
	public final DataTable RetrieveNotSetValues() throws Exception {
		return this.RunSQLReturnTable(SqlBuilder.Retrieve(this));
	}

	/**
	 * 这个表里是否存在
	 * 
	 * @param pk
	 * @param val
	 * @return
	 * @throws Exception
	 */
	public final boolean IsExit(String pk, Object val) throws Exception {
		if (pk.equals("OID")) {
			if (Integer.parseInt(val.toString()) == 0) {
				return false;
			} else {
				return true;
			}
		}
		// else
		// {
		// string sql = "SELECT " + pk + " FROM " +
		// this.getEnMap().getPhysicsTable() +
		// " WHERE " + pk + " ='" + val + "'";
		// return DBAccess.IsExits(sql);
		// }

		QueryObject qo = new QueryObject(this);
		qo.AddWhere(pk, val);
		if (qo.DoQuery() == 0) {
			return false;
		} else {
			return true;
		}
	}

	public final boolean IsExit(String pk1, Object val1, String pk2, Object val2) throws Exception {
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(pk1, val1);
		qo.addAnd();
		qo.AddWhere(pk2, val2);

		if (qo.DoQuery() == 0) {
			return false;
		} else {
			return true;
		}
	}

	public final boolean IsExit(String pk1, Object val1, String pk2, Object val2, String pk3, Object val3)
			throws Exception {
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(pk1, val1);
		qo.addAnd();
		qo.AddWhere(pk2, val2);
		qo.addAnd();
		qo.AddWhere(pk3, val3);

		if (qo.DoQuery() == 0) {
			return false;
		} else {
			return true;
		}
	}

	// delete
	private boolean CheckDB() {

		// 检查数据.
		// CheckDatas ens=new CheckDatas(this.getEnMap().getPhysicsTable());
		// foreach(CheckData en in ens)
		// {
		// string
		// sql="DELETE "+en.RefTBName+" WHERE "+en.RefTBFK+"
		// ='"+this.GetValByKey(en.MainTBPK)
		// +"' ";
		// DBAccess.RunSQL(sql);
		// }

		// 判断是否有明细
		for (EnDtl dtl : this.getEnMap().getDtls()) {
			String sql = "DELETE  FROM  " + dtl.getEns().getGetNewEntity().getEnMap().getPhysicsTable() + "   WHERE  "
					+ dtl.getRefKey() + " ='" + this.getPKVal().toString() + "' ";
			// DBAccess.RunSQL(sql);
			//
			// //string
			// sql="SELECT "+dtl.RefKey+" FROM
			// "+dtl.Ens.GetNewEntity.getEnMap().getPhysicsTable()+" WHERE
			// "+dtl.RefKey+" ='"+this.PKVal.ToString()
			// +"' ";
			// DataTable dt= DBAccess.RunSQLReturnTable(sql);
			// if(dt.Rows.Count==0)
			// continue;
			// else
			// throw new
			// Exception("@["+this.EnDesc+"],删除期间出现错误，它有["+dt.Rows.Count+"]个明细存在,不能删除！");
			//
		}

		return true;
	}

	/**
	 * 删除之前要做的工作
	 * 
	 * @return
	 * @throws Exception
	 */
	protected boolean beforeDelete() throws Exception {
		if (this.getEnMap().getAttrs().Contains("MyFileName")) {
			this.DeleteHisFiles();
		}

		this.CheckDB();
		return true;
	}

	/**
	 * 删除它的文件
	 */
	public final void DeleteHisFiles() {
		// BP.DA.DBAccess.RunSQL("SELECT * FROM sys_filemanager WHERE EnName='"
		// + this.ToString() + "' AND RefVal='" + this.PKVal + "'");

		try {
			BP.DA.DBAccess.RunSQL("DELETE FROM sys_filemanager WHERE EnName='" + this.toString() + "' AND RefVal='"
					+ this.getPKVal() + "'");
		} catch (java.lang.Exception e) {

		}
	}

	/**
	 * 删除它关连的实体．
	 * 
	 * @throws Exception
	 */
	public final void DeleteHisRefEns() throws Exception {
		// 检查数据.
		// CheckDatas ens=new CheckDatas(this.getEnMap().getPhysicsTable());
		// foreach(CheckData en in ens)
		// {
		// string
		// sql="DELETE FROM "+en.RefTBName+" WHERE "+en.RefTBFK+"
		// ='"+this.GetValByKey(en.MainTBPK)
		// +"' ";
		// DBAccess.RunSQL(sql);
		// }

		// 判断是否有明细
		for (EnDtl dtl : this.getEnMap().getDtls()) {
			String sql = "DELETE FROM " + dtl.getEns().getGetNewEntity().getEnMap().getPhysicsTable() + "   WHERE  "
					+ dtl.getRefKey() + " ='" + this.getPKVal().toString() + "' ";
			DBAccess.RunSQL(sql);
		}

		// 判断是否有一对对的关系.
		for (AttrOfOneVSM dtl : this.getEnMap().getAttrsOfOneVSM()) {
			String sql = "DELETE  FROM " + dtl.getEnsOfMM().getGetNewEntity().getEnMap().getPhysicsTable()
					+ "   WHERE  " + dtl.getAttrOfOneInMM() + " ='" + this.getPKVal().toString() + "' ";
			DBAccess.RunSQL(sql);
		}
	}

	/**
	 * 把缓存删除
	 * 
	 * @throws Exception
	 */
	public final void DeleteDataAndCash() throws Exception {
		this.Delete();
		this.DeleteFromCash();
	}

	public final void DeleteFromCash() {
		// 删除缓存.
		CashEntity.Delete(this.toString(), this.getPKVal().toString());
		// 删除数据.
		this.getRow().clear();
	}

	public final int Delete() throws Exception {
		if (!this.beforeDelete()) {
			return 0;
		}

		int i = 0;
		try {
			i = EntityDBAccess.Delete(this);
		} catch (RuntimeException ex) {
			Log.DebugWriteInfo(ex.getMessage());
			throw ex;
		}

		// 开始更新内存数据。
		switch (this.getEnMap().getDepositaryOfEntity()) {
		case Application:
			// CashEntity.Insert(this.toString(), this.getPKVal().toString(),
			// this);
			// 如果执行了这个，在调用insert就会异常。
			// this.DeleteFromCash(); //
			break;
		case None:
			break;
		}

		this.afterDelete();

		return i;
	}

	/**
	 * 直接删除指定的
	 * 
	 * @param pk
	 * @throws Exception
	 */
	public final int Delete(Object pk) throws Exception {
		Paras ps = new Paras();
		ps.Add(this.getPK(), pk);
		switch (this.getEnMap().getEnDBUrl().getDBType()) {
		case Oracle:
		case MSSQL:
		case MySQL:
			return DBAccess.RunSQL("DELETE FROM " + this.getEnMap().getPhysicsTable() + " WHERE " + this.getPK() + " ="
					+ this.getHisDBVarStr() + pk);
		default:
			throw new RuntimeException("没有涉及到的类型。");
		}
	}

	/**
	 * 删除指定的数据
	 * 
	 * @param attr
	 * @param val
	 * @throws Exception
	 */
	public final int Delete(String attr, Object val) {
		Paras ps = new Paras();
		ps.Add(attr, val);
		switch (this.getEnMap().getEnDBUrl().getDBType()) {
		case Oracle:
		case MSSQL:
		case Informix:
		case MySQL:
			return DBAccess.RunSQL("DELETE FROM " + this.getEnMap().getPhysicsTable() + " WHERE "
					+ this.getEnMap().GetAttrByKey(attr).getField() + " =" + this.getHisDBVarStr() + attr, ps);
		case Access:
			return DBAccess.RunSQL("DELETE FROM " + this.getEnMap().getPhysicsTable() + " WHERE "
					+ this.getEnMap().GetAttrByKey(attr).getField() + " =" + this.getHisDBVarStr() + attr, ps);
		default:
			throw new RuntimeException("没有涉及到的类型。");
		}
	}

	public final int Delete(String attr1, Object val1, String attr2, Object val2) {
		Paras ps = new Paras();
		ps.Add(attr1, val1);
		ps.Add(attr2, val2);
		switch (this.getEnMap().getEnDBUrl().getDBType()) {
		case Oracle:
		case MSSQL:
		case Informix:
		case Access:
		case MySQL:
			return DBAccess.RunSQL("DELETE FROM " + this.getEnMap().getPhysicsTable() + " WHERE "
					+ this.getEnMap().GetAttrByKey(attr1).getField() + " =" + this.getHisDBVarStr() + attr1 + " AND "
					+ this.getEnMap().GetAttrByKey(attr2).getField() + " =" + this.getHisDBVarStr() + attr2, ps);
		default:
			throw new RuntimeException("没有涉及到的类型。");
		}
	}

	public final int Delete(String attr1, Object val1, String attr2, Object val2, String attr3, Object val3) {
		Paras ps = new Paras();
		ps.Add(attr1, val1);
		ps.Add(attr2, val2);
		ps.Add(attr3, val3);

		switch (this.getEnMap().getEnDBUrl().getDBType()) {
		case Oracle:
		case MSSQL:
		case Access:
		case MySQL:
			return DBAccess.RunSQL("DELETE FROM " + this.getEnMap().getPhysicsTable() + " WHERE "
					+ this.getEnMap().GetAttrByKey(attr1).getField() + " =" + this.getHisDBVarStr() + attr1 + " AND "
					+ this.getEnMap().GetAttrByKey(attr2).getField() + " =" + this.getHisDBVarStr() + attr2 + " AND "
					+ this.getEnMap().GetAttrByKey(attr3).getField() + " =" + this.getHisDBVarStr() + attr3, ps);
		default:
			throw new RuntimeException("没有涉及到的类型。");
		}
	}

	public final int Delete(String attr1, Object val1, String attr2, Object val2, String attr3, Object val3,
			String attr4, Object val4) {
		Paras ps = new Paras();
		ps.Add(attr1, val1);
		ps.Add(attr2, val2);
		ps.Add(attr3, val3);
		ps.Add(attr4, val4);

		switch (this.getEnMap().getEnDBUrl().getDBType()) {
		case Oracle:
		case MSSQL:
		case Access:
		case MySQL:
			return DBAccess.RunSQL("DELETE FROM " + this.getEnMap().getPhysicsTable() + " WHERE "
					+ this.getEnMap().GetAttrByKey(attr1).getField() + " =" + this.getHisDBVarStr() + attr1 + " AND "
					+ this.getEnMap().GetAttrByKey(attr2).getField() + " =" + this.getHisDBVarStr() + attr2 + " AND "
					+ this.getEnMap().GetAttrByKey(attr3).getField() + " =" + this.getHisDBVarStr() + attr3 + " AND "
					+ this.getEnMap().GetAttrByKey(attr4).getField() + " =" + this.getHisDBVarStr() + attr4, ps);
		default:
			throw new RuntimeException("没有涉及到的类型。");
		}
	}

	protected void afterDelete() throws Exception {
		if (this.getEnMap().getDepositaryOfEntity() != Depositary.Application) {
			return;
		}

		/**
		 * 删除缓存。
		 */
		BP.DA.CashEntity.Delete(this.toString(), this.getPKVal().toString());
		return;
	}

	// 参数字段
	private AtPara getAtPara() {
		Object tempVar = this.getRow().GetValByKey("_ATObj_");
		AtPara at = (AtPara) ((tempVar instanceof AtPara) ? tempVar : null);
		if (at != null) {
			return at;
		}
		try {
			String atParaStr = this.GetValStringByKey("AtPara");
			if (StringHelper.isNullOrEmpty(atParaStr)) {
				// 没有发现数据，就执行初始化.
				this.InitParaFields();

				// 重新获取一次。
				atParaStr = this.GetValStringByKey("AtPara");
				if (StringHelper.isNullOrEmpty(atParaStr)) {
					atParaStr = "";
				}

				at = new AtPara(atParaStr);
				this.SetValByKey("_ATObj_", at);
				return at;
			}
			at = new AtPara(atParaStr);
			this.SetValByKey("_ATObj_", at);
			return at;
		} catch (RuntimeException ex) {
			throw new RuntimeException(
					"@获取参数AtPara时出现异常" + ex.getMessage() + "，可能是您没有加入约定的参数字段AtPara. " + ex.getMessage());
		}
	}

	/**
	 * 初始化参数字段(需要子类重写)
	 * 
	 * @return
	 */
	protected void InitParaFields() {
	}

	/**
	 * 获取参数
	 * 
	 * @param key
	 * @return
	 */
	public final String GetParaString(String key) {
		return getAtPara().GetValStrByKey(key);
	}

	/**
	 * 获取参数
	 * 
	 * @param key
	 * @param isNullAsVal
	 * @return
	 */
	public final String GetParaString(String key, String isNullAsVal) {
		String str = getAtPara().GetValStrByKey(key);
		if (StringHelper.isNullOrEmpty(str)) {
			return isNullAsVal;
		}
		return str;
	}

	/**
	 * 获取参数Init值
	 * 
	 * @param key
	 * @return
	 */
	public final int GetParaInt(String key) {
		return getAtPara().GetValIntByKey(key);
	}

	public final int GetParaInt(String key, int isNullAsVal) {
		return getAtPara().GetValIntByKey(key, isNullAsVal);
	}

	public final float GetParaFloat(String key) {
		return getAtPara().GetValFloatByKey(key);
	}

	/**
	 * 获取参数boolen值
	 * 
	 * @param key
	 * @return
	 */
	public final boolean GetParaBoolen(String key) {
		return getAtPara().GetValBoolenByKey(key);
	}

	/**
	 * 获取参数boolen值
	 * 
	 * @param key
	 * @param IsNullAsVal
	 * @return
	 */
	public final boolean GetParaBoolen(String key, boolean IsNullAsVal) {
		return getAtPara().GetValBoolenByKey(key, IsNullAsVal);
	}

	/**
	 * 设置参数
	 * 
	 * @param key
	 * @param obj
	 */
	public final void SetPara(String key, String obj) {
		if (getAtPara() != null) {
			this.getRow().remove("_ATObj_");
		}

		String atParaStr = this.GetValStringByKey("AtPara");
		if (!atParaStr.contains("@" + key + "=")) {
			atParaStr += "@" + key + "=" + obj;
			this.SetValByKey("AtPara", atParaStr);
			return;
		} else {
			AtPara at = new AtPara(atParaStr);
			at.SetVal(key, obj);
			this.SetValByKey("AtPara", at.GenerAtParaStrs());
			return;
		}
	}

	public final void SetPara(String key, int obj) {
		SetPara(key, (new Integer(obj)).toString());
	}

	public final void SetPara(String key, float obj) {
		SetPara(key, (new Float(obj)).toString());
	}

	public final void SetPara(String key, boolean obj) {
		if (!obj) {
			SetPara(key, "0");
		} else {
			SetPara(key, "1");
		}
	}

	// 通用方法
	/**
	 * 获取实体
	 * 
	 * @param key
	 */
	public final Object GetRefObject(String key) {
		return this.getRow().GetValByKey("_" + key);
		/*
		 * warning return this.getRow()["_" + key];
		 */
		// object obj = this.Row[key];
		// if (obj == null)
		// {
		// if (!this.Row.ContainsKey(key))
		// return null;
		// obj = this.Row[key];
		// }
		// return obj;
	}

	/**
	 * 设置实体
	 * 
	 * @param key
	 * @param obj
	 */
	public final void SetRefObject(String key, Object obj) {
		if (obj == null) {
			return;
		}

		this.getRow().SetValByKey("_" + key, obj);
	}

	// insert
	/**
	 * 在插入之前要做的工作。
	 * 
	 * @return
	 * @throws Exception
	 */
	protected boolean beforeInsert() throws Exception {
		return true;
	}

	protected boolean roll() {
		return true;
	}

	public void InsertWithOutPara() throws Exception {
		this.RunSQL(SqlBuilder.Insert(this));
	}

	/**
	 * Insert .
	 * 
	 * @throws Exception
	 */
	public int Insert() throws Exception {
		if (!this.beforeInsert()) {
			return 0;
		}

		if (!this.beforeUpdateInsertAction()) {
			return 0;
		}

		int i = 0;
		try {
			i = this.DirectInsert();
		} catch (RuntimeException ex) {
			this.CheckPhysicsTable();
			throw ex;
		}

		// 开始更新内存数据。
		switch (this.getEnMap().getDepositaryOfEntity()) {
		case Application:
			CashEntity.Insert(this.toString(), this.getPKVal().toString(), this);
			break;
		case None:
			break;
		}

		this.afterInsert();
		this.afterInsertUpdateAction();

		return i;
	}

	protected void afterInsert() throws Exception {
		// added by liuxc,2016-02-19,新建时，新增一个版本记录
		if (this.getEnMap().IsEnableVer) {
			// 增加版本为1的版本历史记录
			String enName = this.toString();
			String rdt = BP.DA.DataType.getCurrentDataTime();

			// edited by
			// liuxc,2017-03-24,增加判断，如果相同主键的数据曾被删除掉，再次被增加时，会延续被删除时的版本，原有逻辑报错
			EnVer ver = new EnVer();
			ver.setMyPK(enName + "_" + this.getPKVal());

			if (ver.RetrieveFromDBSources() == 0) {
				ver.setNo(enName);
				ver.setPKValue(this.getPKVal().toString());
				ver.setName(this.getEnMap().getEnDesc());
			} else {
				ver.setEVer(ver.getEVer() + 1);
			}

			ver.setRDT(rdt);
			ver.setRec(BP.Web.WebUser.getName());
			ver.Save();

			// 保存字段数据.
			Attrs attrs = this.getEnMap().getAttrs();
			for (Attr attr : attrs.ToJavaList()) {
				if (attr.getIsRefAttr()) {
					continue;
				}

				EnVerDtl dtl = new EnVerDtl();
				dtl.setEnVerPK(ver.getMyPK());
				dtl.setEnVer(ver.getEVer());
				dtl.setEnName(ver.getNo());
				dtl.setAttrKey(attr.getKey());
				dtl.setAttrName(attr.getDesc());
				// dtl.OldVal = this.GetValStrByKey(attr.Key); //第一个版本时，旧值没有
				dtl.setRDT(rdt);
				dtl.setRec(BP.Web.WebUser.getName());
				dtl.setNewVal(this.GetValStrByKey(attr.getKey()));
				dtl.setMyPK(ver.getMyPK() + "_" + attr.getKey() + "_" + dtl.getEnVer());
				dtl.Insert();
			}
		}

		return;
	}

	/**
	 * 在更新与插入之后要做的工作.
	 * 
	 * @throws Exception
	 */
	protected void afterInsertUpdateAction() throws Exception {
		if (this.getEnMap().getHisFKEnumAttrs().size() > 0) {
			this.RetrieveFromDBSources();
		}

		if (this.getEnMap().IsAddRefName) {
			this.ReSetNameAttrVal();
			this.DirectUpdate();
		}
		return;
	}

	/**
	 * 从一个副本上copy. 用于两个数性基本相近的 实体 copy.
	 * 
	 * @param fromEn
	 */
	public void Copy(Entity fromEn) {
		for (Attr attr : this.getEnMap().getAttrs()) {
			// if (attr.IsPK)
			// continue;

			Object obj = fromEn.GetValByKey(attr.getKey());
			if (obj == null) {
				continue;
			}

			this.SetValByKey(attr.getKey(), obj);
		}
	}

	/**
	 * 从一个副本上
	 * 
	 * @param fromRow
	 */
	public void Copy(Row fromRow) {
		Attrs attrs = this.getEnMap().getAttrs();
		for (Attr attr : attrs) {
			try {
				this.SetValByKey(attr.getKey(), fromRow.GetValByKey(attr.getKey()));
			} catch (java.lang.Exception e) {
			}
		}
	}

	public void Copy(XmlEn xmlen) {
		Attrs attrs = this.getEnMap().getAttrs();
		for (Attr attr : attrs) {
			Object obj = null;
			try {
				obj = xmlen.GetValByKey(attr.getKey());
			} catch (java.lang.Exception e) {
				continue;
			}

			if (obj == null || obj.toString().equals("")) {
				continue;
			}
			this.SetValByKey(attr.getKey(), xmlen.GetValByKey(attr.getKey()));
		}
	}

	/**
	 * 复制 Hashtable
	 * 
	 * @param ht
	 */
	public void Copy(java.util.Hashtable ht) {
		/*
		 * warning for (String k : ht.keySet())
		 */
		Set<String> keys = ht.keySet();
		for (String k : keys) {
			Object obj = null;
			try {
				obj = ht.get(k);
			} catch (java.lang.Exception e) {
				continue;
			}

			if (obj == null || obj.toString().equals("")) {
				continue;
			}
			this.SetValByKey(k, obj);
		}
	}

	public void Copy(DataRow dr) {
		for (Attr attr : this.getEnMap().getAttrs()) {
			try {
				this.SetValByKey(attr.getKey(), dr.getValue(attr.getKey()));
				/*
				 * warning this.SetValByKey(attr.getKey(), dr[attr.getKey()]);
				 */
			} catch (java.lang.Exception e) {
			}
		}
	}

	public final String Copy(String refDoc) {
		for (Attr attr : this.get_enMap().getAttrs()) {
			refDoc = refDoc.replace("@" + attr.getKey(), this.GetValStrByKey(attr.getKey()));
		}
		return refDoc;
	}

	public final void Copy() {
		for (Attr attr : this.getEnMap().getAttrs()) {
			if (!attr.getIsPK()) {
				continue;
			}

			if (attr.getMyDataType() == DataType.AppInt) {
				this.SetValByKey(attr.getKey(), 0);
			} else {
				this.SetValByKey(attr.getKey(), "");
			}
		}

		try {
			this.SetValByKey("No", "");
		} catch (java.lang.Exception e) {
		}
	}

	// verify
	/**
	 * 校验数据
	 * 
	 * @return
	 */
	public final boolean verifyData() {
		return true;

		// throw new Exception("@在保存[" + this.EnDesc + "],PK[" + this.PK + "=" +
		// this.PKVal + "]时出现信息录入不整错误：" + str);

	}

	// 更新，插入之前的工作。
	protected boolean beforeUpdateInsertAction() throws Exception {
		switch (this.getEnMap().getEnType()) {
		case View:
		case XML:
		case Ext:
			return false;
		default:
			break;
		}

		this.verifyData();
		return true;
	}

	// 更新，插入之前的工作。

	// 更新操作
	/**
	 * 更新
	 * 
	 * @return
	 * @throws Exception
	 */
	public int Update() throws Exception {
		return this.Update(null);
	}

	/**
	 * 仅仅更新一个属性
	 * 
	 * @param key1
	 *            key1
	 * @param val1
	 *            val1
	 * @return 更新的个数
	 * @throws Exception
	 */
	public final int Update(String key1, Object val1) throws Exception {
		this.SetValByKey(key1, val1);
		return this.Update(key1.split("[,]", -1));
	}

	public final int Update(String key1, Object val1, String key2, Object val2) throws Exception {
		this.SetValByKey(key1, val1);
		this.SetValByKey(key2, val2);

		key1 = key1 + "," + key2;
		return this.Update(key1.split("[,]", -1));
	}

	public final int Update(String key1, Object val1, String key2, Object val2, String key3, Object val3)
			throws Exception {
		this.SetValByKey(key1, val1);
		this.SetValByKey(key2, val2);
		this.SetValByKey(key3, val3);

		key1 = key1 + "," + key2 + "," + key3;
		return this.Update(key1.split("[,]", -1));
	}

	public final int Update(String key1, Object val1, String key2, Object val2, String key3, Object val3, String key4,
			Object val4) throws Exception {
		this.SetValByKey(key1, val1);
		this.SetValByKey(key2, val2);
		this.SetValByKey(key3, val3);
		this.SetValByKey(key4, val4);
		key1 = key1 + "," + key2 + "," + key3 + "," + key4;
		return this.Update(key1.split("[,]", -1));
	}

	public final int Update(String key1, Object val1, String key2, Object val2, String key3, Object val3, String key4,
			Object val4, String key5, Object val5) throws Exception {
		this.SetValByKey(key1, val1);
		this.SetValByKey(key2, val2);
		this.SetValByKey(key3, val3);
		this.SetValByKey(key4, val4);
		this.SetValByKey(key5, val5);

		key1 = key1 + "," + key2 + "," + key3 + "," + key4 + "," + key5;
		return this.Update(key1.split("[,]", -1));
	}

	public final int Update(String key1, Object val1, String key2, Object val2, String key3, Object val3, String key4,
			Object val4, String key5, Object val5, String key6, Object val6) throws Exception {
		this.SetValByKey(key1, val1);
		this.SetValByKey(key2, val2);
		this.SetValByKey(key3, val3);
		this.SetValByKey(key4, val4);
		this.SetValByKey(key5, val5);
		this.SetValByKey(key6, val6);
		key1 = key1 + "," + key2 + "," + key3 + "," + key4 + "," + key5 + "," + key6;
		return this.Update(key1.split("[,]", -1));
	}

	protected boolean beforeUpdate() throws Exception {
		return true;
	}

	/**
	 * 更新实体
	 * 
	 * @throws Exception
	 */
	public final int Update(String[] keys) throws Exception {
		String str = "";
		try {
			str = "@更新之前出现错误 ";
			if (!this.beforeUpdate()) {
				return 0;
			}

			str = "@更新插入之前出现错误";
			if (!this.beforeUpdateInsertAction()) {
				return 0;
			}

			str = "@更新时出现错误";
			int i = EntityDBAccess.Update(this, keys);
			str = "@更新之后出现错误";

			// 开始更新内存数据。
			switch (this.getEnMap().getDepositaryOfEntity()) {
			case Application:
				// this.DeleteFromCash();
				CashEntity.Update(this.toString(), this.getPKVal().toString(), this);
				break;
			case None:
				break;
			}
			this.afterUpdate();
			str = "@更新插入之后出现错误";
			this.afterInsertUpdateAction();
			return i;
		} catch (RuntimeException ex) {
			if (ex.getMessage().contains("将截断字符串") && ex.getMessage().contains("缺少")) {
				// 说明字符串长度有问题.
				try {
					this.CheckPhysicsTable();
				} catch (Exception e) {
					e.printStackTrace();
				}

				// 比较参数那个字段长度有问题
				String errs = "";
				for (Attr attr : this.getEnMap().getAttrs()) {
					if (attr.getMyDataType() != BP.DA.DataType.AppString) {
						continue;
					}

					if (attr.getMaxLength() < this.GetValStrByKey(attr.getKey()).length()) {
						errs += "@映射里面的" + attr.getKey() + "," + attr.getDesc() + ", 相对于输入的数据:{"
								+ this.GetValStrByKey(attr.getKey()) + "}, 太长。";
					}
				}

				if (!errs.equals("")) {
					throw new RuntimeException(
							"@执行更新[" + this.toString() + "]出现错误@错误字段:" + errs + " <br>清你在提交一次。" + ex.getMessage());
				} else {
					throw ex;
				}
			}

			Log.DefaultLogWriteLine(LogType.Error, ex.getMessage());
			if (SystemConfig.getIsDebug()) {
				throw new RuntimeException("@[" + this.getEnDesc() + "]更新期间出现错误:" + str + ex.getMessage());
			} else {
				throw ex;
			}
		}
	}

	private int UpdateOfDebug(String[] keys) throws Exception {
		String str = "";
		try {
			str = "@在更新之前出现错误";
			if (!this.beforeUpdate()) {
				return 0;
			}
			str = "@在beforeUpdateInsertAction出现错误";
			if (!this.beforeUpdateInsertAction()) {
				return 0;
			}
			int i = EntityDBAccess.Update(this, keys);
			str = "@在afterUpdate出现错误";
			this.afterUpdate();
			str = "@在afterInsertUpdateAction出现错误";
			this.afterInsertUpdateAction();
			// this.UpdateMemory();
			return i;
		} catch (RuntimeException ex) {
			String msg = "@[" + this.getEnDesc() + "]UpdateOfDebug更新期间出现错误:" + str + ex.getMessage();
			Log.DefaultLogWriteLine(LogType.Error, msg);

			if (SystemConfig.getIsDebug()) {
				throw new RuntimeException(msg);
			} else {
				throw ex;
			}
		}
	}

	protected void afterUpdate() {
		return;
	}

	// 对文件的处理. add by qin 15/10/31
	public final void SaveBigTxtToDB(String saveToField, String bigTxt) throws Exception {
		String temp = BP.Sys.SystemConfig.getPathOfTemp() + "/" + this.getEnMap().getPhysicsTable() + this.getPKVal()
				+ ".tmp";
		DataType.WriteFile(temp, bigTxt);

		// 写入数据库.
		SaveFileToDB(saveToField, temp);
	}

	/**
	 * 保存文件到数据库
	 * 
	 * @param saveToField
	 *            要保存的字段
	 * @param filefullName
	 *            文件路径
	 * @throws Exception
	 */
	public final void SaveFileToDB(String saveToField, String filefullName) throws Exception {
		try {
			BP.DA.DBAccess.SaveFileToDB(filefullName, this.getEnMap().getPhysicsTable(), this.getPK(),
					this.getPKVal().toString(), saveToField);
		} catch (RuntimeException ex) {
			// 为了防止任何可能出现的数据丢失问题，您应该先仔细检查此脚本，然后再在数据库设计器的上下文之外运行此脚本。
			String sql = "";
			if (BP.DA.DBAccess.getAppCenterDBType().equals(DBType.MSSQL)) {
				sql = "ALTER TABLE " + this.getEnMap().getPhysicsTable() + " ADD " + saveToField + " Image NULL ";
			}

			if (BP.DA.DBAccess.getAppCenterDBType().equals(DBType.Oracle)) {
				sql = "ALTER TABLE " + this.getEnMap().getPhysicsTable() + " ADD " + saveToField + " Blob NULL ";
			}

			if (BP.DA.DBAccess.getAppCenterDBType().equals(DBType.MySQL)) {
				sql = "ALTER TABLE " + this.getEnMap().getPhysicsTable() + " ADD " + saveToField + " MediumBlob NULL ";
			}

			BP.DA.DBAccess.RunSQL(sql);

			throw new RuntimeException("@保存文件期间出现错误，有可能该字段没有被自动创建，现在已经执行创建修复数据表，请重新执行一次." + ex.getMessage());
		}
	}

	/**
	 * 从表的字段里读取文件
	 * 
	 * @param saveToField
	 *            字段
	 * @param filefullName
	 *            文件路径,如果为空怎不保存直接返回文件流，如果不为空则创建文件。
	 * @return 返回文件流
	 * @throws IOException
	 */
	public final byte[] GetFileFromDB(String saveToField, String filefullName) throws IOException {
		BP.DA.DBAccess.GetFileFromDB(filefullName, this.getEnMap().getPhysicsTable(), this.getPK(),
				this.getPKVal().toString(), saveToField);
		return null;
	}

	/**
	 * 从表的字段里读取string
	 * 
	 * @param imgFieldName
	 *            字段名
	 * @return 大文本数据
	 * @throws IOException
	 */
	public final String GetBigTextFromDB(String imgFieldName) throws IOException {
		String tempFile = BP.Sys.SystemConfig.getPathOfTemp() + "/" + this.getEnMap().getPhysicsTable()
				+ this.getPKVals() + ".tmp";

		File file = new File(tempFile);
		if (file.exists()) {
			file.delete();
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return BP.DA.DBAccess.GetTextFileFromDB(tempFile, this.getEnMap().getPhysicsTable(), this.getPK(),
				this.getPKVal().toString(), imgFieldName);
	}

	/**
	 * 从表的字段里读取string
	 * 
	 * @param imgFieldName
	 *            字段名
	 * @return 大文本数据
	 * @throws IOException
	 */
	public final String GetBigTextFromDB(String imgFieldName, String codeType) throws IOException {
		String tempFile = BP.Sys.SystemConfig.getPathOfTemp() + "/" + this.getEnMap().getPhysicsTable()
				+ this.getPKVals() + ".tmp";

		File file = new File(tempFile);
		if (file.exists()) {
			file.delete();
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return BP.DA.DBAccess.GetTextFileFromDB(tempFile, this.getEnMap().getPhysicsTable(), this.getPK(),
				this.getPKVal().toString(), imgFieldName, codeType);
	}

	// 对文件的处理. add by qin 15/10/31

	public int Save() throws Exception {
		if (this.getPK().equals("OID")) {
			if (this.GetValIntByKey("OID") == 0) {
				// this.SetValByKey("OID",EnDA.GenerOID());
				this.Insert();
				return 1;
			} else {
				this.Update();
				return 1;
			}
		} else if (this.getPK().equals("MyPK") || this.getPK().equals("No") || this.getPK().equals("ID")) {
			//自动生成的MYPK，插入前获取主键
            this.beforeUpdateInsertAction();
			String pk = this.GetValStrByKey(this.getPK());
			if (pk.equals("") || pk == null) {
				this.Insert();
				return 1;
			} else {
				int i = this.Update();
				if (i == 0) {
					this.Insert();
					i = 1;
				}
				return i;
			}
		} else {
			if (this.Update() == 0) {
				this.Insert();
			}
			return 1;
		}
	}

	// 关于数据库的处理
	/**
	 * 把系统日期转换为 Oracle 能够存储的日期类型.
	 */
	protected final void TurnSysDataToOrData() {
		Map map = this.getEnMap();
		String val = "";
		for (Attr attr : map.getAttrs()) {
			try {
				val = this.GetValStringByKey(attr.getKey());
				switch (attr.getMyDataType()) {
				case DataType.AppDateTime:
					if (val.toUpperCase().indexOf("_DATE") > 0) {
						continue;
					}
					this.SetValByKey(attr.getKey(),
							" TO_DATE('" + val + "', '" + DataType.getSysDataTimeFormat() + "') ");
					break;
				case DataType.AppDate:
					if (val.toUpperCase().indexOf("_DATE") > 0) {
						continue;
					}

					if (val.length() > 10) {
						val = val.substring(0, 10);
					}
					this.SetValByKey(attr.getKey(),
							" TO_DATE('" + val + "', '" + DataType.getSysDataFormat() + "'    )");
					break;
				default:
					break;
				}
			} catch (RuntimeException ex) {
				throw new RuntimeException(
						"执行日期转换期间出现错误:EnName=" + this.toString() + " TurnSysDataToOrData@ Attr=" + attr.getKey()
								+ " , Val=" + this.GetValStringByKey(attr.getKey()) + " Message=" + ex.getMessage());
			}
		}
	}

	/**
	 * 检查是否是日期
	 */
	protected final void CheckDateAttr() {
		Attrs attrs = this.getEnMap().getAttrs();
		for (Attr attr : attrs) {
			if (attr.getMyDataType() == DataType.AppDate || attr.getMyDataType() == DataType.AppDateTime) {
				java.util.Date dt = this.GetValDateTime(attr.getKey());
			}
		}
	}

	/**
	 * 建立物理表
	 */
	protected final void CreatePhysicsTable() {
		switch (DBAccess.getAppCenterDBType()) {
		case Oracle:

			DBAccess.RunSQL(SqlBuilder.GenerCreateTableSQLOfOra_OK(this));

			break;
		// case Informix:
		// DBAccess.RunSQL(SqlBuilder.GenerCreateTableSQLOfInfoMix(this));
		// break;
		case MSSQL:
			DBAccess.RunSQL(SqlBuilder.GenerCreateTableSQLOfMS(this));
			break;
		case MySQL:
			DBAccess.RunSQL(SqlBuilder.GenerCreateTableSQLOfMySQL(this));
			break;
		// case Access:
		// DBAccess.RunSQL(SqlBuilder.GenerCreateTableSQLOf_OLE(this));
		// break;
		default:
			throw new RuntimeException("@未判断的数据库类型。");
		}
		this.CreateIndexAndPK();
	}

	private void CreateIndexAndPK() {

		// 建立主键.
		int pkconut = this.getPKCount();

		if (pkconut == 1) {
			DBAccess.CreatePK(this.getEnMap().getPhysicsTable(), this.getPKField(),
					this.getEnMap().getEnDBUrl().getDBType());

			DBAccess.CreatIndex(this.getEnMap().getPhysicsTable(), this.getPKField());
		} else if (pkconut == 2) {
			String pk0 = this.getPKs()[0];
			String pk1 = this.getPKs()[1];
			DBAccess.CreatePK(this.getEnMap().getPhysicsTable(), pk0, pk1, this.getEnMap().getEnDBUrl().getDBType());
			DBAccess.CreatIndex(this.getEnMap().getPhysicsTable(), pk0, pk1);

		} else if (pkconut == 3) {

			String pk0 = this.getPKs()[0];
			String pk1 = this.getPKs()[1];
			String pk2 = this.getPKs()[2];
			DBAccess.CreatePK(this.getEnMap().getPhysicsTable(), pk0, pk1, pk2,
					this.getEnMap().getEnDBUrl().getDBType());
			DBAccess.CreatIndex(this.getEnMap().getPhysicsTable(), pk0, pk1, pk2);

		}
	}

	/**
	 * 如果一个属性是外键，并且它还有一个字段存储它的名称。 设置这个外键名称的属性。
	 */
	protected final void ReSetNameAttrVal() {
		Attrs attrs = this.getEnMap().getAttrs();
		for (Attr attr : attrs) {
			if (!attr.getIsFKorEnum()) {
				continue;
			}

			String s = this.GetValRefTextByKey(attr.getKey());
			this.SetValByKey(attr.getKey() + "Name", s);
		}
	}

	private void CheckPhysicsTable_SQL() throws Exception {
		String table = this.get_enMap().getPhysicsTable();
		DBType dbtype = this.get_enMap().getEnDBUrl().getDBType();
		String sqlFields = "";
		String sqlYueShu = "";

		sqlFields = "SELECT column_name as FName,data_type as FType,CHARACTER_MAXIMUM_LENGTH as FLen from information_schema.columns where table_name='"
				+ this.getEnMap().getPhysicsTable() + "'";
		sqlYueShu = "SELECT b.name, a.name FName from sysobjects b join syscolumns a on b.id = a.cdefault where a.id = object_id('"
				+ this.getEnMap().getPhysicsTable() + "') ";

		DataTable dtAttr = null;
		try {
			dtAttr = DBAccess.RunSQLReturnTable(sqlFields);
		} catch (Exception e7) {
			e7.printStackTrace();
		}
		DataTable dtYueShu = null;
		try {
			dtYueShu = DBAccess.RunSQLReturnTable(sqlYueShu);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		// 修复表字段。
		Attrs attrs = this.get_enMap().getAttrs();
		for (Attr attr : attrs) {
			if (attr.getIsRefAttr()) {
				continue;
			}

			String FType = "";
			String Flen = "";

			// 判断是否存在.
			boolean isHave = false;
			for (DataRow dr : dtAttr.Rows) {
				if (dr.getValue("FName").toString().toLowerCase().equals(attr.getField().toLowerCase())) {
					isHave = true;
					FType = (String) ((dr.getValue("FType") instanceof String) ? dr.getValue("FType") : null);
					Flen = dr.getValue("FLen") == null ? "0" : dr.getValue("FLen").toString();
					break;
				}
				/*
				 * warning if
				 * (dr["FName"].toString().toLowerCase().equals(attr.getField
				 * ().toLowerCase())) { isHave = true; FType =
				 * (String)((dr["FType"] instanceof String) ? dr["FType"] :
				 * null); Flen = dr["FLen"].toString(); break; }
				 */
			}

			if (isHave == false) {
				// 不存在此列 , 就增加此列。
				switch (attr.getMyDataType()) {
				case DataType.AppString:
				case DataType.AppDate:
				case DataType.AppDateTime:
					int len = attr.getMaxLength();
					if (len == 0) {
						len = 200;
					}
					// throw new Exception("属性的最小长度不能为0。");
					if (dbtype == DBType.Access && len >= 254) {
						try {
							DBAccess.RunSQL("ALTER TABLE " + this.getEnMap().getPhysicsTable() + " ADD "
									+ attr.getField() + "  Memo DEFAULT '" + attr.getDefaultVal() + "' NULL");
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else {
						try {
							DBAccess.RunSQL(
									"ALTER TABLE " + this.getEnMap().getPhysicsTable() + " ADD " + attr.getField()
											+ " NVARCHAR(" + len + ") DEFAULT '" + attr.getDefaultVal() + "' NULL");
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					continue;
				case DataType.AppInt:
					try {

						if (attr.getIsPK()) {
							DBAccess.RunSQL("ALTER TABLE " + this.getEnMap().getPhysicsTable() + " ADD "
									+ attr.getField() + " INT DEFAULT '" + attr.getDefaultVal() + "' NOT NULL");
						} else

						{
							DBAccess.RunSQL("ALTER TABLE " + this.getEnMap().getPhysicsTable() + " ADD "
									+ attr.getField() + " INT DEFAULT '" + attr.getDefaultVal() + "'   NULL");
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					continue;
				case DataType.AppBoolean:
					try {
						DBAccess.RunSQL("ALTER TABLE " + this.getEnMap().getPhysicsTable() + " ADD " + attr.getField()
								+ " INT DEFAULT '" + attr.getDefaultVal() + "'   NULL");
					} catch (Exception e) {
						e.printStackTrace();
					}
					continue;
				case DataType.AppFloat:
				case DataType.AppMoney:
				case DataType.AppRate:
				case DataType.AppDouble:
					try {
						DBAccess.RunSQL("ALTER TABLE " + this.getEnMap().getPhysicsTable() + " ADD " + attr.getField()
								+ " FLOAT DEFAULT '" + attr.getDefaultVal() + "' NULL");
					} catch (Exception e) {
						e.printStackTrace();
					}
					continue;
				default:
					throw new RuntimeException("error MyFieldType= " + attr.getMyFieldType() + " key=" + attr.getKey());
				}
			}

			// 检查类型是否匹配.
			switch (attr.getMyDataType()) {
			case DataType.AppString:
			case DataType.AppDate:
			case DataType.AppDateTime:
				if (FType.toLowerCase().contains("char")) {
					if (attr.getIsPK())
						continue;

					// 类型正确，检查长度
					if (Flen == null) {
						throw new RuntimeException("" + attr.getKey() + " -" + sqlFields);
					}
					int len = Integer.parseInt(Flen);
					if (len < attr.getMaxLength()) {
						try {
							if (attr.getMaxLength() >= 4000) {
								DBAccess.RunSQL("alter table " + this.getEnMap().getPhysicsTable() + " ALTER column  "
										+ attr.getKey() + " NVARCHAR(4000)");
							} else {
								DBAccess.RunSQL("alter table " + this.getEnMap().getPhysicsTable() + " ALTER column  "
										+ attr.getKey() + " NVARCHAR(" + attr.getMaxLength() + ")");
							}
						} catch (java.lang.Exception e) {
							// 如果类型不匹配，就删除它在重新建, 先删除约束，在删除列，在重建。
							for (DataRow dr : dtYueShu.Rows) {
								if (dr.getValue("FName").toString().toLowerCase().equals(attr.getKey().toLowerCase())) {
									try {
										DBAccess.RunSQL("alter table " + table + " drop constraint "
												+ dr.getValue(0).toString());
									} catch (Exception e1) {
										e1.printStackTrace();
									}
								}
								/*
								 * warning if
								 * (dr["FName"].toString().toLowerCase
								 * ().equals(attr.getKey().toLowerCase())) {
								 * DBAccess.RunSQL("alter table " + table +
								 * " drop constraint " +
								 * dr.getValue(0).toString()); }
								 */
							}

							// 在执行一遍.
							if (attr.getMaxLength() >= 4000) {
								DBAccess.RunSQL("alter table " + this.getEnMap().getPhysicsTable() + " ALTER column "
										+ attr.getKey() + " NVARCHAR(4000)");
							} else {
								DBAccess.RunSQL("alter table " + this.getEnMap().getPhysicsTable() + " ALTER column  "
										+ attr.getKey() + " NVARCHAR(" + attr.getMaxLength() + ")");
							}
						}
					}
				} else {
					// 如果类型不匹配，就删除它在重新建, 先删除约束，在删除列，在重建。
					for (DataRow dr : dtYueShu.Rows) {
						if (dr.getValue("FName").toString().toLowerCase().equals(attr.getKey().toLowerCase())) {
							DBAccess.RunSQL("alter table " + table + " drop constraint " + dr.getValue(0).toString());
						}
						/*
						 * warning if
						 * (dr["FName"].toString().toLowerCase().equals
						 * (attr.getKey().toLowerCase())) {
						 * DBAccess.RunSQL("alter table " + table +
						 * " drop constraint " + dr.getValue(0).toString()); }
						 */
					}

					DBAccess.RunSQL(
							"ALTER TABLE " + this.getEnMap().getPhysicsTable() + " drop column " + attr.getField());
					DBAccess.RunSQL("ALTER TABLE " + this.getEnMap().getPhysicsTable() + " ADD " + attr.getField()
							+ " NVARCHAR(" + attr.getMaxLength() + ") DEFAULT '" + attr.getDefaultVal() + "' NULL");
					continue;
				}
				break;
			case DataType.AppInt:
			case DataType.AppBoolean:
				if (!FType.equals("int")) {
					// 如果类型不匹配，就删除它在重新建, 先删除约束，在删除列，在重建。
					for (DataRow dr : dtYueShu.Rows) {
						if (dr.getValue("FName").toString().toLowerCase().equals(attr.getKey().toLowerCase())) {
							DBAccess.RunSQL("alter table " + table + " drop constraint " + dr.getValue(0).toString());
						}
						/*
						 * warning if
						 * (dr["FName"].toString().toLowerCase().equals
						 * (attr.getKey().toLowerCase())) {
						 * DBAccess.RunSQL("alter table " + table +
						 * " drop constraint " + dr.getValue(0).toString()); }
						 */
					}
					DBAccess.RunSQL(
							"ALTER TABLE " + this.getEnMap().getPhysicsTable() + " drop column " + attr.getField());
					DBAccess.RunSQL("ALTER TABLE " + this.getEnMap().getPhysicsTable() + " ADD " + attr.getField()
							+ " INT DEFAULT '" + attr.getDefaultVal() + "' NULL");
					continue;
				}
				break;
			case DataType.AppFloat:
			case DataType.AppMoney:
			case DataType.AppRate:
			case DataType.AppDouble:
				if (!FType.equals("float")) {
					// 如果类型不匹配，就删除它在重新建, 先删除约束，在删除列，在重建。
					for (DataRow dr : dtYueShu.Rows) {
						if (dr.getValue("FName").toString().toLowerCase().equals(attr.getKey().toLowerCase())) {
							DBAccess.RunSQL("alter table " + table + " drop constraint " + dr.getValue(0).toString());
						}
						/*
						 * warning if
						 * (dr["FName"].toString().toLowerCase().equals
						 * (attr.getKey().toLowerCase())) {
						 * DBAccess.RunSQL("alter table " + table +
						 * " drop constraint " + dr.getValue(0).toString()); }
						 */
					}
					DBAccess.RunSQL(
							"ALTER TABLE " + this.getEnMap().getPhysicsTable() + " drop column " + attr.getField());
					DBAccess.RunSQL("ALTER TABLE " + this.getEnMap().getPhysicsTable() + " ADD " + attr.getField()
							+ " FLOAT DEFAULT '" + attr.getDefaultVal() + "' NULL");
					continue;
				}
				break;
			default:
				throw new RuntimeException("error MyFieldType= " + attr.getMyFieldType() + " key=" + attr.getKey());
			}
		}
		// 修复表字段。

		// 检查枚举类型是否存在.
		attrs = this.get_enMap().getHisEnumAttrs();
		for (Attr attr : attrs) {
			if (attr.getMyDataType() != DataType.AppInt) {
				continue;
			}

			if (attr.UITag == null) {
				continue;
			}

			try {
				SysEnums ses = new SysEnums(attr.getUIBindKey(), attr.UITag);
				continue;
			} catch (java.lang.Exception e2) {
				e2.printStackTrace();
			}

			try {
				String[] strs = attr.UITag.split("[@]", -1);
				SysEnums ens = new SysEnums();
				ens.Delete(SysEnumAttr.EnumKey, attr.getUIBindKey());
				for (String s : strs) {
					if (s.equals("") || s == null) {
						continue;
					}

					String[] vk = s.split("[=]", -1);
					SysEnum se = new SysEnum();
					se.setIntKey(Integer.parseInt(vk[0]));
					se.setLab(vk[1]);
					se.setEnumKey(attr.getUIBindKey());
					se.Insert();
				}
			} catch (RuntimeException ex) {
				throw new RuntimeException(
						"@自动增加枚举时出现错误，请确定您的格式是否正确。" + ex.getMessage() + "attr.UIBindKey=" + attr.getUIBindKey());
			}

		}
		// 建立索引
		try {
			int pkconut = this.getPKCount();
			if (pkconut == 1) {
				DBAccess.CreatIndex(this.get_enMap().getPhysicsTable(), this.getPKField());
			} else if (pkconut == 2) {
				String pk0 = this.getPKs()[0];
				String pk1 = this.getPKs()[1];
				DBAccess.CreatIndex(this.get_enMap().getPhysicsTable(), pk0, pk1);
			} else if (pkconut == 3) {
				try {
					String pk0 = this.getPKs()[0];
					String pk1 = this.getPKs()[1];
					String pk2 = this.getPKs()[2];
					try {
						DBAccess.CreatIndex(this.get_enMap().getPhysicsTable(), pk0, pk1, pk2);
					} catch (Exception e) {
						System.out.println("WF_GenerWorkerlist的索引已经存在");
					}
				} catch (java.lang.Exception e3) {
				}
			} else if (pkconut == 4) {
				try {
					String pk0 = this.getPKs()[0];
					String pk1 = this.getPKs()[1];
					String pk2 = this.getPKs()[2];
					String pk3 = this.getPKs()[3];
					DBAccess.CreatIndex(this.get_enMap().getPhysicsTable(), pk0, pk1, pk2, pk3);
				} catch (java.lang.Exception e4) {
				}
			}
		} catch (RuntimeException ex) {
			Log.DefaultLogWriteLineError(ex.getMessage());
			throw ex;
			// throw new Exception("create pk error :"+ex.Message );
		}

		// 建立主键
		if (!DBAccess.IsExitsTabPK(this.get_enMap().getPhysicsTable())) {
			try {
				int pkconut = this.getPKCount();
				if (pkconut == 1) {
					try {
						DBAccess.CreatePK(this.get_enMap().getPhysicsTable(), this.getPKField(),
								this.get_enMap().getEnDBUrl().getDBType());
						DBAccess.CreatIndex(this.get_enMap().getPhysicsTable(), this.getPKField());
					} catch (RuntimeException ex) {
					}
				} else if (pkconut == 2) {
					try {
						String pk0 = this.getPKs()[0];
						String pk1 = this.getPKs()[1];
						DBAccess.CreatePK(this.get_enMap().getPhysicsTable(), pk0, pk1,
								this.get_enMap().getEnDBUrl().getDBType());
						DBAccess.CreatIndex(this.get_enMap().getPhysicsTable(), pk0, pk1);
					} catch (java.lang.Exception e5) {
					}
				} else if (pkconut == 3) {
					try {
						String pk0 = this.getPKs()[0];
						String pk1 = this.getPKs()[1];
						String pk2 = this.getPKs()[2];
						DBAccess.CreatePK(this.get_enMap().getPhysicsTable(), pk0, pk1, pk2,
								this.get_enMap().getEnDBUrl().getDBType());
						DBAccess.CreatIndex(this.get_enMap().getPhysicsTable(), pk0, pk1, pk2);
					} catch (java.lang.Exception e6) {
					}
				}
			} catch (RuntimeException ex) {
				Log.DefaultLogWriteLineError(ex.getMessage());
				throw ex;
			}
		}
	}

	/**
	 * 检查物理表
	 * 
	 * @throws Exception
	 */
	public final void CheckPhysicsTable() throws Exception {

		this.set_enMap(this.getEnMap());

		// string msg = "";
		if (this.get_enMap().getEnType() == EnType.View || this.get_enMap().getEnType() == EnType.XML
				|| this.get_enMap().getEnType() == EnType.ThirdPartApp || this.get_enMap().getEnType() == EnType.Ext) {
			return;
		}
		if (!DBAccess.IsExitsObject(this.get_enMap().getPhysicsTable())) {
			// 如果物理表不存在就新建立一个物理表。
			this.CreatePhysicsTable();
			return;
		}

		DBType dbtype = this.get_enMap().getEnDBUrl().getDBType();
		if (this.get_enMap().getIsView()) {
			return;
		}

		// 如果不是主应用程序的数据库就不让执行检查. 考虑第三方的系统的安全问题.
		if (this.get_enMap().getEnDBUrl().getDBUrlType() != DBUrlType.AppCenterDSN) {
			return;
		}

		switch (SystemConfig.getAppCenterDBType()) {
		case MSSQL:
			this.CheckPhysicsTable_SQL();
			break;
		case Oracle:
			this.CheckPhysicsTable_Ora();
			break;
		case MySQL:
			this.CheckPhysicsTable_MySQL();
			break;
		case Informix:
			this.CheckPhysicsTable_Informix();
			break;
		default:
			break;
		}

		this.CreateIndexAndPK();

	}

	private void CheckPhysicsTable_Informix() throws Exception {
		// 检查字段是否存在
		String sql = "SELECT *  FROM " + this.getEnMap().getPhysicsTable() + " WHERE 1=2";
		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);

		// 如果不存在.
		for (Attr attr : this.getEnMap().getAttrs()) {
			if (attr.getMyFieldType() == FieldType.RefText) {
				continue;
			}

			if (attr.getIsPK()) {
				continue;
			}

			if (dt.Columns.contains(attr.getKey())) {
				continue;
			}

			if (attr.getKey().equals("AID")) {
				// 自动增长列
				DBAccess.RunSQL("ALTER TABLE " + this.getEnMap().getPhysicsTable() + " ADD " + attr.getField()
						+ " INT  Identity(1,1)");
				continue;
			}

			// 不存在此列 , 就增加此列。
			switch (attr.getMyDataType()) {
			case DataType.AppString:
			case DataType.AppDate:
			case DataType.AppDateTime:
				int len = attr.getMaxLength();
				if (len == 0) {
					len = 200;
				}

				if (len >= 254) {
					DBAccess.RunSQL("alter table " + this.getEnMap().getPhysicsTable() + " add " + attr.getField()
							+ " lvarchar(" + len + ") default '" + attr.getDefaultVal() + "'");
				} else {
					DBAccess.RunSQL("alter table " + this.getEnMap().getPhysicsTable() + " add " + attr.getField()
							+ " varchar(" + len + ") default '" + attr.getDefaultVal() + "'");
				}
				break;
			case DataType.AppInt:
			case DataType.AppBoolean:
				DBAccess.RunSQL("ALTER TABLE " + this.getEnMap().getPhysicsTable() + " ADD " + attr.getField()
						+ " INT8 DEFAULT " + attr.getDefaultVal() + " ");
				break;
			case DataType.AppFloat:
			case DataType.AppMoney:
			case DataType.AppRate:
			case DataType.AppDouble:
				DBAccess.RunSQL("ALTER TABLE " + this.getEnMap().getPhysicsTable() + " ADD " + attr.getField()
						+ " FLOAT DEFAULT  " + attr.getDefaultVal() + " ");
				break;
			default:
				throw new RuntimeException("error MyFieldType= " + attr.getMyFieldType() + " key=" + attr.getKey());
			}
		}

		// 检查字段长度是否符合最低要求
		for (Attr attr : this.getEnMap().getAttrs()) {
			if (attr.getMyFieldType() == FieldType.RefText) {
				continue;
			}
			if (attr.getMyDataType() == DataType.AppDouble || attr.getMyDataType() == DataType.AppFloat
					|| attr.getMyDataType() == DataType.AppInt || attr.getMyDataType() == DataType.AppMoney
					|| attr.getMyDataType() == DataType.AppBoolean || attr.getMyDataType() == DataType.AppRate) {
				continue;
			}

			int maxLen = attr.getMaxLength();
			dt = new DataTable();
			sql = "select c.*  from syscolumns c inner join systables t on c.tabid = t.tabid where t.tabname = lower('"
					+ this.getEnMap().getPhysicsTable().toLowerCase() + "') and c.colname = lower('" + attr.getKey()
					+ "') and c.collength < " + attr.getMaxLength();
			dt = this.RunSQLReturnTable(sql);
			if (dt.Rows.size() == 0) {
				continue;
			}
			for (DataRow dr : dt.Rows) {
				try {
					if (attr.getMaxLength() >= 255) {
						this.RunSQL("alter table " + dr.getValue("owner") + "." + this.getEnMap().getPhysicsTableExt()
								+ " modify " + attr.getField() + " lvarchar(" + attr.getMaxLength() + ")");
						/*
						 * warning this.RunSQL("alter table " + dr["owner"] +
						 * "." + this.getEnMap().getPhysicsTableExt() +
						 * " modify " + attr.getField() + " lvarchar(" +
						 * attr.getMaxLength() + ")");
						 */
					} else {
						this.RunSQL("alter table " + dr.getValue("owner") + "." + this.getEnMap().getPhysicsTableExt()
								+ " modify " + attr.getField() + " varchar(" + attr.getMaxLength() + ")");
						/*
						 * warning this.RunSQL("alter table " + dr["owner"] +
						 * "." + this.getEnMap().getPhysicsTableExt() +
						 * " modify " + attr.getField() + " varchar(" +
						 * attr.getMaxLength() + ")");
						 */
					}
				} catch (RuntimeException ex) {
					BP.DA.Log.DebugWriteWarning(ex.getMessage());
				}
			}
		}

		// 检查枚举类型字段是否是INT 类型
		Attrs attrs = this.get_enMap().getHisEnumAttrs();
		for (Attr attr : attrs) {
			if (attr.getMyDataType() != DataType.AppInt) {
				continue;
			}

			// SUNXD 20170714
			// 由于ALL_TAB_COLUMNS表中有可能会出现用户名(owner)不一样，表名(table_name)一样的数据，导至会去修改其它用户下的表
			// 增加查询条件owner = 当前系统配置的连接用户(SystemConfig.getUser().toUpperCase())

			sql = "SELECT DATA_TYPE FROM ALL_TAB_COLUMNS WHERE OWNER = '" + SystemConfig.getUser().toUpperCase()
					+ "' AND TABLE_NAME='" + this.getEnMap().getPhysicsTableExt().toLowerCase() + "' AND COLUMN_NAME='"
					+ attr.getField().toLowerCase() + "'";
			String val = DBAccess.RunSQLReturnString(sql);
			if (val == null) {
				Log.DefaultLogWriteLineError("@没有检测到字段:" + attr.getKey());
			}

			if (val.indexOf("CHAR") != -1) {
				// 如果它是 varchar 字段
				// SUNXD 20170714
				// 由于ALL_TAB_COLUMNS表中有可能会出现用户名(owner)不一样，表名(table_name)一样的数据，导至会去修改其它用户下的表
				// 增加查询条件owner =
				// 当前系统配置的连接用户(SystemConfig.getUser().toUpperCase())
				sql = "SELECT OWNER FROM ALL_TAB_COLUMNS WHERE OWNER = '" + SystemConfig.getUser().toUpperCase()
						+ "' AND  upper(TABLE_NAME)='" + this.getEnMap().getPhysicsTableExt().toUpperCase()
						+ "' AND UPPER(COLUMN_NAME)='" + attr.getField().toUpperCase() + "' ";
				String OWNER = DBAccess.RunSQLReturnString(sql);
				try {
					this.RunSQL("alter table  " + this.getEnMap().getPhysicsTableExt() + " modify " + attr.getField()
							+ " NUMBER ");
				} catch (RuntimeException ex) {
					Log.DefaultLogWriteLineError("运行sql 失败:alter table  " + this.getEnMap().getPhysicsTableExt()
							+ " modify " + attr.getField() + " NUMBER " + ex.getMessage());
				}
			}
		}

		// 检查枚举类型是否存在.
		attrs = this.get_enMap().getHisEnumAttrs();
		for (Attr attr : attrs) {
			if (attr.getMyDataType() != DataType.AppInt) {
				continue;
			}
			if (attr.UITag == null) {
				continue;
			}
			try {
				SysEnums ses = new SysEnums(attr.getUIBindKey(), attr.UITag);
				continue;
			} catch (java.lang.Exception e) {
			}
			String[] strs = attr.UITag.split("[@]", -1);
			SysEnums ens = new SysEnums();
			ens.Delete(SysEnumAttr.EnumKey, attr.getUIBindKey());
			for (String s : strs) {
				if (s.equals("") || s == null) {
					continue;
				}

				String[] vk = s.split("[=]", -1);
				SysEnum se = new SysEnum();
				se.setIntKey(Integer.parseInt(vk[0]));
				se.setLab(vk[1]);
				se.setEnumKey(attr.getUIBindKey());
				se.Insert();
			}
		}

		this.CreateIndexAndPK();
	}

	private void CheckPhysicsTable_MySQL() throws Exception {
		// 检查字段是否存在
		String sql = "SELECT *  FROM " + this.get_enMap().getPhysicsTable() + " WHERE 1=2";
		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);

		// 如果不存在.
		for (Attr attr : this.get_enMap().getAttrs()) {
			if (attr.getMyFieldType() == FieldType.RefText) {
				continue;
			}

			if (attr.getIsPK()) {
				continue;
			}

			if (dt.Columns.get(attr.getKey().toLowerCase()) != null) {
				continue;
			}

			if (attr.getKey().equals("AID")) {
				// 自动增长列
				DBAccess.RunSQL("ALTER TABLE " + this.get_enMap().getPhysicsTable() + " ADD " + attr.getField()
						+ " INT  Identity(1,1)");
				continue;
			}

			// 不存在此列 , 就增加此列。
			switch (attr.getMyDataType()) {
			case DataType.AppString:
				int len = attr.getMaxLength();
				if (len == 0) {
					len = 200;
				}
				if (len > 3000) {
					DBAccess.RunSQL(
							"ALTER TABLE " + this.get_enMap().getPhysicsTable() + " ADD " + attr.getField() + " TEXT ");
				} else {
					DBAccess.RunSQL("ALTER TABLE " + this.get_enMap().getPhysicsTable() + " ADD " + attr.getField()
							+ " VARCHAR(" + len + ") DEFAULT '" + attr.getDefaultVal() + "' NULL");
				}
				break;
			case DataType.AppDate:
			case DataType.AppDateTime:
				DBAccess.RunSQL("ALTER TABLE " + this.get_enMap().getPhysicsTable() + " ADD " + attr.getField()
						+ " VARCHAR(20) DEFAULT '" + attr.getDefaultVal() + "' NULL");
				break;
			case DataType.AppInt:
			case DataType.AppBoolean:
				DBAccess.RunSQL("ALTER TABLE " + this.get_enMap().getPhysicsTable() + " ADD " + attr.getField()
						+ " INT DEFAULT " + attr.getDefaultVal() + " NULL");
				break;
			case DataType.AppFloat:
			case DataType.AppMoney:
			case DataType.AppRate:
			case DataType.AppDouble:
				DBAccess.RunSQL("ALTER TABLE " + this.get_enMap().getPhysicsTable() + " ADD " + attr.getField()
						+ " FLOAT (50,2)  DEFAULT " + attr.getDefaultVal());
				break;
			default:
				throw new RuntimeException("error MyFieldType= " + attr.getMyFieldType() + " key=" + attr.getKey());
			}
		}

		// 检查字段长度是否符合最低要求
		for (Attr attr : this.get_enMap().getAttrs()) {
			if (attr.getMyFieldType() == FieldType.RefText) {
				continue;
			}
			if (attr.getMyDataType() == DataType.AppDouble || attr.getMyDataType() == DataType.AppFloat
					|| attr.getMyDataType() == DataType.AppInt || attr.getMyDataType() == DataType.AppMoney
					|| attr.getMyDataType() == DataType.AppBoolean || attr.getMyDataType() == DataType.AppRate) {
				continue;
			}
			// MySQL需要检查数据类型的长度

			int maxLen = attr.getMaxLength();
			dt = new DataTable();
			sql = "select character_maximum_length as Len, table_schema as OWNER FROM information_schema.columns WHERE TABLE_SCHEMA='"
					+ BP.Sys.SystemConfig.getAppCenterDBDatabase() + "' AND table_name ='"
					+ this.get_enMap().getPhysicsTable() + "' and column_Name='" + attr.getField()
					+ "' AND character_maximum_length < " + attr.getMaxLength();
			dt = this.RunSQLReturnTable(sql);
			if (dt.Rows.size() == 0) {
				continue;
			}
			for (DataRow dr : dt.Rows) {
				try {
					this.RunSQL("alter table " + dr.getValue("OWNER") + "." + this.get_enMap().getPhysicsTableExt()
							+ " modify " + attr.getField() + " NVARCHAR(" + attr.getMaxLength() + ")");
					/*
					 * warning this.RunSQL("alter table " + dr["OWNER"] + "." +
					 * this.get_enMap().getPhysicsTableExt() + " modify " +
					 * attr.getField() + " NVARCHAR(" + attr.getMaxLength() +
					 * ")");
					 */
				} catch (RuntimeException ex) {
					BP.DA.Log.DebugWriteWarning(ex.getMessage());
				}
			}
		}

		// 检查枚举类型字段是否是INT 类型
		Attrs attrs = this.get_enMap().getHisEnumAttrs();
		for (Attr attr : attrs) {
			if (attr.getMyDataType() != DataType.AppInt) {
				continue;
			}

			sql = "SELECT DATA_TYPE FROM information_schema.columns WHERE table_name='"
					+ this.get_enMap().getPhysicsTable() + "' AND COLUMN_NAME='" + attr.getField()
					+ "' and table_schema='" + SystemConfig.getAppCenterDBDatabase() + "'";
			String val = DBAccess.RunSQLReturnString(sql);
			if (val == null) {
				Log.DefaultLogWriteLineError("@没有检测到字段eunm" + attr.getKey());
			}

			if (val.indexOf("CHAR") != -1) {
				// 如果它是 varchar 字段
				sql = "SELECT table_schema as OWNER FROM information_schema.columns WHERE  table_name='"
						+ this.get_enMap().getPhysicsTableExt() + "' AND COLUMN_NAME='" + attr.getField()
						+ "' and table_schema='" + SystemConfig.getAppCenterDBDatabase() + "'";
				String OWNER = DBAccess.RunSQLReturnString(sql);
				try {
					this.RunSQL("alter table  " + this.get_enMap().getPhysicsTableExt() + " modify " + attr.getField()
							+ " NUMBER ");
				} catch (RuntimeException ex) {
					Log.DefaultLogWriteLineError("运行sql 失败:alter table  " + this.get_enMap().getPhysicsTableExt()
							+ " modify " + attr.getField() + " NUMBER " + ex.getMessage());
				}
			}
		}

		// 检查枚举类型是否存在.
		attrs = this.get_enMap().getHisEnumAttrs();
		for (Attr attr : attrs) {
			if (attr.getMyDataType() != DataType.AppInt) {
				continue;
			}
			if (attr.UITag == null) {
				continue;
			}
			try {
				SysEnums ses = new SysEnums(attr.getUIBindKey(), attr.UITag);
				continue;
			} catch (java.lang.Exception e) {
			}
			String[] strs = attr.UITag.split("[@]", -1);
			SysEnums ens = new SysEnums();
			ens.Delete(SysEnumAttr.EnumKey, attr.getUIBindKey());
			for (String s : strs) {
				if (s.equals("") || s == null) {
					continue;
				}

				String[] vk = s.split("[=]", -1);
				SysEnum se = new SysEnum();
				se.setIntKey(Integer.parseInt(vk[0]));
				se.setLab(vk[1]);
				se.setEnumKey(attr.getUIBindKey());
				se.Insert();
			}
		}
		this.CreateIndexAndPK();
	}

	private void CheckPhysicsTable_Ora() throws Exception {
		// 检查字段是否存在
		String sql = "SELECT *  FROM " + this.getEnMap().getPhysicsTable() + " WHERE 1=2";
		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);

		// 如果不存在.
		for (Attr attr : this.getEnMap().getAttrs()) {
			if (attr.getMyFieldType() == FieldType.RefText) {
				continue;
			}

			if (attr.getIsPK()) {
				continue;
			}

			if (dt.Columns.contains(attr.getKey())) {
				continue;
			}

			if (attr.getKey().equals("AID")) {
				// 自动增长列
				DBAccess.RunSQL("ALTER TABLE " + this.getEnMap().getPhysicsTable() + " ADD " + attr.getField()
						+ " INT  Identity(1,1)");
				continue;
			}

			// 不存在此列 , 就增加此列。
			switch (attr.getMyDataType()) {
			case DataType.AppString:
			case DataType.AppDate:
			case DataType.AppDateTime:
				int len = attr.getMaxLength();
				if (len == 0) {
					len = 200;
				}
				DBAccess.RunSQL("ALTER TABLE " + this.getEnMap().getPhysicsTable() + " ADD " + attr.getField()
						+ " VARCHAR(" + len + ") DEFAULT '" + attr.getDefaultVal() + "' NULL");
				break;
			case DataType.AppInt:
			case DataType.AppBoolean:
				DBAccess.RunSQL("ALTER TABLE " + this.getEnMap().getPhysicsTable() + " ADD " + attr.getField()
						+ " INT DEFAULT " + attr.getDefaultVal() + "  NULL");
				break;
			case DataType.AppFloat:
			case DataType.AppMoney:
			case DataType.AppRate:
			case DataType.AppDouble:
				DBAccess.RunSQL("ALTER TABLE " + this.getEnMap().getPhysicsTable() + " ADD " + attr.getField()
						+ " FLOAT DEFAULT '" + attr.getDefaultVal() + "' NULL");
				break;
			default:
				throw new RuntimeException("error MyFieldType= " + attr.getMyFieldType() + " key=" + attr.getKey());
			}
		}

		// 检查字段长度是否符合最低要求
		for (Attr attr : this.getEnMap().getAttrs()) {
			if (attr.getMyFieldType() == FieldType.RefText) {
				continue;
			}
			if (attr.getMyDataType() == DataType.AppDouble || attr.getMyDataType() == DataType.AppFloat
					|| attr.getMyDataType() == DataType.AppInt || attr.getMyDataType() == DataType.AppMoney
					|| attr.getMyDataType() == DataType.AppBoolean || attr.getMyDataType() == DataType.AppRate) {
				continue;
			}

			int maxLen = attr.getMaxLength();
			dt = new DataTable();
			// SUNXD 20170714
			// 由于ALL_TAB_COLUMNS表中有可能会出现用户名(owner)不一样，表名(table_name)一样的数据，导至会去修改其它用户下的表
			// 增加查询条件owner = 当前系统配置的连接用户(SystemConfig.getUser().toUpperCase())
			sql = "SELECT DATA_LENGTH AS LEN, OWNER FROM ALL_TAB_COLUMNS WHERE OWNER = '"
					+ SystemConfig.getUser().toUpperCase() + "' AND upper(TABLE_NAME)='"
					+ this.getEnMap().getPhysicsTableExt().toUpperCase() + "' AND UPPER(COLUMN_NAME)='"
					+ attr.getField().toUpperCase() + "' AND DATA_LENGTH < " + attr.getMaxLength();
			dt = this.RunSQLReturnTable(sql);
			if (dt.Rows.size() == 0) {
				continue;
			}
			for (DataRow dr : dt.Rows) {
				try {
					this.RunSQL("alter table " + dr.getValue("OWNER") + "." + this.getEnMap().getPhysicsTableExt()
							+ " modify " + attr.getField() + " varchar2(" + attr.getMaxLength() + ")");
					/*
					 * warning this.RunSQL("alter table " + dr["OWNER"] + "." +
					 * this.getEnMap().getPhysicsTableExt() + " modify " +
					 * attr.getField() + " varchar2(" + attr.getMaxLength() +
					 * ")");
					 */
				} catch (RuntimeException ex) {
					BP.DA.Log.DebugWriteWarning(ex.getMessage());
				}
			}
		}

		// 检查枚举类型字段是否是INT 类型
		Attrs attrs = this.get_enMap().getHisEnumAttrs();
		for (Attr attr : attrs) {
			if (attr.getMyDataType() != DataType.AppInt) {
				continue;
			}
			// SUNXD 20170714
			// 由于ALL_TAB_COLUMNS表中有可能会出现用户名(owner)不一样，表名(table_name)一样的数据，导至会去修改其它用户下的表
			// 增加查询条件owner = 当前系统配置的连接用户(SystemConfig.getUser().toUpperCase())

			sql = "SELECT DATA_TYPE FROM ALL_TAB_COLUMNS WHERE OWNER = '" + SystemConfig.getUser().toUpperCase()
					+ "' AND upper(TABLE_NAME)='" + this.getEnMap().getPhysicsTableExt().toUpperCase()
					+ "' AND UPPER(COLUMN_NAME)='" + attr.getField().toUpperCase() + "' ";
			String val = DBAccess.RunSQLReturnString(sql);
			if (val == null) {
				Log.DefaultLogWriteLineError("@没有检测到字段eunm" + attr.getKey());
			}
			if (val.indexOf("CHAR") != -1) {
				// 如果它是 varchar 字段

				// SUNXD 20170714
				// 由于ALL_TAB_COLUMNS表中有可能会出现用户名(owner)不一样，表名(table_name)一样的数据，导至会去修改其它用户下的表
				// 增加查询条件owner =
				// 当前系统配置的连接用户(SystemConfig.getUser().toUpperCase())
				sql = "SELECT A.OWNER FROM ALL_TAB_COLUMNS WHERE OWNER = '" + SystemConfig.getUser().toUpperCase()
						+ "' AND upper(TABLE_NAME)='" + this.getEnMap().getPhysicsTableExt().toUpperCase()
						+ "' AND UPPER(COLUMN_NAME)='" + attr.getField().toUpperCase() + "' ";
				String OWNER = DBAccess.RunSQLReturnString(sql);
				try {
					this.RunSQL("alter table  " + this.getEnMap().getPhysicsTableExt() + " modify " + attr.getField()
							+ " NUMBER ");
				} catch (RuntimeException ex) {
					Log.DefaultLogWriteLineError("运行sql 失败:alter table  " + this.getEnMap().getPhysicsTableExt()
							+ " modify " + attr.getField() + " NUMBER " + ex.getMessage());
				}
			}
		}

		// 检查枚举类型是否存在.
		attrs = this.get_enMap().getHisEnumAttrs();
		for (Attr attr : attrs) {
			if (attr.getMyDataType() != DataType.AppInt) {
				continue;
			}
			if (attr.UITag == null) {
				continue;
			}
			try {
				SysEnums ses = new SysEnums(attr.getUIBindKey(), attr.UITag);
				continue;
			} catch (java.lang.Exception e) {
			}
			String[] strs = attr.UITag.split("[@]", -1);
			SysEnums ens = new SysEnums();
			ens.Delete(SysEnumAttr.EnumKey, attr.getUIBindKey());
			for (String s : strs) {
				if (s.equals("") || s == null) {
					continue;
				}

				String[] vk = s.split("[=]", -1);
				SysEnum se = new SysEnum();
				se.setIntKey(Integer.parseInt(vk[0]));
				se.setLab(vk[1]);
				se.setEnumKey(attr.getUIBindKey());
				se.Insert();
			}
		}
		this.CreateIndexAndPK();
	}

	public final void AutoFull() throws Exception {
		if (null == this.getPKVal() || this.getPKVal().equals("0") || this.getPKVal().equals("")) {
			return;
		}

		if (!this.getEnMap().getIsHaveAutoFull()) {
			return;
		}

		Attrs attrs = this.getEnMap().getAttrs();
		String jsAttrs = "";
		/*
		 * warning java.util.ArrayList al = new java.util.ArrayList();
		 */
		java.util.ArrayList<Attr> al = new java.util.ArrayList();
		for (Attr attr : attrs) {
			if (attr.AutoFullDoc == null || attr.AutoFullDoc.length() == 0) {
				continue;
			}

			// 这个代码需要提纯到基类中去。
			switch (attr.autoFullWay) {
			case Way0:
				continue;
			case Way1_JS:
				al.add(attr);
				break;
			case Way2_SQL:
				String sql = attr.AutoFullDoc;
				sql = sql.replace("~", "'");

				sql = sql.replace("WebUser.No", WebUser.getNo());
				sql = sql.replace("@WebUser.Name", WebUser.getName());
				sql = sql.replace("@WebUser.FK_Dept", WebUser.getFK_Dept());

				if (sql.contains("@")) {
					Attrs attrs1 = this.getEnMap().getAttrs();
					for (Attr a1 : attrs1) {
						if (!sql.contains("@")) {
							break;
						}

						if (!sql.contains("@" + a1.getKey())) {
							continue;
						}

						if (a1.getIsNum()) {
							sql = sql.replace("@" + a1.getKey(), this.GetValStrByKey(a1.getKey()));
						} else {
							sql = sql.replace("@" + a1.getKey(), "'" + this.GetValStrByKey(a1.getKey()) + "'");
						}
					}
				}

				sql = sql.replace("''", "'");
				String val = "";
				try {
					val = DBAccess.RunSQLReturnString(sql);
				} catch (RuntimeException ex) {
					throw new RuntimeException("@字段(" + attr.getKey() + "," + attr.getDesc()
							+ ")自动获取数据期间错误(有可能是您写的sql语句会返回多列多行的table,现在只要一列一行的table才能填充，请检查sql.):"
							+ sql.replace("'", "“") + " @Tech Info:" + ex.getMessage().replace("'", "“") + "@执行的sql:"
							+ sql);
				}

				if (attr.getIsNum()) {
					// 如果是数值类型的就尝试着转换数值，转换不了就跑出异常信息。
					try {
						java.math.BigDecimal d = new java.math.BigDecimal(val);
						/*
						 * warning java.math.BigDecimal d =
						 * java.math.BigDecimal.Parse(val);
						 */
					} catch (java.lang.Exception e) {
						throw new RuntimeException(val);
					}
				}
				this.SetValByKey(attr.getKey(), val);
				break;
			case Way3_FK:
				try {
					String sqlfk = "SELECT @Field FROM @Table WHERE No=@AttrKey";
					String[] strsFK = attr.AutoFullDoc.split("[@]", -1);
					for (String str : strsFK) {
						if (str == null || str.length() == 0) {
							continue;
						}

						String[] ss = str.split("[=]", -1);
						if (ss[0].equals("AttrKey")) {
							String tempV = this.GetValStringByKey(ss[1]);
							if (tempV.equals("") || tempV == null) {
								if (!this.getEnMap().getAttrs().Contains(ss[1])) {
									throw new RuntimeException("@自动获取值信息不完整,Map 中已经不包含Key=" + ss[1] + "的属性。");
								}

								// throw new
								// Exception("@自动获取值信息不完整,Map 中已经不包含Key=" +
								// ss[1] + "的属性。");
								sqlfk = sqlfk.replace('@' + ss[0], "'@xxx'");
								Log.DefaultLogWriteLineWarning(
										"@在自动取值期间出现错误:" + this.toString() + " , " + this.getPKVal() + "没有自动获取到信息。");
							} else {
								sqlfk = sqlfk.replace('@' + ss[0], "'" + this.GetValStringByKey(ss[1]) + "'");
							}
						} else {
							sqlfk = sqlfk.replace('@' + ss[0], ss[1]);
						}
					}

					sqlfk = sqlfk.replace("''", "'");
					this.SetValByKey(attr.getKey(), DBAccess.RunSQLReturnStringIsNull(sqlfk, null));
				} catch (RuntimeException ex) {
					throw new RuntimeException(
							"@在处理自动完成：外键[" + attr.getKey() + ";" + attr.getDesc() + "],时出现错误。异常信息：" + ex.getMessage());
				}
				break;
			case Way4_Dtl:
				if (this.getPKVal().equals("0")) {
					continue;
				}

				String mysql = "SELECT @Way(@Field) FROM @Table WHERE RefPK =" + this.getPKVal();
				String[] strs = attr.AutoFullDoc.split("[@]", -1);
				for (String str : strs) {
					if (str == null || str.length() == 0) {
						continue;
					}

					String[] ss = str.split("[=]", -1);
					mysql = mysql.replace('@' + ss[0], ss[1]);
				}

				String v = DBAccess.RunSQLReturnString(mysql);
				if (v == null) {
					v = "0";
				}
				this.SetValByKey(attr.getKey(), new java.math.BigDecimal(v));
				/*
				 * warning this.SetValByKey(attr.getKey(),
				 * java.math.BigDecimal.Parse(v));
				 */

				break;
			default:
				throw new RuntimeException("未涉及到的类型。");
			}
		}

		// 处理JS的计算。
		for (Attr attr : al) {
			/*
			 * warning String doc = attr.AutoFullDoc.clone().toString();
			 */
			String doc = new String(attr.AutoFullDoc);
			for (Attr a : attrs) {
				if (a.getKey().equals(attr.getKey())) {
					continue;
				}

				doc = doc.replace("@" + a.getKey(), this.GetValStrByKey(a.getKey()).toString());
				doc = doc.replace("@" + a.getDesc(), this.GetValStrByKey(a.getKey()).toString());
			}

			try {
				java.math.BigDecimal d = DataType.ParseExpToDecimal(doc);
				this.SetValByKey(attr.getKey(), d);
			} catch (RuntimeException ex) {
				Log.DefaultLogWriteLineError("@(" + this.toString() + ")在处理自动计算{" + this.getEnDesc() + "}："
						+ this.getPK() + "=" + this.getPKVal() + "时，属性[" + attr.getKey() + "]，计算内容[" + doc + "]，出现错误："
						+ ex.getMessage());
				throw new RuntimeException("@(" + this.toString() + ")在处理自动计算{" + this.getEnDesc() + "}：" + this.getPK()
						+ "=" + this.getPKVal() + "时，属性[" + attr.getKey() + "]，计算内容[" + doc + "]，出现错误："
						+ ex.getMessage());
			}
		}

	}

	@Override
	public String toString() {
		return this.getClass().getName();
	}

	public final String ToJson() {
		return ToJson(true);
	}

	/**
	 * 把一个实体转化成Json.
	 * 
	 * @return 返回一个string json串.
	 */
	public final String ToJson(Boolean isInParaFields) {
		Hashtable ht = getRow();

		// 如果不包含参数字段.
		if (isInParaFields == false) {
			ht.put("EnName", this.toString());
			return BP.Tools.Json.ToJsonEntityModel(ht);
		}

		if (ht.containsKey("AtPara") == true) {
			/* 如果包含这个字段 */
			AtPara ap = getAtPara();
			for (String key : ap.getHisHT().keySet()) {
				ht.put(key, ap.getHisHT().get(key));
			}

			// 把参数属性移除.
			ht.remove("_ATObj_");
		}
		return BP.Tools.Json.ToJson(ht, false);
	}

	public final AtPara getatPara() {
		Object tempVar = this.getRow().GetValByKey("_ATObj_");
		AtPara at = (AtPara) ((tempVar instanceof AtPara) ? tempVar : null);
		if (at != null) {
			return at;
		}
		try {
			String atParaStr = this.GetValStringByKey("AtPara");
			if (DotNetToJavaStringHelper.isNullOrEmpty(atParaStr)) {
				// 没有发现数据，就执行初始化.
				this.InitParaFields();

				// 重新获取一次。
				atParaStr = this.GetValStringByKey("AtPara");
				if (DataType.IsNullOrEmpty(atParaStr)) {
					atParaStr = "";
				}

				at = new AtPara(atParaStr);
				this.SetValByKey("_ATObj_", at);
				return at;
			}
			at = new AtPara(atParaStr);
			this.SetValByKey("_ATObj_", at);
			return at;
		} catch (RuntimeException ex) {
			throw new RuntimeException(
					"@获取参数AtPara时出现异常" + ex.getMessage() + "，可能是您没有加入约定的参数字段AtPara. " + ex.getMessage());
		}
	}
	
	/**
	 * 把entity的实体属性调度到en里面去.
	 * @param fk_mapdata
	 * @return
	 */
	public MapData DTSMapToSys_MapData(String fk_mapdata){
		if(DataType.IsNullOrEmpty(fk_mapdata))
		{
			fk_mapdata = this.getClassIDOfShort();
		}
		
		Map map = this.getEnMap();
		
		//获得短的类名称.
		//region 更新主表信息.
		MapData md = new MapData();
		
		try {
			md.setNo(fk_mapdata);
			if (md.RetrieveFromDBSources() == 0)
				md.Insert();

			md.setEnPK(this.getPK());
			md.setEnsName(this.getClassID());
			md.setName(map.getEnDesc());
			md.setPTable(map.getPhysicsTable());
			md.Update();
			// endregion 更新主表信息.

			// 同步属性 mapattr.
			DTSMapToSys_MapData_InitMapAttr(map.getAttrs(), fk_mapdata);

			// region 同步从表.
			EnDtls dtls = map.getDtls();
			for (EnDtl dtl : dtls.ToJavaList()) {
				MapDtl mdtl = new MapDtl();

				Entity enDtl = dtl.getEns().getGetNewEntity();

				mdtl.setNo(enDtl.getClassIDOfShort());
				if (mdtl.RetrieveFromDBSources() == 0)
					mdtl.Insert();

				mdtl.setName(enDtl.getEnDesc());
				mdtl.setFK_MapData(fk_mapdata);
				mdtl.setPTable(enDtl.getEnMap().getPhysicsTable());

				mdtl.setRefPK(dtl.getRefKey()); // 关联的主键.

				mdtl.Update();

				// 同步字段.
				DTSMapToSys_MapData_InitMapAttr(enDtl.getEnMap().getAttrs(), enDtl.getClassIDOfShort());
				//endregion 同步从表.
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return md;
	}
	
	
	private void DTSMapToSys_MapData_InitMapAttr(Attrs attrs, String fk_mapdata)
	{
		for (Attr attr : attrs.ToJavaList())
        {
            if (attr.getIsRefAttr())
                continue;

            MapAttr mattr = new MapAttr();
            mattr.setKeyOfEn(attr.getKey());
            mattr.setFK_MapData(fk_mapdata);
            mattr.setMyPK(mattr.getFK_MapData() + "_" + mattr.getKeyOfEn());
            try {
				mattr.RetrieveFromDBSources();
			} catch (Exception e1) {
				e1.printStackTrace();
			}

            mattr.setName(attr.getDesc());
            mattr.setDefVal(attr.getDefaultVal().toString());
            mattr.setKeyOfEn(attr.getField());

            mattr.setMaxLen(attr.getMaxLength());
            mattr.setMinLen(attr.getMinLength());
            mattr.setUIBindKey(attr.getUIBindKey());
            mattr.setUIIsLine(attr.UIIsLine);
            mattr.setUIHeight(0);

            if (attr.getMaxLength() > 3000)
                mattr.setUIHeight(10);

            mattr.setUIWidth(attr.getUIWidth());
            mattr.setMyDataType(attr.getMyDataType());

            mattr.setUIRefKey(attr.getUIRefKeyValue());

            mattr.setUIRefKeyText(attr.getUIRefKeyText());
            mattr.setUIVisible(attr.getUIVisible());

            switch (attr.getMyFieldType())
            {
                case Enum:
                case PKEnum:
                    mattr.setUIContralType(attr.getUIContralType());
                    mattr.setLGType(FieldTypeS.Enum);
                    mattr.setUIIsEnable(attr.getUIIsReadonly());
                    break;
                case FK:
                case PKFK:
                    mattr.setUIContralType(attr.getUIContralType());
                    mattr.setLGType(FieldTypeS.FK);
                    //attr.MyDataType = (int)FieldType.FK;
                    mattr.setUIRefKey("No");
                    mattr.setUIRefKeyText("Name");
                    mattr.setUIIsEnable(attr.getUIIsReadonly());
                    break;
                default:
                    mattr.setUIContralType(UIContralType.TB);
                    mattr.setLGType(FieldTypeS.Normal);
                    mattr.setUIIsEnable(!attr.getUIIsReadonly());
                    switch (attr.getMyDataType())
                    {
                        case DataType.AppBoolean:
                            mattr.setUIContralType(UIContralType.CheckBok);
                            mattr.setUIIsEnable(attr.getUIIsReadonly());
                            break;
                        case DataType.AppDate:
                            //if (this.Tag == "1")
                            //    attr.DefaultVal = DataType.CurrentData;
                            break;
                        case DataType.AppDateTime:
                            //if (this.Tag == "1")
                            //    attr.DefaultVal = DataType.CurrentData;
                            break;
                        default:
                            break;
                    }
                    break;
            }
            try {
				if (mattr.Update() == 0)
				    mattr.Insert();
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
	}
	
	public String getClassIDOfShort(){
		String clsID  = this.getClassID();
		return clsID.substring(clsID.lastIndexOf('.')+1);
	}
	
}