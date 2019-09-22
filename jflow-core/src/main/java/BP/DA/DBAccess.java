package BP.DA;

import Oracle.ManagedDataAccess.Client.*;
import MySql.Data.*;
import MySql.*;
import MySql.Data.MySqlClient.*;
import BP.Sys.*;
import java.util.*;
import java.io.*;
import java.nio.file.*;
import java.math.*;

/*
简介：负责存取数据的类
创建时间：2002-10
最后修改时间：2004-2-1 ccb

 说明：
  在次文件种处理了4种方式的连接。
  1， sql server .
  2， oracle.
  3， ole.
  4,  odbc.
  
*/

/** 
 数据库访问。
 这个类负责处理了 实体信息
*/
public class DBAccess
{
	/** 
	 是否大小写敏感
	*/
	public static boolean getIsCaseSensitive()
	{
		if (DBAccess.IsExitsObject("TEST") == true)
		{
			DBAccess.RunSQL("DROP TABLE TEST ");
		}
		if (DBAccess.IsExitsObject("test") == true)
		{
			DBAccess.RunSQL("DROP table test ");
		}

		String mysql = "CREATE TABLE TEST(OID int NOT NULL )";
		DBAccess.RunSQL(mysql);
		if (DBAccess.IsExitsObject("test") == false)
		{
			DBAccess.RunSQL("DROP TABLE TEST ");
			return true;
		}
		if (DBAccess.IsExitsTableCol("test", "oid") == false)
		{
			DBAccess.RunSQL("DROP TABLE TEST ");
			return true;
		}

		return false;

	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 文件存储数据库处理.
	/** 
	 保存文件到数据库 @shilianyu
	 
	 @param bytes 数据流
	 @param tableName 表名称
	 @param tablePK 表主键
	 @param pkVal 主键值
	 @param saveFileField 保存到字段
	*/
//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: public static void SaveBytesToDB(byte[] bytes, string tableName, string tablePK, object pkVal, string saveToFileField)
	public static void SaveBytesToDB(byte[] bytes, String tableName, String tablePK, Object pkVal, String saveToFileField)
	{
		if (BP.Sys.SystemConfig.getAppCenterDBType() == DBType.MSSQL)
		{
			IDbConnection tempVar = BP.DA.DBAccess.getGetAppCenterDBConn();
			SqlConnection cn = tempVar instanceof SqlConnection ? (SqlConnection)tempVar : null;
			if (cn.State != ConnectionState.Open)
			{
				cn.Open();
			}

			SqlCommand cm = new SqlCommand();
			cm.Connection = cn;
			cm.CommandType = CommandType.Text;
			if (cn.State == 0)
			{
				cn.Open();
			}
			cm.CommandText = "UPDATE " + tableName + " SET " + saveToFileField + "=@FlowJsonFile WHERE " + tablePK + " =@PKVal";

			SqlParameter spFile = new SqlParameter("@FlowJsonFile", SqlDbType.Image);
			spFile.Value = bytes;
			cm.Parameters.Add(spFile);

			SqlParameter spPK = new SqlParameter("@PKVal", SqlDbType.VarChar);
			spPK.Value = pkVal;
			cm.Parameters.Add(spPK);

			// 执行它.
			try
			{
				cm.ExecuteNonQuery();
			}
			catch (RuntimeException ex)
			{
				if (BP.DA.DBAccess.IsExitsTableCol(tableName, saveToFileField) == false)
				{
					/*如果没有此列，就自动创建此列.*/
					String sql = "ALTER TABLE " + tableName + " ADD  " + saveToFileField + " image ";
					BP.DA.DBAccess.RunSQL(sql);
					SaveBytesToDB(bytes, tableName, tablePK, pkVal, saveToFileField);
					return;
				}
				throw new RuntimeException("@缺少此字段[" + tableName + "," + saveToFileField + "],有可能系统自动修复." + ex.getMessage());
			}
			return;
		}

		//修复for：jlow  oracle 异常： ORA-01745: 无效的主机/绑定变量名 edited by qin 16.7.1
		//错误的引用oracle的关键字file
		if (BP.Sys.SystemConfig.getAppCenterDBType() == DBType.Oracle)
		{
			IDbConnection tempVar2 = BP.DA.DBAccess.getGetAppCenterDBConn();
			OracleConnection cn = tempVar2 instanceof OracleConnection ? (OracleConnection)tempVar2 : null;
			if (cn.State != ConnectionState.Open)
			{
				cn.Open();
			}

			OracleCommand cm = new OracleCommand();
			cm.Connection = cn;
			cm.CommandType = CommandType.Text;
			if (cn.State == 0)
			{
				cn.Open();
			}
			cm.CommandText = "UPDATE " + tableName + " SET " + saveToFileField + "=:FlowJsonFile WHERE " + tablePK + " =:PKVal";

			OracleParameter spFile = new OracleParameter("FlowJsonFile", OracleDbType.Blob);
			spFile.Value = bytes;
			cm.Parameters.Add(spFile);

			OracleParameter spPK = new OracleParameter("PKVal", OracleDbType.NVarchar2);
			spPK.Value = pkVal;
			cm.Parameters.Add(spPK);

			// 执行它.
			try
			{
				cm.ExecuteNonQuery();
			}
			catch (RuntimeException ex)
			{
				if (BP.DA.DBAccess.IsExitsTableCol(tableName, saveToFileField) == false)
				{
					/*如果没有此列，就自动创建此列.*/
					//修改数据类型   oracle 不存在image类型   edited by qin 16.7.1
					String sql = "ALTER TABLE " + tableName + " ADD  " + saveToFileField + " blob ";
					BP.DA.DBAccess.RunSQL(sql);
					SaveBytesToDB(bytes, tableName, tablePK, pkVal, saveToFileField);
					return;
				}


				throw new RuntimeException("@缺少此字段,有可能系统自动修复." + ex.getMessage());
			}
			return;
		}

		//add by zhoupeng
		if (BP.Sys.SystemConfig.getAppCenterDBType() == DBType.PostgreSQL)
		{
			IDbConnection tempVar3 = BP.DA.DBAccess.getGetAppCenterDBConn();
			Npgsql.NpgsqlConnection cn = tempVar3 instanceof Npgsql.NpgsqlConnection ? (Npgsql.NpgsqlConnection)tempVar3 : null;
			if (cn.State != ConnectionState.Open)
			{
				cn.Open();
			}

			Npgsql.NpgsqlCommand cm = new Npgsql.NpgsqlCommand();
			cm.Connection = cn;
			cm.CommandType = CommandType.Text;
			cm.CommandText = "UPDATE " + tableName + " SET " + saveToFileField + "=@FlowJsonFile WHERE " + tablePK + " =@PKVal";

			Npgsql.NpgsqlParameter spFile = new Npgsql.NpgsqlParameter("FlowJsonFile", NpgsqlTypes.NpgsqlDbType.Bytea);
			spFile.Value = bytes;
			cm.Parameters.Add(spFile);

			NpgsqlParameter spPK = null;
			if (tableName.contains("ND") == true || pkVal.getClass() == Integer.class || pkVal.getClass() == Long.class)
			{
				spPK = new NpgsqlParameter("PKVal", NpgsqlTypes.NpgsqlDbType.Integer);
				spPK.Value = Integer.parseInt(pkVal.toString());

			}
			else
			{
				spPK = new NpgsqlParameter("PKVal", NpgsqlTypes.NpgsqlDbType.Varchar);
				spPK.Value = pkVal;
			}

			//spPK.DbType= NpgsqlTypes.NpgsqlDbType.Integer.
			cm.Parameters.Add(spPK);

			try
			{
				cm.ExecuteNonQuery();
			}
			catch (RuntimeException ex)
			{
				if (BP.DA.DBAccess.IsExitsTableCol(tableName, saveToFileField) == false)
				{
					/*如果没有此列，就自动创建此列.*/
					String sql = "ALTER TABLE " + tableName + " ADD  " + saveToFileField + " bytea NULL ";
					BP.DA.DBAccess.RunSQL(sql);
					SaveBytesToDB(bytes, tableName, tablePK, pkVal, saveToFileField);
					return;
				}
				throw new RuntimeException("@NpgsqlDbType缺少此字段[" + tableName + "," + saveToFileField + "],有可能系统自动修复." + ex.getMessage());

			  //  throw new Exception("@缺少此字段,系统自动修复，请重试一次,错误信息:" + ex.Message);
			}
			return;
		}

		//added by liuxc,2016-12-7，增加对mysql大数据longblob字段存储逻辑
		if (BP.Sys.SystemConfig.getAppCenterDBType() == DBType.MySQL)
		{
			IDbConnection tempVar4 = BP.DA.DBAccess.getGetAppCenterDBConn();
			MySqlConnection cn = tempVar4 instanceof MySqlConnection ? (MySqlConnection)tempVar4 : null;

			if (cn.State != ConnectionState.Open)
			{
				cn.Open();
			}

			MySqlCommand cm = new MySqlCommand();
			cm.Connection = cn;
			cm.CommandType = CommandType.Text;
			cm.CommandText = "UPDATE " + tableName + " SET " + saveToFileField + "=@FlowJsonFile WHERE " + tablePK + " =@PKVal";

			MySqlParameter spFile = new MySqlParameter("FlowJsonFile", MySqlDbType.Blob);
			spFile.Value = bytes;
			cm.Parameters.Add(spFile);

			MySqlParameter spPK = new MySqlParameter("PKVal", MySqlDbType.VarChar);
			spPK.Value = pkVal;
			cm.Parameters.Add(spPK);

			try
			{
				cm.ExecuteNonQuery();
			}
			catch (RuntimeException ex)
			{
				if (BP.DA.DBAccess.IsExitsTableCol(tableName, saveToFileField) == false)
				{
					/*如果没有此列，就自动创建此列.*/
					String sql = "ALTER TABLE " + tableName + " ADD  " + saveToFileField + " BLOB NULL ";
					BP.DA.DBAccess.RunSQL(sql);
					SaveBytesToDB(bytes, tableName, tablePK, pkVal, saveToFileField);
					return;
				}

				throw new RuntimeException("@缺少此字段,有可能系统自动修复." + ex.getMessage());
			}
			return;
		}
	}
	/** 
	 保存文件到数据库
	 
	 @param fullFileName 完成的文件路径
	 @param tableName 表名称
	 @param tablePK 表主键
	 @param pkVal 主键值
	 @param saveFileField 保存到字段
	*/
	public static void SaveBigTextToDB(String docs, String tableName, String tablePK, String pkVal, String saveToFileField)
	{
		System.Text.UnicodeEncoding converter = new System.Text.UnicodeEncoding();
		//byte[] inputBytes = converter.GetBytes(docs);
//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: byte[] inputBytes = System.Text.Encoding.UTF8.GetBytes(docs);
		byte[] inputBytes = docs.getBytes(java.nio.charset.StandardCharsets.UTF_8);

		//执行保存.
		SaveBytesToDB(inputBytes, tableName, tablePK, pkVal, saveToFileField);
	}
	/** 
	 保存文件到数据库
	 
	 @param fullFileName 完成的文件路径
	 @param tableName 表名称
	 @param tablePK 表主键
	 @param pkVal 主键值
	 @param saveFileField 保存到字段
	*/
	public static void SaveFileToDB(String fullFileName, String tableName, String tablePK, String pkVal, String saveToFileField)
	{
		File fi = new File(fullFileName);
		FileInputStream fs = fi.OpenRead();
//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: byte[] bytes = new byte[fs.Length];
		byte[] bytes = new byte[fs.Length];
		fs.read(bytes, 0, (int)fs.Length);

		// bug 的提示者 http://bbs.ccflow.org/showtopic-3958.aspx
		fs.close();
		fs.Dispose();

		//执行保存.
		SaveBytesToDB(bytes, tableName, tablePK, pkVal, saveToFileField);
	}
	public static void GetFileFromDB(String fileFullName, String tableName, String tablePK, String pkVal, String fileSaveField)
	{
//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: byte[] byteFile = GetByteFromDB(tableName, tablePK, pkVal, fileSaveField);
		byte[] byteFile = GetByteFromDB(tableName, tablePK, pkVal, fileSaveField);
		FileOutputStream fs;
		//如果文件不为空,就把流数据保存一个文件.
		if (fileFullName != null)
		{
			File fi = new File(fileFullName);
			fs = fi.OpenWrite();
			fs.write(byteFile, 0, byteFile.length);
			fs.close();
		}
	}
	/** 
	 从数据库里获得文本
	 
	 @param tableName 表名
	 @param tablePK 主键
	 @param pkVal 主键值
	 @param fileSaveField 保存字段
	 @return 
	*/
	public static String GetBigTextFromDB(String tableName, String tablePK, String pkVal, String fileSaveField)
	{
//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: byte[] byteFile = GetByteFromDB(tableName, tablePK, pkVal, fileSaveField);
		byte[] byteFile = GetByteFromDB(tableName, tablePK, pkVal, fileSaveField);
		if (byteFile == null)
		{
			return null;
		}

		String strs = System.Text.Encoding.UTF8.GetString(byteFile);
		int idx = strs.indexOf('$');
		if (idx != 0)
		{
			strs = strs.substring(idx + 1);
		}
		return strs;
	}
	/** 
	 从数据库里提取文件
	 
	 @param tableName 表名
	 @param tablePK 表主键
	 @param pkVal 主键值
	 @param fileSaveField 字段
	*/
//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: public static byte[] GetByteFromDB(string tableName, string tablePK, string pkVal, string fileSaveField)
	public static byte[] GetByteFromDB(String tableName, String tablePK, String pkVal, String fileSaveField)
	{
		if (BP.Sys.SystemConfig.getAppCenterDBType() == DBType.MSSQL)
		{
			IDbConnection tempVar = BP.DA.DBAccess.getGetAppCenterDBConn();
			SqlConnection cn = tempVar instanceof SqlConnection ? (SqlConnection)tempVar : null;
			if (cn.State != ConnectionState.Open)
			{
				cn.Open();
			}

			String strSQL = "SELECT [" + fileSaveField + "] FROM " + tableName + " WHERE " + tablePK + "='" + pkVal + "'";

			SqlDataReader dr = null;
			SqlCommand cm = new SqlCommand();
			cm.Connection = cn;
			cm.CommandText = strSQL;
			cm.CommandType = CommandType.Text;

			// 执行它.
			try
			{
				dr = cm.ExecuteReader();
			}
			catch (RuntimeException e)
			{
				if (!BP.DA.DBAccess.IsExitsTableCol(tableName, fileSaveField))
				{
					/*如果没有此列，就自动创建此列.*/
					String sql = "ALTER TABLE " + tableName + " ADD  " + fileSaveField + " image ";
					BP.DA.DBAccess.RunSQL(sql);
				}
				return GetByteFromDB(tableName, tablePK, pkVal, fileSaveField);
				//throw new Exception("@缺少此字段,有可能系统自动修复." + ex.Message);
			}

//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: byte[] byteFile = null;
			byte[] byteFile = null;
			if (dr.Read())
			{
				if (dr.get(0) == null || DataType.IsNullOrEmpty(dr.get(0).toString()))
				{
					return null;
				}

//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: byteFile = (byte[])dr[0];
				byteFile = (byte[])dr.get(0);
			}
			return byteFile;


		}

		//增加对oracle数据库的逻辑 qin
		if (BP.Sys.SystemConfig.getAppCenterDBType() == DBType.Oracle)
		{
			IDbConnection tempVar2 = BP.DA.DBAccess.getGetAppCenterDBConn();
			OracleConnection cn = tempVar2 instanceof OracleConnection ? (OracleConnection)tempVar2 : null;
			if (cn.State != ConnectionState.Open)
			{
				cn.Open();
			}

			String strSQL = "SELECT " + fileSaveField + " FROM " + tableName + " WHERE " + tablePK + "='" + pkVal + "'";

			OracleDataReader dr = null;
			OracleCommand cm = new OracleCommand();
			cm.Connection = cn;
			cm.CommandText = strSQL;
			cm.CommandType = CommandType.Text;


			// 执行它.
			try
			{
				dr = cm.ExecuteReader();
			}
			catch (RuntimeException ex)
			{
				if (BP.DA.DBAccess.IsExitsTableCol(tableName, fileSaveField) == false)
				{
					/*如果没有此列，就自动创建此列.*/
					String sql = "ALTER TABLE " + tableName + " ADD  " + fileSaveField + " blob ";
					BP.DA.DBAccess.RunSQL(sql);
				}
				throw new RuntimeException("@缺少此字段,有可能系统自动修复." + ex.getMessage());
			}

//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: byte[] byteFile = null;
			byte[] byteFile = null;
			if (dr.Read())
			{
				if (dr[0] == null || DataType.IsNullOrEmpty(dr[0].toString()))
				{
					return null;
				}

//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: byteFile = (byte[])dr[0];
				byteFile = (byte[])dr[0];
			}

			return byteFile;
		}

		//added by liuxc,2016-12-7,增加对mysql数据库的逻辑
		if (BP.Sys.SystemConfig.getAppCenterDBType() == DBType.MySQL)
		{
			IDbConnection tempVar3 = BP.DA.DBAccess.getGetAppCenterDBConn();
			MySqlConnection cn = tempVar3 instanceof MySqlConnection ? (MySqlConnection)tempVar3 : null;
			if (cn.State != ConnectionState.Open)
			{
				cn.Open();
			}

			String strSQL = "SELECT " + fileSaveField + " FROM " + tableName + " WHERE " + tablePK + "='" + pkVal + "'";

			MySqlDataReader dr = null;
			MySqlCommand cm = new MySqlCommand();
			cm.Connection = cn;
			cm.CommandText = strSQL;
			cm.CommandType = CommandType.Text;


			// 执行它.
			try
			{
				dr = cm.ExecuteReader();
			}
			catch (RuntimeException ex)
			{
				if (BP.DA.DBAccess.IsExitsTableCol(tableName, fileSaveField) == false)
				{
					/*如果没有此列，就自动创建此列.*/
					String sql = "ALTER TABLE " + tableName + " ADD " + fileSaveField + " LONGBLOB NULL ";
					BP.DA.DBAccess.RunSQL(sql);
				}
				throw new RuntimeException("@缺少此字段,有可能系统自动修复." + ex.getMessage());
			}

//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: byte[] byteFile = null;
			byte[] byteFile = null;
			if (dr.Read())
			{
				if (dr[0] == null || DataType.IsNullOrEmpty(dr[0].toString()))
				{
					return null;
				}

//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: byteFile = dr[0] instanceof byte[] ? (byte[])dr[0] : null;
				byteFile = dr[0] instanceof byte[] ? (byte[])dr[0] : null;
				//System.Text.Encoding.Default.GetBytes(dr[0].ToString());
			}

			return byteFile;
		}

		//最后仍然没有找到.
		throw new RuntimeException("@获取文件，从数据库里面，没有判断的数据库类型.");
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 文件存储数据库处理.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 事务处理
	/** 
	 执行增加一个事务
	*/
	public static void DoTransactionBegin()
	{
		return;

		//if (SystemConfig.AppCenterDBType != DBType.MSSQL)
		//    return;
		//if (BP.Web.WebUser.No == null)
		//    return;
		//SqlConnection conn = new SqlConnection(SystemConfig.AppCenterDSN);
		//BP.DA.Cash.SetConn(BP.Web.WebUser.No, conn);
		//DBAccess.RunSQL("BEGIN TRANSACTION");
	}
	/** 
	 回滚事务
	*/
	public static void DoTransactionRollback()
	{
		return;

		//if (SystemConfig.AppCenterDBType != DBType.MSSQL)
		//    return;

		//if (BP.Web.WebUser.No == null)
		//    return;

		//DBAccess.RunSQL("Rollback TRANSACTION");
		//SqlConnection conn = BP.DA.Cash.GetConn(BP.Web.WebUser.No) as SqlConnection;
		//conn.Close();
		//conn.Dispose();
	}
	/** 
	 提交事务
	*/
	public static void DoTransactionCommit()
	{
		/*
		return;

		if (SystemConfig.AppCenterDBType != DBType.MSSQL)
		    return;

		if (BP.Web.WebUser.No == null)
		    return;

		DBAccess.RunSQL("Commit TRANSACTION");
		*/
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 事务处理

	public static Paras DealParasBySQL(String sql, Paras ps)
	{
		Paras myps = new Paras();
		for (Para p : ps)
		{
			if (sql.contains(":" + p.ParaName) == false)
			{
				continue;
			}
			myps.Add(p);
		}
		return myps;
	}
	/** 
	 运行一个sql返回该table的第1列，组成一个查询的where in 字符串.
	 
	 @param sql
	 @return i
	*/
	public static String GenerWhereInPKsString(String sql)
	{
		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		return GenerWhereInPKsString(dt);
	}
	/** 
	 通过table的第1列，组成一个查询的where in 字符串.
	 
	 @param dt
	 @return 
	*/
	public static String GenerWhereInPKsString(DataTable dt)
	{
		String pks = "";
		for (DataRow dr : dt.Rows)
		{
			pks += "'" + dr.get(0) + "',";
		}
		if (pks.equals(""))
		{
			return "";
		}
		return pks.substring(0, pks.length() - 1);
	}
	/** 
	 检查是否连接成功.
	 
	 @return 
	*/
	public static boolean TestIsConnection()
	{
		try
		{
			switch (BP.Sys.SystemConfig.getAppCenterDBType())
			{
				case MSSQL:
				case PostgreSQL:
					BP.DA.DBAccess.RunSQLReturnString("SELECT 1+2 ");
					break;
				case Oracle:
				case MySQL:
					BP.DA.DBAccess.RunSQLReturnString("SELECT 1+2 FROM DUAL ");
					break;
				//case DBType.Informix:
				//    BP.DA.DBAccess.RunSQLReturnString("SELECT 1+2 FROM DUAL ");
				//    break;
				default:
					break;
			}
			return true;
		}
		catch (RuntimeException e)
		{
			return false;
		}

		//return true;
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region IO
	public static void copyDirectory(String Src, String Dst)
	{
		String[] Files;
		if (Dst.charAt(Dst.length() - 1) != File.separatorChar)
		{
			Dst += File.separatorChar;
		}
		if (!(new File(Dst)).isDirectory())
		{
			(new File(Dst)).mkdirs();
		}
		Files = Directory.GetFileSystemEntries(Src);
		for (String Element : Files)
		{
			//   Sub   directories   
			if ((new File(Element)).isDirectory())
			{
				copyDirectory(Element, Dst + (new File(Element)).getName());
			}
			//   Files   in   directory   
			else
			{
				Files.copy(Paths.get(Element), Paths.get(Dst + (new File(Element)).getName()), StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);
			}
		}
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 读取Xml

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

	//构造函数
	static
	{
		CurrentSys_Serial = new Hashtable();
		KeyLockState = new Hashtable();
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 运行中定义的变量
	public static Hashtable CurrentSys_Serial;
	private static int readCount = -1;
	private static Hashtable KeyLockState;
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion


//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 产生序列号码方法
	/** 
	 根据标识产生的序列号
	 
	 @param type OID
	 @return 
	*/
	public static int GenerSequenceNumber(String type)
	{
		if (readCount == -1) //系统第一次运行时
		{
			DataTable tb = DBAccess.RunSQLReturnTable("SELECT CfgKey, IntVal FROM Sys_Serial ");
			for (DataRow row : tb.Rows)
			{
				String str = row.get(0).toString().trim();
				int id = (Integer)row.get(1);
				try
				{
					CurrentSys_Serial.put(str, id);
					KeyLockState.put(row.get(0).toString().trim(), false);
				}
				catch (java.lang.Exception e)
				{
				}
			}
			readCount++;
		}
		if (CurrentSys_Serial.containsKey(type) == false)
		{
			DBAccess.RunSQL("insert into Sys_Serial values('" + type + "',1 )");
			return 1;
		}

		while (true)
		{
			while (!(boolean)KeyLockState.get(type))
			{
				KeyLockState.put(type, true);
				int cur = (int)CurrentSys_Serial.get(type);
				if (readCount++ % 10 == 0)
				{
					readCount = 1;
					int n = (int)CurrentSys_Serial.get(type) + 10;

					Paras ps = new Paras();
					ps.Add("intVal", n);
					ps.Add("CfgKey", type);

					String upd = "update Sys_Serial set intVal=" + SystemConfig.getAppCenterDBVarStr() + "intVal WHERE CfgKey=" + SystemConfig.getAppCenterDBVarStr() + "CfgKey";
					DBAccess.RunSQL(upd, ps);
				}

				cur++;
				CurrentSys_Serial.put(type, cur);
				KeyLockState.put(type, false);
				return cur;
			}
		}
	}
	/** 
	 生成 GenerOIDByGUID.
	 
	 @return 
	*/
	public static int GenerOIDByGUID()
	{
		int i = BP.Tools.CRC32Helper.GetCRC32(UUID.NewGuid().toString());
		if (i <= 0)
		{
			i = -i;
		}
		return i;
	}
	/** 
	 生成 GenerOIDByGUID.
	 
	 @return 
	*/
	public static int GenerOIDByGUID(String strs)
	{
		int i = BP.Tools.CRC32Helper.GetCRC32(strs);
		if (i <= 0)
		{
			i = -i;
		}
		return i;
	}
	/** 
	 生成 GenerGUID
	 
	 @return 
	*/
	public static String GenerGUID()
	{
		return UUID.NewGuid().toString();
	}
	/** 
	 锁定OID
	*/
	private static boolean lock_OID = false;
	/** 
	 产生一个OID
	 
	 @return 
	*/
	public static int GenerOID()
	{
		while (lock_OID == true)
		{
		}

		lock_OID = true;
		if (DBAccess.RunSQL("UPDATE Sys_Serial SET IntVal=IntVal+1 WHERE CfgKey='OID'") == 0)
		{
			DBAccess.RunSQL("INSERT INTO Sys_Serial (CfgKey,IntVal) VALUES ('OID',100)");
		}
		int oid = DBAccess.RunSQLReturnValInt("SELECT  IntVal FROM Sys_Serial WHERE CfgKey='OID'");
		lock_OID = false;
		return oid;
	}
	/** 
	 锁
	*/
	private static boolean lock_OID_CfgKey = false;
	/** 
	 生成唯一的序列号
	 
	 @param cfgKey 配置信息
	 @return 唯一的序列号
	*/
	public static long GenerOID_2013(String cfgKey)
	{
		while (lock_OID_CfgKey == true)
		{
		}
		lock_OID_CfgKey = true;

		Paras ps = new Paras();
		ps.Add("CfgKey", cfgKey);
		String sql = "UPDATE Sys_Serial SET IntVal=IntVal+1 WHERE CfgKey=" + SystemConfig.getAppCenterDBVarStr() + "CfgKey";
		int num = DBAccess.RunSQL(sql, ps);
		if (num == 0)
		{
			sql = "INSERT INTO Sys_Serial (CFGKEY,INTVAL) VALUES ('" + cfgKey + "',100)";
			DBAccess.RunSQL(sql);
			lock_OID_CfgKey = false;
			return 100;
		}
		sql = "SELECT IntVal FROM Sys_Serial WHERE CfgKey=" + SystemConfig.getAppCenterDBVarStr() + "CfgKey";
		num = DBAccess.RunSQLReturnValInt(sql, ps);
		lock_OID_CfgKey = false;
		return num;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 第二版本的生成 OID。
	/** 
	 锁
	*/
	private static boolean lock_HT_CfgKey = false;
	private static Hashtable lock_HT = new Hashtable();
	/** 
	 生成唯一的序列号
	 
	 @param cfgKey 配置信息
	 @return 唯一的序列号
	*/
	public static long GenerOID(String cfgKey)
	{
		//while (lock_HT_CfgKey == true)
		//{
		//}
		lock_HT_CfgKey = true;

		if (lock_HT.containsKey(cfgKey) == false)
		{
		}
		else
		{
		}

		Paras ps = new Paras();
		ps.Add("CfgKey", cfgKey);
		//string sql = "UPDATE Sys_Serial SET IntVal=IntVal+1 WHERE CfgKey=" + SystemConfig.AppCenterDBVarStr + "CfgKey";
		//int num = DBAccess.RunSQL(sql, ps);
		String sql = "SELECT IntVal FROM Sys_Serial WHERE CfgKey=" + SystemConfig.getAppCenterDBVarStr() + "CfgKey";
		int num = DBAccess.RunSQLReturnValInt(sql, ps);
		if (num == 0)
		{
			sql = "INSERT INTO Sys_Serial (CFGKEY,INTVAL) VALUES ('" + cfgKey + "',100)";
			DBAccess.RunSQL(sql);
			lock_HT_CfgKey = false;

			if (lock_HT.containsKey(cfgKey) == false)
			{
				lock_HT.put(cfgKey, 200);
			}

			return 100;
		}
		else
		{
			sql = "UPDATE Sys_Serial SET IntVal=IntVal+1 WHERE CfgKey=" + SystemConfig.getAppCenterDBVarStr() + "CfgKey";
			DBAccess.RunSQL(sql, ps);
		}
		sql = "SELECT IntVal FROM Sys_Serial WHERE CfgKey=" + SystemConfig.getAppCenterDBVarStr() + "CfgKey";
		num = DBAccess.RunSQLReturnValInt(sql, ps);
		lock_HT_CfgKey = false;
		return num;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 第二版本的生成 OID。

	/** 
	 获取一个从OID, 更新到OID.
	 用例: 我已经明确知道要用到260个OID, 
	 但是为了避免多次取出造成效率浪费，就可以一次性取出 260个OID.
	 
	 @param cfgKey
	 @param getOIDNum 要获取的OID数量.
	 @return 从OID
	*/
	public static long GenerOID(String cfgKey, int getOIDNum)
	{
		Paras ps = new Paras();
		ps.Add("CfgKey", cfgKey);
		String sql = "UPDATE Sys_Serial SET IntVal=IntVal+" + getOIDNum + " WHERE CfgKey=" + SystemConfig.getAppCenterDBVarStr() + "CfgKey";
		int num = DBAccess.RunSQL(sql, ps);
		if (num == 0)
		{
			getOIDNum = getOIDNum + 100;
			sql = "INSERT INTO Sys_Serial (CFGKEY,INTVAL) VALUES (" + SystemConfig.getAppCenterDBVarStr() + "CfgKey," + getOIDNum + ")";
			DBAccess.RunSQL(sql, ps);
			return 100;
		}
		sql = "SELECT  IntVal FROM Sys_Serial WHERE CfgKey=" + SystemConfig.getAppCenterDBVarStr() + "CfgKey";
		return DBAccess.RunSQLReturnValInt(sql, ps) - getOIDNum;
	}
	/** 
	 
	 
	 @param intKey
	 @return 
	*/
	public static long GenerOIDByKey64(String intKey)
	{
		Paras ps = new Paras();
		ps.Add("CfgKey", intKey);
		String sql = "";
		sql = "UPDATE Sys_Serial SET IntVal=IntVal+1 WHERE CfgKey=" + SystemConfig.getAppCenterDBVarStr() + "CfgKey";
		int num = DBAccess.RunSQL(sql, ps);
		if (num == 0)
		{
			sql = "INSERT INTO Sys_Serial (CFGKEY,INTVAL) VALUES (" + SystemConfig.getAppCenterDBVarStr() + "CfgKey,'1')";
			DBAccess.RunSQL(sql, ps);
			return Long.parseLong(intKey + "1");
		}
		sql = "SELECT IntVal FROM Sys_Serial WHERE CfgKey=" + SystemConfig.getAppCenterDBVarStr() + "CfgKey";
		int val = DBAccess.RunSQLReturnValInt(sql, ps);
		return Long.parseLong(intKey + String.valueOf(val));
	}
	public static int GenerOIDByKey32(String intKey)
	{
		Paras ps = new Paras();
		ps.Add("CfgKey", intKey);

		String sql = "";
		sql = "UPDATE Sys_Serial SET IntVal=IntVal+1 WHERE CfgKey=" + SystemConfig.getAppCenterDBVarStr() + "CfgKey";
		int num = DBAccess.RunSQL(sql, ps);
		if (num == 0)
		{
			sql = "INSERT INTO Sys_Serial (CFGKEY,INTVAL) VALUES (" + SystemConfig.getAppCenterDBVarStr() + "CfgKey,'100')";
			DBAccess.RunSQL(sql, ps);
			return Integer.parseInt(intKey + "100");
		}
		sql = "SELECT IntVal FROM Sys_Serial WHERE CfgKey=" + SystemConfig.getAppCenterDBVarStr() + "CfgKey";
		int val = DBAccess.RunSQLReturnValInt(sql, ps);
		return Integer.parseInt(intKey + String.valueOf(val));
	}
	public static long GenerOID(String table, String intKey)
	{
		Paras ps = new Paras();
		ps.Add("CfgKey", intKey);

		String sql = "";
		sql = "UPDATE " + table + " SET IntVal=IntVal+1 WHERE CfgKey=" + SystemConfig.getAppCenterDBVarStr() + "CfgKey";
		int num = DBAccess.RunSQL(sql, ps);
		if (num == 0)
		{
			sql = "INSERT INTO " + table + " (CFGKEY,INTVAL) VALUES (" + SystemConfig.getAppCenterDBVarStr() + "CfgKey,100)";
			DBAccess.RunSQL(sql, ps);
			return Integer.parseInt(intKey + "100");
		}
		sql = "SELECT  IntVal FROM " + table + " WHERE CfgKey=" + SystemConfig.getAppCenterDBVarStr() + "CfgKey";
		int val = DBAccess.RunSQLReturnValInt(sql, ps);

		return Long.parseLong(intKey + String.valueOf(val));
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 取得连接对象 ，CS、BS共用属性【关键属性】
	/** 
	 AppCenterDBType
	*/
	public static DBType getAppCenterDBType()
	{
		return SystemConfig.getAppCenterDBType();
	}
	public static String _connectionUserID = null;
	/** 
	 连接用户的ID
	*/
	public static String getConnectionUserID()
	{
		if (_connectionUserID == null)
		{
			String[] strs = BP.Sys.SystemConfig.getAppCenterDSN().split("[;]", -1);
			for (String str : strs)
			{
				if (str.contains("user ") == true)
				{
					_connectionUserID = str.split("[=]", -1)[1];
					break;
				}
			}
		}
		return _connectionUserID;
	}
	public static IDbConnection getGetAppCenterDBConn()
	{
		String connstr = BP.Sys.SystemConfig.getAppCenterDSN();
		switch (getAppCenterDBType())
		{
			case MSSQL:
				return new SqlConnection(connstr);
			case Oracle:
				return new OracleConnection(connstr);
			case MySQL:
				return new MySqlConnection(connstr);
			case PostgreSQL:
				return new Npgsql.NpgsqlConnection(connstr);
				//case DBType.Informix: net core 无法支持
				//    return new IfxConnection(connstr);
			case Access:
			default:
				throw new RuntimeException("err@GetAppCenterDBConn发现未知的数据库连接类型！");
		}
	}

	public static IDbDataAdapter getGetAppCenterDBAdapter()
	{
		switch (getAppCenterDBType())
		{
			case MSSQL:
				return new SqlDataAdapter();
			case Oracle:
				return new OracleDataAdapter();
			case MySQL:
				return new MySqlDataAdapter();
			case PostgreSQL:
				return new NpgsqlDataAdapter();
				//case DBType.Informix: net core 无法支持
				//    return new IfxDataAdapter();
			case Access:
			default:
				throw new RuntimeException("err@GetAppCenterDBAdapter发现未知的数据库连接类型！");
		}
	}
	public static IDbCommand getGetAppCenterDBCommand()
	{
		switch (getAppCenterDBType())
		{
			case MSSQL:
				return new SqlCommand();
			case Oracle:
				return new OracleCommand();
			case MySQL:
				return new MySqlCommand();
			case PostgreSQL:
				return new NpgsqlCommand();
				//case DBType.Informix:
				//    return new IfxCommand();
			case Access:
			default:
				throw new RuntimeException("err@GetAppCenterDBCommand发现未知的数据库连接类型！");
		}
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 取得连接对象 ，CS、BS共用属性

	/** 
	 同一个Connetion执行多条sql返回DataSet
	 edited by qin 16.6.30 oracle数据库执行多条sql语句异常的修复
	 
	 @param sqls
	 @return 1
	*/
	public static DataSet RunSQLReturnDataSet(String sqls)
	{
		DataSet ds = new DataSet();
		String[] sqlArray = sqls.split("[;]", -1);
		DataTable dt = null;
		for (int i = 0; i < sqlArray.length; i++)
		{
			if (tangible.StringHelper.isNullOrWhiteSpace(sqlArray[i]))
			{
				continue;
			}

			dt = DBAccess.RunSQLReturnTable(sqlArray[i]);
			dt.TableName = "dt_" + String.valueOf(i);
			ds.Tables.Add(dt);
		}
		return ds;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 运行 SQL

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 在指定的Connection上执行 SQL 语句，返回受影响的行数

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region OleDbConnection
	public static int RunSQLDropTable(String table)
	{
		if (IsExitsObject(new DBUrl(DBUrlType.AppCenterDSN), table))
		{
			switch (getAppCenterDBType())
			{
				case Oracle:
				case MSSQL:
				case Informix:
				case Access:
					return RunSQL("DROP TABLE " + table);
				default:
					throw new RuntimeException(" Exception ");
			}
		}
		return 0;

		/* return RunSQL("TRUNCATE TABLE " + table);*/

	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region SqlConnection
	/** 
	 运行SQL
	*/
	// private static bool lock_SQL_RunSQL = false;
	/** 
	 运行SQL, 返回影响的行数.
	 
	 @param sql msSQL
	 @param conn SqlConnection
	 @return 返回运行结果。
	*/
	public static int RunSQL(String sql, SqlConnection conn, String dsn)
	{
		return RunSQL(sql, conn, CommandType.Text, dsn);
	}
	/** 
	 运行SQL , 返回影响的行数.
	 
	 @param sql msSQL
	 @param conn SqlConnection
	 @param sqlType CommandType
	 @param pars params
	 @return 返回运行结果
	*/
	public static int RunSQL(String sql, SqlConnection conn, CommandType sqlType, String dsn)
	{
		conn.Close();
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if DEBUG
		Debug.WriteLine(sql);
//#endif
		//如果是锁定状态，就等待
		//while (lock_SQL_RunSQL)
		//    ;
		// 开始执行.
		//lock_SQL_RunSQL = true; //锁定
		String step = "1";
		try
		{

			if (conn == null)
			{
				conn = new SqlConnection(dsn);
			}

			if (conn.State != System.Data.ConnectionState.Open)
			{
				conn.ConnectionString = dsn;
				conn.Open();
			}

			step = "2";
			SqlCommand cmd = new SqlCommand(sql, conn);
			cmd.CommandType = sqlType;
			step = "3";

			step = "4";
			int i = 0;
			try
			{
				i = cmd.ExecuteNonQuery();
			}
			catch (RuntimeException ex)
			{
				step = "5";
				//lock_SQL_RunSQL = false;
				cmd.Dispose();
				step = "6";
				throw new RuntimeException("RunSQL step=" + step + ex.getMessage() + " SQL=" + sql);
			}
			step = "7";
			cmd.Dispose();
			// lock_SQL_RunSQL = false;
			return i;
		}
		catch (RuntimeException ex)
		{
			step = "8";
			// lock_SQL_RunSQL = false;
			throw new RuntimeException("RunSQL2 step=" + step + ex.getMessage() + " 设置连接时间=" + conn.ConnectionTimeout);
		}
		finally
		{
			step = "9";
			//lock_SQL_RunSQL = false;
			conn.Close();
		}
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region OracleConnection
	public static int RunSQL(String sql, OracleConnection conn, String dsn)
	{
		return RunSQL(sql, conn, CommandType.Text, dsn);
	}
	public static int RunSQL(String sql, OracleConnection conn, CommandType sqlType, String dsn)
	{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if DEBUG
		Debug.WriteLine(sql);
//#endif
		//如果是锁定状态，就等待
		// while (lock_SQL_RunSQL)
		//  ;
		// 开始执行.
		// lock_SQL_RunSQL = true; //锁定
		String step = "1";
		try
		{
			if (conn == null)
			{
				conn = new OracleConnection(dsn);
			}

			if (conn.State != System.Data.ConnectionState.Open)
			{
				conn.ConnectionString = dsn;
				conn.Open();
			}

			step = "2";
			OracleCommand cmd = new OracleCommand(sql, conn);
			cmd.CommandType = sqlType;
			step = "3";
			int i = 0;
			try
			{
				i = cmd.ExecuteNonQuery();
			}
			catch (RuntimeException ex)
			{
				step = "5";
				// lock_SQL_RunSQL = false;
				cmd.Dispose();
				step = "6";
				throw new RuntimeException("RunSQL step=" + step + ex.getMessage() + " SQL=" + sql);
			}
			step = "7";
			cmd.Dispose();


			//lock_SQL_RunSQL = false;
			return i;
		}
		catch (RuntimeException ex)
		{
			step = "8";
			// lock_SQL_RunSQL = false;
			throw new RuntimeException("RunSQL2 step=" + step + ex.getMessage());
		}
		finally
		{
			step = "9";
			// lock_SQL_RunSQL = false;
			conn.Close();
		}

		/*
		Debug.WriteLine( sql );
		try
		{
		    OracleCommand cmd = new OracleCommand( sql ,conn);
		    cmd.CommandType = sqlType;
		    foreach(object par in pars)
		    {
		        cmd.Parameters.Add( "par",par);
		    }
		    if (conn.State != System.Data.ConnectionState.Open)
		    {
		        conn.Open();
		    }

		    int i= cmd.ExecuteNonQuery();				 
		    cmd.Dispose();
		    return i;				 
		}
		catch(System.Exception ex)
		{
		    throw new Exception(ex.Message + sql );
		}
		finally
		{
		    conn.Close();

		}
		*/
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 通过主应用程序在其他库上运行sql

	/** 
	 删除表的主键
	 
	 @param table 表名称
	*/
	public static void DropTablePK(String table)
	{
		String pkName = DBAccess.GetTablePKName(table);
		if (pkName == null)
		{
			return;
		}

		String sql = "";
		switch (SystemConfig.getAppCenterDBType())
		{
			case Oracle:
			case MSSQL:
				sql = "ALTER TABLE " + table + " DROP CONSTRAINT " + pkName;
				break;
			case PostgreSQL:
				sql = "ALTER TABLE " + table.toLowerCase() + " DROP CONSTRAINT " + pkName.toLowerCase();
				break;
			case MySQL:
				sql = "ALTER TABLE " + table + " DROP primary key";
				break;
			default:
				throw new RuntimeException("err@DropTablePK不支持的数据库类型." + SystemConfig.getAppCenterDBType());
				//break;
		}
		BP.DA.DBAccess.RunSQL(sql);
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region pk
	/** 
	 建立主键
	 
	 @param tab 物理表
	 @param pk 主键
	*/
	public static void CreatePK(String tab, String pk, DBType db)
	{
		if (tab == null || tab.equals(""))
		{
			return;
		}
		if (DBAccess.IsExitsTabPK(tab) == true)
		{
			return;
		}
		String sql;
		switch (db)
		{
			case Informix:
				sql = "ALTER TABLE " + tab.toUpperCase() + " ADD CONSTRAINT  PRIMARY KEY(" + pk + ") CONSTRAINT " + tab + "pk ";
				break;
			case MySQL:
				//   ALTER TABLE Port_emp ADD CONSTRAINT Port_emppk PRIMARY KEY (NO)
				sql = "ALTER TABLE " + tab + " ADD CONSTRAINT  " + tab + "px PRIMARY KEY(" + pk + ")";
				//sql = "ALTER TABLE " + tab + " ADD CONSTRAINT  PRIMARY KEY(" + pk + ") CONSTRAINT " + tab + "pk ";
				break;
			default:
				sql = "ALTER TABLE " + tab.toUpperCase() + " ADD CONSTRAINT " + tab + "pk PRIMARY KEY(" + pk.toUpperCase() + ")";
				break;
		}
		DBAccess.RunSQL(sql);
	}
	public static void CreatePK(String tab, String pk1, String pk2, DBType db)
	{
		if (tab == null || tab.equals(""))
		{
			return;
		}

		if (DBAccess.IsExitsTabPK(tab) == true)
		{
			return;
		}

		String sql;
		switch (db)
		{
			case Informix:
				sql = "ALTER TABLE " + tab.toUpperCase() + " ADD CONSTRAINT  PRIMARY KEY(" + pk1.toUpperCase() + "," + pk2.toUpperCase() + ") CONSTRAINT " + tab + "pk ";
				break;
			case MySQL:
				sql = "ALTER TABLE " + tab + " ADD CONSTRAINT " + tab + "pk  PRIMARY KEY(" + pk1 + "," + pk2 + ")";
				break;
			default:
				sql = "ALTER TABLE " + tab.toUpperCase() + " ADD CONSTRAINT " + tab + "pk  PRIMARY KEY(" + pk1.toUpperCase() + "," + pk2.toUpperCase() + ")";
				break;
		}
		DBAccess.RunSQL(sql);
	}
	public static void CreatePK(String tab, String pk1, String pk2, String pk3, DBType db)
	{
		if (tab == null || tab.equals(""))
		{
			return;
		}

		if (DBAccess.IsExitsTabPK(tab) == true)
		{
			return;
		}

		String sql;
		switch (db)
		{
			case Informix:
				sql = "ALTER TABLE " + tab.toUpperCase() + " ADD CONSTRAINT  PRIMARY KEY(" + pk1.toUpperCase() + "," + pk2.toUpperCase() + "," + pk3.toUpperCase() + ") CONSTRAINT " + tab + "pk ";
				break;
			case MySQL:
				sql = "ALTER TABLE " + tab + " ADD CONSTRAINT " + tab + "pk PRIMARY KEY(" + pk1 + "," + pk2 + "," + pk3 + ")";
				break;
			default:
				sql = "ALTER TABLE " + tab.toUpperCase() + " ADD CONSTRAINT " + tab + "pk PRIMARY KEY(" + pk1.toUpperCase() + "," + pk2.toUpperCase() + "," + pk3.toUpperCase() + ")";
				break;
		}
		DBAccess.RunSQL(sql);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion


//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region index
	public static void CreatIndex(String table, String fields)
	{
		String idxName = table + "_" + fields;
		if (BP.DA.DBAccess.IsExitsObject(idxName) == true)
		{
			return;
		}

		String sql = "";
		try
		{
			sql = "DROP INDEX " + idxName + " ON " + table;
			DBAccess.RunSQL(sql);
		}
		catch (java.lang.Exception e)
		{
		}

		try
		{
			sql = "CREATE INDEX " + idxName + " ON " + table + " (" + fields + ")";
			DBAccess.RunSQL(sql);
		}
		catch (java.lang.Exception e2)
		{
		}
	}
	public static void CreatIndex(String table, String pk1, String pk2)
	{

		try
		{
			DBAccess.RunSQL("CREATE INDEX " + table + "ID ON " + table + " (" + pk1 + "," + pk2 + ")");
		}
		catch (java.lang.Exception e)
		{
		}
	}
	public static void CreatIndex(String table, String pk1, String pk2, String pk3)
	{
		try
		{
			  DBAccess.RunSQL("CREATE INDEX " + table + "ID ON " + table + " (" + pk1 + "," + pk2 + "," + pk3 + ")");
		}
		catch (RuntimeException ex)
		{
		}
	}
	public static void CreatIndex(String table, String pk1, String pk2, String pk3, String pk4)
	{
		try
		{
			DBAccess.RunSQL("CREATE INDEX " + table + "ID ON " + table + " (" + pk1 + "," + pk2 + "," + pk3 + "," + pk4 + ")");
		}
		catch (RuntimeException ex)
		{
		}
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

	public static int CreatTableFromODBC(String selectSQL, String table, String pk)
	{
		DBAccess.RunSQLDropTable(table);
		String sql = "SELECT * INTO " + table + " FROM OPENROWSET('MSDASQL','" + SystemConfig.getAppSettings().get("DBAccessOfODBC") + "','" + selectSQL + "')";
		int i = DBAccess.RunSQL(sql);
		DBAccess.RunSQL("CREATE INDEX " + table + "ID ON " + table + " (" + pk + ")");
		return i;
	}
	public static int CreatTableFromODBC(String selectSQL, String table, String pk1, String pk2)
	{
		DBAccess.RunSQLDropTable(table);
		//DBAccess.RunSQL("DROP TABLE "+table);
		String sql = "SELECT * INTO " + table + " FROM OPENROWSET('MSDASQL','" + SystemConfig.getAppSettings().get("DBAccessOfODBC") + "','" + selectSQL + "')";
		int i = DBAccess.RunSQL(sql);
		DBAccess.RunSQL("CREATE INDEX " + table + "ID ON " + table + " (" + pk1 + "," + pk2 + ")");
		return i;
	}
	public static int CreatTableFromODBC(String selectSQL, String table, String pk1, String pk2, String pk3)
	{
		DBAccess.RunSQLDropTable(table);
		String sql = "SELECT * INTO " + table + " FROM OPENROWSET('MSDASQL','" + SystemConfig.getAppSettings().get("DBAccessOfODBC") + "','" + selectSQL + "')";
		int i = DBAccess.RunSQL(sql);
		DBAccess.RunSQL("CREATE INDEX " + table + "ID ON " + table + " (" + pk1 + "," + pk2 + "," + pk3 + ")");
		return i;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 在当前的Connection执行 SQL 语句，返回受影响的行数
	public static int RunSQL(String sql, CommandType sqlType, String dsn, Object... pars)
	{
		IDbConnection oconn = getGetAppCenterDBConn();
		if (oconn instanceof SqlConnection)
		{
			return RunSQL(sql, (SqlConnection)oconn, sqlType, dsn);
		}
		else if (oconn instanceof OracleConnection)
		{
			return RunSQL(sql, (OracleConnection)oconn, sqlType, dsn);
		}
		else
		{
			throw new RuntimeException("获取数据库连接[GetAppCenterDBConn]失败！");
		}
	}
	public static DataTable ReadProText(String proName)
	{
		String sql = "";
		switch (BP.Sys.SystemConfig.getAppCenterDBType())
		{
			case Oracle:
				sql = "SELECT text FROM user_source WHERE name=UPPER('" + proName + "') ORDER BY LINE ";
				break;
			default:
				sql = "SP_Help  " + proName;
				break;
		}
		try
		{
			return BP.DA.DBAccess.RunSQLReturnTable(sql);
		}
		catch (java.lang.Exception e)
		{
			sql = "select * from Port_Emp WHERE 1=2";
			return BP.DA.DBAccess.RunSQLReturnTable(sql);
		}
	}
	public static void RunSQLScript(String sqlOfScriptFilePath)
	{
		String str = BP.DA.DataType.ReadTextFile(sqlOfScriptFilePath);
		String[] strs = str.split("[;]", -1);
		for (String s : strs)
		{
			if (DataType.IsNullOrEmpty(s) || tangible.StringHelper.isNullOrWhiteSpace(s))
			{
				continue;
			}

			if (s.contains("--"))
			{
				continue;
			}

			if (s.contains("/*"))
			{
				continue;
			}

			BP.DA.DBAccess.RunSQL(s);
		}
	}
	/** 
	 执行具有Go的sql 文本。
	 
	 @param sqlOfScriptFilePath
	*/
	public static void RunSQLScriptGo(String sqlOfScriptFilePath)
	{
		String str = BP.DA.DataType.ReadTextFile(sqlOfScriptFilePath);
		String[] strs = str.split(new String[] {"--GO--"}, StringSplitOptions.RemoveEmptyEntries);
		for (String s : strs)
		{
			if (DataType.IsNullOrEmpty(s) || tangible.StringHelper.isNullOrWhiteSpace(s))
			{
				continue;
			}

			if (s.contains("/**"))
			{
				continue;
			}

			String mysql = s.replace("--GO--", "");
			if (DataType.IsNullOrEmpty(mysql.trim()))
			{
				continue;
			}
			if (s.contains("--"))
			{
				continue;
			}

			BP.DA.DBAccess.RunSQL(mysql);
		}
	}
	public static String DealSQL(String sql)
	{
		return sql;
	  ////  return sql;
	  //= sql.CompareTo("(?ms)('(?:''|[^'])*')|--.*?$|/\\*.*?\\*/|#.*?$|");
	  //  String presult = p.matcher(sql).replaceAll("$1");
	  //  return presult;
	}
	/** 
	 运行SQLs
	 
	 @param sql
	*/
	public static void RunSQLs(String sql)
	{
		if (DataType.IsNullOrEmpty(sql))
		{
			return;
		}

		sql = DealSQL(sql); //去掉注释.

		sql = sql.replace("@GO", "~");
		sql = sql.replace("@", "~");

		if (sql.contains("';'") == false)
		{
		  sql = sql.replace(";", "~");
		}

		sql = sql.replace("UPDATE", "~UPDATE");
		sql = sql.replace("DELETE", "~DELETE");
		sql = sql.replace("INSERT", "~INSERT");

		String[] strs = sql.split("[~]", -1);
		for (String str : strs)
		{
			if (DataType.IsNullOrEmpty(str))
			{
				continue;
			}

			if (str.contains("--") || str.contains("/*"))
			{
				continue;
			}

			RunSQL(str);
		}
	}
	/** 
	 运行带有参数的sql
	 
	 @param ps
	 @return 
	*/
	public static int RunSQL(Paras ps)
	{
		return RunSQL(ps.SQL, ps);
	}
	/** 
	 运行sql
	 
	 @param sql
	 @return 
	*/
	public static int RunSQL(String sql)
	{
		if (sql == null || sql.trim().equals(""))
		{
			return 1;
		}
		Paras ps = new Paras();
		ps.SQL = sql;
		return RunSQL(ps);
	}
	public static int RunSQL(DBUrlType dburl, String sql)
	{
		if (sql == null || sql.trim().equals(""))
		{
			return 1;
		}
		Paras ps = new Paras();
		ps.SQL = sql;
		return RunSQL(ps);


		//switch (dburl)
		//{
		//    case DBUrlType.AppCenterDSN:
		//        return RunSQL(ps);
		//    //case DBUrlType.DBAccessOfMSSQL1:
		//    //    return DBAccessOfMSSQL1.RunSQL(sql);
		//    //case DBUrlType.DBAccessOfMSSQL2:
		//    //    return DBAccessOfMSSQL2.RunSQL(sql);
		//    //case DBUrlType.DBAccessOfOracle1:
		//    //    return DBAccessOfOracle1.RunSQL(sql);
		//    //case DBUrlType.DBAccessOfOracle2:
		//    //    return DBAccessOfOracle2.RunSQL(sql);
		//    default:
		//        throw new Exception("@没有判断的类型" + dburl.ToString());
		//}
	}
	public static int RunSQL(String sql, String paraKey, Object val)
	{
		Paras ens = new Paras();
		ens.Add(paraKey, val);
		return RunSQL(sql, ens);
	}
	public static int RunSQL(String sql, String paraKey1, Object val1, String paraKey2, Object val2)
	{
		Paras ens = new Paras();
		ens.Add(paraKey1, val1);
		ens.Add(paraKey2, val2);
		return RunSQL(sql, ens);
	}
	public static int RunSQL(String sql, String paraKey1, Object val1, String paraKey2, Object val2, String k3, Object v3)
	{
		Paras ens = new Paras();
		ens.Add(paraKey1, val1);
		ens.Add(paraKey2, val2);
		ens.Add(k3, v3);
		return RunSQL(sql, ens);
	}
	/** 
	 
	*/
	public static boolean lockRunSQL = false;
	/** 
	 执行sql
	 
	 @param sql
	 @param paras
	 @return 
	*/
	public static int RunSQL(String sql, Paras paras)
	{
		if (DataType.IsNullOrEmpty(sql))
		{
			return 1;
		}

		int result = 0;
		try
		{
			switch (getAppCenterDBType())
			{
				case MSSQL:
					result = RunSQL_200705_SQL(sql, paras);
					break;
				case Oracle:
					result = RunSQL_200705_Ora(sql.replace("]", "").replace("[", ""), paras);
					break;
				case MySQL:
					result = RunSQL_200705_MySQL(sql, paras);
					break;
				case PostgreSQL:
					result = RunSQL_201902_PSQL(sql, paras);
					break;
				//case DBType.Informix:
				//    result = RunSQL_201205_Informix(sql, paras);
				//    break;
				default:
					throw new RuntimeException("err@RunSQL发现未知的数据库连接类型！");
			}
			lockRunSQL = false;
			return result;
		}
		catch (RuntimeException ex)
		{
			lockRunSQL = false;
			String msg = "";
			Object tempVar = sql.Clone();
			String mysql = tempVar instanceof String ? (String)tempVar : null;

			String dbstr = BP.Sys.SystemConfig.getAppCenterDBVarStr();
			for (Para p : paras)
			{
				msg += "@" + p.ParaName + "=" + p.val + "," + p.DAType.toString();

				String str = mysql;

				mysql = mysql.replace(dbstr + p.ParaName + ",", "'" + p.val + "',");

				// add by qin 16/3/22  
				if (mysql.equals(str)) //表明类似":OID"的字段没被替换
				{
					mysql = mysql.replace(dbstr + p.ParaName, "'" + p.val + "'");
				}

			}
			throw new RuntimeException("执行sql错误:" + ex.getMessage() + " Paras(" + paras.Count + ")=" + msg + "<hr>" + mysql);
		}
	}
	private static Npgsql.NpgsqlConnection _conn = null;
	private static boolean isCloseConn = true;
	private static Npgsql.NpgsqlConnection getconnOfPGSQL()
	{
		return new Npgsql.NpgsqlConnection(SystemConfig.getAppCenterDSN());

		if (_conn == null)
		{
			_conn = new Npgsql.NpgsqlConnection(SystemConfig.getAppCenterDSN());
			return _conn;
		}
		return _conn;
	}
	/** 
	 运行sql返回结果
	 
	 @param sql sql
	 @param paras 参数
	 @return 执行的结果
	*/
	private static int RunSQL_201902_PSQL(String sql, Paras paras)
	{
		if (1 == 1)
		{
			if (paras == null)
			{
				paras = new Paras();
			}
			paras.SQL = sql;
		   // BP.DA.Log.DebugWriteInfo(paras.SQLNoPara+" ; ");
		}

		//Npgsql.NpgsqlConnection conn =   new Npgsql.NpgsqlConnection(SystemConfig.AppCenterDSN);
		Npgsql.NpgsqlConnection conn = DBAccess.getconnOfPGSQL(); // new Npgsql.NpgsqlConnection(SystemConfig.AppCenterDSN);

		if (conn.State != System.Data.ConnectionState.Open)
		{
			conn.ConnectionString = SystemConfig.getAppCenterDSN();
			conn.Open();
		}

		Npgsql.NpgsqlCommand cmd = new Npgsql.NpgsqlCommand(sql, conn);
		cmd.CommandType = CommandType.Text;

		try
		{
			for (Para para : paras)
			{
				Npgsql.NpgsqlParameter oraP = new Npgsql.NpgsqlParameter(para.ParaName, para.val);
				cmd.Parameters.Add(oraP);
			}
			int i = cmd.ExecuteNonQuery();
			cmd.Dispose();

			if (isCloseConn == true)
			{
			   conn.Close();
			}
			return i;
		}
		catch (RuntimeException ex)
		{
			cmd.Dispose();
			conn.Close();

			paras.SQL = sql;
			String msg = "";
			if (paras.Count == 0)
			{
				msg = "SQL=" + sql + ",异常信息:" + ex.getMessage();
			}
			else
			{
				msg = "SQL=" + paras.getSQLNoPara() + ",异常信息:" + ex.getMessage();
			}

			Log.DefaultLogWriteLineInfo(msg);
			throw new RuntimeException(msg);
		}
		finally
		{
			cmd.Dispose();
			conn.Close();
		}
	}
	/** 
	 运行sql返回结果
	 
	 @param sql sql
	 @param paras 参数
	 @return 执行的结果
	*/
	private static int RunSQL_200705_SQL(String sql, Paras paras)
	{
		SqlConnection conn = new SqlConnection(SystemConfig.getAppCenterDSN());
		if (conn.State != System.Data.ConnectionState.Open)
		{
			conn.ConnectionString = SystemConfig.getAppCenterDSN();
			conn.Open();
		}
		SqlCommand cmd = new SqlCommand(sql, conn);
		cmd.CommandType = CommandType.Text;

		try
		{
			for (Para para : paras)
			{
				SqlParameter oraP = new SqlParameter(para.ParaName, para.val);
				cmd.Parameters.Add(oraP);
			}
			int i = cmd.ExecuteNonQuery();
			cmd.Dispose();
			conn.Close();
			return i;
		}
		catch (RuntimeException ex)
		{
			cmd.Dispose();
			conn.Close();

			paras.SQL = sql;
			String msg = "";
			if (paras.Count == 0)
			{
				msg = "SQL=" + sql + ",异常信息:" + ex.getMessage();
			}
			else
			{
				msg = "SQL=" + paras.getSQLNoPara() + ",异常信息:" + ex.getMessage();
			}

			Log.DefaultLogWriteLineInfo(msg);
			throw new RuntimeException(msg);
		}
		finally
		{
			cmd.Dispose();
			conn.Close();
		}
	}
	/** 
	 运行sql
	 
	 @param sql sql
	 @return 执行结果
	*/
	private static int RunSQL_200705_MySQL(String sql)
	{
		return RunSQL_200705_MySQL(sql, new Paras());
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 处理mysql conn的缓存.
	private static Hashtable _ConnHTOfMySQL = null;
	public static Hashtable getConnHTOfMySQL()
	{
		if (_ConnHTOfMySQL == null || _ConnHTOfMySQL.size() <= 0)
		{
			_ConnHTOfMySQL = new Hashtable();
			int numConn = 10;
			for (int i = 0; i < numConn; i++)
			{
				MySqlConnection conn = new MySqlConnection(SystemConfig.getAppCenterDSN());
				conn.Open(); //打开连接.
				_ConnHTOfMySQL.put("Conn" + i, conn);
			}
		}
		return _ConnHTOfMySQL;
	}
	public static MySqlConnection getGetOneMySQLConn()
	{
		for (MySqlConnection conn : _ConnHTOfMySQL)
		{
			if (conn.State == ConnectionState.Closed)
			{
				conn.Open();
				return conn;
			}
		}
		return null;
			//foreach (MySqlConnection conn in _ConnHTOfMySQL)
			//{
			//}
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 处理mysql conn的缓存.

	/** 
	 RunSQL_200705_MySQL
	 
	 @param sql
	 @param paras
	 @return 
	*/
	private static int RunSQL_200705_MySQL(String sql, Paras paras)
	{

		MySqlConnection connOfMySQL = new MySqlConnection(SystemConfig.getAppCenterDSN());
		if (connOfMySQL.State != System.Data.ConnectionState.Open)
		{
			connOfMySQL.ConnectionString = SystemConfig.getAppCenterDSN();
			connOfMySQL.Open();
		}

		int i = 0;
		try
		{
			MySqlCommand cmd = new MySqlCommand(sql, connOfMySQL);
			cmd.CommandType = CommandType.Text;
			for (Para para : paras)
			{
				MySqlParameter oraP = new MySqlParameter(para.ParaName, para.val);
				cmd.Parameters.Add(oraP);
			}
			i = cmd.ExecuteNonQuery();
			cmd.Dispose();

			connOfMySQL.Close();
			//   connOfMySQL.Dispose();
			return i;
		}
		catch (RuntimeException ex)
		{
			connOfMySQL.Close();
			connOfMySQL.Dispose();
			throw new RuntimeException(ex.getMessage() + "@SQL:" + sql);
		}
	}
	private static int RunSQL_200705_Ora(String sql, Paras paras)
	{
		OracleConnection conn = new OracleConnection(SystemConfig.getAppCenterDSN());
		try
		{
			if (conn.State != System.Data.ConnectionState.Open)
			{
				conn.ConnectionString = SystemConfig.getAppCenterDSN();
				conn.Open();
			}

			OracleCommand cmd = new OracleCommand(sql, conn);
			cmd.CommandType = CommandType.Text;

			for (Para para : paras)
			{
				OracleParameter oraP = new OracleParameter(para.ParaName, para.getDATypeOfOra());
				oraP.Size = para.Size;

				if (para.getDATypeOfOra() == OracleDbType.Clob)
				{
					if (DataType.IsNullOrEmpty(para.val instanceof String ? (String)para.val : null) == true)
					{
						oraP.Value = DBNull.Value;
					}
					else
					{
						oraP.Value = para.val;
					}
				}
				else
				{
					oraP.Value = para.val;
				}

				oraP.DbType = para.DAType;

				cmd.Parameters.Add(oraP);
			}
			int i = cmd.ExecuteNonQuery();
			cmd.Dispose();
			conn.Close(); //把它关闭.
			return i;
		}
		catch (RuntimeException ex)
		{
			conn.Close(); //把它关闭.


			if (paras != null)
			{
				for (Para item : paras)
				{
					if (item.DAType == DbType.String)
					{
						if (sql.contains(":" + item.ParaName + ","))
						{
							sql = sql.replace(":" + item.ParaName + ",", "'" + item.val + "',");
						}
						else
						{
							sql = sql.replace(":" + item.ParaName, "'" + item.val + "'");
						}
					}
					else
					{
						if (sql.contains(":" + item.ParaName + ","))
						{
							sql = sql.replace(":" + item.ParaName + ",", item.val + ",");
						}
						else
						{
							sql = sql.replace(":" + item.ParaName, item.val.toString());
						}
					}
				}
			}

			if (BP.Sys.SystemConfig.getIsDebug())
			{
				String msg = "RunSQL2   SQL=" + sql + ex.getMessage();
				//Log.DebugWriteError(msg);

				throw new RuntimeException("err@" + ex.getMessage() + " SQL=" + sql);
			}
			else
			{
				//    Log.DebugWriteError(ex.Message);
				throw new RuntimeException(ex.getMessage() + "@可以执行的SQL:" + sql);
			}
		}
		finally
		{
			conn.Close();
		}
	}

	/*
	/// <summary>
	/// 运行sql
	/// </summary>
	/// <param name="sql">sql</param>
	/// <returns>执行结果</returns>
	private static int RunSQL_201205_Informix(string sql)
	{
	    return RunSQL_201205_Informix(sql, new Paras());
	}
	private static int RunSQL_201205_Informix(string sql, Paras paras)
	{
	    if (paras.Count != 0)
	        sql = DealInformixSQL(sql);

	    IfxConnection conn = new IfxConnection(SystemConfig.AppCenterDSN);
	    try
	    {
	        if (conn == null)
	            conn = new IfxConnection(SystemConfig.AppCenterDSN);

	        if (conn.State != System.Data.ConnectionState.Open)
	        {
	            conn.ConnectionString = SystemConfig.AppCenterDSN;
	            conn.Open();
	        }

	        IfxCommand cmd = new IfxCommand(sql, conn);
	        cmd.CommandType = CommandType.Text;
	        foreach (Para para in paras)
	        {
	            IfxParameter oraP = new IfxParameter(para.ParaName, para.val);
	            cmd.Parameters.Add(oraP);
	        }

	        int i = cmd.ExecuteNonQuery();
	        cmd.Dispose();
	        conn.Close();
	        return i;
	    }
	    catch (System.Exception ex)
	    {
	        conn.Close();
	        string msg = "RunSQL2   SQL=" + sql + "\r\n Message=: " + ex.Message;
	        Log.DebugWriteError(msg);
	        throw new Exception(msg);
	    }
	    finally
	    {
	        conn.Close();
	    }
	}
	*/
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 运行SQL 返回 DataTable
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 在指定的 Connection 上执行

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region SqlConnection
	/** 
	 锁
	*/
	private static boolean lock_msSQL_ReturnTable = false;
	public static DataTable RunSQLReturnTable(String oraSQL, OracleConnection conn, CommandType sqlType, String dsn)
	{

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if DEBUG
		//Debug.WriteLine( oraSQL );
//#endif


		try
		{
			if (conn == null)
			{
				conn = new OracleConnection(dsn);
				conn.Open();
			}

			if (conn.State != ConnectionState.Open)
			{
				conn.ConnectionString = dsn;
				conn.Open();
			}

			OracleDataAdapter oraAda = new OracleDataAdapter(oraSQL, conn);
			oraAda.SelectCommand.CommandType = sqlType;


			DataTable oratb = new DataTable("otb");
			oraAda.Fill(oratb);

			// peng add 07-19
			oraAda.Dispose();

			return oratb;
		}
		catch (RuntimeException ex)
		{
			throw new RuntimeException(ex.getMessage() + " [RunSQLReturnTable on OracleConnection dsn=App ] sql=" + oraSQL + "<br>");
		}
		finally
		{
			conn.Close();
		}
	}

	/** 
	 
	 
	 @param msSQL
	 @param sqlconn
	 @param sqlType
	 @param pars
	 @return 
	*/
	public static DataTable RunSQLReturnTable(String msSQL, SqlConnection conn, String connStr, CommandType sqlType, Object... pars)
	{
		String msg = "step1";

		if (conn.State == ConnectionState.Closed)
		{
			conn.ConnectionString = connStr;
			conn.Open();
		}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if DEBUG
		Debug.WriteLine(msSQL);
//#endif

		while (lock_msSQL_ReturnTable)
		{
			;
		}

		SqlDataAdapter msAda = new SqlDataAdapter(msSQL, conn);
		msg = "error 2";
		msAda.SelectCommand.CommandType = sqlType;
		if (pars != null)
		{
			//CommandType.
			for (Object par : pars)
			{
				msAda.SelectCommand.Parameters.AddWithValue("par", par);
			}
		}

		DataTable mstb = new DataTable("mstb");
		//如果是锁定状态，就等待
		lock_msSQL_ReturnTable = true; //锁定
		try
		{
			msg = "error 3";
			try
			{
				msg = "4";
				msAda.Fill(mstb);
			}
			catch (RuntimeException ex)
			{
				msg = "5";
				lock_msSQL_ReturnTable = false;
				conn.Close();
				throw new RuntimeException(ex.getMessage() + " msg=" + msg + " Run@DBAccess");
			}
			msg = "10";
			msAda.Dispose();
			msg = "11";
			//				if (SystemConfig.IsBSsystem==false )
			//				{
			//					msg="13";
			//					sqlconn.Close();
			//				}
			msg = "14";
			lock_msSQL_ReturnTable = false; // 返回前一定要开锁
			conn.Close();
		}
		catch (RuntimeException ex)
		{
			lock_msSQL_ReturnTable = false;
			conn.Close();
			throw new RuntimeException("[RunSQLReturnTable on SqlConnection 1] step = " + msg + "<BR>" + ex.getMessage() + " sql=" + msSQL);
		}
		return mstb;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region OracleConnection
	private static DataTable RunSQLReturnTable_200705_Ora(String selectSQL, Paras paras)
	{
		OracleConnection conn = new OracleConnection(SystemConfig.getAppCenterDSN());
		try
		{
			if (conn.State != ConnectionState.Open)
			{
				conn.Open();
			}

			OracleDataAdapter ada = new OracleDataAdapter(selectSQL, conn);
			ada.SelectCommand.CommandType = CommandType.Text;

			// 加入参数
			if (paras != null)
			{
				for (Para para : paras)
				{
					OracleParameter myParameter = new OracleParameter(para.ParaName, para.getDATypeOfOra());
					myParameter.Size = para.Size;
					myParameter.Value = para.val;
					ada.SelectCommand.Parameters.Add(myParameter);
				}
			}

			DataTable oratb = new DataTable("otb");
			ada.Fill(oratb);
			ada.Dispose();
			conn.Close();
			return oratb;
		}
		catch (RuntimeException ex)
		{
			conn.Close();
			String msg = "@运行查询在(RunSQLReturnTable_200705_Ora with paras)出错 sql=" + selectSQL + " @异常信息：" + ex.getMessage();
			msg += "@Para Num= " + paras.Count;
			for (Para pa : paras)
			{
				msg += "@" + pa.ParaName + "=" + pa.val;
			}
			Log.DebugWriteError(msg);
			throw new RuntimeException(msg);
		}
		finally
		{
			conn.Close();
		}
	}
	private static DataTable RunSQLReturnTable_200705_SQL(String selectSQL)
	{
		SqlConnection conn = new SqlConnection(SystemConfig.getAppCenterDSN());
		try
		{
			if (conn.State != ConnectionState.Open)
			{
				conn.Open();
			}

			SqlDataAdapter ada = new SqlDataAdapter(selectSQL, conn);
			ada.SelectCommand.CommandType = CommandType.Text;
			DataTable oratb = new DataTable("otb");
			ada.Fill(oratb);
			ada.Dispose();
			return oratb;
		}
		catch (RuntimeException ex)
		{
			String msgErr = ex.getMessage();
			String msg = "@运行查询在(RunSQLReturnTable_200705_SQL)出错 sql=" + selectSQL + " @异常信息：" + msgErr;
			Log.DebugWriteError(msg);
			throw new RuntimeException(msg);
		}
	}

	private static DataTable RunSQLReturnTable_200705_SQL(String sql, Paras paras)
	{
		SqlConnection conn = new SqlConnection(SystemConfig.getAppCenterDSN());
		if (conn.State != ConnectionState.Open)
		{
			conn.Open();
		}

		SqlDataAdapter ada = new SqlDataAdapter(sql, conn);
		ada.SelectCommand.CommandType = CommandType.Text;

		// 加入参数
		if (paras != null) //qin 解决为null时的异常
		{
			for (Para para : paras)
			{
				SqlParameter myParameter = new SqlParameter(para.ParaName, para.val);
				myParameter.Size = para.Size;
				ada.SelectCommand.Parameters.Add(myParameter);
			}
		}

		try
		{
			DataTable oratb = new DataTable("otb");
			ada.Fill(oratb);
			ada.Dispose();
			conn.Close();
			return oratb;
		}
		catch (RuntimeException ex)
		{
			ada.Dispose();
			conn.Close();
			throw new RuntimeException("SQL=" + sql + " Exception=" + ex.getMessage());
		}
	}
	/** 
	 运行sql返回datatable
	 
	 @param sql 要运行的SQL
	 @param paras 参数
	 @return 返回的数据.
	*/
	private static DataTable RunSQLReturnTable_201902_PSQL(String sql, Paras paras)
	{
		Npgsql.NpgsqlConnection conn = DBAccess.getconnOfPGSQL(); // new Npgsql.NpgsqlConnection(SystemConfig.AppCenterDSN);
		if (conn.State != ConnectionState.Open)
		{
			conn.Open();
		}

		Npgsql.NpgsqlDataAdapter ada = new Npgsql.NpgsqlDataAdapter(sql, conn);
		ada.SelectCommand.CommandType = CommandType.Text;

		// 加入参数
		if (paras != null) //qin 解决为null时的异常
		{
			for (Para para : paras)
			{
				// 2019-8-8 zl 适配postgreSql新版驱动，要求数据类型一致
				Object valObj = para.val;

				Npgsql.NpgsqlParameter myParameter = new Npgsql.NpgsqlParameter(para.ParaName, valObj);
				myParameter.Size = para.Size;
				ada.SelectCommand.Parameters.Add(myParameter);
			}
		}

		try
		{
			DataTable oratb = new DataTable("otb");
			ada.Fill(oratb);
			ada.Dispose();

			if (isCloseConn == true)
			{
			   conn.Close();
			}

			return oratb;
		}
		catch (RuntimeException ex)
		{
			ada.Dispose();
			conn.Close();
			throw new RuntimeException("SQL=" + sql + " Exception=" + ex.getMessage());
		}
	}
	private static String DealInformixSQL(String sql)
	{
		if (sql.contains("?") == false)
		{
			return sql;
		}

		String mysql = "";
		if (sql.contains("? ") == true || sql.contains("?,") == true)
		{
			/*如果有空格,说明已经替换过了。*/
			return sql;
		}
		else
		{
			sql += " ";
			/*说明需要处理的变量.*/
			String[] strs = sql.split("[?]", -1);
			mysql = strs[0];
			for (int i = 1; i < strs.length; i++)
			{
				String str = strs[i];
				switch (str.substring(0, 1))
				{
					case " ":
						mysql += "?" + str;
						break;
					case ")":
						mysql += "?" + str;
						break;
					case ",":
						mysql += "?" + str;
						break;
					default:
						char[] chs = str.toCharArray();
						for (char c : chs)
						{
							if (c == ',')
							{
								int idx1 = str.indexOf(",");
								mysql += "?" + str.substring(idx1);
								break;
							}

							if (c == ')')
							{
								int idx1 = str.indexOf(")");
								mysql += "?" + str.substring(idx1);
								break;
							}

							if (c == ' ')
							{
								int idx1 = str.indexOf(" ");
								mysql += "?" + str.substring(idx1);
								break;
							}
						}

						//else
						//{
						//    mysql += "?" + str;
						//}

						//if (str.Contains(")") == true)
						//    mysql += "?" + str.Substring(str.IndexOf(")"));
						//else
						//    mysql += "?" + str;
						break;
				}
			}
		}
		return mysql;
	}

	/*
	/// <summary>
	/// RunSQLReturnTable_200705_Informix
	/// </summary>
	/// <param name="selectSQL">要执行的sql</param>
	/// <returns>返回table</returns>
	private static DataTable RunSQLReturnTable_201205_Informix(string sql, Paras paras)
	{
	    //if (paras.Count != 0 && sql.Contains("?") == false)
	    //{
	    //    sql = DealInformixSQL(sql);
	    //}
	    sql = DealInformixSQL(sql);

	    IfxConnection conn = new IfxConnection(SystemConfig.AppCenterDSN);
	    if (conn.State != ConnectionState.Open)
	        conn.Open();

	    IfxDataAdapter ada = new IfxDataAdapter(sql, conn);
	    ada.SelectCommand.CommandType = CommandType.Text;

	    // 加入参数
	    foreach (Para para in paras)
	    {
	        IfxParameter myParameter = new IfxParameter(para.ParaName, para.val);
	        myParameter.Size = para.Size;
	        ada.SelectCommand.Parameters.Add(myParameter);
	    }

	    try
	    {
	        DataTable oratb = new DataTable("otb");
	        ada.Fill(oratb);
	        ada.Dispose();
	        conn.Close();
	        return oratb;
	    }
	    catch (Exception ex)
	    {
	        ada.Dispose();
	        conn.Close();
	        Log.DefaultLogWriteLineError(sql);
	        Log.DefaultLogWriteLineError(ex.Message);

	        throw new Exception("SQL=" + sql + " Exception=" + ex.Message);
	    }
	    finally
	    {
	        ada.Dispose();
	        conn.Close();
	    }
	}
	*/
	/** 
	 RunSQLReturnTable_200705_SQL
	 
	 @param selectSQL 要执行的sql
	 @return 返回table
	*/
	private static DataTable RunSQLReturnTable_200705_MySQL(String selectSQL)
	{
		return RunSQLReturnTable_200705_MySQL(selectSQL, new Paras());
	}
	/** 
	 RunSQLReturnTable_200705_SQL
	 
	 @param selectSQL 要执行的sql
	 @return 返回table
	*/
	private static DataTable RunSQLReturnTable_200705_MySQL(String sql, Paras paras)
	{
		//  string mcs = "Data Source=127.0.0.1;User ID=root;Password=root;DataBase=wk;Charset=gb2312;";
		//  MySqlConnection conn = new MySqlConnection(SystemConfig.AppCenterDSN);
		//  SqlDataAdapter ad = new SqlDataAdapter("select username,password from person", conn);
		//  DataTable dt = new DataTable();
		//  conn.Open();
		//  ad.Fill(dt);
		//  conn.Close();
		//  return dt;

		try (MySqlConnection conn = new MySqlConnection(SystemConfig.getAppCenterDSN()))
		{
			try (MySqlDataAdapter ada = new MySqlDataAdapter(sql, conn))
			{
				if (conn.State != ConnectionState.Open)
				{
					conn.Open();
				}

				ada.SelectCommand.CommandType = CommandType.Text;

				// 加入参数
				for (Para para : paras)
				{
					MySqlParameter myParameter = new MySqlParameter(para.ParaName, para.val);
					myParameter.Size = para.Size;
					ada.SelectCommand.Parameters.Add(myParameter);
				}

				try
				{
					DataTable oratb = new DataTable("otb");
					ada.Fill(oratb);
					return oratb;
				}
				catch (RuntimeException ex)
				{
					conn.Close();
					throw new RuntimeException("SQL=" + sql + " Exception=" + ex.getMessage());
				}
			}
		}
	}
	/*
	/// <summary>
	/// RunSQLReturnTable_200705_SQL
	/// </summary>
	/// <param name="selectSQL">要执行的sql</param>
	/// <returns>返回table</returns>
	private static DataTable RunSQLReturnTable_201205_Informix(string selectSQL)
	{
	    return RunSQLReturnTable_201205_Informix(selectSQL, new Paras());
	}*/
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 在当前Connection上执行
	public static DataTable RunSQLReturnTable(Paras ps)
	{
		return RunSQLReturnTable(ps.SQL, ps);
	}
	public static int RunSQLReturnTableCount = 0;
	/** 
	 传递一个select 语句返回一个查询结果集合。
	 
	 @param sql select sql
	 @return 查询结果集合DataTable
	*/
	public static DataTable RunSQLReturnTable(String sql)
	{
		Paras ps = new Paras();
		return RunSQLReturnTable(sql, ps);
	}
	public static DataTable RunSQLReturnTable(String sql, String key1, Object v1, String key2, Object v2)
	{
		Paras ens = new Paras();
		ens.Add(key1, v1);
		ens.Add(key2, v2);
		return RunSQLReturnTable(sql, ens);
	}
	public static DataTable RunSQLReturnTable(String sql, String key, Object val)
	{
		Paras ens = new Paras();
		ens.Add(key, val);
		return RunSQLReturnTable(sql, ens);
	}

	/** 
	 通用SQL查询分页返回DataTable
	 
	 @param sql SQL语句，不带排序（Order By）语句
	 @param pageSize 每页记录数量
	 @param pageIdx 请求页码
	 @param key 记录主键（不能为空，不能有重复，必须包含在返回字段中）
	 @param orderKey 排序字段（此字段必须包含在返回字段中）
	 @param orderType 排序方式，ASC/DESC
	 @return 
	*/
	public static DataTable RunSQLReturnTable(String sql, int pageSize, int pageIdx, String key, String orderKey, String orderType)
	{
		switch (DBAccess.getAppCenterDBType())
		{
			case MSSQL:
				return RunSQLReturnTable_201612_SQL(sql, pageSize, pageIdx, key, orderKey, orderType);
			case Oracle:
				return RunSQLReturnTable_201612_Ora(sql, pageSize, pageIdx, orderKey, orderType);
			case MySQL:
				return RunSQLReturnTable_201612_MySql(sql, pageSize, pageIdx, key, orderKey, orderType);
			case PostgreSQL:
				return RunSQLReturnTable_201612_PostgreSQL(sql, pageSize, pageIdx, key, orderKey, orderType);
			default:
				throw new RuntimeException("@未涉及的数据库类型！");
		}
	}
	private static DataTable RunSQLReturnTable_201612_PostgreSQL(String sql, int pageSize, int pageIdx, String key, String orderKey, String orderType)
	{
		String sqlstr = "";
		orderType = tangible.StringHelper.isNullOrWhiteSpace(orderType) ? "ASC" : orderType.toUpperCase();

		if (pageIdx < 1)
		{
			pageIdx = 1;
		}
		//    limit  A  offset  B;  A就是你需要多少行B就是查询的起点位置
		sqlstr = "SELECT * FROM (" + sql + ") T1 WHERE T1." + key + (orderType.equals("ASC") ? " >= " : " <= ") + "(SELECT T2." + key + " FROM (" + sql + ") T2"
				 + (tangible.StringHelper.isNullOrWhiteSpace(orderKey) ? "" : String.format(" ORDER BY T2.%1$s %2$s", orderKey, orderType)) + " LIMIT " + ((pageIdx - 1) * pageSize + 1) + " offset 1) LIMIT " + pageSize;
		return RunSQLReturnTable(sqlstr);
	}
	/** 
	 通用SqlServer查询分页返回DataTable
	 
	 @param sql SQL语句，不带排序（Order By）语句
	 @param pageSize 每页记录数量
	 @param pageIdx 请求页码
	 @param key 记录主键（不能为空，不能有重复，必须包含在返回字段中）
	 @param orderKey 排序字段（此字段必须包含在返回字段中）
	 @param orderType 排序方式，ASC/DESC
	 @return 
	*/
	private static DataTable RunSQLReturnTable_201612_SQL(String sql, int pageSize, int pageIdx, String key, String orderKey, String orderType)
	{
		String sqlstr = "";

		orderType = tangible.StringHelper.isNullOrWhiteSpace(orderType) ? "ASC" : orderType.toUpperCase();

		if (pageIdx < 1)
		{
			pageIdx = 1;
		}

		if (pageIdx == 1)
		{
			sqlstr = "SELECT TOP " + pageSize + " * FROM (" + sql + ") T1" + (tangible.StringHelper.isNullOrWhiteSpace(orderKey) ? "" : String.format(" ORDER BY T1.%1$s %2$s", orderKey, orderType));
		}
		else
		{
			sqlstr = "SELECT TOP " + pageSize + " * FROM (" + sql + ") T1"
					 + " WHERE T1." + key + (orderType.equals("ASC") ? " > " : " < ") + "("
//C# TO JAVA CONVERTER TODO TASK: C# to Java Converter could not resolve the named parameters in the following line:
//ORIGINAL LINE: + " SELECT " + (orderType == "ASC" ? "MAX(T3." : "MIN(T3.") + key + ") FROM ("
					 + " SELECT " + (orderType.equals("ASC") ? "MAX(T3." : "MIN(T3.") + key + ") FROM ("
					 + " SELECT TOP ((" + pageIdx + " - 1) * 10) T2." + key + "FROM (" + sql + ") T2"
					 + (tangible.StringHelper.isNullOrWhiteSpace(orderKey) ? "" : String.format(" ORDER BY T2.%1$s %2$s", orderKey, orderType)) + " ) T3)"
					 + (tangible.StringHelper.isNullOrWhiteSpace(orderKey) ? "" : String.format(" ORDER BY T.%1$s %2$s", orderKey, orderType));
		}

		return RunSQLReturnTable(sqlstr);
	}

	/** 
	 通用Oracle查询分页返回DataTable
	 
	 @param sql SQL语句，不带排序（Order By）语句
	 @param pageSize 每页记录数量
	 @param pageIdx 请求页码
	 @param orderKey 排序字段（此字段必须包含在返回字段中）
	 @param orderType 排序方式，ASC/DESC
	 @return 
	*/
	private static DataTable RunSQLReturnTable_201612_Ora(String sql, int pageSize, int pageIdx, String orderKey, String orderType)
	{
		if (pageIdx < 1)
		{
			pageIdx = 1;
		}

		int start = (pageIdx - 1) * pageSize + 1;
		int end = pageSize * pageIdx;

		orderType = tangible.StringHelper.isNullOrWhiteSpace(orderType) ? "ASC" : orderType.toUpperCase();

		String sqlstr = "SELECT * FROM ( SELECT T1.*, ROWNUM RN "
						+ "FROM (SELECT * FROM  (" + sql + ") T2 "
						+ (tangible.StringHelper.isNullOrWhiteSpace(orderType) ? "" : String.format("ORDER BY T2.%1$s %2$s", orderKey, orderType)) + ") T1 WHERE ROWNUM <= " + end + " ) WHERE RN >=" + start;

		return RunSQLReturnTable(sqlstr);
	}

	/** 
	 通用MySql查询分页返回DataTable
	 
	 @param sql SQL语句，不带排序（Order By）语句
	 @param pageSize 每页记录数量
	 @param pageIdx 请求页码
	 @param key 记录主键（不能为空，不能有重复，必须包含在返回字段中）
	 @param orderKey 排序字段（此字段必须包含在返回字段中）
	 @param orderType 排序方式，ASC/DESC
	 @return 
	*/
	private static DataTable RunSQLReturnTable_201612_MySql(String sql, int pageSize, int pageIdx, String key, String orderKey, String orderType)
	{
		String sqlstr = "";
		orderType = tangible.StringHelper.isNullOrWhiteSpace(orderType) ? "ASC" : orderType.toUpperCase();

		if (pageIdx < 1)
		{
			pageIdx = 1;
		}

		sqlstr = "SELECT * FROM (" + sql + ") T1 WHERE T1." + key + (orderType.equals("ASC") ? " >= " : " <= ") + "(SELECT T2." + key + " FROM (" + sql + ") T2"
				 + (tangible.StringHelper.isNullOrWhiteSpace(orderKey) ? "" : String.format(" ORDER BY T2.%1$s %2$s", orderKey, orderType)) + " LIMIT " + ((pageIdx - 1) * pageSize) + ",1) LIMIT " + pageSize;

		return RunSQLReturnTable(sqlstr);
	}

	private static boolean lockRunSQLReTable = false;
	/** 
	 运行SQL
	 
	 @param sql 带有参数的SQL语句
	 @param paras 参数
	 @return 返回执行结果
	*/
	public static DataTable RunSQLReturnTable(String sql, Paras paras)
	{
		if (DataType.IsNullOrEmpty(sql))
		{
			throw new RuntimeException("要执行的 sql = null ");
		}

		try
		{
			DataTable dt = null;
			switch (getAppCenterDBType())
			{
				case MSSQL:
					dt = RunSQLReturnTable_200705_SQL(sql, paras);
					break;
				case Oracle:
					dt = RunSQLReturnTable_200705_Ora(sql, paras);
					break;
				//case DBType.Informix:
				//    dt = RunSQLReturnTable_201205_Informix(sql, paras);
				//    break;
				case PostgreSQL:
					dt = RunSQLReturnTable_201902_PSQL(sql, paras);
					break;
				case MySQL:
					dt = RunSQLReturnTable_200705_MySQL(sql, paras);
					break;
				default:
					throw new RuntimeException("err@RunSQLReturnTable发现未知的数据库连接类型！");
			}
			return dt;
		}
		catch (RuntimeException ex)
		{
			Log.DefaultLogWriteLineError(ex.getMessage());
			throw ex;
		}
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 在当前Connection上执行

	public static DataTable ToUpper(DataTable dt)
	{
		if (BP.Sys.SystemConfig.getAppCenterDBType() == DBType.Oracle)
		{
			return dt;
		}

		for (DataColumn dc : dt.Columns)
		{
			dc.ColumnName = dc.ColumnName.toUpperCase();
		}
		return dt;

		//return dt;
	}



//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 查询单个值的方法.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region OleDbConnection
	public static float RunSQLReturnValFloat(Paras ps)
	{
		return RunSQLReturnValFloat(ps.SQL, ps, 0);
	}
	public static float RunSQLReturnValFloat(String sql, Paras ps, float val)
	{
		ps.SQL = sql;
		Object obj = DA.DBAccess.RunSQLReturnVal(ps);

		try
		{
			if (obj == null || obj.toString().equals(""))
			{
				return val;
			}
			else
			{
				return Float.parseFloat(obj.toString());
			}
		}
		catch (RuntimeException ex)
		{
			throw new RuntimeException(ex.getMessage() + sql + " @OBJ=" + obj);
		}
	}
	/** 
	 运行sql返回float
	 
	 @param sql 要执行的sql,返回一行一列.
	 @param isNullAsVal 如果是空值就返回的默认值
	 @return float的返回值
	*/
	public static float RunSQLReturnValFloat(String sql, float isNullAsVal)
	{
		return RunSQLReturnValFloat(sql, new Paras(), isNullAsVal);
	}
	/** 
	 sdfsd
	 
	 @param sql
	 @return 
	*/
	public static float RunSQLReturnValFloat(String sql)
	{
		try
		{
			return Float.parseFloat(DA.DBAccess.RunSQLReturnVal(sql).toString());
		}
		catch (RuntimeException ex)
		{
			throw new RuntimeException(ex.getMessage() + sql);
		}
	}
	public static int RunSQLReturnValInt(Paras ps, int IsNullReturnVal)
	{
		return RunSQLReturnValInt(ps.SQL, ps, IsNullReturnVal);
	}
	/** 
	 sdfsd
	 
	 @param sql
	 @param IsNullReturnVal
	 @return 
	*/
	public static int RunSQLReturnValInt(String sql, int IsNullReturnVal)
	{
		Object obj = "";
		obj = DA.DBAccess.RunSQLReturnVal(sql);
		if (obj == null || obj.toString().equals("") || obj == DBNull.Value)
		{
			return IsNullReturnVal;
		}
		else
		{
			return (Integer)obj;
		}
	}
	public static int RunSQLReturnValInt(String sql, int IsNullReturnVal, Paras paras)
	{
		Object obj = "";

		obj = DA.DBAccess.RunSQLReturnVal(sql, paras);
		if (obj == null || obj.toString().equals(""))
		{
			return IsNullReturnVal;
		}
		else
		{
			return (Integer)obj;
		}
	}
	public static BigDecimal RunSQLReturnValDecimal(String sql, BigDecimal IsNullReturnVal, int blws)
	{
		Paras ps = new Paras();
		ps.SQL = sql;
		return RunSQLReturnValDecimal(ps, IsNullReturnVal, blws);
	}
	public static BigDecimal RunSQLReturnValDecimal(Paras ps, BigDecimal IsNullReturnVal, int blws)
	{
		try
		{
			Object obj = DA.DBAccess.RunSQLReturnVal(ps);
			if (obj == null || obj.toString().equals(""))
			{
				return IsNullReturnVal;
			}
			else
			{
				BigDecimal d = BigDecimal.Parse(obj.toString());
				return BigDecimal.Round(d, blws);
			}
		}
		catch (RuntimeException ex)
		{
			throw new RuntimeException(ex.getMessage() + ps.SQL);
		}
	}
	public static int RunSQLReturnValInt(Paras ps)
	{
		String str = DBAccess.RunSQLReturnString(ps.SQL, ps);
		if (str.contains("."))
		{
			str = str.substring(0, str.indexOf("."));
		}
		try
		{
			return Integer.parseInt(str);
		}
		catch (RuntimeException ex)
		{
			throw new RuntimeException("@" + ps.SQL + "   Val=" + str + ex.getMessage());
		}
	}
	public static int RunSQLReturnValInt(String sql)
	{
		Object obj = DBAccess.RunSQLReturnVal(sql);
		if (obj == null || obj == DBNull.Value)
		{
			throw new RuntimeException("@没有获取您要查询的数据,请检查SQL:" + sql + " @关于查询出来的详细信息已经记录日志文件，请处理。");
		}
		String s = obj.toString();
		if (s.contains("."))
		{
			s = s.substring(0, s.indexOf("."));
		}
		return Integer.parseInt(s);
	}
	public static int RunSQLReturnValInt(String sql, Paras paras)
	{
		return (Integer)DA.DBAccess.RunSQLReturnVal(sql, paras);
	}
	public static int RunSQLReturnValInt(String sql, Paras paras, int isNullAsVal)
	{
		try
		{
			return (Integer)DA.DBAccess.RunSQLReturnVal(sql, paras);
		}
		catch (java.lang.Exception e)
		{
			return isNullAsVal;
		}
	}
	public static String RunSQLReturnString(String sql, Paras ps)
	{
		if (ps == null)
		{
			ps = new Paras();
		}
		Object obj = DBAccess.RunSQLReturnVal(sql, ps);
		if (obj == DBNull.Value || obj == null)
		{
			return null;
		}
		else
		{
			return obj.toString();
		}
	}
	/** 
	 执行查询返回结果,如果为dbNull 返回 null.
	 
	 @param sql will run sql.
	 @return ,如果为dbNull 返回 null.
	*/
	public static String RunSQLReturnString(String sql)
	{
		try
		{
			return RunSQLReturnString(sql, new Paras());
		}
		catch (RuntimeException ex)
		{
			throw new RuntimeException("@运行 RunSQLReturnString出现错误：" + ex.getMessage() + sql);
		}
	}
	public static String RunSQLReturnStringIsNull(Paras ps, String isNullAsVal)
	{
		String v = RunSQLReturnString(ps);
		if (v == null)
		{
			return isNullAsVal;
		}
		else
		{
			return v;
		}
	}
	/** 
	 运行sql返回一个值
	 
	 @param sql
	 @param isNullAsVal
	 @return 
	*/
	public static String RunSQLReturnStringIsNull(String sql, String isNullAsVal)
	{
		//try{
		String s = RunSQLReturnString(sql, new Paras());
		if (s == null)
		{
			return isNullAsVal;
		}
		return s;
		//}
		//catch (Exception ex)
		//{
		//    Log.DebugWriteInfo("RunSQLReturnStringIsNull@" + ex.Message);
		//    return isNullAsVal;
		//}
	}
	public static String RunSQLReturnString(Paras ps)
	{
		return RunSQLReturnString(ps.SQL, ps);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region SqlConnection
	/** 
	 查询单个值的方法
	 
	 @param sql sql
	 @param conn SqlConnection
	 @return object
	*/
	public static Object RunSQLReturnVal(String sql, SqlConnection conn, String dsn)
	{
		return RunSQLReturnVal(sql, conn, CommandType.Text, dsn);

	}
	/** 
	 查询单个值的方法
	 
	 @param sql sql
	 @param conn SqlConnection
	 @param sqlType CommandType
	 @param pars pars
	 @return object
	*/
	public static Object RunSQLReturnVal(String sql, SqlConnection conn, CommandType sqlType, String dsn, Object... pars)
	{
		//return DBAccess.RunSQLReturnTable(sql,conn,dsn,sqlType,null).Rows[0][0];

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if DEBUG
		Debug.WriteLine(sql);
//#endif

		Object val = null;
		SqlCommand cmd = null;

		try
		{
			if (conn == null)
			{
				conn = new SqlConnection(dsn);
				conn.Open();
			}

			if (conn.State != System.Data.ConnectionState.Open)
			{
				conn.ConnectionString = dsn;
				conn.Open();
			}

			cmd = new SqlCommand(sql, conn);
			cmd.CommandType = sqlType;
			val = cmd.ExecuteScalar();
		}
		catch (RuntimeException ex)
		{
			//return DBAccess.re

			cmd.Cancel();
			conn.Close();
			cmd.Dispose();
			conn.Dispose();
			throw new RuntimeException(ex.getMessage() + " [RunSQLReturnVal on SqlConnection] " + sql);
		}
		//conn.Close();
		return val;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion


//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 在当前的Connection执行 SQL 语句，返回首行首列
	public static int RunSQLReturnCOUNT(String sql)
	{
		return RunSQLReturnTable(sql).Rows.Count;
		//return RunSQLReturnVal( sql ,sql, sql );
	}
	public static Object RunSQLReturnVal(String sql, String pkey, Object val)
	{
		Paras ps = new Paras();
		ps.Add(pkey, val);

		return RunSQLReturnVal(sql, ps);
	}

	public static Object RunSQLReturnVal(String sql, Paras paras)
	{
		RunSQLReturnTableCount++;
		//  Log.DebugWriteInfo("NUMOF " + RunSQLReturnTableCount + "===RunSQLReturnTable sql=" + sql);
		DataTable dt = null;
		switch (SystemConfig.getAppCenterDBType())
		{
			case Oracle:
				dt = DBAccess.RunSQLReturnTable_200705_Ora(sql, paras);
				break;
			case MSSQL:
				dt = DBAccess.RunSQLReturnTable_200705_SQL(sql, paras);
				break;
			case MySQL:
				dt = DBAccess.RunSQLReturnTable_200705_MySQL(sql, paras);
				break;
			case PostgreSQL:
				dt = DBAccess.RunSQLReturnTable_201902_PSQL(sql, paras);
				break;
			//case DBType.Informix:
			//    dt = DBAccess.RunSQLReturnTable_201205_Informix(sql, paras);
			//    break;
			default:
				throw new RuntimeException("@没有判断的数据库类型");
		}

		if (dt.Rows.Count == 0)
		{
			return null;
		}
		return dt.Rows[0][0];
	}
	public static Object RunSQLReturnVal(Paras ps)
	{
		return RunSQLReturnVal(ps.SQL, ps);
	}
	public static Object RunSQLReturnVal(String sql)
	{
		RunSQLReturnTableCount++;
		DataTable dt = null;
		switch (SystemConfig.getAppCenterDBType())
		{
			case Oracle:
				dt = DBAccess.RunSQLReturnTable_200705_Ora(sql, new Paras());
				break;
			case MSSQL:
				dt = DBAccess.RunSQLReturnTable_200705_SQL(sql, new Paras());
				break;
			case PostgreSQL:
				dt = DBAccess.RunSQLReturnTable_201902_PSQL(sql, new Paras());
				break;
			//case DBType.Informix:
			//    dt = DBAccess.RunSQLReturnTable_201205_Informix(sql, new Paras());
			//    break;
			case MySQL:
				dt = DBAccess.RunSQLReturnTable_200705_MySQL(sql, new Paras());
				break;
			default:
				throw new RuntimeException("@没有判断的数据库类型");
		}
		if (dt.Rows.Count == 0)
		{
			return null;
		}
		return dt.Rows[0][0];
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 检查是不是存在
	/** 
	 检查是不是存在
	 
	 @param sql sql
	 @return 检查是不是存在
	*/
	public static boolean IsExits(String sql)
	{
		if (sql.toUpperCase().contains("SELECT") == false)
		{
			throw new RuntimeException("@非法的查询语句" + sql);
		}

		if (RunSQLReturnVal(sql) == null)
		{
			return false;
		}
		return true;
	}
	public static boolean IsExits(String sql, Paras ps)
	{
		if (RunSQLReturnVal(sql, ps) == null)
		{
			return false;
		}
		return true;
	}

	/** 
	 获得table的主键
	 
	 @param table 表名称
	 @return 主键名称、没有返回为空.
	*/
	public static String GetTablePKName(String table)
	{
		BP.DA.Paras ps = new Paras();
		String sql = "";
		switch (getAppCenterDBType())
		{
			case Access:
				return null;
			case MSSQL:
				sql = "SELECT CONSTRAINT_NAME,column_name FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE WHERE table_name =@Tab ";
				ps.Add("Tab", table);
				break;
			case Oracle:
				sql = "SELECT constraint_name, constraint_type,search_condition, r_constraint_name  from user_constraints WHERE table_name = upper(:tab) AND constraint_type = 'P'";
				ps.Add("Tab", table);
				break;
			case MySQL:
				sql = "SELECT CONSTRAINT_NAME , column_name, table_name CONSTRAINT_NAME from INFORMATION_SCHEMA.KEY_COLUMN_USAGE WHERE table_name =@Tab and table_schema='" + SystemConfig.getAppCenterDBDatabase() + "' ";
				ps.Add("Tab", table);
				break;
			case Informix:
				sql = "SELECT * FROM sysconstraints c inner join systables t on c.tabid = t.tabid where t.tabname = lower(?) and constrtype = 'P'";
				ps.Add("Tab", table);
				break;
			case PostgreSQL:
				sql = " SELECT ";
				sql += " pg_constraint.conname AS pk_name ";
				sql += " FROM ";
				sql += " pg_constraint ";
				sql += " INNER JOIN pg_class ON pg_constraint.conrelid = pg_class.oid ";
				sql += " WHERE ";
				sql += " pg_class.relname =:Tab ";
				sql += " AND pg_constraint.contype = 'p' ";
				ps.Add("Tab", table.toLowerCase());
				break;
			default:
				throw new RuntimeException("@GetTablePKName没有判断的数据库类型.");
		}

		DataTable dt = DBAccess.RunSQLReturnTable(sql, ps);
		if (dt.Rows.Count == 0)
		{
			return null;
		}
		return dt.Rows[0][0].toString();
	}
	/** 
	 判断是否存在主键pk .
	 
	 @param tab 物理表
	 @return 是否存在
	*/
	public static boolean IsExitsTabPK(String tab)
	{
		if (DBAccess.GetTablePKName(tab) == null)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	/** 
	 是否是view 
	 
	 @param tabelOrViewName
	 @return 
	*/
	public static boolean IsView(String tabelOrViewName)
	{
		return IsView(tabelOrViewName, SystemConfig.getAppCenterDBType());
	}
	/** 
	 是否是view
	 
	 @param tabelOrViewName
	 @return 
	*/
	public static boolean IsView(String tabelOrViewName, DBType dbType)
	{
		//if (dbType == null) dbType是Enum，永远不会为null. 张磊 2019-7-24
		//    dbType = SystemConfig.AppCenterDBType;

		String sql = "";
		switch (dbType)
		{
			case Oracle:
				sql = "SELECT TABTYPE  FROM TAB WHERE UPPER(TNAME)=:v";
				DataTable oradt = DBAccess.RunSQLReturnTable(sql, "v", tabelOrViewName.toUpperCase());
				if (oradt.Rows.Count == 0)
				{
					return false;
				}

				if (oradt.Rows[0][0].toString().toUpperCase().trim().equals("V"))
				{
					return true;
				}
				else
				{
					return false;
				}

			case MSSQL:
				sql = "select xtype from sysobjects WHERE name =" + SystemConfig.getAppCenterDBVarStr() + "v";
				DataTable dt1 = DBAccess.RunSQLReturnTable(sql, "v", tabelOrViewName);
				if (dt1.Rows.Count == 0)
				{
					return false;
				}

				if (dt1.Rows[0][0].toString().toUpperCase().trim().equals("V") == true)
				{
					return true;
				}
				else
				{
					return false;
				}
			case PostgreSQL:
				sql = "select relkind from pg_class WHERE relname ='" + tabelOrViewName + "'";
				DataTable dt3 = DBAccess.RunSQLReturnTable(sql);
				if (dt3.Rows.Count == 0)
				{
					return false;
				}

				//如果是个表.
				if (dt3.Rows[0][0].toString().toLowerCase().trim().equals("r") == true)
				{
					return false;
				}
				else
				{
					return true;
				}
			case Informix:
				sql = "select tabtype from systables where tabname = '" + tabelOrViewName.toLowerCase() + "'";
				DataTable dtaa = DBAccess.RunSQLReturnTable(sql);
				if (dtaa.Rows.Count == 0)
				{
					throw new RuntimeException("@表不存在[" + tabelOrViewName + "]");
				}

				if (dtaa.Rows[0][0].toString().toUpperCase().trim().equals("V"))
				{
					return true;
				}
				else
				{
					return false;
				}
			case MySQL:
				sql = "SELECT Table_Type FROM information_schema.TABLES WHERE table_name='" + tabelOrViewName + "' and table_schema='" + SystemConfig.getAppCenterDBDatabase() + "'";
				DataTable dt2 = DBAccess.RunSQLReturnTable(sql);
				if (dt2.Rows.Count == 0)
				{
					return false;
				}

				if (dt2.Rows[0][0].toString().toUpperCase().trim().equals("VIEW"))
				{
					return true;
				}
				else
				{
					return false;
				}
			case Access:
				sql = "select   Type   from   msysobjects   WHERE   UCASE(name)='" + tabelOrViewName.toUpperCase() + "'";
				DataTable dtw = DBAccess.RunSQLReturnTable(sql);
				if (dtw.Rows.Count == 0)
				{
					return false;
				}

				if (dtw.Rows[0][0].toString().trim().equals("5"))
				{
					return true;
				}
				else
				{
					return false;
				}
			default:
				throw new RuntimeException("@没有做的判断。");
		}

		/*DataTable dt = DBAccess.RunSQLReturnTable(sql, "v", tabelOrViewName.ToUpper());
		if (dt.Rows.Count == 0)
		    throw new Exception("@表不存在[" + tabelOrViewName + "]");

		if (dt.Rows[0][0].ToString() == "VIEW")
		    return true;
		else
		    return false;
		return true;*/
	}
	/** 
	 是否存在
	 
	 @param obj
	 @return 
	*/
	public static boolean IsExitsObject(String obj)
	{
		return IsExitsObject(new DBUrl(DBUrlType.AppCenterDSN), obj);
	}
	/** 
	 判断系统中是否存在对象.
	 
	 @param table
	 @return 
	*/
	public static boolean IsExitsObject(DBUrl dburl, String obj)
	{

			//有的同事写的表名包含dbo.导致创建失败.
			obj = obj.replace("dbo.", "");

			// 增加参数.
			Paras ps = new Paras();
			ps.Add("obj", obj);

			switch (getAppCenterDBType())
			{
				case Oracle:
					if (obj.indexOf(".") != -1)
					{
						obj = obj.split("[.]", -1)[1];
					}
					return IsExits("select object_name from all_objects WHERE  object_name = upper(:obj) and OWNER='" + DBAccess.getConnectionUserID().toUpperCase() + "' ", ps);
				case MSSQL:
					return IsExits("SELECT name FROM sysobjects WHERE name = '" + obj + "'");
				case PostgreSQL:
					return IsExits("SELECT relname FROM pg_class WHERE relname = '" + obj.toLowerCase() + "'");
				case Informix:
					return IsExits("select tabname from systables where tabname = '" + obj.toLowerCase() + "'");
				case MySQL:

					/*如果不是检查的PK.*/
					if (obj.indexOf(".") != -1)
					{
						obj = obj.split("[.]", -1)[1];
					}

					// *** 屏蔽到下面的代码, 不需要从那个数据库里取，jflow 发现的bug  edit by :zhoupeng   2016.01.26 for fuzhou.
					return IsExits("SELECT table_name, table_type FROM information_schema.tables  WHERE table_name = '" + obj + "' AND TABLE_SCHEMA='" + BP.Sys.SystemConfig.getAppCenterDBDatabase() + "' ");

				case Access:
					//return false ; //IsExits("SELECT * FROM MSysObjects WHERE (((MSysObjects.Name) =  '"+obj+"' ))");
					return IsExits("SELECT * FROM MSysObjects WHERE Name =  '" + obj + "'");
				default:
					throw new RuntimeException("没有识别的数据库编号");
			}


		//if (dburl.DBUrlType == DBUrlType.DBAccessOfMSSQL1)
		//    return DBAccessOfMSSQL1.IsExitsObject(obj);

		//if (dburl.DBUrlType == DBUrlType.DBAccessOfODBC)
		//    return DBAccessOfODBC.IsExitsObject(obj);

		//if (dburl.DBUrlType == DBUrlType.DBAccessOfOLE)
		//    return DBAccessOfOLE.IsExitsObject(obj);

		//if (dburl.DBUrlType == DBUrlType.DBAccessOfOracle2)
		//    return DBAccessOfOracle2.IsExitsObject(obj);

		//throw new Exception("@没有判断的数据库类型:" + dburl);
	}
	/** 
	 表中是否存在指定的列
	 
	 @param table 表名
	 @param col 列名
	 @return 是否存在
	*/
	public static boolean IsExitsTableCol(String table, String col)
	{
		Paras ps = new Paras();
		ps.Add("tab", table);
		ps.Add("col", col);

		int i = 0;
		switch (DBAccess.getAppCenterDBType())
		{
			case MSSQL:
				i = DBAccess.RunSQLReturnValInt("SELECT COUNT(*) FROM information_schema.COLUMNS  WHERE TABLE_NAME='" + table + "' AND COLUMN_NAME='" + col + "'", 0);
				break;
			case MySQL:
				String sql = "select count(*) FROM information_schema.columns WHERE TABLE_SCHEMA='" + BP.Sys.SystemConfig.getAppCenterDBDatabase() + "' AND table_name ='" + table + "' and column_Name='" + col + "'";
				i = DBAccess.RunSQLReturnValInt(sql);
				break;
			case PostgreSQL:
				String sql1 = "select count(*) from information_schema.columns where   table_name ='" + table.toLowerCase() + "' and  column_name='" + col.toLowerCase() + "'";
				i = DBAccess.RunSQLReturnValInt(sql1);
				break;
			case Oracle:
				if (table.indexOf(".") != -1)
				{
					table = table.split("[.]", -1)[1];
				}
				i = DBAccess.RunSQLReturnValInt("SELECT COUNT(*) from user_tab_columns  WHERE table_name= upper(:tab) AND column_name= upper(:col) ", ps);
				break;
			//case DBType.Informix:
			//    i = DBAccess.RunSQLReturnValInt("select count(*) from syscolumns c where tabid in (select tabid	from systables	where tabname = lower('" + table + "')) and c.colname = lower('" + col + "')", 0);
			//    break;
			//case DBType.Access:
			//    return false;
			//    break;
			default:
				throw new RuntimeException("err@IsExitsTableCol没有判断的数据库类型.");
		}

		if (i == 1)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

	/** 
	 获得表的基础信息，返回如下列:
	 1, 字段名称，字段描述，字段类型，字段长度.
	 
	 @param tableName 表名
	*/

	public static DataTable GetTableSchema(String tableName)
	{
		return GetTableSchema(tableName, true);
	}

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public static DataTable GetTableSchema(string tableName, bool isUpper = true)
	public static DataTable GetTableSchema(String tableName, boolean isUpper)
	{
		String sql = "";
		switch (SystemConfig.getAppCenterDBType())
		{
			case MSSQL:
				sql = "SELECT column_name as FNAME, data_type as FTYPE, CHARACTER_MAXIMUM_LENGTH as FLEN , column_name as FDESC FROM information_schema.columns where table_name='" + tableName + "'";
				break;
			case Oracle:
				sql = "SELECT COLUMN_NAME as FNAME,DATA_TYPE as FTYPE,DATA_LENGTH as FLEN,COLUMN_NAME as FDESC FROM all_tab_columns WHERE table_name = upper('" + tableName + "')";
				break;
			case MySQL:
				sql = "SELECT COLUMN_NAME FNAME,DATA_TYPE FTYPE,CHARACTER_MAXIMUM_LENGTH FLEN,COLUMN_COMMENT FDESC FROM information_schema.columns WHERE table_name='" + tableName + "' and TABLE_SCHEMA='" + SystemConfig.getAppCenterDBDatabase() + "'";
				break;
			default:
				break;
		}

		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		if (isUpper == false)
		{
			dt.Columns["FNAME"].ColumnName = "FName";
			dt.Columns["FTYPE"].ColumnName = "FType";
			dt.Columns["FLEN"].ColumnName = "FLen";
			dt.Columns["FDESC"].ColumnName = "FDesc";
		}
		return dt;
	}

	public static DataTable ToLower(DataTable dt)
	{
		//把列名转成小写.
		for (int i = 0; i < dt.Columns.Count; i++)
		{
			dt.Columns[i].ColumnName = dt.Columns[i].ColumnName.toLowerCase();
		}
		return dt;
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region LoadConfig
	public static void LoadConfig(String cfgFile, String basePath)
	{
		if (!(new File(cfgFile)).isFile())
		{
			throw new RuntimeException("找不到配置文件==>[" + cfgFile + "]1");
		}

		InputStreamReader read = new InputStreamReader(cfgFile);
		String firstline = read.ReadLine();
		String cfg = read.ReadToEnd();
		read.close();

		int start = cfg.toLowerCase().indexOf("<appsettings>");
		int end = cfg.toLowerCase().indexOf("</appsettings>");

		cfg = cfg.substring(start, end + "</appsettings".length() + 1);

		cfgFile = basePath + "\\__$AppConfig.cfg";
		OutputStreamWriter write = new OutputStreamWriter(cfgFile);
		write.write(firstline + System.lineSeparator());
		write.write(cfg);
		write.flush();
		write.close();

		DataSet dscfg = new DataSet("cfg");
		try
		{
			dscfg.ReadXml(cfgFile);
		}
		catch (RuntimeException ex)
		{
			throw ex;
		}

		//  BP.Sys.SystemConfig.CS_AppSettings = new System.Collections.Specialized.NameValueCollection();
		BP.Sys.SystemConfig.CS_DBConnctionDic.clear();
		for (DataRow row : dscfg.Tables["add"].Rows)
		{
			BP.Sys.SystemConfig.getCS_AppSettings().Add(row.get("key").toString().trim(), row.get("value").toString().trim());
		}
		dscfg.Dispose();

		BP.Sys.SystemConfig.setIsBSsystem(false);
	}


//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion
}