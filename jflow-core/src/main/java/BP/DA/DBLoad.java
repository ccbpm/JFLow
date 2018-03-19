package BP.DA;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * DBLoad 的摘要说明。
 */
public class DBLoad
{
	static
	{
	}
	
	public static int ImportTableInto(DataTable impTb, String intoTb,
			String select, int clear)
	{
		int count = 0;
		// DataTable target = null;
		//
		// //导入前是否先清空
		// if(clear==1)
		// {
		// DBAccess.RunSQL("delete from " + intoTb);
		// }
		//
		// try
		// {
		// target = DBAccess.RunSQLReturnTable(select);
		// }
		// catch(RuntimeException ex) //select查询出错，可能是缺少列
		// {
		// throw new RuntimeException("源表格式有误，请核对！"+ex.getMessage()
		// +" :"+select);
		// }
		//
		// Object conn = DBAccess.getGetAppCenterDBConn();
		//
		// SqlDataAdapter sqlada = null;
		// OracleDataAdapter oraada = null;
		// DBType dbt = DBAccess.getAppCenterDBType();
		// if(dbt == DBType.MSSQL)
		// {
		// sqlada = new
		// SqlDataAdapter(select,(SqlConnection)DBAccess.getGetAppCenterDBConn());
		// SqlCommandBuilder bl = new SqlCommandBuilder(sqlada);
		// sqlada.InsertCommand = bl.GetInsertCommand();
		//
		// count = ImportTable(impTb, target, sqlada);
		// }
		// else if(dbt == DBType.Oracle)
		// {
		// oraada = new
		// OracleDataAdapter(select,(OracleConnection)DBAccess.getGetAppCenterDBConn());
		// OracleCommandBuilder bl = new OracleCommandBuilder(oraada);
		// oraada.InsertCommand = bl.GetInsertCommand();
		//
		// count = ImportTable(impTb, target, oraada);
		// }
		// else
		// {
		// throw new RuntimeException("未获取数据库连接！ ");
		// }
		//
		// target.dispose();
		return count;
	}
	
	// private static int ImportTable(DataTable source, DataTable target,
	// SqlDataAdapter sqlada)
	// {
	// int count = 0;
	// try
	// {
	// if(sqlada.InsertCommand.getConnection().getState()!=
	// ConnectionState.Open)
	// {
	// sqlada.InsertCommand.getConnection().Open();
	// }
	// sqlada.InsertCommand.setTransaction(sqlada.InsertCommand.getConnection().BeginTransaction());
	// source.Columns.Add("错误提示",String.class);
	// source.Columns["错误提示"].MaxLength = 1000;
	//
	// int i =0;
	// while(i < source.Rows.size()) //for( int i=0;i<;i++)
	// {
	// for(int c=0;c< target.Columns.size() ;c++)
	// {
	// sqlada.InsertCommand.getParameters()[c].setValue(source.Rows[i][c]);
	// }
	// try //个别记录失败，跳过
	// {
	// sqlada.InsertCommand.ExecuteNonQuery();
	// }
	// catch(RuntimeException ex)
	// {
	// source.Rows[i]["错误提示"] =ex.getMessage();
	// i++;
	// continue;
	// }
	// count++; //已导入的记录数
	// source.Rows.RemoveAt(i);
	// }
	// sqlada.InsertCommand.getTransaction().Commit();
	// }
	// catch(RuntimeException ex)
	// {
	// if(sqlada.InsertCommand.getTransaction()!=null)
	// {
	// sqlada.InsertCommand.getTransaction().Rollback();
	// }
	// sqlada.InsertCommand.getConnection().Close();
	// throw new RuntimeException("导入数据失败！"+ex.getMessage());
	// }
	// return count;
	// }
	// private static int ImportTable(DataTable source, DataTable target,
	// OracleDataAdapter oraada)
	// {
	// int count = 0;
	// try
	// {
	// if(oraada.InsertCommand.getConnection().getState()!=
	// ConnectionState.Open)
	// {
	// oraada.InsertCommand.getConnection().Open();
	// }
	// oraada.InsertCommand.setTransaction(oraada.InsertCommand.getConnection().BeginTransaction());
	// int i =0;
	// while(i < source.Rows.size()) //for( int i=0;i<;i++)
	// {
	// for(int c=0;c< target.Columns.size() ;c++)
	// {
	// oraada.InsertCommand.getParameters()[c].setValue(source.Rows[i][c]);
	// }
	// // if( i>6 )
	// // throw new Exception( "Test！" );
	// try //个别记录失败，跳过
	// {
	// oraada.InsertCommand.ExecuteNonQuery();
	// }
	// catch (java.lang.Exception e)
	// {
	// i++;
	// continue;
	// }
	// count++; //已导入的记录数
	// source.Rows.RemoveAt(i);
	// }
	// oraada.InsertCommand.getTransaction().Commit();
	// }
	// catch(RuntimeException ex)
	// {
	// if(oraada.InsertCommand.getTransaction()!=null)
	// {
	// oraada.InsertCommand.getTransaction().Rollback();
	// }
	// oraada.InsertCommand.getConnection().Close();
	// throw new RuntimeException("导入数据失败！"+ex.getMessage());
	// }
	// return count;
	// }
	
