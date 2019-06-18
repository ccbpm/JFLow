package BP.Sys;

import java.util.List;

import BP.DA.*;
import BP.En.*;

/** 
 数据源
*/
public class SFDBSrc extends EntityNoName
{
	public final String getIcon()
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
	 
	*/
	public final String getUserID()
	{
		return this.GetValStringByKey(SFDBSrcAttr.UserID);
	}
	public final void setUserID(String value)
	{
		this.SetValByKey(SFDBSrcAttr.UserID, value);
	}
	/** 
	 密码
	 
	*/
	public final String getPassword()
	{
		return this.GetValStringByKey(SFDBSrcAttr.Password);
	}
	public final void setPassword(String value)
	{
		this.SetValByKey(SFDBSrcAttr.Password, value);
	}
	/** 
	 数据库名称
	 
	*/
	public final String getDBName()
	{
		return this.GetValStringByKey(SFDBSrcAttr.DBName);
	}
	public final void setDBName(String value)
	{
		this.SetValByKey(SFDBSrcAttr.DBName, value);
	}
	/** 
	 数据库类型
	 
	*/
	public final DBSrcType getDBSrcType()
	{
		return DBSrcType.forValue(this.GetValIntByKey(SFDBSrcAttr.DBSrcType));
	}
	public final void setDBSrcType(DBSrcType value)
	{
		this.SetValByKey(SFDBSrcAttr.DBSrcType, value.getValue());
	}
	public final String getIP()
	{
		return this.GetValStringByKey(SFDBSrcAttr.IP);
	}
	public final void setIP(String value)
	{
		this.SetValByKey(SFDBSrcAttr.IP, value);
	}
	public final DBType getHisDBType()
	{
		switch (this.getDBSrcType())
		{
			case Localhost:
				return SystemConfig.getAppCenterDBType();
			case SQLServer:
				return DBType.MSSQL;
			case Oracle:
				return DBType.Oracle;
			case MySQL:
				return DBType.MySQL;
			case Informix:
				return DBType.Informix;
			default:
				throw new RuntimeException("@没有判断的数据库类型.");
		}
	}
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
		return Integer.parseInt(dt.Rows.get(0).get(0).toString());
	}
	/** 
	 运行SQL
	 @param sql
	 @return 
	*/
	public final int RunSQL(String sql)
	{
		return BP.DA.DBAccess.RunSQL(sql);
		/*int i = 0;
		switch (this.getDBSrcType())
		{
			case Localhost:
				return BP.DA.DBAccess.RunSQL(sql);
			case SQLServer:
				SqlConnection conn = new SqlConnection(this.getConnString());
				SqlCommand cmd = null;

				try
				{
					conn.Open();
					cmd = new SqlCommand(sql, conn);
					cmd.CommandType = CommandType.getText();
					i = cmd.ExecuteNonQuery();
					cmd.dispose();
					conn.Close();
					return i;
				}
				catch (RuntimeException ex)
				{
					if (conn.State == ConnectionState.Open)
					{
						conn.Close();
					}
					if (cmd != null)
					{
						cmd.dispose();
					}
					throw new RuntimeException("RunSQL 错误，SQL=" + sql);
				}
			case Oracle:
				OracleConnection connOra = new OracleConnection(this.getConnString());
				OracleCommand cmdOra = null;

				try
				{
					connOra.Open();
					cmdOra = new OracleCommand(sql, connOra);
					cmdOra.CommandType = CommandType.getText();
					i = cmdOra.ExecuteNonQuery();
					cmdOra.dispose();
					connOra.Close();
					return i;
				}
				catch (RuntimeException ex)
				{
					if (connOra.State == ConnectionState.Open)
					{
						connOra.Close();
					}
					if (cmdOra != null)
					{
						cmdOra.dispose();
					}
					throw new RuntimeException("RunSQL 错误，SQL=" + sql);
				}
			case MySQL:
				MySqlConnection connMySQL = new MySqlConnection(this.getConnString());
				MySqlCommand cmdMySQL = null;
				try
				{
					connMySQL.Open();
					cmdMySQL = new MySqlCommand(sql, connMySQL);
					cmdMySQL.CommandType = CommandType.getText();
					i = cmdMySQL.ExecuteNonQuery();
					cmdMySQL.dispose();
					connMySQL.Close();
					return i;
				}
				catch (RuntimeException ex)
				{
					if (connMySQL.State == ConnectionState.Open)
					{
						connMySQL.Close();
					}
					if (cmdMySQL != null)
					{
						cmdMySQL.dispose();
					}
					throw new RuntimeException("RunSQL 错误，SQL=" + sql);
				}
			case Informix:
				IfxConnection connIfx = new IfxConnection(this.getConnString());
				IfxCommand cmdIfx = null;
				try
				{
					connIfx.Open();
					cmdIfx = new IfxCommand(sql, connIfx);
					cmdIfx.setCommandType(CommandType.getText());
					i = cmdIfx.ExecuteNonQuery();
					cmdIfx.dispose();
					connIfx.Close();
					return i;
				}
				catch (RuntimeException ex)
				{
					if (connIfx.getState() == ConnectionState.Open)
					{
						connIfx.Close();
					}
					if (cmdIfx != null)
					{
						cmdIfx.dispose();
					}
					throw new RuntimeException("RunSQL 错误，SQL=" + sql);
				}
			default:
				throw new RuntimeException("@没有判断的支持的数据库类型.");
		}

		return 0;*/
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
	 @param sql
	 @return 
	*/
	public final DataTable RunSQLReturnTable(String runObj, Paras ps)
	{
		return DBAccess.RunSQLReturnTable(runObj,ps);
		/*switch (this.getDBSrcType())
		{
			case Localhost: //如果是本机，直接在本机上执行.
				return BP.DA.DBAccess.RunSQLReturnTable(runObj, ps);
			case SQLServer: //如果是SQLServer.
				SqlConnection connSQL = new SqlConnection(this.getConnString());
				SqlDataAdapter ada = null;
				SqlParameter myParameter = null;

				try
				{
					connSQL.Open(); //打开.
					ada = new SqlDataAdapter(runObj, connSQL);
					ada.SelectCommand.setCommandType(CommandType.getText());

					// 加入参数
					if (ps != null)
					{
						for (Para para : ps)
						{
							myParameter = new SqlParameter(para.ParaName, para.val);
							myParameter.Size = para.Size;
							ada.SelectCommand.getParameters().Add(myParameter);
						}
					}

					DataTable oratb = new DataTable("otb");
					ada.Fill(oratb);
					ada.dispose();
					connSQL.Close();
					return oratb;
				}
				catch (RuntimeException ex)
				{
					if (ada != null)
					{
						ada.dispose();
					}
					if (connSQL.State == ConnectionState.Open)
					{
						connSQL.Close();
					}
					throw new RuntimeException("SQL=" + runObj + " Exception=" + ex.getMessage());
				}
			case Oracle:
				OracleConnection oracleConn = new OracleConnection(getConnString());
				OracleDataAdapter oracleAda = null;
				OracleParameter myParameterOrcl = null;

				try
				{
					oracleConn.Open();
					oracleAda = new OracleDataAdapter(runObj, oracleConn);
					oracleAda.SelectCommand.setCommandType(CommandType.getText());

					if (ps != null)
					{
						// 加入参数
						for (Para para : ps)
						{
							myParameterOrcl = new OracleParameter(para.ParaName, para.val);
							myParameterOrcl.Size = para.Size;
							oracleAda.SelectCommand.getParameters().Add(myParameterOrcl);
						}
					}

					DataTable oracleTb = new DataTable("otb");
					oracleAda.Fill(oracleTb);
					oracleAda.dispose();
					oracleConn.Close();
					return oracleTb;
				}
				catch (RuntimeException ex)
				{
					if (oracleAda != null)
					{
						oracleAda.dispose();
					}
					if (oracleConn.State == ConnectionState.Open)
					{
						oracleConn.Close();
					}
					throw new RuntimeException("SQL=" + runObj + " Exception=" + ex.getMessage());
				}
			case MySQL:
				MySqlConnection mysqlConn = new MySqlConnection(getConnString());
				MySqlDataAdapter mysqlAda = null;
				MySqlParameter myParameterMysql = null;

				try
				{
					mysqlConn.Open();
					mysqlAda = new MySqlDataAdapter(runObj, mysqlConn);
					mysqlAda.SelectCommand.setCommandType(CommandType.getText());

					if (ps != null)
					{
						// 加入参数
						for (Para para : ps)
						{
							myParameterMysql = new MySqlParameter(para.ParaName, para.val);
							myParameterMysql.Size = para.Size;
							mysqlAda.SelectCommand.getParameters().Add(myParameterMysql);
						}
					}

					DataTable mysqlTb = new DataTable("otb");
					mysqlAda.Fill(mysqlTb);
					mysqlAda.dispose();
					mysqlConn.Close();
					return mysqlTb;
				}
				catch (RuntimeException ex)
				{
					if (mysqlAda != null)
					{
						mysqlAda.dispose();
					}
					if (mysqlConn.State == ConnectionState.Open)
					{
						mysqlConn.Close();
					}
					throw new RuntimeException("SQL=" + runObj + " Exception=" + ex.getMessage());
				}
			case Informix:
				IfxConnection ifxConn = new IfxConnection(getConnString());
				IfxDataAdapter ifxAda = null;
				IfxParameter myParameterIfx = null;

				try
				{
					ifxConn.Open();
					ifxAda = new IfxDataAdapter(runObj, ifxConn);
					ifxAda.getSelectCommand().setCommandType(CommandType.getText());

					if (ps != null)
					{
						// 加入参数
						for (Para para : ps)
						{
							myParameterIfx = new IfxParameter(para.ParaName, para.val);
							myParameterIfx.setSize(para.Size);
							ifxAda.getSelectCommand().getParameters().Add(myParameterIfx);
						}
					}

					DataTable ifxTb = new DataTable("otb");
					ifxAda.Fill(ifxTb);
					ifxAda.dispose();
					ifxConn.Close();
					return ifxTb;
				}
				catch (RuntimeException ex)
				{
					if (ifxAda != null)
					{
						ifxAda.dispose();
					}
					if (ifxConn.getState() == ConnectionState.Open)
					{
						ifxConn.Close();
					}
					throw new RuntimeException("SQL=" + runObj + " Exception=" + ex.getMessage());
				}
			default:
				break;
		}

		return null;*/
	}

	public final DataTable RunSQLReturnTable(String sql, int startRecord, int recordCount)
	{
		return DBAccess.RunSQLReturnTable(sql);
		/*switch (this.getDBSrcType())
		{
			case Localhost: //如果是本机，直接在本机上执行.
				return BP.DA.DBAccess.RunSQLReturnTable(sql);
			case SQLServer: //如果是SQLServer.
				SqlConnection connSQL = new SqlConnection(this.getConnString());
				SqlDataAdapter ada = null;
				try
				{
					connSQL.Open(); //打开.
					ada = new SqlDataAdapter(sql, connSQL);
					ada.SelectCommand.setCommandType(CommandType.getText());
					DataTable oratb = new DataTable("otb");
					ada.Fill(startRecord, recordCount, oratb);
					ada.dispose();
					connSQL.Close();
					return oratb;
				}
				catch (RuntimeException ex)
				{
					if (ada != null)
					{
						ada.dispose();
					}
					if (connSQL.State == ConnectionState.Open)
					{
						connSQL.Close();
					}
					throw new RuntimeException("SQL=" + sql + " Exception=" + ex.getMessage());
				}
			case Oracle:
				OracleConnection oracleConn = new OracleConnection(getConnString());
				OracleDataAdapter oracleAda = null;

				try
				{
					oracleConn.Open();
					oracleAda = new OracleDataAdapter(sql, oracleConn);
					oracleAda.SelectCommand.setCommandType(CommandType.getText());
					DataTable oracleTb = new DataTable("otb");
					oracleAda.Fill(startRecord, recordCount, oracleTb);
					oracleAda.dispose();
					oracleConn.Close();
					return oracleTb;
				}
				catch (RuntimeException ex)
				{
					if (oracleAda != null)
					{
						oracleAda.dispose();
					}
					if (oracleConn.State == ConnectionState.Open)
					{
						oracleConn.Close();
					}
					throw new RuntimeException("SQL=" + sql + " Exception=" + ex.getMessage());
				}
			case MySQL:
				MySqlConnection mysqlConn = new MySqlConnection(getConnString());
				MySqlDataAdapter mysqlAda = null;

				try
				{
					mysqlConn.Open();
					mysqlAda = new MySqlDataAdapter(sql, mysqlConn);
					mysqlAda.SelectCommand.setCommandType(CommandType.getText());
					DataTable mysqlTb = new DataTable("otb");
					mysqlAda.Fill(startRecord, recordCount, mysqlTb);
					mysqlAda.dispose();
					mysqlConn.Close();
					return mysqlTb;
				}
				catch (RuntimeException ex)
				{
					if (mysqlAda != null)
					{
						mysqlAda.dispose();
					}
					if (mysqlConn.State == ConnectionState.Open)
					{
						mysqlConn.Close();
					}
					throw new RuntimeException("SQL=" + sql + " Exception=" + ex.getMessage());
				}
			case Informix:
				IfxConnection ifxConn = new IfxConnection(getConnString());
				IfxDataAdapter ifxAda = null;

				try
				{
					ifxConn.Open();
					ifxAda = new IfxDataAdapter(sql, ifxConn);
					ifxAda.getSelectCommand().setCommandType(CommandType.getText());
					DataTable ifxTb = new DataTable("otb");
					ifxAda.Fill(startRecord, recordCount, ifxTb);
					ifxAda.dispose();
					ifxConn.Close();
					return ifxTb;
				}
				catch (RuntimeException ex)
				{
					if (ifxAda != null)
					{
						ifxAda.dispose();
					}
					if (ifxConn.getState() == ConnectionState.Open)
					{
						ifxConn.Close();
					}
					throw new RuntimeException("SQL=" + sql + " Exception=" + ex.getMessage());
				}
			default:
				break;
		}
		return null;*/
	}

	/** 
	 获取SQLServer链接服务器的表/视图名，根据链接服务器的命名规则组合
	 @param objName 表/视图名称
	 @return 
	*/
	public final String GetLinkedServerObjName(String objName)
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
	*/
	public final String IsExistsObj(String objName)
	{
		String sql = "";
		DataTable dt = null;

		switch (this.getDBSrcType())
		{
			case Localhost:
				sql = GetIsExitsSQL(DBAccess.getAppCenterDBType(), objName, DBAccess.getAppCenterDBType().toString());//DBAccess.getGetAppCenterDBConn().Database
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

		return dt.Rows.size() == 0 ? null : dt.Rows.get(0).get(0).toString();
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
				return String.format("SELECT (CASE s.xtype WHEN 'U' THEN 'TABLE' WHEN 'V' THEN 'VIEW' WHEN 'P' THEN 'PROCEDURE' ELSE 'OTHER' END) OTYPE FROM sysobjects s WHERE s.name = '%1$s'", objName);
			case Oracle:
				return String.format("SELECT uo.OBJECT_TYPE OTYPE FROM user_objects uo WHERE uo.OBJECT_NAME = '%1$s'", objName.toUpperCase());
			case MySQL:
				return String.format("SELECT (CASE t.TABLE_TYPE WHEN 'BASE TABLE' THEN 'TABLE' ELSE 'VIEW' END) OTYPE FROM information_schema.tables t WHERE t.TABLE_SCHEMA = '%2$s' AND t.TABLE_NAME = '%1$s'", objName, dbName);
			case Informix:
				return String.format("SELECT (CASE s.tabtype WHEN 'T' THEN 'TABLE' WHEN 'V' THEN 'VIEW' ELSE 'OTHER' END) OTYPE FROM systables s WHERE s.tabname = '%1$s'", objName);
			case PostgreSQL:
				return String.format("");
			case DB2:
				return String.format("");
			case Access:
				return String.format("");
			default:
				throw new RuntimeException("@没有涉及的数据库类型。");
		}
	}
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
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("Sys_SFDBSrc", "数据源");
		map.Java_SetDepositaryOfEntity(Depositary.None);
		map.Java_SetDepositaryOfMap(Depositary.Application);
		map.Java_SetEnType(EnType.Sys);

		map.AddTBStringPK(SFDBSrcAttr.No, null, "数据源编号(必须是英文)", true, false, 1, 20, 20);
		map.AddTBString(SFDBSrcAttr.Name, null, "数据源名称", true, false, 0, 30, 20);
		map.AddDDLSysEnum(SFDBSrcAttr.DBSrcType, 0, "数据源类型", true, true, SFDBSrcAttr.DBSrcType, "@0=应用系统主数据库(默认)@1=SQLServer数据库@2=Oracle数据库@3=MySQL数据库@4=Informix数据库@50=Dubbo服务@100=WebService数据源");
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

	/** 
	 是表还是视图？
	 @param tabelOrViewName
	 @return 
	*/
	public final boolean IsView(String tabelOrViewName)
	{
		String sql = "";
		switch (this.getHisDBType())
		{
			case Oracle:
				sql = "SELECT TABTYPE  FROM TAB WHERE UPPER(TNAME)=:v";
				DataTable oradt = DBAccess.RunSQLReturnTable(sql, "v", tabelOrViewName.toUpperCase());
				if (oradt.Rows.size() == 0)
				{
					throw new RuntimeException("@表不存在[" + tabelOrViewName + "]");
				}
				if (oradt.Rows.get(0).get(0).toString().toUpperCase().trim().equals("V"))
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
				if (dtw.Rows.size() == 0)
				{
					throw new RuntimeException("@表不存在[" + tabelOrViewName + "]");
				}
				if (dtw.Rows.get(0).get(0).toString().trim().equals("5"))
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
				if (dt1.Rows.size() == 0)
				{
					throw new RuntimeException("@表不存在[" + tabelOrViewName + "]");
				}

				if (dt1.Rows.get(0).get(0).toString().toUpperCase().trim().equals("V"))
				{
					return true;
				}
				else
				{
					return false;
				}
			case Informix:
				sql = "select tabtype from systables where tabname = '" + tabelOrViewName.toLowerCase() + "'";
				DataTable dtaa = DBAccess.RunSQLReturnTable(sql);
				if (dtaa.Rows.size() == 0)
				{
					throw new RuntimeException("@表不存在[" + tabelOrViewName + "]");
				}

				if (dtaa.Rows.get(0).get(0).toString().toUpperCase().trim().equals("V"))
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
				if (dt2.Rows.size() == 0)
				{
					throw new RuntimeException("@表不存在[" + tabelOrViewName + "]");
				}

				if (dt2.Rows.get(0).get(0).toString().toUpperCase().trim().equals("VIEW"))
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
	}

	/** 
	 连接字符串.
	*/
	public final String getConnString()
	{
		switch (this.getDBSrcType())
		{
			case Localhost:
				return BP.Sys.SystemConfig.getAppCenterDSN();
			case SQLServer:
				return "password=" + this.getPassword() + ";persist security info=true;user id=" + this.getUserID() + ";initial catalog=" + this.getDBName() + ";data source=" + this.getIP() + ";timeout=999;multipleactiveresultsets=true";
			case Oracle:
				return "user id=" + this.getUserID() + ";data source=" + this.getIP() + ";password=" + this.getPassword() + ";Max Pool Size=200";
			case MySQL:
				return "Data Source=" + this.getIP() + ";Persist Security info=True;Initial Catalog=" + this.getDBName() + ";User ID=" + this.getUserID() + ";Password=" + this.getPassword() + ";";
			case Informix:
				return "Host=" + this.getIP() + "; Service=; Server=; Database=" + this.getDBName() + "; User id=" + this.getUserID() + "; Password=" + this.getPassword() + "; "; //Service为监听客户端连接的服务名，Server为数据库实例名，这两项没提供
			default:
				throw new RuntimeException("@没有判断的类型.");
		}
	}
	/** 
	 执行连接
	 
	 @return 
	*/
	public final String DoConn()
	{
		if (this.getNo().equals("local"))
		{
			return "本地连接不需要测试是否连接成功.";
		}

		if (this.getDBSrcType() == BP.Sys.DBSrcType.Localhost)
			//throw new Exception("@在该系统中只能有一个本地连接.");
		{
			return "@在该系统中只能有一个本地连接.";
		}

		String dsn = "";
		if (this.getDBSrcType() == BP.Sys.DBSrcType.SQLServer)
		{
			try
			{
				/*System.Data.SqlClient.SqlConnection conn = new System.Data.SqlClient.SqlConnection();
				conn.ConnectionString = this.getConnString();
				conn.Open();
				conn.Close();*/

				//删除应用.
				try
				{
					BP.DA.DBAccess.RunSQL("Exec sp_droplinkedsrvlogin " + this.getNo() + ",Null ");
					BP.DA.DBAccess.RunSQL("Exec sp_dropserver " + this.getNo());
				}
				catch (java.lang.Exception e)
				{
				}

				//创建应用.
				String sql = "";
				sql += "sp_addlinkedserver @server='" + this.getNo() + "', @srvproduct='', @provider='SQLOLEDB', @datasrc='" + this.getIP() + "'";
				BP.DA.DBAccess.RunSQL(sql);

				//执行登录.
				sql = "";
				sql += " EXEC sp_addlinkedsrvlogin '" + this.getNo() + "','false', NULL, '" + this.getUserID() + "', '" + this.getPassword() + "'";
				BP.DA.DBAccess.RunSQL(sql);

				return "恭喜您，该(" + this.getName() + ")连接配置成功。";
			}
			catch (RuntimeException ex)
			{
				return ex.getMessage();
			}
		}

		if (this.getDBSrcType() == BP.Sys.DBSrcType.Oracle)
		{
			try
			{
				dsn = "user id=" + this.getUserID() + ";data source="
						+ this.getDBName() + ";password=" + this.getPassword()
						+ ";Max Pool Size=200";
				// System.Data.OracleClient.OracleConnection conn = new
				// System.Data.OracleClient.OracleConnection();
				// conn.ConnectionString = dsn;
				// conn.Open();
				// conn.Close();
				return "恭喜您，该(" + this.getName() + ")连接配置成功。";
			} catch (RuntimeException ex)
			{
				return ex.getMessage();
			}
		}

		if (this.getDBSrcType() == BP.Sys.DBSrcType.MySQL)
		{
			try
			{
				dsn = "Data Source=" + this.getIP()
						+ ";Persist Security info=True;Initial Catalog="
						+ this.getDBName() + ";User ID=" + this.getUserID()
						+ ";Password=" + this.getPassword() + ";";
				// MySql.Data.MySqlClient.MySqlConnection conn = new
				// MySql.Data.MySqlClient.MySqlConnection();
				// conn.ConnectionString = dsn;
				// conn.Open();
				// conn.Close();
				return "恭喜您，该(" + this.getName() + ")连接配置成功。";
			} catch (RuntimeException ex)
			{
				return ex.getMessage();
			}
		}

		/*if (this.getDBSrcType() == BP.Sys.DBSrcType.Informix)
		{
			try
			{
				IfxConnection conn = new IfxConnection();
				conn.setConnectionString(this.getConnString());
				conn.Open();
				conn.Close();
				return "恭喜您，该(" + this.getName() + ")连接配置成功。";
			}
			catch (RuntimeException ex)
			{
				return ex.getMessage();
			}
		}*/

		if (this.getDBSrcType() == BP.Sys.DBSrcType.WebServices)
		{
			String url = this.getIP() + (this.getIP().endsWith(".asmx") ? "?wsdl" :this.getIP().endsWith(".svc") ? "?singleWsdl" : "");

			try
			{
				/*HttpWebRequest myRequest = (HttpWebRequest)WebRequest.Create(url);
				myRequest.Method = "GET"; //设置提交方式可以为＂ｇｅｔ＂，＂ｈｅａｄ＂等
					
				myRequest.Timeout = 30000; //设置网页响应时间长度
					
				myRequest.AllowAutoRedirect = false; //是否允许自动重定向
				HttpWebResponse myResponse = (HttpWebResponse)myRequest.GetResponse();
				return myResponse.StatusCode == HttpStatusCode.OK ? "连接配置成功。" : "连接配置失败。"; //返回响应的状态
*/			}
			catch (RuntimeException ex)
			{
				return ex.getMessage();
			}
		}

		return "没有涉及到的连接测试类型...";
	}
	/** 
	 获得数据列表.
	 @return 
	*/
	public final DataTable GetTables(boolean isCutFlowTables)
	{
		StringBuilder sql = new StringBuilder();
		sql.append(String.format("SELECT ss.SrcTable FROM Sys_SFTable ss WHERE ss.FK_SFDBSrc = '%1$s'", this.getNo()));

		DataTable allTablesExist = DBAccess.RunSQLReturnTable(sql.toString());

		sql = new StringBuilder();

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
				case WebServices:
					dbType = DBSrcType.WebServices;
				default:
					throw new RuntimeException("没有涉及到的连接测试类型...");
			}
		}

		switch (dbType)
		{
			case SQLServer:
				sql.append("SELECT NAME AS No,");
				sql.append("       [Name] = '[' + (CASE xtype WHEN 'U' THEN '表' ELSE '视图' END) + '] ' + ");
				sql.append("       NAME,");
				sql.append("       xtype");
				sql.append(" FROM   sysobjects");
				sql.append(" WHERE  (xtype = 'U' OR xtype = 'V')");
				// sql.append("       AND (NAME NOT LIKE 'ND%')");
				sql.append("       AND (NAME NOT LIKE 'Demo_%')");
				sql.append("       AND (NAME NOT LIKE 'Sys_%')");
				sql.append("       AND (NAME NOT LIKE 'WF_%')");
				sql.append("       AND (NAME NOT LIKE 'GPM_%')");
				sql.append(" ORDER BY");
				sql.append("       xtype,");
				sql.append("       NAME");
				break;
			case Oracle:
				sql.append("SELECT uo.OBJECT_NAME AS No,");
				sql.append("       '[' || (CASE uo.OBJECT_TYPE");
				sql.append("         WHEN 'TABLE' THEN");
				sql.append("          '表'");
				sql.append("         ELSE");
				sql.append("          '视图'");
				sql.append("       END) || '] ' || uo.OBJECT_NAME AS Name,");
				sql.append("       CASE uo.OBJECT_TYPE");
				sql.append("         WHEN 'TABLE' THEN");
				sql.append("          'U'");
				sql.append("         ELSE");
				sql.append("          'V'");
				sql.append("       END AS xtype");
				sql.append("  FROM user_objects uo");
				sql.append(" WHERE (uo.OBJECT_TYPE = 'TABLE' OR uo.OBJECT_TYPE = 'VIEW')");
				// sql.append("   AND uo.OBJECT_NAME NOT LIKE 'ND%'");
				sql.append("   AND uo.OBJECT_NAME NOT LIKE 'Demo_%'");
				sql.append("   AND uo.OBJECT_NAME NOT LIKE 'Sys_%'");
				sql.append("   AND uo.OBJECT_NAME NOT LIKE 'WF_%'");
				sql.append("   AND uo.OBJECT_NAME NOT LIKE 'GPM_%'");
				sql.append(" ORDER BY uo.OBJECT_TYPE, uo.OBJECT_NAME");
				break;
			case MySQL:
				sql.append("SELECT ");
				sql.append("    table_name AS No,");
				sql.append("    CONCAT('[',");
				sql.append("            CASE table_type");
				sql.append("                WHEN 'BASE TABLE' THEN '表'");
				sql.append("                ELSE '视图'");
				sql.append("            END,");
				sql.append("            '] ',");
				sql.append("            table_name) AS Name,");
				sql.append("    CASE table_type");
				sql.append("        WHEN 'BASE TABLE' THEN 'U'");
				sql.append("        ELSE 'V'");
				sql.append("    END AS xtype");
				sql.append(" FROM");
				sql.append("    information_schema.tables");
				sql.append(" WHERE");
				sql.append(String.format("    table_schema = '%1$s'",
						SystemConfig.getAppCenterDBDatabase()));
				sql.append("        AND (table_type = 'BASE TABLE'");
				sql.append("        OR table_type = 'VIEW')");
				// sql.append("       AND (table_name NOT LIKE 'ND%'");
				sql.append("        AND table_name NOT LIKE 'Demo_%'");
				sql.append("        AND table_name NOT LIKE 'Sys_%'");
				sql.append("        AND table_name NOT LIKE 'WF_%'");
				sql.append("        AND table_name NOT LIKE 'GPM_%'");
				sql.append(" ORDER BY table_type , table_name;");
				break;
			case Informix:
				sql.append("");
				break;
			default:
				break;
		}

		DataTable allTables = null;
		if (this.getNo().equals("local"))
		{
			allTables = DBAccess.RunSQLReturnTable(sql.toString());
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
                        continue;

                    name = dr.getValue("Name").toString();
                    break;
                }
                if (name != null)
                    myDR.setValue("Name", myDR.getValue("Name").toString() + "-" + name);
            }
		}
		else
		{
			try
			{
				allTables = DBAccess.RunSQLReturnTable(sql.toString());
			} catch (RuntimeException ex)
			{
				throw new RuntimeException("@失败:" + ex.getMessage());
			}
		}

		//去除已经使用的表
		String filter = "";
		for (DataRow dr : allTablesExist.Rows)
		{
			filter += String.format("No='%1$s' OR ", dr.getValue(0));
		}

		if ( ! filter.equals(""))
		{

			List<DataRow> deletedRows = allTables.select(rtrim(filter," OR "));
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
					

//				DataRow mydr = dt.NewRow();
//				mydr.setValue("No", dr.getValue("No"));
//				mydr.setValue("Name", dr.getValue("Name"));
//				dt.Rows.add(mydr);
			}

			return dt;
		}
		return allTables;
	}
	public final DataTable GetTables()
	{
		StringBuilder sql = new StringBuilder();
		sql.append(String.format("SELECT ss.SrcTable FROM Sys_SFTable ss WHERE ss.FK_SFDBSrc = '%1$s'", this.getNo()));

		DataTable allTablesExist = DBAccess.RunSQLReturnTable(sql.toString());

		sql = new StringBuilder();

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
				case WebServices:
					dbType = DBSrcType.WebServices;
				default:
					throw new RuntimeException("没有涉及到的连接测试类型...");
			}
		}

		switch (dbType)
		{
			case SQLServer:
				sql.append("SELECT NAME AS No,");
				sql.append("       [Name] = '[' + (CASE xtype WHEN 'U' THEN '表' ELSE '视图' END) + '] ' + ");
				sql.append("       NAME,");
				sql.append("       xtype");
				sql.append(" FROM   sysobjects");
				sql.append(" WHERE  (xtype = 'U' OR xtype = 'V')");
				// sql.append("       AND (NAME NOT LIKE 'ND%')");
				sql.append("       AND (NAME NOT LIKE 'Demo_%')");
				sql.append("       AND (NAME NOT LIKE 'Sys_%')");
				sql.append("       AND (NAME NOT LIKE 'WF_%')");
				sql.append("       AND (NAME NOT LIKE 'GPM_%')");
				sql.append(" ORDER BY");
				sql.append("       xtype,");
				sql.append("       NAME");
				break;
			case Oracle:
				sql.append("SELECT uo.OBJECT_NAME AS No,");
				sql.append("       '[' || (CASE uo.OBJECT_TYPE");
				sql.append("         WHEN 'TABLE' THEN");
				sql.append("          '表'");
				sql.append("         ELSE");
				sql.append("          '视图'");
				sql.append("       END) || '] ' || uo.OBJECT_NAME AS Name,");
				sql.append("       CASE uo.OBJECT_TYPE");
				sql.append("         WHEN 'TABLE' THEN");
				sql.append("          'U'");
				sql.append("         ELSE");
				sql.append("          'V'");
				sql.append("       END AS xtype");
				sql.append("  FROM user_objects uo");
				sql.append(" WHERE (uo.OBJECT_TYPE = 'TABLE' OR uo.OBJECT_TYPE = 'VIEW')");
				// sql.append("   AND uo.OBJECT_NAME NOT LIKE 'ND%'");
				sql.append("   AND uo.OBJECT_NAME NOT LIKE 'Demo_%'");
				sql.append("   AND uo.OBJECT_NAME NOT LIKE 'Sys_%'");
				sql.append("   AND uo.OBJECT_NAME NOT LIKE 'WF_%'");
				sql.append("   AND uo.OBJECT_NAME NOT LIKE 'GPM_%'");
				sql.append(" ORDER BY uo.OBJECT_TYPE, uo.OBJECT_NAME");
				break;
			case MySQL:
				sql.append("SELECT ");
				sql.append("    table_name AS No,");
				sql.append("    CONCAT('[',");
				sql.append("            CASE table_type");
				sql.append("                WHEN 'BASE TABLE' THEN '表'");
				sql.append("                ELSE '视图'");
				sql.append("            END,");
				sql.append("            '] ',");
				sql.append("            table_name) AS Name,");
				sql.append("    CASE table_type");
				sql.append("        WHEN 'BASE TABLE' THEN 'U'");
				sql.append("        ELSE 'V'");
				sql.append("    END AS xtype");
				sql.append(" FROM");
				sql.append("    information_schema.tables");
				sql.append(" WHERE");
				sql.append(String.format("    table_schema = '%1$s'",
						SystemConfig.getAppCenterDBDatabase()));
				sql.append("        AND (table_type = 'BASE TABLE'");
				sql.append("        OR table_type = 'VIEW')");
				// sql.append("       AND (table_name NOT LIKE 'ND%'");
				sql.append("        AND table_name NOT LIKE 'Demo_%'");
				sql.append("        AND table_name NOT LIKE 'Sys_%'");
				sql.append("        AND table_name NOT LIKE 'WF_%'");
				sql.append("        AND table_name NOT LIKE 'GPM_%'");
				sql.append(" ORDER BY table_type , table_name;");
				break;
			case Informix:
				sql.append("");
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
			} catch (RuntimeException ex)
			{
				throw new RuntimeException("@失败:" + ex.getMessage());
			}
		}

		//去除已经使用的表
		String filter = "";
		for (DataRow dr : allTablesExist.Rows)
		{
			filter += String.format("No='%1$s' OR ", dr.getValue(0));
		}

		if ( ! filter.equals(""))
		{

			List<DataRow> deletedRows = allTables.select(rtrim(filter," OR "));
			for (DataRow dr : deletedRows)
			{
				allTables.Rows.remove(dr);
			}
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
	*/
	private String GetDSN()
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
				dsn = "user id=" + this.getUserID() + ";data source=" + this.getDBName() + ";password=" + this.getPassword() + ";Max Pool Size=200";
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

		DBSrcType dbType = this.getDBSrcType();
		if (dbType == BP.Sys.DBSrcType.Localhost)
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
				conn = new System.Data.OracleClient.OracleConnection(dsn);
				break;
			case MySQL:
				conn = new MySql.Data.MySqlClient.MySqlConnection(dsn);
				break;
			case Informix:
				conn = new System.Data.OleDb.OleDbConnection(dsn);
				break;
		}

		return conn;
	}*/

	/*private DataTable RunSQLReturnTable(String sql, System.Data.Common.DbConnection conn, String dsn, CommandType cmdType)
	{
		if (conn instanceof System.Data.SqlClient.SqlConnection)
		{
			return BP.DA.DBAccess.RunSQLReturnTable(sql, (System.Data.SqlClient.SqlConnection)conn, dsn, cmdType);
		}
		if (conn instanceof System.Data.OleDb.OleDbConnection)
		{
			return BP.DA.DBAccess.RunSQLReturnTable(sql, (System.Data.OleDb.OleDbConnection)conn, cmdType);
		}
		if (conn instanceof System.Data.OracleClient.OracleConnection)
		{
			return BP.DA.DBAccess.RunSQLReturnTable(sql, (System.Data.OracleClient.OracleConnection)conn, cmdType, dsn);
		}
		if (conn instanceof MySqlConnection)
		{
			var mySqlConn = (MySqlConnection)conn;
			if (mySqlConn.State != ConnectionState.Open)
			{
				mySqlConn.Open();
			}

			MySqlDataAdapter ada = new MySqlDataAdapter(sql, mySqlConn);
			ada.SelectCommand.setCommandType(CommandType.getText());


			try
			{
				DataTable oratb = new DataTable("otb");
				ada.Fill(oratb);
				ada.dispose();

				conn.Close();
				conn.dispose();
				return oratb;
			}
			catch (RuntimeException ex)
			{
				ada.dispose();
				conn.Close();
				throw new RuntimeException("SQL=" + sql + " Exception=" + ex.getMessage());
			}
		}

		throw new RuntimeException("没有涉及到的连接测试类型...");
		return null;
	}*/

	/** 
	 修改表/视图/列名称（不完善）
	 @param objType 修改对象的类型，TABLE(表)、VIEW(视图)、COLUMN(列)
	 @param oldName 旧名称
	 @param newName 新名称
	 @param tableName 修改列名称时，列所属的表名称
	*/
	public final void Rename(String objType, String oldName, String newName, String tableName)
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
						RunSQL(String.format("ALTER TABLE %1$s CHANGE COLUMN %2$s %3$s %4$s", tableName, oldName, newName, dt.Rows.get(0).get(0)));
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
						RunSQL(String.format("CREATE VIEW %1$s AS %2$s", newName, dt.Rows.get(0).get(0)));
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
	 获取表的字段信息
	 @param tableName 表/视图名称
	 @return 有四个列 No,Name,DBType,DBLength 分别标识  列的字段名，列描述，类型，长度。
	*/
	public final DataTable GetColumns(String tableName)
	{
		//SqlServer数据库
		StringBuilder sql = new StringBuilder();

		DBSrcType dbType = this.getDBSrcType();
		if (dbType == BP.Sys.DBSrcType.Localhost)
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
				sql.append("SELECT sc.name as No,");
				sql.append("       st.name AS [DBType],");
				sql.append("       (");
				sql.append("           CASE ");
				sql.append("                WHEN st.name = 'nchar' OR st.name = 'nvarchar' THEN sc.length / 2");
				sql.append("                ELSE sc.length");
				sql.append("           END");
				sql.append("       ) AS DBLength,");
				sql.append("       sc.colid,");
				sql.append("       ISNULL(ep.[value], '') AS [Name]");
				sql.append("FROM   dbo.syscolumns sc");
				sql.append("       INNER JOIN dbo.systypes st");
				sql.append("            ON  sc.xtype = st.xusertype");
				sql.append("       LEFT OUTER JOIN sys.extended_properties ep");
				sql.append("            ON  sc.id = ep.major_id");
				sql.append("            AND sc.colid = ep.minor_id");
				sql.append("            AND ep.name = 'MS_Description'");
				sql.append(String.format("WHERE  sc.id = OBJECT_ID('dbo.%1$s')", tableName));
				break;
			case Oracle:
				sql.append("SELECT utc.COLUMN_NAME AS No,");
				sql.append("       utc.DATA_TYPE   AS DBType,");
				sql.append("       utc.CHAR_LENGTH AS DBLength,");
				sql.append("       utc.COLUMN_ID   AS colid,");
				sql.append("       ucc.comments    AS Name");
				sql.append("  FROM user_tab_cols utc");
				sql.append("  LEFT JOIN user_col_comments ucc");
				sql.append("    ON ucc.table_name = utc.TABLE_NAME");
				sql.append("   AND ucc.column_name = utc.COLUMN_NAME");
				sql.append(String.format(" WHERE utc.TABLE_NAME = '%1$s'", tableName.toUpperCase()));
				sql.append(" ORDER BY colid ASC");

				break;
			case MySQL:
				sql.append("SELECT ");
				sql.append("    column_name AS 'No',");
				sql.append("    data_type AS 'DBType',");
				sql.append("    IFNULL(character_maximum_length,");
				sql.append("            numeric_precision) AS DBLength,");
				sql.append("    ordinal_position AS colid,");
				sql.append("    column_comment AS 'Name'");
				sql.append("    FROM");
				sql.append("    information_schema.columns");
				sql.append(" WHERE");
				sql.append(String.format("    table_schema = '%1$s'", SystemConfig.getAppCenterDBDatabase()));
				sql.append(String.format("        AND table_name = '%1$s';", tableName));
				break;
			case Informix:
				break;
			default:
				throw new RuntimeException("没有涉及到的连接测试类型...");
		}


		if (this.getNo().equals("local"))
		{
			return DBAccess.RunSQLReturnTable(sql.toString());
		}
		/*
		String dsn = GetDSN();
		System.Data.Common.DbConnection conn = GetConnection(dsn);

		try
		{
			//System.Data.SqlClient.SqlConnection conn = new System.Data.SqlClient.SqlConnection();
			//conn.ConnectionString = dsn;
			conn.Open();

			return RunSQLReturnTable(sql.toString(), conn, dsn, CommandType.getText());
		}
		catch (RuntimeException ex)
		{
			throw new RuntimeException("@失败:" + ex.getMessage() + " dns:" + dsn);
		}
		 */
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
	@Override
	protected boolean beforeUpdateInsertAction() throws Exception
	{
		if (!this.getNo().equals("local") && this.getDBSrcType() == BP.Sys.DBSrcType.Localhost)
		{
			throw new RuntimeException("@在该系统中只能有一个本地连接，请选择其他数据源类型。");
		}

		return super.beforeUpdateInsertAction();
	}

		///#endregion 方法.

}