package BP.DA;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Hashtable;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import BP.Difference.ContextHolderUtils;
import BP.Difference.SystemConfig;
import BP.En.Attr;
import BP.En.Attrs;
import BP.En.Entities;
import BP.En.Entity;
import BP.En.SQLCash;
import BP.Tools.CRC32Helper;
import BP.Tools.StringHelper;


/**
 * 数据库访问。 这个类负责处理了 实体信息
 */
public class DBAccess {
	
	
	 /// <summary>
    /// 是否大小写敏感
    /// </summary>
    public static Boolean IsCaseSensitive() throws Exception
    {
    	if (DBAccess.IsExitsObject("TEST") == true)
            DBAccess.RunSQL("DROP TABLE TEST ");
        
   	 
   		if (DBAccess.IsExitsObject("test") == true)
            DBAccess.RunSQL("DROP TABLE test ");
        
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
	
	public static void SaveBytesToDB(byte[] bytes, String line, String tableName, String tablePK, String pkVal,
			String saveToFileField) throws Exception {
		 
		// 更新文件数据到库
		ResultSet rs = null;
		Connection conn = null;
		Statement stmt = null;
		PreparedStatement pstmt = null;
		try {

			String sql = "UPDATE " + tableName + " SET " + saveToFileField + "=?" + " WHERE " + tablePK + " =?";

			if (SystemConfig.getAppCenterDBType() == DBType.MSSQL) {
				conn = BP.DA.DBAccess.getGetAppCenterDBConn_MSSQL();
				pstmt = conn.prepareStatement(sql);
			} else if (SystemConfig.getAppCenterDBType() == DBType.Oracle) {
				conn = BP.DA.DBAccess.getGetAppCenterDBConn_Oracle();
				pstmt = conn.prepareStatement(sql);
			} else if (SystemConfig.getAppCenterDBType() == DBType.MySQL) {
				conn = BP.DA.DBAccess.getGetAppCenterDBConn_MySQL();
				pstmt = conn.prepareStatement(sql);
			}

			// 数据库字段类型不一致增加判断
			if (SystemConfig.getAppCenterDBType() == DBType.MySQL || SystemConfig.getAppCenterDBType() == DBType.MSSQL) {
				pstmt.setString(1, line);
			} else {
				pstmt.setBytes(1, bytes);
			}
			pstmt.setString(2, pkVal);
			pstmt.execute();
		} catch (Exception ex) {
			if (SystemConfig.getAppCenterDBType().getValue() == DBType.MSSQL.getValue()){
				if (BP.DA.DBAccess.IsExitsTableCol(tableName, saveToFileField) == false)
				{
					/*如果没有此列，就自动创建此列.*/
					String sql = "ALTER TABLE " + tableName + " ADD  " + saveToFileField + " text ";
					BP.DA.DBAccess.RunSQL(sql);
					SaveBytesToDB(bytes, line,tableName, tablePK, pkVal, saveToFileField);
					return;
				}
			}
			if (SystemConfig.getAppCenterDBType().getValue() == DBType.Oracle.getValue()){
				if (BP.DA.DBAccess.IsExitsTableCol(tableName, saveToFileField) == false)
				{

					/*如果没有此列，就自动创建此列.*/
					//修改数据类型   oracle 不存在image类型   edited by qin 16.7.1
					String sql = "ALTER TABLE " + tableName + " ADD  " + saveToFileField + " blob ";
					BP.DA.DBAccess.RunSQL(sql);
					SaveBytesToDB(bytes,line, tableName, tablePK, pkVal, saveToFileField);
					return;

				}
			}
			if (SystemConfig.getAppCenterDBType().getValue() == DBType.MySQL.getValue()){
				if (BP.DA.DBAccess.IsExitsTableCol(tableName, saveToFileField) == false)
				{
					/*如果没有此列，就自动创建此列.*/
					String sql = "ALTER TABLE " + tableName + " ADD  " + saveToFileField + " text NULL ";
					BP.DA.DBAccess.RunSQL(sql);
					SaveBytesToDB(bytes,line, tableName, tablePK, pkVal, saveToFileField);
					return;
				}
			}
			throw new RuntimeException(ex);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		
		return; 
	}

	/**
	 * 保存文件到数据库
	 * 
	 * @param fullFileName
	 *            完成的文件路径
	 * @param tableName
	 *            表名称
	 * @param tablePK
	 *            表主键
	 * @param pkVal
	 *            主键值
	 * @param saveFileField
	 *            保存到字段
	 * @throws Exception
	 */
	public static void SaveFileToDB(String fullFileName, String tableName, String tablePK, String pkVal,
			String saveToFileField) throws Exception {

		// 读取文件内容
		FileInputStream fs = null;
		byte[] bytes = null;
		String line = "";
		try {
			if (SystemConfig.getAppCenterDBType() == DBType.MySQL) {
				line = DataType.ReadTextFile(fullFileName);
			} else {
				fs = new FileInputStream(fullFileName);
				int len = fs.available();
				bytes = new byte[len];
				fs.read(bytes);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (fs != null) {
				fs.close();
			}
		}

	}

	/**
	 * 保存文件到数据库
	 * 
	 * @param fullFileName
	 *            完成的文件路径
	 * @param tableName
	 *            表名称
	 * @param tablePK
	 *            表主键
	 * @param pkVal
	 *            主键值
	 * @param saveFileField
	 *            保存到字段
	 * @throws Exception
	 */
	public static void SaveBigTextToDB(String docs, String tableName, String tablePK, String pkVal,
			String saveToFileField) throws Exception {
		
		if (SystemConfig.getAppCenterDBType()== DBType.MySQL)
		{
			SaveBytesToDB(null, docs, tableName, tablePK, pkVal, saveToFileField);
			return;
		}
		
		SaveBytesToDB(docs.getBytes("UTF-8"), docs, tableName, tablePK, pkVal, saveToFileField);
		return;
		 

	}

	/**
	 * 从数据库里提取文件
	 * 
	 * @param fullFilePath
	 *            要存储的文件路径
	 * @param tableName
	 *            表名
	 * @param tablePK
	 *            表主键
	 * @param pkVal
	 *            主键值
	 * @param fileSaveField
	 *            字段
	 * @throws IOException
	 */
	public static byte[] GetFileFromDB(String fullFileName, String tableName, String tablePK, String pkVal,
			String fileSaveField) throws IOException {

		// 清空文件内容
		File file = new File(fullFileName);
		if (file.exists()) {
			file.delete();
		}
		file.createNewFile();
		// 清空文件内容

		String strSQL = "SELECT [" + fileSaveField + "] FROM " + tableName + " WHERE " + tablePK + "='" + pkVal + "'";

		if (SystemConfig.getAppCenterDBType() == DBType.Oracle) {
			strSQL = strSQL.replace("[", "").replace("]", "");
		}

		if (SystemConfig.getAppCenterDBType() == DBType.MySQL) {
			strSQL = strSQL.replace("[", "`").replace("]", "`");
		}

		DataTable dt = DBAccess.RunSQLReturnTable(strSQL);
		byte[] byteFile = null;
		try {
			if (dt.Rows.get(0).size() > 0) {
				if (dt.Rows.get(0).getValue(0) != null && !"".equals(dt.Rows.get(0).getValue(0))) {
					Object a = dt.Rows.get(0).getValue(0);
					if (a instanceof java.sql.Blob) {
						java.sql.Blob b = (java.sql.Blob) dt.getValue(0, 0);
						InputStream is = b.getBinaryStream();
						byteFile = new byte[(int) b.length()];
						is.read(byteFile);
						is.close();
					} else if (a instanceof java.lang.String) {
						byteFile = ((String) a).getBytes("UTF-8");
					} else {
						byteFile = (byte[]) dt.getValue(0, 0);
					}
					FileOutputStream fos = null;
					try {
						fos = new FileOutputStream(fullFileName);
						fos.write(byteFile);
					} finally {
						if (fos != null) {
							fos.close();
						}
					}
				}
			}
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		return byteFile;
	}

	/**
	 * 从数据库一个表里把img字段读取出来，转化成string返回.
	 * 
	 * @param fullFileName
	 *            要存储的文件路径名称
	 * @param tableName
	 *            表名称
	 * @param tablePK
	 *            表主键
	 * @param pkVal
	 *            主键值
	 * @param fileSaveField
	 *            表字段
	 * @return 读取出来的文本文件
	 * @throws IOException
	 */
	public static String GetTextFileFromDB(String fullFileName, String tableName, String tablePK, String pkVal,
			String fileSaveField) throws IOException {
		GetFileFromDB(fullFileName, tableName, tablePK, pkVal, fileSaveField);
		return DataType.ReadTextFile(fullFileName);
	}

	/**
	 * 从数据库一个表里把img字段读取出来，转化成string返回.
	 * 
	 * @param fullFileName
	 *            要存储的文件路径名称
	 * @param tableName
	 *            表名称
	 * @param tablePK
	 *            表主键
	 * @param pkVal
	 *            主键值
	 * @param fileSaveField
	 *            表字段
	 * @param codeType
	 *            编码格式
	 * @return 读取出来的文本文件
	 * @throws IOException
	 */
	public static String GetTextFileFromDB(String fullFileName, String tableName, String tablePK, String pkVal,
			String fileSaveField, String codeType) throws IOException {
		GetFileFromDB(fullFileName, tableName, tablePK, pkVal, fileSaveField);
		return DataType.ReadTextFile(fullFileName, codeType);
	}

	// 文件存储数据库处理.
	public static Paras DealParasBySQL(String sql, Paras ps) {
		Paras myps = new Paras();
		for (Para p : ps) {
			if (!sql.contains(":" + p.ParaName)) {
				continue;
			}
			myps.Add(p);
		}
		return myps;
	}

	public static int RunSP(String spName, String paraKey, Object paraVal) {
		Paras pas = new Paras();
		pas.Add(paraKey, paraVal);
		return DBAccess.RunSP(spName, pas);
	}

	/**
	 * 运行存储过程
	 * 
	 * @param spName
	 *            名称
	 * @return 返回影响的行数
	 */
	public static int RunSP(String spName) {
		// int i = 0;
		switch (SystemConfig.getAppCenterDBType()) {
		case MSSQL:
		case Access:
			throw new RuntimeException("@没有实现...");
		case Oracle:
			throw new RuntimeException("@没有实现...");
		case Informix:
			throw new RuntimeException("@没有实现...");
		default:
			throw new RuntimeException("Error: " + SystemConfig.getAppCenterDBType());
		}
	}

	public static int RunSPReturnInt(String spName) {
		switch (SystemConfig.getAppCenterDBType()) {
		case MSSQL:
			throw new RuntimeException("@没有实现...");
		case MySQL:
			throw new RuntimeException("@没有实现...");
		case Informix:
			throw new RuntimeException("@没有实现...");
		case Access:
		case Oracle:
			throw new RuntimeException("@没有实现...");
		default:
			throw new RuntimeException("Error: " + SystemConfig.getAppCenterDBType());
		}
	}

	/**
	 * 运行存储过程
	 * 
	 * @param spName
	 *            名称
	 * @param paras
	 *            参数
	 * @return 返回影响的行数
	 */
	public static int RunSP(String spName, Paras paras) {
		// int i = 0;
		switch (SystemConfig.getAppCenterDBType()) {
		case MSSQL:
			throw new RuntimeException("@没有实现...");
		case MySQL:
			// case Access:
			// return DBProcedure.RunSP(spName, paras, new
			// MySqlConnection(SystemConfig.AppCenterDSN));
			throw new RuntimeException("@没有实现...");
		case Oracle:
			throw new RuntimeException("@没有实现...");
		case Informix:
			throw new RuntimeException("@没有实现...");
		default:
			throw new RuntimeException("Error " + SystemConfig.getAppCenterDBType());
		}
	}

	// 运行存储过程返回 DataTable
	/**
	 * 运行存储过程
	 * 
	 * @param spName
	 *            名称
	 * @return DataTable
	 */
	public static DataTable RunSPReTable(String spName) {
		switch (SystemConfig.getAppCenterDBType()) {
		case MSSQL:
		case Access:
			throw new RuntimeException("@没有实现...");
		case Oracle:
			throw new RuntimeException("@没有实现...");
		case Informix:
			throw new RuntimeException("@没有实现...");
		default:
			throw new RuntimeException("Error " + SystemConfig.getAppCenterDBType());

		}
	}

	/**
	 * 运行存储过程
	 * 
	 * @param spName
	 *            名称
	 * @param paras
	 *            参数
	 * @return DataTable
	 */
	public static DataTable RunSPReTable(String spName, Paras paras) {
		switch (SystemConfig.getAppCenterDBType()) {
		case MSSQL:
			throw new RuntimeException("@没有实现...");
		case Oracle:
			throw new RuntimeException("@没有实现...");
		case Informix:
			throw new RuntimeException("@没有实现...");
		case Access:
		default:
			throw new RuntimeException("Error " + SystemConfig.getAppCenterDBType());
		}
	}

	public static void copyDirectory(String Src, String Dst) {
		copyFolder(new File(Src), new File(Dst));
	}

	private static void copyFolder(File src, File dest) {
		try {
			if (src.isDirectory()) {
				if (!dest.exists()) {
					dest.mkdirs();
				}
				String files[] = src.list();
				for (String file : files) {
					File srcFile = new File(src, file);
					File destFile = new File(dest, file);
					// 递归复制
					copyFolder(srcFile, destFile);
				}
			} else {
				InputStream in = new FileInputStream(src);
				OutputStream out = new FileOutputStream(dest);

				byte[] buffer = new byte[1024];

				int length;

				while ((length = in.read(buffer)) > 0) {
					out.write(buffer, 0, length);
				}
				in.close();
				out.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 运行中定义的变量
	public static Hashtable<String, Object> CurrentSys_Serial = new Hashtable<String, Object>();
	private static Hashtable<String, Object> KeyLockState = new Hashtable<String, Object>();
	private static int readCount = -1;

	/**
	 * 根据标识产生的序列号
	 * 
	 * @param type
	 *            OID
	 * @return
	 * @throws Exception
	 */
	public static int GenerSequenceNumber(String type) throws Exception {
		if (readCount == -1) // 系统第一次运行时
		{
			DataTable tb = DBAccess.RunSQLReturnTable("SELECT CfgKey, IntVal FROM Sys_Serial ");
			for (DataRow row : tb.Rows) {
				String str = row.getValue(0).toString().trim();
				int id = Integer.parseInt(str);
				try {
					CurrentSys_Serial.put(str, id);
					KeyLockState.put(str, false);
				} catch (java.lang.Exception e) {
				}
			}
			readCount++;
		}
		if (!CurrentSys_Serial.containsKey(type)) {
			DBAccess.RunSQL("insert into Sys_Serial values('" + type + "',1 )");
			return 1;
		}

		while (true) {
			while (!Boolean.parseBoolean(KeyLockState.get(type).toString())) {
				KeyLockState.put(type, true);
				int cur = Integer.parseInt(CurrentSys_Serial.get(type).toString());
				if (readCount++ % 10 == 0) {
					readCount = 1;
					int n = Integer.parseInt(CurrentSys_Serial.get(type).toString()) + 10;

					Paras ps = new Paras();
					ps.Add("intVal", n);
					ps.Add("CfgKey", type);

					String upd = "update Sys_Serial set intVal=" + SystemConfig.getAppCenterDBVarStr()
							+ "intVal WHERE CfgKey=" + SystemConfig.getAppCenterDBVarStr() + "CfgKey";
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
	 * 通过table的第1列，组成一个查询的where in 字符串.
	 * 
	 * @param dt
	 * @return
	 */
	public static String GenerWhereInPKsString(DataTable dt) {
		String pks = "";
		for (DataRow dr : dt.Rows) {
			pks += "'" + dr.getValue(0) + "',";
		}
		if ("".equals(pks))
			return "";
		return pks.substring(0, pks.length() - 1);
	}

	/**
	 * 生成 GenerOIDByGUID.
	 * 
	 * @return
	 */
	public static int GenerOIDByGUID() {
		/*
		 * warning int i = CRC32Helper.GetCRC32(Guid.NewGuid().toString());
		 */
		int i = CRC32Helper.GetCRC32(UUID.randomUUID().toString());
		if (i <= 0) {
			i = -i;
		}
		return i;
	}

	/**
	 * 生成 GenerOIDByGUID.
	 * 
	 * @return
	 */
	public static int GenerOIDByGUID(String strs) {
		int i = CRC32Helper.GetCRC32(strs);
		if (i <= 0) {
			i = -i;
		}
		return i;
	}

	/**
	 * 生成 GenerGUID
	 * 
	 * @return
	 */
	public static String GenerGUID() {
		/*
		 * warning return Guid.NewGuid().toString();
		 */
		return UUID.randomUUID().toString().replace("-", "");
	}

	/**
	 * 锁定OID
	 */
	private static boolean lock_OID = false;

	/**
	 * 产生一个OID
	 * 
	 * @return
	 * @throws Exception
	 */
	public static int GenerOID() {
		while (lock_OID) {
		}

		lock_OID = true;
		if (DBAccess.RunSQL("UPDATE Sys_Serial SET IntVal=IntVal+1 WHERE CfgKey='OID'") == 0) {
			DBAccess.RunSQL("INSERT INTO Sys_Serial (CfgKey,IntVal) VALUES ('OID',100)");
		}
		int oid = DBAccess.RunSQLReturnValInt("SELECT  IntVal FROM Sys_Serial WHERE CfgKey='OID'");
		lock_OID = false;
		return oid;
	}

	// 第二版本的生成 OID。
	/**
	 * 锁
	 */
	private static boolean lock_HT_CfgKey = false;
	private static Hashtable<String, Object> lock_HT = new Hashtable<String, Object>();

	/**
	 * 生成唯一的序列号
	 * 
	 * @param cfgKey
	 *            配置信息
	 * @return 唯一的序列号
	 * @throws Exception
	 */
	public static long GenerOID(String cfgKey) {

		Paras ps = new Paras();
		ps.Add("CfgKey", cfgKey);
		String sql = "UPDATE Sys_Serial SET IntVal=IntVal+1 WHERE CfgKey=" + SystemConfig.getAppCenterDBVarStr()
				+ "CfgKey";
		int num = DBAccess.RunSQL(sql, ps);
		if (num == 0) {
			sql = "INSERT INTO Sys_Serial (CFGKEY,INTVAL) VALUES ('" + cfgKey + "',100)";
			DBAccess.RunSQL(sql);
			lock_HT_CfgKey = false;

			lock_HT.put(cfgKey, 200);
			return 100;
		}
		sql = "SELECT IntVal FROM Sys_Serial WHERE CfgKey=" + SystemConfig.getAppCenterDBVarStr() + "CfgKey";
		num = DBAccess.RunSQLReturnValInt(sql, ps);
		return num;

	}

	// 第二版本的生成 OID。

	/**
	 * 获取一个从OID, 更新到OID. 用例: 我已经明确知道要用到260个OID, 但是为了避免多次取出造成效率浪费，就可以一次性取出
	 * 260个OID.
	 * 
	 * @param cfgKey
	 * @param getOIDNum
	 *            要获取的OID数量.
	 * @return 从OID
	 * @throws Exception
	 */
	public static long GenerOID(String cfgKey, int getOIDNum) throws Exception {
		Paras ps = new Paras();
		ps.Add("CfgKey", cfgKey);
		String sql = "UPDATE Sys_Serial SET IntVal=IntVal+" + getOIDNum + " WHERE CfgKey="
				+ SystemConfig.getAppCenterDBVarStr() + "CfgKey";
		int num = DBAccess.RunSQL(sql, ps);
		if (num == 0) {
			getOIDNum = getOIDNum + 100;
			sql = "INSERT INTO Sys_Serial (CFGKEY,INTVAL) VALUES (" + SystemConfig.getAppCenterDBVarStr() + "CfgKey,"
					+ getOIDNum + ")";
			DBAccess.RunSQL(sql, ps);
			return 100;
		}
		sql = "SELECT  IntVal FROM Sys_Serial WHERE CfgKey=" + SystemConfig.getAppCenterDBVarStr() + "CfgKey";
		return DBAccess.RunSQLReturnValInt(sql, ps) - getOIDNum;
	}

	public static int GenerOIDByKey32(String intKey) throws Exception {
		Paras ps = new Paras();
		ps.Add("CfgKey", intKey);

		String sql = "";
		sql = "UPDATE Sys_Serial SET IntVal=IntVal+1 WHERE CfgKey=" + SystemConfig.getAppCenterDBVarStr() + "CfgKey";
		int num = DBAccess.RunSQL(sql, ps);
		if (num == 0) {
			sql = "INSERT INTO Sys_Serial (CFGKEY,INTVAL) VALUES (" + SystemConfig.getAppCenterDBVarStr()
					+ "CfgKey,'100')";
			DBAccess.RunSQL(sql, ps);
			return Integer.parseInt(intKey + "100");
		}
		sql = "SELECT IntVal FROM Sys_Serial WHERE CfgKey=" + SystemConfig.getAppCenterDBVarStr() + "CfgKey";
		int val = DBAccess.RunSQLReturnValInt(sql, ps);
		return Integer.parseInt(intKey + (new Integer(val)).toString());
	}

	public static long GenerOID(String table, String intKey) throws Exception {
		Paras ps = new Paras();
		ps.Add("CfgKey", intKey);

		String sql = "";
		sql = "UPDATE " + table + " SET IntVal=IntVal+1 WHERE CfgKey=" + SystemConfig.getAppCenterDBVarStr() + "CfgKey";
		int num = DBAccess.RunSQL(sql, ps);
		if (num == 0) {
			sql = "INSERT INTO " + table + " (CFGKEY,INTVAL) VALUES (" + SystemConfig.getAppCenterDBVarStr()
					+ "CfgKey,100)";
			DBAccess.RunSQL(sql, ps);
			return Integer.parseInt(intKey + "100");
		}
		sql = "SELECT  IntVal FROM " + table + " WHERE CfgKey=" + SystemConfig.getAppCenterDBVarStr() + "CfgKey";
		int val = DBAccess.RunSQLReturnValInt(sql, ps);

		return Long.parseLong(intKey + (new Integer(val)).toString());
	}

	// 取得连接对象 ，CS、BS共用属性【关键属性】
	public static Connection getGetAppCenterDBConn_MSSQL() throws Exception {
		// Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		// Connection conn =
		// DriverManager.getConnection(SystemConfig.getAppCenterDSN(),
		// SystemConfig.getUser(), SystemConfig.getPassword());
		// // // Connection conn =
		// // //
		// //
		// DriverManager.getConnection("jdbc:sqlserver://192.168.0.213:1433;DatabaseName=VIS",
		// // // "username", "password");
		// //
		// /*
		// * warning SqlConnection conn = new
		// * SqlConnection(SystemConfig.getAppCenterDSN()); if (conn.State !=
		// * System.Data.ConnectionState.Open) { conn.ConnectionString =
		// * SystemConfig.getAppCenterDSN(); conn.Open(); } return conn;
		// */
		// return conn;
		return ContextHolderUtils.getInstance().getDataSource().getConnection();
	}

	private static Hashtable<String, Connection> _HashtableConn = null;

	public static void SetConnOfTransactionForMySQL(String buessID, Connection conn) {
		if (_HashtableConn == null)
			_HashtableConn = new Hashtable();

		_HashtableConn.put(buessID, conn);
	}

	public static Connection GetConnOfTransactionForMySQL(String buessID) throws Exception {
		if (buessID == null)
			return null;

		if (_HashtableConn == null) {
			_HashtableConn = new Hashtable();
			return null;
		}

		if (_HashtableConn.containsKey(buessID) == false)
			return null;

		Connection conn = _HashtableConn.get(buessID);
		return conn;
	}

	public static Connection getGetAppCenterDBConn_MySQL() throws Exception {

		return ContextHolderUtils.getInstance().getDataSource().getConnection();

		/*
		 * String id= WebUser.getNoOfRel(); if (id==null) return
		 * ContextHolderUtils.getInstance().getDataSource().getConnection();
		 * 
		 * Connection conn= GetConn(id); if (conn==null) return
		 * ContextHolderUtils.getInstance().getDataSource().getConnection();
		 * 
		 * return conn;
		 */

	}

	public static Connection getGetAppCenterDBConn_Oracle() throws Exception {
		// Class.forName("oracle.jdbc.driver.OracleDriver");
		// Connection conn =
		// DriverManager.getConnection(SystemConfig.getAppCenterDSN(),
		// SystemConfig.getUser(), SystemConfig.getPassword());
		// // Connection conn =
		// //
		// DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:orcl",
		// // "resmg", "resmg");
		// return conn;
		// /*
		// * warning OracleConnection conn = new OracleConnection(
		// * SystemConfig.getAppCenterDSN()); if (conn.State !=
		// * System.Data.ConnectionState.Open) { conn.ConnectionString =
		// * SystemConfig.getAppCenterDSN(); conn.Open(); } return conn;
		// */
		return ContextHolderUtils.getInstance().getDataSource().getConnection();
	}

	// 取得连接对象 ，CS、BS共用属性

	/**
	 * AppCenterDBType
	 */
	public static DBType getAppCenterDBType() {
		return SystemConfig.getAppCenterDBType();
	}

	// 运行 SQL

	// 通过主应用程序在其他库上运行sql

	// pk
	/**
	 * 建立主键
	 * 
	 * @param tab
	 *            物理表
	 * @param pk
	 *            主键
	 * @throws Exception
	 */
	public static void CreatePK(String tab, String pk, DBType db) {
		String sql;

		if (tab == null || "".equals(tab))
			return;

		if (DBAccess.IsExitsTabPK(tab)) {
			return;
		}
		// //先创建表的主键列.
		// sql = "ALTER TABLE " + tab.toUpperCase() + " ADD COLUMN " + pk + "
		// int(11) ";
		// RunSQL(sql);

		// 然后添加主键约束.
		sql = "ALTER TABLE " + tab.toUpperCase() + " ADD CONSTRAINT " + tab + "pk PRIMARY KEY(" + pk.toUpperCase()
				+ ")";
		RunSQL(sql);

	}

	public static void CreatePK(String tab, String pk1, String pk2, DBType db) {

		if (DBAccess.IsExitsTabPK(tab)) {
			return;
		}

		String sql;
		sql = "ALTER TABLE " + tab.toUpperCase() + " ADD CONSTRAINT " + tab + "pk  PRIMARY KEY(" + pk1.toUpperCase()
				+ "," + pk2.toUpperCase() + ")";

		DBAccess.RunSQL(sql);
	}

	public static void CreatePK(String tab, String pk1, String pk2, String pk3, DBType db) {

		if (DBAccess.IsExitsTabPK(tab)) {
			return;
		}

		String sql;

		sql = "ALTER TABLE " + tab.toUpperCase() + " ADD CONSTRAINT " + tab + "pk PRIMARY KEY(" + pk1.toUpperCase()
				+ "," + pk2.toUpperCase() + "," + pk3.toUpperCase() + ")";
		DBAccess.RunSQL(sql);
	}

	// index
	public static void CreatIndex(String table, String pk) {
		return;

	}

	public static void CreatIndex(String table, String pk1, String pk2) {
		// try {
		// DBAccess.RunSQL("CREATE INDEX " + table + "ID ON " + table + " (" +
		// pk1 + "," + pk2 + ")");
		// } catch (java.lang.Exception e) {}
	}

	public static void CreatIndex(String table, String pk1, String pk2, String pk3) {
		// DBAccess.RunSQL("CREATE INDEX " + table + "ID ON " + table + " (" +
		// pk1 + "," + pk2 + "," + pk3 + ")");
	}

	public static void CreatIndex(String table, String pk1, String pk2, String pk3, String pk4) {
		// DBAccess.RunSQL("CREATE INDEX " + table + "ID ON " + table + " (" +
		// pk1 + "," + pk2 + "," + pk3 + "," + pk4 + ")");
	}

	// public static int RunSQL(String sql, Object obj, String dsn, Object...
	// pars) {
	// return 0;
	// object oconn = GetAppCenterDBConn;
	// if (oconn is SqlConnection)
	// return RunSQL(sql, (SqlConnection)oconn, sqlType, dsn);
	// else if (oconn is OracleConnection)
	// return RunSQL(sql, (OracleConnection)oconn, sqlType, dsn);
	// else
	// throw new Exception("获取数据库连接[GetAppCenterDBConn]失败！");
	// }

	public static DataTable ReadProText(String proName) throws Exception {
		String sql = "";
		switch (SystemConfig.getAppCenterDBType()) {
		case Oracle:
			sql = "SELECT text FROM user_source WHERE name=UPPER('" + proName + "') ORDER BY LINE ";
			break;
		default:
			sql = "SP_Help  " + proName;
			break;
		}

		try {
			return BP.DA.DBAccess.RunSQLReturnTable(sql);
		} catch (java.lang.Exception e) {
			sql = "select * from Port_Emp WHERE 1=2";
			return BP.DA.DBAccess.RunSQLReturnTable(sql);
		}
	}

	public static void RunSQLScript(String sqlOfScriptFilePath) {

		String str = DataType.ReadTextFile(sqlOfScriptFilePath);
		String[] strs = str.split("[;]", -1);
		for (String s : strs) {
			/*
			 * warning if (StringHelper.isNullOrEmpty(s) ||
			 * StringHelper.isNullOrWhiteSpace(s)) { continue; }
			 */
			if (StringHelper.isNullOrEmpty(s)) {
				continue;
			}

			if (s.contains("--")) {
				continue;
			}

			if (s.contains("/*")) {
				continue;
			}

			DBAccess.RunSQL(s);
		}
	}

	/**
	 * 执行具有Go的sql 文本。
	 * 
	 * @param sqlOfScriptFilePath
	 * @throws IOException
	 * @throws Exception
	 */
	public static void RunSQLScriptGo(String sqlOfScriptFilePath) throws IOException, Exception {
		String str = BP.DA.DataType.ReadTextFile(sqlOfScriptFilePath);
		/*
		 * warning String[] strs = str.split(new String[] { "--GO--" },
		 * StringSplitOptions.RemoveEmptyEntries);
		 */
		String[] strs = str.split("--GO--");
		for (String s : strs) {
			/*
			 * warning if (StringHelper.isNullOrEmpty(s) ||
			 * StringHelper.isNullOrWhiteSpace(s)) { continue; }
			 */
			if (StringHelper.isNullOrEmpty(s)) {
				continue;
			}

			// if (s.Contains("--"))
			// continue;

			if (s.contains("/**")) {
				continue;
			}

			String mysql = s.replace("--GO--", "");
			if (StringHelper.isNullOrEmpty(mysql.trim())) {
				continue;
			}

			BP.DA.DBAccess.RunSQL(mysql);
		}
	}

	public static void RunSQLs(String sql) {
		if (StringHelper.isNullOrEmpty(sql)) {
			return;
		}

		sql = sql.replace("@GO", "~");
		sql = sql.replace("@", "~");
		String[] strs = sql.split("[~]", -1);
		for (String str : strs) {
			if (StringHelper.isNullOrEmpty(str)) {
				continue;
			}

			if (str.contains("--") || str.contains("/*")) {
				continue;
			}

			RunSQL(str);
		}
	}

	/**
	 * 运行带有参数的sql
	 * 
	 * @param ps
	 * @return
	 * @throws Exception
	 */
	public static int RunSQL(Paras ps) {
		return RunSQL(ps.SQL, ps);
	}

	/**
	 * 运行sql
	 * 
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public static int RunSQL(String sql) {

		if (DataType.IsNullOrEmpty(sql) == true) {
			return 1;
		}

		Paras ps = new Paras();
		ps.SQL = sql;
		return RunSQL(ps);
	}

	public static int RunSQL(String sql, String paraKey, Object val) {
		Paras ens = new Paras();
		ens.Add(paraKey, val);
		return RunSQL(sql, ens);
	}

	public static int RunSQL(String sql, String paraKey1, Object val1, String paraKey2, Object val2) {
		Paras ens = new Paras();
		ens.Add(paraKey1, val1);
		ens.Add(paraKey2, val2);
		return RunSQL(sql, ens);
	}

	public static int RunSQL(String sql, String paraKey1, Object val1, String paraKey2, Object val2, String k3,
			Object v3) {
		Paras ens = new Paras();
		ens.Add(paraKey1, val1);
		ens.Add(paraKey2, val2);
		ens.Add(k3, v3);
		return RunSQL(sql, ens);
	}

	// 执行SQL锁，暂时没有用
	private static boolean lockRunSQL = false;

	/**
	 * 执行sql
	 * 
	 * @param sql
	 * @param paras
	 * @return
	 * @throws Exception
	 */
	public static int RunSQL(String sql, Paras paras) {
		if (sql == null || sql.trim().equals("")) {
			return 1;
		}

		lockRunSQL = true;

		int result = 0;
		try {
			switch (getAppCenterDBType()) {
			case MSSQL:
				result = RunSQL_200705_SQL(sql, paras);
				break;
			case Oracle:
				result = RunSQL_200705_Ora(sql.replace("]", "").replace("[", ""), paras);
				break;
			case MySQL:
				result = RunSQL_200705_MySQL(sql, paras);
				break;
			default:
				throw new RuntimeException("发现未知的数据库连接类型！");
			}
			return result;
		} catch (RuntimeException ex) {
			throw ex;
		} finally {
			lockRunSQL = false;
		}
	}

	/**
	 * 运行sql返回结果
	 * 
	 * @param sql
	 *            sql
	 * @param paras
	 *            参数
	 * @return 执行的结果
	 * @throws Exception
	 */
	private static int RunSQL_200705_SQL(String sql, Paras paras) {
		ResultSet rs = null;
		Connection conn = null;
		Statement stmt = null;
		NamedParameterStatement pstmt = null;
		try {
			conn = DBAccess.getGetAppCenterDBConn_MSSQL();
			int i = 0;
			if (null != paras && paras.size() > 0) {
				pstmt = new NamedParameterStatement(conn, sql);
				PrepareCommand(pstmt, paras);
				i = pstmt.executeUpdate();
			} else {
				stmt = conn.createStatement();// 创建用于执行静态sql语句的Statement对象，st属局部变量
				i = stmt.executeUpdate(sql);
			}

			if (Log.isLoggerDebugEnabled()) {
				Log.DefaultLogWriteLineDebug("SQL: " + sql);
				Log.DefaultLogWriteLineDebug("Param: " + paras.getDebugInfo() + ", Result: Rows=" + i);
			}

			return i;
		} catch (Exception ex) {

			String msg = "@运行更新在(RunSQL_200705_SQL)出错。\n  @SQL: " + sql + "\n  @Param: " + paras.getDebugInfo()
					+ "\n  @异常信息: " + StringUtils.replace(ex.getMessage(), "\n", " ");
			Log.DefaultLogWriteLineError(msg);

			throw new RuntimeException(msg, ex);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
	}

	private static int RunSQL_200705_Ora(String sql, Paras paras) {
		ResultSet rs = null;
		Connection conn = null;
		Statement stmt = null;
		NamedParameterStatement pstmt = null;
		try {
			conn = DBAccess.getGetAppCenterDBConn_Oracle();
			int i = 0;
			if (null != paras && paras.size() > 0) {
				pstmt = new NamedParameterStatement(conn, sql);
				PrepareCommand(pstmt, paras);
				i = pstmt.executeUpdate();
			} else {
				stmt = conn.createStatement();// 创建用于执行静态sql语句的Statement对象，st属局部变量
				i = stmt.executeUpdate(sql);
			}
			if (Log.isLoggerDebugEnabled()) {
				Log.DefaultLogWriteLineDebug("SQL: " + sql);
				Log.DefaultLogWriteLineDebug("Param: " + paras.getDebugInfo() + ", Result: Rows=" + i);
			}
			return i;
		} catch (Exception ex) {
			String msg = "@运行更新在(RunSQL_200705_Ora)出错。\n  @SQL: " + sql + "\n  @Param: " + paras.getDebugInfo()
					+ "\n  @异常信息: " + StringUtils.replace(ex.getMessage(), "\n", " ");
			Log.DefaultLogWriteLineError(msg);
			throw new RuntimeException(msg, ex);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * RunSQL_200705_MySQL
	 * 
	 * @param sql
	 * @param paras
	 * @return
	 * @throws Exception
	 */
	private static int RunSQL_200705_MySQL(String sql, Paras paras) {

		Connection conn = null;
		Statement stmt = null;
		NamedParameterStatement pstmt = null;
		boolean isTtrack = false;
		try {
			// 首先取具有事务的conn. @xushuhao
			conn = GetConnOfTransactionForMySQL(BP.Web.WebUser.getNo());

			if (conn == null)
				conn = DBAccess.getGetAppCenterDBConn_MySQL();
			else
				isTtrack = true;

			int i = 0;
			if (null != paras && paras.size() > 0) {
				pstmt = new NamedParameterStatement(conn, sql);
				PrepareCommand(pstmt, paras);
				i = pstmt.executeUpdate();

			} else {
				stmt = conn.createStatement();// 创建用于执行静态sql语句的Statement对象，st属局部变量
				i = stmt.executeUpdate(sql);
			}

			if (Log.isLoggerDebugEnabled()) {
				Log.DefaultLogWriteLineDebug("SQL: " + sql);
				Log.DefaultLogWriteLineDebug("Param: " + paras.getDebugInfo() + ", Result: Rows=" + i);
			}
			// conn.commit();
			return i;
		} catch (Exception ex) {
			String msg = "@运行更新在(RunSQL_200705_MySQL)出错。\n  @SQL: " + sql + "\n  @Param: " + paras.getDebugInfo()
					+ "\n  @异常信息: " + StringUtils.replace(ex.getMessage(), "\n", " ");
			Log.DefaultLogWriteLineError(msg);
			throw new RuntimeException(msg, ex);
		} finally {
			try {

				if (stmt != null)
					stmt.close();

				if (pstmt != null)
					pstmt.close();

				if (isTtrack == false && conn != null)
					conn.close();

			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
	}

	public static int RunSQLReturnResultSet(String sql, Paras paras, Entity en, Attrs attrs) {

		if (StringHelper.isNullOrEmpty(sql)) {
			throw new RuntimeException("要执行的 sql =null ");
		}

		try {
			switch (getAppCenterDBType()) {
			case MSSQL:
				return RunSQLReturnResultSet_201809_SQL(sql, paras, en, attrs);
			case Oracle:
				return RunSQLReturnResultSet_201809_Ora(sql, paras, en, attrs);
			case MySQL:
				return RunSQLReturnResultSet_201809_MySQL(sql, paras, en, attrs);
			default:
				throw new RuntimeException("@发现未知的数据库连接类型！");
			}
		} catch (RuntimeException ex) {
			throw ex;
		}
	}

	public static int RunSQLReturnResultSet(String sql, Paras paras, Entities ens, Attrs attrs) {

		if (StringHelper.isNullOrEmpty(sql)) {
			throw new RuntimeException("要执行的 sql =null ");
		}

		try {
			ResultSet rs = null;
			switch (getAppCenterDBType()) {
			case MSSQL:
				return RunSQLReturnResultSet_201809_SQL(sql, paras, ens, attrs);
			case Oracle:
				return RunSQLReturnResultSet_201809_Ora(sql, paras, ens, attrs);
			case MySQL:
				return RunSQLReturnResultSet_201809_MySQL(sql, paras, ens, attrs);
			default:
				throw new RuntimeException("@发现未知的数据库连接类型！");
			}
		} catch (RuntimeException ex) {
			throw ex;
		}

	}

	public static DataTable RunSQLReturnTable(String sql, Paras paras) {
		if (StringHelper.isNullOrEmpty(sql)) {
			throw new RuntimeException("要执行的 sql =null ");
		}
		try {
			DataTable dt = null;
			switch (getAppCenterDBType()) {
			case MSSQL:
				dt = RunSQLReturnTable_200705_SQL(sql, paras);
				break;
			case Oracle:
				dt = RunSQLReturnTable_200705_Ora(sql, paras);
				break;
			case MySQL:
				dt = RunSQLReturnTable_200705_MySQL(sql, paras);
				break;
			default:
				throw new RuntimeException("@发现未知的数据库连接类型！");
			}
			return dt;
		} catch (RuntimeException ex) {
			throw ex;
		}
	}

	public static DataTable RunSQLReturnTable_200705_SQL(String sql, Paras paras) {
		ResultSet rs = null;
		Connection conn = null;
		Statement stmt = null;
		NamedParameterStatement pstmt = null;
		if (sql.trim().endsWith("WHERE")) {
			sql = sql.replace("WHERE", "");
		}
		try {
			conn = DBAccess.getGetAppCenterDBConn_MSSQL();
			DataTable oratb = new DataTable("otb");
			if (null != paras && paras.size() > 0) {
				pstmt = new NamedParameterStatement(conn, sql);
				PrepareCommand(pstmt, paras);
				// pstmt.setString(1, "李思");
				rs = pstmt.executeQuery();
				ResultSetMetaData rsmd = rs.getMetaData();
				int size = rsmd.getColumnCount();
				for (int i = 0; i < size; i++) {
					oratb.Columns.Add(rsmd.getColumnName(i + 1), Para.getDAType(rsmd.getColumnType(i + 1)));
				}
				while (rs.next()) {
					DataRow dr = oratb.NewRow();// 產生一列DataRow
					for (int i = 0; i < size; i++) {
						Object val = rs.getObject(i + 1);
						if (dr != null && dr.columns.size() > 0 && dr.columns.get(i).DataType != null) {
							if (dr.columns.get(i).DataType.toString().contains("Integer")
									|| dr.columns.get(i).DataType.toString().contains("Double")) {
								if (val == null) {
									dr.setValue(i, 0);
								} else {
									dr.setValue(i, val);
								}

							} else {
								dr.setValue(i, val);
							}
						} else {
							dr.setValue(i, val);
						}
						// dr.setDataType(i, Para.getDAType(val));
					}
					oratb.Rows.add(dr);// DataTable加入此DataRow
				}
			} else {
				stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
				rs = stmt.executeQuery(sql);
				ResultSetMetaData rsmd = rs.getMetaData();
				int size = rsmd.getColumnCount();
				for (int i = 0; i < size; i++) {
					oratb.Columns.Add(rsmd.getColumnName(i + 1), Para.getDAType(rsmd.getColumnType(i + 1)));
				}
				while (rs.next()) {
					DataRow dr = oratb.NewRow();// 產生一列DataRow
					for (int i = 0; i < size; i++) {
						Object val = rs.getObject(i + 1);
						if (dr != null && dr.columns.size() > 0 && dr.columns.get(i).DataType != null) {
							if (dr.columns.get(i).DataType.toString().contains("Integer")
									|| dr.columns.get(i).DataType.toString().contains("Double")) {
								if (val == null) {
									dr.setValue(i, 0);
								} else {
									dr.setValue(i, val);
								}

							} else {
								dr.setValue(i, val);
							}
						} else {
							dr.setValue(i, val);
						}
					}
					oratb.Rows.add(dr);// DataTable加入此DataRow
				}
			}
			if (Log.isLoggerDebugEnabled()) {
				Log.DefaultLogWriteLineDebug("SQL: " + sql);
				Log.DefaultLogWriteLineDebug("Param: " + paras.getDebugInfo() + ", Result: Rows=" + oratb.Rows.size());
			}
			return oratb;
		} catch (Exception ex) {
			String msg = "@运行查询在(RunSQLReturnTable_200705_SQL)出错。\n  @SQL: " + sql + "\n  @Param: "
					+ paras.getDebugInfo() + "\n  @异常信息: " + StringUtils.replace(ex.getMessage(), "\n", " ");
			Log.DefaultLogWriteLineError(msg);
			throw new RuntimeException(msg, ex);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
	}

	public static int RunSQLReturnResultSet_201809_SQL(String sql, Paras paras, Entity en, Attrs attrs) {
		ResultSet rs = null;
		Connection conn = null;
		Statement stmt = null;
		NamedParameterStatement pstmt = null;
		if (sql.trim().endsWith("WHERE")) {
			sql = sql.replace("WHERE", "");
		}
		try {
			conn = DBAccess.getGetAppCenterDBConn_MSSQL();

			if (null != paras && paras.size() > 0) {
				pstmt = new NamedParameterStatement(conn, sql);
				PrepareCommand(pstmt, paras);
				rs = pstmt.executeQuery();
			} else {
				stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
				rs = stmt.executeQuery(sql);
			}
			if (rs.next() == false)
				return 0;

			Hashtable ht = en.getRow();

			for (int idx = 0; idx < attrs.size(); idx++) {
				Attr attr = attrs.get(idx);

				Object val = rs.getObject(idx + 1);

				if (val == null) {
					if (attr.getIsNum() == true)
						val = 0;
					else
						val = "";
				}

				ht.put(attr.getKey(), val);

			}
			if (Log.isLoggerDebugEnabled()) {
				Log.DefaultLogWriteLineDebug("SQL: " + sql);
				Log.DefaultLogWriteLineDebug("Param: " + paras.getDebugInfo() + ", Result: Rows=" + rs.getRow());
			}
			return 1;
		} catch (Exception ex) {
			String msg = "@运行查询在(RunSQLReturnTable_200705_SQL)出错。\n  @SQL: " + sql + "\n  @Param: "
					+ paras.getDebugInfo() + "\n  @异常信息: " + StringUtils.replace(ex.getMessage(), "\n", " ");
			Log.DefaultLogWriteLineError(msg);
			throw new RuntimeException(msg, ex);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
	}

	private static int RunSQLReturnResultSet_201809_SQL(String sql, Paras paras, Entities ens, Attrs attrs) {
		ResultSet rs = null;
		Connection conn = null;
		Statement stmt = null;
		NamedParameterStatement pstmt = null;
		if (sql.trim().endsWith("WHERE")) {
			sql = sql.replace("WHERE", "");
		}
		try {
			conn = DBAccess.getGetAppCenterDBConn_MSSQL();

			if (null != paras && paras.size() > 0) {
				pstmt = new NamedParameterStatement(conn, sql);
				PrepareCommand(pstmt, paras);
				rs = pstmt.executeQuery();
			} else {
				stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
				rs = stmt.executeQuery(sql);
			}
			Attr attr = null;
			Entity myen = ens.getNewEntity();
			SQLCash sqlCash = myen.getSQLCash();
			BP.En.Map map = myen.getEnMap();

			while (rs.next()) {

				Entity en = ens.getNewEntity();
				en.setSQLCash(sqlCash);
				en.setMap(map);
				// en.setRow( sqlCash.CreateNewRow() );

				Hashtable ht = en.getRow();
				for (int idx = 0; idx < attrs.size(); idx++) {
					attr = attrs.get(idx);
					Object val = rs.getObject(idx + 1);
					if (val == null) {
						if (attr.getIsNum() == true)
							val = 0;
						else
							val = "";
					}
					ht.put(attr.getKey(), val);
				}
				ens.add(en); // 加入里面去.
			}

			if (Log.isLoggerDebugEnabled()) {
				Log.DefaultLogWriteLineDebug("SQL: " + sql);
				Log.DefaultLogWriteLineDebug("Param: " + paras.getDebugInfo() + ", Result: Rows=" + ens.size());
			}
			return ens.size();

		} catch (Exception ex) {
			String msg = "@运行查询在(RunSQLReturnTable_200705_Ora)出错。\n  @SQL: " + sql + "\n  @Param: "
					+ paras.getDebugInfo() + "\n  @异常信息: " + StringUtils.replace(ex.getMessage(), "\n", " ");
			Log.DefaultLogWriteLineError(msg);
			throw new RuntimeException(msg, ex);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
	}

	private static DataTable RunSQLReturnTable_200705_Ora(String sql, Paras paras) {
		ResultSet rs = null;
		Connection conn = null;
		Statement stmt = null;
		NamedParameterStatement pstmt = null;
		if (sql.trim().endsWith("WHERE")) {
			sql = sql.replace("WHERE", "");
		}
		try {
			conn = DBAccess.getGetAppCenterDBConn_Oracle();

			DataTable oratb = new DataTable("otb");
			if (null != paras && paras.size() > 0) {
				pstmt = new NamedParameterStatement(conn, sql);
				PrepareCommand(pstmt, paras);
				rs = pstmt.executeQuery();
				ResultSetMetaData rsmd = rs.getMetaData();
				int size = rsmd.getColumnCount();
				for (int i = 0; i < size; i++) {
					oratb.Columns.Add(rsmd.getColumnName(i + 1), Para.getDAType(rsmd.getColumnType(i + 1)));
				}
				while (rs.next()) {
					DataRow dr = oratb.NewRow();// 產生一列DataRow
					for (int i = 0; i < size; i++) {
						Object val = rs.getObject(i + 1);
						if (dr.columns.get(i).DataType.toString().contains("BigDecimal")) {
							if (val == null) {
								dr.setValue(i, 0);
							} else {
								dr.setValue(i, val);
							}

						} else {
							dr.setValue(i, val);
						}
					}
					oratb.Rows.add(dr);// DataTable加入此DataRow
				}
			} else {
				stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
				rs = stmt.executeQuery(sql);
				ResultSetMetaData rsmd = rs.getMetaData();
				int size = rsmd.getColumnCount();
				for (int i = 0; i < size; i++) {
					oratb.Columns.Add(rsmd.getColumnName(i + 1), Para.getDAType(rsmd.getColumnType(i + 1)));
				}
				while (rs.next()) {
					DataRow dr = oratb.NewRow();// 產生一列DataRow

					for (int i = 0; i < size; i++) {

						Object val = rs.getObject(i + 1);
						if (dr.columns.get(i).DataType.toString().contains("BigDecimal")) {
							if (val == null) {
								dr.setValue(i, 0);
							} else {
								dr.setValue(i, val);
							}

						} else {
							dr.setValue(i, val);
						}
					}
					oratb.Rows.add(dr);// DataTable加入此DataRow
				}
			}
			if (Log.isLoggerDebugEnabled()) {
				Log.DefaultLogWriteLineDebug("SQL: " + sql);
				Log.DefaultLogWriteLineDebug("Param: " + paras.getDebugInfo() + ", Result: Rows=" + oratb.Rows.size());
			}
			return oratb;
		} catch (Exception ex) {
			String msg = "@运行查询在(RunSQLReturnTable_200705_Ora)出错。\n  @SQL: " + sql + "\n  @Param: "
					+ paras.getDebugInfo() + "\n  @异常信息: " + StringUtils.replace(ex.getMessage(), "\n", " ");
			Log.DefaultLogWriteLineError(msg);
			throw new RuntimeException(msg, ex);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
	}

	private static int RunSQLReturnResultSet_201809_Ora(String sql, Paras paras, Entity en, Attrs attrs) {
		ResultSet rs = null;
		Connection conn = null;
		Statement stmt = null;
		NamedParameterStatement pstmt = null;
		if (sql.trim().endsWith("WHERE")) {
			sql = sql.replace("WHERE", "");
		}
		try {
			conn = DBAccess.getGetAppCenterDBConn_Oracle();
			if (null != paras && paras.size() > 0) {
				pstmt = new NamedParameterStatement(conn, sql);
				PrepareCommand(pstmt, paras);
				rs = pstmt.executeQuery();
			} else {
				stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
				rs = stmt.executeQuery(sql);
			}
			if (rs.next() == false)
				return 0;

			Hashtable ht = en.getRow();

			for (int idx = 0; idx < attrs.size(); idx++) {
				Attr attr = attrs.get(idx);

				Object val = rs.getObject(idx + 1);

				if (val == null) {
					if (attr.getIsNum() == true)
						val = 0;
					else
						val = "";
				}

				ht.put(attr.getKey(), val);

			}
			if (Log.isLoggerDebugEnabled()) {
				Log.DefaultLogWriteLineDebug("SQL: " + sql);
				Log.DefaultLogWriteLineDebug("Param: " + paras.getDebugInfo() + ", Result: Rows=" + rs.getRow());
			}
			return 1;
		} catch (Exception ex) {
			String msg = "@运行查询在(RunSQLReturnTable_200705_Ora)出错。\n  @SQL: " + sql + "\n  @Param: "
					+ paras.getDebugInfo() + "\n  @异常信息: " + StringUtils.replace(ex.getMessage(), "\n", " ");
			Log.DefaultLogWriteLineError(msg);
			throw new RuntimeException(msg, ex);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
	}

	private static int RunSQLReturnResultSet_201809_Ora(String sql, Paras paras, Entities ens, Attrs attrs) {
		ResultSet rs = null;
		Connection conn = null;
		Statement stmt = null;
		NamedParameterStatement pstmt = null;
		if (sql.trim().endsWith("WHERE")) {
			sql = sql.replace("WHERE", "");
		}
		try {
			conn = DBAccess.getGetAppCenterDBConn_Oracle();
			if (null != paras && paras.size() > 0) {
				pstmt = new NamedParameterStatement(conn, sql);
				PrepareCommand(pstmt, paras);
				rs = pstmt.executeQuery();
			} else {
				stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
				rs = stmt.executeQuery(sql);
			}
			Attr attr = null;
			Entity myen = ens.getNewEntity();
			SQLCash sqlCash = myen.getSQLCash();
			BP.En.Map map = myen.getEnMap();

			while (rs.next()) {

				Entity en = ens.getNewEntity();
				en.setSQLCash(sqlCash);
				en.setMap(map);
				// en.setRow( sqlCash.CreateNewRow() );

				Hashtable ht = en.getRow();
				for (int idx = 0; idx < attrs.size(); idx++) {
					attr = attrs.get(idx);
					Object val = rs.getObject(idx + 1);
					if (val == null) {
						if (attr.getIsNum() == true)
							val = 0;
						else
							val = "";
					}
					ht.put(attr.getKey(), val);
				}
				ens.add(en); // 加入里面去.
			}

			if (Log.isLoggerDebugEnabled()) {
				Log.DefaultLogWriteLineDebug("SQL: " + sql);
				Log.DefaultLogWriteLineDebug("Param: " + paras.getDebugInfo() + ", Result: Rows=" + ens.size());
			}
			return ens.size();

		} catch (Exception ex) {
			String msg = "@运行查询在(RunSQLReturnTable_200705_Ora)出错。\n  @SQL: " + sql + "\n  @Param: "
					+ paras.getDebugInfo() + "\n  @异常信息: " + StringUtils.replace(ex.getMessage(), "\n", " ");
			Log.DefaultLogWriteLineError(msg);
			throw new RuntimeException(msg, ex);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * RunSQLReturnTable_200705_MySQL
	 * 
	 * @param sql
	 *            要执行的sql
	 * @return 返回table
	 * @throws Exception
	 */
	private static DataTable RunSQLReturnTable_200705_MySQL(String sql, Paras paras) {
		ResultSet rs = null;
		Connection conn = null;
		Statement stmt = null;
		NamedParameterStatement pstmt = null;

		try {
			conn = DBAccess.getGetAppCenterDBConn_MySQL();
			DataTable oratb = new DataTable("otb");
			String nameLabel = "";
			if (null != paras && paras.size() > 0) {
				pstmt = new NamedParameterStatement(conn, sql);
				PrepareCommand(pstmt, paras);
				rs = pstmt.executeQuery();
				ResultSetMetaData rsmd = rs.getMetaData();
				int size = rsmd.getColumnCount();
				for (int i = 0; i < size; i++) {
					// 原来的 oratb.Columns.Add(rsmd.getColumnName(i + 1),
					// Para.getDAType(rsmd.getColumnType(i + 1)));
					// mysql取值导致流程名称重复，改为label取值
					nameLabel = rsmd.getColumnLabel(i + 1);
					oratb.Columns.Add(nameLabel, Para.getDAType(rsmd.getColumnType(i + 1)));
				}
				while (rs.next()) {
					DataRow dr = oratb.NewRow();// 產生一列DataRow
					for (int i = 0; i < size; i++) {
						Object val = rs.getObject(i + 1);
						// Object val = rs.getObject(i + 1);
						if (dr != null && dr.columns.size() > 0 && dr.columns.get(i).DataType != null) {
							if (dr.columns.get(i).DataType.toString().contains("Integer")
									|| dr.columns.get(i).DataType.toString().contains("Float")) {
								if (val == null) {
									dr.setValue(i, 0);
								} else {
									dr.setValue(i, val);
								}

							} else {
								dr.setValue(i, val);
							}
						} else {
							dr.setValue(i, val);
						}
					}
					oratb.Rows.add(dr);// DataTable加入此DataRow
				}
			} else {
				
				if("BP".equals(SystemConfig.getRunOnPlant())) {
					stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
				}else { // jeesite平台时
					stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
				}
				
				rs = stmt.executeQuery(sql);
				ResultSetMetaData rsmd = rs.getMetaData();
				int size = rsmd.getColumnCount();
				for (int i = 0; i < size; i++) {
					// oratb.Columns.Add(rsmd.getColumnName(i + 1),
					// Para.getDAType(rsmd.getColumnType(i + 1)));
					nameLabel = rsmd.getColumnLabel(i + 1);
					oratb.Columns.Add(nameLabel, Para.getDAType(rsmd.getColumnType(i + 1)));
				}
				while (rs.next()) {
					DataRow dr = oratb.NewRow();// 產生一列DataRow
					for (int i = 0; i < size; i++) {

						Object val = rs.getObject(i + 1);
						if (dr != null && dr.columns.size() > 0 && dr.columns.get(i).DataType != null) {
							if (dr.columns.get(i).DataType.toString().contains("Integer")
									|| dr.columns.get(i).DataType.toString().contains("Float")) {
								if (val == null) {
									dr.setValue(i, 0);
								} else {
									dr.setValue(i, val);
								}

							} else {
								dr.setValue(i, val);
							}
						} else {
							dr.setValue(i, val);
						}
					}
					oratb.Rows.add(dr);// DataTable加入此DataRow
				}
			}
			if (Log.isLoggerDebugEnabled()) {
				Log.DefaultLogWriteLineDebug("SQL: " + sql);
				Log.DefaultLogWriteLineDebug("Param: " + paras.getDebugInfo() + ", Result: Rows=" + oratb.Rows.size());
			}
			return oratb;
		} catch (Exception ex) {
			String msg = "@运行查询在(RunSQLReturnTable_200705_MySQL)出错。\n  @SQL: " + sql + "\n  @Param: "
					+ paras.getDebugInfo() + "\n  @异常信息: " + StringUtils.replace(ex.getMessage(), "\n", " ");
			Log.DefaultLogWriteLineError(msg);
			throw new RuntimeException(msg, ex);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
	}

	private static int RunSQLReturnResultSet_201809_MySQL(String sql, Paras paras, Entities ens, Attrs attrs) {
		ResultSet rs = null;
		Connection conn = null;
		Statement stmt = null;
		NamedParameterStatement pstmt = null;

		try {
			conn = DBAccess.getGetAppCenterDBConn_MySQL();
			if (null != paras && paras.size() > 0) {
				pstmt = new NamedParameterStatement(conn, sql);
				PrepareCommand(pstmt, paras);
				rs = pstmt.executeQuery();
			} else {
				stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
				rs = stmt.executeQuery(sql);
			}

			Attr attr = null;
			Entity myen = ens.getNewEntity();
			SQLCash sqlCash = myen.getSQLCash();
			BP.En.Map map = myen.getEnMap();

			while (rs.next()) {

				Entity en = ens.getNewEntity();
				en.setSQLCash(sqlCash);
				en.setMap(map);
				// en.setRow( sqlCash.CreateNewRow() );

				Hashtable ht = en.getRow();
				for (int idx = 0; idx < attrs.size(); idx++) {
					attr = attrs.get(idx);
					Object val = rs.getObject(idx + 1);
					if (val == null) {
						if (attr.getIsNum() == true)
							val = 0;
						else
							val = "";
					}
					ht.put(attr.getKey(), val);
				}
				ens.add(en); // 加入里面去.
			}

			if (Log.isLoggerDebugEnabled()) {
				Log.DefaultLogWriteLineDebug("SQL: " + sql);
				Log.DefaultLogWriteLineDebug("Param: " + paras.getDebugInfo() + ", Result: Rows=" + rs.getRow());
			}
			return ens.size();

		} catch (Exception ex) {
			String msg = "@运行查询在(RunSQLReturnResultSet_201809_MySQL)出错。\n  @SQL: " + sql + "\n  @Param: "
					+ paras.getDebugInfo() + "\n  @异常信息: " + StringUtils.replace(ex.getMessage(), "\n", " ");
			Log.DefaultLogWriteLineError(msg);
			throw new RuntimeException(msg, ex);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
	}

	private static int RunSQLReturnResultSet_201809_MySQL(String sql, Paras paras, Entity en, Attrs attrs) {
		ResultSet rs = null;
		Connection conn = null;
		Statement stmt = null;
		NamedParameterStatement pstmt = null;

		try {
			conn = DBAccess.getGetAppCenterDBConn_MySQL();

			if (null != paras && paras.size() > 0) {
				pstmt = new NamedParameterStatement(conn, sql);
				PrepareCommand(pstmt, paras);
				rs = pstmt.executeQuery();
			} else {
				stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
				rs = stmt.executeQuery(sql);
			}

			if (rs.next() == false)
				return 0;

			Hashtable ht = en.getRow();

			for (int idx = 0; idx < attrs.size(); idx++) {
				Attr attr = attrs.get(idx);

				Object val = rs.getObject(idx + 1);

				if (val == null) {
					if (attr.getIsNum() == true)
						val = 0;
					else
						val = "";
				}

				ht.put(attr.getKey(), val);

				// ht.pu
				// en.ger
				// en.SetValByKey(attr.getKey(), val);
			}

			if (Log.isLoggerDebugEnabled()) {
				Log.DefaultLogWriteLineDebug("SQL: " + sql);
				Log.DefaultLogWriteLineDebug("Param: " + paras.getDebugInfo() + ", Result: Rows=" + rs.getRow());
			}
			return 1;

		} catch (Exception ex) {
			String msg = "@运行查询在(RunSQLReturnResultSet_201809_MySQL)出错。\n  @SQL: " + sql + "\n  @Param: "
					+ paras.getDebugInfo() + "\n  @异常信息: " + StringUtils.replace(ex.getMessage(), "\n", " ");
			Log.DefaultLogWriteLineError(msg);
			throw new RuntimeException(msg, ex);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
	}

	private static void PrepareCommand(NamedParameterStatement ps, Paras params) {
		if (null == params || params.size() <= 0) {
			return;
		}
		try {
			for (Para para : params) {
				if (para.DAType == String.class) {
					try {
						if (para.val != null && !para.val.equals("")) {
							ps.setString(para.ParaName, String.valueOf(para.val));
						} else {
							ps.setString(para.ParaName, "");
						}
					} catch (Exception e) {
						ps.setString(para.ParaName, "");
					}
				} else if (para.DAType == Long.class) {
					ps.setLong(para.ParaName, (Long) para.val);
				} else if (para.DAType == Integer.class) {
					// if ("".equals(para.val))
					// para.val = "0";
					ps.setInt(para.ParaName,
							para.val instanceof String ? Integer.parseInt("" + para.val) : (Integer) para.val);
				} else if (para.DAType == Float.class) {
					ps.setFloat(para.ParaName, (Float) para.val);
				} else if (para.DAType == Double.class) {
					ps.setDouble(para.ParaName, (Double) para.val);
				} else if (para.DAType == BigDecimal.class) {
					ps.setBigDecimal(para.ParaName, (BigDecimal) para.val);
				} else if (para.DAType == Date.class) {
					Date date = (Date) para.val;
					if (date == null) {
						ps.setDate(para.ParaName, null);
					} else {
						ps.setDate(para.ParaName, new java.sql.Date(date.getTime()));
					}
				} else if (para.DAType == Boolean.class) {
					ps.setBoolean(para.ParaName, (Boolean) para.val);
				}
			}
		} catch (SQLException ex) {
			String msg = "@运行查询在(PrepareCommand)出错  @异常信息：" + StringUtils.replace(ex.getMessage(), "\n", " ");
			Log.DefaultLogWriteLineError(msg, ex);
		}
	}

	// 在当前Connection上执行
	public static DataTable RunSQLReturnTable(Paras ps) {
		return RunSQLReturnTable(ps.SQL, ps);
	}

	public static int RunSQLReturnTableCount = 0;

	/**
	 * 传递一个select 语句返回一个查询结果集合。
	 * 
	 * @param sql
	 *            select sql
	 * @return 查询结果集合DataTable
	 * @throws Exception
	 */
	public static DataTable RunSQLReturnTable(String sql) {
		Paras ps = new Paras();
		return RunSQLReturnTable(sql, ps);
	}

	public static DataTable RunSQLReturnTable(String sql, String key1, Object v1, String key2, Object v2)
			throws Exception {
		Paras ens = new Paras();
		ens.Add(key1, v1);
		ens.Add(key2, v2);
		return RunSQLReturnTable(sql, ens);
	}

	public static DataTable RunSQLReturnTable(String sql, String key, Object val) {
		Paras ens = new Paras();
		ens.Add(key, val);
		return RunSQLReturnTable(sql, ens);
	}

	private static boolean lockRunSQLReTable = false;

	public static DataSet RunSQLReturnDataSet(String sqls) {
		DataSet ds = new DataSet();
		String[] str = sqls.split(";");
		for (String s : str) {
			if ("".equals(s) || null == s)
				continue;
			DataTable dt = DBAccess.RunSQLReturnTable(s);
			ds.Tables.add(dt);
		}
		return ds;
	}

	// 查询单个值的方法.

	// OleDbConnection
	public static float RunSQLReturnValFloat(Paras ps) throws Exception {
		return RunSQLReturnValFloat(ps.SQL, ps, 0);
	}

	public static float RunSQLReturnValFloat(String sql, Paras ps, float val) throws Exception {
		ps.SQL = sql;
		Object obj = DBAccess.RunSQLReturnVal(ps);

		try {
			if (obj == null || obj.toString().equals("")) {
				return val;
			} else {
				return Float.parseFloat(obj.toString());
			}
		} catch (RuntimeException ex) {
			throw new RuntimeException(ex.getMessage() + sql + " @OBJ=" + obj, ex);
		}
	}

	/**
	 * 运行sql返回float
	 * 
	 * @param sql
	 *            要执行的sql,返回一行一列.
	 * @param isNullAsVal
	 *            如果是空值就返回的默认值
	 * @return float的返回值
	 * @throws Exception
	 */
	public static float RunSQLReturnValFloat(String sql, float isNullAsVal) throws Exception {
		return RunSQLReturnValFloat(sql, new Paras(), isNullAsVal);
	}

	public static float RunSQLReturnValFloat(String sql) throws Exception {
		try {
			return Float.parseFloat(DBAccess.RunSQLReturnVal(sql).toString());
		} catch (RuntimeException ex) {
			throw new RuntimeException(ex.getMessage() + sql, ex);
		}
	}

	public static int RunSQLReturnValInt(Paras ps, int IsNullReturnVal) {
		return RunSQLReturnValInt(ps.SQL, ps, IsNullReturnVal);
	}

	public static int RunSQLReturnValInt(String sql, int IsNullReturnVal) {
		Object obj = "";
		obj = DBAccess.RunSQLReturnVal(sql);
		if (obj == null || obj.toString().equals("")) {
			return IsNullReturnVal;
		} else {
			if (obj.toString().indexOf(".") != -1) {
				return Integer.parseInt(obj.toString().substring(0, obj.toString().indexOf(".")));
			} else {
				return Integer.parseInt(obj.toString());
			}

		}
	}

	public static int RunSQLReturnValInt(String sql, int IsNullReturnVal, Paras paras) throws Exception {
		Object obj = "";

		obj = DBAccess.RunSQLReturnVal(sql, paras);
		if (obj == null || obj.toString().equals("")) {
			return IsNullReturnVal;
		} else {
			return Integer.parseInt(obj.toString());
		}
	}

	public static BigDecimal RunSQLReturnValDecimal(String sql, BigDecimal IsNullReturnVal, int blws) {
		Paras ps = new Paras();
		ps.SQL = sql;
		return RunSQLReturnValDecimal(ps, IsNullReturnVal, blws);
	}

	public static BigDecimal RunSQLReturnValDecimal(Paras ps, BigDecimal IsNullReturnVal, int blws) {
		try {
			Object obj = DBAccess.RunSQLReturnVal(ps);
			if (obj == null || "".equals(obj.toString())) {
				return IsNullReturnVal;
			} else {
				BigDecimal d = new BigDecimal(Double.valueOf(obj.toString()));
				/*
				 * warning return BigDecimal.(d, blws);
				 */
				return d.setScale(blws, BigDecimal.ROUND_HALF_UP);
			}
		} catch (RuntimeException ex) {
			throw new RuntimeException("RunSQLReturnValDecimal " + ex.getMessage() + ps.SQL, ex);
		}
	}

	// 在当前的Connection执行 SQL 语句，返回首行首列
	public static int RunSQLReturnCOUNT(String sql) {
		return RunSQLReturnTable(sql).Rows.size();
	}

	public static int RunSQLReturnValInt(Paras ps) {

		String str = DBAccess.RunSQLReturnString(ps.SQL, ps);
		if (str.contains(".")) {
			str = str.substring(0, str.indexOf("."));
		}
		try {
			if (StringHelper.isNullOrEmpty(str)) {
				return 0;
			}
			return Integer.parseInt(str);
		} catch (RuntimeException ex) {
			throw new RuntimeException("@" + ps.SQL + "   Val=" + str + ex.getMessage(), ex);
		}
	}

	public static int RunSQLReturnValInt(String sql) {

		Object obj = DBAccess.RunSQLReturnVal(sql);
		/*
		 * warning if (obj == null || obj == DBNull.getValue()) { throw new
		 * RuntimeException("@没有获取您要查询的数据,请检查SQL:" + sql +
		 * " @关于查询出来的详细信息已经记录日志文件，请处理。"); }
		 */
		if (obj == null) {
			throw new RuntimeException("@没有获取您要查询的数据,请检查SQL:" + sql + " @关于查询出来的详细信息已经记录日志文件，请处理。");
		}
		String s = obj.toString();
		if (s.contains(".")) {
			s = s.substring(0, s.indexOf("."));
		}
		return Integer.parseInt(s);
	}

	public static int RunSQLReturnValInt(String sql, Paras paras) {
		return DBAccess.RunSQLReturnValInt(sql, paras, 0);
	}

	public static int RunSQLReturnValInt(String sql, Paras paras, int isNullAsVal) {
		try {
			return Integer.parseInt(DBAccess.RunSQLReturnVal(sql, paras).toString());
		} catch (java.lang.Exception e) {
			return isNullAsVal;
		}
	}

	public static String RunSQLReturnString(String sql, Paras ps) {
		if (ps == null) {
			ps = new Paras();
		}
		Object obj = DBAccess.RunSQLReturnVal(sql, ps);
		if (obj == null) {
			return null;
		} else {
			return obj.toString();
		}
	}

	/**
	 * 执行查询返回结果,如果为dbNull 返回 null.
	 * 
	 * @param sql
	 *            will run sql.
	 * @return ,如果为dbNull 返回 null.
	 * @throws Exception
	 */
	public static String RunSQLReturnString(String sql) {
		return RunSQLReturnString(sql, new Paras());

	}

	public static String RunSQLReturnStringIsNull(Paras ps, String isNullAsVal) throws Exception {
		String v = RunSQLReturnString(ps);
		if (v == null) {
			return isNullAsVal;
		} else {
			return v;
		}
	}

	/**
	 * 运行sql返回一个值
	 * 
	 * @param sql
	 * @param isNullAsVal
	 * @return
	 * @throws Exception
	 */
	public static String RunSQLReturnStringIsNull(String sql, String isNullAsVal) {
		String s = RunSQLReturnString(sql, new Paras());
		if (s == null) {
			return isNullAsVal;
		}
		return s;
	}

	public static String RunSQLReturnString(Paras ps) {
		return RunSQLReturnString(ps.SQL, ps);
	}

	// 在当前的Connection执行 SQL 语句，返回首行首列
	public static Object RunSQLReturnVal(String sql, String pkey, Object val) throws Exception {
		Paras ps = new Paras();
		ps.Add(pkey, val);
		return RunSQLReturnVal(sql, ps);
	}

	public static Object RunSQLReturnVal(String sql, Paras paras) {
		RunSQLReturnTableCount++;
		DataTable dt = null;
		try {
			switch (SystemConfig.getAppCenterDBType()) {
			case Oracle:
				dt = DBAccess.RunSQLReturnTable_200705_Ora(sql, paras);
				break;
			case MSSQL:
				dt = DBAccess.RunSQLReturnTable_200705_SQL(sql, paras);
				break;
			case MySQL:
				dt = DBAccess.RunSQLReturnTable_200705_MySQL(sql, paras);
				break;
			default:
				throw new RuntimeException("@没有判断的数据库类型");
			}
		} catch (RuntimeException e) {
			return null;
		}
		if (dt.Rows.size() == 0) {
			return null;
		}
		return dt.Rows.get(0).getValue(0);
	}

	public static Object RunSQLReturnVal(Paras ps) {
		return RunSQLReturnVal(ps.SQL, ps);
	}

	public static Object RunSQLReturnVal(String sql) {
		RunSQLReturnTableCount++;
		DataTable dt = null;
		// System.out.print(SystemConfig.getAppCenterDBType());
		switch (SystemConfig.getAppCenterDBType()) {
		case Oracle:
			dt = DBAccess.RunSQLReturnTable_200705_Ora(sql, new Paras());
			break;
		case MSSQL:
			dt = DBAccess.RunSQLReturnTable_200705_SQL(sql, new Paras());
			break;
		case MySQL:
			dt = DBAccess.RunSQLReturnTable_200705_MySQL(sql, new Paras());
			break;
		default:
			throw new RuntimeException("@没有判断的数据库类型");
		}
		if (dt.Rows.size() == 0)
			return null;

		return dt.Rows.get(0).getValue(0);
	}

	// 检查是不是存在
	/**
	 * 检查是不是存在
	 * 
	 * @param sql
	 *            sql
	 * @return 检查是不是存在
	 * @throws Exception
	 */
	public static boolean IsExits(String sql) {
		if (RunSQLReturnVal(sql) == null) {
			return false;
		}
		return true;
	}

	public static boolean IsExits(String sql, Paras ps) {
		if (RunSQLReturnVal(sql, ps) == null) {
			return false;
		}
		return true;
	}

	/**
	 * 判断是否存在主键pk .
	 * 
	 * @param tab
	 *            物理表
	 * @return 是否存在
	 * @throws Exception
	 */
	public static boolean IsExitsTabPK(String tab) {
		BP.DA.Paras ps = new Paras();
		ps.Add("Tab", tab);
		String sql = "";
		switch (getAppCenterDBType()) {
		case MSSQL:
			sql = "SELECT column_name, table_name,CONSTRAINT_NAME FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE WHERE table_name =:Tab ";
			break;
		case Oracle:
			sql = "SELECT constraint_name, constraint_type,search_condition, r_constraint_name  from user_constraints WHERE table_name = upper(:Tab) AND constraint_type = 'P'";
			break;
		case MySQL:
			sql = "SELECT column_name, table_name, CONSTRAINT_NAME from INFORMATION_SCHEMA.KEY_COLUMN_USAGE WHERE table_name =:Tab and table_schema='"
					+ SystemConfig.getAppCenterDBDatabase() + "' ";
			break;
		default:
			throw new RuntimeException("数据库连接配置失败！ ");
		}
		DataTable dt = DBAccess.RunSQLReturnTable(sql, ps);
		if (dt.Rows.size() >= 1) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断系统中是否存在对象.
	 * 
	 * @param table
	 * @return
	 * @throws Exception
	 */
	public static boolean IsExitsObject(String obj) throws Exception {
		Paras ps = new Paras();
		ps.Add("obj", obj);

		switch (getAppCenterDBType()) {
		case Oracle:
			if (obj.indexOf(".") != -1) {
				obj = obj.split("[.]", -1)[1];
			}

			String sql = "select object_name from all_objects WHERE  object_name = upper(:obj) and OWNER='"
					+ SystemConfig.getUser().toUpperCase() + "' ";

			return IsExits(sql, ps);

		// return IsExits("select object_name from all_objects WHERE object_name
		// = upper(:obj) ", ps);
		case MSSQL:
			return IsExits("SELECT name  FROM sysobjects  WHERE  name = '" + obj + "'");
		case MySQL:
			// 增加物理表名称非空判断
			if (obj.equals("") || null == obj) {
				return true;
			}
			if (obj.indexOf(".") != -1) {
				obj = obj.split("[.]", -1)[1];
			}
			return IsExits("SELECT table_name, table_type FROM information_schema.tables  WHERE table_name = '" + obj
					+ "' AND   TABLE_SCHEMA='" + SystemConfig.getAppCenterDBDatabase() + "' ");
		default:
			throw new RuntimeException("没有识别的数据库编号");
		}
	}

	/**
	 * 表中是否存在指定的列
	 * 
	 * @param table
	 *            表名
	 * @param col
	 *            列名
	 * @return 是否存在
	 * @throws Exception
	 */
	public static boolean IsExitsTableCol(String table, String col) throws Exception {
		Paras ps = new Paras();
		ps.Add("tab", table);
		ps.Add("col", col);

		int i = 0;
		switch (DBAccess.getAppCenterDBType()) {
		// case Access:
		// return false;
		// break;
		case MSSQL:
			i = DBAccess.RunSQLReturnValInt("SELECT  COUNT(*) FROM information_schema.COLUMNS  WHERE TABLE_NAME='"
					+ table + "' AND COLUMN_NAME='" + col + "'", 0);
			break;
		case MySQL:
			String sql = "select count(*) FROM information_schema.columns WHERE TABLE_SCHEMA='"
					+ SystemConfig.getAppCenterDBDatabase() + "' AND table_name ='" + table
					+ "' and column_Name='" + col + "'";
			i = DBAccess.RunSQLReturnValInt(sql);
			break;
		case Oracle:
			if (table.indexOf(".") != -1) {
				table = table.split("[.]", -1)[1];
			}
			i = DBAccess.RunSQLReturnValInt(
					"SELECT COUNT(*) from user_tab_columns  WHERE table_name= upper(:tab) AND column_name= upper(:col) ",
					ps);
			break;
		default:
			throw new RuntimeException("error");
		}
		if (i == 1) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 获得表的基础信息，返回如下列: 1, 字段名称，字段描述，字段类型，字段长度.
	 * 
	 * @param tableName
	 *            表名
	 */
	public static DataTable GetTableSchema(String tableName) {
		String sql = "";
		switch (SystemConfig.getAppCenterDBType()) {
		case MSSQL:
			sql = "SELECT column_name as FName, data_type as FType, CHARACTER_MAXIMUM_LENGTH as FLen , column_name as FDesc FROM information_schema.columns where table_name='"
					+ tableName + "'";
			break;
		case Oracle:
			sql = "SELECT COLUMN_NAME as FName,DATA_TYPE as FType,DATA_LENGTH as FLen,COLUMN_NAME as FDesc FROM all_tab_columns WHERE table_name = upper('"
					+ tableName + "')";
			break;
		case MySQL:
			sql = "SELECT COLUMN_NAME FName,DATA_TYPE FType,CHARACTER_MAXIMUM_LENGTH FLen,COLUMN_COMMENT FDesc FROM information_schema.columns WHERE table_name='"
					+ tableName + "' and TABLE_SCHEMA='" + SystemConfig.getAppCenterDBDatabase() + "'";
			break;
		default:
			break;
		}

		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		if (SystemConfig.getAppCenterDBType() == DBType.Oracle) {
			dt.Columns.get("FNAME").ColumnName = "FName";
			dt.Columns.get("FTYPE").ColumnName = "FType";
			dt.Columns.get("FLEN").ColumnName = "FLen";
			dt.Columns.get("FDESC").ColumnName = "FDesc";
		}
		return dt;
	}

	/**
	 * 安装前检查数据库用户权限
	 * 
	 * @return
	 */
	public static boolean isCanInstall() {
		String checkSql = "CREATE TABLE CHECKDB(NO int)";
		try {
			// 建表
			RunSQL(checkSql);
			// 建视图
			checkSql = "CREATE VIEW CHECKDBVIEW AS SELECT * FROM CHECKDB";
			RunSQL(checkSql);
			// 删除表
			checkSql = "DROP TABLE CHECKDB";
			RunSQL(checkSql);
			// 删除视图
			checkSql = "DROP VIEW CHECKDBVIEW";
			RunSQL(checkSql);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * 删除表的主键
	 * 
	 * @param table
	 *            表名称
	 */
	public static void DropTablePK(String table) {
		String pkName = DBAccess.GetTablePKName(table);
		if (pkName == null) {
			return;
		}

		String sql = "";
		switch (SystemConfig.getAppCenterDBType()) {
		case Oracle:
		case MSSQL:
			sql = "ALTER TABLE " + table + " DROP CONSTRAINT " + pkName;
			break;
		case MySQL:
			sql = "ALTER TABLE " + table + " DROP primary key";
			break;
		default:
			Log.DebugWriteError("DBAccess DropTablePK " + "@不支持的数据库类型." + SystemConfig.getAppCenterDBType());
			throw new RuntimeException("@不支持的数据库类型." + SystemConfig.getAppCenterDBType());
		}
		BP.DA.DBAccess.RunSQL(sql);
	}

	/**
	 * 获得table的主键
	 * 
	 * @param table
	 *            表名称
	 * @return 主键名称、没有返回为空.
	 */
	public static String GetTablePKName(String table) {
		BP.DA.Paras ps = new Paras();
		ps.Add("Tab", table);
		String sql = "";
		switch (getAppCenterDBType()) {
		case Access:
			return null;
		case MSSQL:
			sql = "SELECT CONSTRAINT_NAME,column_name FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE WHERE table_name ='"
					+ table + "'";
			break;
		case Oracle:
			sql = "SELECT constraint_name, constraint_type,search_condition, r_constraint_name  from user_constraints WHERE table_name = upper('"
					+ table + "') AND constraint_type = 'P'";
			break;
		case MySQL:
			sql = "SELECT CONSTRAINT_NAME , column_name, table_name CONSTRAINT_NAME from INFORMATION_SCHEMA.KEY_COLUMN_USAGE WHERE table_name ='"
					+ table + "' and table_schema='" + SystemConfig.getAppCenterDBDatabase() + "' ";
			break;
		case Informix:
			sql = "SELECT * FROM sysconstraints c inner join systables t on c.tabid = t.tabid where t.tabname = lower('"
					+ table + "') and constrtype = 'P'";
			break;
		default:
			Log.DebugWriteError("DBAccess GetTablePKName " + "@没有判断的数据库类型.");
			throw new RuntimeException("@没有判断的数据库类型.");
		}

		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() == 0) {
			return null;
		}
		return dt.getValue(0, 0).toString();
	}

	/**
	 * 从数据库里获得文本
	 * 
	 * @param tableName
	 *            表名
	 * @param tablePK
	 *            主键
	 * @param pkVal
	 *            主键值
	 * @param fileSaveField
	 *            保存字段
	 * @return
	 * @throws Exception 
	 */
	public static String GetBigTextFromDB(String tableName, String tablePK, String pkVal, String fileSaveField)throws Exception {
		   if (SystemConfig.getAppCenterDBType() == DBType.MSSQL
		         || SystemConfig.getAppCenterDBType() == DBType.MySQL){
		      String strSQL = "SELECT " + fileSaveField + " FROM " + tableName + " WHERE " + tablePK + "='" + pkVal + "'";
		      return DBAccess.RunSQLReturnStringIsNull(strSQL,"");
		   }
		   byte[] byteFile = GetByteFromDB(tableName, tablePK, pkVal, fileSaveField);
		   if (byteFile == null) {
		      return null;
		   }
		   return new String(byteFile,"UTF-8");
		}

	/**
	 * 从数据库里提取文件
	 * 
	 * @param tableName
	 *            表名
	 * @param tablePK
	 *            表主键
	 * @param pkVal
	 *            主键值
	 * @param fileSaveField
	 *            字段
	 */
	public static byte[] GetByteFromDB(String tableName, String tablePK, String pkVal, String fileSaveField)  throws Exception{
		   Connection conn = null; //数据库连接
		   PreparedStatement pstmt = null; //数据库SQL执行
		   ResultSet rs = null; // 执行结果
		   if (SystemConfig.getAppCenterDBType() == DBType.MSSQL)
		      conn = BP.DA.DBAccess.getGetAppCenterDBConn_MSSQL();
		   if (SystemConfig.getAppCenterDBType() == DBType.Oracle)
		      conn = BP.DA.DBAccess.getGetAppCenterDBConn_Oracle();
		   if (SystemConfig.getAppCenterDBType() == DBType.MySQL)
		      conn = BP.DA.DBAccess.getGetAppCenterDBConn_MySQL();
		   String strSQL = "SELECT " + fileSaveField + " FROM " + tableName + " WHERE " + tablePK + "='" + pkVal + "'";
		   pstmt = conn.prepareStatement(strSQL);
		   // 执行它.
		   try
		   {
		      rs = pstmt.executeQuery();
		      byte[] byteFile = null;
		      if (rs.next())
		      {
		         byteFile = rs.getBytes(fileSaveField);
		      }
		      return byteFile;
		   }
		   catch (Exception e)
		   {
		      if (!BP.DA.DBAccess.IsExitsTableCol(tableName, fileSaveField))
		      {
		         /*如果没有此列，就自动创建此列.*/
		         String sql = "ALTER TABLE " + tableName + " ADD  " + fileSaveField + " image ";
		         BP.DA.DBAccess.RunSQL(sql);
		      }
		      return GetByteFromDB(tableName, tablePK, pkVal, fileSaveField);
		   }finally {
		      if(rs != null)
		         rs.close();
		      if(pstmt!=null)
		         pstmt.close();
		      if(conn!=null)
		         conn.close();
		   }
		}

	/**
	 * 开启事物 @xushuaho
	 */
	public static void DoTransactionBegin() throws Exception {

		Connection conn = ContextHolderUtils.getInstance().getDataSource().getConnection();
		conn.setAutoCommit(false); // 设置不能自动提交.
		SetConnOfTransactionForMySQL(BP.Web.WebUser.getNo(), conn);
	}

	// 提交事物 @xushuaho
	public static void DoTransactionCommit() throws Exception {

		String id = BP.Web.WebUser.getNo();
		Connection conn = DBAccess.GetConnOfTransactionForMySQL(id);
		conn.commit();
		conn.setAutoCommit(true); // 设置不能自动提交.
		conn.close();
		_HashtableConn.remove(id);
	}

	// 回滚事物 @xushuaho
	public static void DoTransactionRollback() throws Exception {

		String buessID = BP.Web.WebUser.getNo();

		Connection conn = DBAccess.GetConnOfTransactionForMySQL(buessID);
		conn.rollback();
		conn.setAutoCommit(true); // 设置不能自动提交.
		conn.close();
		_HashtableConn.remove(buessID);

	}

	public static DataTable ToLower(DataTable dt) {
		// 把列名转成小写.
		for (int i = 0; i < dt.Columns.size(); i++) {
			dt.Columns.get(i).ColumnName = dt.Columns.get(i).ColumnName.toLowerCase();
		}
		return dt;
	}

	/**
	 * 检查是否连接成功.
	 * 
	 * @return
	 */
	public static boolean TestIsConnection() {
		try {
			switch (SystemConfig.getAppCenterDBType()) {
			case MSSQL:
				BP.DA.DBAccess.RunSQLReturnString("SELECT 1+2 ");
				break;
			case Oracle:
			case MySQL:
				BP.DA.DBAccess.RunSQLReturnString("SELECT 1+2 FROM DUAL ");
				break;
			case Informix:
				BP.DA.DBAccess.RunSQLReturnString("SELECT 1+2 FROM DUAL ");
				break;
			default:
				break;
			}
			return true;
		} catch (RuntimeException ex) {
			return false;
		}
	}

	/**
	 * 通用SQL查询分页返回DataTable
	 * 
	 * @param sql
	 *            SQL语句，不带排序（Order By）语句
	 * @param pageSize
	 *            每页记录数量
	 * @param pageIdx
	 *            请求页码
	 * @param key
	 *            记录主键（不能为空，不能有重复，必须包含在返回字段中）
	 * @param orderKey
	 *            排序字段（此字段必须包含在返回字段中）
	 * @param orderType
	 *            排序方式，ASC/DESC
	 * @return
	 */
	public static DataTable RunSQLReturnTable(String sql, int pageSize, int pageIdx, String key, String orderKey,
			String orderType) {
		switch (DBAccess.getAppCenterDBType()) {
		case MSSQL:
			return RunSQLReturnTable_201612_SQL(sql, pageSize, pageIdx, key, orderKey, orderType);
		case Oracle:
			return RunSQLReturnTable_201612_Ora(sql, pageSize, pageIdx, orderKey, orderType);
		case MySQL:
			return RunSQLReturnTable_201612_MySql(sql, pageSize, pageIdx, key, orderKey, orderType);
		default:
			throw new RuntimeException("@未涉及的数据库类型！");
		}
	}

	/**
	 * 通用SqlServer查询分页返回DataTable
	 * 
	 * @param sql
	 *            SQL语句，不带排序（Order By）语句
	 * @param pageSize
	 *            每页记录数量
	 * @param pageIdx
	 *            请求页码
	 * @param key
	 *            记录主键（不能为空，不能有重复，必须包含在返回字段中）
	 * @param orderKey
	 *            排序字段（此字段必须包含在返回字段中）
	 * @param orderType
	 *            排序方式，ASC/DESC
	 * @return
	 */
	private static DataTable RunSQLReturnTable_201612_SQL(String sql, int pageSize, int pageIdx, String key,
			String orderKey, String orderType) {
		String sqlstr = "";

		orderType = StringUtils.isEmpty(orderType) ? "ASC" : orderType.toUpperCase();

		if (pageIdx < 1) {
			pageIdx = 1;
		}

		if (pageIdx == 1) {
			sqlstr = "SELECT TOP " + pageSize + " * FROM (" + sql + ") T1" + (StringUtils.isEmpty(orderKey) ? ""
					: String.format(" ORDER BY T1.%1$s %2$s", orderKey, orderType));
		} else {
			sqlstr = "SELECT TOP " + pageSize + " * FROM (" + sql + ") T1" + " WHERE T1." + key
					+ (orderType.equals("ASC") ? " > " : " < ") + "(" + " SELECT "
					+ (orderType.equals("ASC") ? "MAX(T3." : "MIN(T3.") + key + ") FROM (" + " SELECT TOP ((" + pageIdx
					+ " - 1) * 10) T2." + key + "FROM (" + sql + ") T2"
					+ (StringUtils.isEmpty(orderKey) ? ""
							: String.format(" ORDER BY T2.%1$s %2$s", orderKey, orderType))
					+ " ) T3)" + (StringUtils.isEmpty(orderKey) ? ""
							: String.format(" ORDER BY T.%1$s %2$s", orderKey, orderType));
		}

		return RunSQLReturnTable(sqlstr);
	}

	/**
	 * 通用Oracle查询分页返回DataTable
	 * 
	 * @param sql
	 *            SQL语句，不带排序（Order By）语句
	 * @param pageSize
	 *            每页记录数量
	 * @param pageIdx
	 *            请求页码
	 * @param orderKey
	 *            排序字段（此字段必须包含在返回字段中）
	 * @param orderType
	 *            排序方式，ASC/DESC
	 * @return
	 */
	private static DataTable RunSQLReturnTable_201612_Ora(String sql, int pageSize, int pageIdx, String orderKey,
			String orderType) {
		if (pageIdx < 1) {
			pageIdx = 1;
		}

		int start = (pageIdx - 1) * pageSize + 1;
		int end = pageSize * pageIdx;

		orderType = StringUtils.isEmpty(orderType) ? "ASC" : orderType.toUpperCase();

		String sqlstr = "SELECT * FROM ( SELECT T1.*, ROWNUM RN " + "FROM (SELECT * FROM  (" + sql + ") T2 "
				+ (StringUtils.isEmpty(orderType) ? "" : String.format("ORDER BY T2.%1$s %2$s", orderKey, orderType))
				+ ") T1 WHERE ROWNUM <= " + end + " ) WHERE RN >=" + start;

		return RunSQLReturnTable(sqlstr);
	}

	/**
	 * 通用MySql查询分页返回DataTable
	 * 
	 * @param sql
	 *            SQL语句，不带排序（Order By）语句
	 * @param pageSize
	 *            每页记录数量
	 * @param pageIdx
	 *            请求页码
	 * @param key
	 *            记录主键（不能为空，不能有重复，必须包含在返回字段中）
	 * @param orderKey
	 *            排序字段（此字段必须包含在返回字段中）
	 * @param orderType
	 *            排序方式，ASC/DESC
	 * @return
	 */
	private static DataTable RunSQLReturnTable_201612_MySql(String sql, int pageSize, int pageIdx, String key,
			String orderKey, String orderType) {
		String sqlstr = "";
		orderType = StringUtils.isEmpty(orderType) ? "ASC" : orderType.toUpperCase();

		if (pageIdx < 1) {
			pageIdx = 1;
		}

		sqlstr = "SELECT * FROM (" + sql + ") T1 WHERE T1." + key + (orderType.equals("ASC") ? " >= " : " <= ")
				+ "(SELECT T2." + key + " FROM (" + sql + ") T2"
				+ (StringUtils.isEmpty(orderKey) ? "" : String.format(" ORDER BY T2.%1$s %2$s", orderKey, orderType))
				+ " LIMIT " + ((pageIdx - 1) * pageSize) + ",1) LIMIT " + pageSize;

		return RunSQLReturnTable(sqlstr);
	}

	// public static void main(String[] args) throws Exception {
	// // Paras ps = new Paras();
	// // // Para p = new Para("visit_date",Date.class,
	// // // DataType.stringToDate("2014-09-05"));
	// // // ps.Add(p);
	// // DataTable table = RunSQLReturnTable_200705_MySQL(
	// // "select * from media_control where ip = '192.168.0.2'", ps);
	// // System.out.println(Json.ToJson(table));
	//
	// DataSet ds = new DataSet();
	// ds.readXmls("D:/android
	// workspace/jflow/jflow-web/src/main/webapp/DataUser/XML/TempleteSheetOfStartNode.xml");
	// }
	/**
	 * 是否是view
	 *
	 * @param tabelOrViewName
	 * @return
	 */
	public static boolean IsView(String tabelOrViewName)
	{
		return IsView(tabelOrViewName, SystemConfig.getAppCenterDBType());
	}
	/**
	 * 是否是view
	 * 
	 * @param tabelOrViewName
	 * @return
	 */
	public static boolean IsView(String tabelOrViewName,DBType dbType) {
		String sql = "";
		switch (dbType) {
		case Oracle:
			sql = "SELECT TABTYPE  FROM TAB WHERE UPPER(TNAME)=:v";
			DataTable oradt = DBAccess.RunSQLReturnTable(sql, "v", tabelOrViewName.toUpperCase());
			if (oradt.Rows.size() == 0) {
				throw new RuntimeException("@表不存在[" + tabelOrViewName + "]");
			}
			if (oradt.Rows.get(0).getValue(0).toString().toUpperCase().trim().equals("VIEW")) {
				return true;
			} else {
				return false;
			}
		case Access:
			sql = "select   Type   from   msysobjects   WHERE   UCASE(name)='" + tabelOrViewName.toUpperCase() + "'";
			DataTable dtw = DBAccess.RunSQLReturnTable(sql);
			if (dtw.Rows.size() == 0) {
				throw new RuntimeException("@表不存在[" + tabelOrViewName + "]");
			}
			if (dtw.Rows.get(0).getValue(0).toString().trim().equals("5")) {
				return true;
			} else {
				return false;
			}
		case MSSQL:
			sql = "select xtype from sysobjects WHERE name =" + SystemConfig.getAppCenterDBVarStr() + "v";
			DataTable dt1 = DBAccess.RunSQLReturnTable(sql, "v", tabelOrViewName);
			if (dt1.Rows.size() == 0) {
				throw new RuntimeException("@表不存在[" + tabelOrViewName + "]");
			}

			if (dt1.Rows.get(0).getValue(0).toString().toUpperCase().trim().equals("V")) {
				return true;
			} else {
				return false;
			}
		case Informix:
			sql = "select tabtype from systables where tabname = '" + tabelOrViewName.toLowerCase() + "'";
			DataTable dtaa = DBAccess.RunSQLReturnTable(sql);
			if (dtaa.Rows.size() == 0) {
				throw new RuntimeException("@表不存在[" + tabelOrViewName + "]");
			}

			if (dtaa.Rows.get(0).getValue(0).toString().toUpperCase().trim().equals("V")) {
				return true;
			} else {
				return false;
			}
		case MySQL:
			sql = "SELECT Table_Type FROM information_schema.TABLES WHERE table_name='" + tabelOrViewName
					+ "' and table_schema='" + SystemConfig.getAppCenterDBDatabase() + "'";
			DataTable dt2 = DBAccess.RunSQLReturnTable(sql);
			if (dt2.Rows.size() == 0) {
				throw new RuntimeException("@表不存在[" + tabelOrViewName + "]");
			}

			if (dt2.Rows.get(0).getValue(0).toString().toUpperCase().trim().equals("VIEW")) {
				return true;
			} else {
				return false;
			}
		default:
			throw new RuntimeException("@没有做的判断。");
		}

		/*
		 * DataTable dt = DBAccess.RunSQLReturnTable(sql, "v",
		 * tabelOrViewName.toUpperCase()); if (dt.Rows.size() == 0) { throw new
		 * RuntimeException("@表不存在[" + tabelOrViewName + "]"); }
		 * 
		 * if (dt.Rows.get(0).getValue(0).toString().equals("VIEW")) { return
		 * true; } else { return false; } return true;
		 */
	}
}
