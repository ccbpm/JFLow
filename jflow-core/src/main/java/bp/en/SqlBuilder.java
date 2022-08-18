package bp.en;
import bp.da.*;
import bp.difference.SystemConfig;
import bp.sys.*;
import bp.wf.Glo;

import java.math.*;

public class SqlBuilder
{

		///关于IEntitiy的操作
	
	/** 
	 得到主健
	 
	 param en
	 @return 
	*/
	public static String GetKeyConditionOfMS(Entity en)throws Exception
	{
		// 判断特殊情况。
		switch (en.getPK_Field())
		{
			case "OID":
				return " OID=:OID";
			case "No":
				return " No=:No";
			case "MyPK":
				return " MyPK=:MyPK";
			default:
				break;
		}

		if (en.getEnMap().getAttrs().contains("OID"))
		{
			int key = en.GetValIntByKey("OID");
			if (key == 0)
			{
				throw new RuntimeException("@在执行[" + en.getEnMap().getEnDesc() + " " + en.getEnMap().getPhysicsTable() + "]，没有给PK OID 赋值,不能进行查询操作。");
			}
			//   if (en.PKs.Length == 1)
			return " OID=:OID" + key;
		}

		String sql = " (1=1) ";
		for (Attr attr : en.getEnMap().getAttrs())
		{
			if (attr.getMyFieldType() == FieldType.PK || attr.getMyFieldType() == FieldType.PKFK || attr.getMyFieldType() == FieldType.PKEnum)
			{
				if (attr.getMyDataType() == DataType.AppString)
				{
					String val = en.GetValByKey(attr.getKey()).toString();
					if (val == null || val.equals(""))
					{
						throw new RuntimeException("@在执行[" + en.getEnMap().getEnDesc() + " " + en.getEnMap().getPhysicsTable() + "]没有给PK " + attr.getKey() + " 赋值,不能进行查询操作。");
					}
					sql = sql + " AND " + attr.getField() + "=:" + attr.getKey();
					continue;
				}
				if (attr.getMyDataType() == DataType.AppInt)
				{
					if (en.GetValIntByKey(attr.getKey()) == 0 && attr.getKey().equals("OID"))
					{
						throw new RuntimeException("@在执行[" + en.getEnMap().getEnDesc() + " " + en.getEnMap().getPhysicsTable() + "]，没有给PK " + attr.getKey() + " 赋值,不能进行查询操作。");
					}
					sql = sql + " AND " + attr.getField() + "=:" + attr.getKey();
					continue;
				}
			}
		}
		return sql;
	}
	/** 
	 得到主健
	 
	 param en
	 @return 
	*/
	public static String GetKeyConditionOfOLE(Entity en)throws Exception
	{
		// 判断特殊情况。
		if (en.getEnMap().getAttrs().contains("OID"))
		{
			int key = en.GetValIntByKey("OID");
			if (key == 0)
			{
				throw new RuntimeException("@在执行[" + en.getEnMap().getEnDesc() + " " + en.getEnMap().getPhysicsTable() + "]，没有给PK OID 赋值,不能进行查询操作。");
			}

			if (en.getPKs().length == 1)
			{
				return en.getEnMap().getPhysicsTable() + ".OID=" + key;
			}
		}
		//			if (en.getEnMap().getAttrs().contains("MID"))
		//			{
		//				int key=en.GetValIntByKey("MID");
		//				if (key==0)
		//					throw new Exception("@在执行["+en.getEnMap().EnDesc+ " " +en.getEnMap().getPhysicsTable() +"]，没有给PK MID 赋值,不能进行查询操作。");
		//				return en.getEnMap().getPhysicsTable()+".MID="+key ;
		//			}

		String sql = " (1=1) ";
		for (Attr attr : en.getEnMap().getAttrs())
		{
			if (attr.getMyFieldType() == FieldType.PK || attr.getMyFieldType() == FieldType.PKFK || attr.getMyFieldType() == FieldType.PKEnum)
			{
				if (attr.getMyDataType() == DataType.AppString)
				{
					String val = en.GetValByKey(attr.getKey()).toString();
					if (val == null || val.equals(""))
					{
						throw new RuntimeException("@在执行[" + en.getEnMap().getEnDesc() + " " + en.getEnMap().getPhysicsTable() + "]没有给PK " + attr.getKey() + " 赋值,不能进行查询操作。");
					}
					sql = sql + " AND " + en.getEnMap().getPhysicsTable() + ".[" + attr.getField() + "]='" + en.GetValByKey(attr.getKey()).toString() + "'";
					continue;
				}

				if (attr.getMyDataType() == DataType.AppInt)
				{
					if (en.GetValIntByKey(attr.getKey()) == 0 && attr.getKey().equals("OID"))
					{
						throw new RuntimeException("@在执行[" + en.getEnMap().getEnDesc() + " " + en.getEnMap().getPhysicsTable() + "]，没有给PK " + attr.getKey() + " 赋值,不能进行查询操作。");
					}
					sql = sql + " AND " + en.getEnMap().getPhysicsTable() + ".[" + attr.getField() + "]=" + en.GetValStringByKey(attr.getKey());
					continue;
				}
			}
		}
		return sql;
	}
	/** 
	 得到主健
	 
	 param en
	 @return 
	*/
	public static String GenerWhereByPK(Entity en, String dbStr)throws Exception
	{
		if (en.getPKCount() == 1)
		{
			if (dbStr.equals("?"))
			{
				return en.getEnMap().getPhysicsTable() + "." + en.getPK_Field() + "=" + dbStr;
			}
			else
			{
				return en.getEnMap().getPhysicsTable() + "." + en.getPK_Field() + "=" + dbStr + en.getPK();
			}
		}

		String sql = " (1=1) ";
		for (Attr attr : en.getEnMap().getAttrs())
		{
			if (attr.getMyFieldType() == FieldType.PK || attr.getMyFieldType() == FieldType.PKFK || attr.getMyFieldType() == FieldType.PKEnum)
			{
				if (dbStr.equals("?"))
				{
					sql = sql + " AND " + en.getEnMap().getPhysicsTable() + "." + attr.getField() + "=" + dbStr;
				}
				else
				{
					sql = sql + " AND " + en.getEnMap().getPhysicsTable() + "." + attr.getField() + "=" + dbStr + attr.getField();
				}
			}
		}
		return sql;
	}
	/** 
	 
	 
	 param en
	 @return 
	*/
	public static Paras GenerParasPK(Entity en)throws Exception
	{
		Paras paras = new Paras();
		String pk = en.getPK();

		switch (pk)
		{
			case "OID":
				paras.Add("OID", en.GetValIntByKey("OID"));
				return paras;
			case "No":
				paras.Add("No", en.GetValStrByKey("No"));
				return paras;
			case "MyPK":
				paras.Add("MyPK", en.GetValStrByKey("MyPK"));
				return paras;
			case "NodeID":
				paras.Add("NodeID", en.GetValIntByKey("NodeID"));
				return paras;
			case "WorkID":
				paras.Add("WorkID", en.GetValIntByKey("WorkID"));
				return paras;
			default:
				break;
		}


		for (Attr attr : en.getEnMap().getAttrs())
		{
			if (attr.getMyFieldType() == FieldType.PK || attr.getMyFieldType() == FieldType.PKFK || attr.getMyFieldType() == FieldType.PKEnum)
			{
				if (attr.getMyDataType() == DataType.AppString)
				{
					paras.Add(attr.getKey(), en.GetValByKey(attr.getKey()).toString());
					continue;
				}

				if (attr.getMyDataType() == DataType.AppInt)
				{
					paras.Add(attr.getKey(), en.GetValIntByKey(attr.getKey()));
					continue;
				}
			}
		}
		return paras;
	}
	public static String GetKeyConditionOfOraForPara(Entity en)throws Exception
	{
		// 不能删除物理表名称，会引起未定义列。

		// 判断特殊情况, 
		switch (en.getPK())
		{
			case "OID":
				return en.getEnMap().getPhysicsTable() + ".OID=" + en.getHisDBVarStr() + "OID";
			case "ID":
				return en.getEnMap().getPhysicsTable() + ".ID=" + en.getHisDBVarStr() + "ID";
			case "No":
				return en.getEnMap().getPhysicsTable() + ".No=" + en.getHisDBVarStr() + "No";
			case "MyPK":
				return en.getEnMap().getPhysicsTable() + ".MyPK=" + en.getHisDBVarStr() + "MyPK";
			case "NodeID":
				return en.getEnMap().getPhysicsTable() + ".NodeID=" + en.getHisDBVarStr() + "NodeID";
			case "WorkID":
				return en.getEnMap().getPhysicsTable() + ".WorkID=" + en.getHisDBVarStr() + "WorkID";
			default:
				break;
		}

		String sql = " (1=1) ";
		for (Attr attr : en.getEnMap().getAttrs())
		{
			if (attr.getMyFieldType() == FieldType.PK || attr.getMyFieldType() == FieldType.PKFK || attr.getMyFieldType() == FieldType.PKEnum)
			{
				if (attr.getMyDataType() == DataType.AppString)
				{
					sql = sql + " AND " + en.getEnMap().getPhysicsTable() + "." + attr.getField() + "=" + en.getHisDBVarStr() + attr.getKey();
					continue;
				}
				if (attr.getMyDataType() == DataType.AppInt)
				{
					sql = sql + " AND " + en.getEnMap().getPhysicsTable() + "." + attr.getField() + "=" + en.getHisDBVarStr() + attr.getKey();
					continue;
				}
			}
		}
		return sql.substring(" (1=1)  AND ".length());
	}
	public static String GetKeyConditionOfOraForParaDM(Entity en) throws Exception {

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
		for (Attr attr : attrs.ToJavaList()) {
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

	public static String GetKeyConditionOfInformixForPara(Entity en)throws Exception
	{
		// 不能删除物理表名称，会引起未定义列。

		// 判断特殊情况, 
		switch (en.getPK())
		{
			case "OID":
				return en.getEnMap().getPhysicsTable() + ".OID=?";
			case "No":
				return en.getEnMap().getPhysicsTable() + ".No=?";
			case "MyPK":
				return en.getEnMap().getPhysicsTable() + ".MyPK=?";
			case "ID":
				return en.getEnMap().getPhysicsTable() + ".ID=?";
			default:
				break;
		}

		String sql = " (1=1) ";
		for (Attr attr : en.getEnMap().getAttrs())
		{
			if (attr.getMyFieldType() == FieldType.PK || attr.getMyFieldType() == FieldType.PKFK || attr.getMyFieldType() == FieldType.PKEnum)
			{
				if (attr.getMyDataType() == DataType.AppString)
				{
					sql = sql + " AND " + en.getEnMap().getPhysicsTable() + "." + attr.getField() + "=?";
					continue;
				}
				if (attr.getMyDataType() == DataType.AppInt)
				{
					sql = sql + " AND " + en.getEnMap().getPhysicsTable() + "." + attr.getField() + "=?";
					continue;
				}
			}
		}
		return sql.substring(" (1=1)  AND ".length());
	}

		///

	/** 
	 查询全部信息
	 
	 param en 实体
	 @return sql
	 * @throws Exception 
	*/
	public static String RetrieveAll(Entity en) throws Exception
	{
		return SqlBuilder.SelectSQL(en, SystemConfig.getTopNum());
	}
	/** 
	 查询
	 
	 param en 实体
	 @return string
	 * @throws Exception 
	*/
	public static String Retrieve(Entity en) throws Exception
	{
		String sql = "";
		switch (en.getEnMap().getEnDBUrl().getDBType())
		{
			case MSSQL:
			case MySQL:
				sql = SqlBuilder.SelectSQLOfMS(en, 1) + "   AND (" + SqlBuilder.GenerWhereByPK(en, ":") + " )";
				break;
			case Access:
				sql = SqlBuilder.SelectSQLOfOLE(en, 1) + "  AND (" + SqlBuilder.GenerWhereByPK(en, ":") + " )";
				break;
			case Oracle:
			case KingBaseR3:
			case KingBaseR6:
			case Informix:
				sql = SqlBuilder.SelectSQLOfOra(en, 1) + "  AND (" + SqlBuilder.GenerWhereByPK(en, ":") + " )";
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
	public static String RetrieveForPara(Entity en) throws Exception
	{
		String sql = null;
		switch (en.getEnMap().getEnDBUrl().getDBType())
		{
			case MSSQL:
				sql = SqlBuilder.SelectSQLOfMS(en, 1) + " AND " + SqlBuilder.GenerWhereByPK(en, ":");
				break;
			case MySQL:
				sql = SqlBuilder.SelectSQLOfMySQL(en, 1) + " AND " + SqlBuilder.GenerWhereByPK(en, ":");
				break;
			case Oracle:
			case KingBaseR3:
			case KingBaseR6:
				sql = SqlBuilder.SelectSQLOfOra(en, 1) + "AND (" + SqlBuilder.GenerWhereByPK(en, ":") + " )";
				break;
			case Informix:
				sql = SqlBuilder.SelectSQLOfInformix(en, 1) + " WHERE (" + SqlBuilder.GenerWhereByPK(en, "?") + " )";
				break;
			case Access:
				sql = SqlBuilder.SelectSQLOfOLE(en, 1) + " AND " + SqlBuilder.GenerWhereByPK(en, ":");
				break;
			//case DM:
			//	sql = SqlBuilder.SelectSQLOfDM(en, 1) + " AND " + SqlBuilder.GenerWhereByPK(en, ":");
			//	break;
			case DB2:
			default:
				throw new RuntimeException("还没有实现。");
		}
		sql = sql.replace("WHERE  AND", "WHERE");
		sql = sql.replace("WHERE AND", "WHERE");
		return sql;
	}
	public static String RetrieveForPara_bak(Entity en) throws Exception
	{
		switch (en.getEnMap().getEnDBUrl().getDBType())
		{
			case MSSQL:
			case MySQL:
			case Access:
				if (en.getEnMap().getHisFKAttrs().size() == 0)
				{
					return SqlBuilder.SelectSQLOfMS(en, 1) + SqlBuilder.GetKeyConditionOfOraForPara(en);
				}
				else
				{
					return SqlBuilder.SelectSQLOfMS(en, 1) + "  AND (" + SqlBuilder.GetKeyConditionOfOraForPara(en) + " )";
				}
			case Oracle:
			case KingBaseR3:
			case KingBaseR6:
			case Informix:
				if (en.getEnMap().getHisFKAttrs().size() == 0)
				{
					return SqlBuilder.SelectSQLOfOra(en, 1) + SqlBuilder.GetKeyConditionOfOraForPara(en);
				}
				else
				{
					return SqlBuilder.SelectSQLOfOra(en, 1) + "  AND (" + SqlBuilder.GetKeyConditionOfOraForPara(en) + " )";
				}
			case DB2:
			default:
				throw new RuntimeException("还没有实现。");
		}
	}
	public static String GenerFormOfOra(Entity en)throws Exception
	{
		String from = " FROM " + en.getEnMap().getPhysicsTable();

		if (en.getEnMap().getHisFKEnumAttrs().size() == 0)
		{
			return from + " WHERE (1=1) ";
		}

		String mytable = en.getEnMap().getPhysicsTable();
		from += ",";
		// 产生外键表列表。
		Attrs fkAttrs = en.getEnMap().getHisFKAttrs();
		for (Attr attr : fkAttrs)
		{
			if (attr.getMyFieldType() == FieldType.RefText)
			{
				continue;
			}

			Entity en1 = attr.getHisFKEn();
			String fktable = en1.getEnMap().getPhysicsTable();
			fktable = fktable + " " + fktable + "_" + attr.getKey();
			from += fktable + " ,";
		}

		//产生枚举表列表。
		Attrs enumAttrs = en.getEnMap().getHisEnumAttrs();
		for (Attr attr : enumAttrs)
		{
			if (attr.getMyFieldType() == FieldType.RefText)
			{
				continue;
			}
			//string enumTable = "Enum_"+attr.getKey();
			from += " (SELECT Lab, IntKey FROM "+bp.sys.base.Glo.SysEnum()+" WHERE EnumKey='" + attr.getUIBindKey() + "' )  Enum_" + attr.getKey() + " ,";
		}
		from = from.substring(0, from.length() - 1);
		return from;
	}
	public static String GenerFormWhereOfOra(Entity en)throws Exception
	{
		String from = " FROM " + en.getEnMap().getPhysicsTable();

		if (en.getEnMap().getHisFKAttrs().size() == 0)
		{
			return from + " WHERE (1=1) ";
		}

		String mytable = en.getEnMap().getPhysicsTable();
		from += ",";
		// 产生外键表列表。
		Attrs fkAttrs = en.getEnMap().getHisFKAttrs();
		for (Attr attr : fkAttrs)
		{
			if (attr.getMyFieldType() == FieldType.RefText)
			{
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
		for (Attr attr : fkAttrs)
		{
			if (attr.getMyFieldType() == FieldType.RefText)
			{
				continue;
			}

			Entity en1 = attr.getHisFKEn(); // ClassFactory.GetEns(attr.getUIBindKey()).getGetNewEntity();
			String fktable = "T" + attr.getKey();

			if (isAddAnd == false)
			{
				if (attr.getMyDataType() == DataType.AppString)
				{
					where += "(  " + mytable + "." + attr.getKey() + "=" + fktable + "." + en1.getEnMap().GetFieldByKey(attr.getUIRefKeyValue()) + "  (+) )";
				}
				else
				{
					where += "(  " + mytable + "." + attr.getKey() + "=" + fktable + "." + en1.getEnMap().GetFieldByKey(attr.getUIRefKeyValue()) + "  (+) )";
				}

				isAddAnd = true;
			}
			else
			{
				if (attr.getMyDataType() == DataType.AppString)
				{
					where += " AND (  " + mytable + "." + attr.getKey() + "=" + fktable + "." + en1.getEnMap().GetFieldByKey(attr.getUIRefKeyValue()) + "  (+) )";
				}
				else
				{
					where += " AND (  " + mytable + "." + attr.getKey() + "=" + fktable + "." + en1.getEnMap().GetFieldByKey(attr.getUIRefKeyValue()) + "  (+) )";
				}
			}
		}

		where = where.replace("WHERE  AND", "WHERE");
		where = where.replace("WHERE AND", "WHERE");
		return from + where;
	}
	public static String GenerFormWhereOfInformix(Entity en)throws Exception
	{
		String from = " FROM " + en.getEnMap().getPhysicsTable();
		String mytable = en.getEnMap().getPhysicsTable();
		Attrs fkAttrs = en.getEnMap().getHisFKAttrs();
		String where = "";
		boolean isAddAnd = true;

		// 开始形成 外键 where.
		for (Attr attr : fkAttrs)
		{
			if (attr.getMyFieldType() == FieldType.RefText)
			{
				continue;
			}

			Entity en1 = attr.getHisFKEn();
			String fktable = en1.getEnMap().getPhysicsTable();
			if (isAddAnd == true)
			{
				isAddAnd = false;
				where += " LEFT JOIN " + fktable + "  " + fktable + "_" + attr.getKey() + "  ON " + mytable + "." + attr.getKey() + "=" + fktable + "_" + attr.getKey() + "." + en1.getEnMap().GetFieldByKey(attr.getUIRefKeyValue());
			}
			else
			{
				where += " LEFT JOIN " + fktable + "  " + fktable + "_" + attr.getKey() + "  ON " + mytable + "." + attr.getKey() + "=" + fktable + "_" + attr.getKey() + "." + en1.getEnMap().GetFieldByKey(attr.getUIRefKeyValue());
			}
		}

		where = where.replace("WHERE  AND", "WHERE");
		where = where.replace("WHERE AND", "WHERE");
		return from + where;
	}
	/** 
	 生成sql.
	 
	 param en
	 @return 
	*/
	public static String GenerCreateTableSQLOfMS(Entity en)throws Exception
	{
		if (en.getEnMap().getPhysicsTable().equals("") || en.getEnMap().getPhysicsTable() == null)
		{
			return "DELETE FROM "+bp.sys.base.Glo.SysEnum()+" where enumkey='sdsf44a23'";
		}

		//    throw new Exception(en.ToString() +" map error "+en.GetType() );

		String sql = "CREATE TABLE  " + en.getEnMap().getPhysicsTable() + " (";
		Attrs attrs = en.getEnMap().getAttrs();
		if (attrs.size() == 0)
		{
			throw new RuntimeException("@" + en.getEnDesc() + " , [" + en.getEnMap().getPhysicsTable() + "]没有属性/字段集合 attrs.size() = 0 ,能执行数据表的创建.");
		}

		for (Attr attr : attrs.ToJavaList())
		{
			if (attr.getMyFieldType() == FieldType.RefText)
			{
				continue;
			}

			int len = attr.getMaxLength();

			switch (attr.getMyDataType())
			{
				case DataType.AppString:
				case DataType.AppDate:
				case DataType.AppDateTime:
					if (attr.getIsPK())
					{
						sql += "[" + attr.getField() + "]  NVARCHAR (" + attr.getMaxLength() + ") NOT NULL,";
					}
					else
					{
						if (attr.getMaxLength() >= 4000)
						{
							sql += "[" + attr.getField() + "]  NVARCHAR (MAX) NULL,";
						}
						else
						{
							sql += "[" + attr.getField() + "]  NVARCHAR (" + attr.getMaxLength() + ") NULL,";
						}
					}
					break;
				case DataType.AppFloat:
				case DataType.AppMoney:
					sql += "[" + attr.getField() + "] FLOAT NULL,";
					break;
				case DataType.AppBoolean:
				case DataType.AppInt:
					if (attr.getIsPK())
					{
						//说明这个是自动增长的列.
						if (DataType.IsNullOrEmpty(attr.getUIBindKey())==false && attr.getUIBindKey().equals("1"))
						{
							sql += "[" + attr.getField() + "] INT  primary key identity(1,1),";
						}
						else
						{
							sql += "[" + attr.getField() + "] INT NOT NULL,";
						}
					}
					else
					{
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
	/** 
	 执行PSQL.
	 
	 param en 实体
	 @return 生成的SQL
	*/
	public static String GenerCreateTableSQLOfPostgreSQL(Entity en)throws Exception
	{
		if (en.getEnMap().getPhysicsTable().equals("") || en.getEnMap().getPhysicsTable() == null)
		{
			return "DELETE FROM "+bp.sys.base.Glo.SysEnum()+" where enumkey='sdsf44a23'";
		}

		//    throw new Exception(en.ToString() +" map error "+en.GetType() );

		String sql = "CREATE TABLE  " + en.getEnMap().getPhysicsTable() + " (";
		Attrs attrs = en.getEnMap().getAttrs();
		if (attrs.size() == 0)
		{
			throw new RuntimeException("@" + en.getEnDesc() + " , [" + en.getEnMap().getPhysicsTable() + "]没有属性/字段集合 attrs.size() = 0 ,能执行数据表的创建.");
		}

		for (Attr attr : attrs.ToJavaList())
		{
			if (attr.getMyFieldType() == FieldType.RefText)
			{
				continue;
			}

			int len = attr.getMaxLength();

			switch (attr.getMyDataType())
			{
				case DataType.AppString:
				case DataType.AppDate:
				case DataType.AppDateTime:
					if (attr.getIsPK())
					{
						sql += attr.getField() + "  VARCHAR (" + attr.getMaxLength() + ") NOT NULL,";
					}
					else
					{
						if (attr.getMaxLength() >= 4000)
						{
							sql += attr.getField() + " VARCHAR (" + attr.getMaxLength() + ") NULL,";
						}
						else
						{
							sql += attr.getField() + " VARCHAR (" + attr.getMaxLength() + ") NULL,";
						}
					}
					break;
				case DataType.AppFloat:
				case DataType.AppMoney:
					sql += attr.getField() + " FLOAT NULL,";
					break;
				case DataType.AppBoolean:
				case DataType.AppInt:
					if (attr.getIsPK())
					{
						//说明这个是自动增长的列.
						if (DataType.IsNullOrEmpty(attr.getUIBindKey())==false &&attr.getUIBindKey().equals("1"))
						{
							sql += attr.getField() + " INT  primary key identity(1,1),";
						}
						else
						{
							sql += attr.getField() + " INT NOT NULL,";
						}
					}
					else
					{
						sql += attr.getField() + "  INT  NULL,";
					}
					break;
				case DataType.AppDouble:
					sql += attr.getField() + " FLOAT  NULL,";
					break;
				default:
					break;
			}
		}
		sql = sql.substring(0, sql.length() - 1);
		sql += ")";
		return sql;
	}
	public static String GenerCreateTableSQLOf_OLE(Entity en)throws Exception
	{
		String sql = "CREATE TABLE  " + en.getEnMap().getPhysicsTable() + " (";
		for (Attr attr : en.getEnMap().getAttrs())
		{
			if (attr.getMyFieldType() == FieldType.RefText)
			{
				continue;
			}

			int len = attr.getMaxLength();
			switch (attr.getMyDataType())
			{
				case DataType.AppString:
				case DataType.AppDate:
				case DataType.AppDateTime:
					if (attr.getMaxLength() <= 254)
					{
						if (attr.getIsPK())
						{
							sql += "[" + attr.getField() + "]  varchar (" + attr.getMaxLength() + ") NOT NULL,";
						}
						else
						{
							sql += "[" + attr.getField() + "]  varchar (" + attr.getMaxLength() + ") NULL,";
						}
					}
					else
					{
						if (attr.getIsPK())
						{
							sql += "[" + attr.getField() + "]  text  NOT NULL,";
						}
						else
						{
							sql += "[" + attr.getField() + "] text,";
						}
					}
					break;
				case DataType.AppFloat:
				case DataType.AppMoney:
					sql += "[" + attr.getField() + "] float  NULL,";
					break;
				case DataType.AppBoolean:
				case DataType.AppInt:
					if (attr.getIsPK())
					{
						sql += "[" + attr.getField() + "] int NOT NULL,";
					}
					else
					{
						sql += "[" + attr.getField() + "] int  NULL,";
					}
					break;
				case DataType.AppDouble:
					sql += "[" + attr.getField() + "]  float  NULL,";
					break;
				default:
					break;
			}
		}
		sql = sql.substring(0, sql.length() - 1);
		sql += ")";
		return sql;
	}
	public static String GenerCreateTableSQL(Entity en)throws Exception
	{
		switch (DBAccess.getAppCenterDBType())
		{
			case Oracle:
			case KingBaseR3:
			case KingBaseR6:
				return GenerCreateTableSQLOfOra(en);
			case PostgreSQL:
				return GenerCreateTableSQLOfPostgreSQL(en);
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
	/** 
	 生成sql.
	 
	 param en
	 @return 
	*/
	public static String GenerCreateTableSQLOfOra(Entity en)throws Exception
	{
		if (en.getEnMap().getPhysicsTable() == null)
		{
			throw new RuntimeException("您没有为[" + en.getEnDesc() + "],设置物理表。");
		}

		if (en.getEnMap().getPhysicsTable().trim().length() == 0)
		{
			throw new RuntimeException("您没有为[" + en.getEnDesc() + "],设置物理表。");
		}

		String sql = "CREATE TABLE  " + en.getEnMap().getPhysicsTable() + " (";
		for (Attr attr : en.getEnMap().getAttrs())
		{
			if (attr.getMyFieldType() == FieldType.RefText)
			{
				continue;
			}

			switch (attr.getMyDataType())
			{
				case DataType.AppString:
				case DataType.AppDate:
				case DataType.AppDateTime:
					if (attr.getIsPK())
					{
						sql += attr.getField() + " varchar (" + attr.getMaxLength() + ") NOT NULL,";
					}
					else
					{
						if (attr.getMaxLength() >= 4000)
						{
							sql += attr.getField() + " varchar (" + attr.getMaxLength() + ") NULL,";
						}
						else
						{
							sql += attr.getField() + " varchar (" + attr.getMaxLength() + ") NULL,";
						}
					}
					break;
				case DataType.AppFloat:
				case DataType.AppMoney:
				case DataType.AppDouble:
					sql += attr.getField() + " float NULL,";
					break;
				case DataType.AppBoolean:
				case DataType.AppInt:

					if (attr.getIsPK())
					{
						if (DataType.IsNullOrEmpty(attr.getUIBindKey())==false &&attr.getUIBindKey().equals("1"))
						{
							sql += attr.getField() + " int  primary key identity(1,1),";
						}
						else
						{
							sql += attr.getField() + " int NOT NULL,";
						}
					}
					else
					{
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
	public static String GenerCreateTableSQLOfInfoMix(Entity en)throws Exception
	{
		if (en.getEnMap().getPhysicsTable() == null)
		{
			throw new RuntimeException("您没有为[" + en.getEnDesc() + "],设置物理表。");
		}

		if (en.getEnMap().getPhysicsTable().trim().length() == 0)
		{
			throw new RuntimeException("您没有为[" + en.getEnDesc() + "],设置物理表。");
		}

		String sql = "CREATE TABLE  " + en.getEnMap().getPhysicsTable() + " (";
		for (Attr attr : en.getEnMap().getAttrs())
		{
			if (attr.getMyFieldType() == FieldType.RefText)
			{
				continue;
			}

			switch (attr.getMyDataType())
			{
				case DataType.AppString:
				case DataType.AppDate:
				case DataType.AppDateTime:
					if (attr.getMaxLength() >= 255)
					{
						if (attr.getIsPK())
						{
							sql += attr.getField() + " lvarchar (" + attr.getMaxLength() + "),";
						}
						else
						{
							sql += attr.getField() + " lvarchar (" + attr.getMaxLength() + "),";
						}
					}
					else
					{
						if (attr.getIsPK())
						{
							sql += attr.getField() + " varchar (" + attr.getMaxLength() + ") NOT NULL,";
						}
						else
						{
							sql += attr.getField() + " varchar (" + attr.getMaxLength() + "),";
						}
					}
					break;
				case DataType.AppFloat:
				case DataType.AppMoney:
				case DataType.AppDouble:
					sql += attr.getField() + " float,";
					break;
				case DataType.AppBoolean:
				case DataType.AppInt:
					//说明这个是自动增长的列.
					if (attr.getIsPK())
					{
						if (attr.getUIBindKey().equals("1"))
						{
							sql += attr.getField() + "  Serial not null,";
						}
						else
						{
							sql += attr.getField() + " int8 NOT NULL,";
						}
					}
					else
					{
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
	 生成sql.
	 
	 param en
	 @return 
	*/
	public static String GenerCreateTableSQLOfMySQL(Entity en)throws Exception
	{
		if (en.getEnMap().getPhysicsTable() == null)
		{
			throw new RuntimeException("您没有为[" + en.getEnDesc() + "],设置物理表。");
		}

		if (en.getEnMap().getPhysicsTable().trim().length() == 0)
		{
			throw new RuntimeException("您没有为[" + en.getEnDesc() + "],设置物理表。");
		}

		String sql = "CREATE TABLE  " + en.getEnMap().getPhysicsTable() + " (";
		for (Attr attr : en.getEnMap().getAttrs())
		{
			if (attr.getMyFieldType() == FieldType.RefText)
			{
				continue;
			}

			switch (attr.getMyDataType())
			{
				case DataType.AppString:
				case DataType.AppDate:
				case DataType.AppDateTime:
					if (attr.getIsPK())
					{
						sql += attr.getField() + " NVARCHAR (" + attr.getMaxLength() + ") NOT NULL COMMENT '" + attr.getDesc() + "',";
					}
					else
					{
						if (attr.getMaxLength() > 3000)
						{
							sql += attr.getField() + " TEXT NULL COMMENT '" + attr.getDesc() + "',";
						}
						else
						{
							sql += attr.getField() + " NVARCHAR (" + attr.getMaxLength() + ") NULL COMMENT '" + attr.getDesc() + "',";
						}
					}
					break;
				case DataType.AppFloat:
					sql += attr.getField() + " float  NULL COMMENT '" + attr.getDesc() + "',";
					break;
				case DataType.AppMoney:
					sql += attr.getField() + " decimal(20,4)  NULL COMMENT '" + attr.getDesc() + "',";
					break;
				case DataType.AppDouble:
					sql += attr.getField() + " double  NULL COMMENT '" + attr.getDesc() + "',";
					break;
				case DataType.AppBoolean:
				case DataType.AppInt:
					if (attr.getIsPK() && attr.getKey().equals("OID"))
					{
						if (DataType.IsNullOrEmpty(attr.getUIBindKey())==false &&attr.getUIBindKey().equals("1"))
						{
							sql += attr.getField() + " int(4) primary key not null auto_increment,";
						} else {
							sql += attr.getField() + " int   NULL,";
						}
					}
					else
					{
						if (attr.getDefValType() == 0 && attr.getDefaultVal().toString().equals(MapAttrAttr.DefaultVal) == true)
						{
							sql += attr.getField() + " INT  NULL COMMENT '" + attr.getDesc() + "',";
						}
						else
						{
							sql += attr.getField() + " INT DEFAULT " + attr.getDefaultVal() + " COMMENT '" + attr.getDesc() + "',";
						}
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
	public static String GenerFormWhereOfOra(Entity en, Map map)throws Exception
	{
		String from = " FROM " + map.getPhysicsTable();
		//	string where="  ";
		String table = "";
		String tableAttr = "";
		String enTable = map.getPhysicsTable();

		for (Attr attr : map.getAttrs())
		{
			if (attr.getMyFieldType() == FieldType.Normal || attr.getMyFieldType() == FieldType.PK || attr.getMyFieldType() == FieldType.RefText)
			{
				continue;
			}

			if (attr.getMyFieldType() == FieldType.FK || attr.getMyFieldType() == FieldType.PKFK)
			{
				//Entity en1= ClassFactory.GetEns(attr.getUIBindKey()).getGetNewEntity();
				Entity en1 = attr.getHisFKEn();

				table = en1.getEnMap().getPhysicsTable();
				tableAttr = "T" + attr.getKey() + "";
				from = from + " LEFT OUTER JOIN " + table + "   " + tableAttr + " ON " + enTable + "." + attr.getField() + "=" + tableAttr + "." + en1.getEnMap().GetFieldByKey(attr.getUIRefKeyValue());
				continue;
			}
			if (attr.getMyFieldType() == FieldType.Enum || attr.getMyFieldType() == FieldType.PKEnum)
			{
				tableAttr = "Enum_" + attr.getKey();
				from = from + " LEFT OUTER JOIN ( SELECT Lab, IntKey FROM "+bp.sys.base.Glo.SysEnum()+" WHERE EnumKey='" + attr.getUIBindKey() + "' )  Enum_" + attr.getKey() + " ON " + enTable + "." + attr.getField() + "=" + tableAttr + ".IntKey ";
			}
		}
		return from + "  WHERE (1=1) ";
	}

	public static String GenerFormWhereOfMS(Entity en)throws Exception
	{
		String from = " FROM " + en.getEnMap().getPhysicsTable();

		//去除原因有时获取外键不正确
		//if (en.getEnMap().HisFKAttrs.size() == 0)
		//    return from + " WHERE (1=1)";

		String mytable = en.getEnMap().getPhysicsTable();
		Attrs fkAttrs = en.getEnMap().getAttrs();
		MapAttr mapAttr = null;
		for (Attr attr : fkAttrs)
		{
			if (attr.getIsFK() == false)
			{
				continue;
			}

			if (attr.getMyFieldType() == FieldType.RefText)
			{
				continue;
			}

			mapAttr = attr.getToMapAttr();

			//去除webservice填充DDL数据的类型，2015.9.25，added by liuxc
			if (mapAttr.getLGType() == FieldTypeS.Normal && attr.getUIContralType() == UIContralType.DDL)
			{
				continue;
			}

			String fktable = attr.getHisFKEn().getEnMap().getPhysicsTable();
			Attr refAttr = attr.getHisFKEn().getEnMap().GetAttrByKey(attr.getUIRefKeyValue());
			//added by liuxc,2017-9-11，此处增加是否存在实体表，因新增的字典表类型"动态SQL查询"，此类型没有具体的实体表，完全由SQL动态生成的数据集合，此处不判断会使生成的SQL报错
			//if (DBAccess.IsExits()Object(fktable))

			from += " LEFT JOIN " + fktable + " AS " + fktable + "_" + attr.getKey() + " ON " + mytable + "." + attr.getField() + "=" + fktable + "_" + attr.getField() + "." + refAttr.getField();
		}
		return from + " WHERE (1=1) ";
	}
	/** 
	 GenerFormWhere
	 
	 param en
	 @return 
	*/
	public static String GenerFormWhereOfMS(Entity en, Map map)throws Exception
	{
		String from = " FROM " + map.getPhysicsTable();
		//	string where="  ";
		String table = "";
		String tableAttr = "";
		String enTable = map.getPhysicsTable();
		for (Attr attr : map.getAttrs())
		{
			if (attr.getMyFieldType() == FieldType.Normal || attr.getMyFieldType() == FieldType.PK || attr.getMyFieldType() == FieldType.RefText)
			{
				continue;
			}

			if (attr.getMyFieldType() == FieldType.FK || attr.getMyFieldType() == FieldType.PKFK)
			{
				//Entity en1= ClassFactory.GetEns(attr.getUIBindKey()).getGetNewEntity();
				Entity en1 = attr.getHisFKEn();

				table = en1.getEnMap().getPhysicsTable();
				tableAttr = en1.getEnMap().getPhysicsTable() + "_" + attr.getKey();
				if (attr.getMyDataType() == DataType.AppInt)
				{
					from = from + " LEFT OUTER JOIN " + table + " AS " + tableAttr + " ON ISNULL(" + enTable + "." + attr.getField() + ", " + en.GetValIntByKey(attr.getKey()) + ")=" + tableAttr + "." + en1.getEnMap().GetFieldByKey(attr.getUIRefKeyValue());
				}
				else
				{
					from = from + " LEFT OUTER JOIN " + table + " AS " + tableAttr + " ON ISNULL(" + enTable + "." + attr.getField() + ", '" + en.GetValByKey(attr.getKey()) + "')=" + tableAttr + "." + en1.getEnMap().GetFieldByKey(attr.getUIRefKeyValue());
				}
				continue;
			}

			if (attr.getMyFieldType() == FieldType.Enum || attr.getMyFieldType() == FieldType.PKEnum)
			{
				tableAttr = "Enum_" + attr.getKey();
				from = from + " LEFT OUTER JOIN ( SELECT Lab, IntKey FROM "+bp.sys.base.Glo.SysEnum()+" WHERE EnumKey='" + attr.getUIBindKey() + "' )  Enum_" + attr.getKey() + " ON ISNULL(" + enTable + "." + attr.getField() + ", " + en.GetValIntByKey(attr.getKey()) + ")=" + tableAttr + ".IntKey ";
			}
		}

		return from + "  WHERE (1=1) ";
	}
	/** 
	 GenerFormWhere
	 
	 param en
	 @return 
	*/
	protected static String GenerFormWhereOfMSOLE(Entity en)throws Exception
	{
		String fromTop = en.getEnMap().getPhysicsTable();

		String from = "";
		//	string where="  ";
		String table = "";
		String tableAttr = "";
		String enTable = en.getEnMap().getPhysicsTable();
		for (Attr attr : en.getEnMap().getAttrs())
		{
			if (attr.getMyFieldType() == FieldType.Normal || attr.getMyFieldType() == FieldType.PK || attr.getMyFieldType() == FieldType.RefText)
			{
				continue;
			}
			fromTop = "(" + fromTop;

			if (attr.getMyFieldType() == FieldType.FK || attr.getMyFieldType() == FieldType.PKFK)
			{
				Entity en1 = attr.getHisFKEn(); // ClassFactory.GetEns(attr.getUIBindKey()).getGetNewEntity();
				table = en1.getEnMap().getPhysicsTable();
				tableAttr = en1.getEnMap().getPhysicsTable() + "_" + attr.getKey();

				if (attr.getMyDataType() == DataType.AppInt)
				{
					from = from + " LEFT OUTER JOIN " + table + " AS " + tableAttr + " ON IIf( ISNULL(" + enTable + "." + attr.getField() + "), " + en.GetValIntByKey(attr.getKey()) + " , " + enTable + "." + attr.getField() + " )=" + tableAttr + "." + en1.getEnMap().GetFieldByKey(attr.getUIRefKeyValue());
				}
				else
				{
					from = from + " LEFT OUTER JOIN " + table + " AS " + tableAttr + " ON IIf( ISNULL(" + enTable + "." + attr.getField() + "), '" + en.GetValStringByKey(attr.getKey()) + "', " + enTable + "." + attr.getField() + " )=" + tableAttr + "." + en1.getEnMap().GetFieldByKey(attr.getUIRefKeyValue());
				}
			}
			if (attr.getMyFieldType() == FieldType.Enum || attr.getMyFieldType() == FieldType.PKEnum)
			{
				tableAttr = "Enum_" + attr.getKey();
				from = from + " LEFT OUTER JOIN ( SELECT Lab, IntKey FROM "+bp.sys.base.Glo.SysEnum()+" WHERE EnumKey='" + attr.getUIBindKey() + "' )  Enum_" + attr.getKey() + " ON IIf( ISNULL(" + enTable + "." + attr.getField() + "), " + en.GetValIntByKey(attr.getKey()) + ", " + enTable + "." + attr.getField() + ")=" + tableAttr + ".IntKey ";
			}

			from = from + ")";
		}
		fromTop = " FROM " + fromTop;

		return fromTop + from + "  WHERE (1=1) ";
	}

	protected static String SelectSQLOfOra(Entity en, int topNum)throws Exception
	{
		String val = ""; // key = null;
		String mainTable = "";

		if (en.getEnMap().getHisFKAttrs().size() != 0)
		{
			mainTable = en.getEnMap().getPhysicsTable() + ".";
		}

		for (Attr attr : en.getEnMap().getAttrs())
		{
			if (attr.getMyFieldType() == FieldType.RefText)
			{
				continue;
			}
			switch (attr.getMyDataType())
			{
				case DataType.AppString:
					Object tempVar = attr.getDefaultVal();
					if ( DataType.IsNullOrEmpty(tempVar)==true)
					{
						if (attr.getIsKeyEqualField())
						{
							val = val + ", " + mainTable + attr.getField();
						}
						else
						{
							val = val + "," + mainTable + attr.getField() + " " + attr.getKey();
						}
					}
					else
					{
						val = val + ",NVL(" + mainTable + attr.getField() + ", '" + attr.getDefaultVal() + "') " + attr.getKey();
					}

					if (attr.getMyFieldType() == FieldType.FK || attr.getMyFieldType() == FieldType.PKFK)
					{
						Map map = attr.getHisFKEn().getEnMap();
						val = val + ", T" + attr.getKey() + "." + map.GetFieldByKey(attr.getUIRefKeyText()) + " AS " + attr.getKey() + "Text";
					}
					break;
				case DataType.AppInt:
					if (attr.getDefValType() == 0 && attr.getDefaultVal().equals("10002") || attr.getDefaultVal().equals("10002.00"))
					{
						val = val + ", " +mainTable + attr.getKey();
					}
					else
					{
						val = val + ",NVL(" + mainTable + attr.getField() + "," + attr.getDefaultVal() + ")   " + attr.getKey() + "";
					}

					if (attr.getMyFieldType() == FieldType.Enum || attr.getMyFieldType() == FieldType.PKEnum)
					{
						if (DataType.IsNullOrEmpty(attr.getUIBindKey()))
						{
							throw new RuntimeException("@" + en.toString() + " key=" + attr.getKey() + " UITag=" + attr.UITag);
						}

						SysEnums ses = new SysEnums(attr.getUIBindKey(), attr.UITag);
						val = val + "," + ses.GenerCaseWhenForOracle(en.toString(), mainTable, attr.getKey(), attr.getField(), attr.getUIBindKey(), Integer.parseInt(attr.getDefaultVal().toString()));
					}
					if (attr.getMyFieldType() == FieldType.FK || attr.getMyFieldType() == FieldType.PKFK)
					{
						if (attr.getHisFKEns() == null)
						{
							throw new RuntimeException("@生成SQL错误 Entity=" + en.toString() + " 外键字段｛" + attr.getKey() + "." + attr.getDesc() + ", UIBindKey=" + attr.getUIBindKey() + "｝已经无效, 也许该类或者外键字段被移除，请通知管理员解决。");
						}

						Map map = attr.getHisFKEn().getEnMap();
						val = val + ", T" + attr.getKey() + "." + map.GetFieldByKey(attr.getUIRefKeyText()) + "  AS " + attr.getKey() + "Text";
					}
					break;
				case DataType.AppFloat:
					val = val + ", NVL(" + mainTable + attr.getField() + " ," + attr.getDefaultVal().toString() + ") AS  " + attr.getKey();
					break;
				case DataType.AppBoolean:
					if (attr.getDefaultVal().toString().equals("0"))
					{
						val = val + ", NVL(" + mainTable + attr.getField() + ",0) " + attr.getKey();
					}
					else
					{
						val = val + ", NVL(" + mainTable + attr.getField() + ",1) " + attr.getKey();
					}
					break;
				case DataType.AppDouble:
					val = val + ", NVL(" + mainTable + attr.getField() + " ," + attr.getDefaultVal().toString() + ") " + attr.getKey();
					break;
				case DataType.AppMoney:
					val = val + ", NVL(" + mainTable + attr.getField() + "," + attr.getDefaultVal().toString() + ") " + attr.getKey();
					break;
				case DataType.AppDate:
				case DataType.AppDateTime:
					if (attr.getDefaultVal() == null || attr.getDefaultVal().toString().equals(""))
					{
						val = val + "," + mainTable + attr.getField() + " " + attr.getKey();
					}
					else
					{
						val = val + ",NVL(" + mainTable + attr.getField() + ",'" + attr.getDefaultVal().toString() + "') " + attr.getKey();
					}
					break;
				default:
					throw new RuntimeException("@没有定义的数据类型! attr=" + attr.getKey() + " MyDataType =" + attr.getMyDataType());
			}
		}

		return " SELECT  " + val.substring(1) + SqlBuilder.GenerFormWhereOfOra(en);
	}
	/** 
	 SelectSQLOfInformix
	 
	 param en
	 param topNum
	 @return 
	*/
	protected static String SelectSQLOfInformix(Entity en, int topNum)throws Exception
	{
		String val = "";
		String mainTable = "";

		if (en.getEnMap().getHisFKAttrs().size() != 0)
		{
			mainTable = en.getEnMap().getPhysicsTable() + ".";
		}

		for (Attr attr : en.getEnMap().getAttrs())
		{
			if (attr.getMyFieldType() == FieldType.RefText)
			{
				continue;
			}
			switch (attr.getMyDataType())
			{
				case DataType.AppString:
					Object tempVar = attr.getDefaultVal();
					if (DataType.IsNullOrEmpty(tempVar))
					{
						if (attr.getIsKeyEqualField())
						{
							val = val + ", " + mainTable + attr.getField();
						}
						else
						{
							val = val + "," + mainTable + attr.getField() + " " + attr.getKey();
						}
					}
					else
					{
						val = val + ",NVL(" + mainTable + attr.getField() + ", '" + attr.getDefaultVal() + "') " + attr.getKey();
					}

					if (attr.getMyFieldType() == FieldType.FK || attr.getMyFieldType() == FieldType.PKFK)
					{
						Map map = attr.getHisFKEn().getEnMap();
						val = val + ", " + map.getPhysicsTable() + "_" + attr.getKey() + "." + map.GetFieldByKey(attr.getUIRefKeyText()) + " AS " + attr.getKey() + "Text";
					}
					break;
				case DataType.AppInt:

					val = val + ",NVL(" + mainTable + attr.getField() + "," + attr.getDefaultVal() + ")   " + attr.getKey() + "";

					if (attr.getMyFieldType() == FieldType.Enum || attr.getMyFieldType() == FieldType.PKEnum)
					{
						if (DataType.IsNullOrEmpty(attr.getUIBindKey()))
						{
							throw new RuntimeException("@" + en.toString() + " key=" + attr.getKey() + " UITag=" + attr.UITag);
						}

						SysEnums ses = new SysEnums(attr.getUIBindKey(), attr.UITag);
						val = val + "," + ses.GenerCaseWhenForOracle(en.toString(), mainTable, attr.getKey(), attr.getField(), attr.getUIBindKey(), Integer.parseInt(attr.getDefaultVal().toString()));
					}
					if (attr.getMyFieldType() == FieldType.FK || attr.getMyFieldType() == FieldType.PKFK)
					{
						Map map = attr.getHisFKEn().getEnMap();
						val = val + ", " + map.getPhysicsTable() + "_" + attr.getKey() + "." + map.GetFieldByKey(attr.getUIRefKeyText()) + "  AS " + attr.getKey() + "Text";
					}
					break;
				case DataType.AppFloat:
					val = val + ", NVL( round(" + mainTable + attr.getField() + ",4) ," + attr.getDefaultVal().toString() + ") AS  " + attr.getKey();
					break;
				case DataType.AppBoolean:
					if (attr.getDefaultVal().toString().equals("0"))
					{
						val = val + ", NVL(" + mainTable + attr.getField() + ",0) " + attr.getKey();
					}
					else
					{
						val = val + ", NVL(" + mainTable + attr.getField() + ",1) " + attr.getKey();
					}
					break;
				case DataType.AppDouble:
					val = val + ", NVL( round(" + mainTable + attr.getField() + " ,4) ," + attr.getDefaultVal().toString() + ") " + attr.getKey();
					break;
				case DataType.AppMoney:
					val = val + ", NVL( round(" + mainTable + attr.getField() + ",4)," + attr.getDefaultVal().toString() + ") " + attr.getKey();
					break;
				case DataType.AppDate:
				case DataType.AppDateTime:
					if (attr.getDefaultVal() == null || attr.getDefaultVal().toString().equals(""))
					{
						val = val + "," + mainTable + attr.getField() + " " + attr.getKey();
					}
					else
					{
						val = val + ",NVL(" + mainTable + attr.getField() + ",'" + attr.getDefaultVal().toString() + "') " + attr.getKey();
					}
					break;
				default:
					throw new RuntimeException("@没有定义的数据类型! attr=" + attr.getKey() + " MyDataType =" + attr.getMyDataType());
			}
		}
		return " SELECT  " + val.substring(1) + SqlBuilder.GenerFormWhereOfInformix(en);
	}

	public static String SelectSQL(Entity en, int topNum) throws Exception
	{
		switch (en.getEnMap().getEnDBUrl().getDBType())
		{
			case MSSQL:
				return SqlBuilder.SelectSQLOfMS(en, topNum);
			case MySQL:
				return SqlBuilder.SelectSQLOfMySQL(en, topNum);
			case PostgreSQL:
				return SqlBuilder.SelectSQLOfPostgreSQL(en, topNum);
			case Access:
				return SqlBuilder.SelectSQLOfOLE(en, topNum);
			case Oracle:
			case KingBaseR3:
			case KingBaseR6:
				return SqlBuilder.SelectSQLOfOra(en, topNum);
			case Informix:
				return SqlBuilder.SelectSQLOfInformix(en, topNum);
			default:
				throw new RuntimeException("没有判断的情况");
		}
	}

	/** 
	 得到sql of select
	 
	 param en 实体
	 param en Entity
	 @return sql
	*/
	public static String SelectCountSQL(Entity en)throws Exception
	{
		switch (en.getEnMap().getEnDBUrl().getDBType())
		{
			case MSSQL:
				return SqlBuilder.SelectCountSQLOfMS(en);
			case Access:
				return SqlBuilder.SelectSQLOfOLE(en, 0);
			case Oracle:
			case KingBaseR3:
			case KingBaseR6:
			case Informix:
				return SqlBuilder.SelectSQLOfOra(en, 0);
			default:
				return null;
		}
	}
	/** 
	 建立SelectSQLOfOLE 
	 
	 param en 要执行的en
	 param topNum 最高查询个数
	 @return 返回查询sql
	*/
	public static String SelectSQLOfOLE(Entity en, int topNum)throws Exception
	{
		String val = ""; // key = null;
		String mainTable = en.getEnMap().getPhysicsTable() + ".";
		for (Attr attr : en.getEnMap().getAttrs())
		{
			if (attr.getMyFieldType() == FieldType.RefText)
			{
				continue;
			}
			switch (attr.getMyDataType())
			{
				case DataType.AppString:
					val = val + ", IIf( ISNULL(" + mainTable + attr.getField() + "), '" + attr.getDefaultVal().toString() + "', " + mainTable + attr.getField() + " ) AS [" + attr.getKey() + "]";
					if (attr.getMyFieldType() == FieldType.FK || attr.getMyFieldType() == FieldType.PKFK)
					{
						Entity en1 = attr.getHisFKEn(); // ClassFactory.GetEns(attr.getUIBindKey()).getGetNewEntity();
						val = val + ", IIf( ISNULL(" + en1.getEnMap().getPhysicsTable() + "_" + attr.getKey() + "." + en1.getEnMap().GetFieldByKey(attr.getUIRefKeyText()) + ") ,''," + en1.getEnMap().getPhysicsTable() + "_" + attr.getKey() + "." + en1.getEnMap().GetFieldByKey(attr.getUIRefKeyText()) + ") AS " + attr.getKey() + "Text";
					}
					break;
				case DataType.AppInt:
					val = val + ",IIf( ISNULL(" + mainTable + attr.getField() + ")," + attr.getDefaultVal().toString() + "," + mainTable + attr.getField() + ") AS [" + attr.getKey() + "]";
					if (attr.getMyFieldType() == FieldType.Enum || attr.getMyFieldType() == FieldType.PKEnum)
					{
						val = val + ",IIf( ISNULL( Enum_" + attr.getKey() + ".Lab),'',Enum_" + attr.getKey() + ".Lab ) AS " + attr.getKey() + "Text";
					}
					if (attr.getMyFieldType() == FieldType.FK || attr.getMyFieldType() == FieldType.PKFK)
					{
						Entity en1 = attr.getHisFKEn(); // ClassFactory.GetEns(attr.getUIBindKey()).getGetNewEntity();
						val = val + ", IIf( ISNULL(" + en1.getEnMap().getPhysicsTable() + "_" + attr.getKey() + "." + en1.getEnMap().GetFieldByKey(attr.getUIRefKeyText()) + "),0 ," + en1.getEnMap().getPhysicsTable() + "_" + attr.getKey() + "." + en1.getEnMap().GetFieldByKey(attr.getUIRefKeyText()) + ") AS " + attr.getKey() + "Text";
					}
					break;
				case DataType.AppFloat:
					val = val + ",IIf( ISNULL( Round(" + mainTable + attr.getField() + ",2) )," + attr.getDefaultVal().toString() + "," + mainTable + attr.getField() + ") AS [" + attr.getKey() + "]";
					break;
				case DataType.AppBoolean:
					if (attr.getDefaultVal().toString().equals("0"))
					{
						val = val + ", IIf( ISNULL(" + mainTable + attr.getField() + "),0 ," + mainTable + attr.getField() + ") AS [" + attr.getKey() + "]";
					}
					else
					{
						val = val + ",IIf( ISNULL(" + mainTable + attr.getField() + "),1," + mainTable + attr.getField() + ") AS [" + attr.getKey() + "]";
					}
					break;
				case DataType.AppDouble:
					val = val + ", IIf(ISNULL( Round(" + mainTable + attr.getField() + ",4) )," + attr.getDefaultVal().toString() + "," + mainTable + attr.getField() + ") AS [" + attr.getKey() + "]";
					break;
				case DataType.AppMoney:
					val = val + ",IIf( ISNULL(  Round(" + mainTable + attr.getField() + ",2) )," + attr.getDefaultVal().toString() + "," + mainTable + attr.getField() + ") AS [" + attr.getKey() + "]";
					break;
				case DataType.AppDate:
					val = val + ", IIf(ISNULL(" + mainTable + attr.getField() + "), '" + attr.getDefaultVal().toString() + "'," + mainTable + attr.getField() + ") AS [" + attr.getKey() + "]";
					break;
				case DataType.AppDateTime:
					val = val + ", IIf(ISNULL(" + mainTable + attr.getField() + "), '" + attr.getDefaultVal().toString() + "'," + mainTable + attr.getField() + ") AS [" + attr.getKey() + "]";
					break;
				default:
					throw new RuntimeException("@没有定义的数据类型! attr=" + attr.getKey() + " MyDataType =" + attr.getMyDataType());
			}
		}
		if (topNum == -1 || topNum == 0)
		{
			topNum = 99999;
		}

		return " SELECT TOP " + String.valueOf(topNum) + " " + val.substring(1) + SqlBuilder.GenerFormWhereOfMSOLE(en);
	}
	/** 
	 生成sqlserver标准的sql
	 
	 param en 实体类
	 param topNum 前几行
	 @return 生成的SQL
	 * @throws Exception 
	*/
	public static String SelectSQLOfMS(Entity en, int topNum) throws Exception
	{
		String val = ""; // key = null;
		String mainTable = "";

		if (en.getEnMap().getHisFKAttrs().size() != 0)
		{
			mainTable = en.getEnMap().getPhysicsTable() + ".";
		}

		if (en.getEnMap().getAttrs().size() == 0)
		{
			en.DTSMapToSys_MapData();
			throw new RuntimeException("err@错误:" + en.toString() + "没有attrs属性，无法生成SQL, 如果是动态实体请关闭后，重新打开一次。");
		}


		for (Attr attr : en.getEnMap().getAttrs())
		{
			if (attr.getMyFieldType() == FieldType.RefText)
			{
				continue;
			}
			switch (attr.getMyDataType())
			{
				case DataType.AppString:
					Object tempVar = attr.getDefaultVal();
					if (DataType.IsNullOrEmpty(tempVar) == true)
					{
						if (attr.getIsKeyEqualField())
						{
							val = val + ", " + mainTable + attr.getField();
						}
						else
						{
							val = val + "," + mainTable + attr.getField() + " " + attr.getKey();
						}
					}
					else
					{
						val = val + ",ISNULL(" + mainTable + attr.getField() + ", '" + attr.getDefaultVal() + "') " + attr.getKey();
					}

					if (attr.getMyFieldType() == FieldType.FK || attr.getMyFieldType() == FieldType.PKFK)
					{
						if (attr.getHisFKEns() == null)
						{
							throw new RuntimeException("@生成SQL错误 Entity=" + en.toString() + " 外键字段｛" + attr.getKey() + "." + attr.getDesc() + ", UIBindKey=" + attr.getUIBindKey() + "｝已经无效, 也许该类或者外键字段被移除，请通知管理员解决。");
						}

						Map map = attr.getHisFKEn().getEnMap();

						if (DBAccess.IsExitsObject(map.getPhysicsTable()))
						{
							val = val + ", " + map.getPhysicsTable() + "_" + attr.getKey() + "." + map.GetFieldByKey(attr.getUIRefKeyText()) + " AS " + attr.getKey() + "Text";
						}
						else
						{
							val = val + ", '' AS " + attr.getKey() + "Text";
						}
					}
					break;
				case DataType.AppInt:
					if (attr.getIsNull() || (attr.getDefValType() == 0 && attr.getDefaultVal().equals("10002") || attr.getDefaultVal().equals("10002.00")))
					{
						val = val + "," + mainTable + attr.getField() + " " + attr.getKey() + "";
					}
					else
					{
						val = val + ",ISNULL(" + mainTable + attr.getField() + "," + attr.getDefaultVal() + ")   " + attr.getKey() + "";
					}

					if (attr.getMyFieldType() == FieldType.Enum || attr.getMyFieldType() == FieldType.PKEnum)
					{
						if (DataType.IsNullOrEmpty(attr.getUIBindKey()))
						{
							throw new RuntimeException("@" + en.toString() + " key=" + attr.getKey() + " UITag=" + attr.UITag + "");
						}


///#warning 20111-12-03 不应出现异常。
						if (attr.getUIBindKey().contains("."))
						{
							throw new RuntimeException("@" + en.toString() + " &UIBindKey=" + attr.getUIBindKey() + " @Key=" + attr.getKey());
						}

						SysEnums ses = new SysEnums(attr.getUIBindKey(), attr.UITag);
						val = val + "," + ses.GenerCaseWhenForOracle(mainTable, attr.getKey(), attr.getField(), attr.getUIBindKey(), Integer.parseInt(attr.getDefaultVal().toString()));
					}

					if (attr.getMyFieldType() == FieldType.FK || attr.getMyFieldType() == FieldType.PKFK)
					{
						Map map = attr.getHisFKEn().getEnMap();
						val = val + ", " + map.getPhysicsTable() + "_" + attr.getKey() + "." + map.GetFieldByKey(attr.getUIRefKeyText()) + "  AS " + attr.getKey() + "Text";
					}
					break;
				case DataType.AppFloat:
				case DataType.AppDouble:
				case DataType.AppMoney:
					if (attr.getIsNull())
					{
						val = val + "," + mainTable + attr.getField() + " " + attr.getKey();
					}
					else //不处理
					{
						val = val + ", ISNULL(" + mainTable + attr.getField() + " ," + attr.getDefaultVal().toString() + ") AS  " + attr.getKey();
					}
					break;
				case DataType.AppBoolean:
					if (attr.getDefaultVal().toString().equals("0"))
					{
						val = val + ", ISNULL(" + mainTable + attr.getField() + ",0) " + attr.getKey();
					}
					else
					{
						val = val + ", ISNULL(" + mainTable + attr.getField() + ",1) " + attr.getKey();
					}
					break;
				case DataType.AppDate:
				case DataType.AppDateTime:
					if (attr.getDefaultVal() == null || attr.getDefaultVal().toString().equals(""))
					{
						val = val + "," + mainTable + attr.getField() + " " + attr.getKey();
					}
					else
					{
						val = val + ",ISNULL(" + mainTable + attr.getField() + ",'" + attr.getDefaultVal().toString() + "') " + attr.getKey();
					}
					break;
				default:
					throw new RuntimeException("@没有定义的数据类型! attr=" + attr.getKey() + " MyDataType =" + attr.getMyDataType());
			}
		}

		if (topNum == -1 || topNum == 0)
		{
			topNum = 99999;
		}
		return " SELECT  TOP " + String.valueOf(topNum) + " " + val.substring(1) + SqlBuilder.GenerFormWhereOfMS(en);
	}
	/** 
	 生成postgresql标准的sql
	 
	 param en 实体类
	 param topNum 前几行
	 @return 生成的SQL
	*/
	public static String SelectSQLOfPostgreSQL(Entity en, int topNum)throws Exception
	{
		String val = ""; // key = null;
		String mainTable = "";

		if (en.getEnMap().getHisFKAttrs().size() != 0)
		{
			mainTable = en.getEnMap().getPhysicsTable() + ".";
		}

		for (Attr attr : en.getEnMap().getAttrs())
		{
			if (attr.getMyFieldType() == FieldType.RefText)
			{
				continue;
			}
			switch (attr.getMyDataType())
			{
				case DataType.AppString:
					Object tempVar = attr.getDefaultVal();
					if (DataType.IsNullOrEmpty(tempVar) == true)
					{
						if (attr.getIsKeyEqualField())
						{
							val = val + ", " + mainTable + attr.getField();
						}
						else
						{
							val = val + "," + mainTable + attr.getField() + " " + attr.getKey();
						}
					}
					else
					{
					 //   val = val + ",COALESCE(" + mainTable + attr.getField() + ", '" + attr.getDefaultVal() + "') AS " + attr.getKey();
						val = val + ",COALESCE(" + mainTable + attr.getField() + ", '" + attr.getDefaultVal() + "') AS " + attr.getKey();

					}

					if (attr.getMyFieldType() == FieldType.FK || attr.getMyFieldType() == FieldType.PKFK)
					{
						if (attr.getHisFKEns() == null)
						{
							throw new RuntimeException("@生成SQL错误 Entity=" + en.toString() + " 外键字段｛" + attr.getKey() + "." + attr.getDesc() + ", UIBindKey=" + attr.getUIBindKey() + "｝已经无效, 也许该类或者外键字段被移除，请通知管理员解决。");
						}

						Map map = attr.getHisFKEn().getEnMap();

						if (DBAccess.IsExitsObject(map.getPhysicsTable()))
						{
							val = val + ", " + map.getPhysicsTable() + "_" + attr.getKey() + "." + map.GetFieldByKey(attr.getUIRefKeyText()) + " AS " + attr.getKey() + "Text";
						}
						else
						{
							val = val + ", '' AS " + attr.getKey() + "Text";
						}
					}
					break;
				case DataType.AppInt:
					if (attr.getIsNull() || attr.getDefValType() == 0 && attr.getDefaultVal().equals("10002") || attr.getDefaultVal().equals("10002.00"))
					{
						val = val + "," + mainTable + attr.getField() + " " + attr.getKey() + "";
					}
					else
					{
						val = val + ",COALESCE(" + mainTable + attr.getField() + "," + attr.getDefaultVal() + ")  AS " + attr.getKey() + "";
					}

					if (attr.getMyFieldType() == FieldType.Enum || attr.getMyFieldType() == FieldType.PKEnum)
					{
						if (DataType.IsNullOrEmpty(attr.getUIBindKey()))
						{
							throw new RuntimeException("@" + en.toString() + " key=" + attr.getKey() + " UITag=" + attr.UITag + "");
						}


///#warning 20111-12-03 不应出现异常。
						if (attr.getUIBindKey().contains("."))
						{
							throw new RuntimeException("@" + en.toString() + " &UIBindKey=" + attr.getUIBindKey() + " @Key=" + attr.getKey());
						}

						SysEnums ses = new SysEnums(attr.getUIBindKey(), attr.UITag);
						val = val + "," + ses.GenerCaseWhenForOracle(mainTable, attr.getKey(), attr.getField(), attr.getUIBindKey(), Integer.parseInt(attr.getDefaultVal().toString()));
					}

					if (attr.getMyFieldType() == FieldType.FK || attr.getMyFieldType() == FieldType.PKFK)
					{
						Map map = attr.getHisFKEn().getEnMap();
						val = val + ", " + map.getPhysicsTable() + "_" + attr.getKey() + "." + map.GetFieldByKey(attr.getUIRefKeyText()) + "  AS " + attr.getKey() + "Text";
					}
					break;
				case DataType.AppFloat:
				case DataType.AppDouble:
				case DataType.AppMoney:
					if (attr.getIsNull())
					{
						val = val + "," + mainTable + attr.getField() + " " + attr.getKey();
					}
					else //不处理
					{
						val = val + ", COALESCE(" + mainTable + attr.getField() + " ," + attr.getDefaultVal().toString() + ") AS  " + attr.getKey();
					}
					break;
				case DataType.AppBoolean:
					if (attr.getDefaultVal().toString().equals("0"))
					{
						val = val + ", COALESCE(" + mainTable + attr.getField() + ",0) AS " + attr.getKey();
					}
					else
					{
						val = val + ", COALESCE(" + mainTable + attr.getField() + ",1) AS " + attr.getKey();
					}
					break;
				case DataType.AppDate:
				case DataType.AppDateTime:
					if (attr.getDefaultVal() == null || attr.getDefaultVal().toString().equals(""))
					{
						val = val + "," + mainTable + attr.getField() + " " + attr.getKey();
					}
					else
					{
						val = val + ",COALESCE(" + mainTable + attr.getField() + ",'" + attr.getDefaultVal().toString() + "') AS " + attr.getKey();
					}
					break;
				default:
					throw new RuntimeException("@没有定义的数据类型! attr=" + attr.getKey() + " MyDataType =" + attr.getMyDataType());
			}
		}

		if (topNum == -1 || topNum == 0)
		{
			topNum = 99999;
		}
		return " SELECT " + val.substring(1) + SqlBuilder.GenerFormWhereOfMS(en);
	}
	public static String SelectSQLOfMySQL(Entity en, int topNum)throws Exception
	{
		String val = ""; // key = null;
		String mainTable = "";

		if (en.getEnMap().getHisFKAttrs().size() != 0)
		{
			mainTable = en.getEnMap().getPhysicsTable() + ".";
		}

		for (Attr attr : en.getEnMap().getAttrs())
		{
			if (attr.getMyFieldType() == FieldType.RefText)
			{
				continue;
			}
			switch (attr.getMyDataType())
			{
				case DataType.AppString:
					Object tempVar = attr.getDefaultVal();
					if (DataType.IsNullOrEmpty(tempVar) == true)					
					{
						if (attr.getIsKeyEqualField())
						{
							val = val + ", " + mainTable + attr.getField();
						}
						else
						{
							val = val + "," + mainTable + attr.getField() + " " + attr.getKey();
						}
					}
					else
					{
						val = val + ",IFNULL(" + mainTable + attr.getField() + ", '" + attr.getDefaultVal() + "') " + attr.getKey();
					}

					if (attr.getMyFieldType() == FieldType.FK || attr.getMyFieldType() == FieldType.PKFK)
					{
						Map map=null;
						try{
							map = attr.getHisFKEn().getEnMap();
						}catch(Exception e){
							throw new RuntimeException(attr.getUIBindKey()+"的实体类不存在");
						}

						val = val + ", " + map.getPhysicsTable() + "_" + attr.getKey() + "." + map.GetFieldByKey(attr.getUIRefKeyText()) + " AS " + attr.getKey() + "Text";
					}
					break;
				case DataType.AppInt:
					if (attr.getDefValType() == 0 && attr.getDefaultVal().equals("10002") || attr.getDefaultVal().equals("10002.00"))
					{
						val = val + ", " + attr.getKey();
					}
					else
					{
						val = val + ",IFNULL(" + mainTable + attr.getField() + "," + attr.getDefaultVal() + ")   " + attr.getKey() + "";
					}

					if (attr.getMyFieldType() == FieldType.Enum || attr.getMyFieldType() == FieldType.PKEnum)
					{
						if (DataType.IsNullOrEmpty(attr.getUIBindKey()))
						{
							throw new RuntimeException("@" + en.toString() + " key=" + attr.getKey() + " UITag=" + attr.UITag);
						}


///#warning 2011-12-03 不应出现异常。
						if (attr.getUIBindKey().contains("."))
						{
							throw new RuntimeException("@" + en.toString() + " &UIBindKey=" + attr.getUIBindKey() + " @Key=" + attr.getKey());
						}

						SysEnums ses = new SysEnums(attr.getUIBindKey(), attr.UITag);
						val = val + "," + ses.GenerCaseWhenForOracle(mainTable, attr.getKey(), attr.getField(), attr.getUIBindKey(), Integer.parseInt(attr.getDefaultVal().toString()));
					}

					if (attr.getMyFieldType() == FieldType.FK || attr.getMyFieldType() == FieldType.PKFK)
					{
						if (attr.getHisFKEns() == null)
						{
							throw new RuntimeException("@生成SQL错误 Entity=" + en.toString() + " 外键字段｛" + attr.getKey() + "." + attr.getDesc() + ", UIBindKey=" + attr.getUIBindKey() + "｝已经无效, 也许该类或者外键字段被移除，请通知管理员解决。");
						}


						Map map = attr.getHisFKEn().getEnMap();
						val = val + ", " + map.getPhysicsTable() + "_" + attr.getKey() + "." + map.GetFieldByKey(attr.getUIRefKeyText()) + "  AS " + attr.getKey() + "Text";
					}
					break;
				case DataType.AppFloat:
					val = val + ", IFNULL(" + mainTable + attr.getField() + " ," + attr.getDefaultVal().toString() + ") AS  " + attr.getKey();
					break;
				case DataType.AppBoolean:
					if (attr.getDefaultVal().toString().equals("0"))
					{
						val = val + ", IFNULL(" + mainTable + attr.getField() + ",0) " + attr.getKey();
					}
					else
					{
						val = val + ", IFNULL(" + mainTable + attr.getField() + ",1) " + attr.getKey();
					}
					break;
				case DataType.AppDouble:
					val = val + ", IFNULL(" + mainTable + attr.getField() + "  ," + attr.getDefaultVal().toString() + ") " + attr.getKey();
					break;
				case DataType.AppMoney:
					val = val + ", IFNULL(" + mainTable + attr.getField() + "," + attr.getDefaultVal().toString() + ") " + attr.getKey();
					break;
				case DataType.AppDate:
				case DataType.AppDateTime:
					if (attr.getDefaultVal() == null || attr.getDefaultVal().toString().equals(""))
					{
						val = val + "," + mainTable + attr.getField() + " " + attr.getKey();
					}
					else
					{
						val = val + ",IFNULL(" + mainTable + attr.getField() + ",'" + attr.getDefaultVal().toString() + "') " + attr.getKey();
					}
					break;
				default:
					throw new RuntimeException("@没有定义的数据类型! attr=" + attr.getKey() + " MyDataType =" + attr.getMyDataType());
			}
		}

		return " SELECT   " + val.substring(1) + SqlBuilder.GenerFormWhereOfMS(en);
	}

	/** 
	 建立selectSQL 
	 
	 param en 要执行的en
	 param  en
	 @return 返回查询sql
	*/
	public static String SelectCountSQLOfMS(Entity en)throws Exception
	{
		// 判断内存里面是否有 此sql.
		String sql = "SELECT COUNT(*) ";
		return sql + SqlBuilder.GenerFormWhereOfMS(en, en.getEnMap());
	}
	public static String SelectSQLOfOra(String enName, Map map) throws Exception
	{
		String val = ""; // key = null;
		String mainTable = map.getPhysicsTable() + ".";
		for (Attr attr : map.getAttrs())
		{
			if (attr.getMyFieldType() == FieldType.RefText)
			{
				continue;
			}

			switch (attr.getMyDataType())
			{
				case DataType.AppString:
					if (attr.getDefaultVal() == null)
					{
						val = val + ", " + mainTable + attr.getField() + " AS " + attr.getKey();
					}
					else
					{
						val = val + ", NVL(" + mainTable + attr.getField() + ", '" + attr.getDefaultVal() + "') AS " + attr.getKey();
					}

					if (attr.getMyFieldType() == FieldType.FK || attr.getMyFieldType() == FieldType.PKFK)
					{
						Entity en1 = attr.getHisFKEn(); // ClassFactory.GetEns(attr.getUIBindKey()).getGetNewEntity();
						val = val + ", T" + attr.getKey() + "." + en1.getEnMap().GetFieldByKey(attr.getUIRefKeyText()) + " AS " + attr.getKey() + "Text";
					}
					break;
				case DataType.AppInt:
					val = val + ", NVL(" + mainTable + attr.getField() + "," + attr.getDefaultVal() + ") AS  " + attr.getKey() + "";
					if (attr.getMyFieldType() == FieldType.Enum || attr.getMyFieldType() == FieldType.PKEnum)
					{
						SysEnums ses = new SysEnums(attr.getUIBindKey(), attr.UITag);
						val = val + "," + ses.GenerCaseWhenForOracle(enName, mainTable, attr.getKey(), attr.getField(), attr.getUIBindKey(), Integer.parseInt(attr.getDefaultVal().toString()));
					}
					if (attr.getMyFieldType() == FieldType.FK || attr.getMyFieldType() == FieldType.PKFK)
					{
						Entity en1 = attr.getHisFKEn(); // ClassFactory.GetEns(attr.getUIBindKey()).getGetNewEntity();
						val = val + ", T" + attr.getKey() + "." + en1.getEnMap().GetFieldByKey(attr.getUIRefKeyText()) + "  AS " + attr.getKey() + "Text";
					}
					break;
				case DataType.AppFloat:
					val = val + ", NVL(" + mainTable + attr.getField() + "," + attr.getDefaultVal().toString() + ") AS  " + attr.getKey();
					break;
				case DataType.AppBoolean:
					if (attr.getDefaultVal().toString().equals("0"))
					{
						val = val + ", NVL(" + mainTable + attr.getField() + " , 0)  AS " + attr.getKey();
					}
					else
					{
						val = val + ", NVL(  " + mainTable + attr.getField() + ", 1)  AS " + attr.getKey();
					}
					break;
				case DataType.AppDouble:
					val = val + ", NVL(" + mainTable + attr.getField() + "," + attr.getDefaultVal().toString() + ") AS " + attr.getKey();
					break;
				case DataType.AppMoney:
					val = val + ", NVL(" + mainTable + attr.getField() + "," + attr.getDefaultVal().toString() + ") AS " + attr.getKey();
					break;
				case DataType.AppDate:
					val = val + ", NVL(  " + mainTable + attr.getField() + ", '" + attr.getDefaultVal().toString() + "')  AS " + attr.getKey();
					break;
				case DataType.AppDateTime:
					val = val + ", NVL(" + mainTable + attr.getField() + ", '" + attr.getDefaultVal().toString() + "') AS " + attr.getKey();
					break;
				default:
					throw new RuntimeException("@没有定义的数据类型! attr=" + attr.getKey() + " MyDataType =" + attr.getMyDataType());
			}
		}
		return "SELECT " + val.substring(1);
	}
	/** 
	 产生sql
	 
	 @return 
	 * @throws Exception 
	*/
	public static String SelectSQLOfMS(Map map) throws Exception
	{
		String val = ""; // key = null;
		String mainTable = map.getPhysicsTable() + ".";
		for (Attr attr : map.getAttrs())
		{
			if (attr.getMyFieldType() == FieldType.RefText)
			{
				continue;
			}
			switch (attr.getMyDataType())
			{
				case DataType.AppString:
					if (attr.getDefaultVal() == null)
					{
						val = val + " " + mainTable + attr.getField() + " AS [" + attr.getKey() + "]";
					}
					else
					{
						val = val + ",ISNULL(" + mainTable + attr.getField() + ", '" + attr.getDefaultVal().toString() + "') AS [" + attr.getKey() + "]";
					}

					if (attr.getMyFieldType() == FieldType.FK || attr.getMyFieldType() == FieldType.PKFK)
					{
						Entity en1 = attr.getHisFKEn();
						val = val + "," + en1.getEnMap().getPhysicsTable() + "_" + attr.getKey() + "." + en1.getEnMap().GetFieldByKey(attr.getUIRefKeyText()) + " AS " + attr.getKey() + "Text";
					}
					break;
				case DataType.AppInt:
					if (attr.getIsNull())
					{
						/*如果允许为空*/
						val = val + ", " + mainTable + attr.getField() + " AS [" + attr.getKey() + "]";
					}
					else
					{
						val = val + ", ISNULL(" + mainTable + attr.getField() + "," + attr.getDefaultVal().toString() + ") AS [" + attr.getKey() + "]";
						if (attr.getMyFieldType() == FieldType.Enum || attr.getMyFieldType() == FieldType.PKEnum)
						{
							val = val + ", Enum_" + attr.getKey() + ".Lab  AS " + attr.getKey() + "Text";
						}
						if (attr.getMyFieldType() == FieldType.FK || attr.getMyFieldType() == FieldType.PKFK)
						{
							//Entity en1= ClassFactory.GetEns(attr.getUIBindKey()).getGetNewEntity();
							Entity en1 = attr.getHisFKEn();
							val = val + ", " + en1.getEnMap().getPhysicsTable() + "_" + attr.getKey() + "." + en1.getEnMap().GetFieldByKey(attr.getUIRefKeyText()) + " AS " + attr.getKey() + "Text";
						}
					}
					break;
				case DataType.AppFloat:
					val = val + ", ISNULL(" + mainTable + attr.getField() + "," + attr.getDefaultVal().toString() + ") AS [" + attr.getKey() + "]";
					break;
				case DataType.AppBoolean:
					if (attr.getDefaultVal().toString().equals("0"))
					{
						val = val + ", ISNULL(" + mainTable + attr.getField() + ",0) AS [" + attr.getKey() + "]";
					}
					else
					{
						val = val + ", ISNULL(" + mainTable + attr.getField() + ",1) AS [" + attr.getKey() + "]";
					}
					break;
				case DataType.AppDouble:
					val = val + ", ISNULL(" + mainTable + attr.getField() + "," + attr.getDefaultVal().toString() + ") AS [" + attr.getKey() + "]";
					break;
				case DataType.AppMoney:
					val = val + ", ISNULL(" + mainTable + attr.getField() + "," + attr.getDefaultVal().toString() + ") AS [" + attr.getKey() + "]";
					break;
				case DataType.AppDate:
					val = val + ", ISNULL(" + mainTable + attr.getField() + ", '" + attr.getDefaultVal().toString() + "') AS [" + attr.getKey() + "]";
					break;
				case DataType.AppDateTime:
					val = val + ", ISNULL(" + mainTable + attr.getField() + ", '" + attr.getDefaultVal().toString() + "') AS [" + attr.getKey() + "]";
					break;
				default:
					if (attr.getKey().equals("MyNum"))
					{
						val = val + ", COUNT(*)  AS MyNum ";
						break;
					}
					else
					{
						throw new RuntimeException("@没有定义的数据类型! attr=" + attr.getKey() + " MyDataType =" + attr.getMyDataType());
					}
			}
		}
		return "SELECT TOP @TopNum " + val.substring(1);
	}
	public static String UpdateOfMSAccess(Entity en, String[] keys) throws Exception
	{
		String val = "";
		for (Attr attr : en.getEnMap().getAttrs())
		{
			/* 两个PK 的情况 */
			//if (attr.getIsPK())
			//    continue;

			if (keys != null)
			{
				boolean isHave = false;
				for (String s : keys)
				{
					if (attr.getKey().equals(s))
					{
						/* 如果找到了要更新的key */
						isHave = true;
						break;
					}
				}
				if (isHave == false)
				{
					continue;
				}
			}


			if (attr.getMyFieldType() == FieldType.RefText || attr.getMyFieldType() == FieldType.PK || attr.getMyFieldType() == FieldType.PKFK || attr.getMyFieldType() == FieldType.PKEnum)
			{
				continue;
			}

			switch (attr.getMyDataType())
			{
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
					if (da.indexOf("_DATE") == -1)
					{
						val = val + ",[" + attr.getField() + "]='" + da + "'";
					}
					else
					{
						val = val + ",[" + attr.getField() + "]=" + da;
					}
					break;
				default:
					throw new RuntimeException("@SqlBulider.update, 没有这个数据类型");
			}
		}

		String sql = "";

		if (val.equals(""))
		{
			/*如果没有出现要更新.*/
			for (Attr attr : en.getEnMap().getAttrs())
			{
				if (keys != null)
				{
					boolean isHave = false;
					for (String s : keys)
					{
						if (attr.getKey().equals(s))
						{
							/* 如果找到了要更新的key */
							isHave = true;
							break;
						}
					}
					if (isHave == false)
					{
						continue;
					}
				}
				switch (attr.getMyDataType())
				{
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
						if (da.indexOf("_DATE") == -1)
						{
							val = val + ",[" + attr.getField() + "]='" + da + "'";
						}
						else
						{
							val = val + ",[" + attr.getField() + "]=" + da;
						}
						break;
					default:
						throw new RuntimeException("@SqlBulider.update, 没有这个数据类型");
				}
			}
			//return null;
			//throw new Exception("出现了一个不合理的更新：没有要更新的数据。");
		}

		if (val.equals(""))
		{
			String ms = "";
			for (String str : keys)
			{
				ms += str;
			}
			throw new RuntimeException(en.getEnDesc() + "执行更新错误：无效的属性[" + ms + "]对于本实体来说。");
		}
		sql = "UPDATE " + en.getEnMap().getPhysicsTable() + " SET " + val.substring(1) + " WHERE " + SqlBuilder.GetKeyConditionOfOLE(en);
		return sql.replace(",=''", "");
	}
	/** 
	 产生要更新的sql 语句
	 
	 param en 要更新的entity
	 param keys 要更新的keys
	 @return sql
	 * @throws Exception 
	*/
	public static String Update(Entity en, String[] keys) throws Exception
	{
		Map map = en.getEnMap();
		if (map.getAttrs().size() == 0)
		{
			throw new RuntimeException("@实体：" + en.toString() + " ,Attrs属性集合信息丢失，导致无法生成SQL。");
		}

		String val = "";
		for (Attr attr : map.getAttrs())
		{
			if (keys != null)
			{
				boolean isHave = false;
				for (String s : keys)
				{
					if (attr.getKey().equals(s))
					{
						/* 如果找到了要更新的key*/
						isHave = true;
						break;
					}
				}
				if (isHave == false)
				{
					continue;
				}
			}

			if (attr.getMyFieldType() == FieldType.RefText)
			{
				continue;
			}

			/*有可能是两个主键的情况。*/
			//  if (attr.getIsPK())
			//  continue;

			switch (attr.getMyDataType())
			{
				case DataType.AppString:
					if (map.getEnDBUrl().getDBType() == DBType.Access)
					{
						val = val + ",[" + attr.getField() + "]='" + en.GetValStringByKey(attr.getKey()).replace('\'', '~') + "'";
					}
					else
					{
						if (attr.getUIIsDoc() && attr.getKey().equals("Doc"))
						{

							String doc = en.GetValStringByKey(attr.getKey());
							if (map.getAttrs().contains("DocLength"))
							{
								en.SetValByKey("DocLength", doc.length());
							}

							if (doc.length() >= 2000)
							{
								SysDocFile.SetValV2(en.toString(), en.getPKVal().toString(), doc);
								val = val + "," + attr.getField() + "=''";
							}
							else
							{
								val = val + "," + attr.getField() + "='" + en.GetValStringByKey(attr.getKey()).replace('\'', '~') + "'";
							}
						}
						else
						{
							val = val + "," + attr.getField() + "='" + en.GetValStringByKey(attr.getKey()).replace('\'', '~') + "'";
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
					if (da.indexOf("_DATE") == -1)
					{
						val = val + "," + attr.getField() + "='" + da + "'";
					}
					else
					{
						val = val + "," + attr.getField() + "=" + da;
					}
					break;
				default:
					throw new RuntimeException("@SqlBulider.update, 没有这个数据类型");
			}
		}

		String sql = "";

		if (val.equals(""))
		{
			/*如果没有出现要更新.*/
			for (Attr attr : map.getAttrs())
			{
				if (keys != null)
				{
					boolean isHave = false;
					for (String s : keys)
					{
						if (attr.getKey().equals(s))
						{
							/* 如果找到了要更新的key */
							isHave = true;
							break;
						}
					}
					if (isHave == false)
					{
						continue;
					}
				}

				// 两个PK 的情况。
				//if (attr.getIsPK())
				//    continue;


				switch (attr.getMyDataType())
				{
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
						if (da.indexOf("_DATE") == -1)
						{
							val = val + "," + attr.getField() + "='" + da + "'";
						}
						else
						{
							val = val + "," + attr.getField() + "=" + da;
						}
						break;
					default:
						throw new RuntimeException("@SqlBulider.update, 没有这个数据类型");
				}
			}

		}

		if (val.equals(""))
		{
			String ms = "";
			for (String str : keys)
			{
				ms += str;
			}
			throw new RuntimeException(en.getEnDesc() + "执行更新错误：无效的属性[" + ms + "]对于本实体来说。");
		}

		switch (en.getEnMap().getEnDBUrl().getDBType())
		{
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
			case KingBaseR3:
			case KingBaseR6:
			case DM:
				sql = "UPDATE " + en.getEnMap().getPhysicsTable() + " SET " + val.substring(1) + " WHERE "
						+ SqlBuilder.GenerWhereByPK(en, ":");
				break;
			case Informix:
				sql = "UPDATE " + en.getEnMap().getPhysicsTable() + " SET " + val.substring(1) + " WHERE "
						+ SqlBuilder.GenerWhereByPK(en, ":");
				break;
		}
		return sql.replace(",=''", "");
	}
	public static Paras GenerParas_Update_Informix(Entity en, String[] keys) throws Exception
	{
		if (keys == null)
		{
			return GenerParas_Update_Informix(en);
		}

		String mykeys = "@";
		for (Object key : keys)
		{
			mykeys += key + "@";
		}

		Map map = en.getEnMap();
		Paras ps = new Paras();
		for (Attr attr : map.getAttrs())
		{
			if (attr.getMyFieldType() == FieldType.RefText)
			{
				continue;
			}

			if (attr.getIsPK())
			{
				continue;
			}

			if (mykeys.contains("@" + attr.getKey() + "@") == false)
			{
				continue;
			}

			switch (attr.getMyDataType())
			{
				case DataType.AppString:
					if (attr.getUIIsDoc() && attr.getKey().equals("Doc"))
					{
						String doc = en.GetValStrByKey(attr.getKey()).replace('\'', '~');

						if (map.getAttrs().contains("DocLength"))
						{
							en.SetValByKey("DocLength", doc.length());
						}

						if (doc.length() >= 2000)
						{
							SysDocFile.SetValV2(en.toString(), en.getPKVal().toString(), doc);
							ps.Add(attr.getKey(), "");
						}
						else
						{
							ps.Add(attr.getKey(), en.GetValStrByKey(attr.getKey()).replace('\'', '~'));
						}
					}
					else
					{
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
					if (DataType.IsNullOrEmpty(str))
					{
						ps.Add(attr.getKey(), 0);
					}
					else
					{
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
		switch (en.getPK())
		{
			case "OID":
			case "WorkID":
			case "FID":
				ps.Add(en.getPK(), en.GetValIntByKey(en.getPK()));
				break;
			default:
				ps.Add(en.getPK(), en.GetValStrByKey(en.getPK()));
				break;
		}
		return ps;
	}
	public static Paras GenerParas_Update_Informix(Entity en) throws Exception
	{
		String mykeys = "@";

		Map map = en.getEnMap();
		Paras ps = new Paras();
		for (Attr attr : map.getAttrs())
		{
			if (attr.getMyFieldType() == FieldType.RefText)
			{
				continue;
			}

			if (attr.getIsPK())
			{
				continue;
			}

			switch (attr.getMyDataType())
			{
				case DataType.AppString:
					if (attr.getUIIsDoc() && attr.getKey().equals("Doc"))
					{
						String doc = en.GetValStrByKey(attr.getKey()).replace('\'', '~');

						if (map.getAttrs().contains("DocLength"))
						{
							en.SetValByKey("DocLength", doc.length());
						}

						if (doc.length() >= 2000)
						{
							SysDocFile.SetValV2(en.toString(), en.getPKVal().toString(), doc);
							ps.Add(attr.getKey(), "");
						}
						else
						{
							ps.Add(attr.getKey(), en.GetValStrByKey(attr.getKey()).replace('\'', '~'));
						}
					}
					else
					{
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
					if (DataType.IsNullOrEmpty(str))
					{
						ps.Add(attr.getKey(), 0);
					}
					else
					{
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

		String pk = en.getPK();
		switch (pk)
		{
			case "OID":
			case "WorkID":
				ps.Add(en.getPK(), en.GetValIntByKey(pk));
				break;
			case "No":
			case "MyPK":
			case "ID":
				ps.Add(en.getPK(), en.GetValStrByKey(pk));
				break;
			default:
				for (Attr attr : map.getAttrs())
				{
					if (attr.getIsPK() == false)
					{
						continue;
					}
					switch (attr.getMyDataType())
					{
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
				break;
		}
		return ps;
	}
	public static Paras GenerParas(Entity en, String[] keys) throws Exception
	{
		boolean IsEnableNull = SystemConfig.getIsEnableNull();
		String mykeys = "@";
		if (keys != null)
		{
			for (Object key : keys)
			{
				mykeys += key + "@";
			}
		}

		Map map = en.getEnMap();
		Paras ps = new Paras();
		String errKey = "";
		try
		{
			for (Attr attr : map.getAttrs())
			{
				if (attr.getMyFieldType() == FieldType.RefText)
				{
					continue;
				}

				if (keys != null)
				{
					if (mykeys.contains("@" + attr.getKey() + "@") == false)
					{
						if (attr.getIsPK() == false)
						{
							continue;
						}
					}
				}

				errKey = attr.getKey();

				switch (attr.getMyDataType())
				{
					case DataType.AppString:
						if (attr.getMaxLength() >= 4000)
						{
							ps.Add(attr.getKey(), en.GetValStrByKey(attr.getKey()).replace('\'', '~'), true);
						}
						else
						{
							ps.Add(attr.getKey(), en.GetValStrByKey(attr.getKey()).replace('\'', '~'));
						}

						break;
					case DataType.AppBoolean:
						ps.Add(attr.getKey(), en.GetValIntByKey(attr.getKey()));
						break;
					case DataType.AppInt:
						if (attr.getKey().equals("MyPK")) //特殊判断解决 truck 是64位的int类型的数值问题.
						{
							ps.Add(attr.getKey(), en.GetValInt64ByKey(attr.getKey()));
						}
						else
						{
							if (en.getRow().GetValByKey(attr.getKey())==null)
							{
								if (DataType.IsNullOrEmpty(attr.getDefaultValOfReal()))
								{
									ps.Add(attr.getKey(), 0);
								}
								else
								{
									ps.Add(attr.getKey(), Integer.parseInt(attr.getDefaultValOfReal()));
								}
								continue;
							}

							 String strInt = en.GetValStrByKey(attr.getKey());
							if (strInt == null || strInt.equals("") || strInt.equals("null"))
							{
								if(DataType.IsNullOrEmpty(attr.getDefaultValOfReal()) == true)
									ps.Add(attr.getKey(),0);
								else
									ps.Add(attr.getKey(), Integer.parseInt(attr.getDefaultValOfReal()));
							}
							else
							{
								if (attr.getDefValType() == 0 && attr.getDefaultVal().equals(MapAttrAttr.DefaultVal) == true && en.GetValIntByKey(attr.getKey()) == Integer.parseInt(MapAttrAttr.DefaultVal))
								{
									ps.Add(attr.getKey(), Integer.class);
								}
								else
								{
									 ps.Add(attr.getKey(), Integer.parseInt(strInt));
								}
							}
						}
						break;
					//@YLN
					case DataType.AppFloat:
					case DataType.AppDouble:
						String tempVar = en.GetValStrByKey(attr.getKey());
						String str = tempVar instanceof String ? (String)tempVar : null;
						if (DataType.IsNullOrEmpty(str))
						{
							if (IsEnableNull)
							{
								ps.Add(attr.getKey(), Float.class);
							}
							else
							{
								ps.Add(attr.getKey(), 0);
							}
						}
						else
						{
							if (attr.getDefValType() == 0 && attr.getDefaultVal().equals(MapAttrAttr.DefaultVal) == true && en.GetValIntByKey(attr.getKey()) == Long.parseLong(MapAttrAttr.DefaultVal))
							{
								ps.Add(attr.getKey(), Float.class);
							}
							else
							{
								ps.Add(attr.getKey(), new BigDecimal(str));
							}
						}
						break;
					case DataType.AppMoney:
						Object val = en.getRow().GetValByKey(attr.getKey());
						if (DataType.IsNullOrEmpty(val))
						{
							str = "0";
						}
						else
						{
							str = val.toString();
							str = str.replace("￥", "");
							str = str.replace(",", "");
						}
						if (attr.getDefValType() == 0 && attr.getDefaultVal().equals(MapAttrAttr.DefaultVal) == true && str.equals(MapAttrAttr.DefaultVal))
						{
							ps.Add(attr.getKey(), Float.class);
						}
						else
						{
							ps.Add(attr.getKey(), new BigDecimal(str));
						}

						break;
					case DataType.AppDate: // 如果是日期类型。
					case DataType.AppDateTime:
						String da = en.GetValStrByKey(attr.getKey());
						ps.Add(attr.getKey(), da);
						break;
					default:
						throw new RuntimeException("@SqlBulider.update, 没有这个数据类型");
				}
			}
		}
		catch (RuntimeException ex)
		{
			Attr attr = en.getEnMap().GetAttrByKey(errKey);
			errKey = "@attrKey=" + attr.getKey() + ",AttrVal=" + en.getRow().GetValByKey(attr.getKey()) + ",DataType=" + attr.getMyDataTypeStr();
			throw new RuntimeException("生成参数期间错误:" + errKey + "@错误信息:" + ex.getMessage());
		}

		if (keys != null)
		{
			switch (en.getPK())
			{
				case "OID":
				case "WorkID":
					ps.Add(en.getPK(), en.GetValIntByKey(en.getPK()));
					break;
				default:
					ps.Add(en.getPK(), en.GetValStrByKey(en.getPK()));
					break;
			}
		}
		return ps;
	}
	public static String UpdateForPara(Entity en, String[] keys) throws Exception
	{
		String mykey = "";
		if (keys != null)
		{
			for (String s : keys)
			{
				mykey += "@" + s + ",";
			}
		}

		String dbVarStr = en.getHisDBVarStr();
		Map map = en.getEnMap();
		String val = "";
		for (Attr attr : map.getAttrs())
		{
			if (attr.getMyFieldType() == FieldType.RefText || attr.getIsPK())
				continue;

			if (keys != null)
			{
				if (mykey.toString().contains("@" + attr.getKey() + ",") == false)
					continue;
			}

			if (dbVarStr.equals("?"))
			{
				val = val + "," + attr.getField() + "=" + dbVarStr;
			}
			else
			{
				val = val + "," + attr.getField() + "=" + dbVarStr + attr.getKey();
			}
		}
		if (DataType.IsNullOrEmpty(val))
		{
			for (Attr attr : map.getAttrs())
			{
				if (attr.getMyFieldType() == FieldType.RefText)
				{
					continue;
				}

				if (keys != null)
				{
					if (mykey.toString().contains("@" + attr.getKey() + ",") == false)
					{
						continue;
					}
				}

				val = val + "," + attr.getField() + "=" + attr.getField();
			}
			//   throw new Exception("@生成SQL出现错误:" + map.getEnDesc() + "，" + en.ToString() + "，要更新的字段为空。");
		}
		if (!DataType.IsNullOrEmpty(val))
		{
			val = val.substring(1);
		}
		String sql = "";
		switch (en.getEnMap().getEnDBUrl().getDBType())
		{
			case MSSQL:
			case Access:
			case MySQL:
			case PostgreSQL:
				sql = "UPDATE " + en.getEnMap().getPhysicsTable() + " SET " + val + " WHERE " + SqlBuilder.GenerWhereByPK(en, ":");
				break;
			case Informix:
				sql = "UPDATE " + en.getEnMap().getPhysicsTable() + " SET " + val + " WHERE " + SqlBuilder.GenerWhereByPK(en, "?");
				break;
			case Oracle:
			case KingBaseR3:
			case KingBaseR6:
				sql = "UPDATE " + en.getEnMap().getPhysicsTable() + " SET " + val + " WHERE " + SqlBuilder.GenerWhereByPK(en, ":");
				break;
			default:
				throw new RuntimeException("no this case db type . ");
		}
		return sql.replace(",=''", "");
	}
	/** 
	 Delete sql
	 
	 param en
	 @return 
	 * @throws Exception 
	*/
	public static String DeleteForPara(Entity en) throws Exception
	{
		String dbstr = en.getHisDBVarStr();
		String sql = "DELETE FROM " + en.getEnMap().getPhysicsTable() + " WHERE " + SqlBuilder.GenerWhereByPK(en, dbstr);
		return sql;
	}
	public static String InsertForPara(Entity en) throws Exception
	{
		String dbstr = en.getHisDBVarStr();
		if (dbstr.equals("?"))
		{
			return InsertForPara_Informix(en);
		}

		boolean isInnkey = false;
		if (en.getIsOIDEntity())
		{
			EntityOID myen = en instanceof EntityOID ? (EntityOID)en : null;
			isInnkey = false; // myen.IsInnKey;
		}

		String key = "", field = "", val = "";
		for (Attr attr : en.getEnMap().getAttrs())
		{
			if (attr.getMyFieldType() == FieldType.RefText)
			{
				continue;
			}

			if (isInnkey)
			{
				if (attr.getKey().equals("OID"))
				{
					continue;
				}
			}

			key = attr.getKey();
			field = field + ",[" + attr.getField() + "]";
			val = val + "," + dbstr + attr.getKey();
		}
		String sql = "INSERT INTO " + en.getEnMap().getPhysicsTable() + " (" + field.substring(1) + " ) VALUES (" + val.substring(1) + ")";
		return sql;
	}
	public static String InsertForPara_Informix(Entity en) throws Exception
	{
		boolean isInnkey = false;
		if (en.getIsOIDEntity())
		{
			EntityOID myen = en instanceof EntityOID ? (EntityOID)en : null;
			isInnkey = false;
		}

		String key = "", field = "", val = "";
		for (Attr attr : en.getEnMap().getAttrs())
		{
			if (attr.getMyFieldType() == FieldType.RefText)
			{
				continue;
			}

			if (isInnkey)
			{
				if (attr.getKey().equals("OID"))
				{
					continue;
				}
			}

			key = attr.getKey();
			field = field + ",[" + attr.getField() + "]";
			val = val + ",?";
		}
		String sql = "INSERT INTO " + en.getEnMap().getPhysicsTable() + " (" + field.substring(1) + " ) VALUES (" + val.substring(1) + ")";
		return sql;
	}
	/** 
	 Insert 
	 
	 param en
	 @return 
	 * @throws Exception 
	*/
	public static String Insert(Entity en) throws Exception
	{
		String key = "", field = "", val = "";
		Attrs attrs = en.getEnMap().getAttrs();
		if (attrs.size() == 0)
		{
			throw new RuntimeException("@实体：" + en.toString() + " ,Attrs属性集合信息丢失，导致无法生成SQL。");
		}

		for (Attr attr : attrs.ToJavaList())
		{
			if (attr.getMyFieldType() == FieldType.RefText)
			{
				continue;
			}

			key = attr.getKey();
			field = field + "," + attr.getField();
			switch (attr.getMyDataType())
			{
				case DataType.AppString:
					String str = en.GetValStringByKey(key);
					if (DataType.IsNullOrEmpty(str))
					{
						str = "";
					}
					else
					{
						str = str.replace('\'', '~');
					}

					if (attr.getUIIsDoc() && attr.getKey().equals("Doc"))
					{
						if (attrs.contains("DocLength"))
						{
							en.SetValByKey("DocLength", str.length());
						}

						if (str.length() >= 2000)
						{
							SysDocFile.SetValV2(en.toString(), en.getPKVal().toString(), str);
							if (attrs.contains("DocLength"))
							{
								en.SetValByKey("DocLength", str.length());
							}
							val = val + ",''";
						}
						else
						{
							val = val + ",'" + str + "'";
						}
					}
					else
					{
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
					if (strNum.equals(""))
					{
						strNum = "0";
					}
					val = val + "," + strNum;
					break;
				case DataType.AppDate:
				case DataType.AppDateTime:
					String da = en.GetValStringByKey(attr.getKey());
					if (da.indexOf("_DATE") == -1)
					{
						val = val + ",'" + da + "'";
					}
					else
					{
						val = val + "," + da;
					}
					break;
				default:
					throw new RuntimeException("@bulider insert sql error: 没有这个数据类型");
			}
		}
		String sql = "INSERT INTO " + en.getEnMap().getPhysicsTable() + " (" + field.substring(1) + " ) VALUES (" + val.substring(1) + ")";
		return sql;
	}
	public static String InsertOFOLE(Entity en) throws Exception
	{
		String key = "", field = "", val = "";
		for (Attr attr : en.getEnMap().getAttrs())
		{
			if (attr.getMyFieldType() == FieldType.RefText)
			{
				continue;
			}
			key = attr.getKey();
			field = field + ",[" + attr.getField() + "]";
			switch (attr.getMyDataType())
			{
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
					if (str.equals(""))
					{
						str = "0";
					}
					val = val + "," + str;
					break;
				case DataType.AppDate:
				case DataType.AppDateTime:
					String da = en.GetValStringByKey(attr.getKey());
					if (da.indexOf("_DATE") == -1)
					{
						val = val + ",'" + da + "'";
					}
					else
					{
						val = val + "," + da;
					}
					break;
				default:
					throw new RuntimeException("@bulider insert sql error: 没有这个数据类型");
			}
		}
		String sql = "INSERT INTO " + en.getEnMap().getPhysicsTable() + " (" + field.substring(1) + " ) VALUES (" + val.substring(1) + ")";
		return sql;
	}

	/** 
	 获取判断指定表达式如果为空，则返回指定值的SQL表达式
	 <p>注：目前只对MSSQL/ORACLE/MYSQL三种数据库做兼容</p>
	 <p>added by liuxc,2017-03-07</p>
	 
	 param expression 要判断的表达式，在SQL中的写法
	 param isNullBack 判断的表达式为NULL，返回值的表达式，在SQL中的写法
	 @return 
	*/
	public static String GetIsNullInSQL(String expression, String isNullBack)
	{
		switch (DBAccess.getAppCenterDBType())
		{
			case MSSQL:
				return " ISNULL(" + expression + "," + isNullBack + ")";
			case Oracle:
			case KingBaseR3:
			case KingBaseR6:
				return " NVL(" + expression + "," + isNullBack + ")";
			case MySQL:
				return " IFNULL(" + expression + "," + isNullBack + ")";
			case PostgreSQL:
				return " COALESCE(" + expression + "," + isNullBack + ")";
			default:
				throw new RuntimeException("GetIsNullInSQL未涉及的数据库类型");
		}
	}
}