package BP.WF.HttpHandler;

import BP.WF.*;
import BP.Web.*;
import BP.Sys.*;
import BP.DA.*;
import BP.En.*;
import NPOI.HSSF.UserModel.*;
import NPOI.SS.UserModel.*;
import BP.WF.*;
import java.util.*;
import java.io.*;
import java.time.*;
import java.math.*;

public abstract class DirectoryPageBase
{

		///#region 执行方法.
	/** 
	 获得Form数据.
	 
	 @param key key
	 @return 返回值
	*/

	public final String GetValFromFrmByKey(String key)
	{
		return GetValFromFrmByKey(key, null);
	}

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public string GetValFromFrmByKey(string key, string isNullAsVal = null)
	public final String GetValFromFrmByKey(String key, String isNullAsVal)
	{
		String val = HttpContextHelper.RequestParams(key);
		if (val == null && key.contains("DDL_") == false)
		{
			val = HttpContextHelper.RequestParams("DDL_" + key);
		}

		if (val == null && key.contains("TB_") == false)
		{
			val = HttpContextHelper.RequestParams("TB_" + key);
		}

		if (val == null && key.contains("CB_") == false)
		{
			val = HttpContextHelper.RequestParams("CB_" + key);
		}

		if (val == null)
		{
			if (isNullAsVal != null)
			{
				return isNullAsVal;
			}
			return "";
			//throw new Exception("@获取Form参数错误,参数集合不包含[" + key + "]");
		}

		val = val.replace("'", "~");
		return val;
	}
	/** 
	 执行方法
	 
	 @param obj 对象名
	 @param methodName 方法
	 @return 返回执行的结果，执行错误抛出异常
	*/
	public final String DoMethod(DirectoryPageBase myEn, String methodName)
	{
		try
		{
			java.lang.Class tp = myEn.getClass();
			java.lang.reflect.Method mp = tp.getMethod(methodName);
			if (mp == null)
			{
				/* 没有找到方法名字，就执行默认的方法. */
				return myEn.DoDefaultMethod();
			}

			//执行该方法.
			Object[] paras = null;
			Object tempVar = mp.Invoke(this, paras);
			return tempVar instanceof String ? (String)tempVar : null; //调用由此 MethodInfo 实例反射的方法或构造函数。
		}
		catch (RuntimeException ex)
		{
			if (methodName.contains(">") == true)
			{
				return "err@非法的脚本植入.";
			}

			if (ex.getCause() != null)
			{
				return "err@调用类:[" + myEn + "]方法:[" + methodName + "]出现错误:" + ex.getCause();
			}
			else
			{
				return "err@调用类:[" + myEn + "]方法:[" + methodName + "]出现错误:" + ex.getMessage();
			}
		}
	}
	/** 
	 执行默认的方法名称
	 
	 @return 返回执行的结果
	*/
	protected String DoDefaultMethod()
	{
		if (this.getDoType().contains(">") == true)
		{
			return "err@非法的脚本植入.";
		}

		return "err@子类[" + this.toString() + "]没有重写该[" + this.getDoType() + "]方法，请确认该方法是否缺少或者是非public类型的.";
	}

		///#endregion 执行方法.


		///#region 公共方法.
	/** 
	 公共方法获取值
	 
	 @param param 参数名
	 @return 
	*/
	public final String GetRequestVal(String key)
	{
		String val = HttpContextHelper.RequestParams(key);
		if (val == null)
		{
			val = HttpContextHelper.Request.Form[key];
			return val;
		}
		return HttpUtility.UrlDecode(val, System.Text.Encoding.UTF8);
	}
	/** 
	 公共方法获取值
	 
	 @param param 参数名
	 @return 
	*/
	public final int GetRequestValInt(String param)
	{
		String str = GetRequestVal(param);
		if (str == null || str.equals("") || str.equals("null") || str.equals("undefined"))
		{
			return 0;
		}

		try
		{
			return Integer.parseInt(str);
		}
		catch (java.lang.Exception e)
		{
			return 0;
		}
	}
	/** 
	 公共方法获取值
	 
	 @param param 参数名
	 @return 
	*/
	public final boolean GetRequestValBoolen(String param)
	{
		if (this.GetRequestValInt(param) == 1)
		{
			return true;
		}
		return false;
	}
	/** 
	 公共方法获取值
	 
	 @param param
	 @return 
	*/
	public final long GetRequestValInt64(String param)
	{
		String str = GetRequestVal(param);
		if (str == null || str.equals("") || str.equals("null"))
		{
			return 0;
		}
		try
		{
			return Long.parseLong(str);
		}
		catch (java.lang.Exception e)
		{
			return 0;
		}
	}
	/** 
	 数据
	 
	 @param param
	 @return 
	*/
	public final float GetRequestValFloat(String param)
	{
		String str = GetRequestVal(param);
		if (str == null || str.equals("") || str.equals("null"))
		{
			return 0;
		}
		try
		{
			return Float.parseFloat(str);
		}
		catch (java.lang.Exception e)
		{
			return 0;
		}
	}
	public final BigDecimal GetRequestValDecimal(String param)
	{
		String str = GetRequestVal(param);
		if (str == null || str.equals("") || str.equals("null"))
		{
			return 0;
		}
		try
		{
			return BigDecimal.Parse(str);
		}
		catch (java.lang.Exception e)
		{
			return 0;
		}
	}
	/** 
	 获得参数.
	*/
	public final String getRequestParas()
	{
		String urlExt = "";
			//string rawUrl = HttpContextHelper.RequestRawUrl;
			//rawUrl = "&" + rawUrl.Substring(rawUrl.IndexOf('?') + 1);
			//string[] paras = rawUrl.Split('&');
			//foreach (string para in paras)
			//{
			//    if (para == null
			//        || para == ""
			//        || para.Contains("=") == false)
			//        continue;

			//    if (para == "1=1")
			//        continue;

			//    urlExt += "&" + para;
			//}

			// 适配framework和core（注：net core的rawurl中不含form data）
		for (String key : HttpContextHelper.RequestParamKeys)
		{
			if (!key.equals("1")) // 过滤url中1=1的情形
			{
				String value = HttpContextHelper.RequestParams(key);
				if (!DataType.IsNullOrEmpty(value))
				{
					urlExt += String.format("&%1$s=%2$s", key, value);
				}
			}
		}
		return urlExt;
	}
	/** 
	 所有的paras
	*/
	public final String getRequestParasOfAll()
	{
		String urlExt = "";
		String rawUrl = HttpContextHelper.RequestRawUrl;
		rawUrl = "&" + rawUrl.substring(rawUrl.indexOf('?') + 1);
		String[] paras = rawUrl.split("[&]", -1);
		for (String para : paras)
		{
			if (para == null || para.equals("") || para.contains("=") == false)
			{
				continue;
			}

			if (para.equals("1=1"))
			{
				continue;
			}

			urlExt += "&" + para;
		}


		for (String key : HttpContextHelper.RequestParamKeys)
		{
			urlExt += "&" + key + "=" + HttpContextHelper.RequestParams(key);
		}

		return urlExt;
	}

