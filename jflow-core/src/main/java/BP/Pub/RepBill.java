package BP.Pub;

import BP.DA.DBAccess;
import BP.DA.DataRow;
import BP.DA.DataTable;
import BP.Difference.SystemConfig;
import BP.Sys.PubClass;
import BP.Tools.ConvertTools;

public class RepBill extends BP.DTS.DataIOEn
{
	public RepBill()
	{
		this.Title = "WFV3.0单据自动修复线。";
	}
	
	@Override
	public void Do()
	{
		String msg = "";
		String sql = "  SELECT * FROM WF_BillTemplate";
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		for (DataRow dr : dt.Rows)
		{
			String file = SystemConfig.getPathOfCyclostyleFile()
					+ dr.getValue("URL").toString() + ".rtf";
			msg += RepBill.RepairBill(file);
		}
		
		 
	}
	
	public static String RepairBill(String file)
	{
		String msg = "";
		String docs = "";
		
		// 读取文件。
		try
		{
			docs = ConvertTools.StreamReaderToStringConvert(file, "ASCII");
		} catch (Exception ex)
		{
			ex.printStackTrace();
			return "@读取单据模板时出现错误。cfile=" + file + " @Ex=" + ex.getMessage();
			
		}
		
		// 修复。
		docs = RepairLineV2(docs);
		
		// 写入。
		try
		{
			ConvertTools.StreamWriteConvert(docs, file);
		} catch (Exception ex)
		{
			ex.printStackTrace();
			return "@写入单据模板时出现错误。cfile=" + file + " @Ex=" + ex.getMessage();
		}
		msg += "@单据:[" + file + "]成功修复。";
		return msg;
	}
	
	public static String RepairLine(String line) // str
	{
		char[] chs = line.toCharArray();
		String str = "";
		for (char ch : chs)
		{
			if (ch == '\\')
			{
				line = line.replace("\\" + str, "");
				str = "";
			} else if (ch == ' ')
			{
				// 如果等于空格， 直接替换原来的 str
				line = line.replace("\\" + str + ch, "");
				str = "sssssssssssssssssss";
			} else
			{
				str += (new Character(ch)).toString();
			}
		}
		
		line = line.replace("{", "");
		line = line.replace("}", "");
		line = line.replace("\r", "");
		line = line.replace("\n", "");
		line = line.replace(" ", "");
		line = line.replace("..", ".");
		return line;
	}
	
	/**
	 * RepairLineV2
	 * 
	 * @param docs
	 * @return
	 */
	public static String RepairLineV2(String docs) // str
	{
		char[] chars = docs.toCharArray();
		String strs = "";
		for (char c : chars)
		{
			if (c == '<')
			{
				strs = "<";
				continue;
			}
			if (c == '>')
			{
				strs += (new Character(c)).toString();
				String line = strs.toString();
				line = RepairLine(line);
				docs = docs.replace(strs, line);
				strs = "";
				continue;
			}
			strs += (new Character(c)).toString();
		}
		return docs;
	}
}