	public static String GenerFirstTableName(String fileName)
	{
		return GenerTableNameByIndex(fileName, 0);
	}
	
	public static String GenerTableNameByIndex(String fileName, int index)
	{
		String[] excelSheets = GenerTableNames(fileName);
		if (excelSheets != null && excelSheets.length >= index)
		{
			return excelSheets[index];
		}
		return null;
	}
	
	public static String[] GenerTableNames(String fileName)
	{
		return null;
		
		// String strConn = "Provider=Microsoft.Jet.Oledb.4.0;Data Source=" +
		// fileName + ";Extended Properties='Excel 8.0;HDR=Yes;IMEX=1;'";
		// try
		// {
		// if (fileName.toLowerCase().contains(".xlsx"))
		// {
		// strConn = "Microsoft.Jet.OLEDB.4.0;Data Source=" + fileName +
		// ";Extended Properties=\"Excel 8.0;HDR=Yes;IMEX=1\"";
		// }
		//
		// OleDbConnection con = new OleDbConnection(strConn);
		// con.Open();
		// //计算出有多少个工作表sheet
		// DataTable dt = con.GetOleDbSchemaTable(OleDbSchemaGuid.Tables, null);
		// if (dt == null)
		// {
		// return null;
		// }
		//
		// String[] excelSheets = new String[dt.Rows.size()];
		// for (int i = 0; i < dt.Rows.size(); i++)
		// {
		// excelSheets[i] = dt.Rows[i]["TABLE_NAME"].toString();
		// }
		//
		// con.Close();
		// con.dispose();
		// return excelSheets;
		// }
		// catch (RuntimeException ex)
		// {
		// throw new RuntimeException("@获取table出错误：" + ex.getMessage() +
		// strConn);
		// }
	}
	
