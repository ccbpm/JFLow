package bp.sys;

import java.sql.*;
import java.util.List;

import bp.da.*;
import bp.difference.SystemConfig;
import bp.en.EntityNoName;
import bp.en.Map;
import bp.en.RefMethod;
import bp.en.RefMethodType;
import bp.tools.StringHelper;
import org.apache.commons.lang3.StringUtils;

/**
 数据源
 */
public class SFDBSrc extends EntityNoName
{

	///属性
	/**
	 标签
	 * @throws Exception
	 */
	public final String getIcon() throws Exception
	{
		switch (this.getDBSrcType())
		{
			case Localhost:
				return "<img src='/WF/Img/DB.gif' />";
			default:
				return "";
		}
	}
	/**
	 是否是树形实体?
	 * @throws Exception
	 */
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
	public final void setPassword(String value) throws Exception
	{
		this.SetValByKey(SFDBSrcAttr.Password, value);
	}
	/**
	 数据库名称
	 */
	public final String getDBName() throws Exception
	{
		return this.GetValStringByKey(SFDBSrcAttr.DBName);
	}
	public final void setDBName(String value) throws Exception
	{
		this.SetValByKey(SFDBSrcAttr.DBName, value);
	}
	/**
	 数据库类型
	 */
	public final DBSrcType getDBSrcType() throws Exception
	{
		return DBSrcType.forValue(this.GetValIntByKey(SFDBSrcAttr.DBSrcType));
	}
	public final void setDBSrcType(DBSrcType value) throws Exception
	{
		this.SetValByKey(SFDBSrcAttr.DBSrcType, value.getValue());
	}
	/**
	 IP地址
	 */
	public final String getIP() throws Exception
	{
		return this.GetValStringByKey(SFDBSrcAttr.IP);
	}
	public final void setIP(String value) throws Exception
	{
		this.SetValByKey(SFDBSrcAttr.IP, value);
	}
	/**
	 数据库类型
	 * @throws Exception
	 */
	public final DBType getHisDBType() throws Exception
	{
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
			case KingBase:
				return DBType.KingBase;
			default:
				throw new RuntimeException("err@HisDBType没有判断的数据库类型.");
		}
	}

	///


	///数据库访问方法
	/**
	 运行SQL返回数值

	 @param sql 一行一列的SQL
	 @param isNullAsVal 如果为空，就返回制定的值.
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
	/**
	 运行SQL

	 @param sql
	 @return
	  * @throws Exception
	 */
	public final int RunSQL(String sql) throws Exception
	{
		int i =0;
		Connection conn = null;
		Statement stmt = null;
		switch(this.getDBSrcType()){
			case Localhost:
				return DBAccess.RunSQL(sql);
			case Oracle:
			case KingBase:
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
	 获取SQLServer链接服务器的表/视图名，根据链接服务器的命名规则组合

	 @param objName 表/视图名称
	 @return
	  * @throws Exception
	 */
	public final String GetLinkedServerObjName(String objName) throws Exception
	{
		//目前还只是考虑到SqlServer数据库中建立链接服务器的功能，其他数据库还没有考虑
		//Oracle中有DBLink功能，但具体还没有研究；MySQL中的Federated引擎功能还不完善，貌似只能增加mysql的外链数据库，且效率可能不大好也
		switch (this.getDBSrcType())
		{
			case Localhost:
				if (DBAccess.getAppCenterDBType() != DBType.MSSQL)
				{
					throw new RuntimeException("目前只支持CCFlow主数据库为SqlServer的模式，其他数据库类型暂不支持建立数据源。");
				}

				return objName;
			case SQLServer:
				return String.format("%1$s.%2$s.dbo.%3$s", this.getNo(), this.getDBName(), objName);
			case Oracle:
				return String.format("%1$s..%2$s.%3$s", this.getNo(), this.getUserID().toUpperCase(), objName.toUpperCase());
			case MySQL:
				return String.format("OPENQUERY(%1$s,'SELECT * FROM %2$s')", this.getNo(), objName);
			case Informix:
				return String.format("OPENQUERY(%1$s,'SELECT * FROM %2$s')", this.getNo(), objName);
			default:
				throw new RuntimeException("@未涉及的数据库类型。");
		}
	}

	/**
	 判断数据源所在库中是否已经存在指定名称的对象【表/视图】

	 @param objName 表/视图 名称
	 @return 如果不存在，返回null，否则返回对象的类型：TABLE(表)、VIEW(视图)、PROCEDURE(存储过程，判断不完善)、OTHER(其他类型)
	  * @throws Exception
	 */
	public final String IsExistsObj(String objName) throws Exception
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
			case KingBase:
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

	 @param dbType 数据库类型
	 @param objName 表/视图名称
	 @param dbName 数据库名称
	 @return
	 */
	public final String GetIsExitsSQL(DBType dbType, String objName, String dbName)
	{
		switch (dbType)
		{
			case MSSQL:
			case PostgreSQL:
				return String.format("SELECT (CASE s.xtype WHEN 'U' THEN 'TABLE' WHEN 'V' THEN 'VIEW' WHEN 'P' THEN 'PROCEDURE' ELSE 'OTHER' END) OTYPE FROM sysobjects s WHERE s.setName( '%1$s'", objName);
			case Oracle:
			case KingBase:
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

	///


	///构造方法
	/**
	 数据源
	 */
	public SFDBSrc()
	{
	}
	public SFDBSrc(String mypk) throws Exception
	{
		this.setNo(mypk);
		this.Retrieve();
	}
	/**
	 EnMap
	 */
	@Override
	public Map getEnMap() throws Exception
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("Sys_SFDBSrc", "数据源");

		map.AddTBStringPK(SFDBSrcAttr.No, null, "数据源编号(必须是英文)", true, false, 1, 20, 20);
		map.AddTBString(SFDBSrcAttr.Name, null, "数据源名称", true, false, 0, 30, 20);

		map.AddDDLSysEnum(SFDBSrcAttr.DBSrcType, 0, "数据源类型", true, true, SFDBSrcAttr.DBSrcType, "@0=应用系统主数据库(默认)@1=SQLServer数据库@2=Oracle数据库@3=MySQL数据库@4=Informix数据库@8=KingBase数据库@50=Dubbo服务@100=WebService数据源");

		map.AddTBString(SFDBSrcAttr.UserID, null, "数据库登录用户ID", true, false, 0, 30, 20);
		map.AddTBString(SFDBSrcAttr.Password, null, "数据库登录用户密码", true, false, 0, 30, 20);
		map.AddTBString(SFDBSrcAttr.IP, null, "IP地址/数据库实例名", true, false, 0, 500, 20);
		map.AddTBString(SFDBSrcAttr.DBName, null, "数据库名称/Oracle保持为空", true, false, 0, 30, 20);

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

	///


	///方法.
	/**
	 连接字符串.
	 * @throws Exception
	 */
	public final String getConnString() throws Exception
	{
		switch (this.getDBSrcType())
		{
			case Localhost:
				return SystemConfig.getAppCenterDSN();
			case SQLServer:
				return "password=" + this.getPassword() + ";persist security info=true;user id=" + this.getUserID() + ";initial catalog=" + this.getDBName() + ";data source=" + this.getIP() + ";timeout=999;multipleactiveresultsets=true";
			case Oracle:
				return "user id=" + this.getUserID() + ";data source=" + this.getIP() + ";password=" + this.getPassword() + ";Max Pool Size=200";
			case MySQL:
				return "Data Source=" + this.getIP() + ";Persist Security info=True;Initial Catalog=" + this.getDBName() + ";User ID=" + this.getUserID() + ";Password=" + this.getPassword() + ";";
			case Informix:
				return "Host=" + this.getIP() + "; Service=; Server=; Database=" + this.getDBName() + "; User id=" + this.getUserID() + "; Password=" + this.getPassword() + "; "; //Service为监听客户端连接的服务名，Server为数据库实例名，这两项没提供
			case PostgreSQL:
				return "Server=" + this.getIP() + ";Port=5432;Database=" + this.getDBName() + ";UserId=" + this.getUserID() + ";Password=" + this.getPassword() + ";;Pooling=False;";
			default:
				throw new RuntimeException("@没有判断的类型.");
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
			case KingBase:
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
	  * @throws Exception
	 */
	public final String DoConn() throws Exception
	{
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
	  * @throws Exception
	 */
	public final DataTable GetAllTablesWithoutViews() throws Exception
	{
		StringBuilder sql = new StringBuilder();
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
				case KingBase:
					dbType = DBSrcType.KingBase;
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
				sql.append(String.format("    table_schema = '%1$s'", this.getDBSrcType() == DBSrcType.Localhost ? SystemConfig.getAppCenterDBDatabase() :this.getDBName()) + "\r\n");
				sql.append("        AND table_type = 'BASE TABLE'" + "\r\n");
				sql.append("ORDER BY table_name;" + "\r\n");
				break;
			case Informix:
				sql.append("" + "\r\n");
				break;
			case PostgreSQL:
				sql.append("SELECT " + "\r\n");
				sql.append("    table_name No," + "\r\n");
				sql.append("    table_name Name" + "\r\n");
				sql.append("FROM" + "\r\n");
				sql.append("    information_schema.tables" + "\r\n");
				sql.append("WHERE" + "\r\n");
				sql.append(String.format("    table_schema = '%1$s'", this.getDBSrcType() == DBSrcType.Localhost ? SystemConfig.getAppCenterDBDatabase() :this.getDBName()) + "\r\n");
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
	  * @throws Exception
	 */

	public final DataTable GetTables() throws Exception
	{
		return GetTables(false);
	}


	public final DataTable GetTables(boolean isCutFlowTables) throws Exception
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
				case KingBase:
					dbType = DBSrcType.KingBase;
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
			case KingBase:
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

	public static String rtrim(String str,String substr){
		int j=str.length()-1;
		for(;j>-1;j--){
			if(substr.indexOf(str.charAt(j))==-1){
				break;
			}
		}
		return str.substring(0, j+1);
	}

	/**
	 获取连接字符串
	 <p></p>
	 <p>added by liuxc,2015-6-9</p>

	 @return
	  * @throws Exception
	 */
	private String GetDSN() throws Exception
	{
		String dsn = "";

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
				case KingBase:
					dbType = DBSrcType.KingBase;
					break;
				default:
					throw new RuntimeException("没有涉及到的连接测试类型...");
			}
		}

		switch (dbType)
		{
			case SQLServer:
				dsn = "Password=" + this.getPassword() + ";Persist Security Info=True;User ID=" + this.getUserID() + ";Initial Catalog=" + this.getDBName() + ";Data Source=" + this.getIP() + ";Timeout=999;MultipleActiveResultSets=true";
				break;
			case Oracle:
				dsn = "user id=" + this.getUserID() + ";data source=" + this.getIP() + ";password=" + this.getPassword() + ";Max Pool Size=200";
				break;
			case MySQL:
				dsn = "Data Source=" + this.getIP() + ";Persist Security info=True;Initial Catalog=" + this.getDBName() + ";User ID=" + this.getUserID() + ";Password=" + this.getPassword() + ";";
				break;
			case Informix:
				dsn = "Provider=Ifxoledbc;Data Source=" + this.getDBName() + "@" + this.getIP() + ";User ID=" + this.getUserID() + ";Password=" + this.getPassword() + ";";
				break;
			default:
				throw new RuntimeException("没有涉及到的连接测试类型...");
		}
		return dsn;
	}
	/**
	 获取数据库连接

	 @param dsn 连接字符串
	 @return
	 */
	/*private System.Data.Common.DbConnection GetConnection(String dsn)
	{
		System.Data.Common.DbConnection conn = null;

		bp.DBSrcType dbType = this.getDBSrcType();
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
				default:
					throw new RuntimeException("没有涉及到的连接测试类型...");
			}
		}

		switch (dbType)
		{
			case SQLServer:
				conn = new System.Data.SqlClient.SqlConnection(dsn);
				break;
			case Oracle:
				conn = new OracleConnection(dsn);
				break;
			case MySQL:
				conn = new MySql.Data.MySqlClient.MySqlConnection(dsn);
				break;
			// from Zhou 删除IBM
			//case DBSrcType.Informix:
			//    conn = new System.Data.OleDb.OleDbConnection(dsn);
			//    break;
		}

		return conn;
	}*/

	/*private DataTable RunSQLReturnTable(String sql, System.Data.Common.DbConnection conn, String dsn, CommandType cmdType)
	{
		if (conn instanceof System.Data.SqlClient.SqlConnection)
		{
			return DBAccess.RunSQLReturnTable(sql, (System.Data.SqlClient.SqlConnection)conn, dsn, cmdType, null);
		}

		//if (conn is System.Data.OracleClient.OracleConnection)
		//    return DBAccess.RunSQLReturnTable(sql, (System.Data.OracleClient.OracleConnection)conn, cmdType, dsn);
		if (conn instanceof OracleConnection)
		{
			return DBAccess.RunSQLReturnTable(sql, (OracleConnection)conn, cmdType, dsn);
		}

		if (conn instanceof MySqlConnection)
		{
			MySqlConnection mySqlConn = (MySqlConnection)conn;
			if (mySqlConn.State != ConnectionState.Open)
			{
				mySqlConn.Open();
			}

			MySqlDataAdapter ada = new MySqlDataAdapter(sql, mySqlConn);
			ada.SelectCommand.CommandType = CommandType.Text;


			try
			{
				DataTable oratb = new DataTable("otb");
				ada.Fill(oratb);
				ada.Dispose();

				conn.Close();
				conn.Dispose();
				return oratb;
			}
			catch (RuntimeException ex)
			{
				ada.Dispose();
				conn.Close();
				throw new RuntimeException("SQL=" + sql + " Exception=" + ex.getMessage());
			}
		}

		throw new RuntimeException("没有涉及到的连接测试类型...");
		return null;
	}
*/
	/**
	 修改表/视图/列名称（不完善）

	 @param objType 修改对象的类型，TABLE(表)、VIEW(视图)、COLUMN(列)
	 @param oldName 旧名称
	 @param newName 新名称
	 @param tableName 修改列名称时，列所属的表名称
	  * @throws Exception
	 */

	public final void Rename(String objType, String oldName, String newName) throws Exception
	{
		Rename(objType, oldName, newName, null);
	}


	public final void Rename(String objType, String oldName, String newName, String tableName) throws Exception
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
				case KingBase:
					dbType = DBSrcType.KingBase;
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

	 @param expression 要判断的表达式，在SQL中的写法
	 @param isNullBack 判断的表达式为NULL，返回值的表达式，在SQL中的写法
	 @return
	  * @throws Exception
	 */
	public final String GetIsNullInSQL(String expression, String isNullBack) throws Exception
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
				case KingBase:
					dbType = DBSrcType.KingBase;
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
			case KingBase:
				return " ISNULL(" + expression + "," + isNullBack + ")";
			default:
				throw new RuntimeException("GetIsNullInSQL未涉及的数据库类型");
		}
	}

	/**
	 获取表的字段信息

	 @param tableName 表/视图名称
	 @return 有四个列 No,Name,DBType,DBLength 分别标识  列的字段名，列描述，类型，长度。
	  * @throws Exception
	 */
	public final DataTable GetColumns(String tableName) throws Exception
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
				case KingBase:
					dbType = DBSrcType.KingBase;
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
			case KingBase:
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
	protected boolean beforeDelete() throws Exception
	{
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
	protected boolean beforeUpdate() throws Exception
	{
		if (this.getNo().equals("local"))
		{
			throw new RuntimeException("@默认连接(local)不允许删除、更新.");
		}
		return super.beforeUpdate();
	}
	//added by liuxc,2015-11-10,新建修改时，判断只能加一个本地主库数据源
	@Override
	protected boolean beforeUpdateInsertAction() throws Exception
	{
		if (!this.getNo().equals("local") && this.getDBSrcType() == DBSrcType.Localhost)
		{
			throw new RuntimeException("@在该系统中只能有一个本地连接，请选择其他数据源类型。");
		}

		//测试数据库连接
		DoConn();

		return super.beforeUpdateInsertAction();
	}

	/// 方法.

}