		///#endregion


		///#region 属性参数.
	/** 
	 
	*/
	public final String getPKVal()
	{
		String str = this.GetRequestVal("PKVal");

		if (DataType.IsNullOrEmpty(str) == true)
		{
			str = this.GetRequestVal("OID");
		}

		if (DataType.IsNullOrEmpty(str) == true)
		{
			str = this.GetRequestVal("No");
		}

		if (DataType.IsNullOrEmpty(str) == true)
		{
			str = this.GetRequestVal("MyPK");
		}
		if (DataType.IsNullOrEmpty(str) == true)
		{
			str = this.GetRequestVal("NodeID");
		}

		if (DataType.IsNullOrEmpty(str) == true)
		{
			str = this.GetRequestVal("WorkID");
		}

		if (DataType.IsNullOrEmpty(str) == true)
		{
			str = this.GetRequestVal("PK");
		}

		if ("null".equals(str) == true)
		{
			return null;
		}

		return str;
	}
	/** 
	 是否是移动？
	*/
	public final boolean getIsMobile()
	{
		String v = this.GetRequestVal("IsMobile");
		if (v != null && v.equals("1"))
		{
			return true;
		}

		if (HttpContextHelper.RequestRawUrl.Contains("/CCMobile/") == true)
		{
			return true;
		}

		return false;
	}
	/** 
	 编号
	*/
	public final String getNo()
	{
		String str = this.GetRequestVal("No"); // context.Request.QueryString["No"];
		if (str == null || str.equals("") || str.equals("null"))
		{
			return null;
		}
		return str;
	}
	public final String getName()
	{
		String str = this.GetRequestVal("Name");
		if (str == null || str.equals("") || str.equals("null"))
		{
			return null;
		}
		return str;
	}

	/** 
	 执行类型
	*/
	public final String getDoType()
	{
			//获得执行的方法.
		String doType = "";

		doType = this.GetRequestVal("DoType");
		if (DataType.IsNullOrEmpty(doType))
		{
			doType = this.GetRequestVal("Action");
		}

		if (DataType.IsNullOrEmpty(doType))
		{
			doType = this.GetRequestVal("action");
		}

		if (DataType.IsNullOrEmpty(doType))
		{
			doType = this.GetRequestVal("Method");
		}

		return doType;
	}
	public final String getEnName()
	{
		String str = this.GetRequestVal("EnName");

		if (str == null || str.equals("") || str.equals("null"))
		{
			str = this.GetRequestVal("FK_MapData");
		}

		if (str == null || str.equals("") || str.equals("null"))
		{
			return null;
		}

		return str;
	}
	/** 
	 类名
	*/
	public final String getEnsName()
	{
		String str = this.GetRequestVal("EnsName");

		if (str == null || str.equals("") || str.equals("null"))
		{
			str = this.GetRequestVal("FK_MapData");
		}

		if (str == null || str.equals("") || str.equals("null"))
		{
			str = this.GetRequestVal("FrmID");
		}

		if (str == null || str.equals("") || str.equals("null"))
		{
			if (this.getEnName() == null)
			{
				return null;
			}
			return this.getEnName() + "s";
		}
		return str;
	}

	/** 
	 树形结构的类名
	*/
	public final String getTreeEnsName()
	{
		String str = this.GetRequestVal("TreeEnsName");
		if (str == null || str.equals("") || str.equals("null"))
		{
			if (this.getEnName() == null)
			{
				return null;
			}
			return this.getEnName() + "s";
		}
		return str;
	}
	/** 
	 部门编号
	*/
	public final String getFK_Dept()
	{
		String str = this.GetRequestVal("FK_Dept");
		if (str == null || str.equals("") || str.equals("null"))
		{
			return null;
		}
		return str;
	}

