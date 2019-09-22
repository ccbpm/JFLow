package BP.DA;

import Oracle.ManagedDataAccess.Client.*;
import BP.NetPlatformImpl.*;

//using System.Data.OracleClient;

/** 
 DBLoad 的摘要说明。
*/
public class DBLoad
{
	/** 
	 装载
	*/
	public DBLoad()
	{
	}
	public static int ImportTableInto(DataTable impTb, String intoTb, String select, int clear)
	{
		int count = 0;
		DataTable target = null;

		//导入前是否先清空
		if (clear == 1)
		{
			DBAccess.RunSQL("delete from " + intoTb);
		}

		try
		{
			target = DBAccess.RunSQLReturnTable(select);
		}
		catch (RuntimeException ex) //select查询出错，可能是缺少列
		{
			throw new RuntimeException("源表格式有误，请核对！" + ex.getMessage() + " :" + select);
		}

		Object conn = DBAccess.getGetAppCenterDBConn();

		SqlDataAdapter sqlada = null;
		OracleDataAdapter oraada = null;
		DBType dbt = DBAccess.getAppCenterDBType();
		if (dbt == DBType.MSSQL)
		{
			sqlada = new SqlDataAdapter(select, (SqlConnection)DBAccess.getGetAppCenterDBConn());
			SqlCommandBuilder bl = new SqlCommandBuilder(sqlada);
			sqlada.InsertCommand = bl.GetInsertCommand();

			count = ImportTable(impTb, target, sqlada);
		}
		else if (dbt == DBType.Oracle)
		{
			oraada = new OracleDataAdapter(select, (OracleConnection)DBAccess.getGetAppCenterDBConn());
			OracleCommandBuilder bl = new OracleCommandBuilder(oraada);
			oraada.InsertCommand = bl.GetInsertCommand();

			count = ImportTable(impTb, target, oraada);
		}
		else
		{
			throw new RuntimeException("未获取数据库连接！ ");
		}

		target.Dispose();
		return count;
	}
	private static int ImportTable(DataTable source, DataTable target, SqlDataAdapter sqlada)
	{
		int count = 0;
		try
		{
			if (sqlada.InsertCommand.Connection.State != ConnectionState.Open)
			{
				sqlada.InsertCommand.Connection.Open();
			}
			sqlada.InsertCommand.Transaction = sqlada.InsertCommand.Connection.BeginTransaction();
			source.Columns.Add("错误提示", String.class);
			source.Columns["错误提示"].MaxLength = 1000;

			int i = 0;
			while (i < source.Rows.Count) //for( int i=0;i<;i++)
			{
				for (int c = 0; c < target.Columns.Count; c++)
				{
					sqlada.InsertCommand.Parameters[c].Value = source.Rows[i][c];
				}
				try //个别记录失败，跳过
				{
					sqlada.InsertCommand.ExecuteNonQuery();
				}
				catch (RuntimeException ex)
				{
					source.Rows[i]["错误提示"] = ex.getMessage();
					i++;
					continue;
				}
				count++; //已导入的记录数
				source.Rows.RemoveAt(i);
			}
			sqlada.InsertCommand.Transaction.Commit();
		}
		catch (RuntimeException ex)
		{
			if (sqlada.InsertCommand.Transaction != null)
			{
				sqlada.InsertCommand.Transaction.Rollback();
			}
			sqlada.InsertCommand.Connection.Close();
			throw new RuntimeException("导入数据失败！" + ex.getMessage());
		}
		return count;
	}
	private static int ImportTable(DataTable source, DataTable target, OracleDataAdapter oraada)
	{
		int count = 0;
		try
		{
			if (oraada.InsertCommand.Connection.State != ConnectionState.Open)
			{
				oraada.InsertCommand.Connection.Open();
			}
			oraada.InsertCommand.Transaction = oraada.InsertCommand.Connection.BeginTransaction();
			int i = 0;
			while (i < source.Rows.Count) //for( int i=0;i<;i++)
			{
				for (int c = 0; c < target.Columns.Count; c++)
				{
					oraada.InsertCommand.Parameters[c].Value = source.Rows[i][c];
				}
				//					if( i>6 )
				//						throw new Exception( "Test！" );
				try //个别记录失败，跳过
				{
					oraada.InsertCommand.ExecuteNonQuery();
				}
				catch (java.lang.Exception e)
				{
					i++;
					continue;
				}
				count++; //已导入的记录数
				source.Rows.RemoveAt(i);
			}
			oraada.InsertCommand.Transaction.Commit();
		}
		catch (RuntimeException ex)
		{
			if (oraada.InsertCommand.Transaction != null)
			{
				oraada.InsertCommand.Transaction.Rollback();
			}
			oraada.InsertCommand.Connection.Close();
			throw new RuntimeException("导入数据失败！" + ex.getMessage());
		}
		return count;
	}

	public static String GenerFirstTableName(String fileName)
	{
		return DA_DbLoad.GenerTableNameByIndex(fileName, 0);
	}
	public static String GenerTableNameByIndex(String fileName, int index)
	{
		return DA_DbLoad.GenerTableNameByIndex(fileName, index);
	}
	public static String[] GenerTableNames(String fileName)
	{
		return DA_DbLoad.GenerTableNames(fileName);
	}

	public static DataTable ReadExcelFileToDataTable(String fileFullName)
	{
		return ReadExcelFileToDataTable(fileFullName, 0);
	}

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public static DataTable ReadExcelFileToDataTable(string fileFullName, int sheetIdx = 0)
	public static DataTable ReadExcelFileToDataTable(String fileFullName, int sheetIdx)
	{
		String tableName = GenerTableNameByIndex(fileFullName, sheetIdx);
		return ReadExcelFileToDataTableBySQL(fileFullName, tableName);
	}
	public static DataTable ReadExcelFileToDataTable(String fileFullName, String tableName)
	{
		return ReadExcelFileToDataTableBySQL(fileFullName, tableName);
	}
	/** 
	 通过文件，sql ,取出Table.
	 
	 @param filePath
	 @param sql
	 @return 
	*/
	public static DataTable ReadExcelFileToDataTableBySQL(String filePath, String tableName)
	{
		return DA_DbLoad.ReadExcelFileToDataTableBySQL(filePath, tableName);
	}
}