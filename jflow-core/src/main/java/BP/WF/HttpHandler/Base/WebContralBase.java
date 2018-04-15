package BP.WF.HttpHandler.Base;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;

import org.apache.http.protocol.HttpContext;

import BP.DA.DataType;
import BP.DA.Log;
import BP.Sys.Glo;
import BP.Tools.StringHelper;
import cn.jflow.controller.wf.workopt.BaseController;

public abstract class WebContralBase extends BaseController {
	/**
	 * 获得Form数据.
	 * 
	 * @param key
	 *            key
	 * @return 返回值
	 */
	public final String GetValFromFrmByKey(String key,String isNullAsVal) {
		// String val = context.Request.Form[key];
		String val = getRequest().getParameter(key);
		if (val == null && key.contains("DDL_") == false) {
			// val = context.Request.Form["DDL_" + key];
			val = this.getRequest().getParameter("DDL_" + key);
		}

		if (val == null && key.contains("TB_") == false) {
			// val = context.Request.Form["TB_" + key];
			val = getRequest().getParameter("TB" + key);
		}

		if (val == null && key.contains("CB_") == false)
        {
            val = getRequest().getParameter("CB_" + key);
        }
		
		if (val == null) {
			if (isNullAsVal != null)
                return isNullAsVal;
			throw new RuntimeException("@获取Form参数错误,参数集合不包含[" + key + "]");
		}

		val = val.replace("'", "~");
		return val;
	}
	/**
	 * 获得Form数据.
	 * 
	 * @param key
	 *            key
	 * @return 返回值
	 */
	public final String GetValFromFrmByKey(String key) {
		// String val = context.Request.Form[key];
		String val = getRequest().getParameter(key);
		if (val == null && key.contains("DDL_") == false) {
			// val = context.Request.Form["DDL_" + key];
			val = this.getRequest().getParameter("DDL_" + key);
		}

		if (val == null && key.contains("TB_") == false) {
			// val = context.Request.Form["TB_" + key];
			val = getRequest().getParameter("TB_" + key);
		}

		if (val == null && key.contains("CB_") == false)
        {
            val = getRequest().getParameter("CB_" + key);
        }
		
		if (val == null) {
			throw new RuntimeException("@获取Form参数错误,参数集合不包含[" + key + "]");
		}

		val = val.replace("'", "~");
		return val;
	}

	public float GetValFloatFromFrmByKey(String key) {
		String str = this.GetValFromFrmByKey(key);
		if (str == null || str == "")
			throw new RuntimeException("@参数:" + key + "没有取到值.");
		return Float.parseFloat(str);
	}

	public BigDecimal GetValDecimalFromFrmByKey(String key) {
		String str = this.GetValFromFrmByKey(key);
		if (str == null || str == "")
			throw new RuntimeException("@参数:" + key + "没有取到值.");
		return new BigDecimal(str);
	}

	public int getIndex() {
		String str = getRequest().getParameter("Index");
		if (str == null || str == "")
			return 1;
		return Integer.parseInt(str);
	}