	/** 
	 主键
	*/
	public final String getMyPK()
	{
		String str = this.GetRequestVal("MyPK");
		if (DataType.IsNullOrEmpty(str))
		{
			return null;
		}
		return str;
	}
	public final String getFK_Event()
	{
		String str = this.GetRequestVal("FK_Event");
		if (DataType.IsNullOrEmpty(str))
		{
			return null;
		}
		return str;
	}
	/** 
	 字典表
	*/
	public final String getFK_SFTable()
	{
		String str = this.GetRequestVal("FK_SFTable");
		if (DataType.IsNullOrEmpty(str))
		{
			return null;
		}
		return str;
	}
	public final String getEnumKey()
	{
		String str = this.GetRequestVal("EnumKey");
		if (DataType.IsNullOrEmpty(str))
		{
			return null;
		}
		return str;
	}
	public final String getKey()
	{
		String str = this.GetRequestVal("Key");
		if (DataType.IsNullOrEmpty(str))
		{
			return null;
		}
		return str;
	}
	public final String getKeyOfEn()
	{
		String str = this.GetRequestVal("KeyOfEn");
		if (DataType.IsNullOrEmpty(str))
		{
			return null;
		}
		return str;
	}
	/** 
	 FK_MapData
	*/
	public final String getFK_MapData()
	{
		String str = this.GetRequestVal("FK_MapData");
		if (DataType.IsNullOrEmpty(str))
		{
			str = this.GetRequestVal("FrmID");
		}
		if (DataType.IsNullOrEmpty(str))
		{
			str = this.GetRequestVal("EnsName");
		}
		return str;
	}
	/** 
	 扩展信息
	*/
	public final String getFK_MapExt()
	{
		String str = this.GetRequestVal("FK_MapExt");
		if (DataType.IsNullOrEmpty(str))
		{
			str = this.GetRequestVal("MyPK");
			if (DataType.IsNullOrEmpty(str) == true)
			{
				return null;
			}
		}


		return str;
	}
	/** 
	 流程编号
	*/
	public final String getFK_Flow()
	{
		String str = this.GetRequestVal("FK_Flow");
		if (str == null || str.equals("") || str.equals("null"))
		{
			return null;
		}

		if (DataType.IsNumStr(str) == false)
		{
			return "err@";
		}

		return str;
	}
	/** 
	 人员编号
	*/
	public final String getFK_Emp()
	{
		String str = this.GetRequestVal("FK_Emp");
		if (DataType.IsNullOrEmpty(str) == true)
		{
			return null;
		}
		return str;
	}
	/** 
	 相关编号
	*/
	public final String getRefNo()
	{
		String str = this.GetRequestVal("RefNo");
		if (DataType.IsNullOrEmpty(str) == true)
		{
			return null;
		}
		return str;
	}

	/** 
	 表单ID
	*/
	public final String getFrmID()
	{
		String str = this.GetRequestVal("FrmID");
		if (DataType.IsNullOrEmpty(str) == true)
		{
			return this.GetRequestVal("FK_MapData");
		}

		return str;
	}
	public final int getGroupField()
	{
		String str = this.GetRequestVal("GroupField");
		if (DataType.IsNullOrEmpty(str) == true)
		{
			return 0;
		}

		return Integer.parseInt(str);
	}
	/** 
	 节点ID
	*/
	public final int getFK_Node()
	{
		int nodeID = this.GetRequestValInt("FK_Node");
		if (nodeID == 0)
		{
			nodeID = this.GetRequestValInt("NodeID");
		}
		return nodeID;
	}
	public final long getFID()
	{
		return this.GetRequestValInt("FID");

		String str = this.GetRequestVal("FID"); //  context.Request.QueryString["FID"];
		if (DataType.IsNullOrEmpty(str) == true)
		{
			return 0;
		}
		return Integer.parseInt(str);
	}

	private long _workID = 0;
	public final long getWorkID()
	{
		if (_workID != 0)
		{
			return _workID;
		}

		String str = this.GetRequestVal("WorkID");
		if (DataType.IsNullOrEmpty(str) == true)
		{
			str = this.GetRequestVal("PKVal");
			if (DataType.IsNumStr(str) == false)
			{
				return 0;
			}
		}

		if (DataType.IsNullOrEmpty(str) == true)
		{
			str = this.GetRequestVal("OID");
		}

		if (DataType.IsNullOrEmpty(str) == true)
		{
			return 0;
		}

		return Integer.parseInt(str);
	}
	public final void setWorkID(long value)
	{
		_workID = value;
	}
	public final long getCWorkID()
	{
		return this.GetRequestValInt("CWorkID");
	}
	/** 
	 框架ID
	*/
	public final String getFK_MapFrame()
	{


		String str = this.GetRequestVal("FK_MapFrame"); // context.Request.QueryString["FK_MapFrame"];
		if (DataType.IsNullOrEmpty(str) == true)
		{
			return null;
		}
		return str;
	}
	/** 
	 SID
	*/
	public final String getSID()
	{
		String str = this.GetRequestVal("SID"); // context.Request.QueryString["SID"];
		if (DataType.IsNullOrEmpty(str) == true)
		{
			return null;
		}
		return str;
	}
	/** 
	   RefOID
	*/
	public final int getRefOID()
	{
		String str = this.GetRequestVal("RefOID"); //context.Request.QueryString["RefOID"];

		if (DataType.IsNullOrEmpty(str) == true)
		{
			str = this.GetRequestVal("OID"); //  context.Request.QueryString["OID"];
		}

		if (DataType.IsNullOrEmpty(str) == true)
		{
			return 0;
		}

		return Integer.parseInt(str);
	}
	public final int getOID()
	{
		String str = this.GetRequestVal("RefOID"); // context.Request.QueryString["RefOID"];
		if (DataType.IsNullOrEmpty(str) == true)
		{
			str = this.GetRequestVal("OID"); //context.Request.QueryString["OID"];
		}

		if (DataType.IsNullOrEmpty(str) == true)
		{
			return 0;
		}

		return Integer.parseInt(str);
	}
	/** 
	 明细表
	*/
	public final String getFK_MapDtl()
	{
		String str = this.GetRequestVal("FK_MapDtl"); //context.Request.QueryString["FK_MapDtl"];
		if (DataType.IsNullOrEmpty(str) == true)
		{
			str = this.GetRequestVal("EnsName"); // context.Request.QueryString["EnsName"];
		}
		return str;
	}
	/** 
	 页面Index.
	*/
	public final int getPageIdx()
	{
		return this.GetRequestValInt("PageIdx");
	}
	/** 
	 页面大小
	*/
	public final int getPageSize()
	{
		int i = this.GetRequestValInt("PageSize");
		if (i == 0)
		{
			return 10;
		}
		return i;
	}
	public final int getIndex()
	{
		return this.GetRequestValInt("Index");
	}

