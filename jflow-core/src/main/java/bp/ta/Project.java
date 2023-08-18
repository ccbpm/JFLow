package bp.ta;

import bp.en.*;
import bp.en.EntityNoName;

/** 
 項目
*/
public class Project extends EntityNoName
{
//C# TO JAVA CONVERTER TASK: There is no preprocessor in Java:
		///#region 属性.
	public final String getMsg()
	{
		return this.GetValStringByKey(ProjectAttr.Msg);
	}
	public final void setMsg(String value)
	{
		this.SetValByKey(ProjectAttr.Msg, value);
	}
	public final String getPrjDesc()
	{
		return this.GetValStringByKey(ProjectAttr.PrjDesc);
	}
	public final void setPrjDesc(String value)
	{
		this.SetValByKey(ProjectAttr.PrjDesc, value);
	}
	public final int getPRI()
	{
		return this.GetValIntByKey(ProjectAttr.PRI);
	}
	public final void setPRI(int value)
	{
		this.SetValByKey(ProjectAttr.PRI, value);
	}
	public final int getPrjSta()
	{
		return this.GetValIntByKey(ProjectAttr.PrjSta);
	}
	public final void setPrjSta(int value)
	{
		this.SetValByKey(ProjectAttr.PrjSta, value);
	}
	public final int getWCL()
	{
		return this.GetValIntByKey(ProjectAttr.WCL);
	}
	public final void setWCL(int value)
	{
		this.SetValByKey(ProjectAttr.WCL, value);
	}
	public final String getStarterNo()
	{
		return this.GetValStringByKey(ProjectAttr.StarterNo);
	}
	public final void setStarterNo(String value)
	{
		this.SetValByKey(ProjectAttr.StarterNo, value);
	}
	public final String getStarterName()
	{
		return this.GetValStringByKey(ProjectAttr.StarterName);
	}
	public final void setStarterName(String value)
	{
		this.SetValByKey(ProjectAttr.StarterName, value);
	}
	public final String getRDT()
	{
		return this.GetValStringByKey(ProjectAttr.RDT);
	}
	public final void setRDT(String value)
	{
		this.SetValByKey(ProjectAttr.RDT, value);
	}
	public final String getTemplateNo()
	{
		return this.GetValStringByKey(ProjectAttr.TemplateNo);
	}
	public final void setTemplateNo(String value)
	{
		this.SetValByKey(ProjectAttr.TemplateNo, value);
	}
	public final String getTemplateName()
	{
		return this.GetValStringByKey(ProjectAttr.TemplateName);
	}
	public final void setTemplateName(String value)
	{
		this.SetValByKey(ProjectAttr.TemplateName, value);
	}
		///#endregion 属性.

		///#region 统计
	public final int getNumTasks()
	{
		return this.GetValIntByKey(ProjectAttr.NumTasks);
	}
	public final void setNumTasks(int value)
	{
		this.SetValByKey(ProjectAttr.NumTasks, value);
	}
	public final int getNumComplete()
	{
		return this.GetValIntByKey(ProjectAttr.NumComplete);
	}
	public final void setNumComplete(int value)
	{
		this.SetValByKey(ProjectAttr.NumComplete, value);
	}
		///#endregion 统计

		///#region 构造函数
	/** 
	 項目设置
	*/
	public Project()
	{
	}
	/** 
	 項目设置
	 
	 @param no
	*/
	public Project(String no) throws Exception {
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

		Map map = new Map("TA_Project", "項目");

		map.AddTBStringPK(ProjectAttr.No, null, "编号", true, true, 5, 5, 5);
		map.AddTBString(ProjectAttr.Name, null, "名称", true, true, 0, 100, 10);
		map.AddTBString(ProjectAttr.PrjDesc, null, "内容描述", true, true, 0, 100, 10);


		map.AddTBInt(ProjectAttr.PrjSta, 0, "状态", true, true);
		map.AddTBInt(ProjectAttr.PRI, 0, "优先级", true, true);
		map.AddTBInt(ProjectAttr.WCL, 0, "完成率", true, true);

		map.AddTBString(ProjectAttr.Msg, null, "消息", true, true, 0, 500, 10);


		map.AddTBString(ProjectAttr.StarterNo, null, "发起人", true, false, 0, 100, 10, true);
		map.AddTBString(ProjectAttr.StarterName, null, "名称", true, false, 0, 100, 10, true);
		map.AddTBDateTime(ProjectAttr.RDT, null, "发起日期", true, false);

		map.AddTBString(ProjectAttr.TemplateNo, null, "模板编号", true, true, 0, 100, 10);
		map.AddTBString(ProjectAttr.TemplateName, null, "模板名称", true, true, 0, 100, 10);

		map.AddTBInt(ProjectAttr.NumTasks, 0, "任务数", true, true);
	 //   map.AddTBInt(ProjectAttr.NumChecking, 0, "审核中", true, true);
		map.AddTBInt(ProjectAttr.NumComplete, 0, "已完成", true, true);

  //          { Key: 'NumTasks', Name: '任务数', IsShow: true, IsShowMobile: true, DataType: 2, width: 70 },
  //{ Key: 'NumChecking', Name: '待确认', IsShow: true, IsShowMobile: true, DataType: 2, width: 70 },
  //{ Key: 'NumComplete', Name: '完成数', IsShow: true, IsShowMobile: true, DataType: 2, width: 70 },

		this.set_enMap(map);
		return this.get_enMap();
	}
		///#endregion
}
