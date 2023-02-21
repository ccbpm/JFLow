package bp.sys;

import bp.da.*;
import bp.difference.SystemConfig;
import bp.en.*;
import bp.tools.StringUtils;

import java.sql.*;
import java.util.List;

/** 
 数据源
*/
public class SFDBSrc extends EntityNoName
{

		///#region 属性
	public final FieldCaseModel getFieldCaseModel() throws Exception {
		switch (this.getDBSrcType())
		{
			case Oracle:
				return FieldCaseModel.UpperCase;
			case PostgreSQL:
			case UX:
				return FieldCaseModel.Lowercase;
			default:
				return FieldCaseModel.None;
		}
	}
	/** 
	 标签
	*/
	public final String getIcon() throws Exception {
		switch (this.getDBSrcType())
		{
			case Localhost:
				return "<img src='/WF/Img/DB.gif' />";
			default:
				return "";
		}
	}

	public final String getUserID() throws Exception
	{
		return this.GetValStringByKey(SFDBSrcAttr.UserID);
	}
	public final void setUserID(String value) throws Exception
	{
		this.SetValByKey(SFDBSrcAttr.UserID, value);
	}
	/**
	 密码
	 */
	public final String getPassword() throws Exception
	{
		return this.GetValStringByKey(SFDBSrcAttr.Password);
	}
	public final void setIsPassword(String value) throws Exception
	{
		this.SetValByKey(SFDBSrcAttr.Password, value);
	}



	/** 
	 数据库类型
	*/
	public final DBSrcType getDBSrcType() {
		return DBSrcType.forValue(this.GetValIntByKey(SFDBSrcAttr.DBSrcType));
	}
	public final void setDBSrcType(DBSrcType value)
	 {
		this.SetValByKey(SFDBSrcAttr.DBSrcType, value.getValue());
	}
	public final String getDBName()
	{
		return this.GetValStringByKey(SFDBSrcAttr.DBName);
	}
	public final void setDBName(String value)
	 {
		this.SetValByKey(SFDBSrcAttr.DBName, value);
	}
	public final String getIP()
	{
		return this.GetValStringByKey(SFDBSrcAttr.IP);
	}
	public final void setIP(String value)
	 {
		this.SetValByKey(SFDBSrcAttr.IP, value);
	}

	/** 
	 数据库类型
	*/
	public final DBType getHisDBType() {
		switch (this.getDBSrcType())
		{
			case Localhost:
				return bp.difference.SystemConfig.getAppCenterDBType();
			case SQLServer:
				return DBType.MSSQL;
			case Oracle:
				return DBType.Oracle;
			case MySQL:
				return DBType.MySQL;
			case Informix:
				return DBType.Informix;
			case KingBaseR3:
				return DBType.KingBaseR3;
			case KingBaseR6:
				return DBType.KingBaseR6;
			default:
				throw new RuntimeException("err@HisDBType没有判断的数据库类型.");
		}
	}

		///#endregion


		///#region 数据库访问方法
	/** 
	 运行SQL返回数值
	 
	 param sql 一行一列的SQL
	 param isNullAsVal 如果为空，就返回制定的值.
	 @return 要返回的值
	*/
	public final int RunSQLReturnInt(String sql, int isNullAsVal)
	{
		DataTable dt = this.RunSQLReturnTable(sql);
		if (dt.Rows.size() == 0)
		{
			return isNullAsVal;
		}
		return Integer.parseInt(dt.Rows.get(0).getValue(0).toString());
	}


	public final Entities DoQuery(Entities ens, String sql, String expPageSize, String pk, Attrs attrs, int count, int pageSize, int pageIdx, String orderBy) throws Exception {
		return DoQuery(ens, sql, expPageSize, pk, attrs, count, pageSize, pageIdx, orderBy, false);
	}

//ORIGINAL LINE: public Entities DoQuery(Entities ens, string sql, string expPageSize, string pk, Attrs attrs, int count, int pageSize, int pageIdx, string orderBy, bool isDesc = false)
	public final Entities DoQuery(Entities ens, String sql, String expPageSize, String pk, Attrs attrs, int count, int pageSize, int pageIdx, String orderBy, boolean isDesc)throws Exception
	{
		DataTable dt = new DataTable();
		if (count == 0)
		{
			return null;
		}
		int pageNum = 0;
		String orderBySQL = "";
		//如果没有加入排序字段，使用主键
		if (DataType.IsNullOrEmpty(orderBy) == false)
		{
			orderBy = pk;
			String isDescStr = "";
			if (isDesc)
			{
				isDescStr = " DESC ";
			}
			orderBySQL = orderBy + isDescStr;
		}
		sql = sql + " " + orderBySQL;
		try
		{
			if (pageSize == 0)
			{
				pageSize = 10;
			}
			if (pageIdx == 0)
			{
				pageIdx = 1;
			}
			int top = pageSize * (pageIdx - 1) + 1;
			int max = pageSize * pageIdx;
			int myleftCount = count - (pageNum * pageSize);
			String mysql = "";
			switch (this.getDBSrcType())
			{
				case Oracle:
					mysql = "SELECT * FROM (" + sql + " AND ROWNUM<=" + max + ") temp WHERE temp.rn>=" + top;
					break;
				case MySQL:
					mysql = sql + " LIMIT " + pageSize * (pageIdx - 1) + "," + pageSize;
					break;
				case PostgreSQL:
				case UX:
				case SQLServer:
				default:
					//获取主键的类型
					Attr attr = attrs.GetAttrByKeyOfEn(pk);

					//mysql = countSql;
					//mysql = mysql.Substring(mysql.ToUpper().IndexOf("FROM "));
					// mysql = "SELECT  "+ mainTable+pk + " "  + mysql;
					String pks = this.GenerPKsByTableWithPara(pk, attr.getIsNum(), expPageSize, pageSize * (pageIdx - 1), max, null);

					if (pks == null)
					{
						mysql = sql + " AND 1=2 ";
					}
					else
					{
						mysql = sql + " AND OID in(" + pks + ")";
					}
					break;
			}
			dt = this.RunSQLReturnTable(mysql);
			return InitEntitiesByDataTable(ens, dt, null);

		}
		catch (RuntimeException ex)
		{
			throw new RuntimeException("err@数据源执行分页SQL出现错误：" + sql + "错误原因:" + ex.getMessage());
		}
	}

