package bp.gpm.home.windowext;

import bp.en.EntityNoName;
import bp.en.Map;
import bp.en.RefMethod;
import bp.en.RefMethodType;
import bp.en.UAC;
import bp.gpm.home.WindowTemplateAttr;
import bp.web.WebUser;

/** 
标签页
*/
public class Tab extends EntityNoName
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#region 权限控制.
	/** 
	 控制权限
	 * @throws Exception 
	*/
	@Override
	public UAC getHisUAC() throws Exception
	{
		UAC uac = new UAC();
		if (WebUser.getIsAdmin() == true)
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
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#endregion 权限控制.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#region 属性
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#endregion 属性

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#region 构造方法
	/** 
	 标签页
	*/
	public Tab()
	{
	}
	/** 
	 标签页
	 
	 @param no
	 * @throws Exception 
	*/
	public Tab(String no) throws Exception
	{
		this.setNo(no);
		this.Retrieve();
	}
	/** 
	 EnMap
	 * @throws Exception 
	*/
	@Override
	public Map getEnMap() throws Exception
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("GPM_WindowTemplate", "Tab标签页");

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 基本信息.
		map.AddTBStringPK(WindowTemplateAttr.No, null, "编号", true, true, 1, 40, 200);
		map.AddTBInt(WindowTemplateAttr.ColSpan, 2, "占的列数", true, false);
		map.SetHelperAlert(WindowTemplateAttr.ColSpan, "画布按照4列划分布局，输入的输在在1=4之间.");
		map.AddTBString(WindowTemplateAttr.Name, null, "标题", true, false, 0, 300, 20, true);
		map.AddTBString(WindowTemplateAttr.Icon, null, "Icon", true, false, 0, 100, 20, true);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 基本信息.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 更多链接.
		map.AddTBString(WindowTemplateAttr.MoreUrl, null, "更多链接", true, false, 0, 300, 20, true);
		map.AddDDLSysEnum(WindowTemplateAttr.MoreLinkModel, 0, "打开方式", true, true, WindowTemplateAttr.MoreLinkModel, "@0=新窗口@1=本窗口@2=覆盖新窗口");
		map.AddTBString(WindowTemplateAttr.MoreLab, null, "更多标签", true, false, 0, 300, 20);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 更多链接.

		map.AddDtl(new TabDtls(), DtlAttr.RefWindowTemplate);

		RefMethod rm = new RefMethod();
		rm.Title = "样例";
		rm.refMethodType=RefMethodType.RightFrameOpen;
		rm.ClassMethodName = this.toString() + ".AddTemplate()";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "数据源";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.ClassMethodName = this.toString() + ".AddDBSrc()";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "数据源参考";
		rm.refMethodType=RefMethodType.RightFrameOpen;
		rm.ClassMethodName = this.toString() + ".RefSQL()";
		map.AddRefMethod(rm);

		this.set_enMap(map);
		return this.get_enMap();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#region 方法.
	public final String AddTemplate()
	{
		return "../../GPM/Window/Tab.png";
	}
	public final String RefSQL()
	{
		return "../../GPM/Window/RefSQL.htm";
	}
	public final String AddDBSrc()
	{
		return "../../Comm/Search.htm?EnsName=BP.Sys.SFDBSrcs";
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#endregion 方法.

}

