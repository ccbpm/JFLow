package BP.Frm;

import BP.DA.*;
import BP.Web.*;
import BP.En.*;
import BP.Port.*;
import BP.Sys.*;
import java.util.*;

/** 
 功能执行
*/
public class MethodFunc extends EntityMyPK
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 基本属性
	/** 
	 表单ID
	*/
	public final String getFrmID()
	{
		return this.GetValStringByKey(MethodAttr.FrmID);
	}
	public final void setFrmID(String value)
	{
		this.SetValByKey(MethodAttr.FrmID, value);
	}
	/** 
	 方法ID
	*/
	public final String getMethodID()
	{
		return this.GetValStringByKey(MethodAttr.MethodID);
	}
	public final void setMethodID(String value)
	{
		this.SetValByKey(MethodAttr.MethodID, value);
	}
	/** 
	 方法名
	*/
	public final String getMethodName()
	{
		return this.GetValStringByKey(MethodAttr.MethodName);
	}
	public final void setMethodName(String value)
	{
		this.SetValByKey(MethodAttr.MethodName, value);
	}
	public final String getMsgErr()
	{
		return this.GetValStringByKey(MethodAttr.MsgErr);
	}
	public final void setMsgErr(String value)
	{
		this.SetValByKey(MethodAttr.MsgErr, value);
	}
	public final String getMsgSuccess()
	{
		return this.GetValStringByKey(MethodAttr.MsgSuccess);
	}
	public final void setMsgSuccess(String value)
	{
		this.SetValByKey(MethodAttr.MsgSuccess, value);
	}


	public final String getMethodDoc_Url()
	{
		String s = this.GetValStringByKey(MethodAttr.MethodDoc_Url);
		if (DataType.IsNullOrEmpty(s) == true)
		{
			s = "http://192.168.0.100/MyPath/xxx.xx";
		}
		return s;
	}
	public final void setMethodDoc_Url(String value)
	{
		this.SetValByKey(MethodAttr.MethodDoc_Url, value);
	}
	/** 
	 获得或者设置sql脚本.
	*/
	public final String getMethodDoc_SQL()
	{
		String strs = this.GetBigTextFromDB("SQLScript");
		if (strs == null || strs.equals("") == true)
		{
			return this.getMethodDoc_SQL_Demo(); //返回默认信息.
		}
		return strs;
	}
	public final void setMethodDoc_SQL(String value)
	{
		this.SaveBigTxtToDB("SQLScript", value);
	}
	/** 
	 获得该实体的demo.
	*/
	public final String getMethodDoc_JavaScript_Demo()
	{
		String file = SystemConfig.CCFlowAppPath + "WF\\CCBill\\Admin\\MethodDocDemoJS.txt";
		String doc = DataType.ReadTextFile(file); //读取文件.
		doc = doc.replace("/#", "+"); //为什么？
		doc = doc.replace("/$", "-"); //为什么？

		doc = doc.replace("@FrmID", this.getFrmID());

		return doc;
	}
	public final String getMethodDoc_SQL_Demo()
	{
		String file = SystemConfig.CCFlowAppPath + "WF\\CCBill\\Admin\\MethodDocDemoSQL.txt";
		String doc = DataType.ReadTextFile(file); //读取文件.
		doc = doc.replace("@FrmID", this.getFrmID());
		return doc;
	}
	/** 
	 获得JS脚本.
	 
	 @return 
	*/
	public final String Gener_MethodDoc_JavaScript()
	{
		return this.getMethodDoc_JavaScript();
	}

	public final String Gener_MethodDoc_JavaScript_function()
	{
		String paras = "";
		MapAttrs attrs = new MapAttrs(this.MyPK);
		for (MapAttr item : attrs)
		{
			paras += item.KeyOfEn + ",";
		}
		if (attrs.Count > 1)
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
	*/
	public final String Gener_MethodDoc_SQL()
	{
		return this.getMethodDoc_SQL();
	}
	/** 
	 获得或者设置js脚本.
	*/
	public final String getMethodDoc_JavaScript()
	{
		String strs = this.GetBigTextFromDB("JSScript");
		if (strs == null || strs.equals("") == true)
		{
			return this.getMethodDoc_JavaScript_Demo();
		}

		strs = strs.replace("/#", "+");
		strs = strs.replace("/$", "-");
		return strs;
	}
	public final void setMethodDoc_JavaScript(String value)
	{

		this.SaveBigTxtToDB("JSScript", value);

	}

	/** 
	 方法类型：@0=SQL@1=URL@2=JavaScript@3=业务单元
	*/
	public final int getMethodDocTypeOfFunc()
	{
		return this.GetValIntByKey(MethodAttr.MethodDocTypeOfFunc);
	}
	public final void setMethodDocTypeOfFunc(int value)
	{
		this.SetValByKey(MethodAttr.MethodDocTypeOfFunc, value);
	}
	/** 
	 方法类型
	*/
	public final RefMethodType getRefMethodType()
	{
		return (RefMethodType)this.GetValIntByKey(MethodAttr.RefMethodType);
	}
	public final void setRefMethodType(RefMethodType value)
	{
		this.SetValByKey(MethodAttr.RefMethodType, (int)value);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造方法
	/** 
	 权限控制
	*/
	@Override
	public UAC getHisUAC()
	{
		UAC uac = new UAC();
		if (WebUser.IsAdmin)
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
	public MethodFunc(String mypk)
	{
		this.MyPK = mypk;
		this.Retrieve();
	}
	/** 
	 重写基类方法
	*/
	@Override
	public Map getEnMap()
	{
		if (this._enMap != null)
		{
			return this._enMap;
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

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 外观.
		map.AddTBInt(MethodAttr.PopHeight, 100, "弹窗高度", true, false);
		map.AddTBInt(MethodAttr.PopWidth, 260, "弹窗宽度", true, false);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 外观.


//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 显示位置控制.
		map.AddBoolean(MethodAttr.IsMyBillToolBar, true, "是否显示在MyBill.htm工具栏上", true, true, true);
		map.AddBoolean(MethodAttr.IsMyBillToolExt, false, "是否显示在MyBill.htm工具栏右边的更多按钮里", true, true, true);
		map.AddBoolean(MethodAttr.IsSearchBar, false, "是否显示在Search.htm工具栏上(用于批处理)", true, true, true);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 显示位置控制.

		RefMethod rm = new RefMethod();
		rm.Title = "方法参数"; // "设计表单";
		rm.ClassMethodName = this.toString() + ".DoParas";
		rm.Visable = true;
		rm.RefMethodType = getRefMethodType().RightFrameOpen;
		rm.Target = "_blank";
			//rm.GroupName = "开发接口";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "方法内容"; // "设计表单";
		rm.ClassMethodName = this.toString() + ".DoDocs";
		rm.Visable = true;
		rm.RefMethodType = getRefMethodType().RightFrameOpen;
		rm.Target = "_blank";
			//rm.GroupName = "开发接口";
		map.AddRefMethod(rm);


		this._enMap = map;
		return this._enMap;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

	/** 
	 方法参数
	 
	 @return 
	*/
	public final String DoParas()
	{
		return "../../CCBill/Admin/MethodParas.htm?MyPK=" + this.MyPK;
	}
	/** 
	 方法内容
	 
	 @return 
	*/
	public final String DoDocs()
	{
		return "../../CCBill/Admin/MethodDoc.htm?MyPK=" + this.MyPK;
	}
}