	public final Entities InitEntitiesByDataTable(Entities ens, DataTable dt, String[] fullAttrs) throws Exception {
		if (fullAttrs == null)
		{
			Map enMap = ens.getGetNewEntity().getEnMap();
			Attrs attrs = enMap.getAttrs();
			try
			{

				for (DataRow dr : dt.Rows)
				{
					Entity en = ens.getGetNewEntity();
					for (Attr attr : attrs.ToJavaList())
					{
						if (dt.Columns.contains(attr.getKey()) == false && dt.Columns.contains(attr.getKey().toUpperCase()) == false)
						{
							continue;
						}
						if (bp.difference.SystemConfig.AppCenterDBFieldCaseModel() == FieldCaseModel.UpperCase)
						{
							if (attr.getMyFieldType() == FieldType.RefText)
							{
								en.SetValByKey(attr.getKey(), dr.getValue(attr.getKey()));
							}
							else
							{
								en.SetValByKey(attr.getKey(), dr.getValue(attr.getKey().toUpperCase()));
							}
						}
						else if (bp.difference.SystemConfig.AppCenterDBFieldCaseModel() == FieldCaseModel.Lowercase)
						{
							if (attr.getMyFieldType() == FieldType.RefText)
							{
								en.SetValByKey(attr.getKey(), dr.getValue(attr.getKey()));
							}
							else
							{
								en.SetValByKey(attr.getKey(), dr.getValue(attr.getKey().toLowerCase()));
							}
						}
						else
						{
							en.SetValByKey(attr.getKey(), dr.getValue(attr.getKey()));
						}
					}
					ens.AddEntity(en);
				}
			}
			catch (RuntimeException ex)
			{
				// warning 不应该出现的错误. 2011-12-03 add
				String cols = "";
				for (DataColumn dc : dt.Columns)
				{
					cols += " , " + dc.ColumnName;
				}
				throw new RuntimeException("Columns=" + cols + "@Ens=" + ens.toString() + " @异常信息:" + ex.getMessage());
			}

			return ens;
		}

		for (DataRow dr : dt.Rows)
		{
			Entity en = ens.getGetNewEntity();
			for (String str : fullAttrs)
			{
				if (dt.Columns.contains(str) == false && dt.Columns.contains(str.toUpperCase()) == false)
				{
					continue;
				}
				if (bp.difference.SystemConfig.AppCenterDBFieldCaseModel() == FieldCaseModel.UpperCase)
				{
					if (dt.Columns.contains(str) == true)
					{
						en.SetValByKey(str, dr.getValue(str));
					}
					else
					{
						en.SetValByKey(str, dr.getValue(str.toUpperCase()));
					}
				}
				else if (bp.difference.SystemConfig.AppCenterDBFieldCaseModel() == FieldCaseModel.Lowercase)
				{
					if (dt.Columns.contains(str) == true)
					{
						en.SetValByKey(str, dr.getValue(str));
					}
					else
					{
						en.SetValByKey(str, dr.getValue(str.toLowerCase()));
					}
				}

				else
				{
					en.SetValByKey(str, dr.getValue(str));
				}

			}
			ens.AddEntity(en);
		}

		return ens;

	}