	/** 
	 字段属性编号
	*/
	public final String getAth()
	{
		String str = this.GetRequestVal("Ath"); // context.Request.QueryString["Ath"];
		if (DataType.IsNullOrEmpty(str) == true)
		{
			return null;
		}
		return str;
	}

	/** 
	 获得Int数据
	 
	 @param key
	 @return 
	*/
	public final int GetValIntFromFrmByKey(String key)
	{
		String str = this.GetValFromFrmByKey(key);
		if (str == null || str.equals(""))
		{
			throw new RuntimeException("@参数:" + key + "没有取到值.");
		}
		return Integer.parseInt(str);
	}

	public final float GetValFloatFromFrmByKey(String key)
	{
		String str = this.GetValFromFrmByKey(key);
		if (str == null || str.equals(""))
		{
			throw new RuntimeException("@参数:" + key + "没有取到值.");
		}
		return Float.parseFloat(str);
	}
	public final BigDecimal GetValDecimalFromFrmByKey(String key)
	{
		String str = this.GetValFromFrmByKey(key);
		if (str == null || str.equals(""))
		{
			throw new RuntimeException("@参数:" + key + "没有取到值.");
		}
		return BigDecimal.Parse(str);
	}

	public final boolean GetValBoolenFromFrmByKey(String key)
	{
		String val = this.GetValFromFrmByKey(key, "0");
		if (val.equals("on") || val.equals("1"))
		{
			return true;
		}
		if (val == null || val.equals("") || val.equals("0") || val.equals("off"))
		{
			return false;
		}
		return true;
	}

	public final String getRefPK()
	{
		return this.GetRequestVal("RefPK");

			//string str = this.context.Request.QueryString["RefPK"];
			//return str;
	}
	public final String getRefPKVal()
	{
		String str = this.GetRequestVal("RefPKVal");
		if (str == null)
		{
			return "0";
		}
		return str;
	}

		///#endregion 属性.


		///#region 父子流程相关的属性.
	public final long getPWorkID()
	{
		return this.GetRequestValInt64("PWorkID");
	}
	public final long getPFID()
	{
		return this.GetRequestValInt64("PFID");
	}
	public final int getPNodeID()
	{
		return this.GetRequestValInt("PNodeID");
	}
	public final String getPFlowNo()
	{
		return this.GetRequestVal("PFlowNo");
	}

		///#endregion 父子流程相关的属性.


	protected final String ExportGroupExcel(System.Data.DataSet ds, String title, String paras)
	{
		DataTable dt = ds.Tables["GroupSearch"];
		DataTable AttrsOfNum = ds.Tables["AttrsOfNum"];
		DataTable AttrsOfGroup = ds.Tables["AttrsOfGroup"];

		title = title.trim();
		String filename = title + "Ep" + title + ".xls";
		String file = filename;
		boolean flag = true;
		String filepath = BP.Sys.SystemConfig.PathOfTemp;


			///#region 参数及变量设置

		if ((new File(filepath)).isDirectory() == false)
		{
			(new File(filepath)).mkdirs();
		}



		filename = filepath + filename;

		//filename = HttpUtility.UrlEncode(filename);

		FileOutputStream objFileStream = new FileOutputStream(filename);
		OutputStreamWriter objStreamWriter = new OutputStreamWriter(objFileStream, java.nio.charset.StandardCharsets.UTF_16LE);

			///#endregion


			///#region 生成导出文件
		try
		{
			objStreamWriter.write((char)9 + title + (char)9 + System.lineSeparator());
			String strLine = "序号" + (char)9;
			//生成文件标题
			for (DataRow attr : AttrsOfGroup.Rows)
			{
				strLine = strLine + attr.get("Name") + (char)9;
			}
			for (DataRow attr : AttrsOfNum.Rows)
			{
				strLine = strLine + attr.get("Name") + (char)9;
			}

			objStreamWriter.write(strLine + System.lineSeparator());
			strLine = "";
			for (DataRow dr : dt.Rows)
			{
				strLine = strLine + dr.get("IDX") + (char)9;
				for (DataRow attr : AttrsOfGroup.Rows)
				{
					strLine = strLine + dr.get(attr.get("KeyOfEn") + "T") + (char)9;

				}
				for (DataRow attr : AttrsOfNum.Rows)
				{

					strLine = strLine + dr.get(attr.get("KeyOfEn").toString()) + (char)9;
				}

				objStreamWriter.write(strLine + System.lineSeparator());
				strLine = "";
			}

			strLine = "汇总" + (char)9;
			for (DataRow attr : AttrsOfGroup.Rows)
			{

				strLine = strLine + (char)9;
			}

			for (DataRow attr : AttrsOfNum.Rows)
			{
				double d = 0;
				for (DataRow dtr : dt.Rows)
				{
					d += Double.parseDouble(dtr.get(attr.get("KeyOfEn").toString()).toString());
				}
				if (paras.contains(attr.get("KeyOfEn") + "=AVG"))
				{
					if (dt.Rows.size() != 0)
					{
						d = Double.parseDouble((d / dt.Rows.size()).toString("0.0000"));
					}

				}

				if (Integer.parseInt(attr.get("MyDataType").toString()) == DataType.AppInt)
				{
					if (paras.contains(attr.get("KeyOfEn") + "=AVG"))
					{
						strLine = strLine + d + (char)9;
					}
					else
					{
						strLine = strLine + (int)d + (char)9;
					}
				}
				else
				{
				   strLine = strLine + d + (char)9;
				   ;
				}

			}

			objStreamWriter.write(strLine + System.lineSeparator());
			strLine = "";
		}
		catch (java.lang.Exception e)
		{
			flag = false;
		}
		finally
		{
			objStreamWriter.close();
			objFileStream.close();
		}

			///#endregion


			///#region 删除掉旧的文件
		//DelExportedTempFile(filepath);

			///#endregion

		if (flag)
		{
			file = "/DataUser/Temp/" + file;

		}

		return file;
	}

