package BP.Frm;

import BP.DA.*;
import BP.Web.*;
import BP.En.*;
import BP.En.Map;
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
	 * @throws Exception 
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
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
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
		this.set_enMap(map);
		return this.get_enMap();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

	public final void DoUp() throws Exception
	{
		this.DoOrderUp(MethodAttr.FrmID, this.getFrmID(), MethodAttr.Idx);
	}
	public final void DoDown() throws Exception
	{
		this.DoOrderDown(MethodAttr.FrmID, this.getFrmID(), MethodAttr.Idx);
	}


	@Override
	protected boolean beforeUpdateInsertAction() throws Exception
	{
		return super.beforeUpdateInsertAction();
	}

}