	public final String GenerPKsByTableWithPara(String pk, boolean isNum, String sql, int from, int to, Paras paras)
	{
		DataTable dt = this.RunSQLReturnTable(sql, paras);
		String pks = "";
		int i = 0;
		int paraI = 0;

		String dbStr = bp.difference.SystemConfig.getAppCenterDBVarStr();
		for (DataRow dr : dt.Rows)
		{
			i++;
			if (i > from)
			{


				if (isNum == true)
				{
					pks += Integer.parseInt(dr.getValue(pk).toString()) + ",";
				}
				else
				{
					pks += "'" + dr.getValue(pk).toString() + "',";
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
		}
		return pks.substring(0, pks.length() - 1);
	}
	/** 
	 运行SQL
	 
	 param sql
	 @return 
	*/
	public final int RunSQL(String sql) throws Exception {int i =0;
		Connection conn = null;
		Statement stmt = null;
		switch(this.getDBSrcType()){
			case Localhost:
				return DBAccess.RunSQL(sql);
			case Oracle:
			case KingBaseR3:
			case KingBaseR6:
			case MySQL:
			case SQLServer:
				conn =this.getConnection();
				try{
					stmt = conn.createStatement();// 创建用于执行静态sql语句的Statement对象，st属局部变量
					i = stmt.executeUpdate(sql);
					return i;
				}catch (Exception ex) {
					String msg = "@运行外部数据"+this.getDBName()+"SQL报错。\n  @SQL: " + sql + "\n  @异常信息: " + StringUtils.replace(ex.getMessage(), "\n", " ");
					Log.DefaultLogWriteLineError(msg);
					throw new RuntimeException(msg, ex);
				}finally {
					closeAll(conn,stmt,null);
				}
			default:
				throw new Exception("err@没有处理数据库"+this.getDBSrcType()+"类型");
		}
	}


	private  void closeAll(Connection conn, Statement st, ResultSet rs){
		if(conn!=null){
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if(st!=null){
			try {
				st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if(rs!=null){
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	public final void RunSQLs(String sql) throws Exception {
		if (DataType.IsNullOrEmpty(sql))
		{
			return;
		}

		//sql = DealSQL(sql);//去掉注释.

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
	 运行SQL
	 
	 param runObj
	 @return 
	*/



	public final String RunSQLReturnString(String runObj)
	{
		return RunSQLReturnString(runObj, null);
	}

//ORIGINAL LINE: public string RunSQLReturnString(string runObj, string isNullasVal = null)
	public final String RunSQLReturnString(String runObj, String isNullasVal)
	{
		DataTable dt = RunSQLReturnTable(runObj);
		if (dt.Rows.size() == 0)
		{
			return isNullasVal;
		}

		return dt.Rows.get(0).getValue(0).toString();
	}
	/** 
	 运行SQL返回datatable
	 
	 param sql
	 @return 
	*/
//	public final DataTable RunSQLReturnTable(String runObj, Paras ps)
//	{
//		try
//		{
//			switch (this.getDBSrcType())
//			{
//				case Localhost: //如果是本机，直接在本机上执行.
//					return DBAccess.RunSQLReturnTable(runObj, ps);
//				case SQLServer: //如果是SQLServer.
//					SqlConnection connSQL = new SqlConnection(this.getConnString());
//					SqlDataAdapter ada = null;
//					SqlParameter myParameter = null;
//
//					try
//					{
//						connSQL.Open(); //打开.
//						ada = new SqlDataAdapter(runObj, connSQL);
//						ada.SelectCommand.CommandType = CommandType.Text;
//
//						// 加入参数
//						if (ps != null)
//						{
//							for (Para para : ps)
//							{
//								myParameter = new SqlParameter(para.ParaName, para.val);
//								myParameter.Size = para.Size;
//								ada.SelectCommand.Parameters.Add(myParameter);
//							}
//						}
//
//						DataTable oratb = new DataTable("otb");
//						ada.Fill(oratb);
//						ada.Dispose();
//						connSQL.Close();
//						return oratb;
//					}
//					catch (RuntimeException ex)
//					{
//						if (ada != null)
//						{
//							ada.Dispose();
//						}
//						if (connSQL.State == ConnectionState.Open)
//						{
//							connSQL.Close();
//						}
//						throw new RuntimeException("SQL=" + runObj + " Exception=" + ex.getMessage());
//					}
//				case Oracle:
//					OracleConnection oracleConn = new OracleConnection(getConnString());
//					OracleDataAdapter oracleAda = null;
//					OracleParameter myParameterOrcl = null;
//
//					try
//					{
//						oracleConn.Open();
//						oracleAda = new OracleDataAdapter(runObj, oracleConn);
//						oracleAda.SelectCommand.CommandType = CommandType.Text;
//
//						if (ps != null)
//						{
//							// 加入参数
//							for (Para para : ps)
//							{
//								myParameterOrcl = new OracleParameter(para.ParaName, para.val);
//								myParameterOrcl.Size = para.Size;
//								oracleAda.SelectCommand.Parameters.add(myParameterOrcl);
//							}
//						}
//
//						DataTable oracleTb = new DataTable("otb");
//						oracleAda.Fill(oracleTb);
//						oracleAda.close();
//						oracleConn.Close();
//						return oracleTb;
//					}
//					catch (RuntimeException ex)
//					{
//						if (oracleAda != null)
//						{
//							oracleAda.close();
//						}
//						if (oracleConn.State == ConnectionState.Open)
//						{
//							oracleConn.Close();
//						}
//						throw new RuntimeException("SQL=" + runObj + " Exception=" + ex.getMessage());
//					}
//				case MySQL:
//					MySqlConnection mysqlConn = new MySqlConnection(getConnString());
//					MySqlDataAdapter mysqlAda = null;
//					MySqlParameter myParameterMysql = null;
//
//					try
//					{
//						mysqlConn.Open();
//						mysqlAda = new MySqlDataAdapter(runObj, mysqlConn);
//						mysqlAda.SelectCommand.CommandType = CommandType.Text;
//
//						if (ps != null)
//						{
//							// 加入参数
//							for (Para para : ps)
//							{
//								myParameterMysql = new MySqlParameter(para.ParaName, para.val);
//								myParameterMysql.Size = para.Size;
//								mysqlAda.SelectCommand.Parameters.Add(myParameterMysql);
//							}
//						}
//
//						DataTable mysqlTb = new DataTable("otb");
//						mysqlAda.Fill(mysqlTb);
//						mysqlAda.Dispose();
//						mysqlConn.Close();
//						return mysqlTb;
//					}
//					catch (RuntimeException ex)
//					{
//						if (mysqlAda != null)
//						{
//							mysqlAda.Dispose();
//						}
//						if (mysqlConn.State == ConnectionState.Open)
//						{
//							mysqlConn.Close();
//						}
//						throw new RuntimeException("SQL=" + runObj + " Exception=" + ex.getMessage());
//					}
//
//				default:
//					break;
//			}
//			return null;
//		}
//		catch (RuntimeException ex)
//		{
//			throw new RuntimeException("err@从自定义数据源中获取数据失败，" + this.getNo() + " , " + this.getName() + " 异常信息:" + ex.getMessage());
//			Log.DebugWriteError(ex.getMessage());
//		}
//
//	}
//
//	public final DataTable RunSQLReturnTable(String sql, int startRecord, int recordCount)
//	{
//		switch (this.getDBSrcType())
//		{
//			case Localhost: //如果是本机，直接在本机上执行.
//				return DBAccess.RunSQLReturnTable(sql);
//			case SQLServer: //如果是SQLServer.
//				SqlConnection connSQL = new SqlConnection(this.getConnString());
//				SqlDataAdapter ada = null;
//				try
//				{
//					connSQL.Open(); //打开.
//					ada = new SqlDataAdapter(sql, connSQL);
//					ada.SelectCommand.CommandType = CommandType.Text;
//					DataTable oratb = new DataTable("otb");
//					ada.Fill(startRecord, recordCount, oratb);
//					ada.Dispose();
//					connSQL.Close();
//					return oratb;
//				}
//				catch (RuntimeException ex)
//				{
//					if (ada != null)
//					{
//						ada.Dispose();
//					}
//					if (connSQL.State == ConnectionState.Open)
//					{
//						connSQL.Close();
//					}
//					throw new RuntimeException("SQL=" + sql + " Exception=" + ex.getMessage());
//				}
//			case Oracle:
//				OracleConnection oracleConn = new OracleConnection(getConnString());
//				OracleDataAdapter oracleAda = null;
//
//				try
//				{
//					oracleConn.Open();
//					oracleAda = new OracleDataAdapter(sql, oracleConn);
//					oracleAda.SelectCommand.CommandType = CommandType.Text;
//					DataTable oracleTb = new DataTable("otb");
//					oracleAda.Fill(startRecord, recordCount, oracleTb);
//					oracleAda.close();
//					oracleConn.Close();
//					return oracleTb;
//				}
//				catch (RuntimeException ex)
//				{
//					if (oracleAda != null)
//					{
//						oracleAda.close();
//					}
//					if (oracleConn.State == ConnectionState.Open)
//					{
//						oracleConn.Close();
//					}
//					throw new RuntimeException("SQL=" + sql + " Exception=" + ex.getMessage());
//				}
//			case MySQL:
//				MySqlConnection mysqlConn = new MySqlConnection(getConnString());
//				MySqlDataAdapter mysqlAda = null;
//
//				try
//				{
//					mysqlConn.Open();
//					mysqlAda = new MySqlDataAdapter(sql, mysqlConn);
//					mysqlAda.SelectCommand.CommandType = CommandType.Text;
//					DataTable mysqlTb = new DataTable("otb");
//					mysqlAda.Fill(startRecord, recordCount, mysqlTb);
//					mysqlAda.Dispose();
//					mysqlConn.Close();
//					return mysqlTb;
//				}
//				catch (RuntimeException ex)
//				{
//					if (mysqlAda != null)
//					{
//						mysqlAda.Dispose();
//					}
//					if (mysqlConn.State == ConnectionState.Open)
//					{
//						mysqlConn.Close();
//					}
//					throw new RuntimeException("SQL=" + sql + " Exception=" + ex.getMessage());
//				}
//			//
//			//case Sys.DBSrcType.Informix:
//			//    IfxConnection ifxConn = new IfxConnection(ConnString);
//			//    IfxDataAdapter ifxAda = null;
//
//			//    try
//			//    {
//			//        ifxConn.Open();
//			//        ifxAda = new IfxDataAdapter(sql, ifxConn);
//			//        ifxAda.SelectCommand.CommandType = CommandType.Text;
//			//        DataTable ifxTb = new DataTable("otb");
//			//        ifxAda.Fill(startRecord, recordCount, ifxTb);
//			//        ifxAda.Dispose();
//			//        ifxConn.Close();
//			//        return ifxTb;
//			//    }
//			//    catch (Exception ex)
//			//    {
//			//        if (ifxAda != null)
//			//            ifxAda.Dispose();
//			//        if (ifxConn.State == ConnectionState.Open)
//			//            ifxConn.Close();
//			//        throw new Exception("SQL=" + sql + " Exception=" + ex.Message);
//			//    }
//			default:
//				break;
//		}
//		return null;
//	}

	/**
	 运行SQL

	 @param runObj
	 @return
	 */
	public final DataTable RunSQLReturnTable(String runObj)
	{
		return RunSQLReturnTable(runObj, new Paras());
	}
	/**
	 运行SQL返回datatable
	 @param runObj
	 @param ps
	 @return
	 */
	public final DataTable RunSQLReturnTable(String runObj, Paras ps)
	{
		return DBAccess.RunSQLReturnTable(runObj,ps);
	}

	/** 
	 判断数据源所在库中是否已经存在指定名称的对象【表/视图】
	 
	 param objName 表/视图 名称
	 @return 如果不存在，返回null，否则返回对象的类型：TABLE(表)、VIEW(视图)、PROCEDURE(存储过程，判断不完善)、OTHER(其他类型)
	*/
	public final String IsExistsObj(String objName)
	{
		String sql = "";
		DataTable dt = null;

		switch (this.getDBSrcType())
		{
			case Localhost:
				sql = GetIsExitsSQL(DBAccess.getAppCenterDBType(), objName, DBAccess.getAppCenterDBType().toString());
				dt = DBAccess.RunSQLReturnTable(sql);
				break;
			case SQLServer:
				sql = GetIsExitsSQL(DBType.MSSQL, objName, this.getDBName());
				dt = RunSQLReturnTable(sql);
				break;
			case Oracle:
				sql = GetIsExitsSQL(DBType.Oracle, objName, this.getDBName());
				dt = RunSQLReturnTable(sql);
				break;
			case MySQL:
				sql = GetIsExitsSQL(DBType.MySQL, objName, this.getDBName());
				dt = RunSQLReturnTable(sql);
				break;
			case Informix:
				sql = GetIsExitsSQL(DBType.Informix, objName, this.getDBName());
				dt = RunSQLReturnTable(sql);
				break;
			default:
				throw new RuntimeException("@未涉及的数据库类型。");
		}

		return dt.Rows.size() == 0 ? null : dt.Rows.get(0).getValue(0).toString();
	}

	/** 
	 获取判断数据库中是否存在指定名称的表/视图SQL语句
	 
	 param dbType 数据库类型
	 param objName 表/视图名称
	 param dbName 数据库名称
	 @return 
	*/
	public final String GetIsExitsSQL(DBType dbType, String objName, String dbName)
	{
		switch (dbType)
		{
			case MSSQL:
			case PostgreSQL:
			case UX:
				return String.format("SELECT (CASE s.xtype WHEN 'U' THEN 'TABLE' WHEN 'V' THEN 'VIEW' WHEN 'P' THEN 'PROCEDURE' ELSE 'OTHER' END) OTYPE FROM sysobjects s WHERE s.name = '%1$s'", objName);
			case Oracle:
				return String.format("SELECT uo.OBJECT_TYPE OTYPE FROM user_objects uo WHERE uo.OBJECT_NAME = '%1$s'", objName.toUpperCase());
			case MySQL:
				return String.format("SELECT (CASE t.TABLE_TYPE WHEN 'BASE TABLE' THEN 'TABLE' ELSE 'VIEW' END) OTYPE FROM information_schema.tables t WHERE t.TABLE_SCHEMA = '%2$s' AND t.TABLE_NAME = '%1$s'", objName, dbName);
			case Informix:
				return String.format("SELECT (CASE s.tabtype WHEN 'T' THEN 'TABLE' WHEN 'V' THEN 'VIEW' ELSE 'OTHER' END) OTYPE FROM systables s WHERE s.tabname = '%1$s'", objName);
			case DB2:
				return String.format("");
			case Access:
				return String.format("");
			default:
				throw new RuntimeException("@没有涉及的数据库类型。");
		}
	}

		///#endregion


		///#region 构造方法
	/** 
	 数据源
	*/
	public SFDBSrc() {
	}
	public SFDBSrc(String mypk) throws Exception {
		this.setNo(mypk);
		this.Retrieve();
	}
	/** 
	 EnMap
	*/
	@Override
	public bp.en.Map getEnMap()  {
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("Sys_SFDBSrc", "数据源");

		map.AddTBStringPK(SFDBSrcAttr.No, null, "数据源编号(必须是英文)", true, false, 1, 20, 20);
		map.AddTBString(SFDBSrcAttr.Name, null, "数据源名称", true, false, 0, 30, 20);

		map.AddDDLSysEnum(SFDBSrcAttr.DBSrcType, 0, "数据源类型", true, true, SFDBSrcAttr.DBSrcType, "@0=应用系统主数据库(默认)@1=SQLServer数据库@2=Oracle数据库@3=MySQL数据库@4=Informix数据库@50=Dubbo服务@100=WebService数据源");
		map.AddTBString(SFDBSrcAttr.DBName, null, "数据库名称/Oracle保持为空", true, false, 0, 30, 20);
		map.AddTBStringDoc(SFDBSrcAttr.ConnString, null, "连接串", true, false, true);
		String runPlant = bp.difference.SystemConfig.getRunOnPlant();
		if (runPlant.equals("CCFlow") == false && runPlant.equals("bp")==false)
		{
		   map.AddTBString(SFDBSrcAttr.UserID, null, "数据库登录用户ID", true, false, 0, 30, 20);
			map.AddTBString(SFDBSrcAttr.Password, null, "密码", true, false, 0, 30, 20);
			map.AddTBString(SFDBSrcAttr.IP, null, "IP地址/数据库实例名", true, false, 0, 500, 20);
		}

			//map.AddDDLSysEnum(SFDBSrcAttr.DBSrcType, 0, "数据源类型", true, true,
			//    SFDBSrcAttr.DBSrcType, "@0=应用系统主数据库@1=SQLServer@2=Oracle@3=MySQL@4=Infomix");

		RefMethod rm = new RefMethod();

		rm = new RefMethod();
		rm.Title = "测试连接";
		rm.ClassMethodName = this.toString() + ".DoConn";
		rm.refMethodType = RefMethodType.Func; // 仅仅是一个功能.
		map.AddRefMethod(rm);

		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion


		///#region 方法.
	/** 
	 连接字符串.
	*/
	public final String getConnString() throws Exception {
		switch (this.getDBSrcType())
		{
			case Localhost:
				return bp.difference.SystemConfig.getAppCenterDSN();
			default:
				return this.GetValStringByKey(SFDBSrcAttr.ConnString);
					//case Sys.DBSrcType.SQLServer:
					//    return "password=" + this.Password + ";persist security info=true;user id=" + this.UserID + ";initial catalog=" + this.DBName + ";data source=" + this.IP + ";timeout=999;multipleactiveresultsets=true";
					//case Sys.DBSrcType.Oracle:
					//    return "user id=" + this.UserID + ";data source=" + this.IP + ";password=" + this.Password + ";Max Pool Size=200";
					//case Sys.DBSrcType.MySQL:
					//    return "Data Source=" + this.IP + ";Persist Security info=True;Initial Catalog=" + this.DBName + ";User ID=" + this.UserID + ";Password=" + this.Password + ";";
					//case Sys.DBSrcType.Informix:
					//    return "Host=" + this.IP + "; Service=; Server=; Database=" + this.DBName + "; User id=" + this.UserID + "; Password=" + this.Password + "; ";  //Service为监听客户端连接的服务名，Server为数据库实例名，这两项没提供
					//case Sys.DBSrcType.PostgreSQL:
					//    return "Server=" + this.IP + ";Port=5432;Database=" + this.DBName + ";UserId=" + this.UserID + ";Password=" + this.Password + ";;Pooling=False;";
					//default:
					//    throw new Exception("@没有判断的类型.");
		}
	}
	/**
	 连接字符串.
	 * @throws Exception
	 */
	public final Connection getConnection() throws Exception
	{
		String url="";//数据库连接的url
		Connection con=null;
		switch (this.getDBSrcType())
		{

			case SQLServer:
				try{
					//加载MySql的驱动类
					Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver") ;
				}catch(ClassNotFoundException e){
					System.out.println("找不到驱动程序类 ，加载驱动失败！");
					e.printStackTrace() ;
				}
				url="jdbc:sqlserver://" + this.getIP()+"/"+this.getDBName()+";useLOBs=false";
				break;
			case Oracle:
				try{
					//加载MySql的驱动类
					Class.forName("oracle.jdbc.driver.OracleDriver") ;
				}catch(ClassNotFoundException e){
					System.out.println("找不到驱动程序类 ，加载驱动失败！");
					e.printStackTrace() ;
				}
				url="jdbc:oracle:thin:@" + this.getIP();
				break;
			case MySQL:
				try{
					//加载MySql的驱动类
					Class.forName("com.mysql.jdbc.Driver") ;
				}catch(ClassNotFoundException e){
					System.out.println("找不到驱动程序类 ，加载驱动失败！");
					e.printStackTrace() ;
				}
				url="jdbc:mysql://"+this.getIP()+"/"+this.getDBName()+"?useUnicode=true&characterEncoding=utf-8&useOldAliasMetadataBehavior=true&allowMultiQueries=true";
				break;
			case KingBaseR3:
			case KingBaseR6:
				try{
					//加载MySql的驱动类
					Class.forName("com.kingbase8.Driver") ;
				}catch(ClassNotFoundException e){
					System.out.println("找不到驱动程序类 ，加载驱动失败！");
					e.printStackTrace() ;
				}
				url="jdbc:kingbase8://"+this.getIP()+"/"+this.getDBName();
				break;
			default:
				throw new RuntimeException("@没有判断的类型.");
		}
		con = DriverManager.getConnection(url, this.getUserID(), this.getPassword());
		return con;
	}
	/** 
	 执行连接
	 
	 @return 
	*/
	public final String DoConn() throws Exception {

		if (this.getNo().equals("local"))
		{
			return "本地连接不需要测试是否连接成功.";
		}

		if (this.getDBSrcType() == DBSrcType.Localhost)
		{
			//throw new Exception("@在该系统中只能有一个本地连接.");
			return "@在该系统中只能有一个本地连接.";
		}

		String dsn = "";
		if (this.getDBSrcType() == DBSrcType.SQLServer)
		{
			try
			{
				//删除应用.
				try
				{
					DBAccess.RunSQL("Exec sp_droplinkedsrvlogin " + this.getNo() + ",Null ");
					DBAccess.RunSQL("Exec sp_dropserver " + this.getNo());
				}
				catch (java.lang.Exception e)
				{
				}

				//创建应用.
				String sql = "";
				sql += "sp_addlinkedserver @server='" + this.getNo() + "', @srvproduct='', @provider='SQLOLEDB', @datasrc='" + this.getIP() + "'";
				DBAccess.RunSQL(sql);

				//执行登录.
				sql = "";
				sql += " EXEC sp_addlinkedsrvlogin '" + this.getNo() + "','false', NULL, '" + this.getUserID() + "', '" + this.getPassword() + "'";
				DBAccess.RunSQL(sql);

				return "恭喜您，该(" + this.getName() + ")连接配置成功。";
			}
			catch (RuntimeException ex)
			{
				return ex.getMessage();
			}
		}

		if (this.getDBSrcType() == DBSrcType.Oracle)
		{
			try
			{

				return "恭喜您，该(" + this.getName() + ")连接配置成功。";
			}
			catch (RuntimeException ex)
			{
				return ex.getMessage();
			}
		}

		if (this.getDBSrcType() == DBSrcType.MySQL)
		{
			try
			{

				return "恭喜您，该(" + this.getName() + ")连接配置成功。";
			}
			catch (RuntimeException ex)
			{
				return ex.getMessage();
			}
		}

		return "没有涉及到的连接测试类型...";
	}
	/** 
	 获取所有数据表，不包括视图
	 
	 @return 
	*/
	public final DataTable GetAllTablesWithoutViews() throws Exception {
		StringBuilder sql = new StringBuilder();
		DBSrcType dbType = this.getDBSrcType();
		if (dbType == bp.sys.DBSrcType.Localhost)
		{
			switch (bp.difference.SystemConfig.getAppCenterDBType())
			{
				case MSSQL:
					dbType = bp.sys.DBSrcType.SQLServer;
					break;
				case Oracle:
					dbType = bp.sys.DBSrcType.Oracle;
					break;
				case MySQL:
					dbType = bp.sys.DBSrcType.MySQL;
					break;
				case Informix:
					dbType = bp.sys.DBSrcType.Informix;
					break;
				case PostgreSQL:
					dbType = bp.sys.DBSrcType.PostgreSQL;
					break;
				case UX:
					dbType = bp.sys.DBSrcType.UX;
					break;
				default:
					throw new RuntimeException("没有涉及到的连接测试类型...");
			}
		}

		switch (dbType)
		{
			case SQLServer:
				sql.append("SELECT NAME AS No," + "\r\n");
				sql.append("       NAME" + "\r\n");
				sql.append("FROM   sysobjects" + "\r\n");
				sql.append("WHERE  xtype = 'U'" + "\r\n");
				sql.append("ORDER BY" + "\r\n");
				sql.append("       Name" + "\r\n");
				break;
			case Oracle:
				sql.append("SELECT uo.OBJECT_NAME No," + "\r\n");
				sql.append("       uo.OBJECT_NAME Name" + "\r\n");
				sql.append("  FROM user_objects uo" + "\r\n");
				sql.append(" WHERE uo.OBJECT_TYPE = 'TABLE'" + "\r\n");
				sql.append(" ORDER BY uo.OBJECT_NAME" + "\r\n");
				break;
			case MySQL:
				sql.append("SELECT " + "\r\n");
				sql.append("    table_name No," + "\r\n");
				sql.append("    table_name Name" + "\r\n");
				sql.append("FROM" + "\r\n");
				sql.append("    information_schema.tables" + "\r\n");
				sql.append("WHERE" + "\r\n");
				sql.append(String.format("    table_schema = '%1$s'", this.getDBSrcType() == bp.sys.DBSrcType.Localhost ? SystemConfig.getAppCenterDBDatabase() :this.getDBName()) + "\r\n");
				sql.append("        AND table_type = 'BASE TABLE'" + "\r\n");
				sql.append("ORDER BY table_name;" + "\r\n");
				break;
			case Informix:
				sql.append("" + "\r\n");
				break;
			case PostgreSQL:
			case UX:
				sql.append("SELECT " + "\r\n");
				sql.append("    table_name No," + "\r\n");
				sql.append("    table_name Name" + "\r\n");
				sql.append("FROM" + "\r\n");
				sql.append("    information_schema.tables" + "\r\n");
				sql.append("WHERE" + "\r\n");
				sql.append(String.format("    table_schema = '%1$s'", this.getDBSrcType() == bp.sys.DBSrcType.Localhost ? SystemConfig.getAppCenterDBDatabase() :this.getDBName()) + "\r\n");
				sql.append("        AND table_type = 'BASE TABLE'" + "\r\n");
				sql.append("ORDER BY table_name;" + "\r\n");
				break;
			default:
				break;
		}

		DataTable allTables = null;
		if (this.getNo().equals("local"))
		{
			allTables = DBAccess.RunSQLReturnTable(sql.toString());
		}
		else
		{
			try
			{
				allTables = DBAccess.RunSQLReturnTable(sql.toString());
			}
			catch (RuntimeException ex)
			{
				throw new RuntimeException("@失败:" + ex.getMessage() );
			}
		}

		return allTables;
	}
	/** 
	 获得数据列表.
	 
	 @return 
	*/

	public final DataTable GetTables() throws Exception {
		return GetTables(false);
	}

//ORIGINAL LINE: public DataTable GetTables(bool isCutFlowTables = false)
	public final DataTable GetTables(boolean isCutFlowTables)
	{
		StringBuilder sql = new StringBuilder();
		sql.append(String.format("SELECT ss.SrcTable FROM Sys_SFTable ss WHERE ss.FK_SFDBSrc = '%1$s'", this.getNo()));

		DataTable allTablesExist = DBAccess.RunSQLReturnTable(sql.toString());

		sql.setLength(0);

		DBSrcType dbType = this.getDBSrcType();
		if (dbType == DBSrcType.Localhost)
		{
			switch (SystemConfig.getAppCenterDBType())
			{
				case MSSQL:
					dbType = DBSrcType.SQLServer;
					break;
				case Oracle:
					dbType = DBSrcType.Oracle;
					break;
				case MySQL:
					dbType = DBSrcType.MySQL;
					break;
				case Informix:
					dbType = DBSrcType.Informix;
					break;
				case PostgreSQL:
					dbType = DBSrcType.PostgreSQL;
					break;
				case KingBaseR3:
					dbType = DBSrcType.KingBaseR3;
					break;
				case KingBaseR6:
					dbType = DBSrcType.KingBaseR6;
					break;
				default:
					throw new RuntimeException("没有涉及到的连接测试类型...");
			}
		}

		switch (dbType)
		{
			case SQLServer:
				sql.append("SELECT NAME AS No," + "\r\n");
				sql.append("       [Name] = '[' + (CASE xtype WHEN 'U' THEN '表' ELSE '视图' END) + '] ' + " + "\r\n");
				sql.append("       NAME," + "\r\n");
				sql.append("       xtype" + "\r\n");
				sql.append("FROM   sysobjects" + "\r\n");
				sql.append("WHERE  (xtype = 'U' OR xtype = 'V')" + "\r\n");
				//   sql.AppendLine("       AND (NAME NOT LIKE 'ND%')");
				sql.append("       AND (NAME NOT LIKE 'Demo_%')" + "\r\n");
				sql.append("       AND (NAME NOT LIKE 'Sys_%')" + "\r\n");
				sql.append("       AND (NAME NOT LIKE 'WF_%')" + "\r\n");
				sql.append("       AND (NAME NOT LIKE 'GPM_%')" + "\r\n");
				sql.append("ORDER BY" + "\r\n");
				sql.append("       xtype," + "\r\n");
				sql.append("       NAME" + "\r\n");
				break;
			case Oracle:
			case KingBaseR3:
			case KingBaseR6:
				sql.append("SELECT uo.OBJECT_NAME AS \"No\"," + "\r\n");
				sql.append("       '[' || (CASE uo.OBJECT_TYPE" + "\r\n");
				sql.append("         WHEN 'TABLE' THEN" + "\r\n");
				sql.append("          '表'" + "\r\n");
				sql.append("         ELSE" + "\r\n");
				sql.append("          '视图'" + "\r\n");
				sql.append("       END) || '] ' || uo.OBJECT_NAME AS \"Name\"," + "\r\n");
				sql.append("       CASE uo.OBJECT_TYPE" + "\r\n");
				sql.append("         WHEN 'TABLE' THEN" + "\r\n");
				sql.append("          'U'" + "\r\n");
				sql.append("         ELSE" + "\r\n");
				sql.append("          'V'" + "\r\n");
				sql.append("       END AS xtype" + "\r\n");
				sql.append("  FROM user_objects uo" + "\r\n");
				sql.append(" WHERE (uo.OBJECT_TYPE = 'TABLE' OR uo.OBJECT_TYPE = 'VIEW')" + "\r\n");

				sql.append(" ORDER BY uo.OBJECT_TYPE, uo.OBJECT_NAME" + "\r\n");
				break;
			case MySQL:
				sql.append("SELECT " + "\r\n");
				sql.append("    table_name AS No," + "\r\n");
				sql.append("    CONCAT('['," + "\r\n");
				sql.append("            CASE table_type" + "\r\n");
				sql.append("                WHEN 'BASE TABLE' THEN '表'" + "\r\n");
				sql.append("                ELSE '视图'" + "\r\n");
				sql.append("            END," + "\r\n");
				sql.append("            '] '," + "\r\n");
				sql.append("            table_name) AS Name," + "\r\n");
				sql.append("    CASE table_type" + "\r\n");
				sql.append("        WHEN 'BASE TABLE' THEN 'U'" + "\r\n");
				sql.append("        ELSE 'V'" + "\r\n");
				sql.append("    END AS xtype" + "\r\n");
				sql.append("FROM" + "\r\n");
				sql.append("    information_schema.tables" + "\r\n");
				sql.append("WHERE" + "\r\n");
				sql.append(String.format("    table_schema = '%1$s'", this.getDBSrcType() == DBSrcType.Localhost ? SystemConfig.getAppCenterDBDatabase() :this.getDBName()) + "\r\n");
				sql.append("        AND (table_type = 'BASE TABLE'" + "\r\n");
				sql.append("        OR table_type = 'VIEW')" + "\r\n");
				//   sql.AppendLine("       AND (table_name NOT LIKE 'ND%'");
				sql.append("        AND table_name NOT LIKE 'Demo_%'" + "\r\n");
				sql.append("        AND table_name NOT LIKE 'Sys_%'" + "\r\n");
				sql.append("        AND table_name NOT LIKE 'WF_%'" + "\r\n");
				sql.append("        AND table_name NOT LIKE 'GPM_%'" + "\r\n");
				sql.append("ORDER BY table_type , table_name;" + "\r\n");
				break;
			case Informix:
				sql.append("" + "\r\n");
				break;
			case PostgreSQL:
				sql.append("SELECT " + "\r\n");
				sql.append("    table_name AS No," + "\r\n");
				sql.append("    CONCAT('['," + "\r\n");
				sql.append("            CASE table_type" + "\r\n");
				sql.append("                WHEN 'BASE TABLE' THEN '表'" + "\r\n");
				sql.append("                ELSE '视图'" + "\r\n");
				sql.append("            END," + "\r\n");
				sql.append("            '] '," + "\r\n");
				sql.append("            table_name) AS Name," + "\r\n");
				sql.append("    CASE table_type" + "\r\n");
				sql.append("        WHEN 'BASE TABLE' THEN 'U'" + "\r\n");
				sql.append("        ELSE 'V'" + "\r\n");
				sql.append("    END AS xtype" + "\r\n");
				sql.append("FROM" + "\r\n");
				sql.append("    information_schema.tables" + "\r\n");
				sql.append("WHERE" + "\r\n");
				sql.append(String.format("    table_schema = '%1$s'", this.getDBSrcType() == DBSrcType.Localhost ? SystemConfig.getAppCenterDBDatabase() :this.getDBName()) + "\r\n");
				sql.append("        AND (table_type = 'BASE TABLE'" + "\r\n");
				sql.append("        OR table_type = 'VIEW')" + "\r\n");
				//   sql.AppendLine("       AND (table_name NOT LIKE 'ND%'");
				sql.append("        AND table_name NOT LIKE 'Demo_%'" + "\r\n");
				sql.append("        AND table_name NOT LIKE 'Sys_%'" + "\r\n");
				sql.append("        AND table_name NOT LIKE 'WF_%'" + "\r\n");
				sql.append("        AND table_name NOT LIKE 'GPM_%'" + "\r\n");
				sql.append("ORDER BY table_type , table_name;" + "\r\n");
				break;
			default:
				break;
		}

		DataTable allTables = null;
		if (this.getNo().equals("local"))
		{
			allTables = DBAccess.RunSQLReturnTable(sql.toString());


			///把tables 的英文名称替换为中文.
			//把tables 的英文名称替换为中文.
			String mapDT = "SELECT PTable,Name FROM Sys_MapData ";
			DataTable myDT = DBAccess.RunSQLReturnTable(mapDT);
			for (DataRow myDR : allTables.Rows)
			{
				String no = myDR.getValue("No").toString();

				String name = null;
				for (DataRow dr : myDT.Rows)
				{
					String pTable = dr.getValue("PTable").toString();
					if (pTable.equals(no) == false)
					{
						continue;
					}

					name = dr.getValue("Name").toString();
					break;
				}
				if (name != null)
				{
					myDR.setValue("Name", myDR.getValue("Name").toString() + "-" + name);
				}
			}

			/// 把tables 的英文名称替换为中文.


		}
		else
		{
			try
			{
				allTables = DBAccess.RunSQLReturnTable(sql.toString());
			}
			catch (RuntimeException ex)
			{
				throw new RuntimeException("@失败:" + ex.getMessage() );
			}
		}

		//去除已经使用的表
		String filter = "";
		for (DataRow dr : allTablesExist.Rows)
		{
			filter += String.format("No='%1$s' OR ", dr.getValue(0));
		}

		if (!filter.equals(""))
		{
			List<DataRow>  deletedRows = allTables.select(rtrim(filter," OR "));
			for (DataRow dr : deletedRows)
			{
				allTables.Rows.remove(dr);
			}
		}

		//去掉系统表.
		if (isCutFlowTables == true)
		{
			DataTable dt = new DataTable();
			dt.Columns.Add("No", String.class);
			dt.Columns.Add("Name", String.class);

			for (DataRow dr : allTables.Rows)
			{
				String no = dr.getValue("No").toString();

				if (no.contains("WF_") || no.contains("Track") || no.contains("Sys_") || no.contains("Demo_"))
				{
					continue;
				}

				DataRow mydr = dt.NewRow();
				mydr.setValue("No", dr.getValue("No"));
				mydr.setValue("Name", dr.getValue("Name"));
				dt.Rows.add(mydr);
			}

			return dt;
		}

		return allTables;
	}
	/** 
	 获取数据库连接
	 
	 param dsn 连接字符串
	 @return 
	*/
//	private System.Data.Common.DbConnection GetConnection(String dsn)
//	{
//		System.Data.Common.DbConnection conn = null;
//
//		var dbType = this.getDBSrcType();
//		if (dbType == bp.sys.DBSrcType.Localhost)
//		{
//			switch (bp.difference.SystemConfig.getAppCenterDBType())
//			{
//				case MSSQL:
//					dbType = bp.sys.DBSrcType.SQLServer;
//					break;
//				case Oracle:
//					dbType = bp.sys.DBSrcType.Oracle;
//					break;
//				case MySQL:
//					dbType = bp.sys.DBSrcType.MySQL;
//					break;
//				case Informix:
//					dbType = bp.sys.DBSrcType.Informix;
//					break;
//				default:
//					throw new RuntimeException("没有涉及到的连接测试类型...");
//			}
//		}
//		this.setDBSrcType(dbType);
//		switch (dbType)
//		{
//			case SQLServer:
//				conn = new System.Data.SqlClient.SqlConnection(dsn);
//				break;
//			case Oracle:
//				//conn = new System.Data.OracleClient.OracleConnection(dsn);
//				conn = new OracleConnection(dsn);
//				break;
//			case MySQL:
//				conn = new MySql.Data.MySqlClient.MySqlConnection(dsn);
//				break;
//				// from Zhou 删除IBM
//				//case Sys.DBSrcType.Informix:
//				//    conn = new System.Data.OleDb.OleDbConnection(dsn);
//				//    break;
//		}
//		return conn;
//	}

//	private DataTable RunSQLReturnTable(String sql, System.Data.Common.DbConnection conn, String dsn, CommandType cmdType)
//	{
//		if (conn instanceof System.Data.SqlClient.SqlConnection)
//		{
//			return DBAccess.RunSQLReturnTable(sql, (System.Data.SqlClient.SqlConnection)conn, dsn, cmdType, null);
//		}
//
//		//if (conn is System.Data.OracleClient.OracleConnection)
//		//    return DBAccess.RunSQLReturnTable(sql, (System.Data.OracleClient.OracleConnection)conn, cmdType, dsn);
//		if (conn instanceof OracleConnection)
//		{
//			return DBAccess.RunSQLReturnTable(sql, (OracleConnection)conn, cmdType, dsn);
//		}
//
//		if (conn instanceof MySqlConnection)
//		{
//			var mySqlConn = (MySqlConnection)conn;
//			if (mySqlConn.State != ConnectionState.Open)
//			{
//				mySqlConn.Open();
//			}
//
//			var ada = new MySqlDataAdapter(sql, mySqlConn);
//			ada.SelectCommand.CommandType = CommandType.Text;
//			try
//			{
//				DataTable oratb = new DataTable("otb");
//				ada.Fill(oratb);
//				ada.Dispose();
//
//				conn.Close();
//				conn.Dispose();
//				return oratb;
//			}
//			catch (RuntimeException ex)
//			{
//				ada.Dispose();
//				conn.Close();
//				throw new RuntimeException("SQL=" + sql + " Exception=" + ex.getMessage());
//			}
//		}
//
//		throw new RuntimeException("没有涉及到的连接测试类型...");
//		return null;
//	}

	/** 
	 修改表/视图/列名称（不完善）
	 
	 param objType 修改对象的类型，TABLE(表)、VIEW(视图)、COLUMN(列)
	 param oldName 旧名称
	 param newName 新名称
	 param tableName 修改列名称时，列所属的表名称
	*/

	public final void Rename(String objType, String oldName, String newName) throws Exception {
		Rename(objType, oldName, newName, null);
	}


	public final void Rename(String objType, String oldName, String newName, String tableName) throws Exception {
		DBSrcType dbType = this.getDBSrcType();
		if (dbType == bp.sys.DBSrcType.Localhost)
		{
			switch (bp.difference.SystemConfig.getAppCenterDBType())
			{
				case MSSQL:
					dbType = bp.sys.DBSrcType.SQLServer;
					break;
				case Oracle:
					dbType = bp.sys.DBSrcType.Oracle;
					break;
				case MySQL:
					dbType = bp.sys.DBSrcType.MySQL;
					break;
				case Informix:
					dbType = bp.sys.DBSrcType.Informix;
					break;
				case KingBaseR3:
					dbType = bp.sys.DBSrcType.KingBaseR3;
					break;
				case KingBaseR6:
					dbType = bp.sys.DBSrcType.KingBaseR6;
					break;
				default:
					throw new RuntimeException("@没有涉及到的连接测试类型。");
			}
		}

		switch (dbType)
		{
			case SQLServer:
				if (objType.toLowerCase().equals("column"))
				{
					RunSQL(String.format("EXEC SP_RENAME '%1$s', '%2$s', 'COLUMN'", oldName, newName));
				}
				else
				{
					RunSQL(String.format("EXEC SP_RENAME '%1$s', '%2$s'", oldName, newName));
				}
				break;
			case Oracle:
			case KingBaseR3:
			case KingBaseR6:
				if (objType.toLowerCase().equals("column"))
				{
					RunSQL(String.format("ALTER TABLE %1$s RENAME COLUMN %2$s TO %3$s", tableName, oldName, newName));
				}
				else if (objType.toLowerCase().equals("table"))
				{
					RunSQL(String.format("ALTER TABLE %1$s RENAME TO %2$s", oldName, newName));
				}
				else if (objType.toLowerCase().equals("view"))
				{
					RunSQL(String.format("RENAME %1$s TO %2$s", oldName, newName));
				}
				else
				{
					throw new RuntimeException("@未涉及到的Oracle数据库改名逻辑。");
				}
				break;
			case MySQL:
				if (objType.toLowerCase().equals("column"))
				{
					String sql = String.format("SELECT c.COLUMN_TYPE FROM information_schema.columns c WHERE c.TABLE_SCHEMA = '%1$s' AND c.TABLE_NAME = '%2$s' AND c.COLUMN_NAME = '%3$s'", this.getDBName(), tableName, oldName);

					DataTable dt = RunSQLReturnTable(sql);
					if (dt.Rows.size() > 0)
					{
						RunSQL(String.format("ALTER TABLE %1$s CHANGE COLUMN %2$s %3$s %4$s", tableName, oldName, newName, dt.Rows.get(0).getValue(0)));
					}
				}
				else if (objType.toLowerCase().equals("table"))
				{
					RunSQL(String.format("ALTER TABLE `%1$s`.`%2$s` RENAME `%1$s`.`%3$s`", this.getDBName(), oldName, newName));
				}
				else if (objType.toLowerCase().equals("view"))
				{
					String sql = String.format("SELECT t.VIEW_DEFINITION FROM information_schema.views t WHERE t.TABLE_SCHEMA = '%1$s' AND t.TABLE_NAME = '%2$s'", this.getDBName(), oldName);

					DataTable dt = RunSQLReturnTable(sql);
					if (dt.Rows.size() == 0)
					{
						RunSQL("DROP VIEW " + oldName);
					}
					else
					{
						RunSQL(String.format("CREATE VIEW %1$s AS %2$s", newName, dt.Rows.get(0).getValue(0)));
						RunSQL("DROP VIEW " + oldName);
					}
				}
				else
				{
					throw new RuntimeException("@未涉及到的Oracle数据库改名逻辑。");
				}
				break;
			case Informix:

				break;
			default:
				throw new RuntimeException("@没有涉及到的数据库类型。");
		}
	}
	/** 
	 获取判断指定表达式如果为空，则返回指定值的SQL表达式
	 <p>注：目前只对MSSQL/ORACLE/MYSQL三种数据库做兼容</p>
	 <p>added by liuxc,2017-03-07</p>
	 
	 param expression 要判断的表达式，在SQL中的写法
	 param isNullBack 判断的表达式为NULL，返回值的表达式，在SQL中的写法
	 @return 
	*/
	public final String GetIsNullInSQL(String expression, String isNullBack)
	{
		DBSrcType dbType = this.getDBSrcType();
		if (dbType == DBSrcType.Localhost)
		{
			switch (SystemConfig.getAppCenterDBType())
			{
				case MSSQL:
					dbType = DBSrcType.SQLServer;
					break;
				case Oracle:
					dbType = DBSrcType.Oracle;
					break;
				case MySQL:
					dbType = DBSrcType.MySQL;
					break;
				case Informix:
					dbType = DBSrcType.Informix;
					break;
				case KingBaseR3:
					dbType = DBSrcType.KingBaseR3;
					break;
				case KingBaseR6:
					dbType = DBSrcType.KingBaseR6;
					break;
				default:
					throw new RuntimeException("没有涉及到的连接测试类型...");
			}
		}
		switch (dbType)
		{
			case SQLServer:
				return " ISNULL(" + expression + "," + isNullBack + ")";
			case Oracle:
				return " NVL(" + expression + "," + isNullBack + ")";
			case MySQL:
				return " IFNULL(" + expression + "," + isNullBack + ")";
			case PostgreSQL:
				return " COALESCE(" + expression + "," + isNullBack + ")";
			case KingBaseR3:
			case KingBaseR6:
				return " ISNULL(" + expression + "," + isNullBack + ")";
			default:
				throw new RuntimeException("GetIsNullInSQL未涉及的数据库类型");
		}
	}

	/** 
	 获取表的字段信息
	 
	 param tableName 表/视图名称
	 @return 有四个列 No,Name,DBType,DBLength 分别标识  列的字段名，列描述，类型，长度。
	*/
	public final DataTable GetColumns(String tableName)
	{
		//SqlServer数据库
		StringBuilder sql = new StringBuilder();

		DBSrcType dbType = this.getDBSrcType();
		if (dbType ==DBSrcType.Localhost)
		{
			switch (SystemConfig.getAppCenterDBType())
			{
				case MSSQL:
					dbType = DBSrcType.SQLServer;
					break;
				case Oracle:
					dbType = DBSrcType.Oracle;
					break;
				case MySQL:
					dbType = DBSrcType.MySQL;
					break;
				case Informix:
					dbType = DBSrcType.Informix;
					break;
				case KingBaseR3:
					dbType = DBSrcType.KingBaseR3;
					break;
				case KingBaseR6:
					dbType = DBSrcType.KingBaseR6;
					break;
				default:
					throw new RuntimeException("没有涉及到的连接测试类型...");
			}
		}

		switch (dbType)
		{
			case SQLServer:
				sql.append("SELECT sc.name as No," + "\r\n");
				sql.append("       st.name AS [DBType]," + "\r\n");
				sql.append("       (" + "\r\n");
				sql.append("           CASE " + "\r\n");
				sql.append("                WHEN st.name = 'nchar' OR st.name = 'nvarchar' THEN sc.length / 2" + "\r\n");
				sql.append("                ELSE sc.length" + "\r\n");
				sql.append("           END" + "\r\n");
				sql.append("       ) AS DBLength," + "\r\n");
				sql.append("       sc.colid," + "\r\n");
				sql.append(String.format("       %1$s AS [Name]", GetIsNullInSQL("ep.[value]", "''")) + "\r\n");
				sql.append("FROM   dbo.syscolumns sc" + "\r\n");
				sql.append("       INNER JOIN dbo.systypes st" + "\r\n");
				sql.append("            ON  sc.xtype = st.xusertype" + "\r\n");
				sql.append("       LEFT OUTER JOIN sys.extended_properties ep" + "\r\n");
				sql.append("            ON  sc.id = ep.major_id" + "\r\n");
				sql.append("            AND sc.colid = ep.minor_id" + "\r\n");
				sql.append("            AND ep.name = 'MS_Description'" + "\r\n");
				sql.append(String.format("WHERE  sc.id = OBJECT_ID('dbo.%1$s')", tableName) + "\r\n");
				break;
			case Oracle:
			case KingBaseR3:
			case KingBaseR6:
				sql.append("SELECT utc.COLUMN_NAME AS \"No\"," + "\r\n");
				sql.append("       utc.DATA_TYPE   AS \"DBType\"," + "\r\n");
				sql.append("       utc.CHAR_LENGTH AS \"DBLength\"," + "\r\n");
				sql.append("       utc.COLUMN_ID   AS \"colid\"," + "\r\n");
				sql.append( GetIsNullInSQL("ucc.comments", "''") +" AS \"Name\"" + "\r\n");
				sql.append("  FROM user_tab_cols utc" + "\r\n");
				sql.append("  LEFT JOIN user_col_comments ucc" + "\r\n");
				sql.append("    ON ucc.table_name = utc.TABLE_NAME" + "\r\n");
				sql.append("   AND ucc.column_name = utc.COLUMN_NAME" + "\r\n");
				sql.append(String.format(" WHERE utc.TABLE_NAME = '%1$s'", tableName.toUpperCase()) + "\r\n");
				sql.append(" ORDER BY utc.COLUMN_ID ASC" + "\r\n");

				break;
			case MySQL:
				sql.append("SELECT " + "\r\n");
				sql.append("    column_name AS 'No'," + "\r\n");
				sql.append("    data_type AS 'DBType'," + "\r\n");
				sql.append(String.format("    %1$s AS DBLength,", GetIsNullInSQL("character_maximum_length", "numeric_precision")) + "\r\n");
				sql.append("    ordinal_position AS colid," + "\r\n");
				sql.append("    column_comment AS 'Name'" + "\r\n");
				sql.append("FROM" + "\r\n");
				sql.append("    information_schema.columns" + "\r\n");
				sql.append("WHERE" + "\r\n");
				sql.append(String.format("    table_schema = '%1$s'", this.getDBSrcType() == DBSrcType.Localhost ?  SystemConfig.getAppCenterDBDatabase() :this.getDBName()) + "\r\n");
				sql.append(String.format("        AND table_name = '%1$s';", tableName) + "\r\n");
				break;
			case Informix:
				break;
			default:
				throw new RuntimeException("没有涉及到的连接测试类型...");
		}

		DataTable dt = null;
		if (this.getNo().equals("local") == true)
		{
			dt = DBAccess.RunSQLReturnTable(sql.toString());
			return dt;
		}

		return null;
	}

	@Override
	protected boolean beforeDelete() throws Exception {
		if (this.getNo().equals("local"))
		{
			throw new RuntimeException("@默认连接(local)不允许删除、更新.");
		}

		String str = "";
		MapDatas mds = new MapDatas();
		mds.Retrieve(MapDataAttr.DBSrc, this.getNo());
		if (mds.size() != 0)
		{
			str += "如下表单使用了该数据源，您不能删除它。";
			for (MapData md : mds.ToJavaList())
			{
				str += "@\t\n" + md.getNo() + " - " + md.getName();
			}
		}

		SFTables tabs = new SFTables();
		tabs.Retrieve(SFTableAttr.FK_SFDBSrc, this.getNo());
		if (tabs.size() != 0)
		{
			str += "如下 table 使用了该数据源，您不能删除它。";
			for (SFTable tab : tabs.ToJavaList())
			{
				str += "@\t\n" + tab.getNo() + " - " + tab.getName();
			}
		}

		if (!str.equals(""))
		{
			throw new RuntimeException("@删除数据源的时候检查，是否有引用，出现错误：" + str);
		}

		return super.beforeDelete();
	}
	@Override
	protected boolean beforeUpdate() throws Exception {
		if (this.getNo().equals("local"))
		{
			throw new RuntimeException("@默认连接(local)不允许删除、更新.");
		}
		return super.beforeUpdate();
	}
	//added by liuxc,2015-11-10,新建修改时，判断只能加一个本地主库数据源
	@Override
	protected boolean beforeUpdateInsertAction() throws Exception {
		if (!this.getNo().equals("local") && this.getDBSrcType() == bp.sys.DBSrcType.Localhost)
		{
			throw new RuntimeException("@在该系统中只能有一个本地连接，请选择其他数据源类型。");
		}

		//测试数据库连接.
		DoConn();

		return super.beforeUpdateInsertAction();
	}
	public static String rtrim(String str,String substr){
		int j=str.length()-1;
		for(;j>-1;j--){
			if(substr.indexOf(str.charAt(j))==-1){
				break;
			}
		}
		return str.substring(0, j+1);
	}

		///#endregion 方法.

}