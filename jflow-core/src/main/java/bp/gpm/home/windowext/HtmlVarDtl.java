package bp.gpm.home.windowext;

import bp.da.DBAccess;
import bp.en.EntityMyPK;
import bp.en.Map;
import bp.en.UAC;
import bp.sys.SFDBSrcs;

/** 
变量信息
*/
public class HtmlVarDtl extends EntityMyPK
{
	/** 
	 表达式
	 * @throws Exception 
	*/
	public final String getExp0() throws Exception
	{
		return this.GetValStrByKey(DtlAttr.Exp0);
	}
	public final void setExp0(String value) throws Exception
	{
		this.SetValByKey(DtlAttr.Exp0,value);
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#region 权限控制.
	/** 
	 控制权限
	*/
	@Override
	public UAC getHisUAC()
	{
		UAC uac = new UAC();

		uac.IsInsert = true;
		uac.IsDelete = true;
		uac.IsView = true;
		uac.IsUpdate = true;
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
	public HtmlVarDtl()
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

		Map map = new Map("GPM_WindowTemplateDtl", "数据项");

		map.AddMyPK(false);
		map.AddTBString(DtlAttr.RefWindowTemplate, null, "RefWindowTemplate", false, false, 0, 40, 20, false);

		map.AddTBString(DtlAttr.Name, null, "标签", true, false, 0, 300, 70, true);

		map.AddDDLSysEnum(DtlAttr.DBType, 0, "数据源类型", true, true, "WindowsDBType", "@0=数据库查询SQL@1=执行Url返回Json@2=执行\\DataUser\\JSLab\\Windows.js的函数.");
		map.AddDDLEntities(DtlAttr.DBSrc, null, "数据源", new SFDBSrcs(), true);

		map.AddTBString(DtlAttr.Exp0, null, "表达式", true, false, 0, 300, 200, true);

		map.AddTBString(DtlAttr.FontColor, null, "字体风格", true, false, 0, 300, 20, true);

		this.set_enMap(map);
		return this.get_enMap();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#endregion

	@Override
	protected boolean beforeInsert() throws Exception
	{
		this.setMyPK(DBAccess.GenerGUID());
		return super.beforeInsert();
	}

}
