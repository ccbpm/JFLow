package BP.Frm;

import BP.DA.*;
import BP.Web.*;
import BP.En.*;
import BP.Port.*;
import BP.Sys.*;
import java.util.*;

/** 
 表单方法
*/
public class Method extends EntityMyPK
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
	 表单方法
	*/
	public Method()
	{
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

		Map map = new Map("Frm_Method", "表单方法");

		map.AddMyPK();

		map.AddTBString(MethodAttr.FrmID, null, "表单ID", true, false, 0, 300, 10);
		map.AddTBString(MethodAttr.MethodID, null, "方法ID", true, false, 0, 300, 10);
		map.AddTBString(MethodAttr.MethodName, null, "方法名", true, false, 0, 300, 10);
		map.AddTBString(MethodAttr.WarningMsg, null, "功能执行警告信息", true, false, 0, 300, 10);

		map.AddDDLSysEnum(MethodAttr.RefMethodType, 0, "方法类型", true, false, MethodAttr.RefMethodType, "@0=功能@1=模态窗口打开@2=新窗口打开@3=右侧窗口打开@4=实体集合的功能");

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 显示位置控制.
		map.AddBoolean(MethodAttr.IsMyBillToolBar, true, "是否显示在MyBill.htm工具栏上", true, true, true);
		map.AddBoolean(MethodAttr.IsMyBillToolExt, false, "是否显示在MyBill.htm工具栏右边的更多按钮里", true, true, true);
		map.AddBoolean(MethodAttr.IsSearchBar, false, "是否显示在Search.htm工具栏上(用于批处理)", true, true, true);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 显示位置控制.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 外观.
		map.AddTBInt(MethodAttr.PopHeight, 0, "弹窗高度", true, false);
		map.AddTBInt(MethodAttr.PopWidth, 0, "弹窗宽度", true, false);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 外观.


			//对功能有效.
		map.AddTBString(MethodAttr.MsgSuccess, null, "成功提示信息", true, false, 0, 300, 10, true);
		map.AddTBString(MethodAttr.MsgErr, null, "失败提示信息", true, false, 0, 300, 10, true);
		map.AddDDLSysEnum(MethodAttr.WhatAreYouTodo, 0, "执行完毕后干啥？", true, true, MethodAttr.WhatAreYouTodo, "@0=关闭提示窗口@1=关闭提示窗口并刷新@2=转入到Search.htm页面上去");

		map.AddTBInt(MethodAttr.Idx, 0, "Idx", true, false);
		this._enMap = map;
		return this._enMap;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

	public final void DoUp()
	{
		this.DoOrderUp(MethodAttr.FrmID, this.getFrmID(), MethodAttr.Idx);
	}
	public final void DoDown()
	{
		this.DoOrderDown(MethodAttr.FrmID, this.getFrmID(), MethodAttr.Idx);
	}


	@Override
	protected boolean beforeUpdateInsertAction()
	{
		return super.beforeUpdateInsertAction();
	}

}