	/**
	 * 得到Excel表中的值
	 * 
	 * @param hssfCell
	 *            Excel中的每一个格子
	 * @return Excel中每一个格子中的值
	 */
	@SuppressWarnings("static-access")
	private static String getValue(HSSFCell hssfCell)
	{
		if (hssfCell.getCellType() == hssfCell.CELL_TYPE_BOOLEAN)
		{
			// 返回布尔类型的值
			return String.valueOf(hssfCell.getBooleanCellValue());
		} else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC)
		{
			// 返回数值类型的值
			return String.valueOf(hssfCell.getNumericCellValue());
		} else
		{
			// 返回字符串类型的值
			return String.valueOf(hssfCell.getStringCellValue());
		}
	}
	
	@SuppressWarnings("resource")
	public static DataTable GetTableByExt(InputStream is)
	{
		DataTable Tb = new DataTable("Tb");
		Tb.Rows.clear();
		DataColumnCollection collection = new DataColumnCollection(Tb);
		HSSFWorkbook hssfWorkbook = null;
		try
		{
			hssfWorkbook = new HSSFWorkbook(is);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
		// 循环工作表Sheet , 目前支持一个
		// for (int i = 0; i < hssfWorkbook.getNumberOfSheets(); i++) {
		HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);
		if (hssfSheet == null)
		{
			return null;
		}
		// 循环行Row
		int row_size = hssfSheet.getLastRowNum();
		for (int j = 0; j < row_size; j++)
		{
			HSSFRow hssfRow = hssfSheet.getRow(j);
			if (hssfRow == null)
			{
				continue;
			}
			
			// 循环列Cell
			int call_num = hssfRow.getPhysicalNumberOfCells();
			// title
			if (0 == j)
			{
				for (int k = 0; k < call_num; k++)
				{
					HSSFCell hssfCell = hssfRow.getCell(k);
					if (null == hssfCell)
					{
						continue;
					}
					DataColumn column = new DataColumn(getValue(hssfCell));
					collection.Add(column);
				}
			} else
			{ // 内容
				DataRow dataRow = new DataRow(Tb);
				for (int k = 0; k < call_num; k++)
				{
					HSSFCell hssfCell = hssfRow.getCell(k);
					if (null == hssfCell)
					{
						continue;
					}
					dataRow.setValue(collection.get(k), getValue(hssfCell));
				}
				Tb.Rows.add(dataRow);
			}
			
		}
		Tb.Columns = collection;
		return Tb;
	}
	
	/**
	 * @param filePath
	 * @return
	 */
	public static DataTable GetTableByExt(String filePath)
	{
		FileInputStream stream = null;
		try
		{
			stream = new FileInputStream(filePath);
		} catch (IOException e)
		{
			e.printStackTrace();
		}finally{
			if(stream!=null){
				try {
					stream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return GetTableByExt(stream);
		
		// }
		// String typ = System.IO.Path.GetExtension(filePath).toLowerCase();
		// String strConn;
		// if (typ.toLowerCase().equals(".xls"))
		// {
		// if (sql==null)
		// {
		// sql = "SELECT * FROM [" + GenerFirstTableName(filePath) + "]";
		// }
		// strConn = "Provider=Microsoft.Jet.OLEDB.4.0;Data Source =" + filePath
		// + ";Extended Properties=Excel 8.0";
		// System.Data.OleDb.OleDbConnection conn = new
		// OleDbConnection(strConn);
		// OleDbDataAdapter ada = new OleDbDataAdapter(sql, conn);
		// try
		// {
		// conn.Open();
		// ada.Fill(Tb);
		// Tb.TableName = Path.GetFileNameWithoutExtension(filePath);
		// }
		// catch (RuntimeException ex)
		// {
		// conn.Close();
		// throw ex; //(ex.Message);
		// }
		// conn.Close();
		// }
		// //ORIGINAL LINE: case ".xlsx":
		// else if (typ.toLowerCase().equals(".xlsx"))
		// {
		// if (sql == null)
		// {
		// sql = "SELECT * FROM [" + GenerFirstTableName(filePath)+"]";
		// }
		// try
		// {
		// strConn = "Microsoft.ACE.OLEDB.12.0;Data Source=" + filePath +
		// ";Extended Properties=\"Excel 12.0 Xml;HDR=YES\"";
		// System.Data.OleDb.OleDbConnection conn121 = new
		// OleDbConnection(strConn);
		// OleDbDataAdapter ada91 = new OleDbDataAdapter(sql, conn121);
		// conn121.Open();
		// ada91.Fill(Tb);
		// Tb.TableName = Path.GetFileNameWithoutExtension(filePath);
		// conn121.Close();
		// ada91.dispose();
		// }
		// catch (RuntimeException ex1)
		// {
		// try
		// {
		// strConn = "Microsoft.ACE.OLEDB.12.0;Data Source=" + filePath +
		// ";Extended Properties=\"Excel 12.0 Xml;HDR=YES;IMEX=1\"";
		// System.Data.OleDb.OleDbConnection conn1215 = new
		// OleDbConnection(strConn);
		// OleDbDataAdapter ada919 = new OleDbDataAdapter(sql, conn1215);
		// ada919.Fill(Tb);
		// Tb.TableName = Path.GetFileNameWithoutExtension(filePath);
		// ada919.dispose();
		// conn1215.Close();
		// }
		// catch (java.lang.Exception e)
		// {
		//
		// }
		// throw ex1; //(ex.Message);
		// }
		// }
		// //ORIGINAL LINE: case ".dbf":
		// else if (typ.toLowerCase().equals(".dbf"))
		// {
		// strConn = "Driver={Microsoft dBASE Driver (*.DBF)};DBQ=" +
		// System.IO.Path.GetDirectoryName(filePath) + "\\"; //+FilePath;//
		// OdbcConnection conn1 = new OdbcConnection(strConn);
		// OdbcDataAdapter ada1 = new OdbcDataAdapter(sql, conn1);
		// conn1.Open();
		// try
		// {
		// ada1.Fill(Tb);
		// }
		// catch (java.lang.Exception e2) //(System.Exception ex)
		// {
		// try
		// {
		// int sel =
		// ada1.SelectCommand.getCommandText().toLowerCase().indexOf("select") +
		// 6;
		// int from =
		// ada1.SelectCommand.getCommandText().toLowerCase().indexOf("from");
		// String tempVar = ada1.SelectCommand.getCommandText();
		// ada1.SelectCommand.setCommandText(tempVar.substring(0, sel) +
		// tempVar.substring(sel + from - sel));
		// ada1.SelectCommand.setCommandText(ada1.SelectCommand.getCommandText().insert(sel,
		// " top 10 * "));
		// ada1.Fill(Tb);
		// Tb.TableName = "error";
		// }
		// catch (RuntimeException ex)
		// {
		// conn1.Close();
		// throw new RuntimeException("读取DBF数据失败！" + ex.getMessage() + " SQL:" +
		// sql);
		// }
		// }
		// conn1.Close();
		// }
		// else
		// {
		// }
		// return Tb;
		// return null;
	}
}