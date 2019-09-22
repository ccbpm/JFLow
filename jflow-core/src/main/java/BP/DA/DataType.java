package BP.DA;

import BP.Sys.*;
import BP.Web.*;
import java.util.*;
import java.io.*;
import java.time.*;
import java.math.*;

/** 
 DataType 的摘要说明。
*/
public class DataType
{
	public static boolean IsNullOrEmpty(String s)
	{
		if (s.equals("null"))
		{
			return true;
		}

		return DataType.IsNullOrEmpty(s);
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 与日期相关的操作.
	/** 
	 获得指定日期的周1第一天日期.
	 
	 @param dt 指定的日期
	 @return 
	*/
	public static LocalDateTime WeekOfMonday(LocalDateTime dt)
	{
		if (dt.getDayOfWeek() == DayOfWeek.MONDAY)
		{
			return DataType.ParseSysDate2DateTime(dt.toString("yyyy-MM-dd") + " 00:01");
		}

		for (int i = 0; i < 7; i++)
		{
			LocalDateTime mydt = dt.plusDays(-i);
			if (mydt.getDayOfWeek() == DayOfWeek.MONDAY)
			{
				return DataType.ParseSysDate2DateTime(mydt.toString("yyyy-MM-dd") + " 00:01");
			}
		}
		throw new RuntimeException("@系统错误.");
	}
	/** 
	 获得指定日期的周7第7天日期.
	 
	 @param dt 指定的日期
	 @return 
	*/
	public static LocalDateTime WeekOfSunday(LocalDateTime dt)
	{
		if (dt.getDayOfWeek() == DayOfWeek.SUNDAY)
		{
			return DataType.ParseSysDate2DateTime(dt.toString("yyyy-MM-dd") + " 00:01");
		}

		for (int i = 0; i < 7; i++)
		{
			LocalDateTime mydt = dt.plusDays(i);
			if (mydt.getDayOfWeek() == DayOfWeek.SUNDAY)
			{
				return DataType.ParseSysDate2DateTime(mydt.toString("yyyy-MM-dd") + " 00:01");
			}
		}
		throw new RuntimeException("@系统错误.");
	}
	/** 
	 增加日期去掉周末节假日
	 
	 @param dt 日期
	 @param days 增加的天数
	 @return 
	*/
	public static LocalDateTime AddDays(String dt, int days, TWay tway)
	{
		return AddDays(BP.DA.DataType.ParseSysDate2DateTime(dt), days, tway);
	}
	/** 
	 增加日期去掉周末
	 
	 @param dt
	 @param days
	 @return 返回天数
	*/
	public static LocalDateTime AddDays(LocalDateTime dt, int days, TWay tway)
	{
		if (tway == TWay.AllDays)
		{
			return dt.plusDays(days);
		}

		//没有设置节假日.
		if (BP.Sys.GloVar.getHolidays().equals(""))
		{
			// 2015年以前的算法.
			dt = dt.plusDays(days);
			if (dt.getDayOfWeek() == DayOfWeek.SATURDAY)
			{
				return dt.plusDays(2);
			}

			if (dt.getDayOfWeek() == DayOfWeek.SUNDAY)
			{
				return dt.plusDays(1);
			}
			return dt;
		}

		/* 设置节假日. */
		while (days > 0)
		{
			if (BP.Sys.GloVar.getHolidays().contains(dt.toString("MM-dd")))
			{
				dt = dt.plusDays(1);
				continue;
			}

			days--;

			if (days == 0)
			{
				break;
			}

			dt = dt.plusDays(1);
		}
		return dt;
	}
	/**  
	 取指定日期是一年中的第几周 
	  
	 @param dtime 给定的日期 
	 @return 数字 一年中的第几周 
	*/
	public static int WeekOfYear(LocalDateTime dtime)
	{
		int weeknum = 0;
		LocalDateTime tmpdate = LocalDateTime.parse(String.valueOf(dtime.getYear()) + "-1" + "-1");
		DayOfWeek firstweek = tmpdate.getDayOfWeek();
		//if(firstweek) 
		int i = dtime.getDayOfYear() - 1 + firstweek.getValue();
		weeknum = i / 7;
		if (i > 0)
		{
			weeknum++;
		}
		return weeknum;
	}
	public static String TurnToJiDuByDataTime(String dt)
	{
		if (dt.length() <= 6)
		{
			throw new RuntimeException("@要转化季度的日期格式不正确:" + dt);
		}
		String yf = dt.substring(5, 7);
		switch (yf)
		{
			case "01":
			case "02":
			case "03":
				return dt.substring(0, 4) + "-03";
			case "04":
			case "05":
			case "06":
				return dt.substring(0, 4) + "-06";
			case "07":
			case "08":
			case "09":
				return dt.substring(0, 4) + "-09";
			case "10":
			case "11":
			case "12":
				return dt.substring(0, 4) + "-12";
			default:
				break;
		}
		return null;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 将json转换为DataTable
	/** 
	 将json转换为DataTable
	 
	 @param json 得到的json
	 @return 
	*/
	public static DataTable JsonToDataTable(String json)
	{
		return BP.Tools.Json.ToDataTable(json);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region Datatable转换为Json
	/**      
	 Datatable转换为Json     
		 
	 @param table Datatable对象     
	 @return Json字符串     
	*/
	public static String ToJson(DataTable dt)
	{
		StringBuilder jsonString = new StringBuilder();
		jsonString.append("[");
		DataRowCollection drc = dt.Rows;
		if (drc.Count > 0)
		{
			for (int i = 0; i < drc.Count; i++)
			{
				jsonString.append("{");
				for (int j = 0; j < dt.Columns.Count; j++)
				{
					String strKey = dt.Columns[j].ColumnName;
					/**小周鹏修改-2014/11/11----------------------------START**/
					// BillNoFormat对应value:{YYYY}-{MM}-{dd}-{LSH4} Format时会产生异常。
					if (strKey.equals("BillNoFormat"))
					{
						continue;
					}
					/**小周鹏修改-2014/11/11----------------------------END**/
					String strValue = drc.get(i).get(j).toString();
					java.lang.Class type = dt.Columns[j].DataType;
					jsonString.append("\"" + strKey + "\":");

					strValue = String.format(strValue, type);
					if (j < dt.Columns.Count - 1)
					{
						jsonString.append("\"" + strValue + "\",");
					}
					else
					{
						jsonString.append("\"" + strValue + "\"");
					}
				}
				jsonString.append("},");
			}
			jsonString.deleteCharAt(jsonString.length() - 1);
		}
		jsonString.append("]");
		return jsonString.toString();
	}
	/**     
	 DataTable转换为Json     
	*/
	public static String ToJson(DataTable dt, String jsonName)
	{
		StringBuilder Json = new StringBuilder();
		if (DataType.IsNullOrEmpty(jsonName))
		{
			jsonName = dt.TableName;
		}
		Json.append("{\"" + jsonName + "\":[");
		if (dt.Rows.Count > 0)
		{
			for (int i = 0; i < dt.Rows.Count; i++)
			{
				Json.append("{");
				for (int j = 0; j < dt.Columns.Count; j++)
				{
					java.lang.Class type = dt.Rows[i][j].getClass();
					Json.append("\"" + dt.Columns[j].ColumnName.toString() + "\":" + String.format(dt.Rows[i][j].toString(), type));
					if (j < dt.Columns.Count - 1)
					{
						Json.append(",");
					}
				}
				Json.append("}");
				if (i < dt.Rows.Count - 1)
				{
					Json.append(",");
				}
			}
		}
		Json.append("]}");
		return Json.toString();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

	/** 
	 根据通用的树形结构生成行政机构树形结构
	 
	 @param dtTree 通用格式的数据表No,Name,ParentNo列
	 @param dtTree 根目录编号值
	 @return 
	*/
	public static DataTable PraseParentTree2TreeNo(DataTable dtTree, String parentNo)
	{
		DataTable dt = new DataTable();
		dt.Columns.Add("No", String.class);
		dt.Columns.Add("Name", String.class);
		dt.Columns.Add("Grade", String.class);
		dt.Columns.Add("IsDtl", String.class);

		dt.Columns.Add("RefNo", String.class);
		dt.Columns.Add("RefParentNo", String.class);

		dt = _PraseParentTree2TreeNo(dtTree, dt, parentNo);
		return dt;
	}
	private static DataTable _PraseParentTree2TreeNo(DataTable dtTree, DataTable newDt, String parentNo)
	{
		//记录已经转换的数据
		ArrayList<DataRow> removeRows = new ArrayList<DataRow>();
		//DataTable newDtTree = dtTree.Copy();

		//newDtTree.DefaultView.RowFilter = " ParentNo=" + parentNo;
		//newDtTree = newDtTree.DefaultView.ToTable();

		for (DataRow row : dtTree.Rows)
		{
			if (row.get("ParentNo").toString().equals(parentNo) || row.get("No").toString().equals(parentNo))
			{
				DataRow newRow = newDt.NewRow();

				newRow.set("No", row.get("No").toString());
				newRow.set("Name", row.get("Name"));
				newRow.set("IsDtl", "0");


				if (dtTree.Columns.Contains("Idx"))
				{
					newRow.set("Grade", row.get("Idx"));
				}
				if (dtTree.Columns.Contains("RefNo"))
				{
					newRow.set("RefNo", row.get("RefNo"));
				}
				else
				{
					newRow.set("RefNo", row.get("No"));
				}

				newRow.set("RefParentNo", row.get("ParentNo"));

				newDt.Rows.Add(newRow);
				removeRows.add(row);
			}

		}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 将原结构数据转换到新的datable 中
		//foreach (DataRow row in dtTree.Rows)
		//{
		//    if (newDt.Rows.Count == 0)
		//    {
		//        if (!row["IsRoot"].Equals("1"))
		//            continue;

		//        DataRow newRow = newDt.NewRow();

		//        newRow["No"] = row["No"];
		//        newRow["Name"] = row["Name"];
		//        newRow["Grade"] = row["Idx"];
		//        newRow["IsDtl"] = "";

		//        newRow["RefNo"] = row["RefNo"];
		//        newRow["RefParentNo"] = row["ParentNo"];

		//        newDt.Rows.Add(newRow);
		//        removeRows.Add(row);
		//    }
		//    else
		//    {
		//        foreach (DataRow newDtRow in newDt.Rows)
		//        {
		//            if (row["ParentNo"].Equals(newDtRow["No"]))
		//            {
		//                DataRow newRow = newDt.NewRow();

		//                newRow["No"] = row["No"];
		//                newRow["Name"] = row["Name"];
		//                newRow["Grade"] = row["Idx"];
		//                newRow["IsDtl"] = "";

		//                newRow["RefNo"] = row["RefNo"];
		//                newRow["RefParentNo"] = row["ParentNo"];

		//                newDt.Rows.Add(newRow);
		//                removeRows.Add(row);
		//            }
		//        }
		//    }
		//}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 将原结构数据转换到新的datable 中

		//移除已经转换的数据
		for (DataRow row : removeRows)
		{
			dtTree.Rows.Remove(row);
		}
		//如果原结构中还有数据就接着转换
		if (dtTree.Rows.Count != 0)
		{
			_PraseParentTree2TreeNo(dtTree, newDt, dtTree.Rows[0]["No"].toString());
		}
		return newDt;
	}
	public static String PraseGB2312_To_utf8(String text)
	{
		/* 2019-8-12 前后端全部采用utf8，无需转码
		if (DataType.IsNullOrEmpty(text))
		    return text;

		//声明字符集   
		System.Text.Encoding utf8, gb2312;
		//gb2312   
		gb2312 = System.Text.Encoding.GetEncoding("gb2312");
		//utf8   
		utf8 = System.Text.Encoding.GetEncoding("utf-8");
		byte[] gb;
		gb = gb2312.GetBytes(text);
		gb = System.Text.Encoding.Convert(gb2312, utf8, gb);
		//返回转换后的字符   
		return utf8.GetString(gb);
		*/
		return text;
	}

	/** 
	 转换成MB
	 
	 @param val
	 @return 
	*/
	public static float PraseToMB(long val)
	{
		try
		{
//C# TO JAVA CONVERTER TODO TASK: The '0:##.##' format specifier is not converted to Java:
			return Float.parseFloat(String.format("{0:##.##}", val / 1048576));
		}
		catch (java.lang.Exception e)
		{
			return 0;
		}
	}
	public static String PraseStringToUrlFileName(String fileName)
	{

		if (fileName.lastIndexOf('\\') == -1)
		{
			fileName = PraseStringToUrlFileNameExt(fileName, "%", "%25");
			fileName = PraseStringToUrlFileNameExt(fileName, "+", "%2B");
			fileName = PraseStringToUrlFileNameExt(fileName, " ", "%20");
			fileName = PraseStringToUrlFileNameExt(fileName, "/", "%2F");
			fileName = PraseStringToUrlFileNameExt(fileName, "?", "%3F");
			fileName = PraseStringToUrlFileNameExt(fileName, "#", "%23");
			fileName = PraseStringToUrlFileNameExt(fileName, "&", "%26");
			fileName = PraseStringToUrlFileNameExt(fileName, "=", "%3D");
			fileName = PraseStringToUrlFileNameExt(fileName, " ", "%20");
			return fileName;
		}

		//HttpUtility.HtmlEncode(fileName);

		String filePath = fileName.substring(0, fileName.lastIndexOf('\\'));
		String fName = fileName.substring(fileName.lastIndexOf('\\'));
		// fName = HttpUtility.HtmlEncode(fName);
		//if (1 == 2)
		//{
		fName = PraseStringToUrlFileNameExt(fName, "%", "%25");
		fName = PraseStringToUrlFileNameExt(fName, "+", "%2B");
		fName = PraseStringToUrlFileNameExt(fName, " ", "%20");
		fName = PraseStringToUrlFileNameExt(fName, "/", "%2F");
		fName = PraseStringToUrlFileNameExt(fName, "?", "%3F");
		fName = PraseStringToUrlFileNameExt(fName, "#", "%23");
		fName = PraseStringToUrlFileNameExt(fName, "&", "%26");
		fName = PraseStringToUrlFileNameExt(fName, "=", "%3D");
		fName = PraseStringToUrlFileNameExt(fName, " ", "%20");

		// }
		return filePath + fName;
	}
	private static String PraseStringToUrlFileNameExt(String fileName, String val, String replVal)
	{
		fileName = fileName.replace(val, replVal);
		fileName = fileName.replace(val, replVal);
		fileName = fileName.replace(val, replVal);
		fileName = fileName.replace(val, replVal);
		fileName = fileName.replace(val, replVal);
		fileName = fileName.replace(val, replVal);
		fileName = fileName.replace(val, replVal);
		fileName = fileName.replace(val, replVal);
		return fileName;
	}
	/** 
	 处理文件名称
	 
	 @param fileNameFormat 文件格式
	 @return 返回合法的文件名
	*/
	public static String PraseStringToFileName(String fileNameFormat)
	{
		char[] strs = "+#?*\"<>/;,-:%~".toCharArray();
		for (char c : strs)
		{
			fileNameFormat = fileNameFormat.replace(String.valueOf(c), "_");
		}

		strs = "：，。；？".toCharArray();
		for (char c : strs)
		{
			fileNameFormat = fileNameFormat.replace(String.valueOf(c), "_");
		}

		//去掉空格.
		while (fileNameFormat.contains(" ") == true)
		{
			fileNameFormat = fileNameFormat.replace(" ", "");
		}

		//替换特殊字符.
		fileNameFormat = fileNameFormat.replace("\t\n", "");

		//处理合法的文件名.
		StringBuilder rBuilder = new StringBuilder(fileNameFormat);
		for (char rInvalidChar : Path.GetInvalidFileNameChars())
		{
			rBuilder.Replace(String.valueOf(rInvalidChar), "");
		}

		fileNameFormat = rBuilder.toString();

		fileNameFormat = fileNameFormat.replace("__", "_");
		fileNameFormat = fileNameFormat.replace("__", "_");
		fileNameFormat = fileNameFormat.replace("__", "_");
		fileNameFormat = fileNameFormat.replace("__", "_");
		fileNameFormat = fileNameFormat.replace("__", "_");
		fileNameFormat = fileNameFormat.replace("__", "_");
		fileNameFormat = fileNameFormat.replace("__", "_");
		fileNameFormat = fileNameFormat.replace("__", "_");
		fileNameFormat = fileNameFormat.replace(" ", "");
		fileNameFormat = fileNameFormat.replace(" ", "");
		fileNameFormat = fileNameFormat.replace(" ", "");
		fileNameFormat = fileNameFormat.replace(" ", "");
		fileNameFormat = fileNameFormat.replace(" ", "");
		fileNameFormat = fileNameFormat.replace(" ", "");
		fileNameFormat = fileNameFormat.replace(" ", "");
		fileNameFormat = fileNameFormat.replace(" ", "");

		if (fileNameFormat.length() > 240)
		{
			fileNameFormat = fileNameFormat.substring(0, 240);
		}

		return fileNameFormat;
	}
	/** 
	 
	 
	 @param strs
	 @param isNumber
	 @return 
	*/
	public static String PraseAtToInSql(String strs, boolean isNumber)
	{
		if (DataType.IsNullOrEmpty(strs) == true)
		{
			return "''";
		}
		strs = strs.replace("@", "','");
		strs = strs + "'";
		if (strs.length() > 2)
		{
			strs = strs.substring(2);
		}
		if (isNumber)
		{
			strs = strs.replace("'", "");
		}
		return strs;
	}
	/** 
	 将文件转化为二进制
	 
	 @param fileName
	 @return 
	*/
//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: public static byte[] ConvertFileToByte(string fileName)
	public static byte[] ConvertFileToByte(String fileName)
	{
		FileInputStream fs = new FileInputStream(fileName);

//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: byte[] nowByte = new byte[(int)fs.Length];
		byte[] nowByte = new byte[(int)fs.Length];
		try
		{
			fs.read(nowByte, 0, (int)fs.Length);
			return nowByte;
		}
		catch (RuntimeException ex)
		{
			throw ex;
		}
		finally
		{
			fs.close();
		}
	}
	/** 
	 写文件
	 
	 @param file 路径
	 @param Doc 内容
	*/
	public static void WriteFile(String file, String Doc)
	{
		OutputStreamWriter sr;
		if ((new File(file)).isFile())
		{
			(new File(file)).delete();
		}
		try
		{
			//sr = new System.IO.StreamWriter(file, false, System.Text.Encoding.GetEncoding("GB2312"));
//C# TO JAVA CONVERTER WARNING: The java.io.OutputStreamWriter constructor does not accept all the arguments passed to the System.IO.StreamWriter constructor:
//ORIGINAL LINE: sr = new System.IO.StreamWriter(file, false, System.Text.Encoding.UTF8);
			sr = new OutputStreamWriter(file, java.nio.charset.StandardCharsets.UTF_8);
		}
		catch (RuntimeException ex)
		{
			throw new RuntimeException("@文件：" + file + ",错误:" + ex.getMessage());
		}

		sr.write(Doc);
		sr.close();
	}
	/** 
	 写入一个文件
	 
	 @param filePathName
	 @param objData
	 @return 
	*/
//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: public static string WriteFile(string filePathName, byte[] objData)
	public static String WriteFile(String filePathName, byte[] objData)
	{
		String folder = (new File(filePathName)).getParent();
		if ((new File(folder)).isDirectory() == false)
		{
			(new File(folder)).mkdirs();
		}

		if ((new File(filePathName)).isFile() == true)
		{
			(new File(filePathName)).delete();
		}

		FileOutputStream fs = new FileOutputStream(filePathName);
		System.IO.BinaryWriter w = new System.IO.BinaryWriter(fs);
		try
		{
			w.Write(objData);
		}
		catch (RuntimeException ex)
		{
			throw ex;
		}
		finally
		{
			w.Close();
			fs.close();
		}
		return filePathName;
	}
	/** 
	 Http下载文件
	*/
	public static String HttpDownloadFile(String url, String path)
	{
		// 设置参数
		Object tempVar = WebRequest.Create(url);
		HttpWebRequest request = tempVar instanceof HttpWebRequest ? (HttpWebRequest)tempVar : null;

		//发送请求并获取相应回应数据
		Object tempVar2 = request.GetResponse();
		HttpWebResponse response = tempVar2 instanceof HttpWebResponse ? (HttpWebResponse)tempVar2 : null;
		//直到request.GetResponse()程序才开始向目标网页发送Post请求
		InputStream responseStream = response.GetResponseStream();

		//创建本地文件写入流
//C# TO JAVA CONVERTER TODO TASK: C# to Java Converter cannot determine whether this System.IO.FileStream is input or output:
		OutputStream stream = new FileStream(path, FileMode.Create);

//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: byte[] bArr = new byte[1024];
		byte[] bArr = new byte[1024];
		int size = responseStream.read(bArr, 0, (int)bArr.length);
		while (size > 0)
		{
			stream.write(bArr, 0, size);
			size = responseStream.read(bArr, 0, (int)bArr.length);
		}
		stream.close();
		responseStream.close();
		return path;
	}
	/** 
	 读取URL内容
	 
	 @param url 要读取的url
	 @param timeOut 超时时间
	 @param encode text code.
	 @return 返回读取内容
	*/
	public static String ReadURLContext(String url, int timeOut, Encoding encode)
	{
		HttpWebRequest webRequest = null;
		try
		{
			webRequest = (HttpWebRequest)WebRequest.Create(url);
			webRequest.Method = "get";
			webRequest.Timeout = timeOut;
			String str = webRequest.Address.AbsoluteUri;
			str = str.substring(0, str.lastIndexOf("/"));
		}
		catch (RuntimeException ex)
		{
			try
			{
				BP.DA.Log.DefaultLogWriteLineWarning("@读取URL出现错误:URL=" + url + "@错误信息：" + ex.getMessage());
				return null;
			}
			catch (java.lang.Exception e)
			{
				return ex.getMessage();
			}
		}
		//	因为它返回的实例类型是WebRequest而不是HttpWebRequest,因此记得要进行强制类型转换
		//  接下来建立一个HttpWebResponse以便接收服务器发送的信息，它是调用HttpWebRequest.GetResponse来获取的：
		HttpWebResponse webResponse;
		try
		{
			webResponse = (HttpWebResponse)webRequest.GetResponse();
		}
		catch (RuntimeException ex)
		{
			try
			{
				// 如果出现死连接。
				BP.DA.Log.DefaultLogWriteLineWarning("@获取url=" + url + "失败。异常信息:" + ex.getMessage(), true);
				return null;
			}
			catch (java.lang.Exception e2)
			{
				return ex.getMessage();
			}
		}

		//如果webResponse.StatusCode的值为HttpStatusCode.OK，表示成功，那你就可以接着读取接收到的内容了：
		// 获取接收到的流
		InputStream stream = webResponse.GetResponseStream();
//C# TO JAVA CONVERTER WARNING: The java.io.InputStreamReader constructor does not accept all the arguments passed to the System.IO.StreamReader constructor:
//ORIGINAL LINE: System.IO.StreamReader streamReader = new StreamReader(stream, encode);
		InputStreamReader streamReader = new InputStreamReader(stream);
		String content = streamReader.ReadToEnd();
		webResponse.Close();
		return content;
	}
	/** 
	 读取文件
	 
	 @param file 路径
	 @return 内容
	*/
	public static String ReadTextFile(String file)
	{
		InputStreamReader read = new InputStreamReader(file, java.nio.charset.StandardCharsets.UTF_8); // 文件流.
		String doc = read.ReadToEnd(); //读取完毕。
		read.close(); // 关闭。
		return doc;
	}

	/** 
	 读取Xml文件信息,并转换成DataSet对象
	 
	 
	 DataSet ds = new DataSet();
	 ds = CXmlFileToDataSet("/XML/upload.xml");
	 
	 @param xmlFilePath Xml文件地址
	 @return DataSet对象
	*/
	public static DataSet CXmlFileToDataSet(String xmlFilePath)
	{
		if (!DataType.IsNullOrEmpty(xmlFilePath))
		{
			//string path = HttpContext.Current.Server.MapPath(xmlFilePath);
			StringReader StrStream = null;
			XmlTextReader Xmlrdr = null;
			try
			{
				XmlDocument xmldoc = new XmlDocument();
				//根据地址加载Xml文件
				xmldoc.Load(xmlFilePath);

				DataSet ds = new DataSet();
				//读取文件中的字符流
				StrStream = new StringReader(xmldoc.InnerXml);
				//获取StrStream中的数据
				Xmlrdr = new XmlTextReader(StrStream);
				//ds获取Xmlrdr中的数据
				ds.ReadXml(Xmlrdr);
				return ds;
			}
			catch (RuntimeException e)
			{
				throw e;
			}
			finally
			{
				//释放资源
				if (Xmlrdr != null)
				{
					Xmlrdr.Close();
					StrStream.Close();
					StrStream.Dispose();
				}
			}
		}
		else
		{
			return null;
		}
	}
	public static boolean SaveAsFile(String filePath, String doc)
	{
//C# TO JAVA CONVERTER WARNING: The java.io.OutputStreamWriter constructor does not accept all the arguments passed to the System.IO.StreamWriter constructor:
//ORIGINAL LINE: System.IO.StreamWriter sw = new System.IO.StreamWriter(filePath, false);
		OutputStreamWriter sw = new OutputStreamWriter(filePath);
		sw.write(doc);
		sw.close();
		return true;
	}
	public static String ReadTextFile2Html(String file)
	{
		return DataType.ParseText2Html(ReadTextFile(file));
	}
	/** 
	 判断是否全部是汉字
	 
	 @param htmlstr
	 @return 
	*/
	public static boolean CheckIsChinese(String htmlstr)
	{
		char[] chs = htmlstr.toCharArray();
		for (char c : chs)
		{
			int i = String.valueOf(c).length();
			if (i == 1)
			{
				return false;
			}
		}
		return true;
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 元角分
	public static String TurnToFiels(float money)
	{
		String je = (new Float(money)).toString("0.00");

		String strs = "";

		switch (je.length())
		{
			case 7: //         千                                百                                  十                              元                                角                              分;
				strs = "D" + je.substring(0, 1) + ".TW,THOU.TW,D" + je.substring(1, 2) + ".TW,HUN.TW,D" + je.substring(2, 3) + ".TW,TEN.TW,D" + je.substring(3, 4) + ".TW,YUAN.TW,D" + je.substring(5, 6) + ".TW,JIAO.TW,D" + je.substring(6, 7) + ".TW,FEN.TW";
				break;
			case 6: // 百;
				strs = "D" + je.substring(0, 1) + ".TW,HUN.TW,D" + je.substring(1, 2) + ".TW,TEN.TW,D" + je.substring(2, 3) + ".TW,YUAN.TW,D" + je.substring(4, 5) + ".TW,JIAO.TW,D" + je.substring(5, 6) + ".TW,FEN.TW";
				break;
			case 5: // 十;
				strs = "D" + je.substring(0, 1) + ".TW,TEN.TW,D" + je.substring(1, 2) + ".TW,YUAN.TW,D" + je.substring(3, 4) + ".TW,JIAO.TW,D" + je.substring(4, 5) + ".TW,FEN.TW";
				break;
			case 4: // 元;
				if (money > 1)
				{
					strs = "D" + je.substring(0, 1) + ".TW,YUAN.TW,D" + je.substring(2, 3) + ".TW,JIAO.TW,D" + je.substring(3, 4) + ".TW,FEN.TW";
				}
				else
				{
					strs = "D" + je.substring(2, 3) + ".TW,JIAO.TW,D" + je.substring(3, 4) + ".TW,FEN.TW";
				}
				break;
			default:
				throw new RuntimeException("没有涉及到这么大的金额播出");
		}

		//			strs=strs.Replace(",D0.TW,JIAO.TW,D0.TW,FEN.TW",""); // 替换掉 .0角0分;
		//			strs=strs.Replace("D0.TW,HUN.TW,D0.TW,TEN.TW","D0.TW"); // 替换掉 .0百0十 为 0 ;
		//			strs=strs.Replace("D0.TW,THOU.TW","D0.TW");  // 替换掉零千。
		//			strs=strs.Replace("D0.TW,HUN.TW","D0.TW");
		//			strs=strs.Replace("D0.TW,TEN.TW","D0.TW");
		//			strs=strs.Replace("D0.TW,JIAO.TW","D0.TW");
		//			strs=strs.Replace("D0.TW,FEN.TW","D0.TW");
		return strs;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

	public static String Html2Text(String htmlstr)
	{
		return System.Text.RegularExpressions.Regex.Replace(htmlstr, "<[^>]*>", "");
	}
//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: public static string ByteToString(byte[] bye)
	public static String ByteToString(byte[] bye)
	{
		String s = "";
//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: foreach (byte b in bye)
		for (byte b : bye)
		{
			s += String.valueOf(b);
		}
		return s;
	}
//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: public static byte[] StringToByte(string s)
	public static byte[] StringToByte(String s)
	{
//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: byte[] bs = new byte[s.Length];
		byte[] bs = new byte[s.length()];
		char[] cs = s.toCharArray();
		int i = 0;
		for (char c : cs)
		{
//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: bs[i] = Convert.ToByte(c);
			bs[i] = (byte)c;
			i++;
		}
		return bs;
	}
	/** 
	 取道百分比
	 
	 @param a
	 @param b
	 @return 
	*/
	public static String GetPercent(BigDecimal a, BigDecimal b)
	{
		BigDecimal p = a.divide(b);
		return p.toString("0.00%");
	}
	public static String GetWeek(int weekidx)
	{
		switch (weekidx)
		{
			case 0:
				return "星期日";
			case 1:
				return "星期一";
			case 2:
				return "星期二";
			case 3:
				return "星期三";
			case 4:
				return "星期四";
			case 5:
				return "星期五";
			case 6:
				return "星期六";
			default:
				throw new RuntimeException("error weekidx=" + weekidx);
		}
	}

	public static String GetABC(String abc)
	{
		switch (abc)
		{
			case "A":
				return "B";
			case "B":
				return "C";
			case "C":
				return "D";
			case "D":
				return "E";
			case "E":
				return "F";
			case "F":
				return "G";
			case "G":
				return "H";
			case "H":
				return "I";
			case "I":
				return "J";
			case "J":
				return "K";
			case "K":
				return "L";
			case "L":
				return "M";
			case "M":
				return "N";
			case "N":
				return "O";
			case "Z":
				return "A";
			default:
				throw new RuntimeException("abc error" + abc);
		}
	}
	public static String GetBig5(String text)
	{
		System.Text.Encoding e2312 = System.Text.Encoding.GetEncoding("GB2312");
//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: byte[] bs = e2312.GetBytes(text);
		byte[] bs = e2312.GetBytes(text);
		System.Text.Encoding e5 = System.Text.Encoding.GetEncoding("Big5");
//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: byte[] bs5 = System.Text.Encoding.Convert(e2312, e5, bs);
		byte[] bs5 = System.Text.Encoding.Convert(e2312, e5, bs);
		return e5.GetString(bs5);
	}
	/** 
	 返回 data1 - data2 的天数.
	 
	 @param data1 fromday
	 @param data2 today
	 @return 相隔的天数
	*/
	public static int SpanDays(String fromday, String today)
	{
		try
		{
			TimeSpan span = LocalDateTime.parse(today.substring(0, 10)) - LocalDateTime.parse(fromday.substring(0, 10));
			return span.Days;
		}
		catch (java.lang.Exception e)
		{
			//throw new Exception(ex.Message +"" +fromday +"  " +today ) ; 
			return 0;
		}
	}

	public static int SpanHours(String fromday, String today)
	{
		LocalDateTime span = LocalDateTime.parse(today) - LocalDateTime.parse(fromday);
//C# TO JAVA CONVERTER TODO TASK: There is no equivalent to implicit typing in Java unless the Java 10 inferred typing option is selected:
		var days = span.Days;
		return days;
	}
	/** 
	 返回 QuarterFrom - QuarterTo 的季度.
	 
	 @param QuarterFrom QuarterFrom
	 @param QuarterTo QuarterTo
	 @return 相隔的季度
	*/
	public static int SpanQuarter(String _APFrom, String _APTo)
	{
		LocalDateTime fromdate = LocalDateTime.parse(_APFrom + "-01");
		LocalDateTime todate = LocalDateTime.parse(_APTo + "-01");
		int i = 0;
		if (fromdate.compareTo(todate) > 0)
		{
			throw new RuntimeException("选择出错！起始时期" + _APFrom + "不能大于终止时期" + _APTo + "!");
		}

		while (fromdate.compareTo(todate) <= 0)
		{
			i++;
			fromdate = fromdate.plusMonths(1);
		}

		int j = (i + 2) / 3;
		return j;
	}
	/** 
	 到现在的天数。
	 
	 @param data1
	 @return 
	*/
	public static int SpanDays(String data1)
	{
		TimeSpan span = LocalDateTime.now() - LocalDateTime.parse(data1.substring(0, 10));
		return span.Days;
	}
	/** 
	 检查是否是一个字段或者表名称
	 
	 @param str 要检查的字段或者表名称
	 @return 是否合法
	*/
	public static boolean CheckIsFieldOrTableName(String str)
	{
		String s = str.substring(0, 1);
		if (DataType.IsNumStr(s))
		{
			return false;
		}

		String chars = "~!@#$%^&*()_+`{}|:'<>?[];',./";
		if (chars.contains(s) == true)
		{
			return false;
		}
		return true;
	}
	public static String ParseText2Html(String val)
	{
		//val = val.Replace("&", "&amp;");
		//val = val.Replace("<","&lt;");
		//val = val.Replace(">","&gt;");

		//val = val.Replace(char(34), "&quot;");
		//val = val.Replace(char(9), "&nbsp;&nbsp;&nbsp;");
		//val = val.Replace(" ", "&nbsp;");

		return val.replace("\n", "<BR>").replace("~", "'");

		//return val.Replace("\n", "<BR>&nbsp;&nbsp;").Replace("~", "'");

	}
	public static String ParseHtmlToText(String val)
	{
		if (val == null)
		{
			return val;
		}

		val = val.replace("&nbsp;", " ");
		val = val.replace("  ", " ");

		val = val.replace("</td>", "");
		val = val.replace("</TD>", "");

		val = val.replace("</tr>", "");
		val = val.replace("</TR>", "");

		val = val.replace("<tr>", "");
		val = val.replace("<TR>", "");

		val = val.replace("</font>", "");
		val = val.replace("</FONT>", "");

		val = val.replace("</table>", "");
		val = val.replace("</TABLE>", "");


		val = val.replace("<BR>", "\n\t");
		val = val.replace("<BR>", "\n\t");
		val = val.replace("&nbsp;", " ");

		val = val.replace("<BR><BR><BR><BR>", "<BR><BR>");
		val = val.replace("<BR><BR><BR><BR>", "<BR><BR>");
		val = val.replace("<BR><BR>", "<BR>");

		char[] chs = val.toCharArray();

		boolean isStartRec = false;
		String recStr = "";
		for (char c : chs)
		{
			if (c == '<')
			{
				recStr = "";
				isStartRec = true; // 开始记录
			}

			if (isStartRec)
			{
				recStr += String.valueOf(c);
			}

			if (c == '>')
			{
				isStartRec = false;

				if (recStr.equals(""))
				{
					isStartRec = false;
					continue;
				}

				/* 开始分析这个标记内的东西。*/
				String market = recStr.toLowerCase();
				if (market.contains("<img"))
				{
					/* 这是一个图片标记 */
					isStartRec = false;
					recStr = "";
					continue;
				}
				else
				{
					val = val.replace(recStr, "");
					isStartRec = false;
					recStr = "";
				}
			}
		}


		val = val.replace("字体：大中小", "");
		val = val.replace("字体:大中小", "");

		val = val.replace("  ", " ");
		val = val.replace("\t", "");
		val = val.replace("\n", "");
		val = val.replace("\r", "");
		return val;
	}
	/** 
	 将文本转换成可用做Name,Text的文本，文本中仅允许含有汉字、字母、数字、下划线
	 
	 @param nameStr 待转换的文本
	 @param maxLen 文本最大长度，0为不限制，超过maxLen，截取前maxLen字符长度
	 @return 
	*/
	public static String ParseStringForName(String nameStr, int maxLen)
	{
		if (tangible.StringHelper.isNullOrWhiteSpace(nameStr))
		{
			return "";
		}

		String nStr = Regex.Replace(nameStr, RegEx_Replace_OnlyHSZX, "");

		if (maxLen > 0 && nStr.length() > maxLen)
		{
			return nStr.substring(0, maxLen);
		}

		return nStr;
	}
	/** 
	 将文本转换成可用做No的文本，文本中仅允许含有字母、数字、下划线，且开头只能是字母
	 
	 @param noStr 待转换的文本
	 @param maxLen 文本最大长度，0为不限制，超过maxLen，截取前maxLen字符长度
	 @return 
	*/
	public static String ParseStringForNo(String noStr, int maxLen)
	{
		if (tangible.StringHelper.isNullOrWhiteSpace(noStr))
		{
			return "";
		}

		String nStr = Regex.Replace(Regex.Replace(noStr, RegEx_Replace_OnlySZX, ""), RegEx_Replace_FirstXZ, "");

		if (maxLen > 0 && nStr.length() > maxLen)
		{
			return nStr.substring(0, maxLen);
		}

		return nStr;
	}
	/** 
	 去除指定字符串中非数字的字符
	 
	 @param str 字符串
	 @return 
	*/
	public static String ParseStringOnlyIntNumber(String str)
	{
		if (tangible.StringHelper.isNullOrWhiteSpace(str))
		{
			return "";
		}

		return Regex.Replace(str, RegEx_Replace_OnlyIntNum, "");
	}
	/** 
	 去除指定字符串中危险字符
	 <p>注：含有这些字符的参数经过拼接，组成SQL可能包含危险语句</p>
	 <p>涉及字符：' ; -- / &amp; &gt; &lt;</p>
	 
	 @param str 字符串
	 @return 
	*/
	public static String ParseStringFilterDangerousSymbols(String str)
	{
		if (tangible.StringHelper.isNullOrWhiteSpace(str))
		{
			return "";
		}

		return Regex.Replace(str, RegEx_Replace_FilterDangerousSymbols, "").replace("<", "&lt;").replace(">", "&gt;").replace("&", "&amp;");
	}
	/** 
	 将中文转化成拼音
	 
	 @param exp
	 @return 
	*/
	public static String ParseStringToPinyin(String exp)
	{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 特殊字符处理.
		switch (exp)
		{
			case "电话":
				return "Tel";
			case "地址":
				return "Addr";
			case "年龄":
				return "Age";
			case "邮件":
				return "Email";
			case "单价":
				return "DanJia";
			case "金额":
				return "JinE";
			case "单据编号":
				return "BillNo";
			default:
				break;
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 特殊字符处理.

		//特殊处理. 
		exp = exp.replace("单价", "DanJia");
		exp = exp.replace("单件", "DanJian");
		exp = exp.replace("单个", "DanGe");

		exp = exp.trim();
		String pinYin = "", str = null;
		char[] chars = exp.toCharArray();
		for (char c : chars)
		{
			try
			{
				str = CCFormAPI.ChinaMulTonesToPinYin(String.valueOf(c));
				if (str == null)
				{
					str = BP.Tools.chs2py.convert(String.valueOf(c));
				}
				pinYin += str.substring(0, 1).toUpperCase() + str.substring(1);
			}
			catch (java.lang.Exception e)
			{
				pinYin += c;
			}
		}
		return pinYin;
	}
	/** 
	 转化成拼音第一个字母大字
	 
	 @param str 要转化的中文串
	 @return 拼音
	*/
	public static String ParseStringToPinyinWordFirst(String str)
	{
		try
		{
			String _Temp = "";
			for (int i = 0; i < str.length(); i++)
			{
				_Temp = _Temp + BP.DA.DataType.ParseStringToPinyin(str.substring(i, i + 1));
			}
			return _Temp;
		}
		catch (RuntimeException ex)
		{
			throw new RuntimeException("@错误：" + str + "，不能转换成拼音。");
		}
	}
	/** 
	 转化成拼音第一个字母大字
	 
	 @param str 要转化的中文串
	 @return 拼音
	*/
	public static String ParseStringToPinyinJianXie(String str)
	{
		try
		{
			String _Temp = null;
			String re = "";
			for (int i = 0; i < str.length(); i++)
			{
				re = BP.DA.DataType.ParseStringToPinyin(str.substring(i, i + 1));
				_Temp += re.length() == 0 ? "" : re.substring(0, 1);
			}
			return _Temp;
		}
		catch (RuntimeException ex)
		{
			throw new RuntimeException("@错误：" + str + "，不能转换成拼音。");
		}
	}
	/** 
	 转化成 decimal
	 
	 @param exp
	 @return 
	*/
	public static BigDecimal ParseExpToDecimal(String exp)
	{
		if (exp.trim().equals(""))
		{
			throw new RuntimeException("DataType.ParseExpToDecimal要转换的表达式为空。");
		}


		exp = exp.replace("+-", "-");
		exp = exp.replace("￥", "");
		//exp=exp.Replace(" ",""); 不能替换，因为有sql表达公式时间，会出现错误。
		exp = exp.replace("\n", "");
		exp = exp.replace("\t", "");

		exp = exp.replace("＋", "+");
		exp = exp.replace("－", "-");
		exp = exp.replace("＊", "*");
		exp = exp.replace("／", "/");
		exp = exp.replace("）", ")");
		exp = exp.replace("（", "(");

		exp = exp.replace(".00.00", "00");

		exp = exp.replace("--", "- -");


		if (exp.indexOf("@") != -1)
		{
			return 0;
		}

		String val = exp.substring(0, 1);
		if (val.equals("-"))
		{
			exp = exp.substring(1);
		}

		//  exp = exp.Replace("*100%", "*100");

		exp = exp.replace("*100%", "*1");

		try
		{
			return BigDecimal.Parse(exp);
		}
		catch (java.lang.Exception e)
		{
		}

		try
		{
			String sql = "SELECT  " + exp + " as Num  ";
			switch (SystemConfig.getAppCenterDBType())
			{
				case MSSQL:
				case Access:
					sql = "SELECT  " + exp + " as Num  ";
					return DBAccess.RunSQLReturnValDecimal(sql, 0, 2);
				case Oracle:
					sql = "SELECT  " + exp + " NUM from DUAL ";
					return DBAccess.RunSQLReturnValDecimal(sql, 0, 2);
				case Informix:
					sql = "SELECT  " + exp + " NUM from  taa_onerow ";
					return DBAccess.RunSQLReturnValDecimal(sql, 0, 2);
				default:
					break;
			}
		}
		catch (RuntimeException ex)
		{
			Log.DefaultLogWriteLineInfo(ex.getMessage());
			/* 如果抛出异常就按 0  计算。 */
			return 0;
		}

		exp = exp.replace("-0", "");


		try
		{
			BP.Tools.StringExpressionCalculate sc = new BP.Tools.StringExpressionCalculate();
			return sc.TurnToDecimal(exp);
		}
		catch (RuntimeException ex)
		{
			if (exp.indexOf("/") != -1)
			{
				return 0;
			}
			throw new RuntimeException("表达式(\"" + exp + "\")计算错误：" + ex.getMessage());
		}
	}
	public static String ParseFloatToCash(float money)
	{
		if (money == 0)
		{
			return "零圆零角零分";
		}
		BP.Tools.DealString d = new BP.Tools.DealString();
		d.setInputString(String.valueOf(money));
		d.ConvertToChineseNum();
		return d.getOutString();
	}
	public static String ParseFloatToRMB(float money)
	{
		if (money == 0)
		{
			return "零圆零角零分";
		}
		BP.Tools.DealString d = new BP.Tools.DealString();
		d.setInputString(String.valueOf(money));
		d.ConvertToChineseNum();
		return d.getOutString();
	}
	/** 
	 得到一个日期,根据系统
	 
	 @param dataStr
	 @return 
	*/
	public final LocalDateTime Parse(String dataStr)
	{
		return LocalDateTime.parse(dataStr);
	}
	/** 
	 系统定义的时间格式 yyyy-MM-dd .
	*/
	public static String getSysDataFormat()
	{
		return "yyyy-MM-dd";
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 与周相关.
	/** 
	 当前周次
	*/
	public static int getCurrentWeek()
	{
		System.Globalization.GregorianCalendar gc = new System.Globalization.GregorianCalendar();
		int weekOfYear = gc.GetWeekOfYear(LocalDateTime.now(), System.Globalization.CalendarWeekRule.FirstDay, DayOfWeek.MONDAY);
		return weekOfYear;
	}
	/** 
	 根据一个日期，获得该日期属于一年的第几周.
	 
	 @param dataTimeStr 日期时间串，要符合bp格式.
	 @return 该日期属于所在年度的第几周
	*/
	public static int CurrentWeekGetWeekByDay(String dataTimeStr)
	{
		System.Globalization.GregorianCalendar gc = new System.Globalization.GregorianCalendar();
		int weekOfYear = gc.GetWeekOfYear(DataType.ParseSysDate2DateTime(dataTimeStr), System.Globalization.CalendarWeekRule.FirstDay, DayOfWeek.MONDAY);
		return weekOfYear;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion
	/** 
	 格式化日期类型
	 
	 @param dataStr 日期字符串
	 @return 标准的日期类型
	*/
	public static String FormatDataTime(String dataStr)
	{

		return dataStr;
	}
	/** 
	 当前的日期
	*/
	public static String getCurrentData()
	{
		return LocalDateTime.now().toString(DataType.getSysDataFormat());
	}
	public static String getCurrentTime()
	{
		return LocalDateTime.now().toString("hh:mm");
	}
	/** 
	 
	*/
	public static String getCurrentTimeQuarter()
	{
		return LocalDateTime.now().toString("hh:mm");
	}
	/** 
	 给一个时间，返回一个刻种时间。
	 
	 @param time
	 @return 
	*/
	public static String ParseTime2TimeQuarter(String time)
	{
		String hh = time.substring(0, 3);
		int mm = Integer.parseInt(time.substring(3, 5));
		if (mm == 0)
		{
			return hh + "00";
		}

		if (mm < 15)
		{
			return hh + "00";
		}
		if (mm >= 15 && mm < 30)
		{
			return hh + "15";
		}

		if (mm >= 30 && mm < 45)
		{
			return hh + "30";
		}

		if (mm >= 45 && mm < 60)
		{
			return hh + "45";
		}
		return time;
	}
	public static String getCurrentDay()
	{
		return String.format("%d", LocalDateTime.now());
	}

	/** 
	 当前的会计期间
	*/
	public static String getCurrentAP()
	{
		return LocalDateTime.now().toString("yyyy-MM");
	}
	/** 
	 当前的会计期间
	*/
	public static String getCurrentYear()
	{
		return LocalDateTime.now().toString("yyyy");
	}
	public static String getCurrentMonth()
	{
		return LocalDateTime.now().toString("MM");
	}
	/** 
	 当前的会计期间 yyyy-MM
	*/
	public static String getCurrentYearMonth()
	{
		return LocalDateTime.now().toString("yyyy-MM");
	}
	public static String GetJDByMM(String mm)
	{
		String jd = "01";
		switch (mm)
		{
			case "01":
			case "02":
			case "03":
				jd = "01";
				break;
			case "04":
			case "05":
			case "06":
				jd = "04";
				break;
			case "07":
			case "08":
			case "09":
				jd = "07";
				break;
			case "10":
			case "11":
			case "12":
				jd = "10";
				break;
			default:
				throw new RuntimeException("@不是有效的月份格式" + mm);
		}
		return jd;
	}
	/** 
	 当前的季度期间yyyy-MM
	*/
	public static String getCurrentAPOfJD()
	{
		return LocalDateTime.now().toString("yyyy") + "-" + DataType.GetJDByMM(LocalDateTime.now().toString("MM"));
	}
	/** 
	 当前的季度的前一个季度.
	*/
	public static String getCurrentAPOfJDOfFrontFamily()
	{
		LocalDateTime now = LocalDateTime.now().plusMonths(-3);
		return now.toString("yyyy") + "-" + DataType.GetJDByMM(now.toString("MM"));
	}
	/** 
	 yyyy-JD
	*/
	public static String getCurrentAPOfPrevious()
	{
		int m = Integer.parseInt(LocalDateTime.now().toString("MM"));
		return LocalDateTime.now().toString("yyyy-MM");
	}
	/** 
	 取出当前月份的上一个月份
	*/
	public static String getCurrentNYOfPrevious()
	{
		LocalDateTime dt = LocalDateTime.now();
		dt = dt.plusMonths(-1);
		return dt.toString("yyyy-MM");
	}
	/** 
	 取出当前月份的上上一个月份
	*/
	public static String getShangCurrentNYOfPrevious()
	{
		LocalDateTime dt = LocalDateTime.now();
		dt = dt.plusMonths(-2);
		return dt.toString("yyyy-MM");
	}
	/** 
	 当前的季度期间
	*/
	public static String getCurrentAPOfYear()
	{
		return LocalDateTime.now().toString("yyyy");
	}
	/** 
	 当前的日期时间
	*/
	public static String getCurrentDataTime()
	{
		return LocalDateTime.now().toString(DataType.getSysDataTimeFormat());
	}
	public static String getCurrentDataTimeOfDef()
	{
		switch (BP.Web.WebUser.getSysLang())
		{
			case "CH":
			case "B5":
				return getCurrentDataTimeCNOfShort();
			case "EN":
				return LocalDateTime.now().toString("MM/DD/YYYY");
			default:
				break;
		}
		return getCurrentDataTimeCNOfShort();
	}
	public static String getCurrentDataTimeCNOfShort()
	{
		return LocalDateTime.now().toString("yy年MM月dd日 HH时mm分");
	}
	public static String getCurrentDataTimeCNOfLong()
	{
		return LocalDateTime.now().toString("yy年MM月dd日 HH时mm分ss秒");
	}
	public static String getCurrentDataCNOfShort()
	{
		return LocalDateTime.now().toString("yy年MM月dd日");
	}
	public static String getCurrentDataCNOfLong()
	{
		return LocalDateTime.now().toString("yyyy年MM月dd日");
	}
	/** 
	 当前的日期时间
	*/
	public static String getCurrentDataTimeCN()
	{
		return LocalDateTime.now().toString(DataType.getSysDataFormatCN()) + "，" + GetWeekName(LocalDateTime.now().getDayOfWeek());
	}
	private static String GetWeekName(DayOfWeek dw)
	{
		switch (dw)
		{
			case Monday:
				return "星期一";
			case Thursday:
				return "星期四";
			case Friday:
				return "星期五";
			case Saturday:
				return "星期六";
			case Sunday:
				return "星期日";
			case Tuesday:
				return "星期二";
			case Wednesday:
				return "星期三";
			default:
				return "";
		}
	}

	/** 
	 当前的日期时间
	*/
	public static String getCurrentDataTimess()
	{
		return LocalDateTime.now().toString(DataType.getSysDataTimeFormat() + ":ss");
	}
	public static String ParseSysDateTime2SysDate(String sysDateformat)
	{
		try
		{
			return sysDateformat.substring(0, 10);
		}
		catch (RuntimeException ex)
		{
			throw new RuntimeException("日期格式错误:" + sysDateformat + " errorMsg=" + ex.getMessage());
		}
	}
	/** 
	 转化为友好的日期
	 
	 @param sysDateformat 日期
	 @return 
	*/
	public static String ParseSysDate2DateTimeFriendly(String sysDateformat)
	{
		BP.DA.DTTemp dt = new DTTemp();
		return dt.DateStringFromNow(sysDateformat);
	}
	/** 
	 把chichengsoft本系统日期格式转换为系统日期格式。
	 
	 @param sysDateformat yyyy-MM-dd
	 @return DateTime
	*/
	public static LocalDateTime ParseSysDate2DateTime(String sysDateformat)
	{
		if (sysDateformat == null || sysDateformat.trim().length() == 0)
		{
			return LocalDateTime.now();
		}


		try
		{
			if (sysDateformat.length() > 10)
			{
				return ParseSysDateTime2DateTime(sysDateformat);
			}

			sysDateformat = sysDateformat.trim();
			//DateTime.Parse(sysDateformat,
			String[] strs = null;
			if (sysDateformat.indexOf("-") != -1)
			{
				strs = sysDateformat.split("[-]", -1);
			}

			if (sysDateformat.indexOf("/") != -1)
			{
				strs = sysDateformat.split("[/]", -1);
			}

			int year = Integer.parseInt(strs[0]);
			int month = Integer.parseInt(strs[1]);
			int day = Integer.parseInt(strs[2]);

			//DateTime dt= DateTime.Now;
			return LocalDateTime.of(year, month, day, 0, 0, 0);
		}
		catch (RuntimeException ex)
		{
			throw new RuntimeException("日期[" + sysDateformat + "]转换出现错误:" + ex.getMessage() + "无效的日期是格式。");
		}
		//return dt;			 
	}
	/** 
	 2005-11-04 09:12
	 
	 @param sysDateformat 2005-11-04 09:12 
	 @return 
	*/
	public static LocalDateTime ParseSysDateTime2DateTime(String sysDateformat)
	{
		try
		{
			return LocalDateTime.parse(sysDateformat);
		}
		catch (RuntimeException ex)
		{
			throw new RuntimeException("@时间格式不正确:" + sysDateformat + "@技术信息:" + ex.getMessage());
		}
	}

	/** 
	 获取两个时间之间的字符串表示形式，如：1天2时34分
	 <p>added by liuxc,2014-12-4</p>
	 
	 @param t1 开始时间
	 @param t2 结束时间
	 @return 返回：x天x时x分
	*/
	public static String GetSpanTime(LocalDateTime t1, LocalDateTime t2)
	{
		LocalDateTime span = t2 - t1;
//C# TO JAVA CONVERTER TODO TASK: There is no equivalent to implicit typing in Java unless the Java 10 inferred typing option is selected:
		var days = span.Days;
//C# TO JAVA CONVERTER TODO TASK: There is no equivalent to implicit typing in Java unless the Java 10 inferred typing option is selected:
		var hours = span.Hours;
//C# TO JAVA CONVERTER TODO TASK: There is no equivalent to implicit typing in Java unless the Java 10 inferred typing option is selected:
		var minutes = span.Minutes;

		if (days == 0 && hours == 0 && minutes == 0)
		{
			minutes = span.Seconds > 0 ? 1 : 0;
		}

		String spanStr = "";

		if (days > 0)
		{
			spanStr += days + "天";
		}

		if (hours > 0)
		{
			spanStr += hours + "时";
		}

		if (minutes > 0)
		{
			spanStr += minutes + "分";
		}

		if (spanStr.length() == 0)
		{
			spanStr = "0分";
		}

		return spanStr;
	}
	/** 
	 获得两个日期之间的天数
	 
	 @param dtoffrom
	 @param dtofto
	 @return 
	*/
	public static float GeTimeLimits(String dtoffrom, String dtofto)
	{
		LocalDateTime dtfrom = DataType.ParseSysDate2DateTime(dtoffrom);
		LocalDateTime dtto = DataType.ParseSysDate2DateTime(dtofto);

		TimeSpan ts = dtto - dtfrom;
		return (float)Double.isNaN(ts.TotalDays) ? Double.NaN : Math.round(ts.TotalDays * Math.pow(10, 2)) / Math.pow(10, 2);
	}
	/** 
	 获得两个时间的参数
	 
	 @param dtoffrom 时间从
	 @return 
	*/
	public static float GeTimeLimits(String dtoffrom)
	{
		return GeTimeLimits(dtoffrom, DataType.getCurrentDataTime());
	}
	public static float GetSpanMinute(String fromdatetim, String toDateTime)
	{
		LocalDateTime dtfrom = DataType.ParseSysDateTime2DateTime(fromdatetim);
		LocalDateTime dtto = DataType.ParseSysDateTime2DateTime(toDateTime);

		TimeSpan ts = dtfrom - dtto;
		return (float)ts.TotalMinutes;
	}
	/** 
	 到现在的时间
	 
	 @param fromdatetim
	 @return 分中数
	*/
	public static int GetSpanMinute(String fromdatetim)
	{
		LocalDateTime dtfrom = DataType.ParseSysDateTime2DateTime(fromdatetim);
		LocalDateTime dtto = LocalDateTime.now();
		TimeSpan ts = dtfrom - dtto;
		return (int)ts.TotalMinutes + ts.Hours * 60;
	}
	/** 
	 系统定义日期时间格式 yyyy-MM-dd hh:mm
	*/
	public static String getSysDataTimeFormat()
	{
		return "yyyy-MM-dd HH:mm";
	}
	public static String getSysDataFormatCN()
	{
		return "yyyy年MM月dd日";
	}
	public static String getSysDatatimeFormatCN()
	{
		return "yyyy年MM月dd日 HH时mm分";
	}
	public static DBUrlType GetDBUrlByString(String strDBUrl)
	{
		switch (strDBUrl)
		{
			case "AppCenterDSN":
				return DBUrlType.AppCenterDSN;
			case "DBAccessOfOracle1":
				return DBUrlType.DBAccessOfOracle1;
			case "DBAccessOfOracle2":
				return DBUrlType.DBAccessOfOracle2;
			case "DBAccessOfMSSQL1":
				return DBUrlType.DBAccessOfMSSQL1;
			case "DBAccessOfMSSQL2":
				return DBUrlType.DBAccessOfMSSQL2;
			case "DBAccessOfOLE":
				return DBUrlType.DBAccessOfOLE;
			case "DBAccessOfODBC":
				return DBUrlType.DBAccessOfODBC;
			default:
				throw new RuntimeException("@没有此类型[" + strDBUrl + "]");
		}
	}
	public static int GetDataTypeByString(String datatype)
	{
		switch (datatype)
		{
			case "AppBoolean":
				return DataType.AppBoolean;
			case "AppDate":
				return DataType.AppDate;
			case "AppDateTime":
				return DataType.AppDateTime;
			case "AppDouble":
				return DataType.AppDouble;
			case "AppFloat":
				return DataType.AppFloat;
			case "AppInt":
				return DataType.AppInt;
			case "AppMoney":
				return DataType.AppMoney;
			case "AppString":
				return DataType.AppString;
			default:
				throw new RuntimeException("@没有此类型" + datatype);
		}
	}
	public static String GetDataTypeDese(int datatype)
	{
		if (Web.WebUser.getSysLang().equals("CH"))
		{
			switch (datatype)
			{
				case DataType.AppBoolean:
					return "布尔(Int)";
				case DataType.AppDate:
					return "日期nvarchar";
				case DataType.AppDateTime:
					return "日期时间nvarchar";
				case DataType.AppDouble:
					return "双精度(double)";
				case DataType.AppFloat:
					return "浮点(float)";
				case DataType.AppInt:
					return "整型(int)";
				case DataType.AppMoney:
					return "货币(float)";
				case DataType.AppString:
					return "字符(nvarchar)";
				default:
					throw new RuntimeException("@没有此类型");
			}
		}

		switch (datatype)
		{
			case DataType.AppBoolean:
				return "Boolen";
			case DataType.AppDate:
				return "Date";
			case DataType.AppDateTime:
				return "Datetime";
			case DataType.AppDouble:
				return "Double";
			case DataType.AppFloat:
				return "Float";
			case DataType.AppInt:
				return "Int";
			case DataType.AppMoney:
				return "Money";
			case DataType.AppString:
				return "Nvarchar";
			default:
				throw new RuntimeException("@没有此类型");
		}
	}
	/** 
	 产生适应的图片大小
	 用途:在固定容器大小的位置，显示固定的图片。
	 
	 @param height 容器高度
	 @param width 容器宽度
	 @param AdaptHeight 原始图片高度
	 @param AdaptWidth 原始图片宽度
	 @param isFull 是否填充:是,小图片将会放大填充容器. 否,小图片不放大保留原来的大小
	*/
	public static void GenerPictSize(float panelHeight, float panelWidth, tangible.RefObject<Float> AdaptHeight, tangible.RefObject<Float> AdaptWidth, boolean isFullPanel)
	{
		if (isFullPanel == false)
		{
			if (panelHeight <= AdaptHeight.argValue && panelWidth <= AdaptWidth.argValue)
			{
				return;
			}
		}

		float zoom = 1;
		zoom = Math.min(panelHeight / AdaptHeight.argValue, panelWidth / AdaptWidth.argValue);
		AdaptHeight.argValue = AdaptHeight.argValue * zoom;
		AdaptWidth.argValue = AdaptWidth.argValue * zoom;
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 正则表达式
	/** 
	 (RegEx.Replace操作使用)仅含有汉字、数字、字母、下划线
	 <p>示例：</p>
	 <p>   Console.WriteLine(RegEx.Replace("姓名@-._#:：“｜：$?>a:12",RegEx_Replace_OnlyHSZX,""));</p>
	 <p>   输出：姓名_a12</p>
	*/
	public static final String RegEx_Replace_OnlyHSZX = "[^0-9a-zA-Z_\\u4e00-\\u9fa5]";
	/** 
	 (RegEx.Replace操作使用)仅含有数字、字母、下划线
	 <p>示例：</p>
	 <p>   Console.WriteLine(RegEx.Replace("姓名@-._#:：“｜：$?>a:12",RegEx_Replace_OnlySZX,""));</p>
	 <p>   输出：_a12</p>
	*/
	public static final String RegEx_Replace_OnlySZX = "[\\u4e00-\\u9fa5]|[^0-9a-zA-Z_]";
	/** 
	 (RegEx.Replace操作使用)字符串开头不能为数字或下划线
	 <p>示例：</p>
	 <p>   Console.WriteLine(RegEx.Replace("_12_a1",RegEx_Replace_FirstXZ,""));</p>
	 <p>   输出：a1</p>
	*/
	public static final String RegEx_Replace_FirstXZ = "^(_|[0-9])+";
	/** 
	 (RegEx.Replace操作使用)仅含有整形数字
	 <p>示例：</p>
	 <p>   Console.WriteLine(RegEx.Replace("_12_a1",RegEx_Replace_OnlyIntNum,""));</p>
	 <p>   输出：121</p>
	*/
	public static final String RegEx_Replace_OnlyIntNum = "[^0-9]";
	/** 
	 (RegEx.Replace操作使用)字符串不能含有指定危险字符
	 <p>示例：</p>
	 <p>   Console.WriteLine(RegEx.Replace("'_1--2/_a1",RegEx_Replace_FilterDangerousSymbols,""));</p>
	 <p>   输出：_12_a1</p>
	*/
	public static final String RegEx_Replace_FilterDangerousSymbols = "[';/]|[-]{2}";
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 数据类型。
	/** 
	 string
	*/
	public static final int AppString = 1;
	/** 
	 int
	*/
	public static final int AppInt = 2;
	/** 
	 float
	*/
	public static final int AppFloat = 3;
	/** 
	 AppBoolean
	*/
	public static final int AppBoolean = 4;
	/** 
	 AppDouble
	*/
	public static final int AppDouble = 5;
	/** 
	 AppDate
	*/
	public static final int AppDate = 6;
	/** 
	 AppDateTime
	*/
	public static final int AppDateTime = 7;
	/** 
	 AppMoney
	*/
	public static final int AppMoney = 8;
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 其他方法.
	public static String StringToDateStr(String str)
	{
		try
		{
			LocalDateTime dt = LocalDateTime.parse(str);
			String year = String.valueOf(dt.getYear());
			String month = String.valueOf(dt.getMonthValue());
			String day = String.valueOf(dt.getDayOfMonth());
			return year + "-" + tangible.StringHelper.padLeft(month, 2, '0') + "-" + tangible.StringHelper.padLeft(day, 2, '0');
			//return str;
		}
		catch (RuntimeException ex)
		{
			throw ex;
		}
	}
	public static String GenerSpace(int spaceNum)
	{
		if (spaceNum <= 0)
		{
			return "";
		}

		String strs = "";
		while (spaceNum != 0)
		{
			strs += "&nbsp;&nbsp;";
			spaceNum--;
		}
		return strs;
	}
	/** 
	 根据序号生成Idx 的ABC
	 
	 @param idx
	 @return 
	*/
	public static String GenerABC(int idx)
	{
		String strs = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		char[] mystrs = strs.toCharArray();
		return String.valueOf(mystrs[idx]);
	}
	public static String GenerBR(int spaceNum)
	{
		String strs = "";
		while (spaceNum != 0)
		{
			strs += "<BR>";
			spaceNum--;
		}
		return strs;
	}
	public static boolean IsImgExt(String ext)
	{
		ext = ext.replace(".", "").toLowerCase();
		switch (ext)
		{
			case "gif":
			case "jpg":
			case "jepg":
			case "jpeg":
			case "bmp":
			case "png":
			case "tif":
			case "gsp":
			case "mov":
			case "psd":
			case "tiff":
			case "wmf":
				return true;
			default:
				return false;
		}
	}
	public static boolean IsVoideExt(String ext)
	{
		ext = ext.replace(".", "").toLowerCase();
		switch (ext)
		{
			case "mp3":
			case "mp4":
			case "asf":
			case "wma":
			case "rm":
			case "rmvb":
			case "mpg":
			case "wmv":
			case "quicktime":
			case "avi":
			case "flv":
			case "mpeg":
				return true;
			default:
				return false;
		}
	}
	/** 
	 判断是否是Num 字串。
	 
	 @param str
	 @return 
	*/
	public static boolean IsNumStr(String str)
	{
		try
		{
			BigDecimal d = BigDecimal.Parse(str);
			return true;
		}
		catch (java.lang.Exception e)
		{
			return false;
		}
	}
	/** 
	 是不时奇数
	 
	 @param num will judg value
	 @return 
	*/
	public static boolean IsQS(int num)
	{
		int ii = 0;
		for (int i = 0; i < 500; i++)
		{
			if (num == ii)
			{
				return false;
			}
			ii = ii + 2;
		}
		return true;
	}

	public static boolean StringToBoolean(String str)
	{
		if (str == null || str.equals("") || str.equals(",nbsp;"))
		{
			return false;
		}

		if (str.equals("0") || str.equals("1"))
		{
			if (str.equals("0"))
			{
				return false;
			}
			else
			{
				return true;
			}
		}
		else if (str.equals("true") || str.equals("false"))
		{
			if (str.equals("false"))
			{
				return false;
			}
			else
			{
				return true;
			}

		}
		else if (str.equals("是") || str.equals("否"))
		{
			if (str.equals("否"))
			{
				return false;
			}
			else
			{
				return true;
			}
		}
		else
		{
			throw new RuntimeException("@要转换的[" + str + "]不是bool 类型");
		}




	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 其他方法.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 与门户相关的.
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if DEBUG
	private static TimeSpan ts = new TimeSpan(0, 10, 0);
//#else
   private static TimeSpan ts = new TimeSpan(0, 1, 0);
//#endif

	/** 
	 得到WebService对象
	 
	 @return 
	*/
	public static BP.En30.ccportal.PortalInterfaceSoapClient GetPortalInterfaceSoapClientInstance()
	{
		return NetPlatformImpl.DA_DataType.GetPortalInterfaceSoapClientInstance();
	}
	private static String _BPMHost = null;
	/** 
	 当前BPMHost 
	*/
	public static String getBPMHost()
	{
		if (_BPMHost != null)
		{
			return _BPMHost;
		}
		_BPMHost = "http://" + HttpContextHelper.getRequestUrlAuthority();
		return _BPMHost;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 与门户相关的.
}