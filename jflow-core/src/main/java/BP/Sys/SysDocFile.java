package BP.Sys;

import java.io.File;
import java.io.IOException;

import BP.DA.DBType;
import BP.DA.DataType;
import BP.DA.Depositary;
import BP.En.EntityMyPK;
import BP.En.Map;

public class SysDocFile extends EntityMyPK
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// 实现基本属性
	public final String getEnName()
	{
		return this.GetValStringByKey(SysDocFileAttr.EnName);
	}
	
	public final void setEnName(String value)
	{
		this.SetValByKey(SysDocFileAttr.EnName, value);
	}
	
	public final String getRefKey()
	{
		return this.GetValStringByKey(SysDocFileAttr.RefKey);
	}
	
	public final void setRefKey(String value)
	{
		this.SetValByKey(SysDocFileAttr.RefKey, value);
	}
	
	public final String getRefVal()
	{
		return this.GetValStringByKey(SysDocFileAttr.RefVal);
	}
	
	public final void setRefVal(String value)
	{
		this.SetValByKey(SysDocFileAttr.RefVal, value);
	}
	
	public final String getFileName()
	{
		return this.GetValStringByKey(SysDocFileAttr.FileName);
	}
	
	public final void setFileName(String value)
	{
		this.SetValByKey(SysDocFileAttr.FileName, value);
	}
	
	public final int getFileSize()
	{
		return this.GetValIntByKey(SysDocFileAttr.FileSize);
	}
	
	public final void setFileSize(int value)
	{
		this.SetValByKey(SysDocFileAttr.FileSize, value);
	}
	
	public final String getFileType()
	{
		return this.GetValStringByKey(SysDocFileAttr.FileType);
	}
	
	public final void setFileType(String value)
	{
		this.SetValByKey(SysDocFileAttr.FileType, value);
	}
	
	// 字段
	public final String getDocHtml()
	{
		return DataType.ParseText2Html(this.getDocText());
	}
	
	public final String getDocText1()
	{
		return "";
	}
	
	public final String getDocText()
	{
		return this.getD1() + this.getD2() + this.getD3() + this.getD4()
				+ this.getD5() + this.getD6() + this.getD7() + this.getD8()
				+ this.getD9() + this.getD10() + this.getD11() + this.getD12()
				+ this.getD13() + this.getD14() + this.getD15() + this.getD16()
				+ this.getD17() + this.getD18() + this.getD19() + this.getD20();
	}
	
	public final void setDocText(String value)
	{
		int len = value.length();
		this.setFileSize(len);
		int step = 2000;
		int i = 0;
		int idx = -1;
		while (true)
		{
			i++;
			idx++;
			if (len > step * i)
			{
				this.SetValByKey("D" + i,
						value.substring(step * idx, step * idx + step));
			} else
			{
				this.SetValByKey("D" + i, value.substring(step * idx));
				break;
			}
			if (i > 20)
			{
				throw new RuntimeException("数据太大存储不下。");
			}
		}
	}
	
	public final String getD1()
	{
		return this.GetValStrByKey("D1");
	}
	
	public final void setD1(String value)
	{
		this.SetValByKey("D1", value);
	}
	
	public final String getD2()
	{
		return this.GetValStrByKey("D2");
	}
	
	public final void setD2(String value)
	{
		this.SetValByKey("D2", value);
	}
	
	public final String getD3()
	{
		return this.GetValStrByKey("D3");
	}
	
	public final void setD3(String value)
	{
		this.SetValByKey("D3", value);
	}
	
	public final String getD4()
	{
		return this.GetValStrByKey("D4");
	}
	
	public final void setD4(String value)
	{
		this.SetValByKey("D4", value);
	}
	
	public final String getD5()
	{
		return this.GetValStrByKey("D5");
	}
	
	public final void setD5(String value)
	{
		this.SetValByKey("D5", value);
	}
	
	public final String getD6()
	{
		return this.GetValStrByKey("D6");
	}
	
	public final void setD6(String value)
	{
		this.SetValByKey("D6", value);
	}
	
	public final String getD7()
	{
		return this.GetValStrByKey("D7");
	}
	
	public final void setD7(String value)
	{
		this.SetValByKey("D7", value);
	}
	
	public final String getD8()
	{
		return this.GetValStrByKey("D8");
	}
	
	public final void setD8(String value)
	{
		this.SetValByKey("D8", value);
	}
	
	public final String getD9()
	{
		return this.GetValStrByKey("D9");
	}
	
	public final void setD9(String value)
	{
		this.SetValByKey("D9", value);
	}
	
	public final String getD10()
	{
		return this.GetValStrByKey("D10");
	}
	
	public final void setD10(String value)
	{
		this.SetValByKey("D10", value);
	}
	
	public final String getD11()
	{
		return this.GetValStrByKey("D11");
	}
	
	public final void setD11(String value)
	{
		this.SetValByKey("D11", value);
	}
	
	public final String getD12()
	{
		return this.GetValStrByKey("D12");
	}
	
	public final void setD12(String value)
	{
		this.SetValByKey("D12", value);
	}
	
	public final String getD13()
	{
		return this.GetValStrByKey("D13");
	}
	
	public final void setD13(String value)
	{
		this.SetValByKey("D13", value);
	}
	
	public final String getD14()
	{
		return this.GetValStrByKey("D14");
	}
	
	public final void setD14(String value)
	{
		this.SetValByKey("D14", value);
	}
	
	public final String getD15()
	{
		return this.GetValStrByKey("D15");
	}
	
	public final void setD15(String value)
	{
		this.SetValByKey("D15", value);
	}
	
	public final String getD16()
	{
		return this.GetValStrByKey("D16");
	}
	
	public final void setD16(String value)
	{
		this.SetValByKey("D16", value);
	}
	
	public final String getD17()
	{
		return this.GetValStrByKey("D17");
	}
	
	public final void setD17(String value)
	{
		this.SetValByKey("D17", value);
	}
	
	public final String getD18()
	{
		return this.GetValStrByKey("D18");
	}
	
	public final void setD18(String value)
	{
		this.SetValByKey("D18", value);
	}
	
	public final String getD19()
	{
		return this.GetValStrByKey("D19");
	}
	
	public final void setD19(String value)
	{
		this.SetValByKey("D19", value);
	}
	
	public final String getD20()
	{
		return this.GetValStrByKey("D20");
	}
	
	public final void setD20(String value)
	{
		this.SetValByKey("D20", value);
	}
	
	// 构造方法
	public SysDocFile()
	{
	}
	
	public SysDocFile(String pk)
	{
		super(pk);
	}
	
	/**
	 * 注意不初始化数据。
	 * 
	 * @param enName
	 * @param key
	 * @param val
	 */
	public SysDocFile(String enName, String key, String val)
	{
		this.setMyPK(enName + "@" + key + "@" + val);
	}
	
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}
		
		Map map = new Map("Sys_DocFile");
		map.setEnDesc("备注字段文件管理者");
		// map.DepositaryOfEntity = Depositary.Application;
		map.setDepositaryOfMap(Depositary.Application);
		map.AddMyPK();
		
		map.AddTBString(SysDocFileAttr.FileName, null, "名称", false, true, 0,
				200, 30);
		map.AddTBInt(SysDocFileAttr.FileSize, 0, "大小", true, true);
		map.AddTBString(SysDocFileAttr.FileType, null, "文件类型", true, true, 0,
				50, 20);
		
		map.AddTBString("D1", null, "D1", true, true, 0, 4000, 20);
		map.AddTBString("D2", null, "D2", true, true, 0, 4000, 20);
		map.AddTBString("D3", null, "D3", true, true, 0, 4000, 20);
		
		if (map.getEnDBUrl().getDBType() == DBType.Oracle
				|| map.getEnDBUrl().getDBType() == DBType.MSSQL)
		{
			map.AddTBString("D4", null, "D4", true, true, 0, 4000, 20);
			map.AddTBString("D5", null, "D5", true, true, 0, 4000, 20);
			map.AddTBString("D6", null, "D6", true, true, 0, 4000, 20);
			
			map.AddTBString("D7", null, "D7", true, true, 0, 4000, 20);
			map.AddTBString("D8", null, "D8", true, true, 0, 4000, 20);
			map.AddTBString("D9", null, "D9", true, true, 0, 4000, 20);
			
			map.AddTBString("D10", null, "D10", true, true, 0, 4000, 20);
			map.AddTBString("D11", null, "D11", true, true, 0, 4000, 20);
			
			map.AddTBString("D12", null, "D12", true, true, 0, 4000, 20);
			map.AddTBString("D13", null, "D13", true, true, 0, 4000, 20);
			map.AddTBString("D14", null, "D14", true, true, 0, 4000, 20);
			map.AddTBString("D15", null, "D15", true, true, 0, 4000, 20);
			
			map.AddTBString("D16", null, "D16", true, true, 0, 4000, 20);
			map.AddTBString("D17", null, "D17", true, true, 0, 4000, 20);
			map.AddTBString("D18", null, "D18", true, true, 0, 4000, 20);
			map.AddTBString("D19", null, "D19", true, true, 0, 4000, 20);
			map.AddTBString("D20", null, "D20", true, true, 0, 4000, 20);
		}
		
		this.set_enMap(map);
		return this.get_enMap();
	}
	
	// 共用方法 V2.0
	public static String GetValHtmlV2(String enName, String pkVal)
	{
		try
		{
			return BP.DA.DataType.ReadTextFile(BP.Sys.SystemConfig
					.getPathOfFDB() + enName + "/" + pkVal + ".fdb");
		} catch (java.lang.Exception e)
		{
			return null;
		}
	}
	
	// public static String GetValTextV2(String enName, String pkVal)
	// {
	// try
	// {
	// return BP.DA.DataType.ReadTextFile(BP.Sys.SystemConfig.getPathOfFDB() +
	// enName + "\\" + pkVal + ".fdb");
	// }
	// catch (java.lang.Exception e)
	// {
	// return null;
	// }
	// }
	public static void SetValV2(String enName, String pkVal, String val)
	{
		try
		{
			String dir = BP.Sys.SystemConfig.getPathOfFDB() + enName + "\\";
			File file = new File(dir);
			if (!file.exists())
			{
				file.createNewFile();
			}
			
			BP.DA.DataType.SaveAsFile(dir + "\\" + pkVal + ".fdb", val);
		} catch (RuntimeException ex)
		{
			String filePath = BP.Sys.SystemConfig.getPathOfFDB() + enName;
			File file = new File(filePath);
			if (!file.exists())
			{
				try
				{
					file.createNewFile();
				} catch (IOException e)
				{
					e.printStackTrace();
				}
			}
			throw ex;
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	// 共用方法
	public static String GetValHtmlV1(String enName, String pkVal)
	{
		SysDocFile sdf = new SysDocFile();
		sdf.setMyPK(enName + "@Doc@" + pkVal);
		sdf.RetrieveFromDBSources();
		return sdf.getDocHtml();
	}
	
	public static String GetValTextV1(String enName, String pkVal)
	{
		SysDocFile sdf = new SysDocFile();
		sdf.setMyPK(enName + "@Doc@" + pkVal);
		sdf.RetrieveFromDBSources();
		return sdf.getDocText();
	}
	
	public static void SetValV1(String enName, String pkVal, String val)
	{
		SysDocFile sdf = new SysDocFile();
		sdf.setMyPK(enName + "@Doc@" + pkVal);
		sdf.setFileSize(val.length());
		sdf.setDocText(val);
		sdf.Save();
	}
	
	public final void UpdateLoadFileOfAccess(String FileName)
	{
		// FileInfo fi = new FileInfo( FileName );// Replace with your file name
		// if (fi.Exists==false)
		// throw new Exception("文件已经不存在。");
		
		// this.FileSize=fi.Length.ToString();
		// this.FileName = fi.FullName;
		// this.Name=fi.Name;
		// this.Insert();
		
		// byte[] bData = null;
		// //int nNewFileID = 0;
		// // Read file data into buffer
		// using ( FileStream fs = fi.OpenRead() )
		// {
		// bData = new byte[fi.Length];
		// int nReadLength = fs.Read( bData,0, (int)(fi.Length) );
		// }
		
		// // // Add file info into DB
		// // string strQuery = "INSERT INTO FileInfo "
		// // + " ( FileName, FullName, FileData ) "
		// // + " VALUES "
		// // + " ( @FileName, @FullName, @FileData ) "
		// // + " SELECT @@IDENTITY AS 'Identity'";
		
		// string
		// strQuery="UPDATE Sys_FileManager SET FileData=@FileData WHERE OID="+this.OID;
		// OleDbConnection conn =
		// (OleDbConnection)BP.DA.DBAccess.GetAppCenterDBConn ;
		// conn.Open();
		
		// OleDbCommand sqlComm = new OleDbCommand( strQuery,
		// conn);
		
		// //sqlComm.Parameters.Add( "@FileName", fi.Name );
		// //sqlComm.Parameters.Add( "@FullName", fi.FullName );
		// sqlComm.Parameters.AddWithValue("@FileData", bData);
		// sqlComm.ExecuteNonQuery();
		
		// // Get new file ID
		// // SqlDataReader sqlReader = sqlComm.ExecuteReader();
		// // if( sqlReader.Read() )
		// // {
		// // nNewFileID = int.Parse(sqlReader.GetValue(0).ToString());
		// // }
		// //
		// // sqlReader.Close();
		// // sqlComm.Dispose();
		// //
		// // if( nNewFileID > 0 )
		// // {
		// // // Add new item in list view
		// // //ListViewItem itmNew = lsvFileInfo.Items.Add( fi.Name );
		// // //itmNew.Tag = nNewFileID;
		// // }
	}
}