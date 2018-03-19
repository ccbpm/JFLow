package BP.WF;

import BP.En.Entities;
import BP.En.Entity;

/**
 * 任务
 */
public class Tasks extends Entities
{
	// 方法
	/**
	 * 得到它的 Entity
	 */
	@Override
	public Entity getGetNewEntity()
	{
		return new Task();
	}
	
	/**
	 * 任务
	 */
	public Tasks()
	{
	}
}