	/**
	 * 执行方法
	 * 
	 * @param obj
	 *            对象名
	 * @param methodName
	 *            方法
	 * @return 返回执行的结果，执行错误抛出异常
	 */
	public final String DoMethod(WebContralBase myEn, String methodName) {

		java.lang.Class tp = myEn.getClass();
		java.lang.reflect.Method mp = null;
		
		try {
			mp = tp.getMethod(methodName);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}

		if (mp == null) {
			/* 没有找到方法名字，就执行默认的方法. */
			String str= myEn.DoDefaultMethod();
			
			if (str==null || "".equals(str))
				return "err@方法:"+methodName+"没有翻译..";
			
			return str;	 
		}

		// 执行该方法.
		Object[] paras = null;
		Object tempVar = null;
		try {
			tempVar = mp.invoke(this, paras);
			
		} catch (Exception e) {
			
			
			String msg = e.getMessage();
			
			if (e.getCause()!=null)
			{
				msg= e.getCause().getMessage();
			}
			
			
			String str = "";
			if(e.getCause().getMessage().indexOf("wait")>-1){
				str += "@错误原因可能是数据库连接异常";
			}
			
			//str+="\t\n@"+e.getCause().getMessage();
			
			 String myParas =  getRequest().getQueryString();
			 
			 String  errInfo="err@页面类[" + myEn.toString() + ",方法[" + methodName + "]执行错误.";			 
			 errInfo+="\t\n@参数:"+myParas;
			 errInfo+="\t\n@Msg:"+msg;			 
			 errInfo+="\t\n@getStackTrace:"+ e.getStackTrace();
			 
			 
			 
			 Log.DebugWriteError("BP.WF.HttpHangerBase.DoMethod()" + errInfo);			 
			 return errInfo;
			 
			 //err   @" + e.getMessage()+str+"@"+e.getStackTrace()+"\t\n@"+myParas;
			
		}
		return (String) ((tempVar instanceof String) ? tempVar : null); // 调用由此
																		// MethodInfo
																		// 实例反射的方法或构造函数。
	}

	/**
	 * 执行默认的方法名称
	 * 
	 * @return 返回执行的结果
	 */
	protected String DoDefaultMethod() {
		return "@子类没有重写该[" + this.getDoType() + "]方法.";
	}
	 
	/**
	 * 公共方法获取值
	 * 
	 * @param param
	 *            参数名
	 * @return
	 */
	public final String GetRequestVal(String param) {
		
		String val = getRequest().getParameter(param);
		
		if (StringHelper.isNullOrEmpty(val)) {
			val = getRequest().getParameter(param);
		}
		
		
		return val;
		
		
	 /*
		try {
			
		 return URLEncoder.encode(val, "UTF-8");
		 
		 } catch (UnsupportedEncodingException e) {
			 
		    e.printStackTrace();
		     return val;
		 
		 }
		 */
	 
	}
	public Boolean GetRequestValBoolen(String key)
	{
	  if (this.GetRequestValInt(key)==0)
		  return false;
	  
	  return true;
	}
	

	/**
	 * 公共方法获取值
	 * 
	 * @param param
	 *            参数名
	 * @return
	 */
	public final int GetRequestValInt(String param) {
		String str = GetRequestVal(param);
		if (str == null || str.equals("") || str.equals("null")) {
			return 0;
		}
		try {
			return Integer.parseInt(str);
		} catch (java.lang.Exception e) {
			return 0;
		}
	}

	/**
	 * 公共方法获取值
	 * 
	 * @param param
	 * @return
	 */
	public final long GetRequestValInt64(String param) {
		String str = GetRequestVal(param);
		if (str == null || str.equals("") || str.equals("null")) {
			return 0;
		}
		try {
			return Long.parseLong(str);
		} catch (java.lang.Exception e) {
			return 0;
		}
	}

	/**
	 * 数据
	 * 
	 * @param param
	 * @return
	 */
	public final float GetRequestValFloat(String param) {
		String str = GetRequestVal(param);
		if (str == null || str.equals("") || str.equals("null")) {
			return 0;
		}
		try {
			return Float.parseFloat(str);
		} catch (java.lang.Exception e) {
			return 0;
		}
	}

	/**
	 * 获得参数.
	 */
	public final String getRequestParas() {
		String urlExt = "";
		String rawUrl = this.getRequest().getQueryString();
		rawUrl = "&" + rawUrl.substring(rawUrl.indexOf('?') + 1);
		String[] paras = rawUrl.split("[&]", -1);
		for (String para : paras) {
			if (para == null || para.equals("") || para.contains("=") == false) {
				continue;
			}

			if (para.equals("1=1")) {
				continue;
			}

			urlExt += "&" + para;
		}
		return urlExt;
	}

