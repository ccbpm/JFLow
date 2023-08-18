package bp.ta;

import bp.en.EntityOID;
import bp.en.Map;

/** 
 任务
*/
public class Task extends EntityOID
{
//C# TO JAVA CONVERTER TASK: There is no preprocessor in Java:
		///#region 属性
	public final long getPTaskID()
	{
		return this.GetValInt64ByKey(TaskAttr.PTaskID);
	}
	public final void setPTaskID(long value)
	{
		this.SetValByKey(TaskAttr.PTaskID, value);
	}
	public final long getTaskID()
	{
		return this.GetValInt64ByKey(TaskAttr.OID);
	}
	public final void setTaskID(long value)
	{
		this.SetValByKey(TaskAttr.OID, value);
	}
	public final String getNodeName()
	{
		return this.GetValStringByKey(TaskAttr.NodeName);
	}
	public final void setNodeName(String value)
	{
		this.SetValByKey(TaskAttr.NodeName, value);
	}
	public final String getNodeNo()
	{
		return this.GetValStringByKey(TaskAttr.NodeNo);
	}
	public final void setNodeNo(String value)
	{
		this.SetValByKey(TaskAttr.NodeNo, value);
	}
	public final String getSenderNo()
	{
		return this.GetValStringByKey(TaskAttr.SenderNo);
	}
	public final void setSenderNo(String value)
	{
		this.SetValByKey(TaskAttr.SenderNo, value);
	}
	public final String getSenderName()
	{
		return this.GetValStringByKey(TaskAttr.SenderName);
	}
	public final void setSenderName(String value)
	{
		this.SetValByKey(TaskAttr.SenderName, value);
	}
	public final String getTitle()
	{
		return this.GetValStringByKey(TaskAttr.Title);
	}
	public final void setTitle(String value)
	{
		this.SetValByKey(TaskAttr.Title, value);
	}
	public final String getRDT()
	{
		return this.GetValStringByKey(TaskAttr.RDT);
	}
	public final void setRDT(String value)
	{
		this.SetValByKey(TaskAttr.RDT, value);
	}
	/** 
	 最近活动日期
	*/
	public final String getADT()
	{
		return this.GetValStringByKey(TaskAttr.ADT);
	}
	public final void setADT(String value)
	{
		this.SetValByKey(TaskAttr.ADT, value);
	}
	public final String getEmpNo()
	{
		return this.GetValStringByKey(TaskAttr.EmpNo);
	}
	public final void setEmpNo(String value)
	{
		this.SetValByKey(TaskAttr.EmpNo, value);
	}
	public final String getEmpName()
	{
		return this.GetValStringByKey(TaskAttr.EmpName);
	}
	public final void setEmpName(String value)
	{
		this.SetValByKey(TaskAttr.EmpName, value);
	}
	public final int getTaskSta()
	{
		return this.GetValIntByKey(TaskAttr.TaskSta);
	}
	public final void setTaskSta(int value)
	{
		this.SetValByKey(TaskAttr.TaskSta, value);
	}
	public final int getItIsRead()
	{
		return this.GetValIntByKey(TaskAttr.IsRead);
	}
	public final void setItIsRead(int value)
	{
		this.SetValByKey(TaskAttr.IsRead, value);
	}
	public final String getNowMsg()
	{
		return this.GetValStringByKey(TaskAttr.NowMsg);
	}
	public final void setNowMsg(String value)
	{
		this.SetValByKey(TaskAttr.NowMsg, value);
	}

