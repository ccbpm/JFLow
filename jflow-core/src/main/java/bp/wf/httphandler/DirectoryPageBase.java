package bp.wf.httphandler;

import bp.difference.*;
import bp.difference.handler.CommonUtils;
import bp.web.*;
import bp.da.*;
import bp.*;
import bp.wf.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;
import java.math.*;

public abstract class DirectoryPageBase
{


		///#region 执行方法.
	/** 
	 获得Form数据.
	 
	 param key key
	 @return 返回值
	*/

	public final String GetValFromFrmByKey(String key) throws UnsupportedEncodingException {
		return GetValFromFrmByKey(key, null);
	}

//ORIGINAL LINE: public string GetValFromFrmByKey(string key, string isNullAsVal = null)
	public final String GetValFromFrmByKey(String key, String isNullAsVal) throws UnsupportedEncodingException {
		String val = ContextHolderUtils.getRequest().getParameter(key);
		if (val == null && key.toString().contains("DDL_") == false)
		{
			val = this.GetRequestVal("DDL_" + key);
		}

		if (val == null && key.toString().contains("TB_") == false)
		{
			val = this.GetRequestVal("TB_" + key);
		}

		if (val == null && key.toString().contains("CB_") == false)
		{
			val = this.GetRequestVal("CB_" + key);
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
	 token for guangxi jisuanzhongxin.
	 1. 手机端连接服务需要,身份验证,需要token.
	 2. 在全局中配置 TokenHost 地址, 每次调用服务都需要传入Token 参数.
	 3. 如果不配置 TokenHost 就提示错误.
	 4. 仅仅在会话信息丢失后，在调用该方法.
	 
	 param token 获得token.
	 @return 
	*/
	public final String DealToken(DirectoryPageBase page, String mothodName) throws Exception {
		String token = page.GetRequestVal("Token");
		if (DataType.IsNullOrEmpty(token) == true)
		{
			return null;
		}

		//throw new Exception("err@登录信息丢失，或者没有传递过来token,页面:["+page.ToString()+"]方法:["+ mothodName+"]");
		String host = SystemConfig.GetValByKey("TokenHost", null);
		if (DataType.IsNullOrEmpty(host) == true)
		{
			return null;
		}

		//throw new Exception("err@全局变量:TokenHost，没有获取到.");
		token = token.split("[,]", -1)[0];
		String url = host + token;
		String data = DataType.ReadURLContext(url, 5000);

		if (DataType.IsNullOrEmpty(data) == true)
		{
			throw new RuntimeException("err@token失效，请重新登录。" + url + "");
		}

		bp.port.Emp emp = new bp.port.Emp();
		emp.setUserID(data);
		if (emp.RetrieveFromDBSources() == 0)
		{
			throw new RuntimeException("err@根据token获取用户名错误:" + token + ",获取数据为:" + data);
		}

		//执行登录.
		Dev2Interface.Port_Login(data);
		//DBAccess.RunSQL("UPDATE WF_Emp SET Token='" + token + "'  WHERE No='" + emp.No + "'");
		return "info@登录成功.";
	}
	/** 
	 执行方法
	 
	 param obj 对象名
	 param methodName 方法
	 @return 返回执行的结果，执行错误抛出异常
	*/
	public final String DoMethod(DirectoryPageBase myEn, String methodName) throws Exception {
		//deal token
		if (WebUser.getNo() == null)
		{
			boolean isCanDealToken = true;

			if (myEn.getDoType().contains("Login") == true)
			{
				isCanDealToken = false;
			}

			if (myEn.getDoType().contains("Index") == true)
			{
				isCanDealToken = false;
			}

			if (myEn.toString().contains("Admin") == true)
			{
				isCanDealToken = false;
			}

			//if (methodName.contains("WebUser") == true)
			//    isCanDealToken = false;
			//if (isCanDealToken == true)
			   this.DealToken(myEn, myEn.getDoType());
		}

		//string token=myEn.ToString
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
			Object tempVar = mp.invoke(this, paras);
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
				if (ex.getCause().getMessage().indexOf("err@") == 0)
				{
					return ex.getCause().getMessage();
				}
				else
				{
					return "err@调用类:[" + myEn + "]方法:[" + methodName + "]出现错误:" + ex.getCause();
				}
			}
			else
			{
				if (ex.getMessage().indexOf("err@") == 0)
				{
				return ex.getMessage();
				}
			else
			{
				return "err@调用类:[" + myEn + "]方法:[" + methodName + "]出现错误:" + ex.getMessage();
			}
			}
		}
	}
	/** 
	 执行默认的方法名称
	 
	 @return 返回执行的结果
	*/
	protected String DoDefaultMethod() throws Exception {
		if (this.getDoType().contains(">") == true)
		{
			return "err@非法的脚本植入.";
		}

		return "err@子类[" + this.toString() + "]没有重写该[" + this.GetRequestVal("DoMethod") + "]方法，请确认该方法是否缺少或者是非public类型的.";
	}