	protected final String ExportDGToExcel(System.Data.DataTable dt, Entity en, String title, Attrs mapAttrs)
	{
		return ExportDGToExcel(dt, en, title, mapAttrs, null);
	}

	protected final String ExportDGToExcel(System.Data.DataTable dt, Entity en, String title)
	{
		return ExportDGToExcel(dt, en, title, null, null);
	}

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: protected string ExportDGToExcel(System.Data.DataTable dt, Entity en, string title,Attrs mapAttrs=null,string filename=null)
	protected final String ExportDGToExcel(System.Data.DataTable dt, Entity en, String title, Attrs mapAttrs, String filename)
	{
		if (filename == null)
		{
			filename = title + "_" + BP.DA.DataType.CurrentDataCNOfLong + "_" + WebUser.getName() + ".xls"; //"Ep" + this.Session.SessionID + ".xls";
		}
		String file = filename;
		boolean flag = true;
		String filepath = BP.Sys.SystemConfig.PathOfTemp;


			///#region 参数及变量设置


		//如果导出目录没有建立，则建立.
		if ((new File(filepath)).isDirectory() == false)
		{
			(new File(filepath)).mkdirs();
		}

		filename = filepath + filename;

		if ((new File(filename)).isFile())
		{
			(new File(filename)).delete();
		}

		FileOutputStream objFileStream = new FileOutputStream(filename);
		OutputStreamWriter objStreamWriter = new OutputStreamWriter(objFileStream, java.nio.charset.StandardCharsets.UTF_16LE);

			///#endregion


			///#region 生成导出文件
		try
		{
			Attrs attrs = null;
			if (mapAttrs != null)
			{
				attrs = mapAttrs;
			}
			else
			{
				attrs = en.getEnMap().getAttrs();
			}

			Attrs selectedAttrs = null;
			BP.Sys.UIConfig cfg = new UIConfig(en);

			if (cfg.ShowColumns.Length == 0)
			{
				selectedAttrs = attrs;
			}
			else
			{
				selectedAttrs = new Attrs();

				for (Attr attr : attrs)
				{
					boolean contain = false;

					for (String col : cfg.ShowColumns)
					{
						if (col.equals(attr.getKey()))
						{
							contain = true;
							break;
						}
					}

					if (contain)
					{
						selectedAttrs.Add(attr);
					}
				}
			}

			objStreamWriter.WriteLine();
			objStreamWriter.write((char)9 + title + (char)9 + System.lineSeparator());
			objStreamWriter.WriteLine();
			String strLine = "";

			//生成文件标题
			for (Attr attr : selectedAttrs)
			{
				if (attr.Key.equals("OID"))
				{
					continue;
				}

				if (attr.Key.equals("WorkID"))
				{
					continue;
				}

				if (attr.Key.equals("MyNum"))
				{
					continue;
				}

				if (attr.IsFKorEnum)
				{
					continue;
				}

				if (attr.UIVisible == false && attr.MyFieldType != FieldType.RefText)
				{
					continue;
				}

				if (attr.Key.equals("MyFilePath") || attr.Key.equals("MyFileExt") || attr.Key.equals("WebPath") || attr.Key.equals("MyFileH") || attr.Key.equals("MyFileW") || attr.Key.equals("MyFileSize") || attr.Key.equals("RefPK"))
				{
					continue;
				}

				if (attr.MyFieldType == FieldType.RefText)
				{
					strLine = strLine + attr.Desc.Replace("名称","") + (char)9;
				}
				else
				{
					strLine = strLine + attr.getDesc() + (char)9;
				}
			}

			objStreamWriter.write(strLine + System.lineSeparator());
			strLine = "";

			for (DataRow dr : dt.Rows)
			{
				for (Attr attr : selectedAttrs)
				{
					if (attr.IsFKorEnum)
					{
						continue;
					}

					if (attr.UIVisible == false && attr.MyFieldType != FieldType.RefText)
					{
						continue;
					}

					if (attr.Key.equals("OID"))
					{
						continue;
					}

					if (attr.Key.equals("MyNum"))
					{
						continue;
					}

					if (attr.Key.equals("WorkID"))
					{

						continue;
					}

					if (attr.Key.equals("MyFilePath") || attr.Key.equals("MyFileExt") || attr.Key.equals("WebPath") || attr.Key.equals("MyFileH") || attr.Key.equals("MyFileW") || attr.Key.equals("MyFileSize") || attr.Key.equals("RefPK"))
					{
						continue;
					}



					if (attr.MyDataType == DataType.AppBoolean)
					{
						strLine = strLine + (dr.get(attr.getKey()).equals(1) ? "是" : "否") + (char)9;
					}
					else
					{
						String text = dr.get(attr.IsFKorEnum ? (attr.Key + "Text") : attr.getKey()).toString();
						if (attr.Key.equals("FK_NY") && DataType.IsNullOrEmpty(text) == true)
						{
						   text = dr.get(attr.getKey()).toString();
						}
						if (DataType.IsNullOrEmpty(text) == false && (text.contains("\n") == true || text.contains("\r") == true))
						{
							text = text.replace("\n", " ");
							text = text.replace("\r", " ");
						}
						strLine = strLine + text + (char)9;
					}
				}

				objStreamWriter.write(strLine + System.lineSeparator());
				strLine = "";
			}


			objStreamWriter.WriteLine();
			objStreamWriter.write((char)9 + " 制表人：" + (char)9 + WebUser.getName() + (char)9 + "日期：" + (char)9 + LocalDateTime.now().ToShortDateString() + System.lineSeparator());

		}
		catch (java.lang.Exception e)
		{
			flag = false;
		}
		finally
		{
			objStreamWriter.close();
			objFileStream.close();
		}

			///#endregion


			///#region 删除掉旧的文件
		//DelExportedTempFile(filepath);

			///#endregion

		if (flag)
		{
		   file = "/DataUser/Temp/" + file;
			//this.Write_Javascript(" window.open('"+ Request.ApplicationPath + @"/Report/Exported/" + filename +"'); " );
			//this.Write_Javascript(" window.open('"+Request.ApplicationPath+"/Temp/" + file +"'); " );
		}

		return file;
	}


