package bp.sys.base;

import bp.sys.*;
import bp.da.*;
import bp.tools.Cryptos;
import bp.en.*;
import bp.difference.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.io.*;
import java.math.*;

/** 
 公用的静态方法.
*/
public class Glo
{

	public static String EntityJiaMi(String val)
	{
		return EntityJiaMi(val, false);
	}

	public static String EntityJiaMi(String val, boolean isJM)
	{
		if (isJM == false)
		{
			return val;
		}

		return val;

	}

	public static String EntityJieMi(String val)
	{
		return EntityJieMi(val, false);
	}

	public static String EntityJieMi(String val, boolean isJM)
	{
		if (isJM == false)
		{
			return val;
		}

		return val;

	}

	/** 
	 获得真实UserNo,如果是SAAS模式.
	*/
	public static String getUserNo()  {
		String empNo = "No";
		if (SystemConfig.getCCBPMRunModel() == CCBPMRunModel.SAAS)
		{
			empNo = "UserID as No";
		}
		return empNo;
	}
	public static String getUserNoWhitOutAS()  {
		String empNo = "No";
		if (SystemConfig.getCCBPMRunModel() == CCBPMRunModel.SAAS)
		{
			empNo = "UserID";
		}
		return empNo;
	}

	public static String SysEnum()
	{
		if (SystemConfig.getAppCenterDBType().equals(DBType.KingBaseR3)
				|| SystemConfig.getAppCenterDBType().equals(DBType.KingBaseR6))
			return "Sys_Enums";
		return "Sys_Enum";
	}
	/** 
	 处理命名空间.
	 
	 param enName 类名
	 @return 返回处理后的名字
	*/
	public static String DealClassEntityName(String enName)
	{
		if (DataType.IsNullOrEmpty(enName))
		{
			return "";
		}
		if (SystemConfig.Plant == bp.sys.Plant.CSharp)
		{
			return enName;
		}

		int idx = enName.lastIndexOf('.');
		if (idx <= -1)
		{
			return enName;
		}

		String str = enName.substring(0, idx).toLowerCase() + enName.substring(idx);
		return str;
	}
	/** 
	 清除设置的缓存.
	 
	 param frmID
	*/
	public static void ClearMapDataAutoNum(String frmID) throws Exception {
		//执行清空缓存到的AutoNum.
		MapData md = new MapData();
		md.setNo(frmID);
		if (md.RetrieveFromDBSources() != 0)
		{
			md.ClearAutoNumCash(true); //更新缓存.
		}
	}

	/** 
	 更新SID Or OrgNo 的SQL
	 用于集成所用
	 更新被集成的用户的user表
	*/
	public static String getUpdateSIDAndOrgNoSQL() throws Exception {
		return SystemConfig.GetValByKey("UpdateSIDAndOrgNoSQL", null);
	}

	public static HttpServletRequest getRequest()
	{
		return ContextHolderUtils.getRequest();
	}

	/** 
	 获得真实的数据类型
	 
	 param attrs 属性集合
	 param key key
	 param val 值
	 @return 返回val真实的数据类型.
	*/
	public static Object GenerRealType(Attrs attrs, String key, Object val)
	{
		Attr attr = attrs.GetAttrByKey(key);
		return GenerRealType(attr, val);
	}
	public static Object GenerRealType(Attr attr,  Object val){
		switch (attr.getMyDataType())
		{
			case DataType.AppString:
			case DataType.AppDateTime:
			case DataType.AppDate:
				val = val.toString();
				break;
			case DataType.AppInt:
			case DataType.AppBoolean:
				if (val == null || DataType.IsNullOrEmpty(val.toString()))
				{
					return 0;
				}
				val = Integer.parseInt(val.toString());
				break;
			case DataType.AppFloat:
				val = Float.parseFloat(val.toString());
				break;
			case DataType.AppDouble:
				val = Float.parseFloat(val.toString());
				break;
			case DataType.AppMoney:
				val =  new BigDecimal(val.toString());
				break;
			default:
				throw new RuntimeException();
				//break;
		}
		return val;
	}

