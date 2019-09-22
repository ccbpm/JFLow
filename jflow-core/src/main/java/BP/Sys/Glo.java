package BP.Sys;

import BP.Sys.*;
import BP.DA.*;
import BP.Web.*;
import BP.En.*;
import java.util.*;
import java.io.*;
import java.math.*;

/** 
 公用的静态方法.
*/
public class Glo
{
	/** 
	 获得真实的数据类型
	 
	 @param attrs 属性集合
	 @param key key
	 @param val 值
	 @return 返回val真实的数据类型.
	*/
	public static Object GenerRealType(Attrs attrs, String key, Object val)
	{
		Attr attr = attrs.GetAttrByKey(key);
		switch (attr.getMyDataType())
		{
			case DataType.AppString:
			case DataType.AppDateTime:
			case DataType.AppDate:
				val = val.toString();
				break;
			case DataType.AppInt:
			case DataType.AppBoolean:
				val = Integer.parseInt(val.toString());
				break;
			case DataType.AppFloat:
				val = Float.parseFloat(val.toString());
				break;
			case DataType.AppDouble:

				val = Integer.parseInt(val.toString());
				break;
			case DataType.AppMoney:
				val = BigDecimal.Parse(val.toString());
				break;
			default:
				throw new RuntimeException();
				break;
		}
		return val;
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 业务单元.
	private static Hashtable Htable_BuessUnit = null;
	/** 
	 获得节点事件实体
	 
	 @param enName 实例名称
	 @return 获得节点事件实体,如果没有就返回为空.
	*/
	public static BuessUnitBase GetBuessUnitEntityByEnName(String enName)
	{
		if (Htable_BuessUnit == null || Htable_BuessUnit.isEmpty())
		{
			Htable_BuessUnit = new Hashtable();
			ArrayList al = BP.En.ClassFactory.GetObjects("BP.Sys.BuessUnitBase");
			for (BuessUnitBase en : al)
			{
				Htable_BuessUnit.put(en.toString(), en);
			}
		}

		BuessUnitBase myen = Htable_BuessUnit.get(enName) instanceof BuessUnitBase ? (BuessUnitBase)Htable_BuessUnit.get(enName) : null;
		if (myen == null)
		{
			//throw new Exception("@根据类名称获取业务单元实例出现错误:" + enName + ",没有找到该类的实体.");
			BP.DA.Log.DefaultLogWriteLineError("@根据类名称获取业务单元实例出现错误:" + enName + ",没有找到该类的实体.");
			return null;
		}
		return myen;
	}
	/** 
	 获得事件实体String，根据编号或者流程标记
	 
	 @param flowMark 流程标记
	 @param flowNo 流程编号
	 @return null, 或者流程实体.
	*/
	public static String GetBuessUnitEntityStringByFlowMark(String flowMark, String flowNo)
	{
		BuessUnitBase en = GetBuessUnitEntityByFlowMark(flowMark, flowNo);
		if (en == null)
		{
			return "";
		}
		return en.toString();
	}
	/** 
	 获得业务单元.
	 
	 @param flowMark 流程标记
	 @param flowNo 流程编号
	 @return null, 或者流程实体.
	*/
	public static BuessUnitBase GetBuessUnitEntityByFlowMark(String flowMark, String flowNo)
	{

		if (Htable_BuessUnit == null || Htable_BuessUnit.isEmpty())
		{
			Htable_BuessUnit = new Hashtable();
			ArrayList al = BP.En.ClassFactory.GetObjects("BP.Sys.BuessUnitBase");
			Htable_BuessUnit.clear();
			for (BuessUnitBase en : al)
			{
				Htable_BuessUnit.put(en.toString(), en);
			}
		}

		for (String key : Htable_BuessUnit.keySet())
		{
			BuessUnitBase fee = Htable_BuessUnit.get(key) instanceof BuessUnitBase ? (BuessUnitBase)Htable_BuessUnit.get(key) : null;
			if (fee.toString().equals(flowMark) || fee.toString().contains("," + flowNo + ",") == true)
			{
				return fee;
			}
		}
		return null;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 业务单元.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 与 表单 事件实体相关.
	private static Hashtable Htable_FormFEE = null;
	/** 
	 获得节点事件实体
	 
	 @param enName 实例名称
	 @return 获得节点事件实体,如果没有就返回为空.
	*/
	public static FormEventBase GetFormEventBaseByEnName(String enName)
	{
		if (Htable_FormFEE == null)
		{
			Htable_FormFEE = new Hashtable();

			ArrayList al = BP.En.ClassFactory.GetObjects("BP.Sys.FormEventBase");
			Htable_FormFEE.clear();

			for (FormEventBase en : al)
			{
				Htable_FormFEE.put(en.getFormMark(), en);
			}
		}

		for (String key : Htable_FormFEE.keySet())
		{
			FormEventBase fee = Htable_FormFEE.get(key) instanceof FormEventBase ? (FormEventBase)Htable_FormFEE.get(key) : null;

			if (key.contains(","))
			{
				if (key.indexOf(enName + ",") >= 0 || key.length() == key.indexOf("," + enName) + enName.length() + 1)
				{
					return fee;
				}
			}

			if (enName.equals(key))
			{
				return fee;
			}
		}

		return null;

	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 与 表单 事件实体相关.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 与 表单从表 事件实体相关.
	private static Hashtable Htable_FormFEEDtl = null;
	/** 
	 获得节点事件实体
	 
	 @param enName 实例名称
	 @return 获得节点事件实体,如果没有就返回为空.
	*/
	public static FormEventBaseDtl GetFormDtlEventBaseByEnName(String dtlEnName)
	{
		if (Htable_FormFEEDtl == null || Htable_FormFEEDtl.isEmpty())
		{
			Htable_FormFEEDtl = new Hashtable();
			ArrayList al = BP.En.ClassFactory.GetObjects("BP.Sys.FormEventBaseDtl");
			Htable_FormFEEDtl.clear();
			for (FormEventBaseDtl en : al)
			{
				Htable_FormFEEDtl.put(en.getFormDtlMark(), en);
			}
		}

		for (String key : Htable_FormFEEDtl.keySet())
		{
			FormEventBaseDtl fee = Htable_FormFEEDtl.get(key) instanceof FormEventBaseDtl ? (FormEventBaseDtl)Htable_FormFEEDtl.get(key) : null;
			if (fee.getFormDtlMark().indexOf(dtlEnName) >= 0 || fee.getFormDtlMark().equals(dtlEnName))
			{
				return fee;
			}
		}
		return null;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 与 表单 事件实体相关.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 公共变量.
	public static String Plant = "CCFlow";
	/** 
	 部门版本号
	*/
	public static String getDeptsVersion()
	{
		GloVar en = new GloVar();
		en.setNo("DeptsVersion");
		if (en.RetrieveFromDBSources() == 0)
		{
			en.setName("部门版本号");
			en.setVal(BP.DA.DataType.getCurrentDataTime());
			en.setGroupKey("Glo");
			en.Insert();
		}
		return en.getVal();
	}
	/** 
	 部门数量 - 用于显示ccim的下载进度.
	*/
	public static int getDeptsCount()
	{
		return BP.DA.DBAccess.RunSQLReturnValInt("SELECT COUNT(No) as Num FROM Port_Dept");
	}
	/** 
	 人员数量 - 用于显示ccim的下载进度.
	*/
	public static int getEmpsCount()
	{
		return BP.DA.DBAccess.RunSQLReturnValInt("SELECT COUNT(a.No) as Num FROM Port_Emp a, Port_Dept b WHERE A.FK_Dept=B.No AND A.No NOT IN ('admin','Guest')");
	}
	/** 
	 人员版本号
	*/
	public static String getUsersVersion()
	{
		GloVar en = new GloVar();
		en.setNo("UsersVersion");
		if (en.RetrieveFromDBSources() == 0)
		{
			en.setName("人员版本号");
			en.setVal(BP.DA.DataType.getCurrentDataTime());
			en.setGroupKey("Glo");
			en.Insert();
		}
		return en.getVal();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 公共变量.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 写入系统日志(写入的文件:\DataUser\Log\*.*)
	/** 
	 写入一条消息
	 
	 @param msg 消息
	*/
	public static void WriteLineInfo(String msg)
	{
		BP.DA.Log.DefaultLogWriteLineInfo(msg);
	}
	/** 
	 写入一条警告
	 
	 @param msg 消息
	*/
	public static void WriteLineWarning(String msg)
	{
		BP.DA.Log.DefaultLogWriteLineWarning(msg);
	}
	/** 
	 写入一条错误
	 
	 @param msg 消息
	*/
	public static void WriteLineError(String msg)
	{
		BP.DA.Log.DefaultLogWriteLineError(msg);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 写入系统日志

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 写入用户日志(写入的用户表:Sys_UserLog).
	/** 
	 写入用户日志
	 
	 @param logType 类型
	 @param empNo 操作员编号
	 @param msg 信息
	 @param ip IP
	*/
	public static void WriteUserLog(String logType, String empNo, String msg, String ip)
	{
		UserLog ul = new UserLog();
		ul.setMyPK(DBAccess.GenerGUID());
		ul.setFK_Emp(empNo);
		ul.setLogFlag(logType);
		ul.setDocs(msg);
		ul.setIP(ip);
		ul.setRDT(DataType.getCurrentDataTime());
		ul.Insert();
	}
	/** 
	 写入用户日志
	 
	 @param logType 日志类型
	 @param empNo 操作员编号
	 @param msg 消息
	*/
	public static void WriteUserLog(String logType, String empNo, String msg)
	{
		//为了提高运行效率，把这个地方屏蔽了。
		return;
		/*
		UserLog ul = new UserLog();
		ul.MyPK = DBAccess.GenerGUID();
		ul.FK_Emp = empNo;
		ul.LogFlag = logType;
		ul.Docs = msg;
		ul.RDT = DataType.CurrentDataTime;
		try
		{
		    if (BP.Sys.SystemConfig.IsBSsystem)
		        ul.IP = HttpContextHelper.Request.UserHostAddress;
		}
		catch
		{
		}
		ul.Insert();
		*/
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 写入用户日志.

	/** 
	 初始化附件信息
	 如果手工的上传的附件，就要把附加的信息映射出来.
	 
	 @param en
	*/
	public static void InitEntityAthInfo(BP.En.Entity en)
	{
		//求出保存路径.
		String path = en.getEnMap().FJSavePath;
		if (path.equals("") || path == null || path.equals(""))
		{
			path = BP.Sys.SystemConfig.getPathOfDataUser() + en.toString() + "\\";
		}

		if ((new File(path)).isDirectory() == false)
		{
			(new File(path)).mkdirs();
		}

		//获得该目录下所有的文件.
		String[] strs = (new File(path)).list(File::isFile);
		String pkval = en.getPKVal().toString();
		String myfileName = null;
		for (String str : strs)
		{
			if (str.contains(pkval + ".") == false)
			{
				continue;
			}

			myfileName = str;
			break;
		}

		if (myfileName == null)
		{
			return;
		}

		/* 如果包含这二个字段。*/
		String fileName = myfileName;
		fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);

		en.SetValByKey("MyFilePath", path);

		String ext = "";
		if (fileName.indexOf(".") != -1)
		{
			ext = fileName.substring(fileName.lastIndexOf(".") + 1);
		}

		String reldir = path;
		if (reldir.length() > SystemConfig.getPathOfDataUser().length())
		{
			reldir = reldir.substring(reldir.toLowerCase().indexOf("\\datauser\\") + "\\datauser\\".length()).replace("\\", "/");
		}
		else
		{
			reldir = "";
		}

		if (reldir.length() > 0 && Equals(reldir.charAt(0), '/') == true)
		{
			reldir = reldir.substring(1);
		}

		if (reldir.length() > 0 && Equals(reldir.charAt(reldir.length() - 1), '/') == false)
		{
			reldir += "/";
		}

		en.SetValByKey("MyFileExt", ext);
		en.SetValByKey("MyFileName", fileName);
		en.SetValByKey("WebPath", "/DataUser/" + reldir + en.getPKVal() + "." + ext);

		String fullFile = path + "\\" + en.getPKVal() + "." + ext;

		File info = new File(fullFile);
		en.SetValByKey("MyFileSize", BP.DA.DataType.PraseToMB(info.length()));
		if (DataType.IsImgExt(ext))
		{
			System.Drawing.Image img = System.Drawing.Image.FromFile(fullFile);
			en.SetValByKey("MyFileH", img.Height);
			en.SetValByKey("MyFileW", img.Width);
			img.Dispose();
		}
		en.Update();
	}

	public static System.Web.HttpRequest getRequest()
	{
		return System.Web.HttpContext.Current.Request;
	}


	/** 
	 产生消息,senderEmpNo是为了保证写入消息的唯一性，receiveid才是真正的接收者.
	 如果插入失败.
	 
	 @param fromEmpNo 发送人
	 @param now 发送时间
	 @param msg 消息内容
	 @param sendToEmpNo 接受人
	*/
	public static void SendMessageToCCIM(String fromEmpNo, String sendToEmpNo, String msg, String now)
	{
		//暂停对ccim消息提醒的支持.
		return;
	}
	/** 
	 处理生成提示信息,不友好的提示.
	 
	 @param alertInfo 从系统里抛出的错误信息.
	 @return 返回的友好提示信息.
	*/
	public static String GenerFriendlyAlertHtmlInfo(String alertInfo)
	{
		// 格式为: err@错误中文提示信息. tech@info 数据库错误,查询sqL为.
		return alertInfo;
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 加密解密文件.
	public static void File_JiaMi(String fileFullPath)
	{
		//南京宝旺达.
		if (SystemConfig.getCustomerNo().equals("BWDA"))
		{

		}
	}
	public static void File_JieMi(String fileFullPath)
	{
		//南京宝旺达.
		if (SystemConfig.getCustomerNo().equals("BWDA"))
		{

		}
	}
	/** 
	 字符串的解密
	 
	 @param str 加密的字符串
	 @return 返回解密后的字符串
	*/
	public static String String_JieMi(String str)
	{
		//南京宝旺达.
		if (SystemConfig.getCustomerNo().equals("BWDA"))
		{
			return str;
		}

		return str;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 加密解密文件.

}