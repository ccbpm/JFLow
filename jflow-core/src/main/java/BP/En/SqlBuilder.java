package BP.En;

import java.math.BigDecimal;

import BP.DA.DBAccess;
import BP.DA.DBType;
import BP.DA.DataType;
import BP.DA.Paras;
import BP.Difference.SystemConfig;
import BP.Sys.MapAttr;
import BP.Sys.SysDocFile;
import BP.Sys.SysEnums;
import BP.Tools.StringHelper;

public class SqlBuilder {

	/**
	 * 得到主健
	 * 
	 * @param en
	 * @return
	 * @throws Exception 
	 */
	public static String GetKeyConditionOfMS(Entity en) throws Exception {

		String pk = en.getPK();
		if (pk.equals("OID"))
			return en.getEnMap().getPhysicsTable() + ".OID=:OID";

		if (pk.equals("No"))
			return en.getEnMap().getPhysicsTable() + ".No=:No";

		if (pk.equals("MyPK"))
			return en.getEnMap().getPhysicsTable() + ".MyPK=:MyPK";

		if (pk.equals("NodeID"))
			return en.getEnMap().getPhysicsTable() + ".NodeID=:NodeID";

		if (pk.equals("WorkID"))
			return en.getEnMap().getPhysicsTable() + ".WorkID=:WorkID";

		String sql = " (1=1) ";
		Map enMap = en.getEnMap();
		Attrs attrs = enMap.getAttrs();
		String physicsTable = enMap.getPhysicsTable();
		for (Attr attr : attrs) {
			if (attr.getMyFieldType() == FieldType.PK || attr.getMyFieldType() == FieldType.PKFK
					|| attr.getMyFieldType() == FieldType.PKEnum) {
				if (attr.getMyDataType() == DataType.AppString) {
					String val = en.GetValByKey(attr.getKey()).toString();
					if (val == null || val.equals("")) {
						throw new RuntimeException("@在执行[" + en.getEnMap().getEnDesc() + " "
								+ physicsTable + "]没有给PK " + attr.getKey() + " 赋值,不能进行查询操作。");
					}
					sql = sql + " AND " +physicsTable+ "." + attr.getField() + "=:" + attr.getKey();
					continue;
				}
				if (attr.getMyDataType() == DataType.AppInt) {
					if (en.GetValIntByKey(attr.getKey()) == 0 && attr.getKey().equals("OID")) {
						throw new RuntimeException("@在执行[" + en.getEnMap().getEnDesc() + " "
								+ physicsTable + "]，没有给PK " + attr.getKey() + " 赋值,不能进行查询操作。");
					}
					sql = sql + " AND " + physicsTable + "." +attr.getField() + "=:" + attr.getKey();
					continue;
				}
			}
		}
		return sql;
	}

	/**
	 * 得到主健
	 * 
	 * @param en
	 * @return
	 * @throws Exception 
	 */
	public static String GetKeyConditionOfOLE(Entity en) throws Exception {
		// 判断特殊情况。
		Map enMap = en.getEnMap();
		Attrs attrs = enMap.getAttrs();
		if (attrs.Contains("OID")) {
			int key = en.GetValIntByKey("OID");
			if (key == 0) {
				throw new RuntimeException("@在执行[" + enMap.getEnDesc() + " " + enMap.getPhysicsTable()+ "]，没有给PK OID 赋值,不能进行查询操作。");
			}

			if (en.getPKs().length == 1) {
				return enMap.getPhysicsTable() + ".OID=" + key;
			}
		}

		String sql = " (1=1) ";
		String physicsTable = enMap.getPhysicsTable();
		for (Attr attr : attrs) {
			if (attr.getMyFieldType() == FieldType.PK || attr.getMyFieldType() == FieldType.PKFK
					|| attr.getMyFieldType() == FieldType.PKEnum) {
				if (attr.getMyDataType() == DataType.AppString) {
					String val = en.GetValByKey(attr.getKey()).toString();
					if (val == null || val.equals("")) {
						throw new RuntimeException("@在执行[" + enMap.getEnDesc() + " "
								+ physicsTable + "]没有给PK " + attr.getKey() + " 赋值,不能进行查询操作。");
					}
					sql = sql + " AND " + physicsTable + ".[" + attr.getField() + "]='"
							+ en.GetValByKey(attr.getKey()).toString() + "'";
					continue;
				}

				if (attr.getMyDataType() == DataType.AppInt) {
					if (en.GetValIntByKey(attr.getKey()) == 0 && attr.getKey().equals("OID")) {
						throw new RuntimeException("@在执行[" + enMap.getEnDesc() + " "
								+ physicsTable + "]，没有给PK " + attr.getKey() + " 赋值,不能进行查询操作。");
					}
					sql = sql + " AND " + physicsTable + ".[" + attr.getField() + "]="
							+ en.GetValStringByKey(attr.getKey());
					continue;
				}
			}
		}
		return sql;
	}

	/**
	 * 得到主健
	 * 
	 * @param en
	 * @return
	 * @throws Exception 
	 */
	public static String GenerWhereByPK(Entity en, String dbStr) throws Exception {

		if (en.getPKCount() == 1) {
			if (dbStr.equals("?")) {
				return en.getEnMap().getPhysicsTable() + "." + en.getPK() + "=" + dbStr;
			} else {
				return en.getEnMap().getPhysicsTable() + "." + en.getPK() + "=" + dbStr + en.getPK();
			}
		}

		String sql = " (1=1) ";
		Attrs attrs = en.getEnMap().getAttrs();
		for (Attr attr :attrs ) {
			if (attr.getMyFieldType() == FieldType.PK || attr.getMyFieldType() == FieldType.PKFK
					|| attr.getMyFieldType() == FieldType.PKEnum) {
				if (dbStr.equals("?")) {
					sql = sql + " AND " + attr.getField() + "=" + dbStr;
				} else {
					sql = sql + " AND " + attr.getField() + "=" + dbStr + attr.getField();
				}
			}
		}
		return sql;
	}

	/**
	 * @param en
	 * @return
	 * @throws Exception 
	 */
	public static Paras GenerParasPK(Entity en) throws Exception {

		Paras paras = new Paras();
		String pk = en.getPK();

		if (pk.equals("OID")) {
			paras.Add("OID", en.GetValIntByKey("OID"));
			return paras;
		}

		if (pk.equals("No")) {
			paras.Add("No", en.GetValStrByKey("No"));
			return paras;
		}

		if (pk.equals("MyPK")) {
			paras.Add("MyPK", en.GetValStrByKey("MyPK"));
			return paras;
		}
		if (pk.equals("NodeID")) {
			paras.Add("NodeID", en.GetValIntByKey("NodeID"));
			return paras;
		}

		if (pk.equals("WorkID")) {
			paras.Add("WorkID", en.GetValIntByKey("WorkID"));
			return paras;
		}
		
		Attrs attrs = en.getEnMap().getAttrs();
		for (Attr attr : attrs) {
			if (attr.getMyFieldType() == FieldType.PK || attr.getMyFieldType() == FieldType.PKFK
					|| attr.getMyFieldType() == FieldType.PKEnum) {
				if (attr.getMyDataType() == DataType.AppString) {
					paras.Add(attr.getKey(), en.GetValByKey(attr.getKey()).toString());
					continue;
				}

				if (attr.getMyDataType() == DataType.AppInt) {
					paras.Add(attr.getKey(), en.GetValIntByKey(attr.getKey()));
					continue;
				}
			}
		}
		return paras;
	}

	public static String GetKeyConditionOfOraForPara(Entity en) throws Exception {

		String pk = en.getPK();
		if (pk.equals("OID"))
			return en.getEnMap().getPhysicsTable() + ".OID=:OID";

		if (pk.equals("No"))
			return en.getEnMap().getPhysicsTable() + ".No=:No";

		if (pk.equals("MyPK"))
			return en.getEnMap().getPhysicsTable() + ".MyPK=:MyPK";

		if (pk.equals("NodeID"))
			return en.getEnMap().getPhysicsTable() + ".NodeID=:NodeID";

		if (pk.equals("WorkID"))
			return en.getEnMap().getPhysicsTable() + ".WorkID=:WorkID";

		String sql = " (1=1) ";

		Map enMap = en.getEnMap();
		Attrs attrs = enMap.getAttrs();
		String physicsTable = enMap.getPhysicsTable();
		for (Attr attr : attrs) {
			if (attr.getMyFieldType() == FieldType.PK || attr.getMyFieldType() == FieldType.PKFK
					|| attr.getMyFieldType() == FieldType.PKEnum) {
				if (attr.getMyDataType() == DataType.AppString) {
					sql = sql + " AND " + physicsTable + "." + attr.getField() + "=" + en.getHisDBVarStr()
							+ attr.getKey();
					continue;
				}
				if (attr.getMyDataType() == DataType.AppInt) {
					sql = sql + " AND " + physicsTable + "." + attr.getField() + "=" + en.getHisDBVarStr()
							+ attr.getKey();
					continue;
				}
			}
		}
		return sql.substring((new String(" (1=1)  AND ")).length());
	}

	public static String GetKeyConditionOfInformixForPara(Entity en) throws Exception {
		// 不能删除物理表名称，会引起未定义列。
		String pk = en.getPK();
		if (pk.equals("OID"))
			return en.getEnMap().getPhysicsTable() + ".OID=:OID";

		if (pk.equals("No"))
			return en.getEnMap().getPhysicsTable() + ".No=:No";

		if (pk.equals("MyPK"))
			return en.getEnMap().getPhysicsTable() + ".MyPK=:MyPK";

		if (pk.equals("NodeID"))
			return en.getEnMap().getPhysicsTable() + ".NodeID=:NodeID";

		if (pk.equals("WorkID"))
			return en.getEnMap().getPhysicsTable() + ".WorkID=:WorkID";

		String sql = " (1=1) ";
		Map enMap = en.getEnMap();
		Attrs attrs = enMap.getAttrs();
		String physicsTable = enMap.getPhysicsTable();
		for (Attr attr : attrs) {
			if (attr.getMyFieldType() == FieldType.PK || attr.getMyFieldType() == FieldType.PKFK
					|| attr.getMyFieldType() == FieldType.PKEnum) {
				if (attr.getMyDataType() == DataType.AppString) {
					sql = sql + " AND " + physicsTable + "." + attr.getField() + "=?";
					continue;
				}
				if (attr.getMyDataType() == DataType.AppInt) {
					sql = sql + " AND " + physicsTable + "." + attr.getField() + "=?";
					continue;
				}
			}
		}
		return sql.substring((new String(" (1=1)  AND ")).length());
	}

