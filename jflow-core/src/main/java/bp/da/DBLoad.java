package bp.da;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * DBLoad 的摘要说明。
 * 支持2007(包括)以上版本
 */
public class DBLoad
{
	static
	{
	}

	public static class XSSFDateUtil extends DateUtil {
		protected static int absoluteDay(Calendar cal, boolean use1904windowing) {
			return DateUtil.absoluteDay(cal, use1904windowing);
		}
	}

	public static int ImportTableInto(DataTable impTb, String intoTb,
									  String select, int clear)
	{
		int count = 0;

		return count;
	}


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

	}

	/**
	 * 得到Excel表中的值
	 *
	 * @param xssfCell
	 *            Excel中的每一个格子
	 * @return Excel中每一个格子中的值
	 */
	@SuppressWarnings("static-access")
	private static String getValue(XSSFCell xssfCell)
	{
		if (xssfCell.getCellType() == CellType.BOOLEAN)
		{
			// 返回布尔类型的值
			return String.valueOf(xssfCell.getBooleanCellValue());
		}
		if(xssfCell.getCellType() == CellType.NUMERIC){
			// 获取单元格的样式值，即获取单元格格式对应的数值
			int style = xssfCell.getCellStyle().getDataFormat();
			// 判断是否是日期格式
			if (XSSFDateUtil.isCellDateFormatted(xssfCell)) {
				Date date = xssfCell.getDateCellValue();
				// 对不同格式的日期类型做不同的输出，与单元格格式保持一致
				switch (style) {
					case 178:
						return new SimpleDateFormat("yyyy'年'M'月'd'日'").format(date);

					case 14:
						return new SimpleDateFormat("yyyy/MM/dd").format(date);

					case 179:
						return new SimpleDateFormat("yyyy/MM/dd HH:mm").format(date);

					case 181:
						return new SimpleDateFormat("yyyy/MM/dd HH:mm a ").format(date);

					case 22:
						return new SimpleDateFormat(" yyyy/MM/dd HH:mm:ss ").format(date);

					default:
						break;
				}
			} else {
				switch (style) {
					// 单元格格式为百分比，不格式化会直接以小数输出
					case 9:
						return  new DecimalFormat("0.00%").format(xssfCell.getNumericCellValue());

					// DateUtil判断其不是日期格式，在这里也可以设置其输出类型
					case 57:
						return new SimpleDateFormat(" yyyy'年'MM'月' ").format(xssfCell.getDateCellValue());

					default:
						xssfCell.setCellType(CellType.STRING);
						return String.valueOf(xssfCell.getStringCellValue());
				}
			}


		}
		//其余的格式设置成String，返回String
		xssfCell.setCellType(CellType.STRING);
		// 返回字符串类型的值
		return String.valueOf(xssfCell.getStringCellValue());

	}

	private static String getValue(HSSFCell xssfCell)
	{
		if (xssfCell.getCellType() == CellType.BOOLEAN)
		{
			// 返回布尔类型的值
			return String.valueOf(xssfCell.getBooleanCellValue());
		}
		if(xssfCell.getCellType() == CellType.NUMERIC){
			// 获取单元格的样式值，即获取单元格格式对应的数值
			int style = xssfCell.getCellStyle().getDataFormat();
			// 判断是否是日期格式
			if (XSSFDateUtil.isCellDateFormatted(xssfCell)) {
				Date date = xssfCell.getDateCellValue();
				// 对不同格式的日期类型做不同的输出，与单元格格式保持一致
				switch (style) {
					case 178:
						return new SimpleDateFormat("yyyy'年'M'月'd'日'").format(date);

					case 14:
						return new SimpleDateFormat("yyyy/MM/dd").format(date);

					case 179:
						return new SimpleDateFormat("yyyy/MM/dd HH:mm").format(date);

					case 181:
						return new SimpleDateFormat("yyyy/MM/dd HH:mm a ").format(date);

					case 22:
						return new SimpleDateFormat(" yyyy/MM/dd HH:mm:ss ").format(date);

					default:
						break;
				}
			} else {
				switch (style) {
					// 单元格格式为百分比，不格式化会直接以小数输出
					case 9:
						return  new DecimalFormat("0.00%").format(xssfCell.getNumericCellValue());

					// DateUtil判断其不是日期格式，在这里也可以设置其输出类型
					case 57:
						return new SimpleDateFormat(" yyyy'年'MM'月' ").format(xssfCell.getDateCellValue());

					default:
						xssfCell.setCellType(CellType.STRING);
						return String.valueOf(xssfCell.getStringCellValue());
				}
			}


		}
		//其余的格式设置成String，返回String
		xssfCell.setCellType(CellType.STRING);
		// 返回字符串类型的值
		return String.valueOf(xssfCell.getStringCellValue());

	}

	@SuppressWarnings("resource")
	public static DataTable ReadExcelFileToDataTable(InputStream is)throws Exception
	{
		DataTable Tb = new DataTable("Tb");
		Tb.Rows.clear();
		DataColumnCollection collection = new DataColumnCollection(Tb);
		XSSFWorkbook xssfWorkbook = null;

		try
		{
			xssfWorkbook = new XSSFWorkbook(is);
		} catch (IOException e)
		{
			e.printStackTrace();
		}

		// 循环工作表Sheet , 目前支持一个
		// for (int i = 0; i < hssfWorkbook.getNumberOfSheets(); i++) {
		XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);
		if (xssfSheet == null)
		{
			return null;
		}
		// 循环行Row
		int row_size = xssfSheet.getPhysicalNumberOfRows();
		for (int j = 0; j < row_size; j++)
		{
			XSSFRow xssfRow = xssfSheet.getRow(j);
			if (xssfRow == null)
			{
				continue;
			}

			// 循环列Cell
			int call_num = xssfRow.getPhysicalNumberOfCells();
			// title
			if (0 == j)
			{
				for (int k = 0; k < call_num; k++)
				{
					XSSFCell xssfCell = xssfRow.getCell(k);
					if (null == xssfCell)
					{
						continue;
					}
					DataColumn column = new DataColumn(getValue(xssfCell));
					collection.Add(column);
				}
			} else
			{ // 内容
				DataRow dataRow = new DataRow(Tb);
				for (int k = 0; k < call_num; k++)
				{
					XSSFCell xssfCell = xssfRow.getCell(k);
					if (null == xssfCell)
					{
						continue;
					}
					dataRow.setValue(collection.get(k), getValue(xssfCell));
				}
				Tb.Rows.add(dataRow);
			}

		}
		Tb.Columns = collection;
		return Tb;
	}

	public static DataTable ReadExcelFileToDataTable(String fileFullName, int sheetIdx)
	{
		String tableName = GenerTableNameByIndex(fileFullName, sheetIdx);
		return ReadExcelFileToDataTableBySQL(fileFullName, tableName);
	}
	public static DataTable ReadExcelFileToDataTableBySQL(String filePath, String tableName)
	{
		return ReadExcelFileToDataTableBySQL(filePath, tableName);
	}

	public static DataTable ReadExcelFileToDataTable(String filePath)throws Exception
	{
		FileInputStream stream = null;
		DataTable dataTable = null;
		try
		{
			stream = new FileInputStream(filePath);
			dataTable = ReadExcelFileToDataTable(stream);

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

		return dataTable;

	}

	@SuppressWarnings("resource")
	public static DataTable GetTableByExt(InputStream is) throws IOException {
		DataTable Tb = new DataTable("Tb");
		Tb.Rows.clear();
		DataColumnCollection collection = new DataColumnCollection(Tb);


		/*try
		{
			XSSFWorkbook xssfWorkbook = null;
			xssfWorkbook = new XSSFWorkbook(is);
			// 循环工作表Sheet , 目前支持一个
			// for (int i = 0; i < hssfWorkbook.getNumberOfSheets(); i++) {
			XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);
			if (xssfSheet == null)
			{
				return null;
			}
			// 循环行Row
			int row_size = xssfSheet.getPhysicalNumberOfRows();
			for (int j = 0; j < row_size; j++)
			{
				XSSFRow xssfRow = xssfSheet.getRow(j);
				if (xssfRow == null)
				{
					continue;
				}

				// 循环列Cell
				int call_num = xssfRow.getPhysicalNumberOfCells();
				// title
				if (0 == j)
				{
					for (int k = 0; k < call_num; k++)
					{
						XSSFCell xssfCell = xssfRow.getCell(k);
						if (null == xssfCell)
						{
							continue;
						}
						DataColumn column = new DataColumn(getValue(xssfCell));
						collection.Add(column);
					}
				} else
				{ // 内容
					DataRow dataRow = new DataRow(Tb);
					for (int k = 0; k < call_num; k++)
					{
						XSSFCell xssfCell = xssfRow.getCell(k);
						if (null == xssfCell)
						{
							continue;
						}
						dataRow.setValue(collection.get(k), getValue(xssfCell));
					}
					Tb.Rows.add(dataRow);
				}

			}
			Tb.Columns = collection;
		} catch (IOException e)
		{*/
			HSSFWorkbook xssfWorkbook = new HSSFWorkbook(is);
			// 循环工作表Sheet , 目前支持一个
			// for (int i = 0; i < hssfWorkbook.getNumberOfSheets(); i++) {
			HSSFSheet hssfSheet = xssfWorkbook.getSheetAt(0);
			if (hssfSheet == null)
			{
				return null;
			}
			// 循环行Row
			int row_size = hssfSheet.getPhysicalNumberOfRows();
			for (int j = 0; j < row_size; j++)
			{
				HSSFRow xssfRow = hssfSheet.getRow(j);
				if (xssfRow == null)
				{
					continue;
				}

				// 循环列Cell
				int call_num = xssfRow.getPhysicalNumberOfCells();
				// title
				if (0 == j)
				{
					for (int k = 0; k < call_num; k++)
					{
						HSSFCell xssfCell = xssfRow.getCell(k);
						if (null == xssfCell)
						{
							continue;
						}
						DataColumn column = new DataColumn(getValue(xssfCell));
						collection.Add(column);
					}
				} else
				{ // 内容
					DataRow dataRow = new DataRow(Tb);
					for (int k = 0; k < call_num; k++)
					{
						HSSFCell xssfCell = xssfRow.getCell(k);
						if (null == xssfCell)
						{
							continue;
						}
						dataRow.setValue(collection.get(k), getValue(xssfCell));
					}
					Tb.Rows.add(dataRow);
				}

			}
			Tb.Columns = collection;
		//}


		return Tb;
	}

	/**
	 * @param filePath
	 * @return
	 */
	public static DataTable GetTableByExt(String filePath)
	{
		FileInputStream stream = null;
		DataTable dataTable = null;
		try
		{

			stream = new FileInputStream(filePath);
			dataTable = GetTableByExt(stream);

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

		return dataTable;

	}
}