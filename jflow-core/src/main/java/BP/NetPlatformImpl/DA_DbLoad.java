package BP.NetPlatformImpl;

import BP.DA.*;
import BP.En30.ccportal.*;
import java.io.*;

public class DA_DbLoad
{
	public static String GenerFirstTableName(String fileName)
	{
		return GenerTableNameByIndex(fileName, 0);
	}
	public static String GenerTableNameByIndex(String fileName, int index)
	{
		String[] excelSheets = GenerTableNames(fileName);
		if (excelSheets != null)
		{
			return excelSheets[index];
		}

		if (excelSheets.length < index)
		{
			throw new RuntimeException("err@table的索引号错误" + index + "最大索引号为:" + excelSheets.length);
		}

		return null;
	}
	public static String[] GenerTableNames(String fileName)
	{
		String strConn = "Provider=Microsoft.Jet.Oledb.4.0;Data Source=" + fileName + ";Extended Properties='Excel 8.0;HDR=Yes;IMEX=1;'";
		try
		{
			if (fileName.toLowerCase().contains(".xlsx"))
			{
				strConn = "Provider=Microsoft.ACE.OLEDB.12.0;Data Source=" + fileName + ";Extended Properties=\"Excel 8.0;HDR=Yes;IMEX=1\"";
			}

			OleDbConnection con = new OleDbConnection(strConn);
			con.Open();
			//计算出有多少个工作表sheet   
			DataTable dt = con.GetOleDbSchemaTable(OleDbSchemaGuid.Tables, null);
			if (dt == null)
			{
				return null;
			}

			String[] excelSheets = new String[dt.Rows.size()];
			for (int i = 0; i < dt.Rows.size(); i++)
			{
				excelSheets[i] = dt.Rows[i]["TABLE_NAME"].toString();
			}

			con.Close();
			con.Dispose();
			return excelSheets;
		}
		catch (RuntimeException ex)
		{
			throw new RuntimeException("@获取table出错误：" + ex.getMessage() + strConn);
		}
	}

	public static DataTable ReadExcelFileToDataTableBySQL(String filePath, String tableName)
	{
		String sql = "SELECT * FROM [" + tableName + "]";
		DataTable dt = new DataTable("dt");

		String typ = System.IO.Path.GetExtension(filePath).toLowerCase();
		String strConn;
		switch (typ.toLowerCase())
		{
			case ".xls":
				if (sql == null)
				{
					sql = "SELECT * FROM [" + GenerFirstTableName(filePath) + "]";
				}

				strConn = "Provider=Microsoft.Jet.OLEDB.4.0;Data Source =" + filePath + ";Extended Properties=Excel 8.0";
				System.Data.OleDb.OleDbConnection conn = new OleDbConnection(strConn);
				OleDbDataAdapter ada = new OleDbDataAdapter(sql, conn);
				try
				{
					conn.Open();
					ada.Fill(dt);
					dt.TableName = Path.GetFileNameWithoutExtension(filePath);
				}
				catch (RuntimeException ex)
				{
					conn.Close();
					throw ex; //(ex.Message);
				}
				conn.Close();
				return dt;
			case ".xlsx":
				if (sql == null)
				{
					sql = "SELECT * FROM [" + GenerFirstTableName(filePath) + "]";
				}
				try
				{
					strConn = "Provider=Microsoft.ACE.OLEDB.12.0;Data Source=" + filePath + ";Extended Properties=\"Excel 12.0 Xml;HDR=YES\"";
					System.Data.OleDb.OleDbConnection conn121 = new OleDbConnection(strConn);
					OleDbDataAdapter ada91 = new OleDbDataAdapter(sql, conn121);
					conn121.Open();
					ada91.Fill(dt);
					dt.TableName = Path.GetFileNameWithoutExtension(filePath);
					conn121.Close();
					ada91.Dispose();
				}
				catch (RuntimeException ex1)
				{
					try
					{
						strConn = "Microsoft.ACE.OLEDB.12.0;Data Source=" + filePath + ";Extended Properties=\"Excel 12.0 Xml;HDR=YES;IMEX=1\"";
						System.Data.OleDb.OleDbConnection conn1215 = new OleDbConnection(strConn);
						OleDbDataAdapter ada919 = new OleDbDataAdapter(sql, conn1215);
						ada919.Fill(dt);
						dt.TableName = Path.GetFileNameWithoutExtension(filePath);
						ada919.Dispose();
						conn1215.Close();
					}
					catch (java.lang.Exception e)
					{

					}
					throw ex1; //(ex.Message);
				}
				return dt;
			case ".dbf":
				strConn = "Driver={Microsoft dBASE Driver (*.DBF)};DBQ=" + (new File(filePath)).getParent() + "\\"; //+FilePath;//
				OdbcConnection conn1 = new OdbcConnection(strConn);
				OdbcDataAdapter ada1 = new OdbcDataAdapter(sql, conn1);
				conn1.Open();
				try
				{
					ada1.Fill(dt);
				}
				catch (java.lang.Exception e2) //(System.Exception ex)
				{
					try
					{
						int sel = ada1.SelectCommand.CommandText.toLowerCase().indexOf("select") + 6;
						int from = ada1.SelectCommand.CommandText.toLowerCase().indexOf("from");
						ada1.SelectCommand.CommandText = tangible.StringHelper.remove(ada1.SelectCommand.CommandText, sel, from - sel);
						ada1.SelectCommand.CommandText = ada1.SelectCommand.CommandText.insert(sel, " top 10 * ");
						ada1.Fill(dt);
						dt.TableName = "error";
					}
					catch (RuntimeException ex)
					{
						conn1.Close();
						throw new RuntimeException("读取DBF数据失败！" + ex.getMessage() + " SQL:" + sql);
					}
				}
				conn1.Close();
				return dt;
			default:
				break;
		}
		return dt;
	}
}