	public static String GetKeyConditionOfMySQL(Entity en) throws Exception {
		// 不能删除物理表名称，会引起未定义列。
		String pk = en.getPK();

		if (pk.equals("OID"))
			return en.getEnMap().getPhysicsTable() + ".OID=:OID";

		if (pk.equals("No"))
			return en.getEnMap().getPhysicsTable() + ".No=:No";

		if (pk.equals("MyPK"))
			return en.getEnMap().getPhysicsTable() + ".MyPK=:MyPK";

		if (pk.equals("NodeID"))
			return en.getEnMap().getPhysicsTable() + ".NodeID=:NodeID";

		if (pk.equals("WorkID"))
			return en.getEnMap().getPhysicsTable() + ".WorkID=:WorkID";

		String sql = " (1=1) ";
		Map enMap = en.getEnMap();
		Attrs attrs = enMap.getAttrs();
		String physicsTable = enMap.getPhysicsTable();
		for (Attr attr : attrs) {
			if (attr.getMyFieldType() == FieldType.PK || attr.getMyFieldType() == FieldType.PKFK
					|| attr.getMyFieldType() == FieldType.PKEnum) {
				if (attr.getMyDataType() == DataType.AppString) {
					sql = sql + " AND " + physicsTable + "." + attr.getField() + "=:" + attr.getField();
					continue;
				}
				if (attr.getMyDataType() == DataType.AppInt) {
					sql = sql + " AND " + physicsTable + "." + attr.getField() + "=:" + attr.getField();
					continue;
				}
			}
		}
		return sql.substring((new String(" (1=1)  AND ")).length());
	}

	/**
	 * 查询全部信息
	 * 
	 * @param en
	 *            实体
	 * @return sql
	 * @throws Exception
	 */
	public static String RetrieveAll(Entity en) throws Exception {
		return SqlBuilder.SelectSQL(en, SystemConfig.getTopNum());
	}

	/**
	 * 查询
	 * 
	 * @param en
	 *            实体
	 * @return string
	 * @throws Exception
	 */
	public static String Retrieve(Entity en) throws Exception {
		String sql = "";
		switch (SystemConfig.getAppCenterDBType()) {
		case MSSQL:
		case MySQL:
			sql = SqlBuilder.SelectSQLOfMS(en, 1) + "   AND ( " + SqlBuilder.GenerWhereByPK(en, ":") + " )";
			break;
		case Access:
			sql = SqlBuilder.SelectSQLOfOLE(en, 1) + "  AND ( " + SqlBuilder.GenerWhereByPK(en, ":") + " )";
			break;
		case Oracle:
		case Informix:
			sql = SqlBuilder.SelectSQLOfOra(en, 1) + "  AND ( " + SqlBuilder.GenerWhereByPK(en, ":") + " )";
			break;
		case DB2:
			throw new RuntimeException("还没有实现。");
		default:
			throw new RuntimeException("还没有实现。");
		}
		sql = sql.replace("WHERE   AND", " WHERE ");
		sql = sql.replace("WHERE  AND", " WHERE ");
		sql = sql.replace("WHERE AND", " WHERE ");
		return sql;
	}

	public static String RetrieveForPara(Entity en) throws Exception {
		String sql = null;
		switch (SystemConfig.getAppCenterDBType()) {
		case MSSQL:
			sql = SqlBuilder.SelectSQLOfMS(en, 1) + " AND " + SqlBuilder.GenerWhereByPK(en, ":");
			break;
		case MySQL:
			sql = SqlBuilder.SelectSQLOfMySQL(en, 1) + " AND " + SqlBuilder.GenerWhereByPK(en, ":");
			break;
		case Oracle:
			sql = SqlBuilder.SelectSQLOfOra(en, 1) + "AND (" + SqlBuilder.GenerWhereByPK(en, ":") + " )";
			break;
		case Informix:
			sql = SqlBuilder.SelectSQLOfInformix(en, 1) + " WHERE (" + SqlBuilder.GenerWhereByPK(en, "?") + " )";
			break;
		case Access:
			sql = SqlBuilder.SelectSQLOfOLE(en, 1) + " AND " + SqlBuilder.GenerWhereByPK(en, ":");
			break;
		case DB2:
		default:
			throw new RuntimeException("还没有实现。");
		}
		sql = sql.replace("WHERE  AND", "WHERE");
		sql = sql.replace("WHERE AND", "WHERE");
		return sql;
	}

	public static String GenerFormWhereOfOra(Entity en) throws Exception {
		Map enMap = en.getEnMap();
		String from = " FROM " + enMap.getPhysicsTable();

		if (enMap.getHisFKAttrs().size() == 0) {
			return from + " WHERE ";
		}

		String mytable = enMap.getPhysicsTable();
		from += ",";
		// 产生外键表列表。
		Attrs fkAttrs = enMap.getHisFKAttrs();
		for (Attr attr : fkAttrs) {
			if (attr == null || attr.getMyFieldType() == FieldType.RefText) {
				continue;
			}

			String fktable = attr.getHisFKEn().getEnMap().getPhysicsTable();

			fktable = fktable + " T" + attr.getKey();
			from += fktable + " ,";
		}

		from = from.substring(0, from.length() - 1);

		String where = " WHERE ";
		boolean isAddAnd = true;

		// 开始形成 外键 where.
		for (Attr attr : fkAttrs) {
			if (attr == null || attr.getMyFieldType() == FieldType.RefText) {
				continue;
			}

			Entity en1 = attr.getHisFKEn(); // ClassFactory.GetEns(attr.UIBindKey).GetNewEntity;
			String fktable = "T" + attr.getKey();

			if (!isAddAnd) {
				if (attr.getMyDataType() == DataType.AppString) {
					where += "(  " + mytable + "." + attr.getKey() + "=" + fktable + "."
							+ en1.getEnMap().GetFieldByKey(attr.getUIRefKeyValue()) + "  (+) )";
				} else {
					where += "(  " + mytable + "." + attr.getKey() + "=" + fktable + "."
							+ en1.getEnMap().GetFieldByKey(attr.getUIRefKeyValue()) + "  (+) )";
				}

				isAddAnd = true;
			} else {
				if (attr.getMyDataType() == DataType.AppString) {
					where += " AND (  " + mytable + "." + attr.getKey() + "=" + fktable + "."
							+ en1.getEnMap().GetFieldByKey(attr.getUIRefKeyValue()) + "  (+) )";
				} else {
					where += " AND (  " + mytable + "." + attr.getKey() + "=" + fktable + "."
							+ en1.getEnMap().GetFieldByKey(attr.getUIRefKeyValue()) + "  (+) )";
				}
			}
		}

		where = where.replace("WHERE  AND", "WHERE");
		where = where.replace("WHERE AND", "WHERE");
		return from + where;
	}

