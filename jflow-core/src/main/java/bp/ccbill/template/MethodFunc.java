package bp.ccbill.template;

import bp.da.*;
import bp.difference.SystemConfig;
import bp.web.*;
import bp.en.*;
import bp.en.Map;
import bp.sys.*;

/** 
 功能执行
*/
public class MethodFunc extends EntityMyPK
{

		///基本属性
	/** 
	 表单ID
	*/
	public final String getFrmID() throws Exception
	{
		return this.GetValStringByKey(MethodAttr.FrmID);
	}
	public final void setFrmID(String value) throws Exception
	{
		this.SetValByKey(MethodAttr.FrmID, value);
	}
	/** 
	 方法ID
	 * @throws Exception 
	*/
	public final String getMethodID() throws Exception
	{
		return this.GetValStringByKey(MethodAttr.MethodID);
	}
	public final void setMethodID(String value) throws Exception
	{
		this.SetValByKey(MethodAttr.MethodID, value);
	}
	/** 
	 方法名
	 * @throws Exception 
	*/
	public final String getMethodName() throws Exception
	{
		return this.GetValStringByKey(MethodAttr.MethodName);
	}
	public final void setMethodName(String value) throws Exception
	{
		this.SetValByKey(MethodAttr.MethodName, value);
	}
	public final String getMsgErr() throws Exception
	{
		return this.GetValStringByKey(MethodAttr.MsgErr);
	}
	public final void setMsgErr(String value) throws Exception
	{
		this.SetValByKey(MethodAttr.MsgErr, value);
	}
	public final String getMsgSuccess() throws Exception
	{
		return this.GetValStringByKey(MethodAttr.MsgSuccess);
	}
	public final void setMsgSuccess(String value) throws Exception
	{
		this.SetValByKey(MethodAttr.MsgSuccess, value);
	}


	public final String getMethodDoc_Url() throws Exception
	{
		String s = this.GetValStringByKey(MethodAttr.MethodDoc_Url);
		if (DataType.IsNullOrEmpty(s) == true)
		{
			s = "http://192.168.0.100/MyPath/xxx.xx";
		}
		return s;
	}
	public final void setMethodDoc_Url(String value) throws Exception
	{
		this.SetValByKey(MethodAttr.MethodDoc_Url, value);
	}
	/** 
	 获得或者设置sql脚本.
	 * @throws Exception 
	*/
	public final String getMethodDoc_SQL() throws Exception
	{
		String strs = this.GetBigTextFromDB("SQLScript");
		if(DataType.IsNullOrEmpty(strs) == true)
		{
			return this.getMethodDoc_SQL_Demo(); //返回默认信息.
		}
		return strs;
	}
	public final void setMethodDoc_SQL(String value) throws Exception
	{
		this.SaveBigTxtToDB("SQLScript", value);
	}
	/** 
	 获得该实体的demo.
	 * @throws Exception 
	*/
	public final String getMethodDoc_JavaScript_Demo() throws Exception
	{
		String file = SystemConfig.getCCFlowAppPath() + "WF/CCBill/Admin/MethodDocDemoJS.txt";
		String doc = DataType.ReadTextFile(file); //读取文件.
		doc = doc.replace("/#", "+"); //为什么？
		doc = doc.replace("/$", "-"); //为什么？

		doc = doc.replace("@FrmID", this.getFrmID());

		return doc;
	}
	public final String getMethodDoc_SQL_Demo() throws Exception
	{
		String file = SystemConfig.getCCFlowAppPath() + "WF/CCBill/Admin/MethodDocDemoSQL.txt";
		String doc = DataType.ReadTextFile(file); //读取文件.
		doc = doc.replace("@FrmID", this.getFrmID());
		return doc;
	}
	/** 
	 获得JS脚本.
	 
	 @return 
	 * @throws Exception 
	*/
	public final String Gener_MethodDoc_JavaScript() throws Exception
	{
		return this.getMethodDoc_JavaScript();
	}

	public final String Gener_MethodDoc_JavaScript_function() throws Exception
	{
		String paras = "";
		MapAttrs attrs = new MapAttrs(this.getMyPK());
		for (MapAttr item : attrs.ToJavaList())
		{
			paras += item.getKeyOfEn() + ",";
		}
		if (attrs.size() > 1)
		{
			paras = paras.substring(0, paras.length() - 1);
		}

		String strs = " function " + this.getMethodID() + "(" + paras + ") {";
		strs += this.getMethodDoc_JavaScript();
		strs += "}";
		return strs;
	}
	/** 
	 获得SQL脚本
	 
	 @return 
	 * @throws Exception 
	*/
	public final String Gener_MethodDoc_SQL() throws Exception
	{
		return this.getMethodDoc_SQL();
	}
	/** 
	 获得或者设置js脚本.
	 * @throws Exception 
	*/
	public final String getMethodDoc_JavaScript() throws Exception
	{
		String strs = this.GetBigTextFromDB("JSScript");
		if (DataType.IsNullOrEmpty(strs) == true)
		{
			return this.getMethodDoc_JavaScript_Demo();
		}

		strs = strs.replace("/#", "+");
		strs = strs.replace("/$", "-");
		return strs;
	}
	public final void setMethodDoc_JavaScript(String value) throws Exception
	{

		this.SaveBigTxtToDB("JSScript", value);

	}

