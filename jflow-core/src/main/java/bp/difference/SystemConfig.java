package bp.difference;

import java.io.*;
import java.util.Date;
import java.util.Hashtable;
import java.util.Properties;
import bp.da.*;
import bp.difference.handler.CommonUtils;
import bp.sys.CCBPMRunModel;
import bp.sys.OSModel;
import org.slf4j.LoggerFactory;
import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * 系统配置
 * 
 * @author thinkpad
 * 
 */
public class SystemConfig {
	public static String AppCenterDBFieldCaseModel;
	private static org.slf4j.Logger logger = LoggerFactory.getLogger(SystemConfig.class);

	private static boolean _IsBSsystem = true;

	public static String getFTPServerType() {

		return SystemConfig.getAppSettings().get("FTPServerType").toString();
	}

	public static String getFTPServerIP() {
		return SystemConfig.getAppSettings().get("FTPServerIP").toString();
	}

	public static int getFTPServerPort(){
		return SystemConfig.GetValByKeyInt("FTPServerPort",0);
	}

	public static String getFTPUserNo()  {

		String str = SystemConfig.getAppSettings().get("FTPUserNo").toString();
		return str;

	}
	public static String getAdmins(){
		Object obj = getAppSettings().get("admins");
		if(obj == null)
			return "";
		return getAppSettings().get("admins").toString();
	}
	public static String getFTPUserPassword() {
		String str = SystemConfig.getAppSettings().get("FTPUserPassword").toString();
		return str;
	}

	public static bp.sys.Plant Plant = bp.sys.Plant.Java;
	/// <summary>
	/// 附件上传加密
	/// </summary>
	public static boolean getIsEnableAthEncrypt() {
		Object isEnableAthEncryptObj = SystemConfig.getAppSettings().get("IsEnableAthEncrypt");
		if (isEnableAthEncryptObj == null) {
			return false;
		}

		String IsEnableAthEncrypt = isEnableAthEncryptObj.toString();

		if (DataType.IsNullOrEmpty(IsEnableAthEncrypt) == true)
			return false;

		if (SystemConfig.getAppSettings().get("IsEnableAthEncrypt").toString().equals("1"))
			return true;
		return false;

	}

	/// <summary>
	/// 附件上传位置
	/// </summary>
	public static boolean getIsUploadFileToFTP() {
		String IsUploadFileToFTP = SystemConfig.getAppSettings().get("IsUploadFileToFTP").toString();

		if (DataType.IsNullOrEmpty(IsUploadFileToFTP) == true)
			return false;

		if (SystemConfig.getAppSettings().get("IsUploadFileToFTP").toString().equals("1"))
			return true;
		return false;
	}

	public static String getAttachWebSite() {
		return SystemConfig.getAppSettings().get("AttachWebSite").toString();
	}

	/**
	 * OS结构
	 */
	public static OSModel getOSModel()  {
		return OSModel.forValue(SystemConfig.GetValByKeyInt("OSModel", 0));
	}

	public static int getGroupStationModel()  {
		return  SystemConfig.GetValByKeyInt("GroupStationModel", 0);
	}



	/**
	 * 读取配置文件
	 * 
	 * param
	 * @throws Exception
	 */
	public static void ReadConfigFile(InputStream fis) throws Exception {

		if (SystemConfig.getCS_AppSettings() != null) {
			SystemConfig.getCS_AppSettings().clear();
		}

		Properties properties = new Properties();
		try {
			properties.load(fis);
			for (Object s : properties.keySet()) {
				getCS_AppSettings().put(s.toString(), String.valueOf(properties.get(s)));
			}
			fis.close();
		} catch (IOException e) {
			throw new Exception("读取配置文件失败", e);
		}
	}
	/**
	* 运行模式0=单机版，1=集团模式, 2=SAAS模式
	*/
	public static CCBPMRunModel getCCBPMRunModel()
	{
		int val = SystemConfig.GetValByKeyInt("CCBPMRunModel", 0);
		if (val == 0)
			return CCBPMRunModel.Single;

		if (val == 1)
			return CCBPMRunModel.GroupInc;

		if (val == 2)
			return CCBPMRunModel.SAAS;

		return CCBPMRunModel.Single;
	}
	/**
	 传入的参数，是否需要类型
	 */
	public static boolean getAppCenterDBFieldIsParaDBType()
	{
		switch (getAppCenterDBType())
		{
			case UX:
			case PostgreSQL:
				return true;
			default:
				return false;
		}
	}