	public static String GenerFormWhereOfInformix(Entity en) throws Exception {
		Map enMap = en.getEnMap();
		String from = " FROM " + enMap.getPhysicsTable();
		String mytable = enMap.getPhysicsTable();
		Attrs fkAttrs = enMap.getHisFKAttrs();
		String where = "";
		MapAttr mapAttr = null;
		// 开始形成 外键 where.
		for (Attr attr : fkAttrs) {
			if (attr.getMyFieldType() == FieldType.RefText) {
				continue;
			}
			if (!attr.getIsFK())
				continue;
			mapAttr = attr.getToMapAttr();

			// 去除webservice填充DDL数据的类型
			if (mapAttr.getLGType() == FieldTypeS.Normal && mapAttr.getUIContralType() == UIContralType.DDL)
				continue;

			String fktable = attr.getHisFKEn().getEnMap().getPhysicsTable();
			Attr refAttr = attr.getHisFKEn().getEnMap().GetAttrByKey(attr.getUIRefKeyValue());
			try {
				if (DBAccess.IsExitsObject(fktable)) {

					where += " LEFT JOIN " + fktable + "  " + fktable + "_" + attr.getKey() + "  ON " + mytable + "."
							+ attr.getField() + "=" + fktable + "_" + attr.getField() + "." + refAttr.getField();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		where = where.replace("WHERE  AND", "WHERE");
		where = where.replace("WHERE AND", "WHERE");
		return from + where;
	}

	/**
	 * 生成sql.
	 * 
	 * @param en
	 * @return
	 * @throws Exception 
	 */
	public static String GenerCreateTableSQLOfMS(Entity en) throws Exception {
		if ( en.getEnMap().getPhysicsTable() == null || en.getEnMap().getPhysicsTable().equals("")) {
			return "DELETE FROM Sys_enum where enumkey='sdsf44a23'";
		}
		
		Map enMap = en.getEnMap();
		Attrs attrs = enMap.getAttrs();
		String sql = "CREATE TABLE  " + enMap.getPhysicsTable() + " ( ";

		if (attrs.size() == 0) {
			throw new RuntimeException("@" + en.getEnDesc() + " , 没有属性集合 attrs.size() = 0 ,能执行数据表的创建.");
		}

		for (Attr attr : attrs) {
			if (attr.getMyFieldType() == FieldType.RefText) {
				continue;
			}
			switch (attr.getMyDataType()) {
			case DataType.AppString:
			case DataType.AppDate:
			case DataType.AppDateTime:
				if (attr.getIsPK()) {
					sql += "[" + attr.getField() + "]  NVARCHAR (" + attr.getMaxLength() + ") NOT NULL,";
				} else {
					sql += "[" + attr.getField() + "]  NVARCHAR (" + attr.getMaxLength() + ") NULL,";
				}
				break;
			case DataType.AppRate:
			case DataType.AppFloat:
			case DataType.AppMoney:
				sql += "[" + attr.getField() + "] FLOAT NULL,";
				break;
			case DataType.AppBoolean:
			case DataType.AppInt:
				if (attr.getIsPK()) {
					try {
						// 说明这个是自动增长的列.
						if (attr.getUIBindKey().equals("1")) {
							sql += "[" + attr.getField() + "] INT  primary key identity(1,1),";
						} else {
							sql += "[" + attr.getField() + "] INT NOT NULL,";
						}
					} catch (Exception e) {
						sql += "[" + attr.getField() + "] INT NOT NULL,";
					}
				} else {
					sql += "[" + attr.getField() + "] INT  NULL,";
				}
				break;
			case DataType.AppDouble:
				sql += "[" + attr.getField() + "]  FLOAT  NULL,";
				break;
			default:
				break;
			}
		}
		sql = sql.substring(0, sql.length() - 1);
		sql += ")";
		return sql;
	}

	

	public static String GenerCreateTableSQL(Entity en) throws Exception {
		switch (DBAccess.getAppCenterDBType()) {
		case Oracle:
			return GenerCreateTableSQLOfOra_OK(en);
		case Informix:
			return GenerCreateTableSQLOfInfoMix(en);
		case MSSQL:
		case Access:
			return GenerCreateTableSQLOfMS(en);
		default:
			break;
		}
		return null;
	}

	public static String DeleteSysEnumsSQL(String table, String key) {
		switch (DBAccess.getAppCenterDBType()) {
		case Oracle:
			return "DELETE FROM " + table + " WHERE " + key + "=:p";
		case MySQL:
			return "DELETE FROM " + table + " WHERE " + key + "=:p";
		case MSSQL:
			return "DELETE FROM " + table + " WHERE " + key + "=:p";
		case Access:
			return "DELETE FROM " + table + " WHERE " + key + "=:p";
		default:
			break;
		}
		return null;
	}
	
	/*
	 * 生成sql.
	 * 
	 * @param en
	 * @return
	 */
	public static String GenerCreateTableSQLOfOra_OK(Entity en) throws Exception {
		if (en.getEnMap().getPhysicsTable() == null) {
			throw new RuntimeException("您没有为[" + en.getEnDesc() + "],设置物理表。");
		}

		if (en.getEnMap().getPhysicsTable().trim().length() == 0) {
			throw new RuntimeException("您没有为[" + en.getEnDesc() + "],设置物理表。");
		}
		
		Map enMap = en.getEnMap();
		Attrs attrs = enMap.getAttrs();
		
		String sql = "CREATE TABLE  " + enMap.getPhysicsTable() + " (";
		for (Attr attr : attrs) {
			if (attr.getMyFieldType() == FieldType.RefText) {
				continue;
			}

			switch (attr.getMyDataType()) {
			case DataType.AppString:
			case DataType.AppDate:
			case DataType.AppDateTime:
				if (attr.getIsPK()) {
					sql += attr.getField() + " varchar (" + attr.getMaxLength() + ") NOT NULL,";
				} else {
					sql += attr.getField() + " varchar (" + attr.getMaxLength() + ") NULL,";
				}
				break;
			case DataType.AppRate:
			case DataType.AppFloat:
			case DataType.AppMoney:
			case DataType.AppDouble:
				sql += attr.getField() + " float NULL,";
				break;
			case DataType.AppBoolean:
			case DataType.AppInt:

				if (attr.getIsPK()) {
					if ("1".equals(attr.getUIBindKey())) {
						sql += attr.getField() + " int  primary key identity(1,1),";
					} else {
						sql += attr.getField() + " int NOT NULL,";
					}
				} else {
					sql += attr.getField() + " int ,";
				}
				break;
			default:
				break;
			}
		}
		sql = sql.substring(0, sql.length() - 1);
		sql += ")";

		return sql;
	}

	public static String GenerCreateTableSQLOfInfoMix(Entity en) throws Exception {
		if (en.getEnMap().getPhysicsTable() == null) {
			throw new RuntimeException("您没有为[" + en.getEnDesc() + "],设置物理表。");
		}

		if (en.getEnMap().getPhysicsTable().trim().length() == 0) {
			throw new RuntimeException("您没有为[" + en.getEnDesc() + "],设置物理表。");
		}

		Map enMap = en.getEnMap();
		Attrs attrs = enMap.getAttrs();

		String sql = "CREATE TABLE  " + enMap.getPhysicsTable() + " (";
		for (Attr attr : attrs) {
			if (attr.getMyFieldType() == FieldType.RefText) {
				continue;
			}

			switch (attr.getMyDataType()) {
			case DataType.AppString:
			case DataType.AppDate:
			case DataType.AppDateTime:
				if (attr.getMaxLength() >= 255) {
					if (attr.getIsPK()) {
						sql += attr.getField() + " lvarchar (" + attr.getMaxLength() + "),";
					} else {
						sql += attr.getField() + " lvarchar (" + attr.getMaxLength() + "),";
					}
				} else {
					if (attr.getIsPK()) {
						sql += attr.getField() + " varchar (" + attr.getMaxLength() + ") NOT NULL,";
					} else {
						sql += attr.getField() + " varchar (" + attr.getMaxLength() + "),";
					}
				}
				break;
			case DataType.AppRate:
			case DataType.AppFloat:
			case DataType.AppMoney:
			case DataType.AppDouble:
				sql += attr.getField() + " float,";
				break;
			case DataType.AppBoolean:
			case DataType.AppInt:
				// 说明这个是自动增长的列.
				if (attr.getIsPK()) {
					if (attr.getUIBindKey().equals("1")) {
						sql += attr.getField() + "  Serial not null,";
					} else {
						sql += attr.getField() + " int8 NOT NULL,";
					}
				} else {
					sql += attr.getField() + " int8,";
				}
				break;
			default:
				break;
			}
		}

		sql = sql.substring(0, sql.length() - 1);
		sql += ")";

		return sql;
	}

	/**
	 * 生成sql.
	 * 
	 * @param en
	 * @return
	 * @throws Exception 
	 */
	public static String GenerCreateTableSQLOfMySQL(Entity en) throws Exception {
		if (en.getEnMap().getPhysicsTable() == null) {
			throw new RuntimeException("您没有为[" + en.getEnDesc() + "],设置物理表。");
		}

		if (en.getEnMap().getPhysicsTable().trim().length() == 0) {
			throw new RuntimeException("您没有为[" + en.getEnDesc() + ", " + en.toString() + "],设置物理表。");
		}
		
		Map enMap = en.getEnMap();
		Attrs attrs = enMap.getAttrs();
		String sql = "CREATE TABLE  " + enMap.getPhysicsTable() + " (";
		for (Attr attr : attrs) {
			if (attr.getMyFieldType() == FieldType.RefText) {
				continue;
			}

			switch (attr.getMyDataType()) {
			case DataType.AppString:
			case DataType.AppDate:
			case DataType.AppDateTime:
				if (attr.getIsPK()) {
					sql += attr.getField() + " NVARCHAR (" + attr.getMaxLength() + ") NOT NULL,";
				} else {
					if (attr.getMaxLength() > 3000) {
						sql += attr.getField() + " TEXT NULL,";
					} else {
						sql += attr.getField() + " NVARCHAR (" + attr.getMaxLength() + ") NULL,";
					}
				}
				break;
			case DataType.AppRate:
			case DataType.AppFloat:
				sql += attr.getField() + " float  NULL COMMENT '" + attr.getDesc() + "',";
				break;
			case DataType.AppMoney:
				sql += attr.getField() + " decimal(20,4)  NULL COMMENT '" + attr.getDesc() + "',";
				break;
			case DataType.AppDouble:
				sql += attr.getField() + " double  NULL COMMENT '" + attr.getDesc() + "',";
				break;
//			case DataType.AppFloat:
//			case DataType.AppMoney:
//			case DataType.AppDouble:
//				sql += attr.getField() + " float  NULL,";
//				break;
			case DataType.AppBoolean:
			case DataType.AppInt:
				if (attr.getIsPK()) {
					if ("1".equals(attr.getUIBindKey())) {
						sql += attr.getField() + " int(4) primary key not null auto_increment,";
					} else {
						sql += attr.getField() + " int   NULL,";
					}
				} else {
					sql += attr.getField() + " int ,";
				}
				break;
			default:
				break;
			}
		}
		sql = sql.substring(0, sql.length() - 1);
		sql += ") default charset=utf8 ";

		return sql;
	}

	public static String GenerFormWhereOfOra(Entity en, Map map) throws Exception {
		String from = " FROM " + map.getPhysicsTable();
		// string where=" ";
		String table = "";
		String tableAttr = "";
		String enTable = map.getPhysicsTable();

		for (Attr attr : map.getAttrs()) {
			if (attr.getMyFieldType() == FieldType.Normal || attr.getMyFieldType() == FieldType.PK
					|| attr.getMyFieldType() == FieldType.RefText) {
				continue;
			}

			if (attr.getMyFieldType() == FieldType.FK || attr.getMyFieldType() == FieldType.PKFK) {
				Entity en1 = attr.getHisFKEn();

				table = en1.getEnMap().getPhysicsTable();
				tableAttr = "T" + attr.getKey() + "";
				from = from + " LEFT OUTER JOIN " + table + "   " + tableAttr + " ON " + enTable + "." + attr.getField()
						+ "=" + tableAttr + "." + en1.getEnMap().GetFieldByKey(attr.getUIRefKeyValue());
				continue;
			}
			if (attr.getMyFieldType() == FieldType.Enum || attr.getMyFieldType() == FieldType.PKEnum) {
				tableAttr = "Enum_" + attr.getKey();
				from = from + " LEFT OUTER JOIN ( SELECT Lab, IntKey FROM Sys_Enum WHERE EnumKey='"
						+ attr.getUIBindKey() + "' )  Enum_" + attr.getKey() + " ON " + enTable + "." + attr.getField()
						+ "=" + tableAttr + ".IntKey ";

			}
		}

		return from + "  WHERE (1=1) ";
	}

	public static String GenerFormWhereOfMS(Entity en) throws Exception {
		Map enMap = en.getEnMap();
		String from = " FROM " + enMap.getPhysicsTable();

		if (enMap.getHisFKAttrs().size() == 0) {
			return from + " WHERE (1=1)";
		}

		String mytable = enMap.getPhysicsTable();
		Attrs fkAttrs = enMap.getAttrs();
		MapAttr mapAttr = null;
		for (Attr attr : fkAttrs) {

			if (attr.getMyFieldType() == FieldType.RefText) {
				continue;
			}
			if (!attr.getIsFK())
				continue;

			mapAttr = attr.getToMapAttr();

			// 去除webservice填充DDL数据的类型
			if (mapAttr.getLGType() == FieldTypeS.Normal && mapAttr.getUIContralType() == UIContralType.DDL)
				continue;

			String fktable = attr.getHisFKEn().getEnMap().getPhysicsTable();
			Attr refAttr = attr.getHisFKEn().getEnMap().GetAttrByKey(attr.getUIRefKeyValue());

			from += " LEFT JOIN " + fktable + " AS " + fktable + "_" + attr.getKey() + " ON " + mytable + "."
					+ attr.getField() + "=" + fktable + "_" + attr.getField() + "." + refAttr.getField();

		}
		return from + " WHERE (1=1) ";

		
	}

	/**
	 * GenerFormWhere
	 * 
	 * @param en
	 * @return
	 * @throws Exception 
	 */
	public static String GenerFormWhereOfMS(Entity en, Map map) throws Exception {
		String from = " FROM " + map.getPhysicsTable();
		String table = "";
		String tableAttr = "";
		String enTable = map.getPhysicsTable();
		for (Attr attr : map.getAttrs()) {
			if (attr.getMyFieldType() == FieldType.Normal || attr.getMyFieldType() == FieldType.PK
					|| attr.getMyFieldType() == FieldType.RefText) {
				continue;
			}

			if (attr.getMyFieldType() == FieldType.FK || attr.getMyFieldType() == FieldType.PKFK) {
				Entity en1 = attr.getHisFKEn();

				table = en1.getEnMap().getPhysicsTable();
				tableAttr = en1.getEnMap().getPhysicsTable() + "_" + attr.getKey();
				if (attr.getMyDataType() == DataType.AppInt) {
					from = from + " LEFT OUTER JOIN " + table + " AS " + tableAttr + " ON ISNULL( " + enTable + "."
							+ attr.getField() + ", " + en.GetValIntByKey(attr.getKey()) + ")=" + tableAttr + "."
							+ en1.getEnMap().GetFieldByKey(attr.getUIRefKeyValue());
				} else {
					from = from + " LEFT OUTER JOIN " + table + " AS " + tableAttr + " ON ISNULL( " + enTable + "."
							+ attr.getField() + ", '" + en.GetValByKey(attr.getKey()) + "')=" + tableAttr + "."
							+ en1.getEnMap().GetFieldByKey(attr.getUIRefKeyValue());
				}
				continue;
			}
			if (attr.getMyFieldType() == FieldType.Enum || attr.getMyFieldType() == FieldType.PKEnum) {
				tableAttr = "Enum_" + attr.getKey();
				from = from + " LEFT OUTER JOIN ( SELECT Lab, IntKey FROM Sys_Enum WHERE EnumKey='"
						+ attr.getUIBindKey() + "' )  Enum_" + attr.getKey() + " ON ISNULL( " + enTable + "."
						+ attr.getField() + ", " + en.GetValIntByKey(attr.getKey()) + ")=" + tableAttr + ".IntKey ";
			}
		}
		return from + "  WHERE (1=1) ";
	}

	/**
	 * GenerFormWhere
	 * 
	 * @param en
	 * @return
	 * @throws Exception 
	 */
	protected static String GenerFormWhereOfMSOLE(Entity en) throws Exception {
		Map enMap = en.getEnMap();
		String fromTop = enMap.getPhysicsTable();

		String from = "";
		// string where=" ";
		String table = "";
		String tableAttr = "";
		String enTable = enMap.getPhysicsTable();
		Attrs attrs = enMap.getAttrs();
		for (Attr attr : attrs) {
			if (attr.getMyFieldType() == FieldType.Normal || attr.getMyFieldType() == FieldType.PK
					|| attr.getMyFieldType() == FieldType.RefText) {
				continue;
			}
			fromTop = "(" + fromTop;

			if (attr.getMyFieldType() == FieldType.FK || attr.getMyFieldType() == FieldType.PKFK) {
				Entity en1 = attr.getHisFKEn();
				table = en1.getEnMap().getPhysicsTable();
				tableAttr = en1.getEnMap().getPhysicsTable() + "_" + attr.getKey();

				if (attr.getMyDataType() == DataType.AppInt) {
					from = from + " LEFT OUTER JOIN " + table + " AS " + tableAttr + " ON IIf( ISNULL( " + enTable + "."
							+ attr.getField() + "), " + en.GetValIntByKey(attr.getKey()) + " , " + enTable + "."
							+ attr.getField() + " )=" + tableAttr + "."
							+ en1.getEnMap().GetFieldByKey(attr.getUIRefKeyValue());
				} else {
					from = from + " LEFT OUTER JOIN " + table + " AS " + tableAttr + " ON IIf( ISNULL( " + enTable + "."
							+ attr.getField() + "), '" + en.GetValStringByKey(attr.getKey()) + "', " + enTable + "."
							+ attr.getField() + " )=" + tableAttr + "."
							+ en1.getEnMap().GetFieldByKey(attr.getUIRefKeyValue());
				}
			}
			if (attr.getMyFieldType() == FieldType.Enum || attr.getMyFieldType() == FieldType.PKEnum) {
				tableAttr = "Enum_" + attr.getKey();
				from = from + " LEFT OUTER JOIN ( SELECT Lab, IntKey FROM Sys_Enum WHERE EnumKey='"
						+ attr.getUIBindKey() + "' )  Enum_" + attr.getKey() + " ON IIf( ISNULL( " + enTable + "."
						+ attr.getField() + "), " + en.GetValIntByKey(attr.getKey()) + ", " + enTable + "."
						+ attr.getField() + ")=" + tableAttr + ".IntKey ";
			}

			from = from + ")";
		}
		fromTop = " FROM " + fromTop;
		return fromTop + from + "  WHERE (1=1) ";
	}

	protected static String SelectSQLOfOra(Entity en, int topNum) throws Exception {
		String val = ""; // key = null;
		String mainTable = "";
		
		Map enMap = en.getEnMap();
		
		if (enMap.getHisFKAttrs().size() != 0) {
			mainTable = enMap.getPhysicsTable() + ".";
		}
		
		Attrs attrs = enMap.getAttrs();
		for (Attr attr : attrs) {
			if (attr.getMyFieldType() == FieldType.RefText) {
				continue;
			}
			switch (attr.getMyDataType()) {
			case DataType.AppString:
				Object tempVar = attr.getDefaultVal();
				if ( tempVar == null || tempVar.equals("")) {
					if (attr.getIsKeyEqualField()) {
						val = val + "," + mainTable + attr.getField();
					} else {
						val = val + "," + mainTable + attr.getField() + "" + attr.getKey();
					}
				} else {
					val = val + ",NVL(" + mainTable + attr.getField() + ", '" + attr.getDefaultVal() + "') "
							+ attr.getKey();
				}

				if (attr.getMyFieldType() == FieldType.FK || attr.getMyFieldType() == FieldType.PKFK) {
					Map map = attr.getHisFKEn().getEnMap();
					val = val + ", T" + attr.getKey() + "." + map.GetFieldByKey(attr.getUIRefKeyText()) + " AS "
							+ attr.getKey() + "Text";
				}
				break;
			case DataType.AppInt:

				val = val + "," + mainTable + attr.getField() + " " + attr.getKey();

				if (attr.getMyFieldType() == FieldType.Enum || attr.getMyFieldType() == FieldType.PKEnum) {

					val = val + "," + BP.DA.Cash.getCaseWhenSQL(mainTable, attr.getKey(), attr.getField(),
							attr.getUIBindKey(), Integer.parseInt(attr.getDefaultVal().toString()), attr.UITag);
				}
				if (attr.getMyFieldType() == FieldType.FK || attr.getMyFieldType() == FieldType.PKFK) {
					Map map = attr.getHisFKEn().getEnMap();
					val = val + ", T" + attr.getKey() + "." + map.GetFieldByKey(attr.getUIRefKeyText()) + "  AS "
							+ attr.getKey() + "Text";
				}
				break;
			case DataType.AppFloat:
			case DataType.AppBoolean:
			case DataType.AppDouble:
			case DataType.AppMoney:
			case DataType.AppDate:
			case DataType.AppDateTime:
				val = val + "," + mainTable + attr.getField() + " " + attr.getKey();

				break;
			default:
				throw new RuntimeException(
						"@没有定义的数据类型! attr=" + attr.getKey() + " MyDataType =" + attr.getMyDataType());
			}
		}

		return " SELECT  " + val.substring(1) + SqlBuilder.GenerFormWhereOfOra(en);
	}

	/**
	 * SelectSQLOfInformix
	 * 
	 * @param en
	 * @param topNum
	 * @return
	 * @throws Exception
	 */
	protected static String SelectSQLOfInformix(Entity en, int topNum) throws Exception {
		String val = "";
		String mainTable = "";

		if (en.getEnMap().getHisFKAttrs().size() != 0) {
			mainTable = en.getEnMap().getPhysicsTable() + ".";
		}

		for (Attr attr : en.getEnMap().getAttrs()) {
			if (attr.getMyFieldType() == FieldType.RefText) {
				continue;
			}
			switch (attr.getMyDataType()) {
			case DataType.AppString:
				Object tempVar = attr.getDefaultVal();
				if (attr.getDefaultVal() == null || tempVar == null || tempVar.equals("")) {
					if (attr.getIsKeyEqualField()) {
						val = val + ", " + mainTable + attr.getField();
					} else {
						val = val + "," + mainTable + attr.getField() + " " + attr.getKey();
					}
				} else {
					val = val + ",NVL(" + mainTable + attr.getField() + ", '" + attr.getDefaultVal() + "') "
							+ attr.getKey();
				}

				if (attr.getMyFieldType() == FieldType.FK || attr.getMyFieldType() == FieldType.PKFK) {
					Map map = attr.getHisFKEn().getEnMap();
					val = val + ", " + map.getPhysicsTable() + "_" + attr.getKey() + "."
							+ map.GetFieldByKey(attr.getUIRefKeyText()) + " AS " + attr.getKey() + "Text";
				}
				break;
			case DataType.AppInt:

				val = val + ",NVL(" + mainTable + attr.getField() + "," + attr.getDefaultVal() + ")   " + attr.getKey()
						+ "";

				if (attr.getMyFieldType() == FieldType.Enum || attr.getMyFieldType() == FieldType.PKEnum) {
					if (StringHelper.isNullOrEmpty(attr.getUIBindKey())) {
						throw new RuntimeException(
								"@" + en.toString() + " key=" + attr.getKey() + " UITag=" + attr.UITag);
					}

					SysEnums ses = new BP.Sys.SysEnums(attr.getUIBindKey(), attr.UITag);
					val = val + "," + ses.GenerCaseWhenForOracle(en.toString(), mainTable, attr.getKey(),
							attr.getField(), attr.getUIBindKey(), Integer.parseInt(attr.getDefaultVal().toString()));
				}
				if (attr.getMyFieldType() == FieldType.FK || attr.getMyFieldType() == FieldType.PKFK) {
					Map map = attr.getHisFKEn().getEnMap();
					val = val + ", " + map.getPhysicsTable() + "_" + attr.getKey() + "."
							+ map.GetFieldByKey(attr.getUIRefKeyText()) + "  AS " + attr.getKey() + "Text";
				}
				break;
			case DataType.AppFloat:
				val = val + ", NVL( round(" + mainTable + attr.getField() + ",4) ," + attr.getDefaultVal().toString()
						+ ") AS  " + attr.getKey();
				break;
			case DataType.AppBoolean:
				if (attr.getDefaultVal().toString().equals("0")) {
					val = val + ", NVL( " + mainTable + attr.getField() + ",0) " + attr.getKey();
				} else {
					val = val + ", NVL(" + mainTable + attr.getField() + ",1) " + attr.getKey();
				}
				break;
			case DataType.AppDouble:
				val = val + ", NVL( round(" + mainTable + attr.getField() + " ,4) ," + attr.getDefaultVal().toString()
						+ ") " + attr.getKey();
				break;
			case DataType.AppMoney:
				val = val + ", NVL( round(" + mainTable + attr.getField() + ",4)," + attr.getDefaultVal().toString()
						+ ") " + attr.getKey();
				break;
			case DataType.AppDate:
			case DataType.AppDateTime:
				if (attr.getDefaultVal() == null || attr.getDefaultVal().toString().equals("")) {
					val = val + "," + mainTable + attr.getField() + " " + attr.getKey();
				} else {
					val = val + ",NVL(" + mainTable + attr.getField() + ",'" + attr.getDefaultVal().toString() + "') "
							+ attr.getKey();
				}
				break;
			default:
				throw new RuntimeException(
						"@没有定义的数据类型! attr=" + attr.getKey() + " MyDataType =" + attr.getMyDataType());
			}
		}
		return " SELECT  " + val.substring(1) + SqlBuilder.GenerFormWhereOfInformix(en);
	}

	public static String SelectSQL(Entity en, int topNum) throws Exception {
		switch (SystemConfig.getAppCenterDBType()) {
		case MSSQL:
			return SqlBuilder.SelectSQLOfMS(en, topNum);
		case MySQL:
			return SqlBuilder.SelectSQLOfMySQL(en, topNum);
		case Access:
			return SqlBuilder.SelectSQLOfOLE(en, topNum);
		case Oracle:
			return SqlBuilder.SelectSQLOfOra(en, topNum);
		case Informix:
			return SqlBuilder.SelectSQLOfInformix(en, topNum);
		default:
			throw new RuntimeException("没有判断的情况");
		}
	}

	/**
	 * 得到sql of select
	 * 
	 * @param en
	 *            实体
	 * @return sql
	 * @throws Exception
	 */
	public static String SelectCountSQL(Entity en) throws Exception {
		switch (SystemConfig.getAppCenterDBType()) {
		case MSSQL:
			return SqlBuilder.SelectCountSQLOfMS(en);
		case Access:
			return SqlBuilder.SelectSQLOfOLE(en, 0);
		case Oracle:
		case Informix:
			return SqlBuilder.SelectSQLOfOra(en, 0);
		default:
			return null;
		}
	}

	/**
	 * 建立SelectSQLOfOLE
	 * 
	 * @param en
	 *            要执行的en
	 * @param topNum
	 *            最高查询个数
	 * @return 返回查询sql
	 * @throws Exception 
	 */
	public static String SelectSQLOfOLE(Entity en, int topNum) throws Exception {
		String val = "";
		Map enMap = en.getEnMap();
		String mainTable = enMap.getPhysicsTable() + ".";
		Attrs attrs = enMap.getAttrs();
		for (Attr attr : attrs) {
			if (attr.getMyFieldType() == FieldType.RefText) {
				continue;
			}
			switch (attr.getMyDataType()) {
			case DataType.AppString:
				val = val + ", IIf( ISNULL(" + mainTable + attr.getField() + "), '" + attr.getDefaultVal().toString()
						+ "', " + mainTable + attr.getField() + " ) AS [" + attr.getKey() + "]";
				if (attr.getMyFieldType() == FieldType.FK || attr.getMyFieldType() == FieldType.PKFK) {
					Entity en1 = attr.getHisFKEn(); // ClassFactory.GetEns(attr.UIBindKey).GetNewEntity;
					val = val + ", IIf( ISNULL(" + en1.getEnMap().getPhysicsTable() + "_" + attr.getKey() + "."
							+ en1.getEnMap().GetFieldByKey(attr.getUIRefKeyText()) + ") ,'',"
							+ en1.getEnMap().getPhysicsTable() + "_" + attr.getKey() + "."
							+ en1.getEnMap().GetFieldByKey(attr.getUIRefKeyText()) + ") AS " + attr.getKey() + "Text";
				}
				break;
			case DataType.AppInt:
				val = val + ",IIf( ISNULL(" + mainTable + attr.getField() + ")," + attr.getDefaultVal().toString() + ","
						+ mainTable + attr.getField() + ") AS [" + attr.getKey() + "]";
				if (attr.getMyFieldType() == FieldType.Enum || attr.getMyFieldType() == FieldType.PKEnum) {
					val = val + ",IIf( ISNULL( Enum_" + attr.getKey() + ".Lab),'',Enum_" + attr.getKey() + ".Lab ) AS "
							+ attr.getKey() + "Text";
				}
				if (attr.getMyFieldType() == FieldType.FK || attr.getMyFieldType() == FieldType.PKFK) {
					Entity en1 = attr.getHisFKEn(); // ClassFactory.GetEns(attr.UIBindKey).GetNewEntity;
					val = val + ", IIf( ISNULL(" + en1.getEnMap().getPhysicsTable() + "_" + attr.getKey() + "."
							+ en1.getEnMap().GetFieldByKey(attr.getUIRefKeyText()) + "),0 ,"
							+ en1.getEnMap().getPhysicsTable() + "_" + attr.getKey() + "."
							+ en1.getEnMap().GetFieldByKey(attr.getUIRefKeyText()) + ") AS " + attr.getKey() + "Text";
				}
				break;
			case DataType.AppFloat:
				val = val + ",IIf( ISNULL( Round(" + mainTable + attr.getField() + ",2) ),"
						+ attr.getDefaultVal().toString() + "," + mainTable + attr.getField() + ") AS [" + attr.getKey()
						+ "]";
				break;
			case DataType.AppBoolean:
				if (attr.getDefaultVal().toString().equals("0")) {
					val = val + ", IIf( ISNULL(" + mainTable + attr.getField() + "),0 ," + mainTable + attr.getField()
							+ ") AS [" + attr.getKey() + "]";
				} else {
					val = val + ",IIf( ISNULL(" + mainTable + attr.getField() + "),1," + mainTable + attr.getField()
							+ ") AS [" + attr.getKey() + "]";
				}
				break;
			case DataType.AppDouble:
				val = val + ", IIf(ISNULL( Round(" + mainTable + attr.getField() + ",4) ),"
						+ attr.getDefaultVal().toString() + "," + mainTable + attr.getField() + ") AS [" + attr.getKey()
						+ "]";
				break;
			case DataType.AppMoney:
				val = val + ",IIf( ISNULL(  Round(" + mainTable + attr.getField() + ",2) ),"
						+ attr.getDefaultVal().toString() + "," + mainTable + attr.getField() + ") AS [" + attr.getKey()
						+ "]";
				break;
			case DataType.AppDate:
				val = val + ", IIf(ISNULL( " + mainTable + attr.getField() + "), '" + attr.getDefaultVal().toString()
						+ "'," + mainTable + attr.getField() + ") AS [" + attr.getKey() + "]";
				break;
			case DataType.AppDateTime:
				val = val + ", IIf(ISNULL(" + mainTable + attr.getField() + "), '" + attr.getDefaultVal().toString()
						+ "'," + mainTable + attr.getField() + ") AS [" + attr.getKey() + "]";
				break;
			default:
				throw new RuntimeException(
						"@没有定义的数据类型! attr=" + attr.getKey() + " MyDataType =" + attr.getMyDataType());
			}
		}
		if (topNum == -1 || topNum == 0) {
			topNum = 99999;
		}

		return " SELECT TOP " + (new Integer(topNum)).toString() + " " + val.substring(1)
				+ SqlBuilder.GenerFormWhereOfMSOLE(en);
	}

	public static String SelectSQLOfMS(Entity en, int topNum) throws Exception {
		String val = ""; // key = null;
		String mainTable = "";

		Map enMap = en.getEnMap();

		if (enMap.getHisFKAttrs().size() != 0) {
			mainTable = enMap.getPhysicsTable() + ".";
		}
		Attrs atts = enMap.getAttrs();
		for (Attr attr : atts) {
			if (attr.getMyFieldType() == FieldType.RefText)
				continue;

			switch (attr.getMyDataType()) {
			case DataType.AppString:
				Object tempVar = attr.getDefaultVal();
				if (attr.getDefaultVal() == null || tempVar == null || tempVar.equals("")) {
					if (attr.getIsKeyEqualField()) {
						val = val + ", " + mainTable + attr.getField();
					} else {
						val = val + "," + mainTable + attr.getField() + " " + attr.getKey();
					}
				} else {
					val = val + ",ISNULL(" + mainTable + attr.getField() + ", '" + attr.getDefaultVal() + "') "
							+ attr.getKey();
				}

				if (attr.getMyFieldType() == FieldType.FK || attr.getMyFieldType() == FieldType.PKFK) {
					Map map = attr.getHisFKEn().getEnMap();
					val = val + ", " + map.getPhysicsTable() + "_" + attr.getKey() + "."
							+ map.GetFieldByKey(attr.getUIRefKeyText()) + " AS " + attr.getKey() + "Text";
				}
			 
				break;
			case DataType.AppInt:
				val = val + "," + mainTable + attr.getField() + " " + attr.getKey() + "";

				if (attr.getMyFieldType() == FieldType.Enum || attr.getMyFieldType() == FieldType.PKEnum) {
					val = val + "," + BP.DA.Cash.getCaseWhenSQL(mainTable, attr.getKey(), attr.getField(),
							attr.getUIBindKey(), Integer.parseInt(attr.getDefaultVal().toString()), attr.UITag);
				}

				if (attr.getMyFieldType() == FieldType.FK || attr.getMyFieldType() == FieldType.PKFK) {
					if (attr.getHisFKEns() == null)
						throw new Exception(
								"@生成SQL错误 Entity=" + en.toString() + " 外键字段｛" + attr.getKey() + "." + attr.getDesc()
										+ ", UIBindKey=" + attr.getUIBindKey() + "｝已经无效, 也许该类或者外键字段被移除，请通知管理员解决。");

					Map map = attr.getHisFKEn().getEnMap();
					val = val + ", " + map.getPhysicsTable() + "_" + attr.getKey() + "."
							+ map.GetFieldByKey(attr.getUIRefKeyText()) + "  AS " + attr.getKey() + "Text";
				}

				 
				break;
			case DataType.AppFloat:
			case DataType.AppDouble:
			case DataType.AppMoney:
			case DataType.AppBoolean:
			case DataType.AppDate:
			case DataType.AppDateTime:
				val = val + "," + mainTable + attr.getField() + " " + attr.getKey();
				break;
			default:
				throw new RuntimeException(
						"@没有定义的数据类型! attr=" + attr.getKey() + " MyDataType =" + attr.getMyDataType());
			}
		}

		if (topNum == -1 || topNum == 0) {
			topNum = 99999;
		}
		return " SELECT  TOP " + (new Integer(topNum)).toString() + " " + val.substring(1)
				+ SqlBuilder.GenerFormWhereOfMS(en);
	}

	public static String SelectSQLOfMySQL(Entity en, int topNum) throws Exception {

		String val = ""; // key = null;
		String mainTable = "";

		Map enMap = en.getEnMap();

		if (enMap.getHisFKAttrs().size() != 0) {
			mainTable = enMap.getPhysicsTable() + ".";
		}

		Attrs attrs = enMap.getAttrs();

		for (Attr attr : attrs) {
			if (attr.getMyFieldType() == FieldType.RefText)
				continue;

			switch (attr.getMyDataType()) {
			case DataType.AppString:
				Object tempVar = attr.getDefaultVal();
				if (tempVar == null || tempVar.equals("")) {

					if (attr.getIsKeyEqualField()) {
						val = val + ", " + mainTable + attr.getField();
					} else {
						val = val + "," + mainTable + attr.getField() + " " + attr.getKey();
					}

				} else {
					val = val + ",IFNULL(" + mainTable + attr.getField() + ", '" + attr.getDefaultVal() + "') "
							+ attr.getKey();
				}

				if (attr.getMyFieldType() == FieldType.FK || attr.getMyFieldType() == FieldType.PKFK) {
					Map map = attr.getHisFKEn().getEnMap();

					// @xu 删除了一段代码.
					val = val + ", " + map.getPhysicsTable() + "_" + attr.getKey() + "."
							+ map.GetFieldByKey(attr.getUIRefKeyText()) + " AS " + attr.getKey() + "Text";

				}
			 
				break;
			case DataType.AppInt:
				val = val + "," + mainTable + attr.getField() + " " + attr.getKey();

				if (attr.getMyFieldType() == FieldType.Enum || attr.getMyFieldType() == FieldType.PKEnum) {

					val = val + "," + BP.DA.Cash.getCaseWhenSQL(mainTable, attr.getKey(), attr.getField(),
							attr.getUIBindKey(), Integer.parseInt(attr.getDefaultVal().toString()), attr.UITag);

				}

				if (attr.getMyFieldType() == FieldType.FK || attr.getMyFieldType() == FieldType.PKFK) {

					if (attr.getHisFKEns() == null)
						throw new Exception(
								"@生成SQL错误 Entity=" + en.toString() + " 外键字段｛" + attr.getKey() + "." + attr.getDesc()
										+ ", UIBindKey=" + attr.getUIBindKey() + "｝已经无效, 也许该类或者外键字段被移除，请通知管理员解决。");

					Map map = attr.getHisFKEn().getEnMap();
					val = val + ", " + map.getPhysicsTable() + "_" + attr.getKey() + "."
							+ map.GetFieldByKey(attr.getUIRefKeyText()) + "  AS " + attr.getKey() + "Text";
				}
				 
				break;
			case DataType.AppFloat:
			case DataType.AppBoolean:
			case DataType.AppDouble:
			case DataType.AppMoney:
			case DataType.AppDate:
			case DataType.AppDateTime:
				val = val + "," + mainTable + attr.getField() + " " + attr.getKey();
				break;
			default:
				throw new RuntimeException(
						"@没有定义的数据类型! attr=" + attr.getKey() + " MyDataType =" + attr.getMyDataType());
			}
		}

		return " SELECT   " + val.substring(1) + SqlBuilder.GenerFormWhereOfMS(en);
	}


	/**
	 * 建立selectSQL
	 * 
	 * @param en
	 *            要执行的en
	 * @return 返回查询sql
	 * @throws Exception 
	 */
	public static String SelectCountSQLOfMS(Entity en) throws Exception {
		// 判断内存里面是否有 此sql.
		String sql = "SELECT COUNT(*) ";
		return sql + SqlBuilder.GenerFormWhereOfMS(en, en.getEnMap());
	}

	public static String SelectSQLOfOra(String enName, Map map) throws Exception {
		String val = ""; // key = null;
		String mainTable = map.getPhysicsTable() + ".";
		for (Attr attr : map.getAttrs()) {
			if (attr.getMyFieldType() == FieldType.RefText) {
				continue;
			}

			switch (attr.getMyDataType()) {
			case DataType.AppString:
				Object tempVar = attr.getDefaultVal();
				if (attr.getDefaultVal() == null || tempVar == null || tempVar.equals("")) {
					if (attr.getIsKeyEqualField()) {
						val = val + "," + mainTable + attr.getField();
					} else {
						val = val + "," + mainTable + attr.getField() + "" + attr.getKey();
					}
				} else {
					val = val + ",NVL(" + mainTable + attr.getField() + ", '" + attr.getDefaultVal() + "') "
							+ attr.getKey();
				}

				if (attr.getMyFieldType() == FieldType.FK || attr.getMyFieldType() == FieldType.PKFK) {
					Entity en1 = attr.getHisFKEn();
					val = val + ", T" + attr.getKey() + "." + en1.getEnMap().GetFieldByKey(attr.getUIRefKeyText())
							+ " AS " + attr.getKey() + "Text";
				}
				break;
			case DataType.AppInt:
				val = val + ", NVL(" + mainTable + attr.getField() + "," + attr.getDefaultVal() + ") AS  "
						+ attr.getKey() + "";
				if (attr.getMyFieldType() == FieldType.Enum || attr.getMyFieldType() == FieldType.PKEnum) {
					val = val + "," + BP.DA.Cash.getCaseWhenSQL(mainTable, attr.getKey(), attr.getField(),
							attr.getUIBindKey(), Integer.parseInt(attr.getDefaultVal().toString()), attr.UITag);
				}
				if (attr.getMyFieldType() == FieldType.FK || attr.getMyFieldType() == FieldType.PKFK) {
					Map map1 = attr.getHisFKEn().getEnMap();
					val = val + ", T" + attr.getKey() + "." + map1.GetFieldByKey(attr.getUIRefKeyText()) + "  AS "
							+ attr.getKey() + "Text";
				}
				break;
			case DataType.AppFloat:
			case DataType.AppBoolean:
			case DataType.AppDouble:
			case DataType.AppMoney:
			case DataType.AppDate:
			case DataType.AppDateTime:
				val = val + "," + mainTable + attr.getField() + " AS " + attr.getKey();
				break;
			default:
				throw new RuntimeException(
						"@没有定义的数据类型! attr=" + attr.getKey() + " MyDataType =" + attr.getMyDataType());
			}
		}
		return "SELECT " + val.substring(1);
	}

	/**
	 * 产生sql
	 * 
	 * @return
	 * @throws Exception 
	 */
	public static String SelectSQLOfMS(Map map) throws Exception {
		String val = ""; // key = null;
		String mainTable = map.getPhysicsTable() + ".";
		for (Attr attr : map.getAttrs()) {
			if (attr.getMyFieldType() == FieldType.RefText) {
				continue;
			}
			switch (attr.getMyDataType()) {
			case DataType.AppString:
				if (attr.getDefaultVal() == null) {
					val = val + " " + mainTable + attr.getField() + " AS [" + attr.getKey() + "]";
				} else {
					val = val + ",ISNULL(" + mainTable + attr.getField() + ", '" + attr.getDefaultVal().toString()
							+ "') AS [" + attr.getKey() + "]";
				}

				if (attr.getMyFieldType() == FieldType.FK || attr.getMyFieldType() == FieldType.PKFK) {
					Entity en1 = attr.getHisFKEn();
					val = val + "," + en1.getEnMap().getPhysicsTable() + "_" + attr.getKey() + "."
							+ en1.getEnMap().GetFieldByKey(attr.getUIRefKeyText()) + " AS " + attr.getKey() + "Text";
				}
				break;
			case DataType.AppInt:
				if (attr.getIsNull()) {
					// 如果允许为空
					val = val + ", " + mainTable + attr.getField() + " AS [" + attr.getKey() + "]";
				} else {
					val = val + ", ISNULL(" + mainTable + attr.getField() + "," + attr.getDefaultVal().toString()
							+ ") AS [" + attr.getKey() + "]";
					if (attr.getMyFieldType() == FieldType.Enum || attr.getMyFieldType() == FieldType.PKEnum) {
						val = val + ", Enum_" + attr.getKey() + ".Lab  AS " + attr.getKey() + "Text";
					}
					if (attr.getMyFieldType() == FieldType.FK || attr.getMyFieldType() == FieldType.PKFK) {
						Entity en1 = attr.getHisFKEn();
						val = val + ", " + en1.getEnMap().getPhysicsTable() + "_" + attr.getKey() + "."
								+ en1.getEnMap().GetFieldByKey(attr.getUIRefKeyText()) + " AS " + attr.getKey()
								+ "Text";
					}
				}
				break;
			case DataType.AppFloat:
			case DataType.AppBoolean:
			case DataType.AppDouble:
			case DataType.AppMoney:
			case DataType.AppDate:
			case DataType.AppDateTime:
				val = val + ", " + mainTable + attr.getField() + " AS [" + attr.getKey() + "]";
				break;
			default:
				if (attr.getKey().equals("MyNum")) {
					val = val + ", COUNT(*)  AS MyNum ";
					break;
				} else {
					throw new RuntimeException(
							"@没有定义的数据类型! attr=" + attr.getKey() + " MyDataType =" + attr.getMyDataType());
				}
			}
		}
		return "SELECT TOP @TopNum " + val.substring(1);
	}

	public static String UpdateOfMSAccess(Entity en, String[] keys) throws Exception {
		String val = "";
		Attrs attrs = en.getEnMap().getAttrs();
		for (Attr attr : attrs) {

			if (keys != null) {
				boolean isHave = false;
				for (String s : keys) {
					if (attr.getKey().equals(s)) {
						// 如果找到了要更新的key
						isHave = true;
						break;
					}
				}
				if (!isHave) {
					continue;
				}
			}

			if (attr.getMyFieldType() == FieldType.RefText || attr.getMyFieldType() == FieldType.PK
					|| attr.getMyFieldType() == FieldType.PKFK || attr.getMyFieldType() == FieldType.PKEnum) {
				continue;
			}

			switch (attr.getMyDataType()) {
			case DataType.AppString:
				val = val + ",[" + attr.getField() + "]='" + en.GetValStringByKey(attr.getKey()) + "'";
				break;
			case DataType.AppInt:
				val = val + ",[" + attr.getField() + "]=" + en.GetValStringByKey(attr.getKey());
				break;
			case DataType.AppFloat:
			case DataType.AppDouble:
			case DataType.AppMoney:
				String str = en.GetValStringByKey(attr.getKey()).toString();
				str = str.replace("￥", "");
				str = str.replace(",", "");
				val = val + ",[" + attr.getField() + "]=" + str;
				break;
			case DataType.AppBoolean:
				val = val + ",[" + attr.getField() + "]=" + en.GetValStringByKey(attr.getKey());
				break;
			case DataType.AppDate: // 如果是日期类型。
			case DataType.AppDateTime:
				String da = en.GetValStringByKey(attr.getKey());
				if (da.indexOf("_DATE") == -1) {
					val = val + ",[" + attr.getField() + "]='" + da + "'";
				} else {
					val = val + ",[" + attr.getField() + "]=" + da;
				}
				break;
			default:
				throw new RuntimeException("@SqlBulider.update, 没有这个数据类型");
			}
		}

		String sql = "";

		if (val.equals("")) {
			// 如果没有出现要更新.
			for (Attr attr : attrs) {
				if (keys != null) {
					boolean isHave = false;
					for (String s : keys) {
						if (attr.getKey().equals(s)) {
							// 如果找到了要更新的key
							isHave = true;
							break;
						}
					}
					if (!isHave) {
						continue;
					}
				}
				switch (attr.getMyDataType()) {
				case DataType.AppString:
					val = val + ",[" + attr.getField() + "]='" + en.GetValStringByKey(attr.getKey()) + "'";
					break;
				case DataType.AppInt:
					val = val + ",[" + attr.getField() + "]=" + en.GetValStringByKey(attr.getKey());
					break;
				case DataType.AppFloat:
				case DataType.AppDouble:
				case DataType.AppMoney:
					String str = en.GetValStringByKey(attr.getKey()).toString();
					str = str.replace("￥", "");
					str = str.replace(",", "");
					val = val + ",[" + attr.getField() + "]=" + str;
					break;
				case DataType.AppBoolean:
					val = val + ",[" + attr.getField() + "]=" + en.GetValStringByKey(attr.getKey());
					break;
				case DataType.AppDate: // 如果是日期类型。
				case DataType.AppDateTime:
					String da = en.GetValStringByKey(attr.getKey());
					if (da.indexOf("_DATE") == -1) {
						val = val + ",[" + attr.getField() + "]='" + da + "'";
					} else {
						val = val + ",[" + attr.getField() + "]=" + da;
					}
					break;
				default:
					throw new RuntimeException("@SqlBulider.update, 没有这个数据类型");
				}
			}
			// return null;
			// throw new Exception("出现了一个不合理的更新：没有要更新的数据。");
		}

		if (val.equals("")) {
			String ms = "";
			for (String str : keys) {
				ms += str;
			}
			throw new RuntimeException(en.getEnDesc() + "执行更新错误：无效的属性[" + ms + "]对于本实体来说。");
		}
		sql = "UPDATE " + en.getEnMap().getPhysicsTable() + " SET " + val.substring(1) + " WHERE "
				+ SqlBuilder.GetKeyConditionOfOLE(en);
		return sql.replace(",=''", "");
	}

	/**
	 * 产生要更新的sql 语句
	 * 
	 * @param en
	 *            要更新的entity
	 * @param keys
	 *            要更新的keys
	 * @return sql
	 * @throws Exception 
	 */
	public static String Update(Entity en, String[] keys) throws Exception {
		Map map = en.getEnMap();
		if (map.getAttrs().size() == 0) {
			throw new RuntimeException("@实体：" + en.toString() + " ,Attrs属性集合信息丢失，导致无法生成SQL。");
		}

		String val = "";
		for (Attr attr : map.getAttrs()) {
			if (keys != null) {
				boolean isHave = false;
				for (String s : keys) {
					if (attr.getKey().equals(s)) {
						// 如果找到了要更新的key
						isHave = true;
						break;
					}
				}
				if (!isHave) {
					continue;
				}
			}

			if (attr.getMyFieldType() == FieldType.RefText) {
				continue;
			}

			// 有可能是两个主键的情况。
			// if (attr.IsPK)
			// continue;

			switch (attr.getMyDataType()) {
			case DataType.AppString:
				if (map.getEnDBUrl().getDBType() == DBType.Access) {
					val = val + ",[" + attr.getField() + "]='" + en.GetValStringByKey(attr.getKey()).replace('\'', '~')
							+ "'";
				} else {
					if (attr.getUIIsDoc() && attr.getKey().equals("Doc")) {

						String doc = en.GetValStringByKey(attr.getKey());
						if (map.getAttrs().Contains("DocLength")) {
							en.SetValByKey("DocLength", doc.length());
						}

						if (doc.length() >= 2000) {
							SysDocFile.SetValV2(en.toString(), en.getPKVal().toString(), doc);
							val = val + "," + attr.getField() + "=''";
						} else {
							val = val + "," + attr.getField() + "='"
									+ en.GetValStringByKey(attr.getKey()).replace('\'', '~') + "'";
						}
					} else {
						val = val + "," + attr.getField() + "='"
								+ en.GetValStringByKey(attr.getKey()).replace('\'', '~') + "'";
					}
				}
				break;
			case DataType.AppInt:
				val = val + "," + attr.getField() + "=" + en.GetValStringByKey(attr.getKey());
				break;
			case DataType.AppFloat:
			case DataType.AppDouble:
			case DataType.AppMoney:
				String str = en.GetValStringByKey(attr.getKey()).toString();
				str = str.replace("￥", "");
				str = str.replace(",", "");
				val = val + "," + attr.getField() + "=" + str;
				break;
			case DataType.AppBoolean:
				val = val + "," + attr.getField() + "=" + en.GetValStringByKey(attr.getKey());
				break;
			case DataType.AppDate: // 如果是日期类型。
			case DataType.AppDateTime:
				String da = en.GetValStringByKey(attr.getKey());
				if (da.indexOf("_DATE") == -1) {
					val = val + "," + attr.getField() + "='" + da + "'";
				} else {
					val = val + "," + attr.getField() + "=" + da;
				}
				break;
			default:
				throw new RuntimeException("@SqlBulider.update, 没有这个数据类型");
			}
		}

		String sql = "";

		if (val.equals("")) {
			// 如果没有出现要更新.
			for (Attr attr : map.getAttrs()) {
				if (keys != null) {
					boolean isHave = false;
					for (String s : keys) {
						if (attr.getKey().equals(s)) {
							// 如果找到了要更新的key
							isHave = true;
							break;
						}
					}
					if (!isHave) {
						continue;
					}
				}

				// 两个PK 的情况。
				// if (attr.IsPK)
				// continue;

				switch (attr.getMyDataType()) {
				case DataType.AppString:
					val = val + "," + attr.getField() + "='" + en.GetValStringByKey(attr.getKey()) + "'";
					break;
				case DataType.AppInt:
					val = val + "," + attr.getField() + "=" + en.GetValStringByKey(attr.getKey());
					break;
				case DataType.AppFloat:
				case DataType.AppDouble:
				case DataType.AppMoney:
					String str = en.GetValStringByKey(attr.getKey()).toString();
					str = str.replace("￥", "");
					str = str.replace(",", "");
					val = val + "," + attr.getField() + "=" + str;
					break;
				case DataType.AppBoolean:
					val = val + "," + attr.getField() + "=" + en.GetValStringByKey(attr.getKey());
					break;
				case DataType.AppDate: // 如果是日期类型。
				case DataType.AppDateTime:
					String da = en.GetValStringByKey(attr.getKey());
					if (da.indexOf("_DATE") == -1) {
						val = val + "," + attr.getField() + "='" + da + "'";
					} else {
						val = val + "," + attr.getField() + "=" + da;
					}
					break;
				default:
					throw new RuntimeException("@SqlBulider.update, 没有这个数据类型");
				}
			}
			// return null;
			// throw new Exception("出现了一个不合理的更新：没有要更新的数据。");
		}

		if (val.equals("")) {
			String ms = "";
			for (String str : keys) {
				ms += str;
			}
			throw new RuntimeException(en.getEnDesc() + "执行更新错误：无效的属性[" + ms + "]对于本实体来说。");
		}

		switch (SystemConfig.getAppCenterDBType()) {
		case MSSQL:
		case Access:
			sql = "UPDATE " + en.getEnMap().getPhysicsTable() + " SET " + val.substring(1) + " WHERE "
					+ SqlBuilder.GenerWhereByPK(en, ":");
			break;
		case MySQL:
			sql = "UPDATE " + en.getEnMap().getPhysicsTable() + " SET " + val.substring(1) + " WHERE "
					+ SqlBuilder.GenerWhereByPK(en, "?");
			break;
		case Oracle:
			sql = "UPDATE " + en.getEnMap().getPhysicsTable() + " SET " + val.substring(1) + " WHERE "
					+ SqlBuilder.GenerWhereByPK(en, ":");
			break;
		case Informix:
			sql = "UPDATE " + en.getEnMap().getPhysicsTable() + " SET " + val.substring(1) + " WHERE "
					+ SqlBuilder.GenerWhereByPK(en, ":");
			break;
		default:
			throw new RuntimeException("no this case db type . ");
		}
		return sql.replace(",=''", "");
	}

	public static Paras GenerParas_Update_Informix(Entity en, String[] keys) throws Exception {
		if (keys == null) {
			return GenerParas_Update_Informix(en);
		}

		String mykeys = "@";
		for (String key : keys) {
			mykeys += key + "@";
		}

		Map map = en.getEnMap();
		Paras ps = new Paras();
		for (Attr attr : map.getAttrs()) {
			if (attr.getMyFieldType() == FieldType.RefText) {
				continue;
			}

			if (attr.getIsPK()) {
				continue;
			}

			if (!mykeys.contains("@" + attr.getKey() + "@")) {
				continue;
			}

			switch (attr.getMyDataType()) {
			case DataType.AppString:
				if (attr.getUIIsDoc() && attr.getKey().equals("Doc")) {
					String doc = en.GetValStrByKey(attr.getKey()).replace('\'', '~');

					if (map.getAttrs().Contains("DocLength")) {
						en.SetValByKey("DocLength", doc.length());
					}

					if (doc.length() >= 2000) {
						SysDocFile.SetValV2(en.toString(), en.getPKVal().toString(), doc);
						ps.Add(attr.getKey(), "");
					} else {
						ps.Add(attr.getKey(), en.GetValStrByKey(attr.getKey()).replace('\'', '~'));
					}
				} else {
					ps.Add(attr.getKey(), en.GetValStrByKey(attr.getKey()).replace('\'', '~'));
				}
				break;
			case DataType.AppBoolean:
			case DataType.AppInt:
				ps.Add(attr.getKey(), en.GetValIntByKey(attr.getKey()));
				break;
			case DataType.AppFloat:
			case DataType.AppDouble:
			case DataType.AppMoney:
				String str = en.GetValStrByKey(attr.getKey()).toString();
				str = str.replace("￥", "");
				str = str.replace(",", "");
				if (StringHelper.isNullOrEmpty(str)) {
					ps.Add(attr.getKey(), 0);
				} else {
					ps.Add(attr.getKey(), new java.math.BigDecimal(str));
				}
				break;
			case DataType.AppDate: // 如果是日期类型。
			case DataType.AppDateTime:
				String da = en.GetValStringByKey(attr.getKey());
				ps.Add(attr.getKey(), da);
				break;
			default:
				throw new RuntimeException("@SqlBulider.update, 没有这个数据类型");
			}
		}

		if (en.getPK().equals("OID") || en.getPK().equals("WorkID") || en.getPK().equals("FID")) {
			ps.Add(en.getPK(), en.GetValIntByKey(en.getPK()));
		} else {
			ps.Add(en.getPK(), en.GetValStrByKey(en.getPK()));
		}
		return ps;
	}

	public static Paras GenerParas_Update_Informix(Entity en) throws Exception {
		Map map = en.getEnMap();
		Paras ps = new Paras();
		for (Attr attr : map.getAttrs()) {
			if (attr.getMyFieldType() == FieldType.RefText) {
				continue;
			}

			if (attr.getIsPK()) {
				continue;
			}

			switch (attr.getMyDataType()) {
			case DataType.AppString:
				if (attr.getUIIsDoc() && attr.getKey().equals("Doc")) {
					String doc = en.GetValStrByKey(attr.getKey()).replace('\'', '~');

					if (map.getAttrs().Contains("DocLength")) {
						en.SetValByKey("DocLength", doc.length());
					}

					if (doc.length() >= 2000) {
						SysDocFile.SetValV2(en.toString(), en.getPKVal().toString(), doc);
						ps.Add(attr.getKey(), "");
					} else {
						ps.Add(attr.getKey(), en.GetValStrByKey(attr.getKey()).replace('\'', '~'));
					}
				} else {
					ps.Add(attr.getKey(), en.GetValStrByKey(attr.getKey()).replace('\'', '~'));
				}
				break;
			case DataType.AppBoolean:
			case DataType.AppInt:
				ps.Add(attr.getKey(), en.GetValIntByKey(attr.getKey()));
				break;
			case DataType.AppFloat:
			case DataType.AppDouble:
			case DataType.AppMoney:
				String str = en.GetValStrByKey(attr.getKey()).toString();
				str = str.replace("￥", "");
				str = str.replace(",", "");
				if (StringHelper.isNullOrEmpty(str)) {
					ps.Add(attr.getKey(), 0);
				} else {
					ps.Add(attr.getKey(), new java.math.BigDecimal(str));
				}
				break;
			case DataType.AppDate: // 如果是日期类型。
			case DataType.AppDateTime:
				String da = en.GetValStringByKey(attr.getKey());
				ps.Add(attr.getKey(), da);
				break;
			default:
				throw new RuntimeException("@SqlBulider.update, 没有这个数据类型");
			}
		}

		String pk = en.getPK();

		if (pk.equals("OID") || pk.equals("WorkID")) {
			ps.Add(en.getPK(), en.GetValIntByKey(pk));
		} else if (pk.equals("No") || pk.equals("MyPK") || pk.equals("ID")) {
			ps.Add(en.getPK(), en.GetValStrByKey(pk));
		} else {
			for (Attr attr : map.getAttrs()) {
				if (!attr.getIsPK()) {
					continue;
				}
				switch (attr.getMyDataType()) {
				case DataType.AppString:
					ps.Add(attr.getKey(), en.GetValStrByKey(attr.getKey()).replace('\'', '~'));
					break;
				case DataType.AppInt:
					ps.Add(attr.getKey(), en.GetValIntByKey(attr.getKey()));
					break;
				default:
					throw new RuntimeException("@SqlBulider.update, 没有这个数据类型...");
				}
			}
		}
		return ps;
	}

	public static Paras GenerParas(Entity en, String[] keys) throws Exception {
		boolean IsEnableNull = SystemConfig.getIsEnableNull();
		String mykeys = "@";
		if (keys != null) {
			for (String key : keys) {
				mykeys += key + "@";
			}
		}

		Map map = en.getEnMap();
		Paras ps = new Paras();
		for (Attr attr : map.getAttrs()) {
			if (attr.getMyFieldType() == FieldType.RefText) {
				continue;
			}

			if (keys != null) {
				if (!mykeys.contains("@" + attr.getKey() + "@")) {
					if (!attr.getIsPK()) {
						continue;
					}
				}
			}

			switch (attr.getMyDataType()) {
			case DataType.AppString:
				if (attr.getUIIsDoc() && attr.getKey().equals("Doc")) {
					ps.Add(attr.getKey(), en.GetValStrByKey(attr.getKey()).replace('\'', '~'));
				} else {
					ps.Add(attr.getKey(), en.GetValStrByKey(attr.getKey()).replace('\'', '~'));
				}
				break;
			case DataType.AppBoolean:
				ps.Add(attr.getKey(), en.GetValIntByKey(attr.getKey()));
				break;
			case DataType.AppInt:
				if (attr.getKey().equals("MyPK")) // 特殊判断解决truck
													// 是64位的int类型的数值问题.
				{
					ps.Add(attr.getKey(), en.GetValInt64ByKey(attr.getKey()));
					
				} else {
					 
						String s = en.GetValStrByKey(attr.getKey());
						if (StringHelper.isNullOrEmpty(s)) {
							ps.Add(attr.getKey(), 0);
						} else {
							ps.Add(attr.getKey(), en.GetValIntByKey(attr.getKey()));
						}
					 
				}
				break;
			case DataType.AppFloat:
			case DataType.AppDouble:
			case DataType.AppMoney:
				String str = en.GetValStrByKey(attr.getKey()).toString();
				str = str.replace("￥", "");
				str = str.replace(",", "");
				if (StringHelper.isNullOrEmpty(str)) {
					if (IsEnableNull) {
						// ps.AddDBNull(attr.getKey());
						ps.Add(attr.getKey(), null, BigDecimal.class);
					} else {
						ps.Add(attr.getKey(), 0);
					}
				} else {
					ps.Add(attr.getKey(), new BigDecimal(str));
				}
				break;
			case DataType.AppDate: // 如果是日期类型。
			case DataType.AppDateTime:
				String da = en.GetValStringByKey(attr.getKey());
				ps.Add(attr.getKey(), da);
				break;
			default:
				throw new RuntimeException("@SqlBulider.update, 没有这个数据类型");
			}
		}

		if (keys != null) {
			if (en.getPK().equals("OID") || en.getPK().equals("WorkID")) {
				ps.Add(en.getPK(), en.GetValIntByKey(en.getPK()));
			} else {
				ps.Add(en.getPK(), en.GetValStrByKey(en.getPK()));
			}
		}

		return ps;
	}

	public static String UpdateForPara(Entity en, String[] keys) throws Exception {
		String mykey = "";
		if (keys != null) {
			for (String s : keys) {
				mykey += "@" + s + ",";
			}
		}

		String dbVarStr = en.getHisDBVarStr();
		Map map = en.getEnMap();
		String val = "";
		for (Attr attr : map.getAttrs()) {
			if (attr.getMyFieldType() == FieldType.RefText || attr.getIsPK()) {
				continue;
			}

			if (keys != null) {
				if (!mykey.contains("@" + attr.getKey() + ",")) {
					continue;
				}
			}

			val = val + "," + attr.getField() + "=" + dbVarStr + attr.getKey();

		}
		if (StringHelper.isNullOrEmpty(val)) {
			for (Attr attr : map.getAttrs()) {
				if (attr.getMyFieldType() == FieldType.RefText) {
					continue;
				}

				if (keys != null) {
					if (!mykey.contains("@" + attr.getKey() + ",")) {
						continue;
					}
				}

				val = val + "," + attr.getField() + "=" + attr.getField();
			}
		}
		if (!StringHelper.isNullOrEmpty(val)) {
			val = val.substring(1);
		}
		String sql = "UPDATE " + en.getEnMap().getPhysicsTable() + " SET " + val + " WHERE "
				+ SqlBuilder.GenerWhereByPK(en, en.getHisDBVarStr());

		return sql.replace(",=''", "");
	}

	/**
	 * Delete sql
	 * 
	 * @param en
	 * @return
	 * @throws Exception 
	 */
	public static String DeleteForPara(Entity en) throws Exception {
		String dbstr = en.getHisDBVarStr();
		String sql = "DELETE FROM " + en.getEnMap().getPhysicsTable() + " WHERE "
				+ SqlBuilder.GenerWhereByPK(en, dbstr);
		return sql;
	}

	public static String InsertForPara(Entity en) throws Exception {

		String dbstr = en.getHisDBVarStr();

		boolean isMySQL = false;
		if (SystemConfig.getAppCenterDBType() == DBType.MySQL)
			isMySQL = true;

		String field = "", val = "";
		Attrs attrs = en.getEnMap().getAttrs();
		for (Attr attr : attrs) {

			if (attr.getMyFieldType() == FieldType.RefText)
				continue;

			if (isMySQL)
				field = field + "," + attr.getField();
			else
				field = field + ",[" + attr.getField() + "]";

			val = val + "," + dbstr + attr.getKey();
		}
		String sql = "INSERT INTO " + en.getEnMap().getPhysicsTable() + " (" + field.substring(1) + " ) VALUES ( "
				+ val.substring(1) + ")";
		return sql;
	}

	public static String InsertForPara_Informix(Entity en) throws Exception {
		boolean isInnkey = false;
		if (en.getIsOIDEntity()) {
			isInnkey = false;
		}

		String field = "", val = "";
		Attrs attrs = en.getEnMap().getAttrs();
		for (Attr attr : attrs) {
			if (attr.getMyFieldType() == FieldType.RefText) {
				continue;
			}

			if (isInnkey) {
				if (attr.getKey().equals("OID")) {
					continue;
				}
			}

			field = field + ",[" + attr.getField() + "]";
			val = val + ",?";
		}
		String sql = "INSERT INTO " + en.getEnMap().getPhysicsTable() + " (" + field.substring(1) + " ) VALUES ( "
				+ val.substring(1) + ")";
		return sql;
	}

	/**
	 * Insert
	 * 
	 * @param en
	 * @return
	 * @throws Exception 
	 */
	public static String Insert(Entity en) throws Exception {
		String key = "", field = "", val = "";
		Attrs attrs = en.getEnMap().getAttrs();
		if (attrs.size() == 0) 
			throw new RuntimeException("@实体：" + en.toString() + " ,Attrs属性集合信息丢失，导致无法生成SQL。");
		
		for (Attr attr : attrs) {
			if (attr.getMyFieldType() == FieldType.RefText)
				continue;

			key = attr.getKey();
			field = field + "," + attr.getField();
			switch (attr.getMyDataType()) {
			case DataType.AppString:
				String str = en.GetValStringByKey(key);
				if (StringHelper.isNullOrEmpty(str)) {
					str = "";
				} else {
					str = str.replace('\'', '~');
				}

				if (attr.getUIIsDoc() && attr.getKey().equals("Doc")) {
					if (attrs.Contains("DocLength")) {
						en.SetValByKey("DocLength", str.length());
					}

					if (str.length() >= 2000) {
						SysDocFile.SetValV2(en.toString(), en.getPKVal().toString(), str);
						if (attrs.Contains("DocLength")) {
							en.SetValByKey("DocLength", str.length());
						}
						val = val + ",''";
					} else {
						val = val + ",'" + str + "'";
					}
				} else {
					val = val + ",'" + en.GetValStringByKey(key).replace('\'', '~') + "'";
				}
				break;
			case DataType.AppInt:
			case DataType.AppBoolean:
				val = val + "," + en.GetValIntByKey(key);
				break;
			case DataType.AppFloat:
			case DataType.AppDouble:
			case DataType.AppMoney:
				String strNum = en.GetValStringByKey(key).toString();
				strNum = strNum.replace("￥", "");
				strNum = strNum.replace(",", "");
				if (strNum.equals("")) {
					strNum = "0";
				}
				val = val + "," + strNum;
				break;
			case DataType.AppDate:
			case DataType.AppDateTime:
				String da = en.GetValStringByKey(attr.getKey());
				if (da.indexOf("_DATE") == -1) {
					val = val + ",'" + da + "'";
				} else {
					val = val + "," + da;
				}
				break;
			default:
				throw new RuntimeException("@bulider insert sql error: 没有这个数据类型");
			}
		}
		String sql = "INSERT INTO " + en.getEnMap().getPhysicsTable() + " (" + field.substring(1) + " ) VALUES ( "
				+ val.substring(1) + ")";
		return sql;
	}

	public static String InsertOFOLE(Entity en) throws Exception {
		String key = "", field = "", val = "";
		for (Attr attr : en.getEnMap().getAttrs()) {
			if (attr.getMyFieldType() == FieldType.RefText) {
				continue;
			}
			key = attr.getKey();
			field = field + ",[" + attr.getField() + "]";
			switch (attr.getMyDataType()) {
			case DataType.AppString:
				val = val + ", '" + en.GetValStringByKey(key) + "'";
				break;
			case DataType.AppInt:
			case DataType.AppBoolean:
				val = val + "," + en.GetValIntByKey(key);
				break;
			case DataType.AppFloat:
			case DataType.AppDouble:
			case DataType.AppMoney:
				String str = en.GetValStringByKey(key).toString();
				str = str.replace("￥", "");
				str = str.replace(",", "");
				if (str.equals("")) {
					str = "0";
				}
				val = val + "," + str;
				break;
			case DataType.AppDate:
			case DataType.AppDateTime:
				String da = en.GetValStringByKey(attr.getKey());
				if (da.indexOf("_DATE") == -1) {
					val = val + ",'" + da + "'";
				} else {
					val = val + "," + da;
				}
				break;
			default:
				throw new RuntimeException("@bulider insert sql error: 没有这个数据类型");
			}
		}
		String sql = "INSERT INTO " + en.getEnMap().getPhysicsTable() + " (" + field.substring(1) + " ) VALUES ( "
				+ val.substring(1) + ")";
		return sql;
	}
	
	 /// <summary>
    /// 获取判断指定表达式如果为空，则返回指定值的SQL表达式
    /// <para>注：目前只对MSSQL/ORACLE/MYSQL三种数据库做兼容</para>
    /// <para>added by liuxc,2017-03-07</para>
    /// </summary>
    /// <param name="expression">要判断的表达式，在SQL中的写法</param>
    /// <param name="isNullBack">判断的表达式为NULL，返回值的表达式，在SQL中的写法</param>
    /// <returns></returns>
    public static String GetIsNullInSQL(String expression, String isNullBack) throws Exception
    {
        switch (DBAccess.getAppCenterDBType())
        {
            case MSSQL:
                return " ISNULL(" + expression + "," + isNullBack + ")";
            case Oracle:
                return " NVL(" + expression + "," + isNullBack + ")";
            case MySQL:
                return " IFNULL(" + expression + "," + isNullBack + ")";
            case PostgreSQL:
                return " COALESCE(" + expression + "," + isNullBack + ")";
            default:
                throw new Exception("GetIsNullInSQL未涉及的数据库类型");
        }
    }
}