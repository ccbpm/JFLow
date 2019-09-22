package BP.Sys;

import Oracle.ManagedDataAccess.Client.*;
import MySql.*;
import MySql.Data.*;
import MySql.Data.Common.*;
import MySql.Data.MySqlClient.*;
import BP.DA.*;
import BP.Web.*;
import java.util.*;
import java.io.*;
import java.time.*;

/** 
 系统配值
*/
public class SystemConfig
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region ftp 配置.
	/** 
	 ftp服务器类型.
	*/
	public static String getFTPServerType()
	{
		String str = SystemConfig.getAppSettings().get("FTPServerType");
		return BP.Sys.Glo.String_JieMi(str);
	}
	/** 
	 服务器IP
	*/
	public static String getFTPServerIP()
	{
		String str = SystemConfig.getAppSettings().get("FTPServerIP");
		return BP.Sys.Glo.String_JieMi(str);
	}
	/** 
	 用户编号
	*/
	public static String getFTPUserNo()
	{
		String str = SystemConfig.getAppSettings().get("FTPUserNo");
		return BP.Sys.Glo.String_JieMi(str);
	}
	/** 
	 密码
	*/
	public static String getFTPUserPassword()
	{
		String str = SystemConfig.getAppSettings().get("FTPUserPassword");
		return BP.Sys.Glo.String_JieMi(str);
	}
	/** 
	 端口号
	*/
	public static String getFTPPort()
	{
		String str = SystemConfig.getAppSettings().get("FTPPort");
		return BP.Sys.Glo.String_JieMi(str);
	}
	/** 
	 附件上传加密
	*/
	public static boolean getIsEnableAthEncrypt()
	{
		return SystemConfig.GetValByKeyBoolen("IsEnableAthEncrypt", false);
	}
	/** 
	 附件上传位置
	*/
	public static boolean getIsUploadFileToFTP()
	{
		return SystemConfig.GetValByKeyBoolen("IsUploadFileToFTP", false);
	}

	public static String getAttachWebSite()
	{
		return SystemConfig.getAppSettings().get("AttachWebSite");
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 组织结构的配置.

	/** 
	 OS结构
	*/
	public static OSModel getOSModel()
	{
		return Sys.OSModel.OneMore;
			//return (OSModel)SystemConfig.GetValByKeyInt("OSModel", 0);
	}
	public static OSDBSrc getOSDBSrc()
	{
		return OSDBSrc.forValue(SystemConfig.GetValByKeyInt("OSDBSrc", 0));
	}
	/** 
	 结束流程 窗口配置
	*/
	public static IsOpenEndFlow getIsOpenEndFlow()
	{
		return IsOpenEndFlow.forValue(SystemConfig.GetValByKeyInt("IsOpenEndFlow", 0));
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

	/** 
	 运行的平台为转换java平台使用.
	*/
	public static Plant Plant = Sys.Plant.CSharp;
	/** 
	 读取配置文件
	 
	 @param cfgFile
	*/
	public static void ReadConfigFile(String cfgFile)
	{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 清除缓存
		BP.En.ClassFactory._BPAssemblies = null;
		if (BP.En.ClassFactory.Htable_Ens != null)
		{
			BP.En.ClassFactory.Htable_Ens.clear();
		}

		if (BP.En.ClassFactory.Htable_XmlEn != null)
		{
			BP.En.ClassFactory.Htable_XmlEn.clear();
		}

		if (BP.En.ClassFactory.Htable_XmlEns != null)
		{
			BP.En.ClassFactory.Htable_XmlEns.clear();
		}

		if (BP.Sys.SystemConfig.getCS_AppSettings() != null)
		{
			BP.Sys.SystemConfig.getCS_AppSettings().Clear();
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 清除缓存

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 加载 Web.Config 文件配置
		if ((new File(cfgFile)).isFile() == false)
		{
			throw new RuntimeException("文件不存在 [" + cfgFile + "]");
		}
		String _RefConfigPath = cfgFile;
		InputStreamReader read = new InputStreamReader(cfgFile);
		String firstline = read.ReadLine();
		String cfg = read.ReadToEnd();
		read.close();

		int start = cfg.toLowerCase().indexOf("<appsettings>");
		int end = cfg.toLowerCase().indexOf("</appsettings>");

		cfg = cfg.substring(start, end + "</appsettings".length() + 1);

		String tempFile = "Web.config.xml";

		OutputStreamWriter write = new OutputStreamWriter(tempFile);
		write.write(firstline + System.lineSeparator());
		write.write(cfg);
		write.flush();
		write.close();

		DataSet dscfg = new DataSet("cfg");
		dscfg.ReadXml(tempFile);

		//    BP.Sys.SystemConfig.CS_AppSettings = new System.Collections.Specialized.NameValueCollection();
		BP.Sys.SystemConfig.CS_DBConnctionDic.clear();
		for (DataRow row : dscfg.Tables["add"].Rows)
		{
			BP.Sys.SystemConfig.getCS_AppSettings().Add(row.get("key").toString().trim(), row.get("value").toString().trim());
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 关于开发商的信息
	public static String getVer()
	{
		try
		{
			return getAppSettings().get("Ver");
		}
		catch (java.lang.Exception e)
		{
			return "1.0.0";
		}
	}
	public static String getTouchWay()
	{
		try
		{
			return getAppSettings().get("TouchWay");
		}
		catch (java.lang.Exception e)
		{
			return SystemConfig.getCustomerTel() + " 地址:" + SystemConfig.getCustomerAddr();
		}
	}
	public static String getCopyRight()
	{
		try
		{
			return getAppSettings().get("CopyRight");
		}
		catch (java.lang.Exception e)
		{
			return "版权所有@" + getCustomerName();
		}
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 用户配置信息
	/** 
	 系统语言（）
	 对多语言的系统有效。
	*/
	public static String getSysLanguage()
	{
		String s = getAppSettings().get("SysLanguage");
		if (s == null)
		{
			s = "CH";
		}
		return s;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 逻辑处理
	/** 
	 封装了AppSettings
	*/
	private static NameValueCollection _CS_AppSettings;
	public static NameValueCollection getCS_AppSettings()
	{
		if (_CS_AppSettings == null)
		{
			_CS_AppSettings = new NameValueCollection();
		}
		return _CS_AppSettings;
	}
	public static void setCS_AppSettings(NameValueCollection value)
	{
		_CS_AppSettings = value;
	}

	public static void InitOptons(NameValueCollection appSetting, NameValueCollection connections)
	{

	}
	/** 
	 封装了AppSettings
	*/
	public static NameValueCollection getAppSettings()
	{
		if (SystemConfig.getIsBSsystem())
		{
			return System.Configuration.ConfigurationManager.AppSettings;
		}
		else
		{
			return getCS_AppSettings();
		}
	}
	static
	{
		CS_DBConnctionDic = new Hashtable();
	}
	/** 
	 应用程序路径
	*/
	public static String getPhysicalApplicationPath()
	{
		if (SystemConfig.getIsBSsystem() && HttpContextHelper.getCurrent() != null)
		{
			return HttpContextHelper.getPhysicalApplicationPath();
		}
		else
		{
			return AppDomain.CurrentDomain.SetupInformation.ApplicationBase;
		}
	}
	/** 
	 文件放置的路径
	*/
	public static String getPathOfUsersFiles()
	{
		return "/Data/Files/";
	}
	/** 
	 临时文件路径
	*/
	public static String getPathOfTemp()
	{
		return getPathOfDataUser() + "Temp\\";
	}
	public static String getPathOfWorkDir()
	{
		if (BP.Sys.SystemConfig.getIsBSsystem())
		{
			String path1 = HttpContextHelper.getPhysicalApplicationPath() + "\\..\\";
			File info1 = new File(path1);
			return info1.getPath();
		}
		else
		{
			String path = AppDomain.CurrentDomain.BaseDirectory + "\\..\\..\\..\\";
			File info = new File(path);
			return info.getPath();
		}
	}
	public static String getPathOfFDB()
	{
		String s = SystemConfig.getAppSettings().get("FDB");
		if (s.equals("") || s == null)
		{
			return getPathOfWebApp() + "\\DataUser\\FDB\\";
		}
		return s;
	}
	/** 
	 数据文件
	*/
	public static String getPathOfData()
	{
		return getPathOfWebApp() + "\\WF\\Data\\";
	}
	public static String getPathOfDataUser()
	{
		String tmp = SystemConfig.getAppSettings().get("DataUserDirPath");
		if (DataType.IsNullOrEmpty(tmp))
		{
			tmp = getPathOfWebApp() + "DataUser\\";
		}
		else
		{
			if (tmp.contains("\\"))
			{
				tmp.replace("\\", "");
			}

			tmp = getPathOfWebApp() + tmp + "\\DataUser\\";
		}
		return tmp;
	}
	/** 
	 XmlFilePath
	*/
	public static String getPathOfXML()
	{
		return getPathOfWebApp() + "\\WF\\Data\\XML\\";
	}
	public static String getPathOfAppUpdate()
	{
		return getPathOfWebApp() + "\\WF\\Data\\AppUpdate\\";
	}
	public static String getPathOfCyclostyleFile()
	{
		return getPathOfWebApp() + "\\DataUser\\CyclostyleFile\\";
	}
	/** 
	 应用程序名称
	*/
	public static String getAppName()
	{
		return HttpContextHelper.getRequestApplicationPath().replace("/", "");
	}
	/** 
	 ccflow物理目录
	*/
	public static String getCCFlowAppPath()
	{
		if (DataType.IsNullOrEmpty(SystemConfig.getAppSettings().get("DataUserDirPath")) == false)
		{
			return getPathOfWebApp() + SystemConfig.getAppSettings().get("DataUserDirPath");
		}
		return getPathOfWebApp();
	}
	/** 
	 ccflow网站目录
	*/
	public static String getCCFlowWebPath()
	{
		if (!DataType.IsNullOrEmpty(SystemConfig.getAppSettings().get("CCFlowAppPath")))
		{
			return SystemConfig.getAppSettings().get("CCFlowAppPath");
		}
		return "/";
	}
	/** 
	 网站地址用于生成url, 支持cs程序调用ws程序.
	*/
	public static String getHostURL()
	{
		if (DataType.IsNullOrEmpty(SystemConfig.getAppSettings().get("HostURL")) == false)
		{
			return SystemConfig.getAppSettings().get("HostURL");
		}
		return getHostURLOfBS(); // "http:/127.0.0.1/";
	}

	/** 
	 移动端用于生成url, 支持cs程序调用ws程序.
	*/
	public static String getMobileURL()
	{
		if (DataType.IsNullOrEmpty(SystemConfig.getAppSettings().get("MobileURL")) == false)
		{
			return SystemConfig.getAppSettings().get("MobileURL");
		}
		return SystemConfig.getAppSettings().get("MobileURL"); // "http:/127.0.0.1/";
	}

	/** 
	 HostURL 在bs的模式下调用.
	*/
	public static String getHostURLOfBS()
	{
		String url = "http://" + HttpContextHelper.getRequestUrlAuthority();
		return url;
	}
	/** 
	 WebApp Path.
	*/
	public static String getPathOfWebApp()
	{
		if (SystemConfig.getIsBSsystem())
		{
			return HttpContextHelper.getPhysicalApplicationPath();
		}
		else
		{
			if (SystemConfig.getSysNo().equals("FTA"))
			{
				return AppDomain.CurrentDomain.BaseDirectory;
			}
			else
			{
				return AppDomain.CurrentDomain.BaseDirectory + "..\\..\\";
			}
		}
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 共同变量。
	public static boolean IsBSsystem_Test = true;
	/** 
	 是不是BS系统结构。
	*/
	private static boolean _IsBSsystem = true;
	/** 
	 是不是BS系统结构。
	*/
	public static boolean getIsBSsystem()
	{
			// return true;
		return SystemConfig._IsBSsystem;
	}
	public static void setIsBSsystem(boolean value)
	{
		SystemConfig._IsBSsystem = value;
	}
	public static boolean getIsCSsystem()
	{
		return !SystemConfig._IsBSsystem;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 系统配置信息
	/** 
	 执行清空
	*/
	public static void DoClearCash()
	{
		// HttpRuntime.UnloadAppDomain();
		BP.DA.Cash.getMap_Cash().clear();
		BP.DA.Cash.getSQL_Cash().clear();
		BP.DA.Cash.getEnsData_Cash().clear();
		BP.DA.Cash.getEnsData_Cash_Ext().clear();
		BP.DA.Cash.getBS_Cash().clear();
		BP.DA.Cash.getBill_Cash().clear();
		BP.DA.CashEntity.getDCash().clear();
	}
	/** 
	 系统编号
	*/
	public static String getSysNo()
	{
		return getAppSettings().get("SysNo");
	}

	/** 
	 系统名称
	*/
	public static String getSysName()
	{
		String s = getAppSettings().get("SysName");
		if (s == null)
		{
			s = "请在web.config中配置SysName名称。";
		}
		return s;
	}
	public static String getOrderWay()
	{
		return getAppSettings().get("OrderWay");
	}
	public static int getPageSize()
	{
		try
		{
			return Integer.parseInt(getAppSettings().get("PageSize"));
		}
		catch (java.lang.Exception e)
		{
			return 99999;
		}
	}
	public static int getMaxDDLNum()
	{
		try
		{
			return Integer.parseInt(getAppSettings().get("MaxDDLNum"));
		}
		catch (java.lang.Exception e)
		{
			return 50;
		}
	}
	public static int getPageSpan()
	{
		try
		{
			return Integer.parseInt(getAppSettings().get("PageSpan"));
		}
		catch (java.lang.Exception e)
		{
			return 20;
		}
	}
	/** 
	  到的路径.PageOfAfterAuthorizeLogin
	*/
	public static String getPageOfAfterAuthorizeLogin()
	{
		return HttpContextHelper.getRequestApplicationPath() + getAppSettings().get("PageOfAfterAuthorizeLogin");
	}
	/** 
	 丢失session 到的路径.
	*/
	public static String getPageOfLostSession()
	{
		return HttpContextHelper.getRequestApplicationPath() + getAppSettings().get("PageOfLostSession");
	}
	/** 
	 日志路径
	*/
	public static String getPathOfLog()
	{
		return getPathOfWebApp() + "\\DataUser\\Log\\";
	}

	/** 
	 系统名称
	*/
	public static int getTopNum()
	{
		try
		{
			return Integer.parseInt(getAppSettings().get("TopNum"));
		}
		catch (java.lang.Exception e)
		{
			return 99999;
		}
	}
	/** 
	 服务电话
	*/
	public static String getServiceTel()
	{
		return getAppSettings().get("ServiceTel");
	}
	/** 
	 服务E-mail
	*/
	public static String getServiceMail()
	{
		return getAppSettings().get("ServiceMail");
	}
	/** 
	 第3方软件
	*/
	public static String getThirdPartySoftWareKey()
	{
		return getAppSettings().get("ThirdPartySoftWareKey");
	}
	/** 
	 是否启用CCIM?
	*/
	public static boolean getIsEnableCCIM()
	{
		if (getAppSettings().get("IsEnableCCIM").equals("1"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public static boolean getIsEnableNull()
	{
		if (getAppSettings().get("IsEnableNull").equals("1"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	/** 
	 是否 debug 状态
	*/
	public static boolean getIsDebug()
	{
		if (getAppSettings().get("IsDebug").equals("1"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	public static boolean getIsOpenSQLCheck()
	{
		if (getAppSettings().get("IsOpenSQLCheck").equals("0"))
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	/** 
	 是不是多系统工作。
	*/
	public static boolean getIsMultiSys()
	{
		if (getAppSettings().get("IsMultiSys").equals("1"))
		{
			return true;
		}
		return false;
	}

	/** 
	 是否启用密码加密
	*/
	public static boolean getIsEnablePasswordEncryption()
	{
		String tempVar = getAppSettings().get("IsEnablePasswordEncryption");
		String s = tempVar instanceof String ? (String)tempVar : null;
		if (s == null || s.equals("0"))
		{
			return false;
		}
		return true;
	}
	/** 
	 是否多语言？
	*/
	public static boolean getIsMultilingual()
	{
		if (getAppSettings().get("IsMultilingual").equals("1"))
		{
			return true;
		}
		return false;
	}
	/** 
	 使用的语言
	*/
	public static String getLangue()
	{
		String str = getAppSettings().get("Langue");
		if (DataType.IsNullOrEmpty(str))
		{
			return "CN";
		}
		return str;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion


//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 处理临时缓存
	/** 
	 回话丢失时间长度(默认为500分钟)
	*/
	public static int getSessionLostMinute()
	{
		return SystemConfig.GetValByKeyInt("SessionLostMinute", 500000);
	}
	/** 
	 放在 Temp 中的cash 多少时间失效。
	 0, 表示永久不失效。
	*/
	private static int getCashFail()
	{
		try
		{
			return Integer.parseInt(getAppSettings().get("CashFail"));
		}
		catch (java.lang.Exception e)
		{
			return 0;
		}
	}
	/** 
	 当前的 TempCash 是否失效了
	*/
	public static boolean getIsTempCashFail()
	{
		if (SystemConfig.getCashFail() == 0)
		{
			return false;
		}

		if (_CashFailDateTime == null)
		{
			_CashFailDateTime = LocalDateTime.now();
			return true;
		}
		else
		{
			TimeSpan ts = LocalDateTime.now() - _CashFailDateTime;
			if (ts.Minutes >= SystemConfig.getCashFail())
			{
				_CashFailDateTime = LocalDateTime.now();
				return true;
			}
			return false;
		}
	}
	public static LocalDateTime _CashFailDateTime = LocalDateTime.MIN;
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 客户配置信息
	/** 
	 客户编号
	*/
	public static String getCustomerNo()
	{
		return getAppSettings().get("CustomerNo");
	}
	/** 
	 客户名称
	*/
	public static String getCustomerName()
	{
		return getAppSettings().get("CustomerName");
	}
	/** 
	 客户名称
	*/
	public static String getRunOnPlant()
	{
		return (getAppSettings().get("RunOnPlant")) != null ? getAppSettings().get("RunOnPlant") : "";
	}
	public static String getCustomerURL()
	{
		return getAppSettings().get("CustomerURL");
	}
	/** 
	 客户简称
	*/
	public static String getCustomerShortName()
	{
		return getAppSettings().get("CustomerShortName");
	}
	/** 
	 客户地址
	*/
	public static String getCustomerAddr()
	{
		return getAppSettings().get("CustomerAddr");
	}
	/** 
	 客户电话
	*/
	public static String getCustomerTel()
	{
		return getAppSettings().get("CustomerTel");
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 微信相关配置信息
	/** 
	 企业标识
	*/
	public static String getWX_CorpID()
	{
		return getAppSettings().get("CorpID");
	}
	/** 
	 帐号钥匙
	*/
	public static String getWX_AppSecret()
	{
		return getAppSettings().get("AppSecret");
	}
	/** 
	 应用令牌
	*/
	public static String getWX_WeiXinToken()
	{
		return getAppSettings().get("WeiXinToken");
	}
	/** 
	 应用加密所用的秘钥
	*/
	public static String getWX_EncodingAESKey()
	{
		return getAppSettings().get("EncodingAESKey");
	}
	/** 
	 进入应用后的欢迎提示
	*/
	public static boolean getWeiXin_AgentWelCom()
	{
		return GetValByKeyBoolen("WeiXin_AgentWelCom", false);
	}
	/** 
	 应用ID
	*/
	public static String getWX_AgentID()
	{
		return getAppSettings().get("AgentID");
	}
	/** 
	 消息链接网址
	*/
	public static String getWX_MessageUrl()
	{
		return getAppSettings().get("WeiXin_MessageUrl");
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 钉钉配置相关
	/** 
	 企业标识
	*/
	public static String getDing_CorpID()
	{
		return getAppSettings().get("Ding_CorpID");
	}
	/** 
	 密钥
	*/
	public static String getDing_CorpSecret()
	{
		return getAppSettings().get("Ding_CorpSecret");
	}
	/** 
	 登录验证密钥
	*/
	public static String getDing_SSOsecret()
	{
		return getAppSettings().get("Ding_SSOsecret");
	}
	/** 
	 消息超链接服务器地址
	*/
	public static String getDing_MessageUrl()
	{
		return getAppSettings().get("Ding_MessageUrl");
	}
	/** 
	 企业应用编号
	*/
	public static String getDing_AgentID()
	{
		return getAppSettings().get("Ding_AgentID");
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

	/** 
	取得配置 NestedNamesSection 内的相应 key 的内容
	 
	 @param key
	 @return 
	*/
	public static NameValueCollection GetConfig(String key)
	{
		Hashtable ht = (Hashtable)System.Configuration.ConfigurationManager.GetSection("NestedNamesSection");
		return (NameValueCollection)ht.get(key);
	}
	public static String GetValByKey(String key, String isNullas)
	{
		String s = getAppSettings().get(key);
		if (s == null)
		{
			s = isNullas;
		}
		return s;
	}
	public static boolean GetValByKeyBoolen(String key, boolean isNullas)
	{
		String s = getAppSettings().get(key);
		if (s == null)
		{
			return isNullas;
		}

		if (s.equals("1"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	public static int GetValByKeyInt(String key, int isNullas)
	{
		String s = getAppSettings().get(key);
		if (s == null)
		{
			return isNullas;
		}
		return Integer.parseInt(s);
	}
	/** 
	 工作小时数
	 
	 @param key 键
	 @param isNullas 如果是空返回的值
	 @return 
	*/
	public static float GetValByKeyFloat(String key, int isNullas)
	{
		String s = getAppSettings().get(key);
		if (s == null)
		{
			return isNullas;
		}
		return Float.parseFloat(s);
	}
	public static String GetConfigXmlKeyVal(String key)
	{
		try
		{
			DataSet ds = new DataSet("dss");
			ds.ReadXml(BP.Sys.SystemConfig.getPathOfXML() + "\\KeyVal.xml");
			DataTable dt = ds.Tables[0];
			for (DataRow dr : dt.Rows)
			{
				if (dr.get("Key").toString().equals(key))
				{
					return dr.get("Val").toString();
				}
			}
			throw new RuntimeException("在您利用GetXmlConfig 取值出现错误，没有找到key= " + key + ", 请检查 /data/Xml/KeyVal.xml. ");
		}
		catch (RuntimeException ex)
		{
			throw ex;
		}
	}
	public static String GetConfigXmlNode(String fk_Breed, String enName, String key)
	{
		try
		{
			String file = BP.Sys.SystemConfig.getPathOfXML() + "\\Node\\" + fk_Breed + ".xml";
			DataSet ds = new DataSet("dss");
			try
			{
				ds.ReadXml(file);
			}
			catch (java.lang.Exception e)
			{
				return null;
			}
			DataTable dt = ds.Tables[0];
			if (dt.Columns.Contains(key) == false)
			{
				throw new RuntimeException(file + "配置错误，您没有按照格式配置，它不包含标记 " + key);
			}
			for (DataRow dr : dt.Rows)
			{
				if (dr.get("NodeEnName").toString().equals(enName))
				{
					if (dr.get(key).equals(DBNull.Value))
					{
						return null;
					}
					else
					{
						return dr.get(key).toString();
					}

				}
			}
			return null;
		}
		catch (RuntimeException ex)
		{
			throw ex;
		}
	}
	/** 
	 获取xml中的配置信息
	 GroupTitle, ShowTextLen, DefaultSelectedAttrs, TimeSpan
	 
	 @param key
	 @param ensName
	 @return 
	*/
	public static String GetConfigXmlEns(String key, String ensName)
	{
		try
		{
			Object tempVar = BP.DA.Cash.GetObj("TConfigEns", BP.DA.Depositary.Application);
			DataTable dt = tempVar instanceof DataTable ? (DataTable)tempVar : null;
			if (dt == null)
			{
				DataSet ds = new DataSet("dss");
				ds.ReadXml(BP.Sys.SystemConfig.getPathOfXML() + "\\Ens\\ConfigEns.xml");
				dt = ds.Tables[0];
				BP.DA.Cash.AddObj("TConfigEns", BP.DA.Depositary.Application, dt);
			}

			for (DataRow dr : dt.Rows)
			{
				if (dr.get("Key").toString().equals(key) && dr.get("For").toString().equals(ensName))
				{
					return dr.get("Val").toString();
				}
			}
			return null;
		}
		catch (RuntimeException ex)
		{
			throw ex;
		}
	}
	public static String GetConfigXmlSQL(String key)
	{
		try
		{
			DataSet ds = new DataSet("dss");
			ds.ReadXml(BP.Sys.SystemConfig.getPathOfXML() + "\\SQL\\" + BP.Sys.SystemConfig.getThirdPartySoftWareKey() + ".xml");
			DataTable dt = ds.Tables[0];
			for (DataRow dr : dt.Rows)
			{
				if (dr.get("No").toString().equals(key))
				{
					return dr.get("SQL").toString();
				}
			}
			throw new RuntimeException("在您利用 GetXmlConfig 取值出现错误，没有找到key= " + key + ", 请检查 /Data/XML/" + SystemConfig.getThirdPartySoftWareKey() + ".xml. ");
		}
		catch (RuntimeException ex)
		{
			throw ex;
		}
	}
	public static String GetConfigXmlSQLApp(String key)
	{
		try
		{
			DataSet ds = new DataSet("dss");
			ds.ReadXml(BP.Sys.SystemConfig.getPathOfXML() + "\\SQL\\App.xml");
			DataTable dt = ds.Tables[0];
			for (DataRow dr : dt.Rows)
			{
				if (dr.get("No").toString().equals(key))
				{
					return dr.get("SQL").toString();
				}
			}
			throw new RuntimeException("在您利用 GetXmlConfig 取值出现错误，没有找到key= " + key + ", 请检查 /Data/XML/App.xml. ");
		}
		catch (RuntimeException ex)
		{
			throw ex;
		}
	}

	public static String GetConfigXmlSQL(String key, String replaceKey, String replaceVal)
	{
		return GetConfigXmlSQL(key).replace(replaceKey, replaceVal);
	}
	public static String GetConfigXmlSQL(String key, String replaceKey1, String replaceVal1, String replaceKey2, String replaceVal2)
	{
		return GetConfigXmlSQL(key).replace(replaceKey1, replaceVal1).replace(replaceKey2, replaceVal2);
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region dsn
	/** 
	 数据库连接.
	*/
	public static String _AppCenterDSN = null;
	/** 
	 数据库连接
	*/
	public static String getAppCenterDSN()
	{
		if (_AppCenterDSN != null)
		{
			return _AppCenterDSN;
		}

		String str = getAppSettings().get("AppCenterDSN");
		if (DataType.IsNullOrEmpty(str) == false)
		{
			return str;
		}

		str = getAppSettings().get("AppCenterDSN.encryption");

		if (DataType.IsNullOrEmpty(str) == true)
		{
			throw new RuntimeException("err@没有配置数据库连接字符串.");
		}

		DecryptAndEncryptionHelper.decode decode = new DecryptAndEncryptionHelper.decode();
		_AppCenterDSN = decode.decode_exe(str);
		return _AppCenterDSN;
	}
	public static void setAppCenterDSN(String value)
	{
		_AppCenterDSN = value;
	}
	public static String getDBAccessOfOracle()
	{
		return getAppSettings().get("DBAccessOfOracle");
	}
	public static String getDBAccessOfOracle1()
	{
		return getAppSettings().get("DBAccessOfOracle1");
	}
	public static String getDBAccessOfMSSQL()
	{
		return getAppSettings().get("DBAccessOfMSSQL");
	}
	public static String getDBAccessOfOLE()
	{
		String dsn = getAppSettings().get("DBAccessOfOLE");
		if (dsn.indexOf("@Pass") != -1)
		{
			dsn = dsn.replace("@Pass", "helloccs");
		}

		if (dsn.indexOf("@Path") != -1)
		{
			dsn = dsn.replace("@Path", SystemConfig.getPathOfWebApp());
		}
		return dsn;

	}
	public static String getDBAccessOfODBC()
	{
		return getAppSettings().get("DBAccessOfODBC");
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion
	/** 
	 获取主应用程序的数据库部署方式．
	*/
	public static BP.DA.DBModel getAppCenterDBModel()
	{
		switch (getAppSettings().get("AppCenterDBModel"))
		{
			case "Domain":
				return BP.DA.DBModel.Domain;
			default:
				return BP.DA.DBModel.Single;
		}
	}
	/** 
	 获取主应用程序的数据库类型．
	*/
	public static BP.DA.DBType getAppCenterDBType()
	{
		switch (getAppSettings().get("AppCenterDBType"))
		{
			case "MSMSSQL":
			case "MSSQL":
				return BP.DA.DBType.MSSQL;
			case "Oracle":
				return BP.DA.DBType.Oracle;
			case "MySQL":
				return BP.DA.DBType.MySQL;
			case "PostgreSQL":
				return BP.DA.DBType.PostgreSQL;
			case "Access":
				return BP.DA.DBType.Access;
			case "Informix":
				return BP.DA.DBType.Informix;
			default:
				return BP.DA.DBType.Oracle;
		}
	}
	private static String _AppCenterDBDatabase = null;
	/** 
	 数据库名称
	*/
	public static String getAppCenterDBDatabase()
	{
		if (_AppCenterDBDatabase == null)
		{
			switch (BP.DA.DBAccess.getAppCenterDBType())
			{
				case MSSQL:
					SqlConnection connMSSQL = new SqlConnection(SystemConfig.getAppCenterDSN());
					if (connMSSQL.State != ConnectionState.Open)
					{
						connMSSQL.Open();
					}
					_AppCenterDBDatabase = connMSSQL.Database;
					break;
				case Oracle:
					OracleConnection connOra = new OracleConnection(SystemConfig.getAppCenterDSN());
					if (connOra.State != ConnectionState.Open)
					{
						connOra.Open();
					}
					_AppCenterDBDatabase = connOra.Database;
					break;
				case MySQL:
					MySqlConnection connMySQL = new MySqlConnection(SystemConfig.getAppCenterDSN());
					if (connMySQL.State != ConnectionState.Open)
					{
						connMySQL.Open();
					}
					_AppCenterDBDatabase = connMySQL.Database;
					break;
						//From Zhou IBM 删除
					//case DA.DBType.Informix:
					//    IfxConnection connIFX = new IfxConnection(SystemConfig.AppCenterDSN);
					//    if (connIFX.State != ConnectionState.Open)
					//        connIFX.Open();
					//    _AppCenterDBDatabase = connIFX.Database;
					//    break;
				default:
					throw new RuntimeException("@没有判断的数据类型.");
					break;
			}
		}

			// 返回database.
		return _AppCenterDBDatabase;
	}
	/** 
	 获取不同类型的数据库变量标记
	*/
	public static String getAppCenterDBVarStr()
	{
		switch (SystemConfig.getAppCenterDBType())
		{
			case Oracle:
			case PostgreSQL:
				return ":";
			case MySQL:
			case Informix:
				return "?";
			default:
				return "@";
		}
	}

	public static String getAppCenterDBLengthStr()
	{
		switch (SystemConfig.getAppCenterDBType())
		{
			case Oracle:
				return "Length";
			case MSSQL:
				return "LEN";
			case Informix:
				return "Length";
			case Access:
				return "Length";
			default:
				return "Length";
		}
	}
	/** 
	 获取不同类型的substring函数的书写
	*/
	public static String getAppCenterDBSubstringStr()
	{
		switch (SystemConfig.getAppCenterDBType())
		{
			case Oracle:
				return "substr";
			case MSSQL:
				return "substring";
			case Informix:
				return "MySubString";
			case Access:
				return "Mid";
			default:
				return "substring";
		}
	}
	public static String getAppCenterDBAddStringStr()
	{
		switch (SystemConfig.getAppCenterDBType())
		{
			case Oracle:
			case MySQL:
			case Informix:
				return "||";
			default:
				return "+";
		}
	}
	public static Hashtable CS_DBConnctionDic;
}