	/** 
	 方法类型：@0=SQL@1=URL@2=JavaScript@3=业务单元
	 * @throws Exception 
	*/
	public final int getMethodDocTypeOfFunc() throws Exception
	{
		return this.GetValIntByKey(MethodAttr.MethodDocTypeOfFunc);
	}
	public final void setMethodDocTypeOfFunc(int value) throws Exception
	{
		this.SetValByKey(MethodAttr.MethodDocTypeOfFunc, value);
	}
	/** 
	 方法类型
	 * @throws Exception 
	*/
	public final RefMethodType getRefMethodType() throws Exception
	{
		return RefMethodType.forValue(this.GetValIntByKey(MethodAttr.RefMethodType));
	}
	public final void setRefMethodType(RefMethodType value) throws Exception
	{
		this.SetValByKey(MethodAttr.RefMethodType, value.getValue());
	}

		///


		///构造方法
	/** 
	 权限控制
	*/
	@Override
	public UAC getHisUAC() throws Exception
	{
		UAC uac = new UAC();
		if (WebUser.getIsAdmin())
		{
			uac.IsUpdate = true;
			return uac;
		}
		return super.getHisUAC();
	}
	/** 
	 功能执行
	*/
	public MethodFunc()
	{
	}
	public MethodFunc(String mypk) throws Exception
	{
		this.setMyPK(mypk);
		this.Retrieve();
	}
	/** 
	 重写基类方法
	*/
	@Override
	public Map getEnMap() throws Exception
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("Frm_Method", "功能方法");
		map.AddMyPK();

		map.AddTBString(MethodAttr.FrmID, null, "表单ID", true, true, 0, 300, 10);
		map.AddTBString(MethodAttr.MethodName, null, "方法名", true, false, 0, 300, 10, true);
		map.AddTBString(MethodAttr.MethodID, null, "方法ID", true, true, 0, 300, 10);

		map.AddDDLSysEnum(MethodAttr.WhatAreYouTodo, 0, "执行完毕后干啥？", true, true, MethodAttr.WhatAreYouTodo, "@0=关闭提示窗口@1=关闭提示窗口并刷新@2=转入到Search.htm页面上去");

		map.AddTBString(MethodAttr.WarningMsg, null, "功能执行警告信息", true, false, 0, 300, 10, true);
		map.AddDDLSysEnum(MethodAttr.ShowModel, 0, "显示方式", true, true, MethodAttr.ShowModel, "@0=按钮@1=超链接");

		map.AddDDLSysEnum(MethodAttr.MethodDocTypeOfFunc, 0, "内容类型", true, false, "MethodDocTypeOfFunc", "@0=SQL@1=URL@2=JavaScript@3=业务单元");

		map.AddTBString(MethodAttr.MethodDoc_Url, null, "URL执行内容", false, false, 0, 300, 10);
		map.AddTBString(MethodAttr.MsgSuccess, null, "成功提示信息", true, false, 0, 300, 10, true);
		map.AddTBString(MethodAttr.MsgErr, null, "失败提示信息", true, false, 0, 300, 10, true);


			///外观.
		map.AddTBInt(MethodAttr.PopHeight, 100, "弹窗高度", true, false);
		map.AddTBInt(MethodAttr.PopWidth, 260, "弹窗宽度", true, false);

			/// 外观.



			///显示位置控制.
		map.AddBoolean(MethodAttr.IsMyBillToolBar, true, "是否显示在MyBill.htm工具栏上", true, true, true);
		map.AddBoolean(MethodAttr.IsMyBillToolExt, false, "是否显示在MyBill.htm工具栏右边的更多按钮里", true, true, true);
		map.AddBoolean(MethodAttr.IsSearchBar, false, "是否显示在Search.htm工具栏上(用于批处理)", true, true, true);

			/// 显示位置控制.

		RefMethod rm = new RefMethod();
		rm.Title = "方法参数"; // "设计表单";
		rm.ClassMethodName = this.toString() + ".DoParas";
		rm.Visable = true;
		rm.refMethodType = getRefMethodType().RightFrameOpen;
		rm.Target = "_blank";
			//rm.GroupName = "开发接口";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "方法内容"; // "设计表单";
		rm.ClassMethodName = this.toString() + ".DoDocs";
		rm.Visable = true;
		rm.refMethodType = getRefMethodType().RightFrameOpen;
		rm.Target = "_blank";
			//rm.GroupName = "开发接口";
		map.AddRefMethod(rm);


		this.set_enMap(map);
		return this.get_enMap();
	}

		///


		///执行方法.
	/** 
	 方法参数
	 
	 @return 
	 * @throws Exception 
	*/
	public final String DoParas() throws Exception
	{
		return "../../CCBill/Admin/MethodParas.htm?MyPK=" + this.getMyPK();
	}
	/** 
	 方法内容
	 
	 @return 
	 * @throws Exception 
	*/
	public final String DoDocs() throws Exception
	{
		return "../../CCBill/Admin/MethodDoc.htm?MyPK=" + this.getMyPK();
	}

		/// 执行方法.

}