	public static String DataTableToExcel(DataTable dt, String filename, String header, String creator, boolean date, boolean index)
	{
		return DataTableToExcel(dt, filename, header, creator, date, index, false);
	}

	public static String DataTableToExcel(DataTable dt, String filename, String header, String creator, boolean date)
	{
		return DataTableToExcel(dt, filename, header, creator, date, true, false);
	}

	public static String DataTableToExcel(DataTable dt, String filename, String header, String creator)
	{
		return DataTableToExcel(dt, filename, header, creator, false, true, false);
	}

	public static String DataTableToExcel(DataTable dt, String filename, String header)
	{
		return DataTableToExcel(dt, filename, header, null, false, true, false);
	}

	public static String DataTableToExcel(DataTable dt, String filename)
	{
		return DataTableToExcel(dt, filename, null, null, false, true, false);
	}

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public static string DataTableToExcel(DataTable dt, string filename, string header = null, string creator = null, bool date = false, bool index = true, bool download = false)
	public static String DataTableToExcel(DataTable dt, String filename, String header, String creator, boolean date, boolean index, boolean download)
	{

		String file = BP.Sys.SystemConfig.PathOfTemp + filename;
		boolean flag = true;

		String dir = (new File(file)).getParent();
		String name = (new File(filename)).getName();
		long len = 0;
		IRow row = null, headerRow = null, dateRow = null, sumRow = null, creatorRow = null;
		ICell cell = null;
		int r = 0;
		int c = 0;
		int headerRowIndex = 0; //文件标题行序
		int dateRowIndex = 0; //日期行序
		int titleRowIndex = 0; //列标题行序
		int sumRowIndex = 0; //合计行序
		int creatorRowIndex = 0; //创建人行序
		float DEF_ROW_HEIGHT = 20; //默认行高
		float charWidth = 0; //单个字符宽度
		int columnWidth = 0; //列宽，像素
		boolean isDate; //是否是日期格式，否则是日期时间格式
		int decimalPlaces = 2; //小数位数
		boolean qian; //是否使用千位分隔符
		ArrayList<Integer> sumColumns = new ArrayList<Integer>(); //合计列序号集合

		if ((new File(dir)).isDirectory() == false)
		{
			(new File(dir)).mkdirs();
		}


		//一个字符的像素宽度，以Arial，10磅，i进行测算
		try (Bitmap bmp = new Bitmap(10, 10))
		{
			try (Graphics g = Graphics.FromImage(bmp))
			{
				charWidth = g.MeasureString("i", new Font("Arial", 10)).Width;
			}
		}
		//序
		if (index && dt.Columns.Contains("序") == false)
		{
			dt.Columns.Add("序", Integer.class).ExtendedProperties.Add("width", 50);
			dt.Columns.get("序"].SetOrdinal(0);

			for (int i = 0; i < dt.Rows.size(); i++)
			{
				dt.Rows[i]["序"] = i + 1;
			}
		}
		//合计列
		for (DataColumn col : dt.Columns)
		{
			if (col.ExtendedProperties.ContainsKey("sum") == false)
			{
				continue;
			}

			sumColumns.add(col.Ordinal);
		}

		headerRowIndex = DataType.IsNullOrEmpty(header) ? -1 : 0;
		dateRowIndex = date ? (headerRowIndex + 1) : -1;
		titleRowIndex = date ? dateRowIndex + 1 : headerRowIndex == -1 ? 0 : 1;
		sumRowIndex = sumColumns.isEmpty() ? -1 : titleRowIndex + dt.Rows.size() + 1;
		creatorRowIndex = DataType.IsNullOrEmpty(creator) ? -1 : sumRowIndex == -1 ? titleRowIndex + dt.Rows.size() + 1 : sumRowIndex + 1;

		try (FileStream fs = new FileStream(file, FileMode.Create))
		{
			HSSFWorkbook wb = new HSSFWorkbook();
			ISheet sheet = wb.CreateSheet("Sheet1");
			sheet.DefaultRowHeightInPoints = DEF_ROW_HEIGHT;
			IFont font = null;
			IDataFormat fmt = wb.CreateDataFormat();

			if (headerRowIndex != -1)
			{
				headerRow = sheet.CreateRow(headerRowIndex);
			}
			if (date)
			{
				dateRow = sheet.CreateRow(dateRowIndex);
			}
			if (sumRowIndex != -1)
			{
				sumRow = sheet.CreateRow(sumRowIndex);
			}
			if (creatorRowIndex != -1)
			{
				creatorRow = sheet.CreateRow(creatorRowIndex);
			}


				///#region 单元格样式定义
			//列标题单元格样式设定
			ICellStyle titleStyle = wb.CreateCellStyle();
			titleStyle.BorderTop = NPOI.SS.UserModel.BorderStyle.Thin;
			titleStyle.BorderBottom = NPOI.SS.UserModel.BorderStyle.Thin;
			titleStyle.BorderLeft = NPOI.SS.UserModel.BorderStyle.Thin;
			titleStyle.BorderRight = NPOI.SS.UserModel.BorderStyle.Thin;
			titleStyle.VerticalAlignment = VerticalAlignment.Center;
			font = wb.CreateFont();
			font.IsBold = true;
			titleStyle.SetFont(font);

			//"序"列标题样式设定
			ICellStyle idxTitleStyle = wb.CreateCellStyle();
			idxTitleStyle.CloneStyleFrom(titleStyle);
			idxTitleStyle.Alignment = HorizontalAlignment.Center;

			//文件标题单元格样式设定
			ICellStyle headerStyle = wb.CreateCellStyle();
			headerStyle.Alignment = HorizontalAlignment.Center;
			headerStyle.VerticalAlignment = VerticalAlignment.Center;
			font = wb.CreateFont();
			font.FontHeightInPoints = 12;
			font.IsBold = true;
			headerStyle.SetFont(font);

			//制表人单元格样式设定
			ICellStyle userStyle = wb.CreateCellStyle();
			userStyle.Alignment = HorizontalAlignment.Right;
			userStyle.VerticalAlignment = VerticalAlignment.Center;

			//单元格样式设定
			ICellStyle cellStyle = wb.CreateCellStyle();
			cellStyle.BorderTop = NPOI.SS.UserModel.BorderStyle.Thin;
			cellStyle.BorderBottom = NPOI.SS.UserModel.BorderStyle.Thin;
			cellStyle.BorderLeft = NPOI.SS.UserModel.BorderStyle.Thin;
			cellStyle.BorderRight = NPOI.SS.UserModel.BorderStyle.Thin;
			cellStyle.VerticalAlignment = VerticalAlignment.Center;

			//数字单元格样式设定
			ICellStyle numCellStyle = wb.CreateCellStyle();
			numCellStyle.CloneStyleFrom(cellStyle);
			numCellStyle.Alignment = HorizontalAlignment.Right;

			//"序"列单元格样式设定
			ICellStyle idxCellStyle = wb.CreateCellStyle();
			idxCellStyle.CloneStyleFrom(cellStyle);
			idxCellStyle.Alignment = HorizontalAlignment.Center;

			//日期单元格样式设定
			ICellStyle dateCellStyle = wb.CreateCellStyle();
			dateCellStyle.CloneStyleFrom(cellStyle);
			dateCellStyle.DataFormat = fmt.GetFormat("yyyy-m-d;@");

			//日期时间单元格样式设定
			ICellStyle timeCellStyle = wb.CreateCellStyle();
			timeCellStyle.CloneStyleFrom(cellStyle);
			timeCellStyle.DataFormat = fmt.GetFormat("yyyy-m-d h:mm;@");

			//千分位单元格样式设定
			ICellStyle qCellStyle = wb.CreateCellStyle();
			qCellStyle.CloneStyleFrom(cellStyle);
			qCellStyle.Alignment = HorizontalAlignment.Right;
			qCellStyle.DataFormat = fmt.GetFormat("#,##0_ ;@");

			//小数点、千分位单元格样式设定
			HashMap<String, ICellStyle> cstyles = new HashMap<String, ICellStyle>();
			ICellStyle cstyle = null;

				///#endregion

			//输出列标题
			row = sheet.CreateRow(titleRowIndex);
			row.HeightInPoints = DEF_ROW_HEIGHT;

			for (DataColumn col : dt.Columns)
			{
				cell = row.CreateCell(c++);
				cell.SetCellValue(col.ColumnName);
				cell.CellStyle = col.ColumnName.equals("序") ? idxTitleStyle : titleStyle;

				columnWidth = col.ExtendedProperties.ContainsKey("width") ? (int)col.ExtendedProperties["width"] : 100;
				sheet.SetColumnWidth(c - 1, (int)(Math.ceil(columnWidth / charWidth) + 0.72) * 256);

				if (headerRow != null)
				{
					headerRow.CreateCell(c - 1);
				}
				if (dateRow != null)
				{
					dateRow.CreateCell(c - 1);
				}
				if (sumRow != null)
				{
					sumRow.CreateCell(c - 1);
				}
				if (creatorRow != null)
				{
					creatorRow.CreateCell(c - 1);
				}

				//定义数字列单元格样式
				switch (col.DataType.getSimpleName())
				{
					case "Single":
					case "Double":
					case "Decimal":
						decimalPlaces = col.ExtendedProperties.ContainsKey("dots") ? (int)col.ExtendedProperties["dots"] : 2;
						qian = col.ExtendedProperties.ContainsKey("k") ? (boolean)col.ExtendedProperties["k"] : false;

						if (decimalPlaces > 0 && !qian)
						{
							cstyle = wb.CreateCellStyle();
							cstyle.CloneStyleFrom(qCellStyle);
							cstyle.DataFormat = fmt.GetFormat("0." + tangible.StringHelper.padLeft("", decimalPlaces, '0') + "_ ;@");
						}
						else if (decimalPlaces == 0 && qian)
						{
							cstyle = wb.CreateCellStyle();
							cstyle.CloneStyleFrom(qCellStyle);
						}
						else if (decimalPlaces > 0 && qian)
						{
							cstyle = wb.CreateCellStyle();
							cstyle.CloneStyleFrom(qCellStyle);
							cstyle.DataFormat = fmt.GetFormat("#,##0." + tangible.StringHelper.padLeft("", decimalPlaces, '0') + "_ ;@");
						}

						cstyles.put(col.ColumnName, cstyle);
						break;
					default:
						break;
				}
			}
			//输出文件标题
			if (headerRow != null)
			{
				sheet.AddMergedRegion(new NPOI.SS.Util.CellRangeAddress(headerRowIndex, headerRowIndex, 0, dt.Columns.size() - 1));
				cell = headerRow.GetCell(0);
				cell.SetCellValue(header);
				cell.CellStyle = headerStyle;
				headerRow.HeightInPoints = 26;
			}
			//输出日期
			if (dateRow != null)
			{
				sheet.AddMergedRegion(new NPOI.SS.Util.CellRangeAddress(dateRowIndex, dateRowIndex, 0, dt.Columns.size() - 1));
				cell = dateRow.GetCell(0);
				cell.SetCellValue("日期：" + LocalDateTime.Today.toString("yyyy-MM-dd"));
				cell.CellStyle = userStyle;
				dateRow.HeightInPoints = DEF_ROW_HEIGHT;
			}
			//输出制表人
			if (creatorRow != null)
			{
				sheet.AddMergedRegion(new NPOI.SS.Util.CellRangeAddress(creatorRowIndex, creatorRowIndex, 0, dt.Columns.size() - 1));
				cell = creatorRow.GetCell(0);
				cell.SetCellValue("制表人：" + creator);
				cell.CellStyle = userStyle;
				creatorRow.HeightInPoints = DEF_ROW_HEIGHT;
			}

			r = titleRowIndex + 1;
			//输出查询结果
			for (DataRow dr : dt.Rows)
			{
				row = sheet.CreateRow(r++);
				row.HeightInPoints = DEF_ROW_HEIGHT;
				c = 0;

				for (DataColumn col : dt.Columns)
				{
					cell = row.CreateCell(c++);

					switch (col.DataType.getSimpleName())
					{
						case "Boolean":
							cell.CellStyle = cellStyle;
							cell.SetCellValue(Equals(dr.get(col.ColumnName), true) ? "是" : "否");
							break;
						case "DateTime":
							isDate = col.ExtendedProperties.ContainsKey("isdate") ? (boolean)col.ExtendedProperties["isdate"] : false;

							cell.CellStyle = isDate ? dateCellStyle : timeCellStyle;
							cell.SetCellValue(dr.get(col.ColumnName) instanceof String ? (String)dr.get(col.ColumnName) : null);
							break;
						case "Int16":
						case "Int32":
						case "Int64":
							qian = col.ExtendedProperties.ContainsKey("k") ? (boolean)col.ExtendedProperties["k"] : false;

							cell.CellStyle = col.ColumnName.equals("序") ? idxCellStyle : qian ? qCellStyle : numCellStyle;
							cell.SetCellValue((Long)dr.get(col.ColumnName));
							break;
						case "Single":
						case "Double":
						case "Decimal":
							cell.CellStyle = cstyles.get(col.ColumnName);
							cell.SetCellValue(((Double)dr.get(col.ColumnName)));
							break;
						default:
							cell.CellStyle = cellStyle;
							cell.SetCellValue(dr.get(col.ColumnName) instanceof String ? (String)dr.get(col.ColumnName) : null);
							break;
					}
				}
			}
			//合计
			if (sumRow != null)
			{
				sumRow.HeightInPoints = DEF_ROW_HEIGHT;

				for (c = 0; c < dt.Columns.size(); c++)
				{
					cell = sumRow.GetCell(c);
					cell.CellStyle = cellStyle;

					if (sumColumns.contains(c) == false)
					{
						continue;
					}

					cell.SetCellFormula(String.format("SUM(%1$s:%2$s)", GetCellName(c, titleRowIndex + 1), GetCellName(c, titleRowIndex + dt.Rows.size())));
				}
			}

			wb.Write(fs);
			len = fs.Length;
		}


		return null;
	}

	/** 
	 获取单元格的显示名称，格式如A1,B2
	 
	 @param columnIdx 单元格列号
	 @param rowIdx 单元格行号
	 @return 
	*/
	public static String GetCellName(int columnIdx, int rowIdx)
	{
		int[] maxs = new int[] {26, 26 * 26 + 26, 26 * 26 * 26 + (26 * 26 + 26) + 26};
		int col = columnIdx + 1;
		int row = rowIdx + 1;

		if (col > maxs[2])
		{
			throw new RuntimeException("列序号不正确，超出最大值");
		}

		int alphaCount = 1;

		for (int m : maxs)
		{
			if (m < col)
			{
				alphaCount++;
			}
		}

		switch (alphaCount)
		{
			case 1:
				return (char)(col + 64) + row;
			case 2:
				return (char)((col / 26) + 64) + (char)((col % 26) + 64) + row;
			case 3:
				return (char)((col / 26 / 26) + 64) + (char)(((col - col / 26 / 26 * 26 * 26) / 26) + 64) + (char)((col % 26) + 64) + row;
		}

		return "Unkown";
	}

}