package BP.En;

import java.math.BigDecimal;

import BP.DA.Cash;
import BP.DA.DBAccess;
import BP.DA.DBType;
import BP.DA.DataType;
import BP.DA.Depositary;
import BP.DA.Paras;
import BP.Sys.SysDocFile;
import BP.Sys.SysEnums;
import BP.Sys.SystemConfig;
import BP.Tools.StringHelper;

public class SqlBuilder
{
	// 关于IEntitiy的操作
	// public static String GetKeyCondition(IEntity en)
	// {
	// return null;
	// }
	// public static String RetrieveAll(IEntity en)
	// {
	// return null;
	// }
	// public static String Retrieve(IEntity en)
	// {
	// return null;
	// }
	// public static String Insert(IEntity en)
	// {
	// return null;
	// }
	// public static String Delete(IEntity en)
	// {
	// return null;
	// }
	// public static String Update(IEntity en)
	// {
	// return null;
	// }
	
	// GetKeyCondition
	
	/**
	 * 得到主健
	 * 
	 * @param en
	 * @return
	 */
	public static String GetKeyConditionOfMS(Entity en)
	{
		if (en.getPKField().equals("OID"))
		{
			return " OID=:OID";
		} else if (en.getPKField().equals("No"))
		{
			return " No=:No";
		} else if (en.getPKField().equals("MyPK"))
		{
			return " MyPK=:MyPK";
		} else
		{
		}
		
		if (en.getEnMap().getAttrs().Contains("OID"))
		{
			int key = en.GetValIntByKey("OID");
			if (key == 0)
			{
				throw new RuntimeException("@在执行[" + en.getEnMap().getEnDesc()
						+ " " + en.getEnMap().getPhysicsTable()
						+ "]，没有给PK OID 赋值,不能进行查询操作。");
			}
			// if (en.PKs.Length == 1)
			return " OID=:OID" + key;
		}
		
		String sql = " (1=1) ";
		for (Attr attr : en.getEnMap().getAttrs())
		{
			if (attr.getMyFieldType() == FieldType.PK
					|| attr.getMyFieldType() == FieldType.PKFK
					|| attr.getMyFieldType() == FieldType.PKEnum)
			{
				if (attr.getMyDataType() == DataType.AppString)
				{
					String val = en.GetValByKey(attr.getKey()).toString();
					if (val == null || val.equals(""))
					{
						throw new RuntimeException("@在执行["
								+ en.getEnMap().getEnDesc() + " "
								+ en.getEnMap().getPhysicsTable() + "]没有给PK "
								+ attr.getKey() + " 赋值,不能进行查询操作。");
					}
					sql = sql + " AND " + attr.getField() + "=:"
							+ attr.getKey();
					continue;
				}
				if (attr.getMyDataType() == DataType.AppInt)
				{
					if (en.GetValIntByKey(attr.getKey()) == 0
							&& attr.getKey().equals("OID"))
					{
						throw new RuntimeException("@在执行["
								+ en.getEnMap().getEnDesc() + " "
								+ en.getEnMap().getPhysicsTable() + "]，没有给PK "
								+ attr.getKey() + " 赋值,不能进行查询操作。");
					}
					sql = sql + " AND " + attr.getField() + "=:"
							+ attr.getKey();
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
	 */
	public static String GetKeyConditionOfOLE(Entity en)
	{
		// 判断特殊情况。
		if (en.getEnMap().getAttrs().Contains("OID"))
		{
			int key = en.GetValIntByKey("OID");
			if (key == 0)
			{
				throw new RuntimeException("@在执行[" + en.getEnMap().getEnDesc()
						+ " " + en.getEnMap().getPhysicsTable()
						+ "]，没有给PK OID 赋值,不能进行查询操作。");
			}
			
			if (en.getPKs().length == 1)
			{
				return en.getEnMap().getPhysicsTable() + ".OID=" + key;
			}
		}
		// if (en.getEnMap().getAttrs().Contains("MID"))
		// {
		// int key=en.GetValIntByKey("MID");
		// if (key==0)
		// throw new Exception("@在执行["+en.EnMap.EnDesc+ " "
		// +en.getEnMap().getPhysicsTable() +"]，没有给PK MID 赋值,不能进行查询操作。");
		// return en.getEnMap().getPhysicsTable()+".MID="+key ;
		// }
		
		String sql = " (1=1) ";
		for (Attr attr : en.getEnMap().getAttrs())
		{
			if (attr.getMyFieldType() == FieldType.PK
					|| attr.getMyFieldType() == FieldType.PKFK
					|| attr.getMyFieldType() == FieldType.PKEnum)
			{
				if (attr.getMyDataType() == DataType.AppString)
				{
					String val = en.GetValByKey(attr.getKey()).toString();
					if (val == null || val.equals(""))
					{
						throw new RuntimeException("@在执行["
								+ en.getEnMap().getEnDesc() + " "
								+ en.getEnMap().getPhysicsTable() + "]没有给PK "
								+ attr.getKey() + " 赋值,不能进行查询操作。");
					}
					sql = sql + " AND " + en.getEnMap().getPhysicsTable()
							+ ".[" + attr.getField() + "]='"
							+ en.GetValByKey(attr.getKey()).toString() + "'";
					continue;
				}
				
				if (attr.getMyDataType() == DataType.AppInt)
				{
					if (en.GetValIntByKey(attr.getKey()) == 0
							&& attr.getKey().equals("OID"))
					{
						throw new RuntimeException("@在执行["
								+ en.getEnMap().getEnDesc() + " "
								+ en.getEnMap().getPhysicsTable() + "]，没有给PK "
								+ attr.getKey() + " 赋值,不能进行查询操作。");
					}
					sql = sql + " AND " + en.getEnMap().getPhysicsTable()
							+ ".[" + attr.getField() + "]="
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
	 */
	public static String GenerWhereByPK(Entity en, String dbStr)
	{
		if (en.getPKCount() == 1)
		{
			if (dbStr.equals("?"))
			{
				return en.getEnMap().getPhysicsTable() + "." + en.getPKField()
						+ "=" + dbStr;
			} else
			{
				return en.getEnMap().getPhysicsTable() + "." + en.getPKField()
						+ "=" + dbStr + en.getPK();
			}
		}
		
		String sql = " (1=1) ";
		for (Attr attr : en.getEnMap().getAttrs())
		{
			if (attr.getMyFieldType() == FieldType.PK
					|| attr.getMyFieldType() == FieldType.PKFK
					|| attr.getMyFieldType() == FieldType.PKEnum)
			{
				if (dbStr.equals("?"))
				{
					sql = sql + " AND " + attr.getField() + "=" + dbStr;
				} else
				{
					sql = sql + " AND " + attr.getField() + "=" + dbStr
							+ attr.getField();
				}
			}
		}
		return sql;
	}
	
	/**
	 * @param en
	 * @return
	 */
	public static Paras GenerParasPK(Entity en)
	{
		Paras paras = new Paras();
		String pk = en.getPK();
		if (pk.equals("OID"))
		{
			paras.Add("OID", en.GetValIntByKey("OID"));
			return paras;
		}
		
		if (pk.equals("No"))
		{
			paras.Add("No", en.GetValStrByKey("No"));
			return paras;
		}
		
		if (pk.equals("MyPK"))
		{
			paras.Add("MyPK", en.GetValStrByKey("MyPK"));
			return paras;
		}
		if (pk.equals("NodeID"))
		{
			paras.Add("NodeID", en.GetValIntByKey("NodeID"));
			return paras;
		}
		
		if (pk.equals("WorkID"))
		{
			paras.Add("WorkID", en.GetValIntByKey("WorkID"));
			return paras;
		}
		 
		// if (pk == "ID")
		// {
		// paras.Add("ID", en.GetValStrByKey(EntityTreeAttr.No));
		// return paras;
		// }
		
		for (Attr attr : en.getEnMap().getAttrs())
		{
			if (attr.getMyFieldType() == FieldType.PK
					|| attr.getMyFieldType() == FieldType.PKFK
					|| attr.getMyFieldType() == FieldType.PKEnum)
			{
				if (attr.getMyDataType() == DataType.AppString)
				{
					paras.Add(attr.getKey(), en.GetValByKey(attr.getKey())
							.toString());
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
	
	public static String GetKeyConditionOfOraForPara(Entity en)
	{
		// 不能删除物理表名称，会引起未定义列。
		
		// 判断特殊情况,
		if (en.getPK().equals("OID"))
		{
			return en.getEnMap().getPhysicsTable() + ".OID="
					+ en.getHisDBVarStr() + "OID";
		} else if (en.getPK().equals("ID"))
		{
			return en.getEnMap().getPhysicsTable() + ".ID="
					+ en.getHisDBVarStr() + "ID";
		} else if (en.getPK().equals("No"))
		{
			return en.getEnMap().getPhysicsTable() + ".No="
					+ en.getHisDBVarStr() + "No";
		} else if (en.getPK().equals("MyPK"))
		{
			return en.getEnMap().getPhysicsTable() + ".MyPK="
					+ en.getHisDBVarStr() + "MyPK";
		} else
		{
		}
		
		String sql = " (1=1) ";
		for (Attr attr : en.getEnMap().getAttrs())
		{
			if (attr.getMyFieldType() == FieldType.PK
					|| attr.getMyFieldType() == FieldType.PKFK
					|| attr.getMyFieldType() == FieldType.PKEnum)
			{
				if (attr.getMyDataType() == DataType.AppString)
				{
					sql = sql + " AND " + en.getEnMap().getPhysicsTable() + "."
							+ attr.getField() + "=" + en.getHisDBVarStr()
							+ attr.getKey();
					continue;
				}
				if (attr.getMyDataType() == DataType.AppInt)
				{
					sql = sql + " AND " + en.getEnMap().getPhysicsTable() + "."
							+ attr.getField() + "=" + en.getHisDBVarStr()
							+ attr.getKey();
					continue;
				}
			}
		}
		return sql.substring((new String(" (1=1)  AND ")).length());
	}
	
	public static String GetKeyConditionOfInformixForPara(Entity en)
	{
		// 不能删除物理表名称，会引起未定义列。
		if (en.getPK().equals("OID"))
		{
			return en.getEnMap().getPhysicsTable() + ".OID=?";
		} else if (en.getPK().equals("No"))
		{
			return en.getEnMap().getPhysicsTable() + ".No=?";
		} else if (en.getPK().equals("MyPK"))
		{
			return en.getEnMap().getPhysicsTable() + ".MyPK=?";
		} else if (en.getPK().equals("ID"))
		{
			return en.getEnMap().getPhysicsTable() + ".ID=?";
		} else
		{
		}
		
		String sql = " (1=1) ";
		for (Attr attr : en.getEnMap().getAttrs())
		{
			if (attr.getMyFieldType() == FieldType.PK
					|| attr.getMyFieldType() == FieldType.PKFK
					|| attr.getMyFieldType() == FieldType.PKEnum)
			{
				if (attr.getMyDataType() == DataType.AppString)
				{
					sql = sql + " AND " + en.getEnMap().getPhysicsTable() + "."
							+ attr.getField() + "=?";
					continue;
				}
				if (attr.getMyDataType() == DataType.AppInt)
				{
					sql = sql + " AND " + en.getEnMap().getPhysicsTable() + "."
							+ attr.getField() + "=?";
					continue;
				}
			}
		}
		return sql.substring((new String(" (1=1)  AND ")).length());
	}
	
	public static String GetKeyConditionOfMySQL(Entity en)
	{
		// 不能删除物理表名称，会引起未定义列。
		if (en.getPK().equals("OID"))
		{
			return en.getEnMap().getPhysicsTable() + ".OID=:OID";
		} else if (en.getPK().equals("No"))
		{
			return en.getEnMap().getPhysicsTable() + ".No=:No";
		} else if (en.getPK().equals("MyPK"))
		{
			return en.getEnMap().getPhysicsTable() + ".MyPK=:MyPK";
		} else if (en.getPK().equals("ID"))
		{
			return en.getEnMap().getPhysicsTable() + ".ID=:ID";
		} else
		{
		}
		
		String sql = " (1=1) ";
		for (Attr attr : en.getEnMap().getAttrs())
		{
			if (attr.getMyFieldType() == FieldType.PK
					|| attr.getMyFieldType() == FieldType.PKFK
					|| attr.getMyFieldType() == FieldType.PKEnum)
			{
				if (attr.getMyDataType() == DataType.AppString)
				{
					sql = sql + " AND " + en.getEnMap().getPhysicsTable() + "."
							+ attr.getField() + "=:" + attr.getField();
					continue;
				}
				if (attr.getMyDataType() == DataType.AppInt)
				{
					sql = sql + " AND " + en.getEnMap().getPhysicsTable() + "."
							+ attr.getField() + "=:" + attr.getField();
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
	public static String RetrieveAll(Entity en) throws Exception
	{
		return SqlBuilder.SelectSQL(en, SystemConfig.getTopNum());
	}
	
	// public static String getPort_GetSIDSql()
	// {
	// String sql = "";
	// switch (SystemConfig.getAppCenterDBType())
	// {
	// case MySQL:
	// sql = "SELECT SID FROM Port_Emp WHERE No="
	// + SystemConfig.getAppCenterDBVarStr()+"No";
	// break;
	// case MSSQL:
	// case Oracle:
	// sql = "SELECT SID FROM Port_Emp WHERE No="
	// + SystemConfig.getAppCenterDBVarStr() + "No";
	// break;
	// }
	// return sql;
	// }
	/**
	 * 查询
	 * 
	 * @param en
	 *            实体
	 * @return string
	 * @throws Exception 
	 */
	public static String Retrieve(Entity en) throws Exception
	{
		String sql = "";
		switch (en.getEnMap().getEnDBUrl().getDBType())
		{
			case MSSQL:
			case MySQL:
				sql = SqlBuilder.SelectSQLOfMS(en, 1) + "   AND ( "
						+ SqlBuilder.GenerWhereByPK(en, ":") + " )";
				break;
			case Access:
				// sql = SqlBuilder.SelectSQLOfOLE(en, 1) + "  AND ( " +
				// SqlBuilder.GetKeyConditionOfOLE(en,"@") + " )";
				sql = SqlBuilder.SelectSQLOfOLE(en, 1) + "  AND ( "
						+ SqlBuilder.GenerWhereByPK(en, ":") + " )";
				break;
			case Oracle:
			case Informix:
				sql = SqlBuilder.SelectSQLOfOra(en, 1) + "  AND ( "
						+ SqlBuilder.GenerWhereByPK(en, ":") + " )";
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
				sql = SqlBuilder.SelectSQLOfMS(en, 1) + " AND "
						+ SqlBuilder.GenerWhereByPK(en, ":");
				break;
			case MySQL:
				sql = SqlBuilder.SelectSQLOfMySQL(en, 1) + " AND "
						+ SqlBuilder.GenerWhereByPK(en, ":");
				break;
			case Oracle:
				sql = SqlBuilder.SelectSQLOfOra(en, 1) + "AND ("
						+ SqlBuilder.GenerWhereByPK(en, ":") + " )";
				break;
			case Informix:
				sql = SqlBuilder.SelectSQLOfInformix(en, 1) + " WHERE ("
						+ SqlBuilder.GenerWhereByPK(en, "?") + " )";
				break;
			case Access:
				sql = SqlBuilder.SelectSQLOfOLE(en, 1) + " AND "
						+ SqlBuilder.GenerWhereByPK(en, ":");
				break;
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
					return SqlBuilder.SelectSQLOfMS(en, 1)
							+ SqlBuilder.GetKeyConditionOfOraForPara(en);
				} else
				{
					return SqlBuilder.SelectSQLOfMS(en, 1) + "  AND ( "
							+ SqlBuilder.GetKeyConditionOfOraForPara(en) + " )";
				}
				/*
				 * warning return SqlBuilder.SelectSQLOfMS(en, 1) + "  AND ( " +
				 * SqlBuilder.GetKeyConditionOfMS(en) + " )";
				 */
			case Oracle:
			case Informix:
				if (en.getEnMap().getHisFKAttrs().size() == 0)
				{
					return SqlBuilder.SelectSQLOfOra(en, 1)
							+ SqlBuilder.GetKeyConditionOfOraForPara(en);
				} else
				{
					return SqlBuilder.SelectSQLOfOra(en, 1) + "  AND ( "
							+ SqlBuilder.GetKeyConditionOfOraForPara(en) + " )";
				}
			case DB2:
			default:
				throw new RuntimeException("还没有实现。");
		}
	}
	
	/**
	 * 产生Ora 的where.
	 * 
	 * @param en
	 * @return
	 */
	public static String GenerFormWhereOfOra_For9i(Entity en)
	{
		String from = " FROM " + en.getEnMap().getPhysicsTable();
		// string where="  ";
		String table = "";
		String tableAttr = "";
		String enTable = en.getEnMap().getPhysicsTable();
		for (Attr attr : en.getEnMap().getAttrs())
		{
			if (attr.getMyFieldType() == FieldType.Normal
					|| attr.getMyFieldType() == FieldType.PK
					|| attr.getMyFieldType() == FieldType.RefText)
			{
				continue;
			}
			
			if (attr.getMyFieldType() == FieldType.FK
					|| attr.getMyFieldType() == FieldType.PKFK)
			{
				Entity en1 = attr.getHisFKEn();
				;
				table = en1.getEnMap().getPhysicsTable();
				tableAttr = "" + en1.getEnMap().getPhysicsTable() + "_"
						+ attr.getKey() + "";
				from = from + " LEFT OUTER JOIN " + table + "   " + tableAttr
						+ " ON " + enTable + "." + attr.getField() + "="
						+ tableAttr + "."
						+ en1.getEnMap().GetFieldByKey(attr.getUIRefKeyValue());
				// where=where+" AND "+" ("+en.getEnMap().getPhysicsTable()+"."+attr.Field+"="+en1.getEnMap().getPhysicsTable()+"_"+attr.Key+"."+en1.getEnMap().getAttrs().GetFieldByKey(attr.UIRefKeyValue
				// )+" ) " ;
				continue;
			}
			if (attr.getMyFieldType() == FieldType.Enum
					|| attr.getMyFieldType() == FieldType.PKEnum)
			{
				// from= from+ " LEFT OUTER JOIN "+table+" AS "+tableAttr+
				// " ON "+enTable+"."+attr.Field+"="+tableAttr+"."+en1.getEnMap().getAttrs().GetFieldByKey(
				// attr.UIRefKeyValue );
				tableAttr = "Enum_" + attr.getKey();
				from = from
						+ " LEFT OUTER JOIN ( SELECT Lab, IntKey FROM Sys_Enum WHERE EnumKey='"
						+ attr.getUIBindKey() + "' )  Enum_" + attr.getKey()
						+ " ON " + enTable + "." + attr.getField() + "="
						+ tableAttr + ".IntKey ";
				// where=where+" AND  ( "+en.getEnMap().getPhysicsTable()+"."+attr.Field+"= Enum_"+attr.Key+".IntKey ) ";
			}
		}
		// from=from+", "+en.getEnMap().getPhysicsTable();
		// where="("+where+")";
		return from + "  WHERE (1=1) ";
	}
	
	public static String GenerFormOfOra(Entity en)
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
		
		// 产生枚举表列表。
		Attrs enumAttrs = en.getEnMap().getHisEnumAttrs();
		for (Attr attr : enumAttrs)
		{
			if (attr.getMyFieldType() == FieldType.RefText)
			{
				continue;
			}
			// string enumTable = "Enum_"+attr.Key;
			from += " (SELECT Lab, IntKey FROM Sys_Enum WHERE EnumKey='"
					+ attr.getUIBindKey() + "' )  Enum_" + attr.getKey() + " ,";
		}
		from = from.substring(0, from.length() - 1);
		return from;
	}
	
	public static String GenerFormWhereOfOra(Entity en)
	{
		String from = " FROM " + en.getEnMap().getPhysicsTable();
		
		if (en.getEnMap().getHisFKAttrs().size() == 0)
		{
			return from + " WHERE ";
		}
		
		String mytable = en.getEnMap().getPhysicsTable();
		from += ",";
		// 产生外键表列表。
		Attrs fkAttrs = en.getEnMap().getHisFKAttrs();
		for (Attr attr : fkAttrs)
		{
			if (attr == null || attr.getMyFieldType() == FieldType.RefText)
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
			if (attr == null || attr.getMyFieldType() == FieldType.RefText)
			{
				continue;
			}
			
			Entity en1 = attr.getHisFKEn(); // ClassFactory.GetEns(attr.UIBindKey).GetNewEntity;
			String fktable = "T" + attr.getKey();
			
			if (!isAddAnd)
			{
				if (attr.getMyDataType() == DataType.AppString)
				{
					where += "(  "
							+ mytable
							+ "."
							+ attr.getKey()
							+ "="
							+ fktable
							+ "."
							+ en1.getEnMap().GetFieldByKey(
									attr.getUIRefKeyValue()) + "  (+) )";
				} else
				{
					where += "(  "
							+ mytable
							+ "."
							+ attr.getKey()
							+ "="
							+ fktable
							+ "."
							+ en1.getEnMap().GetFieldByKey(
									attr.getUIRefKeyValue()) + "  (+) )";
				}
				
				isAddAnd = true;
			} else
			{
				if (attr.getMyDataType() == DataType.AppString)
				{
					where += " AND (  "
							+ mytable
							+ "."
							+ attr.getKey()
							+ "="
							+ fktable
							+ "."
							+ en1.getEnMap().GetFieldByKey(
									attr.getUIRefKeyValue()) + "  (+) )";
				} else
				{
					where += " AND (  "
							+ mytable
							+ "."
							+ attr.getKey()
							+ "="
							+ fktable
							+ "."
							+ en1.getEnMap().GetFieldByKey(
									attr.getUIRefKeyValue()) + "  (+) )";
				}
			}
		}
		
		where = where.replace("WHERE  AND", "WHERE");
		where = where.replace("WHERE AND", "WHERE");
		return from + where;
	}
	
	public static String GenerFormWhereOfInformix(Entity en)
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
			if (isAddAnd)
			{
				isAddAnd = false;
				where += " LEFT JOIN " + fktable + "  " + fktable + "_"
						+ attr.getKey() + "  ON " + mytable + "."
						+ attr.getKey() + "=" + fktable + "_" + attr.getKey()
						+ "."
						+ en1.getEnMap().GetFieldByKey(attr.getUIRefKeyValue());
			} else
			{
				where += " LEFT JOIN " + fktable + "  " + fktable + "_"
						+ attr.getKey() + "  ON " + mytable + "."
						+ attr.getKey() + "=" + fktable + "_" + attr.getKey()
						+ "."
						+ en1.getEnMap().GetFieldByKey(attr.getUIRefKeyValue());
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
	 */
	public static String GenerCreateTableSQLOfMS(Entity en)
	{
		if (en.getEnMap().getPhysicsTable().equals("")
				|| en.getEnMap().getPhysicsTable() == null)
		{
			return "DELETE FROM Sys_enum where enumkey='sdsf44a23'";
		}
		
		String sql = "CREATE TABLE  " + en.getEnMap().getPhysicsTable() + " ( ";
		Attrs attrs = en.getEnMap().getAttrs();
		if (attrs.size() == 0)
		{
			throw new RuntimeException("@" + en.getEnDesc()
					+ " , 没有属性集合 attrs.Count = 0 ,能执行数据表的创建.");
		}
		
		for (Attr attr : attrs)
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
						sql += "[" + attr.getField() + "]  NVARCHAR ("
								+ attr.getMaxLength() + ") NOT NULL,";
					} else
					{
						sql += "[" + attr.getField() + "]  NVARCHAR ("
								+ attr.getMaxLength() + ") NULL,";
					}
					break;
				case DataType.AppRate:
				case DataType.AppFloat:
				case DataType.AppMoney:
					sql += "[" + attr.getField() + "] FLOAT NULL,";
					break;
				case DataType.AppBoolean:
				case DataType.AppInt:
					if (attr.getIsPK())
					{
						try
						{
							// 说明这个是自动增长的列.
							if (attr.getUIBindKey().equals("1"))
							{
								sql += "[" + attr.getField()
										+ "] INT  primary key identity(1,1),";
							} else
							{
								sql += "[" + attr.getField()
										+ "] INT NOT NULL,";
							}
						} catch (Exception e)
						{
							sql += "[" + attr.getField() + "] INT NOT NULL,";
						}
					} else
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
	
	public static String GenerCreateTableSQLOf_OLE(Entity en)
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
							sql += "[" + attr.getField() + "]  varchar ("
									+ attr.getMaxLength() + ") NOT NULL,";
						} else
						{
							sql += "[" + attr.getField() + "]  varchar ("
									+ attr.getMaxLength() + ") NULL,";
						}
					} else
					{
						if (attr.getIsPK())
						{
							sql += "[" + attr.getField() + "]  text  NOT NULL,";
						} else
						{
							sql += "[" + attr.getField() + "] text,";
						}
					}
					break;
				case DataType.AppRate:
				case DataType.AppFloat:
				case DataType.AppMoney:
					sql += "[" + attr.getField() + "] float  NULL,";
					break;
				case DataType.AppBoolean:
				case DataType.AppInt:
					if (attr.getIsPK())
					{
						sql += "[" + attr.getField() + "] int NOT NULL,";
					} else
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
	
	public static String GenerCreateTableSQL(Entity en)
	{
		switch (DBAccess.getAppCenterDBType())
		{
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
	
	public static String DeleteSysEnumsSQL(String table, String key)
	{
		switch (DBAccess.getAppCenterDBType())
		{
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
	
	// public static String NewWorkSQL1(String table)
	// {
	// switch (DBAccess.getAppCenterDBType())
	// {
	// case Oracle:
	// case MSSQL:
	// return "SELECT OID,FlowEndNode FROM " +table
	// + " WHERE FlowStarter=" + SystemConfig.getAppCenterDBVarStr()
	// + "FlowStarter AND WFState=" + SystemConfig.getAppCenterDBVarStr() +
	// "WFState ";
	// case MySQL:
	// return "SELECT OID,FlowEndNode FROM " +table
	// + " WHERE FlowStarter=" + SystemConfig.getAppCenterDBVarStr()
	// + " AND WFState=" + SystemConfig.getAppCenterDBVarStr() ;
	//
	// default:
	// break;
	// }
	// return null;
	// }
	// public static String NewWorkSQL2(String table)
	// {
	// switch (DBAccess.getAppCenterDBType())
	// {
	// case Oracle:
	// case MSSQL:
	// return "SELECT OID,FlowEndNode FROM " + table
	// + " WHERE GuestNo=" + SystemConfig.getAppCenterDBVarStr() +
	// "GuestNo AND WFState="
	// + SystemConfig.getAppCenterDBVarStr() + "WFState ";
	// case MySQL:
	// return "SELECT OID,FlowEndNode FROM " + table
	// + " WHERE GuestNo=" + SystemConfig.getAppCenterDBVarStr() +
	// " AND WFState="
	// + SystemConfig.getAppCenterDBVarStr() ;
	//
	// default:
	// break;
	// }
	// return null;
	// }
	/**
	 * 生成sql.
	 * 
	 * @param en
	 * @return
	 */
	public static String GenerCreateTableSQLOfOra_OK(Entity en)
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
						sql += attr.getField() + " varchar ("
								+ attr.getMaxLength() + ") NOT NULL,";
					} else
					{
						sql += attr.getField() + " varchar ("
								+ attr.getMaxLength() + ") NULL,";
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
					
					if (attr.getIsPK())
					{
						if ("1".equals(attr.getUIBindKey()))
						{
							sql += attr.getField()
									+ " int  primary key identity(1,1),";
						} else
						{
							sql += attr.getField() + " int NOT NULL,";
						}
					} else
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
	
	public static String GenerCreateTableSQLOfInfoMix(Entity en)
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
							sql += attr.getField() + " lvarchar ("
									+ attr.getMaxLength() + "),";
						} else
						{
							sql += attr.getField() + " lvarchar ("
									+ attr.getMaxLength() + "),";
						}
					} else
					{
						if (attr.getIsPK())
						{
							sql += attr.getField() + " varchar ("
									+ attr.getMaxLength() + ") NOT NULL,";
						} else
						{
							sql += attr.getField() + " varchar ("
									+ attr.getMaxLength() + "),";
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
					if (attr.getIsPK())
					{
						if (attr.getUIBindKey().equals("1"))
						{
							sql += attr.getField() + "  Serial not null,";
						} else
						{
							sql += attr.getField() + " int8 NOT NULL,";
						}
					} else
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
	 * 生成sql.
	 * 
	 * @param en
	 * @return
	 */
	public static String GenerCreateTableSQLOfMySQL(Entity en)
	{
		if (en.getEnMap().getPhysicsTable() == null)
		{
			throw new RuntimeException("您没有为[" + en.getEnDesc() + "],设置物理表。");
		}
		
		if (en.getEnMap().getPhysicsTable().trim().length() == 0)
		{
			throw new RuntimeException("您没有为[" + en.getEnDesc() +", "+en.toString()+ "],设置物理表。");
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
						sql += attr.getField() + " NVARCHAR ("
								+ attr.getMaxLength() + ") NOT NULL,";
					} else
					{
						if (attr.getMaxLength() > 3000)
						{
							sql += attr.getField() + " TEXT NULL,";
						} else
						{
							sql += attr.getField() + " NVARCHAR ("
									+ attr.getMaxLength() + ") NULL,";
						}
					}
					break;
				case DataType.AppRate:
				case DataType.AppFloat:
				case DataType.AppMoney:
				case DataType.AppDouble:
					sql += attr.getField() + " float  NULL,";
					break;
				case DataType.AppBoolean:
				case DataType.AppInt:
					if (attr.getIsPK())
					{
						if ("1".equals(attr.getUIBindKey()))
						{
							sql += attr.getField()
									+ " int(4) primary key not null auto_increment,";
						} else
						{
							sql += attr.getField() + " int   NULL,";
						}
					} else
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
	
	public static String GenerFormWhereOfOra(Entity en, Map map)
	{
		String from = " FROM " + map.getPhysicsTable();
		// string where="  ";
		String table = "";
		String tableAttr = "";
		String enTable = map.getPhysicsTable();
		
		for (Attr attr : map.getAttrs())
		{
			if (attr.getMyFieldType() == FieldType.Normal
					|| attr.getMyFieldType() == FieldType.PK
					|| attr.getMyFieldType() == FieldType.RefText)
			{
				continue;
			}
			
			if (attr.getMyFieldType() == FieldType.FK
					|| attr.getMyFieldType() == FieldType.PKFK)
			{
				// Entity en1= ClassFactory.GetEns(attr.UIBindKey).GetNewEntity;
				Entity en1 = attr.getHisFKEn();
				
				table = en1.getEnMap().getPhysicsTable();
				tableAttr = "T" + attr.getKey() + "";
				from = from + " LEFT OUTER JOIN " + table + "   " + tableAttr
						+ " ON " + enTable + "." + attr.getField() + "="
						+ tableAttr + "."
						+ en1.getEnMap().GetFieldByKey(attr.getUIRefKeyValue());
				// where=where+" AND "+" ("+en.getEnMap().getPhysicsTable()+"."+attr.Field+"="+en1.getEnMap().getPhysicsTable()+"_"+attr.Key+"."+en1.getEnMap().getAttrs().GetFieldByKey(attr.UIRefKeyValue
				// )+" ) " ;
				continue;
			}
			if (attr.getMyFieldType() == FieldType.Enum
					|| attr.getMyFieldType() == FieldType.PKEnum)
			{
				// from= from+ " LEFT OUTER JOIN "+table+" AS "+tableAttr+
				// " ON "+enTable+"."+attr.Field+"="+tableAttr+"."+en1.getEnMap().getAttrs().GetFieldByKey(
				// attr.UIRefKeyValue );
				tableAttr = "Enum_" + attr.getKey();
				from = from
						+ " LEFT OUTER JOIN ( SELECT Lab, IntKey FROM Sys_Enum WHERE EnumKey='"
						+ attr.getUIBindKey() + "' )  Enum_" + attr.getKey()
						+ " ON " + enTable + "." + attr.getField() + "="
						+ tableAttr + ".IntKey ";
				// where=where+" AND  ( "+en.getEnMap().getPhysicsTable()+"."+attr.Field+"= Enum_"+attr.Key+".IntKey ) ";
			}
		}
		// from=from+", "+en.getEnMap().getPhysicsTable();
		// where="("+where+")";
		return from + "  WHERE (1=1) ";
	}
	
	public static String GenerFormWhereOfMS(Entity en)
	{
		String from = " FROM " + en.getEnMap().getPhysicsTable();
		
		if (en.getEnMap().getHisFKAttrs().size() == 0)
		{
			return from + " WHERE (1=1)";
		}
		
		String mytable = en.getEnMap().getPhysicsTable();
		Attrs fkAttrs = en.getEnMap().getAttrs();
		for (Attr attr : fkAttrs)
		{
			
			if (attr.getMyFieldType() == FieldType.RefText)
			{
				continue;
			}
			
			if (attr.getIsFK())
			{
				String fktable = attr.getHisFKEn().getEnMap().getPhysicsTable();
				Attr refAttr = attr.getHisFKEn().getEnMap()
						.GetAttrByKey(attr.getUIRefKeyValue());
				from += " LEFT JOIN " + fktable + " AS " + fktable + "_"
						+ attr.getKey() + " ON " + mytable + "."
						+ attr.getField() + "=" + fktable + "_"
						+ attr.getField() + "." + refAttr.getField();
			} else if (attr.getMyFieldType() == FieldType.BindTable)
			{
				
				from += " LEFT JOIN " + attr.getUIBindKey() + " AS "
						+ attr.getUIBindKey() + "_" + attr.getKey() + " ON "
						+ mytable + "." + attr.getField() + "="
						+ attr.getUIBindKey() + "_" + attr.getField() + "."
						+ attr.getUIRefKeyValue();
			}
		}
		return from + " WHERE (1=1) ";
	}
	
	/**
	 * GenerFormWhere
	 * 
	 * @param en
	 * @return
	 */
	public static String GenerFormWhereOfMS(Entity en, Map map)
	{
		String from = " FROM " + map.getPhysicsTable();
		// string where="  ";
		String table = "";
		String tableAttr = "";
		String enTable = map.getPhysicsTable();
		for (Attr attr : map.getAttrs())
		{
			if (attr.getMyFieldType() == FieldType.Normal
					|| attr.getMyFieldType() == FieldType.PK
					|| attr.getMyFieldType() == FieldType.RefText)
			{
				continue;
			}
			
			if (attr.getMyFieldType() == FieldType.FK
					|| attr.getMyFieldType() == FieldType.PKFK)
			{
				// Entity en1= ClassFactory.GetEns(attr.UIBindKey).GetNewEntity;
				Entity en1 = attr.getHisFKEn();
				
				table = en1.getEnMap().getPhysicsTable();
				tableAttr = en1.getEnMap().getPhysicsTable() + "_"
						+ attr.getKey();
				if (attr.getMyDataType() == DataType.AppInt)
				{
					from = from
							+ " LEFT OUTER JOIN "
							+ table
							+ " AS "
							+ tableAttr
							+ " ON ISNULL( "
							+ enTable
							+ "."
							+ attr.getField()
							+ ", "
							+ en.GetValIntByKey(attr.getKey())
							+ ")="
							+ tableAttr
							+ "."
							+ en1.getEnMap().GetFieldByKey(
									attr.getUIRefKeyValue());
				} else
				{
					from = from
							+ " LEFT OUTER JOIN "
							+ table
							+ " AS "
							+ tableAttr
							+ " ON ISNULL( "
							+ enTable
							+ "."
							+ attr.getField()
							+ ", '"
							+ en.GetValByKey(attr.getKey())
							+ "')="
							+ tableAttr
							+ "."
							+ en1.getEnMap().GetFieldByKey(
									attr.getUIRefKeyValue());
				}
				// where=where+" AND "+" ("+en.getEnMap().getPhysicsTable()+"."+attr.Field+"="+en1.getEnMap().getPhysicsTable()+"_"+attr.Key+"."+en1.getEnMap().getAttrs().GetFieldByKey(attr.UIRefKeyValue
				// )+" ) " ;
				continue;
			}
			if (attr.getMyFieldType() == FieldType.Enum
					|| attr.getMyFieldType() == FieldType.PKEnum)
			{
				// from= from+ " LEFT OUTER JOIN "+table+" AS "+tableAttr+
				// " ON "+enTable+"."+attr.Field+"="+tableAttr+"."+en1.getEnMap().getAttrs().GetFieldByKey(
				// attr.UIRefKeyValue );
				tableAttr = "Enum_" + attr.getKey();
				from = from
						+ " LEFT OUTER JOIN ( SELECT Lab, IntKey FROM Sys_Enum WHERE EnumKey='"
						+ attr.getUIBindKey() + "' )  Enum_" + attr.getKey()
						+ " ON ISNULL( " + enTable + "." + attr.getField()
						+ ", " + en.GetValIntByKey(attr.getKey()) + ")="
						+ tableAttr + ".IntKey ";
				// where=where+" AND  ( "+en.getEnMap().getPhysicsTable()+"."+attr.Field+"= Enum_"+attr.Key+".IntKey ) ";
			}
		}
		// from=from+", "+en.getEnMap().getPhysicsTable();
		// where="("+where+")";
		return from + "  WHERE (1=1) ";
	}
	
	/**
	 * GenerFormWhere
	 * 
	 * @param en
	 * @return
	 */
	protected static String GenerFormWhereOfMSOLE(Entity en)
	{
		String fromTop = en.getEnMap().getPhysicsTable();
		
		String from = "";
		// string where="  ";
		String table = "";
		String tableAttr = "";
		String enTable = en.getEnMap().getPhysicsTable();
		for (Attr attr : en.getEnMap().getAttrs())
		{
			if (attr.getMyFieldType() == FieldType.Normal
					|| attr.getMyFieldType() == FieldType.PK
					|| attr.getMyFieldType() == FieldType.RefText)
			{
				continue;
			}
			fromTop = "(" + fromTop;
			
			if (attr.getMyFieldType() == FieldType.FK
					|| attr.getMyFieldType() == FieldType.PKFK)
			{
				Entity en1 = attr.getHisFKEn(); // ClassFactory.GetEns(attr.UIBindKey).GetNewEntity;
				table = en1.getEnMap().getPhysicsTable();
				tableAttr = en1.getEnMap().getPhysicsTable() + "_"
						+ attr.getKey();
				
				if (attr.getMyDataType() == DataType.AppInt)
				{
					from = from
							+ " LEFT OUTER JOIN "
							+ table
							+ " AS "
							+ tableAttr
							+ " ON IIf( ISNULL( "
							+ enTable
							+ "."
							+ attr.getField()
							+ "), "
							+ en.GetValIntByKey(attr.getKey())
							+ " , "
							+ enTable
							+ "."
							+ attr.getField()
							+ " )="
							+ tableAttr
							+ "."
							+ en1.getEnMap().GetFieldByKey(
									attr.getUIRefKeyValue());
				} else
				{
					from = from
							+ " LEFT OUTER JOIN "
							+ table
							+ " AS "
							+ tableAttr
							+ " ON IIf( ISNULL( "
							+ enTable
							+ "."
							+ attr.getField()
							+ "), '"
							+ en.GetValStringByKey(attr.getKey())
							+ "', "
							+ enTable
							+ "."
							+ attr.getField()
							+ " )="
							+ tableAttr
							+ "."
							+ en1.getEnMap().GetFieldByKey(
									attr.getUIRefKeyValue());
				}
			}
			if (attr.getMyFieldType() == FieldType.Enum
					|| attr.getMyFieldType() == FieldType.PKEnum)
			{
				// from= from+ " LEFT OUTER JOIN "+table+" AS "+tableAttr+
				// " ON "+enTable+"."+attr.Field+"="+tableAttr+"."+en1.getEnMap().getAttrs().GetFieldByKey(
				// attr.UIRefKeyValue );
				tableAttr = "Enum_" + attr.getKey();
				from = from
						+ " LEFT OUTER JOIN ( SELECT Lab, IntKey FROM Sys_Enum WHERE EnumKey='"
						+ attr.getUIBindKey() + "' )  Enum_" + attr.getKey()
						+ " ON IIf( ISNULL( " + enTable + "." + attr.getField()
						+ "), " + en.GetValIntByKey(attr.getKey()) + ", "
						+ enTable + "." + attr.getField() + ")=" + tableAttr
						+ ".IntKey ";
				// where=where+" AND  ( "+en.getEnMap().getPhysicsTable()+"."+attr.Field+"= Enum_"+attr.Key+".IntKey ) ";
			}
			
			from = from + ")";
		}
		fromTop = " FROM " + fromTop;
		// from=from+", "+en.getEnMap().getPhysicsTable();
		// where="("+where+")";
		return fromTop + from + "  WHERE (1=1) ";
	}
	
	protected static String SelectSQLOfOra(Entity en, int topNum) throws Exception
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
					if (attr.getDefaultVal() == null || tempVar == null
							|| tempVar.equals(""))
					{
						if (attr.getIsKeyEqualField())
						{
							val = val + "," + mainTable + attr.getField();
						} else
						{
							val = val + "," + mainTable + attr.getField() + ""
									+ attr.getKey();
						}
					} else
					{
						val = val + ",NVL(" + mainTable + attr.getField()
								+ ", '" + attr.getDefaultVal() + "') "
								+ attr.getKey();
					}
					
					if (attr.getMyFieldType() == FieldType.FK
							|| attr.getMyFieldType() == FieldType.PKFK)
					{
						Map map = attr.getHisFKEn().getEnMap();
						val = val + ", T" + attr.getKey() + "."
								+ map.GetFieldByKey(attr.getUIRefKeyText())
								+ " AS " + attr.getKey() + "Text";
					}
					break;
				case DataType.AppInt:
					
					val = val + ",NVL(" + mainTable + attr.getField() + ","
							+ attr.getDefaultVal() + ")   " + attr.getKey()
							+ "";
					
					if (attr.getMyFieldType() == FieldType.Enum
							|| attr.getMyFieldType() == FieldType.PKEnum)
					{
						if (StringHelper.isNullOrEmpty(attr.getUIBindKey()))
						{
							throw new RuntimeException("@" + en.toString()
									+ " key=" + attr.getKey() + " UITag="
									+ attr.UITag);
						}
						
						SysEnums ses = new BP.Sys.SysEnums(attr.getUIBindKey(),
								attr.UITag);
						val = val
								+ ","
								+ ses.GenerCaseWhenForOracle(en.toString(),
										mainTable, attr.getKey(), attr
												.getField(), attr
												.getUIBindKey(), Integer
												.parseInt(attr.getDefaultVal()
														.toString()));
					}
					if (attr.getMyFieldType() == FieldType.FK
							|| attr.getMyFieldType() == FieldType.PKFK)
					{
						Map map = attr.getHisFKEn().getEnMap();
						val = val + ", T" + attr.getKey() + "."
								+ map.GetFieldByKey(attr.getUIRefKeyText())
								+ "  AS " + attr.getKey() + "Text";
					}
					break;
				case DataType.AppFloat:
					val = val + ", NVL( round(" + mainTable + attr.getField()
							+ ",4) ," + attr.getDefaultVal().toString()
							+ ") AS  " + attr.getKey();
					break;
				case DataType.AppBoolean:
					if (attr.getDefaultVal().toString().equals("0"))
					{
						val = val + ", NVL( " + mainTable + attr.getField()
								+ ",0) " + attr.getKey();
					} else
					{
						val = val + ", NVL(" + mainTable + attr.getField()
								+ ",1) " + attr.getKey();
					}
					break;
				case DataType.AppDouble:
					val = val + ", NVL( round(" + mainTable + attr.getField()
							+ " ,4) ," + attr.getDefaultVal().toString() + ") "
							+ attr.getKey();
					break;
				case DataType.AppMoney:
					val = val + ", NVL( round(" + mainTable + attr.getField()
							+ ",4)," + attr.getDefaultVal().toString() + ") "
							+ attr.getKey();
					break;
				case DataType.AppDate:
				case DataType.AppDateTime:
					if (attr.getDefaultVal() == null
							|| attr.getDefaultVal().toString().equals(""))
					{
						val = val + "," + mainTable + attr.getField() + " "
								+ attr.getKey();
					} else
					{
						val = val + ",NVL(" + mainTable + attr.getField()
								+ ",'" + attr.getDefaultVal().toString()
								+ "') " + attr.getKey();
					}
					break;
				default:
					throw new RuntimeException("@没有定义的数据类型! attr="
							+ attr.getKey() + " MyDataType ="
							+ attr.getMyDataType());
			}
		}
		
		return " SELECT  " + val.substring(1)
				+ SqlBuilder.GenerFormWhereOfOra(en);
	}
	
	/**
	 * SelectSQLOfInformix
	 * 
	 * @param en
	 * @param topNum
	 * @return
	 * @throws Exception 
	 */
	protected static String SelectSQLOfInformix(Entity en, int topNum) throws Exception
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
					if (attr.getDefaultVal() == null || tempVar == null
							|| tempVar.equals(""))
					{
						if (attr.getIsKeyEqualField())
						{
							val = val + ", " + mainTable + attr.getField();
						} else
						{
							val = val + "," + mainTable + attr.getField() + " "
									+ attr.getKey();
						}
					} else
					{
						val = val + ",NVL(" + mainTable + attr.getField()
								+ ", '" + attr.getDefaultVal() + "') "
								+ attr.getKey();
					}
					
					if (attr.getMyFieldType() == FieldType.FK
							|| attr.getMyFieldType() == FieldType.PKFK)
					{
						Map map = attr.getHisFKEn().getEnMap();
						val = val + ", " + map.getPhysicsTable() + "_"
								+ attr.getKey() + "."
								+ map.GetFieldByKey(attr.getUIRefKeyText())
								+ " AS " + attr.getKey() + "Text";
					}
					break;
				case DataType.AppInt:
					
					val = val + ",NVL(" + mainTable + attr.getField() + ","
							+ attr.getDefaultVal() + ")   " + attr.getKey()
							+ "";
					
					if (attr.getMyFieldType() == FieldType.Enum
							|| attr.getMyFieldType() == FieldType.PKEnum)
					{
						if (StringHelper.isNullOrEmpty(attr.getUIBindKey()))
						{
							throw new RuntimeException("@" + en.toString()
									+ " key=" + attr.getKey() + " UITag="
									+ attr.UITag);
						}
						
						SysEnums ses = new BP.Sys.SysEnums(attr.getUIBindKey(),
								attr.UITag);
						val = val
								+ ","
								+ ses.GenerCaseWhenForOracle(en.toString(),
										mainTable, attr.getKey(), attr
												.getField(), attr
												.getUIBindKey(), Integer
												.parseInt(attr.getDefaultVal()
														.toString()));
					}
					if (attr.getMyFieldType() == FieldType.FK
							|| attr.getMyFieldType() == FieldType.PKFK)
					{
						Map map = attr.getHisFKEn().getEnMap();
						val = val + ", " + map.getPhysicsTable() + "_"
								+ attr.getKey() + "."
								+ map.GetFieldByKey(attr.getUIRefKeyText())
								+ "  AS " + attr.getKey() + "Text";
					}
					break;
				case DataType.AppFloat:
					val = val + ", NVL( round(" + mainTable + attr.getField()
							+ ",4) ," + attr.getDefaultVal().toString()
							+ ") AS  " + attr.getKey();
					break;
				case DataType.AppBoolean:
					if (attr.getDefaultVal().toString().equals("0"))
					{
						val = val + ", NVL( " + mainTable + attr.getField()
								+ ",0) " + attr.getKey();
					} else
					{
						val = val + ", NVL(" + mainTable + attr.getField()
								+ ",1) " + attr.getKey();
					}
					break;
				case DataType.AppDouble:
					val = val + ", NVL( round(" + mainTable + attr.getField()
							+ " ,4) ," + attr.getDefaultVal().toString() + ") "
							+ attr.getKey();
					break;
				case DataType.AppMoney:
					val = val + ", NVL( round(" + mainTable + attr.getField()
							+ ",4)," + attr.getDefaultVal().toString() + ") "
							+ attr.getKey();
					break;
				case DataType.AppDate:
				case DataType.AppDateTime:
					if (attr.getDefaultVal() == null
							|| attr.getDefaultVal().toString().equals(""))
					{
						val = val + "," + mainTable + attr.getField() + " "
								+ attr.getKey();
					} else
					{
						val = val + ",NVL(" + mainTable + attr.getField()
								+ ",'" + attr.getDefaultVal().toString()
								+ "') " + attr.getKey();
					}
					break;
				default:
					throw new RuntimeException("@没有定义的数据类型! attr="
							+ attr.getKey() + " MyDataType ="
							+ attr.getMyDataType());
			}
		}
		return " SELECT  " + val.substring(1)
				+ SqlBuilder.GenerFormWhereOfInformix(en);
	}
	
	public static String SelectSQL(Entity en, int topNum) throws Exception
	{
		switch (en.getEnMap().getEnDBUrl().getDBType())
		{
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
	 * @param top
	 *            top
	 * @return sql
	 * @throws Exception 
	 */
	public static String SelectCountSQL(Entity en) throws Exception
	{
		switch (en.getEnMap().getEnDBUrl().getDBType())
		{
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
	 */
	public static String SelectSQLOfOLE(Entity en, int topNum)
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
					val = val + ", IIf( ISNULL(" + mainTable + attr.getField()
							+ "), '" + attr.getDefaultVal().toString() + "', "
							+ mainTable + attr.getField() + " ) AS ["
							+ attr.getKey() + "]";
					if (attr.getMyFieldType() == FieldType.FK
							|| attr.getMyFieldType() == FieldType.PKFK)
					{
						Entity en1 = attr.getHisFKEn(); // ClassFactory.GetEns(attr.UIBindKey).GetNewEntity;
						val = val
								+ ", IIf( ISNULL("
								+ en1.getEnMap().getPhysicsTable()
								+ "_"
								+ attr.getKey()
								+ "."
								+ en1.getEnMap().GetFieldByKey(
										attr.getUIRefKeyText())
								+ ") ,'',"
								+ en1.getEnMap().getPhysicsTable()
								+ "_"
								+ attr.getKey()
								+ "."
								+ en1.getEnMap().GetFieldByKey(
										attr.getUIRefKeyText()) + ") AS "
								+ attr.getKey() + "Text";
					}
					break;
				case DataType.AppInt:
					val = val + ",IIf( ISNULL(" + mainTable + attr.getField()
							+ ")," + attr.getDefaultVal().toString() + ","
							+ mainTable + attr.getField() + ") AS ["
							+ attr.getKey() + "]";
					if (attr.getMyFieldType() == FieldType.Enum
							|| attr.getMyFieldType() == FieldType.PKEnum)
					{
						val = val + ",IIf( ISNULL( Enum_" + attr.getKey()
								+ ".Lab),'',Enum_" + attr.getKey()
								+ ".Lab ) AS " + attr.getKey() + "Text";
					}
					if (attr.getMyFieldType() == FieldType.FK
							|| attr.getMyFieldType() == FieldType.PKFK)
					{
						Entity en1 = attr.getHisFKEn(); // ClassFactory.GetEns(attr.UIBindKey).GetNewEntity;
						val = val
								+ ", IIf( ISNULL("
								+ en1.getEnMap().getPhysicsTable()
								+ "_"
								+ attr.getKey()
								+ "."
								+ en1.getEnMap().GetFieldByKey(
										attr.getUIRefKeyText())
								+ "),0 ,"
								+ en1.getEnMap().getPhysicsTable()
								+ "_"
								+ attr.getKey()
								+ "."
								+ en1.getEnMap().GetFieldByKey(
										attr.getUIRefKeyText()) + ") AS "
								+ attr.getKey() + "Text";
					}
					break;
				case DataType.AppFloat:
					val = val + ",IIf( ISNULL( Round(" + mainTable
							+ attr.getField() + ",2) ),"
							+ attr.getDefaultVal().toString() + "," + mainTable
							+ attr.getField() + ") AS [" + attr.getKey() + "]";
					break;
				case DataType.AppBoolean:
					if (attr.getDefaultVal().toString().equals("0"))
					{
						val = val + ", IIf( ISNULL(" + mainTable
								+ attr.getField() + "),0 ," + mainTable
								+ attr.getField() + ") AS [" + attr.getKey()
								+ "]";
					} else
					{
						val = val + ",IIf( ISNULL(" + mainTable
								+ attr.getField() + "),1," + mainTable
								+ attr.getField() + ") AS [" + attr.getKey()
								+ "]";
					}
					break;
				case DataType.AppDouble:
					val = val + ", IIf(ISNULL( Round(" + mainTable
							+ attr.getField() + ",4) ),"
							+ attr.getDefaultVal().toString() + "," + mainTable
							+ attr.getField() + ") AS [" + attr.getKey() + "]";
					break;
				case DataType.AppMoney:
					val = val + ",IIf( ISNULL(  Round(" + mainTable
							+ attr.getField() + ",2) ),"
							+ attr.getDefaultVal().toString() + "," + mainTable
							+ attr.getField() + ") AS [" + attr.getKey() + "]";
					break;
				case DataType.AppDate:
					val = val + ", IIf(ISNULL( " + mainTable + attr.getField()
							+ "), '" + attr.getDefaultVal().toString() + "',"
							+ mainTable + attr.getField() + ") AS ["
							+ attr.getKey() + "]";
					break;
				case DataType.AppDateTime:
					val = val + ", IIf(ISNULL(" + mainTable + attr.getField()
							+ "), '" + attr.getDefaultVal().toString() + "',"
							+ mainTable + attr.getField() + ") AS ["
							+ attr.getKey() + "]";
					break;
				default:
					throw new RuntimeException("@没有定义的数据类型! attr="
							+ attr.getKey() + " MyDataType ="
							+ attr.getMyDataType());
			}
		}
		if (topNum == -1 || topNum == 0)
		{
			topNum = 99999;
		}
		
		// return " SELECT TOP " +topNum.ToString()+" " +val.substing(1) +
		// " FROM "+en.getEnMap().getPhysicsTable();
		return " SELECT TOP " + (new Integer(topNum)).toString() + " "
				+ val.substring(1) + SqlBuilder.GenerFormWhereOfMSOLE(en);
	}
	
	public static String SelectSQLOfMS(Entity en, int topNum) throws Exception
	{
		String val = ""; // key = null;
		String mainTable = "";
		
		Map enMap = en.getEnMap();
		
		if (enMap.getHisFKAttrs().size() != 0)
		{
			mainTable = enMap.getPhysicsTable() + ".";
		}
		Attrs atts = enMap.getAttrs();
		for (Attr attr : atts)
		{
			if (attr.getMyFieldType() == FieldType.RefText)
			{
				continue;
			}
			switch (attr.getMyDataType())
			{
				case DataType.AppString:
					Object tempVar = attr.getDefaultVal();
					if (attr.getDefaultVal() == null || tempVar == null
							|| tempVar.equals(""))
					{
						if (attr.getIsKeyEqualField())
						{
							val = val + ", " + mainTable + attr.getField();
						} else
						{
							val = val + "," + mainTable + attr.getField() + " "
									+ attr.getKey();
						}
					} else
					{
						val = val + ",ISNULL(" + mainTable + attr.getField()
								+ ", '" + attr.getDefaultVal() + "') "
								+ attr.getKey();
					}
					
					if (attr.getMyFieldType() == FieldType.FK
							|| attr.getMyFieldType() == FieldType.PKFK)
					{
						Map map = attr.getHisFKEn().getEnMap();
						val = val + ", " + map.getPhysicsTable() + "_"
								+ attr.getKey() + "."
								+ map.GetFieldByKey(attr.getUIRefKeyText())
								+ " AS " + attr.getKey() + "Text";
					}
					if (attr.getMyFieldType() == FieldType.BindTable)
					{
						val = val + ", " + attr.getUIBindKey() + "_"
								+ attr.getKey() + "." + attr.getUIRefKeyText()
								+ " AS " + attr.getKey() + "Text";
					}
					break;
				case DataType.AppInt:
					if (attr.getIsNull())
					{
						val = val + "," + mainTable + attr.getField() + " "
								+ attr.getKey() + "";
					} else
					{
						val = val + ",ISNULL(" + mainTable + attr.getField()
								+ "," + attr.getDefaultVal() + ")   "
								+ attr.getKey() + "";
					}
					
					if (attr.getMyFieldType() == FieldType.Enum
							|| attr.getMyFieldType() == FieldType.PKEnum)
					{
						if (StringHelper.isNullOrEmpty(attr.getUIBindKey()))
						{
							throw new RuntimeException("@" + en.toString()
									+ " key=" + attr.getKey() + " UITag="
									+ attr.UITag + "");
						}
						
						// warning 20111-12-03 不应出现异常。
						if (attr.getUIBindKey().contains("."))
						{
							throw new RuntimeException("@" + en.toString()
									+ " &UIBindKey=" + attr.getUIBindKey()
									+ " @Key=" + attr.getKey());
						}
						
						SysEnums ses = new BP.Sys.SysEnums(attr.getUIBindKey(),
								attr.UITag);
						val = val
								+ ","
								+ ses.GenerCaseWhenForOracle(mainTable, attr
										.getKey(), attr.getField(), attr
										.getUIBindKey(), Integer.parseInt(attr
										.getDefaultVal().toString()));
					}
					
					if (attr.getMyFieldType() == FieldType.FK
							|| attr.getMyFieldType() == FieldType.PKFK)
					{
						Map map = attr.getHisFKEn().getEnMap();
						val = val + ", " + map.getPhysicsTable() + "_"
								+ attr.getKey() + "."
								+ map.GetFieldByKey(attr.getUIRefKeyText())
								+ "  AS " + attr.getKey() + "Text";
					}
					
					if (attr.getMyFieldType() == FieldType.BindTable)
					{
						val = val + ", " + attr.getUIBindKey() + "_"
								+ attr.getKey() + "." + attr.getUIRefKeyText()
								+ " AS " + attr.getKey() + "Text";
					}
					break;
				case DataType.AppFloat:
				case DataType.AppDouble:
				case DataType.AppMoney:
					if (attr.getIsNull())
					{
						val = val + "," + mainTable + attr.getField() + " "
								+ attr.getKey();
					} else
					// 需要四舍五入.
					{
						val = val + ", ISNULL( round(" + mainTable
								+ attr.getField() + ",4) ,"
								+ attr.getDefaultVal().toString() + ") AS  "
								+ attr.getKey();
					}
					break;
				case DataType.AppBoolean:
					if (attr.getDefaultVal().toString().equals("0"))
					{
						val = val + ", ISNULL( " + mainTable + attr.getField()
								+ ",0) " + attr.getKey();
					} else
					{
						val = val + ", ISNULL(" + mainTable + attr.getField()
								+ ",1) " + attr.getKey();
					}
					break;
				case DataType.AppDate:
				case DataType.AppDateTime:
					if (attr.getDefaultVal() == null
							|| attr.getDefaultVal().toString().equals(""))
					{
						val = val + "," + mainTable + attr.getField() + " "
								+ attr.getKey();
					} else
					{
						val = val + ",ISNULL(" + mainTable + attr.getField()
								+ ",'" + attr.getDefaultVal().toString()
								+ "') " + attr.getKey();
					}
					break;
				default:
					throw new RuntimeException("@没有定义的数据类型! attr="
							+ attr.getKey() + " MyDataType ="
							+ attr.getMyDataType());
			}
		}
		
		// return " SELECT TOP " +topNum.ToString()+" " +val.substing(1) +
		// " FROM "+en.getEnMap().getPhysicsTable();
		if (topNum == -1 || topNum == 0)
		{
			topNum = 99999;
		}
		return " SELECT  TOP " + (new Integer(topNum)).toString() + " "
				+ val.substring(1) + SqlBuilder.GenerFormWhereOfMS(en);
	}
	
	public static String SelectSQLOfMySQL(Entity en, int topNum) throws Exception
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
					if (attr.getDefaultVal() == null || tempVar == null
							|| tempVar.equals(""))
					{
						if (attr.getIsKeyEqualField())
						{
							val = val + ", " + mainTable + attr.getField();
						} else
						{
							val = val + "," + mainTable + attr.getField() + " "
									+ attr.getKey();
						}
					} else
					{
						val = val + ",IFNULL(" + mainTable + attr.getField()
								+ ", '" + attr.getDefaultVal() + "') "
								+ attr.getKey();
					}
					
					if (attr.getMyFieldType() == FieldType.FK
							|| attr.getMyFieldType() == FieldType.PKFK)
					{
						Map map = attr.getHisFKEn().getEnMap();
						val = val + ", " + map.getPhysicsTable() + "_"
								+ attr.getKey() + "."
								+ map.GetFieldByKey(attr.getUIRefKeyText())
								+ " AS " + attr.getKey() + "Text";
					}
					if (attr.getMyFieldType() == FieldType.BindTable)
					{
						val = val + ", " + attr.getUIBindKey() + "_"
								+ attr.getKey() + "." + attr.getUIRefKeyText()
								+ " AS " + attr.getKey() + "Text";
					}
					break;
				case DataType.AppInt:
					val = val + ",IFNULL(" + mainTable + attr.getField() + ","
							+ attr.getDefaultVal() + ")   " + attr.getKey()
							+ "";
					if (attr.getMyFieldType() == FieldType.Enum
							|| attr.getMyFieldType() == FieldType.PKEnum)
					{
						if (StringHelper.isNullOrEmpty(attr.getUIBindKey()))
						{
							throw new RuntimeException("@" + en.toString()
									+ " key=" + attr.getKey() + " UITag="
									+ attr.UITag);
						}
						
						// warning 2011-12-03 不应出现异常。
						if (attr.getUIBindKey().contains("."))
						{
							throw new RuntimeException("@" + en.toString()
									+ " &UIBindKey=" + attr.getUIBindKey()
									+ " @Key=" + attr.getKey());
						}
						
						SysEnums ses = new BP.Sys.SysEnums(attr.getUIBindKey(),
								attr.UITag);
						val = val
								+ ","
								+ ses.GenerCaseWhenForOracle(mainTable, attr
										.getKey(), attr.getField(), attr
										.getUIBindKey(), Integer.parseInt(attr
										.getDefaultVal().toString()));
					}
					
					if (attr.getMyFieldType() == FieldType.FK
							|| attr.getMyFieldType() == FieldType.PKFK)
					{
						Map map = attr.getHisFKEn().getEnMap();
						val = val + ", " + map.getPhysicsTable() + "_"
								+ attr.getKey() + "."
								+ map.GetFieldByKey(attr.getUIRefKeyText())
								+ "  AS " + attr.getKey() + "Text";
					}
					if (attr.getMyFieldType() == FieldType.BindTable)
					{
						val = val + ", " + attr.getUIBindKey() + "_"
								+ attr.getKey() + "." + attr.getUIRefKeyText()
								+ " AS " + attr.getKey() + "Text";
					}
					break;
				case DataType.AppFloat:
					val = val + ", IFNULL( round(" + mainTable
							+ attr.getField() + ",4) ,"
							+ attr.getDefaultVal().toString() + ") AS  "
							+ attr.getKey();
					break;
				case DataType.AppBoolean:
					if (attr.getDefaultVal().toString().equals("0"))
					{
						val = val + ", IFNULL( " + mainTable + attr.getField()
								+ ",0) " + attr.getKey();
					} else
					{
						val = val + ", IFNULL(" + mainTable + attr.getField()
								+ ",1) " + attr.getKey();
					}
					break;
				case DataType.AppDouble:
					val = val + ", IFNULL( round(" + mainTable
							+ attr.getField() + " ,4) ,"
							+ attr.getDefaultVal().toString() + ") "
							+ attr.getKey();
					break;
				case DataType.AppMoney:
					val = val + ", IFNULL( round(" + mainTable
							+ attr.getField() + ",4),"
							+ attr.getDefaultVal().toString() + ") "
							+ attr.getKey();
					break;
				case DataType.AppDate:
				case DataType.AppDateTime:
					if (attr.getDefaultVal() == null
							|| attr.getDefaultVal().toString().equals(""))
					{
						val = val + "," + mainTable + attr.getField() + " "
								+ attr.getKey();
					} else
					{
						val = val + ",IFNULL(" + mainTable + attr.getField()
								+ ",'" + attr.getDefaultVal().toString()
								+ "') " + attr.getKey();
					}
					break;
				default:
					throw new RuntimeException("@没有定义的数据类型! attr="
							+ attr.getKey() + " MyDataType ="
							+ attr.getMyDataType());
			}
		}
		
		return " SELECT   " + val.substring(1)
				+ SqlBuilder.GenerFormWhereOfMS(en);
	}
	
	/**
	 * 建立selectSQL
	 * 
	 * @param en
	 *            要执行的en
	 * @param topNum
	 *            最高查询个数
	 * @return 返回查询sql
	 */
	public static String SelectSQLOfMS_bak(Entity en, int topNum)
	{
		// 判断内存里面是否有 此sql.
		String sql = "";
		if (en.getEnMap().getDepositaryOfMap() == Depositary.None)
		{
			// 如果
			sql = SelectSQLOfMS(en.getEnMap());
		} else
		{
			Object tempVar = Cash.GetObj(en.toString() + "SQL", en.getEnMap()
					.getDepositaryOfMap());
			sql = (String) ((tempVar instanceof String) ? tempVar : null);
			
			if (sql == null)
			{
				sql = SelectSQLOfMS(en.getEnMap());
				// 把来之不易的sql 放入内存
				Cash.AddObj(en.toString() + "SQL", Depositary.Application, sql);
			}
		}
		
		// 替换他
		if (topNum == -1)
		{
			topNum = 99999;
		}
		
		sql = sql.replace("@TopNum", (new Integer(topNum)).toString());
		return sql + SqlBuilder.GenerFormWhereOfMS(en, en.getEnMap());
	}
	
	/**
	 * 建立selectSQL
	 * 
	 * @param en
	 *            要执行的en
	 * @param topNum
	 *            最高查询个数
	 * @return 返回查询sql
	 */
	public static String SelectCountSQLOfMS(Entity en)
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
						val = val + ", " + mainTable + attr.getField() + " AS "
								+ attr.getKey();
					} else
					{
						val = val + ", NVL(" + mainTable + attr.getField()
								+ ", '" + attr.getDefaultVal() + "') AS "
								+ attr.getKey();
					}
					
					if (attr.getMyFieldType() == FieldType.FK
							|| attr.getMyFieldType() == FieldType.PKFK)
					{
						Entity en1 = attr.getHisFKEn(); // ClassFactory.GetEns(attr.UIBindKey).GetNewEntity;
						val = val
								+ ", T"
								+ attr.getKey()
								+ "."
								+ en1.getEnMap().GetFieldByKey(
										attr.getUIRefKeyText()) + " AS "
								+ attr.getKey() + "Text";
					}
					break;
				case DataType.AppInt:
					val = val + ", NVL(" + mainTable + attr.getField() + ","
							+ attr.getDefaultVal() + ") AS  " + attr.getKey()
							+ "";
					if (attr.getMyFieldType() == FieldType.Enum
							|| attr.getMyFieldType() == FieldType.PKEnum)
					{
						SysEnums ses = new BP.Sys.SysEnums(attr.getUIBindKey(),
								attr.UITag);
						val = val
								+ ","
								+ ses.GenerCaseWhenForOracle(enName, mainTable,
										attr.getKey(), attr.getField(), attr
												.getUIBindKey(), Integer
												.parseInt(attr.getDefaultVal()
														.toString()));
					}
					if (attr.getMyFieldType() == FieldType.FK
							|| attr.getMyFieldType() == FieldType.PKFK)
					{
						Entity en1 = attr.getHisFKEn(); // ClassFactory.GetEns(attr.UIBindKey).GetNewEntity;
						val = val
								+ ", T"
								+ attr.getKey()
								+ "."
								+ en1.getEnMap().GetFieldByKey(
										attr.getUIRefKeyText()) + "  AS "
								+ attr.getKey() + "Text";
					}
					break;
				case DataType.AppFloat:
					val = val + ", NVL( ROUND(" + mainTable + attr.getField()
							+ ", 2 )," + attr.getDefaultVal().toString()
							+ ") AS  " + attr.getKey();
					break;
				case DataType.AppBoolean:
					if (attr.getDefaultVal().toString().equals("0"))
					{
						val = val + ", NVL( " + mainTable + attr.getField()
								+ " , 0)  AS " + attr.getKey();
					} else
					{
						val = val + ", NVL(  " + mainTable + attr.getField()
								+ ", 1)  AS " + attr.getKey();
					}
					break;
				case DataType.AppDouble:
					val = val + ", NVL( ROUND(" + mainTable + attr.getField()
							+ ",4)," + attr.getDefaultVal().toString()
							+ ") AS " + attr.getKey();
					break;
				case DataType.AppMoney:
					val = val + ", NVL( ROUND(" + mainTable + attr.getField()
							+ ",2)," + attr.getDefaultVal().toString()
							+ ") AS " + attr.getKey();
					break;
				case DataType.AppDate:
					val = val + ", NVL(  " + mainTable + attr.getField()
							+ ", '" + attr.getDefaultVal().toString()
							+ "')  AS " + attr.getKey();
					break;
				case DataType.AppDateTime:
					val = val + ", NVL(" + mainTable + attr.getField() + ", '"
							+ attr.getDefaultVal().toString() + "') AS "
							+ attr.getKey();
					break;
				default:
					throw new RuntimeException("@没有定义的数据类型! attr="
							+ attr.getKey() + " MyDataType ="
							+ attr.getMyDataType());
			}
		}
		return "SELECT " + val.substring(1);
	}
	
	/**
	 * 产生sql
	 * 
	 * @return
	 */
	public static String SelectSQLOfMS(Map map)
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
						val = val + " " + mainTable + attr.getField() + " AS ["
								+ attr.getKey() + "]";
					} else
					{
						val = val + ",ISNULL(" + mainTable + attr.getField()
								+ ", '" + attr.getDefaultVal().toString()
								+ "') AS [" + attr.getKey() + "]";
					}
					
					if (attr.getMyFieldType() == FieldType.FK
							|| attr.getMyFieldType() == FieldType.PKFK)
					{
						Entity en1 = attr.getHisFKEn();
						// Entity en1 =
						// ClassFactory.GetEns(attr.UIBindKey).GetNewEntity;
						val = val
								+ ","
								+ en1.getEnMap().getPhysicsTable()
								+ "_"
								+ attr.getKey()
								+ "."
								+ en1.getEnMap().GetFieldByKey(
										attr.getUIRefKeyText()) + " AS "
								+ attr.getKey() + "Text";
					}
					break;
				case DataType.AppInt:
					if (attr.getIsNull())
					{
						// 如果允许为空
						val = val + ", " + mainTable + attr.getField()
								+ " AS [" + attr.getKey() + "]";
					} else
					{
						val = val + ", ISNULL(" + mainTable + attr.getField()
								+ "," + attr.getDefaultVal().toString()
								+ ") AS [" + attr.getKey() + "]";
						if (attr.getMyFieldType() == FieldType.Enum
								|| attr.getMyFieldType() == FieldType.PKEnum)
						{
							val = val + ", Enum_" + attr.getKey() + ".Lab  AS "
									+ attr.getKey() + "Text";
						}
						if (attr.getMyFieldType() == FieldType.FK
								|| attr.getMyFieldType() == FieldType.PKFK)
						{
							// Entity en1=
							// ClassFactory.GetEns(attr.UIBindKey).GetNewEntity;
							Entity en1 = attr.getHisFKEn();
							val = val
									+ ", "
									+ en1.getEnMap().getPhysicsTable()
									+ "_"
									+ attr.getKey()
									+ "."
									+ en1.getEnMap().GetFieldByKey(
											attr.getUIRefKeyText()) + " AS "
									+ attr.getKey() + "Text";
						}
					}
					break;
				case DataType.AppFloat:
					val = val + ", ISNULL(" + mainTable + attr.getField() + ","
							+ attr.getDefaultVal().toString() + ") AS ["
							+ attr.getKey() + "]";
					break;
				case DataType.AppBoolean:
					if (attr.getDefaultVal().toString().equals("0"))
					{
						val = val + ", ISNULL(" + mainTable + attr.getField()
								+ ",0) AS [" + attr.getKey() + "]";
					} else
					{
						val = val + ", ISNULL(" + mainTable + attr.getField()
								+ ",1) AS [" + attr.getKey() + "]";
					}
					break;
				case DataType.AppDouble:
					val = val + ", ISNULL(" + mainTable + attr.getField() + ","
							+ attr.getDefaultVal().toString() + ") AS ["
							+ attr.getKey() + "]";
					break;
				case DataType.AppMoney:
					val = val + ", ISNULL(" + mainTable + attr.getField() + ","
							+ attr.getDefaultVal().toString() + ") AS ["
							+ attr.getKey() + "]";
					break;
				case DataType.AppDate:
					val = val + ", ISNULL(" + mainTable + attr.getField()
							+ ", '" + attr.getDefaultVal().toString()
							+ "') AS [" + attr.getKey() + "]";
					break;
				case DataType.AppDateTime:
					val = val + ", ISNULL(" + mainTable + attr.getField()
							+ ", '" + attr.getDefaultVal().toString()
							+ "') AS [" + attr.getKey() + "]";
					break;
				default:
					if (attr.getKey().equals("MyNum"))
					{
						val = val + ", COUNT(*)  AS MyNum ";
						break;
					} else
					{
						throw new RuntimeException("@没有定义的数据类型! attr="
								+ attr.getKey() + " MyDataType ="
								+ attr.getMyDataType());
					}
			}
		}
		return "SELECT TOP @TopNum " + val.substring(1);
	}
	
	public static String UpdateOfMSAccess(Entity en, String[] keys)
	{
		String val = "";
		for (Attr attr : en.getEnMap().getAttrs())
		{
			// 两个PK 的情况
			// if (attr.IsPK)
			// continue;
			
			if (keys != null)
			{
				boolean isHave = false;
				for (String s : keys)
				{
					if (attr.getKey().equals(s))
					{
						// 如果找到了要更新的key
						isHave = true;
						break;
					}
				}
				if (!isHave)
				{
					continue;
				}
			}
			
			if (attr.getMyFieldType() == FieldType.RefText
					|| attr.getMyFieldType() == FieldType.PK
					|| attr.getMyFieldType() == FieldType.PKFK
					|| attr.getMyFieldType() == FieldType.PKEnum)
			{
				continue;
			}
			
			switch (attr.getMyDataType())
			{
				case DataType.AppString:
					val = val + ",[" + attr.getField() + "]='"
							+ en.GetValStringByKey(attr.getKey()) + "'";
					break;
				case DataType.AppInt:
					val = val + ",[" + attr.getField() + "]="
							+ en.GetValStringByKey(attr.getKey());
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
					val = val + ",[" + attr.getField() + "]="
							+ en.GetValStringByKey(attr.getKey());
					break;
				case DataType.AppDate: // 如果是日期类型。
				case DataType.AppDateTime:
					String da = en.GetValStringByKey(attr.getKey());
					if (da.indexOf("_DATE") == -1)
					{
						val = val + ",[" + attr.getField() + "]='" + da + "'";
					} else
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
			// 如果没有出现要更新.
			for (Attr attr : en.getEnMap().getAttrs())
			{
				if (keys != null)
				{
					boolean isHave = false;
					for (String s : keys)
					{
						if (attr.getKey().equals(s))
						{
							// 如果找到了要更新的key
							isHave = true;
							break;
						}
					}
					if (!isHave)
					{
						continue;
					}
				}
				switch (attr.getMyDataType())
				{
					case DataType.AppString:
						val = val + ",[" + attr.getField() + "]='"
								+ en.GetValStringByKey(attr.getKey()) + "'";
						break;
					case DataType.AppInt:
						val = val + ",[" + attr.getField() + "]="
								+ en.GetValStringByKey(attr.getKey());
						break;
					case DataType.AppFloat:
					case DataType.AppDouble:
					case DataType.AppMoney:
						String str = en.GetValStringByKey(attr.getKey())
								.toString();
						str = str.replace("￥", "");
						str = str.replace(",", "");
						val = val + ",[" + attr.getField() + "]=" + str;
						break;
					case DataType.AppBoolean:
						val = val + ",[" + attr.getField() + "]="
								+ en.GetValStringByKey(attr.getKey());
						break;
					case DataType.AppDate: // 如果是日期类型。
					case DataType.AppDateTime:
						String da = en.GetValStringByKey(attr.getKey());
						if (da.indexOf("_DATE") == -1)
						{
							val = val + ",[" + attr.getField() + "]='" + da
									+ "'";
						} else
						{
							val = val + ",[" + attr.getField() + "]=" + da;
						}
						break;
					default:
						throw new RuntimeException(
								"@SqlBulider.update, 没有这个数据类型");
				}
			}
			// return null;
			// throw new Exception("出现了一个不合理的更新：没有要更新的数据。");
		}
		
		if (val.equals(""))
		{
			String ms = "";
			for (String str : keys)
			{
				ms += str;
			}
			throw new RuntimeException(en.getEnDesc() + "执行更新错误：无效的属性[" + ms
					+ "]对于本实体来说。");
		}
		sql = "UPDATE " + en.getEnMap().getPhysicsTable() + " SET "
				+ val.substring(1) + " WHERE "
				+ SqlBuilder.GetKeyConditionOfOLE(en);
		return sql.replace(",=''", "");
	}
	
	// public static String UPDATE_Sys_SerialSql()
	// {
	// String sql = "";
	// switch (SystemConfig.getAppCenterDBType())
	// {
	// case MySQL:
	// sql = "UPDATE Sys_Serial SET IntVal=IntVal+1 WHERE CfgKey="
	// + SystemConfig.getAppCenterDBVarStr() ;
	// break;
	// case MSSQL:
	// case Oracle:
	// sql = "UPDATE Sys_Serial SET IntVal=IntVal+1 WHERE CfgKey="
	// + SystemConfig.getAppCenterDBVarStr() + "CfgKey";
	// break;
	// }
	// return sql;
	// }
	// public static String INSERT_Sys_SerialSql(String cfgKey)
	// {
	// String sql = "";
	// switch (SystemConfig.getAppCenterDBType())
	// {
	// case MySQL:
	// sql = "INSERT INTO Sys_Serial (CFGKEY,INTVAL) VALUES ('" + cfgKey
	// + "',100)";
	// break;
	// case MSSQL:
	// case Oracle:
	// sql = "INSERT INTO Sys_Serial (CFGKEY,INTVAL) VALUES ('" + cfgKey
	// + "',100)";
	// break;
	// }
	// return sql;
	// }
	// public static String SELECT_Sys_SerialSql()
	// {
	// String sql = "";
	// switch (SystemConfig.getAppCenterDBType())
	// {
	// case MySQL:
	// sql = "SELECT IntVal FROM Sys_Serial WHERE CfgKey="
	// + SystemConfig.getAppCenterDBVarStr();
	// break;
	// case MSSQL:
	// case Oracle:
	// sql = "SELECT IntVal FROM Sys_Serial WHERE CfgKey="
	// + SystemConfig.getAppCenterDBVarStr() + "CfgKey";
	// break;
	// }
	// return sql;
	// }
	/**
	 * 产生要更新的sql 语句
	 * 
	 * @param en
	 *            要更新的entity
	 * @param keys
	 *            要更新的keys
	 * @return sql
	 */
	public static String Update(Entity en, String[] keys)
	{
		Map map = en.getEnMap();
		if (map.getAttrs().size() == 0)
		{
			throw new RuntimeException("@实体：" + en.toString()
					+ " ,Attrs属性集合信息丢失，导致无法生成SQL。");
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
						// 如果找到了要更新的key
						isHave = true;
						break;
					}
				}
				if (!isHave)
				{
					continue;
				}
			}
			
			if (attr.getMyFieldType() == FieldType.RefText)
			{
				continue;
			}
			
			// 有可能是两个主键的情况。
			// if (attr.IsPK)
			// continue;
			
			switch (attr.getMyDataType())
			{
				case DataType.AppString:
					if (map.getEnDBUrl().getDBType() == DBType.Access)
					{
						val = val
								+ ",["
								+ attr.getField()
								+ "]='"
								+ en.GetValStringByKey(attr.getKey()).replace(
										'\'', '~') + "'";
					} else
					{
						if (attr.getUIIsDoc() && attr.getKey().equals("Doc"))
						{
							
							String doc = en.GetValStringByKey(attr.getKey());
							if (map.getAttrs().Contains("DocLength"))
							{
								en.SetValByKey("DocLength", doc.length());
							}
							
							if (doc.length() >= 2000)
							{
								SysDocFile.SetValV2(en.toString(), en
										.getPKVal().toString(), doc);
								val = val + "," + attr.getField() + "=''";
							} else
							{
								val = val
										+ ","
										+ attr.getField()
										+ "='"
										+ en.GetValStringByKey(attr.getKey())
												.replace('\'', '~') + "'";
							}
						} else
						{
							val = val
									+ ","
									+ attr.getField()
									+ "='"
									+ en.GetValStringByKey(attr.getKey())
											.replace('\'', '~') + "'";
						}
					}
					break;
				case DataType.AppInt:
					val = val + "," + attr.getField() + "="
							+ en.GetValStringByKey(attr.getKey());
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
					val = val + "," + attr.getField() + "="
							+ en.GetValStringByKey(attr.getKey());
					break;
				case DataType.AppDate: // 如果是日期类型。
				case DataType.AppDateTime:
					String da = en.GetValStringByKey(attr.getKey());
					if (da.indexOf("_DATE") == -1)
					{
						val = val + "," + attr.getField() + "='" + da + "'";
					} else
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
			// 如果没有出现要更新.
			for (Attr attr : map.getAttrs())
			{
				if (keys != null)
				{
					boolean isHave = false;
					for (String s : keys)
					{
						if (attr.getKey().equals(s))
						{
							// 如果找到了要更新的key
							isHave = true;
							break;
						}
					}
					if (!isHave)
					{
						continue;
					}
				}
				
				// 两个PK 的情况。
				// if (attr.IsPK)
				// continue;
				
				switch (attr.getMyDataType())
				{
					case DataType.AppString:
						val = val + "," + attr.getField() + "='"
								+ en.GetValStringByKey(attr.getKey()) + "'";
						break;
					case DataType.AppInt:
						val = val + "," + attr.getField() + "="
								+ en.GetValStringByKey(attr.getKey());
						break;
					case DataType.AppFloat:
					case DataType.AppDouble:
					case DataType.AppMoney:
						String str = en.GetValStringByKey(attr.getKey())
								.toString();
						str = str.replace("￥", "");
						str = str.replace(",", "");
						val = val + "," + attr.getField() + "=" + str;
						break;
					case DataType.AppBoolean:
						val = val + "," + attr.getField() + "="
								+ en.GetValStringByKey(attr.getKey());
						break;
					case DataType.AppDate: // 如果是日期类型。
					case DataType.AppDateTime:
						String da = en.GetValStringByKey(attr.getKey());
						if (da.indexOf("_DATE") == -1)
						{
							val = val + "," + attr.getField() + "='" + da + "'";
						} else
						{
							val = val + "," + attr.getField() + "=" + da;
						}
						break;
					default:
						throw new RuntimeException(
								"@SqlBulider.update, 没有这个数据类型");
				}
			}
			// return null;
			// throw new Exception("出现了一个不合理的更新：没有要更新的数据。");
		}
		
		if (val.equals(""))
		{
			String ms = "";
			for (String str : keys)
			{
				ms += str;
			}
			throw new RuntimeException(en.getEnDesc() + "执行更新错误：无效的属性[" + ms
					+ "]对于本实体来说。");
		}
		
		switch (en.getEnMap().getEnDBUrl().getDBType())
		{
			case MSSQL:
			case Access:
				sql = "UPDATE " + en.getEnMap().getPhysicsTable() + " SET "
						+ val.substring(1) + " WHERE "
						+ SqlBuilder.GenerWhereByPK(en, ":");
				break;
			case MySQL:
				sql = "UPDATE " + en.getEnMap().getPhysicsTable() + " SET "
						+ val.substring(1) + " WHERE "
						+ SqlBuilder.GenerWhereByPK(en, "?");
				break;
			case Oracle:
				sql = "UPDATE " + en.getEnMap().getPhysicsTable() + " SET "
						+ val.substring(1) + " WHERE "
						+ SqlBuilder.GenerWhereByPK(en, ":");
				break;
			case Informix:
				sql = "UPDATE " + en.getEnMap().getPhysicsTable() + " SET "
						+ val.substring(1) + " WHERE "
						+ SqlBuilder.GenerWhereByPK(en, ":");
				break;
			default:
				throw new RuntimeException("no this case db type . ");
		}
		return sql.replace(",=''", "");
	}
	
	public static Paras GenerParas_Update_Informix(Entity en, String[] keys)
	{
		if (keys == null)
		{
			return GenerParas_Update_Informix(en);
		}
		
		String mykeys = "@";
		for (String key : keys)
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
			
			if (!mykeys.contains("@" + attr.getKey() + "@"))
			{
				continue;
			}
			
			switch (attr.getMyDataType())
			{
				case DataType.AppString:
					if (attr.getUIIsDoc() && attr.getKey().equals("Doc"))
					{
						String doc = en.GetValStrByKey(attr.getKey()).replace(
								'\'', '~');
						
						if (map.getAttrs().Contains("DocLength"))
						{
							en.SetValByKey("DocLength", doc.length());
						}
						
						if (doc.length() >= 2000)
						{
							SysDocFile.SetValV2(en.toString(), en.getPKVal()
									.toString(), doc);
							ps.Add(attr.getKey(), "");
						} else
						{
							ps.Add(attr.getKey(),
									en.GetValStrByKey(attr.getKey()).replace(
											'\'', '~'));
						}
					} else
					{
						ps.Add(attr.getKey(), en.GetValStrByKey(attr.getKey())
								.replace('\'', '~'));
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
					if (StringHelper.isNullOrEmpty(str))
					{
						ps.Add(attr.getKey(), 0);
					} else
					{
						ps.Add(attr.getKey(), new java.math.BigDecimal(str));
						/*
						 * warning ps.Add(attr.getKey(),
						 * java.math.BigDecimal.Parse(str));
						 */
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
		
		if (en.getPK().equals("OID") || en.getPK().equals("WorkID")
				|| en.getPK().equals("FID"))
		{
			ps.Add(en.getPK(), en.GetValIntByKey(en.getPK()));
		} else
		{
			ps.Add(en.getPK(), en.GetValStrByKey(en.getPK()));
		}
		return ps;
	}
	
	public static Paras GenerParas_Update_Informix(Entity en)
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
						String doc = en.GetValStrByKey(attr.getKey()).replace(
								'\'', '~');
						
						if (map.getAttrs().Contains("DocLength"))
						{
							en.SetValByKey("DocLength", doc.length());
						}
						
						if (doc.length() >= 2000)
						{
							SysDocFile.SetValV2(en.toString(), en.getPKVal()
									.toString(), doc);
							ps.Add(attr.getKey(), "");
						} else
						{
							ps.Add(attr.getKey(),
									en.GetValStrByKey(attr.getKey()).replace(
											'\'', '~'));
						}
					} else
					{
						ps.Add(attr.getKey(), en.GetValStrByKey(attr.getKey())
								.replace('\'', '~'));
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
					if (StringHelper.isNullOrEmpty(str))
					{
						ps.Add(attr.getKey(), 0);
					} else
					{
						ps.Add(attr.getKey(), new java.math.BigDecimal(str));
						/*
						 * warning ps.Add(attr.getKey(),
						 * java.math.BigDecimal.Parse(str));
						 */
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
		
		if (pk.equals("OID") || pk.equals("WorkID"))
		{
			ps.Add(en.getPK(), en.GetValIntByKey(pk));
		} else if (pk.equals("No") || pk.equals("MyPK") || pk.equals("ID"))
		{
			ps.Add(en.getPK(), en.GetValStrByKey(pk));
		} else
		{
			for (Attr attr : map.getAttrs())
			{
				if (!attr.getIsPK())
				{
					continue;
				}
				switch (attr.getMyDataType())
				{
					case DataType.AppString:
						ps.Add(attr.getKey(), en.GetValStrByKey(attr.getKey())
								.replace('\'', '~'));
						break;
					case DataType.AppInt:
						ps.Add(attr.getKey(), en.GetValIntByKey(attr.getKey()));
						break;
					default:
						throw new RuntimeException(
								"@SqlBulider.update, 没有这个数据类型...");
				}
			}
		}
		return ps;
	}
	
	public static Paras GenerParas(Entity en, String[] keys)
	{
		boolean IsEnableNull = BP.Sys.SystemConfig.getIsEnableNull();
		String mykeys = "@";
		if (keys != null)
		{
			for (String key : keys)
			{
				mykeys += key + "@";
			}
		}
		
		Map map = en.getEnMap();
		Paras ps = new Paras();
		for (Attr attr : map.getAttrs())
		{
			if (attr.getMyFieldType() == FieldType.RefText)
			{
				continue;
			}
			
			if (keys != null)
			{
				if (!mykeys.contains("@" + attr.getKey() + "@"))
				{
					if (!attr.getIsPK())
					{
						continue;
					}
				}
			}
			
			switch (attr.getMyDataType())
			{
				case DataType.AppString:
					if (attr.getUIIsDoc() && attr.getKey().equals("Doc"))
					{
						ps.Add(attr.getKey(), en.GetValStrByKey(attr.getKey())
								.replace('\'', '~'));
					} else
					{
						ps.Add(attr.getKey(), en.GetValStrByKey(attr.getKey())
								.replace('\'', '~'));
					}
					break;
				case DataType.AppBoolean:
					ps.Add(attr.getKey(), en.GetValIntByKey(attr.getKey()));
					break;
				case DataType.AppInt:
					if (attr.getKey().equals("MyPK")) // 特殊判断解决truck
														// 是64位的int类型的数值问题.
					{		
						ps.Add(attr.getKey(),
								en.GetValInt64ByKey(attr.getKey()));
					} else
					{
						if (IsEnableNull)
						{
							String s = en.GetValStrByKey(attr.getKey());
							if (StringHelper.isNullOrEmpty(s))
							{
								ps.Add(attr.getKey(), 0);
								// ps.AddDBNull(attr.getKey());//,
								// DBNull.Value);
							} else
							{
								ps.Add(attr.getKey(),
										en.GetValIntByKey(attr.getKey()));
							}
						} else
						{
							ps.Add(attr.getKey(),
									en.GetValIntByKey(attr.getKey()));
						}
					}
					break;
				case DataType.AppFloat:
				case DataType.AppDouble:
				case DataType.AppMoney:
					String str = en.GetValStrByKey(attr.getKey()).toString();
					str = str.replace("￥", "");
					str = str.replace(",", "");
					if (StringHelper.isNullOrEmpty(str))
					{
						if (IsEnableNull)
						{
							// ps.AddDBNull(attr.getKey());
							ps.Add(attr.getKey(), null, BigDecimal.class);
						} else
						{
							ps.Add(attr.getKey(), 0);
						}
					} else
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
		
		if (keys != null)
		{
			if (en.getPK().equals("OID") || en.getPK().equals("WorkID"))
			{
				ps.Add(en.getPK(), en.GetValIntByKey(en.getPK()));
			} else
			{
				ps.Add(en.getPK(), en.GetValStrByKey(en.getPK()));
			}
		}
		
		return ps;
	}
	
	public static String UpdateForPara(Entity en, String[] keys)
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
		// string dbstr = en.HisDBVarStr;
		Map map = en.getEnMap();
		String val = "";
		for (Attr attr : map.getAttrs())
		{
			if (attr.getMyFieldType() == FieldType.RefText || attr.getIsPK())
			{
				continue;
			}
			
			if (keys != null)
			{
				if (!mykey.contains("@" + attr.getKey() + ","))
				{
					continue;
				}
			}
			
			// if (attr.IsPK)
			// {
			// // #warning add 2009 - 11- 04
			// // 两个PK 的情况。
			// // continue;
			// if (en.PKCount == 1)
			// continue;
			// }
			// else
			// {
			// if (keys != null)
			// if (!mykey.Contains("@" + attr.Key+","))
			// continue;
			// }
			
			if (dbVarStr.equals("?"))
			{
				val = val + "," + attr.getField() + "=" + dbVarStr;
			} else
			{
				val = val + "," + attr.getField() + "=" + dbVarStr
						+ attr.getKey();
			}
		}
		if (StringHelper.isNullOrEmpty(val))
		{
			for (Attr attr : map.getAttrs())
			{
				if (attr.getMyFieldType() == FieldType.RefText)
				{
					continue;
				}
				
				if (keys != null)
				{
					if (!mykey.contains("@" + attr.getKey() + ","))
					{
						continue;
					}
				}
				
				val = val + "," + attr.getField() + "=" + attr.getField();
			}
			// throw new Exception("@生成SQL出现错误:" + map.EnDesc + "，" +
			// en.ToString() + "，要更新的字段为空。");
		}
		if (!StringHelper.isNullOrEmpty(val))
		{
			val = val.substring(1);
		}
		String sql = "";
		switch (en.getEnMap().getEnDBUrl().getDBType())
		{
			case MSSQL:
			case Access:
				sql = "UPDATE " + en.getEnMap().getPhysicsTable() + " SET "
						+ val + " WHERE " + SqlBuilder.GenerWhereByPK(en, ":");
				break;
			case MySQL:
				sql = "UPDATE " + en.getEnMap().getPhysicsTable() + " SET "
						+ val + " WHERE " + SqlBuilder.GenerWhereByPK(en, ":");
				break;
			case Informix:
				sql = "UPDATE " + en.getEnMap().getPhysicsTable() + " SET "
						+ val + " WHERE " + SqlBuilder.GenerWhereByPK(en, "?");
				break;
			case Oracle:
				sql = "UPDATE " + en.getEnMap().getPhysicsTable() + " SET "
						+ val + " WHERE " + SqlBuilder.GenerWhereByPK(en, ":");
				break;
			default:
				throw new RuntimeException("no this case db type . ");
		}
		return sql.replace(",=''", "");
	}
	
	/**
	 * Delete sql
	 * 
	 * @param en
	 * @return
	 */
	public static String DeleteForPara(Entity en)
	{
		String dbstr = en.getHisDBVarStr();
		String sql = "DELETE FROM " + en.getEnMap().getPhysicsTable()
				+ " WHERE " + SqlBuilder.GenerWhereByPK(en, dbstr);
		return sql;
	}
	
	public static String InsertForPara(Entity en)
	{
		String dbstr = en.getHisDBVarStr();
		if (dbstr.equals("?"))
		{
			return InsertForPara_Informix(en);
		}
		
		boolean isInnkey = false;
		if (en.getIsOIDEntity())
		{
			EntityOID myen = (EntityOID) ((en instanceof EntityOID) ? en : null);
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
		String sql = "INSERT INTO " + en.getEnMap().getPhysicsTable() + " ("
				+ field.substring(1) + " ) VALUES ( " + val.substring(1) + ")";
		return sql;
	}
	
	public static String InsertForPara_Informix(Entity en)
	{
		boolean isInnkey = false;
		if (en.getIsOIDEntity())
		{
			EntityOID myen = (EntityOID) ((en instanceof EntityOID) ? en : null);
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
		String sql = "INSERT INTO " + en.getEnMap().getPhysicsTable() + " ("
				+ field.substring(1) + " ) VALUES ( " + val.substring(1) + ")";
		return sql;
	}
	
	/**
	 * Insert
	 * 
	 * @param en
	 * @return
	 */
	public static String Insert(Entity en)
	{
		String key = "", field = "", val = "";
		Attrs attrs = en.getEnMap().getAttrs();
		if (attrs.size() == 0)
		{
			throw new RuntimeException("@实体：" + en.toString()
					+ " ,Attrs属性集合信息丢失，导致无法生成SQL。");
		}
		
		for (Attr attr : attrs)
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
					if (StringHelper.isNullOrEmpty(str))
					{
						str = "";
					} else
					{
						str = str.replace('\'', '~');
					}
					
					if (attr.getUIIsDoc() && attr.getKey().equals("Doc"))
					{
						if (attrs.Contains("DocLength"))
						{
							en.SetValByKey("DocLength", str.length());
						}
						
						if (str.length() >= 2000)
						{
							SysDocFile.SetValV2(en.toString(), en.getPKVal()
									.toString(), str);
							if (attrs.Contains("DocLength"))
							{
								en.SetValByKey("DocLength", str.length());
							}
							val = val + ",''";
						} else
						{
							val = val + ",'" + str + "'";
						}
					} else
					{
						val = val + ",'"
								+ en.GetValStringByKey(key).replace('\'', '~')
								+ "'";
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
					} else
					{
						val = val + "," + da;
					}
					break;
				default:
					throw new RuntimeException(
							"@bulider insert sql error: 没有这个数据类型");
			}
		}
		String sql = "INSERT INTO " + en.getEnMap().getPhysicsTable() + " ("
				+ field.substring(1) + " ) VALUES ( " + val.substring(1) + ")";
		return sql;
	}
	
	public static String InsertOFOLE(Entity en)
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
					} else
					{
						val = val + "," + da;
					}
					break;
				default:
					throw new RuntimeException(
							"@bulider insert sql error: 没有这个数据类型");
			}
		}
		String sql = "INSERT INTO " + en.getEnMap().getPhysicsTable() + " ("
				+ field.substring(1) + " ) VALUES ( " + val.substring(1) + ")";
		return sql;
	}
}