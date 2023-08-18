package bp.ta;

import bp.en.EntityNoName;
import bp.en.Map;


/** 
 項目
*/
public class Template extends EntityNoName
{
	///#region 属性.
	public final String getTaskModel()
	{
		return this.GetValStringByKey(TemplateAttr.TaskModel);
	}
	public final void setTaskModel(String value)
	{
		this.SetValByKey(TemplateAttr.TaskModel, value);
	}
	public final String getPrjDesc()
	{
		return this.GetValStringByKey(TemplateAttr.PrjDesc);
	}
	public final void setPrjDesc(String value)
	{
		this.SetValByKey(TemplateAttr.PrjDesc, value);
	}
	public final int getPRI()
	{
		return this.GetValIntByKey(TemplateAttr.PRI);
	}
	public final void setPRI(int value)
	{
		this.SetValByKey(TemplateAttr.PRI, value);
	}
	public final int getPrjSta()
	{
		return this.GetValIntByKey(TemplateAttr.PrjSta);
	}
	public final void setPrjSta(int value)
	{
		this.SetValByKey(TemplateAttr.PrjSta, value);
	}
	public final int getWCL()
	{
		return this.GetValIntByKey(TemplateAttr.WCL);
	}
	public final void setWCL(int value)
	{
		this.SetValByKey(TemplateAttr.WCL, value);
	}
	public final String getStarterNo()
	{
		return this.GetValStringByKey(TemplateAttr.StarterNo);
	}
	public final void setStarterNo(String value)
	{
		this.SetValByKey(TemplateAttr.StarterNo, value);
	}
	public final String getStarterName()
	{
		return this.GetValStringByKey(TemplateAttr.StarterName);
	}
	public final void setStarterName(String value)
	{
		this.SetValByKey(TemplateAttr.StarterName, value);
	}
	public final String getRDT()
	{
		return this.GetValStringByKey(TemplateAttr.RDT);
	}
	public final void setRDT(String value)
	{
		this.SetValByKey(TemplateAttr.RDT, value);
	}
		///#endregion 属性.

		///#region 构造函数
	/** 
	 項目设置
	*/
	public Template()
	{
	}
	/** 
	 項目设置
	 
	 @param nodeid
	*/
	public Template(String no) throws Exception {
		this.setNo(no);
		this.Retrieve();
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

		Map map = new Map("TA_Template", "項目");

		map.AddTBStringPK(TemplateAttr.No, null, "编号", true, true, 5, 5, 5);
		map.AddTBString(TemplateAttr.Name, null, "名称", true, true, 0, 100, 10);
		map.AddTBString(TemplateAttr.TaskModel, null, "模式", true, true, 0, 100, 10);

		map.AddTBStringDoc(TemplateAttr.PrjDesc, null, "项目描述", true, false, true, 10);

		map.AddTBInt(TemplateAttr.PrjSta, 0, "状态", true, true);
		map.AddTBInt(TemplateAttr.PRI, 0, "优先级", true, true);
		map.AddTBInt(TemplateAttr.WCL, 0, "完成率", true, true);

		map.AddTBString(TemplateAttr.StarterNo, null, "发起人", true, false, 0, 100, 10, true);
		map.AddTBString(TemplateAttr.StarterName, null, "名称", true, false, 0, 100, 10, true);
		map.AddTBDateTime(TemplateAttr.RDT, null, "发起日期", true, false);

		this.set_enMap(map);
		return this.get_enMap();
	}
		///#endregion
}