	/**
	 * 编号
	 */
	public final String getNo() {
		// String str = context.Request.QueryString["No"];
		String str = getRequest().getParameter("No");
		if (str == null || str.equals("") || str.equals("null")) {
			return null;
		}
		return str;
	}

	public final String getFK_Dept() {
		String str = getRequest().getParameter("FK_Dept");
		if (str == null || str.equals("") || str.equals("null")) {
			return null;
		}
		return str;
	}

	public final String getName() {
		String str = getRequest().getParameter("Name");
		if (str == null || str.equals("") || str.equals("null")) {
			return null;
		}
		return str;
	}

	/**
	 * 执行类型
	 */
	public final String getDoType() {
		// 获得执行的方法.
		String doType = "";

		doType = this.GetRequestVal("DoType");
		if (doType == null) {
			doType = this.GetRequestVal("Action");
		}

		if (doType == null) {
			doType = this.GetRequestVal("action");
		}

		if (doType == null) {
			doType = this.GetRequestVal("Method");
		}

		return doType;
	}

	public final String getEnsName() {
		String str = this.GetRequestVal("EnsName");

		if (str == null || str.equals("") || str.equals("null")) {
			str = this.GetRequestVal("FK_MapData");
		}

		if (str == null || str.equals("") || str.equals("null")) {
			return null;
		}

		return str;
	}

	public final String getMyPK() {
		String str = this.GetRequestVal("MyPK");
		if (str == null || str.equals("") || str.equals("null")) {
			return null;
		}
		return str;
	}

	public final String getFK_Event() {
		String str = this.GetRequestVal("FK_Event");
		if (str == null || str.equals("") || str.equals("null"))
			return null;
		return str;
	}

	/**
	 * 字典表
	 */
	public final String getFK_SFTable() {
		String str = this.GetRequestVal("FK_SFTable");
		if (str == null || str.equals("") || str.equals("null")) {
			return null;
		}
		return str;
	}

	public final String getEnumKey() {
		String str = this.GetRequestVal("EnumKey");
		if (str == null || str.equals("") || str.equals("null")) {
			return null;
		}
		return str;
	}

	public final String getKeyOfEn() {
		String str = this.GetRequestVal("KeyOfEn");
		if (str == null || str.equals("") || str.equals("null")) {
			return null;
		}
		return str;

	}

	/**
	 * FK_MapData
	 */
	public final String getFK_MapData() {
		String str = this.GetRequestVal("FK_MapData");
		if (str == null || str.equals("") || str.equals("null")) {
			 str = this.GetRequestVal("FrmID"); 
		}
		return str;
		
	}

	/**
	 * 扩展信息
	 */
	public final String getFK_MapExt() {
		String str = this.GetRequestVal("FK_MapExt");
		if (StringHelper.isNullOrEmpty(str)) {
			str = this.GetRequestVal("MyPK");
			if (StringHelper.isNullOrEmpty(str)) {
				return null;
			}
		}
		return str;
	}

	/**
	 * 流程编号
	 */
	public final String getFK_Flow() {
		String str = this.GetRequestVal("FK_Flow");
		if (str == null || str.equals("") || str.equals("null")) {
			return null;
		}
		return str;
	}

	public final int getGroupField() {
		String str = this.GetRequestVal("GroupField");
		if (str == null || str.equals("") || str.equals("null")) {
			return 0;
		}

		return Integer.parseInt(str);
	}

	/**
	 * 节点ID
	 */
	public int getFK_Node() {
//		return this.GetRequestValInt("FK_Node");
		 int nodeID = this.GetRequestValInt("FK_Node");
         if (nodeID == 0)
             nodeID = this.GetRequestValInt("NodeID");
         return nodeID;
	}

	public final long getFID() {
		// return this.GetRequestValInt("FID");

		String str = getRequest().getParameter("FID");
		if (str == null || str.equals("") || str.equals("null")) {
			return 0;
		}
		return Integer.parseInt(str);
	}

