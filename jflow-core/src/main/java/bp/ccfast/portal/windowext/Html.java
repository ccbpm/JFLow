package bp.ccfast.portal.windowext;

import bp.en.*;
import bp.ccfast.portal.*;

/** 
 信息块
*/
public class Html extends EntityNoName
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
	 信息块
	*/
	public Html() {
	}
	/** 
	 信息块
	 
	 param no
	*/
	public Html(String no)
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

		Map map = new Map("GPM_WindowTemplate", "Html信息块");


			///#region 基本信息.
		map.AddTBStringPK(WindowTemplateAttr.No, null, "编号", true, true, 1, 40, 200);
		map.AddTBInt(WindowTemplateAttr.ColSpan, 1, "占的列数", true, false);
		map.SetHelperAlert(WindowTemplateAttr.ColSpan, "画布按照4列划分布局，输入的输在在1=4之间.");
		map.AddTBString(WindowTemplateAttr.Name, null, "标题", true, false, 0, 300, 20, true);
		map.AddTBString(WindowTemplateAttr.Icon, null, "Icon", true, false, 0, 100, 20, true);

			///#endregion 基本信息.


			///#region 更多.
		map.AddTBString(WindowTemplateAttr.MoreUrl, null, "更多链接", true, false, 0, 300, 20, true);
		map.AddDDLSysEnum(WindowTemplateAttr.MoreLinkModel, 0, "打开方式", true, true, WindowTemplateAttr.MoreLinkModel, "@0=新窗口@1=本窗口@2=覆盖新窗口");
		map.AddTBString(WindowTemplateAttr.MoreLab, null, "更多标签", true, false, 0, 300, 20);

			///#endregion 更多.

		map.AddTBStringDoc(WindowTemplateAttr.Docs, null, "内容表达式(允许写html代码)", true, false, true, 10);

		RefMethod rm = new RefMethod();
		rm.Title = "样例";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.ClassMethodName = this.toString() + ".AddTemplate()";
		map.AddRefMethod(rm);

		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion

	public final String AddTemplate()  {
		return "../../GPM/Window/Html.png";
	}
}