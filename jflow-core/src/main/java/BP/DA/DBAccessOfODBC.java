package BP.DA;

import Oracle.ManagedDataAccess.Client.*;
import MySql.Data.*;
import MySql.*;
import MySql.Data.MySqlClient.*;
import BP.Sys.*;
import Npgsql.*;
import java.util.*;
import java.io.*;
import java.nio.file.*;
import java.math.*;

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#region ODBC
public class DBAccessOfODBC
{
	/** 
	 检查是不是存在
	*/
	public static boolean IsExits(String selectSQL)
	{
		if (RunSQLReturnVal(selectSQL) == null)
		{
			return false;
		}
		return true;
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 取得连接对象 ，CS、BS共用属性【关键属性】
	public static OdbcConnection getGetSingleConn()
	{
		return new OdbcConnection(SystemConfig.getAppSettings().get("DBAccessOfODBC"));
			/* 2019-7-24 张磊 
			if (SystemConfig.IsBSsystem_Test)
			{
			    OdbcConnection conn = HttpContext.Current.Session["DBAccessOfODBC"] as OdbcConnection;
			    if (conn == null)
			    {
			        conn = new OdbcConnection(SystemConfig.AppSettings["DBAccessOfODBC"]);
			        HttpContext.Current.Session["DBAccessOfODBC"] = conn;
			    }
			    return conn;
			}
			else
			{
			    OdbcConnection conn = SystemConfig.CS_DBConnctionDic["DBAccessOfODBC"] as OdbcConnection;
			    if (conn == null)
			    {
			        conn = new OdbcConnection(SystemConfig.AppSettings["DBAccessOfODBC"]);
			        SystemConfig.CS_DBConnctionDic["DBAccessOfODBC"] = conn;
			    }
			    return conn;
			}
			*/
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 取得连接对象 ，CS、BS共用属性


//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 重载 RunSQLReturnTable

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 使用本地的连接
	public static DataTable RunSQLReturnTable(String sql)
	{
		return RunSQLReturnTable(sql, getGetSingleConn(), CommandType.Text);
	}
	public static DataTable RunSQLReturnTable(String sql, CommandType sqlType, Object... pars)
	{
		return RunSQLReturnTable(sql, getGetSingleConn(), sqlType, pars);
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 使用指定的连接
	public static DataTable RunSQLReturnTable(String sql, OdbcConnection conn)
	{
		return RunSQLReturnTable(sql, conn, CommandType.Text);
	}
	public static DataTable RunSQLReturnTable(String sql, OdbcConnection conn, CommandType sqlType, Object... pars)
	{
		try
		{
			OdbcDataAdapter ada = new OdbcDataAdapter(sql, conn);
			ada.SelectCommand.CommandType = sqlType;
			for (Object par : pars)
			{
				ada.SelectCommand.Parameters.AddWithValue("par", par);
			}
			if (conn.State != ConnectionState.Open)
			{
				conn.Open();
			}
			DataTable dt = new DataTable("tb");
			ada.Fill(dt);
			// peng add 
			ada.Dispose();
			return dt;
		}
		catch (RuntimeException ex)
		{
			throw new RuntimeException(ex.getMessage() + sql);
		}
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 重载 RunSQL

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 使用本地的连接
	public static int RunSQLReturnCOUNT(String sql)
	{
		return RunSQLReturnTable(sql).Rows.Count;
	}
	public static int RunSQL(String sql)
	{
		return RunSQL(sql, getGetSingleConn(), CommandType.Text);
	}
	public static int RunSQL(String sql, CommandType sqlType, Object... pars)
	{
		return RunSQL(sql, getGetSingleConn(), sqlType, pars);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 使用本地的连接

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 使用指定的连接
	public static int RunSQL(String sql, OdbcConnection conn)
	{
		return RunSQL(sql, conn, CommandType.Text);
	}
	public static int RunSQL(String sql, OdbcConnection conn, CommandType sqlType, Object... pars)
	{
		Debug.WriteLine(sql);
		try
		{
			OdbcCommand cmd = new OdbcCommand(sql, conn);
			cmd.CommandType = sqlType;
			for (Object par : pars)
			{
				cmd.Parameters.AddWithValue("par", par);
			}
			if (conn.State != System.Data.ConnectionState.Open)
			{
				conn.Open();
			}
			return cmd.ExecuteNonQuery();
		}
		catch (RuntimeException ex)
		{
			throw new RuntimeException(ex.getMessage() + sql);
		}
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 使用指定的连接

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 执行SQL ，返回首行首列

	/** 
	 运行select sql, 返回一个值。
	 
	 @param sql select sql
	 @return 返回的值object
	*/
	public static float RunSQLReturnFloatVal(String sql)
	{
		return (Float)RunSQLReturnVal(sql, getGetSingleConn(), CommandType.Text);
	}
	public static int RunSQLReturnValInt(String sql)
	{
		return (Integer)RunSQLReturnVal(sql, getGetSingleConn(), CommandType.Text);
	}
	/** 
	 运行select sql, 返回一个值。
	 
	 @param sql select sql
	 @return 返回的值object
	*/
	public static Object RunSQLReturnVal(String sql)
	{
		return RunSQLReturnVal(sql, getGetSingleConn(), CommandType.Text);
	}
	/** 
	 运行select sql, 返回一个值。
	 
	 @param sql select sql
	 @param sqlType CommandType
	 @param pars params
	 @return 返回的值object
	*/
	public static Object RunSQLReturnVal(String sql, CommandType sqlType, Object... pars)
	{
		return RunSQLReturnVal(sql, getGetSingleConn(), sqlType, pars);
	}
	public static Object RunSQLReturnVal(String sql, OdbcConnection conn)
	{
		return RunSQLReturnVal(sql, conn, CommandType.Text);
	}
	public static Object RunSQLReturnVal(String sql, OdbcConnection conn, CommandType sqlType, Object... pars)
	{
		Debug.WriteLine(sql);
		OdbcConnection tmp = new OdbcConnection(conn.ConnectionString);
		OdbcCommand cmd = new OdbcCommand(sql, tmp);
		Object val = null;
		try
		{
			cmd.CommandType = sqlType;
			for (Object par : pars)
			{
				cmd.Parameters.AddWithValue("par", par);
			}
			tmp.Open();
			val = cmd.ExecuteScalar();
		}
		catch (RuntimeException ex)
		{
			tmp.Close();
			throw new RuntimeException(ex.getMessage() + sql);
		}
		tmp.Close();
		return val;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 执行SQL ，返回首行首列

}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#endregion

