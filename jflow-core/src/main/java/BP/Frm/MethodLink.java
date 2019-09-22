package BP.Frm;

import BP.DA.*;
import BP.Web.*;
import BP.En.*;
import BP.Port.*;
import BP.Sys.*;
import java.util.*;

/** 
 连接方法
*/
public class MethodLink extends EntityMyPK
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
	 连接方法
	*/
	public MethodLink()
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


		Map map = new Map("Frm_Method", "连接");

		map.AddMyPK();

		map.AddTBString(MethodAttr.FrmID, null, "表单ID", true, true, 0, 300, 10);
		map.AddTBString(MethodAttr.MethodID, null, "方法ID", true, true, 0, 300, 10);
		map.AddDDLSysEnum(MethodAttr.RefMethodType, 0, "方法类型", true, true, "RefMethodTypeLink", "@1=模态窗口打开@2=新窗口打开@3=右侧窗口打开");

		map.AddDDLSysEnum(MethodAttr.ShowModel, 0, "显示方式", true, true, MethodAttr.ShowModel, "@0=按钮@1=超链接");

		map.AddTBString(MethodAttr.MethodName, null, "方法名", true, false, 0, 300, 10, true);
		map.AddTBStringDoc(MethodAttr.MethodDoc_Url, null, "连接URL", true, false);

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 工具栏.
		map.AddBoolean(MethodAttr.IsMyBillToolBar, true, "是否显示在MyBill.htm工具栏上", true, true, true);
		map.AddBoolean(MethodAttr.IsMyBillToolExt, false, "是否显示在MyBill.htm工具栏右边的更多按钮里", true, true, true);
		map.AddBoolean(MethodAttr.IsSearchBar, false, "是否显示在Search.htm工具栏上(用于批处理)", true, true, true);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 工具栏.

		this._enMap = map;
		return this._enMap;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion
}