		///#region 业务单元.
	private static Hashtable Htable_BuessUnit = null;
	/** 
	 获得节点事件实体
	 
	 param enName 实例名称
	 @return 获得节点事件实体,如果没有就返回为空.
	*/
	public static BuessUnitBase GetBuessUnitEntityByEnName(String enName)
	{
		if (Htable_BuessUnit == null || Htable_BuessUnit.isEmpty())
		{
			Htable_BuessUnit = new Hashtable();
			ArrayList<BuessUnitBase> al = bp.en.ClassFactory.GetObjects("bp.sys.BuessUnitBase");
			for (BuessUnitBase en : al)
			{
				Htable_BuessUnit.put(en.toString(), en);
			}
		}

		BuessUnitBase myen = Htable_BuessUnit.get(enName) instanceof BuessUnitBase ? (BuessUnitBase)Htable_BuessUnit.get(enName) : null;
		if (myen == null)
		{
			//throw new Exception("@根据类名称获取业务单元实例出现错误:" + enName + ",没有找到该类的实体.");
			Log.DebugWriteError("@根据类名称获取业务单元实例出现错误:" + enName + ",没有找到该类的实体.");
			return null;
		}
		return myen;
	}
	/** 
	 获得事件实体String，根据编号或者流程标记
	 
	 param flowMark 流程标记
	 param flowNo 流程编号
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
	 
	 param flowMark 流程标记
	 param flowNo 流程编号
	 @return null, 或者流程实体.
	*/
	public static BuessUnitBase GetBuessUnitEntityByFlowMark(String flowMark, String flowNo)
	{

		if (Htable_BuessUnit == null || Htable_BuessUnit.isEmpty())
		{
			Htable_BuessUnit = new Hashtable();
			ArrayList<BuessUnitBase> al = bp.en.ClassFactory.GetObjects("bp.sys.BuessUnitBase");
			Htable_BuessUnit.clear();
			for (BuessUnitBase en : al)
			{
				Htable_BuessUnit.put(en.toString(), en);
			}
		}

		for (Object key : Htable_BuessUnit.keySet())
		{
			BuessUnitBase fee = Htable_BuessUnit.get(key) instanceof BuessUnitBase ? (BuessUnitBase)Htable_BuessUnit.get(key) : null;
			if (fee.toString().equals(flowMark) || fee.toString().contains("," + flowNo + ",") == true)
			{
				return fee;
			}
		}
		return null;
	}

		///#endregion 业务单元.