	/**
	 * 获取xml中的配置信息 GroupTitle, ShowTextLen, DefaultSelectedAttrs, TimeSpan
	 * 
	 * param key
	 * param ensName
	 * @return
	 */
	public static String GetConfigXmlEns(String key, String ensName) {
		try {
			Object tempVar = bp.da.Cash.GetObj("TConfigEns", bp.da.Depositary.Application);
			DataTable dt = (DataTable) ((tempVar instanceof DataTable) ? tempVar : null);
			if (dt == null) {
				DataSet ds = new DataSet("dss");
				ds.readXml(SystemConfig.getPathOfXML() + "Ens/ConfigEns.xml");
				dt = ds.Tables.get(0);
				bp.da.Cash.AddObj("TConfigEns", bp.da.Depositary.Application, dt);
			}

			for (DataRow dr : dt.Rows) {
				if (dr.getValue("Key").equals(key) && dr.getValue("For").equals(ensName)) {
					return dr.getValue("Val") == null ? "" : dr.getValue("Val").toString();
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			;
		}
		return null;
	}

	/**
	 * 关于开发商的信息
	 * 
	 * @return
	 */
	public static String getVer() {
		try {
			return getAppSettings().get("Ver").toString();
		} catch (java.lang.Exception e) {
			return "1.0.0";
		}
	}

	public static String getTouchWay() {
		try {
			return getAppSettings().get("TouchWay").toString();
		} catch (java.lang.Exception e) {
			return SystemConfig.getCustomerTel() + " 地址:" + SystemConfig.getCustomerAddr();
		}
	}

	public static String getCopyRight() {
		try {
			return getAppSettings().get("CopyRight").toString();
		} catch (java.lang.Exception e) {
			return "版权所有@" + getCustomerName();
		}
	}

	public static String getCompanyID() {
		String s = getAppSettings().get("CompanyID").toString();
		if (DataType.IsNullOrEmpty(s)) {
			return "CCFlow";
		}
		return s;
	}

	/**
	 * 开发商全称
	 * 
	 * @return
	 */
	public static String getDeveloperName() {
		return getAppSettings().get("DeveloperName").toString();
	}

	/**
	 * 开发商简称
	 * 
	 * @return
	 */
	public static String getDeveloperShortName() {
		return getAppSettings().get("DeveloperShortName").toString();
	}

	/**
	 * 开发商电话
	 * 
	 * @return
	 */
	public static String getDeveloperTel() {
		return getAppSettings().get("DeveloperTel").toString();
	}

	/**
	 * 开发商的地址
	 * 
	 * @return
	 */
	public static String getDeveloperAddr() {
		return (String) getAppSettings().get("DeveloperAddr");

	}

	/**
	 * 系统语言 对多语言的系统有效。
	 * 
	 * @return
	 */
	public static String getSysLanguage() {
		return "CH";
	}

		/**
		 * 封装了AppSettings, 负责存放 配置的基本信息
		 */
		private static Hashtable<String, Object> _CS_AppSettings = null;

		public static Hashtable<String, Object> getCS_AppSettings() {

			if (_CS_AppSettings == null || _CS_AppSettings.size() == 0) {
				try {
					_CS_AppSettings = new java.util.Hashtable<String, Object>();

					Properties props = new Properties();
					InputStream is = null;
					try {
						is = bp.difference.Helper.loadResource();
						BufferedReader bf = new BufferedReader(new InputStreamReader(is, "UTF-8"));// 解决读取properties文件中产生中文乱码的问题
						props.load(bf);
					} finally {
						IOUtils.closeQuietly(is);
					}
					_CS_AppSettings = (Hashtable) props;
				} catch (Exception e) {
					//ogger.error("读取配置文件失败");
					throw new RuntimeException("读取配置文件失败", e);
				}
			}
			return _CS_AppSettings;
		}

	/**
	 * 封装了AppSettings
	 * 
	 * @return
	 */
	public static Hashtable<String, Object> getAppSettings() {
		return getCS_AppSettings();
	}

	static {
		CS_DBConnctionDic = new Hashtable<String, Object>();
	}

	/**
	 * 应用程序路径
	 * 
	 * @return
	 */
	public static String getPhysicalApplicationPath() {
		return "D:\\JJFlow\\trunk\\JJFlow\\";
	}

	/**
	 * 文件放置的路径
	 * 
	 * @return
	 */
	public static String getPathOfUsersFiles() {
		return "/Data/Files/";
	}

	/**
	 * 临时文件路径
	 * 
	 * @return
	 */
	public static String getPathOfTemp() {
		return getPathOfDataUser() + "Temp";
	}

	public static String getPathOfWorkDir() {
		return "D:/JFlow/trunk/";
	}

	public static String getPathOfFDB() {
		return getPathOfWebApp() + "/DataUser/FDB/";
	}

	/**
	 * 数据文件
	 * 
	 * @return
	 */
	public static String getPathOfData() {

		bp.da.Log.DebugWriteInfo(getPathOfWebApp() + SystemConfig.getAppSettings().get("DataDirPath").toString()
				+"/Data/");
		return getPathOfWebApp() + SystemConfig.getAppSettings().get("DataDirPath").toString()  + "/Data/";
	}

	public static String getPathOfDataUser(){
		return getPathOfWebApp() + SystemConfig.getAppSettings().get("DataUserDirPath").toString() + "DataUser/";
	}

	/**
	 * XmlFilePath
	 * 
	 * @return
	 */
	public static String getPathOfXML()throws Exception {
		return getPathOfWebApp() + SystemConfig.getAppSettings().get("DataDirPath").toString() + "/Data/XML/";
	}

	public static String getPathOfAppUpdate()throws Exception {
		return getPathOfWebApp() + SystemConfig.getAppSettings().get("DataDirPath").toString() + "/Data/AppUpdate/";
	}

	public static String getPathOfCyclostyleFile()throws Exception {
		return getPathOfWebApp() + "DataUser/CyclostyleFile/";
	}

	/**
	 * 应用程序名称
	 * 
	 * @return
	 */
	public static String getAppName() {
		return "JJFlow";
	}

	/**
	 * ccflow物理目录
	 * 
	 * @return
	 */
	public static String getCCFlowAppPath() {
		if (SystemConfig.getAppSettings().get("DataUserDirPath") != null) {
			return getPathOfWebApp() + SystemConfig.getAppSettings().get("DataUserDirPath").toString();
		}
		return getPathOfWebApp();
	}

	/*
	 * 集成的框架.
	 */
	public static String getRunOnPlant() {

		String str = (String) SystemConfig.getAppSettings().get("RunOnPlant");
		if (str == null) {
			return "bp";
		}

		return str;

	}

	/**
	 * ccflow网站目录
	 * 
	 * @return
	 */
	public static String getCCFlowWebPath() {
		return bp.wf.Glo.getCCFlowAppPath();
	}



	public static boolean getIsJarRun(){
        Object str =  SystemConfig.getAppSettings().get("IsStartJarPackage");
        if(str == null || str.toString().equals("0"))
            return false;
        return true;
    }
	/**
	 * WebApp Path
	 * 
	 * @return
	 */
	public static String getPathOfWebApp(){
		HttpServletRequest request = bp.sys.base.Glo.getRequest();
		if (SystemConfig.getIsBSsystem()) {
			if (request == null || request.getSession() == null) {
				return bp.wf.Glo.getHostURL() + "/";
			} else {
			    if(getIsJarRun() == true )
			        return "resources/";
				String path = bp.sys.base.Glo.getRequest().getSession().getServletContext().getRealPath("") + "/";
				return path;
			}
		} else {
			return "";
		}
	}

	public static boolean getIsBSsystem() {
		return SystemConfig.GetValByKeyBoolen("IsBSsystem", true);
	}

	public static void setIsBSsystem(boolean value) {
		SystemConfig._IsBSsystem = value;
	}

	public static boolean getIsCSsystem() {
		return !SystemConfig.getIsBSsystem();
	}

	// 统配置信息
	/**
	 * 系统编号
	 * 
	 * @return
	 */
	public static String getSysNo() {
		return getAppSettings().get("SysNo").toString();
	}

	/**
	 * 系统名称
	 * 
	 * @return
	 */
	 public static String getSysName() {
		String s = getAppSettings().get("SysName").toString();
//		 String s = null;
//		 try {
//			 s = new String(s1.getBytes("iso-8859-1"),"utf-8");
//		 } catch (UnsupportedEncodingException e) {
//			 e.printStackTrace();
//		 }

		 if (s == null) {
			s = "请在web.propertoes中配置SysName名称";
		}
		return s;
	}

	public static String getOrderWay() {
		return getAppSettings().get("OrderWay").toString();
	}

	public static int getPageSize() {
		try {
			return Integer.parseInt(getAppSettings().get("PageSize").toString());
		} catch (java.lang.Exception e) {
			return 99999;
		}
	}

	public static int getMaxDDLNum() {
		try {
			return Integer.parseInt(getAppSettings().get("MaxDDLNum").toString());
		} catch (java.lang.Exception e) {
			return 50;
		}
	}

	public static int getPageSpan() {
		try {
			return Integer.parseInt(getAppSettings().get("PageSpan").toString());
		} catch (java.lang.Exception e) {
			return 20;
		}
	}

	/**
	 * 到的路径.PageOfAfterAuthorizeLogin
	 * @return
	 */
	public static String getPageOfAfterAuthorizeLogin() {
		/*
		 * warning return BP.Glo.getHttpContextCurrent().Request.ApplicationPath
		 * + "" + getAppSettings().get("PageOfAfterAuthorizeLogin").toString();
		 */
		return bp.sys.base.Glo.class.getClass().getResource("/").getPath() + ""
				+ getAppSettings().get("PageOfAfterAuthorizeLogin").toString();
	}

	/**
	 * 丢失session 到的路径
	 * 
	 * @return
	 */
	public static String getPageOfLostSession() {
		/*
		 * warning return BP.Glo.getHttpContextCurrent().Request.ApplicationPath
		 * + "" + getAppSettings().get("PageOfLostSession").toString();
		 */
		return bp.sys.base.Glo.class.getClass().getResource("/").getPath() + ""
				+ getAppSettings().get("PageOfLostSession").toString();
	}

	/**
	 * 日志路径
	 * 
	 * @return
	 */
	public static String getPathOfLog() throws Exception{
		return getPathOfWebApp() + "/DataUser/Log/";
	}

	/**
	 * 系统名称
	 * 
	 * @return
	 */
	public static int getTopNum() {
		try {
			return Integer.parseInt(getAppSettings().get("TopNum").toString());
		} catch (java.lang.Exception e) {
			return 99999;
		}
	}

	/**
	 * 服务电话
	 * 
	 * @return
	 */
	public static String getServiceTel() {
		return getAppSettings().get("ServiceTel").toString();
	}

	/**
	 * 服务E-mail
	 * 
	 * @return
	 */
	public static String getServiceMail() {
		return getAppSettings().get("ServiceMail").toString();
	}

	/**
	 * 第3方软件
	 * 
	 * @return
	 */
	public static String getThirdPartySoftWareKey() {
		return getAppSettings().get("ThirdPartySoftWareKey").toString();
	}

	public static boolean getIsEnableNull() {
		if (getAppSettings().get("IsEnableNull").toString().equals("1")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 是否 debug 状态
	 * 
	 * @return
	 */
	public static boolean getIsDebug() {
		if (getAppSettings().get("IsDebug").toString().equals("1")) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean getIsOpenSQLCheck() {
		if (getAppSettings().get("IsOpenSQLCheck").toString().equals("0")) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 是不是多系统工作
	 * 
	 * @return
	 */
	public static boolean getIsMultiSys() {
		if (getAppSettings().get("IsMultiSys").toString().equals("1")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 是不是多线程工作
	 * 
	 * @return
	 */
	public static boolean getIsMultiThread_del() {
		if (getAppSettings().get("IsMultiThread").toString().equals("1")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 是不是多语言版本
	 * 
	 * @return
	 */
	public static boolean getIsMultiLanguageSys() {
		if (getAppSettings().get("IsMultiLanguageSys").toString().equals("1")) {
			return true;
		} else {
			return false;
		}
	}

	// 处理临时缓存
	/**
	 * 放在 Temp 中的cash 多少时间失效。0, 表示永久不失效
	 * 
	 * @return
	 */
	private static int getCashFail() {
		try {
			return Integer.parseInt(getAppSettings().get("CashFail").toString());
		} catch (java.lang.Exception e) {
			return 0;
		}
	}

	/**
	 * 当前的 TempCash 是否失效了
	 * 
	 * @return
	 */
	public static boolean getIsTempCashFail() {

		return true;
	}

	public static Date _CashFailDateTime = new Date(0);

	// 客户配置信息
	/**
	 * 客户编号
	 * 
	 * @return
	 */
	public static String getCustomerNo() {
		return getAppSettings().get("CustomerNo").toString();
	}

	/**
	 * 客户名称
	 * 
	 * @return
	 */
	public static String getCustomerName() {
		return getAppSettings().get("CustomerName").toString();
	}

	public static String getCustomerURL() {
		return getAppSettings().get("CustomerURL").toString();
	}

	/**
	 * 客户简称
	 * 
	 * @return
	 */
	public static String getCustomerShortName() {
		return getAppSettings().get("CustomerShortName").toString();
	}

	/**
	 * 客户地址
	 * 
	 * @return
	 */
	public static String getCustomerAddr() {
		return getAppSettings().get("CustomerAddr").toString();
	}

	/**
	 * 客户电话
	 * 
	 * @return
	 */
	public static String getCustomerTel() {
		return getAppSettings().get("CustomerTel").toString();
	}

	/// #region 微信相关配置信息
	/// <summary>
	/// 企业标识
	/// </summary>
	public static String getWX_CorpID() {
		return getAppSettings().get("CorpID").toString();
	}

	/// <summary>
	/// 帐号钥匙
	/// </summary>
	public static String getWX_AppSecret() {
		return getAppSettings().get("AppSecret").toString();
	}

	/// <summary>
	/// 应用令牌
	/// </summary>
	public static String getWX_WeiXinToken() {
		return getAppSettings().get("WeiXinToken").toString();
	}

	/// <summary>
	/// 应用加密所用的秘钥
	/// </summary>
	public static String getWX_EncodingAESKey() {
		return getAppSettings().get("EncodingAESKey").toString();
	}

	/// <summary>
	/// 进入应用后的欢迎提示
	/// </summary>
	public static boolean getWeiXin_AgentWelCom() {
		return GetValByKeyBoolen("WeiXin_AgentWelCom", false);
	}

	/// <summary>
	/// 应用ID
	/// </summary>
	public static String getWX_AgentID() {
		return getAppSettings().get("AgentID").toString();
	}

	/// <summary>
	/// 消息链接网址
	/// </summary>
	public static String getWX_MessageUrl() {
		return getAppSettings().get("WeiXin_MessageUrl").toString();
	}
	/// #endregion
	public static String getWXGZH_WeiXinToken() {
		return getAppSettings().get("GZHToKen").toString();
	}
	public static String getWXGZH_Appid() {
		return getAppSettings().get("GZHAppid").toString();
	}
	public static String getWXGZH_AppSecret(){
		return getAppSettings().get("GZHAppSecret").toString();
	}
	public static String getWeiXin_TemplateId() {
		return getAppSettings().get("WeiXin_TemplateId").toString();
	}
	/// #region 钉钉配置相关
	/// <summary>
	/// 企业标识
	/// </summary>
	public static String getDing_CorpID() {
		return getAppSettings().get("Ding_CorpID").toString();

	}

	/// <summary>
	/// 密钥
	/// </summary>
	public static String getDing_CorpSecret() {
		return getAppSettings().get("Ding_CorpSecret").toString();

	}

	/// <summary>
	/// 登录验证密钥
	/// </summary>
	public static String getDing_SSOsecret() {
		return getAppSettings().get("Ding_SSOsecret").toString();

	}

	/// <summary>
	/// 消息超链接服务器地址
	/// </summary>
	public static String getDing_MessageUrl() {
		return getAppSettings().get("Ding_MessageUrl").toString();

	}

	/// <summary>
	/// 企业应用编号
	/// </summary>
	public static String getDing_AgentID() {
		return getAppSettings().get("Ding_AgentID").toString();

	}
	/// #endregion

	public static String GetValByKey(String key, String isNullas) {

		if (getAppSettings().containsKey(key) == false)
			return isNullas;

		Object s = getAppSettings().get(key);
		if (s == null) {
			s = isNullas;
		}
		return s.toString();
	}

	public static boolean GetValByKeyBoolen(String key, boolean isNullas) {

		if (getAppSettings().containsKey(key) == false)
			return isNullas;

		String s = getAppSettings().get(key).toString();
		if (s == null)
			return isNullas;

		if (s == null)
			return isNullas;

		if (s.equals("1"))
			return true;

		return false;

	}

	public static int GetValByKeyInt(String key, int isNullas) {

		if (getAppSettings().containsKey(key) == false)
			return isNullas;

		Object s = getAppSettings().get(key);
		if (s == null) {
			return isNullas;
		}
		return Integer.parseInt(s.toString());
	}

	public static float GetValByKeyFloat(String key, int isNullas) {

		if (getAppSettings().containsKey(key) == false)
			return isNullas;

		Object s = getAppSettings().get(key);
		if (s == null) {
			return isNullas;
		}
		return Float.parseFloat(s.toString());
	}

	/**
	 * 当前数据库连接
	 * 
	 * @return
	 */
	public static String getAppCenterDSN() {
		String dsn = getAppSettings().get("AppCenterDSN").toString();
		return dsn;
	}

	/**
	 * 当前数据库连接用户.
	 * 
	 * @return
	 * @throws Exception
	 */
	public static String getUser()  {

		if (SystemConfig.getCustomerNo().equals("BWDA")) {
			String user = getAppSettings().get("JflowUser.encryption").toString();
			user = bp.sys.base.Glo.String_JieMi(user);
			return user;
		}

		return getAppSettings().get("JflowUser").toString();
	}

	public static String getPassword() throws Exception {

		if (SystemConfig.getCustomerNo().equals("BWDA")) {
			String user = getAppSettings().get("JflowPassword.encryption").toString();
			user =  bp.sys.base.Glo.String_JieMi(user);
			return user;
		}

		return getAppSettings().get("JflowPassword").toString();

	}

	public static void setAppCenterDSN(String value) {

		getAppSettings().put("AppCenterDSN", value);
	}

	/**
	 * 获取主应用程序的数据库类型
	 * 
	 * @return
	 */
	public static bp.da.DBType getAppCenterDBType() {
		Object jdbcType = getAppSettings().get("AppCenterDBType");
		if (jdbcType != null) {
			String dbType = jdbcType.toString();
			if (dbType.equalsIgnoreCase("MSMSSQL") || dbType.equalsIgnoreCase("MSSQL")) {
				return bp.da.DBType.MSSQL;
			} else if (dbType.equalsIgnoreCase("Oracle")) {
				return bp.da.DBType.Oracle;
			} else if (dbType.equalsIgnoreCase("MySQL")) {
				return bp.da.DBType.MySQL;
			} else if(dbType.equalsIgnoreCase("DM")){
				return bp.da.DBType.DM;
			} else if(dbType.equalsIgnoreCase("KingBaseR3")){
				return DBType.KingBaseR3;
			} else if(dbType.equalsIgnoreCase("KingBaseR6")){
				return DBType.KingBaseR6;
			}
		}
		throw new RuntimeException("位置的数据库类型，请配置AppCenterDBType属性。");
	}

	/**
	 * 获取不同类型的数据库变量标记
	 * 
	 * @return
	 */
	public static String getAppCenterDBVarStr() {
		switch (SystemConfig.getAppCenterDBType()) {
		case MSSQL:
			return ":";
		case Oracle:
		case KingBaseR3:
		case KingBaseR6:
		case DM:
			return ":";
		case Informix:
			return "?";
		case MySQL:
			return ":";
		default:
			return "";
		}
	}

	public static String getAppCenterDBLengthStr() {
		switch (SystemConfig.getAppCenterDBType()) {
		case Oracle:
		case KingBaseR3:
		case KingBaseR6:
		case DM:
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
	 * 获取不同类型的substring函数
	 * 
	 * @return
	 */
	public static String getAppCenterDBSubstringStr() {
		switch (SystemConfig.getAppCenterDBType()) {
		case Oracle:
		case KingBaseR3:
		case KingBaseR6:
		case DM:
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

	private static String _AppCenterDBDatabase = null;

	/**
	 * 数据库名称
	 * 
	 * @return
	 */
	public static String getAppCenterDBDatabase() {
		if (_AppCenterDBDatabase == null) {
			_AppCenterDBDatabase = getAppSettings().get("AppCenterDBDatabase").toString();
		}
		// 返回database.
		return _AppCenterDBDatabase;
	}

	public static String getAppCenterDBAddStringStr() {
		switch (SystemConfig.getAppCenterDBType()) {
		case Oracle:
		case KingBaseR3:
		case KingBaseR6:
		case MySQL:
		case DM:
		case Informix:
			return "||";
		default:
			return "+";
		}
	}

	public static String getAppSavePath() {
		String savePath = getAppSettings().get("SavaPath").toString();
		return savePath;
	}

	public static Hashtable<String, Object> CS_DBConnctionDic;

	public static void DoClearCash_del()throws Exception {
		DoClearCash();
	}

	/**
	 * 执行清空
	 */
	public static void DoClearCash() throws Exception{
		bp.da.Cash.getMap_Cash().clear();
		bp.da.Cash.getSQL_Cash().clear();
		bp.da.Cash.getEnsData_Cash().clear();
		bp.da.Cash.getEnsData_Cash_Ext().clear();
		bp.da.Cash.getBS_Cash().clear();
		bp.da.Cash.getBill_Cash().clear();
		bp.da.CashEntity.getDCash().clear();
	}

	/**
	 * 是否启用CCIM?
	 * 
	 * @return
	 */
	public static boolean getIsEnableCCIM() {
		if ("1".equals(getAppSettings().get("IsEnableCCIM"))) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 是否启用密码加密
	 * 
	 */
	public static boolean getIsEnablePasswordEncryption() {
		String s = (String) ((SystemConfig.getAppSettings().get("IsEnablePasswordEncryption") instanceof String)
				? SystemConfig.getAppSettings().get("IsEnablePasswordEncryption") : null);
		if (s == null || s.equals("0")) {
			return false;
		}
		return true;
	}
	public static String getPasswordEncryptionType(){
		if (DataType.IsNullOrEmpty(SystemConfig.getAppSettings().get("PasswordEncryptionType")) == false) {
			return (String) SystemConfig.getAppSettings().get("PasswordEncryptionType");
		}
		return "0";
	}
	public static String getHostURL() {
		if (DataType.IsNullOrEmpty(SystemConfig.getAppSettings().get("HostURL")) == false) {
			return (String) SystemConfig.getAppSettings().get("HostURL");
		}
		return getHostURLOfBS();
	}
	public static String getAPIHostURL() {
		return getAppSettings().get("APIHostURL").toString();
	}
	public static String getHostURLOfBS() {

		String url = "http://" + CommonUtils.getRequest().getServerName() + ":"
				+ CommonUtils.getRequest().getServerPort() + "/" + CommonUtils.getRequest().getContextPath();
		return url;

	}
	
	public static String getMobileURL() {
		if (DataType.IsNullOrEmpty(SystemConfig.getAppSettings().get("MobileURL")) == false) {
			return (String) SystemConfig.getAppSettings().get("MobileURL");
		}
		return getHostURLOfBS();
	}

	

	/**
	 * 是否多语言
	 */
	public static boolean getIsMultilingual() {
		if (getAppSettings().get("IsMultilingual").toString().equals("1"))
			return true;
		return false;
	}

	/// <summary>
	/// 使用的语言
	/// </summary>
	public static String getLangue() {

		String str = getAppSettings().get("Langue").toString();
		if (DataType.IsNullOrEmpty(str))
			return "CN";
		return str;

	}

	public static String getDing_AppKey() {
		String str = getAppSettings().get("Ding_AppKey").toString();
		return str;
	}

	public static String getDing_AppSecret() {
		String str = getAppSettings().get("Ding_AppSecret").toString();
		return str;
	}

	/**
	 * 调用消息的接口
	 * @return
	 */
	public static String getHandlerOfMessage(){
		Object obj = getAppSettings().get("HandlerOfMessage");
		if(obj == null)
			return "";
		return obj.toString();
	}
	public static String getSecondAppAdmins(){
		String str = getAppSettings().get("SecondAppAdmins").toString();
		return str;
	}
	/// <summary>
	/// 百度云应用AK
	/// </summary>
	public static String getAPIKey()
	{
		Object obj = getAppSettings().get("APIKey");
		if(obj == null)
			return "";
		return obj.toString();

	}
	/// <summary>
	/// 百度云应用SK
	/// </summary>
	public static String getSecretKey()
	{
		Object obj = getAppSettings().get("SecretKey");
		if(obj == null)
			return "";
		return obj.toString();

	}

	/**
	 组织结构集成模式
	 */
	public static boolean getOrganizationIsView()throws Exception
	{
		String val = SystemConfig.GetValByKey("OrganizationIsView","0");
		if (val.equals("0"))
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	public static String getDateType()throws Exception
	{
		return SystemConfig.GetValByKey("DateType", "varchar");
	}
	public static String getAppID()throws Exception
	{
		return SystemConfig.GetValByKey("AppID", "");
	}

	public static String getSaasHost()throws Exception
	{
		String str = bp.difference.SystemConfig.getAppSettings().get("SaasHost").toString();
		if (DataType.IsNullOrEmpty(str) == true)
		{
			return "ccbpm.cn";
		}
		return str;
	}


	/**
	 * 数据库大小写模式
	 */
	public static FieldCaseModel AppCenterDBFieldCaseModel()  {
		switch (getAppCenterDBType())
		{
			case Oracle:
				return FieldCaseModel.UpperCase;
			case KingBaseR3:
				// R3时，查询敏感设置
				String sql ="show case_sensitive;";
				String caseSen="";
				try{
					caseSen = DBAccess.RunSQLReturnString(sql);
				}catch(Exception ex){
					sql="show enable_ci;";
					caseSen = DBAccess.RunSQLReturnString(sql);
					if("on".equals(caseSen))
						return FieldCaseModel.None;
					else
						return FieldCaseModel.UpperCase;
				}
				if("on".equals(caseSen))
					return FieldCaseModel.UpperCase;
				else
					return FieldCaseModel.None;



			case KingBaseR6:
				return FieldCaseModel.Lowercase;
			case PostgreSQL:
				return FieldCaseModel.Lowercase;
			default:
				return FieldCaseModel.None;
		}
	}

}
