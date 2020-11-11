package bp.da;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
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
		} 
		if(hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC){
			// 获取单元格的样式值，即获取单元格格式对应的数值
			int style = hssfCell.getCellStyle().getDataFormat();
			// 判断是否是日期格式
			if (HSSFDateUtil.isCellDateFormatted(hssfCell)) {
				Date date = hssfCell.getDateCellValue();
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
					return  new DecimalFormat("0.00%").format(hssfCell.getNumericCellValue());

				// DateUtil判断其不是日期格式，在这里也可以设置其输出类型
				case 57:
					return new SimpleDateFormat(" yyyy'年'MM'月' ").format(hssfCell.getDateCellValue());

				default:
					hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING); 
					return String.valueOf(hssfCell.getStringCellValue());
				}
			}


		}
		//其余的格式设置成String，返回String
		hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);  
		// 返回字符串类型的值
		return String.valueOf(hssfCell.getStringCellValue());
		
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
		int row_size = hssfSheet.getPhysicalNumberOfRows();
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