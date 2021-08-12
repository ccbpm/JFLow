package bp.gpm.home.windowext;

import bp.en.EntityMyPK;
import bp.en.Map;
import bp.en.UAC;
import bp.web.WebUser;

/** 
变量信息
*/
public class Dtl extends EntityMyPK
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
	 变量信息
	*/
	public Dtl()
	{
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

		Map map = new Map("GPM_WindowTemplateDtl", "Dtl变量信息");

		map.AddMyPK();
		map.AddTBString(DtlAttr.Name, null, "标签", true, false, 0, 300, 20, true);
		map.AddTBString(DtlAttr.FontColor, null, "颜色", true, false, 0, 300, 20, true);
		map.AddTBString(DtlAttr.Exp0, null, "表达式0", true, false, 0, 300, 20, true);
		map.AddTBString(DtlAttr.Exp1, null, "表达式1", true, false, 0, 300, 20, true);
		map.AddTBString(DtlAttr.DBSrc, null, "数据源", true, false, 0, 100, 20, true);

		map.AddTBInt(DtlAttr.WindowsShowType, 0, "显示类型", true, false);

		this.set_enMap(map);
		return this.get_enMap();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#endregion

}
