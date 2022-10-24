package bp.ccfast.portal.windowext;

import bp.en.*;
import bp.ccfast.portal.*;

/** 
 框架信息块
*/
public class iFrame extends EntityNoName
{

		///#region 权限控制.
	/** 
	 控制权限
	*/
	@Override
	public UAC getHisUAC()  {
		UAC uac = new UAC();
		if (bp.web.WebUser.getIsAdmin() == true)
		{
			uac.OpenAll();
		}
		else
		{
			uac.IsView = false;
		}

		uac.IsInsert = false;
		uac.IsDelete = false;
		return uac;
	}

		///#endregion 权限控制.


		///#region 属性

		///#endregion 属性


		///#region 构造方法
	/** 
	 框架信息块
	*/
	public iFrame()  {
	}
	/** 
	 框架信息块
	 
	 param no
	*/
	public iFrame(String no)
	{
		this.setNo(no);
		try {
			this.Retrieve();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/** 
	 EnMap
	*/
	@Override
	public bp.en.Map getEnMap() {
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("GPM_WindowTemplate", "iFrame框架信息块");


			///#region 基本信息.
		map.AddTBStringPK(WindowTemplateAttr.No, null, "编号", true, true, 1, 40, 200);
		map.AddTBInt(WindowTemplateAttr.ColSpan, 1, "占的列数", true, false);
		map.SetHelperAlert(WindowTemplateAttr.ColSpan, "画布按照4列划分布局，输入的输在在1=4之间.");
		map.AddTBString(WindowTemplateAttr.Name, null, "标题", true, false, 0, 300, 20, true);
		map.AddTBString(WindowTemplateAttr.Icon, null, "Icon", true, false, 0, 100, 20, true);

			///#endregion 基本信息.

		map.AddTBString(WindowTemplateAttr.Docs, null, "Url", true, false, 0, 100, 20, true);

		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion

	public final String AddTemplate()  {
		return "../../GPM/Window/iFrame.png";
	}
}