	public final String getPrjNo()
	{
		return this.GetValStringByKey(TaskAttr.PrjNo);
	}
	public final void setPrjNo(String value)
	{
		this.SetValByKey(TaskAttr.PrjNo, value);
	}
	public final String getPrjName()
	{
		return this.GetValStringByKey(TaskAttr.PrjName);
	}
	public final void setPrjName(String value)
	{
		this.SetValByKey(TaskAttr.PrjName, value);
	}
	public final String getStarterNo()
	{
		return this.GetValStringByKey(TaskAttr.StarterNo);
	}
	public final void setStarterNo(String value)
	{
		this.SetValByKey(TaskAttr.StarterNo, value);
	}
	public final String getStarterName()
	{
		return this.GetValStringByKey(TaskAttr.StarterName);
	}
	public final void setStarterName(String value)
	{
		this.SetValByKey(TaskAttr.StarterName, value);
	}
	public final int getPrjSta()
	{
		return this.GetValIntByKey(TaskAttr.PrjSta);
	}
	public final void setPrjSta(int value)
	{
		this.SetValByKey(TaskAttr.PrjSta, value);
	}
	public final int getPRI()
	{
		return this.GetValIntByKey(TaskAttr.PRI);
	}
	public final void setPRI(int value)
	{
		this.SetValByKey(TaskAttr.PRI, value);
	}
	public final int getWCL()
	{
		return this.GetValIntByKey(TaskAttr.WCL);
	}
	public final void setWCL(int value)
	{
		this.SetValByKey(TaskAttr.WCL, value);
	}
//C# TO JAVA CONVERTER TASK: There is no preprocessor in Java:
		///#endregion 属性

//C# TO JAVA CONVERTER TASK: There is no preprocessor in Java:
		///#region 构造函数
	/** 
	 任务
	*/
	public Task()
	{
	}
	/** 
	 任务
	 
	 @param taskID
	*/
	public Task(long taskID) throws Exception {
		this.SetValByKey("OID", taskID);
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

		Map map = new Map("TA_Task", "任务");

			///#region 任务信息
		map.AddTBIntPK(TaskAttr.OID, 0, "工作ID", true, true, true);
		map.AddTBString(TaskAttr.Title, null, "标题", true, false, 0, 50, 300);
		map.AddTBString(TaskAttr.EmpNo, null, "负责人", true, false, 0, 50, 300);
		map.AddTBString(TaskAttr.EmpName, null, "名称", true, false, 0, 50, 300);
		map.AddTBInt(TaskAttr.TaskSta, 0, "Task状态", true, true);
		map.AddTBInt(TaskAttr.IsRead, 0, "是否读取?", true, true);
		map.AddTBDateTime(TaskAttr.RDT, null, "下达日期", true, false);
		map.AddTBDateTime(TaskAttr.ADT, null, "活动日期", true, false);
		map.AddTBString(TaskAttr.NowMsg, null, "即时消息", true, false, 0, 600, 300);

		map.AddTBInt(TaskAttr.PTaskID, 0, "父节点ID", true, true);

		map.AddTBString(TaskAttr.SenderNo, null, "分配人", false, false, 0, 50, 300);
		map.AddTBString(TaskAttr.SenderName, null, "分配人", true, true, 0, 50, 300);
			///#endregion 任务信息

			///#region 项目信息
		map.AddTBString(TaskAttr.PrjNo, null, "项目编号", true, false, 0, 50, 300);
		map.AddTBString(TaskAttr.PrjName, null, "名称", true, false, 0, 50, 300);
		map.AddTBString(TaskAttr.StarterNo, null, "发起人编号", false, false, 0, 50, 300);
		map.AddTBString(TaskAttr.StarterName, null, "发起人名称", true, true, 0, 50, 300);
		map.AddTBInt(TaskAttr.PrjSta, 0, "状态", true, true);
		map.AddTBInt(TaskAttr.PRI, 0, "优先级", true, true);
		map.AddTBInt(TaskAttr.WCL, 0, "完成率", true, true);
			///#endregion 任务信息

			///#region 节点信息
		map.AddTBString(TaskAttr.NodeNo, null, "节点编号", true, false, 0, 50, 300);
		map.AddTBString(TaskAttr.NodeName, null, "节点名称", true, false, 0, 50, 300);
			///#endregion 节点信息

		this.set_enMap(map);
		return this.get_enMap();
	}
		///#endregion
}
