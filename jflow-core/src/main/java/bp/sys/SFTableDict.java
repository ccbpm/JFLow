package bp.sys;
import bp.difference.SystemConfig;
import bp.en.*;
import bp.en.Map;

/** 
 系统字典表
*/
public class SFTableDict extends EntityNoName
{

	private static final long serialVersionUID = 1L;
		///构造方法
	@Override
	public UAC getHisUAC() throws Exception
	{
		UAC uac = new UAC();
		uac.OpenForSysAdmin();
		uac.IsInsert = false;
		return uac;
	}
	/** 
	 系统字典表
	*/
	public SFTableDict()
	{
	}
	/** 
	 EnMap
	*/
	@Override
	public Map getEnMap() throws Exception
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("Sys_SFTable", "系统字典表");

		map.AddTBStringPK(SFTableAttr.No, null, "编号", true, true, 1, 200, 20);
		map.AddTBString(SFTableAttr.Name, null, "名称", true, false, 0, 200, 20);
			//map.AddBoolean(SFTableAttr.IsAutoGenerNo, true, "是否自动生成编号", true, true);
		map.AddDDLSysEnum(SFTableAttr.NoGenerModel, 1, "编号生成规则", true, true, SFTableAttr.NoGenerModel, "@0=自定义@1=流水号@2=标签的全拼@3=标签的简拼@4=按GUID生成");

		map.AddDDLSysEnum(SFTableAttr.CodeStruct, 0, "字典表类型", true, false, SFTableAttr.CodeStruct);
		map.AddDDLSysEnum(SFTableAttr.SrcType, 0, "数据表类型", true, false, SFTableAttr.SrcType, "@0=本地的类@1=创建表@2=表或视图@3=SQL查询表@4=WebServices@5=微服务Handler外部数据源@6=JavaScript外部数据源@7=系统字典表");


		RefMethod rm = new RefMethod();
		rm.Title = "编辑数据";
		rm.ClassMethodName = this.toString() + ".DoEdit";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.IsForEns = false;
		map.AddRefMethod(rm);
		this.set_enMap(map);
		return this.get_enMap();
	}

		///

	/** 
	 编辑数据
	 
	 @return 
	 * @throws Exception 
	*/
	public final String DoEdit() throws Exception
	{
		return SystemConfig.getCCFlowWebPath() + "WF/Admin/FoolFormDesigner/SFTableEditData.htm?FK_SFTable=" + this.getNo() + "&&QueryType=Dict";
	}
	/** 
	 删除之前要做的工作
	 
	 @return 
	 * @throws Exception 
	*/
	@Override
	protected boolean beforeDelete() throws Exception
	{
		MapAttrs attrs = new MapAttrs();
		attrs.Retrieve(MapAttrAttr.UIBindKey, this.getNo());
		if (attrs.size() != 0)
		{
			String err = "";
			for (MapAttr item : attrs.ToJavaList())
			{
				err += " @ " + item.getMyPK() + " " + item.getName();
			}
			throw new RuntimeException("@如下实体字段在引用:" + err + "。您不能删除该表。");
		}
		return super.beforeDelete();
	}
}