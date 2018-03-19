package BP.WF;

import BP.En.EnType;
import BP.En.EntityMyPK;
import BP.En.Map;
import BP.WF.Template.TaskAttr;

/**
 * 任务
 */
public class Task extends EntityMyPK
{
	// 属性
	/**
	 * 参数
	 */
	public final String getParas()
	{
		return this.GetValStringByKey(TaskAttr.Paras);
	}
	
	public final void setParas(String value)
	{
		this.SetValByKey(TaskAttr.Paras, value);
	}
	
	/**
	 * 发起人
	 */
	public final String getStarter()
	{
		return this.GetValStringByKey(TaskAttr.Starter);
	}
	
	public final void setStarter(String value)
	{
		this.SetValByKey(TaskAttr.Starter, value);
	}
	
	/**
	 * 流程编号
	 */
	public final String getFK_Flow()
	{
		return this.GetValStringByKey(TaskAttr.FK_Flow);
	}
	
	public final void setFK_Flow(String value)
	{
		this.SetValByKey(TaskAttr.FK_Flow, value);
	}
	
	/**
	 * 发起时间（可以为空）
	 */
	public final String getStartDT()
	{
		return this.GetValStringByKey(TaskAttr.StartDT);
	}
	
	public final void setStartDT(String value)
	{
		this.SetValByKey(TaskAttr.StartDT, value);
	}
	
	// 构造函数
	/**
	 * Task
	 */
	public Task()
	{
	}
	
	/**
	 * 重写基类方法
	 */
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}
		Map map = new Map("WF_Task");
		map.setEnDesc("任务");
		map.setEnType(EnType.Admin);
		
		map.AddMyPK(); // 唯一的主键.
		map.AddTBString(TaskAttr.FK_Flow, null, "流程编号", true, false, 0, 200, 10);
		map.AddTBString(TaskAttr.Starter, null, "发起人", true, false, 0, 200, 10);
		map.AddTBString(TaskAttr.Paras, null, "参数", true, false, 0, 4000, 10);
		
		// TaskSta 0=未发起，1=成功发起，2=发起失败.
		map.AddTBInt(TaskAttr.TaskSta, 0, "任务状态", true, false);
		
		map.AddTBString(TaskAttr.Msg, null, "消息", true, false, 0, 4000, 10);
		map.AddTBString(TaskAttr.StartDT, null, "发起时间", true, false, 0, 20, 10);
		map.AddTBString(TaskAttr.RDT, null, "插入数据时间", true, false, 0, 20, 10);
		
		this.set_enMap(map);
		return this.get_enMap();
	}
}