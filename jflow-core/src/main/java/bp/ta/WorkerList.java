package bp.ta;


import bp.en.EntityMyPK;
import bp.en.Map;

/**
 工作人员
*/
public class WorkerList extends EntityMyPK
{
		///#region 属性
	public final int getTaskID()
	{
		return this.GetValIntByKey(WorkerListAttr.TaskID);
	}
	public final void setTaskID(int value)
	{
		this.SetValByKey(WorkerListAttr.TaskID, value);
	}
	public final int getPTaskID()
	{
		return this.GetValIntByKey(WorkerListAttr.PTaskID);
	}
	public final void setPTaskID(int value)
	{
		this.SetValByKey(WorkerListAttr.PTaskID, value);
	}
	public final String getPrjNo()
	{
		return this.GetValStringByKey(WorkerListAttr.PrjNo);
	}
	public final void setPrjNo(String value)
	{
		this.SetValByKey(WorkerListAttr.PrjNo, value);
	}
	public final String getPrjName()
	{
		return this.GetValStringByKey(WorkerListAttr.PrjName);
	}
	public final void setPrjName(String value)
	{
		this.SetValByKey(WorkerListAttr.PrjName, value);
	}
	public final String getEmpNo()
	{
		return this.GetValStringByKey(WorkerListAttr.EmpNo);
	}
	public final void setEmpNo(String value)
	{
		this.SetValByKey(WorkerListAttr.EmpNo, value);
	}
	public final String getEmpName()
	{
		return this.GetValStringByKey(WorkerListAttr.EmpName);
	}
	public final void setEmpName(String value)
	{
		this.SetValByKey(WorkerListAttr.EmpName, value);
	}
	public final String getSenderName()
	{
		return this.GetValStringByKey(WorkerListAttr.SenderName);
	}
	public final void setSenderName(String value)
	{
		this.SetValByKey(WorkerListAttr.SenderName, value);
	}
	public final String getSenderNo()
	{
		return this.GetValStringByKey(WorkerListAttr.SenderNo);
	}
	public final void setSenderNo(String value)
	{
		this.SetValByKey(WorkerListAttr.SenderNo, value);
	}
	public final String getDocs()
	{
		return this.GetValStringByKey(WorkerListAttr.Docs);
	}
	public final void setDocs(String value)
	{
		this.SetValByKey(WorkerListAttr.Docs, value);
	}
	public final String getRDT()
	{
		return this.GetValStringByKey(WorkerListAttr.RDT);
	}
	public final void setRDT(String value)
	{
		this.SetValByKey(WorkerListAttr.RDT, value);
	}
	public final String getADT()
	{
		return this.GetValStringByKey(WorkerListAttr.ADT);
	}
	public final void setADT(String value)
	{
		this.SetValByKey(WorkerListAttr.ADT, value);
	}
		///#endregion 属性

		///#region 构造函数
	/** 
	 工作人员
	*/
	public WorkerList()
	{
	}
	/** 
	 工作人员
	 
	 @param mypk
	*/
	public WorkerList(String mypk) throws Exception {
		this.setMyPK(mypk);
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

		Map map = new Map("TA_WorkerList", "工作人员");

		map.AddGroupAttr("工作信息", "");
		map.AddMyPK(true); //TaskID+"_"+EmpNo
		map.AddTBInt(WorkerListAttr.TaskID, 0, "任务ID", true, true);
		map.AddTBString(WorkerListAttr.EmpNo, null, "处理人", true, false, 0, 50, 300);
		map.AddTBString(WorkerListAttr.EmpName, null, "处理人", true, false, 0, 50, 300);

		map.AddTBInt(WorkerListAttr.IsPass, 0, "是否完成?", true, true);
		map.AddTBInt(WorkerListAttr.IsRead, 0, "是否读取?", true, true);
		map.AddTBInt(WorkerListAttr.WCL, 0, "完成率", true, true);
		map.AddTBString(WorkerListAttr.TaskDoc, null, "工作内容", true, false, 0, 50, 300);

		map.AddGroupAttr("任务信息", "");
		map.AddTBString(WorkerListAttr.Title, null, "标题", true, false, 0, 50, 300);
		map.AddTBString(WorkerListAttr.TaskDesc, null, "任务描述", true, false, 0, 50, 300);
		map.AddTBString(WorkerListAttr.SenderNo, null, "发起人编号", true, false, 0, 50, 300);
		map.AddTBString(WorkerListAttr.SenderName, null, "发起人名称", true, false, 0, 50, 300);
		map.AddTBDateTime(WorkerListAttr.RDT, null, "记录日期", true, true);
		map.AddTBDateTime(WorkerListAttr.ADT, null, "活动日期", true, true);

		map.AddTBInt(WorkerListAttr.PTaskID, 0, "P任务ID", true, true);

		map.AddGroupAttr("项目信息", "");
		map.AddTBString(WorkerListAttr.PrjNo, null, "项目编号", true, false, 0, 50, 300);
		map.AddTBString(WorkerListAttr.PrjName, null, "名称", true, false, 0, 50, 300);
		map.AddTBString(WorkerListAttr.StarterNo, null, "发起人编号", true, false, 0, 50, 300);
		map.AddTBString(WorkerListAttr.StarterName, null, "发起人名称", true, false, 0, 50, 300);
		map.AddTBInt(WorkerListAttr.PRI, 0, "优先级", true, true);

		this.set_enMap(map);
		return this.get_enMap();
	}
		///#endregion
}
