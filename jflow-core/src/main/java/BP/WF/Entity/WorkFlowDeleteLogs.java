package BP.WF.Entity;

import BP.En.Entities;
import BP.En.Entity;

/**
 * 流程删除日志s
 */
public class WorkFlowDeleteLogs extends Entities
{
	// 构造
	/**
	 * 流程删除日志s
	 */
	public WorkFlowDeleteLogs()
	{
	}
	
	/**
	 * 得到它的 Entity
	 */
	@Override
	public Entity getGetNewEntity()
	{
		return new WorkFlowDeleteLog();
	}
}