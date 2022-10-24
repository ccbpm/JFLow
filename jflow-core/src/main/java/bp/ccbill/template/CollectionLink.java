package bp.ccbill.template;

import bp.da.*;
import bp.web.*;
import bp.en.*;

/** 
 连接方法
*/
public class CollectionLink extends EntityNoName
{

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

		///#endregion


		///#region 构造方法
	@Override
	public UAC getHisUAC()  {
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
	public CollectionLink()  {
	}
	/** 
	 重写基类方法
	*/
	@Override
	public bp.en.Map getEnMap()  {
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("Frm_Collection", "列表连接");

			//主键.
		map.AddTBStringPK(MethodAttr.No, null, "编号", true, true, 0, 50, 10);
		map.AddTBString(MethodAttr.Name, null, "链接标签", true, false, 0, 300, 10);
		map.AddTBString(MethodAttr.MethodID, null, "方法ID", false, true, 0, 300, 10);

			//功能标记.
		map.AddTBString(MethodAttr.MethodModel, null, "方法模式", false, false, 0, 300, 10);
		map.AddTBString(MethodAttr.Tag1, null, "链接地址", true, false, 0, 300, 10, true);
		map.AddTBString(MethodAttr.Icon, null, "图标", true, false, 0, 50, 10, true);
		map.AddDDLSysEnum(MethodAttr.ShowModel, 0, "显示方式", false, false, MethodAttr.ShowModel, "@0=按钮@1=超链接");

		map.AddDDLSysEnum(MethodAttr.RefMethodType, 0, "页面打开方式", true, true, "RefMethodTypeLink", "@0=模态窗口打开@1=新窗口打开@2=右侧窗口打开@4=转到新页面");

		map.AddTBInt(MethodAttr.PopWidth, 500, "宽度", true, false);
		map.AddTBInt(MethodAttr.PopHeight, 700, "高度", true, false);


		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion

	@Override
	protected boolean beforeInsert() throws Exception {
		if (DataType.IsNullOrEmpty(this.getNo()) == true)
		{
			this.setNo(DBAccess.GenerGUID(0, null, null));
		}
		return super.beforeInsert();
	}
}