		///#endregion 执行方法.


		///#region 公共方法.
	public Hashtable ht = null;
	public final void AddPara(String key, String val)
	{
		if (ht == null)
		{
			ht = new Hashtable();
		}
		ht.put(key, val);
	}
	public final String GetRequestVal(String key) throws UnsupportedEncodingException {
		if (ht != null && ht.containsKey(key))
		{
			String myval = ht.get(key) instanceof String ? (String)ht.get(key) : null;
			return URLDecoder.decode(myval, "UTF-8");

		}

		//String val = HttpContextHelper.RequestQueryString(key);
		String val = CommonUtils.getRequest().getParameter(key);
		if (val == null)
		{
			val = ContextHolderUtils.getRequest().getParameter(key);

			if (val == null)
			{
				return null;
			}
		}
		return URLDecoder.decode(val, "UTF-8");
	}
	/** 
	 公共方法获取值
	 
	 param param 参数名
	 @return 
	*/
	public final int GetRequestValInt(String param) throws UnsupportedEncodingException {
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
	public final int GetRequestValChecked(String param) throws UnsupportedEncodingException {
		String str = GetRequestVal(param);
		if (str == null || str.equals("") || str.equals("null") || str.equals("undefined"))
		{
			return 0;
		}

		return 1;
	}
	/** 
	 公共方法获取值
	 
	 param param 参数名
	 @return 
	*/
	public final boolean GetRequestValBoolen(String param) throws UnsupportedEncodingException {
		if (this.GetRequestValInt(param) == 1)
		{
			return true;
		}
		return false;
	}
	/** 
	 公共方法获取值
	 
	 param param
	 @return 
	*/
	public final long GetRequestValInt64(String param) throws UnsupportedEncodingException {
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
	 
	 param param
	 @return 
	*/
	public final float GetRequestValFloat(String param) throws UnsupportedEncodingException {
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
	public final BigDecimal GetRequestValDecimal(String param) throws UnsupportedEncodingException {
		String str = GetRequestVal(param);
		if (str == null || str.equals("") || str.equals("null"))
		{
			return BigDecimal.valueOf(0);
		}
		try
		{
			return  new BigDecimal(str);
		}
		catch (java.lang.Exception e)
		{
			return BigDecimal.valueOf(0);
		}
	}
	/** 
	 获得参数.
	*/
	public final String getRequestParas() throws Exception {
		String urlExt = "";

			// 适配framework和core（注：net core的rawurl中不含form data）
		for (Object key : CommonUtils.getRequest().getParameterMap().keySet())
		{
			if (key.equals("1") == true || key.equals("t") == true || key.equals("T") == true) // 过滤url中1=1的情形
			{
				continue;
			}

			String value = ContextHolderUtils.getRequest().getParameter(String.valueOf(key));
			if (!DataType.IsNullOrEmpty(value))
			{
				urlExt += String.format("&%1$s=%2$s", key, value);
			}

		}
		return urlExt;
	}
	/** 
	 所有的paras
	*/
	public final String getRequestParasOfAll() throws Exception {
		String urlExt = "";
		String rawUrl = ContextHolderUtils.getRequest().getRequestURI();
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


			if (para.contains("DoType=") || para.contains("DoMethod=") || para.toLowerCase().equals("t") || para.contains("HttpHandlerName="))
			{
				continue;
			}

			urlExt += "&" + para;
		}


		for (Object key : CommonUtils.getRequest().getParameterMap().keySet())
		{

			if (key.equals("DoType") || key.equals("DoMethod")  || key.equals("HttpHandlerName")) //|| key.toLowerCase().equals("t")
			{
				continue;
			}
			if (urlExt.contains("&" + key + "=") == false)
			{
				urlExt += "&" + key + "=" + ContextHolderUtils.getRequest().getParameter(String.valueOf(key));
			}
		}

		return urlExt;
	}

		///#endregion


		///#region 属性参数.
	/** 
	 
	*/
	public final String getPKVal() throws Exception {
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
	public final boolean isMobile() throws Exception {
		String v = this.GetRequestVal("IsMobile");
		if (v != null && v.equals("1"))
		{
			return true;
		}

		if (ContextHolderUtils.getRequest().getRequestURI().contains("/CCMobile/") == true)
		{
			return true;
		}

		return false;
	}
	/** 
	 编号
	*/
	public final String getNo() throws Exception {
		String str = this.GetRequestVal("No"); // context.Request.QueryString["No"];
		if (str == null || str.equals("") || str.equals("null"))
		{
			return null;
		}
		return str;
	}
	public final String getName() throws Exception {
		String str = this.GetRequestVal("Name");
		if (str == null || str.equals("") || str.equals("null"))
		{
			return null;
		}
		return str;
	}
	public final String getUserNo() throws Exception {
		String str = this.GetRequestVal("UserNo");
		if (str == null || str.equals("") || str.equals("null"))
		{
			return null;
		}
		return str;
	}
	public final String getDoWhat() throws Exception {
		String str = this.GetRequestVal("DoWhat");
		if (str == null || str.equals("") || str.equals("null"))
		{
			return null;
		}
		return str;
	}
	/** 
	 执行类型
	*/
	public final String getDoType() throws Exception {
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
	public final String getEnName() throws Exception {
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
	public final String getEnsName() throws Exception {
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
	public final String getTreeEnsName() throws Exception {
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
	public final String getFK_Dept() throws Exception {
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
	public final String getMyPK() throws Exception {
		String str = this.GetRequestVal("MyPK");
		if (DataType.IsNullOrEmpty(str))
		{
			return null;
		}
		return str;
	}
	public final String getFK_Event() throws Exception {
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
	public final String getFK_SFTable() throws Exception {
		String str = this.GetRequestVal("FK_SFTable");
		if (DataType.IsNullOrEmpty(str))
		{
			return null;
		}
		return str;
	}
	public final String getEnumKey() throws Exception {
		String str = this.GetRequestVal("EnumKey");
		if (DataType.IsNullOrEmpty(str))
		{
			return null;
		}
		return str;
	}
	public final String getKey() throws Exception {
		String str = this.GetRequestVal("Key");
		if (DataType.IsNullOrEmpty(str))
		{
			return null;
		}
		return str;
	}
	public final String getKeyOfEn() throws Exception {
		String str = this.GetRequestVal("KeyOfEn");
		if (DataType.IsNullOrEmpty(str))
		{
			return null;
		}
		return str;
	}
	public final String getVals() throws Exception {
		String str = this.GetRequestVal("Vals");
		if (DataType.IsNullOrEmpty(str))
		{
			return null;
		}
		return str;
	}
	/** 
	 FK_MapData
	*/
	public final String getFK_MapData() throws UnsupportedEncodingException {
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
	public final String getFKMapExt() throws Exception {
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
	public final String getFK_Flow() throws Exception {
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
	public final String getFK_Emp() throws Exception {
		String str = this.GetRequestVal("FK_Emp");
		if (DataType.IsNullOrEmpty(str) == true)
		{
			return null;
		}
		return str;
	}
	/** 
	 域
	*/
	public final String getDomain() throws Exception {
		String str = this.GetRequestVal("Domain");
		if (DataType.IsNullOrEmpty(str) == true)
		{
			return null;
		}
		return str;
	}
	/** 
	 相关编号
	*/
	public final String getRefNo() throws Exception {
		String str = this.GetRequestVal("RefNo");
		if (DataType.IsNullOrEmpty(str) == true)
		{
			return null;
		}
		return str;
	}
	/** 
	 组织编号
	*/
	public final String getOrgNo() throws Exception {
		String str = this.GetRequestVal("OrgNo");
		if (DataType.IsNullOrEmpty(str) == true)
		{
			return null;
		}
		return str;
	}
	/** 
	 表单ID 
	*/
	public final String getFrmID() throws Exception {
		String str = this.GetRequestVal("FrmID");

		if (DataType.IsNullOrEmpty(str) == true)
		{
			str = this.GetRequestVal("FK_MapData");
		}

		if (DataType.IsNullOrEmpty(str) == true)
		{
			str = this.GetRequestVal("FK_Frm");
		}

		return str;
	}
	public final int getGroupField() throws Exception {
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
	public final int getFK_Node() throws Exception {
		int nodeID = this.GetRequestValInt("FK_Node");
		if (nodeID == 0)
		{
			nodeID = this.GetRequestValInt("NodeID");
		}
		return nodeID;
	}
	public final int getNodeID() throws Exception {
		return this.getFK_Node();
	}
	public final long getFID() throws Exception {
		//return this.GetRequestValInt("FID");

		String str = this.GetRequestVal("FID"); //  context.Request.QueryString["FID"];
		if (DataType.IsNullOrEmpty(str) == true)
		{
			return 0;
		}
		return Integer.parseInt(str);
	}

	private long _workID = 0;
	public final long getWorkID() throws Exception {
		if (_workID != 0)
		{
			return _workID;
		}
		String str = this.GetRequestVal("WorkID");
		if (DataType.IsNullOrEmpty(str) == true)
		{
			str = this.GetRequestVal("PKVal"); //@hontyan. 这个方法都要修改.
			if (DataType.IsNullOrEmpty(str) == true)
			{
				str = this.GetRequestVal("OID");
			}
		}

		if (DataType.IsNullOrEmpty(str) == true)
		{
			return 0;
		}

		return Integer.parseInt(str);
	}
	public final void setWorkID(long value)throws Exception
	{_workID = value;
	}

	public final String getWorkIDStr() throws Exception {
		String val = this.GetRequestVal("WorkID");
		if (DataType.IsNullOrEmpty(val) == true)
		{
			val = this.GetRequestVal("OID");
		}
		if (DataType.IsNullOrEmpty(val) == true)
		{
			val = this.GetRequestVal("PKVal");
		}
		return val;
	}
	public final long getCWorkID() throws Exception {
		return this.GetRequestValInt("CWorkID");
	}
	/** 
	 框架ID
	*/
	public final String getFKMapFrame() throws Exception {


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
	public final String getSID() throws Exception {
		String str = this.GetRequestVal("Token"); // context.Request.QueryString["Token"];
		if (DataType.IsNullOrEmpty(str) == true)
		{
			return null;
		}
		return str;
	}
	/** 
	   RefOID
	*/
	public final int getRefOID() throws Exception {
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
	public final int getOID() throws Exception {
		String str = this.GetRequestVal("RefOID"); // context.Request.QueryString["RefOID"];
		if (DataType.IsNullOrEmpty(str) == true || str.equals("undefined"))
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
	public final String getFK_MapDtl() throws Exception {
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
	public final int getPageIdx() throws Exception {
		int i = this.GetRequestValInt("PageIdx");
		if (i == 0)
		{
			return 1;
		}
		return i;
	}
	/** 
	 页面大小
	*/
	public final int getPageSize() throws Exception {
		int i = this.GetRequestValInt("PageSize");
		if (i == 0)
		{
			return 10;
		}
		return i;
	}
	public final int getIndex() throws Exception {
		return this.GetRequestValInt("Index");
	}

	/** 
	 字段属性编号
	*/
	public final String getAth() throws Exception {
		String str = this.GetRequestVal("Ath"); // context.Request.QueryString["Ath"];
		if (DataType.IsNullOrEmpty(str) == true)
		{
			return null;
		}
		return str;
	}

	/** 
	 获得Int数据
	 
	 param key
	 @return 
	*/
	public final int GetValIntFromFrmByKey(String key) throws UnsupportedEncodingException {
		String str = this.GetValFromFrmByKey(key);
		if (str == null || str.equals(""))
		{
			throw new RuntimeException("@参数:" + key + "没有取到值.");
		}
		return Integer.parseInt(str);
	}

	public final float GetValFloatFromFrmByKey(String key) throws UnsupportedEncodingException {
		String str = this.GetValFromFrmByKey(key);
		if (str == null || str.equals(""))
		{
			throw new RuntimeException("@参数:" + key + "没有取到值.");
		}
		return Float.parseFloat(str);
	}
	public final BigDecimal GetValDecimalFromFrmByKey(String key) throws UnsupportedEncodingException {
		String str = this.GetValFromFrmByKey(key);
		if (str == null || str.equals(""))
		{
			throw new RuntimeException("@参数:" + key + "没有取到值.");
		}
		return  new BigDecimal(str);
	}

	public final boolean GetValBoolenFromFrmByKey(String key) throws UnsupportedEncodingException {
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

	public final String getRefPK() throws Exception {
		return this.GetRequestVal("RefPK");

			//string str = this.context.Request.QueryString["RefPK"];
			//return str;
	}
	public final String getRefPKVal() throws Exception {
		String str = this.GetRequestVal("RefPKVal");
		if (str == null)
		{
			return "0";
		}
		return str;
	}

		///#endregion 属性.


		///#region 父子流程相关的属性.
	public final long getPWorkID() throws Exception {
		return this.GetRequestValInt64("PWorkID");
	}
	public final long getPFID() throws Exception {
		return this.GetRequestValInt64("PFID");
	}
	public final int getPNodeID() throws Exception {
		return this.GetRequestValInt("PNodeID");
	}
	public final String getPFlowNo() throws Exception {
		return this.GetRequestVal("PFlowNo");
	}

		///#endregion 父子流程相关的属性.
}