		///#region 与 表单 事件实体相关.
	private static Hashtable Htable_FormFEE = null;
	/** 
	 获得节点事件实体
	 
	 param enName 实例名称
	 @return 获得节点事件实体,如果没有就返回为空.
	*/
	public static FormEventBase GetFormEventBaseByEnName(String enName)
	{
		if (Htable_FormFEE == null)
		{
			Htable_FormFEE = new Hashtable();

			ArrayList<FormEventBase> al = bp.en.ClassFactory.GetObjects("bp.sys.FormEventBase");
			Htable_FormFEE.clear();

			for (FormEventBase en : al)
			{
				Htable_FormFEE.put(en.getFormMark(), en);
			}
		}

		for (Object key : Htable_FormFEE.keySet())
		{
			FormEventBase fee = Htable_FormFEE.get(key) instanceof FormEventBase ? (FormEventBase)Htable_FormFEE.get(key) : null;

			if (key.toString().contains(","))
			{
				if (key.toString().indexOf(enName + ",") >= 0 || key.toString().length() == key.toString().indexOf("," + enName) + enName.length() + 1)
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

		///#endregion 与 表单 事件实体相关.


		///#region 与 表单从表 事件实体相关.
	private static Hashtable Htable_FormFEEDtl = null;
	/** 
	 获得节点事件实体
	 param dtlEnName 实例名称
	 @return 获得节点事件实体,如果没有就返回为空.
	*/
	public static FormEventBaseDtl GetFormDtlEventBaseByEnName(String dtlEnName)
	{
		if (Htable_FormFEEDtl == null || Htable_FormFEEDtl.isEmpty())
		{
			Htable_FormFEEDtl = new Hashtable();
			ArrayList<FormEventBaseDtl> al = ClassFactory.GetObjects("BP.Sys.Base.FormEventBaseDtl");
			Htable_FormFEEDtl.clear();
			for (FormEventBaseDtl en : al)
			{
				Htable_FormFEEDtl.put(en.getFormDtlMark(), en);
			}
		}

		for (Object key : Htable_FormFEEDtl.keySet())
		{
			FormEventBaseDtl fee = Htable_FormFEEDtl.get(key) instanceof FormEventBaseDtl ? (FormEventBaseDtl)Htable_FormFEEDtl.get(key) : null;
			if (fee.getFormDtlMark().indexOf(dtlEnName) >= 0 || fee.getFormDtlMark().equals(dtlEnName))
			{
				return fee;
			}
		}
		return null;
	}

		///#endregion 与 表单 事件实体相关.


		///#region 公共变量.
	public static String Plant = "CCFlow";
	/** 
	 部门版本号
	*/
	public static String getDeptsVersion() throws Exception {
		GloVar en = new GloVar();
		en.setNo("DeptsVersion");
		if (en.RetrieveFromDBSources() == 0)
		{
			en.setName("部门版本号");
			en.setVal(DataType.getCurrentDateTime());
			en.setGroupKey("Glo");
			en.Insert();
		}
		return en.getVal();
	}
	/** 
	 人员版本号
	*/
	public static String getUsersVersion() throws Exception {
		GloVar en = new GloVar();
		en.setNo("UsersVersion");
		if (en.RetrieveFromDBSources() == 0)
		{
			en.setName("人员版本号");
			en.setVal(DataType.getCurrentDateTime());
			en.setGroupKey("Glo");
			en.Insert();
		}
		return en.getVal();
	}

		///#endregion 公共变量.


		///#region 写入系统日志(写入的文件:\DataUser\Log\*.*)
	/** 
	 写入一条消息
	 
	 param msg 消息
	*/
	public static void WriteLineInfo(String msg)
	{
		Log.DebugWriteInfo(msg);
	}

	/** 写入一条警告
	 
	 param msg 消息
	*/
	public static void WriteLineWarning(String msg)
	{
		Log.DebugWriteWarning(msg);
	}
	/** 
	 写入一条错误
	 
	 param msg 消息
	*/
	public static void WriteLineError(String msg)
	{
		Log.DebugWriteError(msg);
	}

		///#endregion 写入系统日志


		///#region 写入用户日志(写入的用户表:Sys_UserLog).

	/** 
	 写入用户日志

	 param msg 日志类型
	 param msg 消息
	*/

	public static void WriteUserLog(String msg) throws Exception {

		WriteUserLog(msg, "通用操作");
	}

//ORIGINAL LINE: public static void WriteUserLog(string msg, string logType = "通用操作")
	public static void WriteUserLog(String msg, String logType) throws Exception {
		/*if (SystemConfig.GetValByKeyBoolen("IsEnableLog", false) == false)
		{
			return;
		}

	//    string sql = "INSERT INTO Sys_Log (id,title,exception,) value('" + DBAccess.GenerGUID() + "','" + logType + "','" + msg + "')";

		UserLog ul = new UserLog();
		ul.setMyPK(DBAccess.GenerGUID());
		ul.setEmpNo(WebUser.getNo());
		ul.setEmpName(WebUser.getName());

		ul.setLogFlag(logType);
		ul.setDocs(msg);
		ul.setRDT(DataType.getCurrentDateTime());
		try
		{
			if (SystemConfig.getIsBSsystem())
			{
				ul.setIP(HttpContextHelper.getRequest().UserHostAddress);
			}
		}
		catch (java.lang.Exception e)
		{
		}
		ul.Insert();*/
	}

		///#endregion 写入用户日志.

	/** 
	 初始化附件信息
	 如果手工的上传的附件，就要把附加的信息映射出来.
	 
	 param en
	*/
	public static void InitEntityAthInfo(Entity en) throws Exception {
		//求出保存路径.
		String path = en.getEnMap().FJSavePath;
		if (path.equals("") || path == null || path.equals(""))
		{
			path = SystemConfig.getPathOfDataUser() + en.toString() + "/";
		}

		if ((new File(path)).isDirectory() == false)
		{
			(new File(path)).mkdirs();
		}

		//获得该目录下所有的文件.
		//String[] strs = (new File(path)).list(File:isFile);
		String[] strs=bp.tools.BaseFileUtils.getFiles (path);
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
		fileName = fileName.substring(fileName.lastIndexOf("/") + 1);

		en.SetValByKey("MyFilePath", path);

		String ext = "";
		if (fileName.indexOf(".") != -1)
		{
			ext = fileName.substring(fileName.lastIndexOf(".") + 1);
		}

		String reldir = path;
		if (reldir.length() > SystemConfig.getPathOfDataUser().length())
		{
			reldir = reldir.substring(reldir.toLowerCase().indexOf("\\datauser\\") + "(\\datauser\\)".length()).replace("\\", "/");
		}
		else
		{
			reldir = "";
		}

		if (reldir.length() > 0 && String.valueOf(reldir.charAt(0)).equals( "/" )==true)
		{
			reldir = reldir.substring(1);
		}

		if (reldir.length() > 0 && String.valueOf(reldir.charAt(reldir.length() - 1)).equals( "/" )== false)

		{
			reldir += "/";
		}

		en.SetValByKey("MyFileExt", ext);
		en.SetValByKey("MyFileName", fileName);
		en.SetValByKey("WebPath", "/DataUser/" + reldir + en.getPKVal() + "." + ext);

		String fullFile = path + "\\" + en.getPKVal() + "." + ext;

		File info = new File(fullFile);
		en.SetValByKey("MyFileSize", DataType.PraseToMB(info.length()));
//		if (DataType.IsImgExt(ext))
//		{
//			System.Drawing.Image img = System.Drawing.Image.FromFile(fullFile);
//			en.SetValByKey("MyFileH", img.Height);
//			en.SetValByKey("MyFileW", img.Width);
//			img.Dispose();
//		}
		en.Update();
	}



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
	 
	 param str 加密的字符串
	 @return 返回解密后的字符串
	*/
	public static String String_JieMi (String str)
	{
		//南京宝旺达.
		if (SystemConfig.getCustomerNo().equals("BWDA"))
		{
			return str;
		}

		return str;
	}

		///#endregion 加密解密文件.
	public static String String_JieMi_FTP(String str) throws Exception
	{

		//南京宝旺达.
		if (SystemConfig.getCustomerNo().equals("BWDA"))
			return Cryptos.aesDecrypt(str);

		return str;
	}
}