	public long getWorkID() {
		
		
		 String str = this.GetRequestVal("WorkID");
         if (str == null || str.equals("") || str.equals("null"))
             str = this.GetRequestVal("PKVal");

         if (str == null || str.equals("") || str.equals("null"))
             str = this.GetRequestVal("OID");

         if (str == null || str.equals("") || str.equals("null") )
             return 0;
          
		
		return Integer.parseInt(str);
	}

	/**
	 * 框架ID
	 */
	public final String getFK_MapFrame() {
		// String str = context.Request.QueryString["FK_MapFrame"];
		String str = getRequest().getParameter("FK_MapFrame");
		if (str == null || str.equals("") || str.equals("null")) {
			return null;
		}
		return str;
	}

	/**
	 * RefOID
	 */
	public final int getRefOID() {
		// String str = context.Request.QueryString["RefOID"];
		String str = getRequest().getParameter("RefOID");

		if (str == null || str.equals("") || str.equals("null")) {
			// str = context.Request.QueryString["OID"];
			str = getRequest().getParameter("OID");
		}

		if (str == null || str.equals("") || str.equals("null")) {
			return 0;
		}

		return Integer.parseInt(str);
	}

	/**
	 * 明细表
	 */

	public final String getFK_MapDtl() {
		String str = getRequest().getParameter("FK_MapDtl");
		if (str == null || str.equals("") || str.equals("null")) {
			str = getRequest().getParameter("EnsName");
		}
		return str;
	}

	/**
	 * 页面Index. /** 字段属性编号
	 */
	public final String getAth() {
		// String str = context.Request.QueryString["Ath"];
		String str = getRequest().getParameter("Ath");
		if (str == null || str.equals("") || str.equals("null")) {
			return null;
		}
		return str;
	}

	public HttpContext context = null;

	/**
	 * 获得Int数据
	 * 
	 * @param key
	 * @return
	 */
	public final int GetValIntFromFrmByKey(String key) {
		String str = this.GetValFromFrmByKey(key);
		if (str == null || str.equals("") || str.equals("0")) {
			throw new RuntimeException("@参数:" + key + "没有取到值.");
		}
		return Integer.parseInt(str);
	}

	public final boolean GetValBoolenFromFrmByKey(String key) {
		String val = this.GetValFromFrmByKey(key,"0");
		if (val == "on" || val == "1")
            return true;
		if (val == null || val.equals("") || val.equals("0")) {
			return false;
		}
		return true;
	}

	public final String getRefPK() {
		// String str = this.context.Request.QueryString["RefPK"];
		String str = getRequest().getParameter("RefPK");
		return str;
	}

	public final String getRefPKVal() {
		// String str = this.context.Request.QueryString["RefPKVal"];
		String str = this.getRequest().getParameter("RefPKVal");
		if (str == null) {
			return "1";
		}
		return str;
	}

	/*
	 * 表单ID
	 */
	public final String getFrmID() {
		String str = this.GetRequestVal("FrmID");
		if (str == null || str.equals("") || str.equals("null")) {
			return this.GetRequestVal("FK_MapData");
		}

		return str;
	}
	 private long WorkID;
	 public long getPWorkID()
	 {
		 
		 return this.GetRequestValInt("PWorkID");
	 }
 
	
	 public final long getCWorkID()
	 {
		 return this.GetRequestValInt("CWorkID");
	 }
	
	 ///#region 属性参数.
			/** 
			 @于庆海翻译.
			 
			*/
			public String getPKVal()
			{
				String str= this.GetRequestVal("PKVal");
				
				if (DataType.IsNullOrEmpty(str)==true)
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
				

                if ("null".equals(str) == true)
                    return null;
                
				return str;
			}
	 
	/// 是否是移动？
     /// </summary>
     public boolean getIsMobile()
     {
         String v = this.GetRequestVal("IsMobile");
         if (v != null && v == "1")
             return true;

 		 if(Glo.getRequest().getRequestURI().contains("/CCMobile/"))
             return true;

         return false;
     }
}
