package bp.ccbill.template;

import bp.da.*;
import bp.web.*;
import bp.en.*;
import bp.en.Map;
import bp.port.*;
import bp.sys.*;
import bp.ccbill.*;
import java.util.*;

/** 
 连接方法
*/
public class MethodLink extends EntityMyPK
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
	public final String getMethodDocUrl() throws Exception
	{
		String s = this.GetValStringByKey(MethodAttr.MethodDoc_Url);
		if (DataType.IsNullOrEmpty(s) == true)
		{
			s = "http://192.168.0.100/MyPath/xxx.xx";
		}
		return s;
	}
	public final void setMethodDocUrl(String value) throws Exception
	{
		this.SetValByKey(MethodAttr.MethodDoc_Url, value);
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
	 连接方法
	*/
	public MethodLink()
	{
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

		Map map = new Map("Frm_Method", "连接");

		map.AddMyPK();

		map.AddTBString(MethodAttr.FrmID, null, "表单ID", true, true, 0, 300, 10);
		map.AddTBString(MethodAttr.MethodID, null, "方法ID", true, true, 0, 200, 10);
		map.AddDDLSysEnum(MethodAttr.ShowModel, 0, "显示方式", true, true,MethodAttr.ShowModel,"@0=按钮@1=超链接");
		   // map.AddDDLSysEnum(MethodAttr.ExeType, 0, "执行类型", true, true, MethodAttr.ExeType, "@0=不带参数方法@1=带参数的方法@2=打开Url");

		map.AddDDLSysEnum(MethodAttr.RefMethodType, 0, "页面打开方式", true, true, "RefMethodTypeLink","@0=模态窗口打开@1=新窗口打开@2=右侧窗口打开@4=转到新页面");

		map.AddTBInt(MethodAttr.PopWidth, 500, "宽度", true, false);
		map.AddTBInt(MethodAttr.PopHeight, 700, "高度", true, false);

		map.AddTBString(MethodAttr.MethodName, null, "连接标签", true, false, 0, 200, 10, true);
		map.AddTBString(MethodAttr.MethodDoc_Url, null, "连接URL", true, false, 0, 300, 10);


			///工具栏.
		map.AddBoolean(MethodAttr.IsMyBillToolBar, true, "是否显示在MyBill.htm工具栏上", true, true, true);
		map.AddBoolean(MethodAttr.IsMyBillToolExt, false, "是否显示在MyBill.htm工具栏右边的更多按钮里", true, true, true);
		map.AddBoolean(MethodAttr.IsSearchBar, false, "是否显示在Search.htm工具栏上(用于批处理)", true, true, true);

			/// 工具栏.

		this.set_enMap(map);
		return this.get_enMap();
	}

		///
}